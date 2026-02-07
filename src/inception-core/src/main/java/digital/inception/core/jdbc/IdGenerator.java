/*
 * Copyright Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.core.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

/**
 * Generates monotonically increasing numeric identifiers backed by a single-row-per-key table
 * (typically named {@code idgenerator}).
 *
 * <h2>How it works</h2>
 *
 * <ul>
 *   <li>Runs inside a {@code PROPAGATION_REQUIRES_NEW} Spring transaction, suspending any existing
 *       Spring-managed transaction.
 *   <li>Locks the {@code idgenerator} row for the given {@code name} (or the appropriate key-range
 *       in SQL Server) to ensure uniqueness across threads and processes.
 *   <li>If the row exists, increments {@code current_id} by 1 and returns the new value.
 *   <li>If the row does not exist, attempts to insert {@code current_id = 1}. If another concurrent
 *       transaction inserts first, it re-reads under lock and performs the increment path.
 * </ul>
 *
 * <h2>Agroal / pooling compatibility</h2>
 *
 * This class is compatible with Agroal (or any JDBC pool) because it never holds onto JDBC {@link
 * Connection} instances directly. All access goes through {@link JdbcTemplate}, which obtains and
 * releases connections via Spring's {@code DataSourceUtils}. This ensures pooled connections are
 * correctly returned to Agroal and that the transaction-bound connection is used within the {@code
 * REQUIRES_NEW} transaction.
 *
 * <h2>Requirements</h2>
 *
 * <ul>
 *   <li>The injected {@link PlatformTransactionManager} must manage transactions for the same
 *       {@link DataSource} used by the {@link JdbcTemplate} (i.e., they must be paired).
 *   <li>The table must have a uniqueness constraint on {@code name} (primary key or unique index),
 *       otherwise concurrent inserts can create duplicates.
 *   <li>The SQL uses vendor-specific locking:
 *       <ul>
 *         <li>H2/Oracle/Postgres: {@code SELECT ... FOR UPDATE}
 *         <li>DB2: {@code SELECT ... FOR UPDATE WITH RS}
 *         <li>SQL Server: {@code WITH (UPDLOCK, HOLDLOCK)} hints
 *       </ul>
 * </ul>
 *
 * <h2>Notes</h2>
 *
 * <ul>
 *   <li>The returned IDs are unique and increasing per {@code name}, but are not guaranteed to be
 *       gap-free (e.g., if a transaction rolls back after increment).
 *   <li>For SQL Server with case-insensitive collations, consider normalising {@code name} (e.g.,
 *       upper-casing) if you require case-sensitive key semantics.
 * </ul>
 *
 * @author Marcus Portmann
 */
public class IdGenerator {

  private static final String SQL_INSERT =
      "INSERT INTO idgenerator (name, current_id) VALUES (?, ?)";

  private static final String SQL_UPDATE = "UPDATE idgenerator SET current_id = ? WHERE name = ?";

  private final JdbcTemplate jdbcTemplate;
  private final TransactionTemplate requiresNewTransaction;

  // DB rarely changes at runtime; caching avoids repeated metadata calls.
  private volatile DbDialect cachedDialect;

  /**
   * Creates an {@code IdGenerator} that uses Spring-managed transactions and JDBC access via {@link
   * JdbcTemplate}.
   *
   * <p>The {@code REQUIRES_NEW} transaction created by this component will suspend any existing
   * Spring-managed transaction on the calling thread.
   *
   * @param platformTransactionManager the Spring transaction manager associated with {@code
   *     dataSource}
   * @param dataSource the (possibly Agroal-wrapped) data source used to access the {@code
   *     idgenerator} table
   * @throws IllegalArgumentException if {@code platformTransactionManager} or {@code dataSource} is
   *     {@code null}
   */
  public IdGenerator(PlatformTransactionManager platformTransactionManager, DataSource dataSource) {
    if (platformTransactionManager == null)
      throw new IllegalArgumentException("platformTransactionManager is null");
    if (dataSource == null) throw new IllegalArgumentException("dataSource is null");

    this.jdbcTemplate = new JdbcTemplate(dataSource);

    this.requiresNewTransaction = new TransactionTemplate(platformTransactionManager);
    this.requiresNewTransaction.setPropagationBehavior(
        TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    this.requiresNewTransaction.setReadOnly(false);
  }

  /**
   * Generates the next ID for the specified logical key {@code name}.
   *
   * <p>This method executes in a {@code PROPAGATION_REQUIRES_NEW} transaction. If the caller is
   * already inside a Spring-managed transaction, that outer transaction is suspended for the
   * duration of this call.
   *
   * <p>Concurrency control is achieved by acquiring an update lock on the relevant row/key before
   * updating it. This guarantees that no two concurrent transactions can return the same value for
   * the same {@code name}.
   *
   * @param name the logical key used to select a row in {@code idgenerator}
   * @return the next ID value (either {@code current_id + 1} or {@code 1} if the row is newly
   *     created)
   * @throws SQLException if {@code name} is blank, or if the operation fails due to database access
   *     errors
   */
  public long nextId(String name) throws SQLException {
    if (!StringUtils.hasText(name)) {
      throw new SQLException("Failed to generate the ID: The entity type name is null or empty");
    }

    try {
      Long result =
          requiresNewTransaction.execute(
              status -> {
                DbDialect dialect = getDialect();

                // 1) Lock row if it exists and read current_id
                Long currentId = selectForUpdate(dialect, name);

                if (currentId != null) {
                  long next = currentId + 1L;

                  int updated = jdbcTemplate.update(SQL_UPDATE, next, name);
                  if (updated != 1) {
                    throw new IllegalStateException(
                        "Failed to update the IDGENERATOR table for the entity type with name ("
                            + name
                            + "): updated="
                            + updated);
                  }

                  return next;
                }

                // 2) Row does not exist -> try insert (start at 1)
                try {
                  jdbcTemplate.update(SQL_INSERT, name, 1L);
                  return 1L;

                } catch (DataIntegrityViolationException dupInsert) {
                  // Another thread/process inserted concurrently.
                  // Re-lock row and do the update path.
                  Long now = selectForUpdate(dialect, name);
                  if (now == null) {
                    throw new IllegalStateException(
                        "Insert into IDGENERATOR table raced but row still not visible for the entity type with name ("
                            + name
                            + ")",
                        dupInsert);
                  }

                  long next = now + 1L;
                  int updated = jdbcTemplate.update(SQL_UPDATE, next, name);

                  if (updated != 1) {
                    throw new IllegalStateException(
                        "Failed to update the IDGENERATOR table after duplicate insert for the entity type with name ("
                            + name
                            + "): updated="
                            + updated,
                        dupInsert);
                  }

                  return next;
                }
              });

      if (result == null) {
        throw new SQLException(
            "Failed to generate the ID for the entity type with name ("
                + name
                + "): Transaction returned null");
      }

      return result;

    } catch (Throwable e) {
      throw new SQLException(
          "Failed to generate the ID for the entity type with name (" + name + ")", e);
    }
  }

  private static DbDialect detectDialect(Connection con) throws SQLException {
    DatabaseMetaData md = con.getMetaData();
    String product = md.getDatabaseProductName();
    if (product == null) return DbDialect.UNKNOWN;

    String p = product.toLowerCase();
    if (p.contains("h2")) return DbDialect.H2;
    if (p.contains("oracle")) return DbDialect.ORACLE;
    if (p.contains("postgres")) return DbDialect.POSTGRES;
    if (p.contains("db2")) return DbDialect.DB2;
    if (p.contains("microsoft sql server") || p.contains("sql server")) return DbDialect.SQLSERVER;

    return DbDialect.UNKNOWN;
  }

  private DbDialect getDialect() {
    DbDialect d = cachedDialect;
    if (d != null) return d;

    // Use JdbcTemplate so we definitely use the transaction-bound connection.
    DbDialect detected = jdbcTemplate.execute(IdGenerator::detectDialect);

    cachedDialect = detected;
    return detected;
  }

  private Long selectForUpdate(DbDialect dialect, String name) {
    String sql = dialect.selectForUpdateSql();

    return jdbcTemplate.query(
        sql,
        ps -> ps.setString(1, name),
        rs -> {
          if (!rs.next()) return null;
          long v = rs.getLong(1);
          return rs.wasNull() ? null : v;
        });
  }

  private enum DbDialect {
    H2,
    ORACLE,
    POSTGRES,
    DB2,
    SQLSERVER,
    UNKNOWN;

    String selectForUpdateSql() {
      return switch (this) {
        case SQLSERVER ->
            // SQL Server has no FOR UPDATE; hints provide correct lock semantics.
            "SELECT current_id FROM idgenerator WITH (UPDLOCK, HOLDLOCK) WHERE name = ?";
        case DB2 ->
            // DB2 common idiom to keep lock until commit.
            "SELECT current_id FROM idgenerator WHERE name = ? FOR UPDATE WITH RS";
        case H2, ORACLE, POSTGRES, UNKNOWN ->
            "SELECT current_id FROM idgenerator WHERE name = ? FOR UPDATE";
      };
    }
  }
}

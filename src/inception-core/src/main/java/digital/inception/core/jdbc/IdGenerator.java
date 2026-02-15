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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Generates monotonically increasing numeric identifiers using a single-row-per-key table named
 * {@code idgenerator}.
 *
 * <p>The backing table contains one row per logical key (the {@code name} argument), with the
 * current value stored in {@code current_id}.
 *
 * <p>This generator uses pessimistic locking to serialize increments per {@code name}. It performs:
 *
 * <ol>
 *   <li>{@code SELECT current_id ... FOR UPDATE} (or vendor equivalent) to lock and read the row
 *   <li>{@code UPDATE idgenerator SET current_id = ? WHERE name = ?}
 * </ol>
 *
 * <p>If the row does not exist, it attempts an {@code INSERT} of {@code current_id=1}. If a
 * concurrent transaction inserts first, the resulting duplicate-key exception is detected and the
 * method retries by re-locking the row and performing the update path.
 *
 * <p>This implementation is deliberately agnostic of whether the configured {@link
 * PlatformTransactionManager} is:
 *
 * <ul>
 *   <li>JTA-based (e.g., Narayana via {@code JtaTransactionManager}), or
 *   <li>local JDBC-based (e.g., {@code DataSourceTransactionManager}).
 * </ul>
 *
 * <p>To avoid long-running application transactions holding locks on {@code idgenerator} rows, ID
 * allocation always runs in a <strong>new</strong> transaction using {@link
 * TransactionDefinition#PROPAGATION_REQUIRES_NEW}. If an existing transaction is active, it is
 * suspended (where supported by the transaction manager) for the duration of ID allocation.
 *
 * <p>This class does <strong>not</strong> call {@link Connection#commit()}, {@link
 * Connection#rollback()} or modify {@link Connection#setAutoCommit(boolean)}; transaction
 * completion is fully controlled by the {@link PlatformTransactionManager}.
 *
 * <p>The supplied {@link DataSource} may be XA or non-XA (e.g., Agroal). The connection is acquired
 * <em>inside</em> the {@code REQUIRES_NEW} boundary so it can be correctly associated with the new
 * transaction regardless of the underlying implementation.
 *
 * @author Marcus Portmann
 */
public class IdGenerator {

  private static final String SQL_INSERT =
      "INSERT INTO idgenerator (name, current_id) VALUES (?, ?)";

  private static final String SQL_UPDATE = "UPDATE idgenerator SET current_id = ? WHERE name = ?";

  private final DataSource dataSource;
  private final PlatformTransactionManager transactionManager;

  /** Cached database dialect for the configured {@link DataSource}. */
  private volatile DatabaseDialect cachedDatabaseDialect;

  /**
   * Creates a new {@code IdGenerator} that allocates IDs using the provided {@link DataSource} and
   * executes allocation work inside a new transaction managed by the supplied {@link
   * PlatformTransactionManager}.
   *
   * <p>Because ID allocation uses {@code PROPAGATION_REQUIRES_NEW}, callers can invoke {@link
   * #nextId(String)} from within long-running transactions without holding locks on the {@code
   * idgenerator} table for the duration of the outer transaction.
   *
   * @param transactionManager the Spring transaction manager used to begin/commit/rollback the
   *     {@code REQUIRES_NEW} transaction; must not be {@code null}
   * @param dataSource the data source used to acquire JDBC connections; must not be {@code null}
   * @throws IllegalArgumentException if either argument is {@code null}
   */
  public IdGenerator(PlatformTransactionManager transactionManager, DataSource dataSource) {
    if (dataSource == null) throw new IllegalArgumentException("dataSource is null");
    if (transactionManager == null)
      throw new IllegalArgumentException("transactionManager is null");
    this.dataSource = dataSource;
    this.transactionManager = transactionManager;
  }

  /**
   * Returns the next monotonically increasing identifier for the given logical key.
   *
   * <p>The returned value is strictly increasing per {@code name} (assuming the backing table is
   * not modified externally). Calls for different {@code name} values do not block each other
   * unless the database escalates locks.
   *
   * <p>This method always runs ID allocation in a <strong>new</strong> transaction by using {@link
   * TransactionDefinition#PROPAGATION_REQUIRES_NEW}. If an existing transaction is active, it is
   * suspended (where supported by the configured {@link PlatformTransactionManager}) so that
   * long-running application work does not hold locks on {@code idgenerator} rows.
   *
   * <p>The JDBC {@link Connection} is acquired <em>inside</em> the {@code REQUIRES_NEW} boundary so
   * that it can be correctly associated with the new transaction whether the underlying transaction
   * manager is JTA-based or local JDBC-based.
   *
   * <p>This method does not invoke {@link Connection#commit()}, {@link Connection#rollback()}, or
   * {@link Connection#setAutoCommit(boolean)}; commit/rollback is delegated to Spring via the
   * {@link PlatformTransactionManager}. Any {@link SQLException} or runtime failure results in the
   * {@code REQUIRES_NEW} transaction being rolled back and the exception propagated.
   *
   * @param name the logical key (entity type) for which the id is being generated; must not be
   *     {@code null} or blank
   * @return the next id value (starting at {@code 1})
   * @throws SQLException if the ID cannot be generated due to invalid input, database errors, or
   *     unexpected update counts
   */
  public long nextId(String name) throws SQLException {
    if (name == null || name.isBlank()) {
      throw new SQLException("Failed to generate the ID: The entity type name is null or empty");
    }

    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    def.setName("IdGenerator.nextId");
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    // Optional knobs if you want:
    // def.setTimeout(5);
    // def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

    TransactionStatus status = transactionManager.getTransaction(def);

    try {
      long id;
      try (Connection connection = dataSource.getConnection()) {
        // Dialect detection done after we have a transactional connection.
        DatabaseDialect dialect = getDialect(connection);
        id = nextIdWithinTransaction(connection, dialect, name);
      }

      transactionManager.commit(status);
      return id;

    } catch (Exception e) {
      try {
        transactionManager.rollback(status);
      } catch (RuntimeException rb) {
        e.addSuppressed(rb);
      }

      if (e instanceof SQLException se) throw withContext(se, name);
      throw withContext(new SQLException("Unexpected error", e), name);
    }
  }

  private static SQLException withContext(SQLException e, String name) {
    final String msg = "Failed to generate the ID for the entity type with name (" + name + ")";
    String m = e.getMessage();
    if (m != null && m.contains("Failed to generate the ID for the entity type with name (")) {
      return e;
    }
    return new SQLException(msg, e);
  }

  private static int updateCurrentId(Connection connection, long next, String name)
      throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE)) {
      ps.setLong(1, next);
      ps.setString(2, name);
      return ps.executeUpdate();
    }
  }

  private static void insertRow(Connection connection, String name, long id) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT)) {
      ps.setString(1, name);
      ps.setLong(2, id);
      ps.executeUpdate();
    }
  }

  private static DatabaseDialect detectDialect(Connection connection) throws SQLException {
    DatabaseMetaData databaseMetaData = connection.getMetaData();
    String databaseProductName = databaseMetaData.getDatabaseProductName();
    if (databaseProductName == null) return DatabaseDialect.UNKNOWN;

    String p = databaseProductName.toLowerCase();
    if (p.contains("h2")) return DatabaseDialect.H2;
    if (p.contains("oracle")) return DatabaseDialect.ORACLE;
    if (p.contains("postgres")) return DatabaseDialect.POSTGRES;
    if (p.contains("db2")) return DatabaseDialect.DB2;
    if (p.contains("microsoft sql server") || p.contains("sql server"))
      return DatabaseDialect.SQLSERVER;

    return DatabaseDialect.UNKNOWN;
  }

  private static boolean isDuplicateKey(SQLException e, DatabaseDialect databaseDialect) {
    for (Throwable t = e;
        t != null;
        t = (t instanceof SQLException se) ? se.getNextException() : t.getCause()) {

      if (!(t instanceof SQLException se)) continue;

      String sqlState = se.getSQLState();

      if ("23505".equals(sqlState)) return true;
      if (sqlState != null && sqlState.startsWith("23")) return true;

      int code = se.getErrorCode();
      switch (databaseDialect) {
        case ORACLE -> {
          if (code == 1) return true;
        }
        case DB2 -> {
          if (code == -803 || code == 803) return true;
        }
        case SQLSERVER -> {
          if (code == 2627 || code == 2601) return true;
        }
        case POSTGRES, H2, UNKNOWN -> {
          // handled by SQLState checks above
        }
      }
    }
    return false;
  }

  private DatabaseDialect getDialect(Connection connection) throws SQLException {
    DatabaseDialect d = cachedDatabaseDialect;
    if (d != null) return d;

    DatabaseDialect detected = detectDialect(connection);
    cachedDatabaseDialect = detected;
    return detected;
  }

  private long nextIdWithinTransaction(Connection connection, DatabaseDialect dialect, String name)
      throws SQLException {

    Long currentId = selectForUpdate(connection, dialect, name);

    if (currentId != null) {
      long next = currentId + 1L;

      int updated = updateCurrentId(connection, next, name);
      if (updated != 1) {
        throw new SQLException(
            "Failed to update the IDGENERATOR table for the entity type with name ("
                + name
                + "): updated="
                + updated);
      }
      return next;
    }

    try {
      insertRow(connection, name, 1L);
      return 1L;

    } catch (SQLException insertEx) {
      if (!isDuplicateKey(insertEx, dialect)) {
        throw insertEx;
      }

      Long now = selectForUpdate(connection, dialect, name);
      if (now == null) {
        throw new SQLException(
            "Insert into IDGENERATOR table raced but row still not visible for the entity type with name ("
                + name
                + ")",
            insertEx);
      }

      long next = now + 1L;
      int updated = updateCurrentId(connection, next, name);
      if (updated != 1) {
        throw new SQLException(
            "Failed to update the IDGENERATOR table after duplicate insert for the entity type with name ("
                + name
                + "): updated="
                + updated,
            insertEx);
      }

      return next;
    }
  }

  @SuppressWarnings("SqlSourceToSinkFlow")
  private Long selectForUpdate(Connection connection, DatabaseDialect databaseDialect, String name)
      throws SQLException {
    String sql = databaseDialect.selectForUpdateSql();
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, name);
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) return null;
        long v = rs.getLong(1);
        return rs.wasNull() ? null : v;
      }
    }
  }

  private enum DatabaseDialect {
    H2,
    ORACLE,
    POSTGRES,
    DB2,
    SQLSERVER,
    UNKNOWN;

    String selectForUpdateSql() {
      return switch (this) {
        case SQLSERVER ->
            "SELECT current_id FROM idgenerator WITH (UPDLOCK, HOLDLOCK, ROWLOCK) WHERE name = ?";
        case DB2 -> "SELECT current_id FROM idgenerator WHERE name = ? FOR UPDATE WITH RS";
        case H2, ORACLE, POSTGRES, UNKNOWN ->
            "SELECT current_id FROM idgenerator WHERE name = ? FOR UPDATE";
      };
    }
  }
}

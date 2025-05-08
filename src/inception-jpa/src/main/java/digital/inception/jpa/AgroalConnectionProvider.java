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

package digital.inception.jpa;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.AgroalConnectionFactoryConfiguration.IsolationLevel;
import io.agroal.api.configuration.AgroalConnectionFactoryConfiguration.TransactionIsolation;
import io.agroal.api.configuration.AgroalDataSourceConfiguration;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.HibernateException;
import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.DatabaseConnectionInfo;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * The {@code AgroalConnectionProvider} class provides connections to Hibernate using an Agroal data
 * source.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class AgroalConnectionProvider implements ConnectionProvider, Stoppable, Configurable {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(AgroalConnectionProvider.class);

  /** The Agroal data source. */
  private AgroalDataSource agroalDataSource;

  /** The Agroal database connection info. */
  private AgroalDatabaseConnectionInfo agroalDatabaseConnectionInfo;

  /**
   * Creates a new {@code AgroalConnectionProvider} instance.
   *
   * @param agroalDataSource the Agroal data source
   */
  public AgroalConnectionProvider(AgroalDataSource agroalDataSource) {
    this.agroalDataSource = agroalDataSource;
  }

  @Override
  public void closeConnection(Connection connection) throws SQLException {
    if (connection != null) {
      connection.close();
    }
  }

  @Override
  public void configure(@NonNull Map<String, Object> configValues) {}

  @Override
  public Connection getConnection() throws SQLException {
    if (agroalDataSource == null) {
      throw new HibernateException(
          "The data source associated with the AgroalConnectionProvider is null");
    }

    return agroalDataSource.getConnection();
  }

  @Override
  public DatabaseConnectionInfo getDatabaseConnectionInfo(Dialect dialect) {
    if (agroalDatabaseConnectionInfo == null) {
      agroalDatabaseConnectionInfo = new AgroalDatabaseConnectionInfo(dialect, agroalDataSource);
    }

    return agroalDatabaseConnectionInfo;
  }

  @Override
  public boolean isUnwrappableAs(@NonNull Class<?> unwrapType) {
    return ConnectionProvider.class.equals(unwrapType)
        || AgroalConnectionProvider.class.isAssignableFrom(unwrapType)
        || DataSource.class.isAssignableFrom(unwrapType);
  }

  @Override
  public void stop() {
    this.agroalDataSource = null;
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return true;
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public <T> T unwrap(@NonNull Class<T> unwrapType) {
    if (ConnectionProvider.class.equals(unwrapType)
        || AgroalConnectionProvider.class.isAssignableFrom(unwrapType)) {
      return (T) this;
    } else if (DataSource.class.isAssignableFrom(unwrapType)) {
      return (T) agroalDataSource;
    } else {
      throw new UnknownUnwrapTypeException(unwrapType);
    }
  }

  /**
   * The {@code AgroalDatabaseConnectionInfo} class.
   *
   * @author Marcus Portmann
   */
  /** AgroalDatabaseConnectionInfo encapsulates database connection information for Hibernate. */
  public static class AgroalDatabaseConnectionInfo implements DatabaseConnectionInfo {

    private final String autoCommitMode;

    private final DatabaseVersion databaseVersion;

    private final String isolationLevel;

    private final String jdbcDriver;

    private final String jdbcUrl;

    private final int maxPoolSize;

    private final int minPoolSize;

    /**
     * Constructs a new AgroalDatabaseConnectionInfo.
     *
     * @param dialect the Hibernate dialect
     * @param agroalDataSource the Agroal data source
     */
    public AgroalDatabaseConnectionInfo(Dialect dialect, AgroalDataSource agroalDataSource) {
      this.databaseVersion = dialect.getVersion();

      AgroalDataSourceConfiguration config = agroalDataSource.getConfiguration();
      this.minPoolSize = config.connectionPoolConfiguration().minSize();
      this.maxPoolSize = config.connectionPoolConfiguration().maxSize();

      String driver = "unknown";
      String url = "unknown";
      String autoCommit = "unknown";

      try (Connection connection = agroalDataSource.getConnection()) {
        DatabaseMetaData metaData = connection.getMetaData();
        driver = metaData.getDriverName() + " " + metaData.getDriverVersion();
        url = metaData.getURL();
        autoCommit = String.valueOf(connection.getAutoCommit());
      } catch (SQLException e) {
        log.error("Failed to retrieve database metadata.", e);
      }

      this.jdbcDriver = driver;
      this.jdbcUrl = url;
      this.autoCommitMode = autoCommit;

      IsolationLevel isolation =
          config
              .connectionPoolConfiguration()
              .connectionFactoryConfiguration()
              .jdbcTransactionIsolation();
      this.isolationLevel =
          (isolation != null)
              ? TransactionIsolation.fromLevel(isolation.level()).name()
              : "unknown";
    }

    @Override
    public String getAutoCommitMode() {
      return autoCommitMode;
    }

    @Override
    public DatabaseVersion getDialectVersion() {
      return databaseVersion;
    }

    @Override
    public String getIsolationLevel() {
      return isolationLevel;
    }

    @Override
    public String getJdbcDriver() {
      return jdbcDriver;
    }

    @Override
    public String getJdbcUrl() {
      return jdbcUrl;
    }

    @Override
    public Integer getPoolMaxSize() {
      return maxPoolSize;
    }

    @Override
    public Integer getPoolMinSize() {
      return minPoolSize;
    }

    @Override
    public String toInfoString() {
      return String.format(
          """
          \tDatabase JDBC URL: [%s]
          \tDatabase driver: %s
          \tDatabase version: %s
          \tAutocommit mode: %s
          \tIsolation level: %s
          \tMinimum pool size: %d
          \tMaximum pool size: %d
          """,
          jdbcUrl,
          jdbcDriver,
          databaseVersion,
          autoCommitMode,
          isolationLevel,
          minPoolSize,
          maxPoolSize);
    }
  }
}

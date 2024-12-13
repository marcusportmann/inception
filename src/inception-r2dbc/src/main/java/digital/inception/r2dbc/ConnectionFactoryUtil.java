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

package digital.inception.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * The <b>ConnectionFactoryUtil</b> class provides R2DBC connection factory utility functions.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class ConnectionFactoryUtil {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(ConnectionFactoryUtil.class);

  /** Private default constructor to prevent instantiation. */
  private ConnectionFactoryUtil() {}

  /**
   * Returns the connection validation query for the database with the specified name.
   *
   * @param databaseName the name of the database
   * @return the connection validation query for the database with the specified name
   */
  public static String getValidationQuery(String databaseName) {
    return switch (databaseName.toLowerCase()) {
      case "oracle" -> "SELECT 1 FROM DUAL";
      case "postgresql", "h2", "mssql", "sqlserver" -> "SELECT 1";
      default ->
          throw new UnsupportedOperationException(
              "Failed to determine the validation query for the unsupported database type: "
                  + databaseName);
    };
  }

  /**
   * Initialise the R2DBC connection factory.
   *
   * @param applicationContext the Spring application context
   * @param connectionFactoryConfiguration the R2DBC connection factory configuration
   * @return the R2DBC connection factory
   */
  public static ConnectionFactory initConnectionFactory(
      ApplicationContext applicationContext,
      ConnectionFactoryConfiguration connectionFactoryConfiguration) {

    log.info(
        "Initializing the R2DBC connection factory with URL ("
            + connectionFactoryConfiguration.getUrl()
            + "), username ("
            + connectionFactoryConfiguration.getUsername()
            + "), initial pool size ("
            + connectionFactoryConfiguration.getInitialPoolSize()
            + "), max pool size ("
            + connectionFactoryConfiguration.getMaxPoolSize()
            + connectionFactoryConfiguration.getMaxPoolSize()
            + "), and idle timeout ("
            + connectionFactoryConfiguration.getMaxIdleTime().toSeconds()
            + " seconds)");

    ConnectionFactoryOptions options =
        ConnectionFactoryOptions.parse(connectionFactoryConfiguration.getUrl())
            .mutate()
            .option(
                ConnectionFactoryOptions.USER,
                StringUtils.hasText(connectionFactoryConfiguration.getUsername())
                    ? connectionFactoryConfiguration.getUsername()
                    : "")
            .option(
                ConnectionFactoryOptions.PASSWORD,
                StringUtils.hasText(connectionFactoryConfiguration.getPassword())
                    ? connectionFactoryConfiguration.getPassword()
                    : "")
            .build();

    ConnectionFactory connectionFactory = ConnectionFactories.get(options);
    ConnectionFactoryMetadata metadata = connectionFactory.getMetadata();

    String validationQuery = getValidationQuery(metadata.getName());

    ConnectionPoolConfiguration configuration =
        ConnectionPoolConfiguration.builder()
            .connectionFactory(connectionFactory)
            .initialSize(connectionFactoryConfiguration.getInitialPoolSize())
            .maxSize(connectionFactoryConfiguration.getMaxPoolSize())
            .maxIdleTime(connectionFactoryConfiguration.getMaxIdleTime())
            .validationQuery(validationQuery)
            .build();

    return new ConnectionPool(configuration);
  }
}

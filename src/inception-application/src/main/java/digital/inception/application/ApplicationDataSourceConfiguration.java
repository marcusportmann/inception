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

package digital.inception.application;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.AgroalConnectionPoolConfiguration;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;
import io.agroal.api.transaction.TransactionIntegration;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Properties;
import javax.sql.DataSource;
import liquibase.command.CommandScope;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * The <b>ApplicationDataSourceConfiguration</b> class provides access to the application data
 * source configuration and initialises the application data source.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableAutoConfiguration(
    exclude = {DataSourceAutoConfiguration.class, LiquibaseAutoConfiguration.class})
@ConditionalOnProperty({
  "inception.application.data-source.class-name",
  "inception.application.data-source.url"
})
@SuppressWarnings("unused")
public class ApplicationDataSourceConfiguration {

  /* Logger */
  private static final Logger logger =
      LoggerFactory.getLogger(ApplicationDataSourceConfiguration.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /**
   * The fully qualified name of the data source class used to connect to the application database.
   */
  @Value("${inception.application.data-source.class-name:#{null}}")
  private String className;

  /** Execute the Liquibase changelogs using the data context. */
  @Value("${inception.application.data-source.liquibase.apply-data-context:#{true}}")
  private boolean liquibaseApplyDataContext;

  /**
   * The Liquibase changelog resources on the classpath used to initialize the application database.
   */
  @Value("classpath*:**/db/*.changelog.xml")
  private Resource[] liquibaseChangelogResources;

  /** Is Liquibase enabled? */
  @Value("${inception.application.data-source.liquibase.enabled:#{true}}")
  private boolean liquibaseEnabled;

  /**
   * The maximum size of the database connection pool used to connect to the application database.
   */
  @Value("${inception.application.data-source.max-pool-size:5}")
  private int maxPoolSize;

  /**
   * The minimum size of the database connection pool used to connect to the application database.
   */
  @Value("${inception.application.data-source.min-pool-size:1}")
  private int minPoolSize;

  /** The password for the application database. */
  @Value("${inception.application.data-source.password:#{null}}")
  private String password;

  /** The URL used to connect to the application database. */
  @Value("${inception.application.data-source.url:#{null}}")
  private String url;

  /** The username for the application database. */
  @Value("${inception.application.data-source.username:#{null}}")
  private String username;

  /** The timeout when validating a connection in the database connection pool. */
  @Value("${inception.application.data-source.validation-timeout:30}")
  private int validationTimeout = 30;

  /**
   * Constructs a new <b>ApplicationDataSourceConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   */
  public ApplicationDataSourceConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the data source that can be used to interact with the application database.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean
  @Primary
  public DataSource applicationDataSource() {
    try {
      // See: https://agroal.github.io/docs.html
      Properties agroalProperties = new Properties();
      agroalProperties.setProperty(AgroalPropertiesReader.JDBC_URL, url);

      if (StringUtils.hasText(username)) {
        agroalProperties.setProperty(AgroalPropertiesReader.PRINCIPAL, username);
      }

      if (StringUtils.hasText(password)) {
        agroalProperties.setProperty(AgroalPropertiesReader.CREDENTIAL, password);
      }

      agroalProperties.setProperty(AgroalPropertiesReader.PROVIDER_CLASS_NAME, className);

      agroalProperties.setProperty(
          AgroalPropertiesReader.MIN_SIZE, (minPoolSize > 0) ? Integer.toString(minPoolSize) : "1");
      agroalProperties.setProperty(
          AgroalPropertiesReader.MAX_SIZE, (maxPoolSize > 0) ? Integer.toString(maxPoolSize) : "5");

      AgroalPropertiesReader agroalReaderProperties2 =
          new AgroalPropertiesReader().readProperties(agroalProperties);
      AgroalDataSourceConfigurationSupplier agroalDataSourceConfigurationSupplier =
          agroalReaderProperties2.modify();

      try {
        TransactionIntegration transactionIntegration =
            applicationContext.getBean(TransactionIntegration.class);

        agroalDataSourceConfigurationSupplier.connectionPoolConfiguration(
            cp ->
                cp.connectionValidator(
                        AgroalConnectionPoolConfiguration.ConnectionValidator
                            .defaultValidatorWithTimeout(validationTimeout))
                    .transactionIntegration(transactionIntegration));
      } catch (NoSuchBeanDefinitionException ignored) {
      }

      DataSource dataSource = AgroalDataSource.from(agroalDataSourceConfigurationSupplier);

      boolean isInMemoryH2Database = false;

      try (Connection connection = dataSource.getConnection()) {
        DatabaseMetaData metaData = connection.getMetaData();

        logger.info(
            "Connected to the "
                + metaData.getDatabaseProductName()
                + " application database with version "
                + metaData.getDatabaseProductVersion());

        if (metaData.getDatabaseProductName().equals("H2")) {
          isInMemoryH2Database = true;
        } else {
          logger.info(
              "The default database tables will not be populated for the database type ("
                  + metaData.getDatabaseProductName()
                  + ")");
        }
      }

      // Initialize the in-memory database using Liquibase changeSets
      if (isInMemoryH2Database || liquibaseEnabled) {
        if (isInMemoryH2Database) {
          logger.info("Initializing the in-memory H2 database using Liquibase");
        }

        try (Connection connection = dataSource.getConnection()) {
          liquibase.database.Database database =
              DatabaseFactory.getInstance()
                  .findCorrectDatabaseImplementation(new JdbcConnection(connection));

          for (Resource changelogResource : liquibaseChangelogResources) {
            if (StringUtils.hasText(changelogResource.getFilename())) {
              if (!changelogResource.getFilename().toLowerCase().endsWith("-data.changelog.xml")) {
                String changelogFile = "db/" + changelogResource.getFilename();

                logger.info("Applying Liquibase changelog: " + changelogResource.getFilename());

                new CommandScope("update")
                    .addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database)
                    .addArgumentValue("changeLogFile", changelogFile)
                    .execute();
              }
            }
          }

          for (Resource changelogResource : liquibaseChangelogResources) {
            if (StringUtils.hasText(changelogResource.getFilename())) {
              if (changelogResource.getFilename().toLowerCase().endsWith("-data.changelog.xml")) {
                String changelogFile = "db/" + changelogResource.getFilename();

                logger.info(
                    "Applying Liquibase data changelog: " + changelogResource.getFilename());

                new CommandScope("update")
                    .addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database)
                    .addArgumentValue("changeLogFile", changelogFile)
                    .execute();
              }
            }
          }
        }
      }

      return dataSource;
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialize the application data source", e);
    }
  }

  /**
   * Returns the fully qualified name of the data source class used to connect to the application
   * database.
   *
   * @return the fully qualified name of the data source class used to connect to the application
   *     database
   */
  public String getClassName() {
    return className;
  }

  /**
   * Returns the maximum size of the database connection pool used to connect to the application
   * database.
   *
   * @return the maximum size of the database connection pool used to connect to the application
   *     database
   */
  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  /**
   * Returns the minimum size of the database connection pool used to connect to the application
   * database.
   *
   * @return the minimum size of the database connection pool used to connect to the application
   *     database
   */
  public int getMinPoolSize() {
    return minPoolSize;
  }

  /**
   * Returns the password for the application database.
   *
   * @return the password for the application database
   */
  public String getPassword() {
    return password;
  }

  /**
   * Returns the URL used to connect to the application database.
   *
   * @return the URL used to connect to the application database
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns the username for the application database.
   *
   * @return the username for the application database
   */
  public String getUsername() {
    return username;
  }
}

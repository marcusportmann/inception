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

import io.agroal.api.AgroalDataSource;
import io.agroal.api.AgroalDataSourceMetrics;
import io.agroal.api.configuration.AgroalConnectionPoolConfiguration;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;
import io.agroal.api.transaction.TransactionIntegration;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.lang.reflect.Constructor;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * The {@code DataSourceUtil} class provides data source utility functions.
 *
 * @author Marcus Portmann
 */
public final class DataSourceUtil {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(DataSourceUtil.class);

  /** Private default constructor to prevent instantiation. */
  private DataSourceUtil() {}

  /**
   * Initialise the Agroal data source.
   *
   * @param applicationContext the Spring application context
   * @param dataSourceConfiguration the data source configuration
   * @return the Agroal data source
   */
  public static DataSource initAgroalDataSource(
      ApplicationContext applicationContext, DataSourceConfiguration dataSourceConfiguration) {
    return initAgroalDataSource(applicationContext, dataSourceConfiguration, true);
  }

  /**
   * Initialise the Agroal data source.
   *
   * @param applicationContext the Spring application context
   * @param dataSourceConfiguration the data source configuration
   * @param enableTransactionIntegration enable transaction integration
   * @return the Agroal data source
   */
  public static DataSource initAgroalDataSource(
      ApplicationContext applicationContext,
      DataSourceConfiguration dataSourceConfiguration,
      boolean enableTransactionIntegration) {

    log.info(
        "Initializing the data source with class ("
            + dataSourceConfiguration.getClassName()
            + ") and URL ("
            + dataSourceConfiguration.getUrl()
            + ") using the Agroal connection pool with max pool size "
            + dataSourceConfiguration.getMaxPoolSize()
            + " and a validation timeout of "
            + dataSourceConfiguration.getValidationTimeout()
            + " seconds");

    try {
      // See: https://agroal.github.io/docs.html
      Properties agroalProperties = new Properties();
      agroalProperties.setProperty(
          AgroalPropertiesReader.JDBC_URL, dataSourceConfiguration.getUrl());

      if (StringUtils.hasText(dataSourceConfiguration.getUsername())) {
        agroalProperties.setProperty(
            AgroalPropertiesReader.PRINCIPAL, dataSourceConfiguration.getUsername());
      }

      if (StringUtils.hasText(dataSourceConfiguration.getPassword())) {
        agroalProperties.setProperty(
            AgroalPropertiesReader.CREDENTIAL, dataSourceConfiguration.getPassword());
      }

      agroalProperties.setProperty(
          AgroalPropertiesReader.PROVIDER_CLASS_NAME, dataSourceConfiguration.getClassName());

      agroalProperties.setProperty(
          AgroalPropertiesReader.MIN_SIZE,
          (dataSourceConfiguration.getMinPoolSize() > 0)
              ? Integer.toString(dataSourceConfiguration.getMinPoolSize())
              : "1");
      agroalProperties.setProperty(
          AgroalPropertiesReader.MAX_SIZE,
          (dataSourceConfiguration.getMaxPoolSize() > 0)
              ? Integer.toString(dataSourceConfiguration.getMaxPoolSize())
              : "5");

      agroalProperties.setProperty(AgroalPropertiesReader.ENHANCED_LEAK_REPORT, "true");

      agroalProperties.setProperty(AgroalPropertiesReader.LEAK_TIMEOUT_S, "300");

      agroalProperties.setProperty(AgroalPropertiesReader.METRICS_ENABLED, "true");

      AgroalPropertiesReader agroalReaderProperties =
          new AgroalPropertiesReader().readProperties(agroalProperties);
      AgroalDataSourceConfigurationSupplier agroalDataSourceConfigurationSupplier =
          agroalReaderProperties.modify();

      if (enableTransactionIntegration) {
        try {
          TransactionIntegration transactionIntegration =
              createTransactionIntegration(applicationContext);

          agroalDataSourceConfigurationSupplier.connectionPoolConfiguration(
              cp ->
                  cp.connectionValidator(
                          AgroalConnectionPoolConfiguration.ConnectionValidator
                              .defaultValidatorWithTimeout(
                                  dataSourceConfiguration.getValidationTimeout()))
                      .validateOnBorrow(true)
                      .transactionIntegration(transactionIntegration));
        } catch (NoSuchBeanDefinitionException ignored) {
          agroalDataSourceConfigurationSupplier.connectionPoolConfiguration(
              cp ->
                  cp.connectionValidator(
                          AgroalConnectionPoolConfiguration.ConnectionValidator
                              .defaultValidatorWithTimeout(
                                  dataSourceConfiguration.getValidationTimeout()))
                      .validateOnBorrow(true));
        }
      } else {
        agroalDataSourceConfigurationSupplier.connectionPoolConfiguration(
            cp ->
                cp.connectionValidator(
                        AgroalConnectionPoolConfiguration.ConnectionValidator
                            .defaultValidatorWithTimeout(
                                dataSourceConfiguration.getValidationTimeout()))
                    .validateOnBorrow(true));
      }

      AgroalDataSource agroalDataSource =
          AgroalDataSource.from(
              agroalDataSourceConfigurationSupplier,
              new AgroalDataSourceListener(dataSourceConfiguration));

      bindAgroalDataSourceMetrics(applicationContext, dataSourceConfiguration, agroalDataSource);

      return agroalDataSource;
    } catch (Throwable e) {
      throw new FatalBeanException(
          "Failed to initialize the Agroal data source with url ("
              + dataSourceConfiguration.getUrl()
              + "), username ("
              + dataSourceConfiguration.getUsername()
              + ") and class name ("
              + dataSourceConfiguration.getClassName()
              + ")",
          e);
    }
  }

  private static void bindAgroalDataSourceMetrics(
      ApplicationContext applicationContext,
      DataSourceConfiguration dataSourceConfiguration,
      AgroalDataSource agroalDataSource) {
    String databaseName =
        DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

    try {
      Class.forName("io.micrometer.core.instrument.MeterRegistry");
    } catch (ClassNotFoundException e) {
      log.info(
          "Micrometer not found, metrics will be disabled for the Agroal data source ("
              + databaseName
              + ")");
      return;
    }

    try {
      MeterRegistry meterRegistry = applicationContext.getBean(MeterRegistry.class);

      AgroalDataSourceMetrics agroalDataSourceMetrics = agroalDataSource.getMetrics();

      Gauge.builder(
              "agroal." + databaseName + ".connections.acquired",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::acquireCount)
          .description("Number of times an acquire operation succeeded")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.active",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::activeCount)
          .description(
              "Number of active connections (These connections are in use and not available to be acquired)")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.available",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::availableCount)
          .description("Number of idle connections in the pool, available to be acquired")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.awaiting",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::awaitingCount)
          .description("Number of threads blocked, waiting to acquire a connection")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.blockingTimeAverage",
              agroalDataSourceMetrics,
              metrics -> metrics.blockingTimeAverage().toMillis())
          .description("Average time threads are blocked waiting for a connection (ms)")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.blockingTimeMax",
              agroalDataSourceMetrics,
              metrics -> metrics.blockingTimeMax().toMillis())
          .description("Maximum time a thread was blocked waiting for a connection (ms)")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.destroyed",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::destroyCount)
          .description("Number of destroyed connections")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.flushes",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::flushCount)
          .description("Number of connections removed from the pool, not counting invalid / idle")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.invalid",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::invalidCount)
          .description("Number of connections removed from the pool for being invalid")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.leakDetections",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::leakDetectionCount)
          .description(
              "Number of times a leak was detected (A single connection can be detected multiple times)")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.maxUsed",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::maxUsedCount)
          .description("Maximum number of connections active simultaneously")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.reaped",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::reapCount)
          .description("Number of connections removed from the pool for being idle")
          .register(meterRegistry);

      Gauge.builder(
              "agroal." + databaseName + ".connections.invalid",
              agroalDataSourceMetrics,
              AgroalDataSourceMetrics::invalidCount)
          .description("Number of connections removed from pool for being invalid")
          .register(meterRegistry);

    } catch (NoSuchBeanDefinitionException ignored) {
      // No MeterRegistry bean found, metrics will not be available for the Agroal data source
    } catch (Throwable e) {
      log.error("Failed to bind the metrics for the Agroal data source", e);
    }
  }

  private static TransactionIntegration createTransactionIntegration(
      ApplicationContext applicationContext) {
    try {
      Class<?> transactionIntegrationClass =
          Class.forName("digital.inception.jta.agroal.NarayanaTransactionIntegration");

      Constructor<?> transactionIntegrationConstructor =
          transactionIntegrationClass.getConstructor(ApplicationContext.class);

      Object transactionIntegrationObject =
          transactionIntegrationConstructor.newInstance(applicationContext);

      if (transactionIntegrationObject instanceof TransactionIntegration transactionIntegration) {
        return transactionIntegration;
      }
    } catch (ClassNotFoundException ignored) {
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to create the Narayana transaction integration for the Agroal data source", e);
    }

    return TransactionIntegration.none();
  }
}

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
import io.agroal.api.configuration.AgroalConnectionPoolConfiguration;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;
import io.agroal.api.transaction.TransactionIntegration;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * The <b>DataSourceUtil</b> class provides data source utility functions.
 *
 * @author Marcus Portmann
 */
public final class DataSourceUtil {

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

      AgroalPropertiesReader agroalReaderProperties =
          new AgroalPropertiesReader().readProperties(agroalProperties);
      AgroalDataSourceConfigurationSupplier agroalDataSourceConfigurationSupplier =
          agroalReaderProperties.modify();

      try {
        TransactionIntegration transactionIntegration =
            applicationContext.getBean(TransactionIntegration.class);

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

      return AgroalDataSource.from(agroalDataSourceConfigurationSupplier);
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

  //  /**
  //   * The <b>ConnectionValidator</b> class.
  //   */
  //  private static final class ConnectionValidator implements
  // AgroalConnectionPoolConfiguration.ConnectionValidator {
  //
  //    /**
  //     * The validation query.
  //     */
  //    private final String validationQuery;
  //
  //    /**
  //     * Constructs a new <b>ConnectionValidator</b>.
  //     * @param validationQuery the validation query
  //     */
  //    public ConnectionValidator(String validationQuery) {
  //      this.validationQuery = validationQuery;
  //    }
  //
  //    @Override
  //    public boolean isValid(Connection connection) {
  //      if (StringUtils.hasText(validationQuery)) {
  //        try {
  //
  //        } catch (Throwable e) {
  //          logger.error("Failed to validate the database connection", e);
  //          return false;
  //        }
  //      }
  //
  //      return false;
  //    }
  //  }
}

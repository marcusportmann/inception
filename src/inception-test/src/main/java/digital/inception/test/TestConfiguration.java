/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.test;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.JDBCUtil;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;
import io.agroal.api.transaction.TransactionIntegration;
import io.agroal.narayana.NarayanaTransactionIntegration;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestConfiguration</code> class provides the base Spring configuration for the JUnit
 * test classes that test the capabilities provided by the <b>Inception</b> framework.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableAsync
@EnableAutoConfiguration
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(
    basePackages = {"digital.inception"},
    lazyInit = true,
    excludeFilters = {
      @ComponentScan.Filter(value = SpringBootApplication.class, type = FilterType.ANNOTATION),
      @ComponentScan.Filter(
          pattern = "digital\\.inception\\.application\\.ApplicationDataSourceConfiguration",
          type = FilterType.REGEX),
      @ComponentScan.Filter(
          pattern = "digital\\.inception\\.application\\.ApplicationTransactionManager",
          type = FilterType.REGEX),
      @ComponentScan.Filter(
          pattern = "digital\\.inception\\.persistence\\.PersistenceConfiguration",
          type = FilterType.REGEX),
      @ComponentScan.Filter(
          pattern = "digital\\.inception\\.process\\.ProcessConfiguration",
          type = FilterType.REGEX)
    })
@SuppressWarnings("WeakerAccess")
public class TestConfiguration {

  private static final Object dataSourceLock = new Object();
  private static DataSource dataSource;
  private final ApplicationContext applicationContext;

  /**
   * The resources on the classpath that contain the SQL statements used to initialize the in-memory
   * application database.
   */
  @Value("classpath*:**/*-h2.sql")
  private Resource[] inMemoryInitResources;

  /**
   * Constructs a new <code>TestConfiguration</code>.
   *
   * @param applicationContext the Spring application context
   */
  public TestConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the application entity manager factory associated with the application data source.
   *
   * @return the application entity manager factory associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();

    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(false);
    jpaVendorAdapter.setShowSql(true);
    jpaVendorAdapter.setDatabase(Database.H2);

    localContainerEntityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
    localContainerEntityManagerFactoryBean.setJtaDataSource(dataSource());
    localContainerEntityManagerFactoryBean.setPackagesToScan(
        StringUtils.toStringArray(packagesToScanForEntities()));
    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

    Map<String, Object> jpaPropertyMap = localContainerEntityManagerFactoryBean.getJpaPropertyMap();

    PlatformTransactionManager transactionManager =
        applicationContext.getBean(PlatformTransactionManager.class);

    if (transactionManager instanceof JtaTransactionManager) {
      jpaPropertyMap.put(
          "hibernate.transaction.jta.platform",
          new SpringJtaPlatform(((JtaTransactionManager) transactionManager)));
    }

    return localContainerEntityManagerFactoryBean;
  }

  /**
   * Returns the Spring task executor to use for @Async method invocations.
   *
   * @return the Spring task executor to use for @Async method invocations
   */
  @Bean
  public Executor taskExecutor() {
    return new SimpleAsyncTaskExecutor();
  }

  /**
   * Returns the Spring task scheduler.
   *
   * @return the Spring task scheduler
   */
  @Bean
  public TaskScheduler taskScheduler() {
    return new ConcurrentTaskScheduler();
  }

  /**
   * Initialize the in-memory application database and return a data source that can be used to
   * interact with the database.
   *
   * <p>NOTE: This data source returned by this method must be closed after use with the <code>
   * close()</code> method.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean(name = "applicationDataSource")
  @DependsOn({"transactionManager"})
  protected DataSource dataSource() {
    synchronized (dataSourceLock) {
      if (dataSource == null) {
        try {
          TransactionManager transactionManager =
              applicationContext.getBean(TransactionManager.class);

          TransactionSynchronizationRegistry transactionSynchronizationRegistry =
              applicationContext.getBean(TransactionSynchronizationRegistry.class);

          Properties agroalProperties = new Properties();
          agroalProperties.setProperty(
              AgroalPropertiesReader.JDBC_URL,
              "jdbc:h2:mem:"
                  + Thread.currentThread().getName()
                  + ";AUTOCOMMIT=OFF;MODE=DB2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

          // db2Agroal.setProperty(AgroalPropertiesReader.PRINCIPAL, AgroalH2Utils.DB_USER);
          // db2Agroal.setProperty(AgroalPropertiesReader.CREDENTIAL, AgroalH2Utils.DB_PASSWORD);
          // db2Agroal.setProperty(AgroalPropertiesReader.RECOVERY_PRINCIPAL,
          // AgroalH2Utils.DB_USER);
          // db2Agroal.setProperty(AgroalPropertiesReader.RECOVERY_CREDENTIAL,
          // AgroalH2Utils.DB_PASSWORD);
          agroalProperties.setProperty(
              AgroalPropertiesReader.PROVIDER_CLASS_NAME, "org.h2.jdbcx.JdbcDataSource");
          agroalProperties.setProperty(AgroalPropertiesReader.MAX_SIZE, "10");

          AgroalPropertiesReader agroalReaderProperties2 =
              new AgroalPropertiesReader().readProperties(agroalProperties);
          AgroalDataSourceConfigurationSupplier agroalDataSourceConfigurationSupplier =
              agroalReaderProperties2.modify();
          TransactionIntegration transactionIntegration =
              new NarayanaTransactionIntegration(
                  transactionManager, transactionSynchronizationRegistry);

          //        TransactionIntegration txIntegration2 = new NarayanaTransactionIntegration(
          //          com.arjuna.ats.jta.TransactionManager.transactionManager(),
          // transactionSynchronizationRegistry,
          //          "java:/agroalds2", false, recoveryManagerService);

          agroalDataSourceConfigurationSupplier
              .connectionPoolConfiguration()
              .transactionIntegration(transactionIntegration);

          dataSource =
              new DataSourceProxy(AgroalDataSource.from(agroalDataSourceConfigurationSupplier));

          /*
           * Initialize the in-memory database using the SQL statements contained in the resources
           * for the Inception framework.
           */
          for (Resource databaseInitResource : inMemoryInitResources) {
            if ((!StringUtils.isEmpty(databaseInitResource.getFilename()))
                && databaseInitResource.getFilename().startsWith("inception-")) {
              loadSQL(dataSource, databaseInitResource);
            }
          }

          /*
           * Initialize the in-memory database using the SQL statements contained in any other
           * resources.
           */
          for (Resource databaseInitResource : inMemoryInitResources) {
            if ((!StringUtils.isEmpty(databaseInitResource.getFilename()))
                && (!databaseInitResource.getFilename().startsWith("inception-"))) {
              loadSQL(dataSource, databaseInitResource);
            }
          }
        } catch (Throwable e) {
          throw new RuntimeException("Failed to initialize the in-memory application database", e);
        }
      }

      return dataSource;
    }
  }

  /**
   * Returns the names of the packages to scan for JPA entity classes.
   *
   * @return the names of the packages to scan for JPA entity classes
   */
  protected List<String> packagesToScanForEntities() {
    List<String> packagesToScan = new ArrayList<>();

    packagesToScan.add("digital.inception");

    return packagesToScan;
  }

  private void loadSQL(DataSource dataSource, Resource databaseInitResource)
      throws IOException, SQLException {
    try {
      // Load the SQL statements used to initialize the database tables
      List<String> sqlStatements = JDBCUtil.loadSQL(databaseInitResource.getURL());

      // Get a connection to the in-memory database
      try (Connection connection = dataSource.getConnection()) {
        for (String sqlStatement : sqlStatements) {
          try (Statement statement = connection.createStatement()) {
            statement.execute(sqlStatement);
          }
        }

        connection.commit();
      }
    } catch (SQLException e) {
      try (Connection connection = dataSource.getConnection()) {
        JDBCUtil.shutdownHsqlDatabase(connection);
      } catch (Throwable f) {
        LoggerFactory.getLogger(TestConfiguration.class)
            .error("Failed to shutdown the in-memory application database: " + e.getMessage());
      }

      throw e;
    }
  }
}

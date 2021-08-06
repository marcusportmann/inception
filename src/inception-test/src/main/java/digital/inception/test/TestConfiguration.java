/*
 * Copyright 2021 Marcus Portmann
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digital.inception.core.liquibase.InceptionLiquibaseChangeLogs;
import digital.inception.core.util.JDBCUtil;
import digital.inception.core.util.ResourceUtil;
import digital.inception.json.DateTimeModule;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;
import io.agroal.api.transaction.TransactionIntegration;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.configuration.HubConfiguration;
import liquibase.configuration.LiquibaseConfiguration;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.integration.spring.SpringResourceAccessor;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The <b>TestConfiguration</b> class provides the base Spring configuration for the JUnit test
 * classes that test the capabilities provided by the <b>Inception</b> framework.
 *
 * <p>This configuration class disables the default application data source and Camunda Process
 * Engine bootstrapping using the component scan filters.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableAsync
@EnableCaching
@EnableConfigurationProperties
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(
    basePackages = {"digital.inception"},
    lazyInit = true,
    excludeFilters = {
      @ComponentScan.Filter(value = SpringBootApplication.class, type = FilterType.ANNOTATION),
      @ComponentScan.Filter(
          pattern = "digital\\.inception\\.bmi\\.BMIConfiguration",
          type = FilterType.REGEX)
    })
@SuppressWarnings("WeakerAccess")
public class TestConfiguration implements ResourceLoaderAware {

  private static final String[] IN_MEMORY_DATABASE_INIT_RESOURCE_PATHS = {
    // Utility modules
    "digital/inception/bmi/inception-camunda-h2.sql",
    "digital/inception/bmi/test/inception-camunda-test-h2.sql"
  };

  private static final Object dataSourceLock = new Object();

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TestConfiguration.class);

  private static DataSource dataSource;

  private final ApplicationContext applicationContext;

  /**
   * The resources on the classpath that contain the SQL statements used to initialize the in-memory
   * application database.
   */
  @Value("classpath*:**/*-h2.sql")
  private Resource[] inMemoryInitResources;

  /** The Liquibase change log. */
  @Value("${inception.application.data-source.liquibase.change-log:#{null}}")
  private String liquibaseChangeLog;

  /** Execute the Liquibase data change logs. */
  @Value("${inception.application.data-source.liquibase.data-change-logs:#{true}}")
  private boolean liquibaseDataChangeLogs;

  /** The Spring resource loader. */
  private ResourceLoader resourceLoader;

  /**
   * Constructs a new <b>TestConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   */
  public TestConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  //  /** The optional comma-delimited packages on the classpath to scan for JPA entities. */
  //  @Value("${inception.persistence.entity-packages:#{null}}")
  //  private String packagesToScanForEntities;

  /**
   * Initialize the in-memory application database and return a data source that can be used to
   * interact with the database.
   *
   * <p>This data source returned by this method must be closed after use with the <b>close()</b>
   * method.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean
  @Primary
  public DataSource applicationDataSource() {
    synchronized (dataSourceLock) {
      if (dataSource == null) {
        try {
          Properties agroalProperties = new Properties();
          agroalProperties.setProperty(
              AgroalPropertiesReader.JDBC_URL,
              "jdbc:h2:mem:"
                  + Thread.currentThread().getName()
                  + ";AUTOCOMMIT=OFF;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE");

          agroalProperties.setProperty(
              AgroalPropertiesReader.PROVIDER_CLASS_NAME, "org.h2.jdbcx.JdbcDataSource");
          agroalProperties.setProperty(AgroalPropertiesReader.MAX_SIZE, "5");

          AgroalPropertiesReader agroalReaderProperties2 =
              new AgroalPropertiesReader().readProperties(agroalProperties);
          AgroalDataSourceConfigurationSupplier agroalDataSourceConfigurationSupplier =
              agroalReaderProperties2.modify();

          TransactionIntegration transactionIntegration =
              applicationContext.getBean(TransactionIntegration.class);

          if (transactionIntegration != null) {
            agroalDataSourceConfigurationSupplier
                .connectionPoolConfiguration()
                .transactionIntegration(transactionIntegration);
          }

          dataSource =
              new DataSourceProxy(AgroalDataSource.from(agroalDataSourceConfigurationSupplier));

          // Initialize the in-memory database using Liquibase changeSets
          LiquibaseConfiguration.getInstance()
              .getConfiguration(HubConfiguration.class)
              .setLiquibaseHubMode("OFF");

          try (Connection connection = dataSource.getConnection()) {
            liquibase.database.Database database =
                DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            for (String changeLogFile : InceptionLiquibaseChangeLogs.CORE_CHANGE_LOGS) {
              if (ResourceUtil.classpathResourceExists(changeLogFile)) {
                Liquibase liquibase =
                    new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
                liquibase.update(new Contexts(), new LabelExpression());
              }
            }

            for (String changeLogFile : InceptionLiquibaseChangeLogs.UTILITY_CHANGE_LOGS) {
              if (ResourceUtil.classpathResourceExists(changeLogFile)) {
                Liquibase liquibase =
                    new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
                liquibase.update(new Contexts(), new LabelExpression());
              }
            }

            for (String changeLogFile : InceptionLiquibaseChangeLogs.BUSINESS_CHANGE_LOGS) {
              if (ResourceUtil.classpathResourceExists(changeLogFile)) {
                Liquibase liquibase =
                    new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
                liquibase.update(new Contexts(), new LabelExpression());
              }
            }

            if (liquibaseDataChangeLogs) {
              for (String changeLogFile : InceptionLiquibaseChangeLogs.BUSINESS_DATA_CHANGE_LOGS) {
                if (ResourceUtil.classpathResourceExists(changeLogFile)) {
                  Liquibase liquibase =
                      new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
                  liquibase.update(new Contexts(), new LabelExpression());
                }
              }
            }

            for (String changeLogFile : InceptionLiquibaseChangeLogs.TEST_CHANGE_LOGS) {
              if (ResourceUtil.classpathResourceExists(changeLogFile)) {
                Liquibase liquibase =
                    new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
                liquibase.update(new Contexts(), new LabelExpression());
              }
            }


            if (StringUtils.hasText(liquibaseChangeLog)) {
              SpringResourceAccessor resourceAccessor = new SpringResourceAccessor(resourceLoader);
              Liquibase liquibase = new Liquibase(liquibaseChangeLog, resourceAccessor, database);
              liquibase.update(new Contexts(), new LabelExpression());
            }
          }

          /*
           * Initialize the in-memory database using the SQL statements contained in the resources
           * for the Inception framework in a specific order
           */
          for (String inMemoryDatabaseInitResourcePath : IN_MEMORY_DATABASE_INIT_RESOURCE_PATHS) {
            if (ResourceUtil.classpathResourceExists(inMemoryDatabaseInitResourcePath)) {
              loadSQL(
                  dataSource,
                  ResourceUtil.getClasspathResourceURL(inMemoryDatabaseInitResourcePath));
            }
          }

          /*
           * Initialize the in-memory database using the SQL statements contained in any other
           * resources.
           */
          for (Resource databaseInitResource : inMemoryInitResources) {
            if ((StringUtils.hasText(databaseInitResource.getFilename()))
                && (!databaseInitResource.getFilename().startsWith("inception-"))) {
              loadSQL(dataSource, databaseInitResource.getURL());
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
   * Returns the application entity manager factory bean associated with the application data
   * source.
   *
   * @return the application entity manager factory bean associated with the application data source
   */
  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory(
      @Qualifier("applicationDataSource") DataSource dataSource,
      JpaVendorAdapter jpaVendorAdapter,
      PlatformTransactionManager platformTransactionManager) {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();

    entityManagerFactoryBean.setPersistenceUnitName("application");
    entityManagerFactoryBean.setJtaDataSource(dataSource);
    entityManagerFactoryBean.setPackagesToScan(
        StringUtils.toStringArray(packagesToScanForEntities()));
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

    Map<String, Object> jpaPropertyMap = entityManagerFactoryBean.getJpaPropertyMap();
    jpaPropertyMap.put("hibernate.transaction.coordinator_class", "jta");
    jpaPropertyMap.put("hibernate.transaction.jta.platform", "JBossTS");

    return entityManagerFactoryBean;
  }

  /**
   * Returns the cache manager.
   *
   * @return the cache manager
   */
  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager();
  }

  /**
   * Returns the JPA vendor adapter, which configures the behaviour of the JPA ORM provider based on
   * the type of database for the application data source.
   *
   * @returrn the JpaVendorAdapter, which configures the behaviour of the JPA ORM provider based on
   *     the type of database for the application data source.
   */
  @Bean
  public JpaVendorAdapter jpaVendorAdapter(
      @Qualifier("applicationDataSource") DataSource dataSource) {
    try {
      HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
      jpaVendorAdapter.setGenerateDdl(false);

      try (Connection connection = dataSource.getConnection()) {
        DatabaseMetaData metaData = connection.getMetaData();

        switch (metaData.getDatabaseProductName()) {
          case "H2":
            jpaVendorAdapter.setDatabase(Database.H2);
            jpaVendorAdapter.setShowSql(true);

            break;

          case "Microsoft SQL Server":
            jpaVendorAdapter.setDatabase(Database.SQL_SERVER);
            jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLServer2012Dialect");
            jpaVendorAdapter.setShowSql(false);

            break;

          default:
            jpaVendorAdapter.setDatabase(Database.DEFAULT);
            jpaVendorAdapter.setShowSql(false);

            break;
        }

        return jpaVendorAdapter;
      }
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialize the JpaVendorAdapter bean", e);
    }
  }

  /**
   * Returns the Jackson2 object mapper.
   *
   * @return the Jackson2 object mapper
   */
  @Bean
  public ObjectMapper objectMapper() {
    return jackson2ObjectMapperBuilder().build().disable(SerializationFeature.INDENT_OUTPUT);
  }

  /**
   * Set the Spring resource loader.
   *
   * @param resourceLoader the Spring resource loader
   */
  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
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
   * Returns the WebClient.Builder.
   *
   * @return the default WebClient.Builder
   */
  @Bean
  public WebClient.Builder webClientBuilder() {

    // TODO: Configure with security if available -- MARCUS

    return WebClient.builder();
  }

  /**
   * Returns the <b>Jackson2ObjectMapperBuilder</b> bean, which configures the Jackson JSON
   * processor package.
   *
   * @return the <b>Jackson2ObjectMapperBuilder</b> bean, which configures the Jackson JSON
   *     processor package
   */
  protected Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
    jackson2ObjectMapperBuilder.indentOutput(true);

    /*
     * Install the custom Jackson module that supports serializing and de-serializing ISO 8601 date
     * and date/time values. The jackson-datatype-jsr310 module provided by Jackson was not used as
     * it does not handle timezones correctly for LocalDateTime objects.
     */
    jackson2ObjectMapperBuilder.modulesToInstall(new DateTimeModule());

    return jackson2ObjectMapperBuilder;
  }

  /**
   * Returns the names of the packages to scan for JPA entity classes.
   *
   * @return the names of the packages to scan for JPA entity classes
   */
  protected List<String> packagesToScanForEntities() {
    List<String> packagesToScan = new ArrayList<>();

    //    // Add the packages to scan for entities explicitly specified in the configuration
    // property
    //    if (StringUtils.hasText(this.packagesToScanForEntities)) {
    //      for (String packageToScanForEntities : this.packagesToScanForEntities.split(",")) {
    //        // Replace any existing packages to scan with the higher level package
    //        packagesToScan.removeIf(
    //            packageToScan -> packageToScan.startsWith(packageToScanForEntities));
    //
    //        // Check if there is a higher level package already being scanned
    //        if (packagesToScan.stream().noneMatch(packageToScanForEntities::startsWith)) {
    //          packagesToScan.add(packageToScanForEntities);
    //        }
    //      }
    //    }

    // Add the base packages specified using the EnableJpaRepositories annotation
    Map<String, Object> annotatedBeans =
        applicationContext.getBeansWithAnnotation(EnableJpaRepositories.class);

    for (String beanName : annotatedBeans.keySet()) {
      Class<?> beanClass = annotatedBeans.get(beanName).getClass();

      EnableJpaRepositories enableJpaRepositories =
          beanClass.getAnnotation(EnableJpaRepositories.class);

      if (enableJpaRepositories != null) {
        for (String basePackage : enableJpaRepositories.basePackages()) {
          if (!basePackage.startsWith("digital.inception")) {
            // Replace any existing packages to scan with the higher level package
            packagesToScan.removeIf(packageToScan -> packageToScan.startsWith(basePackage));

            // Check if there is a higher level package already being scanned
            if (packagesToScan.stream().noneMatch(basePackage::startsWith)) {
              packagesToScan.add(basePackage);
            }
          }
        }
      }
    }

    logger.info(
        "Scanning the following packages for JPA entities: "
            + StringUtils.collectionToDelimitedString(packagesToScan, ","));

    return packagesToScan;
  }

  private void loadSQL(DataSource dataSource, URL databaseInitResourceUrl)
      throws IOException, SQLException {
    try {
      // Load the SQL statements used to initialize the database tables
      List<String> sqlStatements = JDBCUtil.loadSQL(databaseInitResourceUrl);

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

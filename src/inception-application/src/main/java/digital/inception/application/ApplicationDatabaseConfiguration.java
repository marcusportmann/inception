/*
 * Copyright 2019 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.JDBCUtil;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;
import io.agroal.api.transaction.TransactionIntegration;
import io.agroal.narayana.NarayanaTransactionIntegration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationDatabaseConfiguration</code> class provides access to the application
 * database configuration and initialises the application data source and the application entity
 * manager factory bean associated with the application data source.
 *
 * @author Marcus Portmann
 */
@Configuration
@ConditionalOnProperty(value = "application.database.dataSource")
@SuppressWarnings("unused")
public class ApplicationDatabaseConfiguration
  implements DisposableBean
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(
      ApplicationDatabaseConfiguration.class);

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The application data source.
   */
  private DataSource dataSource;

  /**
   * The fully qualified name of the data source class used to connect to the application
   * database.
   */
  @Value("${application.database.dataSource:#{null}}")
  private String dataSourceClass;

  /**
   * The resources on the classpath that contain the SQL statements used to initialize the in-memory
   * application database.
   */
  @Value("classpath*:**/*-h2.sql")
  private Resource[] databaseInitResources;

  /**
   * The optional paths to the resources on the classpath that contain the SQL statements used to
   * initialize the in-memory application database.
   */
  @Value("${application.database.inMemoryDatabaseInitResources:#{null}}")
  private String inMemoryDatabaseInitResources;

  /**
   * The maximum size of the database connection pool used to connect to the application database.
   */
  @Value("${application.database.maxPoolSize:5}")
  private int maxPoolSize;

  /**
   * The minimum size of the database connection pool used to connect to the application database.
   */
  @Value("${application.database.minPoolSize:1}")
  private int minPoolSize;

  /**
   * The optional comma-delimited packages on the classpath to scan for JPA entities.
   */
  @Value("${application.database.packagesToScanForEntities:#{null}}")
  private String packagesToScanForEntities;

  /**
   * The URL used to connect to the application database.
   */
  @Value("${application.database.url:#{null}}")
  private String url;

  /**
   * The XA password for the application database.
   */
  @Value("${application.database.xaPassword:#{null}}")
  private String xaPassword;

  /**
   * The XA server name for the application database.
   */
  @Value("${application.database.xaServerName:#{null}}")
  private String xaServerName;

  /**
   * The XA username for the application database.
   */
  @Value("${application.database.xaUsername:#{null}}")
  private String xaUsername;

  /**
   * The username for the application database.
   */
  @Value("${application.database.username:#{null}}")
  private String username;

  /**
   * The password for the application database.
   */
  @Value("${application.database.password:#{null}}")
  private String password;


  /**
   * Is transaction recovery enabled for the application database.
   */
  @Value("${application.database.recoveryEnabled:false}")
  private boolean recoveryEnabled;

  /**
   * The recovery username for the application database.
   */
  @Value("${application.database.recoveryUsername:#{null}}")
  private String recoveryUsername;

  /**
   * The recovery password for the application database.
   */
  @Value("${application.database.recoveryPassword:#{null}}")
  private String recoveryPassword;

  /**
   * Constructs a new <code>ApplicationDatabaseConfiguration</code>.
   *
   * @param applicationContext the Spring application context
   */
  public ApplicationDatabaseConfiguration(ApplicationContext applicationContext)
  {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the data source that can be used to interact with the application database.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean(name = "applicationDataSource")
  @DependsOn({ "transactionManager" })
  public DataSource dataSource()
  {
    try
    {
      if ((dataSourceClass == null) || (url == null))
      {
        throw new ApplicationException("Failed to retrieve the application database configuration");
      }

      TransactionManager transactionManager = applicationContext.getBean(TransactionManager.class);

      TransactionSynchronizationRegistry transactionSynchronizationRegistry = applicationContext.getBean(TransactionSynchronizationRegistry.class);

      Properties agroalProperties = new Properties();
      agroalProperties.setProperty(AgroalPropertiesReader.JDBC_URL, url);
      if (!StringUtils.isEmpty(username))
      {
        agroalProperties.setProperty(AgroalPropertiesReader.PRINCIPAL, username);
      }

      if (!StringUtils.isEmpty(password))
      {
        agroalProperties.setProperty(AgroalPropertiesReader.CREDENTIAL, password);
      }

      if (recoveryEnabled)
      {
        if (!StringUtils.isEmpty(recoveryUsername))
        {
          agroalProperties.setProperty(AgroalPropertiesReader.RECOVERY_PRINCIPAL, recoveryUsername);
        }

        if (!StringUtils.isEmpty(recoveryPassword))
        {
          agroalProperties.setProperty(AgroalPropertiesReader.RECOVERY_CREDENTIAL, recoveryPassword);
        }
      }


      agroalProperties.setProperty(AgroalPropertiesReader.PROVIDER_CLASS_NAME, dataSourceClass);

      agroalProperties.setProperty(AgroalPropertiesReader.MIN_SIZE, minPoolSize > 0 ? Integer.toString(minPoolSize) : "1");
      agroalProperties.setProperty(AgroalPropertiesReader.MAX_SIZE, maxPoolSize > 0 ? Integer.toString(maxPoolSize) : "5");

      AgroalPropertiesReader agroalReaderProperties2 = new AgroalPropertiesReader().readProperties(agroalProperties);
      AgroalDataSourceConfigurationSupplier agroalDataSourceConfigurationSupplier = agroalReaderProperties2.modify();
      TransactionIntegration transactionIntegration = new NarayanaTransactionIntegration(
        transactionManager,
        transactionSynchronizationRegistry);



//          TransactionIntegration txIntegration2 = new NarayanaTransactionIntegration(
//            com.arjuna.ats.jta.TransactionManager.transactionManager(), transactionSynchronizationRegistry,
//            "java:/agroalds2", false, recoveryManagerService);


      agroalDataSourceConfigurationSupplier.connectionPoolConfiguration().transactionIntegration(transactionIntegration);

      dataSource = AgroalDataSource.from(agroalDataSourceConfigurationSupplier);




//      /*
//       * The SAP JDBC driver does not return a DataSource, instead it provides connections so we
//       * make use of the DriverManagerDataSource.
//       */
//      if (dataSourceClass.equals("com.sap.db.jdbc.Driver"))
//      {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName(dataSourceClass);
//        ds.setUrl(url);
//        dataSource = ds;
//      }
//      else
//      {
//        Class<? extends DataSource> dataSourceClass = Thread.currentThread().getContextClassLoader()
//            .loadClass(this.dataSourceClass).asSubclass(DataSource.class);
//
//        dataSource = DataSourceBuilder.create().type(dataSourceClass).url(url).build();
//      }

















      Database databaseVendor = Database.DEFAULT;

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        logger.info("Connected to the " + metaData.getDatabaseProductName()
            + " application database with version " + metaData.getDatabaseProductVersion());

        switch (metaData.getDatabaseProductName())
        {
          case "H2":

            databaseVendor = Database.H2;

            break;

          case "Microsoft SQL Server":

            databaseVendor = Database.SQL_SERVER;

          default:

            logger.info("The default database tables will not be populated for the database type ("
                + metaData.getDatabaseProductName() + ")");

            break;
        }
      }

      if (databaseVendor == Database.H2)
      {
        logger.info("Initializing the in-memory H2 database");

        /*
         * Initialize the in-memory database using the SQL statements contained in the resources
         * for the Inception framework.
         */
        for (Resource databaseInitResource : databaseInitResources)
        {
          if ((!StringUtils.isEmpty(databaseInitResource.getFilename()))
              && databaseInitResource.getFilename().contains("inception-"))
          {
            loadSQL(dataSource, databaseInitResource);
          }
        }

        /*
         * Initialize the in-memory database using the SQL statements contained in any other
         * resources for the application.
         */
        for (Resource databaseInitResource : databaseInitResources)
        {
          if ((!StringUtils.isEmpty(databaseInitResource.getFilename()))
              && (!databaseInitResource.getFilename().contains("inception-")))
          {
            loadSQL(dataSource, databaseInitResource);
          }
        }
      }

      return dataSource;

//      if (dataSource instanceof XADataSource)
//      {
//        return new TransactionalDataSource((XADataSource) dataSource);
//
//        /*
//         * AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
//         *
//         * atomikosDataSourceBean.setUniqueResourceName("ApplicationDataSource");
//         * atomikosDataSourceBean.setXaDataSource((XADataSource) dataSource);
//         *
//         * if (databaseVendor == Database.SQL_SERVER)
//         * {
//         * // Set the test query
//         * atomikosDataSourceBean.setTestQuery("SELECT 1;");
//         *
//         * // Set the XA properties if they are available
//         * // See: https://www.atomikos.com/Documentation/ConfiguringSQLServer
//         * if ((!StringUtils.isEmpty(xaServerName))
//         *     && (!StringUtils.isEmpty(xaUsername))
//         *     && (!StringUtils.isEmpty(xaPassword)))
//         * {
//         *   Properties p = new Properties();
//         *   p.setProperty("serverName", xaServerName);
//         *   p.setProperty("user", xaUsername);
//         *   p.setProperty("password", xaPassword);
//         *
//         *   atomikosDataSourceBean.setXaProperties(p);
//         * }
//         * else
//         * {
//         *   throw new FatalBeanException(
//         *       "No XA properties specified for the AtomikosDataSourceBean for Microsoft SQL Server");
//         * }
//         * }
//         *
//         * if (minPoolSize > 0)
//         * {
//         * atomikosDataSourceBean.setMinPoolSize(minPoolSize);
//         * }
//         * else
//         * {
//         * atomikosDataSourceBean.setMinPoolSize(1);
//         * }
//         *
//         * if (maxPoolSize > 0)
//         * {
//         * atomikosDataSourceBean.setMaxPoolSize(maxPoolSize);
//         * }
//         * else
//         * {
//         * atomikosDataSourceBean.setMinPoolSize(5);
//         * }
//         *
//         * return atomikosDataSourceBean;
//         */
//      }
//      else
//      {
//        return dataSource;
//      }
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialize the application data source", e);
    }
  }

  /**
   * Destroy.
   */
  @Override
  public void destroy()
  {
    shutdownInMemoryApplicationDatabase();
  }

  /**
   * Returns the application entity manager factory bean associated with the application data
   * source.
   *
   * @return the application entity manager factory bean associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean()
  {
    try
    {
      DataSource dataSource = dataSource();

      LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
          new LocalContainerEntityManagerFactoryBean();

      HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
      jpaVendorAdapter.setGenerateDdl(false);

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        switch (metaData.getDatabaseProductName())
        {
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
      }

      entityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
      entityManagerFactoryBean.setJtaDataSource(dataSource);
      entityManagerFactoryBean.setPackagesToScan(StringUtils.toStringArray(
          packagesToScanForEntities()));
      entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

      PlatformTransactionManager platformTransactionManager = applicationContext.getBean(
          PlatformTransactionManager.class);

      if (platformTransactionManager instanceof JtaTransactionManager)
      {
        Map<String, Object> jpaPropertyMap = entityManagerFactoryBean.getJpaPropertyMap();

        jpaPropertyMap.put("hibernate.transaction.jta.platform", new SpringJtaPlatform(
            ((JtaTransactionManager) platformTransactionManager)));
      }

      return entityManagerFactoryBean;
    }
    catch (Throwable e)
    {
      throw new FatalBeanException(
          "Failed to initialize the application entity manager factory bean", e);
    }
  }

  private void loadSQL(DataSource dataSource, Resource databaseInitResource)
    throws IOException, SQLException
  {
    logger.info("Executing the SQL statements in the file '" + databaseInitResource.getFilename()
        + "'");

    try
    {
      // Load the SQL statements used to initialize the database tables
      List<String> sqlStatements = JDBCUtil.loadSQL(databaseInitResource.getURL());

      // Get a connection to the in-memory database
      try (Connection connection = dataSource.getConnection())
      {
        for (String sqlStatement : sqlStatements)
        {
          LoggerFactory.getLogger(Application.class).debug("Executing SQL statement: "
              + sqlStatement);

          try (Statement statement = connection.createStatement())
          {
            statement.execute(sqlStatement);
          }
        }
      }
    }
    catch (SQLException e)
    {
      try (Connection connection = dataSource.getConnection())
      {
        JDBCUtil.shutdownHsqlDatabase(connection);
      }
      catch (Throwable f)
      {
        LoggerFactory.getLogger(Application.class).error(
            "Failed to shutdown the in-memory application database: " + e.getMessage());
      }

      throw e;
    }
  }

  /**
   * Returns the names of the packages to scan for JPA entities.
   *
   * @return the names of the packages to scan for JPA entities
   */
  private List<String> packagesToScanForEntities()
  {
    List<String> packagesToScanForEntities = new ArrayList<>();

    packagesToScanForEntities.add("digital.inception");

    if (!StringUtils.isEmpty(this.packagesToScanForEntities))
    {
      String[] packagesToScan = this.packagesToScanForEntities.split(",");

      Collections.addAll(packagesToScanForEntities, StringUtils.trimArrayElements(packagesToScan));
    }

    return packagesToScanForEntities;
  }

  /**
   * Shutdown the in-memory application database if required.
   */
  private void shutdownInMemoryApplicationDatabase()
  {
    if (dataSource != null)
    {
      try
      {
        try (Connection connection = dataSource.getConnection();
          Statement statement = connection.createStatement())

        {
          var metaData = connection.getMetaData();

          if ("H2".equals(metaData.getDatabaseProductName()))
          {
            logger.info("Shutting down the in-memory " + metaData.getDatabaseProductName()
                + " application database with version " + metaData.getDatabaseProductVersion());

            JDBCUtil.shutdownHsqlDatabase(connection);
          }
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to shutdown the in-memory application database", e);
      }
    }
  }
}

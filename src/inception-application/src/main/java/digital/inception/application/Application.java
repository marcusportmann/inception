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

import com.atomikos.jdbc.AtomikosDataSourceBean;

import digital.inception.core.support.MergedMessageSource;
import digital.inception.core.util.JDBCUtil;
import digital.inception.core.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import javax.sql.DataSource;
import javax.sql.XADataSource;

/**
 * The <code>Application</code> class provides the class that all application-specific application
 * classes should be derived from.
 *
 * @author Marcus Portmann
 */
@Component
@ComponentScan(basePackages = { "digital.inception" }, lazyInit = true)
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SuppressWarnings({ "unused", "WeakerAccess" })
public abstract class Application extends ApplicationBase
  implements DisposableBean
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  /**
   * The distributed in-memory caches.
   */
  Map<String, Map> caches = new ConcurrentHashMap<>();

  /**
   * The Spring application context.
   */
  @Autowired
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
  private String databaseDataSourceClass;

  /**
   * The maximum size of the database connection pool used to connect to the application database.
   */
  @Value("${application.database.maxPoolSize:#{5}}")
  private int databaseMaxPoolSize;

  /**
   * The minimum size of the database connection pool used to connect to the application database.
   */
  @Value("${application.database.minPoolSize:#{1}}")
  private int databaseMinPoolSize;

  /**
   * The URL used to connect to the application database.
   */
  @Value("${application.database.url:#{null}}")
  private String databaseUrl;

  /**
   * The XA password for the application database.
   */
  @Value("${application.database.xaPassword:#{null}}")
  private String databaseXaPassword;

  /**
   * The XA server name for the application database.
   */
  @Value("${application.database.xaServerName:#{null}}")
  private String databaseXaServerName;

  /**
   * The XA username for the application database.
   */
  @Value("${application.database.xaUsername:#{null}}")
  private String databaseXaUsername;

  /**
   * Constructs a new <code>Application</code>.
   */
  public Application() {}

  /**
   * Destroy.
   */
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
      entityManagerFactoryBean.setPackagesToScan(StringUtils.toStringArray(getJpaPackagesToScan()));
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

  /**
   * Returns the application message source.
   *
   * @return the application message source
   */
  @Bean
  public MessageSource messageSource()
  {
    MergedMessageSource messageSource = new MergedMessageSource();
    messageSource.setBasename("classpath*:messages");

    return messageSource;
  }

  /**
   * Returns the Spring task executor to use for @Async method invocations.
   *
   * @return the Spring task executor to use for @Async method invocations
   */
  @Bean
  public Executor taskExecutor()
  {
    return new SimpleAsyncTaskExecutor();
  }

  /**
   * Returns the Spring task scheduler.
   *
   * @return the Spring task scheduler
   */
  @Bean
  public TaskScheduler taskScheduler()
  {
    return new ConcurrentTaskScheduler();
  }

  /**
   * Returns the data source that can be used to interact with the application database.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean(name = "applicationDataSource")
  @DependsOn({ "transactionManager" })
  protected DataSource dataSource()
  {
    try
    {
      if ((databaseDataSourceClass == null) || (databaseUrl == null))
      {
        throw new ApplicationException("Failed to retrieve the application database configuration");
      }

      /*
       * The SAP JDBC driver does not return a DataSource, instead it provides connections so we
       * make use of the DriverManagerDataSource.
       */
      if (databaseDataSourceClass.equals("com.sap.db.jdbc.Driver"))
      {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(databaseDataSourceClass);
        ds.setUrl(databaseUrl);
        dataSource = ds;
      }
      else
      {
        Class<? extends DataSource> dataSourceClass = Thread.currentThread().getContextClassLoader()
            .loadClass(databaseDataSourceClass).asSubclass(DataSource.class);

        dataSource = DataSourceBuilder.create().type(dataSourceClass).url(databaseUrl).build();
      }

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
         * Initialize the in-memory database using the SQL statements contained in the file with
         * the specified resource path.
         */
        for (String resourcePath : getInMemoryDatabaseInitResources())
        {
          logger.info("Executing the SQL statements in the file '" + resourcePath + "'");

          try
          {
            // Load the SQL statements used to initialize the database tables
            List<String> sqlStatements = JDBCUtil.loadSQL(resourcePath);

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
            try (Connection connection = dataSource.getConnection();
              Statement shutdownStatement = connection.createStatement())
            {
              shutdownStatement.executeUpdate("SHUTDOWN");
            }
            catch (Throwable f)
            {
              LoggerFactory.getLogger(Application.class).error(
                  "Failed to shutdown the in-memory application database: " + e.getMessage());
            }

            throw e;
          }
        }
      }

      if (dataSource instanceof XADataSource)
      {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        atomikosDataSourceBean.setUniqueResourceName("ApplicationDataSource");
        atomikosDataSourceBean.setXaDataSource((XADataSource) dataSource);

        if (databaseVendor == Database.SQL_SERVER)
        {
          // Set the test query
          atomikosDataSourceBean.setTestQuery("SELECT 1;");

          // Set the XA properties if they are available
          // See: https://www.atomikos.com/Documentation/ConfiguringSQLServer
          if ((!StringUtil.isNullOrEmpty(databaseXaServerName))
              && (!StringUtil.isNullOrEmpty(databaseXaUsername))
              && (!StringUtil.isNullOrEmpty(databaseXaPassword)))
          {
            Properties p = new Properties();
            p.setProperty("serverName", databaseXaServerName);
            p.setProperty("user", databaseXaUsername);
            p.setProperty("password", databaseXaPassword);

            atomikosDataSourceBean.setXaProperties(p);
          }
          else
          {
            throw new FatalBeanException(
                "No XA properties specified for the AtomikosDataSourceBean "
                + "for Microsoft SQL Server");
          }
        }

        if (databaseMinPoolSize > 0)
        {
          atomikosDataSourceBean.setMinPoolSize(databaseMinPoolSize);
        }
        else
        {
          atomikosDataSourceBean.setMinPoolSize(1);
        }

        if (databaseMaxPoolSize > 0)
        {
          atomikosDataSourceBean.setMaxPoolSize(databaseMaxPoolSize);
        }
        else
        {
          atomikosDataSourceBean.setMinPoolSize(5);
        }

        return atomikosDataSourceBean;
      }
      else
      {
        return dataSource;
      }
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialize the application data source", e);
    }
  }

  /**
   * Returns the paths to the resources on the classpath that contain the SQL statements used to
   * initialize the in-memory application database.
   */
  protected List<String> getInMemoryDatabaseInitResources()
  {
    List<String> resources = new ArrayList<>();

    resources.add("digital/inception/core/inception-core-h2.sql");

    try
    {
      Class.forName("digital.inception.codes.CodesService");

      resources.add("digital/inception/codes/inception-codes-h2.sql");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class.forName("digital.inception.configuration.ConfigurationService");

      resources.add("digital/inception/configuration/inception-configuration-h2.sql");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class.forName("digital.inception.messaging.MessagingService");

      resources.add("digital/inception/messaging/inception-messaging-h2.sql");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class.forName("digital.inception.reporting.ReportingService");

      resources.add("digital/inception/reporting/inception-reporting-h2.sql");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class.forName("digital.inception.scheduler.SchedulerService");

      resources.add("digital/inception/scheduler/inception-scheduler-h2.sql");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class.forName("digital.inception.security.SecurityService");

      resources.add("digital/inception/security/inception-security-h2.sql");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class.forName("digital.inception.sms.SMSService");

      resources.add("digital/inception/sms/inception-sms-h2.sql");
    }
    catch (ClassNotFoundException ignored) {}

    return resources;
  }

  /**
   * Returns the names of the packages to scan for JPA classes.
   *
   * @return the names of the packages to scan for JPA classes
   */
  protected List<String> getJpaPackagesToScan()
  {
    List<String> packagesToScan = new ArrayList<>();

    packagesToScan.add("digital.inception");

    return packagesToScan;
  }

  /**
   * Shutdown the in-memory application database if required.
   */
  protected void shutdownInMemoryApplicationDatabase()
  {
    if (dataSource != null)
    {
      try
      {
        try (Connection connection = dataSource.getConnection();
          Statement statement = connection.createStatement())

        {
          DatabaseMetaData metaData = connection.getMetaData();

          switch (metaData.getDatabaseProductName())
          {
            case "H2":

              logger.info("Shutting down the in-memory " + metaData.getDatabaseProductName()
                  + " application database with version " + metaData.getDatabaseProductVersion());

              statement.executeUpdate("SHUTDOWN");

              break;

            default:

              break;
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

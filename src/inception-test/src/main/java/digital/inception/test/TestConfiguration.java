/*
 * Copyright 2018 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import com.atomikos.jdbc.AtomikosDataSourceBean;
import digital.inception.core.util.JDBCUtil;
import net.sf.cglib.proxy.Enhancer;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestConfiguration</code> class provides the base Spring configuration for the JUnit
 * test classes that test the capabilities provided by the <b>Inception</b> framework.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(basePackages = { "digital.inception" }, lazyInit = true,
    excludeFilters = {@ComponentScan.Filter(value = SpringBootApplication.class,
        type = FilterType.ANNOTATION), @ComponentScan.Filter(pattern = "digital\\.inception\\application\\.ApplicationTransactionManager",
      type = FilterType.REGEX) })
public class TestConfiguration
{
  private static final Object dataSourceLock = new Object();
  private static DataSource dataSource;
  @Autowired
  private ApplicationContext applicationContext;

  /**
   * Returns the application entity manager factory associated with the application data source.
   *
   * @return the application entity manager factory associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory()
  {
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();

    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(false);
    jpaVendorAdapter.setShowSql(true);
    jpaVendorAdapter.setDatabase(Database.H2);

    localContainerEntityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
    localContainerEntityManagerFactoryBean.setJtaDataSource(dataSource());
    localContainerEntityManagerFactoryBean.setPackagesToScan(StringUtils.toStringArray(
        getJpaPackagesToScan()));
    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

    PlatformTransactionManager platformTransactionManager = applicationContext.getBean(
        PlatformTransactionManager.class);

    if ((platformTransactionManager != null)
        && (platformTransactionManager instanceof JtaTransactionManager))
    {
      Map<String, Object> jpaPropertyMap =
          localContainerEntityManagerFactoryBean.getJpaPropertyMap();

      jpaPropertyMap.put("hibernate.transaction.jta.platform", new SpringJtaPlatform(
          ((JtaTransactionManager) platformTransactionManager)));
    }

    return localContainerEntityManagerFactoryBean;
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
   * Initialise the in-memory application database and return a data source that can be used to
   * interact with the database.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean(name = "applicationDataSource")
  @DependsOn({ "transactionManager" })
  protected DataSource dataSource()
  {
    synchronized (dataSourceLock)
    {
      if (dataSource == null)
      {
        boolean logSQL = false;

        try
        {
          JdbcDataSource jdbcDataSource = new JdbcDataSource();

          jdbcDataSource.setURL("jdbc:h2:mem:" + Thread.currentThread().getName()
              + ";MODE=DB2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

          Runtime.getRuntime().addShutdownHook(new Thread(() ->
              {
                try
                {
                  try (Connection connection = jdbcDataSource.getConnection();
                    Statement statement = connection.createStatement())

                  {
                    statement.executeUpdate("SHUTDOWN");
                  }
                }
                catch (Throwable e)
                {
                  throw new RuntimeException(
                      "Failed to shutdown the in-memory application database", e);
                }
              }
              ));

          /*
           * Initialise the in-memory database using the SQL statements contained in the file with
           * the specified resource path.
           */
          for (String resourcePath : getDatabaseInitResources())
          {
            try
            {
              // Load the SQL statements used to initialise the database tables
              List<String> sqlStatements = JDBCUtil.loadSQL(resourcePath);

              // Get a connection to the in-memory database
              try (Connection connection = jdbcDataSource.getConnection())
              {
                for (String sqlStatement : sqlStatements)
                {
                  if (logSQL)
                  {
                    LoggerFactory.getLogger(TestConfiguration.class).info(
                        "Executing SQL statement: " + sqlStatement);
                  }

                  try (Statement statement = connection.createStatement())
                  {
                    statement.execute(sqlStatement);
                  }
                }
              }
            }
            catch (SQLException e)
            {
              try (Connection connection = jdbcDataSource.getConnection();
                Statement shutdownStatement = connection.createStatement())
              {
                shutdownStatement.executeUpdate("SHUTDOWN");
              }
              catch (Throwable f)
              {
                LoggerFactory.getLogger(TestConfiguration.class).error(
                    "Failed to shutdown the in-memory application database: " + e.getMessage());
              }

              throw e;
            }
          }

          Enhancer enhancer = new Enhancer();
          enhancer.setSuperclass(AtomikosDataSourceBean.class);
          enhancer.setCallback(new DataSourceTracker());

          AtomikosDataSourceBean atomikosDataSourceBean =
              (AtomikosDataSourceBean) enhancer.create();

          atomikosDataSourceBean.setUniqueResourceName(Thread.currentThread().getName()
              + "-ApplicationDataSource");

          atomikosDataSourceBean.setXaDataSource(jdbcDataSource);
          atomikosDataSourceBean.setMinPoolSize(5);
          atomikosDataSourceBean.setMaxPoolSize(10);

          dataSource = atomikosDataSourceBean;
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialise the in-memory application database", e);
        }
      }

      return dataSource;
    }
  }

  /**
   * Returns the paths to the resources on the classpath that contain the SQL statements used to
   * initialise the in-memory application database.
   */
  protected List<String> getDatabaseInitResources()
  {
    List<String> resources = new ArrayList<>();

    resources.add("digital/inception/core/inception-core-h2.sql");
    resources.add("digital/inception/test/inception-test-h2.sql");

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
}

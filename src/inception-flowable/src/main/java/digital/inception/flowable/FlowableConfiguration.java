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

package digital.inception.flowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.util.ServiceUtil;
import digital.inception.jpa.JpaUtil;
import digital.inception.flowable.flowable.FormEngineConfiguration;
import digital.inception.flowable.flowable.FormEngineConfigurator;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import javax.sql.DataSource;
import org.flowable.common.engine.impl.AbstractEngineConfiguration;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The <b>WorkflowConfiguration</b> class provides the Spring configuration for the Workflow module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "flowableEntityManagerFactory",
    basePackages = {"digital.inception.flowable"})
public class FlowableConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The data source used to provide connections to the application database. */
  private final DataSource dataSource;

  /* The name of the DMN Engine instance. */
  private final String dmnEngineName = ServiceUtil.getServiceInstanceName("DmnEngine");

  /* The name of the Process Engine instance. */
  private final String processEngineName = ServiceUtil.getServiceInstanceName("ProcessEngine");

  /* The name of the App Engine instance. */
  private final String appEngineName = ServiceUtil.getServiceInstanceName("AppEngine");

  /** The Spring platform transaction manager. */
  private final PlatformTransactionManager transactionManager;

  /** The Jackson ObjectMapper. */
  private ObjectMapper objectMapper;


  /**
   * Constructs a new <b>FlowableConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   * @param dataSource the data source used to provide connections to the application database
   * @param transactionManager the Spring platform transaction manager
   * @param objectMapper the Jackson ObjectMapper
   */
  public FlowableConfiguration(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource,
      PlatformTransactionManager transactionManager,
      ObjectMapper objectMapper) {
    this.applicationContext = applicationContext;
    this.dataSource = dataSource;
    this.transactionManager = transactionManager;
    this.objectMapper = objectMapper;
  }

  private String getDatabaseType(DataSource dataSource) {
    try (Connection connection = dataSource.getConnection()) {
      DatabaseMetaData metaData = connection.getMetaData();

      return switch (metaData.getDatabaseProductName()) {
        case "H2" -> AbstractEngineConfiguration.DATABASE_TYPE_H2;
        case "Microsoft SQL Server" -> AbstractEngineConfiguration.DATABASE_TYPE_MSSQL;
        case "Oracle" -> AbstractEngineConfiguration.DATABASE_TYPE_ORACLE;
        case "PostgreSQL" -> AbstractEngineConfiguration.DATABASE_TYPE_POSTGRES;
        default -> throw new RuntimeException(
            "Failed to set the database type using the unknown database product name ("
                + metaData.getDatabaseProductName() + ")");
      };
    }
    catch (Throwable e) {
      throw new RuntimeException("Failed to determine the database type", e);
    }
  }













  /**
   * Returns the Flowable process engine.
   *
   * @return the Flowable process engine
   */
  @Bean
  public ProcessEngine processEngineConfiguration() {
    try {
      ProcessEngineConfiguration processEngineConfiguration =
          ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
      processEngineConfiguration.setEngineName(processEngineName);
      processEngineConfiguration.setObjectMapper(objectMapper);
      processEngineConfiguration.setDataSource(dataSource);
      // processEngineConfiguration.setTransactionManager(transactionManager);
      processEngineConfiguration.setDatabaseType(getDatabaseType(dataSource));

      /*
       * Prevent Flowable from updating the database schemas, since we manage the database updates
       * through the Inception Framework. Validation will still be performed.
       */
      processEngineConfiguration.setDatabaseSchemaUpdate(
          ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);

      processEngineConfiguration.setDbHistoryUsed(true);
      //processEngineConfiguration.setDisableIdmEngine(true);
      //processEngineConfiguration.setDisableEventRegistry(true);
      //processEngineConfiguration.setAppResourceConverter(
      //    new AppResourceConverterImpl(objectMapper));



      processEngineConfiguration.setCreateDiagramOnDeploy(true);


//      DmnEngineConfigurator dmnEngineConfigurator = new DmnEngineConfigurator();
//      dmnEngineConfigurator.setDmnEngineConfiguration(dmnEngineConfiguration);
//      processEngineConfiguration.addConfigurator(dmnEngineConfigurator);

//      EventRegistryEngineConfigurator eventRegistryEngineConfigurator = new EventRegistryEngineConfigurator();
//      eventRegistryEngineConfigurator.setEventEngineConfiguration(eventRegistryEngineConfiguration);
//      processEngineConfiguration.addConfigurator(eventRegistryEngineConfigurator);

      FormEngineConfiguration formEngineConfiguration = new FormEngineConfiguration();



      FormEngineConfigurator formEngineConfigurator = new FormEngineConfigurator();
      formEngineConfigurator.setFormEngineConfiguration(formEngineConfiguration);
      processEngineConfiguration.addConfigurator(formEngineConfigurator);





//      processEngineConfiguration.setEventSubscriptionSchemaManager(new EventSubscriptionDbSchemaManager());




      ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();



      return processEngine;
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialise the Flowable process engine", e);
    }
  }

//  /**
//   * Returns the Flowable app engine configuration.
//   *
//   * @return the Flowable app engine configuration
//   */
//  @Bean
//  public AppEngineConfiguration appEngineConfiguration() {
//    SpringAppEngineConfiguration appEngineConfiguration = new SpringAppEngineConfiguration();
//    appEngineConfiguration.setApplicationContext(applicationContext);
//    appEngineConfiguration.setAppEngineName(appEngineName);
//    appEngineConfiguration.setDataSource(dataSource);
//    appEngineConfiguration.setTransactionManager(transactionManager);
//    appEngineConfiguration.setDatabaseType(getDatabaseType(dataSource));
//    appEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
//    appEngineConfiguration.setDbHistoryUsed(true);
//    appEngineConfiguration.setDisableIdmEngine(true);
//    appEngineConfiguration.setDisableEventRegistry(true);
//    appEngineConfiguration.setAppResourceConverter(new org.flowable.app.engine.impl.deployer.AppResourceConverterImpl(objectMapper));
//    appEngineConfiguration.setSchemaManager(new AppDbSchemaManager());
//
//    return appEngineConfiguration;
//  }

  /**
   * Returns the flowable entity manager factory bean associated with the application data source.
   *
   * @param dataSource the application data source
   * @param platformTransactionManager the platform transaction manager
   * @return the flowable entity manager factory bean associated with the application data source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean flowableEntityManagerFactory(
      @Qualifier("applicationDataSource") DataSource dataSource,
      PlatformTransactionManager platformTransactionManager) {
    return JpaUtil.createEntityManager(
        "flowable", dataSource, platformTransactionManager, "digital.inception.flowable");
  }

//  /**
//   * Returns the Flowable event registry engine configuration.
//   *
//   * @return the Flowable event registry engine configuration
//   */
//  @Bean
//  public EventRegistryEngineConfiguration eventRegistryEngineConfiguration() {
//    SpringEventRegistryEngineConfiguration eventRegistryEngineConfiguration = new SpringEventRegistryEngineConfiguration();
//
//    eventRegistryEngineConfiguration.setApplicationContext(applicationContext);
//    eventRegistryEngineConfiguration.setEngineName(dmnEngineName);
//    eventRegistryEngineConfiguration.setDatabaseType(getDatabaseType(dataSource));
//    eventRegistryEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
//    eventRegistryEngineConfiguration.setDataSource(dataSource);
//    eventRegistryEngineConfiguration.setTransactionManager(transactionManager);
//    eventRegistryEngineConfiguration.setDbHistoryUsed(true);
//    eventRegistryEngineConfiguration.setSchemaManager(new EventDbSchemaManager());
//
//    return eventRegistryEngineConfiguration;
//  }

//  /**
//   * Returns the Flowable DMN engine configuration.
//   *
//   * @return the Flowable DMN engine configuration
//   */
//  @Bean
//  public DmnEngineConfiguration dmnEngineConfiguration() {
//    SpringDmnEngineConfiguration dmnEngineConfiguration = new SpringDmnEngineConfiguration();
//    dmnEngineConfiguration.setApplicationContext(applicationContext);
//    dmnEngineConfiguration.setEngineName(dmnEngineName);
//    dmnEngineConfiguration.setDatabaseType(getDatabaseType(dataSource));
//    dmnEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
//    dmnEngineConfiguration.setDataSource(dataSource);
//    dmnEngineConfiguration.setTransactionManager(transactionManager);
//    dmnEngineConfiguration.setDbHistoryUsed(true);
//    dmnEngineConfiguration.setSchemaManager(new DmnDbSchemaManager());
//
//    return dmnEngineConfiguration;
//  }

//  /**
//   * Returns the form engine configuration.
//   *
//   * @return the form engine configuration
//   */
//  protected FormEngineConfiguration formEngineConfiguration() {
//    return new FormEngineConfiguration();
//  }

  private DataSource unwrapDataSource(DataSource dataSource) {
    try {
      dataSource = dataSource.unwrap(DataSource.class);

      if (dataSource.getClass().getName().equals("io.agroal.pool.DataSource")) {
        Field connectionPoolField = dataSource.getClass().getDeclaredField("connectionPool");
        connectionPoolField.setAccessible(true);

        Object connectionPool = connectionPoolField.get(dataSource);

        if (connectionPool != null) {
          Field connectionFactoryField =
              connectionPool.getClass().getDeclaredField("connectionFactory");
          connectionFactoryField.setAccessible(true);

          Object connectionFactory = connectionFactoryField.get(connectionPool);

          Field dataSourceField = connectionFactory.getClass().getDeclaredField("dataSource");
          dataSourceField.setAccessible(true);
          Object connectionFactoryDataSource = dataSourceField.get(connectionFactory);

          if (connectionFactoryDataSource != null) {
            if (DataSource.class.isAssignableFrom(connectionFactoryDataSource.getClass())) {
              return (DataSource) connectionFactoryDataSource;
            }
          }

          Field xaDataSourceField = connectionFactory.getClass().getDeclaredField("xaDataSource");
          xaDataSourceField.setAccessible(true);
          Object connectionFactoryXaDataSource = xaDataSourceField.get(connectionFactory);

          if (connectionFactoryXaDataSource != null) {
            if (DataSource.class.isAssignableFrom(connectionFactoryXaDataSource.getClass())) {
              return (DataSource) connectionFactoryXaDataSource;
            }
          }
        }
      }

      return dataSource;
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to unwrap the data source (" + dataSource.getClass().getName() + ")", e);
    }
  }
}

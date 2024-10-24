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

package digital.inception.workflow;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.util.ServiceUtil;
import digital.inception.jpa.JpaUtil;
import digital.inception.workflow.flowable.DmnDbSchemaManager;
import digital.inception.workflow.flowable.FormEngineConfiguration;
import digital.inception.workflow.flowable.FormEngineConfigurator;
import digital.inception.workflow.flowable.FormService;
import java.lang.reflect.Field;
import javax.sql.DataSource;
import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.db.SchemaManager;
import org.flowable.dmn.engine.DmnEngineConfiguration;
import org.flowable.dmn.engine.configurator.DmnEngineConfigurator;

import org.flowable.dmn.spring.SpringDmnEngineConfiguration;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.app.AppResourceConverterImpl;
import org.flowable.spring.SpringProcessEngineConfiguration;
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
    entityManagerFactoryRef = "workflowEntityManagerFactory",
    basePackages = {"digital.inception.workflow"})
public class WorkflowConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The data source used to provide connections to the application database. */
  private final DataSource dataSource;

  /* The name of the DMN Engine instance. */
  private final String dmnEngineName = ServiceUtil.getServiceInstanceName("DmnEngine");

  /* The name of the Process Engine instance. */
  private final String processEngineName = ServiceUtil.getServiceInstanceName("ProcessEngine");

  /** The Spring platform transaction manager. */
  private final PlatformTransactionManager transactionManager;

  /**
   * Constructs a new <b>ProcessConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   * @param dataSource the data source used to provide connections to the application database
   * @param transactionManager the Spring platform transaction manager
   */
  public WorkflowConfiguration(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource,
      PlatformTransactionManager transactionManager) {
    this.applicationContext = applicationContext;
    this.dataSource = dataSource;
    this.transactionManager = transactionManager;
  }

  /**
   * Returns the Flowable Process Engine.
   *
   * @param formService the Forms Service
   * @return the Flowable Process Engine
   */
  @Bean
  public ProcessEngine processEngine(FormService formService) {
    try {
      ObjectMapper objectMapper =
          new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      // DataSource dataSource = unwrapDataSource(this.dataSource);

      SpringProcessEngineConfiguration processEngineConfiguration =
          new SpringProcessEngineConfiguration();
      processEngineConfiguration.setApplicationContext(applicationContext);
      processEngineConfiguration.setEngineName(processEngineName);
      // TODO: Only do this if we detect an H2 database running in Postgres mode -- MARCUS
      processEngineConfiguration.setDatabaseType(
          AbstractEngineConfiguration.DATABASE_TYPE_POSTGRES);

//      processEngineConfiguration.setDatabaseSchemaUpdate(
//          ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);


//      processEngineConfiguration.setDatabaseSchemaUpdate(
//          ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);
      processEngineConfiguration.setDataSource(dataSource);
      processEngineConfiguration.setTransactionManager(transactionManager);
      processEngineConfiguration.setDbHistoryUsed(true);
      processEngineConfiguration.setDisableIdmEngine(true);
      processEngineConfiguration.setDisableEventRegistry(true);
      processEngineConfiguration.setAppResourceConverter(
          new AppResourceConverterImpl(objectMapper));

      // Enable and disable specific capabilities
      // processEngineConfiguration.addConfigurator(new FormEngineConfigurator());

      //        Map<String, org.flowable.engine.impl.form.FormEngine> formEngines = new HashMap<>();
      //        formEngines.put("Inception", new FormEngine());
      //
      //
      //
      //
      //        processEngineConfiguration.setFormEngines(formEngines);
      //        processEngineConfiguration.setFormService(new FormServiceImpl());

      DmnEngineConfigurator dmnEngineConfigurator = new DmnEngineConfigurator();
      dmnEngineConfigurator.setDmnEngineConfiguration(dmnEngineConfiguration());
      processEngineConfiguration.addConfigurator(dmnEngineConfigurator);

      FormEngineConfigurator formEngineConfigurator = new FormEngineConfigurator();
      formEngineConfigurator.setFormEngineConfiguration(formEngineConfiguration());
      processEngineConfiguration.addConfigurator(formEngineConfigurator);

      // Enable and disable specific capabilities
      // TODO: REMOVE CAMUNDA processEngineConfiguration.setAuthorizationEnabled(false);
      // TODO: REMOVE CAMUNDA processEngineConfiguration.setCmmnEnabled(true);
      // TODO: REMOVE CAMUNDA processEngineConfiguration.setDbIdentityUsed(false);
      // TODO: REMOVE CAMUNDA processEngineConfiguration.setDmnEnabled(true);
      // TODO: CONVERT CAMUNDA
      // processEngineConfiguration.setHistory(ProcessEngineConfiguration.HISTORY_DEFAULT);

      return processEngineConfiguration.buildProcessEngine();
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialise the Flowable Process Engine", e);
    }
  }

  /**
   * Returns the workflow entity manager factory bean associated with the application data source.
   *
   * @param dataSource the application data source
   * @param platformTransactionManager the platform transaction manager
   * @return the workflow entity manager factory bean associated with the application data source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean workflowEntityManagerFactory(
      @Qualifier("applicationDataSource") DataSource dataSource,
      PlatformTransactionManager platformTransactionManager) {
    return JpaUtil.createEntityManager(
        "workflow", dataSource, platformTransactionManager, "digital.inception.workflow");
  }

  /**
   * Returns the DMN engine configuration.
   *
   * @return the DMN engine configuration
   */
  protected DmnEngineConfiguration dmnEngineConfiguration() {
    SpringDmnEngineConfiguration dmnEngineConfiguration = new SpringDmnEngineConfiguration();
    dmnEngineConfiguration.setApplicationContext(applicationContext);
    dmnEngineConfiguration.setEngineName(dmnEngineName);
    // TODO: Only do this if we detect an H2 database running in Postgres mode -- MARCUS
    dmnEngineConfiguration.setDatabaseType(AbstractEngineConfiguration.DATABASE_TYPE_POSTGRES);
    // dmnEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);
    dmnEngineConfiguration.setDataSource(dataSource);
    dmnEngineConfiguration.setTransactionManager(transactionManager);
    dmnEngineConfiguration.setDbHistoryUsed(true);
    dmnEngineConfiguration.setSchemaManager(new DmnDbSchemaManager());

    //dmnEngineConfiguration.setDeploymentMode();

    return dmnEngineConfiguration;
  }





  /**
   * Returns the form engine configuration.
   *
   * @return the form engine configuration
   */
  protected FormEngineConfiguration formEngineConfiguration() {
    return new FormEngineConfiguration();
  }

  //
  //  /**
  //   * Returns the Flowable DMN Engine.
  //   *
  //   * @return the Flowable DMN Engine
  //   */
  //  @Bean
  //  public DmnEngine dmnEngine() {
  //    try {
  //      SpringDmnEngineConfiguration dmnEngineConfiguration = new SpringDmnEngineConfiguration();
  //      dmnEngineConfiguration.setApplicationContext(applicationContext);
  //      // TODO: Only do this if we detect an H2 database running in Postgres mode -- MARCUS
  //
  // dmnEngineConfiguration.setDatabaseType(AbstractEngineConfiguration.DATABASE_TYPE_POSTGRES);
  //
  // dmnEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);
  //      dmnEngineConfiguration.setDataSource(dataSource);
  //      dmnEngineConfiguration.setTransactionManager(transactionManager);
  //      dmnEngineConfiguration.setEngineName(dmnEngineName);
  //      dmnEngineConfiguration.setDbHistoryUsed(true);
  //
  //      return dmnEngineConfiguration.buildDmnEngine();
  //    } catch (Throwable e) {
  //      throw new FatalBeanException("Failed to initialise the Flowable DMN Engine", e);
  //    }
  //  }

  //  /**
  //   * Returns the <b>Jackson2ObjectMapperBuilder</b> bean, which configures the Jackson JSON
  //   * processor package.
  //   *
  //   * @return the <b>Jackson2ObjectMapperBuilder</b> bean, which configures the Jackson JSON
  //   *     processor package
  //   */
  //  protected Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
  //    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
  //    jackson2ObjectMapperBuilder.indentOutput(true);
  //
  //    /*
  //     * Install the custom Jackson module that supports serializing and de-serializing ISO 8601
  // date
  //     * and date/time values. The jackson-datatype-jsr310 module provided by Jackson was not used
  // as
  //     * it does not handle timezones correctly for LocalDateTime, OffsetDateTime or Instant
  // objects.
  //     */
  //    jackson2ObjectMapperBuilder.modulesToInstall(new DateTimeModule());
  //
  //    return jackson2ObjectMapperBuilder;
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

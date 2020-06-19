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

package digital.inception.bmi;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ServiceUtil;
import javax.sql.DataSource;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BMIConfiguration</code> class provides the Spring configuration for the Business
 * Modeling and Integration (BMI) module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "applicationPersistenceUnit",
    basePackages = {"digital.inception.bmi"})
public class BMIConfiguration {

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The data source used to provide connections to the application database.
   */
  private DataSource dataSource;

  /* The name of the Process Engine instance. */
  private String processEngineName = ServiceUtil.getServiceInstanceName("ProcessEngine");

  /**
   * The Spring platform transaction manager.
   */
  private PlatformTransactionManager transactionManager;

  /**
   * Constructs a new <code>ProcessConfiguration</code>.
   *
   * @param applicationContext the Spring application context
   * @param dataSource         the data source used to provide connections to the application
   *                           database
   * @param transactionManager the Spring platform transaction manager
   */
  public BMIConfiguration(ApplicationContext applicationContext, @Qualifier(
      "applicationDataSource") DataSource dataSource,
      PlatformTransactionManager transactionManager) {
    this.applicationContext = applicationContext;
    this.dataSource = dataSource;
    this.transactionManager = transactionManager;
  }

  /**
   * Returns the Camunda Process Engine.
   *
   * @return the Camunda Process Engine
   */
  @Bean
  public ProcessEngine processEngine() {
    try {
      SpringProcessEngineConfiguration processEngineConfiguration =
          new SpringProcessEngineConfiguration();
      processEngineConfiguration.setApplicationContext(applicationContext);
      processEngineConfiguration.setCmmnEnabled(true);
      processEngineConfiguration.setDatabaseSchema("CAMUNDA");
      processEngineConfiguration
          .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
      processEngineConfiguration.setDatabaseTablePrefix("CAMUNDA.");
      processEngineConfiguration.setDataSource(dataSource);
      processEngineConfiguration.setJobExecutorActivate(true);
      processEngineConfiguration.setTransactionManager(transactionManager);

      // Disable specific capabilities
      processEngineConfiguration.setAuthorizationEnabled(false);
      processEngineConfiguration.setDbHistoryUsed(false);
      processEngineConfiguration.setDbIdentityUsed(false);
      processEngineConfiguration.setDmnEnabled(false);
      processEngineConfiguration.setHistory(ProcessEngineConfiguration.HISTORY_NONE);

      return processEngineConfiguration.buildProcessEngine();
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialise the Flowable Process Engine", e);
    }
  }
}

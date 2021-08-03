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

package digital.inception.bmi;

import digital.inception.core.util.ServiceUtil;
import java.util.Map;
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
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * The <b>BMIConfiguration</b> class provides the Spring configuration for the Business Modeling and
 * Integration (BMI) module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "bmiEntityManagerFactory",
    basePackages = {"digital.inception.bmi"})
public class BMIConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The data source used to provide connections to the application database. */
  private final DataSource dataSource;

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
  public BMIConfiguration(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource,
      PlatformTransactionManager transactionManager) {
    this.applicationContext = applicationContext;
    this.dataSource = dataSource;
    this.transactionManager = transactionManager;
  }

  /**
   * Returns the bmi entity manager factory bean associated with the application data source.
   *
   * @return the bmi entity manager factory bean associated with the application data source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean bmiEntityManagerFactory(
      @Qualifier("applicationDataSource") DataSource dataSource,
      JpaVendorAdapter jpaVendorAdapter,
      PlatformTransactionManager platformTransactionManager) {
    try {
      LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
          new LocalContainerEntityManagerFactoryBean();

      entityManagerFactoryBean.setPersistenceUnitName("bmi");

      entityManagerFactoryBean.setJtaDataSource(dataSource);

      entityManagerFactoryBean.setPackagesToScan("digital.inception.bmi");

      entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

      Map<String, Object> jpaPropertyMap = entityManagerFactoryBean.getJpaPropertyMap();

      if (platformTransactionManager instanceof JtaTransactionManager) {
        jpaPropertyMap.put("hibernate.transaction.coordinator_class", "jta");
        jpaPropertyMap.put("hibernate.transaction.jta.platform", "JBossTS");
      }

      return entityManagerFactoryBean;
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialize the bmi entity manager factory bean", e);
    }
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
      processEngineConfiguration.setDatabaseSchemaUpdate(
          ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
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

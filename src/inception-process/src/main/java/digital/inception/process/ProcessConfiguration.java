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

package digital.inception.process;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ServiceUtil;
import digital.inception.process.camunda.ProcessEngineConfiguration;
import digital.inception.security.ISecurityService;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

//~--- JDK imports ------------------------------------------------------------

import javax.sql.DataSource;

/**
 * The <code>ProcessConfiguration</code> class provides the Spring configuration
 * for the Process module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "applicationPersistenceUnit",
    basePackages = { "digital.inception.process" })
public class ProcessConfiguration
{
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
   * The Security Service.
   */
  private ISecurityService securityService;

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
   * @param securityService    the Security Service
   */
  public ProcessConfiguration(ApplicationContext applicationContext, @Qualifier(
      "applicationDataSource") DataSource dataSource,
      PlatformTransactionManager transactionManager, ISecurityService securityService)
  {
    this.applicationContext = applicationContext;
    this.dataSource = dataSource;
    this.securityService = securityService;
    this.transactionManager = transactionManager;
  }

  /**
   * Returns the Camunda Process Engine.
   *
   * @return the Camunda Process Engine
   */
  @Bean
  public ProcessEngine processEngine()
  {
    try
    {
      digital.inception.process.camunda.ProcessEngineConfiguration processEngineConfiguration =
          new ProcessEngineConfiguration(processEngineName, applicationContext, dataSource,
          transactionManager, securityService);

      ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
      factoryBean.setProcessEngineConfiguration(processEngineConfiguration);

      return factoryBean.getObject();
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialise the Camunda Process Engine", e);
    }
  }

///**
// * Return the History Service for the Camunda Process Engine.
// *
// * @return the History Service for the Camunda Process Engine
// */
//@Bean
//@DependsOn("processEngine")
//public HistoryService processEngineHistoryService()
//{
//  return processEngine().getHistoryService();
//}
//
///**
// * Return the Management Service for the Camunda Process Engine.
// *
// * @return the Management Service for the Camunda Process Engine
// */
//@Bean
//@DependsOn("processEngine")
//public ManagementService processEngineManagementService()
//{
//  return processEngine().getManagementService();
//}
//
///**
// * Return the Repository Service for the Camunda Process Engine.
// *
// * @return the Repository Service for the Camunda Process Engine
// */
//@Bean
//@DependsOn("processEngine")
//public RepositoryService processEngineRepositoryService()
//{
//  return processEngine().getRepositoryService();
//}
//
///**
// * Return the Runtime Service for the Camunda Process Engine.
// *
// * @return the Runtime Service for the Camunda Process Engine
// */
//@Bean
//@DependsOn("processEngine")
//public RuntimeService processEngineRuntimeService()
//{
//  return processEngine().getRuntimeService();
//}
//
///**
// * Return the Task Service for the Camunda Process Engine.
// *
// * @return the Task Service for the Camunda Process Engine
// */
//@Bean
//@DependsOn("processEngine")
//public TaskService processEngineTaskService()
//{
//  return processEngine().getTaskService();
//}
}

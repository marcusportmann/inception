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

import digital.inception.core.util.ResourceUtil;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

/**
 * The <code>ProcessServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>ProcessService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ProcessServiceTest
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ProcessServiceTest.class);

  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  DataSource dataSource;

  /**
   * The Camunda Process Engine.
   */
  @Autowired
  private ProcessEngine processEngine;

  /**
   * The Process Service.
   */
  @Autowired
  private IProcessService processService;

  /**
   * Check database test.
   */
  @Test
  public void checkDatabaseTest()
    throws Exception
  {
    try (Connection connection = dataSource.getConnection())
    {
      DatabaseMetaData metaData = connection.getMetaData();

      try (ResultSet schemasResultSet = metaData.getSchemas())
      {
        while (schemasResultSet.next())
        {
          String catalog = schemasResultSet.getString(2);
          String schema = schemasResultSet.getString(1);

          System.out.println(catalog + " - " + schema);

          try (ResultSet tablesResultSet = metaData.getTables(catalog, schema, "%", null))
          {
            while (tablesResultSet.next())
            {
              System.out.println("  " + tablesResultSet.getString(3));
            }
          }
        }
      }
    }
  }

  /**
   * Test the process engine.
   */
  @Test
  public void processEngineTest()
    throws Exception
  {
    byte[] testProcessV1Data = ResourceUtil.getClasspathResource(
        "digital/inception/process/TestV1.bpmn");
    byte[] testProcessV2Data = ResourceUtil.getClasspathResource(
        "digital/inception/process/TestV2.bpmn");

    UUID testProcessId = UUID.randomUUID();

    DeploymentBuilder processDeploymentV1 = processEngine.getRepositoryService().createDeployment();
    processDeploymentV1.addInputStream(testProcessId.toString() + ".bpmn20.xml",
        new ByteArrayInputStream(testProcessV1Data));

    Deployment deploymentV1 = processDeploymentV1.deploy();

    DeploymentBuilder processDeploymentV2 = processEngine.getRepositoryService().createDeployment();
    processDeploymentV2.addInputStream(testProcessId.toString() + ".bpmn20.xml",
        new ByteArrayInputStream(testProcessV2Data));

    Deployment deploymentV2 = processDeploymentV2.deploy();

    List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService()
        .createProcessDefinitionQuery().latestVersion().list();

    // processEngine.getRuntimeService().

    // processEngine.getRepositoryService().get

//  String id = deployment.getId();
//
//  System.out.println("deployment.getId() = " + deployment.getId());

    processEngine.getRuntimeService().startProcessInstanceByKey("Process_14may5q");


    Thread.sleep(30000L);

    // processEngine.getRuntimeService().startProcessInstanceById()

  }
}

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

import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.JobQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
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
import org.testng.Assert;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;

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

    List<String> testProcessV1Ids = processService.validateBPMN(testProcessV1Data);

    assertEquals("The correct number of process IDs was not retrieved for version 1 of the test process", 1, testProcessV1Ids.size());

    assertEquals("The correct process ID was not retrieved for version 1 of the test process", "Inception.Test", testProcessV1Ids.get(0));

    byte[] testProcessV2Data = ResourceUtil.getClasspathResource(
        "digital/inception/process/TestV2.bpmn");

    List<String> testProcessV2Ids = processService.validateBPMN(testProcessV2Data);

    assertEquals("The correct number of process IDs was not retrieved for version 2 of the test process", 1, testProcessV2Ids.size());

    assertEquals("The correct process ID was not retrieved for version 1 of the test process", "Inception.Test", testProcessV2Ids.get(0));

    DeploymentBuilder processDeploymentV1 = processEngine.getRepositoryService().createDeployment();
    processDeploymentV1.addInputStream(testProcessV1Ids.get(0) + ".bpmn20.xml",
        new ByteArrayInputStream(testProcessV1Data));

    Deployment deploymentV1 = processDeploymentV1.deploy();

    List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService()
        .createProcessDefinitionQuery().latestVersion().list();

    processEngine.getRuntimeService().startProcessInstanceByKey("Inception.Test");

    /*
     * Version 1 of the test process starts asynchronously so we need to ensure it starts executing
     * by executing the associated job.
     */
    JobQuery jobQuery = processEngine.getManagementService().createJobQuery();

    List<Job> jobs = jobQuery.list();

    for (Job job : jobs)
    {
      processEngine.getManagementService().executeJob(job.getId());
    }

    // Retrieve the list of running process instances
    ProcessInstanceQuery processInstanceQuery = processEngine.getRuntimeService().createProcessInstanceQuery();
    processInstanceQuery.processDefinitionKey(testProcessV1Ids.get(0));
    List<ProcessInstance> processInstances = processInstanceQuery.list();

    // Retrieve the tasks for the Administrators group
    TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
    taskQuery.taskCandidateGroup("Administrators");
    List<Task> tasks = taskQuery.list();

    if (tasks.size() > 0)
    {
      processEngine.getTaskService().claim(tasks.get(0).getId(), "Administrator");

      processEngine.getTaskService().complete(tasks.get(0).getId());
    }





    int xxx = 0;
    xxx++;




//    DeploymentBuilder processDeploymentV2 = processEngine.getRepositoryService().createDeployment();
//    processDeploymentV2.addInputStream(testProcessId.toString() + ".bpmn20.xml",
//        new ByteArrayInputStream(testProcessV2Data));
//
//    Deployment deploymentV2 = processDeploymentV2.deploy();



//    Thread.sleep(10000L);




    // processEngine.getRuntimeService().startProcessInstanceById()

  }
}

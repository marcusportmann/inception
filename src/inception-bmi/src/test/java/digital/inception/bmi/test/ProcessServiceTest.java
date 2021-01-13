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

package digital.inception.bmi.test;

// ~--- non-JDK imports --------------------------------------------------------

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import digital.inception.bmi.CaseDefinitionSummary;
import digital.inception.bmi.ICaseService;
import digital.inception.bmi.IProcessService;
import digital.inception.bmi.ProcessDefinitionSummary;
import digital.inception.core.util.ResourceUtil;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.repository.CaseDefinitionQuery;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.CaseExecution;
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

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ProcessServiceTest</code> class contains the implementation of the JUnit tests for the
 * <code>ProcessService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ProcessServiceTest {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ProcessServiceTest.class);

  /** The Case Service. */
  @Autowired private ICaseService caseService;

  /** The data source used to provide connections to the application database. */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /** The Camunda Process Engine. */
  @Autowired private ProcessEngine processEngine;

  /** The Process Service. */
  @Autowired private IProcessService processService;

  /** Test the case definition functionality. */
  @Test
  public void caseDefinitionTest() throws Exception {
    byte[] testCaseData = ResourceUtil.getClasspathResource("digital/inception/bmi/test/Test.cmmn");

    List<CaseDefinitionSummary> caseDefinitionSummaries =
        caseService.createCaseDefinition(testCaseData);

    caseDefinitionSummaries = caseService.updateCaseDefinition(testCaseData);

    int xxx = 0;
    xxx++;
  }

  /** Check database test. */
  // @Test
  public void checkDatabaseTest() throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      DatabaseMetaData metaData = connection.getMetaData();

      try (ResultSet schemasResultSet = metaData.getSchemas()) {
        while (schemasResultSet.next()) {
          String catalog = schemasResultSet.getString(2);
          String schema = schemasResultSet.getString(1);

          System.out.println(catalog + " - " + schema);

          try (ResultSet tablesResultSet = metaData.getTables(catalog, schema, "%", null)) {
            while (tablesResultSet.next()) {
              System.out.println("  " + tablesResultSet.getString(3));
            }
          }
        }
      }
    }
  }

  /** Test the process definition functionality. */
  @Test
  public void processDefinitionTest() throws Exception {
    byte[] testProcessV1Data =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestV1.bpmn");

    List<ProcessDefinitionSummary> processDefinitionSummaries =
        processService.createProcessDefinition(testProcessV1Data);

    assertEquals(
        "The correct number of process definitions was not retrieved for version 1 of the Inception.Test process definition",
        1,
        processDefinitionSummaries.size());

    assertEquals(
        "The correct process definition ID was not retrieved for version 1 of the Inception.Test process definition",
        "Inception.Test",
        processDefinitionSummaries.get(0).getId());

    byte[] testProcessV2Data =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestV2.bpmn");

    processDefinitionSummaries = processService.updateProcessDefinition(testProcessV2Data);

    assertEquals(
        "The correct number of process definitions was not retrieved for version 2 of the Inception.Test process definition",
        1,
        processDefinitionSummaries.size());

    assertEquals(
        "The correct process definition ID was not retrieved for version 2 of the Inception.Test process definition",
        "Inception.Test",
        processDefinitionSummaries.get(0).getId());

    List<ProcessDefinitionSummary> retrievedProcessDefinitionSummaries =
        processService.getProcessDefinitionSummaries();

    boolean foundProcessDefinition = false;

    for (ProcessDefinitionSummary processDefinitionSummary : retrievedProcessDefinitionSummaries) {
      if (processDefinitionSummary.getId().equals("Inception.Test")) {
        foundProcessDefinition = true;

        break;
      }
    }

    if (!foundProcessDefinition) {
      fail("Failed to retrieve the summary for the process definition (Inception.Test)");
    }

    //    Map<String, Object> parameters = new HashMap<>();
    //
    //    processService.startProcessInstance("Inception.Test", parameters);

  }

  /** Test the process engine. */

  // @Test
  public void processEngineTest() throws Exception {
    byte[] testProcessV1Data =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestV1.bpmn");

    List<ProcessDefinitionSummary> processDefinitionSummaries =
        processService.validateBPMN(testProcessV1Data);

    assertEquals(
        "The correct number of process definitions was not retrieved for version 1 of the Inception.Test process definition",
        1,
        processDefinitionSummaries.size());

    assertEquals(
        "The correct process definition ID was not retrieved for version 1 of the Inception.Test process definition",
        "Inception.Test",
        processDefinitionSummaries.get(0).getId());

    byte[] testProcessV2Data =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestV2.bpmn");

    processDefinitionSummaries = processService.validateBPMN(testProcessV2Data);

    assertEquals(
        "The correct number of process definitions was not retrieved for version 2 of the Inception.Test process definition",
        1,
        processDefinitionSummaries.size());

    assertEquals(
        "The correct process definition ID was not retrieved for version 1 of the Inception.Test process",
        "Inception.Test",
        processDefinitionSummaries.get(0).getId());

    DeploymentBuilder processDeploymentV1 = processEngine.getRepositoryService().createDeployment();
    processDeploymentV1.addInputStream(
        processDefinitionSummaries.get(0).getId() + ".bpmn",
        new ByteArrayInputStream(testProcessV2Data));

    Deployment deploymentV1 = processDeploymentV1.deploy();

    List<ProcessDefinition> processDefinitions =
        processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().list();

    boolean foundProcessDefinition = false;

    for (ProcessDefinition processDefinition : processDefinitions) {
      if (processDefinition.getKey().equals("Inception.Test")) {
        foundProcessDefinition = true;

        break;
      }
    }

    if (!foundProcessDefinition) {
      fail("Failed to find the process definition (Inception.Test)");
    }

    processEngine.getRuntimeService().startProcessInstanceByKey("Inception.Test");

    /*
     * The test process starts asynchronously so we need to ensure it starts executing by executing
     * the associated job.
     */
    JobQuery jobQuery = processEngine.getManagementService().createJobQuery();

    List<Job> jobs = jobQuery.list();

    assertEquals(
        "Failed to find the job to asynchronously start the process instance", 1, jobs.size());

    for (Job job : jobs) {
      processEngine.getManagementService().executeJob(job.getId());
    }

    // Retrieve the list of running process instances
    ProcessInstanceQuery processInstanceQuery =
        processEngine.getRuntimeService().createProcessInstanceQuery();
    processInstanceQuery.processDefinitionKey(processDefinitionSummaries.get(0).getId());

    List<ProcessInstance> processInstances = processInstanceQuery.list();

    assertEquals("Failed to find the running process instance", 1, processInstances.size());

    // Retrieve the tasks for the Administrators group
    TaskQuery taskQuery =
        processEngine.getTaskService().createTaskQuery().taskCandidateGroup("Administrators");
    List<Task> tasks = taskQuery.list();

    assertEquals("Failed to find the task for the Administrators group", 1, tasks.size());

    String taskId = tasks.get(0).getId();

    processEngine.getTaskService().claim(taskId, "jack");

    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();

    assertEquals("Failed to find the task", 1, tasks.size());
    assertEquals(
        "Failed to confirm that the task has been claimed by jack",
        "jack",
        tasks.get(0).getAssignee());

    processEngine.getTaskService().delegateTask(tasks.get(0).getId(), "jill");

    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();

    assertEquals("Failed to find the task", 1, tasks.size());
    assertEquals(
        "Failed to confirm that the task has been assigned to jill",
        "jill",
        tasks.get(0).getAssignee());

    processEngine.getTaskService().resolveTask(tasks.get(0).getId());

    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();

    assertEquals("Failed to find the task", 1, tasks.size());
    assertEquals(
        "Failed to confirm that the resolved task has been re-assigned to jack",
        "jack",
        tasks.get(0).getAssignee());

    processEngine.getTaskService().setOwner(tasks.get(0).getId(), "jill");

    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();

    assertEquals("Failed to find the task", 1, tasks.size());
    assertEquals(
        "Failed to confirm that the task owner has been changed to to jill",
        "jill",
        tasks.get(0).getOwner());

    processEngine.getTaskService().complete(taskId);
  }

  /** Test the process with case functionality. */
  // @Test
  public void processWithCaseTest() throws Exception {
    byte[] testEmbeddedProcessData =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestEmbedded.bpmn");

    processService.createProcessDefinition(testEmbeddedProcessData);

    byte[] testWithCaseProcessData =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestWithCase.bpmn");

    processService.createProcessDefinition(testWithCaseProcessData);

    byte[] testCaseData = ResourceUtil.getClasspathResource("digital/inception/bmi/test/Test.cmmn");

    DeploymentBuilder testCaseDeploymentBuilder =
        processEngine.getRepositoryService().createDeployment();
    testCaseDeploymentBuilder.addInputStream("Test.cmmn", new ByteArrayInputStream(testCaseData));

    Deployment testCaseDeployment = testCaseDeploymentBuilder.deploy();

    CaseDefinitionQuery caseDefinitionQuery =
        processEngine.getRepositoryService().createCaseDefinitionQuery();

    List<CaseDefinition> caseDefinitions = caseDefinitionQuery.list();

    Map<String, Object> variables = new HashMap<>();

    variables.put("TestVariableName", "TestVariableValue");

    processEngine
        .getRuntimeService()
        .startProcessInstanceByKey("Inception.TestWithCase", variables);

    JobQuery jobQuery = processEngine.getManagementService().createJobQuery();

    List<Job> jobs = jobQuery.list();

    ProcessInstanceQuery processInstanceQuery =
        processEngine.getRuntimeService().createProcessInstanceQuery();
    processInstanceQuery.processDefinitionKey("Inception.TestWithCase");

    List<ProcessInstance> processInstances = processInstanceQuery.list();

    TaskQuery taskQuery =
        processEngine.getTaskService().createTaskQuery().taskCandidateGroup("Administrators");

    List<Task> tasks = taskQuery.list();

    List<CaseExecution> caseExecutions =
        processEngine.getCaseService().createCaseExecutionQuery().caseDefinitionKey("Test").list();

    for (CaseExecution caseExecution : caseExecutions) {
      boolean isActive = caseExecution.isActive();

      if (!isActive) {
        processEngine.getCaseService().manuallyStartCaseExecution(caseExecution.getId());
      }
    }

    taskQuery =
        processEngine.getTaskService().createTaskQuery().taskCandidateGroup("Administrators");

    tasks = taskQuery.list();

    int xxx = 0;
    xxx++;
  }
}

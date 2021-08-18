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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import digital.inception.bmi.IProcessService;
import digital.inception.bmi.ProcessDefinitionSummary;
import digital.inception.core.util.ResourceUtil;
import digital.inception.test.InceptionExtension;
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
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.JobQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>ProcessServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>ProcessService</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, ProcessServiceTestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ProcessServiceTest {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ProcessServiceTest.class);

  /** The data source used to provide connections to the application database. */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /** The Camunda Process Engine. */
  @Autowired private ProcessEngine processEngine;

  /** The Process Service. */
  @Autowired private IProcessService processService;

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
        1,
        processDefinitionSummaries.size(),
        "The correct number of process definitions was not retrieved for version 1 of the Inception.Test process definition");

    assertEquals(
        "Inception.Test",
        processDefinitionSummaries.get(0).getId(),
        "The correct process definition ID was not retrieved for version 1 of the Inception.Test process definition");

    byte[] testProcessV2Data =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestV2.bpmn");

    processDefinitionSummaries = processService.updateProcessDefinition(testProcessV2Data);

    assertEquals(
        1,
        processDefinitionSummaries.size(),
        "The correct number of process definitions was not retrieved for version 2 of the Inception.Test process definition");

    assertEquals(
        "Inception.Test",
        processDefinitionSummaries.get(0).getId(),
        "The correct process definition ID was not retrieved for version 2 of the Inception.Test process definition");

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

    processService.deleteProcessDefinition("Inception.Test");
  }

  /** Test the process engine. */
  @Test
  public void processEngineTest() throws Exception {
    byte[] testProcessV1DefinitionData =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestV1.bpmn");

    List<ProcessDefinitionSummary> processDefinitionSummaries =
        processService.validateBPMN(testProcessV1DefinitionData);

    assertEquals(
        1,
        processDefinitionSummaries.size(),
        "The correct number of process definitions was not retrieved for version 1 of the Inception.Test process definition");

    assertEquals(
        "Inception.Test",
        processDefinitionSummaries.get(0).getId(),
        "The correct process definition ID was not retrieved for version 1 of the Inception.Test process definition");

    byte[] testProcessV2DefinitionData =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestV2.bpmn");

    processService.createProcessDefinition(testProcessV1DefinitionData);

    processDefinitionSummaries = processService.validateBPMN(testProcessV2DefinitionData);

    assertEquals(
        1,
        processDefinitionSummaries.size(),
        "The correct number of process definitions was not retrieved for version 2 of the Inception.Test process definition");

    assertEquals(
        "Inception.Test",
        processDefinitionSummaries.get(0).getId(),
        "The correct process definition ID was not retrieved for version 1 of the Inception.Test process");

    processService.updateProcessDefinition(testProcessV2DefinitionData);

    byte[] testEmbeddedProcessDefinitionData =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestEmbedded.bpmn");

    processService.createProcessDefinition(testEmbeddedProcessDefinitionData);

    List<ProcessDefinition> processDefinitions =
        processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().list();

    assertEquals(2, processDefinitions.size(), "The correct number of process definitions was not retrieved");

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

    Map<String, Object> processInstanceParameters = new HashMap<>();

    processInstanceParameters.put("TestVariable", "MyTestVariable");

    processService.startProcessInstance("Inception.Test", processInstanceParameters);

    /*
     * The test process starts asynchronously, so we need to ensure it starts executing by executing
     * the associated job.
     */
    JobQuery jobQuery = processEngine.getManagementService().createJobQuery();

    List<Job> jobs = jobQuery.list();

    assertEquals(
        1, jobs.size(), "Failed to find the job to asynchronously start the process instance");

    for (Job job : jobs) {
      processEngine.getManagementService().executeJob(job.getId());
    }

    // Retrieve the list of running process instances
    ProcessInstanceQuery processInstanceQuery =
        processEngine.getRuntimeService().createProcessInstanceQuery();
    processInstanceQuery.processDefinitionKey(processDefinitionSummaries.get(0).getId());

    List<ProcessInstance> processInstances = processInstanceQuery.list();

    assertEquals(1, processInstances.size(), "Failed to find the running process instance");

    // Retrieve the tasks for the Administrators group
    TaskQuery taskQuery =
        processEngine.getTaskService().createTaskQuery().taskCandidateGroup("Administrators");
    List<Task> tasks = taskQuery.list();

    assertEquals(1, tasks.size(), "Failed to find the task for the Administrators group");

    String taskId = tasks.get(0).getId();

    processEngine.getTaskService().claim(taskId, "jack");

    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();

    assertEquals(1, tasks.size(), "Failed to find the task");
    assertEquals(
        "jack",
        tasks.get(0).getAssignee(),
        "Failed to confirm that the task has been claimed by jack");

    processEngine.getTaskService().delegateTask(tasks.get(0).getId(), "jill");

    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();

    assertEquals(1, tasks.size(), "Failed to find the task");
    assertEquals(
        "jill",
        tasks.get(0).getAssignee(),
        "Failed to confirm that the task has been assigned to jill");

    processEngine.getTaskService().resolveTask(tasks.get(0).getId());

    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();

    assertEquals(1, tasks.size(), "Failed to find the task");
    assertEquals(
        "jack",
        tasks.get(0).getAssignee(),
        "Failed to confirm that the resolved task has been re-assigned to jack");

    processEngine.getTaskService().setOwner(tasks.get(0).getId(), "jill");

    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();

    assertEquals(1, tasks.size(), "Failed to find the task");
    assertEquals(
        "jill",
        tasks.get(0).getOwner(),
        "Failed to confirm that the task owner has been changed to to jill");

    processEngine.getTaskService().complete(taskId);

    processService.deleteProcessDefinition("Inception.Test");
  }
}

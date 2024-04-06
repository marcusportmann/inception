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

package digital.inception.workflow.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import digital.inception.core.util.ResourceUtil;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import digital.inception.workflow.service.IProcessService;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import javax.sql.DataSource;
// import org.camunda.bpm.engine.ProcessEngine;
// import org.camunda.bpm.engine.form.TaskFormData;
// import org.camunda.bpm.engine.persistence.ProcessDefinition;
// import org.camunda.bpm.engine.runtime.Job;
// import org.camunda.bpm.engine.runtime.JobQuery;
// import org.camunda.bpm.engine.runtime.ProcessInstance;
// import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
// import org.camunda.bpm.engine.task.Task;
// import org.camunda.bpm.engine.task.TaskQuery;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.job.api.Job;
import org.flowable.job.api.JobQuery;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;

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

  @Autowired private PlatformTransactionManager platformTransactionManager;

  /** The Flowable Process Engine. */
  @Autowired private ProcessEngine processEngine;

  /** The Process Service. */
  @Autowired private IProcessService processService;

  // @Test
  @Transactional
  public void camundaProcessTest() throws Exception {

    byte[] bpmnData =
        ResourceUtil.getClasspathResource("digital/inception/workflow/test/camundaTest.bpmn");

    Deployment deployment =
        processEngine
            .getRepositoryService()
            .createDeployment()
            .addBytes("camundaTest.bpmn", bpmnData)
            .deploy();

    ProcessDefinitionQuery processDefinitionQuery =
        processEngine.getRepositoryService().createProcessDefinitionQuery();
    processDefinitionQuery.processDefinitionKey("camundaTest");

    long processDefinitionCount = processDefinitionQuery.count();

    List<ProcessDefinition> processDefinitions = processDefinitionQuery.list();

    Map<String, Object> parameters = new HashMap<>();

    ProcessInstance processInstance =
        processEngine.getRuntimeService().startProcessInstanceByKey("camundaTest", parameters);

    int xxx = 0;
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

  // @Test
  @Transactional
  public void flowableTest() throws Exception {

    byte[] deploymentData =
        ResourceUtil.getClasspathResource(
            "digital/inception/workflow/test/flowableWorkTest-bar.zip");

    Deployment deployment =
        processEngine
            .getRepositoryService()
            .createDeployment()
            .name("flowableWorkTest-bar.zip")
            .addZipInputStream(new ZipInputStream(new ByteArrayInputStream(deploymentData)))
            .deploy();

    JdbcTemplate jdbcTemplate =
        new JdbcTemplate(processEngine.getProcessEngineConfiguration().getDataSource());

    jdbcTemplate.query(
        "SELECT * FROM ACT_RE_PROCDEF",
        (rs, rowNum) -> {
          System.out.println(
              "[DEBUG] Found process definition with key ("
                  + rs.getString("KEY_")
                  + ") and ("
                  + rs.getString("NAME_")
                  + ")");
          return "";
        });

    ProcessDefinitionQuery processDefinitionQuery =
        processEngine.getRepositoryService().createProcessDefinitionQuery();
    processDefinitionQuery.processDefinitionKey("testProcess");

    long processDefinitionCount = processDefinitionQuery.count();

    List<ProcessDefinition> processDefinitions = processDefinitionQuery.list();

    Map<String, Object> parameters = new HashMap<>();

    ProcessInstance processInstance =
        processEngine.getRuntimeService().startProcessInstanceByKey("testProcess", parameters);

    //    FormInfo startFormInfo =
    //        processEngine
    //            .getRuntimeService()
    //            .getStartFormModel(processDefinitions.get(0).getId(), processInstance.getId());

    /*
     * The start event for the test process is asynchronous. This allows us to retrieve and handle
     * the start form. After this is done, we need to trigger the execution of the process by
     * executing the associated job.
     */
    JobQuery jobQuery =
        processEngine
            .getManagementService()
            .createJobQuery()
            .processInstanceId(processInstance.getId());

    List<Job> jobs = jobQuery.list();

    assertEquals(
        1, jobs.size(), "Failed to find the job to asynchronously start the process instance");
    for (Job job : jobs) {
      processEngine.getManagementService().executeJob(job.getId());
    }

    // Retrieve the list of running process instances
    ProcessInstanceQuery processInstanceQuery =
        processEngine.getRuntimeService().createProcessInstanceQuery();
    processInstanceQuery.processDefinitionKey("testProcess");

    List<ProcessInstance> processInstances = processInstanceQuery.list();

    assertEquals(1, processInstances.size(), "Failed to find the running process instance");

    // Retrieve the tasks
    TaskQuery taskQuery =
        processEngine
            .getTaskService()
            .createTaskQuery()
            .processInstanceId(processInstances.get(0).getId());

    List<Task> tasks = taskQuery.list();

    assertEquals(1, tasks.size(), "Failed to find the task for the Administrators group");
    ////
    ////    String taskId = tasks.get(0).getId();
    ////
    ////    TaskFormData taskFormData = processEngine.getFormService().getTaskFormData(taskId);
    ////
    ////    processEngine.getTaskService().claim(taskId, "jack");
    ////
    ////    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
    ////
    ////    assertEquals(1, tasks.size(), "Failed to find the task");
    ////    assertEquals(
    ////        "jack",
    ////        tasks.get(0).getAssignee(),
    ////        "Failed to confirm that the task has been claimed by jack");
    ////
    ////    processEngine.getTaskService().delegateTask(tasks.get(0).getId(), "jill");
    ////
    ////    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
    ////
    ////    assertEquals(1, tasks.size(), "Failed to find the task");
    ////    assertEquals(
    ////        "jill",
    ////        tasks.get(0).getAssignee(),
    ////        "Failed to confirm that the task has been assigned to jill");
    ////
    ////    processEngine.getTaskService().resolveTask(tasks.get(0).getId());
    ////
    ////    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
    ////
    ////    assertEquals(1, tasks.size(), "Failed to find the task");
    ////    assertEquals(
    ////        "jack",
    ////        tasks.get(0).getAssignee(),
    ////        "Failed to confirm that the resolved task has been re-assigned to jack");
    ////
    ////    processEngine.getTaskService().setOwner(tasks.get(0).getId(), "jill");
    ////
    ////    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
    ////
    ////    assertEquals(1, tasks.size(), "Failed to find the task");
    ////    assertEquals(
    ////        "jill",
    ////        tasks.get(0).getOwner(),
    ////        "Failed to confirm that the task owner has been changed to to jill");
    ////
    ////    processEngine.getTaskService().complete(taskId);
    ////
    ////    processService.deleteProcessDefinition("Inception.Test");

    int xxx = 0;
    xxx++;
  }

  //  /** Test the process definition functionality. */
  //  @Test
  //  public void processDefinitionTest() throws Exception {
  //    byte[] testProcessV1Data =
  //        ResourceUtil.getClasspathResource("digital/inception/workflow/test/TestV1.bpmn");
  //
  //    List<ProcessDefinitionSummary> processDefinitionSummaries =
  //        processService.createProcessDefinition(testProcessV1Data);
  //
  //    assertEquals(
  //        1,
  //        processDefinitionSummaries.size(),
  //        "The correct number of process definitions was not retrieved for version 1 of the
  // Inception.Test process definition");
  //
  //    assertEquals(
  //        "Inception.Test",
  //        processDefinitionSummaries.get(0).getId(),
  //        "The correct process definition ID was not retrieved for version 1 of the Inception.Test
  // process definition");
  //
  //    byte[] testProcessV2Data =
  //        ResourceUtil.getClasspathResource("digital/inception/workflow/test/TestV2.bpmn");
  //
  //    processDefinitionSummaries = processService.updateProcessDefinition(testProcessV2Data);
  //
  //    assertEquals(
  //        1,
  //        processDefinitionSummaries.size(),
  //        "The correct number of process definitions was not retrieved for version 2 of the
  // Inception.Test process definition");
  //
  //    assertEquals(
  //        "Inception.Test",
  //        processDefinitionSummaries.get(0).getId(),
  //        "The correct process definition ID was not retrieved for version 2 of the Inception.Test
  // process definition");
  //
  //    List<ProcessDefinitionSummary> retrievedProcessDefinitionSummaries =
  //        processService.getProcessDefinitionSummaries();
  //
  //    boolean foundProcessDefinition = false;
  //
  //    for (ProcessDefinitionSummary processDefinitionSummary :
  // retrievedProcessDefinitionSummaries) {
  //      if (processDefinitionSummary.getId().equals("Inception.Test")) {
  //        foundProcessDefinition = true;
  //
  //        break;
  //      }
  //    }
  //
  //    if (!foundProcessDefinition) {
  //      fail("Failed to retrieve the summary for the process definition (Inception.Test)");
  //    }
  //
  //    processService.deleteProcessDefinition("Inception.Test");
  //  }
  //
  //  /** Test the process engine. */
  //  @Test
  //  public void processEngineTest() throws Exception {
  //    byte[] testProcessV1DefinitionData =
  //        ResourceUtil.getClasspathResource("digital/inception/workflow/test/TestV1.bpmn");
  //
  //    List<ProcessDefinitionSummary> processDefinitionSummaries =
  //        processService.validateBPMN(testProcessV1DefinitionData);
  //
  //    assertEquals(
  //        1,
  //        processDefinitionSummaries.size(),
  //        "The correct number of process definitions was not retrieved for version 1 of the
  // Inception.Test process definition");
  //
  //    assertEquals(
  //        "Inception.Test",
  //        processDefinitionSummaries.get(0).getId(),
  //        "The correct process definition ID was not retrieved for version 1 of the Inception.Test
  // process definition");
  //
  //    byte[] testProcessV2DefinitionData =
  //        ResourceUtil.getClasspathResource("digital/inception/workflow/test/TestV2.bpmn");
  //
  //    processService.createProcessDefinition(testProcessV1DefinitionData);
  //
  //    processDefinitionSummaries = processService.validateBPMN(testProcessV2DefinitionData);
  //
  //    assertEquals(
  //        1,
  //        processDefinitionSummaries.size(),
  //        "The correct number of process definitions was not retrieved for version 2 of the
  // Inception.Test process definition");
  //
  //    assertEquals(
  //        "Inception.Test",
  //        processDefinitionSummaries.get(0).getId(),
  //        "The correct process definition ID was not retrieved for version 1 of the Inception.Test
  // process");
  //
  //    processService.updateProcessDefinition(testProcessV2DefinitionData);
  //
  //    byte[] testEmbeddedProcessDefinitionData =
  //        ResourceUtil.getClasspathResource("digital/inception/workflow/test/TestEmbedded.bpmn");
  //
  //    processService.createProcessDefinition(testEmbeddedProcessDefinitionData);
  //
  ////    List<ProcessDefinition> processDefinitions =
  ////
  // processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().list();
  ////
  ////    assertEquals(
  ////        2,
  ////        processDefinitions.size(),
  ////        "The correct number of process definitions was not retrieved");
  ////
  ////    boolean foundProcessDefinition = false;
  ////
  ////    for (ProcessDefinition processDefinition : processDefinitions) {
  ////      if (processDefinition.getKey().equals("Inception.Test")) {
  ////        foundProcessDefinition = true;
  ////
  ////        break;
  ////      }
  ////    }
  ////
  ////    if (!foundProcessDefinition) {
  ////      fail("Failed to find the process definition (Inception.Test)");
  ////    }
  ////
  ////    Map<String, Object> processInstanceParameters = new HashMap<>();
  ////
  ////    processInstanceParameters.put("TestVariable", "MyTestVariable");
  ////
  ////    processService.startProcessInstance("Inception.Test", processInstanceParameters);
  //
  //    /*
  //     * The test process starts asynchronously, so we need to ensure it starts executing by
  // executing
  //     * the associated job.
  //     */
  ////    JobQuery jobQuery = processEngine.getManagementService().createJobQuery();
  ////
  ////    List<Job> jobs = jobQuery.list();
  ////
  ////    assertEquals(
  ////        1, jobs.size(), "Failed to find the job to asynchronously start the process
  // instance");
  ////
  ////    for (Job job : jobs) {
  ////      processEngine.getManagementService().executeJob(job.getId());
  ////    }
  ////
  ////    // Retrieve the list of running process instances
  ////    ProcessInstanceQuery processInstanceQuery =
  ////        processEngine.getRuntimeService().createProcessInstanceQuery();
  ////    processInstanceQuery.processDefinitionKey(processDefinitionSummaries.get(0).getId());
  ////
  ////    List<ProcessInstance> processInstances = processInstanceQuery.list();
  ////
  ////    assertEquals(1, processInstances.size(), "Failed to find the running process instance");
  ////
  ////    // Retrieve the tasks for the Administrators group
  ////    TaskQuery taskQuery =
  ////        processEngine
  ////            .getTaskService()
  ////            .createTaskQuery()
  ////            .taskCandidateGroup("Administrators")
  ////            .initializeFormKeys();
  ////    List<Task> tasks = taskQuery.list();
  ////
  ////    assertEquals(1, tasks.size(), "Failed to find the task for the Administrators group");
  ////
  ////    String taskId = tasks.get(0).getId();
  ////
  ////    TaskFormData taskFormData = processEngine.getFormService().getTaskFormData(taskId);
  ////
  ////    processEngine.getTaskService().claim(taskId, "jack");
  ////
  ////    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
  ////
  ////    assertEquals(1, tasks.size(), "Failed to find the task");
  ////    assertEquals(
  ////        "jack",
  ////        tasks.get(0).getAssignee(),
  ////        "Failed to confirm that the task has been claimed by jack");
  ////
  ////    processEngine.getTaskService().delegateTask(tasks.get(0).getId(), "jill");
  ////
  ////    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
  ////
  ////    assertEquals(1, tasks.size(), "Failed to find the task");
  ////    assertEquals(
  ////        "jill",
  ////        tasks.get(0).getAssignee(),
  ////        "Failed to confirm that the task has been assigned to jill");
  ////
  ////    processEngine.getTaskService().resolveTask(tasks.get(0).getId());
  ////
  ////    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
  ////
  ////    assertEquals(1, tasks.size(), "Failed to find the task");
  ////    assertEquals(
  ////        "jack",
  ////        tasks.get(0).getAssignee(),
  ////        "Failed to confirm that the resolved task has been re-assigned to jack");
  ////
  ////    processEngine.getTaskService().setOwner(tasks.get(0).getId(), "jill");
  ////
  ////    tasks = processEngine.getTaskService().createTaskQuery().taskId(taskId).list();
  ////
  ////    assertEquals(1, tasks.size(), "Failed to find the task");
  ////    assertEquals(
  ////        "jill",
  ////        tasks.get(0).getOwner(),
  ////        "Failed to confirm that the task owner has been changed to to jill");
  ////
  ////    processEngine.getTaskService().complete(taskId);
  ////
  ////    processService.deleteProcessDefinition("Inception.Test");
  //  }
}

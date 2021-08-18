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

import digital.inception.bmi.CaseDefinitionSummary;
import digital.inception.bmi.ICaseService;
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
 * The <b>CaseServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>ProcessService</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, CaseServiceTestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
    })
public class CaseServiceTest {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CaseServiceTest.class);

  /** The Case Service. */
  @Autowired private ICaseService caseService;

  /** The Camunda Process Engine. */
  @Autowired private ProcessEngine processEngine;

  /** The Process Service. */
  @Autowired private IProcessService processService;

//  /** Test the case definition functionality. */
//  @Test
//  public void caseDefinitionTest() throws Exception {
//    byte[] testCaseData = ResourceUtil.getClasspathResource("digital/inception/bmi/test/Test.cmmn");
//
//    List<CaseDefinitionSummary> caseDefinitionSummaries =
//        caseService.createCaseDefinition(testCaseData);
//
//    caseDefinitionSummaries = caseService.updateCaseDefinition(testCaseData);
//
//
//  }

  /** Test the process with embedded case functionality. */
  @Test
  public void processWithEmbeddedProcessAndCaseTest() throws Exception {
    byte[] testEmbeddedProcessDefinitionData =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestEmbedded.bpmn");

    processService.createProcessDefinition(testEmbeddedProcessDefinitionData);

    byte[] testWithCaseProcessDefinitionData =
        ResourceUtil.getClasspathResource("digital/inception/bmi/test/TestWithCase.bpmn");

    processService.createProcessDefinition(testWithCaseProcessDefinitionData);

    byte[] testCaseDefinitionData = ResourceUtil.getClasspathResource("digital/inception/bmi/test/Test.cmmn");

    caseService.createCaseDefinition(testCaseDefinitionData);

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

    assertEquals(1, tasks.size(), "The correct number of tasks was not retrieved");

    assertEquals("Required Human Task", tasks.get(0).getName(), "The correct task name was not retrieved");
  }
}

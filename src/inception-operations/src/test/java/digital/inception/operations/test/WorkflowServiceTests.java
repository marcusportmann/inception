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

package digital.inception.operations.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.operations.service.DocumentService;
import digital.inception.test.InceptionExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionNotFoundException;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionNotFoundException;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.service.WorkflowService;
import digital.inception.core.util.ResourceUtil;
import digital.inception.core.validation.ValidationSchemaType;
import digital.inception.test.TestConfiguration;

/**
 * The <b>WorkflowServiceTests</b> class contains the JUnit tests for the <b>WorkflowService</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, OperationsConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
    })
public class WorkflowServiceTests {

  /**
   * The Document Service.
   */
  @Autowired private
  DocumentService documentService;

  /**
   * The Jackson Object Mapper.
   */
  @Autowired
  private ObjectMapper objectMapper;

  /**
   * The Workflow Service.
   */
  @Autowired private
  WorkflowService workflowService;

  /**
   * Test the JSON workflow functionality.
   */
  @Test
  public void jsonWorkflowTest() throws Exception {
    DocumentDefinition documentDefinition = new DocumentDefinition("test_document_definition_" + System.currentTimeMillis(), "Test Document Definition");

    workflowService.createDocumentDefinition(documentDefinition);

    WorkflowDefinition workflowDefinition = new WorkflowDefinition("test_json_workflow_definition_" + System.currentTimeMillis(), 1, "Test JSON Workflow Definition",
        ValidationSchemaType.JSON, ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    workflowDefinition.addDocumentDefinition(documentDefinition.getId(), true);

    workflowService.createWorkflowDefinition(workflowDefinition);

    TestWorkflowData testWorkflowData = new TestWorkflowData(UUID.randomUUID(), "This is name " + System.currentTimeMillis(),
        LocalDate.of(1976, 03, 07), new BigDecimal("1234.56"), OffsetDateTime.now());

    String testWorkflowDate = objectMapper.writeValueAsString(testWorkflowData);

    CreateWorkflowRequest createWorkflowRequest = new CreateWorkflowRequest(workflowDefinition.getId(), testWorkflowDate);

    Workflow theWorkflow = workflowService.createWorkflow(createWorkflowRequest, OffsetDateTime.now(), "SYSTEM");

    Workflow retrievedWorkflow = workflowService.getWorkflow(theWorkflow.getId());

    compareWorkflows(theWorkflow, retrievedWorkflow);

    testWorkflowData.setName(testWorkflowData.getName() + " Updated");

    testWorkflowDate = objectMapper.writeValueAsString(testWorkflowData);

    UpdateWorkflowRequest updateWorkflowRequest = new UpdateWorkflowRequest(theWorkflow.getId(), WorkflowStatus.COMPLETED);

    workflowService.updateWorkflow(updateWorkflowRequest, OffsetDateTime.now(), "SYSTEM");

    The new status for the workflow

        The updated data for the workflow



    int xxx = 0;
    xxx++;
  }

  /**
   * Test the workflow definition functionality.
   */
  @Test
  public void workflowDefinitionTest() throws Exception {
    DocumentDefinition documentDefinition = new DocumentDefinition("test_document_definition_" + System.currentTimeMillis(), "Test Document Definition");

    documentService.createDocumentDefinition(documentDefinition);

    DocumentDefinition retrievedDocumentDefinition = documentService.getDocumentDefinition(documentDefinition.getId());

    compareDocumentDefinitions(documentDefinition, retrievedDocumentDefinition);

    WorkflowDefinition workflowDefinition = new WorkflowDefinition("test_json_workflow_definition_" + System.currentTimeMillis(), 1, "Test JSON Workflow Definition",
        ValidationSchemaType.JSON, ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    workflowDefinition.addDocumentDefinition(documentDefinition.getId(), true);

    workflowService.createWorkflowDefinition(workflowDefinition);

    WorkflowDefinition retrievedWorkflowDefinition = workflowService.getWorkflowDefinition(workflowDefinition.getId());

    compareWorkflowDefinitions(workflowDefinition, retrievedWorkflowDefinition);

    WorkflowDefinition updatedWorkflowDefinition = workflowService.updateWorkflowDefinition(workflowDefinition);

    retrievedWorkflowDefinition = workflowService.getWorkflowDefinition(workflowDefinition.getId());

    assertEquals(2, retrievedWorkflowDefinition.getVersion());

    workflowService.deleteWorkflowDefinition(workflowDefinition.getId());

    WorkflowDefinitionNotFoundException workflowDefinitionNotFoundException = assertThrows(
        WorkflowDefinitionNotFoundException.class, () -> {
      workflowService.getWorkflowDefinition(workflowDefinition.getId());
    });

    documentService.deleteDocumentDefinition(documentDefinition.getId());

    DocumentDefinitionNotFoundException documentDefinitionNotFoundException = assertThrows(
        DocumentDefinitionNotFoundException.class, () -> {
      workflowService.documentService(documentDefinition.getId());
    });
  }

  private void compareWorkflowDefinitions(
      WorkflowDefinition workflowDefinition1, WorkflowDefinition workflowDefinition2) {
    assertEquals(
        workflowDefinition1.getId(), workflowDefinition2.getId(), "The ID values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getVersion(), workflowDefinition2.getVersion(), "The version values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getName(), workflowDefinition2.getName(), "The name values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getValidationSchemaType(), workflowDefinition2.getValidationSchemaType(), "The validation schema type values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getValidationSchema(), workflowDefinition2.getValidationSchema(), "The validation schema values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getDocumentDefinitions().size(), workflowDefinition2.getDocumentDefinitions().size(), "The number of document definitions for the workflow definitions do not match");

    workflowDefinition1.getDocumentDefinitions().forEach(workflowDefinitionDocumentDefinition1 -> {
      boolean foundDocumentDefinition = workflowDefinition2.getDocumentDefinitions().stream().anyMatch(
          workflowDefinitionDocumentDefinition2 -> Objects.equals(
              workflowDefinitionDocumentDefinition1,
              workflowDefinitionDocumentDefinition2));
      if (!foundDocumentDefinition) {
        fail("Failed to find the document definition ("
            + workflowDefinitionDocumentDefinition1.getDocumentDefinitionId()
            + ") for the workflow definition ("
            + workflowDefinition1.getId() + ") version ("
            + workflowDefinition1.getVersion() + ")");
      }
    });
  }

  private void compareWorkflows(Workflow workflow1, Workflow workflow2) {
    assertEquals(
        workflow1.getId(), workflow2.getId(), "The ID values for the workflows do not match");
    assertEquals(
        workflow1.getParentId(), workflow2.getParentId(), "The parent ID values for the workflows do not match");
    assertEquals(
        workflow1.getDefinitionId(), workflow2.getDefinitionId(), "The definition ID values for the workflows do not match");
    assertEquals(
        workflow1.getDefinitionVersion(), workflow2.getDefinitionVersion(), "The definition version values for the workflows do not match");
    assertEquals(
        workflow1.getStatus(), workflow2.getStatus(), "The status values for the workflows do not match");
    assertEquals(
        workflow1.getData(), workflow2.getData(), "The data values for the workflows do not match");
    assertEquals(
        workflow1.getCreated(), workflow2.getCreated(), "The created values for the workflows do not match");
    assertEquals(
        workflow1.getCreatedBy(), workflow2.getCreatedBy(), "The created by values for the workflows do not match");
    assertEquals(
        workflow1.getUpdated(), workflow2.getUpdated(), "The updated values for the workflows do not match");
    assertEquals(
        workflow1.getUpdatedBy(), workflow2.getUpdatedBy(), "The updated by values for the workflows do not match");
  }



}


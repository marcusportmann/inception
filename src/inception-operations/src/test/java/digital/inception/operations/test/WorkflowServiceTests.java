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
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.time.TimeUnit;
import digital.inception.core.util.ResourceUtil;
import digital.inception.core.util.TenantUtil;
import digital.inception.core.validation.ValidationSchemaType;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.RequiredDocumentAttribute;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowEngineAttribute;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.service.DocumentService;
import digital.inception.operations.service.WorkflowService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
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

/**
 * The {@code WorkflowServiceTests} class contains the JUnit tests for The {@code WorkflowService}
 * class.
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

  /** The Document Service. */
  @Autowired private DocumentService documentService;

  /** The Jackson Object Mapper. */
  @Autowired private ObjectMapper objectMapper;

  /** The Workflow Service. */
  @Autowired private WorkflowService workflowService;

  /** Test the JSON workflow functionality. */
  @Test
  public void jsonWorkflowTest() throws Exception {
    DocumentDefinitionCategory documentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_document_definition_category", "Test Document Definition Category");

    documentService.createDocumentDefinitionCategory(documentDefinitionCategory);

    DocumentDefinition documentDefinition =
        new DocumentDefinition(
            "test_document_definition",
            documentDefinitionCategory.getId(),
            "Test Document Definition");

    documentService.createDocumentDefinition(documentDefinition);

    WorkflowDefinitionCategory workflowDefinitionCategory =
        new WorkflowDefinitionCategory(
            "test_workflow_definition_category", "Test Workflow Definition Category");

    workflowService.createWorkflowDefinitionCategory(workflowDefinitionCategory);

    WorkflowDefinition workflowDefinition =
        new WorkflowDefinition(
            "test_json_workflow_definition",
            1,
            workflowDefinitionCategory.getId(),
            "Test JSON Workflow Definition",
            "flowable_embedded",
            ValidationSchemaType.JSON,
            ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    workflowDefinition.addDocumentDefinition(documentDefinition.getId(), true, false);

    workflowService.createWorkflowDefinition(workflowDefinition);

    TestWorkflowData testWorkflowData =
        new TestWorkflowData(
            UUID.randomUUID(),
            "This is name " + System.currentTimeMillis(),
            LocalDate.of(1976, 03, 07),
            new BigDecimal("1234.56"),
            OffsetDateTime.now());

    String testWorkflowDataJson = objectMapper.writeValueAsString(testWorkflowData);

    CreateWorkflowRequest createWorkflowRequest =
        new CreateWorkflowRequest(
            workflowDefinition.getId(), UUID.randomUUID().toString(), testWorkflowDataJson);

    Workflow workflow =
        workflowService.createWorkflow(TenantUtil.DEFAULT_TENANT_ID, createWorkflowRequest);

    Workflow retrievedWorkflow =
        workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    compareWorkflows(workflow, retrievedWorkflow);

    testWorkflowData.setName(testWorkflowData.getName() + " Updated");

    testWorkflowDataJson = objectMapper.writeValueAsString(testWorkflowData);

    UpdateWorkflowRequest updateWorkflowRequest =
        new UpdateWorkflowRequest(workflow.getId(), WorkflowStatus.COMPLETED, testWorkflowDataJson);

    workflowService.updateWorkflow(TenantUtil.DEFAULT_TENANT_ID, updateWorkflowRequest);

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(WorkflowStatus.COMPLETED, retrievedWorkflow.getStatus());
    assertEquals(testWorkflowDataJson, retrievedWorkflow.getData());

    workflowService.deleteWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    workflowService.deleteWorkflowDefinition(workflowDefinition.getId());

    workflowService.deleteWorkflowDefinitionCategory(workflowDefinitionCategory.getId());

    documentService.deleteDocumentDefinition(documentDefinition.getId());

    documentService.deleteDocumentDefinitionCategory(documentDefinitionCategory.getId());
  }

  /** Test the workflow service functionality. */
  @Test
  public void workflowServiceTest() throws Exception {

    WorkflowEngineAttribute workflowEngineAttribute =
        new WorkflowEngineAttribute(
            "test_workflow_engine_attribute_name", "test_workflow_engine_attribute_value");

    List<WorkflowEngineAttribute> workflowEngineAttributes = List.of(workflowEngineAttribute);

    WorkflowEngine workflowEngine =
        new WorkflowEngine(
            "test_workflow_engine",
            "Test Workflow Engine",
            "digital.inception.operations.test.TestWorkflowEngineConnector",
            workflowEngineAttributes);

    workflowService.createWorkflowEngine(workflowEngine);

    WorkflowEngine retrievedWorkflowEngine =
        workflowService.getWorkflowEngine(workflowEngine.getId());

    compareWorkflowEngines(workflowEngine, retrievedWorkflowEngine);

    List<WorkflowEngine> workflowEngines = workflowService.getWorkflowEngines();

    assertEquals(2, workflowEngines.size());

    compareWorkflowEngines(workflowEngine, workflowEngines.get(1));

    DocumentDefinitionCategory sharedDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_shared_document_definition_category", "Test Shared Document Definition Category");

    documentService.createDocumentDefinitionCategory(sharedDocumentDefinitionCategory);

    DocumentDefinitionCategory tenantDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_tenant_document_definition_category",
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition Category");

    documentService.createDocumentDefinitionCategory(tenantDocumentDefinitionCategory);

    DocumentDefinition sharedDocumentDefinition =
        new DocumentDefinition(
            "test_shared_document_definition",
            sharedDocumentDefinitionCategory.getId(),
            "Test Shared Document Definition",
            List.of(
                RequiredDocumentAttribute.EXPIRY_DATE,
                RequiredDocumentAttribute.EXTERNAL_REFERENCE,
                RequiredDocumentAttribute.ISSUE_DATE));

    documentService.createDocumentDefinition(sharedDocumentDefinition);

    DocumentDefinition tenantDocumentDefinition =
        new DocumentDefinition(
            "test_tenant_document_definition",
            tenantDocumentDefinitionCategory.getId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition");

    documentService.createDocumentDefinition(tenantDocumentDefinition);

    WorkflowDefinitionCategory sharedWorkflowDefinitionCategory =
        new WorkflowDefinitionCategory(
            "test_shared_workflow_definition_category", "Test Shared Workflow Definition Category");

    workflowService.createWorkflowDefinitionCategory(sharedWorkflowDefinitionCategory);

    WorkflowDefinitionCategory retrievedWorkflowDefinitionCategory =
        workflowService.getWorkflowDefinitionCategory(sharedWorkflowDefinitionCategory.getId());

    compareWorkflowDefinitionCategories(
        sharedWorkflowDefinitionCategory, retrievedWorkflowDefinitionCategory);

    WorkflowDefinitionCategory tenantWorkflowDefinitionCategory =
        new WorkflowDefinitionCategory(
            "test_tenant_workflow_definition_category",
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Workflow Definition Category");

    workflowService.createWorkflowDefinitionCategory(tenantWorkflowDefinitionCategory);

    retrievedWorkflowDefinitionCategory =
        workflowService.getWorkflowDefinitionCategory(tenantWorkflowDefinitionCategory.getId());

    compareWorkflowDefinitionCategories(
        tenantWorkflowDefinitionCategory, retrievedWorkflowDefinitionCategory);

    List<WorkflowDefinitionCategory> workflowDefinitionCategories =
        workflowService.getWorkflowDefinitionCategories(TenantUtil.DEFAULT_TENANT_ID);
    assertEquals(2, workflowDefinitionCategories.size());

    workflowDefinitionCategories =
        workflowService.getWorkflowDefinitionCategories(UUID.randomUUID());
    assertEquals(1, workflowDefinitionCategories.size());

    compareWorkflowDefinitionCategories(
        sharedWorkflowDefinitionCategory, workflowDefinitionCategories.getFirst());

    WorkflowDefinition sharedWorkflowDefinition =
        new WorkflowDefinition(
            "test_shared_workflow_definition",
            1,
            sharedWorkflowDefinitionCategory.getId(),
            "Test Shared Workflow Definition",
            "test_workflow_engine",
            ValidationSchemaType.JSON,
            ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    sharedWorkflowDefinition.addDocumentDefinition(sharedDocumentDefinition.getId(), true, true);

    workflowService.createWorkflowDefinition(sharedWorkflowDefinition);

    WorkflowDefinition retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(sharedWorkflowDefinition.getId());

    compareWorkflowDefinitions(sharedWorkflowDefinition, retrievedWorkflowDefinition);

    WorkflowDefinition tenantWorkflowDefinition =
        new WorkflowDefinition(
            "test_tenant_workflow_definition",
            1,
            tenantWorkflowDefinitionCategory.getId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Workflow Definition",
            "test_workflow_engine",
            ValidationSchemaType.JSON,
            ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    tenantWorkflowDefinition.addDocumentDefinition(tenantDocumentDefinition.getId(), true, false);
    tenantWorkflowDefinition.addDocumentDefinition(
        sharedDocumentDefinition.getId(), true, true, TimeUnit.MONTHS, 6);

    workflowService.createWorkflowDefinition(tenantWorkflowDefinition);

    retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(tenantWorkflowDefinition.getId());

    compareWorkflowDefinitions(tenantWorkflowDefinition, retrievedWorkflowDefinition);

    List<WorkflowDefinition> workflowDefinitions =
        workflowService.getWorkflowDefinitions(
            TenantUtil.DEFAULT_TENANT_ID, sharedWorkflowDefinitionCategory.getId());
    assertEquals(1, workflowDefinitions.size());
    assertEquals(sharedWorkflowDefinition.getId(), workflowDefinitions.getFirst().getId());

    workflowDefinitions =
        workflowService.getWorkflowDefinitions(
            TenantUtil.DEFAULT_TENANT_ID, tenantWorkflowDefinitionCategory.getId());
    assertEquals(1, workflowDefinitions.size());
    assertEquals(tenantWorkflowDefinition.getId(), workflowDefinitions.getFirst().getId());

    workflowDefinitions =
        workflowService.getWorkflowDefinitions(
            UUID.randomUUID(), tenantWorkflowDefinitionCategory.getId());
    assertEquals(0, workflowDefinitions.size());

    tenantWorkflowDefinition.setName("Updated Test Tenant Workflow Definition");
    tenantWorkflowDefinition.setCategoryId(sharedDocumentDefinitionCategory.getId());
    tenantWorkflowDefinition.removeDocumentDefinition(sharedDocumentDefinition.getId());

    workflowService.updateWorkflowDefinition(tenantWorkflowDefinition);

    retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(tenantWorkflowDefinition.getId());

    compareWorkflowDefinitions(tenantWorkflowDefinition, retrievedWorkflowDefinition);

    workflowService.deleteWorkflowDefinition(tenantWorkflowDefinition.getId());

    assertThrows(
        WorkflowDefinitionNotFoundException.class,
        () -> {
          workflowService.getWorkflowDefinition(tenantWorkflowDefinition.getId());
        });

    workflowService.deleteWorkflowDefinition(sharedWorkflowDefinition.getId());

    assertThrows(
        WorkflowDefinitionNotFoundException.class,
        () -> {
          workflowService.getWorkflowDefinition(sharedWorkflowDefinition.getId());
        });

    tenantWorkflowDefinitionCategory.setName("Updated Test Tenant Workflow Definition Category");
    tenantWorkflowDefinitionCategory.setTenantId(UUID.randomUUID());

    workflowService.updateWorkflowDefinitionCategory(tenantWorkflowDefinitionCategory);

    retrievedWorkflowDefinitionCategory =
        workflowService.getWorkflowDefinitionCategory(tenantWorkflowDefinitionCategory.getId());

    compareWorkflowDefinitionCategories(
        tenantWorkflowDefinitionCategory, retrievedWorkflowDefinitionCategory);

    workflowService.deleteWorkflowDefinitionCategory(tenantWorkflowDefinitionCategory.getId());

    assertThrows(
        WorkflowDefinitionCategoryNotFoundException.class,
        () -> {
          workflowService.getWorkflowDefinitionCategory(tenantWorkflowDefinitionCategory.getId());
        });

    workflowService.deleteWorkflowDefinitionCategory(sharedWorkflowDefinitionCategory.getId());

    assertThrows(
        WorkflowDefinitionCategoryNotFoundException.class,
        () -> {
          workflowService.getWorkflowDefinitionCategory(sharedWorkflowDefinitionCategory.getId());
        });

    documentService.deleteDocumentDefinition(tenantDocumentDefinition.getId());

    documentService.deleteDocumentDefinition(sharedDocumentDefinition.getId());

    documentService.deleteDocumentDefinitionCategory(tenantDocumentDefinitionCategory.getId());

    documentService.deleteDocumentDefinitionCategory(sharedDocumentDefinitionCategory.getId());
  }

  private void compareWorkflowDefinitionCategories(
      WorkflowDefinitionCategory workflowDefinitionCategory1,
      WorkflowDefinitionCategory workflowDefinitionCategory2) {
    assertEquals(
        workflowDefinitionCategory1.getId(),
        workflowDefinitionCategory2.getId(),
        "The ID values for the workflow definition categories do not match");
    assertEquals(
        workflowDefinitionCategory1.getName(),
        workflowDefinitionCategory2.getName(),
        "The name values for the workflow definition categories do not match");
    assertEquals(
        workflowDefinitionCategory1.getTenantId(),
        workflowDefinitionCategory2.getTenantId(),
        "The tenant ID values for the workflow definition categories do not match");
  }

  private void compareWorkflowDefinitions(
      WorkflowDefinition workflowDefinition1, WorkflowDefinition workflowDefinition2) {
    assertEquals(
        workflowDefinition1.getCategoryId(),
        workflowDefinition2.getCategoryId(),
        "The category ID values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getEngineId(),
        workflowDefinition2.getEngineId(),
        "The engine ID values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getId(),
        workflowDefinition2.getId(),
        "The ID values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getName(),
        workflowDefinition2.getName(),
        "The name values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getTenantId(),
        workflowDefinition2.getTenantId(),
        "The tenant ID values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getValidationSchema(),
        workflowDefinition2.getValidationSchema(),
        "The validation schema values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getValidationSchemaType(),
        workflowDefinition2.getValidationSchemaType(),
        "The validation schema type values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getVersion(),
        workflowDefinition2.getVersion(),
        "The version values for the workflow definitions do not match");
    assertEquals(
        workflowDefinition1.getDocumentDefinitions().size(),
        workflowDefinition2.getDocumentDefinitions().size(),
        "The number of document definitions for the workflow definitions do not match");

    workflowDefinition1
        .getDocumentDefinitions()
        .forEach(
            workflowDefinitionDocumentDefinition1 -> {
              boolean foundDocumentDefinition =
                  workflowDefinition2.getDocumentDefinitions().stream()
                      .anyMatch(
                          workflowDefinitionDocumentDefinition2 ->
                              Objects.equals(
                                  workflowDefinitionDocumentDefinition1,
                                  workflowDefinitionDocumentDefinition2));
              if (!foundDocumentDefinition) {
                fail(
                    "Failed to find the document definition ("
                        + workflowDefinitionDocumentDefinition1.getDocumentDefinitionId()
                        + ") for the workflow definition ("
                        + workflowDefinition1.getId()
                        + ") version ("
                        + workflowDefinition1.getVersion()
                        + ")");
              }
            });
  }

  private void compareWorkflowEngines(
      WorkflowEngine workflowEngine1, WorkflowEngine workflowEngine2) {
    assertEquals(
        workflowEngine1.getConnectorClassName(),
        workflowEngine2.getConnectorClassName(),
        "The connector class name values for the workflow engines do not match");
    assertEquals(
        workflowEngine1.getId(),
        workflowEngine2.getId(),
        "The ID values for the workflow engines do not match");
    assertEquals(
        workflowEngine1.getName(),
        workflowEngine2.getName(),
        "The name values for the workflow engines do not match");

    workflowEngine1
        .getAttributes()
        .forEach(
            workflowEngineAttribute1 -> {
              boolean foundAttribute =
                  workflowEngine2.getAttributes().stream()
                      .anyMatch(
                          workflowEngineAttribute2 ->
                              Objects.equals(workflowEngineAttribute1, workflowEngineAttribute2));
              if (!foundAttribute) {
                fail(
                    "Failed to find the attribute ("
                        + workflowEngineAttribute1.getName()
                        + ") for the workflow engine ("
                        + workflowEngine1.getId()
                        + ")");
              }
            });
  }

  private void compareWorkflows(Workflow workflow1, Workflow workflow2) {
    assertEquals(
        workflow1.getData(), workflow2.getData(), "The data values for the workflows do not match");
    assertEquals(
        workflow1.getDefinitionId(),
        workflow2.getDefinitionId(),
        "The definition ID values for the workflows do not match");
    assertEquals(
        workflow1.getDefinitionVersion(),
        workflow2.getDefinitionVersion(),
        "The definition version values for the workflows do not match");
    assertEquals(
        workflow1.getExternalReference(),
        workflow2.getExternalReference(),
        "The external reference values for the workflows do not match");
    assertEquals(
        workflow1.getId(), workflow2.getId(), "The ID values for the workflows do not match");
    assertEquals(
        workflow1.getParentId(),
        workflow2.getParentId(),
        "The parent ID values for the workflows do not match");
    assertEquals(
        workflow1.getStatus(),
        workflow2.getStatus(),
        "The status values for the workflows do not match");
    assertEquals(
        workflow1.getTenantId(),
        workflow2.getTenantId(),
        "The tenant ID values for the workflows do not match");
  }
}

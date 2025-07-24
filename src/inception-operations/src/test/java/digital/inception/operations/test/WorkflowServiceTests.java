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
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.time.TimeUnit;
import digital.inception.core.util.ResourceUtil;
import digital.inception.core.util.TenantUtil;
import digital.inception.core.validation.ValidationSchemaType;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.RequiredDocumentAttribute;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionAttribute;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowEngineAttribute;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.model.WorkflowSummary;
import digital.inception.operations.service.DocumentService;
import digital.inception.operations.service.WorkflowService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.math.BigDecimal;
import java.security.SecureRandom;
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

  /** The secure random number generator. */
  private static final SecureRandom secureRandom = new SecureRandom();

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
            "test_document_definition_category_" + randomId(), "Test Document Definition Category");

    documentService.createDocumentDefinitionCategory(documentDefinitionCategory);

    DocumentDefinition documentDefinition =
        new DocumentDefinition(
            "test_document_definition_" + randomId(),
            documentDefinitionCategory.getId(),
            "Test Document Definition");

    documentService.createDocumentDefinition(documentDefinition);

    WorkflowDefinitionCategory workflowDefinitionCategory =
        new WorkflowDefinitionCategory(
            "test_workflow_definition_category_" + randomId(), "Test Workflow Definition Category");

    workflowService.createWorkflowDefinitionCategory(workflowDefinitionCategory);

    WorkflowDefinition workflowDefinition =
        new WorkflowDefinition(
            "test_json_workflow_definition_" + randomId(),
            1,
            workflowDefinitionCategory.getId(),
            "Test JSON Workflow Definition",
            "flowable_embedded",
            ValidationSchemaType.JSON,
            ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    workflowDefinition.addDocumentDefinition(documentDefinition.getId(), true, false);

    workflowDefinition.addAttribute(
        new WorkflowDefinitionAttribute("process_id", UUID.randomUUID().toString()));

    workflowService.createWorkflowDefinition(workflowDefinition);

    TestWorkflowData testWorkflowData =
        new TestWorkflowData(
            UUID.randomUUID(),
            "This is name " + randomId(),
            LocalDate.of(1976, 3, 7),
            new BigDecimal("1234.56"),
            OffsetDateTime.now());

    String testWorkflowDataJson = objectMapper.writeValueAsString(testWorkflowData);

    CreateWorkflowRequest createWorkflowRequest =
        new CreateWorkflowRequest(
            workflowDefinition.getId(), UUID.randomUUID().toString(), testWorkflowDataJson);

    Workflow workflow =
        workflowService.createWorkflow(
            TenantUtil.DEFAULT_TENANT_ID, createWorkflowRequest, "TEST1");

    assertTrue(workflowService.workflowExists(TenantUtil.DEFAULT_TENANT_ID, workflow.getId()));

    Workflow retrievedWorkflow =
        workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    compareWorkflows(workflow, retrievedWorkflow);

    WorkflowSummaries workflowSummaries =
        workflowService.getWorkflowSummaries(
            TenantUtil.DEFAULT_TENANT_ID,
            workflow.getDefinitionId(),
            WorkflowStatus.IN_PROGRESS,
            null,
            WorkflowSortBy.DEFINITION_ID,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, workflowSummaries.getTotal());

    WorkflowSummary workflowSummary = workflowSummaries.getWorkflowSummaries().getFirst();

    assertEquals(
        retrievedWorkflow.getDefinitionId(),
        workflowSummary.getDefinitionId(),
        "Invalid value for the \"definitionId\" workflow summary property");
    assertEquals(
        retrievedWorkflow.getDefinitionVersion(),
        workflowSummary.getDefinitionVersion(),
        "Invalid value for the \"definitionVersion\" workflow summary property");
    assertEquals(
        retrievedWorkflow.getExternalReference(),
        workflowSummary.getExternalReference(),
        "Invalid value for the \"externalReference\" workflow summary property");
    assertEquals(
        retrievedWorkflow.getId(),
        workflowSummary.getId(),
        "Invalid value for the \"id\" workflow summary property");
    assertEquals(
        retrievedWorkflow.getParentId(),
        workflowSummary.getParentId(),
        "Invalid value for the \"parentId\" workflow summary property");
    assertEquals(
        retrievedWorkflow.getStatus(),
        workflowSummary.getStatus(),
        "Invalid value for the \"status\" workflow summary property");
    assertEquals(
        retrievedWorkflow.getTenantId(),
        workflowSummary.getTenantId(),
        "Invalid value for the \"tenantId\" workflow summary property");

    testWorkflowData.setName(testWorkflowData.getName() + " Updated");

    testWorkflowDataJson = objectMapper.writeValueAsString(testWorkflowData);

    UpdateWorkflowRequest updateWorkflowRequest =
        new UpdateWorkflowRequest(workflow.getId(), WorkflowStatus.COMPLETED, testWorkflowDataJson);

    Workflow updatedWorkflow =
        workflowService.updateWorkflow(
            TenantUtil.DEFAULT_TENANT_ID, updateWorkflowRequest, "TEST2");

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(WorkflowStatus.COMPLETED, retrievedWorkflow.getStatus());
    assertEquals(testWorkflowDataJson, retrievedWorkflow.getData());

    CreateWorkflowNoteRequest createWorkflowNoteRequest =
        new CreateWorkflowNoteRequest(workflow.getId(), "This is the workflow note content.");

    WorkflowNote workflowNote =
        workflowService.createWorkflowNote(
            TenantUtil.DEFAULT_TENANT_ID, createWorkflowNoteRequest, "TEST1");

    assertTrue(
        workflowService.workflowNoteExists(
            TenantUtil.DEFAULT_TENANT_ID, workflowNote.getWorkflowId(), workflowNote.getId()));

    WorkflowNote retrievedWorkflowNote =
        workflowService.getWorkflowNote(TenantUtil.DEFAULT_TENANT_ID, workflowNote.getId());

    compareWorkflowNotes(workflowNote, retrievedWorkflowNote);

    UpdateWorkflowNoteRequest updateWorkflowNoteRequest =
        new UpdateWorkflowNoteRequest(workflowNote.getId(), "This is the workflow note content.");

    WorkflowNote updatedWorkflowNote =
        workflowService.updateWorkflowNote(
            TenantUtil.DEFAULT_TENANT_ID, updateWorkflowNoteRequest, "TEST2");

    retrievedWorkflowNote =
        workflowService.getWorkflowNote(TenantUtil.DEFAULT_TENANT_ID, updatedWorkflowNote.getId());

    compareWorkflowNotes(updatedWorkflowNote, retrievedWorkflowNote);

    WorkflowNotes workflowNotes =
        workflowService.getWorkflowNotes(
            TenantUtil.DEFAULT_TENANT_ID,
            workflow.getId(),
            "TEST2",
            WorkflowNoteSortBy.CREATED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, workflowNotes.getTotal());

    compareWorkflowNotes(updatedWorkflowNote, workflowNotes.getWorkflowNotes().getFirst());

    workflowService.deleteWorkflowNote(TenantUtil.DEFAULT_TENANT_ID, workflowNote.getId());

    assertThrows(
        WorkflowNoteNotFoundException.class,
        () -> {
          workflowService.getWorkflowNote(TenantUtil.DEFAULT_TENANT_ID, workflowNote.getId());
        });

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
            "test_workflow_engine_attribute_name_" + randomId(),
            "test_workflow_engine_attribute_value");

    List<WorkflowEngineAttribute> workflowEngineAttributes = List.of(workflowEngineAttribute);

    WorkflowEngine workflowEngine =
        new WorkflowEngine(
            "test_workflow_engine_" + randomId(),
            "Test Workflow Engine",
            "digital.inception.operations.test.TestWorkflowEngineConnector",
            workflowEngineAttributes);

    workflowService.createWorkflowEngine(workflowEngine);

    assertTrue(workflowService.workflowEngineExists(workflowEngine.getId()));

    WorkflowEngine retrievedWorkflowEngine =
        workflowService.getWorkflowEngine(workflowEngine.getId());

    compareWorkflowEngines(workflowEngine, retrievedWorkflowEngine);

    List<WorkflowEngine> workflowEngines = workflowService.getWorkflowEngines();

    assertEquals(2, workflowEngines.size());

    compareWorkflowEngines(workflowEngine, workflowEngines.get(1));

    DocumentDefinitionCategory sharedDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_shared_document_definition_category_" + randomId(),
            "Test Shared Document Definition Category");

    documentService.createDocumentDefinitionCategory(sharedDocumentDefinitionCategory);

    DocumentDefinitionCategory tenantDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_tenant_document_definition_category",
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition Category");

    documentService.createDocumentDefinitionCategory(tenantDocumentDefinitionCategory);

    DocumentDefinition sharedDocumentDefinition =
        new DocumentDefinition(
            "test_shared_document_definition_" + randomId(),
            sharedDocumentDefinitionCategory.getId(),
            "Test Shared Document Definition",
            List.of(
                RequiredDocumentAttribute.EXPIRY_DATE,
                RequiredDocumentAttribute.EXTERNAL_REFERENCE,
                RequiredDocumentAttribute.ISSUE_DATE));

    documentService.createDocumentDefinition(sharedDocumentDefinition);

    DocumentDefinition tenantDocumentDefinition =
        new DocumentDefinition(
            "test_tenant_document_definition_" + randomId(),
            tenantDocumentDefinitionCategory.getId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition");

    documentService.createDocumentDefinition(tenantDocumentDefinition);

    WorkflowDefinitionCategory sharedWorkflowDefinitionCategory =
        new WorkflowDefinitionCategory(
            "test_shared_workflow_definition_category_" + randomId(),
            "Test Shared Workflow Definition Category");

    workflowService.createWorkflowDefinitionCategory(sharedWorkflowDefinitionCategory);

    assertTrue(
        workflowService.workflowDefinitionCategoryExists(sharedWorkflowDefinitionCategory.getId()));

    WorkflowDefinitionCategory retrievedWorkflowDefinitionCategory =
        workflowService.getWorkflowDefinitionCategory(sharedWorkflowDefinitionCategory.getId());

    compareWorkflowDefinitionCategories(
        sharedWorkflowDefinitionCategory, retrievedWorkflowDefinitionCategory);

    WorkflowDefinitionCategory tenantWorkflowDefinitionCategory =
        new WorkflowDefinitionCategory(
            "test_tenant_workflow_definition_category_" + randomId(),
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
            "test_shared_workflow_definition_" + randomId(),
            1,
            sharedWorkflowDefinitionCategory.getId(),
            "Test Shared Workflow Definition",
            workflowEngine.getId(),
            ValidationSchemaType.JSON,
            ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    sharedWorkflowDefinition.addDocumentDefinition(sharedDocumentDefinition.getId(), true, true);

    workflowService.createWorkflowDefinition(sharedWorkflowDefinition);

    assertTrue(workflowService.workflowDefinitionExists(sharedWorkflowDefinition.getId()));

    assertTrue(
        workflowService.workflowDefinitionExists(
            sharedWorkflowDefinition.getCategoryId(), sharedWorkflowDefinition.getId()));

    WorkflowDefinition retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(sharedWorkflowDefinition.getId());

    compareWorkflowDefinitions(sharedWorkflowDefinition, retrievedWorkflowDefinition);

    WorkflowDefinition tenantWorkflowDefinition =
        new WorkflowDefinition(
            "test_tenant_workflow_definition_" + randomId(),
            1,
            tenantWorkflowDefinitionCategory.getId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Workflow Definition",
            workflowEngine.getId(),
            ValidationSchemaType.JSON,
            ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    tenantWorkflowDefinition.addAttribute(
        new WorkflowDefinitionAttribute("attribute_name", "attribute_value"));
    tenantWorkflowDefinition.addAttribute(
        new WorkflowDefinitionAttribute("another_attribute_name", "another_attribute_value"));

    tenantWorkflowDefinition.addDocumentDefinition(tenantDocumentDefinition.getId(), true, false);
    tenantWorkflowDefinition.addDocumentDefinition(
        sharedDocumentDefinition.getId(), true, true, TimeUnit.MONTHS, 6);

    workflowService.createWorkflowDefinition(tenantWorkflowDefinition);

    retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(tenantWorkflowDefinition.getId());

    compareWorkflowDefinitions(tenantWorkflowDefinition, retrievedWorkflowDefinition);

    List<WorkflowDefinitionSummary> workflowDefinitionSummaries =
        workflowService.getWorkflowDefinitionSummaries(
            TenantUtil.DEFAULT_TENANT_ID, sharedWorkflowDefinitionCategory.getId());

    assertEquals(1, workflowDefinitionSummaries.size());
    assertEquals(sharedWorkflowDefinition.getId(), workflowDefinitionSummaries.getFirst().getId());

    workflowDefinitionSummaries =
        workflowService.getWorkflowDefinitionSummaries(
            TenantUtil.DEFAULT_TENANT_ID, tenantWorkflowDefinitionCategory.getId());
    assertEquals(1, workflowDefinitionSummaries.size());
    assertEquals(tenantWorkflowDefinition.getId(), workflowDefinitionSummaries.getFirst().getId());

    workflowDefinitionSummaries =
        workflowService.getWorkflowDefinitionSummaries(
            UUID.randomUUID(), tenantWorkflowDefinitionCategory.getId());
    assertEquals(0, workflowDefinitionSummaries.size());

    assertThrows(
        DuplicateWorkflowDefinitionVersionException.class,
        () -> {
          workflowService.createWorkflowDefinition(tenantWorkflowDefinition);
        });

    tenantWorkflowDefinition.setVersion(tenantWorkflowDefinition.getVersion() + 1);
    workflowService.createWorkflowDefinition(tenantWorkflowDefinition);

    workflowDefinitionSummaries =
        workflowService.getWorkflowDefinitionSummaries(
            TenantUtil.DEFAULT_TENANT_ID, tenantWorkflowDefinitionCategory.getId());

    assertEquals(1, workflowDefinitionSummaries.size());
    assertEquals(tenantWorkflowDefinition.getId(), workflowDefinitionSummaries.getFirst().getId());
    assertEquals(2, workflowDefinitionSummaries.getFirst().getVersion());

    retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(tenantWorkflowDefinition.getId());

    assertEquals(2, retrievedWorkflowDefinition.getVersion());

    tenantWorkflowDefinition.setName("Updated Test Tenant Workflow Definition");
    tenantWorkflowDefinition.removeDocumentDefinition(sharedDocumentDefinition.getId());

    tenantWorkflowDefinition.addAttribute(
        new WorkflowDefinitionAttribute("attribute_name", "updated_attribute_value"));

    tenantWorkflowDefinition.removeAttributeWithName("another_attribute_name");

    workflowService.updateWorkflowDefinition(tenantWorkflowDefinition);

    retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(tenantWorkflowDefinition.getId());

    compareWorkflowDefinitions(tenantWorkflowDefinition, retrievedWorkflowDefinition);

    workflowService.deleteWorkflowDefinitionVersion(tenantWorkflowDefinition.getId(), 2);

    retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinitionVersion(tenantWorkflowDefinition.getId(), 1);

    assertEquals(tenantWorkflowDefinition.getId(), retrievedWorkflowDefinition.getId());
    assertEquals(1, retrievedWorkflowDefinition.getVersion());

    retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(tenantWorkflowDefinition.getId());

    assertEquals(1, retrievedWorkflowDefinition.getVersion());

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

    workflowEngineAttribute =
        new WorkflowEngineAttribute(
            "test_workflow_engine_attribute_name_" + randomId(),
            "test_workflow_engine_attribute_value");

    workflowEngine.addAttribute(workflowEngineAttribute);
    workflowEngine.setName("Test Workflow Engine Updated");

    workflowService.updateWorkflowEngine(workflowEngine);

    retrievedWorkflowEngine = workflowService.getWorkflowEngine(workflowEngine.getId());

    compareWorkflowEngines(workflowEngine, retrievedWorkflowEngine);

    workflowService.deleteWorkflowEngine(workflowEngine.getId());

    assertThrows(
        WorkflowEngineNotFoundException.class,
        () -> {
          workflowService.deleteWorkflowEngine(workflowEngine.getId());
        });
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
        workflowDefinition1.getAttributes().size(),
        workflowDefinition2.getAttributes().size(),
        "The number of attributes for the workflow definitions do not match");
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
        .getAttributes()
        .forEach(
            attribute1 -> {
              boolean foundAttribute =
                  workflowDefinition2.getAttributes().stream()
                      .anyMatch(
                          attribute2 -> Objects.equals(attribute1.getName(), attribute2.getName()));
              if (!foundAttribute) {
                fail(
                    "Failed to find the attribute ("
                        + attribute1.getName()
                        + ") for the workflow definition ("
                        + workflowDefinition1.getId()
                        + ") version ("
                        + workflowDefinition1.getVersion()
                        + ")");
              }
            });

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

  private void compareWorkflowNotes(WorkflowNote workflowNote1, WorkflowNote workflowNote2) {
    assertEquals(
        workflowNote1.getId(),
        workflowNote2.getId(),
        "The ID values for the workflow notes do not match");
    assertEquals(
        workflowNote1.getContent(),
        workflowNote2.getContent(),
        "The content values for the workflow notes do not match");
    assertEquals(
        workflowNote1.getCreated(),
        workflowNote2.getCreated(),
        "The created values for the workflow notes do not match");
    assertEquals(
        workflowNote1.getCreatedBy(),
        workflowNote2.getCreatedBy(),
        "The created by values for the workflow notes do not match");
    assertEquals(
        workflowNote1.getUpdated(),
        workflowNote2.getUpdated(),
        "The updated values for the workflow notes do not match");
    assertEquals(
        workflowNote1.getUpdatedBy(),
        workflowNote2.getUpdatedBy(),
        "The updated by values for the workflow notes do not match");
  }

  private void compareWorkflows(Workflow workflow1, Workflow workflow2) {
    assertEquals(
        workflow1.getCreated(),
        workflow2.getCreated(),
        "The created values for the workflows do not match");
    assertEquals(
        workflow1.getCreatedBy(),
        workflow2.getCreatedBy(),
        "The created by values for the workflows do not match");
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
    assertEquals(
        workflow1.getUpdated(),
        workflow2.getUpdated(),
        "The updated values for the workflows do not match");
    assertEquals(
        workflow1.getUpdatedBy(),
        workflow2.getUpdatedBy(),
        "The updated by values for the workflows do not match");
  }

  private String randomId() {
    return String.format("%04X", secureRandom.nextInt(0x10000));
  }
}

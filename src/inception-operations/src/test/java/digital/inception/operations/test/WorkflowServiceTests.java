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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.file.FileType;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.ResourceUtil;
import digital.inception.core.util.StringUtil;
import digital.inception.core.util.TenantUtil;
import digital.inception.core.validation.ValidationSchemaType;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.WorkflowAttributeDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.DocumentAttributeDefinition;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.RequiredDocumentAttribute;
import digital.inception.operations.model.SuspendWorkflowRequest;
import digital.inception.operations.model.SuspendWorkflowStepRequest;
import digital.inception.operations.model.UnsuspendWorkflowRequest;
import digital.inception.operations.model.UnsuspendWorkflowStepRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowAttributeDefinition;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionAttribute;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionDocumentDefinition;
import digital.inception.operations.model.WorkflowDefinitionPermission;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocumentStatus;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowEngineAttribute;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowPermissionType;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStepDefinition;
import digital.inception.operations.model.WorkflowStepStatus;
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
    // Create the document attribute definition
    DocumentAttributeDefinition documentAttributeDefinition =
        new DocumentAttributeDefinition(
            "test_document_attribute_code",
            "Test Document Attribute",
            true,
            null,
            TenantUtil.DEFAULT_TENANT_ID);

    documentService.createDocumentAttributeDefinition(documentAttributeDefinition);

    // Create the document definition category
    DocumentDefinitionCategory documentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_document_definition_category_" + randomId(), "Test Document Definition Category");

    documentService.createDocumentDefinitionCategory(documentDefinitionCategory);

    // Create the document definitions
    DocumentDefinition documentDefinition =
        new DocumentDefinition(
            "test_document_definition_" + randomId(),
            documentDefinitionCategory.getId(),
            "Test Document Definition",
            List.of(
                RequiredDocumentAttribute.EXPIRY_DATE,
                RequiredDocumentAttribute.EXTERNAL_REFERENCE,
                RequiredDocumentAttribute.ISSUE_DATE));

    documentService.createDocumentDefinition(documentDefinition);

    DocumentDefinition anotherDocumentDefinition =
        new DocumentDefinition(
            "another_test_document_definition_" + randomId(),
            documentDefinitionCategory.getId(),
            "Another Test Document Definition");

    documentService.createDocumentDefinition(anotherDocumentDefinition);

    // Create the workflow attribute definition
    WorkflowAttributeDefinition workflowAttributeDefinition =
        new WorkflowAttributeDefinition(
            "test_workflow_attribute_code",
            "Test Workflow Attribute",
            true,
            null,
            TenantUtil.DEFAULT_TENANT_ID);

    workflowService.createWorkflowAttributeDefinition(workflowAttributeDefinition);

    // Create the workflow definition category
    WorkflowDefinitionCategory workflowDefinitionCategory =
        new WorkflowDefinitionCategory(
            "test_workflow_definition_category_" + randomId(), "Test Workflow Definition Category");

    workflowService.createWorkflowDefinitionCategory(workflowDefinitionCategory);

    // Create the workflow definition
    WorkflowDefinition workflowDefinition =
        new WorkflowDefinition(
            "test_json_workflow_definition_" + randomId(),
            1,
            workflowDefinitionCategory.getId(),
            "Test JSON Workflow Definition",
            "flowable_embedded",
            ValidationSchemaType.JSON,
            ResourceUtil.getStringClasspathResource("TestData.schema.json"));

    workflowDefinition.addDocumentDefinition(documentDefinition.getId(), true, false, true);
    workflowDefinition.addDocumentDefinition(anotherDocumentDefinition.getId(), false, true, false);

    workflowDefinition.addStepDefinition(
        new WorkflowStepDefinition(
            1,
            "test_workflow_step_1",
            "Test Workflow Step 1",
            "The description for Test Workflow Step 1",
            false,
            false));

    workflowDefinition.addStepDefinition(
        new WorkflowStepDefinition(
            2,
            "test_workflow_step_2",
            "Test Workflow Step 2",
            "The description for Test Workflow Step 2",
            false,
            true,
            "P1D"));

    workflowDefinition.addStepDefinition(
        new WorkflowStepDefinition(
            3,
            "test_workflow_step_3",
            "Test Workflow Step 3",
            "The description for Test Workflow Step 3",
            false,
            false));

    workflowDefinition.addAttribute(
        new WorkflowDefinitionAttribute("process_id", UUID.randomUUID().toString()));

    workflowService.createWorkflowDefinition(workflowDefinition);

    // Initiate the workflow
    TestWorkflowData testWorkflowData =
        new TestWorkflowData(
            UUID.randomUUID(),
            "This is name " + randomId(),
            LocalDate.of(1976, 3, 7),
            new BigDecimal("1234.56"),
            OffsetDateTime.now());

    String testWorkflowDataJson = objectMapper.writeValueAsString(testWorkflowData);

    InitiateWorkflowRequest initiateWorkflowRequest =
        new InitiateWorkflowRequest(
            workflowDefinition.getId(),
            UUID.randomUUID().toString(),
            List.of(
                new WorkflowAttribute(
                    "test_workflow_attribute_code", "test_workflow_attribute_value")),
            testWorkflowDataJson);

    Workflow workflow =
        workflowService.initiateWorkflow(
            TenantUtil.DEFAULT_TENANT_ID, initiateWorkflowRequest, "TEST1");

    // Verify the workflow exists
    assertTrue(workflowService.workflowExists(TenantUtil.DEFAULT_TENANT_ID, workflow.getId()));

    // Retrieve the workflow
    Workflow retrievedWorkflow =
        workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    compareWorkflows(workflow, retrievedWorkflow);

    // Retrieve the workflow summaries, matching on the workflow attribute value
    WorkflowSummaries workflowSummaries =
        workflowService.getWorkflowSummaries(
            TenantUtil.DEFAULT_TENANT_ID,
            workflow.getDefinitionId(),
            WorkflowStatus.ACTIVE,
            "test_workflow_attribute_value",
            WorkflowSortBy.INITIATED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, workflowSummaries.getTotal());

    // Retrieve the workflow summary
    WorkflowSummary workflowSummary = workflowSummaries.getWorkflowSummaries().getFirst();

    assertEquals(
        retrievedWorkflow.getDefinitionId(),
        workflowSummary.getDefinitionId(),
        "Invalid value for the \"workflowDefinitionId\" workflow summary property");
    assertEquals(
        retrievedWorkflow.getDefinitionVersion(),
        workflowSummary.getDefinitionVersion(),
        "Invalid value for the \"workflowDefinitionVersion\" workflow summary property");
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

    // Retrieve the workflow documents for the workflow
    WorkflowDocuments retrievedWorkflowDocuments =
        workflowService.getWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID,
            workflow.getId(),
            "TEST1",
            WorkflowDocumentSortBy.REQUESTED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, retrievedWorkflowDocuments.getTotal());
    assertEquals(
        documentDefinition.getId(),
        retrievedWorkflowDocuments.getWorkflowDocuments().getFirst().getDocumentDefinitionId());
    assertEquals(
        "TEST1", retrievedWorkflowDocuments.getWorkflowDocuments().getFirst().getRequestedBy());

    // Retrieve the outstanding workflow documents for the workflow
    List<OutstandingWorkflowDocument> outstandingWorkflowDocuments =
        workflowService.getOutstandingWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(1, outstandingWorkflowDocuments.size());
    assertEquals(
        documentDefinition.getId(),
        outstandingWorkflowDocuments.getFirst().getDocumentDefinitionId());

    // Update the workflow
    testWorkflowData.setName(testWorkflowData.getName() + " Updated");

    testWorkflowDataJson = objectMapper.writeValueAsString(testWorkflowData);

    UpdateWorkflowRequest updateWorkflowRequest =
        new UpdateWorkflowRequest(
            workflow.getId(),
            WorkflowStatus.COMPLETED,
            List.of(
                new WorkflowAttribute(
                    "test_workflow_attribute_code", "test_workflow_attribute_value_updated")),
            testWorkflowDataJson);

    Workflow updatedWorkflow =
        workflowService.updateWorkflow(
            TenantUtil.DEFAULT_TENANT_ID, updateWorkflowRequest, "TEST2");

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(WorkflowStatus.COMPLETED, retrievedWorkflow.getStatus());
    assertEquals(testWorkflowDataJson, retrievedWorkflow.getData());
    assertEquals(
        "test_workflow_attribute_value_updated",
        retrievedWorkflow.getAttributes().getFirst().getValue());

    // Provide a workflow document
    byte[] multiPagePdfData = ResourceUtil.getClasspathResource("MultiPagePdf.pdf");

    ProvideWorkflowDocumentRequest provideWorkflowDocumentRequest =
        new ProvideWorkflowDocumentRequest(
            retrievedWorkflowDocuments.getWorkflowDocuments().getFirst().getId(),
            FileType.PDF,
            "MultiPagePdf.pdf",
            List.of(
                new DocumentAttribute(
                    "test_document_attribute_code", "test_document_attribute_value")),
            multiPagePdfData);

    provideWorkflowDocumentRequest.setExternalReference(UUID.randomUUID().toString());
    provideWorkflowDocumentRequest.setIssueDate(LocalDate.of(2015, 10, 10));
    provideWorkflowDocumentRequest.setExpiryDate(LocalDate.of(2050, 11, 11));
    provideWorkflowDocumentRequest.setDescription("The provide workflow document description.");

    workflowService.provideWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, provideWorkflowDocumentRequest, "TEST1");

    WorkflowDocument retrievedWorkflowDocument =
        workflowService.getWorkflowDocument(
            TenantUtil.DEFAULT_TENANT_ID, provideWorkflowDocumentRequest.getWorkflowDocumentId());

    assertEquals(WorkflowDocumentStatus.VERIFIABLE, retrievedWorkflowDocument.getStatus());
    assertEquals("TEST1", retrievedWorkflowDocument.getProvidedBy());
    assertNotNull(retrievedWorkflowDocument.getProvided());
    assertEquals(
        "The provide workflow document description.", retrievedWorkflowDocument.getDescription());

    assertEquals(
        retrievedWorkflowDocument.getWorkflowId(),
        workflowService.getWorkflowIdForWorkflowDocument(
            TenantUtil.DEFAULT_TENANT_ID, retrievedWorkflowDocument.getId()));

    Document retrievedDocument =
        documentService.getDocument(
            TenantUtil.DEFAULT_TENANT_ID, retrievedWorkflowDocument.getDocumentId());

    UUID retrievedDocumentId = retrievedDocument.getId();

    assertEquals(documentDefinition.getId(), retrievedDocument.getDefinitionId());
    assertEquals("MultiPagePdf.pdf", retrievedDocument.getName());
    assertEquals(FileType.PDF, retrievedDocument.getFileType());
    assertEquals(multiPagePdfData.length, retrievedDocument.getData().length);
    assertEquals(LocalDate.of(2015, 10, 10), retrievedDocument.getIssueDate());
    assertEquals(LocalDate.of(2050, 11, 11), retrievedDocument.getExpiryDate());
    assertEquals(1, retrievedDocument.getAttributes().size());
    assertEquals(
        "test_document_attribute_code", retrievedDocument.getAttributes().getFirst().getCode());
    assertEquals(
        "test_document_attribute_value", retrievedDocument.getAttributes().getFirst().getValue());

    // Provide the workflow document again, replacing the existing document
    provideWorkflowDocumentRequest.setName("AnotherMultiPagePdf.pdf");
    provideWorkflowDocumentRequest.setIssueDate(LocalDate.of(2016, 10, 10));
    provideWorkflowDocumentRequest.setExpiryDate(LocalDate.of(2060, 11, 11));

    workflowService.provideWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, provideWorkflowDocumentRequest, "TEST2");

    retrievedWorkflowDocument =
        workflowService.getWorkflowDocument(
            TenantUtil.DEFAULT_TENANT_ID, provideWorkflowDocumentRequest.getWorkflowDocumentId());

    assertFalse(documentService.documentExists(TenantUtil.DEFAULT_TENANT_ID, retrievedDocumentId));

    retrievedDocument =
        documentService.getDocument(
            TenantUtil.DEFAULT_TENANT_ID, retrievedWorkflowDocument.getDocumentId());

    assertNotEquals(retrievedDocumentId, retrievedDocument.getId());

    assertEquals(documentDefinition.getId(), retrievedDocument.getDefinitionId());
    assertEquals("AnotherMultiPagePdf.pdf", retrievedDocument.getName());
    assertEquals(FileType.PDF, retrievedDocument.getFileType());
    assertEquals(multiPagePdfData.length, retrievedDocument.getData().length);
    assertEquals(LocalDate.of(2016, 10, 10), retrievedDocument.getIssueDate());
    assertEquals(LocalDate.of(2060, 11, 11), retrievedDocument.getExpiryDate());

    // Verify the workflow document
    VerifyWorkflowDocumentRequest verifyWorkflowDocumentRequest =
        new VerifyWorkflowDocumentRequest(
            retrievedWorkflowDocument.getId(), "The verify workflow document description.");

    workflowService.verifyWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, verifyWorkflowDocumentRequest, "TEST1");

    retrievedWorkflowDocument =
        workflowService.getWorkflowDocument(
            TenantUtil.DEFAULT_TENANT_ID, provideWorkflowDocumentRequest.getWorkflowDocumentId());

    assertEquals(WorkflowDocumentStatus.VERIFIED, retrievedWorkflowDocument.getStatus());
    assertEquals("TEST1", retrievedWorkflowDocument.getVerifiedBy());
    assertNotNull(retrievedWorkflowDocument.getVerified());
    assertNull(retrievedWorkflowDocument.getRejected());
    assertNull(retrievedWorkflowDocument.getRejectedBy());
    assertNull(retrievedWorkflowDocument.getRejectionReason());
    assertEquals(
        "The verify workflow document description.", retrievedWorkflowDocument.getDescription());

    // Reject the workflow document
    RejectWorkflowDocumentRequest rejectWorkflowDocumentRequest =
        new RejectWorkflowDocumentRequest(
            retrievedWorkflowDocument.getId(),
            "This is a test rejection reason.",
            "The reject workflow document description.");

    workflowService.rejectWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, rejectWorkflowDocumentRequest, "TEST2");

    retrievedWorkflowDocument =
        workflowService.getWorkflowDocument(
            TenantUtil.DEFAULT_TENANT_ID, provideWorkflowDocumentRequest.getWorkflowDocumentId());

    assertEquals(WorkflowDocumentStatus.REJECTED, retrievedWorkflowDocument.getStatus());
    assertEquals("TEST2", retrievedWorkflowDocument.getRejectedBy());
    assertEquals(
        "This is a test rejection reason.", retrievedWorkflowDocument.getRejectionReason());
    assertNotNull(retrievedWorkflowDocument.getRejected());
    assertNull(retrievedWorkflowDocument.getVerified());
    assertNull(retrievedWorkflowDocument.getVerifiedBy());
    assertEquals(
        "The reject workflow document description.", retrievedWorkflowDocument.getDescription());

    // Request the workflow document
    RequestWorkflowDocumentRequest requestWorkflowDocumentRequest =
        new RequestWorkflowDocumentRequest(
            workflow.getId(),
            anotherDocumentDefinition.getId(),
            "The request workflow document description.");

    workflowService.requestWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, requestWorkflowDocumentRequest, "TEST1");

    retrievedWorkflowDocuments =
        workflowService.getWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID,
            workflow.getId(),
            "TEST1",
            WorkflowDocumentSortBy.REQUESTED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(2, retrievedWorkflowDocuments.getTotal());

    outstandingWorkflowDocuments =
        workflowService.getOutstandingWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(2, outstandingWorkflowDocuments.size());

    // Delete the workflow document
    workflowService.deleteWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, retrievedWorkflowDocument.getId());

    retrievedWorkflowDocuments =
        workflowService.getWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID,
            workflow.getId(),
            "TEST1",
            WorkflowDocumentSortBy.REQUESTED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, retrievedWorkflowDocuments.getTotal());

    outstandingWorkflowDocuments =
        workflowService.getOutstandingWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(1, outstandingWorkflowDocuments.size());

    // Create a workflow note for the workflow
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

    // Update the workflow note for the workflow
    UpdateWorkflowNoteRequest updateWorkflowNoteRequest =
        new UpdateWorkflowNoteRequest(workflowNote.getId(), "This is the workflow note content.");

    WorkflowNote updatedWorkflowNote =
        workflowService.updateWorkflowNote(
            TenantUtil.DEFAULT_TENANT_ID, updateWorkflowNoteRequest, "TEST2");

    retrievedWorkflowNote =
        workflowService.getWorkflowNote(TenantUtil.DEFAULT_TENANT_ID, updatedWorkflowNote.getId());

    compareWorkflowNotes(updatedWorkflowNote, retrievedWorkflowNote);

    // Retrieve the workflow notes for the workflow
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

    // Initiate the workflow step
    workflowService.initiateWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new InitiateWorkflowStepRequest(workflow.getId(), "test_workflow_step_1"));

    assertThrows(
        InvalidArgumentException.class,
        () -> {
          workflowService.initiateWorkflowStep(
              TenantUtil.DEFAULT_TENANT_ID,
              new InitiateWorkflowStepRequest(workflow.getId(), "test_workflow_step_invalid"));
        });

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(1, retrievedWorkflow.getSteps().size());
    assertEquals("test_workflow_step_1", retrievedWorkflow.getSteps().getFirst().getCode());
    assertEquals(WorkflowStepStatus.ACTIVE, retrievedWorkflow.getSteps().getFirst().getStatus());
    assertNotNull(retrievedWorkflow.getSteps().getFirst().getInitiated());

    // Suspend the workflow step
    workflowService.suspendWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new SuspendWorkflowStepRequest(workflow.getId(), "test_workflow_step_1"));

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(1, retrievedWorkflow.getSteps().size());
    assertEquals("test_workflow_step_1", retrievedWorkflow.getSteps().getFirst().getCode());
    assertEquals(WorkflowStepStatus.SUSPENDED, retrievedWorkflow.getSteps().getFirst().getStatus());
    assertNotNull(retrievedWorkflow.getSteps().getFirst().getSuspended());

    // Unsuspend the workflow step
    workflowService.unsuspendWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new UnsuspendWorkflowStepRequest(workflow.getId(), "test_workflow_step_1"));

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(1, retrievedWorkflow.getSteps().size());
    assertEquals("test_workflow_step_1", retrievedWorkflow.getSteps().getFirst().getCode());
    assertEquals(WorkflowStepStatus.ACTIVE, retrievedWorkflow.getSteps().getFirst().getStatus());
    assertNull(retrievedWorkflow.getSteps().getFirst().getSuspended());

    // Suspend the workflow
    workflowService.suspendWorkflow(
        TenantUtil.DEFAULT_TENANT_ID, new SuspendWorkflowRequest(workflow.getId()), "TEST1");

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(WorkflowStatus.SUSPENDED, retrievedWorkflow.getStatus());
    assertNotNull(retrievedWorkflow.getSuspended());
    assertEquals(1, retrievedWorkflow.getSteps().size());
    assertEquals("test_workflow_step_1", retrievedWorkflow.getSteps().getFirst().getCode());
    assertEquals(WorkflowStepStatus.SUSPENDED, retrievedWorkflow.getSteps().getFirst().getStatus());
    assertNotNull(retrievedWorkflow.getSteps().getFirst().getSuspended());

    // Unsuspend the workflow
    workflowService.unsuspendWorkflow(
        TenantUtil.DEFAULT_TENANT_ID, new UnsuspendWorkflowRequest(workflow.getId()));

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(WorkflowStatus.ACTIVE, retrievedWorkflow.getStatus());
    assertNull(retrievedWorkflow.getSuspended());
    assertEquals(1, retrievedWorkflow.getSteps().size());
    assertEquals("test_workflow_step_1", retrievedWorkflow.getSteps().getFirst().getCode());
    assertEquals(WorkflowStepStatus.ACTIVE, retrievedWorkflow.getSteps().getFirst().getStatus());
    assertNull(retrievedWorkflow.getSteps().getFirst().getSuspended());

    // Finalize the workflow step
    workflowService.finalizeWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new FinalizeWorkflowStepRequest(
            workflow.getId(), "test_workflow_step_1", WorkflowStepStatus.COMPLETED));

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(1, retrievedWorkflow.getSteps().size());
    assertEquals("test_workflow_step_1", retrievedWorkflow.getSteps().getFirst().getCode());
    assertEquals(WorkflowStepStatus.COMPLETED, retrievedWorkflow.getSteps().getFirst().getStatus());
    assertNotNull(retrievedWorkflow.getSteps().getFirst().getFinalized());

    // Finalize the workflow
    workflowService.finalizeWorkflow(
        TenantUtil.DEFAULT_TENANT_ID,
        new FinalizeWorkflowRequest(workflow.getId(), WorkflowStatus.COMPLETED),
        "TEST1");

    retrievedWorkflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(WorkflowStatus.COMPLETED, retrievedWorkflow.getStatus());
    assertNotNull(retrievedWorkflow.getFinalized());
    assertEquals("TEST1", retrievedWorkflow.getFinalizedBy());

    // Delete the workflow note
    workflowService.deleteWorkflowNote(TenantUtil.DEFAULT_TENANT_ID, workflowNote.getId());

    assertThrows(
        WorkflowNoteNotFoundException.class,
        () -> {
          workflowService.getWorkflowNote(TenantUtil.DEFAULT_TENANT_ID, workflowNote.getId());
        });

    // Delete the document linked to the workflow document
    documentService.deleteDocument(
        TenantUtil.DEFAULT_TENANT_ID, retrievedWorkflowDocument.getDocumentId());

    // Delete the workflow
    workflowService.deleteWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    // Delete the workflow definition
    workflowService.deleteWorkflowDefinition(workflowDefinition.getId());

    // Delete the workflow definition category
    workflowService.deleteWorkflowDefinitionCategory(workflowDefinitionCategory.getId());

    // Delete the workflow attribute definition
    workflowService.deleteWorkflowAttributeDefinition(workflowAttributeDefinition.getCode());

    // Delete the document definition
    documentService.deleteDocumentDefinition(documentDefinition.getId());

    // Delete the document definition category
    documentService.deleteDocumentDefinitionCategory(documentDefinitionCategory.getId());

    // Delete the document attribute definition
    documentService.deleteDocumentAttributeDefinition(documentAttributeDefinition.getCode());
  }

  /** Test the workflow service functionality. */
  @Test
  public void workflowServiceTest() throws Exception {
    WorkflowAttributeDefinition workflowAttributeDefinition =
        new WorkflowAttributeDefinition(
            "test_workflow_attribute_code",
            "Test Workflow Attribute",
            true,
            null,
            TenantUtil.DEFAULT_TENANT_ID);

    workflowService.createWorkflowAttributeDefinition(workflowAttributeDefinition);

    WorkflowAttributeDefinition retrievedWorkflowAttributeDefinition =
        workflowService.getWorkflowAttributeDefinition(workflowAttributeDefinition.getCode());

    compareWorkflowAttributeDefinitions(
        workflowAttributeDefinition, retrievedWorkflowAttributeDefinition);

    List<WorkflowAttributeDefinition> workflowAttributeDefinitions =
        workflowService.getWorkflowAttributeDefinitions(TenantUtil.DEFAULT_TENANT_ID);

    assertEquals(1, workflowAttributeDefinitions.size());

    compareWorkflowAttributeDefinitions(
        workflowAttributeDefinition, workflowAttributeDefinitions.getFirst());

    workflowAttributeDefinition.setDescription("Updated Test Workflow Attribute");

    workflowService.updateWorkflowAttributeDefinition(workflowAttributeDefinition);

    retrievedWorkflowAttributeDefinition =
        workflowService.getWorkflowAttributeDefinition(workflowAttributeDefinition.getCode());

    compareWorkflowAttributeDefinitions(
        workflowAttributeDefinition, retrievedWorkflowAttributeDefinition);

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

    sharedWorkflowDefinition.addDocumentDefinition(
        sharedDocumentDefinition.getId(), true, true, true);

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

    tenantWorkflowDefinition.addStepDefinition(
        new WorkflowStepDefinition(
            1,
            "test_workflow_step_1",
            "Test Workflow Step 1",
            "The description for Test Workflow Step 1",
            false,
            false));

    tenantWorkflowDefinition.addStepDefinition(
        new WorkflowStepDefinition(
            2,
            "test_workflow_step_2",
            "Test Workflow Step 2",
            "The description for Test Workflow Step 2",
            false,
            false));

    tenantWorkflowDefinition.addStepDefinition(
        new WorkflowStepDefinition(
            3,
            "test_workflow_step_3",
            "Test Workflow Step 3",
            "The description for Test Workflow Step 3",
            false,
            false));

    tenantWorkflowDefinition.addDocumentDefinition(
        tenantDocumentDefinition.getId(), true, false, true);
    tenantWorkflowDefinition.addDocumentDefinition(
        sharedDocumentDefinition.getId(), true, true, true, "P3M");

    tenantWorkflowDefinition.setPermissions(
        List.of(
            new WorkflowDefinitionPermission(
                "Administrator", WorkflowPermissionType.INITIATE_WORKFLOW)));

    workflowService.createWorkflowDefinition(tenantWorkflowDefinition);

    retrievedWorkflowDefinition =
        workflowService.getWorkflowDefinition(tenantWorkflowDefinition.getId());

    compareWorkflowDefinitions(tenantWorkflowDefinition, retrievedWorkflowDefinition);

    List<WorkflowDefinitionPermission> retrievedWorkflowDefinitionPermissions =
        workflowService.getWorkflowDefinitionPermissions(
            tenantWorkflowDefinition.getId(), tenantWorkflowDefinition.getVersion());

    assertEquals(1, retrievedWorkflowDefinitionPermissions.size());

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

    tenantWorkflowDefinition.removeAttribute("another_attribute_name");

    tenantWorkflowDefinition.removeStepDefinition("test_workflow_step_2");

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

    workflowService.deleteWorkflowAttributeDefinition(workflowAttributeDefinition.getCode());

    assertThrows(
        WorkflowAttributeDefinitionNotFoundException.class,
        () -> {
          workflowService.getWorkflowAttributeDefinition(workflowAttributeDefinition.getCode());
        });
  }

  private void compareWorkflowAttributeDefinitions(
      WorkflowAttributeDefinition workflowAttributeDefinition1,
      WorkflowAttributeDefinition workflowAttributeDefinition2) {
    assertEquals(
        workflowAttributeDefinition1.getCode(),
        workflowAttributeDefinition2.getCode(),
        "The code values for the workflow attribute definitions do not match");
    assertEquals(
        workflowAttributeDefinition1.getDescription(),
        workflowAttributeDefinition2.getDescription(),
        "The description values for the workflow attribute definitions do not match");
    assertEquals(
        workflowAttributeDefinition1.getWorkflowDefinitionId(),
        workflowAttributeDefinition2.getWorkflowDefinitionId(),
        "The workflow definition ID values for the workflow attribute definitions do not match");
    assertEquals(
        workflowAttributeDefinition1.isRequired(),
        workflowAttributeDefinition2.isRequired(),
        "The required values for the workflow attribute definitions do not match");
    assertEquals(
        workflowAttributeDefinition1.getTenantId(),
        workflowAttributeDefinition2.getTenantId(),
        "The tenant ID values for the workflow attribute definitions do not match");
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

  private void compareWorkflowDefinitionDocumentDefinitions(
      WorkflowDefinitionDocumentDefinition workflowDefinitionDocumentDefinition1,
      WorkflowDefinitionDocumentDefinition workflowDefinitionDocumentDefinition2) {
    assertEquals(
        workflowDefinitionDocumentDefinition1.getDocumentDefinitionId(),
        workflowDefinitionDocumentDefinition2.getDocumentDefinitionId(),
        "The document definition ID values for the workflow definition document definitions do not match");
    assertEquals(
        workflowDefinitionDocumentDefinition1.isRequired(),
        workflowDefinitionDocumentDefinition2.isRequired(),
        "The required values for the workflow definition document definitions do not match");
    assertEquals(
        workflowDefinitionDocumentDefinition1.isSingular(),
        workflowDefinitionDocumentDefinition2.isSingular(),
        "The singular values for the workflow definition document definitions do not match");
    assertEquals(
        workflowDefinitionDocumentDefinition1.getValidityPeriod(),
        workflowDefinitionDocumentDefinition2.getValidityPeriod(),
        "The validity period values for the workflow definition document definitions do not match");
    assertEquals(
        workflowDefinitionDocumentDefinition1.isVerifiable(),
        workflowDefinitionDocumentDefinition2.isVerifiable(),
        "The verifiable values for the workflow definition document definitions do not match");
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
        "The workflow engine ID values for the workflow definitions do not match");
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
        workflowDefinition1.getAttributes().size(),
        workflowDefinition2.getAttributes().size(),
        "The number of attributes for the workflow definitions do not match");

    workflowDefinition1
        .getAttributes()
        .forEach(
            attribute1 -> {
              boolean foundAttribute =
                  workflowDefinition2.getAttributes().stream()
                      .anyMatch(
                          attribute2 ->
                              StringUtil.equalsIgnoreCase(
                                  attribute1.getCode(), attribute2.getCode()));
              if (!foundAttribute) {
                fail(
                    "Failed to find the attribute ("
                        + attribute1.getCode()
                        + ") for the workflow definition ("
                        + workflowDefinition1.getId()
                        + ") version ("
                        + workflowDefinition1.getVersion()
                        + ")");
              }
            });

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
    workflowDefinition1
        .getDocumentDefinitions()
        .forEach(
            workflowDefinitionDocumentDefinition1 -> {
              workflowDefinition2
                  .getDocumentDefinitions()
                  .forEach(
                      workflowDefinitionDocumentDefinition2 -> {
                        if (Objects.equals(
                            workflowDefinitionDocumentDefinition1,
                            workflowDefinitionDocumentDefinition2)) {
                          compareWorkflowDefinitionDocumentDefinitions(
                              workflowDefinitionDocumentDefinition1,
                              workflowDefinitionDocumentDefinition2);
                        }
                      });
            });

    assertEquals(
        workflowDefinition1.getStepDefinitions().size(),
        workflowDefinition2.getStepDefinitions().size(),
        "The number of step definitions for the workflow definitions do not match");
    workflowDefinition1
        .getStepDefinitions()
        .forEach(
            workflowStepDefinition1 -> {
              boolean foundStepDefinition =
                  workflowDefinition2.getStepDefinitions().stream()
                      .anyMatch(
                          workflowStepDefinition2 ->
                              Objects.equals(workflowStepDefinition1, workflowStepDefinition2));
              if (!foundStepDefinition) {
                fail(
                    "Failed to find the workflow step definition ("
                        + workflowStepDefinition1.getCode()
                        + ") for the workflow definition ("
                        + workflowDefinition1.getId()
                        + ") version ("
                        + workflowDefinition1.getVersion()
                        + ")");
              }
            });
    workflowDefinition1
        .getStepDefinitions()
        .forEach(
            workflowStepDefinition1 -> {
              workflowDefinition2
                  .getStepDefinitions()
                  .forEach(
                      workflowStepDefinition2 -> {
                        if (Objects.equals(workflowStepDefinition1, workflowStepDefinition2)) {
                          compareWorkflowStepDefinitions(
                              workflowStepDefinition1, workflowStepDefinition2);
                        }
                      });
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
    assertEquals(
        workflowEngine1.getAttributes().size(),
        workflowEngine2.getAttributes().size(),
        "The number of attributes for the workflow engines do not match");

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
                        + workflowEngineAttribute1.getCode()
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

  private void compareWorkflowStepDefinitions(
      WorkflowStepDefinition workflowStepDefinition1,
      WorkflowStepDefinition workflowStepDefinition2) {
    assertEquals(
        workflowStepDefinition1.getCode(),
        workflowStepDefinition2.getCode(),
        "The code values for the workflow step definitions do not match");
    assertEquals(
        workflowStepDefinition1.getDescription(),
        workflowStepDefinition2.getDescription(),
        "The description values for the workflow step definitions do not match");
    assertEquals(
        workflowStepDefinition1.getName(),
        workflowStepDefinition2.getName(),
        "The name values for the workflow step definitions do not match");
    assertEquals(
        workflowStepDefinition1.getSequence(),
        workflowStepDefinition2.getSequence(),
        "The sequence values for the workflow step definitions do not match");
    assertEquals(
        workflowStepDefinition1.getTimeToComplete(),
        workflowStepDefinition2.getTimeToComplete(),
        "The time to complete values for the workflow step definitions do not match");
  }

  private void compareWorkflows(Workflow workflow1, Workflow workflow2) {
    assertEquals(
        workflow1.getInitiated(),
        workflow2.getInitiated(),
        "The created values for the workflows do not match");
    assertEquals(
        workflow1.getInitiatedBy(),
        workflow2.getInitiatedBy(),
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
        workflow1.getPartyId(),
        workflow2.getPartyId(),
        "The party ID values for the workflows do not match");
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

    assertEquals(
        workflow1.getAttributes().size(),
        workflow2.getAttributes().size(),
        "The number of attributes for the workflows do not match");

    workflow1
        .getAttributes()
        .forEach(
            workflowAttribute1 -> {
              boolean foundAttribute =
                  workflow2.getAttributes().stream()
                      .anyMatch(
                          workflowAttribute2 ->
                              Objects.equals(workflowAttribute1, workflowAttribute2));
              if (!foundAttribute) {
                fail(
                    "Failed to find the attribute ("
                        + workflowAttribute1.getCode()
                        + ") for the workflow ("
                        + workflow1.getId()
                        + ")");
              }
            });
  }

  private String randomId() {
    return String.format("%04X", secureRandom.nextInt(0x10000));
  }
}

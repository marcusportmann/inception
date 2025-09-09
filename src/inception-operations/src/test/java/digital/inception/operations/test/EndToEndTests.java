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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import digital.inception.core.file.FileType;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.ResourceUtil;
import digital.inception.core.util.TenantUtil;
import digital.inception.core.validation.ValidationSchemaType;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.model.CreateInteractionNoteRequest;
import digital.inception.operations.model.DelinkInteractionFromWorkflowRequest;
import digital.inception.operations.model.DelinkPartyFromInteractionRequest;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.DocumentAttributeDefinition;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.DocumentDefinitionSummary;
import digital.inception.operations.model.DocumentExternalReference;
import digital.inception.operations.model.ExternalReferenceType;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.InitiateWorkflowInteractionLink;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionAttachmentSummary;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.LinkInteractionToWorkflowRequest;
import digital.inception.operations.model.LinkPartyToInteractionRequest;
import digital.inception.operations.model.MailboxProtocol;
import digital.inception.operations.model.ObjectType;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.UpdateInteractionNoteRequest;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowAttributeDefinition;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionAttribute;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowExternalReference;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStepDefinition;
import digital.inception.operations.model.WorkflowStepStatus;
import digital.inception.operations.service.BackgroundInteractionSourceSynchronizer;
import digital.inception.operations.service.DocumentService;
import digital.inception.operations.service.InteractionService;
import digital.inception.operations.service.OperationsReferenceService;
import digital.inception.operations.service.WorkflowService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
 * The {@code EndToEndTests} class.
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
public class EndToEndTests {

  private static final boolean ENABLE_GREEN_MAIL_SECURITY = false;

  private static final String FIRST_FROM_EMAIL_ADDRESS = "bsmith@example.com";

  private static final String FIRST_FROM_NAME = "Bob Smith";

  private static final String FIRST_TO_EMAIL_ADDRESS = "service@fitlife.com";

  private static final String FIRST_TO_NAME = "FitLife Customer Service";

  private static final String SECOND_FROM_EMAIL_ADDRESS = "sbloggs@example.com";

  private static final String SECOND_FROM_NAME = "Sally Bloggs";

  private static final String TO_PASSWORD = "Password1";

  private static final String TO_USERNAME = "service@fitlife.com";

  /** The secure random number generator. */
  private static final SecureRandom secureRandom = new SecureRandom();

  /** The Background Interaction Source Synchronizer. */
  @Autowired
  private BackgroundInteractionSourceSynchronizer backgroundInteractionSourceSynchronizer;

  /** The Document Service. */
  @Autowired private DocumentService documentService;

  private GreenMail greenMail;

  /** The Interaction Service. */
  @Autowired private InteractionService interactionService;

  /** The Jackson Object Mapper. */
  @Autowired private ObjectMapper objectMapper;

  /** The Operations Reference Service. */
  @Autowired private OperationsReferenceService operationsReferenceService;

  /** The Workflow Service. */
  @Autowired private WorkflowService workflowService;

  /** The end-to-end test. */
  @Test
  public void endToEndTest() throws Exception {
    //   ___       _                      _   _               ____       _
    //  |_ _|_ __ | |_ ___ _ __ __ _  ___| |_(_) ___  _ __   / ___|  ___| |_ _   _ _ __
    //   | || '_ \| __/ _ \ '__/ _` |/ __| __| |/ _ \| '_ \  \___ \ / _ \ __| | | | '_ \
    //   | || | | | ||  __/ | | (_| | (__| |_| | (_) | | | |  ___) |  __/ |_| |_| | |_) |
    //  |___|_| |_|\__\___|_|  \__,_|\___|\__|_|\___/|_| |_| |____/ \___|\__|\__,_| .__/
    //                                                                            |_|

    /*
     * Create the mailbox interaction source, which will consume email messages from the in-memory
     * GreenMail mail server and turn them into interactions with associated interaction
     * attachments.
     */
    InteractionSource interactionSource = getFitLifeCustomerServiceMailboxInteractionSource();

    interactionService.createInteractionSource(TenantUtil.DEFAULT_TENANT_ID, interactionSource);

    // Create the first message
    Message firstMessage =
        OperationsTestUtil.getOriginalOutlookHTMLMessageWithEmbeddedImage(
            greenMail.getSmtp().createSession(),
            FIRST_FROM_EMAIL_ADDRESS,
            FIRST_FROM_NAME,
            FIRST_TO_EMAIL_ADDRESS,
            FIRST_TO_NAME);

    // Send the first message
    Transport.send(firstMessage);

    // Create the second message
    Message secondMessage =
        OperationsTestUtil.getOriginalOutlookHTMLMessageWithEmbeddedImage(
            greenMail.getSmtp().createSession(),
            SECOND_FROM_EMAIL_ADDRESS,
            SECOND_FROM_NAME,
            FIRST_TO_EMAIL_ADDRESS,
            FIRST_TO_NAME);

    // Send the second message
    Transport.send(secondMessage);

    // Wait for mail to arrive
    assertTrue(greenMail.waitForIncomingEmail(5000, 2));

    Integer numberOfNewInteractions =
        backgroundInteractionSourceSynchronizer.synchronizeInteractionSources();

    assertEquals(2, numberOfNewInteractions);

    //   ____                                        _     ____       _
    //  |  _ \  ___   ___ _   _ _ __ ___   ___ _ __ | |_  / ___|  ___| |_ _   _ _ __
    //  | | | |/ _ \ / __| | | | '_ ` _ \ / _ \ '_ \| __| \___ \ / _ \ __| | | | '_ \
    //  | |_| | (_) | (__| |_| | | | | | |  __/ | | | |_   ___) |  __/ |_| |_| | |_) |
    //  |____/ \___/ \___|\__,_|_| |_| |_|\___|_| |_|\__| |____/ \___|\__|\__,_| .__/
    //                                                                         |_|

    // Create the document external reference type
    ExternalReferenceType documentExternalReferenceType =
        new ExternalReferenceType(
            "test_document_external_reference_code",
            "Test Document External Reference",
            "Test Document External Reference Description",
            ObjectType.DOCUMENT);

    operationsReferenceService.createExternalReferenceType(documentExternalReferenceType);

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
            "Test Document Definition");

    documentService.createDocumentDefinition(documentDefinition);

    DocumentDefinition anotherDocumentDefinition =
        new DocumentDefinition(
            "another_test_document_definition_" + randomId(),
            documentDefinitionCategory.getId(),
            "Another Test Document Definition");

    documentService.createDocumentDefinition(anotherDocumentDefinition);

    //  __        __         _     __ _                 ____       _
    //  \ \      / /__  _ __| | __/ _| | _____      __ / ___|  ___| |_ _   _ _ __
    //   \ \ /\ / / _ \| '__| |/ / |_| |/ _ \ \ /\ / / \___ \ / _ \ __| | | | '_ \
    //    \ V  V / (_) | |  |   <|  _| | (_) \ V  V /   ___) |  __/ |_| |_| | |_) |
    //     \_/\_/ \___/|_|  |_|\_\_| |_|\___/ \_/\_/   |____/ \___|\__|\__,_| .__/
    //                                                                      |_|

    // Create the workflow external reference type
    ExternalReferenceType workflowExternalReferenceType =
        new ExternalReferenceType(
            "test_workflow_external_reference_code",
            "Test Workflow External Reference",
            "Test Workflow External Reference Description",
            ObjectType.WORKFLOW,
            TenantUtil.DEFAULT_TENANT_ID);

    operationsReferenceService.createExternalReferenceType(workflowExternalReferenceType);

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
            "dummy",
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
        new WorkflowDefinitionAttribute("process_definition_key", UUID.randomUUID().toString()));

    workflowService.createWorkflowDefinition(workflowDefinition);

    //   ___       _                      _   _               _____         _
    //  |_ _|_ __ | |_ ___ _ __ __ _  ___| |_(_) ___  _ __   |_   _|__  ___| |_
    //   | || '_ \| __/ _ \ '__/ _` |/ __| __| |/ _ \| '_ \    | |/ _ \/ __| __|
    //   | || | | | ||  __/ | | (_| | (__| |_| | (_) | | | |   | |  __/\__ \ |_
    //  |___|_| |_|\__\___|_|  \__,_|\___|\__|_|\___/|_| |_|   |_|\___||___/\__|
    //
    InteractionSummaries interactionSummaries =
        interactionService.getInteractionSummaries(
            TenantUtil.DEFAULT_TENANT_ID,
            interactionSource.getId(),
            null,
            null,
            null,
            null,
            null,
            null,
            null);

    assertEquals(2, interactionSummaries.getInteractionSummaries().size());

    UUID firstInteractionId = interactionSummaries.getInteractionSummaries().getFirst().getId();
    String firstConversationId =
        interactionSummaries.getInteractionSummaries().getFirst().getConversationId();
    UUID secondInteractionId = interactionSummaries.getInteractionSummaries().get(1).getId();
    String secondConversationId =
        interactionSummaries.getInteractionSummaries().get(1).getConversationId();

    Interaction interaction =
        interactionService.getInteraction(TenantUtil.DEFAULT_TENANT_ID, firstInteractionId);

    InteractionAttachmentSummaries interactionAttachmentSummaries =
        interactionService.getInteractionAttachmentSummaries(
            TenantUtil.DEFAULT_TENANT_ID, firstInteractionId, null, null, null, null, null);

    assertEquals(10, interactionAttachmentSummaries.getInteractionAttachmentSummaries().size());

    List<UUID> interactionAttachmentIds =
        interactionAttachmentSummaries.getInteractionAttachmentSummaries().stream()
            .map(InteractionAttachmentSummary::getId)
            .toList();

    // Create an interaction note for the interaction
    CreateInteractionNoteRequest createInteractionNoteRequest =
        new CreateInteractionNoteRequest(
            interaction.getId(), "This is the interaction note content.");

    InteractionNote interactionNote =
        interactionService.createInteractionNote(
            TenantUtil.DEFAULT_TENANT_ID, createInteractionNoteRequest, "TEST1");

    // Update the interaction note for the interaction
    UpdateInteractionNoteRequest updateInteractionNoteRequest =
        new UpdateInteractionNoteRequest(
            interactionNote.getId(), "This is the interaction note content.");

    InteractionNote updatedInteractionNote =
        interactionService.updateInteractionNote(
            TenantUtil.DEFAULT_TENANT_ID, updateInteractionNoteRequest, "TEST2");

    // Retrieve the interaction notes for the interaction
    InteractionNotes interactionNotes =
        interactionService.getInteractionNotes(
            TenantUtil.DEFAULT_TENANT_ID,
            interaction.getId(),
            null,
            InteractionNoteSortBy.CREATED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, interactionNotes.getTotal());

    // Link the interaction to a party
    UUID partyId = UUID.randomUUID();

    interactionService.linkPartyToInteraction(
        TenantUtil.DEFAULT_TENANT_ID,
        new LinkPartyToInteractionRequest(firstInteractionId, partyId));

    interaction =
        interactionService.getInteraction(TenantUtil.DEFAULT_TENANT_ID, firstInteractionId);

    assertEquals(partyId, interaction.getPartyId());

    // Delink the party from the interaction
    interactionService.delinkPartyFromInteraction(
        TenantUtil.DEFAULT_TENANT_ID, new DelinkPartyFromInteractionRequest(firstInteractionId));

    interaction =
        interactionService.getInteraction(TenantUtil.DEFAULT_TENANT_ID, firstInteractionId);

    assertNull(interaction.getPartyId());

    //  __        __         _     __ _                 _____         _
    //  \ \      / /__  _ __| | __/ _| | _____      __ |_   _|__  ___| |_
    //   \ \ /\ / / _ \| '__| |/ / |_| |/ _ \ \ /\ / /   | |/ _ \/ __| __|
    //    \ V  V / (_) | |  |   <|  _| | (_) \ V  V /    | |  __/\__ \ |_
    //     \_/\_/ \___/|_|  |_|\_\_| |_|\___/ \_/\_/     |_|\___||___/\__|
    //

    // Initiate the workflow
    TestWorkflowData workflowData =
        new TestWorkflowData(
            UUID.randomUUID(),
            "This is name " + randomId(),
            LocalDate.of(1976, 3, 7),
            new BigDecimal("1234.56"),
            OffsetDateTime.now());

    String workflowDataJson = objectMapper.writeValueAsString(workflowData);

    InitiateWorkflowRequest initiateWorkflowRequest =
        new InitiateWorkflowRequest(
            workflowDefinition.getId(),
            null,
            UUID.randomUUID(),
            false,
            List.of(
                new WorkflowExternalReference(
                    "test_workflow_external_reference_code",
                    "test_workflow_external_reference_value")),
            List.of(
                new WorkflowAttribute(
                    "test_workflow_attribute_code", "test_workflow_attribute_value")),
            List.of(new InitiateWorkflowInteractionLink(firstInteractionId, firstConversationId)),
            null,
            workflowDataJson);

    Workflow workflow =
        workflowService.initiateWorkflow(
            TenantUtil.DEFAULT_TENANT_ID, initiateWorkflowRequest, "TEST1");

    // Request the additional workflow document
    RequestWorkflowDocumentRequest requestWorkflowDocumentRequest =
        new RequestWorkflowDocumentRequest(workflow.getId(), anotherDocumentDefinition.getId());

    workflowService.requestWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, requestWorkflowDocumentRequest, "TEST1");

    // Retrieve the outstanding workflow documents for the workflow
    List<OutstandingWorkflowDocument> outstandingWorkflowDocuments =
        workflowService.getOutstandingWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(2, outstandingWorkflowDocuments.size());

    // Provide the workflow documents
    byte[] multiPagePdfData = ResourceUtil.getClasspathResource("MultiPagePdf.pdf");

    for (OutstandingWorkflowDocument outstandingWorkflowDocument : outstandingWorkflowDocuments) {
      ProvideWorkflowDocumentRequest provideWorkflowDocumentRequest =
          new ProvideWorkflowDocumentRequest(
              outstandingWorkflowDocument.getId(),
              FileType.PDF,
              "MultiPagePdf.pdf",
              null,
              null,
              null,
              List.of(
                  new DocumentExternalReference(
                      "test_document_external_reference_code",
                      "test_document_external_reference_value")),
              List.of(
                  new DocumentAttribute(
                      "test_document_attribute_code", "test_document_attribute_value")),
              null,
              multiPagePdfData);

      workflowService.provideWorkflowDocument(
          TenantUtil.DEFAULT_TENANT_ID, provideWorkflowDocumentRequest, "TEST1");
    }

    Thread.sleep(2000L);

    outstandingWorkflowDocuments =
        workflowService.getOutstandingWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(0, outstandingWorkflowDocuments.size());

    WorkflowDocuments workflowDocuments =
        workflowService.getWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID,
            workflow.getId(),
            null,
            WorkflowDocumentSortBy.REQUESTED,
            SortDirection.ASCENDING,
            0,
            10);

    List<UUID> documentIds =
        workflowDocuments.getWorkflowDocuments().stream()
            .map(WorkflowDocument::getDocumentId)
            .toList();

    // Verify the first workflow document
    VerifyWorkflowDocumentRequest verifyWorkflowDocumentRequest =
        new VerifyWorkflowDocumentRequest(
            workflowDocuments.getWorkflowDocuments().getFirst().getId());

    workflowService.verifyWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, verifyWorkflowDocumentRequest, "TEST1");

    // Reject the second workflow document
    RejectWorkflowDocumentRequest rejectWorkflowDocumentRequest =
        new RejectWorkflowDocumentRequest(
            workflowDocuments.getWorkflowDocuments().get(1).getId(),
            "This is a test rejection reason.");

    workflowService.rejectWorkflowDocument(
        TenantUtil.DEFAULT_TENANT_ID, rejectWorkflowDocumentRequest, "TEST2");

    outstandingWorkflowDocuments =
        workflowService.getOutstandingWorkflowDocuments(
            TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(1, outstandingWorkflowDocuments.size());

    // Link an interaction to the workflow
    LinkInteractionToWorkflowRequest linkInteractionToWorkflowRequest =
        new LinkInteractionToWorkflowRequest(
            workflow.getId(), secondInteractionId, secondConversationId);

    workflowService.linkInteractionToWorkflow(
        TenantUtil.DEFAULT_TENANT_ID, linkInteractionToWorkflowRequest, "TEST2");

    workflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(secondInteractionId, workflow.getInteractionLinks().get(1).getInteractionId());
    assertEquals("TEST2", workflow.getInteractionLinks().get(1).getLinkedBy());

    // Delink an interaction from the workflow
    DelinkInteractionFromWorkflowRequest delinkInteractionFromWorkflowRequest =
        new DelinkInteractionFromWorkflowRequest(workflow.getId(), secondInteractionId);

    workflowService.delinkInteractionFromWorkflow(
        TenantUtil.DEFAULT_TENANT_ID, delinkInteractionFromWorkflowRequest);

    workflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    assertEquals(1, workflow.getInteractionLinks().size());

    assertEquals(firstInteractionId, workflow.getInteractionLinks().getFirst().getInteractionId());
    assertEquals("TEST1", workflow.getInteractionLinks().getFirst().getLinkedBy());

    // Initiate the first workflow step
    workflowService.initiateWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new InitiateWorkflowStepRequest(workflow.getId(), "test_workflow_step_1"));

    // Finalize the first workflow step
    workflowService.finalizeWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new FinalizeWorkflowStepRequest(
            workflow.getId(), "test_workflow_step_1", WorkflowStepStatus.COMPLETED));

    // Initiate the second workflow step
    workflowService.initiateWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new InitiateWorkflowStepRequest(workflow.getId(), "test_workflow_step_2"));

    // Finalize the second workflow step
    workflowService.finalizeWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new FinalizeWorkflowStepRequest(
            workflow.getId(), "test_workflow_step_2", WorkflowStepStatus.COMPLETED));

    // Initiate the third workflow step
    workflowService.initiateWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new InitiateWorkflowStepRequest(workflow.getId(), "test_workflow_step_3"));

    // Finalize the third workflow step
    workflowService.finalizeWorkflowStep(
        TenantUtil.DEFAULT_TENANT_ID,
        new FinalizeWorkflowStepRequest(
            workflow.getId(), "test_workflow_step_3", WorkflowStepStatus.COMPLETED));

    // Finalize the workflow
    workflowService.finalizeWorkflow(
        TenantUtil.DEFAULT_TENANT_ID,
        new FinalizeWorkflowRequest(workflow.getId(), WorkflowStatus.COMPLETED),
        "TEST1");

    workflow = workflowService.getWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    //  __        __         _     __ _                  ____ _
    //  \ \      / /__  _ __| | __/ _| | _____      __  / ___| | ___  __ _ _ __  _   _ _ __
    //   \ \ /\ / / _ \| '__| |/ / |_| |/ _ \ \ /\ / / | |   | |/ _ \/ _` | '_ \| | | | '_ \
    //    \ V  V / (_) | |  |   <|  _| | (_) \ V  V /  | |___| |  __/ (_| | | | | |_| | |_) |
    //     \_/\_/ \___/|_|  |_|\_\_| |_|\___/ \_/\_/    \____|_|\___|\__,_|_| |_|\__,_| .__/
    //                                                                                |_|
    // Delete the workflows
    workflowService.deleteWorkflow(TenantUtil.DEFAULT_TENANT_ID, workflow.getId());

    for (String workflowDefinitionCategoryId : List.of(workflowDefinitionCategory.getId())) {
      List<WorkflowDefinitionSummary> workflowDefinitionSummaries =
          workflowService.getWorkflowDefinitionSummaries(
              TenantUtil.DEFAULT_TENANT_ID, workflowDefinitionCategoryId);

      // Delete the workflow definitions
      for (String workflowDefinitionId :
          workflowDefinitionSummaries.stream().map(WorkflowDefinitionSummary::getId).toList()) {
        workflowService.deleteWorkflowDefinition(workflowDefinitionId);
      }

      // Delete the workflow definition category
      workflowService.deleteWorkflowDefinitionCategory(workflowDefinitionCategoryId);
    }

    workflowService.deleteWorkflowAttributeDefinition(workflowAttributeDefinition.getCode());

    operationsReferenceService.deleteExternalReferenceType("test_workflow_external_reference_code");

    //   ____                                        _      ____ _
    //  |  _ \  ___   ___ _   _ _ __ ___   ___ _ __ | |_   / ___| | ___  __ _ _ __  _   _ _ __
    //  | | | |/ _ \ / __| | | | '_ ` _ \ / _ \ '_ \| __| | |   | |/ _ \/ _` | '_ \| | | | '_ \
    //  | |_| | (_) | (__| |_| | | | | | |  __/ | | | |_  | |___| |  __/ (_| | | | | |_| | |_) |
    //  |____/ \___/ \___|\__,_|_| |_| |_|\___|_| |_|\__|  \____|_|\___|\__,_|_| |_|\__,_| .__/
    //                                                                                   |_|
    for (UUID documentId : documentIds) {
      documentService.deleteDocument(TenantUtil.DEFAULT_TENANT_ID, documentId);
    }

    for (String documentDefinitionCategoryId : List.of(documentDefinitionCategory.getId())) {
      List<DocumentDefinitionSummary> documentDefinitionSummaries =
          documentService.getDocumentDefinitionSummaries(
              TenantUtil.DEFAULT_TENANT_ID, documentDefinitionCategoryId);

      // Delete the document definitions
      for (String documentDefinitionId :
          documentDefinitionSummaries.stream().map(DocumentDefinitionSummary::getId).toList()) {

        documentService.deleteDocumentDefinition(documentDefinitionId);
      }

      // Delete the document definition category
      documentService.deleteDocumentDefinitionCategory(documentDefinitionCategoryId);
    }

    documentService.deleteDocumentAttributeDefinition(documentAttributeDefinition.getCode());

    operationsReferenceService.deleteExternalReferenceType("test_document_external_reference_code");

    //   ___       _                      _   _                ____ _
    //  |_ _|_ __ | |_ ___ _ __ __ _  ___| |_(_) ___  _ __    / ___| | ___  __ _ _ __  _   _ _ __
    //   | || '_ \| __/ _ \ '__/ _` |/ __| __| |/ _ \| '_ \  | |   | |/ _ \/ _` | '_ \| | | | '_ \
    //   | || | | | ||  __/ | | (_| | (__| |_| | (_) | | | | | |___| |  __/ (_| | | | | |_| | |_) |
    //  |___|_| |_|\__\___|_|  \__,_|\___|\__|_|\___/|_| |_|  \____|_|\___|\__,_|_| |_|\__,_| .__/
    //                                                                                      |_|

    // Delete the interaction notes
    for (UUID interactionNoteId : List.of(interactionNote.getId())) {
      interactionService.deleteInteractionNote(TenantUtil.DEFAULT_TENANT_ID, interactionNoteId);
    }

    // Delete the interaction attachments
    for (UUID interactionAttachmentId : interactionAttachmentIds) {
      interactionService.deleteInteractionAttachment(
          TenantUtil.DEFAULT_TENANT_ID, interactionAttachmentId);
    }

    // Delete the interaction
    for (UUID interactionId : List.of(firstInteractionId, secondInteractionId)) {
      interactionService.deleteInteraction(TenantUtil.DEFAULT_TENANT_ID, interactionId);
    }

    // Delete the interaction source
    interactionService.deleteInteractionSource(
        TenantUtil.DEFAULT_TENANT_ID, interactionSource.getId());
  }

  @BeforeEach
  protected void setUp() {
    // Start a GreenMail server with the IMAP protocol
    if (ENABLE_GREEN_MAIL_SECURITY) {
      greenMail = new GreenMail(ServerSetupTest.SMTPS_IMAPS);
    } else {
      greenMail = new GreenMail(ServerSetupTest.SMTP_IMAP);
    }

    GreenMailUser greenMailUser =
        greenMail.setUser(FIRST_TO_EMAIL_ADDRESS, TO_USERNAME, TO_PASSWORD);

    greenMail.start();
  }

  @AfterEach
  protected void tearDown() {
    // Stop the GreenMail server
    greenMail.stop();
  }

  private String documentSetUp() throws Exception {
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
            "Test Document Definition");

    documentService.createDocumentDefinition(documentDefinition);

    DocumentDefinition anotherDocumentDefinition =
        new DocumentDefinition(
            "another_test_document_definition_" + randomId(),
            documentDefinitionCategory.getId(),
            "Another Test Document Definition");

    documentService.createDocumentDefinition(anotherDocumentDefinition);

    return documentDefinitionCategory.getId();
  }

  private InteractionSource getFitLifeCustomerServiceMailboxInteractionSource() {
    return InteractionSource.createMailboxInteractionSource(
        UUID.randomUUID(),
        TenantUtil.DEFAULT_TENANT_ID,
        "FitLife Customer Service Mailbox",
        true,
        ENABLE_GREEN_MAIL_SECURITY ? MailboxProtocol.STANDARD_IMAPS : MailboxProtocol.STANDARD_IMAP,
        "localhost",
        ENABLE_GREEN_MAIL_SECURITY ? 3993 : 3143,
        TO_USERNAME,
        TO_PASSWORD,
        FIRST_TO_EMAIL_ADDRESS,
        true,
        false,
        true);
  }

  private String randomId() {
    return String.format("%04X", secureRandom.nextInt(0x10000));
  }

  private void waitForInteractionToProcess(UUID tenantId, UUID interactionId) throws Exception {
    for (int i = 0; i < 50; i++) {
      Interaction interaction = interactionService.getInteraction(tenantId, interactionId);

      if (interaction.getStatus() == InteractionStatus.AVAILABLE) {
        return;
      }

      Thread.sleep(250);
    }

    throw new RuntimeException(
        "Timed out waiting for the interaction ("
            + interactionId
            + ") for the tenant ("
            + tenantId
            + ") to process");
  }

  private void workflowSetUp() throws Exception {}
}

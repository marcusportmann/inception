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

package digital.inception.operations.store;

import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DuplicateWorkflowDocumentException;
import digital.inception.operations.exception.DuplicateWorkflowException;
import digital.inception.operations.exception.DuplicateWorkflowNoteException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
import digital.inception.operations.exception.WorkflowInteractionLinkNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.exception.WorkflowStepNotFoundException;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.SearchWorkflowsRequest;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngineIds;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowStepStatus;
import digital.inception.operations.model.WorkflowSummaries;
import java.util.List;
import java.util.UUID;

/**
 * The {@code WorkflowStore} interface defines the functionality provided by a workflow store, which
 * manages the persistence of workflows.
 *
 * @author Marcus Portmann
 */
public interface WorkflowStore {

  /**
   * Cancel the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param canceledBy the person or system canceling the workflow
   * @param cancellationReason the reason the workflow was canceled
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be canceled
   */
  void cancelWorkflow(UUID tenantId, UUID workflowId, String canceledBy, String cancellationReason)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Create the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflow the workflow
   * @return the workflow
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition version for the
   *     workflow could not be found
   * @throws DuplicateWorkflowException if the workflow already exists
   * @throws ServiceUnavailableException if the workflow could not be created
   */
  Workflow createWorkflow(UUID tenantId, Workflow workflow)
      throws WorkflowDefinitionVersionNotFoundException,
          DuplicateWorkflowException,
          ServiceUnavailableException;

  /**
   * Create the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocument the workflow document
   * @return the workflow document
   * @throws DuplicateWorkflowDocumentException if the workflow document already exists
   * @throws ServiceUnavailableException if the workflow document could not be created
   */
  WorkflowDocument createWorkflowDocument(UUID tenantId, WorkflowDocument workflowDocument)
      throws DuplicateWorkflowDocumentException, ServiceUnavailableException;

  /**
   * Create the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowNote the workflow note
   * @return the workflow note
   * @throws DuplicateWorkflowNoteException if the workflow note already exists
   * @throws ServiceUnavailableException if the workflow note could not be created
   */
  WorkflowNote createWorkflowNote(UUID tenantId, WorkflowNote workflowNote)
      throws DuplicateWorkflowNoteException, ServiceUnavailableException;

  /**
   * Delete the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be deleted
   */
  void deleteWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Delete the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocumentId the ID for the workflow document
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be deleted
   */
  void deleteWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException;

  /**
   * Delete the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowNoteId the ID for the workflow note
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be deleted
   */
  void deleteWorkflowNote(UUID tenantId, UUID workflowNoteId)
      throws WorkflowNoteNotFoundException, ServiceUnavailableException;

  /**
   * Delink an interaction from a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param interactionId the ID for the interaction
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws WorkflowInteractionLinkNotFoundException if the workflow interaction link could not be
   *     found
   * @throws ServiceUnavailableException if the interaction could not be delinked from the workflow
   */
  void delinkInteractionFromWorkflow(UUID tenantId, UUID workflowId, UUID interactionId)
      throws InteractionNotFoundException,
          WorkflowNotFoundException,
          WorkflowInteractionLinkNotFoundException,
          ServiceUnavailableException;

  /**
   * Finalize the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param status the final status for the workflow
   * @param finalizedBy the person or system finalizing the workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be finalized
   */
  void finalizeWorkflow(UUID tenantId, UUID workflowId, WorkflowStatus status, String finalizedBy)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Finalize the workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow step is associated with
   * @param step the code for the workflow step
   * @param status the final status for the workflow step
   * @param nextStep the code for the next workflow step to initiate
   * @throws WorkflowStepNotFoundException if the workflow step could not be found
   * @throws ServiceUnavailableException if the workflow step could not be finalized
   */
  void finalizeWorkflowStep(
      UUID tenantId, UUID workflowId, String step, WorkflowStepStatus status, String nextStep)
      throws WorkflowStepNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the IDs for the active workflows for the workflow engine.
   *
   * @param tenantId the ID for the tenant
   * @param workflowEngineId the ID for the workflow engine
   * @return the IDs for the active workflows for the workflow engine
   * @throws ServiceUnavailableException if the IDs for the active workflows for the workflow engine
   *     could not be retrieved
   */
  List<UUID> getActiveWorkflowIdsForWorkflowEngine(UUID tenantId, String workflowEngineId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the document definition ID for the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocumentId the ID for the workflow document
   * @return the document definition ID for the workflow document
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the document definition ID could not be retrieved for
   *     the workflow document
   */
  String getDocumentDefinitionIdForWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the outstanding workflow documents for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the outstanding workflow documents are associated
   *     with
   * @return the outstanding workflow documents
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the outstanding workflow documents could not be
   *     retrieved
   */
  List<OutstandingWorkflowDocument> getOutstandingWorkflowDocuments(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @return the workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be retrieved
   */
  Workflow getWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow.
   *
   * @param workflowId the ID for the workflow
   * @return the workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be retrieved
   */
  Workflow getWorkflow(UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the composite {@link WorkflowDefinitionId} (id + version) for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @return the composite {@link WorkflowDefinitionId} (id + version) for the workflow with the
   *     specified ID
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the {@link WorkflowDefinitionId} (id + version) for the
   *     workflow could not be retrieved
   */
  WorkflowDefinitionId getWorkflowDefinitionIdForWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocumentId the ID for the workflow document
   * @return the workflow document
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be retrieved
   */
  WorkflowDocument getWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow documents for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow documents are associated with
   * @param filter the filter to apply to the workflow documents
   * @param sortBy the method used to sort the workflow documents, e.g. by created
   * @param sortDirection the sort direction to apply to the workflow documents
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of workflow documents that should be retrieved
   * @return the workflow documents
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow documents could not be retrieved
   */
  WorkflowDocuments getWorkflowDocuments(
      UUID tenantId,
      UUID workflowId,
      String filter,
      WorkflowDocumentSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow engine IDs for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @return the workflow engine IDs for the workflow
   * @throws WorkflowNotFoundException if the workflow could be found
   * @throws ServiceUnavailableException if the workflow engine IDs could not be retrieved for the
   *     workflow
   */
  WorkflowEngineIds getWorkflowEngineIdsForWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow ID for the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocumentId the ID for the workflow document
   * @return the workflow ID for the workflow document
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow ID could not be retrieved for the workflow
   *     document
   */
  UUID getWorkflowIdForWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowNoteId the ID for the workflow note
   * @return the workflow note
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be retrieved
   */
  WorkflowNote getWorkflowNote(UUID tenantId, UUID workflowNoteId)
      throws WorkflowNoteNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow notes for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow notes are associated with
   * @param filter the filter to apply to the workflow notes
   * @param sortBy the method used to sort the workflow notes e.g. by created
   * @param sortDirection the sort direction to apply to the workflow notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of workflow notes that should be retrieved
   * @return the workflow notes
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow notes could not be retrieved
   */
  WorkflowNotes getWorkflowNotes(
      UUID tenantId,
      UUID workflowId,
      String filter,
      WorkflowNoteSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the workflows.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionId the workflow definition ID filter to apply to the workflow
   *     summaries
   * @param status the status filter to apply to the workflow summaries
   * @param filter the filter to apply to the workflow summaries
   * @param sortBy the method used to sort the workflow summaries e.g. by definition ID
   * @param sortDirection the sort direction to apply to the workflow summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of workflow summaries that should be retrieved
   * @return the summaries for the workflows
   * @throws ServiceUnavailableException if the workflow summaries could not be retrieved
   */
  WorkflowSummaries getWorkflowSummaries(
      UUID tenantId,
      String workflowDefinitionId,
      WorkflowStatus status,
      String filter,
      WorkflowSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException;

  /**
   * Initiate the workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow step is associated with
   * @param step the code for the workflow step
   * @return the workflow step
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow step could not be initiated
   */
  WorkflowStep initiateWorkflowStep(UUID tenantId, UUID workflowId, String step)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Link an interaction to a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param interactionId the ID for the interaction
   * @param conversationId the ID for the conversation the interaction is associated with
   * @param linkedBy the person or system linking the interaction to the workflow
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the interaction could not be linked to the workflow
   */
  void linkInteractionToWorkflow(
      UUID tenantId, UUID workflowId, UUID interactionId, String conversationId, String linkedBy)
      throws InteractionNotFoundException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Provide a workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param provideWorkflowDocumentRequest the request to provide a workflow document
   * @param providedBy the person or system providing the workflow document
   * @return the workflow document that was provided
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be provided
   */
  WorkflowDocument provideWorkflowDocument(
      UUID tenantId,
      ProvideWorkflowDocumentRequest provideWorkflowDocumentRequest,
      String providedBy)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException;

  /**
   * Reject a workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param rejectWorkflowDocumentRequest the request to reject a workflow document
   * @param rejectedBy the person or system rejecting the workflow document
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be rejected
   */
  void rejectWorkflowDocument(
      UUID tenantId, RejectWorkflowDocumentRequest rejectWorkflowDocumentRequest, String rejectedBy)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException;

  /**
   * Request a workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param requestWorkflowDocumentRequest the request to request a workflow document
   * @param requestedBy the person or system requesting the workflow document
   * @return the workflow document
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the workflow document could not be requested
   */
  WorkflowDocument requestWorkflowDocument(
      UUID tenantId,
      RequestWorkflowDocumentRequest requestWorkflowDocumentRequest,
      String requestedBy)
      throws DocumentDefinitionNotFoundException, ServiceUnavailableException;

  /**
   * Search for workflows.
   *
   * @param tenantId the ID for the tenant
   * @param searchWorkflowsRequest the request to search for workflows matching specific criteria
   * @return the summaries for the workflows matching the search criteria
   * @throws ServiceUnavailableException if the workflow search failed
   */
  WorkflowSummaries searchWorkflows(UUID tenantId, SearchWorkflowsRequest searchWorkflowsRequest)
      throws ServiceUnavailableException;

  /**
   * Set the status for the workflow and, if applicable, the status for any associated workflow
   * steps.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param status the new status for the workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the status for the workflow could not be set
   */
  void setWorkflowStatus(UUID tenantId, UUID workflowId, WorkflowStatus status)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Suspend the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param suspendedBy the person or system suspending the workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be suspended
   */
  void suspendWorkflow(UUID tenantId, UUID workflowId, String suspendedBy)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Suspend the workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow step is associated with
   * @param step the code for the workflow step
   * @throws WorkflowStepNotFoundException if the workflow step could not be found
   * @throws ServiceUnavailableException if the workflow step could not be suspended
   */
  void suspendWorkflowStep(UUID tenantId, UUID workflowId, String step)
      throws WorkflowStepNotFoundException, ServiceUnavailableException;

  /**
   * Unsuspend the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be unsuspended
   */
  void unsuspendWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Unsuspend the workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow step is associated with
   * @param step the code for the workflow step
   * @throws WorkflowStepNotFoundException if the workflow step could not be found
   * @throws ServiceUnavailableException if the workflow step could not be unsuspended
   */
  void unsuspendWorkflowStep(UUID tenantId, UUID workflowId, String step)
      throws WorkflowStepNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflow the workflow
   * @return the updated workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be updated
   */
  Workflow updateWorkflow(UUID tenantId, Workflow workflow)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocument the workflow document
   * @return the updated workflow document
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be updated
   */
  WorkflowDocument updateWorkflowDocument(UUID tenantId, WorkflowDocument workflowDocument)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowNote the workflow note
   * @return the updated workflow note
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be updated
   */
  WorkflowNote updateWorkflowNote(UUID tenantId, WorkflowNote workflowNote)
      throws WorkflowNoteNotFoundException, ServiceUnavailableException;

  /**
   * Verify a workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param verifyWorkflowDocumentRequest the request to verify a workflow document
   * @param verifiedBy the person or system verifying the workflow document
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be verified
   */
  void verifyWorkflowDocument(
      UUID tenantId, VerifyWorkflowDocumentRequest verifyWorkflowDocumentRequest, String verifiedBy)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException;

  /**
   * Check whether the workflow document exists.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow document is associated with
   * @param workflowDocumentId the ID for the workflow document
   * @return {@code true} if the workflow document exists or {@code false} otherwise
   * @throws ServiceUnavailableException if the existence of the workflow document could not be
   *     determined
   */
  boolean workflowDocumentExists(UUID tenantId, UUID workflowId, UUID workflowDocumentId)
      throws ServiceUnavailableException;

  /**
   * Check whether the workflow document exists.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocumentId the ID for the workflow document
   * @return {@code true} if the workflow document exists or {@code false} otherwise
   * @throws ServiceUnavailableException if the existence of the workflow document could not be
   *     determined
   */
  boolean workflowDocumentExists(UUID tenantId, UUID workflowDocumentId)
      throws ServiceUnavailableException;

  /**
   * Check whether the workflow exists.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @return {@code true} if the workflow exists or {@code false} otherwise
   * @throws ServiceUnavailableException if the existence of the workflow could not be determined
   */
  boolean workflowExists(UUID tenantId, UUID workflowId) throws ServiceUnavailableException;

  /**
   * Check whether the workflow note exists.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow note is associated with
   * @param workflowNoteId the ID for the workflow note
   * @return {@code true} if the workflow note exists or {@code false} otherwise
   * @throws ServiceUnavailableException if the existence of the workflow note could not be
   *     determined
   */
  boolean workflowNoteExists(UUID tenantId, UUID workflowId, UUID workflowNoteId)
      throws ServiceUnavailableException;
}

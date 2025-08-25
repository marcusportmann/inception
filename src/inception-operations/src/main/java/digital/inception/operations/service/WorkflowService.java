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

package digital.inception.operations.service;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.connector.WorkflowEngineConnector;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DuplicateWorkflowAttributeDefinitionException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InvalidWorkflowStatusException;
import digital.inception.operations.exception.WorkflowAttributeDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowInteractionLinkNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.exception.WorkflowStepNotFoundException;
import digital.inception.operations.model.CancelWorkflowRequest;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.DelinkInteractionFromWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.LinkInteractionToWorkflowRequest;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.StartWorkflowRequest;
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
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDefinitionPermission;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowSummaries;
import java.util.List;
import java.util.UUID;

/**
 * The {@code WorkflowService} interface defines the functionality provided by a Workflow Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface WorkflowService {

  /**
   * Cancel a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param cancelWorkflowRequest the request to cancel a workflow
   * @param canceledBy the person or system who canceled the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be canceled
   */
  void cancelWorkflow(UUID tenantId, CancelWorkflowRequest cancelWorkflowRequest, String canceledBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Create the workflow attribute definition.
   *
   * @param workflowAttributeDefinition the workflow attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWorkflowAttributeDefinitionException if the workflow attribute definition
   *     already exists
   * @throws ServiceUnavailableException if the workflow attribute definition could not be created
   */
  void createWorkflowAttributeDefinition(WorkflowAttributeDefinition workflowAttributeDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowAttributeDefinitionException,
          ServiceUnavailableException;

  /**
   * Create the workflow definition version.
   *
   * @param workflowDefinition the workflow definition version
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWorkflowDefinitionVersionException if the workflow definition version already
   *     exists
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws WorkflowEngineNotFoundException if the workflow engine could not be found
   * @throws DocumentDefinitionNotFoundException if a document definition reference by the workflow
   *     definition version could not be found
   * @throws ServiceUnavailableException if the workflow definition version could not be created
   */
  void createWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionVersionException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowEngineNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the workflow definition category.
   *
   * @param workflowDefinitionCategory the workflow definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWorkflowDefinitionCategoryException if the workflow definition category
   *     already exists
   * @throws ServiceUnavailableException if the workflow definition category could not be created
   */
  void createWorkflowDefinitionCategory(WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionCategoryException,
          ServiceUnavailableException;

  /**
   * Create the workflow engine.
   *
   * @param workflowEngine the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWorkflowEngineException if the workflow engine already exists
   * @throws ServiceUnavailableException if the workflow engine could not be created
   */
  void createWorkflowEngine(WorkflowEngine workflowEngine)
      throws InvalidArgumentException,
          DuplicateWorkflowEngineException,
          ServiceUnavailableException;

  /**
   * Create the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param createWorkflowNoteRequest the request to create a workflow note
   * @param createdBy the person or system that created the workflow note
   * @return the workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow note could not be created
   */
  WorkflowNote createWorkflowNote(
      UUID tenantId, CreateWorkflowNoteRequest createWorkflowNoteRequest, String createdBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Delete the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be deleted
   */
  void deleteWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Delete the workflow attribute definition.
   *
   * @param workflowAttributeDefinitionCode the code for the workflow attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowAttributeDefinitionNotFoundException if the workflow attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the workflow attribute definition could not be deleted
   */
  void deleteWorkflowAttributeDefinition(String workflowAttributeDefinitionCode)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete all versions of the workflow definition.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws ServiceUnavailableException if the workflow could not be deleted
   */
  void deleteWorkflowDefinition(String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the workflow definition category.
   *
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition category could not be deleted
   */
  void deleteWorkflowDefinitionCategory(String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the workflow definition version.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version for the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition version could not
   *     be found
   * @throws ServiceUnavailableException if the workflow definition version could not be deleted
   */
  void deleteWorkflowDefinitionVersion(String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocumentId the ID for the workflow document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be deleted
   */
  void deleteWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the workflow engine.
   *
   * @param workflowEngineId the ID for the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowEngineNotFoundException if the workflow engine could not be found
   * @throws ServiceUnavailableException if the workflow engine could not be deleted
   */
  void deleteWorkflowEngine(String workflowEngineId)
      throws InvalidArgumentException, WorkflowEngineNotFoundException, ServiceUnavailableException;

  /**
   * Delete the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowNoteId the ID for the workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be deleted
   */
  void deleteWorkflowNote(UUID tenantId, UUID workflowNoteId)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException;

  /**
   * Delink an interaction from a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param delinkInteractionFromWorkflowRequest the request to delink an interaction from a
   *     workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws WorkflowInteractionLinkNotFoundException if the workflow interaction link could not be
   *     found
   * @throws ServiceUnavailableException if the interaction could not be delinked from the workflow
   */
  void delinkInteractionFromWorkflow(
      UUID tenantId, DelinkInteractionFromWorkflowRequest delinkInteractionFromWorkflowRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          WorkflowNotFoundException,
          WorkflowInteractionLinkNotFoundException,
          ServiceUnavailableException;

  /**
   * Finalize a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param finalizeWorkflowRequest the request to finalize a workflow
   * @param finalizedBy the person or system finalizing the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be finalized
   */
  void finalizeWorkflow(
      UUID tenantId, FinalizeWorkflowRequest finalizeWorkflowRequest, String finalizedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Finalize a workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param finalizeWorkflowStepRequest the request to finalize a workflow step
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowStepNotFoundException if the workflow step could not be found
   * @throws ServiceUnavailableException if the workflow step could not be finalized
   */
  void finalizeWorkflowStep(UUID tenantId, FinalizeWorkflowStepRequest finalizeWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowStepNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the IDs for the active workflows for the workflow engine.
   *
   * @param workflowEngineId the ID for the workflow engine
   * @return the IDs for the active workflows for the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the IDs for the active workflows for the workflow engine
   *     could not be retrieved
   */
  List<UUID> getActiveWorkflowIdsForWorkflowEngine(String workflowEngineId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the outstanding workflow documents for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the outstanding workflow documents are associated
   *     with
   * @return the outstanding workflow documents
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the outstanding workflow documents could not be
   *     retrieved
   */
  List<OutstandingWorkflowDocument> getOutstandingWorkflowDocuments(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the required workflow attribute definitions.
   *
   * @param tenantId the ID for the tenant
   * @return the required workflow attribute definitions
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the required workflow attribute definitions could not be
   *     retrieved
   */
  List<WorkflowAttributeDefinition> getRequiredWorkflowAttributeDefinitions(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @return the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be retrieved
   */
  Workflow getWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow.
   *
   * @param workflowId the ID for the workflow
   * @return the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be retrieved
   */
  Workflow getWorkflow(UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow attribute definition.
   *
   * @param workflowAttributeDefinitionCode the code for the workflow attribute definition
   * @return the workflow attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowAttributeDefinitionNotFoundException if the workflow attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the workflow attribute definition could not be retrieved
   */
  WorkflowAttributeDefinition getWorkflowAttributeDefinition(String workflowAttributeDefinitionCode)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow attribute definitions.
   *
   * @param tenantId the ID for the tenant
   * @return the workflow attribute definitions
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the workflow attribute definitions could not be
   *     retrieved
   */
  List<WorkflowAttributeDefinition> getWorkflowAttributeDefinitions(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the latest version of the workflow definition.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @return the latest version of the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws ServiceUnavailableException if the latest version of the workflow definition could not
   *     be retrieved
   */
  WorkflowDefinition getWorkflowDefinition(String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow definition categories.
   *
   * @param tenantId the ID for the tenant
   * @return the workflow definition categories
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the workflow definition categories could not be
   *     retrieved
   */
  List<WorkflowDefinitionCategory> getWorkflowDefinitionCategories(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the workflow definition category.
   *
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @return the workflow definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition category could not be retrieved
   */
  WorkflowDefinitionCategory getWorkflowDefinitionCategory(String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the composite {@link WorkflowDefinitionId} (id + version) for the workflow with the
   * specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @return the composite {@link WorkflowDefinitionId} (id + version) for the workflow with the
   *     specified ID
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the {@link WorkflowDefinitionId} (id + version) for the
   *     workflow could not be retrieved
   */
  WorkflowDefinitionId getWorkflowDefinitionIdForWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the permissions for the workflow definition.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version for the workflow definition
   * @return the permissions for the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition version could not
   *     be found
   * @throws ServiceUnavailableException if the permissions for the workflow definition could not be
   *     retrieved
   */
  List<WorkflowDefinitionPermission> getWorkflowDefinitionPermissions(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the summaries for the workflow definitions associated with the workflow definition
   * category with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionCategoryId the ID for the workflow definition category the workflow
   *     definitions are associated with
   * @return the summaries for the workflow definitions associated with the workflow definition
   *     category with the specified ID
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition summaries could not be retrieved
   */
  List<WorkflowDefinitionSummary> getWorkflowDefinitionSummaries(
      UUID tenantId, String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow definition version.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @return the latest workflow definition version
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition could not be
   *     found
   * @throws ServiceUnavailableException if the latest workflow definition version could not be
   *     retrieved
   */
  WorkflowDefinition getWorkflowDefinitionVersion(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocumentId the ID for the workflow document
   * @return the workflow document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be retrieved
   */
  WorkflowDocument getWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException;

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
   * @return the workflow documents
   * @throws InvalidArgumentException if an argument is invalid
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
      Integer pageSize)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow engine.
   *
   * @param workflowEngineId the ID for the workflow engine
   * @return the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowEngineNotFoundException if the workflow engine could not be found
   * @throws ServiceUnavailableException if the workflow engine could not be retrieved
   */
  WorkflowEngine getWorkflowEngine(String workflowEngineId)
      throws InvalidArgumentException, WorkflowEngineNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow engine connector for the workflow engine with the specified ID.
   *
   * @param workflowEngineId the ID for the workflow engine
   * @return the workflow engine connector for the workflow engine with the specified ID
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the workflow engine connector could not be retrieved
   */
  WorkflowEngineConnector getWorkflowEngineConnector(String workflowEngineId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the IDs for the workflow engines.
   *
   * @return the IDs for the workflow engines
   * @throws ServiceUnavailableException if the IDs for the workflow engines could not be retrieved
   */
  List<String> getWorkflowEngineIds() throws ServiceUnavailableException;

  /**
   * Retrieve the workflow engines.
   *
   * @return the workflow engines
   * @throws ServiceUnavailableException if the workflow engines could not be retrieved
   */
  List<WorkflowEngine> getWorkflowEngines() throws ServiceUnavailableException;

  /**
   * Retrieve the workflow ID for the workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDocumentId the ID for the workflow document
   * @return the workflow ID for the workflow document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow ID could not be retrieved for the workflow
   *     document
   */
  UUID getWorkflowIdForWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowNoteId the ID for the workflow note
   * @return the workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be retrieved
   */
  WorkflowNote getWorkflowNote(UUID tenantId, UUID workflowNoteId)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the workflow notes for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow notes are associated with
   * @param filter the filter to apply to the workflow notes
   * @param sortBy the method used to sort the workflow notes e.g. by created at
   * @param sortDirection the sort direction to apply to the workflow notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the workflow notes
   * @throws InvalidArgumentException if an argument is invalid
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
      Integer pageSize)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

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
   * @return the summaries for the workflows
   * @throws InvalidArgumentException if an argument is invalid
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
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Initiate a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param initiateWorkflowRequest the request to initiate a workflow
   * @param initiatedBy the person or system initiating the workflow
   * @return the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws InteractionNotFoundException if an interaction linked to the workflow could not be
   *     found
   * @throws ServiceUnavailableException if the workflow could not be initiated
   */
  Workflow initiateWorkflow(
      UUID tenantId, InitiateWorkflowRequest initiateWorkflowRequest, String initiatedBy)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          InteractionNotFoundException,
          ServiceUnavailableException;

  /**
   * Initiate a workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param initiateWorkflowStepRequest the request to initiate a workflow step
   * @return the workflow step
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow step could not be initiated
   */
  WorkflowStep initiateWorkflowStep(
      UUID tenantId, InitiateWorkflowStepRequest initiateWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Check whether a workflow attribute with the specified code is valid for a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinitionId the ID for the workflow definition the workflow is associated with
   * @param attributeCode the code for the workflow attribute
   * @return {@code true} if a workflow attribute with the specified code is valid or {@code false}
   *     otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the validity of the workflow attribute code could not be
   *     verified
   */
  boolean isValidWorkflowAttribute(UUID tenantId, String workflowDefinitionId, String attributeCode)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Link an interaction to a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param linkInteractionToWorkflowRequest the request to link an interaction to a workflow
   * @param linkedBy the person or system linking the interaction to the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the interaction could not be linked to the workflow
   */
  void linkInteractionToWorkflow(
      UUID tenantId,
      LinkInteractionToWorkflowRequest linkInteractionToWorkflowRequest,
      String linkedBy)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          WorkflowNotFoundException,
          ServiceUnavailableException;

  /**
   * Provide a workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param provideWorkflowDocumentRequest the request to provide a workflow document
   * @param providedBy the person or system providing the workflow document
   * @return the ID for the document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be provided
   */
  UUID provideWorkflowDocument(
      UUID tenantId,
      ProvideWorkflowDocumentRequest provideWorkflowDocumentRequest,
      String providedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException;

  /**
   * Reject a workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param rejectWorkflowDocumentRequest the request to reject a workflow document
   * @param rejectedBy the person or system rejecting the workflow document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be rejected
   */
  void rejectWorkflowDocument(
      UUID tenantId, RejectWorkflowDocumentRequest rejectWorkflowDocumentRequest, String rejectedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException;

  /**
   * Request a workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param requestWorkflowDocumentRequest the request to request a workflow document
   * @param requestedBy the person or system requesting the workflow document
   * @return the ID for the workflow document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the workflow document could not be requested
   */
  UUID requestWorkflowDocument(
      UUID tenantId,
      RequestWorkflowDocumentRequest requestWorkflowDocumentRequest,
      String requestedBy)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Set the status for the workflow and, if applicable, the status for any associated workflow
   * steps.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param status the new status for the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the status for the workflow could not be set
   */
  void setWorkflowStatus(UUID tenantId, UUID workflowId, WorkflowStatus status)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Start an existing workflow.
   *
   * @param tenantId the ID for the tenant
   * @param startWorkflowRequest the request to start a workflow
   * @param startedBy the person or system starting the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InvalidWorkflowStatusException the status of the workflow is invalid for the operation
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be started
   */
  void startWorkflow(UUID tenantId, StartWorkflowRequest startWorkflowRequest, String startedBy)
      throws InvalidArgumentException,
          InvalidWorkflowStatusException,
          WorkflowNotFoundException,
          ServiceUnavailableException;

  /**
   * Suspend a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param suspendWorkflowRequest the request to suspend a workflow
   * @param suspendedBy the person or system who suspended the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be suspended
   */
  void suspendWorkflow(
      UUID tenantId, SuspendWorkflowRequest suspendWorkflowRequest, String suspendedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Suspend a workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param suspendWorkflowStepRequest the request to suspend a workflow step
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws WorkflowStepNotFoundException if the workflow step could not be found
   * @throws ServiceUnavailableException if the workflow step could not be suspended
   */
  void suspendWorkflowStep(UUID tenantId, SuspendWorkflowStepRequest suspendWorkflowStepRequest)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowStepNotFoundException,
          ServiceUnavailableException;

  /**
   * Unsuspend a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param unsuspendWorkflowRequest the request to unsuspend a workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be unsuspended
   */
  void unsuspendWorkflow(UUID tenantId, UnsuspendWorkflowRequest unsuspendWorkflowRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Unsuspend a workflow step.
   *
   * @param tenantId the ID for the tenant
   * @param unsuspendWorkflowStepRequest the request to unsuspend a workflow step
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws WorkflowStepNotFoundException if the workflow step could not be found
   * @throws ServiceUnavailableException if the workflow step could not be unsuspended
   */
  void unsuspendWorkflowStep(
      UUID tenantId, UnsuspendWorkflowStepRequest unsuspendWorkflowStepRequest)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowStepNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param updateWorkflowRequest the request to update the workflow
   * @param updatedBy the person or system updating the workflow
   * @return the updated workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be created
   */
  Workflow updateWorkflow(
      UUID tenantId, UpdateWorkflowRequest updateWorkflowRequest, String updatedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow attribute definition.
   *
   * @param workflowAttributeDefinition the workflow attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowAttributeDefinitionNotFoundException if the workflow attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the workflow attribute definition could not be updated
   */
  void updateWorkflowAttributeDefinition(WorkflowAttributeDefinition workflowAttributeDefinition)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the workflow definition version.
   *
   * @param workflowDefinition the workflow definition version
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition version could not
   *     be found
   * @throws WorkflowEngineNotFoundException if the workflow engine could not be found
   * @throws DocumentDefinitionNotFoundException if a document definition reference by the workflow
   *     definition version could not be found
   * @throws ServiceUnavailableException if the workflow definition version could not be updated
   */
  void updateWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          WorkflowEngineNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the workflow definition category.
   *
   * @param workflowDefinitionCategory the workflow definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws ServiceUnavailableException if the workflow definition category could not be updated
   */
  void updateWorkflowDefinitionCategory(WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the workflow engine.
   *
   * @param workflowEngine the workflow engine
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowEngineNotFoundException if the workflow engine could not be found
   * @throws ServiceUnavailableException if the workflow engine could not be updated
   */
  void updateWorkflowEngine(WorkflowEngine workflowEngine)
      throws InvalidArgumentException, WorkflowEngineNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param updateWorkflowNoteRequest the request to update a workflow note
   * @param updatedBy the person or system updating the workflow note
   * @return the updated workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be updated
   */
  WorkflowNote updateWorkflowNote(
      UUID tenantId, UpdateWorkflowNoteRequest updateWorkflowNoteRequest, String updatedBy)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException;

  /**
   * Validate whether the required workflow attributes have been provided for the workflow
   * definition.
   *
   * @param tenantId the ID for the tenant
   * @param parameter the parameter path to the attributes being validated, e.g.
   *     initiateWorkflowRequest.attributes
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowAttributes the workflow attributes to validate
   * @throws InvalidArgumentException if a required workflow attribute has not been provided
   * @throws ServiceUnavailableException if the required workflow attributes could not be validated
   */
  void validateRequiredWorkflowAttributes(
      UUID tenantId,
      String parameter,
      String workflowDefinitionId,
      List<WorkflowAttribute> workflowAttributes)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Verify a workflow document.
   *
   * @param tenantId the ID for the tenant
   * @param verifyWorkflowDocumentRequest the request to verify a workflow document
   * @param verifiedBy the person or system verifying the workflow document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDocumentNotFoundException if the workflow document could not be found
   * @throws ServiceUnavailableException if the workflow document could not be verified
   */
  void verifyWorkflowDocument(
      UUID tenantId, VerifyWorkflowDocumentRequest verifyWorkflowDocumentRequest, String verifiedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException;

  /**
   * Check whether the workflow definition category exists.
   *
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @return {@code true} if the workflow definition category exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the workflow definition category failed
   */
  boolean workflowDefinitionCategoryExists(String workflowDefinitionCategoryId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the workflow definition exists.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @return {@code true} if the workflow definition exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the workflow definition failed
   */
  boolean workflowDefinitionExists(String workflowDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the workflow definition exists and is associated with the workflow definition
   * category with the specified ID.
   *
   * @param workflowDefinitionCategoryId the ID for the workflow definition category
   * @param workflowDefinitionId the ID for the workflow definition
   * @return {@code true} if the workflow definition exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the workflow definition failed
   */
  boolean workflowDefinitionExists(String workflowDefinitionCategoryId, String workflowDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the workflow definition version exists.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version for the workflow definition
   * @return {@code true} if the workflow definition version exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the workflow definition version failed
   */
  boolean workflowDefinitionVersionExists(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the workflow document exists.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow document is associated with
   * @param workflowDocumentId the ID for the workflow document
   * @return {@code true} if the workflow document exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the existence of the workflow document could not be
   *     determined
   */
  boolean workflowDocumentExists(UUID tenantId, UUID workflowId, UUID workflowDocumentId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the workflow engine exists.
   *
   * @param workflowEngineId the ID for the workflow engine
   * @return {@code true} if the workflow engine exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the workflow engine failed
   */
  boolean workflowEngineExists(String workflowEngineId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the workflow exists.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @return {@code true} if the workflow exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the existence of the workflow could not be determined
   */
  boolean workflowExists(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the workflow note exists.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow the workflow note is associated with
   * @param workflowNoteId the ID for the workflow note
   * @return {@code true} if the workflow note exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the existence of the workflow note could not be
   *     determined
   */
  boolean workflowNoteExists(UUID tenantId, UUID workflowId, UUID workflowNoteId)
      throws InvalidArgumentException, ServiceUnavailableException;
}

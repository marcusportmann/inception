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
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
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
   * Create a new workflow.
   *
   * @param tenantId the ID for the tenant
   * @param createWorkflowRequest the request to create a workflow
   * @param createdBy the username for the user creating the workflow
   * @return the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws ServiceUnavailableException if the workflow could not be created
   */
  Workflow createWorkflow(
      UUID tenantId, CreateWorkflowRequest createWorkflowRequest, String createdBy)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
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
   * @throws ServiceUnavailableException if the workflow definition version could not be created
   */
  void createWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionVersionException,
          WorkflowDefinitionCategoryNotFoundException,
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
   * @param createdBy the username for the user who created the workflow note
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
   * @throws ServiceUnavailableException if the workflow definition categories could not be
   *     retrieved
   */
  List<WorkflowDefinitionCategory> getWorkflowDefinitionCategories(UUID tenantId)
      throws ServiceUnavailableException;

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
   * Retrieve the workflow engines.
   *
   * @return the workflow engines
   * @throws ServiceUnavailableException if the workflow engines could not be retrieved
   */
  List<WorkflowEngine> getWorkflowEngines() throws ServiceUnavailableException;

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
   * @param maxResults the maximum number of workflow notes that should be retrieved
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
      Integer pageSize,
      int maxResults)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the workflows.
   *
   * @param tenantId the ID for the tenant
   * @param definitionId the workflow definition ID filter to apply to the workflow summaries
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
      String definitionId,
      WorkflowStatus status,
      String filter,
      WorkflowSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Update the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param updateWorkflowRequest the request to update the workflow
   * @param updatedBy the username for the user updating the workflow
   * @return the updated workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be created
   */
  Workflow updateWorkflow(
      UUID tenantId, UpdateWorkflowRequest updateWorkflowRequest, String updatedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow definition version.
   *
   * @param workflowDefinition the workflow definition version
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionCategoryNotFoundException if the workflow definition category could
   *     not be found
   * @throws WorkflowDefinitionVersionNotFoundException if the workflow definition version could not
   *     be found
   * @throws ServiceUnavailableException if the workflow definition version could not be updated
   */
  void updateWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
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
   * @param updatedBy the username for the user updating the workflow note
   * @return the updated workflow note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be updated
   */
  WorkflowNote updateWorkflowNote(
      UUID tenantId, UpdateWorkflowNoteRequest updateWorkflowNoteRequest, String updatedBy)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException;

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
   * Check whether the workflow with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the workflow is associated with
   * @param workflowId the ID for the workflow
   * @return {@code true} if the workflow with the specified tenant ID and ID exists or {@code
   *     false} otherwise
   * @throws ServiceUnavailableException if the existence of the workflow could not be determined
   */
  boolean workflowExists(UUID tenantId, UUID workflowId) throws ServiceUnavailableException;

  /**
   * Check whether the workflow note with the specified tenant ID, workflow ID and ID exists.
   *
   * @param tenantId the ID for the tenant the workflow note is associated with
   * @param workflowId the ID for the workflow the workflow note is associated with
   * @param workflowNoteId the ID for the workflow note
   * @return {@code true} if the workflow note with the specified tenant ID, workflow ID and ID
   *     exists or {@code false} otherwise
   * @throws ServiceUnavailableException if the existence of the workflow note could not be
   *     determined
   */
  boolean workflowNoteExists(UUID tenantId, UUID workflowId, UUID workflowNoteId)
      throws ServiceUnavailableException;
}

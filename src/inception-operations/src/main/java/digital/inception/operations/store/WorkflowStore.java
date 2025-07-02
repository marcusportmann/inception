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
import digital.inception.operations.exception.DuplicateWorkflowException;
import digital.inception.operations.exception.DuplicateWorkflowNoteException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowSummaries;
import java.util.UUID;

/**
 * The {@code WorkflowStore} interface defines the functionality provided by a workflow store, which
 * manages the persistence of workflows.
 *
 * @author Marcus Portmann
 */
public interface WorkflowStore {

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
   * Create the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowNote the workflow note
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
      WorkflowStatus status,
      String filter,
      WorkflowSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException;

  /**
   * Update the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflow the workflow
   * @return the workflow
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be updated
   */
  Workflow updateWorkflow(UUID tenantId, Workflow workflow)
      throws WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow note.
   *
   * @param tenantId the ID for the tenant
   * @param workflowNote the workflow note
   * @throws WorkflowNoteNotFoundException if the workflow note could not be found
   * @throws ServiceUnavailableException if the workflow note could not be updated
   */
  WorkflowNote updateWorkflowNote(UUID tenantId, WorkflowNote workflowNote)
      throws WorkflowNoteNotFoundException, ServiceUnavailableException;

  /**
   * Returns whether a workflow with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the workflow is associated with
   * @param workflowId the ID for the workflow
   * @return {@code true} if a workflow with the specified tenant ID and ID exists or {@code false}
   *     otherwise
   * @throws ServiceUnavailableException if the existence of the workflow could not be determined
   */
  boolean workflowExists(UUID tenantId, UUID workflowId) throws ServiceUnavailableException;
}

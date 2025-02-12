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

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.model.DuplicateWorkflowException;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.model.WorkflowNotFoundException;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowSummaries;
import java.util.UUID;

/**
 * The <b>WorkflowStore</b> interface defines the functionality provided by a workflow store, which
 * manages the persistence of workflows.
 *
 * @author Marcus Portmann
 */
public interface WorkflowStore {

  /**
   * Create the new workflow.
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
}

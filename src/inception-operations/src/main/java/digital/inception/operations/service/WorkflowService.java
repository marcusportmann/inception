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

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.DuplicateWorkflowDefinitionException;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionNotFoundException;
import digital.inception.operations.model.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.model.WorkflowNotFoundException;
import java.util.UUID;

/**
 * The {@code WorkflowService} interface defines the functionality provided by a Workflow Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface WorkflowService {

  /** The ID for the default tenant. */
  UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  /**
   * Create a new workflow.
   *
   * @param tenantId the ID for the tenant
   * @param createWorkflowRequest the request to create a workflow
   * @param createdBy the person or system requesting the workflow creation
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
   * Create the new workflow definition.
   *
   * @param workflowDefinition the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWorkflowDefinitionException if the workflow definition already exists
   * @throws ServiceUnavailableException if the workflow definition could not be created
   */
  void createWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionException,
          ServiceUnavailableException;

  /**
   * Delete the workflow.
   *
   * @param workflowId the ID for the workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be deleted
   */
  void deleteWorkflow(UUID workflowId)
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
   * Update the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param updateWorkflowRequest the request to update the workflow
   * @param updatedBy the person or system requesting the workflow update
   * @return the updated workflow
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowNotFoundException if the workflow could not be found
   * @throws ServiceUnavailableException if the workflow could not be created
   */
  Workflow updateWorkflow(
      UUID tenantId, UpdateWorkflowRequest updateWorkflowRequest, String updatedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException;

  /**
   * Update the workflow definition.
   *
   * @param workflowDefinition the workflow definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WorkflowDefinitionNotFoundException if the workflow definition could not be found
   * @throws ServiceUnavailableException if the workflow definition could not be updated
   */
  void updateWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException;

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
}

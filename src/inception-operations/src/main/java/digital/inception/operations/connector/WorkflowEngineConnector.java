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

package digital.inception.operations.connector;

import digital.inception.operations.model.ValidWorkflowDefinitionAttribute;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowFormType;
import digital.inception.operations.model.WorkflowStatus;
import java.util.List;
import java.util.UUID;

/**
 * The {@code WorkflowEngineConnector} interface defines the interface that must be implemented by
 * all workflow engine connectors.
 *
 * @author Marcus Portmann
 */
public interface WorkflowEngineConnector {

  /**
   * Cancel a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinition the workflow definition
   * @param workflow the workflow
   * @throws WorkflowEngineConnectorException if the workflow could not be canceled
   */
  void cancelWorkflow(UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException;

  /**
   * Returns the valid workflow definition attributes for the workflow engine connector.
   *
   * @return the valid workflow definition attributes for the workflow engine connector
   */
  List<ValidWorkflowDefinitionAttribute> getValidWorkflowDefinitionAttributes();

  /**
   * Retrieve the data for a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinition the workflow definition
   * @param workflow the workflow
   * @return the workflow-engine-specific data for the workflow
   * @throws WorkflowEngineConnectorException if the data could not be retrieved for the workflow
   */
  byte[] getWorkflowData(UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException;

  /**
   * Retrieve the workflow form for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinition the workflow definition
   * @param workflow the workflow
   * @param workflowFormType the workflow form type
   * @return the workflow form
   * @throws WorkflowEngineConnectorException if the workflow form could not be retrieved for the
   *     workflow
   */
  byte[] getWorkflowForm(
      UUID tenantId,
      WorkflowDefinition workflowDefinition,
      Workflow workflow,
      WorkflowFormType workflowFormType)
      throws WorkflowEngineConnectorException;

  /**
   * Retrieve the status of the workflow from the workflow engine.
   *
   * @param tenantId the ID for the tenant
   * @param workflow the workflow
   * @return the status of the workflow retrieved from the workflow engine
   * @throws WorkflowEngineConnectorException if the status of the workflow could not be retrieved
   *     from the workflow engine
   */
  WorkflowStatus getWorkflowStatus(UUID tenantId, Workflow workflow)
      throws WorkflowEngineConnectorException;

  /**
   * Start a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinition the workflow definition
   * @param attributes the workflow attributes
   * @param data the XML or JSON data for the workflow
   * @return the ID of the corresponding process or case instance in the workflow engine
   * @throws WorkflowEngineConnectorException if the workflow could not be started
   */
  String startWorkflow(
      UUID tenantId,
      WorkflowDefinition workflowDefinition,
      List<WorkflowAttribute> attributes,
      String data)
      throws WorkflowEngineConnectorException;

  /**
   * Indicates whether the workflow engine connector can fetch a workflow’s current status from the
   * underlying workflow engine.
   *
   * @return {@code true} if workflow status retrieval from the workflow engine is supported or
   *     {@code false} otherwise
   */
  boolean supportsWorkflowStatusRetrieval();

  /**
   * Suspend a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinition the workflow definition
   * @param workflow the workflow
   * @throws WorkflowEngineConnectorException if the workflow could not be suspended
   */
  void suspendWorkflow(UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException;

  /**
   * Unsuspend a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinition the workflow definition
   * @param workflow the workflow
   * @throws WorkflowEngineConnectorException if the workflow could not be unsuspended
   */
  void unsuspendWorkflow(UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException;

  /**
   * Update the workflow data.
   *
   * @param tenantId the ID for the tenant
   * @param workflowDefinition the workflow definition
   * @param workflow the workflow
   * @param data the updated XML or JSON data for the workflow
   * @throws WorkflowEngineConnectorException if the workflow data could not be updated
   */
  void updateWorkflowData(
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow, String data)
      throws WorkflowEngineConnectorException;
}

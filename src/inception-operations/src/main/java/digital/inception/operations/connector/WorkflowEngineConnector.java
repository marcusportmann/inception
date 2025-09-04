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

import digital.inception.operations.model.EventType;
import digital.inception.operations.model.ValidWorkflowDefinitionAttribute;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowFormType;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowVariable;
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
   * @param workflowId the ID for the workflow
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   * @throws WorkflowEngineConnectorException if the workflow could not be canceled
   */
  void cancelWorkflow(UUID tenantId, UUID workflowId, String engineInstanceId)
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
   * @param workflowId the ID for the workflow
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   * @return the workflow-engine-specific data for the workflow
   * @throws WorkflowEngineConnectorException if the data could not be retrieved for the workflow
   */
  byte[] getWorkflowData(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException;

  /**
   * Retrieve the workflow form for the workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   * @param workflowFormType the workflow form type
   * @return the workflow form
   * @throws WorkflowEngineConnectorException if the workflow form could not be retrieved for the
   *     workflow
   */
  byte[] getWorkflowForm(
      UUID tenantId, UUID workflowId, String engineInstanceId, WorkflowFormType workflowFormType)
      throws WorkflowEngineConnectorException;

  /**
   * Retrieve the status of the workflow from the workflow engine.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   * @return the status of the workflow retrieved from the workflow engine
   * @throws WorkflowEngineConnectorException if the status of the workflow could not be retrieved
   *     from the workflow engine
   */
  WorkflowStatus getWorkflowStatus(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException;

  /**
   * Process the workflow document event.
   *
   * @param workflowDefinition the workflow definition
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   * @param workflowDocumentId the ID for the workflow document
   * @param eventType the event type
   * @throws WorkflowEngineConnectorException if the workflow document event could not be processed
   */
  void processWorkflowDocumentEvent(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId,
      UUID workflowDocumentId,
      EventType eventType)
      throws WorkflowEngineConnectorException;

  /**
   * Start a workflow.
   *
   * @param workflowDefinition the workflow definition
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param attributes the workflow attributes
   * @param variables the workflow variables
   * @param data the JSON or XML data for the workflow
   * @return the ID for the corresponding process or case instance in the workflow engine
   * @throws WorkflowEngineConnectorException if the workflow could not be started
   */
  String startWorkflow(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      List<WorkflowAttribute> attributes,
      List<WorkflowVariable> variables,
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
   * @param workflowId the ID for the workflow
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   * @throws WorkflowEngineConnectorException if the workflow could not be suspended
   */
  void suspendWorkflow(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException;

  /**
   * Unsuspend a workflow.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   * @throws WorkflowEngineConnectorException if the workflow could not be unsuspended
   */
  void unsuspendWorkflow(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException;

  /**
   * Update the workflow data.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   * @param data the updated JSON or XML data for the workflow
   * @throws WorkflowEngineConnectorException if the workflow data could not be updated
   */
  void updateWorkflowData(UUID tenantId, UUID workflowId, String engineInstanceId, String data)
      throws WorkflowEngineConnectorException;
}

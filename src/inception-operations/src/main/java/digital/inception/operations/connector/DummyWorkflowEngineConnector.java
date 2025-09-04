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
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowFormType;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowVariable;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationContext;

/**
 * The {@code DummyWorkflowEngineConnector} class provides the dummy workflow engine connector.
 *
 * @author Marcus Portmann
 */
public class DummyWorkflowEngineConnector extends AbstractWorkflowEngineConnectorBase
    implements WorkflowEngineConnector {

  /**
   * Constructs a {@code DummyWorkflowEngineConnector}.
   *
   * @param applicationContext the Spring application context
   * @param workflowEngine the workflow engine the workflow engine connector is associated with
   */
  public DummyWorkflowEngineConnector(
      ApplicationContext applicationContext, WorkflowEngine workflowEngine) {
    super(applicationContext, workflowEngine);
  }

  @Override
  public void cancelWorkflow(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException {
    log.info("Cancelling the workflow (" + workflowId + ") for the tenant (" + tenantId + ")");
  }

  @Override
  public List<ValidWorkflowDefinitionAttribute> getValidWorkflowDefinitionAttributes() {
    return List.of(
        new ValidWorkflowDefinitionAttribute(
            "process_definition_key", "Process Definition Key", true));
  }

  @Override
  public byte[] getWorkflowData(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException {
    return new byte[0];
  }

  @Override
  public byte[] getWorkflowForm(
      UUID tenantId, UUID workflowId, String engineInstanceId, WorkflowFormType workflowFormType)
      throws WorkflowEngineConnectorException {
    return new byte[0];
  }

  @Override
  public WorkflowStatus getWorkflowStatus(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException {
    log.info(
        "Retrieving the status of the workflow ("
            + workflowId
            + ") from the workflow engine for the tenant ("
            + tenantId
            + ")");

    return WorkflowStatus.UNKNOWN;
  }

  @Override
  public void processWorkflowDocumentEvent(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId,
      UUID workflowDocumentId,
      EventType eventType)
      throws WorkflowEngineConnectorException {
    log.info(
        "Processing the workflow document event ("
            + eventType
            + ") for the workflow document ("
            + workflowDocumentId
            + ") for the workflow ("
            + workflowId
            + ") for the tenant ("
            + tenantId
            + ")");
  }

  @Override
  public String startWorkflow(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      List<WorkflowAttribute> attributes,
      List<WorkflowVariable> variables,
      String data)
      throws WorkflowEngineConnectorException {
    String engineWorkflowId = UUID.randomUUID().toString();

    log.info(
        "Starting the workflow ("
            + engineWorkflowId
            + ") with the workflow definition ("
            + workflowDefinition.getId()
            + ") version ("
            + workflowDefinition.getVersion()
            + ") for the tenant ("
            + tenantId
            + ")");

    return engineWorkflowId;
  }

  @Override
  public boolean supportsWorkflowStatusRetrieval() {
    return true;
  }

  @Override
  public void suspendWorkflow(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException {
    log.info("Suspending the workflow (" + workflowId + ") for the tenant (" + tenantId + ")");
  }

  @Override
  public void unsuspendWorkflow(UUID tenantId, UUID workflowId, String engineInstanceId)
      throws WorkflowEngineConnectorException {
    log.info("Unsuspending the workflow (" + workflowId + ") for the tenant (" + tenantId + ")");
  }

  @Override
  public void updateWorkflowData(
      UUID tenantId, UUID workflowId, String engineInstanceId, String data)
      throws WorkflowEngineConnectorException {
    log.info(
        "Updating the data for the workflow ("
            + workflowId
            + ") for the tenant ("
            + tenantId
            + ")");
  }
}

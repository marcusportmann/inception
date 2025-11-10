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

import digital.inception.operations.connector.AbstractWorkflowEngineConnectorBase;
import digital.inception.operations.connector.WorkflowEngineConnector;
import digital.inception.operations.connector.WorkflowEngineConnectorException;
import digital.inception.operations.model.EventType;
import digital.inception.operations.model.FormDefinition;
import digital.inception.operations.model.ValidWorkflowDefinitionAttribute;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowFormType;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowVariable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.ApplicationContext;

/**
 * The {@code TestWorkflowEngineConnector} class provides a workflow engine connector implementation
 * for testing purposes.
 *
 * @author Marcus Portmann
 */
public class TestWorkflowEngineConnector extends AbstractWorkflowEngineConnectorBase
    implements WorkflowEngineConnector {

  /**
   * Constructs a {@code TestWorkflowEngineConnector}.
   *
   * @param applicationContext the Spring application context
   * @param workflowEngine the workflow engine the workflow engine connector is associated with
   */
  public TestWorkflowEngineConnector(
      ApplicationContext applicationContext, WorkflowEngine workflowEngine) {
    super(applicationContext, workflowEngine);
  }

  @Override
  public void cancelWorkflow(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId)
      throws WorkflowEngineConnectorException {
    log.info(
        "Cancelling the workflow ("
            + workflowId
            + ") with the engine instance ID ("
            + engineInstanceId
            + ") for the tenant ("
            + tenantId
            + ")");
  }

  @Override
  public Optional<FormDefinition> getFormDefinition(
      WorkflowDefinition workflowDefinition, WorkflowFormType workflowFormType)
      throws WorkflowEngineConnectorException {
    return Optional.empty();
  }

  @Override
  public List<ValidWorkflowDefinitionAttribute> getValidWorkflowDefinitionAttributes() {
    return List.of(
        new ValidWorkflowDefinitionAttribute(
            "processDefinitionKey", "Process Definition Key", false),
        new ValidWorkflowDefinitionAttribute("attributeName", "Attribute Name", false),
        new ValidWorkflowDefinitionAttribute(
            "anotherAttributeName", "Another Attribute Name", false));
  }

  @Override
  public byte[] getWorkflowData(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId)
      throws WorkflowEngineConnectorException {
    return new byte[0];
  }

  @Override
  public WorkflowStatus getWorkflowStatus(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId)
      throws WorkflowEngineConnectorException {
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
            + ") with the engine instance ID ("
            + engineInstanceId
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
  public void suspendWorkflow(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId)
      throws WorkflowEngineConnectorException {
    log.info(
        "Suspending the workflow ("
            + workflowId
            + ") with the engine instance ID ("
            + engineInstanceId
            + ") for the tenant ("
            + tenantId
            + ")");
  }

  @Override
  public void unsuspendWorkflow(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId)
      throws WorkflowEngineConnectorException {
    log.info(
        "Unsuspending the workflow ("
            + workflowId
            + ") with the engine instance ID ("
            + engineInstanceId
            + ") for the tenant ("
            + tenantId
            + ")");
  }

  @Override
  public void updateWorkflowData(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId,
      String data)
      throws WorkflowEngineConnectorException {
    log.info(
        "Updating the data for the workflow ("
            + workflowId
            + ") with the engine instance ID ("
            + engineInstanceId
            + ") for the tenant ("
            + tenantId
            + ")");
  }
}

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
 * The {@code FlowableEmbeddedWorkflowEngineConnector} class provides the workflow engine connector
 * for the embedded Flowable execution engine.
 *
 * @author Marcus Portmann
 */
public class FlowableEmbeddedWorkflowEngineConnector extends AbstractWorkflowEngineConnectorBase
    implements WorkflowEngineConnector {

  /**
   * Constructs a {@code FlowableEmbeddedWorkflowEngineConnector}.
   *
   * @param applicationContext the Spring application context
   * @param workflowEngine the workflow engine the workflow engine connector is associated with
   */
  public FlowableEmbeddedWorkflowEngineConnector(
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
    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to cancel the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public Optional<FormDefinition> getFormDefinition(
      WorkflowDefinition workflowDefinition, WorkflowFormType workflowFormType)
      throws WorkflowEngineConnectorException {
    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to retrieve the form definition for the workflow form type ("
              + workflowFormType
              + ") for the workflow definition ("
              + workflowDefinition.getId()
              + ") version ("
              + workflowDefinition.getVersion()
              + ")",
          e);
    }
  }

  @Override
  public List<ValidWorkflowDefinitionAttribute> getValidWorkflowDefinitionAttributes() {
    return List.of(
        new ValidWorkflowDefinitionAttribute(
            "process_definition_key", "Process Definition Key", true));
  }

  @Override
  public byte[] getWorkflowData(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId)
      throws WorkflowEngineConnectorException {
    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to retrieve the data for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public WorkflowStatus getWorkflowStatus(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId)
      throws WorkflowEngineConnectorException {
    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to retrieve the status for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
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

    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to process the workflow document event ("
              + eventType
              + ") for the workflow document ("
              + workflowDocumentId
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
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
    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to start the workflow with the workflow definition ("
              + workflowDefinition.getId()
              + ") version ("
              + workflowDefinition.getVersion()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
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
    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to suspend the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public void unsuspendWorkflow(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId)
      throws WorkflowEngineConnectorException {
    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to unsuspend the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public void updateWorkflowData(
      WorkflowDefinition workflowDefinition,
      UUID tenantId,
      UUID workflowId,
      String engineInstanceId,
      String data)
      throws WorkflowEngineConnectorException {
    try {
      throw new RuntimeException("Not Implemented");
    } catch (Throwable e) {
      throw new WorkflowEngineConnectorException(
          "Failed to update the data for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }
}

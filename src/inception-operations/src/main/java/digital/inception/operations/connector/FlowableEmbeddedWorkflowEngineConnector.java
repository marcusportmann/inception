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

import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowFormType;
import java.util.List;
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
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException {
    throw new WorkflowEngineConnectorException("Not Implemented");
  }

  @Override
  public byte[] getWorkflowData(
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException {
    throw new WorkflowEngineConnectorException("Not Implemented");
  }

  @Override
  public byte[] getWorkflowForm(
      UUID tenantId,
      WorkflowDefinition workflowDefinition,
      Workflow workflow,
      WorkflowFormType workflowFormType)
      throws WorkflowEngineConnectorException {
    throw new WorkflowEngineConnectorException("Not Implemented");
  }

  @Override
  public String initiateWorkflow(
      UUID tenantId,
      WorkflowDefinition workflowDefinition,
      List<WorkflowAttribute> attributes,
      String data)
      throws WorkflowEngineConnectorException {
    throw new WorkflowEngineConnectorException("Not Implemented");
  }

  @Override
  public void suspendWorkflow(
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException {
    throw new WorkflowEngineConnectorException("Not Implemented");
  }

  @Override
  public void unsuspendWorkflow(
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException {
    throw new WorkflowEngineConnectorException("Not Implemented");
  }

  @Override
  public void updateWorkflowData(
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow, String data)
      throws WorkflowEngineConnectorException {
    throw new WorkflowEngineConnectorException("Not Implemented");
  }
}

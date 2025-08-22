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
import digital.inception.operations.model.ValidWorkflowDefinitionAttribute;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowFormType;
import digital.inception.operations.model.WorkflowStatus;
import java.util.List;
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
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException {
    log.info(
        "Cancelling the workflow ("
            + workflow.getId()
            + ") with the workflow definition ("
            + workflowDefinition.getId()
            + ") version ("
            + workflowDefinition.getVersion()
            + ") for the tenant ("
            + tenantId
            + ")");
  }

  @Override
  public List<ValidWorkflowDefinitionAttribute> getValidWorkflowDefinitionAttributes() {
    return List.of(
        new ValidWorkflowDefinitionAttribute(
            "process_definition_key", "Process Definition Key", false),
        new ValidWorkflowDefinitionAttribute("attribute_name", "Attribute Name", false),
        new ValidWorkflowDefinitionAttribute(
            "another_attribute_name", "Another Attribute Name", false));
  }

  @Override
  public byte[] getWorkflowData(
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException {
    return new byte[0];
  }

  @Override
  public byte[] getWorkflowForm(
      UUID tenantId,
      WorkflowDefinition workflowDefinition,
      Workflow workflow,
      WorkflowFormType workflowFormType)
      throws WorkflowEngineConnectorException {
    return new byte[0];
  }

  @Override
  public WorkflowStatus getWorkflowStatus(UUID tenantId, Workflow workflow)
      throws WorkflowEngineConnectorException {
    return WorkflowStatus.UNKNOWN;
  }

  @Override
  public String startWorkflow(
      UUID tenantId,
      WorkflowDefinition workflowDefinition,
      List<WorkflowAttribute> attributes,
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
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException {
    log.info(
        "Suspending the workflow ("
            + workflow.getId()
            + ") with the workflow definition ("
            + workflowDefinition.getId()
            + ") version ("
            + workflowDefinition.getVersion()
            + ") for the tenant ("
            + tenantId
            + ")");
  }

  @Override
  public void unsuspendWorkflow(
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow)
      throws WorkflowEngineConnectorException {
    log.info(
        "Unsuspending the workflow ("
            + workflow.getId()
            + ") with the workflow definition ("
            + workflowDefinition.getId()
            + ") version ("
            + workflowDefinition.getVersion()
            + ") for the tenant ("
            + tenantId
            + ")");
  }

  @Override
  public void updateWorkflowData(
      UUID tenantId, WorkflowDefinition workflowDefinition, Workflow workflow, String data)
      throws WorkflowEngineConnectorException {
    log.info(
        "Updating the data for the workflow ("
            + workflow.getId()
            + ") with the workflow definition ("
            + workflowDefinition.getId()
            + ") version ("
            + workflowDefinition.getVersion()
            + ") for the tenant ("
            + tenantId
            + ")");
  }
}

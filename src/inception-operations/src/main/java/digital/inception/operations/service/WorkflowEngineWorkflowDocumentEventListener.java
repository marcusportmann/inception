/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.operations.service;
//
// import digital.inception.operations.connector.WorkflowEngineConnector;
// import digital.inception.operations.model.WorkflowEngineIds;
// import digital.inception.operations.service.WorkflowService.WorkflowDocumentEvent;
// import java.util.UUID;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.context.event.EventListener;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Component;
//
/// **
// * The {@code WorkflowEngineWorkflowDocumentEventListener} class.
// *
// * @author Marcus Portmann
// */
// @Component
// public class WorkflowEngineWorkflowDocumentEventListener {
//
//  /* Logger */
//  private static final Logger log =
//      LoggerFactory.getLogger(WorkflowEngineWorkflowDocumentEventListener.class);
//
//  /** The Workflow Service. */
//  private final WorkflowService workflowService;
//
//  /**
//   * Constructs a new {@code WorkflowEngineWorkflowDocumentEventListener}
//   *
//   * @param workflowService the Workflow Service
//   */
//  public WorkflowEngineWorkflowDocumentEventListener(WorkflowService workflowService) {
//    this.workflowService = workflowService;
//  }
//
//  /**
//   * Process a workflow document event.
//   *
//   * @param workflowDocumentEvent the workflow document event
//   */
//  @Async("workflowDocumentEventExecutor")
//  @EventListener
//  @SuppressWarnings("unused")
//  public void processWorkflowDocumentEvent(WorkflowDocumentEvent workflowDocumentEvent) {
//    try {
//      UUID workflowId =
//          workflowService.getWorkflowIdForWorkflowDocument(
//              workflowDocumentEvent.tenantId(), workflowDocumentEvent.workflowDocumentId());
//
//      WorkflowEngineIds workflowEngineIds =
//          workflowService.getWorkflowEngineIdsForWorkflow(
//              workflowDocumentEvent.tenantId(), workflowId);
//
//      WorkflowEngineConnector workflowEngineConnector =
//          workflowService.getWorkflowEngineConnector(workflowEngineIds.getEngineId());
//
//      workflowEngineConnector.processWorkflowDocumentEvent(
//          workflowDocumentEvent.tenantId(),
//          workflowId,
//          workflowEngineIds.getEngineInstanceId(),
//          workflowDocumentEvent);
//    } catch (Throwable e) {
//      log.error("Failed to process the workflow document event", e);
//    }
//  }
// }

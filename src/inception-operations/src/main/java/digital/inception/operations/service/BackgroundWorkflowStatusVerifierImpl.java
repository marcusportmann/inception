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

import digital.inception.operations.connector.WorkflowEngineConnector;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowStatus;
import jakarta.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The {@code BackgroundWorkflowStatusVerifierImpl} class implements the Background Workflow Status
 * Verifier.
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public class BackgroundWorkflowStatusVerifierImpl
    implements BackgroundWorkflowStatusVerifier, SmartLifecycle {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(BackgroundWorkflowStatusVerifierImpl.class);

  /** Is the Background Workflow Status Verifier executing? */
  private final AtomicBoolean executing = new AtomicBoolean(false);

  /** Is the Background Workflow Status Verifier running. */
  private final AtomicBoolean running = new AtomicBoolean(false);

  /** The Workflow Service. */
  private final WorkflowService workflowService;

  /**
   * Constructs a new {@code BackgroundWorkflowStatusVerifierImpl}.
   *
   * @param workflowService the Workflow Service
   */
  public BackgroundWorkflowStatusVerifierImpl(WorkflowService workflowService) {
    this.workflowService = workflowService;
  }

  /** Initialize the Background Workflow Status Verifier. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Background Workflow Status Verifier");
  }

  @Override
  public boolean isRunning() {
    return running.get();
  }

  @Override
  public void start() {
    if (running.compareAndSet(false, true)) {
      log.info("Background Workflow Status Verifier started");
    }
  }

  @Override
  public void stop() {
    if (running.compareAndSet(true, false)) {
      log.info("Shutting down the Background Workflow Status Verifier");
    }
  }

  /** Verify the workflow statuses. */
  @Scheduled(cron = "0 0 22 * * ?")
  public void verifyWorkflowStatuses() {
    if (!executing.compareAndSet(false, true)) {
      return;
    }

    try {
      for (String workflowEngineId : workflowService.getWorkflowEngineIds()) {

        WorkflowEngineConnector workflowEngineConnector =
            workflowService.getWorkflowEngineConnector(workflowEngineId);

        if (workflowEngineConnector.supportsWorkflowStatusRetrieval()) {

          for (UUID workflowId :
              workflowService.getActiveWorkflowIdsForWorkflowEngine(workflowEngineId)) {
            // Stop here if the Background Workflow Status Verifier is being shutdown
            if (!isRunning()) {
              return;
            }

            Workflow workflow = workflowService.getWorkflow(workflowId);

            WorkflowStatus workflowStatus =
                workflowEngineConnector.getWorkflowStatus(workflow.getTenantId(), workflow);

            if ((workflowStatus != WorkflowStatus.UNKNOWN)
                && (workflowStatus != workflow.getStatus())) {
              log.info(
                  "The workflow status ("
                      + workflowStatus
                      + ") returned by the workflow engine ("
                      + workflowEngineId
                      + ") for the workflow ("
                      + workflowId
                      + ") does not match the expected workflow status ("
                      + workflow.getStatus()
                      + ") and will be updated");

              if (workflowStatus == WorkflowStatus.ACTIVE) {
                // Do nothing
              } else if (workflowStatus == WorkflowStatus.COMPLETED) {
                workflowService.setWorkflowStatus(
                    workflow.getTenantId(), workflow.getId(), WorkflowStatus.COMPLETED);
              } else if (workflowStatus == WorkflowStatus.SUSPENDED) {
                workflowService.setWorkflowStatus(
                    workflow.getTenantId(), workflow.getId(), WorkflowStatus.SUSPENDED);
              } else if (workflowStatus == WorkflowStatus.TERMINATED) {
                workflowService.setWorkflowStatus(
                    workflow.getTenantId(), workflow.getId(), WorkflowStatus.TERMINATED);
              } else if (workflowStatus == WorkflowStatus.FAILED) {
                workflowService.setWorkflowStatus(
                    workflow.getTenantId(), workflow.getId(), WorkflowStatus.FAILED);
              }
            }
          }
        }
      }
    } catch (Throwable e) {
      log.error("Failed to verify the workflow statuses", e);
    } finally {
      executing.set(false);
    }
  }
}

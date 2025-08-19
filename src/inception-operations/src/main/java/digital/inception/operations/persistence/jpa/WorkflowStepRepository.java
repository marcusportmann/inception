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

package digital.inception.operations.persistence.jpa;

import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowStepId;
import digital.inception.operations.model.WorkflowStepStatus;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code WorkflowStepRepository} interface provides the persistence operations for the {@link
 * WorkflowStep} domain type.
 *
 * @author Marcus Portmann
 */
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, WorkflowStepId> {

  /**
   * Finalize a workflow step.
   *
   * @param workflowId the workflow ID
   * @param code the code for the workflow step
   * @param status the final status for the workflow step
   * @param finalized the date and time the workflow step was finalized
   * @return the number of rows that were updated (0 or 1)
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
      update WorkflowStep ws
         set ws.suspended   = null,
             ws.finalized   = :finalized,
             ws.status      = :status
       where ws.workflowId  = :workflowId
         and ws.code        = :code
      """)
  int finalizeWorkflowStep(
      @Param("workflowId") UUID workflowId,
      @Param("code") String code,
      @Param("status") WorkflowStepStatus status,
      @Param("finalized") OffsetDateTime finalized);

  /**
   * Retrieve the workflow steps with the specified status for the workflow with the specified ID.
   *
   * @param workflowId the ID for the workflow
   * @param status the status of the workflow steps
   * @return the workflow steps with the specified status for the workflow with the specified ID
   */
  List<WorkflowStep> findByWorkflowIdAndStatus(UUID workflowId, WorkflowStepStatus status);

  /**
   * Suspend a workflow step.
   *
   * @param workflowId the workflow ID
   * @param code the code for the workflow step
   * @param suspended the date and time the workflow step was suspended
   * @return the number of rows that were updated (0 or 1)
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
      update WorkflowStep ws
         set ws.finalized   = null,
             ws.suspended   = :suspended,
             ws.status      = digital.inception.operations.model.WorkflowStepStatus.SUSPENDED
       where ws.workflowId  = :workflowId
         and ws.code        = :code
      """)
  int suspendWorkflowStep(
      @Param("workflowId") UUID workflowId,
      @Param("code") String code,
      @Param("suspended") OffsetDateTime suspended);

  /**
   * Unsuspend a workflow step.
   *
   * @param workflowId the workflow ID
   * @param code the code for the workflow step
   * @return the number of rows that were updated (0 or 1)
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
      update WorkflowStep ws
         set ws.finalized   = null,
             ws.suspended   = null,
             ws.status      = digital.inception.operations.model.WorkflowStepStatus.ACTIVE
       where ws.workflowId  = :workflowId
         and ws.code        = :code
      """)
  int unsuspendWorkflowStep(@Param("workflowId") UUID workflowId, @Param("code") String code);
}

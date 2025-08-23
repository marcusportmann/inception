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

package digital.inception.executor.persistence.jpa;

import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskStatus;
import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code TaskRepository} interface declares the persistence for the {@code Task} domain type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface TaskRepository extends JpaRepository<Task, UUID> {

  /**
   * Unlock the multistep task and advance it to the specified step.
   *
   * @param taskId the ID for the task
   * @param step the code for the next task step for the multistep task
   * @param nextExecution the date and time the task will next be executed
   * @param executionTime the time taken to complete the current task step in milliseconds
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.QUEUED, "
          + "t.step = :step, t.executionAttempts = 0, t.nextExecution = :nextExecution, "
          + "t.executionTime = t.executionTime + :executionTime, "
          + "t.locked = null, t.lockName = null where t.id = :taskId")
  void advanceTaskToStep(
      @Param("taskId") UUID taskId,
      @Param("step") String step,
      @Param("nextExecution") OffsetDateTime nextExecution,
      @Param("executionTime") long executionTime);

  /**
   * Unlock the multistep task, advance it to the specified step, and update the task data.
   *
   * @param taskId the ID for the task
   * @param step the code for the next task step for the multistep task
   * @param data the updated task data
   * @param nextExecution the date and time the task will next be executed
   * @param executionTime the time taken to complete the current task step in milliseconds
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.QUEUED, "
          + "t.step = :step, t.data = :data, t.executionAttempts = 0, "
          + "t.executionTime = t.executionTime + :executionTime, "
          + "t.nextExecution = :nextExecution, t.locked = null, t.lockName = null "
          + "where t.id = :taskId")
  void advanceTaskToStep(
      @Param("taskId") UUID taskId,
      @Param("step") String step,
      @Param("data") String data,
      @Param("nextExecution") OffsetDateTime nextExecution,
      @Param("executionTime") long executionTime);

  /**
   * Cancel the batch.
   *
   * @param batchId the ID for the batch
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.CANCELED, "
          + "t.nextExecution = null, t.locked = null, t.lockName = null "
          + "where t.batchId = :batchId and "
          + "((t.status = digital.inception.executor.model.TaskStatus.CANCELED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.QUEUED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.SUSPENDED))")
  void cancelBatch(@Param("batchId") String batchId);

  /**
   * Cancel the task.
   *
   * @param taskId the ID for the task
   * @return the number of affected rows
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.CANCELED, "
          + "t.nextExecution = null, t.locked = null, t.lockName = null "
          + "where t.id = :taskId and "
          + "((t.status = digital.inception.executor.model.TaskStatus.CANCELED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.QUEUED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.SUSPENDED))")
  int cancelTask(@Param("taskId") UUID taskId);

  /**
   * Complete the task.
   *
   * @param taskId the ID for the task
   * @param currentTimestamp the current date and time
   * @param data the task data
   * @param executionTime the time taken to complete the last task step for a multistep task or the
   *     task for a single step task in milliseconds
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.COMPLETED, "
          + "t.executed =:currentTimestamp, t.data = :data, t.nextExecution = null, "
          + "t.executionTime = t.executionTime + :executionTime, "
          + "t.locked = null, t.lockName = null where t.id = :taskId")
  void completeTask(
      @Param("taskId") UUID taskId,
      @Param("currentTimestamp") OffsetDateTime currentTimestamp,
      @Param("data") String data,
      @Param("executionTime") long executionTime);

  /**
   * Complete the task.
   *
   * @param taskId the ID for the task
   * @param currentTimestamp the current date and time
   * @param executionTime the time taken to complete the last task step for a multistep task or the
   *     task for a single step task in milliseconds
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.COMPLETED, "
          + "t.executed =:currentTimestamp, t.nextExecution = null, "
          + "t.executionTime = t.executionTime + :executionTime, "
          + "t.locked = null, t.lockName = null "
          + "where t.id = :taskId")
  void completeTask(
      @Param("taskId") UUID taskId,
      @Param("currentTimestamp") OffsetDateTime currentTimestamp,
      @Param("executionTime") long executionTime);

  /**
   * Returns the number of tasks associated with the batch with the specified ID.
   *
   * @param batchId the ID for the batch
   * @return the number of tasks associated with the batch with the specified ID
   */
  int countByBatchId(String batchId);

  /**
   * Find the number of tasks with the specified task type that are currently queued or
   * executing.
   *
   * @param taskTypeCode the code for the task type
   * @return the number of tasks with the specified task type that are currently queued or executing
   */
  @Query(
      "SELECT count(t.id) FROM Task t WHERE t.type = :taskTypeCode and "
          + "((t.status = digital.inception.executor.model.TaskStatus.QUEUED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.EXECUTING))")
  int countTasksWithTaskTypeQueuedOrExecuting(@Param("taskTypeCode") String taskTypeCode);

  /**
   * Delay the task.
   *
   * @param taskId the ID for the task
   * @param nextExecution the date and time the task will be executed
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.QUEUED, "
          + "t.nextExecution = :nextExecution, t.locked = null, t.lockName = null "
          + "where t.id = :taskId")
  void delayTask(
      @Param("taskId") UUID taskId, @Param("nextExecution") OffsetDateTime nextExecution);

  /**
   * Delete the task with the specified ID if it exists.
   *
   * @param taskId the ID for the task
   */
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from Task t where t.id = :taskId")
  void deleteTaskIfExists(@Param("taskId") UUID taskId);

  /**
   * Fail the task.
   *
   * @param taskId the ID for the task
   * @param currentTimestamp the current date and time
   * @param failure the description of the failure for the task
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.FAILED, "
          + "t.executed =:currentTimestamp, t.nextExecution = null, t.locked = null, "
          + "t.failure = :failure, t.lockName = null "
          + "where t.id = :taskId")
  void failTask(
      @Param("taskId") UUID taskId,
      @Param("currentTimestamp") OffsetDateTime currentTimestamp,
      @Param("failure") String failure);

  /**
   * Find the task with the specified external reference.
   *
   * @param externalReference the external reference for the task
   * @return an Optional containing the task or an empty Optional if the task could not be found
   */
  Optional<Task> findByExternalReference(String externalReference);

  /**
   * Retrieves a task by its ID with a "SELECT FOR UPDATE" lock to prevent other transactions from
   * modifying or deleting the task until the current transaction completes.
   *
   * <p>This method is intended for use in scenarios where a pessimistic lock is required to ensure
   * data consistency during concurrent updates. The method applies a PESSIMISTIC_WRITE lock on the
   * selected row, effectively blocking other transactions from making changes to it until the
   * current transaction finishes.
   *
   * <p>Note: This method must be called within a transactional context, as the lock is held only
   * for the duration of the transaction.
   *
   * @param taskId the ID for the task
   * @return the task
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select t FROM Task t WHERE t.id = :taskId")
  Task findByIdForUpdate(@Param("taskId") UUID taskId);

  /**
   * Retrieve all tasks with the specified statuses.
   *
   * @param statuses the statuses
   * @param pageable the pagination information
   * @return all tasks with the specified statuses
   */
  @Query("SELECT t FROM Task t WHERE t.status IN :statuses")
  Page<Task> findByStatusIn(@Param("statuses") List<TaskStatus> statuses, Pageable pageable);

  /**
   * Find the tasks queued for execution.
   *
   * @param currentTimestamp the current date and time
   * @param pageable the pagination information
   * @return the tasks queued for execution
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select t from Task t where "
          + "exists (select 1 from TaskType tt where tt.code = t.type and tt.enabled = true) and "
          + "t.status = digital.inception.executor.model.TaskStatus.QUEUED and "
          + "((t.nextExecution is null) or (t.nextExecution <= :currentTimestamp)) "
          + "order by t.priority, t.queued")
  List<Task> findTasksQueuedForExecutionForWrite(
      @Param("currentTimestamp") OffsetDateTime currentTimestamp, Pageable pageable);

  /**
   * Find the tasks to archive and delete.
   *
   * @param executedBefore the date and time used to select the tasks to be archived or deleted
   * @param pageable the pagination information
   * @return the tasks to archive and delete
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "SELECT t FROM Task t WHERE (t.executed <= :executedBefore) and "
          + "((t.status = digital.inception.executor.model.TaskStatus.COMPLETED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.FAILED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.CANCELED))")
  Page<Task> findTasksToArchiveAndDelete(
      @Param("executedBefore") OffsetDateTime executedBefore, Pageable pageable);

  /**
   * Find the status of the task.
   *
   * @param taskId the ID for the task
   * @return an Optional containing the status of the task or an empty Optional if the task could
   *     not be found
   */
  @Query("SELECT t.status FROM Task t WHERE t.id = :taskId")
  Optional<TaskStatus> getTaskStatus(@Param("taskId") UUID taskId);

  /**
   * Lock the task for execution.
   *
   * @param taskId the ID for the task
   * @param lockName the name of the lock
   * @param currentTimestamp the current date and time
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.locked = :currentTimestamp, t.lockName = :lockName, "
          + "t.status = digital.inception.executor.model.TaskStatus.EXECUTING, "
          + "t.executionAttempts = t.executionAttempts + 1 "
          + "where t.id = :taskId")
  void lockTaskForExecution(
      @Param("taskId") UUID taskId,
      @Param("lockName") String lockName,
      @Param("currentTimestamp") OffsetDateTime currentTimestamp);

  /**
   * Requeue the task.
   *
   * @param taskId the ID for the task
   * @param nextExecution the date and time the task will be executed
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.QUEUED, "
          + "t.locked = null, t.lockName = null, t.nextExecution = :nextExecution "
          + "where t.id = :taskId")
  void requeueTask(
      @Param("taskId") UUID taskId, @Param("nextExecution") OffsetDateTime nextExecution);

  /**
   * Reset the hung tasks with the specified task type, which have been locked for execution before
   * the specified date and time.
   *
   * @param taskTypeCode the code for the task type for the hung tasks
   * @param lockedBefore the date and time a task must have been locked before to be considered hung
   * @return the number of hung tasks that were reset
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.QUEUED, "
          + "t.locked = null, t.lockName = null "
          + "where t.type = :taskTypeCode and "
          + "t.status = digital.inception.executor.model.TaskStatus.EXECUTING and "
          + "t.locked <= :lockedBefore")
  int resetHungTasks(
      @Param("taskTypeCode") String taskTypeCode,
      @Param("lockedBefore") OffsetDateTime lockedBefore);

  /**
   * Reset the hung tasks, which have been locked for execution before the specified date and time.
   *
   * @param lockedBefore the date and time a task must have been locked before to be considered hung
   * @return the number of hung tasks that were reset
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.QUEUED, "
          + "t.locked = null, t.lockName = null "
          + "where t.status = digital.inception.executor.model.TaskStatus.EXECUTING and "
          + "t.locked <= :lockedBefore")
  int resetHungTasks(@Param("lockedBefore") OffsetDateTime lockedBefore);

  /**
   * Reset the task locks with the specified status.
   *
   * @param status the status
   * @param newStatus the new status for the tasks
   * @param lockName the lock name
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = :newStatus, t.locked = null, t.lockName = null "
          + "where t.lockName = :lockName and t.status = :status")
  void resetTaskLocks(
      @Param("status") TaskStatus status,
      @Param("newStatus") TaskStatus newStatus,
      @Param("lockName") String lockName);

  /**
   * Set the task status.
   *
   * @param taskId the ID for the task
   * @param status the status for the task
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Task t set t.status = :status where t.id = :taskId")
  void setTaskStatus(@Param("taskId") UUID taskId, @Param("status") TaskStatus status);

  /**
   * Suspend the batch.
   *
   * @param batchId the ID for the batch
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.SUSPENDED, "
          + "t.nextExecution = null, t.locked = null, t.lockName = null "
          + "where t.batchId = :batchId and "
          + "((t.status = digital.inception.executor.model.TaskStatus.QUEUED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.SUSPENDED))")
  void suspendBatch(@Param("batchId") String batchId);

  /**
   * Suspend the task.
   *
   * @param taskId the ID for the task
   * @return the number of affected rows
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.SUSPENDED, "
          + "t.nextExecution = null, t.locked = null, t.lockName = null "
          + "where t.id = :taskId and "
          + "((t.status = digital.inception.executor.model.TaskStatus.QUEUED) or "
          + "(t.status = digital.inception.executor.model.TaskStatus.SUSPENDED))")
  int suspendTask(@Param("taskId") UUID taskId);

  /**
   * Unlock the task.
   *
   * @param taskId the ID for the task
   * @param status the status for the task
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = :status, t.locked = null, t.lockName = null "
          + "where t.id = :taskId")
  void unlockTask(@Param("taskId") UUID taskId, @Param("status") TaskStatus status);

  /**
   * Unlock the task and update the task data.
   *
   * @param taskId the ID for the task
   * @param status the status for the task
   * @param data the task data
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = :status, t.data = :data, t.locked = null, t.lockName = null "
          + "where t.id = :taskId")
  void unlockTask(
      @Param("taskId") UUID taskId, @Param("status") TaskStatus status, @Param("data") String data);

  /**
   * Unsuspend the batch.
   *
   * @param batchId the ID for the batch
   * @param currentTimestamp the current date and time
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.QUEUED, "
          + "t.nextExecution = :currentTimestamp, t.locked = null, t.lockName = null "
          + "where t.batchId = :batchId and "
          + "t.status = digital.inception.executor.model.TaskStatus.SUSPENDED")
  void unsuspendBatch(
      @Param("batchId") String batchId, @Param("currentTimestamp") OffsetDateTime currentTimestamp);

  /**
   * Unsuspend the task.
   *
   * @param taskId the ID for the task
   * @param currentTimestamp the current date and time
   * @return the number of affected rows
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Task t set t.status = digital.inception.executor.model.TaskStatus.QUEUED, "
          + "t.nextExecution = :currentTimestamp, t.locked = null, t.lockName = null "
          + "where t.id = :taskId and "
          + "t.status = digital.inception.executor.model.TaskStatus.SUSPENDED")
  int unsuspendTask(
      @Param("taskId") UUID taskId, @Param("currentTimestamp") OffsetDateTime currentTimestamp);
}

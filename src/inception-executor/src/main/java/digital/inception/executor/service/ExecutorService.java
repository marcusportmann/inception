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

package digital.inception.executor.service;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.executor.model.ArchivedTask;
import digital.inception.executor.model.ArchivedTaskNotFoundException;
import digital.inception.executor.model.BatchTasksNotFoundException;
import digital.inception.executor.model.DuplicateTaskTypeException;
import digital.inception.executor.model.InvalidTaskStatusException;
import digital.inception.executor.model.QueueTaskRequest;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskEvent;
import digital.inception.executor.model.TaskExecutionDelayedException;
import digital.inception.executor.model.TaskExecutionFailedException;
import digital.inception.executor.model.TaskExecutionResult;
import digital.inception.executor.model.TaskExecutionRetryableException;
import digital.inception.executor.model.TaskNotFoundException;
import digital.inception.executor.model.TaskSortBy;
import digital.inception.executor.model.TaskStatus;
import digital.inception.executor.model.TaskSummaries;
import digital.inception.executor.model.TaskType;
import digital.inception.executor.model.TaskTypeNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code ExecutorService} interface defines the functionality provided by an Executor Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface ExecutorService {

  /**
   * Archive and delete the historical tasks.
   *
   * @throws ServiceUnavailableException if the historical tasks could not be archived and deleted
   */
  void archiveAndDeleteHistoricalTasks() throws ServiceUnavailableException;

  /**
   * Cancel the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be cancelled
   */
  void cancelBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException;

  /**
   * Cancel the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be cancelled
   */
  void cancelTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException;

  /**
   * Complete the task.
   *
   * <p>NOTE: The implementation of this method is explicitly not annotated with the @Transactional
   * annotation to prevent a race condition between committing the transaction to persist a task in
   * the database and triggering the asynchronous processing of the task in a separate thread. If
   * the transaction is not committed, the task will not be retrieved by the
   * getNextTaskQueuedForExecution method invoked by the BackgroundTaskExecutor.
   *
   * @param task the task
   * @param taskExecutionResult the result of executing the task, including the next step for a
   *     multistep task and whether the task data should be updated
   * @param executionTime the time taken to complete the current task step for a multistep task or
   *     the task for a single step task in milliseconds
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the task could not be completed
   */
  void completeTask(Task task, TaskExecutionResult taskExecutionResult, long executionTime)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Create the task type.
   *
   * @param taskType the {@code TaskType} instance containing the information for the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateTaskTypeException if the task type already exists
   * @throws ServiceUnavailableException if the task type could not be created
   */
  void createTaskType(TaskType taskType)
      throws InvalidArgumentException, DuplicateTaskTypeException, ServiceUnavailableException;

  /**
   * Delay the task.
   *
   * @param task the task
   * @param delay the delay for the task in milliseconds
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the task could not be delayed
   */
  void delayTask(Task task, long delay)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Delete the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be deleted
   */
  void deleteTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Delete the task type.
   *
   * @param taskTypeCode the code for the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be deleted
   */
  void deleteTaskType(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Execute the task.
   *
   * @param task the task
   * @return the result of executing the task, including the next step for a multistep task and
   *     whether the task data should be updated
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskExecutionFailedException if the task execution failed and cannot be retried
   * @throws TaskExecutionRetryableException if the task execution failed because of temporary error
   *     and can be retried
   * @throws TaskExecutionDelayedException if the task execution should be delayed
   */
  TaskExecutionResult executeTask(Task task)
      throws InvalidArgumentException,
          TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException;

  /**
   * Fail the task.
   *
   * @param task the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the task could not be failed
   */
  void failTask(Task task) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the archived task.
   *
   * @param archivedTaskId the ID for the archived task
   * @return the archived task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ArchivedTaskNotFoundException if the archived task could not be found
   * @throws ServiceUnavailableException if the archived task could not be retrieved
   */
  ArchivedTask getArchivedTask(UUID archivedTaskId)
      throws InvalidArgumentException, ArchivedTaskNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the next task that is queued for execution.
   *
   * <p>The task will be locked to prevent duplicate processing.
   *
   * @return an Optional containing the next task that is queued for execution or an empty Optional
   *     if no tasks are currently queued for execution
   * @throws ServiceUnavailableException if the next task queued for execution could not be
   *     retrieved
   */
  Optional<Task> getNextTaskQueuedForExecution() throws ServiceUnavailableException;

  /**
   * Retrieve the task.
   *
   * @param taskId the ID for the task
   * @return the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be retrieved
   */
  Task getTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the task with the specified external reference.
   *
   * @param externalReference the external reference for the task
   * @return the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be retrieved
   */
  Task getTaskByExternalReference(String externalReference)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the task events for the task.
   *
   * @param taskId the ID for the task
   * @return the task events for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task events for the task could not be retrieved
   */
  List<TaskEvent> getTaskEventsForTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the status of the task.
   *
   * @param taskId the ID for the task
   * @return the status of the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the status of the task could not be retrieved
   */
  TaskStatus getTaskStatus(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the tasks.
   *
   * @param type the task type code filter to apply to the task summaries
   * @param status the status filter to apply to the task summaries
   * @param filter the filter to apply to the task summaries
   * @param sortBy the method used to sort the task summaries e.g. by type
   * @param sortDirection the sort direction to apply to the task summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the tasks
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the task summaries could not be retrieved
   */
  TaskSummaries getTaskSummaries(
      String type,
      TaskStatus status,
      String filter,
      TaskSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the task type.
   *
   * @param taskTypeCode the code for the task type
   * @return the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be retrieved
   */
  TaskType getTaskType(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the task type.
   *
   * @param taskTypeCode the code for the task type
   * @return the name of the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the name of the task type could not be retrieved
   */
  String getTaskTypeName(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve all the task types.
   *
   * @return the task types
   * @throws ServiceUnavailableException if the task types could not be retrieved
   */
  List<TaskType> getTaskTypes() throws ServiceUnavailableException;

  /**
   * Returns whether a task with the specified task type is currently queued or executing.
   *
   * @param taskTypeCode the code for the task type
   * @return {@code true} if a task with the specified task type is currently queued or executing
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if existence of a task with the specified task type that is
   *     currently queued or executing could not be verified
   */
  boolean isTaskWithTaskTypeQueuedOrExecuting(String taskTypeCode)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Queue a task for execution.
   *
   * <p>NOTE: The implementation of this method is explicitly not annotated with the @Transactional
   * annotation to prevent a race condition between committing the transaction to persist a task in
   * the database and triggering the asynchronous processing of the task in a separate thread. If
   * the transaction is not committed, the task will not be retrieved by the
   * getNextTaskQueuedForExecution method invoked by the BackgroundTaskExecutor.
   *
   * @param queueTaskRequest the request to queue a task for execution
   * @return the ID for the task that has been queued for execution
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task could not be queued for execution
   */
  UUID queueTask(QueueTaskRequest queueTaskRequest)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Queue a task for execution.
   *
   * <p>NOTE: The implementation of this method is explicitly not annotated with the @Transactional
   * annotation to prevent a race condition between committing the transaction to persist a task in
   * the database and triggering the asynchronous processing of the task in a separate thread. If
   * the transaction is not committed, the task will not be retrieved by the
   * getNextTaskQueuedForExecution method invoked by the BackgroundTaskExecutor.
   *
   * @param type the code for the task type
   * @param batchId the ID for the task batch
   * @param externalReference the external reference for the task
   * @param suspended the flag indicating that the task must be suspended
   * @param dataObject the task data object that will be serialized to JSON
   * @return the ID for the task that has been queued for execution
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task could not be queued for execution
   */
  UUID queueTask(
      String type, String batchId, String externalReference, boolean suspended, Object dataObject)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Queue a task for execution.
   *
   * <p>NOTE: The implementation of this method is explicitly not annotated with the @Transactional
   * annotation to prevent a race condition between committing the transaction to persist a task in
   * the database and triggering the asynchronous processing of the task in a separate thread. If
   * the transaction is not committed, the task will not be retrieved by the
   * getNextTaskQueuedForExecution method invoked by the BackgroundTaskExecutor.
   *
   * @param type the code for the task type
   * @param batchId the ID for the task batch
   * @param externalReference the external reference for the task
   * @param dataObject the task data object that will be serialized to JSON
   * @return the ID for the task that has been queued for execution
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task could not be queued for execution
   */
  UUID queueTask(String type, String batchId, String externalReference, Object dataObject)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Queue a task for execution.
   *
   * <p>NOTE: The implementation of this method is explicitly not annotated with the @Transactional
   * annotation to prevent a race condition between committing the transaction to persist a task in
   * the database and triggering the asynchronous processing of the task in a separate thread. If
   * the transaction is not committed, the task will not be retrieved by the
   * getNextTaskQueuedForExecution method invoked by the BackgroundTaskExecutor.
   *
   * @param type the code for the task type
   * @param batchId the ID for the task batch
   * @param dataObject the task data object that will be serialized to JSON
   * @return the ID for the task that has been queued for execution
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task could not be queued for execution
   */
  UUID queueTask(String type, String batchId, Object dataObject)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Queue a task for execution.
   *
   * <p>NOTE: The implementation of this method is explicitly not annotated with the @Transactional
   * annotation to prevent a race condition between committing the transaction to persist a task in
   * the database and triggering the asynchronous processing of the task in a separate thread. If
   * the transaction is not committed, the task will not be retrieved by the
   * getNextTaskQueuedForExecution method invoked by the BackgroundTaskExecutor.
   *
   * @param type the code for the task type
   * @param dataObject the task data object that will be serialized to JSON
   * @return the ID for the task that has been queued for execution
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task could not be queued for execution
   */
  UUID queueTask(String type, Object dataObject)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;

  /**
   * Requeue the task for execution.
   *
   * <p>NOTE: The implementation of this method is explicitly not annotated with the @Transactional
   * annotation to prevent a race condition between committing the transaction to persist a task in
   * the database and triggering the asynchronous processing of the task in a separate thread. If
   * the transaction is not committed, the task will not be retrieved by the
   * getNextTaskQueuedForExecution method invoked by the BackgroundTaskExecutor.
   *
   * @param task the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be re-queued for execution
   */
  void requeueTask(Task task)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Reset "hung" tasks, which have been locked and executing longer than a global or
   * task-type-specific timeout.
   *
   * @throws ServiceUnavailableException if the hung tasks could not be reset
   */
  void resetHungTasks() throws ServiceUnavailableException;

  /**
   * Reset the task locks.
   *
   * @param status the current status of the tasks that have been locked
   * @param newStatus the new status for the tasks that have been unlocked
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the task locks could not be reset
   */
  void resetTaskLocks(TaskStatus status, TaskStatus newStatus)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Set the number of days historical tasks will be retained before they are archived or deleted.
   *
   * @param historicalTaskRetentionDays the number of days historical tasks will be retained before
   *     they are archived or deleted
   */
  void setHistoricalTaskRetentionDays(int historicalTaskRetentionDays);

  /**
   * Suspend the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be suspended
   */
  void suspendBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException;

  /**
   * Suspend the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be suspended
   */
  void suspendTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException;

  /**
   * Returns whether the task type exists.
   *
   * @param taskTypeCode the code for the task type
   * @return {@code true} if the task type exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if existence of the task type could not be verified
   */
  boolean taskTypeExists(String taskTypeCode)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Unlock a locked task.
   *
   * @param taskId the ID for the task
   * @param status the new status for the unlocked task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be unlocked
   */
  void unlockTask(UUID taskId, TaskStatus status)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException;

  /**
   * Unsuspend the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be unsuspended
   */
  void unsuspendBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException;

  /**
   * Unsuspend the task.
   *
   * <p>NOTE: The implementation of this method is explicitly not annotated with the @Transactional
   * annotation to prevent a race condition between committing the transaction to persist a task in
   * the database and triggering the asynchronous processing of the task in a separate thread. If
   * the transaction is not committed, the task will not be retrieved by the
   * getNextTaskQueuedForExecution method invoked by the BackgroundTaskExecutor.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be unsuspended
   */
  void unsuspendTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException;

  /**
   * Update the task type.
   *
   * @param taskType the {@code TaskType} instance containing the updated information for the task
   *     type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be updated
   */
  void updateTaskType(TaskType taskType)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException;
}

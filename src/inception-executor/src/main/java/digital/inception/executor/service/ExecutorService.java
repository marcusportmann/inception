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

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.ValidationError;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.ServiceUtil;
import digital.inception.executor.model.ArchivedTask;
import digital.inception.executor.model.ArchivedTaskNotFoundException;
import digital.inception.executor.model.BatchTasksNotFoundException;
import digital.inception.executor.model.DuplicateTaskTypeException;
import digital.inception.executor.model.ITaskExecutor;
import digital.inception.executor.model.InvalidTaskStatusException;
import digital.inception.executor.model.QueueTaskRequest;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskEvent;
import digital.inception.executor.model.TaskEventType;
import digital.inception.executor.model.TaskExecutionDelayedException;
import digital.inception.executor.model.TaskExecutionFailedException;
import digital.inception.executor.model.TaskExecutionResult;
import digital.inception.executor.model.TaskExecutionRetryableException;
import digital.inception.executor.model.TaskNotFoundException;
import digital.inception.executor.model.TaskSortBy;
import digital.inception.executor.model.TaskStatus;
import digital.inception.executor.model.TaskSummaries;
import digital.inception.executor.model.TaskSummary;
import digital.inception.executor.model.TaskType;
import digital.inception.executor.model.TaskTypeNotFoundException;
import digital.inception.executor.persistence.ArchivedTaskRepository;
import digital.inception.executor.persistence.TaskEventRepository;
import digital.inception.executor.persistence.TaskRepository;
import digital.inception.executor.persistence.TaskSummaryRepository;
import digital.inception.executor.persistence.TaskTypeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The <b>ExecutorService</b> class provides the Executor Service implementation.
 *
 * <p>NOTE: The completeTask method, queueTask methods, requeueTask method, and unsuspendTask method
 * are explicitly not annotated with the @Transactional annotation to prevent a race condition
 * between committing the transaction to persist a task in the database and triggering the
 * asynchronous processing of the task in a separate thread. If the transaction is not committed,
 * the task will not be retrieved by the getNextTaskQueuedForExecution method invoked by the
 * BackgroundTaskExecutor.
 *
 * @author Marcus Portmann
 */
@Slf4j
@Service
@SuppressWarnings({"unused"})
public class ExecutorService implements IExecutorService {

  /** The maximum number of filtered tasks. */
  private static final int MAX_FILTERED_TASKS = 100;

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The Archived Task Repository. */
  private final ArchivedTaskRepository archivedTaskRepository;

  /* The name of the Executor Service instance. */
  private final String instanceName = ServiceUtil.getServiceInstanceName("ExecutorService");

  /** The Task Event Repository. */
  private final TaskEventRepository taskEventRepository;

  private final ConcurrentHashMap<String, ITaskExecutor> taskExecutors = new ConcurrentHashMap<>();

  /** The Task Repository. */
  private final TaskRepository taskRepository;

  /** The Task Summary Repository. */
  private final TaskSummaryRepository taskSummaryRepository;

  /** The Task Type Repository. */
  private final TaskTypeRepository taskTypeRepository;

  private final ReadWriteLock taskTypesLock = new ReentrantReadWriteLock();

  /** The JSR-380 validator. */
  private final Validator validator;

  /* Entity Manager */
  @PersistenceContext(unitName = "executor")
  private EntityManager entityManager;

  /*
   * The number of days historical tasks will be retained before they are archived or deleted.
   */
  @Value("${inception.executor.historical-task-retention-days:60}")
  private int historicalTaskRetentionDays;

  /** Is debugging enabled for the Inception Framework? */
  @Value("${inception.debug.enabled:#{false}}")
  private boolean inDebugMode;

  /*
   * The maximum number of times the execution of a task will be attempted.
   */
  @Value("${inception.executor.maximum-task-execution-attempts:144}")
  private int maximumTaskExecutionAttempts;

  /*
   * The delay in milliseconds between successive attempts to execute a task.
   */
  @Value("${inception.executor.task-execution-retry-delay:60000}")
  private long taskExecutionRetryDelay;

  /*
   * The amount of time in milliseconds after which a locked and executing task will be considered
   * "hung" and will be reset.
   */
  @Value("${inception.executor.task-execution-timeout:43200000}")
  private long taskExecutionTimeout;

  private volatile ConcurrentHashMap<String, TaskType> taskTypes;

  /**
   * Constructs a new <b>ExecutorService</b>.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-380 validator
   * @param archivedTaskRepository the Archived Task Repository
   * @param taskEventRepository the Task Event Repository
   * @param taskRepository the Task Repository
   * @param taskSummaryRepository the Task Summary Repository
   * @param taskTypeRepository the Task Type Repository
   */
  public ExecutorService(
      ApplicationContext applicationContext,
      Validator validator,
      ArchivedTaskRepository archivedTaskRepository,
      TaskEventRepository taskEventRepository,
      TaskRepository taskRepository,
      TaskSummaryRepository taskSummaryRepository,
      TaskTypeRepository taskTypeRepository) {
    this.applicationContext = applicationContext;
    this.validator = validator;
    this.archivedTaskRepository = archivedTaskRepository;
    this.taskEventRepository = taskEventRepository;
    this.taskRepository = taskRepository;
    this.taskSummaryRepository = taskSummaryRepository;
    this.taskTypeRepository = taskTypeRepository;
  }

  @Override
  public void archiveAndDeleteHistoricalTasks() throws ServiceUnavailableException {
    try {
      OffsetDateTime executedBefore = OffsetDateTime.now().minusDays(historicalTaskRetentionDays);

      // Keep looping while we still have historical tasks to archive or delete
      while (true) {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "queued");

        Page<Task> tasks = taskRepository.findTasksToArchiveAndDelete(executedBefore, pageable);

        // We have no more tasks to process
        if (tasks.isEmpty()) {
          return;
        }

        tasks.forEach(
            (task -> {
              TaskType taskType;
              try {
                taskType = getTaskType(task.getType());
              } catch (Throwable e) {
                log.error(
                    "Failed to retrieve the task type ("
                        + task.getType()
                        + ") for the task ("
                        + task.getId()
                        + "). The task will be DELETED");
                taskType = null;
              }

              if (taskType != null) {
                if (((task.getStatus() == TaskStatus.COMPLETED) && (taskType.getArchiveCompleted()))
                    || ((task.getStatus() == TaskStatus.FAILED) && (taskType.getArchiveFailed()))) {
                  if (log.isDebugEnabled()) {
                    log.debug(
                        "Archiving the task ("
                            + task.getId()
                            + ") with type ("
                            + task.getType()
                            + ") and status ("
                            + task.getStatus()
                            + ")");
                  }

                  archiveTask(task);
                }

                if (log.isDebugEnabled()) {
                  log.debug(
                      "Deleting the task ("
                          + task.getId()
                          + ") with type ("
                          + task.getType()
                          + ") and status ("
                          + task.getStatus()
                          + ")");
                }

                taskRepository.deleteById(task.getId());
              } else {
                if (log.isDebugEnabled()) {
                  log.debug(
                      "Deleting the task ("
                          + task.getId()
                          + ") with type ("
                          + task.getType()
                          + ") and status ("
                          + task.getStatus()
                          + ")");
                }

                taskRepository.deleteById(task.getId());
              }
            }));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to archive and delete the historical tasks", e);
    }
  }

  @Override
  public void cancelBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(batchId)) {
      throw new InvalidArgumentException("batchId");
    }

    try {
      if (taskRepository.countByBatchId(batchId) == 0) {
        throw new BatchTasksNotFoundException(batchId);
      }

      taskRepository.cancelBatch(batchId);
    } catch (BatchTasksNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to cancel the batch (" + batchId + ")", e);
    }
  }

  @Override
  public void cancelTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    if (taskId == null) {
      throw new InvalidArgumentException("taskId");
    }

    try {
      if (!taskRepository.existsById(taskId)) {
        throw new TaskNotFoundException(taskId);
      }

      if (taskRepository.cancelTask(taskId) == 0) {
        throw new InvalidTaskStatusException(taskId);
      }
    } catch (TaskNotFoundException | InvalidTaskStatusException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to cancel the task (" + taskId + ")", e);
    }
  }

  @Override
  public void completeTask(Task task, TaskExecutionResult taskExecutionResult, long executionTime)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (task == null) {
      throw new InvalidArgumentException("task");
    }

    if (taskExecutionResult == null) {
      throw new InvalidArgumentException("taskExecutionResult");
    }

    try {
      TaskType taskType = getTaskType(task.getType());

      // If we need to move to the next task step for a multistep task
      if (taskExecutionResult.getNextTaskStep() != null) {
        OffsetDateTime nextExecution =
            (taskExecutionResult.getNextTaskStepDelay() != null)
                ? OffsetDateTime.now()
                    .plus(taskExecutionResult.getNextTaskStepDelay(), ChronoUnit.MILLIS)
                : OffsetDateTime.now();

        // If we have to update the task data after executing the previous task step
        if (StringUtils.hasText(taskExecutionResult.getUpdatedTaskData())) {
          taskRepository.advanceTaskToStep(
              task.getId(),
              taskExecutionResult.getNextTaskStep().getCode(),
              taskExecutionResult.getUpdatedTaskData(),
              nextExecution,
              executionTime);

          task.setData(taskExecutionResult.getUpdatedTaskData());
        } else {
          taskRepository.advanceTaskToStep(
              task.getId(),
              taskExecutionResult.getNextTaskStep().getCode(),
              nextExecution,
              executionTime);
        }

        createTaskEvent(TaskEventType.STEP_COMPLETED, taskType, task);

        applicationContext.getBean(BackgroundTaskExecutor.class).executeTasks();
      }
      // Complete the single step task, or the last step of a multistep task
      else {
        if (StringUtils.hasText(taskExecutionResult.getUpdatedTaskData())) {
          taskRepository.completeTask(
              task.getId(),
              OffsetDateTime.now(),
              taskExecutionResult.getUpdatedTaskData(),
              executionTime);

          task.setData(taskExecutionResult.getUpdatedTaskData());
        } else {
          taskRepository.completeTask(task.getId(), OffsetDateTime.now(), executionTime);
        }

        createTaskEvent(TaskEventType.TASK_COMPLETED, taskType, task);
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to complete the task", e);
    }
  }

  @Override
  public void createTaskType(TaskType taskType)
      throws InvalidArgumentException, DuplicateTaskTypeException, ServiceUnavailableException {
    validateTaskType(taskType);

    try {
      taskTypesLock.writeLock().lock();

      try {
        if (taskTypeRepository.existsById(taskType.getCode())) {
          throw new DuplicateTaskTypeException(taskType.getCode());
        }

        taskTypeRepository.saveAndFlush(taskType);

        taskTypes = null;
      } catch (DuplicateTaskTypeException e) {
        throw e;
      } catch (Throwable e) {
        throw new ServiceUnavailableException(
            "Failed to create the task type (" + taskType.getCode() + ")", e);
      }
    } finally {
      taskTypesLock.writeLock().unlock();
    }
  }

  @Override
  public void delayTask(Task task, long delay)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (task == null) {
      throw new InvalidArgumentException("task");
    }

    try {
      taskRepository.delayTask(task.getId(), OffsetDateTime.now().plus(delay, ChronoUnit.MILLIS));
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delay the task", e);
    }
  }

  @Override
  public void deleteTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    if (taskId == null) {
      throw new InvalidArgumentException("taskId");
    }

    try {
      if (!taskRepository.existsById(taskId)) {
        throw new TaskNotFoundException(taskId);
      }

      taskRepository.deleteById(taskId);
    } catch (TaskNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delete the task (" + taskId + ")", e);
    }
  }

  @Override
  public void deleteTaskType(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(taskTypeCode)) {
      throw new InvalidArgumentException("taskTypeCode");
    }

    try {
      taskTypesLock.writeLock().lock();

      try {
        if (!taskTypeRepository.existsById(taskTypeCode)) {
          throw new TaskTypeNotFoundException(taskTypeCode);
        }

        taskTypeRepository.deleteById(taskTypeCode);

        taskTypes = null;
      } catch (TaskTypeNotFoundException e) {
        throw e;
      } catch (Throwable e) {
        throw new ServiceUnavailableException(
            "Failed to delete the task type (" + taskTypeCode + ")", e);
      }
    } finally {
      taskTypesLock.writeLock().unlock();
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public TaskExecutionResult executeTask(Task task)
      throws InvalidArgumentException,
          TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException {
    validateTask(task);

    try {
      TaskType taskType = getTaskType(task.getType());

      ITaskExecutor taskExecutor = getTaskExecutorForTaskType(taskType);

      return taskExecutor.executeTask(task);
    } catch (TaskExecutionFailedException e) {
      if (e.getOriginalMessage() != null) {
        task.setFailure(e.getOriginalMessage());
      }
      throw e;
    } catch (TaskExecutionRetryableException | TaskExecutionDelayedException e) {
      throw e;
    } catch (Throwable e) {
      throw new TaskExecutionFailedException(task.getId(), e);
    }
  }

  @Override
  public void failTask(Task task) throws InvalidArgumentException, ServiceUnavailableException {
    if (task == null) {
      throw new InvalidArgumentException("task");
    }

    try {
      TaskType taskType = getTaskType(task.getType());

      taskRepository.failTask(task.getId(), OffsetDateTime.now(), task.getFailure());

      createTaskEvent(TaskEventType.TASK_FAILED, taskType, task);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to fail the task", e);
    }
  }

  @Override
  public ArchivedTask getArchivedTask(UUID archivedTaskId)
      throws InvalidArgumentException, ArchivedTaskNotFoundException, ServiceUnavailableException {
    if (archivedTaskId == null) {
      throw new InvalidArgumentException("archivedTaskId");
    }

    try {
      Optional<ArchivedTask> archivedTaskOptional = archivedTaskRepository.findById(archivedTaskId);

      if (archivedTaskOptional.isPresent()) {
        return archivedTaskOptional.get();
      } else {
        throw new ArchivedTaskNotFoundException(archivedTaskId);
      }
    } catch (ArchivedTaskNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the archived task (" + archivedTaskId + ")", e);
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Optional<Task> getNextTaskQueuedForExecution() throws ServiceUnavailableException {
    try {
      // Handle the situation where different time precisions are used in the database
      OffsetDateTime now = OffsetDateTime.now().plusSeconds(1);

      PageRequest pageRequest = PageRequest.of(0, 1);

      List<Task> tasks = taskRepository.findTasksQueuedForExecutionForWrite(now, pageRequest);

      if (!tasks.isEmpty()) {
        Task task = tasks.getFirst();

        OffsetDateTime locked = OffsetDateTime.now();

        taskRepository.lockTaskForExecution(task.getId(), instanceName, locked);

        entityManager.detach(task);

        task.setStatus(TaskStatus.EXECUTING);
        task.setLocked(locked);
        task.setLockName(instanceName);
        task.incrementExecutionAttempts();

        return Optional.of(task);
      } else {
        return Optional.empty();
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the next task that has been queued for execution", e);
    }
  }

  @Override
  public Task getTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    if (taskId == null) {
      throw new InvalidArgumentException("taskId");
    }

    try {
      Optional<Task> taskOptional = taskRepository.findById(taskId);

      if (taskOptional.isPresent()) {
        return taskOptional.get();
      } else {
        throw new TaskNotFoundException(taskId);
      }
    } catch (TaskNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the task (" + taskId + ")", e);
    }
  }

  @Override
  public Task getTaskByExternalReference(String externalReference)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    if (externalReference == null) {
      throw new InvalidArgumentException("externalReference");
    }

    try {
      Optional<Task> taskOptional = taskRepository.findByExternalReference(externalReference);

      if (taskOptional.isPresent()) {
        return taskOptional.get();
      } else {
        throw new TaskNotFoundException(externalReference);
      }
    } catch (TaskNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the task with the external reference (" + externalReference + ")", e);
    }
  }

  @Override
  public List<TaskEvent> getTaskEventsForTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    if (taskId == null) {
      throw new InvalidArgumentException("taskId");
    }

    try {
      if (!taskRepository.existsById(taskId)) {
        throw new TaskNotFoundException(taskId);
      }

      return taskEventRepository.findByTaskIdOrderByTimestampAsc(taskId);
    } catch (TaskNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the task events for the task (" + taskId + ")", e);
    }
  }

  @Override
  public TaskStatus getTaskStatus(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    if (taskId == null) {
      throw new InvalidArgumentException("taskId");
    }

    try {
      Optional<TaskStatus> taskStatusOptional = taskRepository.getTaskStatus(taskId);

      if (taskStatusOptional.isPresent()) {
        return taskStatusOptional.get();
      } else {
        throw new TaskNotFoundException(taskId);
      }
    } catch (TaskNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the status of the task (" + taskId + ")", e);
    }
  }

  @Override
  public TaskSummaries getTaskSummaries(
      String type,
      TaskStatus status,
      String filter,
      TaskSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = TaskSortBy.QUEUED;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.DESCENDING;
    }

    try {
      PageRequest pageRequest;

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = MAX_FILTERED_TASKS;
      }

      String sortProperty;

      if (sortBy == TaskSortBy.TYPE) {
        sortProperty = "type";
      } else {
        sortProperty = "queued";
      }

      pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_TASKS),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              sortProperty);

      Page<TaskSummary> taskSummaryPage =
          taskSummaryRepository.findAll(
              (Specification<TaskSummary>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    if (StringUtils.hasText(type)) {
                      predicates.add(criteriaBuilder.equal(root.get("type"), type));
                    }

                    if (status != null) {
                      predicates.add(criteriaBuilder.equal(root.get("status"), status));
                    }

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("batchId")),
                                  "%" + filter.toLowerCase() + "%"),
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("externalReference")),
                                  "%" + filter.toLowerCase() + "%")));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new TaskSummaries(
          taskSummaryPage.toList(),
          taskSummaryPage.getTotalElements(),
          type,
          status,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered task summaries", e);
    }
  }

  @Override
  public TaskType getTaskType(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(taskTypeCode)) {
      throw new InvalidArgumentException("taskTypeCode");
    }

    try {
      taskTypesLock.readLock().lock();

      try {
        // Synchronized the initialisation of the task type cache
        if (taskTypes == null) {
          synchronized (this) {
            if (taskTypes == null) {
              List<TaskType> taskTypes = getTaskTypes();

              this.taskTypes = new ConcurrentHashMap<>();

              for (TaskType taskType : taskTypes) {
                this.taskTypes.put(taskType.getCode(), taskType);
              }
            }
          }
        }

        TaskType taskType = taskTypes.get(taskTypeCode);

        if (taskType != null) {
          return taskType;
        } else {
          throw new TaskTypeNotFoundException(taskTypeCode);
        }
      } catch (TaskTypeNotFoundException e) {
        throw e;
      } catch (Throwable e) {
        throw new ServiceUnavailableException(
            "Failed to retrieve the task type (" + taskTypeCode + ")", e);
      }
    } finally {
      taskTypesLock.readLock().unlock();
    }
  }

  @Override
  public String getTaskTypeName(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    try {
      return getTaskType(taskTypeCode).getName();
    } catch (InvalidArgumentException | TaskTypeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the name of the task type (" + taskTypeCode + ")", e);
    }
  }

  @Override
  public List<TaskType> getTaskTypes() throws ServiceUnavailableException {
    try {
      return taskTypeRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the task types", e);
    }
  }

  /** Initialize the Executor Service. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Executor Service (" + instanceName + ")");
  }

  @Override
  public boolean isTaskWithTaskTypeQueuedOrExecuting(String taskTypeCode)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(taskTypeCode)) {
      throw new InvalidArgumentException("taskTypeCode");
    }

    try {
      return taskRepository.countTasksWithTaskTypeQueuedOrExecuting(taskTypeCode) > 0;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether a task with the task type ("
              + taskTypeCode
              + ") is queued or executing",
          e);
    }
  }

  @Override
  public UUID queueTask(
      String type, String batchId, String externalReference, boolean suspended, Object dataObject)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(type)) {
      throw new InvalidArgumentException("type");
    }

    if (dataObject == null) {
      throw new InvalidArgumentException("dataObject");
    }

    try {
      ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);

      String taskData = objectMapper.writeValueAsString(dataObject);

      QueueTaskRequest queueTaskRequest =
          new QueueTaskRequest(type, batchId, externalReference, taskData, suspended);

      return queueTask(queueTaskRequest);
    } catch (InvalidArgumentException | TaskTypeNotFoundException | ServiceUnavailableException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to queue the task with type (" + type + ") for execution", e);
    }
  }

  @Override
  public UUID queueTask(String type, String batchId, String externalReference, Object dataObject)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return queueTask(type, batchId, externalReference, false, dataObject);
  }

  @Override
  public UUID queueTask(String type, String batchId, Object dataObject)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return queueTask(type, batchId, null, false, dataObject);
  }

  @Override
  public UUID queueTask(String type, Object dataObject)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return queueTask(type, null, null, false, dataObject);
  }

  @Override
  public UUID queueTask(QueueTaskRequest queueTaskRequest)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    validateQueueTaskRequest(queueTaskRequest);

    try {
      TaskType taskType = getTaskType(queueTaskRequest.getType());

      ITaskExecutor taskExecutor = getTaskExecutorForTaskType(taskType);

      String initialTaskStep = taskExecutor.getInitialTaskStep();

      Task task;

      if (initialTaskStep == null) {
        task = new Task(queueTaskRequest.getType(), queueTaskRequest.getData());

      } else {
        task = new Task(queueTaskRequest.getType(), initialTaskStep, queueTaskRequest.getData());
      }

      if ((queueTaskRequest.getSuspended() != null) && queueTaskRequest.getSuspended()) {
        task.setStatus(TaskStatus.SUSPENDED);
      }

      if (StringUtils.hasText(queueTaskRequest.getBatchId())) {
        task.setBatchId(queueTaskRequest.getBatchId());
      }

      if (StringUtils.hasText(queueTaskRequest.getExternalReference())) {
        task.setExternalReference(queueTaskRequest.getExternalReference());
      }

      taskRepository.saveAndFlush(task);

      applicationContext.getBean(BackgroundTaskExecutor.class).executeTasks();

      return task.getId();
    } catch (TaskTypeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to queue the task with type (" + queueTaskRequest.getType() + ") for execution",
          e);
    }
  }

  @Override
  public void requeueTask(Task task)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    if (task == null) {
      throw new InvalidArgumentException("task");
    }

    try {
      if (!taskRepository.existsById(task.getId())) {
        throw new TaskNotFoundException(task.getId());
      }

      TaskType taskType = getTaskType(task.getType());

      // Determine the maximum execution attempts for the task
      int maximumTaskExecutionAttempts =
          (taskType.getMaximumExecutionAttempts() != null)
              ? taskType.getMaximumExecutionAttempts()
              : this.maximumTaskExecutionAttempts;

      if (task.getExecutionAttempts() >= maximumTaskExecutionAttempts) {
        log.warn(
            "The task ("
                + task.getId()
                + ") has exceeded the maximum number of execution attempts ("
                + maximumTaskExecutionAttempts
                + ") and will be marked as \"Failed\"");

        taskRepository.unlockTask(task.getId(), TaskStatus.FAILED);
      } else {
        // Determine the retry delay
        long taskExecutionRetryDelay =
            (taskType.getRetryDelay() != null)
                ? taskType.getRetryDelay()
                : this.taskExecutionRetryDelay;

        OffsetDateTime nextExecution =
            OffsetDateTime.now().plus(taskExecutionRetryDelay, ChronoUnit.MILLIS);

        taskRepository.requeueTask(task.getId(), nextExecution);

        applicationContext.getBean(BackgroundTaskExecutor.class).executeTasks();
      }
    } catch (TaskNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to requeue the task (" + task.getId() + ")", e);
    }
  }

  @Override
  public void resetHungTasks() throws ServiceUnavailableException {
    try {
      // Apply the task-type-specific task timeouts
      for (TaskType taskType : getTaskTypes()) {
        if (taskType.getExecutionTimeout() != null) {
          OffsetDateTime lockedBefore =
              OffsetDateTime.now().minus(taskType.getExecutionTimeout(), ChronoUnit.MILLIS);

          if (inDebugMode) {
            log.info(
                "Resetting the hung tasks of type ("
                    + taskType.getCode()
                    + ") that were locked for execution before "
                    + lockedBefore);
          }

          int numberOfResetHungTasks =
              taskRepository.resetHungTasks(taskType.getCode(), lockedBefore);

          if (numberOfResetHungTasks > 0) {
            log.warn(
                "Reset "
                    + numberOfResetHungTasks
                    + " hung tasks of type ("
                    + taskType.getCode()
                    + ") that were locked for execution before "
                    + lockedBefore);
          }
        }
      }

      // Apply the global task timeout
      OffsetDateTime lockedBefore =
          OffsetDateTime.now().minus(taskExecutionTimeout, ChronoUnit.MILLIS);

      if (inDebugMode) {
        log.info("Resetting the hung tasks that were locked for execution before " + lockedBefore);
      }

      int numberOfResetHungTasks = taskRepository.resetHungTasks(lockedBefore);

      if (numberOfResetHungTasks > 0) {
        log.warn(
            "Reset "
                + numberOfResetHungTasks
                + " hung tasks that were locked for execution before "
                + lockedBefore);
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to reset the hung tasks", e);
    }
  }

  @Override
  public void resetTaskLocks(TaskStatus status, TaskStatus newStatus)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (status == null) {
      throw new InvalidArgumentException("status");
    }

    if (newStatus == null) {
      throw new InvalidArgumentException("newStatus");
    }

    try {
      taskRepository.resetTaskLocks(status, newStatus, instanceName);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to reset the locks for the tasks with status ("
              + status
              + ") that have been locked using the lock name ("
              + instanceName
              + ")",
          e);
    }
  }

  @Override
  public void setHistoricalTaskRetentionDays(int historicalTaskRetentionDays) {
    this.historicalTaskRetentionDays = historicalTaskRetentionDays;
  }

  @Override
  public void suspendBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(batchId)) {
      throw new InvalidArgumentException("batchId");
    }

    try {
      if (taskRepository.countByBatchId(batchId) == 0) {
        throw new BatchTasksNotFoundException(batchId);
      }

      taskRepository.suspendBatch(batchId);
    } catch (BatchTasksNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to suspend the batch (" + batchId + ")", e);
    }
  }

  @Override
  public void suspendTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    if (taskId == null) {
      throw new InvalidArgumentException("taskId");
    }

    try {
      if (!taskRepository.existsById(taskId)) {
        throw new TaskNotFoundException(taskId);
      }

      if (taskRepository.suspendTask(taskId) == 0) {
        throw new InvalidTaskStatusException(taskId);
      }
    } catch (TaskNotFoundException | InvalidTaskStatusException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to suspend the task (" + taskId + ")", e);
    }
  }

  @Override
  public boolean taskTypeExists(String taskTypeCode)
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      getTaskType(taskTypeCode);

      return true;
    } catch (TaskTypeNotFoundException e) {
      return false;
    }
  }

  @Override
  public void unlockTask(UUID taskId, TaskStatus status)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    if (taskId == null) {
      throw new InvalidArgumentException("taskId");
    }

    try {
      if (!taskRepository.existsById(taskId)) {
        throw new TaskNotFoundException(taskId);
      }

      taskRepository.unlockTask(taskId, status);
    } catch (TaskNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unlock and set the status for the task (" + taskId + ") to (" + status + ")",
          e);
    }
  }

  @Override
  public void unsuspendBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(batchId)) {
      throw new InvalidArgumentException("batchId");
    }

    try {
      if (taskRepository.countByBatchId(batchId) == 0) {
        throw new BatchTasksNotFoundException(batchId);
      }

      taskRepository.unsuspendBatch(batchId, OffsetDateTime.now());
    } catch (BatchTasksNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to unsuspend the batch (" + batchId + ")", e);
    }
  }

  @Override
  public void unsuspendTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    if (taskId == null) {
      throw new InvalidArgumentException("taskId");
    }

    try {
      if (!taskRepository.existsById(taskId)) {
        throw new TaskNotFoundException(taskId);
      }

      if (taskRepository.unsuspendTask(taskId, OffsetDateTime.now()) == 0) {
        throw new InvalidTaskStatusException(taskId);
      }

      applicationContext.getBean(BackgroundTaskExecutor.class).executeTasks();
    } catch (TaskNotFoundException | InvalidTaskStatusException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to unsuspend the task (" + taskId + ")", e);
    }
  }

  @Override
  public void updateTaskType(TaskType taskType)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    validateTaskType(taskType);

    try {
      taskTypesLock.writeLock().lock();

      try {
        Optional<TaskType> taskTypeOptional = taskTypeRepository.findById(taskType.getCode());

        if (taskTypeOptional.isEmpty()) {
          throw new TaskTypeNotFoundException(taskType.getCode());
        }

        taskTypeRepository.saveAndFlush(taskType);

        taskTypes = null;
      } catch (TaskTypeNotFoundException e) {
        throw e;
      } catch (Throwable e) {
        throw new ServiceUnavailableException(
            "Failed to update the task type (" + taskType.getCode() + ")", e);
      }
    } finally {
      taskTypesLock.writeLock().unlock();
    }
  }

  private void archiveTask(Task task) {
    ArchivedTask archivedTask =
        new ArchivedTask(
            task.getId(),
            task.getBatchId(),
            task.getType(),
            task.getStep(),
            task.getStatus(),
            task.getQueued(),
            task.getExecuted(),
            task.getExecutionTime(),
            task.getExternalReference(),
            task.getData());

    archivedTaskRepository.saveAndFlush(archivedTask);
  }

  private void createTaskEvent(TaskEventType taskEventType, TaskType taskType, Task task) {
    if (taskType.isEventTypeEnabledWithTaskData(taskEventType)) {
      taskEventRepository.saveAndFlush(new TaskEvent(taskEventType, task, true));
    } else if (taskType.isEventTypeEnabled(taskEventType)) {
      taskEventRepository.saveAndFlush(new TaskEvent(taskEventType, task, false));
    }
  }

  private ITaskExecutor getTaskExecutorForTaskType(TaskType taskType) {
    try {
      if (taskExecutors.containsKey(taskType.getExecutorClass())) {
        return taskExecutors.get(taskType.getExecutorClass());
      }

      Class<?> taskExecutorClass =
          Thread.currentThread().getContextClassLoader().loadClass(taskType.getExecutorClass());

      Object taskExecutorObject =
          applicationContext.getAutowireCapableBeanFactory().createBean(taskExecutorClass);

      // Check if the task executor object is a valid task executor
      if (taskExecutorObject instanceof ITaskExecutor taskExecutor) {
        taskExecutors.put(taskType.getExecutorClass(), taskExecutor);
        return taskExecutor;
      } else {
        throw new RuntimeException(
            "The task executor class ("
                + taskType.getClass()
                + ") for the task type ("
                + taskType.getCode()
                + ") does not implement the digital.inception.executor.model.ITaskExecutor interface");
      }
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to instantiate and initialize the task executor ("
              + taskType.getExecutorClass()
              + ") for the task type ("
              + taskType.getCode()
              + ")",
          e);
    }
  }

  private void validateQueueTaskRequest(QueueTaskRequest queueTaskRequest)
      throws InvalidArgumentException {
    if (queueTaskRequest == null) {
      throw new InvalidArgumentException("queueTaskRequest");
    }

    Set<ConstraintViolation<QueueTaskRequest>> constraintViolations =
        validator.validate(queueTaskRequest);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "queueTaskRequest", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateTask(Task task) throws InvalidArgumentException {
    if (task == null) {
      throw new InvalidArgumentException("task");
    }

    Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "task", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateTaskType(TaskType taskType) throws InvalidArgumentException {
    if (taskType == null) {
      throw new InvalidArgumentException("taskType");
    }

    Set<ConstraintViolation<TaskType>> constraintViolations = validator.validate(taskType);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "taskType", ValidationError.toValidationErrors(constraintViolations));
    }
  }
}

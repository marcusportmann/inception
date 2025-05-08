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

package digital.inception.executor.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.executor.model.BatchTasksNotFoundException;
import digital.inception.executor.model.DuplicateTaskTypeException;
import digital.inception.executor.model.InvalidTaskStatusException;
import digital.inception.executor.model.QueueTaskRequest;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskEvent;
import digital.inception.executor.model.TaskNotFoundException;
import digital.inception.executor.model.TaskSortBy;
import digital.inception.executor.model.TaskStatus;
import digital.inception.executor.model.TaskSummaries;
import digital.inception.executor.model.TaskType;
import digital.inception.executor.model.TaskTypeNotFoundException;
import digital.inception.executor.service.ExecutorService;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code ExecutorApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class ExecutorApiControllerImpl extends SecureApiController
    implements ExecutorApiController {

  /** The Executor Service. */
  private final ExecutorService executorService;

  /**
   * Creates a new {@code ExecutorApiControllerImpl} instance.
   *
   * @param applicationContext the Spring application context
   * @param executorService the Executor Service
   */
  public ExecutorApiControllerImpl(
      ApplicationContext applicationContext, ExecutorService executorService) {
    super(applicationContext);

    this.executorService = executorService;
  }

  @Override
  public void cancelBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    executorService.cancelBatch(batchId);
  }

  @Override
  public void cancelTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    executorService.cancelTask(taskId);
  }

  @Override
  public void createTaskType(TaskType taskType)
      throws InvalidArgumentException, DuplicateTaskTypeException, ServiceUnavailableException {
    executorService.createTaskType(taskType);
  }

  @Override
  public void deleteTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    executorService.deleteTask(taskId);
  }

  @Override
  public void deleteTaskType(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    executorService.deleteTaskType(taskTypeCode);
  }

  @Override
  public Task getTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    return executorService.getTask(taskId);
  }

  @Override
  public List<TaskEvent> getTaskEventsForTask(UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    return executorService.getTaskEventsForTask(taskId);
  }

  @Override
  public TaskSummaries getTaskSummaries(
      String taskTypeCode,
      TaskStatus status,
      String filter,
      TaskSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return executorService.getTaskSummaries(
        taskTypeCode, status, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public TaskType getTaskType(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return executorService.getTaskType(taskTypeCode);
  }

  @Override
  public String getTaskTypeName(String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return executorService.getTaskTypeName(taskTypeCode);
  }

  @Override
  public List<TaskType> getTaskTypes() throws ServiceUnavailableException {
    return executorService.getTaskTypes();
  }

  @Override
  public UUID queueTask(QueueTaskRequest queueTaskRequest)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return executorService.queueTask(queueTaskRequest);
  }

  @Override
  public void suspendBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    executorService.suspendBatch(batchId);
  }

  @Override
  public void suspendTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    executorService.suspendTask(taskId);
  }

  @Override
  public void unsuspendBatch(String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    executorService.unsuspendBatch(batchId);
  }

  @Override
  public void unsuspendTask(UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    executorService.unsuspendTask(taskId);
  }

  @Override
  public void updateTaskType(String taskTypeCode, TaskType taskType)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(taskTypeCode)) {
      throw new InvalidArgumentException("taskTypeCode");
    }

    if (taskType == null) {
      throw new InvalidArgumentException("taskType");
    }

    if (!taskTypeCode.equals(taskType.getCode())) {
      throw new InvalidArgumentException("taskType");
    }

    executorService.updateTaskType(taskType);
  }
}

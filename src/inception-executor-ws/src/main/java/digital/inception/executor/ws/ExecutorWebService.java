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

package digital.inception.executor.ws;

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
import digital.inception.executor.service.IExecutorService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.UUID;

/**
 * The <b>ExecutorWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ExecutorService",
    name = "IExecutorService",
    targetNamespace = "https://inception.digital/executor")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ExecutorWebService {

  /** The Executor Service. */
  private final IExecutorService executorService;

  /**
   * Constructs a new <b>ExecutorWebService</b>.
   *
   * @param executorService the Executor Service
   */
  public ExecutorWebService(IExecutorService executorService) {
    this.executorService = executorService;
  }

  /**
   * Cancel the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be cancelled
   */
  @WebMethod(operationName = "CancelBatch")
  public void cancelBatch(@WebParam(name = "BatchId") @XmlElement(required = true) String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    executorService.cancelBatch(batchId);
  }

  /**
   * Cancel the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be cancelled
   */
  @WebMethod(operationName = "CancelTask")
  public void cancelTask(@WebParam(name = "TaskId") @XmlElement(required = true) UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    executorService.cancelTask(taskId);
  }

  /**
   * Create the task type.
   *
   * @param taskType the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateTaskTypeException if the task type already exists
   * @throws ServiceUnavailableException if the task type could not be created
   */
  @WebMethod(operationName = "CreateTaskType")
  public void createTaskType(
      @WebParam(name = "TaskType") @XmlElement(required = true) TaskType taskType)
      throws InvalidArgumentException, DuplicateTaskTypeException, ServiceUnavailableException {
    executorService.createTaskType(taskType);
  }

  /**
   * Delete the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be deleted
   */
  @WebMethod(operationName = "DeleteTask")
  public void deleteTask(@WebParam(name = "TaskId") @XmlElement(required = true) UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    executorService.deleteTask(taskId);
  }

  /**
   * Delete the task type.
   *
   * @param taskTypeCode the code for the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be deleted
   */
  @WebMethod(operationName = "DeleteTaskType")
  public void deleteTaskType(
      @WebParam(name = "TaskTypeCode") @XmlElement(required = true) String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    executorService.deleteTaskType(taskTypeCode);
  }

  /**
   * Retrieve the task
   *
   * @param taskId the ID for the task
   * @return the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task could not be retrieved
   */
  @WebMethod(operationName = "GetTask")
  @WebResult(name = "Task")
  public Task getTask(@WebParam(name = "TaskId") @XmlElement(required = true) UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    return executorService.getTask(taskId);
  }

  /**
   * Retrieve the task events for a task
   *
   * @param taskId the ID for the task
   * @return the task events for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws ServiceUnavailableException if the task events for the task could not be retrieved
   */
  @WebMethod(operationName = "GetTaskEventsForTask")
  @WebResult(name = "TaskEvent")
  public List<TaskEvent> getTaskEventsForTask(
      @WebParam(name = "TaskId") @XmlElement(required = true) UUID taskId)
      throws InvalidArgumentException, TaskNotFoundException, ServiceUnavailableException {
    return executorService.getTaskEventsForTask(taskId);
  }

  /**
   * Retrieve the summaries for the tasks.
   *
   * @param type the optional task type code filter to apply to the task summaries
   * @param status the optional status filter to apply to the task summaries
   * @param filter the optional filter to apply to the task summaries
   * @param sortBy the optional method used to sort the task summaries e.g. by type
   * @param sortDirection the optional sort direction to apply to the task summaries
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the summaries for the tasks
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tasks summaries could not be retrieved
   */
  @WebMethod(operationName = "GetTaskSummaries")
  @WebResult(name = "TaskSummary")
  public TaskSummaries getTaskSummaries(
      @WebParam(name = "Type") @XmlElement String type,
      @WebParam(name = "Status") @XmlElement TaskStatus status,
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortBy") @XmlElement TaskSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return executorService.getTaskSummaries(
        type, status, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the task type
   *
   * @param taskTypeCode the code for the task type
   * @return the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be retrieved
   */
  @WebMethod(operationName = "GetTaskType")
  @WebResult(name = "TaskType")
  public TaskType getTaskType(
      @WebParam(name = "TaskTypeCode") @XmlElement(required = true) String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return executorService.getTaskType(taskTypeCode);
  }

  /**
   * Retrieve the name of the task type.
   *
   * @param taskTypeCode the code for the task type
   * @return the name of the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the name of the task type could not be retrieved
   */
  @WebMethod(operationName = "GetTaskTypeName")
  @WebResult(name = "TaskTypeName")
  public String getTaskTypeName(
      @WebParam(name = "TaskTypeCode") @XmlElement(required = true) String taskTypeCode)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return executorService.getTaskTypeName(taskTypeCode);
  }

  /**
   * Retrieve all the task types.
   *
   * @return the task types
   * @throws ServiceUnavailableException if the task types could not be retrieved
   */
  @WebMethod(operationName = "GetTaskTypes")
  @WebResult(name = "TaskType")
  public List<TaskType> getTaskTypes() throws ServiceUnavailableException {
    return executorService.getTaskTypes();
  }

  /**
   * Queue a task for execution.
   *
   * @param queueTaskRequest the request to queue a task for execution
   * @return the ID for the task that has been queued for execution
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task could not be queued for execution
   */
  @WebMethod(operationName = "QueueTask")
  @WebResult(name = "TaskId")
  public UUID queueTask(
      @WebParam(name = "QueueTaskRequest") @XmlElement(required = true)
          QueueTaskRequest queueTaskRequest)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    return executorService.queueTask(queueTaskRequest);
  }

  /**
   * Suspend the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be suspended
   */
  @WebMethod(operationName = "SuspendBatch")
  public void suspendBatch(@WebParam(name = "BatchId") @XmlElement(required = true) String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    executorService.suspendBatch(batchId);
  }

  /**
   * Suspend the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be suspended
   */
  @WebMethod(operationName = "SuspendTask")
  public void suspendTask(@WebParam(name = "TaskId") @XmlElement(required = true) UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    executorService.suspendTask(taskId);
  }

  /**
   * Unsuspend the batch.
   *
   * @param batchId the ID for the batch
   * @throws InvalidArgumentException if an argument is invalid
   * @throws BatchTasksNotFoundException if no tasks could be found for the batch
   * @throws ServiceUnavailableException if the batch could not be unsuspended
   */
  @WebMethod(operationName = "UnsuspendBatch")
  public void unsuspendBatch(
      @WebParam(name = "BatchId") @XmlElement(required = true) String batchId)
      throws InvalidArgumentException, BatchTasksNotFoundException, ServiceUnavailableException {
    executorService.unsuspendBatch(batchId);
  }

  /**
   * Unsuspend the task.
   *
   * @param taskId the ID for the task
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskNotFoundException if the task could not be found
   * @throws InvalidTaskStatusException if the status of the task is invalid for the operation
   * @throws ServiceUnavailableException if the task could not be unsuspended
   */
  @WebMethod(operationName = "UnsuspendTask")
  public void unsuspendTask(@WebParam(name = "TaskId") @XmlElement(required = true) UUID taskId)
      throws InvalidArgumentException,
          TaskNotFoundException,
          InvalidTaskStatusException,
          ServiceUnavailableException {
    executorService.unsuspendTask(taskId);
  }

  /**
   * Update the task type.
   *
   * @param taskType the task type
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TaskTypeNotFoundException if the task type could not be found
   * @throws ServiceUnavailableException if the task type could not be updated
   */
  @WebMethod(operationName = "UpdateTaskType")
  public void updateTaskType(
      @WebParam(name = "TaskType") @XmlElement(required = true) TaskType taskType)
      throws InvalidArgumentException, TaskTypeNotFoundException, ServiceUnavailableException {
    executorService.updateTaskType(taskType);
  }
}

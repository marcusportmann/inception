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

package digital.inception.executor.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The {@code SimpleTaskExecutor} class provides a templatized abstract base class that executors
 * for simple single-step tasks can extend. It provides capabilities for serializing and
 * deserializing a task data object to and from JSON.
 *
 * @param <TaskDataType> the task data type
 * @author Marcus Portmann
 */
public abstract class SimpleTaskExecutor<TaskDataType> implements TaskExecutor {

  private final ObjectMapper objectMapper;

  private final Class<TaskDataType> taskDataTypeClass;

  /**
   * Constructs a new {@code SimpleTaskExecutor} instance
   *
   * @param objectMapper the Jackson ObjectMapper instance
   * @param taskDataTypeClass the class for the task data type
   */
  public SimpleTaskExecutor(ObjectMapper objectMapper, Class<TaskDataType> taskDataTypeClass) {
    this.objectMapper = objectMapper;
    this.taskDataTypeClass = taskDataTypeClass;
  }

  @Override
  public TaskExecutionResult executeTask(Task task)
      throws TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException {
    try {
      TaskDataType taskData = deserializeTaskData(task.getData(), taskDataTypeClass);

      if (executeTask(task, taskData)) {
        return new TaskExecutionResult(null, serializeTaskData(taskData));
      } else {
        return TaskExecutionResult.EMPTY_TASK_EXECUTION_RESULT;
      }
    } catch (TaskExecutionFailedException
        | TaskExecutionRetryableException
        | TaskExecutionDelayedException e) {
      throw e;
    } catch (Throwable e) {
      throw new TaskExecutionFailedException(task.getId(), e);
    }
  }

  /**
   * Execute a task.
   *
   * @param task the task
   * @param taskData the task data
   * @return {@code true} if the task data has been updated and should be persisted or {@code false}
   *     otherwise
   * @throws TaskExecutionFailedException if the task execution failed
   * @throws TaskExecutionRetryableException if the task execution failed because of temporary error
   *     and can be retried
   * @throws TaskExecutionDelayedException if the task execution should be delayed
   */
  public abstract boolean executeTask(Task task, TaskDataType taskData)
      throws TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException;

  @Override
  public String getInitialTaskStep() {
    return null;
  }

  private TaskDataType deserializeTaskData(String json, Class<TaskDataType> taskDataTypeClass)
      throws JsonProcessingException {
    return objectMapper.readValue(json, taskDataTypeClass);
  }

  private String serializeTaskData(TaskDataType taskData) throws JsonProcessingException {
    return objectMapper.writeValueAsString(taskData);
  }
}

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
import java.util.List;

/**
 * The {@code MultistepTaskExecutor} class provides a templatized abstract base class that executors
 * for multistep tasks can extend. It provides capabilities for serializing and deserializing a task
 * data object to and from JSON.
 *
 * @param <TaskDataType> the task data type
 * @author Marcus Portmann
 */
public abstract class MultistepTaskExecutor<TaskDataType> implements TaskExecutor {

  private final ObjectMapper objectMapper;

  private final Class<TaskDataType> taskDataTypeClass;

  private final List<TaskStep> taskSteps;

  /**
   * Constructs a new {@code MultistepTaskExecutor} instance
   *
   * @param objectMapper the Jackson ObjectMapper instance
   * @param taskDataTypeClass the class for the task data type
   * @param taskSteps the task steps for the multistep task type associated with this multistep task
   *     executor
   */
  public MultistepTaskExecutor(
      ObjectMapper objectMapper, Class<TaskDataType> taskDataTypeClass, List<TaskStep> taskSteps) {
    this.objectMapper = objectMapper;
    this.taskDataTypeClass = taskDataTypeClass;
    this.taskSteps = taskSteps;
  }

  @Override
  public TaskExecutionResult executeTask(Task task)
      throws TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException {
    try {
      TaskStep taskStep = getTaskStep(task.getStep());

      if (taskStep == null) {
        throw new TaskExecutionFailedException(
            task.getId(),
            "Failed to execute the task ("
                + task.getId()
                + ") with the invalid task step ("
                + task.getStep()
                + ")");
      }

      TaskDataType taskData = deserializeTaskData(task.getData(), taskDataTypeClass);

      TaskStep nextTaskStep = getNextTaskStep(task.getStep(), taskData);

      if (executeTaskStep(task, taskStep, taskData)) {
        return new TaskExecutionResult(
            nextTaskStep, taskStep.getPostExecutionDelay(), serializeTaskData(taskData));
      } else {
        return new TaskExecutionResult(nextTaskStep, taskStep.getPostExecutionDelay());
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
   * Execute a task step.
   *
   * @param task the task
   * @param taskStep the task step
   * @param taskData the task data
   * @return {@code true} if the task data has been updated and should be persisted or {@code false}
   *     otherwise
   * @throws TaskExecutionFailedException if the task execution failed
   * @throws TaskExecutionRetryableException if the task execution failed because of temporary error
   *     and can be retried
   * @throws TaskExecutionDelayedException if the task execution should be delayed
   */
  public abstract boolean executeTaskStep(Task task, TaskStep taskStep, TaskDataType taskData)
      throws TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException;

  @Override
  public String getInitialTaskStep() {
    if (taskSteps.isEmpty()) {
      return null;
    } else {
      return taskSteps.getFirst().getCode();
    }
  }

  /**
   * Returns the next task step.
   *
   * @param currentTaskStepCode the code for the current task step
   * @param taskData the task date
   * @return the next task step
   */
  protected TaskStep getNextTaskStep(String currentTaskStepCode, TaskDataType taskData) {
    for (int i = 0; i < (taskSteps.size() - 1); i++) {
      TaskStep taskStep = taskSteps.get(i);
      if (taskStep.getCode().equals(currentTaskStepCode)) {
        return taskSteps.get(i + 1);
      }
    }

    return null;
  }

  private TaskDataType deserializeTaskData(String json, Class<TaskDataType> taskDataTypeClass)
      throws JsonProcessingException {
    return objectMapper.readValue(json, taskDataTypeClass);
  }

  private TaskStep getTaskStep(String taskStepCode) {
    for (TaskStep taskStep : taskSteps) {
      if (taskStep.getCode().equals(taskStepCode)) {
        return taskStep;
      }
    }

    return null;
  }

  private String serializeTaskData(TaskDataType taskData) throws JsonProcessingException {
    return objectMapper.writeValueAsString(taskData);
  }
}

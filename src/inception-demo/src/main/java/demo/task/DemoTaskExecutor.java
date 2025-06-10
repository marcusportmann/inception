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

package demo.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.executor.exception.TaskExecutionFailedException;
import digital.inception.executor.exception.TaskExecutionRetryableException;
import digital.inception.executor.model.MultistepTaskExecutor;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskStep;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code DemoTaskExecutor} class.
 *
 * @author Marcus Portmann
 */
public class DemoTaskExecutor extends MultistepTaskExecutor<DemoTaskData> {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(DemoTaskExecutor.class);

  /**
   * Constructs a new {@code DemoTaskExecutor} instance
   *
   * @param objectMapper the Jackson ObjectMapper instance
   */
  public DemoTaskExecutor(ObjectMapper objectMapper) {
    super(
        objectMapper,
        DemoTaskData.class,
        List.of(
            new TaskStep("step_1", "Step 1"),
            new TaskStep("step_2", "Step 2"),
            new TaskStep("step_3", "Step 3"),
            new TaskStep("step_4", "Step 4")));
  }

  @Override
  public boolean executeTaskStep(Task task, TaskStep taskStep, DemoTaskData taskData)
      throws TaskExecutionFailedException, TaskExecutionRetryableException {
    log.info(
        "Executing the step ("
            + taskStep.getName()
            + ") for the task ("
            + task.getId()
            + ") with type ("
            + task.getType()
            + ")");

    switch (task.getStep()) {
      case "step_1" -> {
        return false;
      }
      case "step_2" -> {
        if (taskData.getFailTask()) {
          throw new TaskExecutionFailedException(
              task.getId(), "Simulating the failure of a task with type (" + task.getType() + ")");
        }

        if (taskData.getRetryTask()) {
          throw new TaskExecutionRetryableException(
              task.getId(),
              "Simulating the temporary failure of a task with type (" + task.getType() + ")");
        }

        return false;
      }
      case "step_3" -> {
        taskData.setMessage("Message updated by step 3");
        return true;
      }
      case "step_4" -> {
        if (taskData.getSlowTask()) {
          try {
            Thread.sleep(60000L);
          } catch (Throwable ignored) {
          }
        }

        return false;
      }
      default -> {
        throw new TaskExecutionFailedException(
            task.getId(),
            "Invalid step (" + taskStep.getCode() + ") for task (" + task.getId() + ")");
      }
    }
  }
}

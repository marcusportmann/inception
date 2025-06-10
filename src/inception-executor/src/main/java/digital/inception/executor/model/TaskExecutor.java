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

import digital.inception.executor.exception.TaskExecutionDelayedException;
import digital.inception.executor.exception.TaskExecutionFailedException;
import digital.inception.executor.exception.TaskExecutionRetryableException;

/**
 * The {@code TaskExecutor} interface defines the functionality that must be provided by all task
 * executors.
 *
 * @author Marcus Portmann
 */
public interface TaskExecutor {

  /**
   * Execute a task.
   *
   * @param task the task
   * @return the result of executing the task, including the next step for a multistep task and
   *     whether the task data should be updated
   * @throws TaskExecutionFailedException if the task execution failed
   * @throws TaskExecutionRetryableException if the task execution failed because of temporary error
   *     and can be retried
   * @throws TaskExecutionDelayedException if the task execution should be delayed
   */
  TaskExecutionResult executeTask(Task task)
      throws TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException;

  /**
   * Returns the code for the initial task step for a multistep task or {@code null} for a
   * single-step task.
   *
   * @return the code for the initial task step for a multistep task or {@code null} for a
   *     single-step task
   */
  String getInitialTaskStep();
}

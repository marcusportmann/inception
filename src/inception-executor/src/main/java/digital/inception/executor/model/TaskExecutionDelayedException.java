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

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;
import java.util.UUID;

/**
 * The {@code TaskExecutionDelayedException} exception is thrown to indicate that the execution of a
 * task should be delayed.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/executor/task-execution-delayed",
    title = "The task execution delayed.",
    status = 500)
@WebFault(
    name = "TaskExecutionDelayedException",
    targetNamespace = "https://inception.digital/executor",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TaskExecutionDelayedException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /** The delay in milliseconds. */
  private final long delay;

  /** The ID for the task. */
  private final UUID taskId;

  /**
   * Constructs a new {@code TaskExecutionDelayedException}.
   *
   * @param taskId the ID for the task
   * @param delay the delay in milliseconds
   */
  public TaskExecutionDelayedException(UUID taskId, long delay) {
    super("Execution delayed for task (" + taskId + ") by (" + delay + ") milliseconds");
    this.taskId = taskId;
    this.delay = delay;
  }

  /**
   * Returns the delay in milliseconds.
   *
   * @return the delay in milliseconds
   */
  public long getDelay() {
    return delay;
  }

  /**
   * Returns the ID for the task.
   *
   * @return the ID for the task
   */
  public UUID getTaskId() {
    return taskId;
  }
}

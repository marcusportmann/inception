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
 * The <b>TaskExecutionFailedException</b> exception is thrown to indicate an error condition as a
 * result of a failure to execute a task.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/executor/task-execution-failed",
    title = "The task execution failed.",
    status = 500)
@WebFault(
    name = "TaskExecutionFailedException",
    targetNamespace = "https://inception.digital/executor",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TaskExecutionFailedException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /** The original message. */
  private final String originalMessage;

  /**
   * Constructs a new <b>TaskExecutionFailedException</b>.
   *
   * @param taskId the ID for the task
   */
  public TaskExecutionFailedException(UUID taskId) {
    super("Failed to execute the task (" + taskId + ")");
    originalMessage = null;
  }

  /**
   * Constructs a new <b>TaskExecutionFailedException</b>.
   *
   * @param taskId the ID for the task
   * @param message The message saved for later retrieval by the <b>getMessage()</b> method.
   */
  public TaskExecutionFailedException(UUID taskId, String message) {
    super("Failed to execute the task (" + taskId + "): " + message);
    originalMessage = message;
  }

  /**
   * Constructs a new <b>TaskExecutionFailedException</b>.
   *
   * @param taskId the ID for the task
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public TaskExecutionFailedException(UUID taskId, Throwable cause) {
    super("Failed to execute the task (" + taskId + "): " + cause.getMessage(), cause);
    originalMessage = cause.getMessage();
  }

  /**
   * Constructs a new <b>TaskExecutionFailedException</b>.
   *
   * @param taskId the ID for the task
   * @param message The message saved for later retrieval by the <b>getMessage()</b> method.
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public TaskExecutionFailedException(UUID taskId, String message, Throwable cause) {
    super("Failed to execute the task (" + taskId + "): " + message, cause);
    originalMessage = message;
  }

  /**
   * Returns the original message.
   *
   * @return the original message
   */
  public String getOriginalMessage() {
    return originalMessage;
  }
}

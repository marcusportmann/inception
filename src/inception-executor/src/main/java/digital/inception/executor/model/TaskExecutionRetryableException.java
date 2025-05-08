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
 * The {@code TaskExecutionRetryableException} exception is thrown to indicate that the execution of
 * a task failed because of a temporary error and can be retried.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/executor/task-execution-retryable",
    title = "The task execution failed because of a temporary error and can be retried.",
    status = 500)
@WebFault(
    name = "TaskExecutionRetryableException",
    targetNamespace = "https://inception.digital/executor",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TaskExecutionRetryableException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Creates a new {@code TaskExecutionRetryableException} instance.
   *
   * @param taskId the ID for the task
   */
  public TaskExecutionRetryableException(UUID taskId) {
    super(
        "The execution of the task ("
            + taskId
            + ") failed because of a temporary error and can be retried");
  }

  /**
   * Creates a new {@code TaskExecutionRetryableException} instance.
   *
   * @param taskId the ID for the task
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public TaskExecutionRetryableException(UUID taskId, String message) {
    super(
        "The execution of the task ("
            + taskId
            + ") failed because of a temporary error and can be retried: "
            + message);
  }

  /**
   * Creates a new {@code TaskExecutionRetryableException} instance.
   *
   * @param taskId the ID for the task
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public TaskExecutionRetryableException(UUID taskId, Throwable cause) {
    super(
        "The execution of the task ("
            + taskId
            + ") failed because of a temporary error and can be retried: "
            + cause.getMessage(),
        cause);
  }

  /**
   * Creates a new {@code TaskExecutionRetryableException} instance.
   *
   * @param taskId the ID for the task
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public TaskExecutionRetryableException(UUID taskId, String message, Throwable cause) {
    super(
        "The execution of the task ("
            + taskId
            + ") failed because of a temporary error and can be retried: "
            + message,
        cause);
  }
}

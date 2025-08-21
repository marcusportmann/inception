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

package digital.inception.operations.connector;

import java.io.Serial;

/**
 * The {@code WorkflowEngineConnectorException} exception is thrown to indicate an error condition
 * when working with a workflow engine connector.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class WorkflowEngineConnectorException extends Exception {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the workflow engine the workflow engine connector is associated with. */
  private final String engineId;

  /**
   * Constructs a new {@code WorkflowEngineConnectorException} instance with {@code null} as its
   * message.
   *
   * @param engineId The ID for the workflow engine the workflow engine connector is associated
   *     with.
   */
  public WorkflowEngineConnectorException(String engineId) {
    super();

    this.engineId = engineId;
  }

  /**
   * Constructs a new {@code WorkflowEngineConnectorException} instance with the specified message.
   *
   * @param engineId The ID for the workflow engine the workflow engine connector is associated
   *     with.
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public WorkflowEngineConnectorException(String engineId, String message) {
    super(message);

    this.engineId = engineId;
  }

  /**
   * Constructs a new {@code WorkflowEngineConnectorException} instance with the specified cause and
   * a message of {@code (cause==null ? null : cause.toString())} (which typically contains the
   * class and message of cause).
   *
   * @param engineId The ID for the workflow engine the workflow engine connector is associated
   *     with.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public WorkflowEngineConnectorException(String engineId, Throwable cause) {
    super(cause);

    this.engineId = engineId;
  }

  /**
   * Constructs a new {@code WorkflowEngineConnectorException} instance with the specified message
   * and cause.
   *
   * @param engineId The ID for the workflow engine the workflow engine connector is associated
   *     with.
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public WorkflowEngineConnectorException(String engineId, String message, Throwable cause) {
    super(message, cause);

    this.engineId = engineId;
  }

  /**
   * Returns the ID for the workflow engine the workflow engine connector is associated with.
   *
   * @return the ID for the workflow engine the workflow engine connector is associated with
   */
  public String getEngineId() {
    return engineId;
  }
}

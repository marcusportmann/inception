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
 * The <b>TaskNotFoundException</b> exception is thrown to indicate an error condition as a result
 * of a task that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/executor/task-not-found",
    title = "The task could not be found.",
    status = 404)
@WebFault(
    name = "TaskNotFoundException",
    targetNamespace = "https://inception.digital/executor",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TaskNotFoundException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>TaskNotFoundException</b>.
   *
   * @param taskId the ID for the task
   */
  public TaskNotFoundException(UUID taskId) {
    super("The task (" + taskId + ") could not be found");
  }

  /**
   * Constructs a new <b>TaskNotFoundException</b>.
   *
   * @param externalReference the external reference for the task
   */
  public TaskNotFoundException(String externalReference) {
    super("The task with the external reference (" + externalReference + ") could not be found");
  }
}

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
 * The <b>InvalidTaskStatusException</b> exception is thrown to indicate that the status of the task
 * is invalid for the requested operation.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/executor/invalid-task-status",
    title = "The status of the task is invalid for the requested operation.",
    status = 409)
@WebFault(
    name = "InvalidTaskStatusException",
    targetNamespace = "https://inception.digital/executor",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class InvalidTaskStatusException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>InvalidTaskStatusException</b>.
   *
   * @param taskId the ID for the task
   */
  public InvalidTaskStatusException(UUID taskId) {
    super("The status of the task (%s) is invalid for the requested operation".formatted(taskId));
  }
}

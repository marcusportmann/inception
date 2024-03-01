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

package digital.inception.security;

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;
import java.util.UUID;

/**
 * A <b>DuplicateUserDirectoryException</b> is thrown to indicate that a security operation failed
 * as a result of a duplicate user directory.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/security/duplicate-user-directory",
    title = "A user directory with the specified ID or name already exists.",
    status = 409)
@WebFault(
    name = "DuplicateUserDirectoryException",
    targetNamespace = "http://inception.digital/security",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateUserDirectoryException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>DuplicateUserDirectoryException</b>.
   *
   * @param name the name of the user directory
   */
  public DuplicateUserDirectoryException(String name) {
    super("The user directory (" + name + ") already exists");
  }

  /**
   * Constructs a new <b>DuplicateUserDirectoryException</b>.
   *
   * @param userDirectoryId the ID for the user directory
   */
  public DuplicateUserDirectoryException(UUID userDirectoryId) {
    super("The user directory (" + userDirectoryId + ") already exists");
  }
}

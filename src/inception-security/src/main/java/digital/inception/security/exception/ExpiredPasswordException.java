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

package digital.inception.security.exception;

import digital.inception.core.exception.Problem;
import digital.inception.core.exception.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * An {@code ExpiredPasswordException} exception is thrown to indicate that a security operation
 * failed as a result of an expired password.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/security/expired-password",
    title = "The password for the user has expired.",
    status = 409)
@WebFault(
    name = "ExpiredPasswordException",
    targetNamespace = "https://inception.digital/security",
    faultBean = "digital.inception.core.exception.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ExpiredPasswordException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code ExpiredPasswordException}.
   *
   * @param username the username for the user
   */
  public ExpiredPasswordException(String username) {
    super("The password for the user (" + username + ") has expired");
  }
}

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

package digital.inception.security.model;

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * An <b>AuthenticationFailedException</b> is thrown to indicate that a security operation failed as
 * a result of an authentication failure.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/security/authentication-failed",
    title = "Authentication failed.",
    status = 401)
@WebFault(
    name = "AuthenticationFailedException",
    targetNamespace = "https://inception.digital/security",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AuthenticationFailedException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Creates a new {@code AuthenticationFailedException} instance with the specified message.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public AuthenticationFailedException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code AuthenticationFailedException} instance with the specified message and
   * cause.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public AuthenticationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}

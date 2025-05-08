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
 * A <b>InvalidPolicyDataException</b> is thrown to indicate that a security operation failed as a
 * result of invalid policy data, e.g. the ID or version of the policy do not match what was
 * specified in the XACML data.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/security/invalid-policy-data",
    title = "The policy data is invalid.",
    status = 400)
@WebFault(
    name = "InvalidPolicyDataException",
    targetNamespace = "https://inception.digital/security",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings("unused")
public class InvalidPolicyDataException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Creates a new {@code InvalidPolicyDataException} instance with the specified message.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public InvalidPolicyDataException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code InvalidPolicyDataException} instance with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public InvalidPolicyDataException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@code InvalidPolicyDataException} instance.
   *
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public InvalidPolicyDataException(Throwable cause) {
    super("The policy data is invalid", cause);
  }
}

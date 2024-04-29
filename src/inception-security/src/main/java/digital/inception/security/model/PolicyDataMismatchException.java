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
 * A <b>PolicyDataMismatchException</b> is thrown to indicate that a security operation failed as a
 * result of a policy data mismatch error, i.e. where the policy ID or version does not match the
 * policy data.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/security/policy-data-mismatch",
    title = "The policy type, policy ID or policy version does not match the policy data.",
    status = 400)
@WebFault(
    name = "PolicyDataMismatchException",
    targetNamespace = "https://inception.digital/security",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PolicyDataMismatchException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>PolicyDataMismatchException</b> with the specified message.
   *
   * @param message The message saved for later retrieval by the <b>getMessage()</b> method.
   */
  public PolicyDataMismatchException(String message) {
    super(message);
  }

  /**
   * Constructs a new <b>PolicyDataMismatchException</b> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <b>getMessage()</b> method.
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public PolicyDataMismatchException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new <b>PolicyDataMismatchException</b>.
   *
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public PolicyDataMismatchException(Throwable cause) {
    super("The policy type, policy ID or policy version does not match the policy data", cause);
  }
}

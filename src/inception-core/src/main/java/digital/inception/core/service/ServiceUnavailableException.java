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

package digital.inception.core.service;

import digital.inception.core.api.ProblemDetails;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The {@code ServiceUnavailableException} exception is thrown to indicate an error condition when a
 * service is unavailable and a request could not be processed.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@WebFault(
    name = "ServiceUnavailableException",
    targetNamespace = "https://inception.digital/core",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused", "WeakerAccess"})
public class ServiceUnavailableException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /** The problem details object, as defined by RFC 7807, associated with the exception. */
  private final ProblemDetails problemDetails;

  /**
   * Creates a new {@code ServiceUnavailableException} instance with the specified message.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public ServiceUnavailableException(String message) {
    super(message);

    problemDetails = null;
  }

  /**
   * Creates a new {@code ServiceUnavailableException} instance with the specified message and
   * cause.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public ServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);

    problemDetails = null;
  }

  /**
   * Creates a new {@code ServiceUnavailableException} instance with the specified message and
   * cause.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param problemDetails the problem details object, as defined by RFC 7807, associated with the
   *     exception
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public ServiceUnavailableException(
      String message, ProblemDetails problemDetails, Throwable cause) {
    super(message, cause);

    this.problemDetails = problemDetails;
  }

  /**
   * Creates a new {@code ServiceUnavailableException} instance with the specified message and
   * cause.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param problemDetails the problem details object, as defined by RFC 7807, associated with the
   *     exception
   */
  public ServiceUnavailableException(String message, ProblemDetails problemDetails) {
    super(message);

    this.problemDetails = problemDetails;
  }

  /**
   * Returns the problem details object, as defined by RFC 7807, associated with the exception.
   *
   * @return the problem details object, as defined by RFC 7807, associated with the exception
   */
  public ProblemDetails getProblemDetails() {
    return problemDetails;
  }
}

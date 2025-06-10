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

package digital.inception.core.exception;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The {@code BusinessException} exception is thrown to indicate a business error when invoking a
 * service.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/business-error",
    title = "A business error has occurred and your request could not be processed.",
    status = 422)
@WebFault(
    name = "BusinessException",
    targetNamespace = "https://inception.digital/core",
    faultBean = "digital.inception.core.exception.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused", "WeakerAccess"})
public class BusinessException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /** The error code identifying the business error. */
  private final String code;

  /**
   * Constructs a new {@code BusinessException} instance with the specified message.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public BusinessException(String message) {
    super(message);
    this.code = "";
  }

  /**
   * Constructs a new {@code BusinessException} instance with the specified code and message.
   *
   * @param code The error code identifying the business error.
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public BusinessException(String code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Constructs a new {@code BusinessException} instance with the specified code, message and cause.
   *
   * @param code The error code identifying the business error.
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public BusinessException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Constructs a new {@code BusinessException} instance with the specified code, message and cause.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public BusinessException(String message, Throwable cause) {
    super(message, cause);
    this.code = "";
  }

  /**
   * Returns the error code identifying the business error.
   *
   * @return the error code identifying the business error
   */
  public String getCode() {
    return code;
  }
}

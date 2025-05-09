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

import digital.inception.core.util.ISO8601Util;
import java.io.Serial;
import java.time.OffsetDateTime;

/**
 * The {@code RichException} exception is thrown when additional information related to an error
 * condition exists including the time the error occurred and an associated error code. condition.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class RichException extends Exception {

  private static final String NO_ERROR_CODE = "NONE";

  @Serial private static final long serialVersionUID = 1000000;

  /** The date and time the exception occurred. */
  private final OffsetDateTime when;

  /** The error code. */
  private String code;

  /** Constructs a new {@code RichException} instance with {@code null} as its message. */
  public RichException() {
    super();
    this.when = OffsetDateTime.now();
  }

  /**
   * Constructs a new {@code RichException} instance with the specified message.
   *
   * @param message the message saved for later retrieval by the {@code getMessage()} method
   */
  public RichException(String message) {
    super(message);
    this.when = OffsetDateTime.now();
  }

  /**
   * Constructs a new {@code RichException} instance with the specified cause and a message of
   * {@code (cause==null ? null : cause.toString())} (which typically contains the class and message
   * of cause).
   *
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public RichException(Throwable cause) {
    super(cause);
    this.when = OffsetDateTime.now();
  }

  /**
   * Constructs a new {@code RichException} instance with the specified code and message.
   *
   * @param code the error code
   * @param message the message saved for later retrieval by the {@code getMessage()} method
   */
  public RichException(String code, String message) {
    super(message);
    this.code = code;
    this.when = OffsetDateTime.now();
  }

  /**
   * Constructs a new {@code RichException} instance with the specified message and cause.
   *
   * @param message the message saved for later retrieval by the {@code getMessage()} method
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public RichException(String message, Throwable cause) {
    super(message, cause);
    this.when = OffsetDateTime.now();
  }

  /**
   * Constructs a new {@code RichException} instance with the specified code, message and cause.
   *
   * @param code the error code
   * @param message the message saved for later retrieval by the {@code getMessage()} method
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public RichException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
    this.when = OffsetDateTime.now();
  }

  /**
   * Returns the error code or NONE if no error code was specified.
   *
   * @return the error code or NONE if no error code was specified
   */
  public String getCode() {
    return (code == null) ? NO_ERROR_CODE : code;
  }

  /**
   * Returns the date and time the exception occurred.
   *
   * @return the date and time the exception occurred
   */
  public OffsetDateTime getWhen() {
    return when;
  }

  /**
   * Returns the date and time the exception occurred as a String.
   *
   * @return the date and time the exception occurred as a String
   */
  @SuppressWarnings("unused")
  public String getWhenAsString() {
    return ISO8601Util.fromOffsetDateTime(when);
  }
}

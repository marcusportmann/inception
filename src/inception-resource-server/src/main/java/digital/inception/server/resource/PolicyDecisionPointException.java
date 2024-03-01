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

package digital.inception.server.resource;

import digital.inception.core.exception.RichRuntimeException;
import java.io.Serial;

/**
 * The <b>PolicyDecisionPointException</b> exception is thrown to indicate a policy decision point
 * error.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PolicyDecisionPointException extends RichRuntimeException {

  @Serial private static final long serialVersionUID = 1000000;

  /** Constructs a new <b>PolicyDecisionPointException</b> with <b>null</b> as its message. */
  public PolicyDecisionPointException() {
    super();
  }

  /**
   * Constructs a new <b>PolicyDecisionPointException</b> with the specified message.
   *
   * @param message the message saved for later retrieval by the <b>getMessage()</b> method
   */
  public PolicyDecisionPointException(String message) {
    super(message);
  }

  /**
   * Constructs a new <b>PolicyDecisionPointException</b> with the specified cause and a message of
   * <b>(cause==null ? null : cause.toString())</b> (which typically contains the class and message
   * of cause).
   *
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public PolicyDecisionPointException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new <b>PolicyDecisionPointException</b> with the specified code and message.
   *
   * @param code the error code for the error
   * @param message the message saved for later retrieval by the <b>getMessage()</b> method
   */
  public PolicyDecisionPointException(String code, String message) {
    super(code, message);
  }

  /**
   * Constructs a new <b>PolicyDecisionPointException</b> with the specified message and cause.
   *
   * @param message the message saved for later retrieval by the <b>getMessage()</b> method
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public PolicyDecisionPointException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new <b>PolicyDecisionPointException</b> with the specified code, message and
   * cause.
   *
   * @param code the error code for the error
   * @param message the message saved for later retrieval by the <b>getMessage()</b> method
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public PolicyDecisionPointException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }
}

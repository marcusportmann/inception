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

package digital.inception.kafka;

import java.io.Serial;

/**
 * The <code>TransientErrorException</code> exception is thrown to indicate a transient error
 * condition when a Kafka processor or one of its dependencies is temporarily unavailable.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class TransientErrorException extends Exception {
  @Serial private static final long serialVersionUID = 1000000;

  /** Constructs a new <code>TransientErrorException</code>. */
  public TransientErrorException() {
    super();
  }

  /**
   * Constructs a new <code>TransientErrorException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public TransientErrorException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>TransientErrorException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method. (A
   *     <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public TransientErrorException(String message, Throwable cause) {
    super(message, cause);
  }
}

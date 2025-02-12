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

package digital.inception.api.client;

import java.io.Serial;

/**
 * The <b>ApiClientException</b> exception is thrown to indicate an API client error.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ApiClientException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1000000;

  /** Constructs a new <b>ApiClientException</b> with <b>null</b> as its message. */
  public ApiClientException() {
    super();
  }

  /**
   * Constructs a new <b>ApiClientException</b> with the specified message.
   *
   * @param message The message saved for later retrieval by the <b>getMessage()</b> method.
   */
  public ApiClientException(String message) {
    super(message);
  }

  /**
   * Constructs a new <b>ApiClientException</b> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <b>getMessage()</b> method.
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public ApiClientException(String message, Throwable cause) {
    super(message, cause);
  }
}

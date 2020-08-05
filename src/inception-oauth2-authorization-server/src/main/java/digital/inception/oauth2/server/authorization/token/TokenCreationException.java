/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.oauth2.server.authorization.token;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.service.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TokenCreationException</code> exception is thrown to indicate an error condition when
 * creating an OAuth2 token.
 *
 * <p>NOTE: This is a checked exception to prevent the automatic rollback of the current
 * transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(
    value = HttpStatus.INTERNAL_SERVER_ERROR,
    reason = "The OAuth2 token could not be created")
@SuppressWarnings({"unused"})
public class TokenCreationException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>TokenCreationException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public TokenCreationException(String message) {
    super("TokenCreationError", message);
  }

  /**
   * Constructs a new <code>TokenCreationException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method. (A
   *                <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public TokenCreationException(String message, Throwable cause) {
    super("TokenCreationError", message, cause);
  }
}

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

package digital.inception.server.authorization.token;

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import java.io.Serial;

/**
 * The {@code TokenCreationException} exception is thrown to indicate an error condition when
 * creating an OAuth2 token.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/oauth2/token-creation",
    title = "The OAuth2 token could not be created.")
public class TokenCreationException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code TokenCreationException} instance with the specified message.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public TokenCreationException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code TokenCreationException} instance with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public TokenCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}

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

package digital.inception.server.authorization.token.exception;

import digital.inception.core.exception.Problem;
import digital.inception.core.exception.ServiceException;
import java.io.Serial;

/**
 * The {@code InvalidOAuth2RefreshTokenException} exception is thrown to indicate an error condition
 * as a result of an invalid OAuth2 refresh token.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/oauth2/invalid-oauth2-refresh-token",
    title = "The OAuth2 refresh token is invalid.",
    status = 400)
public class InvalidOAuth2RefreshTokenException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code InvalidOAuth2RefreshTokenException} instance with the specified
   * message.
   */
  public InvalidOAuth2RefreshTokenException() {
    super("The OAuth2 refresh token is invalid");
  }
}

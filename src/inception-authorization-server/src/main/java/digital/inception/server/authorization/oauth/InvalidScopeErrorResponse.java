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

package digital.inception.server.authorization.oauth;

import org.springframework.http.HttpStatus;

/**
 * The {@code InvalidScopeErrorResponse} class holds the information for an OAuth2 invalid scope
 * error response.
 *
 * @author Marcus Portmann
 */
public class InvalidScopeErrorResponse extends ErrorResponse {

  /** The error code for the OAuth2 invalid scope error response. */
  public static final String ERROR_CODE = "invalid_request";

  /** Constructs a new {@code InvalidScopeErrorResponse}. */
  public InvalidScopeErrorResponse() {
    super(HttpStatus.BAD_REQUEST, ERROR_CODE);
  }

  /**
   * Constructs a new {@code InvalidScopeErrorResponse}.
   *
   * @param errorDescription the human-readable ASCII text description of the error
   */
  public InvalidScopeErrorResponse(String errorDescription) {
    super(HttpStatus.BAD_REQUEST, ERROR_CODE, errorDescription);
  }
}

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

import java.util.Set;

/**
 * The <code>ITokenService</code> interface defines the functionality provided by a Token Service
 * implementation, which manages the OAuth2 tokens for an application.
 *
 * @author Marcus Portmann
 */
public interface ITokenService {

  /**
   * Issue an OAuth2 access token for the specified user.
   *
   * @param username the username identifying the user
   * @param scopes   the optional scope(s) associated to the access token
   *
   * @return the OAuth2 access token
   */
  OAuth2AccessToken issueOAuth2AccessToken(String username, Set<String> scopes)
      throws TokenServiceException;

  /**
   * Issue an OAuth2 refresh token for the specified user.
   *
   * @param username the username identifying the user
   * @param scopes   the optional scope(s) associated to the refresh token
   *
   * @return the OAuth2 refresh token
   */
  OAuth2RefreshToken issueOAuth2RefreshToken(String username, Set<String> scopes)
      throws TokenServiceException;

  /**
   * Refresh an OAuth2 access token and if required the OAuth2 refresh token.
   *
   * @param encodedOAuth2RefreshToken the encoded OAuth2 refresh token
   *
   * @return the refreshed tokens
   */
  RefreshedOAuth2Tokens refreshOAuth2Tokens(String encodedOAuth2RefreshToken)
      throws InvalidOAuth2RefreshTokenException, TokenServiceException;
}

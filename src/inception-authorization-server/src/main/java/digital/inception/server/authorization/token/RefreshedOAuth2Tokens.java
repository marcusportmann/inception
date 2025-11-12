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

/**
 * The {@code RefreshedOAuth2Tokens} class holds a refreshed OAuth2 access token and optionally a
 * refreshed OAuth2 refresh token.
 *
 * @author Marcus Portmann
 * @param accessToken The OAuth2 access token.
 * @param refreshToken The OAuth2 refresh token.
 */
public record RefreshedOAuth2Tokens(
    OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken) {

  /**
   * Constructs a new {@code RefreshedOAuth2Tokens}.
   *
   * @param accessToken the OAuth2 access token
   * @param refreshToken the OAuth2 refresh token
   */
  public RefreshedOAuth2Tokens {}

  /**
   * Returns the OAuth2 access token.
   *
   * @return the OAuth2 access token
   */
  @Override
  public OAuth2AccessToken accessToken() {
    return accessToken;
  }

  /**
   * Returns the OAuth2 refresh token.
   *
   * @return the OAuth2 refresh token
   */
  @Override
  public OAuth2RefreshToken refreshToken() {
    return refreshToken;
  }
}

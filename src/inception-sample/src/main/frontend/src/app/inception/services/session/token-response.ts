/*
 * Copyright 2018 Marcus Portmann
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

/**
 * The TokenResponse interface defines the structure of the token response returned by the Spring
 * OAuth2 authorization server.
 *
 * @author Marcus Portmann
 */

export interface TokenResponse {

  /**
   * The base-64 encoded OAuth2 JWT access token.
   */
  access_token?: string;

  /**
   * The epoch timestamp, for the local timezone, giving the date and time the OAuth2 JWT access
   * token will expire.
   */
  expires_in?: number;

  /**
   * The unique ID
   */
  jti?: string;

  /**
   * The base-64 encoded JWT refresh token.
   */
  refresh_token?: string;

  /**
   * The OAuth2 scopes.
   */
  scope?: string[];

  /**
   * The OAuth2 token type e.g. bearer.
   */
  token_type?: string;
}
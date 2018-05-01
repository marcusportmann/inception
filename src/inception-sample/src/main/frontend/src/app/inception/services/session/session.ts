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
 * The Session class holds the information for an active user session associated with an
 * authenticated user.
 *
 * @author Marcus Portmann
 */
export class Session {

  /**
   * The username for the user the user session is associated with.
   */
  username: string;

  /**
   * The OAuth2 scopes for the user session.
   */
  scopes: string[];

  /**
   * The codes identifying the functions the user associated with the user session has access to.
   */
  functions: string[];

  /**
   * The base-64 encoded OAuth2 JWT access token for the user session.
   */
  accessToken: string;

  /**
   * The date and time the OAuth2 JWT access token for the user session will expire.
   */
  accessTokenExpiry: Date;

  /**
   * The base-64 encoded OAuth2 refresh token for the user session.
   */
  refreshToken: string;

  /**
   * Constructs a new Session.
   *
   * @param {string} username          The username for the user the user session is associated
   *                                   with.
   * @param {string[]} scopes          The OAuth2 scopes for the user session.
   * @param {string[]} functions       The codes identifying the functions the user associated with
   *                                   the user session has access to.
   * @param {string} accessToken       The base-64 encoded OAuth2 JWT access token for the user
   *                                   session.
   * @param {number} accessTokenExpiry The date and time the OAuth2 JWT access token for the user
   *                                   session will expire.
   * @param {string} refreshToken      The base-64 encoded OAuth2 refresh token for the user
   *                                   session.
   */
  constructor(username: string, scopes: string[], functions: string[], accessToken: string, accessTokenExpiry: number, refreshToken: string) {
    this.username = username;
    this.scopes = scopes;
    this.functions = functions;
    this.accessToken = accessToken;
    this.accessTokenExpiry = new Date(accessTokenExpiry * 1000);
    this.refreshToken = refreshToken;
  }
}

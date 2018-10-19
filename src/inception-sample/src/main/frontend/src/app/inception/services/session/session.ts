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

import {Organization} from "../security/organization";

/**
 * The Session class holds the information for an active user session associated with an
 * authenticated user.
 *
 * @author Marcus Portmann
 */
export class Session {

  /**
   * The username for the user, the user session is associated with.
   */
  username: string;

  /**
   * The OAuth2 scopes for the user session.
   */
  scopes: string[];

  /**
   * The codes identifying the functions the user, associated with the user session, has access to.
   */
  functionCodes: string[];

  /**
   * The base-64 encoded OAuth2 JWT access token for the user session.
   */
  accessToken: string;

  /**
   * The ISO8601 format string giving the date and time the OAuth2 JWT access token for the user
   * session will expire.
   */
  accessTokenExpiry: string;

  /**
   * The base-64 encoded OAuth2 refresh token for the user session.
   */
  refreshToken: string;

  /**
   * The organizations for the user, the user session is associated with.
   */
  organizations: Organization[];

  /**
   * Constructs a new Session.
   *
   * @param {string} username              The username for the user the user session is associated
   *                                       with.
   * @param {string[]} scopes              The OAuth2 scopes for the user session.
   * @param {string[]} functionCodes       The codes identifying the functions the user associated
   *                                       with the user session has access to.
   * @param {Organization[]} organizations The organizations for the user, the user session is
   *                                       associated with.
   * @param {string} accessToken           The base-64 encoded OAuth2 JWT access token for the user
   *                                       session.
   * @param {number} accessTokenExpiry     The ISO8601 format string giving the date and time the
   *                                       OAuth2 JWT access token for the user session will expire.
   * @param {string} refreshToken          The base-64 encoded OAuth2 refresh token for the user
   *                                       session.
   */
  constructor(username: string, scopes: string[], functionCodes: string[], organizations: Organization[], accessToken: string, accessTokenExpiry: string, refreshToken: string) {
    this.username = username;
    this.scopes = scopes;
    this.functionCodes = functionCodes;
    this.organizations = organizations;
    this.accessToken = accessToken;
    this.accessTokenExpiry = accessTokenExpiry;
    this.refreshToken = refreshToken;
  }
}

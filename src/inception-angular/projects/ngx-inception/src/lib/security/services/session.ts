/*
 * Copyright 2019 Marcus Portmann
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

import {Organization} from './organization';

/**
 * The Session class holds the information for an active user session associated with an
 * authenticated user. All values are stored as strings to support the serialization of the user
 * session.
 *
 * @author Marcus Portmann
 */
export class Session {

  /**
   * The username for the user, the user session is associated with.
   */
  username: string;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the user directory the
   * user is associated with.
   */
  userDirectoryId: string;

  /**
   * The full name for the user.
   */
  userFullName: string;

  /**
   * The OAuth2 scopes for the user session.
   */
  scopes: string[];

  /**
   * The authorities for the user session associated with the session.
   */
  authorities: string[];

  /**
   * The base-64 encoded OAuth2 JWT access token for the user session.
   */
  accessToken: string;

  /**
   * The date and time the OAuth2 JWT access token for the user session will expire.
   */
  accessTokenExpiry?: Date;

  /**
   * The base-64 encoded OAuth2 refresh token for the user session.
   */
  refreshToken?: string;

  /**
   * The selected organization for the user session.
   */
  organization?: Organization;

  /**
   * Constructs a new Session.
   *
   * @param username          The username for the user the user session is associated with.
   * @param userDirectoryId   The Universally Unique Identifier (UUID) uniquely identifying the
   *                          user directory the user is associated with.
   * @param userFullName      The full name for the user.
   * @param scopes            The OAuth2 scopes for the user session.
   * @param authorities       The The authorities for the user session associated with the session.
   * @param accessToken       The base-64 encoded OAuth2 JWT access token for the user session.
   * @param accessTokenExpiry The string representation of the epoch timestamp giving the date and
   *                          time the OAuth2 JWT access token for the user session will expire.
   * @param refreshToken      The base-64 encoded OAuth2 refresh token for the user session.
   */
  constructor(username: string, userDirectoryId: string, userFullName: string, scopes: string[], authorities: string[],
              accessToken: string, accessTokenExpiry?: Date, refreshToken?: string) {
    this.username = username;
    this.userDirectoryId = userDirectoryId;
    this.userFullName = userFullName;
    this.scopes = scopes;
    this.authorities = authorities;
    this.accessToken = accessToken;
    this.accessTokenExpiry = accessTokenExpiry;
    this.refreshToken = refreshToken;
  }

  /**
   * Confirm that the user associated with the session has the specified authority.
   *
   * @param requiredAuthority The required authority.
   *
   * @return True if the user associated with the session has the specified authority or false
   *         otherwise.
   */
  hasAuthority(requiredAuthority: string): boolean {

    requiredAuthority = requiredAuthority.toLowerCase();

    for (const authority of this.authorities) {
      if (authority.toLowerCase() === requiredAuthority) {
        return true;
      }
    }

    return false;
  }

  /**
   * Confirm that the user associated with the session has access to the specified function.
   *
   * @param functionCode The code uniquely identifying the function.
   *
   * @return True if the user associated with the session has access to the specified function
   *         or false otherwise.
   */
  hasFunction(functionCode: string): boolean {
    return this.hasAuthority('FUNCTION_' + functionCode);
  }

  /**
   * Confirm that the user associated with the session has the specified role.
   *
   * @param roleName The name of the role.
   *
   * @return True if the user associated with the session has the specified role or false
   *         otherwise.
   */
  hasRole(roleName: string): boolean {
    return this.hasAuthority('ROLE_' + roleName);
  }
}

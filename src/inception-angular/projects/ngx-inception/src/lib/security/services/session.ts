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
   * The base-64 encoded OAuth2 JWT access token for the user session.
   */
  accessToken: string;

  /**
   * The date and time the OAuth2 JWT access token for the user session will expire.
   */
  accessTokenExpiry?: Date;

  /**
   * The codes identifying the functions assigned to the user associated with the user session.
   */
  functionCodes: string[];

  /**
   * The selected organization for the user session.
   */
  organization?: Organization;

  /**
   * The Universally Unique Identifiers (UUIDs) uniquely identifying the organizations the user is
   * associated with.
   */
  organizationIds: string[];

  /**
   * The base-64 encoded OAuth2 refresh token for the user session.
   */
  refreshToken?: string;

  /**
   * The codes identifying the roles assigned to the user associated with the user session.
   */
  roleCodes: string[];

  /**
   * The OAuth2 scopes for the user session.
   */
  scopes: string[];

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
   * The full name of the user.
   */
  userFullName: string;

  /**
   * Constructs a new Session.
   *
   * @param username          The username for the user the user session is associated with.
   * @param userDirectoryId   The Universally Unique Identifier (UUID) uniquely identifying the
   *                          user directory the user is associated with.
   * @param userFullName      The full name of the user.
   * @param scopes            The OAuth2 scopes for the user session.
   * @param roleCodes         The codes identifying the roles assigned to the user associated with
   *                          the user session.
   * @param functionCodes     The codes identifying the functions assigned to the user associated
   *                          with the user session.
   * @param organizationIds   The Universally Unique Identifiers (UUIDs) uniquely identifying the
   *                          organizations the user is associated with.
   * @param accessToken       The base-64 encoded OAuth2 JWT access token for the user session.
   * @param accessTokenExpiry The string representation of the epoch timestamp giving the date and
   *                          time the OAuth2 JWT access token for the user session will expire.
   * @param refreshToken      The base-64 encoded OAuth2 refresh token for the user session.
   */
  constructor(username: string, userDirectoryId: string, userFullName: string, scopes: string[],
              roleCodes: string[], functionCodes: string[], organizationIds: string[],
              accessToken: string, accessTokenExpiry?: Date, refreshToken?: string) {
    this.username = username;
    this.userDirectoryId = userDirectoryId;
    this.userFullName = userFullName;
    this.scopes = scopes;
    this.roleCodes = roleCodes;
    this.functionCodes = functionCodes;
    this.organizationIds = organizationIds;
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
    if (requiredAuthority.startsWith("ROLE_")) {
      if (requiredAuthority.length < 6) {
        return false;
      } else {
        return this.hasRole(requiredAuthority.substr(5));
      }
    }

    if (requiredAuthority.startsWith("FUNCTION_")) {
      if (requiredAuthority.length < 10) {
        return false;
      } else {
        return this.hasFunction(requiredAuthority.substr(9));
      }
    }

    return false;
  }

  /**
   * Confirm that the user associated with the session has been assigned the required function.
   *
   * @param requiredFunctionCode The code uniquely identifying the required function.
   *
   * @return True if the user associated with the session has been assigned the required function
   *         or false otherwise.
   */
  hasFunction(requiredFunctionCode: string): boolean {
    for (const functionCode of this.functionCodes) {
      if (functionCode.localeCompare(requiredFunctionCode, undefined, {sensitivity: 'accent'}) === 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Confirm that the user associated with the session has been assigned the required role.
   *
   * @param requiredRoleCode The code uniquely identifying the required role.
   *
   * @return True if the user associated with the session has been assigned the required role or
   *         false otherwise.
   */
  hasRole(requiredRoleCode: string): boolean {
    for (const roleCode of this.roleCodes) {
      if (roleCode.localeCompare(requiredRoleCode, undefined, {sensitivity: 'accent'}) === 0) {
        return true;
      }
    }

    return false;
  }
}

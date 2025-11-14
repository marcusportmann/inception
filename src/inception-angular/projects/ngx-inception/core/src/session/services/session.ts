/*
 * Copyright Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Represents an active user session associated with an authenticated user.
 *
 * @author Marcus Portmann
 */
export class Session {
  private static readonly FUNCTION_PREFIX = 'FUNCTION_';

  private static readonly ROLE_PREFIX = 'ROLE_';

  /**
   * The base-64 encoded OAuth2 JWT access token for the user session.
   */
  public readonly accessToken: string;

  /**
   * The date and time the OAuth2 JWT access token for the user session will expire.
   */
  public readonly accessTokenExpiry?: Date;

  /**
   * The codes for the functions assigned to the user associated with the user session.
   */
  public readonly functionCodes: readonly string[];

  /**
   * The name of the user.
   */
  public readonly name: string;

  /**
   * The base-64 encoded OAuth2 refresh token for the user session.
   */
  public readonly refreshToken?: string;

  /**
   * The codes for the roles assigned to the user associated with the user session.
   */
  public readonly roleCodes: readonly string[];

  /**
   * The OAuth2 scopes for the user session.
   */
  public readonly scopes: readonly string[];

  /**
   * The ID for the selected tenant for the user session.
   */
  public tenantId?: string;

  /**
   * The IDs for tenants the user is associated with.
   */
  public readonly tenantIds: readonly string[];

  /**
   * The ID for the user directory the user is associated with.
   */
  public readonly userDirectoryId: string;

  /**
   * The username for the user the session is associated with.
   */
  public readonly username: string;

  private readonly functionCodeSet: Set<string>;

  // Normalized sets for efficient, case-insensitive lookups
  private readonly roleCodeSet: Set<string>;

  /**
   * Constructs a new Session.
   *
   * @param username          The username for the user the session is associated with.
   * @param userDirectoryId   The ID for the user directory the user is associated with.
   * @param name              The name of the user.
   * @param scopes            The OAuth2 scopes for the user session.
   * @param roleCodes         The codes for the roles assigned to the user.
   * @param functionCodes     The codes for the functions assigned to the user.
   * @param tenantIds         The IDs for the tenants the user is associated with.
   * @param accessToken       The base-64 encoded OAuth2 JWT access token for the user session.
   * @param accessTokenExpiry The date and time the OAuth2 JWT access token for the user session
   *                          will expire.
   * @param refreshToken      The base-64 encoded OAuth2 refresh token for the user session.
   */
  constructor(
    username: string,
    userDirectoryId: string,
    name: string,
    scopes: string[],
    roleCodes: string[],
    functionCodes: string[],
    tenantIds: string[],
    accessToken: string,
    accessTokenExpiry?: Date,
    refreshToken?: string
  ) {
    this.username = username;
    this.userDirectoryId = userDirectoryId;
    this.name = name;
    this.scopes = scopes ?? [];
    this.roleCodes = roleCodes ?? [];
    this.functionCodes = functionCodes ?? [];
    this.tenantIds = tenantIds ?? [];
    this.accessToken = accessToken;
    this.accessTokenExpiry = accessTokenExpiry;
    this.refreshToken = refreshToken;

    this.roleCodeSet = new Set(this.roleCodes.map((c) => c.toUpperCase()));
    this.functionCodeSet = new Set(this.functionCodes.map((c) => c.toUpperCase()));
  }

  /**
   * Returns true if the access token has expired (based on the local clock).
   */
  // noinspection JSUnusedGlobalSymbols
  get isAccessTokenExpired(): boolean {
    if (!this.accessTokenExpiry) {
      return false;
    }

    return this.accessTokenExpiry.getTime() <= Date.now();
  }

  /**
   * Returns true if the user associated with the session has the specified authority.
   *
   * Authorities are expected in the form:
   *  - "ROLE_<roleCode>"
   *  - "FUNCTION_<functionCode>"
   *
   * @param requiredAuthority The required authority.
   */
  hasAuthority(requiredAuthority: string | null | undefined): boolean {
    if (!requiredAuthority) {
      return false;
    }

    if (requiredAuthority.startsWith(Session.ROLE_PREFIX)) {
      const code = requiredAuthority.slice(Session.ROLE_PREFIX.length);
      return code.length > 0 && this.hasRole(code);
    }

    if (requiredAuthority.startsWith(Session.FUNCTION_PREFIX)) {
      const code = requiredAuthority.slice(Session.FUNCTION_PREFIX.length);
      return code.length > 0 && this.hasFunction(code);
    }

    return false;
  }

  /**
   * Returns true if the user associated with the session has been assigned the required function.
   *
   * @param requiredFunctionCode The code for the required function.
   */
  hasFunction(requiredFunctionCode: string | null | undefined): boolean {
    if (!requiredFunctionCode) {
      return false;
    }

    return this.functionCodeSet.has(requiredFunctionCode.toUpperCase());
  }

  /**
   * Returns true if the user associated with the session has been assigned the required role.
   *
   * @param requiredRoleCode The code for the required role.
   */
  hasRole(requiredRoleCode: string | null | undefined): boolean {
    if (!requiredRoleCode) {
      return false;
    }

    return this.roleCodeSet.has(requiredRoleCode.toUpperCase());
  }
}

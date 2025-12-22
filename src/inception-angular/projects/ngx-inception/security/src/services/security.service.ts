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

import { HttpClient, HttpErrorResponse, HttpParams, HttpResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, InvalidArgumentError,
  ProblemDetails, ResponseConverter, ServiceUnavailableError, SortDirection
} from 'ngx-inception/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { GenerateTokenRequest } from './generate-token-request';
import { Group } from './group';
import { GroupMember } from './group-member';
import { GroupMemberType } from './group-member-type';
import { GroupMembers } from './group-members';
import { GroupRole } from './group-role';
import { Groups } from './groups';
import { PasswordChange } from './password-change';
import { PasswordChangeReason } from './password-change-reason';
import { Policy } from './policy';
import { PolicySortBy } from './policy-sort-by';
import { PolicySummaries } from './policy-summaries';
import { Role } from './role';
import {
  AuthenticationFailedError, DuplicateGroupError, DuplicatePolicyError, DuplicateTenantError,
  DuplicateUserDirectoryError, DuplicateUserError, ExistingGroupMembersError, ExistingGroupsError,
  ExistingPasswordError, ExistingUsersError, GroupMemberNotFoundError, GroupNotFoundError,
  GroupRoleNotFoundError, InvalidPolicyDataError, InvalidSecurityCodeError, PolicyDataMismatchError,
  PolicyNotFoundError, RoleNotFoundError, TenantNotFoundError, TenantUserDirectoryNotFoundError,
  TokenNotFoundError, UserDirectoryNotFoundError, UserLockedError, UserNotFoundError
} from './security.service.errors';
import { Tenant } from './tenant';
import { TenantUserDirectory } from './tenant-user-directory';
import { Tenants } from './tenants';
import { Token } from './token';
import { TokenSortBy } from './token-sort-by';
import { TokenStatus } from './token-status';
import { TokenSummaries } from './token-summaries';
import { User } from './user';
import { UserDirectory } from './user-directory';
import { UserDirectoryCapabilities } from './user-directory-capabilities';
import { UserDirectorySummaries } from './user-directory-summaries';
import { UserDirectorySummary } from './user-directory-summary';
import { UserDirectoryType } from './user-directory-type';
import { UserSortBy } from './user-sort-by';
import { Users } from './users';

/**
 * The Security Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class SecurityService {
  private readonly config = inject<InceptionConfig>(INCEPTION_CONFIG);

  private readonly httpClient = inject(HttpClient);

  /**
   * Constructs a new SecurityService.
   */
  constructor() {
    console.log('Initializing the Security Service');
  }

  /**
   * Add the group member to the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   * @param memberType      The group member type.
   * @param memberName      The name of the group member.
   *
   * @return True if the group member was successfully added to the group or false otherwise.
   */
  addMemberToGroup(
    userDirectoryId: string,
    groupName: string,
    memberType: GroupMemberType,
    memberName: string
  ): Observable<boolean> {
    const groupMember = new GroupMember(userDirectoryId, groupName, memberType, memberName);

    return this.httpClient
      .post<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName +
          '/members',
        groupMember,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to add the group member to the group.'))
      );
  }

  /**
   * Add the role to the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   * @param roleCode        The code for the role.
   *
   * @return True if the role was successfully added to the group or false otherwise.
   */
  addRoleToGroup(
    userDirectoryId: string,
    groupName: string,
    roleCode: string
  ): Observable<boolean> {
    const groupRole = new GroupRole(userDirectoryId, groupName, roleCode);

    return this.httpClient
      .post<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName +
          '/roles',
        groupRole,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to add the role to the group.'))
      );
  }

  /**
   * Add the user directory to the tenant.
   *
   * @param tenantId        The ID for the tenant.
   * @param userDirectoryId The ID for the user directory.
   *
   * @return True if the user directory was successfully added to the tenant or false
   *         otherwise.
   */
  addUserDirectoryToTenant(tenantId: string, userDirectoryId: string): Observable<boolean> {
    const tenantUserDirectory = new TenantUserDirectory(tenantId, userDirectoryId);

    return this.httpClient
      .post<boolean>(
        this.config.apiUrlPrefix + '/security/tenants/' + tenantId + '/user-directories',
        tenantUserDirectory,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(
          SecurityService.handleApiError('Failed to add the user directory to the tenant.')
        )
      );
  }

  /**
   * Administratively change the password for the user.
   *
   * @param userDirectoryId      The ID for the user directory.
   * @param username             The username for the user.
   * @param newPassword          The new password.
   * @param expirePassword       Expire the user's password?
   * @param lockUser             Lock the user?
   * @param resetPasswordHistory Reset the user's password history?
   *
   * @return True if the user's password was changed successfully or false otherwise.
   */
  adminChangePassword(
    userDirectoryId: string,
    username: string,
    newPassword: string,
    expirePassword: boolean,
    lockUser: boolean,
    resetPasswordHistory: boolean
  ): Observable<boolean> {
    const passwordChange = new PasswordChange(
      PasswordChangeReason.Administrative,
      newPassword,
      undefined,
      undefined,
      expirePassword,
      lockUser,
      resetPasswordHistory
    );

    return this.httpClient
      .put<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/users/' +
          username +
          '/password',
        passwordChange,
        {
          observe: 'response'
        }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(
          SecurityService.handleApiError(
            'Failed to administratively change the password for the user.'
          )
        )
      );
  }

  /**
   * Change the password for the user.
   *
   * @param username    The username for the user.
   * @param password    The password for the user that is used to authorize the operation.
   * @param newPassword The new password.
   *
   * @return True if the user's password was changed successfully or false otherwise.
   */
  changePassword(username: string, password: string, newPassword: string): Observable<boolean> {
    const passwordChange = new PasswordChange(PasswordChangeReason.User, newPassword, password);

    return this.httpClient
      .put<boolean>(
        this.config.apiUrlPrefix + '/security/users/' + username + '/password',
        passwordChange,
        {
          observe: 'response'
        }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to change the password for the user.'))
      );
  }

  /**
   * Create the new group.
   *
   * @param group The group to create.
   *
   * @return True if the group was created successfully or false otherwise.
   */
  createGroup(group: Group): Observable<boolean> {
    return this.httpClient
      .post<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          group.userDirectoryId +
          '/groups',
        group,
        {
          observe: 'response'
        }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to create the group.'))
      );
  }

  /**
   * Create the new policy.
   *
   * @param policy The policy to create.
   *
   * @return True if the policy was created successfully or false otherwise.
   */
  createPolicy(policy: Policy): Observable<boolean> {
    const httpParams = new HttpParams();

    return this.httpClient
      .post<boolean>(this.config.apiUrlPrefix + '/security/policies', policy, {
        params: httpParams,
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to create the policy.'))
      );
  }

  /**
   * Create the new tenant.
   *
   * @param tenant        The tenant to create.
   * @param createUserDirectory Should a new internal user directory be created for the
   *                            tenant?
   *
   * @return True if the tenant was created successfully or false otherwise.
   */
  createTenant(tenant: Tenant, createUserDirectory?: boolean): Observable<boolean> {
    let httpParams = new HttpParams();
    httpParams = httpParams.append(
      'createUserDirectory',
      createUserDirectory === undefined ? 'false' : createUserDirectory ? 'true' : 'false'
    );

    return this.httpClient
      .post<boolean>(this.config.apiUrlPrefix + '/security/tenants', tenant, {
        params: httpParams,
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to create the tenant.'))
      );
  }

  /**
   * Create the new user.
   *
   * @param user            The user to create.
   * @param expiredPassword Create the user with its password expired?
   * @param userLocked      Create the user locked?
   *
   * @return True if the user was created successfully or false otherwise.
   */
  createUser(user: User, expiredPassword?: boolean, userLocked?: boolean): Observable<boolean> {
    let httpParams = new HttpParams();
    httpParams = httpParams.append(
      'expiredPassword',
      expiredPassword === undefined ? 'false' : expiredPassword ? 'true' : 'false'
    );
    httpParams = httpParams.append(
      'userLocked',
      userLocked === undefined ? 'false' : userLocked ? 'true' : 'false'
    );

    return this.httpClient
      .post<boolean>(
        this.config.apiUrlPrefix + '/security/user-directories/' + user.userDirectoryId + '/users',
        user,
        {
          params: httpParams,
          observe: 'response'
        }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to create the user.'))
      );
  }

  /**
   * Create the new user directory.
   *
   * @param userDirectory The user directory to create.
   *
   * @return True if the user directory was created successfully or false otherwise.
   */
  createUserDirectory(userDirectory: UserDirectory): Observable<boolean> {
    return this.httpClient
      .post<boolean>(this.config.apiUrlPrefix + '/security/user-directories', userDirectory, {
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to create the user directory.'))
      );
  }

  /**
   * Delete the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   *
   * @return True if the group was deleted or false otherwise.
   */
  deleteGroup(userDirectoryId: string, groupName: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to delete the group.'))
      );
  }

  /**
   * Delete the policy.
   *
   * @param policyId The ID for the policy.
   *
   * @return True if the policy was deleted or false otherwise.
   */
  deletePolicy(policyId: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(this.config.apiUrlPrefix + '/security/policies/' + policyId, {
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to delete the policy.'))
      );
  }

  /**
   * Delete the tenant.
   *
   * @param tenantId The ID for the tenant.
   *
   * @return True if the tenant was deleted or false otherwise.
   */
  deleteTenant(tenantId: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(this.config.apiUrlPrefix + '/security/tenants/' + tenantId, {
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to delete the tenant.'))
      );
  }

  /**
   * Delete the token.
   *
   * @param tokenId The ID for the token.
   *
   * @return True if the token was deleted or false otherwise.
   */
  deleteToken(tokenId: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(this.config.apiUrlPrefix + '/security/tokens/' + tokenId, {
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to delete the token.'))
      );
  }

  /**
   * Delete the user.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param username        The username for the user.
   *
   * @return True if the user was deleted or false otherwise.
   */
  deleteUser(userDirectoryId: string, username: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/users/' +
          username,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to delete the user.'))
      );
  }

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId The ID for the user directory.
   *
   * @return True if the user directory was deleted or false otherwise.
   */
  deleteUserDirectory(userDirectoryId: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(this.config.apiUrlPrefix + '/security/user-directories/' + userDirectoryId, {
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to delete the user directory.'))
      );
  }

  /**
   * Generate a token.
   *
   * @param generateTokenRequest The request to generate the token.
   *
   * @return The token.
   */
  generateToken(generateTokenRequest: GenerateTokenRequest): Observable<Token> {
    return this.httpClient
      .post<Token>(this.config.apiUrlPrefix + '/security/generate-token', generateTokenRequest)
      .pipe(catchError(SecurityService.handleApiError('Failed to generate the token.')));
  }

  /**
   * Retrieve the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   *
   * @return The group.
   */
  getGroup(userDirectoryId: string, groupName: string): Observable<Group> {
    return this.httpClient
      .get<Group>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName,
        { reportProgress: true }
      )
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the group.')));
  }

  /**
   * Retrieve all the group names.
   *
   * @param userDirectoryId The ID for the user directory.
   *
   * @return The group names.
   */
  getGroupNames(userDirectoryId: string): Observable<string[]> {
    return this.httpClient
      .get<string[]>(
        this.config.apiUrlPrefix + '/security/user-directories/' + userDirectoryId + '/group-names',
        {
          reportProgress: true
        }
      )
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the group names.')));
  }

  /**
   * Retrieve the names of the groups the user is a member of.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param username        The username for the user.
   *
   * @return The names of the groups the user is a member of.
   */
  getGroupNamesForUser(userDirectoryId: string, username: string): Observable<string[]> {
    return this.httpClient
      .get<
        string[]
      >(this.config.apiUrlPrefix + '/security/user-directories/' + userDirectoryId + '/users/' + username + '/group-names', { reportProgress: true })
      .pipe(
        catchError(
          SecurityService.handleApiError('Failed to retrieve the group names for the user.')
        )
      );
  }

  /**
   * Retrieve the groups.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param filter          The filter to apply to the groups.
   * @param sortDirection   The sort direction to apply to the groups.
   * @param pageIndex       The page index.
   * @param pageSize        The page size.
   *
   * @return The groups.
   */
  getGroups(
    userDirectoryId: string,
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<Groups> {
    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient
      .get<Groups>(
        this.config.apiUrlPrefix + '/security/user-directories/' + userDirectoryId + '/groups',
        {
          params,
          reportProgress: true
        }
      )
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the groups.')));
  }

  /**
   * Retrieve the members for the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   * @param filter          The filter to apply to the group members.
   * @param sortDirection   The sort direction to apply to the group members.
   * @param pageIndex       The page index.
   * @param pageSize        The page size.
   *
   * @return The members for the group.
   */
  getMembersForGroup(
    userDirectoryId: string,
    groupName: string,
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<GroupMembers> {
    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient
      .get<GroupMembers>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName +
          '/members',
        {
          params,
          reportProgress: true
        }
      )
      .pipe(
        catchError(SecurityService.handleApiError('Failed to retrieve the members for the group.'))
      );
  }

  /**
   * Retrieve all the policies.
   *
   * @return The policies.
   */
  getPolicies(): Observable<Policy[]> {
    return this.httpClient
      .get<Policy[]>(this.config.apiUrlPrefix + '/security/policies', {
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the policies.')));
  }

  /**
   * Retrieve the policy.
   *
   * @param policyId The ID for the policy.
   *
   * @return The policy.
   */
  getPolicy(policyId: string): Observable<Policy> {
    return this.httpClient
      .get<Policy>(this.config.apiUrlPrefix + '/security/policies/' + policyId, {
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the policy.')));
  }

  /**
   * Retrieve the name of the policy.
   *
   * @param policyId The ID for the policy.
   *
   * @return The name of the policy.
   */
  getPolicyName(policyId: string): Observable<string> {
    return this.httpClient
      .get<string>(this.config.apiUrlPrefix + '/security/policies/' + policyId + '/name', {
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the policy name.')));
  }

  /**
   * Retrieve the policy summaries.
   *
   * @param filter        The filter to apply to the policy summaries.
   * @param sortBy        The method used to sort the policy summaries e.g., by name.
   * @param sortDirection The sort direction to apply to the policy summaries.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   *
   * @return The policy summaries.
   */
  @ResponseConverter getPolicySummaries(
    filter?: string,
    sortBy?: PolicySortBy,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<PolicySummaries> {
    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortBy != null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient
      .get<PolicySummaries>(this.config.apiUrlPrefix + '/security/policy-summaries', {
        params,
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the policy summaries.')));
  }

  /**
   * Retrieve the codes for the roles that have been assigned to the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   *
   * @return The codes for the roles that have been assigned to the group.
   */
  getRoleCodesForGroup(userDirectoryId: string, groupName: string): Observable<string[]> {
    return this.httpClient
      .get<string[]>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName +
          '/role-codes',
        {
          reportProgress: true
        }
      )
      .pipe(
        catchError(
          SecurityService.handleApiError('Failed to retrieve the role codes for the group.')
        )
      );
  }

  /**
   * Retrieve all the roles.
   *
   * @return The roles.
   */
  getRoles(): Observable<Role[]> {
    return this.httpClient
      .get<Role[]>(this.config.apiUrlPrefix + '/security/roles', {
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the roles.')));
  }

  /**
   * Retrieve the roles that have been assigned to the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   *
   * @return The roles that have been assigned to the group.
   */
  getRolesForGroup(userDirectoryId: string, groupName: string): Observable<GroupRole[]> {
    return this.httpClient
      .get<GroupRole[]>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName +
          '/roles',
        {
          reportProgress: true
        }
      )
      .pipe(
        catchError(SecurityService.handleApiError('Failed to retrieve the roles for the group.'))
      );
  }

  /**
   * Retrieve the tenant.
   *
   * @param tenantId The ID for the tenant.
   *
   * @return The tenant.
   */
  getTenant(tenantId: string): Observable<Tenant> {
    return this.httpClient
      .get<Tenant>(this.config.apiUrlPrefix + '/security/tenants/' + tenantId, {
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the tenant.')));
  }

  /**
   * Retrieve the name of the tenant.
   *
   * @param tenantId The ID for the tenant.
   *
   * @return The name of the tenant.
   */
  getTenantName(tenantId: string): Observable<string> {
    return this.httpClient
      .get<string>(this.config.apiUrlPrefix + '/security/tenants/' + tenantId + '/name', {
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the tenant name.')));
  }

  /**
   * Retrieve the tenants.
   *
   * @param filter        The filter to apply to the tenants.
   * @param sortDirection The sort direction to apply to the tenants.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   *
   * @return The tenants.
   */
  getTenants(
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<Tenants> {
    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient
      .get<Tenants>(this.config.apiUrlPrefix + '/security/tenants', {
        params,
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the tenants.')));
  }

  /**
   * Retrieve the tenants the user directory is associated with.
   *
   * @param userDirectoryId The ID for the user directory.
   *
   * @return The tenants the user directory is associated with.
   */
  getTenantsForUserDirectory(userDirectoryId: string): Observable<Tenant[]> {
    return this.httpClient
      .get<
        Tenant[]
      >(this.config.apiUrlPrefix + '/security/user-directories/' + userDirectoryId + '/tenants', { reportProgress: true })
      .pipe(
        catchError(
          SecurityService.handleApiError(
            'Failed to retrieve the tenants associated with the user directory.'
          )
        )
      );
  }

  /**
   * Retrieve the token.
   *
   * @param tokenId The ID for the token.
   *
   * @return The token.
   */
  getToken(tokenId: string): Observable<Token> {
    return this.httpClient
      .get<Token>(this.config.apiUrlPrefix + '/security/tokens/' + tokenId, {
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the token.')));
  }

  /**
   * Retrieve the name of the token.
   *
   * @param tokenId The ID for the token.
   *
   * @return The name of the token.
   */
  getTokenName(tokenId: string): Observable<string> {
    return this.httpClient
      .get<string>(this.config.apiUrlPrefix + '/security/tokens/' + tokenId + '/name', {
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the token name.')));
  }

  /**
   * Retrieve the token summaries.
   *
   * @param status        The status filter to apply to the token summaries.
   * @param filter        The filter to apply to the token summaries.
   * @param sortBy        The method used to sort the token summaries e.g., by name.
   * @param sortDirection The sort direction to apply to the token summaries.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   *
   * @return The token summaries.
   */
  @ResponseConverter getTokenSummaries(
    status?: TokenStatus,
    filter?: string,
    sortBy?: TokenSortBy,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<TokenSummaries> {
    let params = new HttpParams();

    if (status != null) {
      params = params.append('status', String(status));
    }

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortBy != null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient
      .get<TokenSummaries>(this.config.apiUrlPrefix + '/security/token-summaries', {
        params,
        reportProgress: true
      })
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the token summaries.')));
  }

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param username        The username for the user.
   *
   * @return The user.
   */
  @ResponseConverter getUser(userDirectoryId: string, username: string): Observable<User> {
    return this.httpClient
      .get<User>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/users/' +
          username,
        { reportProgress: true }
      )
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the user.')));
  }

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId The ID for the user directory.
   *
   * @return The user directory.
   */
  getUserDirectory(userDirectoryId: string): Observable<UserDirectory> {
    return this.httpClient
      .get<UserDirectory>(
        this.config.apiUrlPrefix + '/security/user-directories/' + userDirectoryId,
        { reportProgress: true }
      )
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the user directory.')));
  }

  /**
   * Retrieve the capabilities for the user directory.
   *
   * @param userDirectoryId The ID for the user directory.
   *
   * @return The capabilities for the user directory.
   */
  getUserDirectoryCapabilities(userDirectoryId: string): Observable<UserDirectoryCapabilities> {
    return this.httpClient
      .get<UserDirectoryCapabilities>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/capabilities',
        { reportProgress: true }
      )
      .pipe(
        catchError(
          SecurityService.handleApiError(
            'Failed to retrieve the capabilities for the user directory.'
          )
        )
      );
  }

  /**
   * Retrieve the name of the user directory.
   *
   * @param userDirectoryId The ID for the user directory.
   *
   * @return The name of the user directory.
   */
  getUserDirectoryName(userDirectoryId: string): Observable<string> {
    return this.httpClient
      .get<string>(
        this.config.apiUrlPrefix + '/security/user-directories/' + userDirectoryId + '/name',
        { reportProgress: true }
      )
      .pipe(
        catchError(SecurityService.handleApiError('Failed to retrieve the user directory name.'))
      );
  }

  /**
   * Retrieve the user directory summaries.
   *
   * @param filter        The filter to apply to the user directory summaries.
   * @param sortDirection The sort direction to apply to the user directory summaries.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   *
   * @return The user directory summaries.
   */
  getUserDirectorySummaries(
    filter?: string,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<UserDirectorySummaries> {
    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient
      .get<UserDirectorySummaries>(
        this.config.apiUrlPrefix + '/security/user-directory-summaries',
        {
          params,
          reportProgress: true
        }
      )
      .pipe(
        catchError(
          SecurityService.handleApiError('Failed to retrieve the user directory summaries.')
        )
      );
  }

  /**
   * Retrieve the summaries for the user directories the tenant is associated with.
   *
   * @param tenantId The ID for the tenant.
   *
   * @return The summaries for the user directories the tenant is associated with.
   */
  getUserDirectorySummariesForTenant(tenantId: string): Observable<UserDirectorySummary[]> {
    return this.httpClient
      .get<
        UserDirectorySummary[]
      >(this.config.apiUrlPrefix + '/security/tenants/' + tenantId + '/user-directory-summaries', { reportProgress: true })
      .pipe(
        catchError(
          SecurityService.handleApiError(
            'Failed to retrieve the summaries for the user directories associated with the tenant.'
          )
        )
      );
  }

  /**
   * Retrieve the user directory type for the user directory.
   *
   * @param userDirectoryId The ID for the user directory.
   *
   * @return The user directory type for the user directory.
   */
  getUserDirectoryTypeForUserDirectory(userDirectoryId: string): Observable<UserDirectoryType> {
    return this.httpClient
      .get<UserDirectoryType>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/user-directory-type',
        { reportProgress: true }
      )
      .pipe(
        catchError(
          SecurityService.handleApiError(
            'Failed to retrieve the user directory type for the user directory.'
          )
        )
      );
  }

  /**
   * Retrieve the user directory types.
   *
   * @return The user directory types.
   */
  getUserDirectoryTypes(): Observable<UserDirectoryType[]> {
    return this.httpClient
      .get<
        UserDirectoryType[]
      >(this.config.apiUrlPrefix + '/security/user-directory-types', { reportProgress: true })
      .pipe(
        catchError(SecurityService.handleApiError('Failed to retrieve the user directory types.'))
      );
  }

  /**
   * Retrieve the name of the user.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param username        The username for the user.
   *
   * @return The name of the user.
   */
  getUserName(userDirectoryId: string, username: string): Observable<string> {
    return this.httpClient
      .get<string>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/users/' +
          username +
          '/name',
        { reportProgress: true }
      )
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the name of the user.')));
  }

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param filter          The filter to apply to the users.
   * @param sortBy          The method used to sort the users e.g., by name.
   * @param sortDirection   The sort direction to apply to the users.
   * @param pageIndex       The page index.
   * @param pageSize        The page size.
   *
   * @return The users.
   */
  @ResponseConverter getUsers(
    userDirectoryId: string,
    filter?: string,
    sortBy?: UserSortBy,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<Users> {
    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (sortBy != null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection != null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex != null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize != null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient
      .get<Users>(
        this.config.apiUrlPrefix + '/security/user-directories/' + userDirectoryId + '/users',
        {
          params,
          reportProgress: true
        }
      )
      .pipe(catchError(SecurityService.handleApiError('Failed to retrieve the users.')));
  }

  /**
   * Initiate the password reset process for the user.
   *
   * @param username         The username for the user.
   * @param resetPasswordUrl The reset password URL.
   *
   * @return True if the password reset process was initiated successfully or false otherwise.
   */
  initiatePasswordReset(username: string, resetPasswordUrl: string): Observable<boolean> {
    let params = new HttpParams();
    params = params.append('resetPasswordUrl', resetPasswordUrl);

    return this.httpClient
      .post<boolean>(
        this.config.apiUrlPrefix +
          '/security/users/' +
          username +
          '/reset-password',
        null,
        {
          observe: 'response',
          params
        }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to initiate the password reset.'))
      );
  }

  /**
   * Reinstate the token.
   *
   * @param tokenId The ID for the token.
   *
   * @return True if the token was reinstated or false otherwise.
   */
  reinstateToken(tokenId: string): Observable<boolean> {
    return this.httpClient
      .post<boolean>(
        this.config.apiUrlPrefix + '/security/tokens/' + tokenId + '/reinstate',
        null,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to reinstate the token.'))
      );
  }

  /**
   * Remove the group member from the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   * @param memberType      The group member type.
   * @param memberName      The name of the group member.
   *
   * @return True if the group member was successfully removed from the group or false otherwise.
   */
  removeMemberFromGroup(
    userDirectoryId: string,
    groupName: string,
    memberType: GroupMemberType,
    memberName: string
  ): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName +
          '/members/' +
          memberType +
          '/' +
          memberName,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(
          SecurityService.handleApiError('Failed to remove the group member from the group.')
        )
      );
  }

  /**
   * Remove the role from the group.
   *
   * @param userDirectoryId The ID for the user directory.
   * @param groupName       The name of the group.
   * @param roleCode        The code for the role.
   *
   * @return True if the role was successfully removed from the group or false otherwise.
   */
  removeRoleFromGroup(
    userDirectoryId: string,
    groupName: string,
    roleCode: string
  ): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          userDirectoryId +
          '/groups/' +
          groupName +
          '/roles/' +
          roleCode,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to remove the role from the group.'))
      );
  }

  /**
   * Remove the user directory from the tenant.
   *
   * @param tenantId        The ID for the tenant.
   * @param userDirectoryId The ID for the user directory.
   *
   * @return True if the user directory was successfully removed from the tenant or false
   *         otherwise.
   */
  removeUserDirectoryFromTenant(tenantId: string, userDirectoryId: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(
        this.config.apiUrlPrefix +
          '/security/tenants/' +
          tenantId +
          '/user-directories/' +
          userDirectoryId,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(
          SecurityService.handleApiError('Failed to remove the user directory from the tenant.')
        )
      );
  }

  /**
   * Reset the password for the user.
   *
   * @param username     The username for the user.
   * @param newPassword  The new password.
   * @param securityCode The security code.
   *
   * @return True if the user's password was reset successfully or false otherwise.
   */
  resetPassword(username: string, newPassword: string, securityCode: string): Observable<boolean> {
    const passwordChange = new PasswordChange(
      PasswordChangeReason.Reset,
      newPassword,
      undefined,
      securityCode
    );

    return this.httpClient
      .put<boolean>(
        this.config.apiUrlPrefix + '/security/users/' + username + '/password',
        passwordChange,
        {
          observe: 'response'
        }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to reset the password for the user.'))
      );
  }

  /**
   * Revoke the token.
   *
   * @param tokenId The ID for the token.
   *
   * @return True if the token was revoked or false otherwise.
   */
  revokeToken(tokenId: string): Observable<boolean> {
    return this.httpClient
      .post<boolean>(this.config.apiUrlPrefix + '/security/tokens/' + tokenId + '/revoke', null, {
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to revoke the token.'))
      );
  }

  /**
   * Update the group.
   *
   * @param group The group to update.
   *
   * @return True if the group was updated successfully or false otherwise.
   */
  updateGroup(group: Group): Observable<boolean> {
    return this.httpClient
      .put<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          group.userDirectoryId +
          '/groups/' +
          group.name,
        group,
        {
          observe: 'response'
        }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to update the group.'))
      );
  }

  /**
   * Update the policy.
   *
   * @param policy The policy to update.
   *
   * @return True if the policy was updated successfully or false otherwise.
   */
  updatePolicy(policy: Policy): Observable<boolean> {
    return this.httpClient
      .put<boolean>(this.config.apiUrlPrefix + '/security/policies/' + policy.id, policy, {
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to update the policy.'))
      );
  }

  /**
   * Update the tenant.
   *
   * @param tenant The tenant to update.
   *
   * @return True if the tenant was updated successfully or false otherwise.
   */
  updateTenant(tenant: Tenant): Observable<boolean> {
    return this.httpClient
      .put<boolean>(this.config.apiUrlPrefix + '/security/tenants/' + tenant.id, tenant, {
        observe: 'response'
      })
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to update the tenant.'))
      );
  }

  /**
   * Update the user.
   *
   * @param user           The user to update.
   * @param expirePassword Expire the user's password?
   * @param lockUser       Lock the user?
   *
   * @return True if the user was updated successfully or false otherwise.
   */
  updateUser(user: User, expirePassword?: boolean, lockUser?: boolean): Observable<boolean> {
    let httpParams = new HttpParams();
    httpParams = httpParams.append(
      'expirePassword',
      expirePassword === undefined ? 'false' : expirePassword ? 'true' : 'false'
    );
    httpParams = httpParams.append(
      'lockUser',
      lockUser === undefined ? 'false' : lockUser ? 'true' : 'false'
    );

    return this.httpClient
      .put<boolean>(
        this.config.apiUrlPrefix +
          '/security/user-directories/' +
          user.userDirectoryId +
          '/users/' +
          user.username,
        user,
        {
          params: httpParams,
          observe: 'response'
        }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to update the user.'))
      );
  }

  /**
   * Update the user directory.
   *
   * @param userDirectory The user directory to update.
   *
   * @return True if the user directory was updated successfully or false otherwise.
   */
  updateUserDirectory(userDirectory: UserDirectory): Observable<boolean> {
    return this.httpClient
      .put<boolean>(
        this.config.apiUrlPrefix + '/security/user-directories/' + userDirectory.id,
        userDirectory,
        { observe: 'response' }
      )
      .pipe(
        map(SecurityService.isResponse204),
        catchError(SecurityService.handleApiError('Failed to update the user directory.'))
      );
  }

  private static handleApiError(defaultMessage: string) {
    return (httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, AuthenticationFailedError.TYPE)) {
        return throwError(() => new AuthenticationFailedError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateGroupError.TYPE)) {
        return throwError(() => new DuplicateGroupError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicatePolicyError.TYPE)) {
        return throwError(() => new DuplicatePolicyError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateTenantError.TYPE)) {
        return throwError(() => new DuplicateTenantError(httpErrorResponse));
      } else if (
        ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateUserDirectoryError.TYPE)
      ) {
        return throwError(() => new DuplicateUserDirectoryError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, DuplicateUserError.TYPE)) {
        return throwError(() => new DuplicateUserError(httpErrorResponse));
      } else if (
        ProblemDetails.isProblemDetails(httpErrorResponse, ExistingGroupMembersError.TYPE)
      ) {
        return throwError(() => new ExistingGroupMembersError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, ExistingGroupsError.TYPE)) {
        return throwError(() => new ExistingGroupsError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, ExistingPasswordError.TYPE)) {
        return throwError(() => new ExistingPasswordError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, ExistingUsersError.TYPE)) {
        return throwError(() => new ExistingUsersError(httpErrorResponse));
      } else if (
        ProblemDetails.isProblemDetails(httpErrorResponse, GroupMemberNotFoundError.TYPE)
      ) {
        return throwError(() => new GroupMemberNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, GroupNotFoundError.TYPE)) {
        return throwError(() => new GroupNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, GroupRoleNotFoundError.TYPE)) {
        return throwError(() => new GroupRoleNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, InvalidPolicyDataError.TYPE)) {
        return throwError(() => new InvalidPolicyDataError(httpErrorResponse));
      } else if (
        ProblemDetails.isProblemDetails(httpErrorResponse, InvalidSecurityCodeError.TYPE)
      ) {
        return throwError(() => new InvalidSecurityCodeError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, PolicyDataMismatchError.TYPE)) {
        return throwError(() => new PolicyDataMismatchError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, PolicyNotFoundError.TYPE)) {
        return throwError(() => new PolicyNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, RoleNotFoundError.TYPE)) {
        return throwError(() => new RoleNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, TenantNotFoundError.TYPE)) {
        return throwError(() => new TenantNotFoundError(httpErrorResponse));
      } else if (
        ProblemDetails.isProblemDetails(httpErrorResponse, TenantUserDirectoryNotFoundError.TYPE)
      ) {
        return throwError(() => new TenantUserDirectoryNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, TokenNotFoundError.TYPE)) {
        return throwError(() => new TokenNotFoundError(httpErrorResponse));
      } else if (
        ProblemDetails.isProblemDetails(httpErrorResponse, UserDirectoryNotFoundError.TYPE)
      ) {
        return throwError(() => new UserDirectoryNotFoundError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, UserLockedError.TYPE)) {
        return throwError(() => new UserLockedError(httpErrorResponse));
      } else if (ProblemDetails.isProblemDetails(httpErrorResponse, UserNotFoundError.TYPE)) {
        return throwError(() => new UserNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError(defaultMessage, httpErrorResponse));
    };
  }

  /**
   * Helper method to check if the response status is 204.
   *
   * @param httpResponse The HTTP response.
   * @return True if the status is 204, otherwise false.
   */
  private static isResponse204(httpResponse: HttpResponse<boolean>): boolean {
    return httpResponse.status === 204;
  }
}

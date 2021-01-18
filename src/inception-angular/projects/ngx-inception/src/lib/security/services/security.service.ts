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

import {Inject, Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of, Subject, throwError, timer} from 'rxjs';
import {catchError, map, mergeMap, switchMap} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpParams, HttpResponse} from '@angular/common/http';
import {Tenant} from './tenant';
import {
  AuthenticationFailedError,
  DuplicateGroupError,
  DuplicateTenantError,
  DuplicateUserDirectoryError,
  DuplicateUserError,
  ExistingGroupMemberError,
  ExistingGroupMembersError,
  ExistingGroupRoleError,
  ExistingTenantUserDirectoryError,
  ExistingPasswordError,
  GroupMemberNotFoundError,
  GroupNotFoundError,
  GroupRoleNotFoundError,
  InvalidSecurityCodeError,
  LoginError,
  TenantNotFoundError,
  TenantUserDirectoryNotFoundError,
  PasswordExpiredError,
  SecurityServiceError,
  UserDirectoryNotFoundError,
  UserLockedError,
  UserNotFoundError
} from './security.service.errors';
import {CommunicationError} from '../../core/errors/communication-error';
import {ApiError} from '../../core/errors/api-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {SortDirection} from '../../core/sorting/sort-direction';
import {Tenants} from './tenants';
import {Users} from './users';
import {User} from './user';
import {UserDirectorySummary} from './user-directory-summary';
import {UserSortBy} from './user-sort-by';
import {UserDirectorySummaries} from './user-directory-summaries';
import {UserDirectory} from './user-directory';
import {UserDirectoryType} from './user-directory-type';
import {Group} from './group';
import {Groups} from './groups';
import {GroupMember} from './group-member';
import {GroupMembers} from './group-members';
import {GroupMemberType} from './group-member-type';
import {UserDirectoryCapabilities} from './user-directory-capabilities';
import {PasswordChangeReason} from './password-change-reason';
import {PasswordChange} from './password-change';
import {Role} from './role';
import {GroupRole} from './group-role';
import {TenantUserDirectory} from './tenant-user-directory';
import {INCEPTION_CONFIG, InceptionConfig} from '../../inception-config';
import {Session} from './session';
import {TokenResponse} from './token-response';
import {JwtHelperService} from '@auth0/angular-jwt';

/**
 * The Security Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  /**
   * The current active session.
   */
  session$: Subject<Session | null> = new BehaviorSubject<Session | null>(null);

  /**
   * Constructs a new SecurityService.
   *
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig, private httpClient: HttpClient) {
    console.log('Initializing the Security Service');

    // Start the session refresher
    timer(0, 10000).pipe(switchMap(() => this.refreshSession()))
    .subscribe((refreshedSession: Session | null) => {
      if (refreshedSession) {
        console.log('Successfully refreshed session: ', refreshedSession);
      }
    });
  }

  /**
   * Add the group member to the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   * @param memberType      The group member type.
   * @param memberName      The name of the group member.
   *
   * @return True if the group member was successfully added to the group or false otherwise.
   */
  addMemberToGroup(userDirectoryId: string, groupName: string, memberType: GroupMemberType,
                   memberName: string): Observable<boolean> {
    const groupMember = new GroupMember(userDirectoryId, groupName, memberType, memberName);

    return this.httpClient.post<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/members', groupMember, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else if (apiError.code === 'ExistingGroupMemberError') {
          return throwError(new ExistingGroupMemberError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to add the group member to the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Add the role to the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   * @param roleCode        The code for the role.
   *
   * @return True if the role was successfully added to the group or false otherwise.
   */
  addRoleToGroup(userDirectoryId: string, groupName: string, roleCode: string): Observable<boolean> {
    const groupRole = new GroupRole(userDirectoryId, groupName, roleCode);

    return this.httpClient.post<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/roles', groupRole, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else if (apiError.code === 'RoleNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else if (apiError.code === 'ExistingGroupRoleError') {
          return throwError(new ExistingGroupRoleError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to add the role to the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Add the user directory to the tenant.
   *
   * @param tenantId        The Universally Unique Identifier (UUID) for the tenant.
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return True if the user directory was successfully added to the tenant or false
   *         otherwise.
   */
  addUserDirectoryToTenant(tenantId: string, userDirectoryId: string): Observable<boolean> {
    const tenantUserDirectory = new TenantUserDirectory(tenantId, userDirectoryId);

    return this.httpClient.post<boolean>(
      this.config.securityApiUrlPrefix + '/tenants/' + tenantId + '/user-directories',
      tenantUserDirectory, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'TenantNotFoundError') {
          return throwError(new TenantNotFoundError(apiError));
        } else if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'ExistingTenantUserDirectoryError') {
          return throwError(new ExistingTenantUserDirectoryError(apiError));
        } else {
          return throwError(
            new SecurityServiceError('Failed to add the user directory to the tenant.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Administratively change the password for the user.
   *
   * @param userDirectoryId      The Universally Unique Identifier (UUID) for the user directory.
   * @param username             The username for the user.
   * @param newPassword          The new password.
   * @param expirePassword       Expire the user's password?
   * @param lockUser             Lock the user?
   * @param resetPasswordHistory Reset the user's password history?
   *
   * @return True if the user's password was changed successfully or false otherwise.
   */
  adminChangePassword(userDirectoryId: string, username: string, newPassword: string, expirePassword: boolean,
                      lockUser: boolean, resetPasswordHistory: boolean): Observable<boolean> {
    const passwordChange = new PasswordChange(PasswordChangeReason.Administrative, newPassword, undefined, undefined,
      expirePassword, lockUser, resetPasswordHistory);

    return this.httpClient.put<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username) + '/password', passwordChange, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else {
          return throwError(
            new SecurityServiceError('Failed to administratively change the password for the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Change the password for the user.
   *
   * @param username    The username for the user.
   * @param password    The password for the user that is used to authorise the operation.
   * @param newPassword The new password.
   *
   * @return True if the user's password was changed successfully or false otherwise.
   */
  changePassword(username: string, password: string, newPassword: string): Observable<boolean> {
    const passwordChange = new PasswordChange(PasswordChangeReason.User, newPassword, password);

    return this.httpClient.put<boolean>(
      this.config.securityApiUrlPrefix + '/users/' + encodeURIComponent(username) + '/password', passwordChange, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else if (apiError.code === 'AuthenticationFailedError') {
          return throwError(new AuthenticationFailedError(apiError));
        } else if (apiError.code === 'ExistingPasswordError') {
          return throwError(new ExistingPasswordError(apiError));
        } else if (apiError.code === 'UserLockedError') {
          return throwError(new UserLockedError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to change the password for the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Create the new group.
   *
   * @param group The group to create.
   *
   * @return True if the group was created successfully or false otherwise.
   */
  createGroup(group: Group): Observable<boolean> {
    return this.httpClient.post<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + group.userDirectoryId + '/groups', group, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'DuplicateGroupError') {
          return throwError(new DuplicateGroupError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to create the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
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
    httpParams = httpParams.append('createUserDirectory',
      createUserDirectory === undefined ? 'false' : (createUserDirectory ? 'true' : 'false'));

    return this.httpClient.post<boolean>(this.config.securityApiUrlPrefix + '/tenants', tenant, {
      params: httpParams,
      observe: 'response'
    }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'DuplicateTenantError') {
          return throwError(new DuplicateTenantError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to create the tenant.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
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
    httpParams = httpParams.append('expiredPassword',
      expiredPassword === undefined ? 'false' : (expiredPassword ? 'true' : 'false'));
    httpParams = httpParams.append('userLocked', userLocked === undefined ? 'false' : (userLocked ? 'true' : 'false'));

    return this.httpClient.post<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + user.userDirectoryId + '/users', user, {
        params: httpParams,
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'DuplicateUserError') {
          return throwError(new DuplicateUserError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to create the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Create the new user directory.
   *
   * @param userDirectory The user directory to create.
   *
   * @return True if the user directory was created successfully or false otherwise.
   */
  createUserDirectory(userDirectory: UserDirectory): Observable<boolean> {
    return this.httpClient.post<boolean>(this.config.securityApiUrlPrefix + '/user-directories', userDirectory,
      {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'DuplicateUserDirectoryError') {
          return throwError(new DuplicateUserDirectoryError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to create the user directory.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Delete the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   *
   * @return True if the group was deleted or false otherwise.
   */
  deleteGroup(userDirectoryId: string, groupName: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName), {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else if (apiError.code === 'ExistingGroupMembersError') {
          return throwError(new ExistingGroupMembersError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to delete the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Delete the tenant.
   *
   * @param tenantId The Universally Unique Identifier (UUID) for the tenant.
   *
   * @return True if the tenant was deleted or false otherwise.
   */
  deleteTenant(tenantId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.config.securityApiUrlPrefix + '/tenants/' + tenantId,
      {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'TenantNotFoundError') {
          return throwError(new TenantNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to delete the tenant.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Delete the user.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param username        The username for the user.
   *
   * @return True if the user was deleted or false otherwise.
   */
  deleteUser(userDirectoryId: string, username: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username), {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to delete the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return True if the user directory was deleted or false otherwise.
   */
  deleteUserDirectory(userDirectoryId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId,
      {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to delete the user directory.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   *
   * @return The group.
   */
  getGroup(userDirectoryId: string, groupName: string): Observable<Group> {
    return this.httpClient.get<Group>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName), {reportProgress: true}).pipe(map((group: Group) => {
      return group;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve all the group names.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return The group names.
   */
  getGroupNames(userDirectoryId: string): Observable<string[]> {
    return this.httpClient.get<string[]>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/group-names', {
        reportProgress: true,
      }).pipe(map((groupNames: string[]) => {
      return groupNames;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the group names.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the names of the groups the user is a member of.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param username        The username for the user.
   *
   * @return The names of the groups the user is a member of.
   */
  getGroupNamesForUser(userDirectoryId: string, username: string): Observable<string[]> {
    return this.httpClient.get<string[]>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username) + '/group-names', {reportProgress: true})
    .pipe(map((groupNames: string[]) => {
      return groupNames;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the group names for the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the groups.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param filter          The optional filter to apply to the groups.
   * @param sortDirection   The optional sort direction to apply to the groups.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   *
   * @return The groups.
   */
  getGroups(userDirectoryId: string, filter?: string, sortDirection?: SortDirection, pageIndex?: number,
            pageSize?: number): Observable<Groups> {

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

    return this.httpClient.get<Groups>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups', {
        params,
        reportProgress: true,
      }).pipe(map((groups: Groups) => {
      return groups;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the groups.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the tenant.
   *
   * @param tenantId The Universally Unique Identifier (UUID) for the tenant.
   *
   * @return The tenant.
   */
  getTenant(tenantId: string): Observable<Tenant> {
    return this.httpClient.get<Tenant>(this.config.securityApiUrlPrefix + '/tenants/' + tenantId,
      {reportProgress: true})
    .pipe(map((tenant: Tenant) => {
      return tenant;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'TenantNotFoundError') {
          return throwError(new TenantNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the tenant.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the members for the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   * @param filter          The optional filter to apply to the group members.
   * @param sortDirection   The optional sort direction to apply to the group members.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   *
   * @return The members for the group.
   */
  getMembersForGroup(userDirectoryId: string, groupName: string, filter?: string, sortDirection?: SortDirection,
                     pageIndex?: number, pageSize?: number): Observable<GroupMembers> {

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

    return this.httpClient.get<GroupMembers>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/members', {
        params,
        reportProgress: true,
      }).pipe(map((groupMembers: GroupMembers) => {
      return groupMembers;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the members for the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the codes for the roles that have been assigned to the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   *
   * @return The codes for the roles that have been assigned to the group.
   */
  getRoleCodesForGroup(userDirectoryId: string, groupName: string): Observable<string[]> {
    return this.httpClient.get<string[]>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/role-codes', {
        reportProgress: true,
      }).pipe(map((roleCodes: string[]) => {
      return roleCodes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the role codes for the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve all the roles.
   *
   * @return The roles.
   */
  getRoles(): Observable<Role[]> {
    return this.httpClient.get<Role[]>(this.config.securityApiUrlPrefix + '/roles', {
      reportProgress: true,
    }).pipe(map((roles: Role[]) => {
      return roles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new SecurityServiceError('Failed to retrieve the roles.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the roles that have been assigned to the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   *
   * @return The roles that have been assigned to the group.
   */
  getRolesForGroup(userDirectoryId: string, groupName: string): Observable<GroupRole[]> {
    return this.httpClient.get<GroupRole[]>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/roles', {
        reportProgress: true,
      }).pipe(map((groupRoles: GroupRole[]) => {
      return groupRoles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the roles for the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the name of the tenant.
   *
   * @param tenantId The Universally Unique Identifier (UUID) for the tenant.
   *
   * @return The name of the tenant.
   */
  getTenantName(tenantId: string): Observable<string> {
    return this.httpClient.get<string>(this.config.securityApiUrlPrefix + '/tenants/' + tenantId + '/name',
      {reportProgress: true}).pipe(map((tenantName: string) => {
      return tenantName;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'TenantNotFoundError') {
          return throwError(new TenantNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the tenant name.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the tenants.
   *
   * @param filter        The optional filter to apply to the tenants.
   * @param sortDirection The optional sort direction to apply to the tenants.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The tenants.
   */
  getTenants(filter?: string, sortDirection?: SortDirection, pageIndex?: number,
             pageSize?: number): Observable<Tenants> {

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

    return this.httpClient.get<Tenants>(this.config.securityApiUrlPrefix + '/tenants', {
      params,
      reportProgress: true,
    }).pipe(map((tenants: Tenants) => {
      return tenants;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new SecurityServiceError('Failed to retrieve the tenants.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the tenants the user directory is associated with.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return The tenants the user directory is associated with.
   */
  getTenantsForUserDirectory(userDirectoryId: string): Observable<Tenant[]> {
    return this.httpClient.get<Tenant[]>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/tenants',
      {reportProgress: true})
    .pipe(map((tenants: Tenant[]) => {
      return tenants;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(
            new SecurityServiceError('Failed to retrieve the tenants associated with the user directory.',
              apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param username        The username for the user.
   *
   * @return The user.
   */
  getUser(userDirectoryId: string, username: string): Observable<User> {
    return this.httpClient.get<User>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username), {reportProgress: true}).pipe(map((user: User) => {
      return user;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return The user directory.
   */
  getUserDirectory(userDirectoryId: string): Observable<UserDirectory> {
    return this.httpClient.get<UserDirectory>(this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId,
      {reportProgress: true})
    .pipe(map((userDirectory: UserDirectory) => {
      return userDirectory;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the user directory.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the capabilities for the user directory.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return The capabilities for the user directory.
   */
  getUserDirectoryCapabilities(userDirectoryId: string): Observable<UserDirectoryCapabilities> {
    return this.httpClient.get<UserDirectoryCapabilities>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/capabilities',
      {reportProgress: true})
    .pipe(map((capabilities: UserDirectoryCapabilities) => {
      return capabilities;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(
            new SecurityServiceError('Failed to retrieve the capabilities for the user directory.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the name of the user directory.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return The name of the user directory.
   */
  getUserDirectoryName(userDirectoryId: string): Observable<string> {
    return this.httpClient.get<string>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/name', {reportProgress: true})
    .pipe(map((userDirectoryName: string) => {
      return userDirectoryName;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the user directory name.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the summaries for the user directories the tenant is associated with.
   *
   * @param tenantId The Universally Unique Identifier (UUID) for the tenant.
   *
   * @return The summaries for the user directories the tenant is associated with.
   */
  getUserDirectorySummariesForTenant(tenantId: string): Observable<UserDirectorySummary[]> {
    return this.httpClient.get<UserDirectorySummary[]>(
      this.config.securityApiUrlPrefix + '/tenants/' + tenantId + '/user-directory-summaries',
      {reportProgress: true})
    .pipe(map((codeCategories: UserDirectorySummary[]) => {
      return codeCategories;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'TenantNotFoundError') {
          return throwError(new TenantNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError(
            'Failed to retrieve the summaries for the user directories associated with the tenant.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the user directory type for the user directory.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return The user directory type for the user directory.
   */
  getUserDirectoryTypeForUserDirectory(userDirectoryId: string): Observable<UserDirectoryType> {
    return this.httpClient.get<UserDirectoryType>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/user-directory-type',
      {reportProgress: true})
    .pipe(map((userDirectoryType: UserDirectoryType) => {
      return userDirectoryType;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(
            new SecurityServiceError('Failed to retrieve the user directory type for the user directory.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the user directory types.
   *
   * @return The user directory types.
   */
  getUserDirectoryTypes(): Observable<UserDirectoryType[]> {
    return this.httpClient.get<UserDirectoryType[]>(this.config.securityApiUrlPrefix + '/user-directory-types',
      {reportProgress: true})
    .pipe(map((userDirectoryTypes: UserDirectoryType[]) => {
      return userDirectoryTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new SecurityServiceError('Failed to retrieve the user directory types.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the name of the user.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param username        The username for the user.
   *
   * @return The name of the user.
   */
  getUserName(userDirectoryId: string, username: string): Observable<string> {
    return this.httpClient.get<string>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username) + '/name', {reportProgress: true})
    .pipe(map((userName: string) => {
      return userName;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the name of the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param filter          The optional filter to apply to the users.
   * @param sortBy          The optional method used to sort the users e.g. by name.
   * @param sortDirection   The optional sort direction to apply to the users.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   *
   * @return The users.
   */
  getUsers(userDirectoryId: string, filter?: string, sortBy?: UserSortBy, sortDirection?: SortDirection,
           pageIndex?: number, pageSize?: number): Observable<Users> {

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

    return this.httpClient.get<Users>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/users', {
        params,
        reportProgress: true,
      }).pipe(map((users: Users) => {
      return users;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to retrieve the users.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve the user directory summaries.
   *
   * @param filter        The optional filter to apply to the user directory summaries.
   * @param sortDirection The optional sort direction to apply to the user directory summaries.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The user directory summaries.
   */
  getUserDirectorySummaries(filter?: string, sortDirection?: SortDirection, pageIndex?: number,
                            pageSize?: number): Observable<UserDirectorySummaries> {

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

    return this.httpClient.get<UserDirectorySummaries>(this.config.securityApiUrlPrefix + '/user-directory-summaries', {
      params,
      reportProgress: true,
    }).pipe(map((userDirectorySummaries: UserDirectorySummaries) => {
      return userDirectorySummaries;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new SecurityServiceError('Failed to retrieve the user directory summaries.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
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

    return this.httpClient.post<boolean>(
      this.config.securityApiUrlPrefix + '/users/' + encodeURIComponent(username) + '/reset-password', null, {
        observe: 'response',
        params,
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to initiate the password reset.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Logon.
   *
   * @param username The username.
   * @param password The password.
   *
   * @return The current active session.
   */
  login(username: string, password: string): Observable<Session | null> {

    // TODO: REMOVE HARD CODED SCOPE AND CLIENT ID -- MARCUS

    const body = new HttpParams()
    .set('grant_type', 'password')
    .set('username', username)
    .set('password', password);
    // .set('scope', 'inception-sample')
    // .set('client_id', 'inception-sample');

    const options = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}};

    return this.httpClient.post<TokenResponse>(this.config.oauthTokenUrl, body.toString(), options)
    .pipe(mergeMap((tokenResponse: TokenResponse) => {
      this.session$.next(
        SecurityService.createSessionFromAccessToken(tokenResponse.access_token, tokenResponse.refresh_token));

      return this.session$;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {

      if (httpErrorResponse.status === 400) {
        if (httpErrorResponse.error && (httpErrorResponse.error.error === 'invalid_grant') &&
          httpErrorResponse.error.error_description) {
          if (httpErrorResponse.error.error_description.includes('Bad credentials')) {
            return throwError(new LoginError(httpErrorResponse));
          } else if (httpErrorResponse.error.error_description.includes('User locked')) {
            return throwError(new UserLockedError(httpErrorResponse));
          } else if (httpErrorResponse.error.error_description.includes('Credentials expired')) {
            return throwError(new PasswordExpiredError(httpErrorResponse));
          }
        }

        return throwError(new LoginError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Logout the current active session if one exists.
   */
  logout(): void {
    this.session$.next(null);
  }

  /**
   * Remove the group member from the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   * @param memberType      The group member type.
   * @param memberName      The name of the group member.
   *
   * @return True if the group member was successfully removed from the group or false otherwise.
   */
  removeMemberFromGroup(userDirectoryId: string, groupName: string, memberType: GroupMemberType,
                        memberName: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/members/' + memberType + '/' + encodeURIComponent(memberName),
      {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else if (apiError.code === 'GroupMemberNotFoundError') {
          return throwError(new GroupMemberNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to remove the group member from the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Remove the role from the group.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   * @param groupName       The name of the group.
   * @param roleCode        The code for the role.
   *
   * @return True if the role was successfully removed from the group or false otherwise.
   */
  removeRoleFromGroup(userDirectoryId: string, groupName: string, roleCode: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/roles/' + roleCode, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else if (apiError.code === 'GroupRoleNotFoundError') {
          return throwError(new GroupRoleNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to remove the role from the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Remove the user directory from the tenant.
   *
   * @param tenantId        The Universally Unique Identifier (UUID) for the tenant.
   * @param userDirectoryId The Universally Unique Identifier (UUID) for the user directory.
   *
   * @return True if the user directory was successfully removed from the tenant or false
   *         otherwise.
   */
  removeUserDirectoryFromTenant(tenantId: string, userDirectoryId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.securityApiUrlPrefix + '/tenants/' + tenantId + '/user-directories/' + userDirectoryId,
      {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'TenantNotFoundError') {
          return throwError(new TenantNotFoundError(apiError));
        } else if (apiError.code === 'TenantUserDirectoryNotFoundError') {
          return throwError(new TenantUserDirectoryNotFoundError(apiError));
        } else {
          return throwError(
            new SecurityServiceError('Failed to remove the user directory from the tenant.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
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
    const passwordChange = new PasswordChange(PasswordChangeReason.Reset, newPassword, undefined, securityCode);

    return this.httpClient.put<boolean>(
      this.config.securityApiUrlPrefix + '/users/' + encodeURIComponent(username) + '/password', passwordChange, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else if (apiError.code === 'InvalidSecurityCodeError') {
          return throwError(new InvalidSecurityCodeError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to reset the password for the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Update the group.
   *
   * @param group The group to update.
   *
   * @return True if the group was updated successfully or false otherwise.
   */
  updateGroup(group: Group): Observable<boolean> {
    return this.httpClient.put<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + group.userDirectoryId + '/groups/' +
      encodeURIComponent(group.name), group, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to update the group.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Update the tenant.
   *
   * @param tenant The tenant to update.
   *
   * @return True if the tenant was updated successfully or false otherwise.
   */
  updateTenant(tenant: Tenant): Observable<boolean> {
    return this.httpClient.put<boolean>(this.config.securityApiUrlPrefix + '/tenants/' + tenant.id,
      tenant, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'TenantNotFoundError') {
          return throwError(new TenantNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to update the tenant.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
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
    httpParams =
      httpParams.append('expirePassword', expirePassword === undefined ? 'false' : (expirePassword ? 'true' : 'false'));
    httpParams = httpParams.append('lockUser', lockUser === undefined ? 'false' : (lockUser ? 'true' : 'false'));

    return this.httpClient.put<boolean>(
      this.config.securityApiUrlPrefix + '/user-directories/' + user.userDirectoryId + '/users/' +
      encodeURIComponent(user.username), user, {
        params: httpParams,
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to update the user.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Update the user directory.
   *
   * @param userDirectory The user directory to update.
   *
   * @return True if the user directory was updated successfully or false otherwise.
   */
  updateUserDirectory(userDirectory: UserDirectory): Observable<boolean> {
    return this.httpClient.put<boolean>(this.config.securityApiUrlPrefix + '/user-directories/' + userDirectory.id,
      userDirectory, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(apiError));
        } else {
          return throwError(new SecurityServiceError('Failed to update the user directory.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  private static createSessionFromAccessToken(accessToken: string, refreshToken: string | undefined): Session {
    const helper = new JwtHelperService();

    // tslint:disable-next-line
    const token: any = helper.decodeToken(accessToken);

    const accessTokenExpiry: Date | null = helper.getTokenExpirationDate(accessToken);

    return new Session((!!token.sub) ? token.sub : '',
      (!!token.user_directory_id) ? token.user_directory_id : '',
      (!!token.name) ? token.name : '',
      (!!token.scope) ? token.scope.split(' ') : [],
      (!!token.roles) ? token.roles : [],
      (!!token.functions) ? token.functions : [],
      (!!token.tenants) ? token.tenants : [],
      accessToken,
      (!!accessTokenExpiry) ? accessTokenExpiry : undefined, refreshToken);
  }

  private refreshSession(): Observable<Session | null> {
    return this.session$.pipe(mergeMap((currentSession: Session | null) => {
      if (currentSession) {
        const selectedTenant = currentSession.tenant;

        /*
         * If the access token will expire with 60 seconds then obtain a new one using the refresh
         * token if it exists. This will cause constant refreshes if the lifespan of the token
         * is less than 60 seconds.
         */
        if (currentSession.accessTokenExpiry && currentSession.refreshToken) {
          if (Date.now() > (currentSession.accessTokenExpiry.getTime() - 30000)) {
            const body = new HttpParams()
            .set('grant_type', 'refresh_token')
            .set('refresh_token', currentSession.refreshToken);

            const options = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}};

            return this.httpClient.post<TokenResponse>(this.config.oauthTokenUrl, body.toString(), options)
            .pipe(map((tokenResponse: TokenResponse) => {
              const refreshedSession: Session = SecurityService.createSessionFromAccessToken(
                tokenResponse.access_token,
                (!!tokenResponse.refresh_token) ? tokenResponse.refresh_token : currentSession.refreshToken);

              refreshedSession.tenant = selectedTenant;

              this.session$.next(refreshedSession);

              return refreshedSession;
            }), catchError((httpErrorResponse: HttpErrorResponse) => {
              console.log('Failed to refresh the user session.', httpErrorResponse);

              if ((httpErrorResponse.status === 400) || (httpErrorResponse.status === 401)) {
                this.session$.next(null);

                // // noinspection JSIgnoredPromiseFromCall
                // this.router.navigate(['/']);
              }

              return of(null);
            }));
          }
        }
      }

      return of(null);
    }));
  }
}

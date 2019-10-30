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

import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpParams, HttpResponse} from '@angular/common/http';
import {Organization} from './organization';
import {
  DuplicateGroupError,
  DuplicateOrganizationError,
  DuplicateUserDirectoryError,
  DuplicateUserError,
  ExistingGroupMemberError,
  ExistingGroupMembersError,
  ExistingGroupRoleError,
  GroupMemberNotFoundError,
  GroupNotFoundError,
  GroupRoleNotFoundError,
  OrganizationNotFoundError,
  SecurityServiceError,
  UserDirectoryNotFoundError,
  UserNotFoundError
} from './security.service.errors';
import {CommunicationError} from '../../errors/communication-error';
import {ApiError} from '../../errors/api-error';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {environment} from '../../../../environments/environment';
import {SortDirection} from './sort-direction';
import {Organizations} from './organizations';
import {Users} from './users';
import {User} from './user';
import {UserDirectorySummary} from './user-directory-summary';
import {UserSortBy} from './user-sort-by';
import {UserDirectorySummaries} from './user-directory-summaries';
import {UserDirectory} from './user-directory';
import {UserDirectoryType} from './user-directory-type';
import {Group} from "./group";
import {Groups} from "./groups";
import {GroupMember} from "./group-member";
import {GroupMembers} from "./group-members";
import {GroupMemberType} from "./group-member-type";
import {UserDirectoryCapabilities} from "./user-directory-capabilities";
import {PasswordChangeReason} from "./password-change-reason";
import {PasswordChange} from "./password-change";
import {Role} from "./role";
import {GroupRole} from "./group-role";

/**
 * The Security Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class SecurityService {

  /**
   * Constructs a new SecurityService.
   *
   * @param httpClient The HTTP client.
   * @param i18n       The internationalization service.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Security Service');
  }

  /**
   * Add the group member to the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   * @param memberType      The group member type.
   * @param memberName      The name identifying the group member.
   *
   * @return True if the group member was successfully added to the group or false otherwise.
   */
  addMemberToGroup(userDirectoryId: string, groupName: string, memberType: GroupMemberType,
                   memberName: string): Observable<boolean> {
    let groupMember = new GroupMember(userDirectoryId, groupName, memberType, memberName);

    return this.httpClient.post<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/members', groupMember, {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.code === 'GroupNotFoundError') {
            return throwError(new GroupNotFoundError(this.i18n({
              id: '@@security_service_the_group_could_not_be_found',
              value: 'The group could not be found.'
            }), apiError));
          } else if (apiError.code === 'UserNotFoundError') {
            return throwError(new UserNotFoundError(this.i18n({
              id: '@@security_service_the_user_could_not_be_found',
              value: 'The user could not be found.'
            }), apiError));
          } else if (apiError.code === 'ExistingGroupMemberError') {
            return throwError(new ExistingGroupMemberError(this.i18n({
              id: '@@security_service_the_group_member_already_exists',
              value: 'The group member already exists.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_add_the_group_member_to_the_group',
              value: 'Failed to add the group member to the group.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Add the role to the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   * @param roleCode        The code used to uniquely identify the role.
   *
   * @return True if the role was successfully added to the group or false otherwise.
   */
  addRoleToGroup(userDirectoryId: string, groupName: string,
                 roleCode: string): Observable<boolean> {
    const groupRole = new GroupRole(userDirectoryId, groupName, roleCode);

    return this.httpClient.post<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/roles', groupRole, {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.code === 'GroupNotFoundError') {
            return throwError(new GroupNotFoundError(this.i18n({
              id: '@@security_service_the_group_could_not_be_found',
              value: 'The group could not be found.'
            }), apiError));
          } else if (apiError.code === 'RoleNotFoundError') {
            return throwError(new UserNotFoundError(this.i18n({
              id: '@@security_service_the_role_could_not_be_found',
              value: 'The role could not be found.'
            }), apiError));
          } else if (apiError.code === 'ExistingGroupRoleError') {
            return throwError(new ExistingGroupRoleError(this.i18n({
              id: '@@security_service_the_group_role_already_exists',
              value: 'The group role already exists.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_add_the_role_to_the_group',
              value: 'Failed to add the role to the group.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Administratively change the password for the user.
   *
   * @param userDirectoryId      The ID used to uniquely identify the user directory.
   * @param username             The username identifying the user.
   * @param newPassword          The new password.
   * @param expirePassword       Expire the user's password?
   * @param lockUser             Lock the user?
   * @param resetPasswordHistory Reset the user's password history?
   *
   * @return True if the user's password was changed successfully or false otherwise.
   */
  adminChangePassword(userDirectoryId: string, username: string, newPassword: string,
                      expirePassword: boolean, lockUser: boolean,
                      resetPasswordHistory: boolean): Observable<boolean> {
    let passwordChange = new PasswordChange(PasswordChangeReason.Administrative, newPassword,
      undefined, undefined, expirePassword, lockUser, resetPasswordHistory);

    return this.httpClient.put<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username) + '/password', passwordChange, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(this.i18n({
            id: '@@security_service_the_user_could_not_be_found',
            value: 'The user could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_administratively_change_the_password_for_the_user',
            value: 'Failed to administratively change the password for the user.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Change the password for the user.
   *
   * @param username    The username identifying the user.
   * @param password    The password for the user that is used to authorise the operation.
   * @param newPassword The new password.
   *
   * @return True if the user's password was changed successfully or false otherwise.
   */
  changePassword(username: string, password: string, newPassword: string): Observable<boolean> {
    let passwordChange = new PasswordChange(PasswordChangeReason.User, newPassword, password);

    return this.httpClient.put<boolean>(
      environment.securityServiceUrlPrefix + '/users/' + encodeURIComponent(username) + '/password',
      passwordChange, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(this.i18n({
            id: '@@security_service_the_user_could_not_be_found',
            value: 'The user could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_change_the_password_for_the_user',
            value: 'Failed to change the password for the user.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
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
      environment.securityServiceUrlPrefix + '/user-directories/' + group.userDirectoryId +
      '/groups', group, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'DuplicateGroupError') {
          return throwError(new DuplicateGroupError(this.i18n({
            id: '@@security_service_the_group_already_exists',
            value: 'A group with the specified name already exists.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_create_the_group',
            value: 'Failed to create the group.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Create the new organization.
   *
   * @param organization        The organization to create.
   * @param createUserDirectory Should a new internal user directory be created for the
   *                            organization?
   *
   * @return True if the organization was created successfully or false otherwise.
   */
  createOrganization(organization: Organization,
                     createUserDirectory?: boolean): Observable<boolean> {
    let httpParams = new HttpParams();
    httpParams = httpParams.append('createUserDirectory',
      createUserDirectory === undefined ? 'false' : (createUserDirectory ? 'true' : 'false'));

    return this.httpClient.post<boolean>(environment.securityServiceUrlPrefix + '/organizations',
      organization, {
        params: httpParams,
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'DuplicateOrganizationError') {
          return throwError(new DuplicateOrganizationError(this.i18n({
            id: '@@security_service_the_organization_already_exists',
            value: 'An organization with the specified ID or name already exists.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_create_the_organization',
            value: 'Failed to create the organization.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
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
    httpParams = httpParams.append('userLocked',
      userLocked === undefined ? 'false' : (userLocked ? 'true' : 'false'));

    return this.httpClient.post<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + user.userDirectoryId + '/users',
      user, {
        params: httpParams,
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'DuplicateUserError') {
          return throwError(new DuplicateUserError(this.i18n({
            id: '@@security_service_the_user_already_exists',
            value: 'A user with the specified username already exists.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_create_the_user',
            value: 'Failed to create the user.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
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
    return this.httpClient.post<boolean>(environment.securityServiceUrlPrefix + '/user-directories',
      userDirectory, {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'DuplicateUserDirectoryError') {
          return throwError(new DuplicateUserDirectoryError(this.i18n({
            id: '@@security_service_the_user_directory_already_exists',
            value: 'A user directory with the specified ID or name already exists.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_create_the_user_directory',
            value: 'Failed to create the user directory.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Delete the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   *
   * @return True if the group was deleted or false otherwise.
   */
  deleteGroup(userDirectoryId: string, groupName: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName), {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.code === 'GroupNotFoundError') {
            return throwError(new GroupNotFoundError(this.i18n({
              id: '@@security_service_the_group_could_not_be_found',
              value: 'The group could not be found.'
            }), apiError));
          } else if (apiError.code === 'ExistingGroupMembersError') {
            return throwError(new ExistingGroupMembersError(this.i18n({
              id: '@@security_service_the_group_has_existing_members',
              value: 'The group has existing members.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_delete_the_group',
              value: 'Failed to delete the group.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Delete the organization.
   *
   * @param organizationId The ID used to uniquely identify the organization.
   *
   * @return True if the organization was deleted or false otherwise.
   */
  deleteOrganization(organizationId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/organizations/' + organizationId,
      {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'OrganizationNotFoundError') {
            return throwError(new OrganizationNotFoundError(this.i18n({
              id: '@@security_service_the_organization_could_not_be_found',
              value: 'The organization could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_delete_the_organization',
              value: 'Failed to delete the organization.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Delete the user.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param username        The username identifying the user.
   *
   * @return True if the user was deleted or false otherwise.
   */
  deleteUser(userDirectoryId: string, username: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username), {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.code === 'UserNotFoundError') {
            return throwError(new UserNotFoundError(this.i18n({
              id: '@@security_service_the_user_could_not_be_found',
              value: 'The user could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_delete_the_user',
              value: 'Failed to delete the user.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   *
   * @return True if the user directory was deleted or false otherwise.
   */
  deleteUserDirectory(userDirectoryId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId,
      {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_delete_the_user_directory',
              value: 'Failed to delete the user directory.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   *
   * @return The group.
   */
  getGroup(userDirectoryId: string, groupName: string): Observable<Group> {
    return this.httpClient.get<Group>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName), {reportProgress: true}).pipe(map((group: Group) => {
      return group;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(this.i18n({
            id: '@@security_service_the_group_could_not_be_found',
            value: 'The group could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_group',
            value: 'Failed to retrieve the group.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve all the group names.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   *
   * @return The group names.
   */
  getGroupNames(userDirectoryId: string): Observable<string[]> {
    return this.httpClient.get<string[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId +
      '/group-names', {
        reportProgress: true,
      }).pipe(map((groupNames: string[]) => {
      return groupNames;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_group_names',
            value: 'Failed to retrieve the group names.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the names identifying the groups the user is a member of.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param username        The username identifying the user.
   *
   * @return The names identifying the groups the user is a member of.
   */
  getGroupNamesForUser(userDirectoryId: string, username: string): Observable<string[]> {
    return this.httpClient.get<string[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username) + '/group-names', {reportProgress: true})
      .pipe(map((groupNames: string[]) => {
        return groupNames;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.code === 'UserNotFoundError') {
            return throwError(new UserNotFoundError(this.i18n({
              id: '@@security_service_the_user_could_not_be_found',
              value: 'The user could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_retrieve_the_group_names_for_the_user',
              value: 'Failed to retrieve the group names for the user.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the groups.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param filter          The optional filter to apply to the groups.
   * @param sortDirection   The optional sort direction to apply to the groups.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   *
   * @return The groups.
   */
  getGroups(userDirectoryId: string, filter?: string, sortDirection?: SortDirection,
            pageIndex?: number, pageSize?: number): Observable<Groups> {

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

    return this.httpClient.get<Group[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups', {
        observe: 'response',
        params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<Group[]>) => {
      const totalCount = Number(response.headers.get('X-Total-Count'));

      return new Groups(userDirectoryId, response.body ? response.body : [], totalCount, filter,
        sortDirection, pageIndex, pageSize);
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_groups',
            value: 'Failed to retrieve the groups.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the organization.
   *
   * @param organizationId The ID used to uniquely identify the organization.
   *
   * @return The organization.
   */
  getOrganization(organizationId: string): Observable<Organization> {
    return this.httpClient.get<Organization>(
      environment.securityServiceUrlPrefix + '/organizations/' + organizationId,
      {reportProgress: true}).pipe(map((organization: Organization) => {
      return organization;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'OrganizationNotFoundError') {
          return throwError(new OrganizationNotFoundError(this.i18n({
            id: '@@security_service_the_organization_could_not_be_found',
            value: 'The organization could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_organization',
            value: 'Failed to retrieve the organization.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the name of the organization.
   *
   * @param organizationId The ID used to uniquely identify the organization.
   *
   * @return The name of the organization.
   */
  getOrganizationName(organizationId: string): Observable<string> {
    return this.httpClient.get<string>(
      environment.securityServiceUrlPrefix + '/organizations/' + organizationId + '/name',
      {reportProgress: true}).pipe(map((organizationName: string) => {
      return organizationName;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'OrganizationNotFoundError') {
          return throwError(new OrganizationNotFoundError(this.i18n({
            id: '@@security_service_the_organization_could_not_be_found',
            value: 'The organization could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_organization_name',
            value: 'Failed to retrieve the organization name.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the members for the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   * @param filter          The optional filter to apply to the group members.
   * @param sortDirection   The optional sort direction to apply to the group members.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   *
   * @return The members for the group.
   */
  getMembersForGroup(userDirectoryId: string, groupName: string, filter?: string,
                     sortDirection?: SortDirection, pageIndex?: number,
                     pageSize?: number): Observable<GroupMembers> {

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

    return this.httpClient.get<GroupMember[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/members', {
        observe: 'response',
        params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<GroupMember[]>) => {
      const totalCount = Number(response.headers.get('X-Total-Count'));

      return new GroupMembers(userDirectoryId, groupName, response.body ? response.body : [],
        totalCount, filter, sortDirection, pageIndex, pageSize);
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(this.i18n({
            id: '@@security_service_the_group_could_not_be_found',
            value: 'The group could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_members_for_the_group',
            value: 'Failed to retrieve the members for the group.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the organizations.
   *
   * @param filter        The optional filter to apply to the organizations.
   * @param sortDirection The optional sort direction to apply to the organizations.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The organizations.
   */
  getOrganizations(filter?: string, sortDirection?: SortDirection, pageIndex?: number,
                   pageSize?: number): Observable<Organizations> {

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

    return this.httpClient.get<Organization[]>(
      environment.securityServiceUrlPrefix + '/organizations', {
        observe: 'response',
        params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<Organization[]>) => {
      const totalCount = Number(response.headers.get('X-Total-Count'));

      return new Organizations(response.body ? response.body : [], totalCount, filter,
        sortDirection, pageIndex, pageSize);
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new SecurityServiceError(this.i18n({
          id: '@@security_service_failed_to_retrieve_the_organizations',
          value: 'Failed to retrieve the organizations.'
        }), apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the organizations the user directory is associated with.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   *
   * @return The organizations the user directory is associated with.
   */
  getOrganizationsForUserDirectory(userDirectoryId: string): Observable<Organization[]> {
    return this.httpClient.get<Organization[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId +
      '/organizations', {reportProgress: true})
      .pipe(map((organizations: Organization[]) => {
        return organizations;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@codes_service_failed_to_retrieve_the_organizations_associated_with_the_user_directory',
              value: 'Failed to retrieve the organizations associated with the user directory.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the codes for the roles that have been assigned to the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   *
   * @return The codes for the roles that have been assigned to the group.
   */
  getRoleCodesForGroup(userDirectoryId: string, groupName: string): Observable<string[]> {
    return this.httpClient.get<string[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/role-codes', {
        reportProgress: true,
      }).pipe(map((roleCodes: string[]) => {
      return roleCodes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(this.i18n({
            id: '@@security_service_the_group_could_not_be_found',
            value: 'The group could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_role_codes_for_the_group',
            value: 'Failed to retrieve the role codes for the group.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve all the roles.
   *
   * @return The roles.
   */
  getRoles(): Observable<Role[]> {
    return this.httpClient.get<Role[]>(environment.securityServiceUrlPrefix + '/roles', {
      reportProgress: true,
    }).pipe(map((roles: Role[]) => {
      return roles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new SecurityServiceError(this.i18n({
          id: '@@security_service_failed_to_retrieve_the_roles',
          value: 'Failed to retrieve the roles.'
        }), apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the roles that have been assigned to the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   *
   * @return The roles that have been assigned to the group.
   */
  getRolesForGroup(userDirectoryId: string, groupName: string): Observable<GroupRole[]> {
    return this.httpClient.get<GroupRole[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/roles', {
        reportProgress: true,
      }).pipe(map((groupRoles: GroupRole[]) => {
      return groupRoles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(this.i18n({
            id: '@@security_service_the_group_could_not_be_found',
            value: 'The group could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_roles_for_the_group',
            value: 'Failed to retrieve the roles for the group.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param username        The username identifying the user.
   *
   * @return The user.
   */
  getUser(userDirectoryId: string, username: string): Observable<User> {
    return this.httpClient.get<User>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username), {reportProgress: true}).pipe(map((user: User) => {
      return user;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(this.i18n({
            id: '@@security_service_the_user_could_not_be_found',
            value: 'The user could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_user',
            value: 'Failed to retrieve the user.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   *
   * @return The user directory.
   */
  getUserDirectory(userDirectoryId: string): Observable<UserDirectory> {
    return this.httpClient.get<UserDirectory>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId,
      {reportProgress: true})
      .pipe(map((userDirectory: UserDirectory) => {
        return userDirectory;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_retrieve_the_user_directory',
              value: 'Failed to retrieve the user directory.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the capabilities for the user directory.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   *
   * @return The capabilities for the user directory.
   */
  getUserDirectoryCapabilities(userDirectoryId: string): Observable<UserDirectoryCapabilities> {
    return this.httpClient.get<UserDirectoryCapabilities>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId +
      '/capabilities', {reportProgress: true})
      .pipe(map((capabilities: UserDirectoryCapabilities) => {
        return capabilities;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_retrieve_the_capabilities_for_the_user_directory',
              value: 'Failed to retrieve the capabilities for the user directory.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the name of the user directory.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   *
   * @return The name of the user directory.
   */
  getUserDirectoryName(userDirectoryId: string): Observable<string> {
    return this.httpClient.get<string>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/name',
      {reportProgress: true})
      .pipe(map((userDirectoryName: string) => {
        return userDirectoryName;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_retrieve_the_user_directory_name',
              value: 'Failed to retrieve the user directory name.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the summaries for the user directories the organization is associated with.
   *
   * @param organizationId The ID used to uniquely identify the organization.
   *
   * @return The summaries for the user directories the organization is associated with.
   */
  getUserDirectorySummariesForOrganization(organizationId: string): Observable<UserDirectorySummary[]> {
    return this.httpClient.get<UserDirectorySummary[]>(
      environment.securityServiceUrlPrefix + '/organizations/' + organizationId +
      '/user-directory-summaries', {reportProgress: true})
      .pipe(map((codeCategories: UserDirectorySummary[]) => {
        return codeCategories;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'OrganizationNotFoundError') {
            return throwError(new OrganizationNotFoundError(this.i18n({
              id: '@@security_service_the_organization_could_not_be_found',
              value: 'The organization could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@codes_service_failed_to_retrieve_the_summaries_for_the_user_directories_associated_with_the_organization',
              value: 'Failed to retrieve the summaries for the user directories associated with the organization.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the user directory type for the user directory.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   *
   * @return The user directory type for the user directory.
   */
  getUserDirectoryTypeForUserDirectory(userDirectoryId: string): Observable<UserDirectoryType> {
    return this.httpClient.get<UserDirectoryType>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId +
      '/user-directory-type', {reportProgress: true})
      .pipe(map((userDirectoryType: UserDirectoryType) => {
        return userDirectoryType;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_retrieve_the_user_directory_type_for_the_user_directory',
              value: 'Failed to retrieve the user directory type for the user directory.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the user directory types.
   *
   * @return The user directory types.
   */
  getUserDirectoryTypes(): Observable<UserDirectoryType[]> {
    return this.httpClient.get<UserDirectoryType[]>(
      environment.securityServiceUrlPrefix + '/user-directory-types', {reportProgress: true})
      .pipe(map((userDirectoryTypes: UserDirectoryType[]) => {
        return userDirectoryTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_user_directory_types',
            value: 'Failed to retrieve the user directory types.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the full name for the user.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param username        The username identifying the user.
   *
   * @return The full name for the user.
   */
  getUserFullName(userDirectoryId: string, username: string): Observable<string> {
    return this.httpClient.get<string>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/users/' +
      encodeURIComponent(username) + '/full-name', {reportProgress: true})
      .pipe(map((userFullName: string) => {
        return userFullName;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.code === 'UserNotFoundError') {
            return throwError(new UserNotFoundError(this.i18n({
              id: '@@security_service_the_user_could_not_be_found',
              value: 'The user could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_retrieve_the_user_full_name',
              value: 'Failed to retrieve the full name for the user.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param filter          The optional filter to apply to the users.
   * @param sortBy          The optional method used to sort the users e.g. by last name.
   * @param sortDirection   The optional sort direction to apply to the users.
   * @param pageIndex       The optional page index.
   * @param pageSize        The optional page size.
   *
   * @return The users.
   */
  getUsers(userDirectoryId: string, filter?: string, sortBy?: UserSortBy,
           sortDirection?: SortDirection, pageIndex?: number,
           pageSize?: number): Observable<Users> {

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

    return this.httpClient.get<User[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/users', {
        observe: 'response',
        params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<User[]>) => {
      const totalCount = Number(response.headers.get('X-Total-Count'));

      return new Users(userDirectoryId, response.body ? response.body : [], totalCount, filter,
        sortBy, sortDirection, pageIndex, pageSize);
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_retrieve_the_users',
            value: 'Failed to retrieve the users.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
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

    return this.httpClient.get<UserDirectorySummary[]>(
      environment.securityServiceUrlPrefix + '/user-directory-summaries', {
        observe: 'response',
        params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<UserDirectorySummary[]>) => {
      const totalCount = Number(response.headers.get('X-Total-Count'));

      return new UserDirectorySummaries(response.body ? response.body : [], totalCount, filter,
        sortDirection, pageIndex, pageSize);
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new SecurityServiceError(this.i18n({
          id: '@@security_service_failed_to_retrieve_the_user_directory_summaries',
          value: 'Failed to retrieve the user directory summaries.'
        }), apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Remove the group member from the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   * @param memberType      The group member type.
   * @param memberName      The name identifying the group member.
   *
   * @return True if the group member was successfully removed from the group or false otherwise.
   */
  removeMemberFromGroup(userDirectoryId: string, groupName: string, memberType: GroupMemberType,
                        memberName: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/members/' + memberType + '/' +
      encodeURIComponent(memberName), {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.code === 'GroupNotFoundError') {
            return throwError(new GroupNotFoundError(this.i18n({
              id: '@@security_service_the_group_could_not_be_found',
              value: 'The group could not be found.'
            }), apiError));
          } else if (apiError.code === 'GroupMemberNotFoundError') {
            return throwError(new GroupMemberNotFoundError(this.i18n({
              id: '@@security_service_the_group_member_could_not_be_found',
              value: 'The group member could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_remove_the_group_member_from_the_group',
              value: 'Failed to remove the group member from the group.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Remove the role from the group.
   *
   * @param userDirectoryId The ID used to uniquely identify the user directory.
   * @param groupName       The name identifying the group.
   * @param roleCode        The code used to uniquely identify the role.
   *
   * @return True if the role was successfully removed from the group or false otherwise.
   */
  removeRoleFromGroup(userDirectoryId: string, groupName: string,
                      roleCode: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/groups/' +
      encodeURIComponent(groupName) + '/roles/' + roleCode, {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.code === 'GroupNotFoundError') {
            return throwError(new GroupNotFoundError(this.i18n({
              id: '@@security_service_the_group_could_not_be_found',
              value: 'The group could not be found.'
            }), apiError));
          } else if (apiError.code === 'GroupRoleNotFoundError') {
            return throwError(new GroupRoleNotFoundError(this.i18n({
              id: '@@security_service_the_group_role_could_not_be_found',
              value: 'The group role could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_remove_the_role_from_the_group',
              value: 'Failed to remove the role from the group.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Reset the forgotten password for the user.
   *
   * @param username     The username identifying the user.
   * @param newPassword  The new password.
   * @param securityCode The security code.
   *
   * @return True if the user's password was reset successfully or false otherwise.
   */
  resetForgottenPassword(username: string, newPassword: string,
                         securityCode: string): Observable<boolean> {
    let passwordChange = new PasswordChange(PasswordChangeReason.Forgotten, newPassword, undefined,
      securityCode);

    return this.httpClient.put<boolean>(
      environment.securityServiceUrlPrefix + '/users/' + encodeURIComponent(username) + '/password',
      passwordChange, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(this.i18n({
            id: '@@security_service_the_user_could_not_be_found',
            value: 'The user could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_reset_the_forgotten_password_for_the_user',
            value: 'Failed to reset the forgotten password for the user.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
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
      environment.securityServiceUrlPrefix + '/user-directories/' + group.userDirectoryId +
      '/groups/' + encodeURIComponent(group.name), group, {
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'GroupNotFoundError') {
          return throwError(new GroupNotFoundError(this.i18n({
            id: '@@security_service_the_group_could_not_be_found',
            value: 'The group could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_update_the_group',
            value: 'Failed to update the group.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }

  /**
   * Update the organization.
   *
   * @param organization The organization to update.
   *
   * @return True if the organization was updated successfully or false otherwise.
   */
  updateOrganization(organization: Organization): Observable<boolean> {
    return this.httpClient.put<boolean>(
      environment.securityServiceUrlPrefix + '/organizations/' + organization.id, organization,
      {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'OrganizationNotFoundError') {
            return throwError(new OrganizationNotFoundError(this.i18n({
              id: '@@security_service_the_organization_could_not_be_found',
              value: 'The organization could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_update_the_organization',
              value: 'Failed to update the organization.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
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
    httpParams = httpParams.append('expirePassword',
      expirePassword === undefined ? 'false' : (expirePassword ? 'true' : 'false'));
    httpParams = httpParams.append('lockUser',
      lockUser === undefined ? 'false' : (lockUser ? 'true' : 'false'));

    return this.httpClient.put<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + user.userDirectoryId +
      '/users/' + encodeURIComponent(user.username), user, {
        params: httpParams,
        observe: 'response'
      }).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'UserDirectoryNotFoundError') {
          return throwError(new UserDirectoryNotFoundError(this.i18n({
            id: '@@security_service_the_user_directory_could_not_be_found',
            value: 'The user directory could not be found.'
          }), apiError));
        } else if (apiError.code === 'UserNotFoundError') {
          return throwError(new UserNotFoundError(this.i18n({
            id: '@@security_service_the_user_could_not_be_found',
            value: 'The user could not be found.'
          }), apiError));
        } else {
          return throwError(new SecurityServiceError(this.i18n({
            id: '@@security_service_failed_to_update_the_user',
            value: 'Failed to update the user.'
          }), apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
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
    return this.httpClient.put<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectory.id, userDirectory,
      {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'UserDirectoryNotFoundError') {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else {
            return throwError(new SecurityServiceError(this.i18n({
              id: '@@security_service_failed_to_update_the_user_directory',
              value: 'Failed to update the user directory.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }
}

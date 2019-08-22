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
  DuplicateOrganizationError, DuplicateUserDirectoryError,
  DuplicateUserError,
  OrganizationNotFoundError, SecurityServiceError, UserDirectoryNotFoundError, UserNotFoundError
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

    return this.httpClient.post<boolean>(
      environment.securityServiceUrlPrefix + '/organizations',
      organization, {params: httpParams, observe: 'response'}).pipe(
      map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 409) {
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
    httpParams = httpParams.append(
      'userLocked', userLocked === undefined ? 'false' : (userLocked ? 'true' : 'false'));

    return this.httpClient.post<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(
      user.userDirectoryId) + '/users',
      user, {params: httpParams, observe: 'response'}).pipe(
      map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 404) {
            return throwError(new UserDirectoryNotFoundError(this.i18n({
              id: '@@security_service_the_user_directory_could_not_be_found',
              value: 'The user directory could not be found.'
            }), apiError));
          } else if (apiError.status === 409) {
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
   * @param organization The user directory to create.
   *
   * @return True if the user directory was created successfully or false otherwise.
   */
  createUserDirectory(userDirectory: UserDirectory): Observable<boolean> {
    return this.httpClient.post<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories',
      userDirectory, {observe: 'response'}).pipe(
      map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 409) {
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
   * Delete the organization.
   *
   * @param organizationId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization.
   *
   * @return True if the organization was deleted or false otherwise.
   */
  deleteOrganization(organizationId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/organizations/' + encodeURIComponent(organizationId),
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
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
   * @param userDirectoryId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory.
   * @param username        The username identifying the user.
   *
   * @return True if the user was deleted or false otherwise.
   */
  deleteUser(userDirectoryId: string, username: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(
      userDirectoryId) + '/users/' + encodeURIComponent(username),
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
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
   * @param userDirectoryId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory.
   *
   * @return True if the user directory was deleted or false otherwise.
   */
  deleteUserDirectory(userDirectoryId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(
      userDirectoryId),
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
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
   * Retrieve the organization.
   *
   * @param organizationId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization.
   *
   * @return The organization.
   */
  getOrganization(organizationId: string): Observable<Organization> {
    return this.httpClient.get<Organization>(
      environment.securityServiceUrlPrefix + '/organizations/' + encodeURIComponent(organizationId),
      {reportProgress: true}).pipe(map((organization: Organization) => {
      return organization;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
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
        sortDirection, pageIndex,
        pageSize);
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
   * @param userDirectoryId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory.
   *
   * @return The organizations the user directory is associated with.
   */
  getOrganizationsForUserDirectory(userDirectoryId: string): Observable<Organization[]> {
    return this.httpClient.get<Organization[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(
      userDirectoryId) +
      '/organizations', {reportProgress: true})
      .pipe(map((organizations: Organization[]) => {
        return organizations;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 404) {
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
   * Retrieve the user.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory.
   * @param username        The username identifying the user.
   *
   * @return The user.
   */
  getUser(userDirectoryId: string, username: string): Observable<User> {
    return this.httpClient.get<User>(
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(
      userDirectoryId) + '/users/' + encodeURIComponent(username),
      {reportProgress: true}).pipe(map((user: User) => {
      return user;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
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
   * @param userDirectoryId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory.
   *
   * @return The user directory.
   */
  getUserDirectory(userDirectoryId: string): Observable<UserDirectory> {
    return this.httpClient.get<UserDirectory>(
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(userDirectoryId),
      {reportProgress: true}).pipe(map((userDirectory: UserDirectory) => {
      return userDirectory;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
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
   * Retrieve the summaries for the user directories the organization is associated with.
   *
   * @param organizationId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization.
   *
   * @return The summaries for the user directories the organization is associated with.
   */
  getUserDirectorySummariesForOrganization(organizationId: string): Observable<UserDirectorySummary[]> {
    return this.httpClient.get<UserDirectorySummary[]>(
      environment.securityServiceUrlPrefix + '/organizations/' + encodeURIComponent(
      organizationId) +
      '/user-directory-summaries', {reportProgress: true})
      .pipe(map((codeCategories: UserDirectorySummary[]) => {
        return codeCategories;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 404) {
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
   * Retrieve the users.
   *
   * @param userDirectoryId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory.
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
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(
      userDirectoryId) + '/users', {
        observe: 'response',
        params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<User[]>) => {
      const totalCount = Number(response.headers.get('X-Total-Count'));

      return new Users(userDirectoryId, response.body ? response.body : [], totalCount, filter,
        sortBy, sortDirection,
        pageIndex, pageSize);
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.status === 404) {
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
        sortDirection, pageIndex,
        pageSize);
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
   * Update the organization.
   *
   * @param organization The organization to update.
   *
   * @return True if the organization was updated successfully or false otherwise.
   */
  updateOrganization(organization: Organization): Observable<boolean> {
    return this.httpClient.put<boolean>(
      environment.securityServiceUrlPrefix + '/organizations/' + encodeURIComponent(
      organization.id), organization, {observe: 'response'}).pipe(
      map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 404) {
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
    httpParams = httpParams.append(
      'lockUser', lockUser === undefined ? 'false' : (lockUser ? 'true' : 'false'));

    return this.httpClient.put<boolean>(
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(
      user.userDirectoryId) + '/users/' +
      encodeURIComponent(user.username), user, {params: httpParams, observe: 'response'}).pipe(
      map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 404) {
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
      environment.securityServiceUrlPrefix + '/user-directories/' + encodeURIComponent(
      userDirectory.id), userDirectory, {observe: 'response'}).pipe(
      map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 404) {
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

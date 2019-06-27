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
  OrganizationNotFoundError,
  SecurityServiceError,
  UserDirectoryNotFoundError
} from './security.service.errors';
import {CommunicationError} from '../../errors/communication-error';
import {ApiError} from '../../errors/api-error';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {environment} from '../../../../environments/environment';
import {SortDirection} from './sort-direction';
import {Organizations} from './organizations';
import {Users} from "./users";
import {User} from "./user";
import {UserDirectorySummary} from "./user-directory-summary";
import {UserSortBy} from "./user-sort-by";

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
   * Delete the organization.
   *
   * @param organizationId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization.
   *
   * @return True if the organization was deleted or false otherwise.
   */
  deleteOrganization(organizationId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.securityServiceUrlPrefix + '/organizations/' + organizationId,
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<any>) => {
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

    if (filter !== null) {
      params = params.append('filter', filter);
    }

    if (sortDirection !== null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex !== null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize !== null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<Organization[]>(
      environment.securityServiceUrlPrefix + '/organizations', {
        observe: 'response',
        params: params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<Organization[]>) => {
      const totalCount = Number(response.headers.get('X-Total-Count'));

      return new Organizations(response.body, totalCount, filter, sortDirection, pageIndex,
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
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the organizations the user directory is associated with
   */
  getOrganizationsForUserDirectory(userDirectoryId: string): Observable<Organization[]> {
    return this.httpClient.get<Organization[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/organizations',
      {reportProgress: true})
      .pipe(map((organizations: Organization[]) => {
        return organizations;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.status === 404) {
            return throwError(new OrganizationNotFoundError(this.i18n({
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
   * Retrieve the summaries for the user directories the organization is associated with.
   *
   * @param organizationId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization.
   *
   * @return The summaries for the user directories the organization is associated with.
   */
  getUserDirectorySummariesForOrganization(organizationId: string): Observable<UserDirectorySummary[]> {
    return this.httpClient.get<UserDirectorySummary[]>(
      environment.securityServiceUrlPrefix + '/organizations/' + organizationId + '/user-directory-summaries',
      {reportProgress: true})
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
   * @return The organizations.
   */
  getUsers(userDirectoryId: string, filter?: string, sortBy?: UserSortBy,
           sortDirection?: SortDirection, pageIndex?: number,
           pageSize?: number): Observable<Users> {

    let params = new HttpParams();

    if (filter !== null) {
      params = params.append('filter', filter);
    }

    if (sortBy !== null) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection !== null) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex !== null) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize !== null) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<User[]>(
      environment.securityServiceUrlPrefix + '/user-directories/' + userDirectoryId + '/users', {
        observe: 'response',
        params: params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<User[]>) => {
      const totalCount = Number(response.headers.get('X-Total-Count'));

      return new Users(response.body, totalCount, filter, sortDirection, pageIndex,
        pageSize);
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
}

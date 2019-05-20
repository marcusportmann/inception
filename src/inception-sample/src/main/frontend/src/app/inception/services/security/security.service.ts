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
import {OrganizationNotFoundError, SecurityServiceError} from './security.service.errors';
import {CommunicationError} from '../../errors/communication-error';
import {ApiError} from '../../errors/api-error';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {environment} from '../../../../environments/environment';
import {SortDirection} from "./sort-direction";
import {FilteredOrganizations} from "./filtered-organizations";

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
      environment.configurationServiceUrlPrefix + '/organizations/' + organizationId,
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
   * Retrieve the filtered organizations using pagination.
   *
   * @param filter        the optional filter to apply to the organization name
   * @param sortDirection the optional sort direction to apply to the organization name
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return The organizations.
   */
  getFilteredOrganizations(filter?: string, sortDirection?: SortDirection, pageIndex?: number,
                   pageSize?: number): Observable<FilteredOrganizations> {

    let params = new HttpParams();

    if (filter) {
      params = params.append('filter', filter);
    }

    if (sortDirection) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient.get<Organization[]>(
      environment.securityServiceUrlPrefix + '/organizations', {
        observe: 'response',
        params: params,
        reportProgress: true,
      }).pipe(map((response: HttpResponse<Organization[]>) => {
        let totalCount = Number(response.headers.get("X-Total-Count"));

        var filteredOrganizations = new FilteredOrganizations(totalCount, response.body);

      return filteredOrganizations;
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
}

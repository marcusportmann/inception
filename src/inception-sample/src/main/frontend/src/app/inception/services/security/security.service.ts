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

import {Inject, Injectable} from '@angular/core';
import {Observable, pipe, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';


import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Organization} from "./organization";
import {SessionError} from "../session/session.service.errors";
import {SecurityServiceError} from "./security.service.errors";
import {CommunicationError} from "../../errors/communication-error";
import {ApiError} from "../../errors/api-error";
import {Router} from "@angular/router";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";

/**
 * The SecurityService class provides the Security Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class SecurityService {

  /**
   * Constructs a new SecurityService.
   *
   * @param {HttpClient} httpClient The HTTP client.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Security Service');
  }

  /**
   * Retrieve the organizations.
   *
   * @return {Observable<Organization[]>} The list of organizations.
   */
  public getOrganizations(): Observable<Organization[]> {

    return this.httpClient.get<Organization[]>('http://localhost:20000/api/organizations', {reportProgress: true}).pipe(
      map((organizations: Organization[]) => {

        return organizations;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new SecurityServiceError(this.i18n({id: '@@security_service_failed_to_retrieve_the_organizations',
            value: 'Failed to retrieve the organizations.'}), apiError));
        }
        else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        }
        else {
          return throwError(new SystemUnavailableError(this.i18n({id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'}), httpErrorResponse));
         }
      }));
  }
}

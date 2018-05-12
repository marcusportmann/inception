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
import {Observable} from 'rxjs/Rx';
import {catchError} from 'rxjs/operators';
import {map} from 'rxjs/operators';


import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Organization} from "./organization";
import {SessionError} from "../session/session.service.errors";
import {SecurityServiceError} from "./security.service.errors";
import {CommunicationError} from "../../common/errors/communication-error";

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
  constructor(private httpClient: HttpClient) {
  }

  /**
   * Retrieve the organizations.
   *
   * @return {Observable<Organization[]>} The list of organizations.
   */
  public getOrganizations(): Observable<Organization[]> {

    return this.httpClient.get<Organization[]>('http://localhost:20000/api/organizations').pipe(
      map((organizations: Organization[]) => {

        return organizations;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (httpErrorResponse.error && httpErrorResponse.error.exception) {
          return Observable.throw(new SecurityServiceError(httpErrorResponse));
        }
        else {
          return Observable.throw(new CommunicationError(new Date(), httpErrorResponse.statusText, httpErrorResponse.message));
        }

      }));
  }
}

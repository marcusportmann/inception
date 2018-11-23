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

import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {CodeCategory} from "./code-category";
import {CodesServiceError} from "./codes.service.errors";
import {CommunicationError} from "../../errors/communication-error";
import {ApiError} from "../../errors/api-error";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";
import {environment} from "../../../../environments/environment";

/**
 * The CodesService class provides the Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class CodesService {

  /**
   * Constructs a new CodesService.
   *
   * @param {HttpClient} httpClient The HTTP client.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Codes Service');
  }

  /**
   * Retrieve the code categories.
   *
   * @return {Observable<CodeCategory[]>} The list of code categories.
   */
  public getCodeCategories(): Observable<CodeCategory[]> {

    return this.httpClient.get<CodeCategory[]>(environment.codesServiceGetCodeCategoriesUrl, {reportProgress: true}).pipe(
      map((codeCategories: CodeCategory[]) => {

        return codeCategories;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new CodesServiceError(this.i18n({
            id: '@@codes_service_failed_to_retrieve_the_code_categories',
            value: 'Failed to retrieve the code categories.'
          }), apiError));
        }
        else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        }
        else {
          return throwError(new SystemUnavailableError(this.i18n({
            id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'
          }), httpErrorResponse));
        }
      }));
  }
}

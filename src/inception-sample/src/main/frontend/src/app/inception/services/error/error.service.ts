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

import { Injectable } from "@angular/core";

import {Error} from "../../errors/error";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {environment} from "../../../../environments/environment";
import {catchError, map} from "rxjs/operators";
import {ApiError} from "../../errors/api-error";
import {Observable, throwError} from "rxjs";
import {CommunicationError} from "../../errors/communication-error";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";
import {ErrorReport} from "./error-report";
import {v4 as uuid} from "uuid";
import {ErrorServiceError} from "./error.service.errors";

/**
 * The Error Service implementation that provides the capability to capture and process application
 * and back-end errors and submit error reports.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class ErrorService {

  /**
   * Constructs a new ErrorService.
   *
   * @param {HttpClient} httpClient The HTTP client.
   * @param {I18n} i18n             The internationalization service.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Error Service');
  }

  /**
   * Send the error report for the error.
   *
   * @param {Error} error The error.
   */
  sendErrorReport(email: string, feedback: string, error: Error): Observable<boolean> {
    let errorReport: ErrorReport = new ErrorReport(uuid(), environment.applicationId,
      environment.applicationVersion, error.message,
      error.cause ? JSON.stringify(error.cause) : '', error.timestamp,
      (!email || 0 === email.length) ? null : email,
      null, (!feedback || 0 === feedback.length) ? null : feedback);

    return this.httpClient.post<boolean>(environment.errorServiceUrlPrefix + '/error-reports',
      errorReport, {observe: "response"}).pipe(
      map((httpResponse: HttpResponse<any>) => {
        return true;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          let apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new ErrorServiceError(this.i18n({
            id: '@@error_service_failed_to_send_the_error_report',
            value: 'Failed to send the error report.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }
}

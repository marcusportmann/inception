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

import {Error} from '../../errors/error';
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {environment} from '../../../../environments/environment';
import {catchError, map} from 'rxjs/operators';
import {ApiError} from '../../errors/api-error';
import {Observable, throwError} from 'rxjs';
import {CommunicationError} from '../../errors/communication-error';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {ErrorReport} from './error-report';
import {v4 as uuid} from 'uuid';
import {ErrorServiceError} from './error.service.errors';

/**
 * The Error Service implementation that provides the capability to capture and process application
 * and back-end errors and submit error reports.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  /**
   * Constructs a new ErrorService.
   *
   * @param httpClient The HTTP client.
   * @param i18n       The internationalization service.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Error Service');
  }

  /**
   * Send the error report for the error.
   *
   * @param error    The error.
   * @param email    The optional e-mail address of the user submitting the error report.
   * @param feedback The optional feedback from the user submitting the error report.
   */
  sendErrorReport(error: Error, email?: string, feedback?: string): Observable<boolean> {
    const errorReport: ErrorReport = new ErrorReport(uuid(), environment.applicationId,
      environment.applicationVersion, error.message, error.cause ? JSON.stringify(error.cause) : '',
      error.timestamp, email, feedback);

    return this.httpClient.post<boolean>(environment.errorServiceUrlPrefix + '/error-reports',
      errorReport, {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ErrorServiceError(this.i18n({
          id: '@@error_send_error_report_error',
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

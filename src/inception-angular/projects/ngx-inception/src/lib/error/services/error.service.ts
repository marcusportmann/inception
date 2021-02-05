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

import {Error} from '../../core/errors/error';
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {ApiError} from '../../core/errors/api-error';
import {Observable, throwError} from 'rxjs';
import {CommunicationError} from '../../core/errors/communication-error';
import {ServiceUnavailableError} from '../../core/errors/service-unavailable-error';
import {ErrorReport} from './error-report';
import {v4 as uuid} from 'uuid';
import {INCEPTION_CONFIG, InceptionConfig} from '../../inception-config';

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
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig, private httpClient: HttpClient) {
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
    const errorReport: ErrorReport = new ErrorReport(uuid(), this.config.applicationId, this.config.applicationVersion,
      error.message, error.cause ? JSON.stringify(error.cause) : '', error.timestamp, email, feedback);

    return this.httpClient.post<boolean>(this.config.errorApiUrlPrefix + '/error-reports', errorReport,
      {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new ServiceUnavailableError('Failed to send the error report.', apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new ServiceUnavailableError('Failed to send the error report.', httpErrorResponse));
      }
    }));
  }
}

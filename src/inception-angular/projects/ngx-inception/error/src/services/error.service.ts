/*
 * Copyright Marcus Portmann
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

import { HttpClient, HttpErrorResponse, HttpParams, HttpResponse } from '@angular/common/http';
import {Inject, Injectable} from '@angular/core';
import {
  AccessDeniedError, CommunicationError, Error, INCEPTION_CONFIG, InceptionConfig,
  InvalidArgumentError, ProblemDetails, ResponseConverter, ServiceUnavailableError, SortDirection
} from 'ngx-inception/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {v4 as uuid} from 'uuid';
import {ErrorReport} from './error-report';
import {ErrorReportSortBy} from './error-report-sort-by';
import {ErrorReportSummaries} from './error-report-summaries';
import {ErrorReportNotFoundError} from './error.service.errors';

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
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig,
              private httpClient: HttpClient) {
    console.log('Initializing the Error Service');
  }

  /**
   * Retrieve the error report.
   *
   * @param errorReportId The ID for the error report.
   *
   * @return The error report.
   */
  @ResponseConverter getErrorReport(errorReportId: string): Observable<ErrorReport> {
    return this.httpClient.get<ErrorReport>(
      this.config.apiUrlPrefix + '/error/error-reports/' + encodeURIComponent(errorReportId),
      {reportProgress: true})
    .pipe(map((errorReport: ErrorReport) => {
      return errorReport;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ProblemDetails.isProblemDetails(httpErrorResponse, ErrorReportNotFoundError.TYPE)) {
        return throwError(() => new ErrorReportNotFoundError(httpErrorResponse));
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the error report.',
        httpErrorResponse));
    }));
  }

  /**
   * Retrieve the error report summaries.
   *
   * @param filter        The optional filter to apply to the error report summaries.
   * @param fromDate      ISO 8601 format date value for the date to retrieve the error report
   *                      summaries from.
   * @param toDate        ISO 8601 format date value for the date to retrieve the error report
   *                      summaries to.
   * @param sortBy        The optional method used to sort the error report summaries e.g. by who
   *                      submitted them.
   * @param sortDirection The optional sort direction to apply to the error report summaries.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   *
   * @return The users.
   */
  @ResponseConverter getErrorReportSummaries(filter?: string, fromDate?: string, toDate?: string,
                                             sortBy?: ErrorReportSortBy,
                                             sortDirection?: SortDirection, pageIndex?: number,
                                             pageSize?: number): Observable<ErrorReportSummaries> {

    let params = new HttpParams();

    if (filter != null) {
      params = params.append('filter', filter);
    }

    if (fromDate != null) {
      params = params.append('fromDate', fromDate);
    }

    if (toDate != null) {
      params = params.append('toDate', toDate);
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

    return this.httpClient.get<ErrorReportSummaries>(
      this.config.apiUrlPrefix + '/error/error-report-summaries', {
        params,
        reportProgress: true,
      }).pipe(map((errorReportSummaries: ErrorReportSummaries) => {
      return errorReportSummaries;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(
        () => new ServiceUnavailableError('Failed to retrieve the error report summaries.',
          httpErrorResponse));
    }));
  }

  /**
   * Send the error report for the error.
   *
   * @param error    The error.
   * @param email    The optional email address of the user submitting the error report.
   * @param feedback The optional feedback from the user submitting the error report.
   */
  sendErrorReport(error: Error, email?: string, feedback?: string): Observable<boolean> {
    const errorReport: ErrorReport = new ErrorReport(uuid(), this.config.applicationId,
      this.config.applicationVersion, error.message, error.cause ? JSON.stringify(error.cause) : '',
      error.timestamp, email, feedback);

    return this.httpClient.post<boolean>(this.config.apiUrlPrefix + '/error/error-reports',
      errorReport, {observe: 'response'})
    .pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(
        () => new ServiceUnavailableError('Failed to send the error report.', httpErrorResponse));
    }));
  }
}

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
import { inject, Injectable } from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, InvalidArgumentError,
  ProblemDetails, ResponseConverter, ServiceUnavailableError, SortDirection
} from 'ngx-inception/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { v4 as uuid } from 'uuid';
import { ErrorReport } from './error-report';
import { ErrorReportSortBy } from './error-report-sort-by';
import { ErrorReportSummaries } from './error-report-summaries';
import { ErrorReportNotFoundError } from './error.service.errors';

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
  private readonly config = inject<InceptionConfig>(INCEPTION_CONFIG);

  private readonly httpClient = inject(HttpClient);

  /**
   * Constructs a new ErrorService.
   */
  constructor() {
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
    return this.httpClient
      .get<ErrorReport>(
        `${this.config.apiUrlPrefix}/error/error-reports/${errorReportId}`,
        { reportProgress: true }
      )
      .pipe(
        catchError((error) =>
          ErrorService.handleApiError(error, 'Failed to retrieve the error report.')
        )
      );
  }

  /**
   * Retrieve the error report summaries.
   *
   * @param filter        The filter to apply to the error report summaries.
   * @param fromDate      ISO 8601 format date value for the date to retrieve the error report
   *                      summaries from.
   * @param toDate        ISO 8601 format date value for the date to retrieve the error report
   *                      summaries to.
   * @param sortBy        The method used to sort the error report summaries e.g., by who
   *                      submitted them.
   * @param sortDirection The sort direction to apply to the error report summaries.
   * @param pageIndex     The page index.
   * @param pageSize      The page size.
   *
   * @return The users.
   */
  @ResponseConverter getErrorReportSummaries(
    filter?: string,
    fromDate?: string,
    toDate?: string,
    sortBy?: ErrorReportSortBy,
    sortDirection?: SortDirection,
    pageIndex?: number,
    pageSize?: number
  ): Observable<ErrorReportSummaries> {
    let params = new HttpParams();

    if (filter) {
      params = params.append('filter', filter);
    }

    if (fromDate) {
      params = params.append('fromDate', fromDate);
    }

    if (toDate) {
      params = params.append('toDate', toDate);
    }

    if (sortBy) {
      params = params.append('sortBy', String(sortBy));
    }

    if (sortDirection) {
      params = params.append('sortDirection', sortDirection);
    }

    if (pageIndex !== undefined) {
      params = params.append('pageIndex', String(pageIndex));
    }

    if (pageSize !== undefined) {
      params = params.append('pageSize', String(pageSize));
    }

    return this.httpClient
      .get<ErrorReportSummaries>(`${this.config.apiUrlPrefix}/error/error-report-summaries`, {
        params,
        reportProgress: true
      })
      .pipe(
        catchError((error) =>
          ErrorService.handleApiError(error, 'Failed to retrieve the error report summaries.')
        )
      );
  }

  /**
   * Send the error report for the error.
   *
   * @param error    The error.
   * @param email    The email address of the user submitting the error report.
   * @param feedback The feedback from the user submitting the error report.
   */
  sendErrorReport(error: Error, email?: string, feedback?: string): Observable<boolean> {
    const maybeTimestamp = (error as { timestamp?: unknown }).timestamp;

    const timestamp = maybeTimestamp instanceof Date ? maybeTimestamp : new Date();

    const causeString = (() => {
      if (error.cause === undefined || error.cause === null) {
        return '';
      }
      try {
        return JSON.stringify(error.cause);
      } catch {
        return '';
      }
    })();

    const errorReport: ErrorReport = new ErrorReport(
      uuid(),
      this.config.applicationId,
      this.config.applicationVersion,
      error.message,
      causeString,
      timestamp,
      email,
      feedback
    );

    return this.httpClient
      .post<boolean>(`${this.config.apiUrlPrefix}/error/error-reports`, errorReport, {
        observe: 'response'
      })
      .pipe(
        map(ErrorService.isResponse204),
        catchError((err) => ErrorService.handleApiError(err, 'Failed to send the error report.'))
      );
  }

  private static handleApiError(
    httpErrorResponse: HttpErrorResponse,
    defaultMessage: string
  ): Observable<never> {
    if (ProblemDetails.isProblemDetails(httpErrorResponse, ErrorReportNotFoundError.TYPE)) {
      return throwError(() => new ErrorReportNotFoundError(httpErrorResponse));
    } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
      return throwError(() => new AccessDeniedError(httpErrorResponse));
    } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
      return throwError(() => new CommunicationError(httpErrorResponse));
    } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
      return throwError(() => new InvalidArgumentError(httpErrorResponse));
    }

    return throwError(() => new ServiceUnavailableError(defaultMessage, httpErrorResponse));
  }

  // Centralized method to check if HTTP response status is 204 (No Content)
  private static isResponse204(httpResponse: HttpResponse<boolean>): boolean {
    return httpResponse.status === 204;
  }
}

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

import {
  HttpClient,
  HttpErrorResponse,
  HttpResponse
} from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import {
  AccessDeniedError,
  CommunicationError,
  INCEPTION_CONFIG,
  InceptionConfig,
  InvalidArgumentError,
  ProblemDetails,
  ServiceUnavailableError
} from 'ngx-inception/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ReportDefinition } from './report-definition';
import { ReportDefinitionSummary } from './report-definition-summary';
import {
  DuplicateReportDefinitionError,
  ReportDefinitionNotFoundError
} from './reporting.service.errors';

/**
 * The Reporting Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class ReportingService {
  private config = inject<InceptionConfig>(INCEPTION_CONFIG);
  private httpClient = inject(HttpClient);

  static readonly MAX_TEMPLATE_SIZE: number = 10485760;

  /**
   * Constructs a new ReportingService.
   *
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   */
  constructor() {
    console.log('Initializing the Reporting Service');
  }

  /**
   * Create a report definition.
   *
   * @param reportDefinition The report definition to create.
   * @return True if the report definition was created successfully or false otherwise.
   */
  createReportDefinition(
    reportDefinition: ReportDefinition
  ): Observable<boolean> {
    return this.httpClient
      .post<boolean>(
        `${this.config.apiUrlPrefix}/reporting/report-definitions`,
        reportDefinition,
        { observe: 'response' }
      )
      .pipe(
        map(ReportingService.isResponse204),
        catchError(
          ReportingService.handleApiError(
            'Failed to create the report definition.'
          )
        )
      );
  }

  /**
   * Delete the report definition.
   *
   * @param reportDefinitionId The ID for the report definition.
   * @return True if the report definition was deleted or false otherwise.
   */
  deleteReportDefinition(reportDefinitionId: string): Observable<boolean> {
    return this.httpClient
      .delete<boolean>(
        `${this.config.apiUrlPrefix}/reporting/report-definitions/${encodeURIComponent(
          reportDefinitionId
        )}`,
        { observe: 'response' }
      )
      .pipe(
        map(ReportingService.isResponse204),
        catchError(
          ReportingService.handleApiError(
            'Failed to delete the report definition.'
          )
        )
      );
  }

  /**
   * Retrieve the report definition.
   *
   * @param reportDefinitionId The ID for the report definition.
   * @return The report definition.
   */
  getReportDefinition(
    reportDefinitionId: string
  ): Observable<ReportDefinition> {
    return this.httpClient
      .get<ReportDefinition>(
        `${this.config.apiUrlPrefix}/reporting/report-definitions/${encodeURIComponent(
          reportDefinitionId
        )}`,
        { reportProgress: true }
      )
      .pipe(
        catchError(
          ReportingService.handleApiError(
            'Failed to retrieve the report definition.'
          )
        )
      );
  }

  /**
   * Retrieve the name of the report definition.
   *
   * @param reportDefinitionId The ID for the report definition.
   * @return The name of the report definition.
   */
  getReportDefinitionName(reportDefinitionId: string): Observable<string> {
    return this.httpClient
      .get<string>(
        `${this.config.apiUrlPrefix}/reporting/report-definitions/${encodeURIComponent(
          reportDefinitionId
        )}/name`,
        { reportProgress: true }
      )
      .pipe(
        catchError(
          ReportingService.handleApiError(
            'Failed to retrieve the report definition name.'
          )
        )
      );
  }

  /**
   * Retrieve the summaries for all the report definitions.
   *
   * @return The summaries for all the report definitions.
   */
  getReportDefinitionSummaries(): Observable<ReportDefinitionSummary[]> {
    return this.httpClient
      .get<
        ReportDefinitionSummary[]
      >(`${this.config.apiUrlPrefix}/reporting/report-definitions`, { reportProgress: true })
      .pipe(
        catchError(
          ReportingService.handleApiError(
            'Failed to retrieve the summaries for the report definitions.'
          )
        )
      );
  }

  /**
   * Retrieve all the report definitions.
   *
   * @return The report definitions.
   */
  getReportDefinitions(): Observable<ReportDefinition[]> {
    return this.httpClient
      .get<
        ReportDefinition[]
      >(`${this.config.apiUrlPrefix}/reporting/report-definitions`, { reportProgress: true })
      .pipe(
        catchError(
          ReportingService.handleApiError(
            'Failed to retrieve the report definitions.'
          )
        )
      );
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinition The report definition.
   * @return True if the report definition was updated successfully or false otherwise.
   */
  updateReportDefinition(
    reportDefinition: ReportDefinition
  ): Observable<boolean> {
    return this.httpClient
      .put<boolean>(
        `${this.config.apiUrlPrefix}/reporting/report-definitions/${encodeURIComponent(
          reportDefinition.id
        )}`,
        reportDefinition,
        { observe: 'response' }
      )
      .pipe(
        map(ReportingService.isResponse204),
        catchError(
          ReportingService.handleApiError(
            'Failed to update the report definition.'
          )
        )
      );
  }

  private static handleApiError(defaultMessage: string) {
    return (httpErrorResponse: HttpErrorResponse) => {
      if (
        ProblemDetails.isProblemDetails(
          httpErrorResponse,
          DuplicateReportDefinitionError.TYPE
        )
      ) {
        return throwError(
          () => new DuplicateReportDefinitionError(httpErrorResponse)
        );
      } else if (
        ProblemDetails.isProblemDetails(
          httpErrorResponse,
          ReportDefinitionNotFoundError.TYPE
        )
      ) {
        return throwError(
          () => new ReportDefinitionNotFoundError(httpErrorResponse)
        );
      } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (
        InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)
      ) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(
        () => new ServiceUnavailableError(defaultMessage, httpErrorResponse)
      );
    };
  }

  /**
   * Helper method to check if the response status is 204.
   *
   * @param httpResponse The HTTP response to check.
   * @return True if the response status is 204, otherwise false.
   */
  private static isResponse204(httpResponse: HttpResponse<boolean>): boolean {
    return httpResponse.status === 204;
  }
}

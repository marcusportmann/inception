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
import {Observable, throwError} from "rxjs";
import {environment} from "../../../../environments/environment";
import {catchError, map} from "rxjs/operators";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {ApiError} from "../../errors/api-error";
import {
  DuplicateReportDefinitionError, ReportDefinitionNotFoundError, ReportingServiceError
} from "./reporting.service.errors";
import {CommunicationError} from "../../errors/communication-error";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {ReportDefinition} from "./report-definition";
import {ReportDefinitionSummary} from "./report-definition-summary";

/**
 * The Reporting Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class ReportingService {

  /**
   * Constructs a new ReportingService.
   *
   * @param httpClient The HTTP client.
   * @param i18n       The internationalization service.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Reporting Service');
  }

  /**
   * Create a report definition.
   *
   * @param reportDefinition The report definition to create.
   *
   * @return True if the report definition was created successfully or false otherwise.
   */
  createReportDefinition(reportDefinition: ReportDefinition): Observable<boolean> {
    return this.httpClient.post<boolean>(
      environment.reportingServiceUrlPrefix + '/report-definitions', reportDefinition,
      {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'ReportDefinitionNotFoundError') {
            return throwError(new ReportDefinitionNotFoundError(this.i18n, apiError));
          } else if (apiError.code === 'DuplicateReportDefinitionError') {
            return throwError(new DuplicateReportDefinitionError(this.i18n, apiError));
          } else {
            return throwError(new ReportingServiceError(this.i18n({
              id: '@@reporting_create_report_definition_error',
              value: 'Failed to create the report definition.'
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
   * Delete the report definition.
   *
   * @param reportDefinitionId The Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition.
   *
   * @return True if the report definition was deleted or false otherwise.
   */
  deleteReportDefinition(reportDefinitionId: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.reportingServiceUrlPrefix + '/report-definitions/' +
      encodeURIComponent(reportDefinitionId), {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'ReportDefinitionNotFoundError') {
            return throwError(new ReportDefinitionNotFoundError(this.i18n, apiError));
          } else {
            return throwError(new ReportingServiceError(this.i18n({
              id: '@@reporting_delete_report_definition_error',
              value: 'Failed to delete the report definition.'
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
   * Retrieve the report definition.
   *
   * @param reportDefinitionId The Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition.
   *
   * @return The report definition.
   */
  getReportDefinition(reportDefinitionId: string): Observable<ReportDefinition> {
    return this.httpClient.get<ReportDefinition>(
      environment.reportingServiceUrlPrefix + '/report-definitions/' +
      encodeURIComponent(reportDefinitionId), {reportProgress: true})
      .pipe(map((reportDefinition: ReportDefinition) => {
        return reportDefinition;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'ReportDefinitionNotFoundError') {
            return throwError(new ReportDefinitionNotFoundError(this.i18n, apiError));
          } else {
            return throwError(new ReportingServiceError(this.i18n({
              id: '@@reporting_get_report_definition_error',
              value: 'Failed to retrieve the report definition.'
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
   * Retrieve the name of the report definition.
   *
   * @param reportDefinitionId The Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition.
   *
   * @return The name of the report definition.
   */
  getReportDefinitionName(reportDefinitionId: string): Observable<string> {
    return this.httpClient.get<string>(
      environment.reportingServiceUrlPrefix + '/report-definitions/' +
      encodeURIComponent(reportDefinitionId) + '/name', {
        reportProgress: true,
      }).pipe(map((reportDefinitionName: string) => {
      return reportDefinitionName;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'ReportDefinitionNotFoundError') {
          return throwError(new ReportDefinitionNotFoundError(this.i18n, apiError));
        } else {
          return throwError(new ReportingServiceError(this.i18n({
            id: '@@reporting_get_report_definition_name_error',
            value: 'Failed to retrieve the report definition name.'
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
   * Retrieve the summaries for all the report definitions.
   *
   * @return The summaries for all the report definitions.
   */
  getReportDefinitionSummaries(): Observable<ReportDefinitionSummary[]> {
    return this.httpClient.get<ReportDefinitionSummary[]>(
      environment.reportingServiceUrlPrefix + '/report-definitions', {reportProgress: true})
      .pipe(map((reportDefinitionSummaries: ReportDefinitionSummary[]) => {
        return reportDefinitionSummaries;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new ReportingServiceError(this.i18n({
            id: '@@reporting_get_report_definition_summaries_error',
            value: 'Failed to retrieve the summaries for the report definitions.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Retrieve all the report definitions.
   *
   * @return The report definitions.
   */
  getReportDefinitions(): Observable<ReportDefinition[]> {
    return this.httpClient.get<ReportDefinition[]>(
      environment.reportingServiceUrlPrefix + '/report-definitions', {reportProgress: true})
      .pipe(map((reportDefinitions: ReportDefinition[]) => {
        return reportDefinitions;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new ReportingServiceError(this.i18n({
            id: '@@reporting_get_report_definitions_error',
            value: 'Failed to retrieve the report definitions.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinition The report definition
   *
   * @return True if the report definition was updated successfully or false otherwise.
   */
  updateReportDefinition(reportDefinition: ReportDefinition): Observable<boolean> {
    return this.httpClient.put<boolean>(
      environment.reportingServiceUrlPrefix + '/report-definitions/' +
      encodeURIComponent(reportDefinition.id), reportDefinition, {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'ReportDefinitionNotFoundError') {
            return throwError(new ReportDefinitionNotFoundError(this.i18n, apiError));
          } else {
            return throwError(new ReportingServiceError(this.i18n({
              id: '@@reporting_update_report_definition_error',
              value: 'Failed to update the report definition.'
            }), apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }
}

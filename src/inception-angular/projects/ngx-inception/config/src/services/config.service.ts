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

import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Inject, Injectable} from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, InvalidArgumentError,
  ProblemDetails, ServiceUnavailableError
} from 'ngx-inception/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Config} from './config';
import {ConfigNotFoundError} from './config.service.errors';

/**
 * The Config Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  /**
   * Constructs a new ConfigService.
   *
   * @param config     The Inception config.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig,
              private httpClient: HttpClient) {
    console.log('Initializing the Config Service');
  }

  /**
   * Delete the config.
   *
   * @param id The ID for the config.
   *
   * @return True if the config was deleted or false otherwise.
   */
  deleteConfig(id: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      `${this.config.apiUrlPrefix}/config/configs/${encodeURIComponent(id)}`,
      {observe: 'response'})
    .pipe(map(ConfigService.isResponse204),
      catchError((error) => ConfigService.handleApiError(error, 'Failed to delete the config.')));
  }

  /**
   * Retrieve the config.
   *
   * @param id The ID for the config.
   *
   * @return The config.
   */
  getConfig(id: string): Observable<Config> {
    return this.httpClient.get<Config>(
      `${this.config.apiUrlPrefix}/config/configs/${encodeURIComponent(id)}`,
      {reportProgress: true})
    .pipe(
      catchError((error) => ConfigService.handleApiError(error, 'Failed to retrieve the config.')));
  }

  /**
   * Retrieve the config value.
   *
   * @param id The ID for the config.
   *
   * @return The config value.
   */
  getConfigValue(id: string): Observable<string> {
    return this.httpClient.get<string>(
      `${this.config.apiUrlPrefix}/config/configs/${encodeURIComponent(id)}/value`,
      {reportProgress: true})
    .pipe(catchError(
      (error) => ConfigService.handleApiError(error, 'Failed to retrieve the config value.')));
  }

  /**
   * Retrieve all the configs.
   *
   * @return The configs.
   */
  getConfigs(): Observable<Config[]> {
    return this.httpClient.get<Config[]>(`${this.config.apiUrlPrefix}/config/configs`,
      {reportProgress: true})
    .pipe(catchError(
      (error) => ConfigService.handleApiError(error, 'Failed to retrieve the configs.')));
  }

  /**
   * Save the config.
   *
   * @param config The config.
   *
   * @return True if the config was saved successfully or false otherwise.
   */
  saveConfig(config: Config): Observable<boolean> {
    return this.httpClient.post<boolean>(`${this.config.apiUrlPrefix}/config/configs`, config,
      {observe: 'response'})
    .pipe(map(ConfigService.isResponse204),
      catchError((error) => ConfigService.handleApiError(error, 'Failed to save the config.')));
  }

  private static handleApiError(httpErrorResponse: HttpErrorResponse,
                                defaultMessage: string): Observable<never> {
    if (ProblemDetails.isProblemDetails(httpErrorResponse, ConfigNotFoundError.TYPE)) {
      return throwError(() => new ConfigNotFoundError(httpErrorResponse));
    } else if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
      return throwError(() => new AccessDeniedError(httpErrorResponse));
    } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
      return throwError(() => new CommunicationError(httpErrorResponse));
    } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
      return throwError(() => new InvalidArgumentError(httpErrorResponse));
    }
    return throwError(() => new ServiceUnavailableError(defaultMessage, httpErrorResponse));
  }

  private static isResponse204(httpResponse: HttpResponse<boolean>): boolean {
    return httpResponse.status === 204;
  }
}

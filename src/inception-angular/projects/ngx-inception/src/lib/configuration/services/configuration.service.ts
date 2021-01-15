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
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {ApiError} from '../../core/errors/api-error';
import {CodesServiceError} from '../../codes/services/codes.service.errors';
import {CommunicationError} from '../../core/errors/communication-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {ConfigurationNotFoundError, ConfigurationServiceError} from './configuration.service.errors';
import {Configuration} from './configuration';
import {INCEPTION_CONFIG, InceptionConfig} from '../../inception-config';

/**
 * The Configuration Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  /**
   * Constructs a new ConfigurationService.
   *
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig, private httpClient: HttpClient) {
    console.log('Initializing the Configuration Service');
  }

  /**
   * Delete the configuration.
   *
   * @param key The key for the configuration.
   *
   * @return True if the configuration was deleted or false otherwise.
   */
  deleteConfiguration(key: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      this.config.configurationApiUrlPrefix + '/configurations/' + encodeURIComponent(key), {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'ConfigurationNotFoundError') {
            return throwError(new ConfigurationNotFoundError(apiError));
          } else {
            return throwError(new ConfigurationServiceError('Failed to delete the configuration.', apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse));
        }
      }));
  }

  /**
   * Retrieve the configuration.
   *
   * @param key The key for the configuration.
   *
   * @return The configuration.
   */
  getConfiguration(key: string): Observable<Configuration> {
    return this.httpClient.get<Configuration>(
      this.config.configurationApiUrlPrefix + '/configurations/' + encodeURIComponent(key), {reportProgress: true})
      .pipe(map((configuration: Configuration) => {
        return configuration;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'ConfigurationNotFoundError') {
            return throwError(new ConfigurationNotFoundError(apiError));
          } else {
            return throwError(new ConfigurationServiceError('Failed to retrieve the configuration.', apiError));
          }
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse));
        }
      }));
  }

  /**
   * Retrieve the configuration value.
   *
   * @param key The key for the configuration.
   *
   * @return The configuration value.
   */
  getConfigurationValue(key: string): Observable<string> {
    return this.httpClient.get<string>(
      this.config.configurationApiUrlPrefix + '/configurations/' + encodeURIComponent(key) + '/value', {
        reportProgress: true
      }).pipe(map((value: string) => {
      return value;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'ConfigurationNotFoundError') {
          return throwError(new ConfigurationNotFoundError(apiError));
        } else {
          return throwError(new ConfigurationServiceError('Failed to retrieve the configuration value.', apiError));
        }
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse));
      }
    }));
  }

  /**
   * Retrieve all the configurations.
   *
   * @return The configurations.
   */
  getConfigurations(): Observable<Configuration[]> {
    return this.httpClient.get<Configuration[]>(this.config.configurationApiUrlPrefix + '/configurations',
      {reportProgress: true})
      .pipe(map((configurations: Configuration[]) => {
        return configurations;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new CodesServiceError('Failed to retrieve the configurations.', apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse));
        }
      }));
  }

  /**
   * Save the configuration.
   *
   * @param configuration The configuration.
   *
   * @return True if the configuration was saved successfully or false otherwise.
   */
  saveConfiguration(configuration: Configuration): Observable<boolean> {
    return this.httpClient.post<boolean>(this.config.configurationApiUrlPrefix + '/configurations', configuration,
      {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new CodesServiceError('Failed to save the configuration.', apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse));
        }
      }));
  }
}

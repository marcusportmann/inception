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
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Observable, throwError} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {catchError, map} from 'rxjs/operators';
import {ApiError} from '../../errors/api-error';
import {CodesServiceError} from '../codes/codes.service.errors';
import {CommunicationError} from '../../errors/communication-error';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {
  ConfigurationNotFoundError, ConfigurationServiceError
} from './configuration.service.errors';
import {Configuration} from './configuration';

/**
 * The Configuration Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class ConfigurationService {

  /**
   * Constructs a new ConfigurationService.
   *
   * @param httpClient The HTTP client.
   * @param i18n       The internationalization service.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Configuration Service');
  }

  /**
   * Delete the configuration.
   *
   * @param key The key used to uniquely identify the configuration.
   *
   * @return True if the configuration was deleted or false otherwise.
   */
  deleteConfiguration(key: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(
      environment.configurationServiceUrlPrefix + '/configurations/' + encodeURIComponent(key),
      {observe: 'response'})
      .pipe(map((httpResponse: HttpResponse<boolean>) => {
        return httpResponse.status === 204;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'ConfigurationNotFoundError') {
            return throwError(new ConfigurationNotFoundError(this.i18n({
              id: '@@configuration_service_the_configuration_could_not_be_found',
              value: 'The configuration could not be found.'
            }), apiError));
          } else {
            return throwError(new ConfigurationServiceError(this.i18n({
              id: '@@configuration_service_failed_to_delete_the_configuration',
              value: 'Failed to delete the configuration.'
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
   * Retrieve the configuration.
   *
   * @param key The key used to uniquely identify the configuration.
   *
   * @return The configuration.
   */
  getConfiguration(key: string): Observable<Configuration> {
    return this.httpClient.get<Configuration>(
      environment.configurationServiceUrlPrefix + '/configurations/' + encodeURIComponent(key),
      {reportProgress: true})
      .pipe(map((configuration: Configuration) => {
        return configuration;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          if (apiError.code === 'ConfigurationNotFoundError') {
            return throwError(new ConfigurationNotFoundError(this.i18n({
              id: '@@configuration_service_the_configuration_could_not_be_found',
              value: 'The configuration could not be found.'
            }), apiError));
          } else {
            return throwError(new ConfigurationServiceError(this.i18n({
              id: '@@configuration_service_failed_to_retrieve_the_configuration',
              value: 'Failed to retrieve the configuration.'
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
   * Retrieve the configuration value.
   *
   * @param key The key used to uniquely identify the configuration.
   *
   * @return The configuration value.
   */
  getConfigurationValue(key: string): Observable<string> {
    return this.httpClient.get<string>(
      environment.configurationServiceUrlPrefix + '/configurations/' + encodeURIComponent(key) +
      '/value', {
        reportProgress: true
      }).pipe(map((value: string) => {
      return value;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        if (apiError.code === 'ConfigurationNotFoundError') {
          return throwError(new ConfigurationNotFoundError(this.i18n({
            id: '@@configuration_service_the_configuration_could_not_be_found',
            value: 'The configuration could not be found.'
          }), apiError));
        } else {
          return throwError(new ConfigurationServiceError(this.i18n({
            id: '@@configuration_service_failed_to_retrieve_the_configuration_value',
            value: 'Failed to retrieve the configuration value.'
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
   * Retrieve all the configurations.
   *
   * @return The configurations.
   */
  getConfigurations(): Observable<Configuration[]> {
    return this.httpClient.get<Configuration[]>(
      environment.configurationServiceUrlPrefix + '/configurations', {reportProgress: true})
      .pipe(map((configurations: Configuration[]) => {
        return configurations;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (ApiError.isApiError(httpErrorResponse)) {
          const apiError: ApiError = new ApiError(httpErrorResponse);

          return throwError(new CodesServiceError(this.i18n({
            id: '@@configuration_service_failed_to_retrieve_the_configurations',
            value: 'Failed to retrieve the configurations.'
          }), apiError));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
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
    return this.httpClient.post<boolean>(
      environment.configurationServiceUrlPrefix + '/configurations', configuration,
      {observe: 'response'}).pipe(map((httpResponse: HttpResponse<boolean>) => {
      return httpResponse.status === 204;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (ApiError.isApiError(httpErrorResponse)) {
        const apiError: ApiError = new ApiError(httpErrorResponse);

        return throwError(new CodesServiceError(this.i18n({
          id: '@@configuration_service_failed_to_save_the_configuration',
          value: 'Failed to save the configuration.'
        }), apiError));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(new CommunicationError(httpErrorResponse, this.i18n));
      } else {
        return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
      }
    }));
  }
}

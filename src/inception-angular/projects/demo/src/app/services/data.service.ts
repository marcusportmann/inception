/*
 * Copyright 2022 Marcus Portmann
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
  ResponseConverter, ServiceUnavailableError
} from 'ngx-inception/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Data} from './data';

/**
 * The Data Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class DataService {

  /**
   * Constructs a new DataService.
   *
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig, private httpClient: HttpClient) {
    console.log('Initializing the Data Service');
  }

  /**
   * Retrieve the data.
   *
   * @return The data.
   */
  @ResponseConverter
  getData(): Observable<Data> {
    return this.httpClient.get<Data>(this.config.apiUrlPrefix + '/data/data',
      {reportProgress: true})
    .pipe(map((data: Data) => {
      return data;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
        return throwError(() => new InvalidArgumentError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the data.', httpErrorResponse));
    }));
  }

  /**
   * Validate the data.
   *
   * @param code The data to validate
   */
  validateData(data: Data): Observable<boolean> {
    return this.httpClient.post<boolean>(
      this.config.apiUrlPrefix + '/data/validate', data,
      {observe: 'response'})
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

      return throwError(() => new ServiceUnavailableError('Failed to validate the data.', httpErrorResponse));
    }));
  }
}

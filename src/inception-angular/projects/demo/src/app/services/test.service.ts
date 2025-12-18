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

import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import {
  AccessDeniedError, CommunicationError, INCEPTION_CONFIG, InceptionConfig, InvalidArgumentError,
  ResponseConverter, ServiceUnavailableError
} from 'ngx-inception/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

/**
 * The Test Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class TestService {
  private config = inject<InceptionConfig>(INCEPTION_CONFIG);
  private httpClient = inject(HttpClient);

  /**
   * Constructs a new TestService.
   *
   * @param config     The Inception configuration.
   * @param httpClient The HTTP client.
   */
  constructor() {
    console.log('Initializing the Test Service');
  }

  /**
   * Test the exception handling.
   */
  @ResponseConverter
  testExceptionHandling(): Observable<boolean> {
    return this.httpClient
      .get<boolean>(this.config.apiUrlPrefix + '/test/test-exception-handling', {
        observe: 'response'
      })
      .pipe(
        map((httpResponse: HttpResponse<boolean>) => {
          return httpResponse.status === 200;
        }),
        catchError((httpErrorResponse: HttpErrorResponse) => {
          if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
            return throwError(() => new AccessDeniedError(httpErrorResponse));
          } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
            return throwError(() => new CommunicationError(httpErrorResponse));
          } else if (InvalidArgumentError.isInvalidArgumentError(httpErrorResponse)) {
            return throwError(() => new InvalidArgumentError(httpErrorResponse));
          }

          return throwError(
            () =>
              new ServiceUnavailableError(
                'Testing the exception handling at ' + new Date().toISOString() + '.',
                httpErrorResponse
              )
          );
        })
      );
  }
}

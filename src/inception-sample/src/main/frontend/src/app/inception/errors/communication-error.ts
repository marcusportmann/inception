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

import {Error} from './error';
import {HttpErrorResponse} from '@angular/common/http';
import {I18n} from '@ngx-translate/i18n-polyfill';

/**
 * The CommunicationError class holds the information for a communication error.
 *
 * @author Marcus Portmann
 */
export class CommunicationError extends Error {

  /**
   *  The HTTP status-code for the error.
   */
  status: number;

  /**
   * The HTTP reason-phrase for the HTTP status-code for the error.
   */
  statusText: string;

  /**
   * The optional URL for the HTTP request that resulted in the error.
   */
  url?: string;

  /**
   * Constructs a new CommunicationError.
   *
   * @param httpErrorResponse The HTTP error response containing the error information.
   * @param i18n              The internationalization service.
   */
  constructor(httpErrorResponse: HttpErrorResponse, i18n: I18n) {

    super(i18n({
      id: '@@communication_error_a_communication_error_occurred',
      value: 'A communication error occurred.'
    }));

    this.status = httpErrorResponse.status;
    this.statusText = httpErrorResponse.statusText;
    this.url = httpErrorResponse.url;
  }

  /**
   * Returns whether the specified HTTP error response is as a result of a communication error.
   *
   * @param httpErrorResponse The HTTP error response.
   *
   * @return True if the HTTP error response is as a result of a communication error or false
   *         otherwise.
   */
  static isCommunicationError(httpErrorResponse: HttpErrorResponse): boolean {
    return (httpErrorResponse.name === 'HttpErrorResponse') && (httpErrorResponse.status === 0) &&
      httpErrorResponse.error && (httpErrorResponse.error instanceof ProgressEvent) &&
      (httpErrorResponse.error.type === 'error');
  }
}

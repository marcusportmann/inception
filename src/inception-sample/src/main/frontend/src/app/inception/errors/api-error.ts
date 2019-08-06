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

import {HttpErrorResponse} from '@angular/common/http';
import {ValidationError} from './validation-error';

/**
 * The ApiError class holds the information for an API error returned by a RESTful API.
 *
 * @author Marcus Portmann
 */
export class ApiError {

  /**
   * The optional error detail.
   */
  detail?: string;

  /**
   * The optional fully qualified name of the exception associated with the error.
   */
  exception?: string;

  /**
   * The error message.
   */
  message: string;

  /**
   * The optional name of the entity associated with the error e.g. the name of the argument or
   * parameter.
   */
  name?: string;

  /**
   * The optional stack trace associated with the error.
   */
  stackTrace?: string;

  /**
   *  The HTTP status-code for the error.
   */
  status: number;

  /**
   * The HTTP reason-phrase for the HTTP status-code for the error.
   */
  statusText: string;

  /**
   * The date and time the error occurred.
   */
  timestamp: Date;

  /**
   * The URI for the HTTP request that resulted in the error.
   */
  uri: string;

  /**
   * The URL for the HTTP request that result in the error.
   */
  url: string;

  /**
   * The optional validation errors associated with the error.
   */
  validationErrors?: ValidationError[];

  /**
   * Constructs a new RestControllerError.
   *
   * @param httpErrorResponse The HTTP error response containing the error information.
   */
  constructor(httpErrorResponse: HttpErrorResponse) {
    this.timestamp = new Date(httpErrorResponse.error.timestamp);
    this.message = httpErrorResponse.error.message;
    this.status = httpErrorResponse.error.status;
    this.statusText = httpErrorResponse.error.statusText;
    this.uri = httpErrorResponse.error.uri;
    this.detail = httpErrorResponse.error.detail;
    this.exception = httpErrorResponse.error.exception;
    this.stackTrace = httpErrorResponse.error.stackTrace;
    this.name = httpErrorResponse.error.name;
    this.validationErrors = httpErrorResponse.error.validationErrors;
    this.url = (!!httpErrorResponse.url) ? httpErrorResponse.url : '';
  }

  /**
   * Returns whether the specified HTTP error response is as a result of an API error.
   *
   * @param httpErrorResponse The HTTP error response.
   *
   * @return True if the HTTP error response is as a result of an API error or false otherwise.
   */
  static isApiError(httpErrorResponse: HttpErrorResponse): boolean {
    return !!((httpErrorResponse.name === 'HttpErrorResponse') && httpErrorResponse.error &&
      (httpErrorResponse.error.timestamp) && (httpErrorResponse.error.message) &&
      (httpErrorResponse.error.status) && (httpErrorResponse.error.statusText) &&
      (httpErrorResponse.error.uri));
  }
}



/*
 * Copyright 2018 Marcus Portmann
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

import {Error} from "./error";
import {HttpErrorResponse} from "@angular/common/http";

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
   * The URL for the HTTP request that resulted in the error.
   */
  url?: string;

  /**
   * Constructs a new Error.
   *
   * @param {Date} timestamp The date and time the error occurred.
   * @param {string} message The error message.
   * @param {number} status The HTTP status-code for the error.
   * @param {string} statusText The HTTP reason-phrase for the HTTP status-code for the error.
   * @param {string} uri The URL for the HTTP request that resulted in the error.
   */
  constructor(timestamp: Date, message: string, status: number, statusText: string, url?: string) {
    super(timestamp, message);

    this.status = status;
    this.statusText = statusText;
    this.url = url;
  }

  /**
   * Constructs a new CommunicationError from the HTTP error response.
   *
   * @param {HttpErrorResponse} httpErrorResponse The HTTP error response.
   *
   * @return the new CommunicationError
   */
  static fromHttpErrorResponse(httpErrorResponse: HttpErrorResponse) : CommunicationError {

    return new CommunicationError(new Date(), httpErrorResponse.status, httpErrorResponse.statusText, httpErrorResponse.url)
  }

  /**
   * Returns whether the specified HTTP error response is as a result of a communication error.
   *
   * @param httpErrorResponse The HTTP error response.
   *
   * @return {boolean} True if the HTTP error response is as a result of a communication error or
   *                   false otherwise.
   */
  static isCommunicationError(httpErrorResponse: HttpErrorResponse): boolean {
    if ((httpErrorResponse.name === 'HttpErrorResponse')
      && (httpErrorResponse.status == 0)
      && httpErrorResponse.error
      && (httpErrorResponse.error instanceof ProgressEvent)
      && (httpErrorResponse.error.type === 'error')) {
      return true;
    }
    else {
      return false;
    }
  }
}

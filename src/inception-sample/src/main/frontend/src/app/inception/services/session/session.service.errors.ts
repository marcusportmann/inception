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

import {Error} from "../../errors/error";
import {HttpErrorResponse} from "@angular/common/http";

/**
 * The LoginError class holds the information for a login error.
 *
 * @author Marcus Portmann
 */
export class LoginError extends Error {

  /**
   * Constructs a new LoginError.
   *
   * @param {Date} timestamp The date and time the error occurred.
   * @param {string} message The message.
   * @param {string} detail  The optional detail.
   */
  constructor(timestamp: Date, message: string, detail?: string) {
    super(timestamp, message, detail);
  }

  /**
   * Constructs a new LoginError from the HTTP error response.
   *
   * @param {HttpErrorResponse} httpErrorResponse The HTTP error response.
   *
   * @return the new LoginError
   */
  static fromHttpErrorResponse(httpErrorResponse: HttpErrorResponse) : LoginError {

    return new LoginError(new Date(), httpErrorResponse.statusText, httpErrorResponse.error.error_description)
  }

  /**
   * Returns whether the specified HTTP error response is as a result of a login error.
   *
   * @param httpErrorResponse The HTTP error response.
   *
   * @return {boolean} True if the HTTP error response is as a result of a login error or false
   *                   otherwise.
   */
  static isLoginError(httpErrorResponse: HttpErrorResponse): boolean {
    if ((httpErrorResponse.name === 'HttpErrorResponse')
      && (httpErrorResponse.status == 0)
      && httpErrorResponse.error
      && (httpErrorResponse.error.error)) {
      return true;
    }
    else {
      return false;
    }
  }

}

/**
 * The SessionError class holds the information for a session error.
 *
 * @author Marcus Portmann
 */
export class SessionError extends Error {

  /**
   * Constructs a new SessionError.
   *
   * @param {Date} timestamp The date and time the error occurred.
   * @param {string} message The message.
   * @param {string} detail  The optional detail.
   */
  constructor(timestamp: Date, message: string, detail?: string) {
    super(timestamp, message, detail);
  }
}

/**
 * The SessionServiceError class holds the information for a Session Service error.
 *
 * @author Marcus Portmann
 */
export class SessionServiceError extends Error {

  /**
   * Constructs a new SessionServiceError.
   *
   * @param {Date} timestamp The date and time the error occurred.
   * @param {string} message The message.
   * @param {string} detail  The optional detail.
   */
  constructor(timestamp: Date, message: string, detail?: string) {
    super(timestamp, message, detail);
  }

  /**
   * Constructs a new SessionServiceError from the HTTP error response.
   *
   * @param {HttpErrorResponse} httpErrorResponse The HTTP error response.
   *
   * @return the new SessionServiceError
   */
  static fromHttpErrorResponse(httpErrorResponse: HttpErrorResponse) : SessionServiceError {

    // TODO: FIX THE ERROR BELOW AND ADD THE CORRECT DETAILS -- MARCUS

    return new SessionServiceError(new Date(), httpErrorResponse.statusText);

  }
}

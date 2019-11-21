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

import {ApiError} from './api-error';
import {HttpErrorResponse} from '@angular/common/http';
import {HttpError} from './http-error';

/**
 * The base class that all error classes should be derived from.
 *
 * @author Marcus Portmann
 */
export class Error {

  /**
   * The optional cause of the error.
   */
  cause?: ApiError | HttpErrorResponse | HttpError;

  /**
   * The error message.
   */
  message: string;

  /**
   * The date and time the error occurred.
   */
  timestamp: Date;

  /**
   * Constructs a new Error.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {

    this.message = message;
    this.timestamp = new Date();

    if (cause) {
      if (cause instanceof ApiError) {
        this.timestamp = cause.timestamp;
        this.cause = cause;
      } else if (cause instanceof HttpErrorResponse) {
        if (cause.error) {
          this.cause =
            new HttpError(cause.error.error ? cause.error.error : '',
              cause.error.error_description ? cause.error.error_description :
                '', cause.message, cause.status, cause.statusText,
              cause.url ? cause.url : '');
        } else {
          this.cause = new HttpError('', '', cause.message, cause.status,
            cause.statusText, cause.url ? cause.url : '');
        }
      } else {
        this.cause = cause;
      }
    }
  }
}



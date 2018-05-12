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
import {ValidationError} from "./validation-error";

/**
 * The RestControllerError class provides the base class that all error classes, which represent
 * errors returned by REST controllers, should be derived from.
 *
 * @author Marcus Portmann
 */
export class RestControllerError extends Error {

  /**
   * The URI for the HTTP request that resulted in the error.
   */
  uri: string;

  /**
   *  The HTTP status-code for the error.
   */
  status: number;

  /**
   * The HTTP reason-phrase for the HTTP status-code for the error.
   */
  statusText: string;

  /**
   * The optional fully qualified name of the exception associated with the error.
   */
  exception?: string;

  /**
   * The optional stack trace associated with the error.
   */
  stackTrace?: string;

  /**
   * The optional name of the entity associated with the error e.g. the name of the argument or
   * parameter.
   */
  name?: string;

  /**
   * The optional validation errors associated with the error.
   */
  validationErrors?: ValidationError[];

  /**
   * Constructs a new RestControllerError.
   *
   * @param {HttpErrorResponse} httpErrorResponse The HTTP error response containing the error
   *                                              information returned by the REST controller.
   */
  constructor(httpErrorResponse: HttpErrorResponse) {
    super(httpErrorResponse.error.timestamp, httpErrorResponse.error.message, httpErrorResponse.error.detail);

    this.uri = httpErrorResponse.error.uri;
    this.status = httpErrorResponse.error.status;
    this.statusText = httpErrorResponse.error.statusText;
    this.exception = httpErrorResponse.error.exception;
    this.stackTrace = httpErrorResponse.error.stackTrace;
    this.name = httpErrorResponse.error.name;
    this.validationErrors = httpErrorResponse.error.validationErrors;
  }
}



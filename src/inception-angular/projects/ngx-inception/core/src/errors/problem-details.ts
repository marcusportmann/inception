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

import { HttpErrorResponse } from '@angular/common/http';
import {ValidationError} from './validation-error';

/**
 * The ProblemDetails class holds holds the information for a Problem Details Object as defined in
 * RFC 7807.
 *
 * @author Marcus Portmann
 */
export class ProblemDetails {

  /**
   * The human-readable explanation specific to this occurrence of the problem.
   */
  detail: string;

  /**
   * The name of the parameter associated with the problem.
   */
  parameter?: string;

  /**
   * The stack trace generated by the origin server for the problem.
   */
  stackTrace?: string;

  /**
   * The HTTP status code generated by the origin server for this occurrence of the problem.
   */
  status: number;

  /**
   * The date and time the error occurred.
   */
  timestamp: Date;

  /**
   * The short, human-readable summary of the problem type; it should not change from occurrence to
   * occurrence of the problem, except for purposes of localization.
   */
  title: string;

  /**
   * The URI reference that identifies the problem type.
   */
  type: string;

  /**
   * The validation errors associated with the problem.
   */
  validationErrors?: ValidationError[];

  /**
   * Constructs a new ProblemDetails.
   *
   * @param httpErrorResponse The HTTP error response containing the problem details information.
   */
  constructor(httpErrorResponse: HttpErrorResponse) {
    this.detail = httpErrorResponse.error.detail;
    this.parameter = httpErrorResponse.error.parameter;
    this.stackTrace = httpErrorResponse.error.stackTrace;
    this.status = httpErrorResponse.error.status;
    this.timestamp = new Date(httpErrorResponse.error.timestamp);
    this.title = httpErrorResponse.error.title;
    this.type = httpErrorResponse.error.type;
    this.validationErrors = httpErrorResponse.error.validationErrors;
  }

  /**
   * Returns whether the specified HTTP error response is as a result of an API error represented as
   * a problem details object.
   *
   * @param httpErrorResponse The HTTP error response.
   * @param errorCode         The error code.
   *
   * @return True if the HTTP error response is as a result of an API error represented as a problem
   *         details object.
   */
  static isProblemDetails(httpErrorResponse: HttpErrorResponse, type?: string): boolean {
    return !!((httpErrorResponse.name === 'HttpErrorResponse') && httpErrorResponse.error &&
      (httpErrorResponse.error.timestamp) && (httpErrorResponse.error.type) &&
      (httpErrorResponse.error.title) && (httpErrorResponse.error.status) &&
      (httpErrorResponse.error.detail) && (((type == null) || (type == undefined)) ||
        ((httpErrorResponse.error.type) && (httpErrorResponse.error.type == type))));
  }
}



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

import {HttpErrorResponse} from "@angular/common/http";
import {RestControllerError} from "../../errors/rest-controller-error";

/**
 * The SecurityServiceError class holds the information for a Security Service error.
 *
 * @author Marcus Portmann
 */
export class SecurityServiceError extends RestControllerError {

  /**
   * Constructs a new SecurityServiceError.
   *
   * @param {HttpErrorResponse} httpErrorResponse The HTTP error response containing the error
   *                                              information returned by the Security Service.
   */
  constructor(httpErrorResponse: HttpErrorResponse) {
    super(httpErrorResponse);
  }
}

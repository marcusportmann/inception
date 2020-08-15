/*
 * Copyright 2020 Marcus Portmann
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

/**
 * The SystemUnavailableError class holds the information for a system unavailable error.
 *
 * @author Marcus Portmann
 */
export class SystemUnavailableError extends Error {

  /**
   * Constructs a new SystemUnavailableError.
   *
   * @param httpErrorResponse The HTTP error response containing the error information.
   */
  constructor(httpErrorResponse: HttpErrorResponse) {
    super($localize`:@@core_system_unavailable_error:An error has occurred and your request could not be processed at this time.`, httpErrorResponse);
  }
}

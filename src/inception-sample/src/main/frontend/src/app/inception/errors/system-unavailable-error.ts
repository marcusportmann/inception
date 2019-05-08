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
 * The SystemUnavailableError class holds the information for a system unavailable error.
 *
 * @author Marcus Portmann
 */
export class SystemUnavailableError extends Error {

  /**
   * Constructs a new SystemUnavailableError.
   *
   * @param httpErrorResponse The HTTP error response containing the error information.
   * @param i18n              The internationalization service.
   */
  constructor(httpErrorResponse: HttpErrorResponse, i18n: I18n) {

    super(i18n({id: '@@system_unavailable_error',
      value: 'An error has occurred and the system is unable to process your request at this time.'}));

    // TODO: Retrieve error details from HttpErrorResponse and save
  }
}

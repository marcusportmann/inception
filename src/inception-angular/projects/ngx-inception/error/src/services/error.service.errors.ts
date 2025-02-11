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
import {Error, HttpError, ProblemDetails} from 'ngx-inception/core';

/**
 * The ErrorReportNotFoundError class holds the information for an error report not found error.
 *
 * @author Marcus Portmann
 */
export class ErrorReportNotFoundError extends Error {

  static readonly TYPE = 'https://inception.digital/problems/error/error-report-not-found';

  /**
   * Constructs a new ErrorReportNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@error_error_report_not_found_error:The error report could not be found.`,
      cause);
  }
}

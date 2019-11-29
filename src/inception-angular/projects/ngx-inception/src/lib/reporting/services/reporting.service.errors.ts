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
import {HttpError} from '../../core/errors/http-error';
import {Error} from '../../core/errors/error';
import {ApiError} from '../../core/errors/api-error';
import {I18n} from '@ngx-translate/i18n-polyfill';

/**
 * The DuplicateReportDefinitionError class holds the information for a duplicate report definition
 * error.
 *
 * @author Marcus Portmann
 */
export class DuplicateReportDefinitionError extends Error {

  /**
   * Constructs a new DuplicateReportDefinitionError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@reporting_duplicate_report_definition_error',
      value: 'The report definition already exists.'
    }), cause);
  }
}

/**
 * The ReportDefinitionNotFoundError class holds the information for a report definition not found
 * error.
 *
 * @author Marcus Portmann
 */
export class ReportDefinitionNotFoundError extends Error {

  /**
   * Constructs a new ReportDefinitionNotFoundError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@reporting_report_definition_not_found_error',
      value: 'The report definition could not be found.'
    }), cause);
  }
}

/**
 * The ReportingServiceError class holds the information for a Reporting Service error.
 *
 * @author Marcus Portmann
 */
export class ReportingServiceError extends Error {

  /**
   * Constructs a new ReportingServiceError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

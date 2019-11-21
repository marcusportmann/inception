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
import {HttpError} from '../../errors/http-error';
import {Error} from '../../errors/error';
import {ApiError} from '../../errors/api-error';
import {I18n} from '@ngx-translate/i18n-polyfill';

/**
 * The CodeCategoryNotFoundError class holds the information for a code category not found error.
 *
 * @author Marcus Portmann
 */
export class CodeCategoryNotFoundError extends Error {

  /**
   * Constructs a new CodeCategoryNotFoundError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@codes_code_category_not_found_error',
      value: 'The code category could not be found.'
    }), cause);
  }
}

/**
 * The CodeNotFoundError class holds the information for a code not found error.
 *
 * @author Marcus Portmann
 */
export class CodeNotFoundError extends Error {

  /**
   * Constructs a new CodeNotFoundError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@codes_code_not_found_error',
      value: 'The code could not be found.'
    }), cause);
  }
}

/**
 * The CodesServiceError class holds the information for a Codes Service error.
 *
 * @author Marcus Portmann
 */
export class CodesServiceError extends Error {

  /**
   * Constructs a new CodesServiceError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The DuplicateCodeCategoryError class holds the information for a duplicate code category error.
 *
 * @author Marcus Portmann
 */
export class DuplicateCodeCategoryError extends Error {

  /**
   * Constructs a new DuplicateCodeCategoryError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@codes_duplicate_code_error',
      value: 'The code category already exists.'
    }), cause);
  }
}

/**
 * The DuplicateCodeError class holds the information for a duplicate code error.
 *
 * @author Marcus Portmann
 */
export class DuplicateCodeError extends Error {

  /**
   * Constructs a new DuplicateCodeError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@codes_duplicate_code_error',
      value: 'The code already exists.'
    }), cause);
  }
}

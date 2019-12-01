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

/**
 * The DuplicateJobError class holds the information for a duplicate job error.
 *
 * @author Marcus Portmann
 */
export class DuplicateJobError extends Error {

  /**
   * Constructs a new DuplicateJobError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super('The job already exists.', cause);
  }
}

/**
 * The JobNotFoundError class holds the information for a job not found error.
 *
 * @author Marcus Portmann
 */
export class JobNotFoundError extends Error {

  /**
   * Constructs a new JobNotFoundError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super('The job could not be found.', cause);
  }
}

/**
 * The SchedulerServiceError class holds the information for a Scheduler Service error.
 *
 * @author Marcus Portmann
 */
export class SchedulerServiceError extends Error {

  /**
   * Constructs a new SchedulerServiceError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

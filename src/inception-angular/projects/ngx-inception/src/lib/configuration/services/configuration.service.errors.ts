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

import {HttpErrorResponse} from '@angular/common/http';
import {HttpError} from '../../core/errors/http-error';
import {Error} from '../../core/errors/error';
import {ProblemDetails} from '../../core/errors/problem-details';

/**
 * The ConfigurationNotFoundError class holds the information for a configuration not found error.
 *
 * @author Marcus Portmann
 */
export class ConfigurationNotFoundError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/configuration/configuration-not-found';

  /**
   * Constructs a new ConfigurationNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@configuration_configuration_not_found_error:The configuration could not be found.`, cause);
  }
}



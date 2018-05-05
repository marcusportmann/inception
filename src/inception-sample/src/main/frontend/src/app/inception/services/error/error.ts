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

/**
 *
 */
export class ValidationError {

};


/**
 * The Error class provides the base class that all error classes should be derived from.
 *
 * @author Marcus Portmann
 */
export abstract class Error {

  /**
   * The URI for the HTTP request that resulted in the error.
   */
  uri: string;

  /**
   * The The date and time the error occurred.
   */
  timestamp: Date;

  /**
   *  The HTTP status-code for the error.
   */
  status: number;

  /**
   * The HTTP reason-phrase for the HTTP status-code for the error.
   */
  reasonPhrase: string;

  /**
   * The message.
   */
  message: string;

  /**
   * The detail.
   */
  detail?: string;

  /**
   * The fully qualified name of the exception associated with the error.
   */
  exception?: string;

  /**
   * The stack trace associated with the error.
   */
  stackTrace?: string;

  /**
   * The optional name of the entity associated with the error e.g. the name of the argument or
   * parameter.
   */
  name: string;

  /**
   * The validation errors associated with the error.
   */
  validationErrprs: ValidationError[];

}

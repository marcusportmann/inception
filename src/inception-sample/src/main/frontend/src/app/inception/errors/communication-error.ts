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

import {Error} from "./error";

/**
 * The CommunicationError class holds the information for a communication error.
 *
 * @author Marcus Portmann
 */
export class CommunicationError extends Error {

  /**
   * Constructs a new CommunicationError.
   *
   * @param {Date} timestamp The date and time the error occurred.
   * @param {string} message The message.
   * @param {string} detail  The optional detail.
   */
  constructor(timestamp: Date, message: string, detail?: string) {
    super(timestamp, message, detail);
  }
}

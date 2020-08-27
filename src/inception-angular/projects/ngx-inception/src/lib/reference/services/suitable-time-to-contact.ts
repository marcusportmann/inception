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

/**
 * The SuitableTimeToContact class holds the information for a suitable time to contact.
 *
 * @author Marcus Portmann
 */
export class SuitableTimeToContact {

  /**
   * The code for the suitable time to contact.
   */
  code: string;

  /**
   * The description for the suitable time to contact.
   */
  description: string;

  /**
   * The Unicode locale identifier for the suitable time to contact.
   */
  localeId: string;

  /**
   * The name of the suitable time to contact.
   */
  name: string;

  /**
   * The sort index for the suitable time to contact.
   */
  sortIndex: number;

  /**
   * Constructs a new SuitableTimeToContact.
   *
   * @param code        The code for the suitable time to contact.
   * @param localeId    The Unicode locale identifier for the suitable time to contact.
   * @param sortIndex   The sort index for the suitable time to contact.
   * @param name        The name of the suitable time to contact.
   * @param description The description for the suitable time to contact.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}

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
 * The Occupation class holds the information for an occupation.
 *
 * @author Marcus Portmann
 */
export class Occupation {

  /**
   * The code for the occupation.
   */
  code: string;

  /**
   * The description for the occupation.
   */
  description: string;

  /**
   * The Unicode locale identifier for the occupation.
   */
  localeId: string;

  /**
   * The name of the occupation.
   */
  name: string;

  /**
   * The sort index for the occupation.
   */
  sortIndex: number;

  /**
   * Constructs a new Occupation.
   *
   * @param code        The code for the occupation.
   * @param localeId    The Unicode locale identifier for the occupation.
   * @param sortIndex   The sort index for the occupation.
   * @param name        The name of the occupation.
   * @param description The description for the occupation.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}

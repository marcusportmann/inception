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
 * The MinorType class holds the information for a minor type.
 *
 * @author Marcus Portmann
 */
export class MinorType {

  /**
   * The code for the minor type.
   */
  code: string;

  /**
   * The description for the minor type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the minor type.
   */
  localeId: string;

  /**
   * The name of the minor type.
   */
  name: string;

  /**
   * The sort index for the minor type.
   */
  sortIndex: number;

  /**
   * Constructs a new MinorType.
   *
   * @param code        The code for the minor type.
   * @param localeId    The Unicode locale identifier for the minor type.
   * @param sortIndex   The sort index for the minor type.
   * @param name        The name of the minor type.
   * @param description The description for the minor type.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}

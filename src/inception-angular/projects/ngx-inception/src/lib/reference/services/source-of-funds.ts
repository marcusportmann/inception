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
 * The SourceOfFunds class holds the information for a source of funds.
 *
 * @author Marcus Portmann
 */
export class SourceOfFunds {

  /**
   * The code for the source of funds.
   */
  code: string;

  /**
   * The description for the source of funds.
   */
  description: string;

  /**
   * The Unicode locale identifier for the source of funds.
   */
  localeId: string;

  /**
   * The name of the source of funds.
   */
  name: string;

  /**
   * The sort index for the source of funds.
   */
  sortIndex: number;

  /**
   * Constructs a new SourceOfFunds.
   *
   * @param code        The code for the source of funds.
   * @param localeId    The Unicode locale identifier for the source of funds.
   * @param sortIndex   The sort index for the source of funds.
   * @param name        The name of the source of funds.
   * @param description The description for the source of funds.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}

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
 * The ResidentialStatus class holds the information for a residential status.
 *
 * @author Marcus Portmann
 */
export class ResidentialStatus {

  /**
   * The code for the residential status.
   */
  code: string;

  /**
   * The description for the residential status.
   */
  description: string;

  /**
   * The Unicode locale identifier for the residential status.
   */
  localeId: string;

  /**
   * The name of the residential status.
   */
  name: string;

  /**
   * The sort index for the residential status.
   */
  sortIndex: number;

  /**
   * Constructs a new ResidentialStatus.
   *
   * @param code        The code for the residential status.
   * @param localeId    The Unicode locale identifier for the residential status.
   * @param sortIndex   The sort index for the residential status.
   * @param name        The name of the residential status.
   * @param description The description for the residential status.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}

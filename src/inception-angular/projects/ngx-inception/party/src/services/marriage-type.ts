/*
 * Copyright 2021 Marcus Portmann
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
 * The MarriageType class holds the information for a marriage type.
 *
 * @author Marcus Portmann
 */
export class MarriageType {

  /**
   * The code for the marriage type.
   */
  code: string;

  /**
   * The description for the marriage type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the marriage type.
   */
  localeId: string;

  /**
   * The code for the marital status the marriage type is associated with.
   */
  maritalStatus: string;

  /**
   * The name of the marriage type.
   */
  name: string;

  /**
   * The sort index for the marriage type.
   */
  sortIndex: number;

  /**
   * Constructs a new MarriageType.
   *
   * @param maritalStatus The code for the marital status the marriage type is associated with.
   * @param code          The code for the marriage type.
   * @param localeId      The Unicode locale identifier for the marriage type.
   * @param sortIndex     The sort index for the marriage type.
   * @param name          The name of the marriage type.
   * @param description   The description for the marriage type.
   */
  constructor(maritalStatus: string, code: string, localeId: string, sortIndex: number,
              name: string, description: string) {
    this.maritalStatus = maritalStatus;
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}

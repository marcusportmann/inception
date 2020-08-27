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
 * The PermitType class holds the information for a permit type.
 *
 * @author Marcus Portmann
 */
export class PermitType {

  /**
   * The code for the permit type.
   */
  code: string;

  /**
   * The code identifying the country of issue for the permit type.
   */
  countryOfIssue: string;

  /**
   * The description for the permit type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the permit type.
   */
  localeId: string;

  /**
   * The name of the permit type.
   */
  name: string;

  /**
   * The sort index for the permit type.
   */
  sortIndex: number;

  /**
   * Constructs a new PermitType.
   *
   * @param code           The code for the permit type.
   * @param localeId       The Unicode locale identifier for the permit type.
   * @param sortIndex      The sort index for the permit type.
   * @param name           The name of the permit type.
   * @param description    The description for the permit type.
   * @param countryOfIssue The code identifying the country of issue for the permit type.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              countryOfIssue: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.countryOfIssue = countryOfIssue;
  }
}

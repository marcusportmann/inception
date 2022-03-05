/*
 * Copyright 2022 Marcus Portmann
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
 * The ResidencePermitType class holds the information for a residence permit type.
 *
 * @author Marcus Portmann
 */
export class ResidencePermitType {

  /**
   * The code for the residence permit type.
   */
  code: string;

  /**
   * The ISO 3166-1 alpha-2 code for the country of issue for the residence permit type.
   */
  countryOfIssue: string;

  /**
   * The description for the residence permit type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the residence permit type.
   */
  localeId: string;

  /**
   * The name of the residence permit type.
   */
  name: string;

  /**
   * The regular expression pattern used to validate a number for the residence permit type.
   */
  pattern?: string;

  /**
   * The sort index for the residence permit type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the residence permit type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new ResidencePermitType.
   *
   * @param code           The code for the residence permit type.
   * @param localeId       The Unicode locale identifier for the residence permit type.
   * @param sortIndex      The sort index for the residence permit type.
   * @param name           The name of the residence permit type.
   * @param description    The description for the residence permit type.
   * @param countryOfIssue The ISO 3166-1 alpha-2 code for the country of issue for the residence
   *                       permit type.
   * @param pattern        The regular expression pattern used to validate a number for the
   *                       residence permit type.
   * @param tenantId       The ID for the tenant the residence permit type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              countryOfIssue: string, pattern?: string, tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.countryOfIssue = countryOfIssue;
    this.pattern = pattern;
    this.tenantId = tenantId;
  }
}

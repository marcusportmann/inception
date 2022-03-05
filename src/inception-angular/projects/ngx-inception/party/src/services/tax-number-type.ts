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
 * The TaxNumberType class holds the information for a tax number type.
 *
 * @author Marcus Portmann
 */
export class TaxNumberType {

  /**
   * The code for the tax number type.
   */
  code: string;

  /**
   * The ISO 3166-1 alpha-2 code for the country of issue for the tax number type.
   */
  countryOfIssue: string;

  /**
   * The description for the tax number type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the tax number type.
   */
  localeId: string;

  /**
   * The name of the tax number type.
   */
  name: string;

  /**
   * The codes for the party types the tax number type is associated with.
   */
  partyTypes: string[];

  /**
   * The regular expression pattern used to validate a number for the tax number type.
   */
  pattern?: string;

  /**
   * The sort index for the tax number type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the tax number type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new TaxNumberType.
   *
   * @param code           The code for the tax number type.
   * @param localeId       The Unicode locale identifier for the tax number type.
   * @param sortIndex      The sort index for the tax number type.
   * @param name           The name of the tax number type.
   * @param description    The description for the tax number type.
   * @param countryOfIssue The ISO 3166-1 alpha-2 code for the country of issue for the tax number
   *                       type.
   * @param partyTypes     The codes for the party types the tax number type is associated with.
   * @param pattern        The regular expression pattern used to validate a number for the tax
   *                       number type.
   * @param tenantId       The ID for the tenant the tax number type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              countryOfIssue: string, partyTypes: string[], pattern?: string, tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.countryOfIssue = countryOfIssue;
    this.partyTypes = partyTypes;
    this.pattern = pattern;
    this.tenantId = tenantId;
  }
}

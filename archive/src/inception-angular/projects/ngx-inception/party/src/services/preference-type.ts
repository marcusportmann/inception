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
 * The PreferenceType class holds the information for a preference type.
 *
 * @author Marcus Portmann
 */
export class PreferenceType {

  /**
   * The code for the preference type category the preference type is associated with.
   */
  category: string;

  /**
   * The code for the preference type.
   */
  code: string;

  /**
   * The description for the preference type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the preference type.
   */
  localeId: string;

  /**
   * The name of the preference type.
   */
  name: string;

  /**
   * The codes for the party types the preference type is associated with..
   */
  partyTypes: string[];

  /**
   * The regular expression pattern used to validate a value for the preference type.
   */
  pattern?: string;

  /**
   * The sort index for the preference type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the preference type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new PreferenceType.
   *
   * @param category    The code for the preference type category the preference type is associated
   *                    with.
   * @param code        The code for the preference type.
   * @param localeId    The Unicode locale identifier for the preference type.
   * @param sortIndex   The sort index for the preference type.
   * @param name        The name of the preference type.
   * @param description The description for the preference type.
   * @param partyTypes  The codes for the party types the preference type is associated with.
   * @param pattern     The regular expression pattern used to validate a value for the preference
   *                    type.
   * @param tenantId    The ID for the tenant the preference type is specific to.
   */
  constructor(category: string, code: string, localeId: string, sortIndex: number,
              name: string, description: string, partyTypes: string[], pattern?: string,
              tenantId?: string) {
    this.category = category;
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.partyTypes = partyTypes;
    this.pattern = pattern;
    this.tenantId = tenantId;
  }
}

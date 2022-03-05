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
 * The AttributeType class holds the information for an attribute type.
 *
 * @author Marcus Portmann
 */
export class AttributeType {

  /**
   * The code for the attribute type category the attribute type is associated with.
   */
  category: string;

  /**
   * The code for the attribute type.
   */
  code: string;

  /**
   * The description for the attribute type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the attribute type.
   */
  localeId: string;

  /**
   * The name of the attribute type.
   */
  name: string;

  /**
   * The codes for the party types the attribute type is associated with..
   */
  partyTypes: string[];

  /**
   * The regular expression pattern used to validate a string value for the attribute type.
   */
  pattern?: string;

  /**
   * The sort index for the attribute type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the attribute type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new AttributeType.
   *
   * @param category    The code for the attribute type category the attribute type is associated
   *                    with.
   * @param code        The code for the attribute type.
   * @param localeId    The Unicode locale identifier for the attribute type.
   * @param sortIndex   The sort index for the attribute type.
   * @param name        The name of the attribute type.
   * @param description The description for the attribute type.
   * @param partyTypes  The codes for the party types the attribute type is associated with.
   * @param pattern     The regular expression pattern used to validate a string value for the
   *                    attribute type.
   * @param tenantId    The ID for the tenant the attribute type is specific to.
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

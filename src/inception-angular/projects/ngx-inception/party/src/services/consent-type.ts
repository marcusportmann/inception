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
 * The ConsentType class holds the information for a consent type.
 *
 * @author Marcus Portmann
 */
export class ConsentType {

  /**
   * The code for the consent type.
   */
  code: string;

  /**
   * The description for the consent type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the consent type.
   */
  localeId: string;

  /**
   * The name of the consent type.
   */
  name: string;

  /**
   * The sort index for the consent type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the consent type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new ConsentType.
   *
   * @param code        The code for the consent type.
   * @param localeId    The Unicode locale identifier for the consent type.
   * @param sortIndex   The sort index for the consent type.
   * @param name        The name of the consent type.
   * @param description The description for the consent type.
   * @param tenantId    The ID for the tenant the consent type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.tenantId = tenantId;
  }
}

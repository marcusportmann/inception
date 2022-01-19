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
 * The MandataryType class holds the information for a mandatary type.
 *
 * @author Marcus Portmann
 */
export class MandataryType {

  /**
   * The code for the mandatary type.
   */
  code: string;

  /**
   * The description for the mandatary type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the mandatary type.
   */
  localeId: string;

  /**
   * The code for the mandate type the mandatary type is associated with.
   */
  mandateType: string;

  /**
   * The name of the mandatary type.
   */
  name: string;

  /**
   * The sort index for the mandatary type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the mandatary type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new MandataryType.
   *
   * @param mandateType The code for the mandate type the mandatary type is associated with.
   * @param code        The code for the mandatary type.
   * @param localeId    The Unicode locale identifier for the mandatary type.
   * @param sortIndex   The sort index for the mandatary type.
   * @param name        The name of the mandatary type.
   * @param description The description for the mandatary type.
   * @param tenantId    The ID for the tenant the mandatary type is specific to.
   */
  constructor(mandateType: string, code: string, localeId: string, sortIndex: number, name: string,
              description: string, tenantId?: string) {
    this.mandateType = mandateType;
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.tenantId = tenantId;
  }
}

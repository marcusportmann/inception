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
 * The RoleType class holds the information for a role type.
 *
 * @author Marcus Portmann
 */
export class RoleType {

  /**
   * The code for the role type.
   */
  code: string;

  /**
   * The description for the role type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the role type.
   */
  localeId: string;

  /**
   * The name of the role type.
   */
  name: string;

  /**
   * The codes for the party types the role type is associated with..
   */
  partyTypes: string[];

  /**
   * The sort index for the role type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the role type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new RoleType.
   *
   * @param code        The code for the role type.
   * @param localeId    The Unicode locale identifier for the role type.
   * @param sortIndex   The sort index for the role type.
   * @param name        The name of the role type.
   * @param description The description for the role type.
   * @param partyTypes  The codes for the party types the role type is associated with.
   * @param tenantId    The ID for the tenant the role type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              partyTypes: string[], tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.partyTypes = partyTypes;
    this.tenantId = tenantId;
  }
}

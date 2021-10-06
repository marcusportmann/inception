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
 * The PhysicalAddressPurpose class holds the information for a physical address purpose.
 *
 * @author Marcus Portmann
 */
export class PhysicalAddressPurpose {

  /**
   * The code for the physical address purpose.
   */
  code: string;

  /**
   * The description for the physical address purpose.
   */
  description: string;

  /**
   * The Unicode locale identifier for the physical address purpose.
   */
  localeId: string;

  /**
   * The name of the physical address purpose.
   */
  name: string;

  /**
   * The codes for the party types the physical address purpose is associated with.
   */
  partyTypes: string[];

  /**
   * The sort index for the physical address purpose.
   */
  sortIndex: number;

  /**
   * The Universally Unique Identifier (UUID) for the tenant the physical address purpose is
   * specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new PhysicalAddressPurpose.
   *
   * @param code        The code for the physical address purpose.
   * @param localeId    The Unicode locale identifier for the physical address purpose.
   * @param sortIndex   The sort index for the physical address purpose.
   * @param name        The name of the physical address purpose.
   * @param description The description for the physical address purpose.
   * @param partyTypes  The codes for the party types the physical address purpose is associated
   *                    with.
   * @param tenantId    The Universally Unique Identifier (UUID) for the tenant the physical address
   *                    purpose is specific to.
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

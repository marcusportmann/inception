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
 * The LockType class holds the information for a lock type.
 *
 * @author Marcus Portmann
 */
export class LockType {

  /**
   * The code for the lock type category the lock type is associated with.
   */
  category: string;

  /**
   * The code for the lock type.
   */
  code: string;

  /**
   * The description for the lock type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the lock type.
   */
  localeId: string;

  /**
   * The name of the lock type.
   */
  name: string;

  /**
   * The codes for the party types the lock type is associated with..
   */
  partyTypes: string[];

  /**
   * The sort index for the lock type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the lock type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new LockType.
   *
   * @param category    The code for the lock type category the lock type is associated with.
   * @param code        The code for the lock type.
   * @param localeId    The Unicode locale identifier for the lock type.
   * @param sortIndex   The sort index for the lock type.
   * @param name        The name of the lock type.
   * @param description The description for the lock type.
   * @param partyTypes  The codes for the party types the lock type is associated with.
   * @param tenantId    The ID for the tenant the lock type is specific to.
   */
  constructor(category: string, code: string, localeId: string, sortIndex: number,
              name: string, description: string, partyTypes: string[], tenantId?: string) {
    this.category = category;
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.partyTypes = partyTypes;
    this.tenantId = tenantId;
  }
}

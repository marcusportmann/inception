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
 * The PhysicalAddressType class holds the information for a physical address type.
 *
 * @author Marcus Portmann
 */
export class PhysicalAddressType {

  /**
   * The code for the physical address type.
   */
  code: string;

  /**
   * The description for the physical address type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the physical address type.
   */
  localeId: string;

  /**
   * The name of the physical address type.
   */
  name: string;

  /**
   * The sort index for the physical address type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the physical address type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new PhysicalAddressType.
   *
   * @param code        The code for the physical address type.
   * @param localeId    The Unicode locale identifier for the physical address type.
   * @param sortIndex   The sort index for the physical address type.
   * @param name        The name of the physical address type.
   * @param description The description for the physical address type.
   * @param tenantId    The ID for the tenant the physical address type is specific to.
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





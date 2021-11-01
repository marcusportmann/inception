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
 * The RolePurpose class holds the information for a purpose for a role.
 *
 * @author Marcus Portmann
 */
export class RolePurpose {

  /**
   * The code for the role purpose.
   */
  code: string;

  /**
   * The description for the role purpose.
   */
  description: string;

  /**
   * The Unicode locale identifier for the role purpose.
   */
  localeId: string;

  /**
   * The name of the role purpose.
   */
  name: string;

  /**
   * The sort index for the role purpose.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the role purpose is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new RolePurpose.
   *
   * @param code        The code for the role purpose.
   * @param localeId    The Unicode locale identifier for the role purpose.
   * @param sortIndex   The sort index for the role purpose.
   * @param name        The name of the role purpose.
   * @param description The description for the role purpose.
   * @param tenantId    The ID for the tenant the role purpose is specific to.
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

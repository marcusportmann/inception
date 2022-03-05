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
 * The NextOfKinType class holds the information for a next of kin type.
 *
 * @author Marcus Portmann
 */
export class NextOfKinType {

  /**
   * The code for the next of kin type.
   */
  code: string;

  /**
   * The description for the next of kin type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the next of kin type.
   */
  localeId: string;

  /**
   * The name of the next of kin type.
   */
  name: string;

  /**
   * The sort index for the next of kin type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the next of kin type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new NextOfKinType.
   *
   * @param code        The code for the next of kin type.
   * @param localeId    The Unicode locale identifier for the next of kin type.
   * @param sortIndex   The sort index for the next of kin type.
   * @param name        The name of the next of kin type.
   * @param description The description for the next of kin type.
   * @param tenantId    The ID for the tenant the next of kin type is specific to.
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

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
 * The SourceOfFunds class holds the information for a type of source of funds.
 *
 * @author Marcus Portmann
 */
export class SourceOfFundsType {

  /**
   * The code for the source of funds type.
   */
  code: string;

  /**
   * The description for the source of funds type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the source of funds type.
   */
  localeId: string;

  /**
   * The name of the source of funds type.
   */
  name: string;

  /**
   * The sort index for the source of funds type.
   */
  sortIndex: number;

  /**
   * The Universally Unique Identifier (UUID) for the tenant the source of funds type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new SourceOfFunds.
   *
   * @param code        The code for the source of funds.
   * @param localeId    The Unicode locale identifier for the source of funds.
   * @param sortIndex   The sort index for the source of funds.
   * @param name        The name of the source of funds.
   * @param description The description for the source of funds.
   * @param tenantId    The Universally Unique Identifier (UUID) for the tenant the source of funds
   *                    type is specific to.
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

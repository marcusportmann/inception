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
 * The Gender class holds the information for a gender.
 *
 * @author Marcus Portmann
 */
export class Gender {

  /**
   * The code for the gender.
   */
  code: string;

  /**
   * The description for the gender.
   */
  description: string;

  /**
   * The Unicode locale identifier for the gender.
   */
  localeId: string;

  /**
   * The name of the gender.
   */
  name: string;

  /**
   * The sort index for the gender.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the gender is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new Gender.
   *
   * @param code        The code for the gender.
   * @param localeId    The Unicode locale identifier for the gender.
   * @param sortIndex   The sort index for the gender.
   * @param name        The name of the gender.
   * @param description The description for the gender.
   * @param tenantId    The ID for the tenant the gender is specific to.
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

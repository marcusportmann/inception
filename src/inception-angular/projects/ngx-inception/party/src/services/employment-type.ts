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
 * The EmploymentType class holds the information for an employment type.
 *
 * @author Marcus Portmann
 */
export class EmploymentType {

  /**
   * The code for the employment type.
   */
  code: string;

  /**
   * The description for the employment type.
   */
  description: string;

  /**
   * The code for the employment status the employment type is associated with.
   */
  employmentStatus: string;

  /**
   * The Unicode locale identifier for the employment type.
   */
  localeId: string;

  /**
   * The name of the employment type.
   */
  name: string;

  /**
   * The sort index for the employment type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the employment type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new EmploymentType.
   *
   * @param employmentStatus The code for the employment status the employment type is associated
   *                         with.
   * @param code             The code for the employment type.
   * @param localeId         The Unicode locale identifier for the employment type.
   * @param sortIndex        The sort index for the employment type.
   * @param name             The name of the employment type.
   * @param description      The description for the employment type.
   * @param tenantId         The ID for the tenant the employment
   *                         type is specific to.
   */
  constructor(employmentStatus: string, code: string, localeId: string, sortIndex: number,
              name: string, description: string, tenantId?: string) {
    this.employmentStatus = employmentStatus;
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.tenantId = tenantId;
  }
}

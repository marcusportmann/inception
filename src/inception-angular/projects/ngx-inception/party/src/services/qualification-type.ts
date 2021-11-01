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
 * The QualificationType class holds the information for a qualification type.
 *
 * @author Marcus Portmann
 */
export class QualificationType {

  /**
   * The code for the qualification type.
   */
  code: string;

  /**
   * The description for the qualification type.
   */
  description: string;

  /**
   * The code for the field of study for the qualification type.
   */
  fieldOfStudy: string;

  /**
   * The Unicode locale identifier for the qualification type.
   */
  localeId: string;

  /**
   * The name of the qualification type.
   */
  name: string;

  /**
   * The sort index for the qualification type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the qualification type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new QualificationType.
   *
   * @param code          The code for the qualification type.
   * @param localeId      The Unicode locale identifier for the qualification type.
   * @param sortIndex     The sort index for the qualification type.
   * @param name          The name of the qualification type.
   * @param description   The description for the qualification type.
   * @param fieldOfStudy  The code for the field of study for the qualification type.
   * @param tenantId      The ID for the tenant the qualification type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              fieldOfStudy: string, tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.fieldOfStudy = fieldOfStudy;
    this.tenantId = tenantId;
  }
}

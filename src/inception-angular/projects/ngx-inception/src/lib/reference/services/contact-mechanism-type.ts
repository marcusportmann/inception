/*
 * Copyright 2020 Marcus Portmann
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
 * The ContactMechanismType class holds the information for a contact mechanism type.
 *
 * @author Marcus Portmann
 */
export class ContactMechanismType {

  /**
   * The code for the contact mechanism type.
   */
  code: string;

  /**
   * The description for the contact mechanism type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the contact mechanism type.
   */
  localeId: string;

  /**
   * The name of the contact mechanism type.
   */
  name: string;

  /**
   * The numeric code for the contact mechanism sub type.
   */
  numericCode: number;

  /**
   * The plural for the contact mechanism type.
   */
  plural: string;

  /**
   * The sort index for the contact mechanism type.
   */
  sortIndex: number;

  /**
   * Constructs a new ContactMechanismType.
   *
   * @param code        The code for the contact mechanism type.
   * @param localeId    The Unicode locale identifier for the contact mechanism type.
   * @param numericCode The numeric code for the contact mechanism type.
   * @param sortIndex   The sort index for the contact mechanism type.
   * @param name        The name of the contact mechanism type.
   * @param plural      The plural for the contact mechanism type.
   * @param description The description for the contact mechanism type.
   */
  constructor(code: string, localeId: string, numericCode: number, sortIndex: number,
              name: string, plural: string, description: string) {
    this.code = code;
    this.localeId = localeId;
    this.numericCode = numericCode;
    this.sortIndex = sortIndex;
    this.name = name;
    this.plural = plural;
    this.description = description;
  }
}

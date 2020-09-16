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
 * The ContactMechanismPurpose class holds the information for a contact mechanism purpose.
 *
 * @author Marcus Portmann
 */
export class ContactMechanismPurpose {

  /**
   * The code for the contact mechanism purpose.
   */
  code: string;

  /**
   * The description for the contact mechanism purpose.
   */
  description: string;

  /**
   * The Unicode locale identifier for the contact mechanism purpose.
   */
  localeId: string;

  /**
   * The name of the contact mechanism purpose.
   */
  name: string;

  /**
   * The numeric code for the contact mechanism purpose.
   */
  numericCode: number;

  /**
   * The code for the party type the contact mechanism purpose is associated with.
   */
  partyType: string;

  /**
   * The sort index for the contact mechanism purpose.
   */
  sortIndex: number;

  /**
   * The code for the contact mechanism type the contact mechanism purpose is associated with.
   */
  type: string;

  /**
   * Constructs a new ContactMechanismPurpose.
   *
   * @param type        The code for the contact mechanism type the contact mechanism purpose is
   *                    associated with.
   * @param code        The code for the contact mechanism purpose.
   * @param localeId    The Unicode locale identifier for the contact mechanism purpose.
   * @param numericCode The numeric code for the contact mechanism purpose.
   * @param partyType   The code for the party type the contact mechanism purpose is associated
   *                    with.
   * @param sortIndex   The sort index for the contact mechanism purpose.
   * @param name        The name of the contact mechanism purpose.
   * @param description The description for the contact mechanism purpose.
   */
  constructor(type: string, code: string, localeId: string, numericCode: number, partyType: string,
              sortIndex: number, name: string, description: string) {
    this.type = type;
    this.code = code;
    this.localeId = localeId;
    this.numericCode = numericCode;
    this.partyType = partyType;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}

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
 * The ContactMechanismRole class holds the information for a contact mechanism role.
 *
 * @author Marcus Portmann
 */
export class ContactMechanismRole {

  /**
   * The code for the contact mechanism role.
   */
  code: string;

  /**
   * The code for the contact mechanism type the contact mechanism role is associated with.
   */
  contactMechanismType: string;

  /**
   * The description for the contact mechanism role.
   */
  description: string;

  /**
   * The Unicode locale identifier for the contact mechanism role.
   */
  localeId: string;

  /**
   * The name of the contact mechanism role.
   */
  name: string;

  /**
   * The codes for the party types the contact mechanism role is associated with.
   */
  partyTypes: string[];

  /**
   * The sort index for the contact mechanism role.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the contact mechanism role is specific
   * to.
   */
  tenantId?: string;

  /**
   * Constructs a new ContactMechanismRole.
   *
   * @param contactMechanismType The code for the contact mechanism type the contact mechanism role
   *                             is associated with.
   * @param code                 The code for the contact mechanism role.
   * @param localeId             The Unicode locale identifier for the contact mechanism role.
   * @param sortIndex            The sort index for the contact mechanism role.
   * @param name                 The name of the contact mechanism role.
   * @param description          The description for the contact mechanism role.
   * @param partyTypes           The codes for the party types the contact mechanism role is
   *                             associated with.
   * @param tenantId             The ID for the tenant the contact
   *                             mechanism role is specific to.
   */
  constructor(contactMechanismType: string, code: string, localeId: string, sortIndex: number,
              name: string, description: string, partyTypes: string[], tenantId?: string) {
    this.contactMechanismType = contactMechanismType;
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.partyTypes = partyTypes;
    this.tenantId = tenantId;
  }
}

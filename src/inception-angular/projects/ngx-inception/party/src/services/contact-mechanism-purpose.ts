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
   * The codes for the contact mechanism types the contact mechanism purpose is associated with.
   */
  contactMechanismTypes: string[];

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
   * The codes for the party types the contact mechanism purpose is associated with.
   */
  partyTypes: string[];

  /**
   * The sort index for the contact mechanism purpose.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the contact mechanism purpose is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new ContactMechanismPurpose.
   *
   * @param code                  The code for the contact mechanism purpose.
   * @param localeId              The Unicode locale identifier for the contact mechanism purpose.
   * @param sortIndex             The sort index for the contact mechanism purpose.
   * @param name                  The name of the contact mechanism purpose.
   * @param description           The description for the contact mechanism purpose.
   * @param contactMechanismTypes The codes for the contact mechanism types the contact mechanism
   *                              purpose is associated with.
   * @param partyTypes            The codes for the party types the contact mechanism purpose is
   *                              associated with.
   * @param tenantId              The ID for the tenant the contact mechanism purpose is specific
   *                              to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              contactMechanismTypes: string[], partyTypes: string[], tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.contactMechanismTypes = contactMechanismTypes;
    this.partyTypes = partyTypes;
    this.tenantId = tenantId;
  }
}

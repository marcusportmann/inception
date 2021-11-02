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
 * The ContactMechanism class holds the information for a contact mechanism for an organization or
 * person.
 *
 * @author Marcus Portmann
 */
export class ContactMechanism {

  /**
   * The codes for the contact mechanism purposes.
   */
  purposes: string[];

  /**
   * The code for the contact mechanism role.
   */
  role: string;

  /**
   * The code for the contact mechanism type.
   */
  type: string;

  /**
   * The value for the contact mechanism.
   */
  value: string;

  /**
   * Constructs a new ContactMechanism.
   *
   * @param type     The code for the contact mechanism type.
   * @param role     The code for the contact mechanism role.
   * @param value    The value for the contact mechanism.
   * @param purposes The codes for the contact mechanism purposes.
   */
  constructor(type: string, role: string, value: string, purposes?: string[]) {
    this.type = type;
    this.role = role;
    this.value = value;

    if (purposes == null) {
      this.purposes = [];
    } else {
      this.purposes = purposes;
    }
  }
}

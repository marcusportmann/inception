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
 * The TimeToContact class holds the information for a time to contact.
 *
 * @author Marcus Portmann
 */
export class TimeToContact {

  /**
   * The code for the time to contact.
   */
  code: string;

  /**
   * The description for the time to contact.
   */
  description: string;

  /**
   * The Unicode locale identifier for the time to contact.
   */
  localeId: string;

  /**
   * The name of the time to contact.
   */
  name: string;

  /**
   * The sort index for the time to contact.
   */
  sortIndex: number;

  /**
   * The Universally Unique Identifier (UUID) for the tenant the time to contact is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new TimeToContact.
   *
   * @param code           The code for the time to contact.
   * @param localeId       The Unicode locale identifier for the time to contact.
   * @param sortIndex      The sort index for the time to contact.
   * @param name           The name of the time to contact.
   * @param description    The description for the time to contact.
   * @param tenantId       The Universally Unique Identifier (UUID) for the tenant the time to
   *                       contact is specific to.
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

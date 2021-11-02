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
 * The Title class holds the information for a title.
 *
 * @author Marcus Portmann
 */
export class Title {

  /**
   * The abbreviation for the title.
   */
  abbreviation: string;

  /**
   * The code for the title.
   */
  code: string;

  /**
   * The description for the title.
   */
  description: string;

  /**
   * The Unicode locale identifier for the title.
   */
  localeId: string;

  /**
   * The name of the title.
   */
  name: string;

  /**
   * The sort index for the title.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the title is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new Title.
   *
   * @param code         The code for the title.
   * @param localeId     The Unicode locale identifier for the title.
   * @param sortIndex    The sort index for the title.
   * @param name         The name of the title.
   * @param abbreviation The abbreviation for the title.
   * @param description  The description for the title.
   * @param tenantId     The ID for the tenant the title is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, abbreviation: string,
              description: string, tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.abbreviation = abbreviation;
    this.description = description;
    this.tenantId = tenantId;
  }
}

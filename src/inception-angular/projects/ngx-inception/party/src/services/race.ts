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
 * The Race class holds the information for a race.
 *
 * @author Marcus Portmann
 */
export class Race {

  /**
   * The code for the race.
   */
  code: string;

  /**
   * The description for the race.
   */
  description: string;

  /**
   * The Unicode locale identifier for the race.
   */
  localeId: string;

  /**
   * The name of the race.
   */
  name: string;

  /**
   * The sort index for the race.
   */
  sortIndex: number;

  /**
   * The Universally Unique Identifier (UUID) for the tenant the race is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new Race.
   *
   * @param code        The code for the race.
   * @param localeId    The Unicode locale identifier for the race.
   * @param sortIndex   The sort index for the race.
   * @param name        The name of the race.
   * @param description The description for the race.
   * @param tenantId    The Universally Unique Identifier (UUID) for the tenant the race is specific
   *                    to.
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

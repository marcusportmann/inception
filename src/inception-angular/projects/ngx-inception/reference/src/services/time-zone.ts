/*
 * Copyright Marcus Portmann
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
 * The TimeZone class holds the information for a time zone.
 *
 * @author Marcus Portmann
 */
export class TimeZone {
  /**
   * The description for the time zone.
   */
  description: string;

  /**
   * The ID for the time zone.
   */
  id: string;

  /**
   * The Unicode locale identifier for the time zone.
   */
  localeId: string;

  /**
   * The name of the time zone.
   */
  name: string;

  /**
   * The sort index for the time zone.
   */
  sortIndex: number;

  /**
   * Constructs a new TimeZone.
   *
   * @param id          The ID for the time zone.
   * @param localeId    The Unicode locale identifier for the time zone.
   * @param sortIndex   The sort index for the time zone.
   * @param name        The name of the time zone.
   * @param description The description for the time zone.
   */
  constructor(
    id: string,
    localeId: string,
    sortIndex: number,
    name: string,
    description: string
  ) {
    this.id = id;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}

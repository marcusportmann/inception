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
 * The Data class holds the data information.
 *
 * @author Marcus Portmann
 */
export class Data {
  /**
   * The ISO 8601 format date value for the data.
   */
  dateValue: string;

  /**
   * The ID for the data.
   */
  id: number;

  /**
   * The integer value for the data.
   */
  integerValue: number;

  /**
   * The string string value for the data.
   */
  stringValue: string;

  /**
   * The timestamp value for the data.
   */
  timestampValue: Date;

  /**
   * The zoned timestamp value for the data.
   */
  zonedTimestampValue: Date;

  /**
   * Constructs a new Data.
   *
   * @param id                  The ID for the data.
   * @param integerValue        The integer value.
   * @param stringValue         The string value for the data.
   * @param dateValue           The ISO 8601 format date value for the data.
   * @param timestampValue      The timestamp value for the data.
   * @param zonedTimestampValue The zoned timestamp value for the data.
   */
  constructor(
    id: number,
    integerValue: number,
    stringValue: string,
    dateValue: string,
    timestampValue: Date,
    zonedTimestampValue: Date
  ) {
    this.id = id;
    this.integerValue = integerValue;
    this.stringValue = stringValue;
    this.dateValue = dateValue;
    this.timestampValue = timestampValue;
    this.zonedTimestampValue = zonedTimestampValue;
  }
}

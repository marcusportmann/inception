/*
 * Copyright 2022 Marcus Portmann
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
 * The MandateProperty class holds the information for a mandate property for a mandate.
 *
 * @author Marcus Portmann
 */
export class MandateProperty {

  /**
   * The boolean value for the mandate property.
   */
  booleanValue?: boolean;

  /**
   * The ISO 8601 format date value for the mandate property.
   */
  dateValue?: string;

  /**
   * The decimal value for the mandate property.
   */
  decimalValue?: number;

  /**
   * The double value for the mandate property.
   */
  doubleValue?: number;

  /**
   * The integer value for the mandate property.
   */
  integerValue?: number;

  /**
   * The string value for the mandate property.
   */
  stringValue?: string;

  /**
   * The code for the mandate property type.
   */
  type: string;

  /**
   * Constructs a new MandateProperty.
   *
   * @param type The code for the mandate property type.
   */
  constructor(type: string) {
    this.type = type;
  }
}







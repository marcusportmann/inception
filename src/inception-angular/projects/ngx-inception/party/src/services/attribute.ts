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

import {MeasurementUnit} from "./measurement-unit";

/**
 * The Attribute class holds the information for an attribute for an organization or person.
 *
 * @author Marcus Portmann
 */
export class Attribute {

  /**
   * The boolean value for the attribute.
   */
  booleanValue?: boolean;

  /**
   * The date value for the attribute.
   */
  dateValue?: Date;

  /**
   * The decimal value for the attribute.
   */
  decimalValue?: number;

  /**
   * The double value for the attribute.
   */
  doubleValue?: number;

  /**
   * The integer value for the attribute.
   */
  integerValue?: number;

  /**
   * The string value for the attribute.
   */
  stringValue?: string;

  /**
   * The code for the attribute type.
   */
  type: string;

  /**
   * The measurement unit for the attribute.
   */
  unit?: MeasurementUnit;

  /**
   * Constructs a new Attribute.
   *
   * @param type The code for the attribute type.
   */
  constructor(type: string) {
    this.type = type;
  }
}







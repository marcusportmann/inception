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
 * The AssociationProperty class holds the information for an association property for an
 * association.
 *
 * @author Marcus Portmann
 */
export class AssociationProperty {

  /**
   * The boolean value for the association property.
   */
  booleanValue?: boolean;

  /**
   * The date value for the association property.
   */
  dateValue?: Date;

  /**
   * The decimal value for the association property.
   */
  decimalValue?: number;

  /**
   * The double value for the association property.
   */
  doubleValue?: number;

  /**
   * The integer value for the association property.
   */
  integerValue?: number;

  /**
   * The string value for the association property.
   */
  stringValue?: string;

  /**
   * The code for the association property type.
   */
  type: string;

  /**
   * Constructs a new AssociationProperty.
   *
   * @param type The code for the association property type.
   */
  constructor(type: string) {
    this.type = type;
  }
}







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

import {ValueType} from "./value-type";

/**
 * The MandatePropertyType class holds the information for a mandate property type.
 *
 * @author Marcus Portmann
 */
export class MandatePropertyType {

  /**
   * The code for the mandate property type.
   */
  code: string;

  /**
   * The description for the mandate property type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the mandate property type.
   */
  localeId: string;

  /**
   * The code for the mandate type the mandate property type is associated with.
   */
  mandateType: string;

  /**
   * The name of the mandate property type.
   */
  name: string;

  /**
   *  The regular expression pattern used to validate a string value for the mandate property type.
   */
  pattern?: string;

  /**
   * The sort index for the mandate property type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the mandate property type is specific to.
   */
  tenantId?: string;

  /**
   * The value type for the mandate property type.
   */
  valueType: ValueType;

  /**
   * Constructs a new MandatePropertyType.
   *
   * @param mandateType The code for the mandate type the mandate property type is associated with.
   * @param code            The code for the mandate property type.
   * @param localeId        The Unicode locale identifier for the mandate property type.
   * @param sortIndex       The sort index for the mandate property type.
   * @param name            The name of the mandate property type.
   * @param description     The description for the mandate property type.
   * @param valueType       The value type for the mandate property type.
   * @param pattern         The regular expression pattern used to validate a string value for the
   *                        mandate property type.
   * @param tenantId        The ID for the tenant the mandate property type is specific to.
   */
  constructor(mandateType: string, code: string, localeId: string, sortIndex: number,
              name: string, description: string, valueType: ValueType, pattern?: string,
              tenantId?: string) {
    this.mandateType = mandateType;
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.valueType = valueType;
    this.pattern = pattern;
    this.tenantId = tenantId;
  }
}

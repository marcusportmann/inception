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

import {ValueType} from "./value-type";

/**
 * The AssociationPropertyType class holds the information for an association property type.
 *
 * @author Marcus Portmann
 */
export class AssociationPropertyType {

  /**
   * The code for the association type the association property type is associated with.
   */
  associationType: string;

  /**
   * The code for the association property type.
   */
  code: string;

  /**
   * The description for the association property type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the association property type.
   */
  localeId: string;

  /**
   * The name of the association property type.
   */
  name: string;

  /**
   * The sort index for the association property type.
   */
  sortIndex: number;

  /**
   * The Universally Unique Identifier (UUID) for the tenant the association property type is
   * specific to.
   */
  tenantId?: string;

  /**
   * The value type for the association property type.
   */
  valueType: ValueType;

  /**
   * Constructs a new AssociationPropertyType.
   *
   * @param associationType The code for the association type the association property type is
   *                        associated with.
   * @param code            The code for the association property type.
   * @param localeId        The Unicode locale identifier for the association property type.
   * @param sortIndex       The sort index for the association property type.
   * @param name            The name of the association property type.
   * @param description     The description for the association property type.
   * @param valueType       The value type for the association property type.
   * @param tenantId        The Universally Unique Identifier (UUID) for the tenant the association
   *                        property type is specific to.
   */
  constructor(associationType: string, code: string, localeId: string, sortIndex: number,
              name: string, description: string, valueType: ValueType, tenantId?: string) {
    this.associationType = associationType;
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.valueType = valueType;
    this.tenantId = tenantId;
  }
}

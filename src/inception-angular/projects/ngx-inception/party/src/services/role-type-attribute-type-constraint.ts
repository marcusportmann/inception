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

import {ConstraintType} from "./constraint-type";

/**
 * The RoleTypeAttributeTypeConstraint class holds the information for a constraint that should be
 * applied to an attribute for a party that is assigned a particular role.
 *
 * @author Marcus Portmann
 */
export class RoleTypeAttributeTypeConstraint {

  /**
   * The code for the attribute type.
   */
  attributeType: string;

  /**
   * The qualifier for the attribute type.
   */
  attributeTypeQualifier: string;

  /**
   * The code for the role type.
   */
  roleType: string;

  /**
   * The constraint type.
   */
  type: ConstraintType;

  /**
   * The value to apply when validating the attribute value, e.g. the length, the regular expression
   * pattern, etc.
   */
  value?: string;

  /**
   * Constructs a new RoleTypeAttributeTypeConstraint.
   *
   * @param roleType               The code for the role type.
   * @param attributeType          The code for the attribute type.
   * @param attributeTypeQualifier The qualifier for the attribute type.
   * @param type                   The constraint type.
   * @param value                  The value to apply when validating the attribute value, e.g. the
   *                               length, the regular expression pattern, etc.
   */
  constructor(roleType: string, attributeType: string, attributeTypeQualifier: string,
              type: ConstraintType, value?: string) {
    this.roleType = roleType;
    this.attributeType = attributeType;
    this.attributeTypeQualifier = attributeTypeQualifier;
    this.type = type;
    this.value = value;
  }


}

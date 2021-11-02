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
 * The RoleTypePreferenceTypeConstraint class holds the information for a constraint that should be
 * applied to a preference for a party that is assigned a particular role.
 *
 * @author Marcus Portmann
 */
export class RoleTypePreferenceTypeConstraint {

  /**
   * The code for the preference type.
   */
  preferenceType: string;

  /**
   * The code for the role type.
   */
  roleType: string;

  /**
   * The constraint type.
   */
  type: ConstraintType;

  /**
   * The value to apply when validating the preference value, e.g. the length, the regular
   * expression pattern, etc.
   */
  value?: string;

  /**
   * Constructs a new RoleTypePreferenceTypeConstraint.
   *
   * @param roleType       The code for the role type.
   * @param preferenceType The code for the preference type.
   * @param type           The constraint type.
   * @param value          The value to apply when validating the preference value, e.g. the length,
   *                       the regular expression pattern, etc.
   */
  constructor(roleType: string, preferenceType: string, type: ConstraintType, value?: string) {
    this.roleType = roleType;
    this.preferenceType = preferenceType;
    this.type = type;
    this.value = value;
  }
}

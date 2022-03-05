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
 * The SkillType class holds the information for a skill type.
 *
 * @author Marcus Portmann
 */
export class SkillType {

  /**
   * The code for the skill type.
   */
  code: string;

  /**
   * The description for the skill type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the skill type.
   */
  localeId: string;

  /**
   * The name of the skill type.
   */
  name: string;

  /**
   * The code for the parent skill type the skill type is associated with.
   */
  parent?: string;

  /**
   * The sort index for the skill type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the skill type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new SkillType.
   *
   * @param code        The code for the skill type.
   * @param localeId    The Unicode locale identifier for the skill type.
   * @param sortIndex   The sort index for the skill type.
   * @param name        The name of the skill type.
   * @param description The description for the skill type.
   * @param parent      The code for the parent skill type the skill type is associated with.
   * @param tenantId    The ID for the tenant the skill type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description:
    string, parent?: string, tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.parent = parent;
    this.tenantId = tenantId;
  }
}

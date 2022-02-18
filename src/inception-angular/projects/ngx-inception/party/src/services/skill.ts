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

import {SkillProficiencyLevel} from './skill-proficiency-level';

/**
 * The Skill class holds the information for a skill possessed by a person.
 *
 * @author Marcus Portmann
 */
export class Skill {

  /**
   * The proficiency level for the skill.
   */
  level: SkillProficiencyLevel;

  /**
   * The code for the skill type.
   */
  type: string;

  /**
   * Constructs a new Skill.
   *
   * @param type  The code for the skill type.
   * @param level The proficiency level for the skill.
   */
  constructor(type: string, level: SkillProficiencyLevel) {
    this.type = type;
    this.level = level;
  }
}

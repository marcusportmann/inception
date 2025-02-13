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
 * See the License for the specific skill governing permissions and
 * limitations under the License.
 */

package digital.inception.party.model;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>SkillProficiencyLevel</b> enumeration defines the skill proficiency levels.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A skill proficiency level")
@XmlEnum
@XmlType(name = "SkillProficiencyLevel", namespace = "https://inception.digital/party")
public enum SkillProficiencyLevel implements CodeEnum {
  /** Novice. */
  @XmlEnumValue("Novice")
  NOVICE("novice", "Novice"),

  /** Advanced Beginner. */
  @XmlEnumValue("AdvancedBeginner")
  ADVANCED_BEGINNER("advanced_beginner", "Advanced Beginner"),

  /** Competence. */
  @XmlEnumValue("Competence")
  COMPETENCE("competence", "Competence"),

  /** Proficient. */
  @XmlEnumValue("Proficient")
  PROFICIENT("proficient", "Proficient"),

  /** Expert. */
  @XmlEnumValue("Expert")
  EXPERT("expert", "Expert");

  private final String code;

  private final String description;

  SkillProficiencyLevel(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the skill proficiency level.
   *
   * @return the code for the skill proficiency level
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the skill proficiency level.
   *
   * @return the description for the skill proficiency level
   */
  public String description() {
    return description;
  }
}

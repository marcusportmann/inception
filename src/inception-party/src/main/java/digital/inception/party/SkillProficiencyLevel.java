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
 * See the License for the specific skill governing permissions and
 * limitations under the License.
 */

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>SkillProficiencyLevel</b> enumeration defines the skill proficiency levels.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A skill proficiency level")
@XmlEnum
@XmlType(name = "SkillProficiencyLevel", namespace = "http://inception.digital/party")
public enum SkillProficiencyLevel {
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
   * Returns the skill proficiency level given by the specified code value.
   *
   * @param code the code for the skill proficiency level
   * @return the skill proficiency level given by the specified code value
   */
  @JsonCreator
  public static SkillProficiencyLevel fromCode(String code) {
    switch (code) {
      case "novice":
        return SkillProficiencyLevel.NOVICE;

      case "advanced_beginner":
        return SkillProficiencyLevel.ADVANCED_BEGINNER;

      case "competence":
        return SkillProficiencyLevel.COMPETENCE;

      case "proficient":
        return SkillProficiencyLevel.PROFICIENT;

      case "expert":
        return SkillProficiencyLevel.EXPERT;

      default:
        throw new RuntimeException(
            "Failed to determine the skill proficiency level with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the skill proficiency level.
   *
   * @return the code for the skill proficiency level
   */
  @JsonValue
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

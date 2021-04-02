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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>LanguageProficiencyLevel</b> enumeration defines the language proficiency levels.
 *
 * <p>Based on the Common European Framework of Reference for Languages (CEFR) language proficiency
 * levels.
 *
 * @see <a
 *     href="https://en.wikipedia.org/wiki/Common_European_Framework_of_Reference_for_Languages">Common
 *     European Framework of Reference for Languages</a>
 * @author Marcus Portmann
 */
@Schema(description = "A language proficiency level")
@XmlEnum
@XmlType(name = "LanguageProficiencyLevel", namespace = "http://inception.digital/party")
public enum LanguageProficiencyLevel {
  @XmlEnumValue("Beginner")
  BEGINNER("beginner", "Beginner"),
  @XmlEnumValue("Elementary")
  ELEMENTARY("elementary", "Elementary"),
  @XmlEnumValue("Intermediate")
  INTERMEDIATE("intermediate", "Intermediate"),
  @XmlEnumValue("Advanced")
  ADVANCED("advanced", "Advanced"),
  @XmlEnumValue("Proficient")
  PROFICIENT("proficient", "Proficient");

  private final String code;

  private final String description;

  LanguageProficiencyLevel(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the language proficiency level given by the specified code value.
   *
   * @param code the code for the language proficiency level
   * @return the language proficiency level given by the specified code value
   */
  @JsonCreator
  public static LanguageProficiencyLevel fromCode(String code) {
    switch (code) {
      case "beginner":
        return LanguageProficiencyLevel.BEGINNER;

      case "elementary":
        return LanguageProficiencyLevel.ELEMENTARY;

      case "intermediate":
        return LanguageProficiencyLevel.INTERMEDIATE;

      case "advanced":
        return LanguageProficiencyLevel.ADVANCED;

      case "proficient":
        return LanguageProficiencyLevel.PROFICIENT;

      default:
        throw new RuntimeException(
            "Failed to determine the language proficiency level with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the language proficiency level.
   *
   * @return the code for the language proficiency level
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the language proficiency level.
   *
   * @return the description for the language proficiency level
   */
  public String description() {
    return description;
  }
}

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
 * The <b>RequiredMandataries</b> enumeration defining the possible numbers of mandataries required
 * to execute mandates.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A mandatary requirement")
@XmlEnum
@XmlType(name = "RequiredMandataries", namespace = "http://inception.digital/party")
public enum RequiredMandataries {
  @XmlEnumValue("All")
  ALL("all", "All Mandataries"),
  @XmlEnumValue("Any")
  ANY("any", "Any Mandatary"),
  @XmlEnumValue("AnyTwo")
  ANY_TWO("any_two", "Any Two Mandataries"),
  @XmlEnumValue("AnyThree")
  ANY_THREE("any_three", "Any Three Mandataries"),
  @XmlEnumValue("AnyFour")
  ANY_FOUR("any_four", "Any Four Mandataries"),
  @XmlEnumValue("AnyFive")
  ANY_FIVE("any_five", "Any Five Mandataries");

  private final String code;

  private final String description;

  RequiredMandataries(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the required mandataries given by the specified code value.
   *
   * @param code the code for the required mandataries
   * @return the required mandataries given by the specified code value
   */
  @JsonCreator
  public static RequiredMandataries fromCode(String code) {
    switch (code) {
      case "all":
        return RequiredMandataries.ALL;

      case "any":
        return RequiredMandataries.ANY;

      case "any_two":
        return RequiredMandataries.ANY_TWO;

      case "any_three":
        return RequiredMandataries.ANY_THREE;

      case "any_four":
        return RequiredMandataries.ANY_FOUR;

      case "any_five":
        return RequiredMandataries.ANY_FIVE;

      default:
        throw new RuntimeException(
            "Failed to determine the required mandataries with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the code for the required mandataries.
   *
   * @return the code for the required mandataries
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the required mandataries.
   *
   * @return the description for the required mandataries
   */
  public String description() {
    return description;
  }
}

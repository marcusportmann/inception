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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.party.model;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code RequiredMandataries} enumeration defining the possible numbers of mandataries required
 * to execute mandates.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A mandatary requirement")
@XmlEnum
@XmlType(name = "RequiredMandataries", namespace = "https://inception.digital/party")
public enum RequiredMandataries implements CodeEnum {
  /** All mandataries. */
  @XmlEnumValue("All")
  ALL("all", "All Mandataries"),

  /** Any mandatary. */
  @XmlEnumValue("Any")
  ANY("any", "Any Mandatary"),

  /** Any two mandataries. */
  @XmlEnumValue("AnyTwo")
  ANY_TWO("any_two", "Any Two Mandataries"),

  /** Any three mandataries. */
  @XmlEnumValue("AnyThree")
  ANY_THREE("any_three", "Any Three Mandataries"),

  /** Any four mandataries. */
  @XmlEnumValue("AnyFour")
  ANY_FOUR("any_four", "Any Four Mandataries"),

  /** Any five mandataries. */
  @XmlEnumValue("AnyFive")
  ANY_FIVE("any_five", "Any Five Mandataries");

  private final String code;

  private final String description;

  RequiredMandataries(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the required mandataries.
   *
   * @return the code for the required mandataries
   */
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

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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>PartyType</b> enumeration defines the possible party types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The party type")
@XmlEnum
@XmlType(name = "PartyType", namespace = "http://inception.digital/party")
public enum PartyType {
  /** Organization. */
  @XmlEnumValue("Organization")
  ORGANIZATION("organization", "Organization"),

  /** Person. */
  @XmlEnumValue("Person")
  PERSON("person", "Person"),

  /** Unknown. */
  @XmlEnumValue("Unknown")
  UNKNOWN("unknown", "Unknown");

  private final String code;

  private final String description;

  PartyType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the party type given by the specified code value.
   *
   * @param code the code for the party type
   * @return the party type given by the specified code value
   */
  @JsonCreator
  public static PartyType fromCode(String code) {
    return switch (code) {
      case "organization" -> PartyType.ORGANIZATION;
      case "person" -> PartyType.PERSON;
      case "unknown" -> PartyType.UNKNOWN;
      default ->
          throw new RuntimeException(
              "Failed to determine the party type with the invalid code (" + code + ")");
    };
  }

  /**
   * Returns the code for the party type.
   *
   * @return the code for the party type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the party type.
   *
   * @return the description for the party type
   */
  public String description() {
    return description;
  }
}

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
 * The <b>EntityType</b> enumeration defines the possible entity types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The entity type")
@XmlEnum
@XmlType(name = "EntityType", namespace = "http://inception.digital/party")
public enum EntityType {
  /** Association. */
  @XmlEnumValue("Association")
  ASSOCIATION("association", "An association"),

  /** Mandate. */
  @XmlEnumValue("Mandate")
  MANDATE("mandate", "A mandate"),

  /** Organization. */
  @XmlEnumValue("Organization")
  ORGANIZATION("organization", "An organization"),

  /** Person. */
  @XmlEnumValue("Person")
  PERSON("person", "A person");

  private final String code;

  private final String description;

  EntityType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the entity type given by the specified code value.
   *
   * @param code the code for the entity type
   * @return the entity type given by the specified code value
   */
  @JsonCreator
  public static EntityType fromCode(String code) {
    return switch (code) {
      case "association" -> EntityType.ASSOCIATION;
      case "mandate" -> EntityType.MANDATE;
      case "organization" -> EntityType.ORGANIZATION;
      case "person" -> EntityType.PERSON;
      default -> throw new RuntimeException(
          "Failed to determine the entity type with the invalid code (" + code + ")");
    };
  }

  /**
   * Returns the code for the entity type.
   *
   * @return the code for the entity type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the entity type.
   *
   * @return the description for the entity type
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the entity type enumeration value.
   *
   * @return the string representation of the entity type enumeration value
   */
  public String toString() {
    return description;
  }
}

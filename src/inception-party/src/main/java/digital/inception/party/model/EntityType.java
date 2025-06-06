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
 * The {@code EntityType} enumeration defines the possible entity types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The entity type")
@XmlEnum
@XmlType(name = "EntityType", namespace = "https://inception.digital/party")
public enum EntityType implements CodeEnum {
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
   * Returns the code for the entity type.
   *
   * @return the code for the entity type
   */
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
   * Returns the string representation of the enumeration value.
   *
   * @return the string representation of the enumeration value
   */
  public String toString() {
    return description;
  }
}

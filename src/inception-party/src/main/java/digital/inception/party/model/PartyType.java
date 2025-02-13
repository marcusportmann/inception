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
 * The <b>PartyType</b> enumeration defines the possible party types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The party type")
@XmlEnum
@XmlType(name = "PartyType", namespace = "https://inception.digital/party")
public enum PartyType implements CodeEnum {
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
   * Returns the code for the party type.
   *
   * @return the code for the party type
   */
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

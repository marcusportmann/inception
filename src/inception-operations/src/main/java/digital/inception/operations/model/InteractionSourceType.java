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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>InteractionSourceType</b> enumeration defines the possible interaction source types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The interaction source type")
@XmlEnum
@XmlType(name = "InteractionSourceType", namespace = "https://inception.digital/operations")
public enum InteractionSourceType {

  /** Mailbox. */
  @XmlEnumValue("Mailbox")
  MAILBOX("mailbox", "Mailbox"),

  /** WhatsApp. */
  @XmlEnumValue("WhatsApp")
  WHATSAPP("whatsapp", "WhatsApp");

  private final String code;

  private final String description;

  InteractionSourceType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the interaction source type given by the specified code value.
   *
   * @param code the code for the interaction source type
   * @return the interaction source type given by the specified code value
   */
  @JsonCreator
  public static InteractionSourceType fromCode(String code) {
    for (InteractionSourceType value : InteractionSourceType.values()) {
      if (value.code.equalsIgnoreCase(code)) {
        return value;
      }
    }
    throw new RuntimeException(
        "Failed to determine the interaction source type with the invalid code (" + code + ")");
  }

  /**
   * Returns the code for the interaction source type.
   *
   * @return the code for the interaction source type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the interaction source type.
   *
   * @return the description for the interaction source type
   */
  public String description() {
    return description;
  }
}

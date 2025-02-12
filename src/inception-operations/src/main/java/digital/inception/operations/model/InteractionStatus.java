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
 * The <b>InteractionStatus</b> enumeration defines the possible statuses for an interaction.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The interaction status")
@XmlEnum
@XmlType(name = "InteractionStatus", namespace = "https://inception.digital/operations")
public enum InteractionStatus {

  /** Received. */
  @XmlEnumValue("Received")
  RECEIVED("received", "Received"),

  /** Assigned. */
  @XmlEnumValue("Assigned")
  ASSIGNED("assigned", "Assigned"),

  /** Resolved. */
  @XmlEnumValue("Resolved")
  RESOLVED("resolved", "Resolved"),

  /** Archived. */
  @XmlEnumValue("Archived")
  ARCHIVED("archived", "Archived");

  private final String code;

  private final String description;

  InteractionStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the interaction status given by the specified code value.
   *
   * @param code the code for the interaction status
   * @return the interaction status given by the specified code value
   */
  @JsonCreator
  public static InteractionStatus fromCode(String code) {
    for (InteractionStatus value : InteractionStatus.values()) {
      if (value.code.equalsIgnoreCase(code)) {
        return value;
      }
    }
    throw new RuntimeException(
        "Failed to determine the interaction status with the invalid code (" + code + ")");
  }

  /**
   * Returns the code for the interaction status.
   *
   * @return the code for the interaction status
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the interaction status.
   *
   * @return the description for the interaction status
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the interaction status enumeration value.
   *
   * @return the string representation of the interaction status enumeration value
   */
  public String toString() {
    return description;
  }
}

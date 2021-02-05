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

package digital.inception.messaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/** The enumeration giving the possible priorities for a message. */
@Schema(description = "The message priority")
@XmlEnum
@XmlType(name = "MessagePriority", namespace = "http://messaging.inception.digital")
public enum MessagePriority {
  @XmlEnumValue("Low")
  LOW("low", "Low"),
  @XmlEnumValue("Medium")
  MEDIUM("medium", "Medium"),
  @XmlEnumValue("High")
  HIGH("high", "High");

  private final String code;

  private final String description;

  MessagePriority(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the message priority given by the specified code value.
   *
   * @param code the code for the message priority
   * @return the message priority given by the specified code value
   */
  @JsonCreator
  public static MessagePriority fromCode(String code) {
    switch (code) {
      case "low":
        return MessagePriority.LOW;
      case "medium":
        return MessagePriority.MEDIUM;
      case "high":
        return MessagePriority.HIGH;
      default:
        throw new RuntimeException(
            "Failed to determine the message priority with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the message priority for the specified numeric code.
   *
   * @param numericCode the numeric code for the message priority
   * @return the message priority given by the specified numeric code value
   */
  public static MessagePriority fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 1:
        return MessagePriority.LOW;
      case 5:
        return MessagePriority.MEDIUM;
      case 10:
        return MessagePriority.HIGH;
      default:
        throw new RuntimeException(
            "Failed to determine the message priority for the numeric code (" + numericCode + ")");
    }
  }

  /**
   * Returns the numeric code for the message priority.
   *
   * @param messagePriority the message priority
   * @return the numeric code for the message priority
   */
  public static int toNumericCode(MessagePriority messagePriority) {
    switch (messagePriority) {
      case LOW:
        return 1;
      case MEDIUM:
        return 5;
      case HIGH:
        return 10;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the message priority ("
                + messagePriority.code()
                + ")");
    }
  }

  /**
   * Returns the code for the message priority.
   *
   * @return the code for the message priority
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the message priority.
   *
   * @return the description for the message priority
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the message priority enumeration value.
   *
   * @return the string representation of the message priority enumeration value
   */
  public String toString() {
    return description;
  }
}

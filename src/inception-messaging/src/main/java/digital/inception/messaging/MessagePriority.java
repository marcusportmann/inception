/*
 * Copyright 2020 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/** The enumeration giving the possible priorities for a message. */
@Schema(description = "The message priority, i.e. 1 = Low, 5 = Medium, 10 = High")
@XmlEnum
@XmlType(name = "MessagePriority", namespace = "http://messaging.inception.digital")
public enum MessagePriority {
  @XmlEnumValue("Low")
  LOW(1, "Low"),
  @XmlEnumValue("Medium")
  MEDIUM(5, "Medium"),
  @XmlEnumValue("High")
  HIGH(10, "High");

  /** The code identifying the message priority. */
  private final int code;

  /** The description for the message priority. */
  private final String description;

  MessagePriority(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the message priority given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the message priority
   * @return the message priority given by the specified numeric code value
   */
  @JsonCreator
  public static MessagePriority fromCode(int code) {
    switch (code) {
      case 1:
        return MessagePriority.LOW;

      case 5:
        return MessagePriority.MEDIUM;

      case 10:
        return MessagePriority.HIGH;

      default:
        return MessagePriority.MEDIUM;
    }
  }

  /**
   * Returns the code identifying the message priority.
   *
   * @return the code identifying the message priority
   */
  @JsonValue
  public int code() {
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

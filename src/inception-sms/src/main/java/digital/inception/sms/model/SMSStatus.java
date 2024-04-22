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

package digital.inception.sms.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>SMSStatus</b> enumeration defines the possible statuses for a SMS.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The SMS status")
@XmlEnum
@XmlType(name = "SMSStatus", namespace = "http://inception.digital/sms")
public enum SMSStatus {
  /** Unknown. */
  @XmlEnumValue("Unknown")
  UNKNOWN("unknown", "Unknown"),

  /** Queued. */
  @XmlEnumValue("Queued")
  QUEUED("queued", "Queued"),

  /** Sending. */
  @XmlEnumValue("Sending")
  SENDING("sending", "Sending"),

  /** Sent. */
  @XmlEnumValue("Sent")
  SENT("sent", "Sent"),

  /** Failed. */
  @XmlEnumValue("Failed")
  FAILED("failed", "Failed"),

  /** Any. */
  @XmlEnumValue("Any")
  ANY("any", "Any");

  private final String code;

  private final String description;

  SMSStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the SMS status given by the specified code value.
   *
   * @param code the code for the SMS status
   * @return the SMS status given by the specified code value
   */
  @JsonCreator
  public static SMSStatus fromCode(String code) {
    return switch (code) {
      case "unknown" -> SMSStatus.UNKNOWN;
      case "queued" -> SMSStatus.QUEUED;
      case "sending" -> SMSStatus.SENDING;
      case "sent" -> SMSStatus.SENT;
      case "failed" -> SMSStatus.FAILED;
      case "any" -> SMSStatus.ANY;
      default ->
          throw new RuntimeException(
              "Failed to determine the SMS status with the invalid code (" + code + ")");
    };
  }

  /**
   * Returns the code for the SMS status.
   *
   * @return the code for the SMS status
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the SMS status.
   *
   * @return the description for the SMS status
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the SMS status enumeration value.
   *
   * @return the string representation of the SMS status enumeration value
   */
  public String toString() {
    return description;
  }
}

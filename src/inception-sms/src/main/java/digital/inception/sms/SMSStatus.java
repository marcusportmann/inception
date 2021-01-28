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

package digital.inception.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>SMSStatus</b> enumeration defines the possible statuses for a SMS.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The SMS status")
@XmlEnum
@XmlType(name = "SMSStatus", namespace = "http://sms.inception.digital")
public enum SMSStatus {
  @XmlEnumValue("Unknown")
  UNKNOWN("unknown", "Unknown"),
  @XmlEnumValue("QueuedForSending")
  QUEUED_FOR_SENDING("queued_for_sending", "QueuedForSending"),
  @XmlEnumValue("Sending")
  SENDING("sending", "Sending"),
  @XmlEnumValue("Sent")
  SENT("sent", "Sent"),
  @XmlEnumValue("Failed")
  FAILED("failed", "Failed"),
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
    switch (code) {
      case "unknown":
        return SMSStatus.UNKNOWN;
      case "queued_for_sending":
        return SMSStatus.QUEUED_FOR_SENDING;
      case "sending":
        return SMSStatus.SENDING;
      case "sent":
        return SMSStatus.SENT;
      case "failed":
        return SMSStatus.FAILED;
      case "any":
        return SMSStatus.ANY;
      default:
        throw new RuntimeException(
            "Failed to determine the SMS status with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the sms status for the specified numeric code.
   *
   * @param numericCode the numeric code for the sms status
   * @return the sms status given by the specified numeric code value
   */
  public static SMSStatus fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 1:
        return SMSStatus.UNKNOWN;
      case 2:
        return SMSStatus.QUEUED_FOR_SENDING;
      case 3:
        return SMSStatus.SENDING;
      case 4:
        return SMSStatus.SENT;
      case 5:
        return SMSStatus.FAILED;
      case -1:
        return SMSStatus.ANY;
      default:
        throw new RuntimeException(
            "Failed to determine the sms status for the numeric code (" + numericCode + ")");
    }
  }

  /**
   * Returns the numeric code for the sms status.
   *
   * @param smsStatus the sms status
   * @return the numeric code for the sms status
   */
  public static int toNumericCode(SMSStatus smsStatus) {
    switch (smsStatus) {
      case UNKNOWN:
        return 1;
      case QUEUED_FOR_SENDING:
        return 2;
      case SENDING:
        return 3;
      case SENT:
        return 4;
      case FAILED:
        return 5;
      case ANY:
        return -1;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the sms status (" + smsStatus.code() + ")");
    }
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

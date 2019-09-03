/*
 * Copyright 2017 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>SMSStatus</code> enumeration defines the possible statuses for a SMS.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "SMSStatus")
@XmlEnum
@XmlType(name = "SMSStatus", namespace = "http://sms.inception.digital")
public enum SMSStatus
{
  @XmlEnumValue("Unknown")
  UNKNOWN(0, "Unknown"),
  @XmlEnumValue("QueuedForSending")
  QUEUED_FOR_SENDING(1, "QueuedForSending"),
  @XmlEnumValue("Sending")
  SENDING(2, "Sending"),
  @XmlEnumValue("Sent")
  SENT(3, "Sent"),
  @XmlEnumValue("Failed")
  FAILED(4, "Failed"),
  @XmlEnumValue("Any")
  ANY(-1, "Any");

  private int code;
  private String description;

  SMSStatus(int code, String description)
  {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the SMS status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the SMS status
   *
   * @return the SMS status given by the specified numeric code value
   */
  @JsonCreator
  public static SMSStatus fromCode(int code)
  {
    switch (code)
    {
      case 1:
        return SMSStatus.QUEUED_FOR_SENDING;

      case 2:
        return SMSStatus.SENDING;

      case 3:
        return SMSStatus.SENT;

      case 4:
        return SMSStatus.FAILED;

      case -1:
        return SMSStatus.ANY;

      default:
        return SMSStatus.UNKNOWN;
    }
  }

  /**
   * Returns the numeric code value identifying the SMS status.
   *
   * @return the numeric code value identifying the SMS status
   */
  @JsonValue
  public int code()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> value of the numeric code value identifying the SMS status.
   *
   * @return the <code>String</code> value of the numeric code value identifying the SMS status
   */
  public String codeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the description for the SMS status.
   *
   * @return the description for the SMS status
   */
  public String description()
  {
    return description;
  }

  /**
   * Return the string representation of the SMS status enumeration value.
   *
   * @return the string representation of the SMS status enumeration value
   */
  public String toString()
  {
    return description;
  }
}

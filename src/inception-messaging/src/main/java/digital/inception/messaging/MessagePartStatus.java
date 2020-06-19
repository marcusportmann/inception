/*
 * Copyright 2019 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

//~--- JDK imports ------------------------------------------------------------

/**
 * The enumeration giving the possible statuses for a message part.
 */
@ApiModel(value = "MessagePartStatus")
@XmlEnum
@XmlType(name = "MessagePartStatus", namespace = "http://messaging.inception.digital")
public enum MessagePartStatus {
  @XmlEnumValue("Initialized")
  INITIALIZED(0, "Initialized"),
  @XmlEnumValue("QueuedForSending")
  QUEUED_FOR_SENDING(1, "QueuedForSending"),
  @XmlEnumValue("Sending")
  SENDING(2, "Sending"),
  @XmlEnumValue("QueuedForAssembly")
  QUEUED_FOR_ASSEMBLY(3, "QueuedForAssembly"),
  @XmlEnumValue("Assembling")
  ASSEMBLING(4, "Assembling"),
  @XmlEnumValue("QueuedForDownload")
  QUEUED_FOR_DOWNLOAD(5, "QueuedForDownload"),
  @XmlEnumValue("Downloading")
  DOWNLOADING(6, "Downloading"),
  @XmlEnumValue("Aborted")
  ABORTED(7, "Aborted"),
  @XmlEnumValue("Failed")
  FAILED(8, "Failed"),
  @XmlEnumValue("Unknown")
  UNKNOWN(-1, "Unknown");

  private int code;
  private String description;

  MessagePartStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the message part status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the message part status
   *
   * @return the message part status given by the specified numeric code value
   */
  @JsonCreator
  public static MessagePartStatus fromCode(int code) {
    switch (code) {
      case 0:
        return MessagePartStatus.INITIALIZED;

      case 1:
        return MessagePartStatus.QUEUED_FOR_SENDING;

      case 2:
        return MessagePartStatus.SENDING;

      case 3:
        return MessagePartStatus.QUEUED_FOR_ASSEMBLY;

      case 4:
        return MessagePartStatus.ASSEMBLING;

      case 5:
        return MessagePartStatus.QUEUED_FOR_DOWNLOAD;

      case 6:
        return MessagePartStatus.DOWNLOADING;

      case 7:
        return MessagePartStatus.ABORTED;

      case 8:
        return MessagePartStatus.FAILED;

      default:
        return MessagePartStatus.UNKNOWN;
    }
  }

  /**
   * Returns the numeric code value identifying the message part status.
   *
   * @return the numeric code value identifying the message part status
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the description for the message part status.
   *
   * @return the description for the message part status
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the message part status enumeration value.
   *
   * @return the string representation of the message part status enumeration value
   */
  public String toString() {
    return description;
  }
}

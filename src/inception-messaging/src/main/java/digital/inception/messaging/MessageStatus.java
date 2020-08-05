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

/**
 * The enumeration giving the possible statuses for a message.
 */
@Schema(description = "MessageStatus")
@XmlEnum
@XmlType(name = "MessageStatus", namespace = "http://messaging.inception.digital")
public enum MessageStatus {
  @XmlEnumValue("Initialized")
  INITIALIZED(0, "Initialized"),
  @XmlEnumValue("QueuedForSending")
  QUEUED_FOR_SENDING(1, "QueuedForSending"),
  @XmlEnumValue("QueuedForProcessing")
  QUEUED_FOR_PROCESSING(2, "QueuedForProcessing"),
  @XmlEnumValue("Aborted")
  ABORTED(3, "Aborted"),
  @XmlEnumValue("Failed")
  FAILED(4, "Failed"),
  @XmlEnumValue("Processing")
  PROCESSING(5, "Processing"),
  @XmlEnumValue("Sending")
  SENDING(6, "Sending"),
  @XmlEnumValue("QueuedForDownload")
  QUEUED_FOR_DOWNLOAD(7, "QueuedForDownload"),
  @XmlEnumValue("Downloading")
  DOWNLOADING(8, "Downloading"),
  @XmlEnumValue("Processed")
  PROCESSED(9, "Processed"),
  @XmlEnumValue("Unknown")
  UNKNOWN(-1, "Unknown");

  private int code;

  private String description;

  MessageStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the message status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the message status
   *
   * @return the message status given by the specified numeric code value
   */
  @JsonCreator
  public static MessageStatus fromCode(int code) {
    switch (code) {
      case 0:
        return MessageStatus.INITIALIZED;

      case 1:
        return MessageStatus.QUEUED_FOR_SENDING;

      case 2:
        return MessageStatus.QUEUED_FOR_PROCESSING;

      case 3:
        return MessageStatus.ABORTED;

      case 4:
        return MessageStatus.FAILED;

      case 5:
        return MessageStatus.PROCESSING;

      case 6:
        return MessageStatus.SENDING;

      case 7:
        return MessageStatus.QUEUED_FOR_DOWNLOAD;

      case 8:
        return MessageStatus.DOWNLOADING;

      case 9:
        return MessageStatus.PROCESSED;

      default:
        return MessageStatus.UNKNOWN;
    }
  }

  /**
   * Returns the numeric code value identifying the message status.
   *
   * @return the numeric code value identifying the message status
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the description for the message status.
   *
   * @return the description for the message status
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the message status enumeration value.
   *
   * @return the string representation of the message status enumeration value
   */
  public String toString() {
    return description;
  }
}

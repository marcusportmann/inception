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

/** The enumeration giving the possible statuses for a message part. */
@Schema(description = "The message part status")
@XmlEnum
@XmlType(name = "MessagePartStatus", namespace = "http://inception.digital/messaging")
public enum MessagePartStatus {
  /** Initialized. */
  @XmlEnumValue("Initialized")
  INITIALIZED("initialized", "Initialized"),

  /** Queued for sending. */
  @XmlEnumValue("QueuedForSending")
  QUEUED_FOR_SENDING("queued_for_sending", "QueuedForSending"),

  /** Sending. */
  @XmlEnumValue("Sending")
  SENDING("sending", "Sending"),

  /** Queued for assembly. */
  @XmlEnumValue("QueuedForAssembly")
  QUEUED_FOR_ASSEMBLY("queued_for_assembly", "QueuedForAssembly"),

  /** Assembling. */
  @XmlEnumValue("Assembling")
  ASSEMBLING("assembling", "Assembling"),

  /** Queued for download. */
  @XmlEnumValue("QueuedForDownload")
  QUEUED_FOR_DOWNLOAD("queued_for_download", "QueuedForDownload"),

  /** Downloading. */
  @XmlEnumValue("Downloading")
  DOWNLOADING("downloading", "Downloading"),

  /** Aborted. */
  @XmlEnumValue("Aborted")
  ABORTED("aborted", "Aborted"),

  /** Failed. */
  @XmlEnumValue("Failed")
  FAILED("failed", "Failed");

  private final String code;

  private final String description;

  MessagePartStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the message part status given by the specified code value.
   *
   * @param code the code for the message part status
   * @return the message part status given by the specified code value
   */
  @JsonCreator
  public static MessagePartStatus fromCode(String code) {
    switch (code) {
      case "initialized":
        return MessagePartStatus.INITIALIZED;

      case "queued_for_sending":
        return MessagePartStatus.QUEUED_FOR_SENDING;

      case "sending":
        return MessagePartStatus.SENDING;

      case "queued_for_assembly":
        return MessagePartStatus.QUEUED_FOR_ASSEMBLY;

      case "assembling":
        return MessagePartStatus.ASSEMBLING;

      case "queued_for_download":
        return MessagePartStatus.QUEUED_FOR_DOWNLOAD;

      case "downloading":
        return MessagePartStatus.DOWNLOADING;

      case "aborted":
        return MessagePartStatus.ABORTED;

      case "failed":
        return MessagePartStatus.FAILED;

      default:
        throw new RuntimeException(
            "Failed to determine the message part status with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the message part status for the specified numeric code.
   *
   * @param numericCode the numeric code for the message part status
   * @return the message part status given by the specified numeric code value
   */
  public static MessagePartStatus fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 1:
        return MessagePartStatus.INITIALIZED;
      case 2:
        return MessagePartStatus.QUEUED_FOR_SENDING;
      case 3:
        return MessagePartStatus.SENDING;
      case 4:
        return MessagePartStatus.QUEUED_FOR_ASSEMBLY;
      case 5:
        return MessagePartStatus.ASSEMBLING;
      case 6:
        return MessagePartStatus.QUEUED_FOR_DOWNLOAD;
      case 7:
        return MessagePartStatus.DOWNLOADING;
      case 8:
        return MessagePartStatus.ABORTED;
      case 9:
        return MessagePartStatus.FAILED;
      default:
        throw new RuntimeException(
            "Failed to determine the message part status for the numeric code ("
                + numericCode
                + ")");
    }
  }

  /**
   * Returns the numeric code for the message part status.
   *
   * @param messagePartStatus the message part status
   * @return the numeric code for the message part status
   */
  public static int toNumericCode(MessagePartStatus messagePartStatus) {
    switch (messagePartStatus) {
      case INITIALIZED:
        return 1;
      case QUEUED_FOR_SENDING:
        return 2;
      case SENDING:
        return 3;
      case QUEUED_FOR_ASSEMBLY:
        return 4;
      case ASSEMBLING:
        return 5;
      case QUEUED_FOR_DOWNLOAD:
        return 6;
      case DOWNLOADING:
        return 7;
      case ABORTED:
        return 8;
      case FAILED:
        return 9;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the message part status ("
                + messagePartStatus.code()
                + ")");
    }
  }

  /**
   * Returns the code for the message part status.
   *
   * @return the code for the message part status
   */
  @JsonValue
  public String code() {
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

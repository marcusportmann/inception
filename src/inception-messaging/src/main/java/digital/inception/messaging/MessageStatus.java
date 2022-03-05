/*
 * Copyright 2022 Marcus Portmann
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

/** The enumeration giving the possible statuses for a message. */
@Schema(description = "The message status")
@XmlEnum
@XmlType(name = "MessageStatus", namespace = "http://inception.digital/messaging")
public enum MessageStatus {
  /** Initialized. */
  @XmlEnumValue("Initialized")
  INITIALIZED("initialized", "Initialized"),

  /** Queued for sending. */
  @XmlEnumValue("QueuedForSending")
  QUEUED_FOR_SENDING("queued_for_sending", "QueuedForSending"),

  /** Queued for processing. */
  @XmlEnumValue("QueuedForProcessing")
  QUEUED_FOR_PROCESSING("queued_for_processing", "QueuedForProcessing"),

  /** Aborted. */
  @XmlEnumValue("Aborted")
  ABORTED("aborted", "Aborted"),

  /** Failed. */
  @XmlEnumValue("Failed")
  FAILED("failed", "Failed"),

  /** Processing. */
  @XmlEnumValue("Processing")
  PROCESSING("processing", "Processing"),

  /** Sending. */
  @XmlEnumValue("Sending")
  SENDING("sending", "Sending"),

  /** Queued for download. */
  @XmlEnumValue("QueuedForDownload")
  QUEUED_FOR_DOWNLOAD("queued_for_download", "QueuedForDownload"),

  /** Downloading. */
  @XmlEnumValue("Downloading")
  DOWNLOADING("downloading", "Downloading"),

  /** Processed. */
  @XmlEnumValue("Processed")
  PROCESSED("processed", "Processed");

  private final String code;

  private final String description;

  MessageStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the message status given by the specified code value.
   *
   * @param code the code for the message status
   * @return the message status given by the specified code value
   */
  @JsonCreator
  public static MessageStatus fromCode(String code) {
    switch (code) {
      case "initialized":
        return MessageStatus.INITIALIZED;
      case "queued_for_sending":
        return MessageStatus.QUEUED_FOR_SENDING;
      case "queued_for_processing":
        return MessageStatus.QUEUED_FOR_PROCESSING;
      case "aborted":
        return MessageStatus.ABORTED;
      case "failed":
        return MessageStatus.FAILED;
      case "processing":
        return MessageStatus.PROCESSING;
      case "sending":
        return MessageStatus.SENDING;
      case "queued_for_download":
        return MessageStatus.QUEUED_FOR_DOWNLOAD;
      case "downloading":
        return MessageStatus.DOWNLOADING;
      case "processed":
        return MessageStatus.PROCESSED;
      default:
        throw new RuntimeException(
            "Failed to determine the message status with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the message status for the specified numeric code.
   *
   * @param numericCode the numeric code for the message status
   * @return the message status given by the specified numeric code value
   */
  public static MessageStatus fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 1:
        return MessageStatus.INITIALIZED;
      case 2:
        return MessageStatus.QUEUED_FOR_SENDING;
      case 3:
        return MessageStatus.QUEUED_FOR_PROCESSING;
      case 4:
        return MessageStatus.ABORTED;
      case 5:
        return MessageStatus.FAILED;
      case 6:
        return MessageStatus.PROCESSING;
      case 7:
        return MessageStatus.SENDING;
      case 8:
        return MessageStatus.QUEUED_FOR_DOWNLOAD;
      case 9:
        return MessageStatus.DOWNLOADING;
      case 10:
        return MessageStatus.PROCESSED;
      default:
        throw new RuntimeException(
            "Failed to determine the message status for the numeric code (" + numericCode + ")");
    }
  }

  /**
   * Returns the numeric code for the message status.
   *
   * @param messageStatus the message status
   * @return the numeric code for the message status
   */
  public static int toNumericCode(MessageStatus messageStatus) {
    switch (messageStatus) {
      case INITIALIZED:
        return 1;
      case QUEUED_FOR_SENDING:
        return 2;
      case QUEUED_FOR_PROCESSING:
        return 3;
      case ABORTED:
        return 4;
      case FAILED:
        return 5;
      case PROCESSING:
        return 6;
      case SENDING:
        return 7;
      case QUEUED_FOR_DOWNLOAD:
        return 8;
      case DOWNLOADING:
        return 9;
      case PROCESSED:
        return 10;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the message status ("
                + messageStatus.code()
                + ")");
    }
  }

  /**
   * Returns the code for the message status.
   *
   * @return the code for the message status
   */
  @JsonValue
  public String code() {
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

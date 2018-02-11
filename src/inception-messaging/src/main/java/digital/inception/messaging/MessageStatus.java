/*
 * Copyright 2018 Marcus Portmann
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

/**
 * The enumeration giving the possible statuses for a message.
 */
public enum MessageStatus
{
  INITIALISED(0, "Initialised"), QUEUED_FOR_SENDING(1, "QueuedForSending"), QUEUED_FOR_PROCESSING(
      2, "QueuedForProcessing"), ABORTED(3, "Aborted"), FAILED(4, "Failed"), PROCESSING(5,
      "Processing"), SENDING(6, "Sending"), QUEUED_FOR_DOWNLOAD(7, "QueuedForDownload"),
      DOWNLOADING(8, "Downloading"), PROCESSED(10, "Processed"), UNKNOWN(-1, "Unknown");

  private int code;
  private String name;

  MessageStatus(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the message status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the message status
   *
   * @return the message status given by the specified numeric code value
   */
  public static MessageStatus fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return MessageStatus.INITIALISED;

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

      case 10:
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
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the name of the message status.
   *
   * @return the name of the message status
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the message status enumeration value.
   *
   * @return the string representation of the message status enumeration value
   */
  public String toString()
  {
    return name;
  }
}

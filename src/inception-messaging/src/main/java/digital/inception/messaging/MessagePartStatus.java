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
 * The enumeration giving the possible statuses for a message part.
 */
public enum MessagePartStatus
{
  INITIALIZED(0, "Initialized"), QUEUED_FOR_SENDING(1, "QueuedForSending"), SENDING(2, "Sending"),
      QUEUED_FOR_ASSEMBLY(3, "QueuedForAssembly"), ASSEMBLING(4, "Assembling"), QUEUED_FOR_DOWNLOAD(
      5, "QueuedForDownload"), DOWNLOADING(6, "Downloading"), ABORTED(7, "Aborted"), FAILED(8,
      "Failed"), UNKNOWN(-1, "Unknown");

  private int code;
  private String name;

  MessagePartStatus(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the message part status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the message part status
   *
   * @return the message part status given by the specified numeric code value
   */
  public static MessagePartStatus fromCode(int code)
  {
    switch (code)
    {
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
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the name of the message part status.
   *
   * @return the name of the message part status
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the message part status enumeration value.
   *
   * @return the string representation of the message part status enumeration value
   */
  public String toString()
  {
    return name;
  }
}

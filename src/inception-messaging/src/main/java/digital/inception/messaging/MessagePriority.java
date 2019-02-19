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

/**
 * The enumeration giving the possible priorities for a message.
 */
public enum MessagePriority
{
  LOW(1, "Low"), MEDIUM(5, "Medium"), HIGH(10, "High");

  /**
   * The code identifying the message priority.
   */
  private int code;

  /**
   * The name of the message priority.
   */
  private String name;

  MessagePriority(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the message priority given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the message priority
   *
   * @return the message priority given by the specified numeric code value
   */
  public static MessagePriority fromCode(int code)
  {
    switch (code)
    {
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
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the name of the message priority.
   *
   * @return the name of the message priority
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the message priority enumeration value.
   *
   * @return the string representation of the message priority enumeration value
   */
  public String toString()
  {
    return name;
  }
}

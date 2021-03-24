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

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Parser;

/**
 * The <b>WbxmlMessageData</b> class provides the abstract base class from which all WBXML-based
 * infrastructural and application-specific message data classes should be derived.
 *
 * @author Marcus Portmann
 */
public abstract class WbxmlMessageData {

  /** The code for the message type for the message data. */
  private final String messageType;

  /** The message priority for the message data. */
  private final MessagePriority messageTypePriority;

  /**
   * Constructs a new <b>WbxmlMessageData</b>.
   *
   * @param messageType the code for the message type for the message data
   * @param messagePriority the message priority for the message data
   */
  public WbxmlMessageData(String messageType, MessagePriority messagePriority) {
    this.messageType = messageType;
    this.messageTypePriority = messagePriority;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   * @return <b>true</b> if the message data was extracted successfully from the WBXML data or
   *     <b>false</b> otherwise
   */
  public abstract boolean fromMessageData(byte[] messageData) throws MessagingException;

  /**
   * Returns the code for the message type for the message data.
   *
   * @return the code for the message type for the message data
   */
  public String getMessageType() {
    return messageType;
  }

  /**
   * Returns the message priority for the message data.
   *
   * @return the message priority for the message data
   */
  public MessagePriority getMessageTypePriority() {
    return messageTypePriority;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   *     message
   */
  public abstract byte[] toMessageData() throws MessagingException;

  /**
   * Parse the WBXML data representation of the message data.
   *
   * @param data the WBXML data representation of the message data
   * @return the WBXML document representing the message data
   */
  protected Document parseWBXML(byte[] data) throws MessagingException {
    try {
      Parser parser = new Parser();

      return parser.parse(data);
    } catch (Throwable e) {
      throw new MessagingException("Failed to parse the WBXML message data", e);
    }
  }
}

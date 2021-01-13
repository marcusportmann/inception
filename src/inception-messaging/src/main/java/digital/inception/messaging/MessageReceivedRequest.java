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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import java.util.UUID;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessageReceivedRequest</code> class represents a request sent by a mobile device to
 * acknowledge the successful download of a message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"WeakerAccess"})
public class MessageReceivedRequest {

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the device the message received
   * request originated from.
   */
  private final UUID deviceId;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the message that was successfully
   * downloaded.
   */
  private final UUID messageId;

  /**
   * Constructs a new <code>MessageReceivedRequest</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message received request information
   */
  public MessageReceivedRequest(Document document) {
    Element rootElement = document.getRootElement();

    this.deviceId = UUID.fromString(rootElement.getAttributeValue("deviceId"));
    this.messageId = UUID.fromString(rootElement.getAttributeValue("messageId"));
  }

  /**
   * Constructs a new <code>MessageReceivedRequest</code>.
   *
   * @param deviceId the Universally Unique Identifier (UUID) uniquely identifying the device the
   *     message received request originated from
   * @param messageId the Universally Unique Identifier (UUID) uniquely identifying the message that
   *     was successfully downloaded
   */
  public MessageReceivedRequest(UUID deviceId, UUID messageId) {
    this.deviceId = deviceId;
    this.messageId = messageId;
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message received request
   * information or <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   * @return <code>true</code> if the WBXML document contains valid message received request
   *     information or <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document) {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessageReceivedRequest")
        && (rootElement.getAttributes().size() == 2)
        && rootElement.hasAttribute("deviceId")
        && rootElement.hasAttribute("messageId");
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the device the message
   * received request originated from.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the device the message
   *     received request originated from
   */
  public UUID getDeviceId() {
    return deviceId;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the message that was
   * successfully downloaded.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the message that was
   *     successfully downloaded
   */
  public UUID getMessageId() {
    return messageId;
  }

  /**
   * Returns the String representation of the message received request.
   *
   * @return the String representation of the message received request.
   */
  @Override
  public String toString() {
    return String.format(
        "<MessageReceivedRequest deviceId=\"%s\" messageId=\"%s\"/>", deviceId, messageId);
  }

  /**
   * Returns the WBXML representation of the message received request.
   *
   * @return the WBXML representation of the message received request
   */
  public byte[] toWBXML() {
    Element rootElement = new Element("MessageReceivedRequest");

    rootElement.setAttribute("deviceId", deviceId.toString());
    rootElement.setAttribute("messageId", messageId.toString());

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

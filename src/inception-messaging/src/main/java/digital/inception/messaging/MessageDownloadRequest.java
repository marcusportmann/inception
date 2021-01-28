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
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import java.util.UUID;

/**
 * The <b>MessageDownloadRequest</b> class represents a request sent by a mobile device to
 * download the queued messages for the device.
 *
 * <p>No information in the download request is encrypted and the request itself is not
 * authenticated. This is because the queued messages returned will themselves be encrypted.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("WeakerAccess")
public class MessageDownloadRequest {

  /**
   * The Universally Unique Identifier (UUID) for the device the message download request originated
   * from.
   */
  private UUID deviceId;

  /** The username for the user whose messages should be downloaded. */
  private String username;

  /**
   * Constructs a new <b>MessageDownloadRequest</b> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message download information
   */
  public MessageDownloadRequest(Document document) {
    Element rootElement = document.getRootElement();

    this.deviceId = UUID.fromString(rootElement.getAttributeValue("deviceId"));
    this.username = rootElement.getAttributeValue("username");
  }

  /**
   * Constructs a new <b>MessageDownloadRequest</b>.
   *
   * @param deviceId the Universally Unique Identifier (UUID) for the device the message download
   *     request originated from
   * @param username the username for the user whose messages should be downloaded
   */
  public MessageDownloadRequest(UUID deviceId, String username) {
    this.deviceId = deviceId;
    this.username = username;
  }

  /**
   * Returns <b>true</b> if the WBXML document contains valid message download request
   * information or <b>false</b> otherwise.
   *
   * @param document the WBXML document to validate
   * @return <b>true</b> if the WBXML document contains valid message download request
   *     information or <b>false</b> otherwise
   */
  public static boolean isValidWBXML(Document document) {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessageDownloadRequest")
        && (rootElement.getAttributes().size() == 2)
        && rootElement.hasAttribute("deviceId")
        && rootElement.hasAttribute("username");
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the device the message download request
   * originated from.
   *
   * @return the Universally Unique Identifier (UUID) for the device the message download request
   *     originated from
   */
  public UUID getDeviceId() {
    return deviceId;
  }

  /**
   * Returns the username for the user whose messages should be downloaded.
   *
   * @return the username for the user whose messages should be downloaded
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the device the message download request
   * originated from.
   *
   * @param deviceId the Universally Unique Identifier (UUID) for the device the message download
   *     request originated from
   */
  public void setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
  }

  /**
   * Set the username for the user whose messages should be downloaded.
   *
   * @param username the username for the user whose messages should be downloaded
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Returns the String representation of the message download request.
   *
   * @return the String representation of the message download request.
   */
  @Override
  public String toString() {
    return String.format(
        "<MessageDownloadRequest deviceId=\"%s\" username=\"%s\"/>", deviceId, username);
  }

  /**
   * Returns the WBXML representation of the message download request.
   *
   * @return the WBXML representation of the message download request
   */
  public byte[] toWBXML() {
    Element rootElement = new Element("MessageDownloadRequest");

    rootElement.setAttribute("deviceId", deviceId.toString());
    rootElement.setAttribute("username", username);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

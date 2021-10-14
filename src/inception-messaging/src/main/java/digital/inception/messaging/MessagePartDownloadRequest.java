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
 * The <b>MessagePartDownloadRequest</b> class represents a request sent a mobile device to download
 * the queued message parts for the device.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"WeakerAccess"})
public class MessagePartDownloadRequest {

  /** The ID for the device the message part download request originated from. */
  private UUID deviceId;

  /** The username for the user whose message parts should be downloaded. */
  private String username;

  /**
   * Constructs a new <b>MessagePartDownloadRequest</b> and populates it from the information stored
   * in the specified WBXML document.
   *
   * @param document the WBXML document containing the message part download information
   */
  public MessagePartDownloadRequest(Document document) {
    Element rootElement = document.getRootElement();

    rootElement
        .getAttributeValue("deviceId")
        .ifPresent(deviceId -> this.deviceId = UUID.fromString(deviceId));

    rootElement.getAttributeValue("username").ifPresent(username -> this.username = username);
  }

  /**
   * Constructs a new <b>MessagePartDownloadRequest</b>.
   *
   * @param deviceId the ID for the device the message part download request originated from
   * @param username the username for the user whose message parts should be downloaded
   */
  public MessagePartDownloadRequest(UUID deviceId, String username) {
    this.deviceId = deviceId;
    this.username = username;
  }

  /**
   * Returns <b>true</b> if the WBXML document contains valid message part download request
   * information or <b>false</b> otherwise.
   *
   * @param document the WBXML document to validate
   * @return <b>true</b> if the WBXML document contains valid message part download request
   *     information or <b>false</b> otherwise
   */
  public static boolean isValidWBXML(Document document) {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePartDownloadRequest")
        && (rootElement.getAttributes().size() == 2)
        && rootElement.hasAttribute("deviceId")
        && rootElement.hasAttribute("username");
  }

  /**
   * Returns the ID for the device the message part download request originated from.
   *
   * @return the ID for the device the message part download request originated from
   */
  public UUID getDeviceId() {
    return deviceId;
  }

  /**
   * Returns the username for the user whose message parts should be downloaded.
   *
   * @return the username for the user whose message parts should be downloaded
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the ID for the device the message part download request originated from.
   *
   * @param deviceId the ID for the device the message part download request originated from
   */
  public void setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
  }

  /**
   * Set the username for the user whose message parts should be downloaded.
   *
   * @param username the username for the user whose message parts should be downloaded
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Returns the String representation of the message part download request.
   *
   * @return the String representation of the message part download request.
   */
  @Override
  public String toString() {
    return String.format(
        "<MessagePartDownloadRequest deviceId=\"%s\" username=\"%s\"/>", deviceId, username);
  }

  /**
   * Returns the WBXML representation of the message part download request.
   *
   * @return the WBXML representation of the message part download request
   */
  public byte[] toWBXML() {
    Element rootElement = new Element("MessagePartDownloadRequest");

    rootElement.setAttribute("deviceId", deviceId.toString());
    rootElement.setAttribute("username", username);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

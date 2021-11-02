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

package digital.inception.messaging.messages;

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.messaging.MessagePriority;
import digital.inception.messaging.MessagingException;
import digital.inception.messaging.WbxmlMessageData;
import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 * The <b>AuthenticateRequestData</b> class manages the data for a "Authenticate Request" message.
 *
 * <p>This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class AuthenticateRequestData extends WbxmlMessageData {

  /** The message type code for the "Authenticate Request" message. */
  public static final String MESSAGE_TYPE = "AuthenticateRequest";

  /** The ID for the device the authentication request originated from. */
  private UUID deviceId;

  /** The password used to authenticate the user. */
  private String password;

  /** The username for the user associated with the message. */
  private String username;

  /** Constructs a new <b>AuthenticateRequestData</b>. */
  public AuthenticateRequestData() {
    super(MESSAGE_TYPE, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <b>AuthenticateRequestData</b>.
   *
   * @param username the username for the user associated with the message
   * @param password the password used to authenticate the user
   * @param deviceId the ID for the device the authentication request originated from
   */
  public AuthenticateRequestData(String username, String password, UUID deviceId) {
    super(MESSAGE_TYPE, MessagePriority.HIGH);

    this.deviceId = deviceId;
    this.password = password;
    this.username = username;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   * @return <b>true</b> if the message data was extracted successfully from the WBXML data or
   *     <b>false</b> otherwise
   */
  @Override
  public boolean fromMessageData(byte[] messageData) throws MessagingException {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("AuthenticateRequest")) {
      return false;
    }

    if ((!rootElement.hasChild("Username"))
        || (!rootElement.hasChild("Password"))
        || (!rootElement.hasChild("DeviceId"))) {
      return false;
    }

    rootElement
        .getChildText("DeviceId")
        .ifPresent(deviceId -> this.deviceId = UUID.fromString(deviceId));

    rootElement.getChildText("Password").ifPresent(password -> this.password = password);

    rootElement.getChildText("Username").ifPresent(username -> this.username = username);

    return true;
  }

  /**
   * Returns the ID for the device the authentication request originated from.
   *
   * @return the ID for the device the authentication request originated from
   */
  public UUID getDeviceId() {
    return deviceId;
  }

  /**
   * Returns the password used to authenticate the user.
   *
   * @return the password used to authenticate the user
   */
  public String getPassword() {
    return password;
  }

  /**
   * Returns the username for the user associated with the message.
   *
   * @return the username for the user associated with the message
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   *     message
   */
  @Override
  public byte[] toMessageData() {
    Element rootElement = new Element("AuthenticateRequest");

    rootElement.addContent(new Element("DeviceId", deviceId.toString()));
    rootElement.addContent(new Element("Password", StringUtils.hasText(password) ? password : ""));
    rootElement.addContent(new Element("Username", StringUtils.hasText(username) ? username : ""));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

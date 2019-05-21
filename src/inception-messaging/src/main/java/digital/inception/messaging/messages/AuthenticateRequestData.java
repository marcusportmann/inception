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

package digital.inception.messaging.messages;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.messaging.MessagePriority;
import digital.inception.messaging.MessagingServiceException;
import digital.inception.messaging.WbxmlMessageData;

import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

/**
 * The <code>AuthenticateRequestData</code> class manages the data for a
 * "Authenticate Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class AuthenticateRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Authenticate Request" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
      "d21fb54e-5c5b-49e8-881f-ce00c6ced1a3");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the
   * authentication request originated from.
   */
  private UUID deviceId;

  /**
   * The password used to authenticate the user.
   */
  private String password;

  /**
   * The username identifying the user associated with the message.
   */
  private String username;

  /**
   * Constructs a new <code>AuthenticateRequestData</code>.
   */
  public AuthenticateRequestData()
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <code>AuthenticateRequestData</code>.
   *
   * @param username the username identifying the user associated with the message
   * @param password the password used to authenticate the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the authentication request originated from
   */
  public AuthenticateRequestData(String username, String password, UUID deviceId)
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.deviceId = deviceId;
    this.password = password;
    this.username = username;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean fromMessageData(byte[] messageData)
    throws MessagingServiceException
  {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("AuthenticateRequest"))
    {
      return false;
    }

    if ((!rootElement.hasChild("Username"))
        || (!rootElement.hasChild("Password"))
        || (!rootElement.hasChild("DeviceId")))
    {
      return false;
    }

    this.deviceId = UUID.fromString(rootElement.getChildText("DeviceId"));
    this.password = rootElement.getChildText("Password");
    this.username = rootElement.getChildText("Username");

    return true;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the device the
   * authentication request originated from.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the device the
   *         authentication request originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the password used to authenticate the user.
   *
   * @return the password used to authenticate the user
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Returns the username identifying the user associated with the message.
   *
   * @return the username identifying the user associated with the message
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   *         message
   */
  @Override
  public byte[] toMessageData()
  {
    Element rootElement = new Element("AuthenticateRequest");

    rootElement.addContent(new Element("DeviceId", deviceId.toString()));
    rootElement.addContent(new Element("Password", StringUtils.isEmpty(password)
        ? ""
        : password));
    rootElement.addContent(new Element("Username", StringUtils.isEmpty(username)
        ? ""
        : username));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

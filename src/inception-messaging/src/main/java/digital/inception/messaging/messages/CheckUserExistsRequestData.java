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

import digital.inception.core.util.StringUtil;
import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.messaging.MessagePriority;
import digital.inception.messaging.MessagingServiceException;
import digital.inception.messaging.WbxmlMessageData;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

/**
 * The <code>CheckUserExistsRequestData</code> class manages the data for a
 * "Check User Exists Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class CheckUserExistsRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Check User Exists Request" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
      "cc005e6a-b01b-48eb-98a0-026297be69f3");

  /**
   * The username identifying the user.
   */
  private String username;

  /**
   * Constructs a new <code>CheckUserExistsRequestData</code>.
   */
  public CheckUserExistsRequestData()
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <code>CheckUserExistsRequestData</code>.
   *
   * @param username the username identifying the user
   */
  public CheckUserExistsRequestData(String username)
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

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
  public boolean fromMessageData(byte[] messageData)
    throws MessagingServiceException
  {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("CheckUserExistsRequest"))
    {
      return false;
    }

    if (!rootElement.hasChild("Username"))
    {
      return false;
    }

    this.username = rootElement.getChildText("Username");

    return true;
  }

  /**
   * Returns the username identifying the user.
   *
   * @return the username identifying the user
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
  public byte[] toMessageData()
    throws MessagingServiceException
  {
    Element rootElement = new Element("CheckUserExistsRequest");

    rootElement.addContent(new Element("Username", StringUtil.notNull(username)));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

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

import digital.inception.core.util.ISO8601Util;
import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.messaging.MessagePriority;
import digital.inception.messaging.MessagingServiceException;
import digital.inception.messaging.WbxmlMessageData;

//~--- JDK imports ------------------------------------------------------------

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The <code>GetCodeCategoryRequestData</code> class manages the data for a
 * "Get Code Category Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class GetCodeCategoryRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Get Code Category Request" message.
   */
  public static final String MESSAGE_TYPE_ID = "94d60eb6-a062-492d-b5e7-9fb1f05cf088";

  /**
   * The ID used to uniquely identify the code category to retrieve.
   */
  private String codeCategoryId;

  /**
   * The date and time the code category was last retrieved.
   */
  private LocalDateTime lastRetrieved;

  /**
   * The parameters.
   */
  private Map<String, String> parameters;

  /**
   * Should the codes for the code category be returned if the code category is current?
   */
  private boolean returnCodesIfCurrent;

  /**
   * Constructs a new <code>GetCodeCategoryRequestData</code>.
   */
  public GetCodeCategoryRequestData()
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <code>GetCodeCategoryRequestData</code>.
   *
   * @param codeCategoryId       the ID used to uniquely identify the code category to retrieve
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the codes for the code category be returned if the code
   *                             category is current
   */
  public GetCodeCategoryRequestData(String codeCategoryId, LocalDateTime lastRetrieved,
      boolean returnCodesIfCurrent)
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.codeCategoryId = codeCategoryId;
    this.lastRetrieved = lastRetrieved;
    this.parameters = new HashMap<>();
    this.returnCodesIfCurrent = returnCodesIfCurrent;
  }

  /**
   * Constructs a new <code>GetCodeCategoryRequestData</code>.
   *
   * @param codeCategoryId       the ID used to uniquely identify the code category to retrieve
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param parameters           the parameters
   * @param returnCodesIfCurrent should the codes for the code category be returned if the code
   *                             category is current
   */
  public GetCodeCategoryRequestData(String codeCategoryId, LocalDateTime lastRetrieved, Map<String,
      String> parameters, boolean returnCodesIfCurrent)
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.codeCategoryId = codeCategoryId;
    this.lastRetrieved = lastRetrieved;
    this.parameters = parameters;
    this.returnCodesIfCurrent = returnCodesIfCurrent;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   * <code>false</code> otherwise
   */
  @Override
  public boolean fromMessageData(byte[] messageData)
    throws MessagingServiceException
  {
    Element rootElement = parseWBXML(messageData).getRootElement();

    if (!rootElement.getName().equals("GetCodeCategoryRequest"))
    {
      return false;
    }

    if ((!rootElement.hasChild("CodeCategoryId"))
        || (!rootElement.hasChild("LastRetrieved"))
        || (!rootElement.hasChild("ReturnCodesIfCurrent")))
    {
      return false;
    }

    this.codeCategoryId = rootElement.getChildText("CodeCategoryId");

    String lastRetrievedValue = rootElement.getChildText("LastRetrieved");

    if (lastRetrievedValue.contains("T"))
    {
      try
      {
        this.lastRetrieved = ISO8601Util.toLocalDateTime(lastRetrievedValue);
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to parse the LastRetrieved ISO8601 timestamp ("
            + lastRetrievedValue + ") for the \"Get Code Category Request\" message", e);
      }
    }
    else
    {
      this.lastRetrieved = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(
          lastRetrievedValue)), ZoneId.systemDefault());
    }

    this.parameters = new HashMap<>();

    List<Element> parameterElements = rootElement.getChildren("Parameter");

    for (Element parameterElement : parameterElements)
    {
      String parameterName = parameterElement.getAttributeValue("name");
      String parameterValue = parameterElement.getAttributeValue("value");

      this.parameters.put(parameterName, parameterValue);
    }

    this.returnCodesIfCurrent = Boolean.parseBoolean(rootElement.getChildText(
        "ReturnCodesIfCurrent"));

    return true;
  }

  /**
   * Returns the ID uniquely identifying the code category to retrieve.
   *
   * @return the ID uniquely identify the code category to retrieve
   */
  public String getCodeCategoryId()
  {
    return codeCategoryId;
  }

  /**
   * Returns the date and time the code category was last retrieved.
   *
   * @return the date and time the code category was last retrieved
   */
  public LocalDateTime getLastRetrieved()
  {
    return lastRetrieved;
  }

  /**
   * Returns the parameters.
   *
   * @return the parameters
   */
  public Map<String, String> getParameters()
  {
    return parameters;
  }

  /**
   * Returns <code>true</code> if the codes for the code category be returned if the code category
   * is current or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the codes for the code category be returned if the code category
   *         is current or <code>false</code> otherwise
   */
  public boolean getReturnCodesIfCurrent()
  {
    return returnCodesIfCurrent;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   * message
   */
  @Override
  public byte[] toMessageData()
  {
    Element rootElement = new Element("GetCodeCategoryRequest");

    rootElement.addContent(new Element("CodeCategoryId", codeCategoryId));
    rootElement.addContent(new Element("LastRetrieved",
        (lastRetrieved == null)
        ? ISO8601Util.now()
        : ISO8601Util.fromLocalDateTime(lastRetrieved)));

    for (String parameterName : parameters.keySet())
    {
      String parameterValue = parameters.get(parameterName);

      Element parameterElement = new Element("Parameter");

      parameterElement.setAttribute("name", parameterName);
      parameterElement.setAttribute("value", parameterValue);

      rootElement.addContent(parameterElement);
    }

    rootElement.addContent(new Element("ReturnCodesIfCurrent", String.valueOf(
        returnCodesIfCurrent)));

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

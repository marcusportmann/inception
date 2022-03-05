/*
 * Copyright 2022 Marcus Portmann
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

import digital.inception.core.util.ISO8601Util;
import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.messaging.MessagePriority;
import digital.inception.messaging.MessagingException;
import digital.inception.messaging.WbxmlMessageData;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The <b>GetCodeCategoryRequestData</b> class manages the data for a "Get Code Category Request"
 * message.
 *
 * <p>This is a synchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class GetCodeCategoryRequestData extends WbxmlMessageData {

  /** The message type code for the "Get Code Category Request" message. */
  public static final String MESSAGE_TYPE = "GetCodeCategoryRequest";

  /** The ID for the code category to retrieve. */
  private String codeCategoryId;

  /** The date and time the code category was last retrieved. */
  private LocalDateTime lastRetrieved;

  /** The parameters. */
  private Map<String, String> parameters;

  /** Should the codes for the code category be returned if the code category is current? */
  private boolean returnCodesIfCurrent;

  /** Constructs a new <b>GetCodeCategoryRequestData</b>. */
  public GetCodeCategoryRequestData() {
    super(MESSAGE_TYPE, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <b>GetCodeCategoryRequestData</b>.
   *
   * @param codeCategoryId the ID for the code category to retrieve
   * @param lastRetrieved the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the codes for the code category be returned if the code
   *     category is current
   */
  public GetCodeCategoryRequestData(
      String codeCategoryId, LocalDateTime lastRetrieved, boolean returnCodesIfCurrent) {
    super(MESSAGE_TYPE, MessagePriority.HIGH);

    this.codeCategoryId = codeCategoryId;
    this.lastRetrieved = lastRetrieved;
    this.parameters = new HashMap<>();
    this.returnCodesIfCurrent = returnCodesIfCurrent;
  }

  /**
   * Constructs a new <b>GetCodeCategoryRequestData</b>.
   *
   * @param codeCategoryId the ID for the code category to retrieve
   * @param lastRetrieved the date and time the code category was last retrieved
   * @param parameters the parameters
   * @param returnCodesIfCurrent should the codes for the code category be returned if the code
   *     category is current
   */
  public GetCodeCategoryRequestData(
      String codeCategoryId,
      LocalDateTime lastRetrieved,
      Map<String, String> parameters,
      boolean returnCodesIfCurrent) {
    super(MESSAGE_TYPE, MessagePriority.HIGH);

    this.codeCategoryId = codeCategoryId;
    this.lastRetrieved = lastRetrieved;
    this.parameters = parameters;
    this.returnCodesIfCurrent = returnCodesIfCurrent;
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
    Element rootElement = parseWBXML(messageData).getRootElement();

    if (!rootElement.getName().equals("GetCodeCategoryRequest")) {
      return false;
    }

    if ((!rootElement.hasChild("CodeCategoryId"))
        || (!rootElement.hasChild("LastRetrieved"))
        || (!rootElement.hasChild("ReturnCodesIfCurrent"))) {
      return false;
    }

    rootElement
        .getChildText("CodeCategoryId")
        .ifPresent(codeCategoryId -> this.codeCategoryId = codeCategoryId);

    rootElement
        .getChildText("LastRetrieved")
        .ifPresent(
            lastRetrievedValue -> {
              if (lastRetrievedValue.contains("T")) {
                try {
                  this.lastRetrieved = ISO8601Util.toLocalDateTime(lastRetrievedValue);
                } catch (Throwable e) {
                  throw new RuntimeException(
                      "Failed to parse the LastRetrieved ISO8601 timestamp ("
                          + lastRetrievedValue
                          + ") for the \"Get Code Category Request\" message",
                      e);
                }
              } else {
                this.lastRetrieved =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(Long.parseLong(lastRetrievedValue)),
                        ZoneId.systemDefault());
              }
            });

    this.parameters = new HashMap<>();

    List<Element> parameterElements = rootElement.getChildren("Parameter");

    for (Element parameterElement : parameterElements) {
      Optional<String> parameterName = parameterElement.getAttributeValue("name");
      Optional<String> parameterValue = parameterElement.getAttributeValue("value");

      if (parameterName.isPresent() && parameterValue.isPresent()) {
        this.parameters.put(parameterName.get(), parameterValue.get());
      }
    }

    rootElement
        .getChildText("ReturnCodesIfCurrent")
        .ifPresent(
            returnCodesIfCurrent ->
                this.returnCodesIfCurrent = Boolean.parseBoolean(returnCodesIfCurrent));

    return true;
  }

  /**
   * Returns the ID for the code category to retrieve.
   *
   * @return the ID for the code category to retrieve
   */
  public String getCodeCategoryId() {
    return codeCategoryId;
  }

  /**
   * Returns the date and time the code category was last retrieved.
   *
   * @return the date and time the code category was last retrieved
   */
  public LocalDateTime getLastRetrieved() {
    return lastRetrieved;
  }

  /**
   * Returns the parameters.
   *
   * @return the parameters
   */
  public Map<String, String> getParameters() {
    return parameters;
  }

  /**
   * Returns <b>true</b> if the codes for the code category be returned if the code category is
   * current or <b>false</b> otherwise.
   *
   * @return <b>true</b> if the codes for the code category be returned if the code category is
   *     current or <b>false</b> otherwise
   */
  public boolean getReturnCodesIfCurrent() {
    return returnCodesIfCurrent;
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
    Element rootElement = new Element("GetCodeCategoryRequest");

    rootElement.addContent(new Element("CodeCategoryId", codeCategoryId));
    rootElement.addContent(
        new Element(
            "LastRetrieved",
            (lastRetrieved == null)
                ? ISO8601Util.now()
                : ISO8601Util.fromLocalDateTime(lastRetrieved)));

    for (String parameterName : parameters.keySet()) {
      String parameterValue = parameters.get(parameterName);

      Element parameterElement = new Element("Parameter");

      parameterElement.setAttribute("name", parameterName);
      parameterElement.setAttribute("value", parameterValue);

      rootElement.addContent(parameterElement);
    }

    rootElement.addContent(
        new Element("ReturnCodesIfCurrent", String.valueOf(returnCodesIfCurrent)));

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

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
import org.springframework.util.StringUtils;

/**
 * The <b>TestResponseData</b> class manages the data for a "Test Response" message.
 *
 * <p>This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class TestResponseData extends WbxmlMessageData {

  /** The message type code for the "Test Response" message. */
  public static final String MESSAGE_TYPE = "TestResponse";

  /** The test value. */
  private String testValue;

  /** Constructs a new <b>TestResponseData</b>. */
  public TestResponseData() {
    super(MESSAGE_TYPE, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <b>TestResponseData</b>.
   *
   * @param testValue the test value
   */
  public TestResponseData(String testValue) {
    super(MESSAGE_TYPE, MessagePriority.HIGH);

    this.testValue = testValue;
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

    if (!rootElement.getName().equals("TestResponse")) {
      return false;
    }

    if (!rootElement.hasChild("TestValue")) {
      return false;
    }

    rootElement.getChildText("TestValue").ifPresent(testValue -> this.testValue = testValue);

    return true;
  }

  /**
   * Returns the test value.
   *
   * @return the test value
   */
  public String getTestValue() {
    return testValue;
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
    Element rootElement = new Element("TestResponse");

    rootElement.addContent(
        new Element("TestValue", StringUtils.hasText(testValue) ? testValue : ""));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

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

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.messaging.MessagePriority;
import digital.inception.messaging.MessagingException;
import digital.inception.messaging.WbxmlMessageData;
import org.springframework.util.StringUtils;

/**
 * The <b>AnotherTestResponseData</b> class manages the data for a "Another Test Response" message.
 *
 * <p>This is an asynchronous message.
 *
 * @author Marcus Portmann
 */
public class AnotherTestResponseData extends WbxmlMessageData {

  /** The message type code for the "Another Test Response" message. */
  public static final String MESSAGE_TYPE = "AnotherTestResponse";

  /** The test data. */
  private byte[] testData;

  /** The test value. */
  private String testValue;

  /** Constructs a new <b>AnotherTestResponseData</b>. */
  public AnotherTestResponseData() {
    super(MESSAGE_TYPE, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <b>AnotherTestResponseData</b>.
   *
   * @param testValue the test value
   * @param testData the test data
   */
  public AnotherTestResponseData(String testValue, byte[] testData) {
    super(MESSAGE_TYPE, MessagePriority.HIGH);

    this.testValue = testValue;
    this.testData = testData;
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

    if (!rootElement.getName().equals("AnotherTestResponse")) {
      return false;
    }

    if ((!rootElement.hasChild("TestValue")) || (!rootElement.hasChild("TestData"))) {
      return false;
    }

    rootElement.getChildText("TestValue").ifPresent(testValue -> this.testValue = testValue);

    rootElement.getChildOpaque("TestData").ifPresent(testData -> this.testData = testData);

    return true;
  }

  /**
   * Returns the test data.
   *
   * @return the test data
   */
  public byte[] getTestData() {
    return testData;
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
    Element rootElement = new Element("AnotherTestResponse");

    rootElement.addContent(
        new Element("TestValue", StringUtils.hasText(testValue) ? testValue : ""));
    rootElement.addContent(new Element("TestData", testData));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

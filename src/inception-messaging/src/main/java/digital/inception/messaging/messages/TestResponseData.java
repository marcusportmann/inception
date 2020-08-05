/*
 * Copyright 2020 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.messaging.MessagePriority;
import digital.inception.messaging.MessagingServiceException;
import digital.inception.messaging.WbxmlMessageData;
import java.util.UUID;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestResponseData</code> class manages the data for a "Test Response" message.
 *
 * <p>This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class TestResponseData extends WbxmlMessageData {

  /**
   * The UUID for the "Test Response" message.
   */
  public static final UUID MESSAGE_TYPE_ID =
      UUID.fromString("a3bad7ba-f9d4-4403-b54a-cb1f335ebbad");

  /**
   * The test value.
   */
  private String testValue;

  /**
   * Constructs a new <code>TestResponseData</code>.
   */
  public TestResponseData() {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <code>TestResponseData</code>.
   *
   * @param testValue the test value
   */
  public TestResponseData(String testValue) {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.testValue = testValue;
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
  public boolean fromMessageData(byte[] messageData) throws MessagingServiceException {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("TestResponse")) {
      return false;
    }

    if (!rootElement.hasChild("TestValue")) {
      return false;
    }

    this.testValue = rootElement.getChildText("TestValue");

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
   * message
   */
  @Override
  public byte[] toMessageData() {
    Element rootElement = new Element("TestResponse");

    rootElement.addContent(
        new Element("TestValue", StringUtils.isEmpty(testValue) ? "" : testValue));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

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
 * The <code>CheckUserExistsResponseData</code> class manages the data for a "Check User Exists
 * Response" message.
 *
 * <p>This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class CheckUserExistsResponseData extends WbxmlMessageData {

  /** The error code returned when an unknown error occurred. */
  public static final int ERROR_CODE_UNKNOWN_ERROR = -1;

  /** The UUID for the "Check User Exists Response" message. */
  public static final UUID MESSAGE_TYPE_ID =
      UUID.fromString("a38bd55e-3470-46f1-a96a-a6b08a9adc63");

  /** The error code returned to indicate success. */
  private static final int ERROR_CODE_SUCCESS = 0;

  /** The message returned to indicate success. */
  private static final String ERROR_MESSAGE_SUCCESS = "Success";

  /** The error code; */
  private int errorCode;

  /** The error message. */
  private String errorMessage;

  /** <code>true</code> if the user exists or <code>false</code> otherwise. */
  private boolean userExists;

  /** Constructs a new <code>CheckUserExistsResponseData</code>. */
  public CheckUserExistsResponseData() {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <code>CheckUserExistsResponseData</code>.
   *
   * @param userExists <code>true</code> if the user exists or <code>false</code> otherwise
   */
  public CheckUserExistsResponseData(boolean userExists) {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.userExists = userExists;
    this.errorCode = ERROR_CODE_SUCCESS;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
  }

  /**
   * Constructs a new <code>CheckUserExistsResponseData</code>.
   *
   * @param errorCode the error code
   * @param errorMessage the error message
   */
  public CheckUserExistsResponseData(int errorCode, String errorMessage) {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   *     <code>false</code> otherwise
   */
  @Override
  public boolean fromMessageData(byte[] messageData) throws MessagingServiceException {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("CheckUserExistsResponse")) {
      return false;
    }

    if ((!rootElement.hasChild("ErrorCode"))
        || (!rootElement.hasChild("ErrorMessage"))
        || (!rootElement.hasChild("UserExists"))) {
      return false;
    }

    try {
      this.errorCode = Integer.parseInt(rootElement.getChildText("ErrorCode"));
    } catch (Throwable e) {
      return false;
    }

    this.errorMessage = rootElement.getChildText("ErrorMessage");

    try {
      this.userExists = Boolean.parseBoolean(rootElement.getChildText("UserExists"));
    } catch (Throwable e) {
      return false;
    }

    return true;
  }

  /**
   * Returns the error code.
   *
   * @return the error code
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Returns the error message.
   *
   * @return the error message
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Returns <code>true</code> if the user exists or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the user exists or <code>false</code> otherwise
   */
  public boolean getUserExists() {
    return userExists;
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
    Element rootElement = new Element("CheckUserExistsResponse");

    rootElement.addContent(new Element("ErrorCode", String.valueOf(errorCode)));
    rootElement.addContent(
        new Element("ErrorMessage", StringUtils.hasText(errorMessage) ? errorMessage : ""));
    rootElement.addContent(new Element("UserExists", String.valueOf(userExists)));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

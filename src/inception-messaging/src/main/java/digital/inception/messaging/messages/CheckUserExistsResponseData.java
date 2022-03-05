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
 * The <b>CheckUserExistsResponseData</b> class manages the data for a "Check User Exists Response"
 * message.
 *
 * <p>This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class CheckUserExistsResponseData extends WbxmlMessageData {

  /** The error code returned when an unknown error occurred. */
  public static final int ERROR_CODE_UNKNOWN_ERROR = -1;

  /** The message type code for the "Check User Exists Response" message. */
  public static final String MESSAGE_TYPE = "CheckUserExistsResponse";

  /** The error code returned to indicate success. */
  private static final int ERROR_CODE_SUCCESS = 0;

  /** The message returned to indicate success. */
  private static final String ERROR_MESSAGE_SUCCESS = "Success";

  /** The error code; */
  private int errorCode;

  /** The error message. */
  private String errorMessage;

  /** <b>true</b> if the user exists or <b>false</b> otherwise. */
  private boolean userExists;

  /** Constructs a new <b>CheckUserExistsResponseData</b>. */
  public CheckUserExistsResponseData() {
    super(MESSAGE_TYPE, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <b>CheckUserExistsResponseData</b>.
   *
   * @param userExists <b>true</b> if the user exists or <b>false</b> otherwise
   */
  public CheckUserExistsResponseData(boolean userExists) {
    super(MESSAGE_TYPE, MessagePriority.HIGH);

    this.userExists = userExists;
    this.errorCode = ERROR_CODE_SUCCESS;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
  }

  /**
   * Constructs a new <b>CheckUserExistsResponseData</b>.
   *
   * @param errorCode the error code
   * @param errorMessage the error message
   */
  public CheckUserExistsResponseData(int errorCode, String errorMessage) {
    super(MESSAGE_TYPE, MessagePriority.HIGH);

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
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

    if (!rootElement.getName().equals("CheckUserExistsResponse")) {
      return false;
    }

    if ((!rootElement.hasChild("ErrorCode"))
        || (!rootElement.hasChild("ErrorMessage"))
        || (!rootElement.hasChild("UserExists"))) {
      return false;
    }

    try {
      rootElement
          .getChildText("ErrorCode")
          .ifPresent(errorCode -> this.errorCode = Integer.parseInt(errorCode));
    } catch (Throwable e) {
      return false;
    }

    rootElement
        .getChildText("ErrorMessage")
        .ifPresent(errorMessage -> this.errorMessage = errorMessage);

    try {
      rootElement
          .getChildText("UserExists")
          .ifPresent(userExists -> this.userExists = Boolean.parseBoolean(userExists));
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
   * Returns <b>true</b> if the user exists or <b>false</b> otherwise.
   *
   * @return <b>true</b> if the user exists or <b>false</b> otherwise
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

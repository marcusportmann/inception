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
 * The <b>GetCodeCategoryResponseData</b> class manages the data for a "Get Code Category Response"
 * message.
 *
 * <p>This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class GetCodeCategoryResponseData extends WbxmlMessageData {

  /** The error code returned when an unknown error occurred. */
  public static final int ERROR_CODE_UNKNOWN_ERROR = -1;

  /** The message type code for the "Get Code Category Response" message. */
  public static final String MESSAGE_TYPE = "GetCodeCategoryResponse";

  /** The error code returned to indicate success. */
  private static final int ERROR_CODE_SUCCESS = 0;

  /** The message returned to indicate success. */
  private static final String ERROR_MESSAGE_SUCCESS = "Success";

  /** The code category. */
  private CodeCategoryData codeCategory;

  /**
   * The error code indicating the result of retrieving the code category where a code of '0'
   * indicates success and a non-zero code indicates an error condition.
   */
  private int errorCode;

  /** The error message describing the result of retrieving the code category. */
  private String errorMessage;

  /** Constructs a new <b>GetCodesResponseData</b>. */
  public GetCodeCategoryResponseData() {
    super(MESSAGE_TYPE, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <b>GetCodeCategoryResponseData</b>.
   *
   * @param codeCategory the data for the code category
   */
  public GetCodeCategoryResponseData(CodeCategoryData codeCategory) {
    super(MESSAGE_TYPE, MessagePriority.HIGH);

    this.errorCode = ERROR_CODE_SUCCESS;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
    this.codeCategory = codeCategory;
  }

  /**
   * Constructs a new <b>GetCodeCategoryResponseData</b>.
   *
   * @param errorCode the error code indicating the result of retrieving the code category
   * @param errorMessage the error message describing the result of retrieving the code category
   */
  public GetCodeCategoryResponseData(int errorCode, String errorMessage) {
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
    Element rootElement = parseWBXML(messageData).getRootElement();

    if (!rootElement.getName().equals("GetCodeCategoryResponse")) {
      return false;
    }

    if ((!rootElement.hasChild("ErrorCode")) || (!rootElement.hasChild("ErrorMessage"))) {
      return false;
    }

    rootElement
        .getChildText("ErrorCode")
        .ifPresent(errorCode -> this.errorCode = Integer.parseInt(errorCode));

    rootElement
        .getChildText("ErrorMessage")
        .ifPresent(errorMessage -> this.errorMessage = errorMessage);

    if (errorCode == 0) {
      if (!rootElement.hasChild("CodeCategory")) {
        return false;
      }

      rootElement
          .getChild("CodeCategory")
          .ifPresent(
              codeCategoryElement -> this.codeCategory = new CodeCategoryData(codeCategoryElement));
    }

    return true;
  }

  /**
   * Returns the code category.
   *
   * @return the code category
   */
  public CodeCategoryData getCodeCategory() {
    return codeCategory;
  }

  /**
   * Returns the error code indicating the result of retrieving the code category where a code of
   * '0' indicates success and a non-zero code indicates an error condition.
   *
   * @return the error code indicating the result of retrieving the code category where a code of
   *     '0' indicates success and a non-zero code indicates an error condition
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Returns the error message describing the result of retrieving the code category.
   *
   * @return the error message describing the result of retrieving the code category
   */
  public String getErrorMessage() {
    return errorMessage;
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
    Element rootElement = new Element("GetCodeCategoryResponse");

    rootElement.addContent(new Element("ErrorCode", Integer.toString(errorCode)));
    rootElement.addContent(
        new Element("ErrorMessage", StringUtils.hasText(errorMessage) ? errorMessage : ""));

    if (codeCategory != null) {
      rootElement.addContent(codeCategory.toElement());
    }

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

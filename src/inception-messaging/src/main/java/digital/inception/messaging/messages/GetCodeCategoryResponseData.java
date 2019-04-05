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
 * The <code>GetCodeCategoryResponseData</code> class manages the data for a
 * "Get Code Category Response" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class GetCodeCategoryResponseData extends WbxmlMessageData
{
  /**
   * The error code returned when an unknown error occurred.
   */
  public static final int ERROR_CODE_UNKNOWN_ERROR = -1;

  /**
   * The error code returned to indicate success.
   */
  private static final int ERROR_CODE_SUCCESS = 0;

  /**
   * The message returned to indicate success.
   */
  private static final String ERROR_MESSAGE_SUCCESS = "Success";

  /**
   * The UUID for the "Get Code Category Response" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
      "0336b544-91e5-4eb9-81db-3dd94e116c92");

  /**
   * The code category.
   */
  private CodeCategoryData codeCategory;

  /**
   * The error code indicating the result of retrieving the code category where a code of '0'
   * indicates success and a non-zero code indicates an error condition.
   */
  private int errorCode;

  /**
   * The error message describing the result of retrieving the code category.
   */
  private String errorMessage;

  /**
   * Constructs a new <code>GetCodesResponseData</code>.
   */
  public GetCodeCategoryResponseData()
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <code>GetCodeCategoryResponseData</code>.
   *
   * @param codeCategory the data for the code category
   */
  public GetCodeCategoryResponseData(CodeCategoryData codeCategory)
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.errorCode = ERROR_CODE_SUCCESS;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
    this.codeCategory = codeCategory;
  }

  /**
   * Constructs a new <code>GetCodeCategoryResponseData</code>.
   *
   * @param errorCode    the error code indicating the result of retrieving the code category
   * @param errorMessage the error message describing the result of retrieving the code category
   */
  public GetCodeCategoryResponseData(int errorCode, String errorMessage)
  {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   * <code>false</code> otherwise
   */
  public boolean fromMessageData(byte[] messageData)
    throws MessagingServiceException
  {
    Element rootElement = parseWBXML(messageData).getRootElement();

    if (!rootElement.getName().equals("GetCodeCategoryResponse"))
    {
      return false;
    }

    if ((!rootElement.hasChild("ErrorCode")) || (!rootElement.hasChild("ErrorMessage")))
    {
      return false;
    }

    this.errorCode = Integer.parseInt(rootElement.getChildText("ErrorCode"));
    this.errorMessage = rootElement.getChildText("ErrorMessage");

    if (errorCode == 0)
    {
      if (!rootElement.hasChild("CodeCategory"))
      {
        return false;
      }

      this.codeCategory = new CodeCategoryData(rootElement.getChild("CodeCategory"));
    }

    return true;
  }

  /**
   * Returns the code category.
   *
   * @return the code category
   */
  public CodeCategoryData getCodeCategory()
  {
    return codeCategory;
  }

  /**
   * Returns the error code indicating the result of retrieving the code category where a code of
   * '0' indicates success and a non-zero code indicates an error condition.
   *
   * @return the error code indicating the result of retrieving the code category where a code of
   * '0' indicates success and a non-zero code indicates an error condition
   */
  public int getErrorCode()
  {
    return errorCode;
  }

  /**
   * Returns the error message describing the result of retrieving the code category.
   *
   * @return the error message describing the result of retrieving the code category
   */
  public String getErrorMessage()
  {
    return errorMessage;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   * message
   */
  public byte[] toMessageData()
    throws MessagingServiceException
  {
    Element rootElement = new Element("GetCodeCategoryResponse");

    rootElement.addContent(new Element("ErrorCode", Integer.toString(errorCode)));
    rootElement.addContent(new Element("ErrorMessage", StringUtil.notNull(errorMessage)));

    if (codeCategory != null)
    {
      rootElement.addContent(codeCategory.toElement());
    }

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

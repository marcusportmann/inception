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

package digital.inception.messaging;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.core.wbxml.Parser;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * The <code>MessageResult</code> class stores the results of processing a message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class MessageResult
{
  /**
   * The message result code returned to indicate that an error occurred while decrypting a
   * message.
   */
  public static final int ERROR_DECRYPTION_FAILED = -3;

  /**
   * The message result code returned to indicate an invalid request.
   */
  public static final int ERROR_INVALID_REQUEST = -1;

  /**
   * The message result code returned to indicate that an error occurred while processing a message.
   */
  public static final int ERROR_PROCESSING_FAILED = -4;

  /**
   * The message result code returned to indicate that an error occurred while queueing a message.
   */
  public static final int ERROR_QUEUEING_FAILED = -5;

  /**
   * The message result code returned to indicate an error processing a message with an
   * unrecognised type.
   */
  public static final int ERROR_UNRECOGNISED_TYPE = -2;

  /**
   * The message result code returned to indicated successful processing of a message.
   */
  public static final int SUCCESS = 0;

  /**
   * The result code. A result code of 0 is used to indicate that the message was processed
   * successfully.
   */
  private long code;

  /**
   * The user-friendly text description of the result of processing the message. The
   * <code>detail</code> field may be blank if the message was processed successfully.
   */
  private String detail;

  /**
   * The flattened information for the exception that resulted from processing the message.
   */
  private String exception;

  /**
   * The message associated with the message result e.g. a response message.
   */
  private Message message;

  /**
   * Constructs a new <code>MessageResult</code>.
   *
   * @param document the WBXML document containing the message result information
   */
  public MessageResult(Document document)
    throws MessagingServiceException
  {
    Element rootElement = document.getRootElement();

    this.code = Long.parseLong(rootElement.getAttributeValue("code"));
    this.detail = rootElement.getAttributeValue("detail");

    if (rootElement.hasChild("Exception"))
    {
      Element exceptionElement = rootElement.getChild("Exception");

      exception = exceptionElement.getText();
    }

    if (rootElement.hasChild("Message"))
    {
      Element messageElement = rootElement.getChild("Message");

      try
      {
        Parser parser = new Parser();

        Document messageDocument = parser.parse(messageElement.getOpaque());

        message = new Message(messageDocument);
      }
      catch (Throwable e)
      {
        throw new MessagingServiceException(
            "Failed to parse the WBXML for the message associated with the message result", e);
      }
    }
  }

  /**
   * Constructs a new <code>MessageResult</code>.
   *
   * @param code   the result code
   * @param detail the text description of the result of processing the message
   */
  public MessageResult(long code, String detail)
  {
    this.code = code;
    this.detail = detail;
  }

  /**
   * Constructs a new <code>MessageResult</code>.
   *
   * @param code    the result code
   * @param detail  the text description of the result of processing the message
   * @param message the message associated with the <code>MessageResult</code>
   */
  public MessageResult(long code, String detail, Message message)
  {
    this.code = code;
    this.detail = detail;
    this.message = message;
  }

  /**
   * Constructs a new <code>MessageResult</code>.
   *
   * @param code   the result code
   * @param detail the text description of the result of processing the message
   * @param cause  the exception that resulted from processing the message
   */
  public MessageResult(long code, String detail, Throwable cause)
  {
    this.code = code;
    this.detail = detail;

    if (cause != null)
    {
      try
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        cause.printStackTrace(pw);
        pw.flush();

        exception = baos.toString();
      }
      catch (Throwable e)
      {
        exception = "Unable to dump the stack for the exception (" + cause + "): " + e.getMessage();
      }
    }
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message result information or
   * <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message result information or
   * <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element messageResultElement = document.getRootElement();

    return messageResultElement.getName().equals("MessageResult")
        && (messageResultElement.getAttributes().size() == 2)
        && !((!messageResultElement.hasAttribute("code")) || (!messageResultElement.hasAttribute(
            "detail")));
  }

  /**
   * Returns the result code.
   *
   * @return the result code
   */
  public long getCode()
  {
    return code;
  }

  /**
   * Returns the user-friendly text description of the result of processing the message.
   *
   * @return the user-friendly text description of the result of processing the message
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Returns the flattened information for the exception that resulted from processing the message.
   *
   * @return the flattened information for the exception that resulted from processing the message
   */
  public String getException()
  {
    return exception;
  }

  /**
   * Returns the message associated with the message result e.g. a response message.
   *
   * @return the message associated with the message result e.g. a response message.
   */
  public Message getMessage()
  {
    return message;
  }

  /**
   * Set the result code.
   *
   * @param code the result code
   */
  public void setCode(long code)
  {
    this.code = code;
  }

  /**
   * Set the user-friendly text description of the result of processing the message.
   *
   * @param detail the user-friendly text description of the result of processing the message
   */
  public void setDetail(String detail)
  {
    this.detail = detail;
  }

  /**
   * Set the flattened information for the exception that resulted from processing the message.
   *
   * @param exception the flattened information for the exception that resulted from processing the
   *                  message
   */
  public void setException(String exception)
  {
    this.exception = exception;
  }

  /**
   * Set the message associated with the message result e.g. a response message.
   *
   * @param message the message associated with the message result e.g. a response message
   */
  public void setMessage(Message message)
  {
    this.message = message;
  }

  /**
   * Returns the WBXML representation of the message result.
   *
   * @return the WBXML representation of the message result
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessageResult");

    rootElement.setAttribute("code", Long.toString(code));
    rootElement.setAttribute("detail", detail);

    if (exception != null)
    {
      Element exceptionElement = new Element("Exception");

      exceptionElement.addContent(exception);
      rootElement.addContent(exceptionElement);
    }

    if (message != null)
    {
      Element messageElement = new Element("Message");

      messageElement.addContent(message.toWBXML());
      rootElement.addContent(messageElement);
    }

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

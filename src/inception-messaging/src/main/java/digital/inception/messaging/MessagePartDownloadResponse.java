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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessagePartDownloadResponse</code> class represents the response to a request sent by
 * a mobile device to download the queued message parts for the device.
 *
 * @author Marcus Portmann
 */
public class MessagePartDownloadResponse
{
  /**
   * The error code returned to indicate an invalid request.
   */
  public static final int ERROR_INVALID_REQUEST = -1;

  /**
   * The error code returned to indicate an unknown error.
   */
  public static final int ERROR_UNKNOWN = -2;

  /**
   * The message result code returned to indicated successful processing of a message part download
   * request.
   */
  public static final int SUCCESS = 0;

  /**
   * The result code. A result code of 0 is used to indicate that the message part download request
   * was processed successfully.
   */
  private long code;

  /**
   * The user-friendly text description of the result of processing the message part download
   * request. The <code>detail</code> field may be blank if the message download request was
   * processed successfully.
   */
  private String detail;

  /**
   * The flattened information for the exception that resulted from processing the message part
   * download request.
   */
  private String exception;

  /**
   * The message parts being downloaded.
   */
  private List<MessagePart> messageParts;

  /**
   * Constructs a new <code>MessagePartDownloadResponse</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message part download response information
   */
  public MessagePartDownloadResponse(Document document)
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

    List<Element> messagePartElements = rootElement.getChildren("MessagePart");

    this.messageParts = new ArrayList<>();

    for (Element messagePartElement : messagePartElements)
    {
      try
      {
        Parser parser = new Parser();

        Document messagePartDocument = parser.parse(messagePartElement.getOpaque());

        this.messageParts.add(new MessagePart(messagePartDocument));
      }
      catch (Throwable e)
      {
        throw new MessagingServiceException(
            "Failed to parse the WBXML for a message part associated with "
            + "the message part download response", e);
      }
    }
  }

  /**
   * Constructs a new <code>MessagePartDownloadResponse</code>.
   *
   * @param messageParts the message parts being downloaded
   */
  public MessagePartDownloadResponse(List<MessagePart> messageParts)
  {
    this.code = SUCCESS;
    this.detail = "";
    this.messageParts = messageParts;
  }

  /**
   * Constructs a new <code>MessagePartDownloadResponse</code>.
   *
   * @param code   the result code
   * @param detail the text description of the result of processing the message part download
   *               request
   */
  public MessagePartDownloadResponse(long code, String detail)
  {
    this.code = code;
    this.detail = detail;
  }

  /**
   * Constructs a new <code>MessagePartDownloadResponse</code>.
   *
   * @param code   the result code
   * @param detail the text description of the result of processing the message part download
   *               request
   * @param cause  the exception that resulted from processing the message part download request
   */
  public MessagePartDownloadResponse(long code, String detail, Throwable cause)
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
   * Returns <code>true</code> if the WBXML document contains valid message part download response
   * information or <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message part download response
   * information or <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePartDownloadResponse")
        && (rootElement.getAttributes().size() == 2)
        && rootElement.hasAttribute("code")
        && rootElement.hasAttribute("detail");
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
   * Return the user-friendly text description of the result of processing the message part
   * download request.
   *
   * @return the user-friendly text description of the result of processing the message part
   * download request
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Return the flattened information for the exception that resulted from processing the message
   * part download request.
   *
   * @return the flattened information for the exception that resulted from processing the message
   * part download request
   */
  public String getException()
  {
    return exception;
  }

  /**
   * Returns the message parts being downloaded.
   *
   * @return the message parts being downloaded
   */
  public List<MessagePart> getMessageParts()
  {
    return messageParts;
  }

  /**
   * Returns the String representation of the message part download response.
   *
   * @return the String representation of the message part download response.
   */
  @Override
  public String toString()
  {
    StringBuilder buffer = new StringBuilder("<MessagePartDownloadResponse");

    buffer.append(" code=\"").append(code).append("\"");
    buffer.append(" detail=\"").append(detail).append("\"");
    buffer.append(">");

    if (messageParts != null)
    {
      for (MessagePart messagePart : messageParts)
      {
        buffer.append(messagePart.toString());
      }
    }

    buffer.append("</MessagePartDownloadResponse>");

    return buffer.toString();
  }

  /**
   * Returns the WBXML representation of the message part download response.
   *
   * @return the WBXML representation of the message part download response
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessagePartDownloadResponse");

    rootElement.setAttribute("code", Long.toString(code));
    rootElement.setAttribute("detail", detail);

    if (exception != null)
    {
      Element exceptionElement = new Element("Exception");

      exceptionElement.addContent(exception);
      rootElement.addContent(exceptionElement);
    }

    if (messageParts != null)
    {
      for (MessagePart messagePart : messageParts)
      {
        rootElement.addContent(new Element("MessagePart", messagePart.toWBXML()));
      }
    }

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

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

package digital.inception.messaging;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.core.wbxml.Parser;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessageDownloadResponse</code> class represents the response to a request sent by a
 * mobile device to download the queued messages for the device.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class MessageDownloadResponse {

  /** The error code returned to indicate an invalid request. */
  public static final int ERROR_INVALID_REQUEST = -1;

  /** The error code returned to indicate an unknown error. */
  public static final int ERROR_UNKNOWN = -2;

  /**
   * The message result code returned to indicate successful processing of a message download
   * request.
   */
  public static final int SUCCESS = 0;

  /**
   * The result code. A result code of 0 is used to indicate that the message download request was
   * processed successfully.
   */
  private long code;

  /**
   * The user-friendly text description of the result of processing the message download request.
   * The <code>detail</code> field may be blank if the message download request was processed
   * successfully.
   */
  private String detail;

  /**
   * The flattened information for the exception that resulted from processing the message download
   * request.
   */
  private String exception;

  /** The messages being downloaded. */
  private List<Message> messages;

  /**
   * Constructs a new <code>MessageDownloadResponse</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message download response information
   */
  public MessageDownloadResponse(Document document) throws MessagingServiceException {
    Element rootElement = document.getRootElement();

    this.code = Long.parseLong(rootElement.getAttributeValue("code"));
    this.detail = rootElement.getAttributeValue("detail");

    if (rootElement.hasChild("Exception")) {
      Element exceptionElement = rootElement.getChild("Exception");

      exception = exceptionElement.getText();
    }

    List<Element> messageElements = rootElement.getChildren("Message");

    this.messages = new ArrayList<>();

    for (Element messageElement : messageElements) {
      try {
        Parser parser = new Parser();

        Document messageDocument = parser.parse(messageElement.getOpaque());

        this.messages.add(new Message(messageDocument));
      } catch (Throwable e) {
        throw new MessagingServiceException(
            "Failed to parse the WBXML for a message associated with "
                + "the message download response",
            e);
      }
    }
  }

  /**
   * Constructs a new <code>MessageDownloadResponse</code>.
   *
   * @param messages the messages being downloaded
   */
  MessageDownloadResponse(List<Message> messages) {
    this.code = SUCCESS;
    this.detail = "";
    this.messages = messages;
  }

  /**
   * Constructs a new <code>MessageDownloadResponse</code>.
   *
   * @param code the result code
   * @param detail the text description of the result of processing the message download request
   */
  MessageDownloadResponse(long code, String detail) {
    this.code = code;
    this.detail = detail;
  }

  /**
   * Constructs a new <code>MessageDownloadResponse</code>.
   *
   * @param code the result code
   * @param detail the text description of the result of processing the message download request
   * @param cause the exception that resulted from processing the message download request
   */
  public MessageDownloadResponse(long code, String detail, Throwable cause) {
    this.code = code;
    this.detail = detail;

    if (cause != null) {
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        cause.printStackTrace(pw);
        pw.flush();

        exception = baos.toString();
      } catch (Throwable e) {
        exception = "Unable to dump the stack for the exception (" + cause + "): " + e.getMessage();
      }
    }
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message download response
   * information or <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   * @return <code>true</code> if the WBXML document contains valid message download response
   *     information or <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document) {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessageDownloadResponse")
        && (rootElement.getAttributes().size() == 2)
        && rootElement.hasAttribute("code")
        && rootElement.hasAttribute("detail");
  }

  /**
   * Returns the result code.
   *
   * @return the result code
   */
  public long getCode() {
    return code;
  }

  /**
   * Return the user-friendly text description of the result of processing the message download
   * request.
   *
   * @return the user-friendly text description of the result of processing the message download
   *     request
   */
  public String getDetail() {
    return detail;
  }

  /**
   * Return the flattened information for the exception that resulted from processing the message
   * download request.
   *
   * @return the flattened information for the exception that resulted from processing the message
   *     download request
   */
  public String getException() {
    return exception;
  }

  /**
   * Returns the messages being downloaded.
   *
   * @return the messages being downloaded
   */
  public List<Message> getMessages() {
    return messages;
  }

  /**
   * Returns the number of messages being downloaded.
   *
   * @return the number of messages being downloaded
   */
  public int getNumberOfMessages() {
    return (messages != null) ? messages.size() : 0;
  }

  /**
   * Set the result code.
   *
   * @param code the result code
   */
  public void setCode(long code) {
    this.code = code;
  }

  /**
   * Set the user-friendly text description of the result of processing the message download
   * request.
   *
   * @param detail the user-friendly text description of the result of processing the message
   *     download request
   */
  public void setDetail(String detail) {
    this.detail = detail;
  }

  /**
   * Set the flattened information for the exception that resulted from processing the message
   * download request.
   *
   * @param exception the flattened information for the exception that resulted from processing the
   *     message download request
   */
  public void setException(String exception) {
    this.exception = exception;
  }

  /**
   * Set the messages being downloaded.
   *
   * @param messages the messages being downloaded
   */
  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  /**
   * Returns the String representation of the message download response.
   *
   * @return the String representation of the message download response.
   */
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder("<MessageDownloadResponse");

    buffer.append(" code=\"").append(code).append("\"");
    buffer.append(" detail=\"").append(detail).append("\"");
    buffer.append(">");

    if (messages != null) {
      for (Message message : messages) {
        buffer.append(message.toString());
      }
    }

    buffer.append("</MessageDownloadResponse>");

    return buffer.toString();
  }

  /**
   * Returns the WBXML representation of the message download response.
   *
   * @return the WBXML representation of the message download response
   */
  public byte[] toWBXML() {
    Element rootElement = new Element("MessageDownloadResponse");

    rootElement.setAttribute("code", Long.toString(code));
    rootElement.setAttribute("detail", detail);

    if (exception != null) {
      Element exceptionElement = new Element("Exception");

      exceptionElement.addContent(exception);
      rootElement.addContent(exceptionElement);
    }

    if (messages != null) {
      for (Message message : messages) {
        rootElement.addContent(new Element("Message", message.toWBXML()));
      }
    }

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

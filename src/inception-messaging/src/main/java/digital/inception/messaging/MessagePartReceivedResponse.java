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

package digital.inception.messaging;

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * The <b>MessagePartReceivedResponse</b> class represents the response to a request sent by a
 * mobile device to acknowledge the successful download of a message part.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class MessagePartReceivedResponse {

  /** The error code returned to indicate an invalid request. */
  public static final int ERROR_INVALID_REQUEST = -1;

  /** The error code returned to indicate an unknown error. */
  public static final int ERROR_UNKNOWN = -2;

  /**
   * The message result code returned to indicated successful processing of a message part received
   * request.
   */
  public static final int SUCCESS = 0;

  /**
   * The result code. A result code of 0 is used to indicate that the message part received request
   * was processed successfully.
   */
  private long code;

  /**
   * The user-friendly text description of the result of processing the message part received
   * request. The <b>detail</b> field may be blank if the message received request was processed
   * successfully.
   */
  private String detail;

  /**
   * The flattened information for the exception that resulted from processing the message part
   * received request.
   */
  private String exception;

  /**
   * Constructs a new <b>MessagePartReceivedResponse</b> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message part received response information
   */
  public MessagePartReceivedResponse(Document document) {
    Element rootElement = document.getRootElement();

    this.code = Long.parseLong(rootElement.getAttributeValue("code"));
    this.detail = rootElement.getAttributeValue("detail");

    if (rootElement.hasChild("Exception")) {
      Element exceptionElement = rootElement.getChild("Exception");

      exception = exceptionElement.getText();
    }
  }

  /**
   * Constructs a new <b>MessagePartReceivedResponse</b>.
   *
   * @param code the result code
   * @param detail the text description of the result of processing the message part download
   *     request
   */
  public MessagePartReceivedResponse(long code, String detail) {
    this.code = code;
    this.detail = detail;
  }

  /**
   * Constructs a new <b>MessagePartReceivedResponse</b>.
   *
   * @param code the result code
   * @param detail the text description of the result of processing the message part received
   *     request
   * @param cause the exception that resulted from processing the message part received request
   */
  public MessagePartReceivedResponse(long code, String detail, Throwable cause) {
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
        exception =
            String.format(
                "Unable to dump the stack for the exception (%s): %s", cause, e.getMessage());
      }
    }
  }

  /**
   * Returns <b>true</b> if the WBXML document contains valid message part received response
   * information or <b>false</b> otherwise.
   *
   * @param document the WBXML document to validate
   * @return <b>true</b> if the WBXML document contains valid message part received response
   *     information or <b>false</b> otherwise
   */
  public static boolean isValidWBXML(Document document) {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePartReceivedResponse")
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
   * Return the user-friendly text description of the result of processing the message part received
   * request.
   *
   * @return the user-friendly text description of the result of processing the message part
   *     received request
   */
  public String getDetail() {
    return detail;
  }

  /**
   * Return the flattened information for the exception that resulted from processing the message
   * part received request.
   *
   * @return the flattened information for the exception that resulted from processing the message
   *     part received request
   */
  public String getException() {
    return exception;
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
   * Set the user-friendly text description of the result of processing the message part download
   * request.
   *
   * @param detail the user-friendly text description of the result of processing the message part
   *     download request
   */
  public void setDetail(String detail) {
    this.detail = detail;
  }

  /**
   * Set the flattened information for the exception that resulted from processing the message part
   * download request.
   *
   * @param exception the flattened information for the exception that resulted from processing the
   *     message part download request
   */
  public void setException(String exception) {
    this.exception = exception;
  }

  /**
   * Returns the String representation of the message part received response.
   *
   * @return the String representation of the message part received response.
   */
  @Override
  public String toString() {
    return String.format("<MessagePartReceivedResponse code=\"%d\" detail=\"%s\"/>", code, detail);
  }

  /**
   * Returns the WBXML representation of the message part received response.
   *
   * @return the WBXML representation of the message part received response
   */
  public byte[] toWBXML() {
    Element rootElement = new Element("MessagePartReceivedResponse");

    rootElement.setAttribute("code", Long.toString(code));
    rootElement.setAttribute("detail", detail);

    if (exception != null) {
      Element exceptionElement = new Element("Exception");

      exceptionElement.addContent(exception);
      rootElement.addContent(exceptionElement);
    }

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

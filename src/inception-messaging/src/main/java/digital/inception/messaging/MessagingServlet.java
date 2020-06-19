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

import digital.inception.Debug;
import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Parser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessageServlet</code> servlet.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class MessagingServlet extends HttpServlet {

  /**
   * The HTTP content-type used when receiving and sending WBXML.
   */
  private static final String WBXML_CONTENT_TYPE = "application/wbxml";

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(MessagingServlet.class);
  private static final long serialVersionUID = 1000000;

  /**
   * Is the messaging servlet initialized?
   */
  private boolean isInitialized;

  /* Messaging Service */
  @Autowired
  private IMessagingService messagingService;

  /**
   * Constructs a new <code>MessagingServlet</code>.
   */
  public MessagingServlet() {
  }

  /**
   * Initialize the servlet.
   *
   * @param config the servlet configuration
   */
  @Override
  public void init(ServletConfig config)
      throws ServletException {
    super.init(config);

    initMessagingServlet();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (!isInitialized) {
      initMessagingServlet();
    }

    // Check the format of the request data
    if ((request.getContentType() == null)
        || (!request.getContentType().equals(WBXML_CONTENT_TYPE))) {
      response.sendError(500, String.format("Invalid content type (%s) expecting (%s)",
          request.getContentType(), WBXML_CONTENT_TYPE));

      return;
    }

    try {
      // Retrieve the WBXML document from the HTTP servlet request
      Document document = readRequestDocument(request);

      if (document == null) {
        return;
      }

      switch (document.getRootElement().getName()) {
        // We are processing a Message...
        case "Message":
          if (!processMessage(document, response)) {
            if (Debug.inDebugMode()) {
              logger.debug("Failed to process the message: " + document.toString());
            }
          }

          break;

        // We are processing a MessagePart...
        case "MessagePart":
          if (!processMessagePart(document, response)) {
            if (Debug.inDebugMode()) {
              logger.debug("Failed to process the message part: " + document.toString());
            }
          }

          break;

        // We are processing a request to download messages queued for a device
        case "MessageDownloadRequest":
          if (!processMessageDownloadRequest(document, response)) {
            if (Debug.inDebugMode()) {
              logger.debug("Failed to process the message download request: "
                  + document.toString());
            }
          }

          break;

        // We are processing an acknowledgement that a message has been downloaded successfully
        case "MessageReceivedRequest":
          if (!processMessageReceivedRequest(document, response)) {
            if (Debug.inDebugMode()) {
              logger.debug("Failed to process the message received request: "
                  + document.toString());
            }
          }

          break;

        // We are processing a request to download message parts queued for a device
        case "MessagePartDownloadRequest":
          if (!processMessagePartDownloadRequest(document, response)) {
            if (Debug.inDebugMode()) {
              logger.debug("Failed to process the message part download request: "
                  + document.toString());
            }
          }

          break;

        // We are processing an acknowledgement that a message part has been downloaded successfully
        case "MessagePartReceivedRequest":
          if (!processMessagePartReceivedRequest(document, response)) {
            if (Debug.inDebugMode()) {
              logger.debug("Failed to process the message part received request: "
                  + document.toString());
            }
          }

          break;

        default:
          throw new ServletException(String.format(
              "Failed to process the unrecognised WBXML document with the root element (%s) read "
                  + "from the HTTP servlet request", document.getRootElement().getName()));
      }
    } catch (Throwable e) {
      logger.error("Failed to process the HTTP request", e);

      writeErrorResponse(e.getMessage(), e, response);
    }
  }

  private synchronized void initMessagingServlet() {
    if (!isInitialized) {
      /*
       * If the Messaging Service has not been injected e.g. because we are invoking the Messaging
       * Servlet as part of a unit test then stop here.
       */
      if (messagingService == null) {
        return;
      }

      /*
       * Reset the locks for any messages that were locked for downloading by a remote user-device
       * combination but which were not successfully downloaded.
       */
      try {
        logger.info("Resetting the message locks for the messages being downloaded");

        messagingService.resetMessageLocks(MessageStatus.DOWNLOADING, MessageStatus
            .QUEUED_FOR_DOWNLOAD);
      } catch (Throwable e) {
        logger.error("Failed to reset the message locks for the messages being downloaded", e);
      }

      /*
       * Reset the locks for the message parts that were locked for assembly but which were not
       * successfully assembled.
       */
      try {
        logger.info("Resetting the message part locks for the message parts being assembled");

        messagingService.resetMessagePartLocks(MessagePartStatus.ASSEMBLING, MessagePartStatus
            .QUEUED_FOR_ASSEMBLY);
      } catch (Exception e) {
        logger.error(
            "Failed to reset the message part locks for the message parts being assembled", e);
      }

      isInitialized = true;
    }
  }

  private boolean processMessage(Document document, HttpServletResponse response)
      throws MessagingServiceException {
    // Is the WBXML document valid
    if (!Message.isValidWBXML(document)) {
      logger.warn("Failed to process the invalid message WBXML document: " + document.toString());

      MessageResult result = new MessageResult(MessageResult.ERROR_INVALID_REQUEST,
          "Failed to process the invalid WBXML document containing the message information");

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }

    // Create the message and initialize it using the WBXML document sent as part of the request
    Message requestMessage = new Message(document);

    // Check whether we can process the message
    if (!messagingService.canProcessMessage(requestMessage)) {
      logger.warn(String.format(
          "Failed to process the unrecognised message (%s) from the user (%s) and the device (%s)",
          requestMessage.getId(), requestMessage.getUsername(), requestMessage.getDeviceId()));

      MessageResult messageResult = new MessageResult(MessageResult.ERROR_UNRECOGNISED_TYPE,
          String.format("Failed to process the message (%s) with the unrecognised type (%s)",
              requestMessage.getId(), requestMessage.getTypeId()));

      writeResponseDocument(messageResult.toWBXML(), response);

      return false;
    }

    if (logger.isDebugEnabled()) {
      logger.debug(String.format(
          "Processing the message (%s) with type (%s) from the user (%s) and the device (%s)",
          requestMessage.getId(), requestMessage.getTypeId(), requestMessage.getUsername(),
          requestMessage.getDeviceId()));

      logger.debug(requestMessage.toString());
    }

    /*
     * Process the message synchronously if required, otherwise queue it for asynchronous
     * processing.
     */
    if (messagingService.isSynchronousMessage(requestMessage)) {
      // Decrypt the message data if required
      boolean isRequestMessageEncrypted = requestMessage.isEncrypted();

      if (requestMessage.isEncrypted()) {
        if (!messagingService.decryptMessage(requestMessage)) {
          logger.warn(String.format(
              "Failed to decrypt the message (%s) from the user (%s) and device (%s)",
              requestMessage.getId(), requestMessage.getUsername(), requestMessage.getDeviceId()));

          MessageResult messageResult = new MessageResult(MessageResult.ERROR_DECRYPTION_FAILED,
              String.format("Failed to decrypt and process the message (%s)",
                  requestMessage.getId()));

          writeResponseDocument(messageResult.toWBXML(), response);

          return false;
        }
      } else {
        if (messagingService.isSecureMessage(requestMessage)) {
          logger.warn(String.format(
              "Failed to process the message (%s) from the user (%s) and device (%s) that should "
                  + "be processed securely but is not encrypted", requestMessage.getId(),
              requestMessage.getUsername(), requestMessage.getDeviceId()));

          MessageResult messageResult = new MessageResult(MessageResult.ERROR_DECRYPTION_FAILED,
              "Failed to process the message (" + requestMessage.getId() +
                  ") that should be processed securely and is not encrypted");

          writeResponseDocument(messageResult.toWBXML(), response);

          return false;
        }
      }

      try {
        // Attempt to archive the synchronous request message
        messagingService.archiveMessage(requestMessage);

        // Attempt to process the synchronous message
        Message responseMessage = messagingService.processMessage(requestMessage);

        // Attempt to archive the synchronous response message
        messagingService.archiveMessage(responseMessage);

        MessageResult messageResult;

        if (responseMessage != null) {
          if ((isRequestMessageEncrypted) && (!responseMessage.isEncrypted())) {
            messagingService.encryptMessage(responseMessage);
          }

          messageResult = new MessageResult(0, String.format(
              "Successfully processed the message (%s)", requestMessage.getId()), responseMessage);
        } else {
          messageResult = new MessageResult(0, String.format(
              "Successfully processed the message (%s)", requestMessage.getId()));
        }

        writeResponseDocument(messageResult.toWBXML(), response);

        return true;
      } catch (Throwable e) {
        logger.error(String.format(
            "Failed to process the message (%s) from the user (%s) and device (%s)",
            requestMessage.getId(), requestMessage.getUsername(), requestMessage.getDeviceId()), e);

        MessageResult messageResult = new MessageResult(MessageResult.ERROR_PROCESSING_FAILED,
            String.format("Failed to process the message (%s)", requestMessage.getId()), e);

        writeResponseDocument(messageResult.toWBXML(), response);

        return false;
      }
    } else if (messagingService.isAsynchronousMessage(requestMessage)) {
      return queueMessageForAsynchronousProcessing(requestMessage, response);
    } else {
      MessageResult result = new MessageResult(MessageResult.ERROR_PROCESSING_FAILED, String.format(
          "Synchronous and asynchronous processing are not supported for the message (%s) with "
              + "type (%s)", requestMessage.getId(), requestMessage.getTypeId()));

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }
  }

  private boolean processMessageDownloadRequest(Document document, HttpServletResponse response) {
    // Is the WBXML document valid
    if (!MessageDownloadRequest.isValidWBXML(document)) {
      logger.warn("Failed to process the invalid message download request WBXML document: "
          + document.toString());

      MessageDownloadResponse result = new MessageDownloadResponse(MessageDownloadResponse
          .ERROR_INVALID_REQUEST,
          "Failed to process the invalid WBXML document containing the message download request "
              + "information");

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }

    MessageDownloadRequest downloadRequest = new MessageDownloadRequest(document);

    try {
      List<Message> messages = messagingService.getMessagesQueuedForDownload(
          downloadRequest.getUsername(), downloadRequest.getDeviceId());

      if (logger.isDebugEnabled()) {
        logger.debug(String.format(
            "Found %d messages queued for download for the user (%s) and the device (%s)",
            messages.size(), downloadRequest.getUsername(), downloadRequest.getDeviceId()));
      }

      MessageDownloadResponse downloadResponse = new MessageDownloadResponse(messages);

      writeResponseDocument(downloadResponse.toWBXML(), response);

      /*
       * NOTE: The messages are NOT flagged as successfully downloaded until we receive a
       *       notification from the device that this is the case. If the application is
       *       restarted in the interim then the messages "locked" for download will be
       *       "unlocked" and the messages will be downloaded again. It is the responsibility
       *       of the device to ignore any duplicate messages.
       *
       *       See the MessageReceivedRequest section below.
       */

      return true;
    } catch (Throwable e) {
      logger.error(String.format(
          "Failed to retrieve the messages that have been queued for download for the user (%s) "
              + "and the device (%s)", downloadRequest.getUsername(),
          downloadRequest.getDeviceId()),
          e);

      MessageDownloadResponse downloadResponse = new MessageDownloadResponse(MessageDownloadResponse
          .ERROR_UNKNOWN, String.format(
          "Failed to retrieve the messages that have been queued for download for the user (%s) "
              + "and the device (%s)", downloadRequest.getUsername(),
          downloadRequest.getDeviceId()),
          e);

      writeResponseDocument(downloadResponse.toWBXML(), response);

      return false;
    }
  }

  private boolean processMessagePart(Document document, HttpServletResponse response) {
    // Is the WBXML document valid
    if (!MessagePart.isValidWBXML(document)) {
      logger.warn("Failed to process the invalid message part WBXML document: "
          + document.toString());

      MessagePartResult result = new MessagePartResult(MessagePartResult.ERROR_INVALID_REQUEST,
          "Failed to process the invalid WBXML document containing the message part information");

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }

    /*
     * Create the message part and initialize it using the WBXML document sent as part of the
     * request
     */
    MessagePart messagePart = new MessagePart(document);

    // Check whether we can process the message part
    if (!messagingService.canQueueMessagePartForAssembly(messagePart)) {
      logger.warn(String.format(
          "Failed to queue the unrecognised message part (%s) from the user (%s) and the device "
              + "(%s) with message type (%s)", messagePart.getId(),
          messagePart.getMessageUsername(),
          messagePart.getMessageDeviceId(), messagePart.getMessageTypeId()));

      MessagePartResult result = new MessagePartResult(MessagePartResult.ERROR_UNRECOGNISED_TYPE,
          String.format(
              "Failed to queue the unrecognised message part (%s) from the user (%s) and the device "
                  + "(%s) with message type (%s)", messagePart.getId(),
              messagePart.getMessageUsername(),
              messagePart.getMessageDeviceId(), messagePart.getMessageTypeId()));

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }

    logger.debug(String.format(
        "Queuing the message part (%d/%d) for the message (%s) from the user (%s) and the device "
            + "(%s) with message type (%s)", messagePart.getPartNo(), messagePart.getTotalParts(),
        messagePart.getMessageId(), messagePart.getMessageUsername(),
        messagePart.getMessageDeviceId(), messagePart.getMessageTypeId()));

    logger.debug(messagePart.toString());

    // Queue the message part for assembly
    try {
      messagingService.queueMessagePartForAssemblyAndAssembleAndProcessMessage(messagePart);

      MessagePartResult result = new MessagePartResult(MessagePartResult.SUCCESS, String.format(
          "Successfully queued the message part (%s)", messagePart.getId()));

      writeResponseDocument(result.toWBXML(), response);

      return true;
    } catch (Exception e) {
      logger.error(String.format(
          "Failed to queue the message part (%s) from the user (%s) and the device (%s) with "
              + "message type (%s)", messagePart.getId(), messagePart.getMessageUsername(),
          messagePart.getMessageDeviceId(), messagePart.getMessageTypeId()), e);

      MessagePartResult result = new MessagePartResult(MessagePartResult.ERROR_QUEUEING_FAILED,
          String.format("Failed to queue the message part (%s)", messagePart.getId()), e);

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }
  }

  private boolean processMessagePartDownloadRequest(Document document,
      HttpServletResponse response) {
    // Is the WBXML document valid
    if (!MessagePartDownloadRequest.isValidWBXML(document)) {
      logger.warn("Failed to process the invalid message part download request WBXML document: "
          + document.toString());

      MessagePartDownloadResponse result = new MessagePartDownloadResponse(
          MessagePartDownloadResponse.ERROR_INVALID_REQUEST,
          "Failed to process the invalid WBXML document containing the message part download "
              + "request information");

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }

    MessagePartDownloadRequest downloadRequest = new MessagePartDownloadRequest(document);

    try {
      List<MessagePart> messageParts = messagingService.getMessagePartsQueuedForDownload(
          downloadRequest.getUsername(), downloadRequest.getDeviceId());

      if (logger.isDebugEnabled()) {
        logger.debug(String.format(
            "Found %d message partsc queued for download for the user (%s) and the device (%s)",
            messageParts.size(), downloadRequest.getUsername(), downloadRequest.getDeviceId()));
      }

      /*
       * NOTE: The message parts are not encrypted. If the message data needs to be encrypted then
       *       the original message needs to be encrypted BEFORE it is queued for download and
       *       split up into a number of message parts.
       */

      MessagePartDownloadResponse downloadResponse = new MessagePartDownloadResponse(messageParts);

      writeResponseDocument(downloadResponse.toWBXML(), response);

      /*
       * NOTE: The message parts are NOT flagged as successfully downloaded until we receive a
       *       notification from the device that this is the case. If the application is
       *       restarted in the interim then the message parts "locked" for download will be
       *       "unlocked" and the message parts will be downloaded again. It is the responsibility
       *       of the device to ignore any duplicate message parts.
       *
       *       See the MessagePartReceivedRequest section below.
       */

      return true;
    } catch (Throwable e) {
      logger.error(String.format(
          "Failed to retrieve the message parts that have been queued for download for the user "
              + "(%s) and the device (%s)", downloadRequest.getUsername(),
          downloadRequest.getDeviceId()), e);

      MessagePartDownloadResponse downloadResponse = new MessagePartDownloadResponse(
          MessagePartDownloadResponse.ERROR_UNKNOWN, String.format(
          "Failed to retrieve the message parts that have been queued for download for the user "
              + "(%s) and the device (%s)", downloadRequest.getUsername(),
          downloadRequest.getDeviceId()), e);

      writeResponseDocument(downloadResponse.toWBXML(), response);

      return false;
    }
  }

  private boolean processMessagePartReceivedRequest(Document document,
      HttpServletResponse response) {
    if (!MessagePartReceivedRequest.isValidWBXML(document)) {
      logger.warn("Failed to process the invalid message part received request WBXML document: "
          + document.toString());

      MessagePartReceivedResponse result = new MessagePartReceivedResponse(
          MessagePartReceivedResponse.ERROR_INVALID_REQUEST,
          "Failed to process the invalid WBXML document containing the message part received "
              + "request information");

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }

    MessagePartReceivedRequest receivedRequest = new MessagePartReceivedRequest(document);

    try {
      messagingService.deleteMessagePart(receivedRequest.getMessagePartId());

      MessagePartReceivedResponse result = new MessagePartReceivedResponse(
          MessagePartReceivedResponse.SUCCESS, String.format(
          "Successfully acknowledged receipt of the message part (%s)",
          receivedRequest.getMessagePartId()));

      writeResponseDocument(result.toWBXML(), response);

      return true;
    } catch (Exception e) {
      logger.error(String.format(
          "Failed to process the message part received request for the message part (%s) from the "
              + "device (%s)", receivedRequest.getMessagePartId(), receivedRequest.getDeviceId()),
          e);

      MessagePartReceivedResponse result = new MessagePartReceivedResponse(
          MessagePartReceivedResponse.ERROR_UNKNOWN, String.format(
          "Failed to process the message part received request for the message part (%s) from the "
              + "device (%s)", receivedRequest.getMessagePartId(), receivedRequest.getDeviceId()),
          e);

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }
  }

  private boolean processMessageReceivedRequest(Document document, HttpServletResponse response) {
    if (!MessageReceivedRequest.isValidWBXML(document)) {
      logger.warn("Failed to process the invalid message received request WBXML document: "
          + document.toString());

      MessageReceivedResponse result = new MessageReceivedResponse(MessageReceivedResponse
          .ERROR_INVALID_REQUEST,
          "Failed to process the invalid WBXML document containing the message received request "
              + "information");

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }

    MessageReceivedRequest receivedRequest = new MessageReceivedRequest(document);

    try {
      messagingService.deleteMessage(receivedRequest.getMessageId());

      MessageReceivedResponse result = new MessageReceivedResponse(MessageReceivedResponse.SUCCESS,
          String.format("Successfully acknowledged receipt of the message (%s)",
              receivedRequest.getMessageId()));

      writeResponseDocument(result.toWBXML(), response);

      return true;
    } catch (Exception e) {
      logger.error(String.format(
          "Failed to process the message received request for the message (%s) from the device (%s)",
          receivedRequest.getMessageId(), receivedRequest.getDeviceId()), e);

      MessageReceivedResponse result = new MessageReceivedResponse(MessageReceivedResponse
          .ERROR_UNKNOWN, String.format(
          "Failed to process the message received request for the message (%s) from the device (%s)",
          receivedRequest.getMessageId(), receivedRequest.getDeviceId()), e);

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }
  }

  private boolean queueMessageForAsynchronousProcessing(Message message,
      HttpServletResponse response) {
    try {
      /*
       * Check whether the message has already been queued for processing by examining the
       * archive log. This will prevent duplicate message submissions from the device due
       * to communication errors.
       */
      if (messagingService.isMessageArchived(message.getId())) {
        logger.debug(String.format(
            "The message (%s) has already been queued for processing and will be ignored",
            message.getId()));

        return true;
      }

      messagingService.queueMessageForProcessingAndProcessMessage(message);

      MessageResult result = new MessageResult(0, String.format(
          "Successfully queued the message (%s) for processing", message.getId()));

      writeResponseDocument(result.toWBXML(), response);

      return true;
    } catch (Exception e) {
      logger.error(String.format(
          "Failed to queue the message (%s) from the user (%s) and device (%s) for processing",
          message.getId(), message.getUsername(), message.getDeviceId()), e);

      MessageResult result = new MessageResult(MessageResult.ERROR_QUEUEING_FAILED, String.format(
          "Failed to queue the message (%s) for processing", message.getId()), e);

      writeResponseDocument(result.toWBXML(), response);

      return false;
    }
  }

  /**
   * Read the WBXML request document from the HTTP servlet request.
   *
   * @param request the HTTP servlet request to read the WBXML request document from
   *
   * @return the WBXML request document
   */
  private Document readRequestDocument(HttpServletRequest request)
      throws ServletException {
    // Read the request data
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try (ServletInputStream in = request.getInputStream()) {
      byte[] readBuffer = new byte[2048];

      int numberOfBytesRead;

      while ((numberOfBytesRead = in.read(readBuffer)) != -1) {
        baos.write(readBuffer, 0, numberOfBytesRead);
      }
    } catch (Throwable e) {
      // A network error means that the document could not be read so stop here
      return null;
    }

    try {
      Parser parser = new Parser();

      return parser.parse(baos.toByteArray());
    } catch (Throwable e) {
      throw new ServletException(
          "Failed to parse the WBXML request document from the HTTP servlet request", e);
    }
  }

  /**
   * Write the specified error information to the HTTP response.
   *
   * @param message   the error message
   * @param exception the exception containing the error information which may be <code>null</code>
   * @param response  the HTTP servlet response to write the error information to
   */
  private void writeErrorResponse(String message, Throwable exception,
      HttpServletResponse response) {
    try {
      response.sendError(500, message);

      PrintWriter pw = response.getWriter();

      pw.println("<html>");
      pw.println("<head>");
      pw.println("  <style>");
      pw.println(
          "    body {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 8pt;}");
      pw.println(
          "    h1 {thirdparty-family: Tahoma, Verdana, Arial, Helvetica; thirdparty-size: 12pt;}");
      pw.println("      .section {padding-top: 10px; padding-bottom: 2px; color: green; "
          + "thirdparty-weight: bold; thirdparty-size: 9pt;}");
      pw.println("    .className {color: 808080;}");
      pw.println("  </style>");
      pw.println("</head>");
      pw.println("<body>");

      pw.println("  <h1><thirdparty color=\"red\">ERROR</thirdparty></h1>");
      pw.println("  " + exception.getMessage());

      pw.println("</body>");
      pw.println("</html>");
    } catch (IOException ignored) {
    }
  }

  /**
   * Write the binary data for the WBXML response document to the HTTP servlet response.
   *
   * @param data     the binary data for the WBXML response document
   * @param response the HTTP servlet response to write the binary data for the WBXML response
   *                 document to
   */
  private void writeResponseDocument(byte[] data, HttpServletResponse response) {
    try {
      ServletOutputStream out = response.getOutputStream();

      response.setContentType(WBXML_CONTENT_TYPE);

      out.write(data);

      out.flush();
    } catch (Throwable e) {
      logger.error(
          "Failed to write the binary data for the WBXML response document to the HTTP servlet "
              + "response", e);
    }
  }
}

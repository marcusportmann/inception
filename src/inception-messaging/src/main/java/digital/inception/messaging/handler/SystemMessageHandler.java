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

package digital.inception.messaging.handler;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.codes.Code;
import digital.inception.codes.CodeCategory;
import digital.inception.codes.CodeCategoryNotFoundException;
import digital.inception.codes.ICodesService;
import digital.inception.core.util.Base64Util;
import digital.inception.core.util.ExceptionUtil;
import digital.inception.error.ErrorReport;
import digital.inception.error.IErrorService;
import digital.inception.messaging.IMessagingService;
import digital.inception.messaging.Message;
import digital.inception.messaging.MessageTranslator;
import digital.inception.messaging.messages.AnotherTestRequestData;
import digital.inception.messaging.messages.AnotherTestResponseData;
import digital.inception.messaging.messages.AuthenticateRequestData;
import digital.inception.messaging.messages.AuthenticateResponseData;
import digital.inception.messaging.messages.CheckUserExistsRequestData;
import digital.inception.messaging.messages.CheckUserExistsResponseData;
import digital.inception.messaging.messages.CodeCategoryData;
import digital.inception.messaging.messages.GetCodeCategoryRequestData;
import digital.inception.messaging.messages.GetCodeCategoryResponseData;
import digital.inception.messaging.messages.SubmitErrorReportRequestData;
import digital.inception.messaging.messages.SubmitErrorReportResponseData;
import digital.inception.messaging.messages.TestRequestData;
import digital.inception.messaging.messages.TestResponseData;
import digital.inception.security.AuthenticationFailedException;
import digital.inception.security.ISecurityService;
import digital.inception.security.Organization;
import digital.inception.security.UserNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SystemMessageHandler</code> class implements the message handler that processes the
 * "system" messages for the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "SpringJavaAutowiredMembersInspection"})
public class SystemMessageHandler extends MessageHandler {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SystemMessageHandler.class);

  /* Codes Service */
  @Autowired
  private ICodesService codesService;

  /* Error Service */
  @Autowired
  private IErrorService errorService;

  /* Messaging Service */
  @Autowired
  private IMessagingService messagingService;

  /* Security Service */
  @Autowired
  private ISecurityService securityService;

  /**
   * Constructs a new <code>SystemMessageHandler</code>.
   *
   * @param messageHandlerConfig the configuration information for this message handler
   */
  @SuppressWarnings("unused")
  public SystemMessageHandler(MessageHandlerConfig messageHandlerConfig) {
    super("System Message Handler", messageHandlerConfig);
  }

  /**
   * Process the specified message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   */
  @Override
  public Message processMessage(Message message)
      throws MessageHandlerException {
    // Process a "Authenticate Request" message
    if (message.getTypeId().equals(AuthenticateRequestData.MESSAGE_TYPE_ID)) {
      return processAuthenticateMessage(message);
    }

    // Process a "Check User Exists Request" message
    else if (message.getTypeId().equals(CheckUserExistsRequestData.MESSAGE_TYPE_ID)) {
      return processCheckUserExistsMessage(message);
    }

    // Process a "Test Request" message
    else if (message.getTypeId().equals(TestRequestData.MESSAGE_TYPE_ID)) {
      return processTestMessage(message);
    }

    // Process a "Another Test Request" message
    else if (message.getTypeId().equals(AnotherTestRequestData.MESSAGE_TYPE_ID)) {
      return processAnotherTestMessage(message);
    }

    // Process a "Submit Error Report Request" message
    else if (message.getTypeId().equals(SubmitErrorReportRequestData.MESSAGE_TYPE_ID)) {
      return processSubmitErrorReportRequestMessage(message);
    }

    // Process a "Get Code Category Request" message
    else if (message.getTypeId().equals(GetCodeCategoryRequestData.MESSAGE_TYPE_ID)) {
      return processGetCodeCategoryRequestMessage(message);
    }

    throw new MessageHandlerException(String.format(
        "Failed to process the unrecognised message (%s) with type (%s) from the user (%s) and "
            + "device (%s)", message.getId(), message.getTypeId(), message.getUsername(),
        message.getDeviceId()));
  }

  private Message processAnotherTestMessage(Message requestMessage)
      throws MessageHandlerException {
    try {
      logger.info(requestMessage.toString());

      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      AnotherTestRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new AnotherTestRequestData());

      AnotherTestResponseData responseData = new AnotherTestResponseData(
          requestData.getTestValue(), requestData.getTestData());

      Message responseMessage = messageTranslator.toMessage(responseData,
          requestMessage.getCorrelationId());

      logger.info(responseMessage.toString());

      return responseMessage;
    } catch (Throwable e) {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processAuthenticateMessage(Message requestMessage)
      throws MessageHandlerException {
    try {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      AuthenticateRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new AuthenticateRequestData());

      // Authenticate the user
      AuthenticateResponseData responseData;

      try {
        UUID userDirectoryId = securityService.authenticate(requestData.getUsername(),
            requestData.getPassword());

        List<Organization> organizations = securityService.getOrganizationsForUserDirectory(
            userDirectoryId);

        byte[] userEncryptionKey = messagingService.deriveUserDeviceEncryptionKey(
            requestData.getUsername(), requestData.getDeviceId());

        if (logger.isDebugEnabled()) {
          logger.debug(String.format(
              "Generated the encryption key (%s) for the user (%s) and the device (%s)",
              Base64Util.encodeBytes(userEncryptionKey, false), requestData.getUsername(),
              requestData.getDeviceId()));
        }

        responseData = new AuthenticateResponseData(organizations, userEncryptionKey,
            new HashMap<>());
      } catch (AuthenticationFailedException | UserNotFoundException e) {
        responseData = new AuthenticateResponseData(AuthenticateResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format("Failed to authenticate the user (%s)",
            requestData.getUsername()));
      } catch (Throwable e) {
        logger.error(String.format("Failed to authenticate the user (%s)",
            requestData.getUsername()), e);

        responseData = new AuthenticateResponseData(AuthenticateResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format("Failed to authenticate the user (%s): %s",
            requestData.getUsername(), e.getMessage()));
      }

      return messageTranslator.toMessage(responseData, requestMessage.getCorrelationId());
    } catch (Throwable e) {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processCheckUserExistsMessage(Message requestMessage)
      throws MessageHandlerException {
    try {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      CheckUserExistsRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new CheckUserExistsRequestData());

      CheckUserExistsResponseData responseData;

      try {
        if (logger.isDebugEnabled()) {
          logger.debug(String.format("Checking if the user (%s) exists",
              requestData.getUsername()));
        }

        if (securityService.getUserDirectoryIdForUser(requestData.getUsername()) != null) {
          responseData = new CheckUserExistsResponseData(true);
        } else {
          responseData = new CheckUserExistsResponseData(false);
        }
      } catch (Throwable e) {
        responseData = new CheckUserExistsResponseData(CheckUserExistsResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format("Failed to check if the user (%s) exists: %s",
            requestData.getUsername(), e.getMessage()));

      }

      return messageTranslator.toMessage(responseData);
    } catch (Throwable e) {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processGetCodeCategoryRequestMessage(Message requestMessage)
      throws MessageHandlerException {
    try {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      GetCodeCategoryRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new GetCodeCategoryRequestData());

      GetCodeCategoryResponseData responseData;

      try {
        CodeCategory codeCategory = codesService.getCodeCategory(requestData.getCodeCategoryId());

        String codeData = codesService.getCodeCategoryData(requestData.getCodeCategoryId());

        List<Code> codes;

        if (requestData.getParameters().isEmpty()) {
          codes = codesService.getCodes(requestData.getCodeCategoryId());
        } else {
          codes = codesService.getCodesWithParameters(requestData.getCodeCategoryId(),
              requestData.getParameters());
        }

        CodeCategoryData codeCategoryData = new CodeCategoryData(codeCategory, codeData, codes);

        responseData = new GetCodeCategoryResponseData(codeCategoryData);
      } catch (CodeCategoryNotFoundException e) {
        responseData = new GetCodeCategoryResponseData(GetCodeCategoryResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format(
            "Failed to retrieve the code category (%s): The code category could not be found",
            requestData.getCodeCategoryId()));
      } catch (Throwable e) {
        logger.error(String.format("Failed to retrieve the code category (%s)",
            requestData.getCodeCategoryId()), e);

        responseData = new GetCodeCategoryResponseData(GetCodeCategoryResponseData
            .ERROR_CODE_UNKNOWN_ERROR, String.format(
            "Failed to retrieve the code category (%s): %s", requestData.getCodeCategoryId(),
            ExceptionUtil.getNestedMessages(e)));
      }

      return messageTranslator.toMessage(responseData);
    } catch (Throwable e) {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processSubmitErrorReportRequestMessage(Message requestMessage)
      throws MessageHandlerException {
    try {
      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      SubmitErrorReportRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new SubmitErrorReportRequestData());

      ErrorReport errorReport = new ErrorReport(requestData.getId(),
          requestData.getApplicationId(), requestData.getApplicationVersion(),
          requestData.getDescription(), requestData.getDetail(), requestData.getCreated(),
          requestData.getWho(), requestData.getDeviceId(), requestData.getFeedback(),
          requestData.getData());

      errorService.createErrorReport(errorReport);

      SubmitErrorReportResponseData responseData = new SubmitErrorReportResponseData(0,
          SubmitErrorReportResponseData.ERROR_MESSAGE_SUCCESS, requestData.getId());

      return messageTranslator.toMessage(responseData);
    } catch (Throwable e) {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }

  private Message processTestMessage(Message requestMessage)
      throws MessageHandlerException {
    try {
      logger.info(requestMessage.toString());

      MessageTranslator messageTranslator = new MessageTranslator(requestMessage.getUsername(),
          requestMessage.getDeviceId());

      TestRequestData requestData = messageTranslator.fromMessage(requestMessage,
          new TestRequestData());

      TestResponseData responseData = new TestResponseData(requestData.getTestValue());

      Message responseMessage = messageTranslator.toMessage(responseData);

      logger.info(responseMessage.toString());

      return responseMessage;
    } catch (Throwable e) {
      throw new MessageHandlerException(String.format("Failed to process the message (%s)",
          requestMessage.getTypeId()), e);
    }
  }
}

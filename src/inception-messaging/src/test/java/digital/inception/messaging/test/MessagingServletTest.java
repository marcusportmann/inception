/*
 * Copyright 2018 Marcus Portmann
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

package digital.inception.messaging.test;

//~--- non-JDK imports --------------------------------------------------------

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import digital.inception.core.util.Base64Util;
import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Parser;
import digital.inception.messaging.*;
import digital.inception.messaging.messages.*;
import digital.inception.test.TestClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.servlet.Servlet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessagingServletTest</code> class contains the implementation of the JUnit
 * tests for the <code>MessagingServlet</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { MessagingTestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class MessagingServletTest
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(MessagingServletTest.class);

  /**
   * The HTTP content-type used when receiving and sending WBXML.
   */
  private static final String WBXML_CONTENT_TYPE = "application/wbxml";
  private static final UUID DEVICE_ID = UUID.randomUUID();
  private static final String USERNAME = "Administrator";
  private static final String PASSWORD = "Password1";
  private ServletUnitClient servletUnitClient;

  /**
   * The Spring application context.
   */
  @Autowired
  private ApplicationContext applicationContext;

  /**
   * Test the "Another Test" asynchronous message functionality.
   */
  @Test
  public void anotherTestMessageTest()
    throws Exception
  {
    byte[] userEncryptionKey = authenticateUser(USERNAME, PASSWORD, DEVICE_ID);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID,
        userEncryptionKey);

    AnotherTestRequestData requestData = new AnotherTestRequestData("Test Value",
        "Test Data".getBytes());

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    MessageResult messageResult = sendMessage(requestMessage);

    assertEquals(MessageResult.SUCCESS, messageResult.getCode());
    assertEquals(null, messageResult.getMessage());

    // Sleep to give the back-end a chance to process the message
    try
    {
      Thread.sleep(2500L);
    }
    catch (Throwable ignored) {}

    // Retrieve the messages queued for download
    MessageDownloadResponse messageDownloadResponse = sendMessageDownloadRequest(DEVICE_ID,
        USERNAME);

    assertEquals(MessageDownloadResponse.SUCCESS, messageDownloadResponse.getCode());
    assertEquals(messageDownloadResponse.getNumberOfMessages(),
        messageDownloadResponse.getMessages().size());

    List<Message> messages = messageDownloadResponse.getMessages();

    assertEquals(1, messages.size());

    logger.info("Downloaded " + messages.size() + " messages");

    for (Message message : messages)
    {
      assertEquals(requestMessage.getCorrelationId(), message.getCorrelationId());
      assertEquals(1, message.getDownloadAttempts());

      logger.info("Downloaded message (" + message.getId() + ") with type (" + message.getTypeId()
          + ")");

      MessageReceivedResponse messageReceivedResponse = sendMessageReceivedRequest(DEVICE_ID,
          message.getId());

      assertEquals(MessageReceivedResponse.SUCCESS, messageReceivedResponse.getCode());

      AnotherTestResponseData responseData = messageTranslator.fromMessage(message,
          new AnotherTestResponseData());

      assertEquals(AnotherTestResponseData.MESSAGE_TYPE_ID, responseData.getMessageTypeId());
      assertEquals(MessagePriority.HIGH, responseData.getMessageTypePriority());
      assertEquals("Test Value", responseData.getTestValue());
      assertArrayEquals("Test Data".getBytes(), requestData.getTestData());
    }
  }

  /**
   * Test the "Another Test" asynchronous multi-part message functionality.
   */
  @Test
  public void anotherTestMultiPartMessageTest()
    throws Exception
  {
    byte[] userEncryptionKey = authenticateUser(USERNAME, PASSWORD, DEVICE_ID);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID,
        userEncryptionKey);

    byte[] testData = new byte[64 * 1024];

    new SecureRandom().nextBytes(testData);

    AnotherTestRequestData requestData = new AnotherTestRequestData("Test Value", testData);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    MessageResult messageResult = sendMessage(requestMessage);

    assertEquals(MessageResult.SUCCESS, messageResult.getCode());
    assertEquals(null, messageResult.getMessage());

    // Sleep to give the back-end a chance to process the message
    try
    {
      Thread.sleep(2500L);
    }
    catch (Throwable ignored) {}

    // Retrieve the messages queued for download
    MessageDownloadResponse messageDownloadResponse = sendMessageDownloadRequest(DEVICE_ID,
        USERNAME);

    assertEquals(MessageDownloadResponse.SUCCESS, messageDownloadResponse.getCode());

    List<Message> messages = messageDownloadResponse.getMessages();

    logger.info("Downloaded " + messages.size() + " messages");

    for (Message message : messages)
    {
      logger.info("Downloaded message (" + message.getId() + ") with type (" + message.getTypeId()
          + ")");

      MessageReceivedResponse messageReceivedResponse = sendMessageReceivedRequest(DEVICE_ID,
          message.getId());

      assertEquals(MessageReceivedResponse.SUCCESS, messageReceivedResponse.getCode());

      AnotherTestResponseData responseData = messageTranslator.fromMessage(
          messageResult.getMessage(), new AnotherTestResponseData());

      assertEquals("Test Value", responseData.getTestValue());
      assertArrayEquals(testData, requestData.getTestData());
    }

    // Retrieve the message parts queued for download
    MessagePartDownloadResponse messagePartDownloadResponse = sendMessagePartDownloadRequest(
        DEVICE_ID, USERNAME);

    assertEquals(MessagePartDownloadResponse.SUCCESS, messagePartDownloadResponse.getCode());

    List<MessagePart> messageParts = messagePartDownloadResponse.getMessageParts();

    assertEquals(2, messageParts.size());

    logger.info("Downloaded " + messageParts.size() + " message parts");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    for (MessagePart messagePart : messageParts)
    {
      assertEquals(1, messagePart.getDownloadAttempts());
      assertEquals(requestMessage.getCorrelationId(), messagePart.getMessageCorrelationId());

      logger.info("Downloaded message part (" + messagePart.getPartNo() + "/"
          + messagePart.getTotalParts() + ") with ID (" + messagePart.getId() + ") and type ("
          + messagePart.getMessageTypeId() + ")");

      MessagePartReceivedResponse messagePartReceivedResponse = sendMessagePartReceivedRequest(
          DEVICE_ID, messagePart.getId());

      assertEquals(0, messagePartReceivedResponse.getCode());

      baos.write(messagePart.getData());
    }

    MessageDigest md = MessageDigest.getInstance("SHA-256");

    String messageChecksum = Base64Util.encodeBytes(md.digest(baos.toByteArray()));

    assertEquals(messageParts.get(0).getMessageChecksum(), messageChecksum);

    Message reconstructedMessage = new Message(messageParts.get(0).getMessageUsername(),
        messageParts.get(0).getMessageDeviceId(), messageParts.get(0).getMessageTypeId(),
        messageParts.get(0).getMessageCorrelationId(), messageParts.get(0).getMessagePriority(),
        baos.toByteArray(), messageParts.get(0).getMessageDataHash(), messageParts.get(0)
        .getMessageEncryptionIV());

    AnotherTestResponseData anotherTestResponseData = messageTranslator.fromMessage(
        reconstructedMessage, new AnotherTestResponseData());

    assertArrayEquals(testData, anotherTestResponseData.getTestData());
  }

  /**
   * Test the "Test" synchronous message functionality.
   */
  @Test
  public void testMessageTest()
    throws Exception
  {
    byte[] userEncryptionKey = authenticateUser(USERNAME, PASSWORD, DEVICE_ID);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID,
        userEncryptionKey);

    TestRequestData requestData = new TestRequestData("Test Value");

    Message requestMessage = messageTranslator.toMessage(requestData);

    MessageResult messageResult = sendMessage(requestMessage);

    assertEquals(MessageResult.SUCCESS, messageResult.getCode());

    TestResponseData responseData = messageTranslator.fromMessage(messageResult.getMessage(),
        new TestResponseData());

    assertEquals(TestResponseData.MESSAGE_TYPE_ID, responseData.getMessageTypeId());
    assertEquals(MessagePriority.HIGH, responseData.getMessageTypePriority());
    assertEquals("Test Value", responseData.getTestValue());
  }

  private byte[] authenticateUser(String username, String password, UUID deviceId)
    throws Exception
  {
    AuthenticateRequestData requestData = new AuthenticateRequestData(username, password, deviceId);

    MessageTranslator messageTranslator = new MessageTranslator(username, deviceId);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    MessageResult messageResult = sendMessage(requestMessage);

    assertEquals(MessageResult.SUCCESS, messageResult.getCode());

    AuthenticateResponseData responseData = messageTranslator.fromMessage(
        messageResult.getMessage(), new AuthenticateResponseData());

    assertEquals(AuthenticateResponseData.MESSAGE_TYPE_ID, responseData.getMessageTypeId());
    assertEquals(MessagePriority.HIGH, responseData.getMessageTypePriority());
    assertEquals(0, responseData.getErrorCode());
    assertNotNull(responseData.getUserEncryptionKey());

    return responseData.getUserEncryptionKey();
  }

  private InvocationContext getMessagingServletInvocationContext(byte[] wbxmlRequestData)
    throws Exception
  {
    if (servletUnitClient == null)
    {
      ServletRunner servletRunner = new ServletRunner();
      servletRunner.registerServlet("MessagingServlet", MessagingServlet.class.getName());

      servletUnitClient = servletRunner.newClient();
    }

    PostMethodWebRequest request = new PostMethodWebRequest("http://localhost/MessagingServlet",
        new ByteArrayInputStream(wbxmlRequestData), WBXML_CONTENT_TYPE);

    InvocationContext invocationContext = servletUnitClient.newInvocation(request);

    Servlet messagingServlet = invocationContext.getServlet();

    applicationContext.getAutowireCapableBeanFactory().autowireBean(messagingServlet);

    return invocationContext;
  }

  private byte[] invokeMessagingServlet(byte[] requestData)
    throws Exception
  {
    InvocationContext invocationContext = getMessagingServletInvocationContext(requestData);

    invocationContext.getServlet().service(invocationContext.getRequest(),
        invocationContext.getResponse());

    WebResponse response = invocationContext.getServletResponse();

    assertEquals(200, response.getResponseCode());
    assertEquals(WBXML_CONTENT_TYPE, response.getContentType());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    InputStream in = response.getInputStream();

    int numberOfBytesRead;
    byte[] buffer = new byte[1024];

    while ((numberOfBytesRead = in.read(buffer)) != -1)
    {
      baos.write(buffer, 0, numberOfBytesRead);
    }

    return baos.toByteArray();
  }

  private MessageResult sendMessage(Message message)
    throws Exception
  {
    try
    {
      if (message.getData().length > Message.MAX_ASYNC_MESSAGE_SIZE)
      {
        // Calculate the hash for the message data to use as the message checksum
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        messageDigest.update(message.getData());

        String messageChecksum = Base64Util.encodeBytes(messageDigest.digest());

        // Split the message up into a number of message parts and persist each message part
        int numberOfParts = message.getData().length / MessagePart.MAX_MESSAGE_PART_SIZE;

        if ((message.getData().length % MessagePart.MAX_MESSAGE_PART_SIZE) > 0)
        {
          numberOfParts++;
        }

        for (int i = 0; i < numberOfParts; i++)
        {
          byte[] messagePartData;

          // If this is not the last message part
          if (i < (numberOfParts - 1))
          {
            messagePartData = new byte[MessagePart.MAX_MESSAGE_PART_SIZE];

            System.arraycopy(message.getData(), (i * MessagePart.MAX_MESSAGE_PART_SIZE),
                messagePartData, 0, MessagePart.MAX_MESSAGE_PART_SIZE);
          }

          // If this is the last message part
          else
          {
            int sizeOfPart = message.getData().length - (i * MessagePart.MAX_MESSAGE_PART_SIZE);

            messagePartData = new byte[sizeOfPart];

            System.arraycopy(message.getData(), (i * MessagePart.MAX_MESSAGE_PART_SIZE),
                messagePartData, 0, sizeOfPart);
          }

          MessagePart messagePart = new MessagePart(i + 1, numberOfParts, message.getId(),
              message.getUsername(), message.getDeviceId(), message.getTypeId(),
              message.getCorrelationId(), message.getPriority(), message.getCreated(),
              message.getDataHash(), message.getEncryptionIV(), messageChecksum, messagePartData);

          messagePart.setStatus(MessagePartStatus.QUEUED_FOR_SENDING);

          // Send the message part
          byte[] data = invokeMessagingServlet(messagePart.toWBXML());

          Parser parser = new Parser();

          Document document = parser.parse(data);

          // Check if we have received a valid message result
          if (MessagePartResult.isValidWBXML(document))
          {
            logger.info("Uploaded the message part (" + messagePart.getPartNo() + "/"
                + messagePart.getTotalParts() + ") for the message (" + messagePart.getMessageId()
                + ") from the user (" + messagePart.getMessageUsername() + ") and the device ("
                + messagePart.getMessageDeviceId() + ") with message type ("
                + messagePart.getMessageTypeId() + ")");
          }
          else
          {
            throw new RuntimeException("The WBXML response data from the remote server is not a "
                + "valid MessageResult document");
          }

          MessagePartResult messagePartResult = new MessagePartResult(document);

          assertEquals(MessagePartResult.SUCCESS, messagePartResult.getCode());
        }

        return new MessageResult(MessageResult.SUCCESS, "");
      }
      else
      {
        byte[] data = invokeMessagingServlet(message.toWBXML());

        Parser parser = new Parser();

        Document document = parser.parse(data);

        // Check if we have received a valid message result
        if (MessageResult.isValidWBXML(document))
        {
          return new MessageResult(document);
        }
        else
        {
          throw new RuntimeException("The WBXML response data from the remote server is not a "
              + "valid MessageResult document");
        }
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to send the message (" + message.getId() + "): "
          + e.getMessage(), e);
    }
  }

  private MessageDownloadResponse sendMessageDownloadRequest(UUID deviceId, String username)
    throws Exception
  {
    MessageDownloadRequest messageDownloadRequest = new MessageDownloadRequest(deviceId, username);

    byte[] data = invokeMessagingServlet(messageDownloadRequest.toWBXML());

    Parser parser = new Parser();

    Document document = parser.parse(data);

    if (MessageDownloadResponse.isValidWBXML(document))
    {
      return new MessageDownloadResponse(document);
    }
    else
    {
      throw new RuntimeException("Failed to send the message download request: "
          + "The WBXML response data from the remote server is not a valid "
          + "MessageDownloadResponse document");
    }
  }

  private MessagePartDownloadResponse sendMessagePartDownloadRequest(UUID deviceId, String username)
    throws Exception
  {
    MessagePartDownloadRequest messagePartDownloadRequest = new MessagePartDownloadRequest(
        deviceId, username);

    byte[] data = invokeMessagingServlet(messagePartDownloadRequest.toWBXML());

    Parser parser = new Parser();

    Document document = parser.parse(data);

    if (MessagePartDownloadResponse.isValidWBXML(document))
    {
      return new MessagePartDownloadResponse(document);
    }
    else
    {
      throw new RuntimeException("Failed to send the message part download request: "
          + "The WBXML response data from the remote server is not a valid "
          + "MessagePartDownloadResponse document");
    }
  }

  private MessagePartReceivedResponse sendMessagePartReceivedRequest(UUID deviceId, UUID messageId)
    throws Exception
  {
    MessagePartReceivedRequest messagePartReceivedRequest = new MessagePartReceivedRequest(
        deviceId, messageId);

    byte[] data = invokeMessagingServlet(messagePartReceivedRequest.toWBXML());

    Parser parser = new Parser();

    Document document = parser.parse(data);

    if (MessagePartReceivedResponse.isValidWBXML(document))
    {
      return new MessagePartReceivedResponse(document);
    }
    else
    {
      throw new RuntimeException("Failed to send the message part received request: "
          + "The WBXML response data from the remote server is not a valid "
          + "MessagePartReceivedResponse document");
    }
  }

  private MessageReceivedResponse sendMessageReceivedRequest(UUID deviceId, UUID messageId)
    throws Exception
  {
    MessageReceivedRequest messageReceivedRequest = new MessageReceivedRequest(deviceId, messageId);

    byte[] data = invokeMessagingServlet(messageReceivedRequest.toWBXML());

    Parser parser = new Parser();

    Document document = parser.parse(data);

    if (MessageReceivedResponse.isValidWBXML(document))
    {
      return new MessageReceivedResponse(document);
    }
    else
    {
      throw new RuntimeException("Failed to send the message received request: "
          + "The WBXML response data from the remote server is not a valid "
          + "MessageReceivedResponse document");
    }
  }
}

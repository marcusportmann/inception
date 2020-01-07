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

package digital.inception.messaging.test;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.codes.Code;
import digital.inception.codes.CodeCategory;
import digital.inception.codes.ICodesService;
import digital.inception.core.util.Base64Util;
import digital.inception.messaging.IMessagingService;
import digital.inception.messaging.Message;
import digital.inception.messaging.MessageTranslator;
import digital.inception.messaging.messages.*;
import digital.inception.security.SecurityService;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.*;

/**
 * The <code>SystemMessageTest</code> class contains the implementation of the JUnit
 * tests for the "system" messages supported by the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class SystemMessageTest
{
  private static final UUID DEVICE_ID = UUID.randomUUID();
  private static final String PASSWORD = "Password1";
  private static final String USERNAME = "Administrator";

  /**
   * The Codes Service.
   */
  @Autowired
  private ICodesService codesService;

  /**
   * The Messaging Service.
   */
  @Autowired
  private IMessagingService messagingService;

  /**
   * Test the "Another Test" request and response message functionality.
   */
  @Test
  public void anotherTestMessageTest()
    throws Exception
  {
    String testValue = "This is the test value";
    byte[] testData = "This is test data".getBytes();

    AnotherTestRequestData requestData = new AnotherTestRequestData(testValue, testData);

    assertEquals(testValue, requestData.getTestValue());
    assertArrayEquals(testData, requestData.getTestData());

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    AnotherTestResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new AnotherTestResponseData());

    assertEquals(testValue, responseData.getTestValue());
    assertArrayEquals(testData, responseData.getTestData());
  }

  /**
   * Test the "Authentication" message.
   */
  @Test
  public void authenticationTest()
    throws Exception
  {
    AuthenticateRequestData requestData = new AuthenticateRequestData(USERNAME, PASSWORD,
        DEVICE_ID);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    AuthenticateResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new AuthenticateResponseData());

    assertEquals(0, responseData.getErrorCode());
    assertNotNull(responseData.getErrorMessage());

    List<OrganizationData> organizations = responseData.getOrganizations();

    assertEquals(1, organizations.size());

    OrganizationData organization = organizations.get(0);

    assertEquals(SecurityService.ADMINISTRATION_ORGANIZATION_ID, organization.getId());
    assertEquals("Administration", organization.getName());
    assertNotNull(responseData.getUserEncryptionKey());
    assertNotNull(responseData.getUserProperties());

    Map<String, Object> userProperties = responseData.getUserProperties();

    assertEquals(0, userProperties.size());

    assertEquals(requestMessage.getCorrelationId(), responseMessage.getCorrelationId());
  }

  /**
   * Test the "Check User Exists" message.
   */
  @Test
  public void checkUserExistsTest()
    throws Exception
  {
    CheckUserExistsRequestData requestData = new CheckUserExistsRequestData(USERNAME);

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    CheckUserExistsResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new CheckUserExistsResponseData());

    assertEquals(0, responseData.getErrorCode());
    assertNotNull(responseData.getErrorMessage());
    assertTrue(responseData.getUserExists());
  }

  /**
   * Test the "Get Code Category" message.
   */
  @Test
  public void getGetCodeCategoryTest()
    throws Exception
  {
    CodeCategory testStandardCodeCategory = new CodeCategory("TestStandardCodeCategory1",
        "Test Standard Code Category 1", "", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

    if (testStandardCodeCategory != null)
    {
      codesService.createCodeCategory(testStandardCodeCategory);

      List<Code> testCodes = new ArrayList<>();

      for (int i = 1; i <= 10; i++)
      {
        Code testStandardCode = new Code("Test Standard Code ID " + i,
            testStandardCodeCategory.getId(), "Test Standard Code Name " + i,
            "Test Standard Code Value " + i);

        codesService.createCode(testStandardCode);

        testCodes.add(testStandardCode);
      }

      GetCodeCategoryRequestData requestData = new GetCodeCategoryRequestData(
          testStandardCodeCategory.getId(), LocalDateTime.now(), true);

      MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

      Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

      Message responseMessage = messagingService.processMessage(requestMessage);

      GetCodeCategoryResponseData responseData = messageTranslator.fromMessage(responseMessage,
          new GetCodeCategoryResponseData());

      assertEquals(0, responseData.getErrorCode());
      assertNotNull(responseData.getErrorMessage());

      CodeCategoryData codeCategory = responseData.getCodeCategory();

      assertEquals(testStandardCodeCategory.getId(), codeCategory.getId());
      assertEquals(testStandardCodeCategory.getName(), codeCategory.getName());
      assertEquals(testStandardCodeCategory.getUpdated(), codeCategory.getLastUpdated());
      assertEquals(testCodes.size(), codeCategory.getCodes().size());

      boolean foundMatchingCode = false;

      for (Code testCode : testCodes)
      {
        for (CodeData code : codeCategory.getCodes())
        {
          if (testCode.getId().equals(code.getId()))
          {
            assertEquals(testCode.getCodeCategoryId(), code.getCodeCategoryId());
            assertEquals(testCode.getName(), code.getName());
            assertEquals(testCode.getValue(), code.getValue());

            foundMatchingCode = true;

            break;
          }
        }

        if (!foundMatchingCode)
        {
          fail(String.format("Failed to find the matching code (%s)", testCode));
        }

        foundMatchingCode = false;
      }
    }

    CodeCategory testCustomCodeCategory = new CodeCategory("TestCustomCodeCategory2",
        "Test Custom Code Category 2", "", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

    if (testCustomCodeCategory != null)
    {
      codesService.createCodeCategory(testCustomCodeCategory);

      GetCodeCategoryRequestData requestData = new GetCodeCategoryRequestData(
          testCustomCodeCategory.getId(), LocalDateTime.now(), true);

      MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

      Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

      Message responseMessage = messagingService.processMessage(requestMessage);

      GetCodeCategoryResponseData responseData = messageTranslator.fromMessage(responseMessage,
          new GetCodeCategoryResponseData());

      assertEquals(0, responseData.getErrorCode());
      assertNotNull(responseData.getErrorMessage());

      CodeCategoryData codeCategory = responseData.getCodeCategory();

      assertEquals(testCustomCodeCategory.getId(), codeCategory.getId());
      assertEquals(testCustomCodeCategory.getName(), codeCategory.getName());
      assertEquals(testCustomCodeCategory.getUpdated(), codeCategory.getLastUpdated());
    }
  }

  /**
   * Test the "Get Code Category With Parameters" message.
   */
  @Test
  public void getGetCodeCategoryWithParametersTest()
    throws Exception
  {
    Map<String, String> parameters = new HashMap<>();

    parameters.put("Parameter Name 1", "Parameter Value 1");
    parameters.put("Parameter Name 2", "Parameter Value 2");

    CodeCategory testStandardCodeCategory = new CodeCategory("TestStandardCodeCategory2",
        "Test Standard Code Category 2", "", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

    if (testStandardCodeCategory != null)
    {
      codesService.createCodeCategory(testStandardCodeCategory);

      List<Code> testCodes = new ArrayList<>();

      for (int i = 1; i <= 10; i++)
      {
        Code testStandardCode = new Code("Test Standard Code ID " + i,
            testStandardCodeCategory.getId(), "Test Standard Code Name " + i,
            "Test Standard Code Value " + i);

        codesService.createCode(testStandardCode);

        testCodes.add(testStandardCode);
      }

      GetCodeCategoryRequestData requestData = new GetCodeCategoryRequestData(
          testStandardCodeCategory.getId(), LocalDateTime.now(), parameters, true);

      MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

      Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

      Message responseMessage = messagingService.processMessage(requestMessage);

      GetCodeCategoryResponseData responseData = messageTranslator.fromMessage(responseMessage,
          new GetCodeCategoryResponseData());

      assertEquals(0, responseData.getErrorCode());
      assertNotNull(responseData.getErrorMessage());

      CodeCategoryData codeCategory = responseData.getCodeCategory();

      assertEquals(testStandardCodeCategory.getId(), codeCategory.getId());
      assertEquals(testStandardCodeCategory.getName(), codeCategory.getName());
      assertEquals(testStandardCodeCategory.getUpdated(), codeCategory.getLastUpdated());
      assertEquals(testCodes.size(), codeCategory.getCodes().size());

      boolean foundMatchingCode = false;

      for (Code testCode : testCodes)
      {
        for (CodeData code : codeCategory.getCodes())
        {
          if (testCode.getId().equals(code.getId()))
          {
            assertEquals(testCode.getCodeCategoryId(), code.getCodeCategoryId());
            assertEquals(testCode.getName(), code.getName());
            assertEquals(testCode.getValue(), code.getValue());

            foundMatchingCode = true;

            break;
          }
        }

        if (!foundMatchingCode)
        {
          fail(String.format("Failed to find the matching code (%s)", testCode));
        }

        foundMatchingCode = false;
      }
    }

    CodeCategory testCustomCodeCategory = new CodeCategory("TestCustomCodeCategory1",
        "Test Custom Code Category 1", "", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

    if (testCustomCodeCategory != null)
    {
      codesService.createCodeCategory(testCustomCodeCategory);

      GetCodeCategoryRequestData requestData = new GetCodeCategoryRequestData(
          testCustomCodeCategory.getId(), LocalDateTime.now(), parameters, true);

      MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

      Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

      Message responseMessage = messagingService.processMessage(requestMessage);

      GetCodeCategoryResponseData responseData = messageTranslator.fromMessage(responseMessage,
          new GetCodeCategoryResponseData());

      assertEquals(0, responseData.getErrorCode());
      assertNotNull(responseData.getErrorMessage());

      CodeCategoryData codeCategory = responseData.getCodeCategory();

      assertEquals(testCustomCodeCategory.getId(), codeCategory.getId());
      assertEquals(testCustomCodeCategory.getName(), codeCategory.getName());
      assertEquals(testCustomCodeCategory.getUpdated(), codeCategory.getLastUpdated());
    }
  }

  /**
   * Test the "Submit Error Report" message.
   */
  @Test
  public void submitErrorReportTest()
    throws Exception
  {
    SubmitErrorReportRequestData requestData = new SubmitErrorReportRequestData(UUID.randomUUID(),
        "ApplicationId", "1.0.0", "Test Description", "Test Detail", "Test Feedback",
        LocalDateTime.now(), "Administrator", UUID.randomUUID(), Base64Util.encodeBytes(
        "Test Data".getBytes()));

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    SubmitErrorReportResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new SubmitErrorReportResponseData());

    assertEquals(0, responseData.getErrorCode());
    assertNotNull(responseData.getErrorMessage());
    assertEquals(requestData.getId(), responseData.getErrorReportId());
  }

  /**
   * Test the "Test" request and response message functionality.
   */
  @Test
  public void testMessageTest()
    throws Exception
  {
    String testValue = "This is the test value";

    TestRequestData requestData = new TestRequestData(testValue);

    assertEquals(testValue, requestData.getTestValue());

    MessageTranslator messageTranslator = new MessageTranslator(USERNAME, DEVICE_ID);

    Message requestMessage = messageTranslator.toMessage(requestData, UUID.randomUUID());

    Message responseMessage = messagingService.processMessage(requestMessage);

    TestResponseData responseData = messageTranslator.fromMessage(responseMessage,
        new TestResponseData());

    assertEquals(testValue, responseData.getTestValue());
  }
}

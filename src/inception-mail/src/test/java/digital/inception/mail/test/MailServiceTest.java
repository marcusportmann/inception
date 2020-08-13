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

package digital.inception.mail.test;

// ~--- non-JDK imports --------------------------------------------------------

import static digital.inception.test.Assert.assertEqualsToMillisecond;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import digital.inception.core.util.ResourceUtil;
import digital.inception.mail.IMailService;
import digital.inception.mail.MailTemplate;
import digital.inception.mail.MailTemplateContentType;
import digital.inception.mail.MailTemplateNotFoundException;
import digital.inception.mail.MailTemplateSummary;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MailServiceTest</code> class contains the implementation of the JUnit tests for the
 * <code>MailService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestExecutionListeners(
    listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
    })
public class MailServiceTest {

  private static int mailTemplateCount;

  /**
   * The Mail Service.
   */
  @Autowired
  private IMailService mailService;

  private static synchronized MailTemplate getTestMailTemplateDetails() {
    mailTemplateCount++;

    byte[] testMailTemplate =
        ResourceUtil.getClasspathResource("digital/inception/mail/test/TestMailTemplate");

    MailTemplate mailTemplate = new MailTemplate();
    mailTemplate.setId("TestMailTemplate" + mailTemplateCount);
    mailTemplate.setName("Test Mail Template " + mailTemplateCount);
    mailTemplate.setContentType(MailTemplateContentType.HTML);
    mailTemplate.setTemplate(testMailTemplate);
    mailTemplate.setUpdated(LocalDateTime.now());

    return mailTemplate;
  }

  /**
   * Test the mail template functionality.
   */
  @Test
  public void mailTemplateTest() throws Exception {
    MailTemplate mailTemplate = getTestMailTemplateDetails();

    mailService.createMailTemplate(mailTemplate);

    MailTemplate retrievedMailTemplate = mailService.getMailTemplate(mailTemplate.getId());

    compareMailTemplates(mailTemplate, retrievedMailTemplate);

    assertEqualsToMillisecond(
        "The correct updated time was not retrieved for the mail template",
        mailTemplate.getUpdated(),
        mailService.getMailTemplateUpdated(mailTemplate.getId()));

    boolean mailTemplateExists = mailService.mailTemplateExists(mailTemplate.getId());

    assertTrue("The mail template does not exist", mailTemplateExists);

    String retrievedMailTemplateName = mailService.getMailTemplateName(mailTemplate.getId());

    assertEquals(
        "The correct mail template name was not retrieved",
        mailTemplate.getName(),
        retrievedMailTemplateName);

    mailTemplate.setName("Updated " + mailTemplate.getName());

    mailService.updateMailTemplate(mailTemplate);
    assertEqualsToMillisecond(
        "The correct updated time was not retrieved for the mail template",
        mailTemplate.getUpdated(),
        mailService.getMailTemplateUpdated(mailTemplate.getId()));

    retrievedMailTemplate = mailService.getMailTemplate(mailTemplate.getId());

    compareMailTemplates(mailTemplate, retrievedMailTemplate);

    assertEqualsToMillisecond(
        "The correct updated time was not retrieved for the mail template",
        mailTemplate.getUpdated(),
        mailService.getMailTemplateUpdated(mailTemplate.getId()));

    mailTemplateExists = mailService.mailTemplateExists(mailTemplate.getId());

    assertTrue("The updated mail template does not exist", mailTemplateExists);

    List<MailTemplate> mailTemplates = mailService.getMailTemplates();

    assertEquals("The correct number of mail templates was not retrieved", 1, mailTemplates.size());

    compareMailTemplates(mailTemplate, mailTemplates.get(0));

    MailTemplateSummary retrievedMailTemplateSummary =
        mailService.getMailTemplateSummary(mailTemplate.getId());

    compareMailTemplateToMailTemplateSummary(mailTemplate, retrievedMailTemplateSummary);

    List<MailTemplateSummary> mailTemplateSummaries = mailService.getMailTemplateSummaries();

    assertEquals(
        "The correct number of mail template summaries was not retrieved",
        1,
        mailTemplateSummaries.size());

    compareMailTemplateToMailTemplateSummary(mailTemplate, mailTemplateSummaries.get(0));

    Map<String, String> mapTemplateParameters = new HashMap<>();
    mapTemplateParameters.put("name", "Joe Bloggs");

    System.out.println(
        mailService.processMailTemplate(mailTemplate.getId(), mapTemplateParameters));

    mailService.deleteMailTemplate(mailTemplate.getId());

    try {
      mailService.getMailTemplate(mailTemplate.getId());

      fail("The mail template that should have been deleted was retrieved successfully");
    } catch (MailTemplateNotFoundException ignored) {
    }
  }

  /**
   * Test the send mail functionality.
   */
  // @Test
  public void sendMailTest() throws Exception {
    MailTemplate mailTemplate = getTestMailTemplateDetails();

    mailService.createMailTemplate(mailTemplate);

    Map<String, String> mapTemplateParameters = new HashMap<>();
    mapTemplateParameters.put("name", "Joe Bloggs");

    mailService.sendMail(
        Collections.singletonList("test@test.com"),
        "Test Subject",
        "no-reply@inception.digital",
        "Inception",
        mailTemplate.getId(),
        mapTemplateParameters);
  }

  private void compareMailTemplateToMailTemplateSummary(
      MailTemplate mailTemplate, MailTemplateSummary mailTemplateSummary) {
    assertEquals(
        "The ID values for the two mail template summaries do not match",
        mailTemplate.getId(),
        mailTemplateSummary.getId());
    assertEquals(
        "The name values for the two mail template summaries do not match",
        mailTemplate.getName(),
        mailTemplateSummary.getName());
  }

  private void compareMailTemplates(MailTemplate mailTemplate1, MailTemplate mailTemplate2) {
    assertEquals(
        "The ID values for the two mail templates do not match",
        mailTemplate1.getId(),
        mailTemplate2.getId());
    assertEquals(
        "The name values for the two mail templates do not match",
        mailTemplate1.getName(),
        mailTemplate2.getName());
    assertEquals(
        "The content type values for the two mail templates do not match",
        mailTemplate1.getContentType(),
        mailTemplate2.getContentType());
    assertArrayEquals(
        "The template values for the two mail templates do not match",
        mailTemplate1.getTemplate(),
        mailTemplate2.getTemplate());
    assertEqualsToMillisecond(
        "The updated values for the two mail templates do not match",
        mailTemplate1.getUpdated(),
        mailTemplate2.getUpdated());
  }
}

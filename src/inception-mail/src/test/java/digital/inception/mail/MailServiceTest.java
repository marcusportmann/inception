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

package digital.inception.mail;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ResourceUtil;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;

/**
 * The <code>MailServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>MailService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class MailServiceTest
{
  private static int mailTemplateCount;

  /**
   * The Mail Service.
   */
  @Autowired
  private IMailService mailService;

  /**
   * Test the message template functionality.
   */
  @Test
  public void messageTemplateTest()
    throws Exception
  {
    MailTemplate mailTemplate = getTestMailTemplateDetails();

    mailService.createMailTemplate(mailTemplate);

    MailTemplate retrievedMailTemplate = mailService.getMailTemplate(mailTemplate.getId());

    compareMailTemplates(mailTemplate, retrievedMailTemplate);

    boolean mailTemplateExists = mailService.mailTemplateExists(mailTemplate.getId());

    assertTrue("The mail template that was saved does not exist", mailTemplateExists);

    mailTemplate.setName("Updated " + mailTemplate.getName());

    mailService.updateMailTemplate(mailTemplate);

    retrievedMailTemplate = mailService.getMailTemplate(mailTemplate.getId());

    compareMailTemplates(mailTemplate, retrievedMailTemplate);

    mailTemplateExists = mailService.mailTemplateExists(mailTemplate.getId());

    assertTrue("The updated mail template that was saved does not exist", mailTemplateExists);

    long numberOfMailTemplates = mailService.getNumberOfMailTemplates();

    assertEquals("The correct number of mail templates was not retrieved", 1,
        numberOfMailTemplates);

    List<MailTemplate> mailTemplates = mailService.getMailTemplates();

    assertEquals("The correct number of mail templates was not retrieved", 1, mailTemplates.size());

    compareMailTemplates(mailTemplate, mailTemplates.get(0));

    MailTemplateSummary retrievedMailTemplateSummary = mailService.getMailTemplateSummary(
        mailTemplate.getId());

    compareMailTemplateToMailTemplateSummary(mailTemplate, retrievedMailTemplateSummary);

    List<MailTemplateSummary> mailTemplateSummaries = mailService.getMailTemplateSummaries();

    assertEquals("The correct number of mail template summaries was not retrieved", 1,
        mailTemplateSummaries.size());

    compareMailTemplateToMailTemplateSummary(mailTemplate, mailTemplateSummaries.get(0));

    mailService.deleteMailTemplate(mailTemplate.getId());

    try
    {
      mailService.getMailTemplate(mailTemplate.getId());

      fail("The mail template that should have been deleted was retrieved successfully");
    }
    catch (MailTemplateNotFoundException ignored) {}
  }

  /**
   * Test the send mail functionality.
   */
  //@Test
  public void sendMailTest()
    throws Exception
  {
    MailTemplate mailTemplate = getTestMailTemplateDetails();

    mailService.createMailTemplate(mailTemplate);

    Map<String, String> mapTemplateParameters = new HashMap<>();

    mailService.sendMail(Collections.singletonList("test@test.com"), "Test Subject",
        "no-reply@inception.digital", "Inception", mailTemplate.getId(), mapTemplateParameters);
  }

  private static synchronized MailTemplate getTestMailTemplateDetails()
  {
    mailTemplateCount++;

    byte[] testMailTemplate = ResourceUtil.getClasspathResource(
        "digital/inception/mail/TestMailTemplate");

    MailTemplate mailTemplate = new MailTemplate();
    mailTemplate.setId(UUID.randomUUID());
    mailTemplate.setName("Test Mail Template " + mailTemplateCount);
    mailTemplate.setContentType(MailTemplateContentType.HTML);
    mailTemplate.setTemplate(testMailTemplate);

    return mailTemplate;
  }

  private void compareMailTemplateToMailTemplateSummary(MailTemplate mailTemplate,
      MailTemplateSummary mailTemplateSummary)
  {
    assertEquals("The ID values for the two mail template summaries do not match",
        mailTemplate.getId(), mailTemplateSummary.getId());
    assertEquals("The name values for the two mail template summaries do not match",
        mailTemplate.getName(), mailTemplateSummary.getName());
  }

  private void compareMailTemplates(MailTemplate mailTemplate1, MailTemplate mailTemplate2)
  {
    assertEquals("The ID values for the two mail templates do not match", mailTemplate1.getId(),
        mailTemplate2.getId());
    assertEquals("The name values for the two mail templates do not match",
        mailTemplate1.getName(), mailTemplate2.getName());
    assertArrayEquals("The template values for the two mail templates do not match",
        mailTemplate1.getTemplate(), mailTemplate2.getTemplate());
  }
}

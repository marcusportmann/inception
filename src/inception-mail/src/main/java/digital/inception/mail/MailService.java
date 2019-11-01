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

import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//~--- JDK imports ------------------------------------------------------------

import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * The <code>MailService</code> class provides the Mail Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({ "unused" })
public class MailService
  implements IMailService
{
  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The Mail Template Repository.
   */
  private MailTemplateRepository mailTemplateRepository;

  /**
   * The Mail Template Summary Repository.
   */
  private MailTemplateSummaryRepository mailTemplateSummaryRepository;

  /**
   * Constructs a new <code>MailService</code>.
   *
   * @param applicationContext            the Spring application context
   * @param mailTemplateRepository        the Mail Template Repository
   * @param mailTemplateSummaryRepository the Mail Template Summary Repository
   */
  public MailService(ApplicationContext applicationContext, MailTemplateRepository mailTemplateRepository,
      MailTemplateSummaryRepository mailTemplateSummaryRepository)
  {
    this.applicationContext = applicationContext;
    this.mailTemplateRepository = mailTemplateRepository;
    this.mailTemplateSummaryRepository = mailTemplateSummaryRepository;
  }

  /**
   * Create the new mail template.
   *
   * @param mailTemplate the <code>MailTemplate</code> instance containing the information
   *                     for the new mail template
   */
  @Override
  @Transactional
  public void createMailTemplate(MailTemplate mailTemplate)
    throws DuplicateMailTemplateException, MailServiceException
  {
    try
    {
      if (mailTemplateRepository.existsById(mailTemplate.getId()))
      {
        throw new DuplicateMailTemplateException(mailTemplate.getId());
      }

      mailTemplateRepository.saveAndFlush(mailTemplate);
    }
    catch (DuplicateMailTemplateException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to create the mail template (" + mailTemplate.getId()
          + ")", e);
    }
  }

  /**
   * Delete the existing mail template.
   *
   * @param mailTemplateId the ID used to uniquely identify the mail template
   */
  @Override
  @Transactional
  public void deleteMailTemplate(UUID mailTemplateId)
    throws MailTemplateNotFoundException, MailServiceException
  {
    try
    {
      if (!mailTemplateRepository.existsById(mailTemplateId))
      {
        throw new MailTemplateNotFoundException(mailTemplateId);
      }

      mailTemplateRepository.deleteById(mailTemplateId);
    }
    catch (MailTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to delete the mail template (" + mailTemplateId + ")",
          e);
    }
  }

  /**
   * Retrieve the mail template.
   *
   * @param mailTemplateId the ID used to uniquely identify the mail template
   *
   * @return the mail template
   */
  @Override
  public MailTemplate getMailTemplate(UUID mailTemplateId)
    throws MailTemplateNotFoundException, MailServiceException
  {
    try
    {
      Optional<MailTemplate> mailTemplateOptional = mailTemplateRepository.findById(mailTemplateId);

      if (mailTemplateOptional.isPresent())
      {
        return mailTemplateOptional.get();
      }
      else
      {
        throw new MailTemplateNotFoundException(mailTemplateId);
      }
    }
    catch (MailTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to retrieve the mail template (" + mailTemplateId
          + ")", e);
    }
  }

  /**
   * Returns the summaries for all the mail templates.
   *
   * @return the summaries for all the mail templates
   */
  @Override
  public List<MailTemplateSummary> getMailTemplateSummaries()
    throws MailServiceException
  {
    try
    {
      return mailTemplateSummaryRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to retrieve the summaries for the mail templates", e);
    }

  }

  /**
   * Retrieve the summary for the mail template.
   *
   * @param mailTemplateId the ID used to uniquely identify the mail template
   *
   * @return the summary for the mail template
   */
  @Override
  public MailTemplateSummary getMailTemplateSummary(UUID mailTemplateId)
    throws MailTemplateNotFoundException, MailServiceException
  {
    try
    {
      Optional<MailTemplateSummary> mailTemplateSummaryOptional =
          mailTemplateSummaryRepository.findById(mailTemplateId);

      if (mailTemplateSummaryOptional.isPresent())
      {
        return mailTemplateSummaryOptional.get();
      }
      else
      {
        throw new MailTemplateNotFoundException(mailTemplateId);
      }
    }
    catch (MailTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to retrieve the summary for the mail template ("
          + mailTemplateId + ")", e);
    }
  }

  /**
   * Returns all the mail templates.
   *
   * @return all the mail templates
   */
  @Override
  public List<MailTemplate> getMailTemplates()
    throws MailServiceException
  {
    try
    {
      return mailTemplateRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to retrieve the mail templates", e);
    }

  }

  /**
   * Returns the number of mail templates.
   *
   * @return the number of mail templates
   */
  @Override
  public long getNumberOfMailTemplates()
    throws MailServiceException
  {
    try
    {
      return mailTemplateRepository.count();
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to retrieve the number of mail templates", e);
    }

  }

  /**
   * Check whether the mail template exists.
   *
   * @param mailTemplateId the ID used to uniquely identify the mail template
   *
   * @return <code>true</code> if the mail template exists or <code>false</code> otherwise
   */
  @Override
  public boolean mailTemplateExists(UUID mailTemplateId)
    throws MailServiceException
  {
    try
    {
      return mailTemplateRepository.existsById(mailTemplateId);
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to check whether the mail template (" + mailTemplateId
          + ")", e);
    }
  }

  /**
   * Send a mail.
   *
   * @param to                     the list of e-mail addresses to send the mail to
   * @param subject                the subject for the mail
   * @param from                   the from e-mail address
   * @param fromName               the from e-mail name
   * @param mailTemplateId         the ID used to uniquely identify the mail template
   * @param mailTemplateParameters the parameters to apply to the mail template
   */
  public void sendMail(List<String> to, String subject, String from, String fromName,
      UUID mailTemplateId, Map<String, String> mailTemplateParameters)
    throws MailTemplateNotFoundException, MailServiceException
  {
    try
    {
      // Retrieve the mail template
      Optional<MailTemplate> mailTemplateOptional = mailTemplateRepository.findById(mailTemplateId);

      if (mailTemplateOptional.isEmpty())
      {
        throw new MailTemplateNotFoundException(mailTemplateId);
      }

      MailTemplate mailTemplate = mailTemplateOptional.get();

      JavaMailSender javaMailSender = applicationContext.getBean(JavaMailSender.class);

      if (javaMailSender != null)
      {
        // Send the e-mail message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(new InternetAddress(from, fromName));

        helper.setTo(to.toArray(new String[to.size()]));

        helper.setSubject(subject);

        helper.setText(new String(mailTemplate.getTemplate(), StandardCharsets.UTF_8),
          mailTemplate.getContentType() == MailTemplateContentType.HTML);

        javaMailSender.send(helper.getMimeMessage());
      }
      else
      {
        throw new MailServiceException("No JavaMailSender bean has been configured");
      }
    }
    catch (MailTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to send the mail", e);
    }
  }

  /**
   * Update the mail template.
   *
   * @param mailTemplate the <code>MailTemplate</code> instance containing the updated
   *                         information for the mail template
   */
  @Override
  @Transactional
  public void updateMailTemplate(MailTemplate mailTemplate)
    throws MailTemplateNotFoundException, MailServiceException
  {
    try
    {
      if (!mailTemplateRepository.existsById(mailTemplate.getId()))
      {
        throw new MailTemplateNotFoundException(mailTemplate.getId());
      }

      mailTemplateRepository.saveAndFlush(mailTemplate);
    }
    catch (MailTemplateNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MailServiceException("Failed to update the mail template (" + mailTemplate.getId()
          + ")", e);
    }
  }
}

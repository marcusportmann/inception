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

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The <code>IMailService</code> interface defines the functionality provided by a Mail Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IMailService
{
  /**
   * Create the new mail template.
   *
   * @param mailTemplate the <code>MailTemplate</code> instance containing the information
   *                     for the new mail template
   */
  void createMailTemplate(MailTemplate mailTemplate)
    throws DuplicateMailTemplateException, MailServiceException;

  /**
   * Delete the existing mail template.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   */
  void deleteMailTemplate(UUID mailTemplateId)
    throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Retrieve the mail template.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   *
   * @return the mail template
   */
  MailTemplate getMailTemplate(UUID mailTemplateId)
    throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Retrieve the name of the mail template.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   *
   * @return the name of the mail template
   */
  String getMailTemplateName(UUID mailTemplateId)
    throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Returns the summaries for all the mail templates.
   *
   * @return the summaries for all the mail templates
   */
  List<MailTemplateSummary> getMailTemplateSummaries()
    throws MailServiceException;

  /**
   * Retrieve the summary for the mail template.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   *
   * @return the summary for the mail template
   */
  MailTemplateSummary getMailTemplateSummary(UUID mailTemplateId)
    throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Returns all the mail templates.
   *
   * @return all the mail templates
   */
  List<MailTemplate> getMailTemplates()
    throws MailServiceException;

  /**
   * Returns the number of mail templates.
   *
   * @return the number of mail templates
   */
  long getNumberOfMailTemplates()
    throws MailServiceException;

  /**
   * Check whether the mail template exists.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   *
   * @return <code>true</code> if the mail template exists or <code>false</code> otherwise
   */
  boolean mailTemplateExists(UUID mailTemplateId)
    throws MailServiceException;

  /**
   * Send a mail.
   *
   * @param to                     the list of e-mail addresses to send the mail to
   * @param subject                the subject for the mail
   * @param from                   the from e-mail address
   * @param fromName               the from e-mail name
   * @param mailTemplateId         the Universally Unique Identifier (UUID) used to uniquely
   *                               identify the mail template
   * @param mailTemplateParameters the parameters to apply to the mail template
   */
  void sendMail(List<String> to, String subject, String from, String fromName, UUID mailTemplateId,
      Map<String, String> mailTemplateParameters)
    throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Update the mail template.
   *
   * @param mailTemplate the <code>MailTemplate</code> instance containing the updated
   *                         information for the mail template
   */
  void updateMailTemplate(MailTemplate mailTemplate)
    throws MailTemplateNotFoundException, MailServiceException;
}

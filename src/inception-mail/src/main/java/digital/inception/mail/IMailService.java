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

// ~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The <code>IMailService</code> interface defines the functionality provided by a Mail Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IMailService {

  /**
   * Create the new mail template.
   *
   * @param mailTemplate the <code>MailTemplate</code> instance containing the information for the
   *     new mail template
   */
  void createMailTemplate(MailTemplate mailTemplate)
      throws DuplicateMailTemplateException, MailServiceException;

  /**
   * Delete the existing mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   */
  void deleteMailTemplate(String mailTemplateId)
      throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Retrieve the mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @return the mail template
   */
  MailTemplate getMailTemplate(String mailTemplateId)
      throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Retrieve the name of the mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @return the name of the mail template
   */
  String getMailTemplateName(String mailTemplateId)
      throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Returns the summaries for all the mail templates.
   *
   * @return the summaries for all the mail templates
   */
  List<MailTemplateSummary> getMailTemplateSummaries() throws MailServiceException;

  /**
   * Retrieve the summary for the mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @return the summary for the mail template
   */
  MailTemplateSummary getMailTemplateSummary(String mailTemplateId)
      throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Returns the date and time the mail template was last updated.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @return the date and time the mail template was last updated
   */
  LocalDateTime getMailTemplateUpdated(String mailTemplateId)
      throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Returns all the mail templates.
   *
   * @return all the mail templates
   */
  List<MailTemplate> getMailTemplates() throws MailServiceException;

  /**
   * Returns the number of mail templates.
   *
   * @return the number of mail templates
   */
  long getNumberOfMailTemplates() throws MailServiceException;

  /**
   * Check whether the mail template exists.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @return <code>true</code> if the mail template exists or <code>false</code> otherwise
   */
  boolean mailTemplateExists(String mailTemplateId) throws MailServiceException;

  /**
   * Process the mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @param templateParameters the template parameters
   * @return the output of processing the template
   */
  String processMailTemplate(String mailTemplateId, Map<String, String> templateParameters)
      throws MailServiceException;

  /**
   * Send a mail.
   *
   * @param to the list of e-mail addresses to send the mail to
   * @param subject the subject for the mail
   * @param from the from e-mail address
   * @param fromName the from e-mail name
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @param mailTemplateParameters the parameters to apply to the mail template
   */
  void sendMail(
      List<String> to,
      String subject,
      String from,
      String fromName,
      String mailTemplateId,
      Map<String, String> mailTemplateParameters)
      throws MailTemplateNotFoundException, MailServiceException;

  /**
   * Update the mail template.
   *
   * @param mailTemplate the <code>MailTemplate</code> instance containing the updated information
   *     for the mail template
   */
  void updateMailTemplate(MailTemplate mailTemplate)
      throws MailTemplateNotFoundException, MailServiceException;
}

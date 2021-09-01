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

package digital.inception.mail;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <b>MailWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "MailService",
    name = "IMailService",
    targetNamespace = "http://inception.digital/mail")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class MailWebService {

  /** The Mail Service. */
  private final IMailService mailService;

  /**
   * Constructs a new <b>MailWebService</b>.
   *
   * @param mailService the Mail Service
   */
  public MailWebService(IMailService mailService) {
    this.mailService = mailService;
  }

  /**
   * Create the new mail template.
   *
   * @param mailTemplate the mail template to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateMailTemplateException if the mail template already exists
   * @throws ServiceUnavailableException if the mail template could not be created
   */
  @WebMethod(operationName = "CreateMailTemplate")
  public void createMailTemplate(
      @WebParam(name = "MailTemplate") @XmlElement(required = true) MailTemplate mailTemplate)
      throws InvalidArgumentException, DuplicateMailTemplateException, ServiceUnavailableException {
    mailService.createMailTemplate(mailTemplate);
  }

  /**
   * Delete the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailTemplateNotFoundException if the mail template could not be found
   * @throws ServiceUnavailableException if the mail template could not be deleted
   */
  @WebMethod(operationName = "DeleteMailTemplate")
  public void deleteMailTemplate(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    mailService.deleteMailTemplate(mailTemplateId);
  }

  /**
   * Retrieve the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @return the mail template
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailTemplateNotFoundException if the mail template could not be found
   * @throws ServiceUnavailableException if the mail template could not be retrieved
   */
  @WebMethod(operationName = "GetMailTemplate")
  @WebResult(name = "MailTemplate")
  public MailTemplate getMailTemplate(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    return mailService.getMailTemplate(mailTemplateId);
  }

  /**
   * Retrieve the name of the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @return the name of the mail template
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailTemplateNotFoundException if the mail template could not be found
   * @throws ServiceUnavailableException if the name of the mail template could not be retrieved
   */
  @WebMethod(operationName = "GetMailTemplateName")
  @WebResult(name = "MailTemplateName")
  public String getMailTemplateName(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    return mailService.getMailTemplateName(mailTemplateId);
  }

  /**
   * Retrieve the mail template summaries.
   *
   * @return the mail template summaries
   * @throws ServiceUnavailableException if the mail template summaries could not be retrieved
   */
  @WebMethod(operationName = "GetMailTemplateSummaries")
  @WebResult(name = "MailTemplateSummary")
  public List<MailTemplateSummary> getMailTemplateSummaries() throws ServiceUnavailableException {
    return mailService.getMailTemplateSummaries();
  }

  /**
   * Retrieve the mail templates.
   *
   * @return the mail templates
   * @throws ServiceUnavailableException if the mail templates could not be retrieved
   */
  @WebMethod(operationName = "GetMailTemplates")
  @WebResult(name = "MailTemplate")
  public List<MailTemplate> getMailTemplates() throws ServiceUnavailableException {
    return mailService.getMailTemplates();
  }

  /**
   * Update the mail template.
   *
   * @param mailTemplate the mail template
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailTemplateNotFoundException if the mail template could not be found
   * @throws ServiceUnavailableException if the mail template could not be updated
   */
  @WebMethod(operationName = "UpdateMailTemplate")
  public void updateMailTemplate(
      @WebParam(name = "MailTemplate") @XmlElement(required = true) MailTemplate mailTemplate)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    mailService.updateMailTemplate(mailTemplate);
  }
}

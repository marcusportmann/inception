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



import digital.inception.core.validation.InvalidArgumentException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;



/**
 * The <code>MailWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "MailService",
    name = "IMailService",
    targetNamespace = "http://mail.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class MailWebService {

  /** The Mail Service. */
  private final IMailService mailService;

  /**
   * Constructs a new <code>MailWebService</code>.
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
   */
  @WebMethod(operationName = "CreateMailTemplate")
  public void createMailTemplate(
      @WebParam(name = "MailTemplate") @XmlElement(required = true) MailTemplate mailTemplate)
      throws InvalidArgumentException, DuplicateMailTemplateException, MailServiceException {
    mailService.createMailTemplate(mailTemplate);
  }

  /**
   * Delete the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   */
  @WebMethod(operationName = "DeleteMailTemplate")
  public void deleteMailTemplate(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    mailService.deleteMailTemplate(mailTemplateId);
  }

  /**
   * Retrieve the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @return the mail template
   */
  @WebMethod(operationName = "GetMailTemplate")
  @WebResult(name = "MailTemplate")
  public MailTemplate getMailTemplate(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    return mailService.getMailTemplate(mailTemplateId);
  }

  /**
   * Retrieve the name of the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @return the name of the mail template
   */
  @WebMethod(operationName = "GetMailTemplateName")
  @WebResult(name = "MailTemplateName")
  public String getMailTemplateName(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    return mailService.getMailTemplateName(mailTemplateId);
  }

  /**
   * Retrieve the mail template summaries.
   *
   * @return the mail template summaries
   */
  @WebMethod(operationName = "GetMailTemplateSummaries")
  @WebResult(name = "MailTemplateSummary")
  public List<MailTemplateSummary> getMailTemplateSummaries() throws MailServiceException {
    return mailService.getMailTemplateSummaries();
  }

  /**
   * Retrieve the mail templates.
   *
   * @return the mail templates
   */
  @WebMethod(operationName = "GetMailTemplates")
  @WebResult(name = "MailTemplate")
  public List<MailTemplate> getMailTemplates() throws MailServiceException {
    return mailService.getMailTemplates();
  }

  /**
   * Update the mail template.
   *
   * @param mailTemplate the mail template
   */
  @WebMethod(operationName = "UpdateMailTemplate")
  public void updateMailTemplate(
      @WebParam(name = "MailTemplate") @XmlElement(required = true) MailTemplate mailTemplate)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    mailService.updateMailTemplate(mailTemplate);
  }
}

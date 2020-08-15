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

package digital.inception.mail;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import java.util.List;
import java.util.Set;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.annotation.XmlElement;

// ~--- JDK imports ------------------------------------------------------------

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

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <code>MailWebService</code>.
   *
   * @param mailService the Mail Service
   * @param validator the JSR-303 validator
   */
  public MailWebService(IMailService mailService, Validator validator) {
    this.mailService = mailService;
    this.validator = validator;
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
    if (mailTemplate == null) {
      throw new InvalidArgumentException("mailTemplate");
    }

    Set<ConstraintViolation<MailTemplate>> constraintViolations = validator.validate(mailTemplate);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "mailTemplate", ValidationError.toValidationErrors(constraintViolations));
    }

    mailService.createMailTemplate(mailTemplate);
  }

  /**
   * Delete the mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   */
  @WebMethod(operationName = "DeleteMailTemplate")
  public void deleteMailTemplate(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    if (mailTemplateId == null) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    mailService.deleteMailTemplate(mailTemplateId);
  }

  /**
   * Retrieve the mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @return the mail template
   */
  @WebMethod(operationName = "GetMailTemplate")
  @WebResult(name = "MailTemplate")
  public MailTemplate getMailTemplate(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    if (mailTemplateId == null) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    return mailService.getMailTemplate(mailTemplateId);
  }

  /**
   * Retrieve the name of the mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @return the name of the mail template
   */
  @WebMethod(operationName = "GetMailTemplateName")
  @WebResult(name = "MailTemplateName")
  public String getMailTemplateName(
      @WebParam(name = "MailTemplateId") @XmlElement(required = true) String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    if (mailTemplateId == null) {
      throw new InvalidArgumentException("mailTemplateId");
    }

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
    if (mailTemplate == null) {
      throw new InvalidArgumentException("mailTemplate");
    }

    Set<ConstraintViolation<MailTemplate>> constraintViolations = validator.validate(mailTemplate);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "mailTemplate", ValidationError.toValidationErrors(constraintViolations));
    }

    mailService.updateMailTemplate(mailTemplate);
  }
}

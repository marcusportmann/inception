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

import digital.inception.rs.RestControllerError;
import digital.inception.rs.RestUtil;
import digital.inception.rs.SecureRestController;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;

import io.swagger.annotations.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * The <code>MailRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Api(tags = "Mail API")
@RestController
@RequestMapping(value = "/api/mail")
@SuppressWarnings({ "unused" })
public class MailRestController extends SecureRestController
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(MailRestController.class);

  /**
   * The Mail Service.
   */
  private IMailService mailService;

  /**
   * The JSR-303 validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>MailRestController</code>.
   *
   * @param mailService the Mail Service
   * @param validator   the JSR-303 validator
   */
  public MailRestController(IMailService mailService, Validator validator)
  {
    this.mailService = mailService;
    this.validator = validator;
  }

  /**
   * Create the mail template.
   *
   * @param mailTemplate the mail template to create
   */
  @ApiOperation(value = "Create the mail template", notes = "Create the mail template")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The mail template was created successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 409, message = "A mail template with the specified ID already exists",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/mail-templates", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public void createMailTemplate(@ApiParam(name = "mailTemplate", value = "The mail template",
      required = true)
  @RequestBody MailTemplate mailTemplate)
    throws InvalidArgumentException, DuplicateMailTemplateException, MailServiceException
  {
    if (mailTemplate == null)
    {
      throw new InvalidArgumentException("mailTemplate");
    }

    Set<ConstraintViolation<MailTemplate>> constraintViolations = validator.validate(mailTemplate);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("mailTemplate", ValidationError.toValidationErrors(
          constraintViolations));
    }

    mailService.createMailTemplate(mailTemplate);
  }

  /**
   * Delete the mail template.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   */
  @ApiOperation(value = "Delete the mail template", notes = "Delete the mail template")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The mail template was deleted successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The mail template could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/mail-templates/{mailTemplateId}", method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public void deleteMailTemplate(@ApiParam(name = "mailTemplateId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the mail template",
      required = true)
  @PathVariable UUID mailTemplateId)
    throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException
  {
    if (mailTemplateId == null)
    {
      throw new InvalidArgumentException("mailTemplateId");
    }

    mailService.deleteMailTemplate(mailTemplateId);
  }

  /**
   * Retrieve the mail template.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   */
  @ApiOperation(value = "Delete the mail template", notes = "Delete the mail template")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The mail template could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/mail-templates/{mailTemplateId}", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public MailTemplate getMailTemplate(@ApiParam(name = "mailTemplateId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the mail template",
      required = true)
  @PathVariable UUID mailTemplateId)
    throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException
  {
    if (mailTemplateId == null)
    {
      throw new InvalidArgumentException("mailTemplateId");
    }

    return mailService.getMailTemplate(mailTemplateId);
  }

  /**
   * Retrieve the name of the mail template.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   *
   * @return the name of the mail template
   */
  @ApiOperation(value = "Retrieve the name of the mail template",
      notes = "Retrieve the name of the mail template")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The mail template could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/mail-templates/{mailTemplateId}/name", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public String getMailTemplateName(@ApiParam(name = "mailTemplateId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the mail template",
      required = true)
  @PathVariable UUID mailTemplateId)
    throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException
  {
    if (StringUtils.isEmpty(mailTemplateId))
    {
      throw new InvalidArgumentException("mailTemplateId");
    }

    return RestUtil.quote(mailService.getMailTemplateName(mailTemplateId));
  }

  /**
   * Retrieve the mail template summaries.
   *
   * @return the mail template summaries
   */
  @ApiOperation(value = "Retrieve the mail template summaries",
      notes = "Retrieve the mail template summaries")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/mail-template-summaries", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public List<MailTemplateSummary> getMailTemplateSummaries()
    throws MailServiceException
  {
    return mailService.getMailTemplateSummaries();
  }

  /**
   * Retrieve the mail templates.
   *
   * @return the mail templates
   */
  @ApiOperation(value = "Retrieve the mail templates", notes = "Retrieve the mail templates")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/mail-templates", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public List<MailTemplate> getMailTemplates()
    throws MailServiceException
  {
    return mailService.getMailTemplates();
  }

  /**
   * Send a test mail.
   */
  @ApiOperation(value = "Send a test mail", notes = "Send a test mail")
  @ApiResponses(value = { @ApiResponse(code = 204, message = "The mail was sent successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The mail template could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 409, message = "A mail template with the specified ID already exists",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/send-test-mail", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public void sendMailTest()
    throws InvalidArgumentException, DuplicateMailTemplateException, MailTemplateNotFoundException,
        MailServiceException
  {
    MailTemplate mailTemplate = new MailTemplate();
    mailTemplate.setId(UUID.randomUUID());
    mailTemplate.setName("Test Mail Template");
    mailTemplate.setContentType(MailTemplateContentType.HTML);
    mailTemplate.setTemplate("Hello World!".getBytes());

    mailService.createMailTemplate(mailTemplate);

    MailTemplate retrievedMailTemplate = mailService.getMailTemplate(mailTemplate.getId());

    logger.info("Retrieved mail template (" + retrievedMailTemplate.getName() + ") with ID ("
        + retrievedMailTemplate.getId() + ")");
  }

  /**
   * Update the mail template.
   *
   * @param mailTemplateId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       mail template
   * @param mailTemplate   the mail template
   */
  @ApiOperation(value = "Update the mail template", notes = "Update the mail template")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The mail template was updated successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The mail template could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/mail-templates/{mailTemplateId}", method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public void updateMailTemplate(@ApiParam(name = "mailTemplateId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the mailTemplate",
      required = true)
  @PathVariable UUID mailTemplateId, @ApiParam(name = "mailTemplate", value = "The mail template",
      required = true)
  @RequestBody MailTemplate mailTemplate)
    throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException
  {
    if (mailTemplate == null)
    {
      throw new InvalidArgumentException("mailTemplate");
    }

    if (!mailTemplate.getId().equals(mailTemplateId))
    {
      throw new InvalidArgumentException("mailTemplateId");
    }

    Set<ConstraintViolation<MailTemplate>> constraintViolations = validator.validate(mailTemplate);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("mailTemplate", ValidationError.toValidationErrors(
          constraintViolations));
    }

    mailService.updateMailTemplate(mailTemplate);
  }
}

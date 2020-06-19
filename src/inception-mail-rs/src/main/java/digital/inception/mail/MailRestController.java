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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.RestControllerError;
import digital.inception.rs.RestUtil;
import digital.inception.rs.SecureRestController;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MailRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Mail API")
@RestController
@RequestMapping(value = "/api/mail")
@SuppressWarnings({"unused"})
public class MailRestController extends SecureRestController {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(MailRestController.class);

  /** The Mail Service. */
  private final IMailService mailService;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <code>MailRestController</code>.
   *
   * @param mailService the Mail Service
   * @param validator the JSR-303 validator
   */
  public MailRestController(IMailService mailService, Validator validator) {
    this.mailService = mailService;
    this.validator = validator;
  }

  /**
   * Create the mail template.
   *
   * @param mailTemplate the mail template to create
   */
  @Operation(summary = "Create the mail template", description = "Create the mail template")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The mail template was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A mail template with the specified ID already exists",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/mail-templates",
      method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public void createMailTemplate(
      @Parameter(name = "mailTemplate", description = "The mail template", required = true)
          @RequestBody
          MailTemplate mailTemplate)
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
  @Operation(summary = "Delete the mail template", description = "Delete the mail template")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The mail template was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/mail-templates/{mailTemplateId}",
      method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public void deleteMailTemplate(
      @Parameter(
              name = "mailTemplateId",
              description = "The ID uniquely identifying the mail template",
              required = true)
          @PathVariable
          String mailTemplateId)
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
   */
  @Operation(summary = "Delete the mail template", description = "Delete the mail template")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/mail-templates/{mailTemplateId}",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public MailTemplate getMailTemplate(
      @Parameter(
              name = "mailTemplateId",
              description = "The ID uniquely identifying the mail template",
              required = true)
          @PathVariable
          String mailTemplateId)
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
  @Operation(
      summary = "Retrieve the name of the mail template",
      description = "Retrieve the name of the mail template")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/mail-templates/{mailTemplateId}/name",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public String getMailTemplateName(
      @Parameter(
              name = "mailTemplateId",
              description = "The ID uniquely identifying the mail template",
              required = true)
          @PathVariable
          String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    if (StringUtils.isEmpty(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    return RestUtil.quote(mailService.getMailTemplateName(mailTemplateId));
  }

  /**
   * Retrieve the mail template summaries.
   *
   * @return the mail template summaries
   */
  @Operation(
      summary = "Retrieve the mail template summaries",
      description = "Retrieve the mail template summaries")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/mail-template-summaries",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public List<MailTemplateSummary> getMailTemplateSummaries() throws MailServiceException {
    return mailService.getMailTemplateSummaries();
  }

  /**
   * Retrieve the mail templates.
   *
   * @return the mail templates
   */
  @Operation(summary = "Retrieve the mail templates", description = "Retrieve the mail templates")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/mail-templates",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public List<MailTemplate> getMailTemplates() throws MailServiceException {
    return mailService.getMailTemplates();
  }

  /** Send a test mail. */
  @Operation(summary = "Send a test mail", description = "Send a test mail")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The mail was sent successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A mail template with the specified ID already exists",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/send-test-mail",
      method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public void sendMailTest()
      throws DuplicateMailTemplateException, MailTemplateNotFoundException, MailServiceException {
    MailTemplate mailTemplate = new MailTemplate();
    mailTemplate.setId("TestMailTemplate");
    mailTemplate.setName("Test Mail Template");
    mailTemplate.setContentType(MailTemplateContentType.HTML);
    mailTemplate.setTemplate("Hello World!".getBytes());

    mailService.createMailTemplate(mailTemplate);

    MailTemplate retrievedMailTemplate = mailService.getMailTemplate(mailTemplate.getId());

    logger.info(
        "Retrieved mail template ("
            + retrievedMailTemplate.getName()
            + ") with ID ("
            + retrievedMailTemplate.getId()
            + ")");
  }

  /**
   * Update the mail template.
   *
   * @param mailTemplateId the ID uniquely identifying the mail template
   * @param mailTemplate the mail template
   */
  @Operation(summary = "Update the mail template", description = "Update the mail template")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The mail template was updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestControllerError.class)))
      })
  @RequestMapping(
      value = "/mail-templates/{mailTemplateId}",
      method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Mail.MailTemplateAdministration')")
  public void updateMailTemplate(
      @Parameter(
              name = "mailTemplateId",
              description = "The ID uniquely identifying the mailTemplate",
              required = true)
          @PathVariable
          String mailTemplateId,
      @Parameter(name = "mailTemplate", description = "The mail template", required = true)
          @RequestBody
          MailTemplate mailTemplate)
      throws InvalidArgumentException, MailTemplateNotFoundException, MailServiceException {
    if (mailTemplate == null) {
      throw new InvalidArgumentException("mailTemplate");
    }

    if (!mailTemplate.getId().equals(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    Set<ConstraintViolation<MailTemplate>> constraintViolations = validator.validate(mailTemplate);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "mailTemplate", ValidationError.toValidationErrors(constraintViolations));
    }

    mailService.updateMailTemplate(mailTemplate);
  }
}

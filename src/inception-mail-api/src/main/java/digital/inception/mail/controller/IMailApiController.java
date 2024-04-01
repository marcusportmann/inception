/*
 * Copyright Marcus Portmann
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

package digital.inception.mail.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.mail.model.DuplicateMailTemplateException;
import digital.inception.mail.model.MailTemplate;
import digital.inception.mail.model.MailTemplateNotFoundException;
import digital.inception.mail.model.MailTemplateSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The <b>IMailApiController</b> interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Mail")
@RequestMapping(value = "/api/mail")
// @el (isSecurityDisabled: digital.inception.api.SecureApiSecurityExpressionRoot.isSecurityEnabled)
public interface IMailApiController {

  /**
   * Create the new mail template.
   *
   * @param mailTemplate the mail template to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateMailTemplateException if the mail template already exists
   * @throws ServiceUnavailableException if the mail template could not be created
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
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A mail template with the specified ID already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mail-templates",
      method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Mail.MailTemplateAdministration')")
  void createMailTemplate(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The mail template to create",
              required = true)
          @RequestBody
          MailTemplate mailTemplate)
      throws InvalidArgumentException, DuplicateMailTemplateException, ServiceUnavailableException;

  /**
   * Delete the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailTemplateNotFoundException if the mail template could not be found
   * @throws ServiceUnavailableException if the mail template could not be deleted
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
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mail-templates/{mailTemplateId}",
      method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Mail.MailTemplateAdministration')")
  void deleteMailTemplate(
      @Parameter(
              name = "mailTemplateId",
              description = "The ID for the mail template",
              required = true)
          @PathVariable
          String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @return the mail template
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailTemplateNotFoundException if the mail template could not be found
   * @throws ServiceUnavailableException if the mail template could not be retrieved
   */
  @Operation(summary = "Retrieve the mail template", description = "Retrieve the mail template")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mail-templates/{mailTemplateId}",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Mail.MailTemplateAdministration')")
  MailTemplate getMailTemplate(
      @Parameter(
              name = "mailTemplateId",
              description = "The ID for the mail template",
              required = true)
          @PathVariable
          String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @return the name of the mail template
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailTemplateNotFoundException if the mail template could not be found
   * @throws ServiceUnavailableException if the name of the mail template could not be retrieved
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
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mail-templates/{mailTemplateId}/name",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Mail.MailTemplateAdministration')")
  String getMailTemplateName(
      @Parameter(
              name = "mailTemplateId",
              description = "The ID for the mail template",
              required = true)
          @PathVariable
          String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the mail template summaries.
   *
   * @return the mail template summaries
   * @throws ServiceUnavailableException if the mail template summaries could not be retrieved
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
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mail-template-summaries",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Mail.MailTemplateAdministration')")
  List<MailTemplateSummary> getMailTemplateSummaries() throws ServiceUnavailableException;

  /**
   * Retrieve the mail templates.
   *
   * @return the mail templates
   * @throws ServiceUnavailableException if the mail templates could not be retrieved
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
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mail-templates",
      method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Mail.MailTemplateAdministration')")
  List<MailTemplate> getMailTemplates() throws ServiceUnavailableException;

  //  /** Send a test mail. */
  //  @Operation(summary = "Send a test mail", description = "Send a test mail")
  //  @ApiResponses(
  //      value = {
  //        @ApiResponse(responseCode = "204", description = "The mail was sent successfully"),
  //        @ApiResponse(
  //            responseCode = "400",
  //            description = "Invalid argument",
  //            content =
  //                @Content(
  //                    mediaType = "application/problem+json",
  //                    schema = @Schema(implementation = ProblemDetails.class))),
  //          @ApiResponse(
  //              responseCode = "403",
  //              description = "Access denied",
  //              content =
  //              @Content(
  //                  mediaType = "application/problem+json",
  //                  schema = @Schema(implementation = ProblemDetails.class))),
  //        @ApiResponse(
  //            responseCode = "404",
  //            description = "The mail template could not be found",
  //            content =
  //                @Content(
  //                    mediaType = "application/problem+json",
  //                    schema = @Schema(implementation = ProblemDetails.class))),
  //        @ApiResponse(
  //            responseCode = "409",
  //            description = "A mail template with the specified ID already exists",
  //            content =
  //                @Content(
  //                    mediaType = "application/problem+json",
  //                    schema = @Schema(implementation = ProblemDetails.class))),
  //        @ApiResponse(
  //            responseCode = "500",
  //            description =
  //                "An error has occurred and the request could not be processed at this time",
  //            content =
  //                @Content(
  //                    mediaType = "application/problem+json",
  //                    schema = @Schema(implementation = ProblemDetails.class)))
  //      })
  //  @RequestMapping(
  //      value = "/send-test-mail",
  //      method = RequestMethod.POST,
  //      produces = "application/json")
  //  @ResponseStatus(HttpStatus.NO_CONTENT)
  //  @PreAuthorize(
  //      "isSecurityDisabled() or hasRole('Administrator') or
  // hasAccessToFunction('Mail.MailTemplateAdministration')")
  //  public void sendMailTest()
  //      throws InvalidArgumentException, DuplicateMailTemplateException,
  //          MailTemplateNotFoundException, ServiceUnavailableException;

  /**
   * Update the mail template.
   *
   * @param mailTemplateId the ID for the mail template
   * @param mailTemplate the mail template
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailTemplateNotFoundException if the mail template could not be found
   * @throws ServiceUnavailableException if the mail template could not be updated
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
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The mail template could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/mail-templates/{mailTemplateId}",
      method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Mail.MailTemplateAdministration')")
  void updateMailTemplate(
      @Parameter(
              name = "mailTemplateId",
              description = "The ID for the mail template",
              required = true)
          @PathVariable
          String mailTemplateId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The mail template to update",
              required = true)
          @RequestBody
          MailTemplate mailTemplate)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException;
}

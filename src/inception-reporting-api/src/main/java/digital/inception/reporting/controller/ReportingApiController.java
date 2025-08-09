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

package digital.inception.reporting.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.reporting.exception.DuplicateReportDefinitionException;
import digital.inception.reporting.exception.ReportDefinitionNotFoundException;
import digital.inception.reporting.model.ReportDefinition;
import digital.inception.reporting.model.ReportDefinitionSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code ReportingApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Reporting")
@RequestMapping(value = "/api/reporting")
// @el (isSecurityDisabled: PolicyDecisionPointSecurityExpressionRoot.isSecurityDisabled)
public interface ReportingApiController {

  /**
   * Create the report definition.
   *
   * @param reportDefinition the report definition to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateReportDefinitionException if the report definition already exists
   * @throws ServiceUnavailableException if the report definition could not be created
   */
  @Operation(summary = "Create the report definition", description = "Create the report definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The report definition was created"),
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
            description = "A report definition with the specified ID already exists",
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
      value = "/report-definitions",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Reporting.ReportingAdministration') or hasAccessToFunction('Reporting.ReportDefinitionAdministration')")
  void createReportDefinition(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The report definition to create",
              required = true)
          @RequestBody
          ReportDefinition reportDefinition)
      throws InvalidArgumentException,
          DuplicateReportDefinitionException,
          ServiceUnavailableException;

  /**
   * Delete the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the report definition could not be deleted
   */
  @Operation(summary = "Delete the report definition", description = "Delete the report definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The report definition was deleted"),
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
            description = "The report definition could not be found",
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
      value = "/report-definitions/{reportDefinitionId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Reporting.ReportingAdministration') or hasAccessToFunction('Reporting.ReportDefinitionAdministration')")
  void deleteReportDefinition(
      @Parameter(
              name = "reportDefinitionId",
              description = "The ID for the report definition",
              required = true)
          @PathVariable
          String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Generate the PDF report.
   *
   * @param generateReportRequest the request to generate the PDF report
   * @return the PDF report
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the PDF report could not be generated
   */
  @Operation(summary = "Generate the PDF report", description = "Generate the PDF report")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The PDF report was generated"),
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
            description = "The report definition could not be found",
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
      value = "/generate-report",
      method = RequestMethod.POST,
      produces = "application/pdf")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<byte[]> generateReport(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to generate a report",
              required = true)
          @RequestBody
          GenerateReportRequest generateReportRequest)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @return the report definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the report definition could not be retrieved
   */
  @Operation(
      summary = "Retrieve the report definition",
      description = "Retrieve the report definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The report definition was retrieved"),
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
            description = "The report definition could not be found",
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
      value = "/report-definitions/{reportDefinitionId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Reporting.ReportingAdministration') or hasAccessToFunction('Reporting.ReportDefinitionAdministration')")
  ReportDefinition getReportDefinition(
      @Parameter(
              name = "reportDefinition",
              description = "The ID for the report definition",
              required = true)
          @PathVariable
          String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the name of the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @return the name of the report definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the name of the report definition could not be retrieved
   */
  @Operation(
      summary = "Retrieve the name of the report definition",
      description = "Retrieve the name of the report definition")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The name of the report definition was retrieved"),
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
            description = "The report definition could not be found",
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
      value = "/report-definitions/{reportDefinitionId}/name",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  String getReportDefinitionName(
      @Parameter(
              name = "reportDefinitionId",
              description = "The ID for the report definition",
              required = true)
          @PathVariable
          String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the report definition summaries.
   *
   * @return the report definition summaries
   * @throws ServiceUnavailableException if the report definition summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the report definition summaries",
      description = "Retrieve the report definition summaries")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The report definition summaries were retrieved"),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
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
      value = "/report-definition-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Reporting.ReportingAdministration') or hasAccessToFunction('Reporting.ReportDefinitionAdministration')")
  List<ReportDefinitionSummary> getReportDefinitionSummaries() throws ServiceUnavailableException;

  /**
   * Retrieve the report definitions.
   *
   * @return the report definitions
   * @throws ServiceUnavailableException if the report definitions could not be retrieved
   */
  @Operation(
      summary = "Retrieve the report definitions",
      description = "Retrieve the report definitions")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The report definitions were retrieved"),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
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
      value = "/report-definitions",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Reporting.ReportingAdministration') or hasAccessToFunction('Reporting.ReportDefinitionAdministration')")
  List<ReportDefinition> getReportDefinitions() throws ServiceUnavailableException;

  /**
   * Update the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @param reportDefinition the report definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the report definition could not be updated
   */
  @Operation(summary = "Update the report definition", description = "Update the report definition")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The report definition was updated"),
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
            description = "The report definition could not be found",
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
      value = "/report-definitions/{reportDefinitionId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Reporting.ReportingAdministration') or hasAccessToFunction('Reporting.ReportDefinitionAdministration')")
  void updateReportDefinition(
      @Parameter(
              name = "reportDefinitionId",
              description = "The ID for the report definition",
              required = true)
          @PathVariable
          String reportDefinitionId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The report definition to update",
              required = true)
          @RequestBody
          ReportDefinition reportDefinition)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException;
}

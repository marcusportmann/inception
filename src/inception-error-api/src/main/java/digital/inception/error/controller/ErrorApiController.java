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

package digital.inception.error.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.error.model.ErrorReport;
import digital.inception.error.model.ErrorReportNotFoundException;
import digital.inception.error.model.ErrorReportSortBy;
import digital.inception.error.model.ErrorReportSummaries;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The <b>ErrorApiController</b> interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Error")
@RequestMapping(value = "/api/error")
// @el (isSecurityDisabled: digital.inception.api.SecureApiSecurityExpressionRoot.isSecurityEnabled)
public interface ErrorApiController {

  /**
   * Create the new error report.
   *
   * @param errorReport the error report
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the error report could not be created
   */
  @Operation(summary = "Create the error report", description = "Create the error report")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The error report was created successfully"),
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/error-reports",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void createErrorReport(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The error report to create",
              required = true)
          @RequestBody
          ErrorReport errorReport)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the error report.
   *
   * @param errorReportId the ID for the error report
   * @return the error report
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ErrorReportNotFoundException if the error report could not be found
   * @throws ServiceUnavailableException if the error report could not be retrieved
   */
  @Operation(summary = "Retrieve the error report", description = "Retrieve the error report")
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
            description = "The error report could not be found",
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
      value = "/error-reports/{errorReportId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Error.ErrorReportAdministration') or hasAccessToFunction('Error.ViewErrorReport')")
  ErrorReport getErrorReport(
      @Parameter(
              name = "errorReportId",
              description = "The ID for the error report",
              required = true)
          @PathVariable
          UUID errorReportId)
      throws InvalidArgumentException, ErrorReportNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the error report summaries.
   *
   * @param filter the filter to apply to the error report summaries
   * @param fromDate the ISO 8601 format date value for the date to retrieve the error report
   *     summaries from
   * @param toDate the ISO 8601 format date value for the date to retrieve the error report
   *     summaries from
   * @param sortBy the method used to sort the error report summaries e.g. by who submitted them
   * @param sortDirection the sort direction to apply to the error report summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the error report summaries
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the error report summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the error report summaries",
      description = "Retrieve the error report summaries")
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
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/error-report-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Error.ErrorReportAdministration') or hasAccessToFunction('Error.ViewErrorReport')")
  ErrorReportSummaries getErrorReportSummaries(
      @Parameter(name = "filter", description = "The filter to apply to the error report summaries")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "fromDate",
              description =
                  "The ISO 8601 format date value for the date to retrieve the error report summaries from",
              example = "2015-01-01")
          @RequestParam(value = "fromDate", required = false)
          LocalDate fromDate,
      @Parameter(
              name = "toDate",
              description =
                  "The ISO 8601 format date value for the date to retrieve the error report summaries to",
              example = "2030-12-31")
          @RequestParam(value = "toDate", required = false)
          LocalDate toDate,
      @Parameter(
              name = "sortBy",
              description =
                  "The method used to sort the error report summaries e.g. by who submitted them")
          @RequestParam(value = "sortBy", required = false)
          ErrorReportSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the error report summaries")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;
}

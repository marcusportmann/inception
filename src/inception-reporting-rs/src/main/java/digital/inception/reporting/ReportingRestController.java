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

package digital.inception.reporting;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.RestControllerError;
import digital.inception.rs.RestUtil;
import digital.inception.rs.SecureRestController;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportingRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Api(tags = "Reporting API")
@RestController
@RequestMapping(value = "/api/reporting")
@SuppressWarnings({"unused"})
public class ReportingRestController extends SecureRestController {

  /**
   * The data source used to provide connections to the application database.
   */
  private DataSource dataSource;

  /**
   * The Reporting Service.
   */
  private IReportingService reportingService;

  /**
   * The JSR-303 validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>ReportingRestController</code>.
   *
   * @param dataSource       the data source used to provide connections to the application
   *                         database
   * @param reportingService the Reporting Service
   * @param validator        the JSR-303 validator
   */
  public ReportingRestController(@Qualifier("applicationDataSource") DataSource dataSource,
      IReportingService reportingService, Validator validator) {
    this.dataSource = dataSource;
    this.reportingService = reportingService;
    this.validator = validator;
  }

  /**
   * Create the report definition.
   *
   * @param reportDefinition the report definition to create
   */
  @ApiOperation(value = "Create the report definition", notes = "Create the report definition")
  @ApiResponses(value = {@ApiResponse(code = 204,
      message = "The report definition was created successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 409, message = "A report definition with the specified ID already exists",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/report-definitions", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Reporting.ReportingAdministration') or hasAuthority('FUNCTION_Reporting.ReportDefinitionAdministration')")
  public void createReportDefinition(@ApiParam(name = "reportDefinition",
      value = "The report definition", required = true)
  @RequestBody ReportDefinition reportDefinition)
      throws InvalidArgumentException, DuplicateReportDefinitionException, ReportingServiceException {
    if (reportDefinition == null) {
      throw new InvalidArgumentException("reportDefinition");
    }

    Set<ConstraintViolation<ReportDefinition>> constraintViolations = validator.validate(
        reportDefinition);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException("reportDefinition", ValidationError.toValidationErrors(
          constraintViolations));
    }

    reportingService.createReportDefinition(reportDefinition);
  }

  /**
   * Delete the report definition.
   *
   * @param reportDefinitionId the ID used to uniquely identify the report definition
   */
  @ApiOperation(value = "Delete the report definition", notes = "Delete the report definition")
  @ApiResponses(value = {@ApiResponse(code = 204,
      message = "The report definition was deleted successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The report definition could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/report-definitions/{reportDefinitionId}", method = RequestMethod.DELETE,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Reporting.ReportingAdministration') or hasAuthority('FUNCTION_Reporting.ReportDefinitionAdministration')")
  public void deleteReportDefinition(@ApiParam(name = "reportDefinitionId",
      value = "The ID used to uniquely identify the report definition", required = true)
  @PathVariable String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException {
    if (reportDefinitionId == null) {
      throw new InvalidArgumentException("reportDefinitionId");
    }

    reportingService.deleteReportDefinition(reportDefinitionId);
  }

  /**
   * Generate the PDF report.
   *
   * @param generateReportRequest the request to generate the PDF report
   *
   * @return the PDF report
   */
  @ApiOperation(value = "Generate the PDF report", notes = "Generate the PDF report")
  @ApiResponses(value = {@ApiResponse(code = 200,
      message = "The PDF report was generated successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The report definition could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/generate-report", method = RequestMethod.POST,
      produces = "application/pdf")
  public ResponseEntity<byte[]> generateReport(@ApiParam(name = "generateReportRequest",
      value = "The request to generate a report", required = true)
  @RequestBody GenerateReportRequest generateReportRequest)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException {
    if (generateReportRequest == null) {
      throw new InvalidArgumentException("generateReportRequest");
    }

    Set<ConstraintViolation<GenerateReportRequest>> constraintViolations = validator.validate(
        generateReportRequest);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException("generateReportRequest",
          ValidationError.toValidationErrors(constraintViolations));
    }

    Map<String, Object> parameters = new HashMap<>();

    for (ReportParameter reportParameter : generateReportRequest.getReportParameters()) {
      parameters.put(reportParameter.getName(), reportParameter.getValue());
    }

    ReportDefinitionSummary reportDefinitionSummary = reportingService.getReportDefinitionSummary(
        generateReportRequest.getReportDefinitionId());

    try (Connection connection = dataSource.getConnection()) {
      byte[] reportPDF = reportingService.createReportPDF(
          generateReportRequest.getReportDefinitionId(), parameters, connection);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType("application/pdf"));
      headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
      headers.add("Pragma", "no-cache");
      headers.add("Expires", "0");

      String filename = reportDefinitionSummary.getName().replaceAll(" ", "") + ".pdf";
      headers.setContentDispositionFormData(filename, filename);
      headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

      return new ResponseEntity<>(reportPDF, headers, HttpStatus.OK);
    } catch (ReportDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ReportingServiceException("Failed to generate the PDF report", e);
    }
  }

  /**
   * Retrieve the report definition.
   *
   * @param reportDefinitionId the ID used to uniquely identify the report definition
   *
   * @return the report definition
   */
  @ApiOperation(value = "Retrieve the report definition", notes = "Retrieve the report definition")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The report definition could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/report-definitions/{reportDefinitionId}", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Reporting.ReportingAdministration') or hasAuthority('FUNCTION_Reporting.ReportDefinitionAdministration')")
  public ReportDefinition getReportDefinition(@ApiParam(name = "reportDefinition",
      value = "The ID used to uniquely identify the report definition", required = true)
  @PathVariable String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException {
    if (StringUtils.isEmpty(reportDefinitionId)) {
      throw new InvalidArgumentException("reportDefinitionId");
    }

    return reportingService.getReportDefinition(reportDefinitionId);
  }

  /**
   * Retrieve the name of the report definition.
   *
   * @param reportDefinitionId the ID used to uniquely identify the report definition
   *
   * @return the name of the report definition
   */
  @ApiOperation(value = "Retrieve the name of the report definition",
      notes = "Retrieve the name of the report definition")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The report definition could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/report-definitions/{reportDefinitionId}/name",
      method = RequestMethod.GET, produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public String getReportDefinitionName(@ApiParam(name = "reportDefinitionId",
      value = "The ID used to uniquely identify the report definition", required = true)
  @PathVariable String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException {
    if (StringUtils.isEmpty(reportDefinitionId)) {
      throw new InvalidArgumentException("reportDefinitionId");
    }

    return RestUtil.quote(reportingService.getReportDefinitionName(reportDefinitionId));
  }

  /**
   * Retrieve the report definition summaries.
   *
   * @return the report definition summaries
   */
  @ApiOperation(value = "Retrieve the report definition summaries",
      notes = "Retrieve the report definition summaries")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/report-definition-summaries", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Reporting.ReportingAdministration') or hasAuthority('FUNCTION_Reporting.ReportDefinitionAdministration')")
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
      throws ReportingServiceException {
    return reportingService.getReportDefinitionSummaries();
  }

  /**
   * Retrieve the report definitions.
   *
   * @return the report definitions
   */
  @ApiOperation(value = "Retrieve the report definitions",
      notes = "Retrieve the report definitions")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/report-definitions", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Reporting.ReportingAdministration') or hasAuthority('FUNCTION_Reporting.ReportDefinitionAdministration')")
  public List<ReportDefinition> getReportDefinitions()
      throws ReportingServiceException {
    return reportingService.getReportDefinitions();
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinitionId the ID used to uniquely identify the report definition
   * @param reportDefinition   the report definition
   */
  @ApiOperation(value = "Update the report definition", notes = "Update the report definition")
  @ApiResponses(value = {@ApiResponse(code = 204,
      message = "The report definition was updated successfully"),
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class),
      @ApiResponse(code = 404, message = "The report definition could not be found",
          response = RestControllerError.class),
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class)})
  @RequestMapping(value = "/report-definitions/{reportDefinitionId}", method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "hasRole('Administrator') or hasAuthority('FUNCTION_Reporting.ReportingAdministration') or hasAuthority('FUNCTION_Reporting.ReportDefinitionAdministration')")
  public void updateReportDefinition(@ApiParam(name = "reportDefinitionId",
      value = "The ID used to uniquely identify the reportDefinition", required = true)
  @PathVariable String reportDefinitionId, @ApiParam(name = "reportDefinition",
      value = "The report definition", required = true)
  @RequestBody ReportDefinition reportDefinition)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException {
    if (reportDefinition == null) {
      throw new InvalidArgumentException("reportDefinition");
    }

    if (!reportDefinition.getId().equals(reportDefinitionId)) {
      throw new InvalidArgumentException("reportDefinitionId");
    }

    Set<ConstraintViolation<ReportDefinition>> constraintViolations = validator.validate(
        reportDefinition);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException("reportDefinition", ValidationError.toValidationErrors(
          constraintViolations));
    }

    reportingService.updateReportDefinition(reportDefinition);
  }
}

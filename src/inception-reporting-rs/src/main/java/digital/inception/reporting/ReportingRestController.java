/*
 * Copyright 2018 Marcus Portmann
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
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.sql.Connection;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportingRestController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ReportingRestController
{
  /* Reporting Service */
  @Autowired
  private IReportingService reportingService;

  /* Validator */
  @Autowired
  private Validator validator;

  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Create a report definition.
   *
   * @param reportDefinition the report definition to create
   */
  @ApiOperation(value = "Create a report definition", notes = "Create the report definition")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The report definition was created successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 409, message = "A report definition with the specified ID already exists",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/reportDefinitions", method = RequestMethod.POST,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void createReportDefinition(@ApiParam(name = "reportDefinition",
      value = "The report definition", required = true)
  @RequestBody ReportDefinition reportDefinition)
    throws InvalidArgumentException, DuplicateReportDefinitionException, ReportingServiceException
  {
    if (reportDefinition == null)
    {
      throw new InvalidArgumentException("reportDefinition");
    }

    Set<ConstraintViolation<ReportDefinition>> constraintViolations = validator.validate(
        reportDefinition);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("reportDefinition", ValidationError.toValidationErrors(
          constraintViolations));
    }

    reportingService.createReportDefinition(reportDefinition);
  }

  /**
   * Delete the report definition.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   */
  @ApiOperation(value = "Delete a report definition", notes = "Delete the report definition")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The report definition was deleted successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The report definition could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/reportDefinitions/{reportDefinitionId}",
      method = RequestMethod.DELETE, produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteReportDefinition(@ApiParam(name = "reportDefinitionId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the report definition",
      required = true)
  @PathVariable UUID reportDefinitionId)
    throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException
  {
    if (reportDefinitionId == null)
    {
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
  @ApiOperation(value = "Generate a PDF report", notes = "Generate the PDF report")
  @ApiResponses(value = { @ApiResponse(code = 200,
      message = "The PDF report was generated successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The report definition could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/generateReport", method = RequestMethod.POST,
      produces = "application/pdf")
  public ResponseEntity<byte[]> generateReport(
      @RequestBody GenerateReportRequest generateReportRequest)
    throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException
  {
    if (generateReportRequest == null)
    {
      throw new InvalidArgumentException("generateReportRequest");
    }

    Set<ConstraintViolation<GenerateReportRequest>> constraintViolations = validator.validate(
        generateReportRequest);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("generateReportRequest",
          ValidationError.toValidationErrors(constraintViolations));
    }

    Map<String, Object> parameters = new HashMap<>();

    for (ReportParameter reportParameter : generateReportRequest.getReportParameters())
    {
      parameters.put(reportParameter.getName(), reportParameter.getValue());
    }

    ReportDefinitionSummary reportDefinitionSummary = reportingService.getReportDefinitionSummary(
        generateReportRequest.getReportDefinitionId());

    try (Connection connection = dataSource.getConnection())
    {
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

      ResponseEntity<byte[]> response = new ResponseEntity<>(reportPDF, headers, HttpStatus.OK);

      return response;
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to generate the PDF report", e);
    }
  }

  /**
   * Retrieve the report definition summaries.
   *
   * @return the report definition summaries
   */
  @ApiOperation(value = "Retrieve the report definition summaries",
      notes = "Retrieve the report definition summaries")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/reportDefinitionSummaries", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
    throws ReportingServiceException
  {
    return reportingService.getReportDefinitionSummaries();
  }

  /**
   * Retrieve the report definitions.
   *
   * @return the report definitions
   */
  @ApiOperation(value = "Retrieve the report definitions",
      notes = "Retrieve the report definitions")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK") ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/reportDefinitions", method = RequestMethod.GET,
      produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public List<ReportDefinition> getReportDefinitions()
    throws ReportingServiceException
  {
    return reportingService.getReportDefinitions();
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   * @param reportDefinition   the report definition
   */
  @ApiOperation(value = "Update a report definition", notes = "Update the report definition")
  @ApiResponses(value = { @ApiResponse(code = 204,
      message = "The report definition was updated successfully") ,
      @ApiResponse(code = 400, message = "Invalid argument", response = RestControllerError.class) ,
      @ApiResponse(code = 404, message = "The report definition could not be found",
          response = RestControllerError.class) ,
      @ApiResponse(code = 500,
          message = "An error has occurred and the service is unable to process the request at this time",
          response = RestControllerError.class) })
  @RequestMapping(value = "/api/reportDefinitions/{reportDefinitionId}", method = RequestMethod.PUT,
      produces = "application/json")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateReportDefinition(@ApiParam(name = "reportDefinitionId",
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the reportDefinition",
      required = true)
  @PathVariable UUID reportDefinitionId, @ApiParam(name = "reportDefinition",
      value = "The report definition", required = true)
  @RequestBody ReportDefinition reportDefinition)
    throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException
  {
    if (reportDefinition == null)
    {
      throw new InvalidArgumentException("reportDefinition");
    }

    if (!reportDefinition.getId().equals(reportDefinitionId))
    {
      throw new InvalidArgumentException("reportDefinitionId");
    }

    Set<ConstraintViolation<ReportDefinition>> constraintViolations = validator.validate(
        reportDefinition);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("reportDefinition", ValidationError.toValidationErrors(
          constraintViolations));
    }

    reportingService.updateReportDefinition(reportDefinition);
  }
}

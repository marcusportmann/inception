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

import digital.inception.api.SecureApiController;
import digital.inception.core.api.ApiUtil;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.exception.ValidationError;
import digital.inception.reporting.exception.DuplicateReportDefinitionException;
import digital.inception.reporting.exception.ReportDefinitionNotFoundException;
import digital.inception.reporting.model.ReportDefinition;
import digital.inception.reporting.model.ReportDefinitionSummary;
import digital.inception.reporting.model.ReportParameter;
import digital.inception.reporting.service.ReportingService;
import jakarta.validation.ConstraintViolation;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code ReportingApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class ReportingApiControllerImpl extends SecureApiController
    implements ReportingApiController {

  /** The data source used to provide connections to the application database. */
  private final DataSource dataSource;

  /** The Reporting Service. */
  private final ReportingService reportingService;

  /**
   * Constructs a new {@code ReportingApiControllerImpl}.
   *
   * @param applicationContext the Spring application context
   * @param dataSource the data source used to provide connections to the application database
   * @param reportingService the Reporting Service
   */
  public ReportingApiControllerImpl(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource,
      ReportingService reportingService) {
    super(applicationContext);

    this.dataSource = dataSource;
    this.reportingService = reportingService;
  }

  @Override
  public void createReportDefinition(ReportDefinition reportDefinition)
      throws InvalidArgumentException,
          DuplicateReportDefinitionException,
          ServiceUnavailableException {
    reportingService.createReportDefinition(reportDefinition);
  }

  @Override
  public void deleteReportDefinition(String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    reportingService.deleteReportDefinition(reportDefinitionId);
  }

  @Override
  public ResponseEntity<byte[]> generateReport(GenerateReportRequest generateReportRequest)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    if (generateReportRequest == null) {
      throw new InvalidArgumentException("generateReportRequest");
    }

    Set<ConstraintViolation<GenerateReportRequest>> constraintViolations =
        getValidator().validate(generateReportRequest);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "generateReportRequest", ValidationError.toValidationErrors(constraintViolations));
    }

    Map<String, Object> parameters = new HashMap<>();

    for (ReportParameter reportParameter : generateReportRequest.getReportParameters()) {
      parameters.put(reportParameter.getName(), reportParameter.getValue());
    }

    ReportDefinitionSummary reportDefinitionSummary =
        reportingService.getReportDefinitionSummary(generateReportRequest.getReportDefinitionId());

    try (Connection connection = dataSource.getConnection()) {
      byte[] reportPDF =
          reportingService.createReportPDF(
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
      throw new ServiceUnavailableException("Failed to generate the PDF report", e);
    }
  }

  @Override
  public ReportDefinition getReportDefinition(String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    return reportingService.getReportDefinition(reportDefinitionId);
  }

  @Override
  public String getReportDefinitionName(String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    return ApiUtil.quote(reportingService.getReportDefinitionName(reportDefinitionId));
  }

  @Override
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
      throws ServiceUnavailableException {
    return reportingService.getReportDefinitionSummaries();
  }

  @Override
  public List<ReportDefinition> getReportDefinitions() throws ServiceUnavailableException {
    return reportingService.getReportDefinitions();
  }

  @Override
  public void updateReportDefinition(String reportDefinitionId, ReportDefinition reportDefinition)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(reportDefinitionId)) {
      throw new InvalidArgumentException("reportDefinitionId");
    }

    if (reportDefinition == null) {
      throw new InvalidArgumentException("reportDefinition");
    }

    if (!reportDefinitionId.equals(reportDefinition.getId())) {
      throw new InvalidArgumentException("reportDefinition");
    }

    reportingService.updateReportDefinition(reportDefinition);
  }
}

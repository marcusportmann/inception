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

package digital.inception.reporting;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.validation.InvalidArgumentException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.sql.DataSource;
import javax.xml.bind.annotation.XmlElement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportingWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ReportingService",
    name = "IReportingService",
    targetNamespace = "http://reporting.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ReportingWebService {

  /** The data source used to provide connections to the application database. */
  private final DataSource dataSource;

  /** The Reporting Service. */
  private final IReportingService reportingService;

  /**
   * Constructs a new <code>ReportingWebService</code>.
   *
   * @param dataSource the data source used to provide connections to the application database
   * @param reportingService the Reporting Service
   */
  public ReportingWebService(
      @Qualifier("applicationDataSource") DataSource dataSource,
      IReportingService reportingService) {
    this.dataSource = dataSource;
    this.reportingService = reportingService;
  }

  /**
   * Create the new report definition.
   *
   * @param reportDefinition the report definition to create
   */
  @WebMethod(operationName = "CreateReportDefinition")
  public void createReportDefinition(
      @WebParam(name = "ReportDefinition") @XmlElement(required = true)
          ReportDefinition reportDefinition)
      throws InvalidArgumentException, DuplicateReportDefinitionException,
          ReportingServiceException {
    reportingService.createReportDefinition(reportDefinition);
  }

  /**
   * Delete the report definition.
   *
   * @param reportDefinitionId the ID uniquely identifying the report definition
   */
  @WebMethod(operationName = "DeleteReportDefinition")
  public void deleteReportDefinition(
      @WebParam(name = "ReportDefinitionId") @XmlElement(required = true) String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException,
          ReportingServiceException {
    reportingService.deleteReportDefinition(reportDefinitionId);
  }

  /**
   * Generate the PDF report.
   *
   * @param reportDefinitionId the ID uniquely identifying the report definition
   * @param reportParameters the report parameters
   * @return the PDF report
   */
  @WebMethod(operationName = "GenerateReport")
  @WebResult(name = "Report")
  public byte[] generateReport(
      @WebParam(name = "ReportDefinitionId") @XmlElement(required = true) String reportDefinitionId,
      @WebParam(name = "ReportParameters") @XmlElement(required = true)
          List<ReportParameter> reportParameters)
      throws InvalidArgumentException, ReportDefinitionNotFoundException,
          ReportingServiceException {
    if (!StringUtils.hasText(reportDefinitionId)) {
      throw new InvalidArgumentException("reportDefinitionId");
    }

    Map<String, Object> parameters = new HashMap<>();

    for (ReportParameter reportParameter : reportParameters) {
      parameters.put(reportParameter.getName(), reportParameter.getValue());
    }

    try (Connection connection = dataSource.getConnection()) {
      return reportingService.createReportPDF(reportDefinitionId, parameters, connection);
    } catch (ReportDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ReportingServiceException("Failed to generate the PDF report", e);
    }
  }

  /**
   * Get the report definition.
   *
   * @param reportDefinitionId the ID uniquely identifying the report definition
   * @return the report definition
   */
  @WebMethod(operationName = "GetReportDefinition")
  @WebResult(name = "ReportDefinition")
  public ReportDefinition getReportDefinition(
      @WebParam(name = "ReportDefinitionId") @XmlElement(required = true) String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException,
          ReportingServiceException {
    return reportingService.getReportDefinition(reportDefinitionId);
  }

  /**
   * Retrieve the name of the report definition.
   *
   * @param reportDefinitionId the ID uniquely identifying the report definition
   * @return the name of report definition
   */
  @WebMethod(operationName = "GetReportDefinitionName")
  @WebResult(name = "ReportDefinitionName")
  public String getReportDefinitionName(
      @WebParam(name = "ReportDefinitionId") @XmlElement(required = true) String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException,
          ReportingServiceException {
    return reportingService.getReportDefinitionName(reportDefinitionId);
  }

  /**
   * Retrieve the report definition summaries.
   *
   * @return the report definition summaries
   */
  @WebMethod(operationName = "GetReportDefinitionSummaries")
  @WebResult(name = "ReportDefinitionSummary")
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
      throws ReportingServiceException {
    return reportingService.getReportDefinitionSummaries();
  }

  /**
   * Retrieve the report definitions.
   *
   * @return the report definitions
   */
  @WebMethod(operationName = "GetReportDefinitions")
  @WebResult(name = "ReportDefinition")
  public List<ReportDefinition> getReportDefinitions() throws ReportingServiceException {
    return reportingService.getReportDefinitions();
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinition the report definition
   */
  @WebMethod(operationName = "UpdateReportDefinition")
  public void updateReportDefinition(
      @WebParam(name = "ReportDefinition") @XmlElement(required = true)
          ReportDefinition reportDefinition)
      throws InvalidArgumentException, ReportDefinitionNotFoundException,
          ReportingServiceException {
    reportingService.updateReportDefinition(reportDefinition);
  }
}

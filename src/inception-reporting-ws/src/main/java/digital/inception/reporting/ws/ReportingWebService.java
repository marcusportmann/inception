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

package digital.inception.reporting.ws;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.reporting.exception.DuplicateReportDefinitionException;
import digital.inception.reporting.exception.ReportDefinitionNotFoundException;
import digital.inception.reporting.model.ReportDefinition;
import digital.inception.reporting.model.ReportDefinitionSummary;
import digital.inception.reporting.model.ReportParameter;
import digital.inception.reporting.service.ReportingService;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * The {@code ReportingWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ReportingService",
    name = "IReportingService",
    targetNamespace = "https://inception.digital/reporting")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ReportingWebService extends AbstractWebServiceBase {

  /** The data source used to provide connections to the application database. */
  private final DataSource dataSource;

  /** The Reporting Service. */
  private final ReportingService reportingService;

  /**
   * Constructs a new {@code ReportingWebService}.
   *
   * @param applicationContext the Spring application context
   * @param dataSource the data source used to provide connections to the application database
   * @param reportingService the Reporting Service
   */
  public ReportingWebService(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource,
      ReportingService reportingService) {
    super(applicationContext);

    this.dataSource = dataSource;
    this.reportingService = reportingService;
  }

  /**
   * Create the report definition.
   *
   * @param reportDefinition the report definition to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateReportDefinitionException if the report definition already exists
   * @throws ServiceUnavailableException if the report definition could not be created
   */
  @WebMethod(operationName = "CreateReportDefinition")
  public void createReportDefinition(
      @WebParam(name = "ReportDefinition") @XmlElement(required = true)
          ReportDefinition reportDefinition)
      throws InvalidArgumentException,
          DuplicateReportDefinitionException,
          ServiceUnavailableException {
    reportingService.createReportDefinition(reportDefinition);
  }

  /**
   * Delete the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the report definition could not be deleted
   */
  @WebMethod(operationName = "DeleteReportDefinition")
  public void deleteReportDefinition(
      @WebParam(name = "ReportDefinitionId") @XmlElement(required = true) String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    reportingService.deleteReportDefinition(reportDefinitionId);
  }

  /**
   * Generate the PDF report.
   *
   * @param reportDefinitionId the ID for the report definition
   * @param reportParameters the report parameters
   * @return the PDF report
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the PDF report could not be generated
   */
  @WebMethod(operationName = "GenerateReport")
  @WebResult(name = "Report")
  public byte[] generateReport(
      @WebParam(name = "ReportDefinitionId") @XmlElement(required = true) String reportDefinitionId,
      @WebParam(name = "ReportParameters") @XmlElement(required = true)
          List<ReportParameter> reportParameters)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
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
      throw new ServiceUnavailableException("Failed to generate the PDF report", e);
    }
  }

  /**
   * Retrieve the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @return the report definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the report definition could not be retrieved
   */
  @WebMethod(operationName = "GetReportDefinition")
  @WebResult(name = "ReportDefinition")
  public ReportDefinition getReportDefinition(
      @WebParam(name = "ReportDefinitionId") @XmlElement(required = true) String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    return reportingService.getReportDefinition(reportDefinitionId);
  }

  /**
   * Retrieve the name of the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @return the name of report definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the name of the report definition could not be retrieved
   */
  @WebMethod(operationName = "GetReportDefinitionName")
  @WebResult(name = "ReportDefinitionName")
  public String getReportDefinitionName(
      @WebParam(name = "ReportDefinitionId") @XmlElement(required = true) String reportDefinitionId)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    return reportingService.getReportDefinitionName(reportDefinitionId);
  }

  /**
   * Retrieve the report definition summaries.
   *
   * @return the report definition summaries
   * @throws ServiceUnavailableException if the report definition summaries could not be retrieved
   */
  @WebMethod(operationName = "GetReportDefinitionSummaries")
  @WebResult(name = "ReportDefinitionSummary")
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
      throws ServiceUnavailableException {
    return reportingService.getReportDefinitionSummaries();
  }

  /**
   * Retrieve the report definitions.
   *
   * @return the report definitions
   * @throws ServiceUnavailableException if the report definitions could not be retrieved
   */
  @WebMethod(operationName = "GetReportDefinitions")
  @WebResult(name = "ReportDefinition")
  public List<ReportDefinition> getReportDefinitions() throws ServiceUnavailableException {
    return reportingService.getReportDefinitions();
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinition the report definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ReportDefinitionNotFoundException if the report definition could not be found
   * @throws ServiceUnavailableException if the report definition could not be updated
   */
  @WebMethod(operationName = "UpdateReportDefinition")
  public void updateReportDefinition(
      @WebParam(name = "ReportDefinition") @XmlElement(required = true)
          ReportDefinition reportDefinition)
      throws InvalidArgumentException,
          ReportDefinitionNotFoundException,
          ServiceUnavailableException {
    reportingService.updateReportDefinition(reportDefinition);
  }
}

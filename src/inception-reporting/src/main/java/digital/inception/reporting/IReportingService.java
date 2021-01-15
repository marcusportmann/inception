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
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IReportingService</code> interface defines the functionality provided by a Reporting
 * Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IReportingService {

  /** The username used to identify operations performed by the system. */
  String SYSTEM_USERNAME = "SYSTEM";

  /**
   * Create the new report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the information
   *     for the new report definition
   */
  void createReportDefinition(ReportDefinition reportDefinition)
      throws InvalidArgumentException, DuplicateReportDefinitionException, ReportingServiceException;

  /**
   * Create the PDF for the report using a connection retrieved from the application data source.
   *
   * @param reportDefinitionId the ID for the report definition
   * @param parameters the parameters for the report
   * @return the PDF data for the report
   */
  byte[] createReportPDF(String reportDefinitionId, Map<String, Object> parameters)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException;

  /**
   * Create the PDF for the report.
   *
   * @param reportDefinitionId the ID for the report definition
   * @param parameters the parameters for the report
   * @param connection the database connection used to retrieve the report data
   * @return the PDF data for the report
   */
  byte[] createReportPDF(
      String reportDefinitionId, Map<String, Object> parameters, Connection connection)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException;

  /**
   * Create the PDF for the report.
   *
   * @param reportDefinitionId the ID for the report definition
   * @param parameters the parameters for the report
   * @param document the XML document containing the report data
   * @return the PDF data for the report
   */
  byte[] createReportPDF(
      String reportDefinitionId, Map<String, Object> parameters, Document document)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException;

  /**
   * Delete the existing report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   */
  void deleteReportDefinition(String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException;

  /**
   * Returns the real path to the folder where the local Jasper reports are stored.
   *
   * @return the real path to the folder where the local Jasper reports are stored
   */
  String getLocalReportFolderPath();

  /**
   * Retrieve the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @return the report definition
   */
  ReportDefinition getReportDefinition(String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException;

  /**
   * Retrieve the name of the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @return the name of the report definition
   */
  String getReportDefinitionName(String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException;

  /**
   * Returns the summaries for all the report definitions.
   *
   * @return the summaries for all the report definitions
   */
  List<ReportDefinitionSummary> getReportDefinitionSummaries() throws ReportingServiceException;

  /**
   * Retrieve the summary for the report definition.
   *
   * @param reportDefinitionId the ID for the report definition
   * @return the summary for the report definition
   */
  ReportDefinitionSummary getReportDefinitionSummary(String reportDefinitionId)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException;

  /**
   * Returns all the report definitions.
   *
   * @return all the report definitions
   */
  List<ReportDefinition> getReportDefinitions() throws ReportingServiceException;

  /**
   * Check whether the report definition exists.
   *
   * @param reportDefinitionId the ID for the report definition
   * @return <code>true</code> if the report definition exists or <code>false</code> otherwise
   */
  boolean reportDefinitionExists(String reportDefinitionId) throws InvalidArgumentException, ReportingServiceException;

  /**
   * Set the real path to the folder where the local Jasper reports are stored.
   *
   * @param localReportFolderPath the real path to the folder where the local Jasper reports are
   *     stored
   */
  void setLocalReportFolderPath(String localReportFolderPath);

  /**
   * Update the report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the updated
   *     information for the report definition
   */
  void updateReportDefinition(ReportDefinition reportDefinition)
      throws InvalidArgumentException, ReportDefinitionNotFoundException, ReportingServiceException;
}

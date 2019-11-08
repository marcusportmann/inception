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

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;

import java.sql.Connection;

import java.util.*;

import javax.sql.DataSource;

/**
 * The <code>ReportingService</code> class provides the Reporting Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class ReportingService
  implements IReportingService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ReportingService.class);

  /**
   * The data source used to provide connections to the application database.
   */
  private DataSource dataSource;

  /* The real path to the folder where the local Jasper reports are stored. */
  private String localReportFolderPath;

  /**
   * The Report Definition Repository.
   */
  private ReportDefinitionRepository reportDefinitionRepository;

  /**
   * The Report Definition Summary Repository.
   */
  private ReportDefinitionSummaryRepository reportDefinitionSummaryRepository;

  /**
   * Constructs a new <code>ReportingService</code>.
   *
   * @param dataSource                        the data source used to provide connections to the
   *                                          application database
   * @param reportDefinitionRepository        the Report Definition Repository
   * @param reportDefinitionSummaryRepository the Report Definition Summary Repository
   */
  public ReportingService(@Qualifier("applicationDataSource") DataSource dataSource,
      ReportDefinitionRepository reportDefinitionRepository,
      ReportDefinitionSummaryRepository reportDefinitionSummaryRepository)
  {
    this.dataSource = dataSource;
    this.reportDefinitionRepository = reportDefinitionRepository;
    this.reportDefinitionSummaryRepository = reportDefinitionSummaryRepository;
  }

  /**
   * Create the new report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the information
   *                         for the new report definition
   */
  @Override
  public void createReportDefinition(ReportDefinition reportDefinition)
    throws DuplicateReportDefinitionException, ReportingServiceException
  {
    try
    {
      if (reportDefinitionRepository.existsById(reportDefinition.getId()))
      {
        throw new DuplicateReportDefinitionException(reportDefinition.getId());
      }

      reportDefinitionRepository.saveAndFlush(reportDefinition);
    }
    catch (DuplicateReportDefinitionException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to create the report definition ("
          + reportDefinition.getId() + ")", e);
    }
  }

  /**
   * Create the PDF for the report using a connection retrieved from the application data source.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   * @param parameters         the parameters for the report
   *
   * @return the PDF data for the report
   */
  @Override
  public byte[] createReportPDF(UUID reportDefinitionId, Map<String, Object> parameters)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return createReportPDF(reportDefinitionId, parameters, connection);
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to create the PDF for the report using the report definition ("
          + reportDefinitionId + ")", e);
    }
  }

  /**
   * Create the PDF for the report.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   * @param parameters         the parameters for the report
   * @param connection         the database connection used to retrieve the report data
   *
   * @return the PDF data for the report
   */
  @Override
  public byte[] createReportPDF(UUID reportDefinitionId, Map<String, Object> parameters,
      Connection connection)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    try
    {
      Optional<ReportDefinition> reportDefinitionOptional = reportDefinitionRepository.findById(
          reportDefinitionId);

      if (reportDefinitionOptional.isEmpty())
      {
        throw new ReportDefinitionNotFoundException(reportDefinitionId);
      }

      Map<String, Object> localParameters = new HashMap<>();

      if (StringUtils.isEmpty(getLocalReportFolderPath()))
      {
        localParameters.put("SUBREPORT_DIR", getLocalReportFolderPath());
      }

      for (String name : parameters.keySet())
      {
        localParameters.put(name, parameters.get(name));
      }

      JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(
          reportDefinitionOptional.get().getTemplate()), localParameters, connection);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to create the PDF for the report using the report definition ("
          + reportDefinitionId + ")", e);
    }
  }

  /**
   * Create the PDF for the report.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   * @param parameters         the parameters for the report
   * @param document           the XML document containing the report data
   *
   * @return the PDF data for the report
   */
  @Override
  public byte[] createReportPDF(UUID reportDefinitionId, Map<String, Object> parameters,
      Document document)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    try
    {
      ReportDefinition reportDefinition = getReportDefinition(reportDefinitionId);

      Map<String, Object> localParameters = new HashMap<>();

      localParameters.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
      localParameters.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
      localParameters.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
      localParameters.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
      localParameters.put(JRParameter.REPORT_LOCALE, Locale.US);

      if (StringUtils.isEmpty(getLocalReportFolderPath()))
      {
        localParameters.put("SUBREPORT_DIR", getLocalReportFolderPath());
      }

      for (String name : parameters.keySet())
      {
        localParameters.put(name, parameters.get(name));
      }

      JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(
          reportDefinition.getTemplate()), localParameters);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to create the PDF for the report using the report definintion ("
          + reportDefinitionId + ")", e);
    }
  }

  /**
   * Delete the existing report definition.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   */
  @Override
  public void deleteReportDefinition(UUID reportDefinitionId)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    try
    {
      if (!reportDefinitionRepository.existsById(reportDefinitionId))
      {
        throw new ReportDefinitionNotFoundException(reportDefinitionId);
      }

      reportDefinitionRepository.deleteById(reportDefinitionId);
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to delete the report definition ("
          + reportDefinitionId + ")", e);
    }
  }

  /**
   * Returns the real path to the folder where the local Jasper reports are stored.
   *
   * @return the real path to the folder where the local Jasper reports are stored
   */
  public String getLocalReportFolderPath()
  {
    return localReportFolderPath;
  }

  /**
   * Returns the number of report definitions.
   *
   * @return the number of report definitions
   */
  @Override
  public long getNumberOfReportDefinitions()
    throws ReportingServiceException
  {
    try
    {
      return reportDefinitionRepository.count();
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to retrieve the number of report definitions", e);
    }
  }

  /**
   * Retrieve the report definition.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   *
   * @return the report definition
   */
  @Override
  public ReportDefinition getReportDefinition(UUID reportDefinitionId)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    try
    {
      Optional<ReportDefinition> reportDefinitionOptional = reportDefinitionRepository.findById(
          reportDefinitionId);

      if (reportDefinitionOptional.isPresent())
      {
        return reportDefinitionOptional.get();
      }
      else
      {
        throw new ReportDefinitionNotFoundException(reportDefinitionId);
      }
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to retrieve the report definition ("
          + reportDefinitionId + ")", e);
    }
  }

  /**
   * Retrieve the name of the report definition.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   *
   * @return the name of the report definition
   */
  @Override
  public String getReportDefinitionName(UUID reportDefinitionId)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    try
    {
      Optional<String> nameOptional = reportDefinitionRepository.getNameById(reportDefinitionId);

      if (nameOptional.isPresent())
      {
        return nameOptional.get();
      }

      throw new ReportDefinitionNotFoundException(reportDefinitionId);
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to retrieve the name for the report definition ("
          + reportDefinitionId + ")", e);
    }

  }

  /**
   * Returns the summaries for all the report definitions.
   *
   * @return the summaries for all the report definitions
   */
  @Override
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
    throws ReportingServiceException
  {
    try
    {
      return reportDefinitionSummaryRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to retrieve the summaries for the report definitions", e);
    }
  }

  /**
   * Retrieve the summary for the report definition.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   *
   * @return the summary for the report definition
   */
  @Override
  public ReportDefinitionSummary getReportDefinitionSummary(UUID reportDefinitionId)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    try
    {
      Optional<ReportDefinitionSummary> reportDefinitionSummaryOptional =
          reportDefinitionSummaryRepository.findById(reportDefinitionId);

      if (reportDefinitionSummaryOptional.isPresent())
      {
        return reportDefinitionSummaryOptional.get();
      }
      else
      {
        throw new ReportDefinitionNotFoundException(reportDefinitionId);
      }
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to retrieve the summary for the report definition (" + reportDefinitionId + ")",
          e);
    }
  }

  /**
   * Returns all the report definitions.
   *
   * @return all the report definitions
   */
  @Override
  public List<ReportDefinition> getReportDefinitions()
    throws ReportingServiceException
  {
    try
    {
      return reportDefinitionRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to retrieve the report definitions", e);
    }
  }

  /**
   * Check whether the report definition exists.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   *
   * @return <code>true</code> if the report definition exists or <code>false</code> otherwise
   */
  @Override
  public boolean reportDefinitionExists(UUID reportDefinitionId)
    throws ReportingServiceException
  {
    try
    {
      return reportDefinitionRepository.existsById(reportDefinitionId);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to check whether the report definition ("
          + reportDefinitionId + ")", e);
    }
  }

  /**
   * Set the real path to the folder where the local Jasper reports are stored.
   *
   * @param localReportFolderPath the real path to the folder where the local Jasper reports are
   *                              stored
   */
  public void setLocalReportFolderPath(String localReportFolderPath)
  {
    this.localReportFolderPath = localReportFolderPath;
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the updated
   *                         information for the report definition
   */
  @Override
  public void updateReportDefinition(ReportDefinition reportDefinition)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    try
    {
      if (!reportDefinitionRepository.existsById(reportDefinition.getId()))
      {
        throw new ReportDefinitionNotFoundException(reportDefinition.getId());
      }

      reportDefinitionRepository.saveAndFlush(reportDefinition);
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to update the report definition ("
          + reportDefinition.getId() + ")", e);
    }
  }
}

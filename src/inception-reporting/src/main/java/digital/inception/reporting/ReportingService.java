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

import org.springframework.util.StringUtils;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
   * Constructs a new <code>ReportingService</code>.
   *
   * @param dataSource the data source used to provide connections to the application database
   */
  public ReportingService(@Qualifier("applicationDataSource") DataSource dataSource)
  {
    this.dataSource = dataSource;
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
    String createReportDefinitionSQL =
        "INSERT INTO reporting.report_definitions (id, name, template) VALUES (?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createReportDefinitionSQL))
    {
      if (reportDefinitionExists(connection, reportDefinition.getId()))
      {
        throw new DuplicateReportDefinitionException(reportDefinition.getId());
      }

      statement.setObject(1, reportDefinition.getId());
      statement.setString(2, reportDefinition.getName());
      statement.setBytes(3, reportDefinition.getTemplate());

      if (statement.executeUpdate() != 1)
      {
        throw new ReportingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createReportDefinitionSQL));
      }
    }
    catch (DuplicateReportDefinitionException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to create the report definition (%s)", reportDefinition.getId()), e);
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
      throw new ReportingServiceException(String.format(
          "Failed to create the PDF for the report using the report definition (%s)",
          reportDefinitionId), e);
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
      ReportDefinition reportDefinition = getReportDefinition(connection, reportDefinitionId);

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
          reportDefinition.getTemplate()), localParameters, connection);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to create the PDF for the report using the report definition (%s)",
          reportDefinitionId), e);
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
      throw new ReportingServiceException(String.format(
          "Failed to create the PDF for the report using the report definintion (%s)",
          reportDefinitionId), e);
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
    String deleteReportDefinitionSQL = "DELETE FROM reporting.report_definitions WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteReportDefinitionSQL))
    {
      statement.setObject(1, reportDefinitionId);

      if (statement.executeUpdate() != 1)
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
      throw new ReportingServiceException(String.format(
          "Failed to delete the report definition (%s)", reportDefinitionId), e);
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
  public int getNumberOfReportDefinitions()
    throws ReportingServiceException
  {
    String getNumberOfReportDefinitionsSQL = "SELECT COUNT(id) FROM reporting.report_definitions";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfReportDefinitionsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new ReportingServiceException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              getNumberOfReportDefinitionsSQL));
        }
      }
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
    try (Connection connection = dataSource.getConnection())
    {
      return getReportDefinition(connection, reportDefinitionId);
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to retrieve the report definition (%s)", reportDefinitionId), e);
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
    String getReportDefinitionSummariesSQL = "SELECT id, name FROM reporting.report_definitions";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getReportDefinitionSummariesSQL))
    {
      List<ReportDefinitionSummary> reportDefinitionSummaries = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          reportDefinitionSummaries.add(getReportDefinitionSummary(rs));
        }
      }

      return reportDefinitionSummaries;
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
    String getReportDefinitionSummaryByIdSQL =
        "SELECT id, name FROM reporting.report_definitions WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getReportDefinitionSummaryByIdSQL))
    {
      statement.setObject(1, reportDefinitionId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getReportDefinitionSummary(rs);
        }
        else
        {
          throw new ReportDefinitionNotFoundException(reportDefinitionId);
        }
      }
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to retrieve the summary for the report definition (%s)", reportDefinitionId), e);
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
    String getReportDefinitionsSQL = "SELECT id, name, template FROM reporting.report_definitions";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getReportDefinitionsSQL))
    {
      List<ReportDefinition> reportDefinitions = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          reportDefinitions.add(getReportDefinition(rs));
        }
      }

      return reportDefinitions;
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
    try (Connection connection = dataSource.getConnection())
    {
      return reportDefinitionExists(connection, reportDefinitionId);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to check whether the report definition (%s)", reportDefinitionId), e);
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
   *
   * @return the updated report definition
   */
  @Override
  public ReportDefinition updateReportDefinition(ReportDefinition reportDefinition)
    throws ReportDefinitionNotFoundException, ReportingServiceException
  {
    String updateReportDefinitionSQL =
        "UPDATE reporting.report_definitions SET name=?, template=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateReportDefinitionSQL))
    {
      statement.setString(1, reportDefinition.getName());
      statement.setBytes(2, reportDefinition.getTemplate());
      statement.setObject(3, reportDefinition.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new ReportDefinitionNotFoundException(reportDefinition.getId());
      }

      return reportDefinition;
    }
    catch (ReportDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to update the report definition (%s)", reportDefinition.getId()), e);
    }
  }

  private ReportDefinition getReportDefinition(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinition(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getBytes(3));
  }

  private ReportDefinition getReportDefinition(Connection connection, UUID reportDefinitionId)
    throws ReportDefinitionNotFoundException, SQLException
  {
    String getReportDefinitionByIdSQL =
        "SELECT id, name, template FROM reporting.report_definitions WHERE id=?";

    try (PreparedStatement statement = connection.prepareStatement(getReportDefinitionByIdSQL))
    {
      statement.setObject(1, reportDefinitionId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getReportDefinition(rs);
        }
        else
        {
          throw new ReportDefinitionNotFoundException(reportDefinitionId);
        }
      }
    }
  }

  private ReportDefinitionSummary getReportDefinitionSummary(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinitionSummary(UUID.fromString(rs.getString(1)), rs.getString(2));
  }

  private boolean reportDefinitionExists(Connection connection, UUID reportDefinitionId)
    throws ReportingServiceException, SQLException
  {
    String reportDefinitionExistsSQL =
        "SELECT COUNT(id) FROM reporting.report_definitions WHERE id=?";

    try (PreparedStatement statement = connection.prepareStatement(reportDefinitionExistsSQL))
    {
      statement.setObject(1, reportDefinitionId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (rs.getInt(1) > 0);
        }
        else
        {
          throw new ReportingServiceException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              reportDefinitionExistsSQL));
        }
      }
    }

  }
}

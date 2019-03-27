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

package digital.inception.error;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

/**
 * The <code>ErrorService</code> class provides the Error Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({ "unused" })
public class ErrorService
  implements IErrorService
{
  /**
   * The data source used to provide connections to the database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Create the error report.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   */
  @Override
  public void createErrorReport(ErrorReport errorReport)
    throws ErrorServiceException
  {
    String createErrorReportSQL = "INSERT INTO error.error_reports (id, application_id, "
        + "application_version, description, detail, created, who, device_id, feedback, data) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createErrorReportSQL))
    {
      String description = errorReport.getDescription();

      if (description.length() > 4000)
      {
        description = description.substring(0, 4000);
      }

      String detail = StringUtil.notNull(errorReport.getDetail());

      if (detail.length() > 4000)
      {
        detail = detail.substring(0, 4000);
      }

      String who = errorReport.getWho();

      if ((who != null) && (who.length() > 1000))
      {
        who = who.substring(0, 1000);
      }

      String deviceId = errorReport.getDeviceId();

      if ((deviceId != null) && (deviceId.length() > 50))
      {
        deviceId = deviceId.substring(0, 50);
      }

      String feedback = errorReport.getFeedback();

      if ((feedback != null) && (feedback.length() > 4000))
      {
        feedback = feedback.substring(0, 4000);
      }

      statement.setObject(1, errorReport.getId());
      statement.setString(2, errorReport.getApplicationId());
      statement.setString(3, errorReport.getApplicationVersion());
      statement.setString(4, description);
      statement.setString(5, detail);

      statement.setTimestamp(6, Timestamp.valueOf(errorReport.getCreated()));

      if (who != null)
      {
        statement.setString(7, who);
      }
      else
      {
        statement.setNull(7, Types.VARCHAR);
      }

      if (deviceId != null)
      {
        statement.setObject(8, deviceId);
      }
      else
      {
        statement.setNull(8, Types.VARCHAR);
      }

      if (feedback != null)
      {
        statement.setString(9, feedback);
      }
      else
      {
        statement.setNull(9, Types.VARCHAR);
      }

      if (errorReport.getData() !=  null)
      {
        statement.setBytes(10, errorReport.getData());
      }
      else
      {
        statement.setNull(10, Types.BLOB);
      }

      if (statement.executeUpdate() != 1)
      {
        throw new ErrorServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createErrorReportSQL));
      }
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException(String.format("Failed to create the error report (%s)",
          errorReport.getId()), e);
    }
  }

  /**
   * Retrieve the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report
   *
   * @return the error report or <code>null</code> if the error report could not be found
   */
  @Override
  public ErrorReport getErrorReport(UUID errorReportId)
    throws ErrorServiceException
  {
    String getErrorReportSQL = "SELECT id, application_id, application_version, description, "
        + "detail, created, who, device_id, feedback, data FROM error.error_reports WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getErrorReportSQL))
    {
      statement.setObject(1, errorReportId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildErrorReportFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException(String.format("Failed to retrieve the error report (%s)",
          errorReportId), e);
    }
  }

  /**
   * Retrieve the summary for the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report
   *
   * @return the summary for the error report or <code>null</code> if the error report could not be
   *         found
   */
  @Override
  public ErrorReportSummary getErrorReportSummary(UUID errorReportId)
    throws ErrorServiceException
  {
    String getErrorReportSummarySQL =
        "SELECT id, application_id, application_version, description, created, who, device_id "
        + "FROM error.error_reports WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getErrorReportSummarySQL))
    {
      statement.setObject(1, errorReportId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildErrorReportSummaryFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException(String.format(
          "Failed to retrieve the summary for the error report (%s)", errorReportId), e);
    }
  }

  /**
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error
   *                               reports to retrieve
   *
   * @return the summaries for the most recent error reports
   */
  @Override
  public List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
    throws ErrorServiceException
  {
    String getMostRecentErrorReportSummariesSQL =
        "SELECT id, application_id, application_version, description, created, who, device_id "
        + "FROM error.error_reports ";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(String.format(
          "%s ORDER BY created DESC FETCH FIRST %d ROWS ONLY",
          getMostRecentErrorReportSummariesSQL, maximumNumberOfEntries)))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<ErrorReportSummary> errorReportSummaries = new ArrayList<>();

        while (rs.next())
        {
          errorReportSummaries.add(buildErrorReportSummaryFromResultSet(rs));
        }

        return errorReportSummaries;
      }
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException(
          "Failed to retrieve the summaries for the most recent error reports", e);
    }
  }

  /**
   * Returns the total number of error reports.
   *
   * @return the total number of error reports
   */
  @Override
  public int getNumberOfErrorReports()
    throws ErrorServiceException
  {
    String getNumberOfErrorReportsSQL = "SELECT COUNT(id) FROM error.error_reports";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfErrorReportsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
      }
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException("Failed to retrieve the total number of error reports", e);
    }
  }

  private ErrorReport buildErrorReportFromResultSet(ResultSet rs)
    throws SQLException
  {
    byte[] data = rs.getBytes(10);

    return new ErrorReport(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3),
        rs.getString(4), rs.getString(5), rs.getTimestamp(6).toLocalDateTime(), rs.getString(7),
        rs.getString(8), rs.getString(9), (data == null)
        ? new byte[0]
        : data);
  }

  private ErrorReportSummary buildErrorReportSummaryFromResultSet(ResultSet rs)
    throws SQLException
  {
    return new ErrorReportSummary(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(
        3), rs.getString(4), rs.getTimestamp(5).toLocalDateTime(), rs.getString(6), rs.getString(
        7));
  }
}

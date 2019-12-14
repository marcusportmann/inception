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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

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
   * The Error Report Repository.
   */
  private ErrorReportRepository errorReportRepository;

  /**
   * The Error Report Summary Repository.
   */
  private ErrorReportSummaryRepository errorReportSummaryRepository;

  /**
   * Constructs a new <code>ErrorService</code>.
   *
   * @param errorReportRepository the Error Report Repository
   * @param errorReportSummaryRepository the Error Report Summary Repository
   */
  public ErrorService(ErrorReportRepository errorReportRepository,
      ErrorReportSummaryRepository errorReportSummaryRepository)
  {
    this.errorReportRepository = errorReportRepository;
    this.errorReportSummaryRepository = errorReportSummaryRepository;
  }

  /**
   * Create the error report.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   */
  @Override
  @Transactional
  public void createErrorReport(ErrorReport errorReport)
    throws ErrorServiceException
  {
    try
    {
      String description = errorReport.getDescription();

      if (description.length() > 4000)
      {
        description = description.substring(0, 4000);
      }

      errorReport.setDescription(description);

      String detail = StringUtils.isEmpty(errorReport.getDetail())
          ? ""
          : errorReport.getDetail();

      if (detail.length() > 4000)
      {
        detail = detail.substring(0, 4000);
      }

      errorReport.setDetail(detail);

      String who = errorReport.getWho();

      if ((who != null) && (who.length() > 1000))
      {
        who = who.substring(0, 1000);
      }

      errorReport.setWho(who);

      String feedback = errorReport.getFeedback();

      if ((feedback != null) && (feedback.length() > 4000))
      {
        feedback = feedback.substring(0, 4000);
      }

      errorReport.setFeedback(feedback);

      errorReportRepository.saveAndFlush(errorReport);
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException("Failed to create the error report (" + errorReport.getId()
          + ")", e);
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
    throws ErrorReportNotFoundException, ErrorServiceException
  {
    try
    {
      Optional<ErrorReport> errorReportOptional = errorReportRepository.findById(errorReportId);

      if (errorReportOptional.isPresent())
      {
        return errorReportOptional.get();
      }
      else
      {
        throw new ErrorReportNotFoundException(errorReportId);
      }
    }
    catch (ErrorReportNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException("Failed to retrieve the error report (" + errorReportId
          + ")", e);
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
    throws ErrorReportNotFoundException, ErrorServiceException
  {
    try
    {
      Optional<ErrorReportSummary> errorReportSummaryOptional =
          errorReportSummaryRepository.findById(errorReportId);

      if (errorReportSummaryOptional.isPresent())
      {
        return errorReportSummaryOptional.get();
      }
      else
      {
        throw new ErrorReportNotFoundException(errorReportId);
      }
    }
    catch (ErrorReportNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException("Failed to retrieve the summary for the error report ("
          + errorReportId + ")", e);
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
    try
    {
      Pageable pageable = PageRequest.of(0, maximumNumberOfEntries, Sort.Direction.DESC, "created");

      Page<ErrorReportSummary> errorReportSummaryPage = errorReportSummaryRepository.findAll(
          pageable);

      return errorReportSummaryPage.getContent();
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
  public long getNumberOfErrorReports()
    throws ErrorServiceException
  {
    try
    {
      return errorReportRepository.count();
    }
    catch (Throwable e)
    {
      throw new ErrorServiceException("Failed to retrieve the total number of error reports", e);
    }
  }
}

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

package digital.inception.error;

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.InvalidArgumentException;
import java.util.List;
import java.util.UUID;

/**
 * The <b>IErrorService</b> interface defines the functionality provided by an Error Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface IErrorService {

  /**
   * Create the new entry for the error report in the database.
   *
   * @param errorReport the <b>ErrorReport</b> instance containing the information for the error
   *     report
   */
  void createErrorReport(ErrorReport errorReport)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) for the error report
   * @return the error report or <b>null</b> if the error report could not be found
   */
  ErrorReport getErrorReport(UUID errorReportId)
      throws InvalidArgumentException, ErrorReportNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summary for the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) for the error report
   * @return the summary for the error report or <b>null</b> if the error report could not be found
   */
  ErrorReportSummary getErrorReportSummary(UUID errorReportId)
      throws InvalidArgumentException, ErrorReportNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error reports
   *     to retrieve
   * @return the summaries for the most recent error reports
   */
  List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
      throws InvalidArgumentException, ServiceUnavailableException;
}

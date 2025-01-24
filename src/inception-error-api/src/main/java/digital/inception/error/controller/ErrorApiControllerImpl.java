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

package digital.inception.error.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.error.model.ErrorReport;
import digital.inception.error.model.ErrorReportNotFoundException;
import digital.inception.error.model.ErrorReportSortBy;
import digital.inception.error.model.ErrorReportSummaries;
import digital.inception.error.service.ErrorService;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>ErrorApiControllerImpl</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class ErrorApiControllerImpl extends SecureApiController implements ErrorApiController {

  /** The Error Service. */
  private final ErrorService errorService;

  /**
   * Constructs a new <b>ErrorApiControllerImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param errorService the Error Service
   */
  public ErrorApiControllerImpl(ApplicationContext applicationContext, ErrorService errorService) {
    super(applicationContext);

    this.errorService = errorService;
  }

  @Override
  public void createErrorReport(ErrorReport errorReport)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (errorReport == null) {
      throw new InvalidArgumentException("errorReport");
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      errorReport.setWho(authentication.getName());
    }

    errorService.createErrorReport(errorReport);
  }

  @Override
  public ErrorReport getErrorReport(UUID errorReportId)
      throws InvalidArgumentException, ErrorReportNotFoundException, ServiceUnavailableException {
    return errorService.getErrorReport(errorReportId);
  }

  @Override
  public ErrorReportSummaries getErrorReportSummaries(
      String filter,
      LocalDate fromDate,
      LocalDate toDate,
      ErrorReportSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return errorService.getErrorReportSummaries(
        filter, fromDate, toDate, sortBy, sortDirection, pageIndex, pageSize);
  }
}

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

package digital.inception.error.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.error.model.ErrorReport;
import digital.inception.error.model.ErrorReportNotFoundException;
import digital.inception.error.model.ErrorReportSortBy;
import digital.inception.error.model.ErrorReportSummaries;
import digital.inception.error.model.ErrorReportSummary;
import digital.inception.error.persistence.jpa.ErrorReportRepository;
import digital.inception.error.persistence.jpa.ErrorReportSummaryRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The <b>ErrorServiceImpl</b> class provides the Error Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused"})
public class ErrorServiceImpl extends AbstractServiceBase implements ErrorService {

  /** The maximum number of filtered error report summaries. */
  private static final int MAX_FILTERED_ERROR_REPORT_SUMMARIES = 100;

  /** The Error Report Repository. */
  private final ErrorReportRepository errorReportRepository;

  /** The Error Report Summary Repository. */
  private final ErrorReportSummaryRepository errorReportSummaryRepository;

  /**
   * Constructs a new <b>ErrorServiceImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param errorReportRepository the Error Report Repository
   * @param errorReportSummaryRepository the Error Report Summary Repository
   */
  public ErrorServiceImpl(
      ApplicationContext applicationContext,
      ErrorReportRepository errorReportRepository,
      ErrorReportSummaryRepository errorReportSummaryRepository) {
    super(applicationContext);

    this.errorReportRepository = errorReportRepository;
    this.errorReportSummaryRepository = errorReportSummaryRepository;
  }

  @Override
  public void createErrorReport(ErrorReport errorReport)
      throws InvalidArgumentException, ServiceUnavailableException {
    // Truncate the detail if required
    if ((errorReport.getDetail() != null)
        && (errorReport.getDetail().length() > ErrorReport.MAX_DETAIL_SIZE)) {
      errorReport.setDetail(
          errorReport.getDetail().substring(0, ErrorReport.MAX_DETAIL_SIZE - 3) + "...");
    }

    validateArgument("errorReport", errorReport);

    try {
      String description = errorReport.getDescription();

      if (description.length() > 2000) {
        description = description.substring(0, 2000);
      }

      errorReport.setDescription(description);

      String detail = StringUtils.hasText(errorReport.getDetail()) ? errorReport.getDetail() : "";

      if (detail.length() > ErrorReport.MAX_DETAIL_SIZE) {
        detail = detail.substring(0, ErrorReport.MAX_DETAIL_SIZE);
      }

      errorReport.setDetail(detail);

      String who = errorReport.getWho();

      if ((who != null) && (who.length() > 1000)) {
        who = who.substring(0, 1000);
      }

      errorReport.setWho(who);

      String feedback = errorReport.getFeedback();

      if ((feedback != null) && (feedback.length() > 2000)) {
        feedback = feedback.substring(0, 2000);
      }

      errorReport.setFeedback(feedback);

      errorReportRepository.saveAndFlush(errorReport);

      if (inDebugMode()) {
        ObjectMapper objectMapper = getApplicationContext().getBean(ObjectMapper.class);

        if (objectMapper != null) {
          log.info(
              "Error Report: "
                  + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorReport));
        }
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the error report (" + errorReport.getId() + ")", e);
    }
  }

  @Override
  public ErrorReport getErrorReport(UUID errorReportId)
      throws InvalidArgumentException, ErrorReportNotFoundException, ServiceUnavailableException {
    if (errorReportId == null) {
      throw new InvalidArgumentException("errorReportId");
    }

    try {
      Optional<ErrorReport> errorReportOptional = errorReportRepository.findById(errorReportId);

      if (errorReportOptional.isPresent()) {
        return errorReportOptional.get();
      } else {
        throw new ErrorReportNotFoundException(errorReportId);
      }
    } catch (ErrorReportNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the error report (" + errorReportId + ")", e);
    }
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
    if (fromDate == null) {
      fromDate = LocalDate.now().minusMonths(1);
    }

    if (toDate == null) {
      toDate = LocalDate.now();
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = ErrorReportSortBy.CREATED;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.DESCENDING;
    }

    try {
      PageRequest pageRequest;

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = MAX_FILTERED_ERROR_REPORT_SUMMARIES;
      }

      String sortProperty;
      if (sortBy == ErrorReportSortBy.WHO) {
        sortProperty = "who";
      } else {
        sortProperty = "created";
      }

      pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_ERROR_REPORT_SUMMARIES),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              sortProperty);

      final OffsetDateTime fromOffsetDateTime =
          fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime();

      final OffsetDateTime toOffsetDateTime =
          toDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime();

      Page<ErrorReportSummary> errorReportSummaryPage =
          errorReportSummaryRepository.findAll(
              (Specification<ErrorReportSummary>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                            root.get("created"), fromOffsetDateTime));

                    predicates.add(criteriaBuilder.lessThan(root.get("created"), toOffsetDateTime));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.like(
                              criteriaBuilder.lower(root.get("who")),
                              "%" + filter.toLowerCase() + "%"));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new ErrorReportSummaries(
          errorReportSummaryPage.toList(),
          errorReportSummaryPage.getTotalElements(),
          filter,
          fromDate,
          toDate,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered error report summaries", e);
    }
  }

  @Override
  public ErrorReportSummary getErrorReportSummary(UUID errorReportId)
      throws InvalidArgumentException, ErrorReportNotFoundException, ServiceUnavailableException {
    if (errorReportId == null) {
      throw new InvalidArgumentException("errorReportId");
    }

    try {
      Optional<ErrorReportSummary> errorReportSummaryOptional =
          errorReportSummaryRepository.findById(errorReportId);

      if (errorReportSummaryOptional.isPresent()) {
        return errorReportSummaryOptional.get();
      } else {
        throw new ErrorReportNotFoundException(errorReportId);
      }
    } catch (ErrorReportNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summary for the error report (" + errorReportId + ")", e);
    }
  }

  @Override
  public List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (maximumNumberOfEntries < 0) {
      throw new InvalidArgumentException("maximumNumberOfEntries");
    }

    try {
      PageRequest pageRequest =
          PageRequest.of(0, maximumNumberOfEntries, Sort.Direction.DESC, "created");

      Page<ErrorReportSummary> errorReportSummaryPage =
          errorReportSummaryRepository.findAll(pageRequest);

      return errorReportSummaryPage.getContent();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the most recent error reports", e);
    }
  }
}

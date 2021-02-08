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

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ValidationError;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The <b>ErrorService</b> class provides the Error Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused"})
public class ErrorService implements IErrorService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ErrorService.class);

  /** The Error Report Repository. */
  private final ErrorReportRepository errorReportRepository;

  /** The Error Report Summary Repository. */
  private final ErrorReportSummaryRepository errorReportSummaryRepository;

  /** The Jackson ObjectMapper. */
  private final ObjectMapper objectMapper;

  /** The JSR-303 validator. */
  private final Validator validator;

  /* Is debugging enabled for the Inception Framework? */
  @Value("${inception.debug:#{false}}")
  private boolean debug;

  /**
   * Constructs a new <b>ErrorService</b>.
   *
   * @param validator the JSR-303 validator
   * @param objectMapper the Jackson ObjectMapper
   * @param errorReportRepository the Error Report Repository
   * @param errorReportSummaryRepository the Error Report Summary Repository
   */
  public ErrorService(
      Validator validator,
      ObjectMapper objectMapper,
      ErrorReportRepository errorReportRepository,
      ErrorReportSummaryRepository errorReportSummaryRepository) {
    this.validator = validator;
    this.objectMapper = objectMapper;
    this.errorReportRepository = errorReportRepository;
    this.errorReportSummaryRepository = errorReportSummaryRepository;
  }

  /**
   * Create the new error report.
   *
   * @param errorReport the <b>ErrorReport</b> instance containing the information for the error
   *     report
   */
  @Override
  @Transactional
  public void createErrorReport(ErrorReport errorReport)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (errorReport == null) {
      throw new InvalidArgumentException("errorReport");
    }

    // Truncate the detail if required
    if ((errorReport.getDetail() != null)
        && (errorReport.getDetail().length() > ErrorReport.MAX_DETAIL_SIZE)) {
      errorReport.setDetail(
          errorReport.getDetail().substring(0, ErrorReport.MAX_DETAIL_SIZE - 3) + "...");
    }

    Set<ConstraintViolation<ErrorReport>> constraintViolations = validator.validate(errorReport);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "errorReport", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      String description = errorReport.getDescription();

      if (description.length() > 4000) {
        description = description.substring(0, 4000);
      }

      errorReport.setDescription(description);

      String detail = StringUtils.hasText(errorReport.getDetail()) ? errorReport.getDetail() : "";

      if (detail.length() > 4000) {
        detail = detail.substring(0, 4000);
      }

      errorReport.setDetail(detail);

      String who = errorReport.getWho();

      if ((who != null) && (who.length() > 1000)) {
        who = who.substring(0, 1000);
      }

      errorReport.setWho(who);

      String feedback = errorReport.getFeedback();

      if ((feedback != null) && (feedback.length() > 4000)) {
        feedback = feedback.substring(0, 4000);
      }

      errorReport.setFeedback(feedback);

      errorReportRepository.saveAndFlush(errorReport);

      if (debug) {
        logger.info(
            "Error Report: "
                + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorReport));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the error report (" + errorReport.getId() + ")", e);
    }
  }

  /**
   * Retrieve the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) for the error report
   * @return the error report or <b>null</b> if the error report could not be found
   */
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

  /**
   * Retrieve the summary for the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) for the error report
   * @return the summary for the error report or <b>null</b> if the error report could not be found
   */
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

  /**
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error reports
   *     to retrieve
   * @return the summaries for the most recent error reports
   */
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

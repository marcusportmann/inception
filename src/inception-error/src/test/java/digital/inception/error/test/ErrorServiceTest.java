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

package digital.inception.error.test;

import static digital.inception.test.Assert.assertEqualsToMillisecond;
import static org.junit.Assert.assertEquals;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.Base64Util;
import digital.inception.error.ErrorReport;
import digital.inception.error.ErrorReportSortBy;
import digital.inception.error.ErrorReportSummaries;
import digital.inception.error.ErrorReportSummary;
import digital.inception.error.IErrorService;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>ErrorServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>ErrorService</b> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ErrorServiceTest {

  /** The Error Service. */
  @Autowired private IErrorService errorService;

  private static synchronized ErrorReport getTestErrorReport() {
    return new ErrorReport(
        UuidCreator.getShortPrefixComb(),
        "ApplicationId",
        "ApplicationVersion",
        "Description",
        "Detail",
        LocalDateTime.now(),
        "Who",
        UuidCreator.getShortPrefixComb(),
        "Feedback",
        Base64Util.encodeBytes("Data".getBytes()));
  }

  /** Test the error report functionality. */
  @Test
  public void errorReportTest() throws Exception {
    ErrorReport errorReport = getTestErrorReport();

    errorService.createErrorReport(errorReport);

    ErrorReport retrievedErrorReport = errorService.getErrorReport(errorReport.getId());

    compareErrorReports(errorReport, retrievedErrorReport);

    ErrorReportSummary retrievedErrorReportSummary =
        errorService.getErrorReportSummary(errorReport.getId());

    compareErrorReportAndErrorReportSummary(errorReport, retrievedErrorReportSummary);

    List<ErrorReportSummary> errorReportSummaries =
        errorService.getMostRecentErrorReportSummaries(1000);

    assertEquals(
        "The number of error report summaries is incorrect", 1, errorReportSummaries.size());

    compareErrorReportAndErrorReportSummary(errorReport, errorReportSummaries.get(0));

    ErrorReportSummaries filteredErrorReportSummaries =
        errorService.getErrorReportSummaries(
            errorReport.getWho(), ErrorReportSortBy.WHO, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The number of filtered error report summaries is incorrect",
        1,
        (long) filteredErrorReportSummaries.getTotal());

    compareErrorReportAndErrorReportSummary(
        errorReport, filteredErrorReportSummaries.getErrorReportSummaries().get(0));
  }

  /** Test the functionality to retrieve the most recent error report summaries. */
  @Test
  public void getMostRecentErrorReportSummariesTest() throws Exception {
    List<ErrorReport> errorReports = new ArrayList<>();

    for (int i = 0; i < 20; i++) {
      ErrorReport errorReport = getTestErrorReport();

      errorService.createErrorReport(errorReport);

      errorReports.add(errorReport);

      Thread.sleep(10L);
    }

    List<ErrorReportSummary> errorReportSummaries =
        errorService.getMostRecentErrorReportSummaries(10);

    assertEquals(
        "The number of error report summaries is incorrect", 10, errorReportSummaries.size());

    for (int i = 0; i < 10; i++) {
      assertEquals(
          "The error report summary does not match the error report",
          errorReportSummaries.get(i).getId(),
          errorReports.get((errorReports.size() - 1) - i).getId());
    }
  }

  private void compareErrorReportAndErrorReportSummary(
      ErrorReport errorReport, ErrorReportSummary errorReportSummary) {
    assertEquals(
        "The ID values for the error reports do not match",
        errorReport.getId(),
        errorReportSummary.getId());
    assertEquals(
        "The application ID values for the error reports do not match",
        errorReport.getApplicationId(),
        errorReportSummary.getApplicationId());
    assertEquals(
        "The application version values for the error reports do not match",
        errorReport.getApplicationVersion(),
        errorReportSummary.getApplicationVersion());
    assertEquals(
        "The description values for the error reports do not match",
        errorReport.getDescription(),
        errorReportSummary.getDescription());
    assertEqualsToMillisecond(
        "The created values for the error reports do not match",
        errorReport.getCreated(),
        errorReportSummary.getCreated());
    assertEquals(
        "The who values for the error reports do not match",
        errorReport.getWho(),
        errorReportSummary.getWho());
    assertEquals(
        "The device ID values for the error reports do not match",
        errorReport.getDeviceId(),
        errorReportSummary.getDeviceId());
  }

  private void compareErrorReports(ErrorReport errorReport1, ErrorReport errorReport2) {
    assertEquals(
        "The ID values for the error reports do not match",
        errorReport1.getId(),
        errorReport2.getId());
    assertEquals(
        "The application ID values for the error reports do not match",
        errorReport1.getApplicationId(),
        errorReport2.getApplicationId());
    assertEquals(
        "The application version values for the error reports do not match",
        errorReport1.getApplicationVersion(),
        errorReport2.getApplicationVersion());
    assertEquals(
        "The description values for the error reports do not match",
        errorReport1.getDescription(),
        errorReport2.getDescription());
    assertEquals(
        "The detail values for the error reports do not match",
        errorReport1.getDetail(),
        errorReport2.getDetail());
    assertEqualsToMillisecond(
        "The created values for the error reports do not match",
        errorReport1.getCreated(),
        errorReport2.getCreated());
    assertEquals(
        "The who values for the error reports do not match",
        errorReport1.getWho(),
        errorReport2.getWho());
    assertEquals(
        "The device ID values for the error reports do not match",
        errorReport1.getDeviceId(),
        errorReport2.getDeviceId());
    assertEquals(
        "The feedback values for the error reports do not match",
        errorReport1.getFeedback(),
        errorReport2.getFeedback());
    assertEquals(
        "The data values for the error reports do not match",
        errorReport1.getData(),
        errorReport2.getData());
  }
}

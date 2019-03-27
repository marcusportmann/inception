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

import digital.inception.codes.CodeCategory;
import digital.inception.core.util.ResourceUtil;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The <code>ErrorServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>ErrorService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ErrorServiceTest
{
  /**
   * The Error Service.
   */
  @Autowired
  private IErrorService errorService;

  /**
   * Test the error report functionality.
   */
  @Test
  public void errorReportTest()
    throws Exception
  {
    ErrorReport errorReport = getTestErrorReport();

    errorService.createErrorReport(errorReport);

    ErrorReport retrievedErrorReport = errorService.getErrorReport(errorReport.getId());

    compareErrorReports(errorReport, retrievedErrorReport);

    ErrorReportSummary retrievedErrorReportSummary = errorService.getErrorReportSummary(errorReport.getId());

    compareErrorReportAndErrorReportSummary(errorReport, retrievedErrorReportSummary);

    assertEquals("The number of error reports is incorrect", 1, errorService.getNumberOfErrorReports());

    List<ErrorReportSummary> errorReportSummaries = errorService.getMostRecentErrorReportSummaries(1000);

    assertEquals("The number of error report summaries is incorrect", 1, errorReportSummaries.size());

    compareErrorReportAndErrorReportSummary(errorReport, errorReportSummaries.get(0));
  }

  private static synchronized ErrorReport getTestErrorReport()
  {
    ErrorReport errorReport = new ErrorReport(UUID.randomUUID(), "ApplicationId", "ApplicationVersion",
      "Description", "Detail", LocalDateTime.now(), "Who", "DeviceId", "Feedback", "Data".getBytes() );

    return errorReport;
  }

  private void compareErrorReports(ErrorReport errorReport1, ErrorReport errorReport2)
  {
    assertEquals("The ID values for the two error reports do not match", errorReport1.getId(),
      errorReport2.getId());
    assertEquals("The application ID values for the two error reports do not match", errorReport1.getApplicationId(),
      errorReport2.getApplicationId());
    assertEquals("The application version values for the two error reports do not match", errorReport1.getApplicationVersion(),
      errorReport2.getApplicationVersion());
    assertEquals("The description values for the two error reports do not match", errorReport1.getDescription(),
      errorReport2.getDescription());
    assertEquals("The detail values for the two error reports do not match", errorReport1.getDetail(),
      errorReport2.getDetail());
    assertEquals("The created values for the two error reports do not match", errorReport1.getCreated(),
      errorReport2.getCreated());
    assertEquals("The who values for the two error reports do not match", errorReport1.getWho(),
      errorReport2.getWho());
    assertEquals("The device ID values for the two error reports do not match", errorReport1.getDeviceId(),
      errorReport2.getDeviceId());
    assertEquals("The feedback values for the two error reports do not match", errorReport1.getFeedback(),
      errorReport2.getFeedback());
    assertArrayEquals("The data values for the two error reports do not match", errorReport1.getData(), errorReport2.getData());
  }

  private void compareErrorReportAndErrorReportSummary(ErrorReport errorReport, ErrorReportSummary errorReportSummary)
  {
    assertEquals("The ID values for the two error reports do not match", errorReport.getId(),
      errorReportSummary.getId());
    assertEquals("The application ID values for the two error reports do not match", errorReport.getApplicationId(),
      errorReportSummary.getApplicationId());
    assertEquals("The application version values for the two error reports do not match", errorReport.getApplicationVersion(),
      errorReportSummary.getApplicationVersion());
    assertEquals("The description values for the two error reports do not match", errorReport.getDescription(),
      errorReportSummary.getDescription());
    assertEquals("The created values for the two error reports do not match", errorReport.getCreated(),
      errorReportSummary.getCreated());
    assertEquals("The who values for the two error reports do not match", errorReport.getWho(),
      errorReportSummary.getWho());
    assertEquals("The device ID values for the two error reports do not match", errorReport.getDeviceId(),
      errorReportSummary.getDeviceId());
  }
}
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

package digital.inception.reporting.test;

// ~--- non-JDK imports --------------------------------------------------------

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import digital.inception.core.util.ResourceUtil;
import digital.inception.reporting.IReportingService;
import digital.inception.reporting.ReportDefinition;
import digital.inception.reporting.ReportDefinitionNotFoundException;
import digital.inception.reporting.ReportDefinitionSummary;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportingServiceTest</code> class contains the implementation of the JUnit tests for
 * the <code>ReportingService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ReportingServiceTest {

  private static int reportDefinitionCount;

  /** The data source used to provide connections to the application database. */
  @Autowired private DataSource dataSource;

  /** The Reporting Service. */
  @Autowired private IReportingService reportingService;

  private static synchronized ReportDefinition getTestReportDefinitionDetails() {
    reportDefinitionCount++;

    byte[] testReportTemplate =
        ResourceUtil.getClasspathResource("digital/inception/reporting/test/TestReport.jasper");

    ReportDefinition reportDefinition = new ReportDefinition();
    reportDefinition.setId("TestReport" + reportDefinitionCount);
    reportDefinition.setName("Test Report Definition " + reportDefinitionCount);
    reportDefinition.setTemplate(testReportTemplate);

    return reportDefinition;
  }

  /** Test the create report PDF functionality. */
  @Test
  public void createReportPDFTest() throws Exception {
    ReportDefinition reportDefinition = getTestReportDefinitionDetails();

    reportingService.createReportDefinition(reportDefinition);

    Map<String, Object> parameters = new HashMap<>();

    try (Connection connection = dataSource.getConnection()) {
      reportingService.createReportPDF(reportDefinition.getId(), parameters, connection);
    }

    reportingService.deleteReportDefinition(reportDefinition.getId());
  }

  /** Test the report definition functionality. */
  @Test
  public void reportDefinitionTest() throws Exception {
    ReportDefinition reportDefinition = getTestReportDefinitionDetails();

    reportingService.createReportDefinition(reportDefinition);

    ReportDefinition retrievedReportDefinition =
        reportingService.getReportDefinition(reportDefinition.getId());

    compareReportDefinitions(reportDefinition, retrievedReportDefinition);

    boolean reportDefinitionExists =
        reportingService.reportDefinitionExists(reportDefinition.getId());

    assertTrue("The report definition does not exist", reportDefinitionExists);

    String retrievedReportDefinitionName =
        reportingService.getReportDefinitionName(reportDefinition.getId());

    assertEquals(
        "The correct report definition name was not retrieved",
        reportDefinition.getName(),
        retrievedReportDefinitionName);

    reportDefinition.setName("Updated " + reportDefinition.getName());

    reportingService.updateReportDefinition(reportDefinition);

    retrievedReportDefinition = reportingService.getReportDefinition(reportDefinition.getId());

    compareReportDefinitions(reportDefinition, retrievedReportDefinition);

    reportDefinitionExists = reportingService.reportDefinitionExists(reportDefinition.getId());

    assertTrue("The updated report definition does not exist", reportDefinitionExists);

    List<ReportDefinition> reportDefinitions = reportingService.getReportDefinitions();

    assertEquals(
        "The correct number of report definitions was not retrieved", 1, reportDefinitions.size());

    compareReportDefinitions(reportDefinition, reportDefinitions.get(0));

    ReportDefinitionSummary retrievedReportDefinitionSummary =
        reportingService.getReportDefinitionSummary(reportDefinition.getId());

    compareReportDefinitionToReportDefinitionSummary(
        reportDefinition, retrievedReportDefinitionSummary);

    List<ReportDefinitionSummary> reportDefinitionSummaries =
        reportingService.getReportDefinitionSummaries();

    assertEquals(
        "The correct number of report definition summaries was not retrieved",
        1,
        reportDefinitionSummaries.size());

    compareReportDefinitionToReportDefinitionSummary(
        reportDefinition, reportDefinitionSummaries.get(0));

    reportingService.deleteReportDefinition(reportDefinition.getId());

    try {
      reportingService.getReportDefinition(reportDefinition.getId());

      fail("The report definition that should have been deleted was retrieved successfully");
    } catch (ReportDefinitionNotFoundException ignored) {
    }
  }

  private void compareReportDefinitionToReportDefinitionSummary(
      ReportDefinition reportDefinition, ReportDefinitionSummary reportDefinitionSummary) {
    assertEquals(
        "The ID values for the two report definition summaries do not match",
        reportDefinition.getId(),
        reportDefinitionSummary.getId());
    assertEquals(
        "The name values for the two report definition summaries do not match",
        reportDefinition.getName(),
        reportDefinitionSummary.getName());
  }

  private void compareReportDefinitions(
      ReportDefinition reportDefinition1, ReportDefinition reportDefinition2) {
    assertEquals(
        "The ID values for the two report definitions do not match",
        reportDefinition1.getId(),
        reportDefinition2.getId());
    assertEquals(
        "The name values for the two report definitions do not match",
        reportDefinition1.getName(),
        reportDefinition2.getName());
    assertArrayEquals(
        "The template values for the two report definitions do not match",
        reportDefinition1.getTemplate(),
        reportDefinition2.getTemplate());
  }
}

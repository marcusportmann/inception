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

package digital.inception.sample;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.application.Application;
import digital.inception.core.util.ResourceUtil;
import digital.inception.reporting.IReportingService;
import digital.inception.reporting.ReportDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleApplication</code> provides the implementation of the Inception Framework
 * application class for the sample application.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
@ComponentScan(basePackages = {"digital.inception"})
@EnableJpaRepositories(
    entityManagerFactoryRef = "applicationPersistenceUnit",
    basePackages = {"digital.inception.sample"})
public class SampleApplication extends Application implements InitializingBean {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

  /** The Reporting Service. */
  private final IReportingService reportingService;

  /**
   * Constructs a new <code>SampleApplication</code>.
   *
   * @param applicationContext the Spring application context
   * @param reportingService the Reporting Service
   */
  public SampleApplication(
      ApplicationContext applicationContext, IReportingService reportingService) {
    super(applicationContext);

    this.reportingService = reportingService;
  }

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }

  /** Initialize the sample application. */
  @Override
  public void afterPropertiesSet() {
    try {
      byte[] sampleReportDefinitionData =
          ResourceUtil.getClasspathResource("digital/inception/sample/SampleReport.jasper");

      ReportDefinition sampleReportDefinition =
          new ReportDefinition(
              "Inception.Sample.SampleReport", "Sample Report", sampleReportDefinitionData);

      if (!reportingService.reportDefinitionExists(sampleReportDefinition.getId())) {
        reportingService.createReportDefinition(sampleReportDefinition);
        logger.info("Saved the \"Sample Report\" report definition");
      }
    } catch (Throwable e) {
      throw new RuntimeException("Failed to initialize the Sample application", e);
    }
  }
}

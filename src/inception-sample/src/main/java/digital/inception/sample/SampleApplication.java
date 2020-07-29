/*
 * Copyright 2020 Marcus Portmann
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
import digital.inception.codes.ICodesService;
import digital.inception.configuration.IConfigurationService;
import digital.inception.core.util.ResourceUtil;
import digital.inception.error.IErrorService;
import digital.inception.mail.IMailService;
import digital.inception.reporting.IReportingService;
import digital.inception.reporting.ReportDefinition;
import digital.inception.sample.model.ISampleService;
import digital.inception.scheduler.ISchedulerService;
import digital.inception.security.ISecurityService;
import javax.sql.DataSource;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// import springfox.documentation.builders.PathSelectors;
// import springfox.documentation.builders.RequestHandlerSelectors;
// import springfox.documentation.service.ApiInfo;
// import springfox.documentation.service.Contact;
// import springfox.documentation.spi.DocumentationType;
// import springfox.documentation.spring.web.plugins.Docket;
// import springfox.documentation.swagger2.annotations.EnableSwagger2;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleApplication</code> provides the implementation of the Wicket Web Application
 * class for the sample application.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
@ComponentScan(
    basePackages = {"digital.inception"},
    lazyInit = false)
@EnableJpaRepositories(
    entityManagerFactoryRef = "applicationPersistenceUnit",
    basePackages = {"digital.inception.sample"})
public class SampleApplication extends Application implements InitializingBean {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

  /** The Codes Service. */
  private ICodesService codesService;

  /** The Configuration Service. */
  private IConfigurationService configurationService;

  /** The data source used to provide connections to the application database. */
  private DataSource dataSource;

  /** The Error Service. */
  private IErrorService errorService;

  /** The Mail Service. */
  private IMailService mailService;

  /** The Reporting Service. */
  private IReportingService reportingService;

  /** The Sample Service. */
  private ISampleService sampleService;

  /** The Scheduler Service. */
  private ISchedulerService schedulerService;

  /** The Security Service. */
  private ISecurityService securityService;

  /** The JSR-303 validator. */
  private Validator validator;

  /**
   * Constructs a new <code>SampleApplication</code>.
   *
   * @param applicationContext the Spring application context
   * @param dataSource the data source used to provide connections to the application database
   * @param codesService the Codes Service
   * @param configurationService the Configuration Service
   * @param errorService the Error Service
   * @param mailService the Mail Service
   * @param reportingService the Reporting Service
   * @param sampleService the Sample Service
   * @param schedulerService the Scheduler Service
   * @param securityService the Security Service
   * @param validator the JSR-303 validator
   */
  public SampleApplication(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource,
      ICodesService codesService,
      IConfigurationService configurationService,
      IErrorService errorService,
      IMailService mailService,
      IReportingService reportingService,
      ISampleService sampleService,
      ISchedulerService schedulerService,
      ISecurityService securityService,
      Validator validator) {
    super(applicationContext);

    this.dataSource = dataSource;
    this.codesService = codesService;
    this.configurationService = configurationService;
    this.errorService = errorService;
    this.mailService = mailService;
    this.reportingService = reportingService;
    this.sampleService = sampleService;
    this.schedulerService = schedulerService;
    this.securityService = securityService;
    this.validator = validator;
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

  //  /**
  //   * Returns the Spring bean for the Springfox Swagger 2 documentation generation component.
  //   *
  //   * @return the Spring bean for the Springfox Swagger 2 documentation generation component
  //   */
  //  @Bean
  //  public Docket api() {
  //    ApiInfo apiInfo =
  //        new ApiInfo(
  //            "Sample",
  //            "REST API documentation for the Sample application",
  //            Version.PROJECT_VERSION,
  //            "",
  //            new Contact("Marcus Portmann", "", ""),
  //            "Apache 2.0",
  //            "http://www.apache.org/licenses/LICENSE-2.0.html",
  //            new ArrayList<>());
  //
  //    // Versioned API path selector: /v[0-9]*/.*
  //    return new Docket(DocumentationType.SWAGGER_2)
  //        .select()
  //        .apis(RequestHandlerSelectors.any())
  //        .paths(PathSelectors.regex("/api/.*"))
  //        .build()
  //        .useDefaultResponseMessages(false)
  //        .apiInfo(apiInfo);
  //  }

//  /**
//   * Returns the Spring bean for the Codes Service web service.
//   *
//   * @return the Spring bean for the Codes Service web service
//   */
//  @Bean
//  protected Endpoint codesWebService() {
//    return createWebServiceEndpoint("CodesService", new CodesWebService(codesService, validator));
//  }
//
//  /**
//   * Returns the Spring bean for the Configuration Service web service.
//   *
//   * @return the Spring bean for the Configuration Service web service
//   */
//  @Bean
//  protected Endpoint configurationWebService() {
//    return createWebServiceEndpoint(
//        "ConfigurationService", new ConfigurationWebService(configurationService, validator));
//  }
//
//  /**
//   * Returns the Spring bean for the Error Service web service.
//   *
//   * @return the Spring bean for the Error Service web service
//   */
//  @Bean
//  protected Endpoint errorWebService() {
//    return createWebServiceEndpoint("ErrorService", new ErrorWebService(errorService, validator));
//  }
//
//  /**
//   * Returns the Spring bean for the Mail Service web service.
//   *
//   * @return the Spring bean for the Mail Service web service
//   */
//  @Bean
//  protected Endpoint mailWebService() {
//    return createWebServiceEndpoint("MailService", new MailWebService(mailService, validator));
//  }
//
//  /**
//   * Returns the Spring bean for the Reporting Service web service.
//   *
//   * @return the Spring bean for the Reporting Service web service
//   */
//  @Bean
//  protected Endpoint reportingWebService() {
//    return createWebServiceEndpoint(
//        "ReportingService", new ReportingWebService(dataSource, reportingService, validator));
//  }

//  /**
//   * Returns the Spring bean for the Sample Service web service.
//   *
//   * @return the Spring bean for the Sample Service web service
//   */
//  @Bean
//  protected Endpoint sampleWebService() {
//    return createWebServiceEndpoint("SampleService", new SampleWebService(sampleService));
//  }

  /// **
  // * Returns the in-memory distributed cache manager.
  // *
  // * @return the in-memory distributed cache manager
  // */
  // @Bean
  // protected CacheManager cacheManager()
  //  throws CacheManagerException
  // {
  //  return new CacheManager(configuration.getCacheManager());
  // }

//  /**
//   * Returns the Spring bean for the Security Service web service.
//   *
//   * @return the Spring bean for the Security Service web service
//   */
//  @Bean
//  protected Endpoint securityWebService() {
//    return createWebServiceEndpoint(
//        "SecurityService", new SecurityWebService(securityService, validator));
//  }
}

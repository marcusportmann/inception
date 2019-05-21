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

package digital.inception.sample;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.application.Application;
import digital.inception.codes.CodesWebService;
import digital.inception.codes.ICodesService;
import digital.inception.configuration.ConfigurationWebService;
import digital.inception.configuration.IConfigurationService;
import digital.inception.core.util.ISO8601Util;
import digital.inception.core.util.ResourceUtil;
import digital.inception.error.ErrorWebService;
import digital.inception.error.IErrorService;
import digital.inception.reporting.IReportingService;
import digital.inception.reporting.ReportDefinition;
import digital.inception.reporting.ReportingWebService;
import digital.inception.sample.api.SampleServiceController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import javax.validation.Validator;

import javax.xml.ws.Endpoint;

/**
 * The <code>SampleApplication</code> provides the implementation of the Wicket Web Application
 * class for the sample application.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
@ComponentScan(basePackages = { "digital.inception" }, lazyInit = true)
@EnableSwagger2
public class SampleApplication extends Application
  implements InitializingBean
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

  /**
   * The Codes Service.
   */
  private ICodesService codesService;

  /**
   * The Configuration Service.
   */
  private IConfigurationService configurationService;

  /**
   * The Error Service.
   */
  private IErrorService errorService;

  /**
   * The Reporting Service.
   */
  private IReportingService reportingService;

  /**
   * The Sample Service Controller.
   */
  private SampleServiceController sampleServiceController;

  /**
   * The JSR-303 validator.
   */
  private Validator validator;

  /**
   * The data source used to provide connections to the application database.
   */
  private DataSource dataSource;


  /**
   * Constructs a new <code>SampleApplication</code>.
   *
   * @param applicationContext      the Spring application context
   * @param dataSource              the data source used to provide connections to the application
   *                                database
   * @param codesService            the Codes Service
   * @param configurationService    the Configuration Service
   * @param errorService            the Error Service
   * @param reportingService        the Reporting Service
   * @param sampleServiceController the Sample Service Controller
   * @param validator               the JSR-303 validator
   */
  public SampleApplication(ApplicationContext applicationContext, @Qualifier("applicationDataSource") DataSource dataSource, ICodesService codesService,
      IConfigurationService configurationService, IErrorService errorService,
      IReportingService reportingService, SampleServiceController sampleServiceController,
      Validator validator)
  {
    super(applicationContext);

    this.dataSource = dataSource;
    this.codesService = codesService;
    this.configurationService = configurationService;
    this.errorService = errorService;
    this.reportingService = reportingService;
    this.sampleServiceController = sampleServiceController;
    this.validator = validator;
  }

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args)
  {
    SpringApplication.run(SampleApplication.class, args);
  }

  /**
   * Initialize the sample application.
   */
  @Override
  public void afterPropertiesSet()
  {
    try
    {
      byte[] sampleReportDefinitionData = ResourceUtil.getClasspathResource(
          "digital/inception/sample/SampleReport.jasper");

      ReportDefinition sampleReportDefinition = new ReportDefinition(UUID.fromString(
          "2a4b74e8-7f03-416f-b058-b35bb06944ef"), "Sample Report", sampleReportDefinitionData);

      if (!reportingService.reportDefinitionExists(sampleReportDefinition.getId()))
      {
        reportingService.createReportDefinition(sampleReportDefinition);
        logger.info("Saved the \"Sample Report\" report definition");
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialize the Sample application", e);
    }
  }

  /**
   * Returns the Spring bean for the Springfox Swagger 2 documentation generation component.
   *
   * @return the Spring bean for the Springfox Swagger 2 documentation generation component
   */
  @Bean
  public Docket api()
  {
    ApiInfo apiInfo = new ApiInfo("Sample", "REST API documentation for the Sample application",
        Version.PROJECT_VERSION, "", new Contact("Marcus Portmann", "", ""), "Apache 2.0",
        "http://www.apache.org/licenses/LICENSE-2.0.html", new ArrayList<>());

    // Versioned API path selector: /v[0-9]*/.*
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("/api/.*")).build().useDefaultResponseMessages(false).apiInfo(
        apiInfo);
  }

  /**
   * Returns the Spring bean for the Codes Service web service.
   *
   * @return the Spring bean for the Codes Service web service
   */
  @Bean
  protected Endpoint codesWebService()
  {
    return createWebServiceEndpoint("CodesService", new CodesWebService(codesService, validator));
  }

  /**
   * Returns the Spring bean for the Configuration Service web service.
   *
   * @return the Spring bean for the Configuration Service web service
   */
  @Bean
  protected Endpoint configurationWebService()
  {
    return createWebServiceEndpoint("ConfigurationService", new ConfigurationWebService(
        configurationService, validator));
  }

  /**
   * Returns the Spring bean for the Error Service web service.
   *
   * @return the Spring bean for the Error Service web service
   */
  @Bean
  protected Endpoint errorWebService()
  {
    return createWebServiceEndpoint("ErrorService", new ErrorWebService(errorService, validator));
  }

///**
// * Returns the in-memory distributed cache manager.
// *
// * @return the in-memory distributed cache manager
// */
//@Bean
//protected CacheManager cacheManager()
//  throws CacheManagerException
//{
//  return new CacheManager(configuration.getCacheManager());
//}





  /**
   * Returns the Spring bean for the Reporting Service web service.
   *
   * @return the Spring bean for the Reporting Service web service
   */
  @Bean
  protected Endpoint reportingWebService()
  {
    return createWebServiceEndpoint("ReportingService", new ReportingWebService(dataSource,
        reportingService, validator));
  }

  /**
   * Returns the Spring bean for the Sample Service web service.
   *
   * @return the Spring bean for the Sample Service web service
   */
  @Bean
  @DependsOn("sampleServiceController")
  protected Endpoint sampleWebService()
  {
    return createWebServiceEndpoint("SampleService", sampleServiceController);
  }
}

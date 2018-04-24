/*
 * Copyright 2018 Marcus Portmann
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
import digital.inception.configuration.ConfigurationWebService;
import digital.inception.core.util.ResourceUtil;
import digital.inception.reporting.IReportingService;
import digital.inception.reporting.ReportDefinition;
import digital.inception.reporting.ReportingWebService;
import digital.inception.sample.api.SampleServiceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

import javax.annotation.PostConstruct;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

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
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

  /* Sample Service Controller */
  @Autowired
  private SampleServiceController sampleServiceController;

  /* Reporting Service */
  @Autowired
  private IReportingService reportingService;

//
///* Sample Configuration */
//@Inject
//private Configuration configuration;

  /**
   * Constructs a new <code>SampleApplication</code>.
   */
  public SampleApplication() {}

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
    return createWebServiceEndpoint("CodesService", new CodesWebService());
  }

  /**
   * Returns the Spring bean for the Configuration Service web service.
   *
   * @return the Spring bean for the Configuration Service web service
   */
  @Bean
  protected Endpoint configurationWebService()
  {
    return createWebServiceEndpoint("ConfigurationService", new ConfigurationWebService());
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
   * Returns the paths to the resources on the classpath that contain the SQL statements used to
   * initialize the in-memory application database.
   */
  @Override
  protected List<String> getInMemoryDatabaseInitResources()
  {
    List<String> resources = super.getInMemoryDatabaseInitResources();

    resources.add("digital/inception/sample/inception-sample-h2.sql");

    return resources;
  }

  /**
   * Returns the names of the packages to scan for JPA classes.
   *
   * @return the names of the packages to scan for JPA classes
   */
  @Override
  protected List<String> getJpaPackagesToScan()
  {
    List<String> packagesToScan = super.getJpaPackagesToScan();

    packagesToScan.add("digital.inception.sample");

    return packagesToScan;
  }

  /**
   * Returns the Spring bean for the Reporting Service web service.
   *
   * @return the Spring bean for the Reporting Service web service
   */
  @Bean
  protected Endpoint reportingWebService()
  {
    return createWebServiceEndpoint("ReportingService", new ReportingWebService());
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

  /**
   * Initialize the Sample application.
   */
  @PostConstruct
  private void initSampleApplication()
  {
    try
    {
      byte[] cxfXml = ResourceUtil.getClasspathResource("/cxf.xml");

      int xxx = 0;
      xxx++;
    }
    catch (Throwable e)
    {
      int xxx = 0;
      xxx++;

    }

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

///**
// * Setup the navigation hierarchy for the application.
// *
// * @param root the root of the navigation hierarchy
// */
//@Override
//protected void initNavigation(NavigationGroup root)
//{
//  root.addItem(new NavigationLink("Home", "fa fa-home", HomePage.class));
//  root.addItem(new NavigationLink("Dashboard", "fa fa-home", DashboardPage.class));
//
//  NavigationGroup dialogsGroup = new NavigationGroup("Dialogs", "fa fa-window-restore");
//
//  dialogsGroup.addItem(new NavigationLink("Test Extensible Dialog",
//      TestExtensibleDialogImplementationPage.class));
//  dialogsGroup.addItem(new NavigationLink("Test Extensible Form Dialog",
//      TestExtensibleFormDialogImplementationPage.class));
//
//  root.addItem(dialogsGroup);
//
//  NavigationGroup formsGroup = new NavigationGroup("Forms", "fa fa-pencil-square-o");
//
//  formsGroup.addItem(new NavigationLink("Test Form", TestFormPage.class));
//
//  root.addItem(formsGroup);
//
//  super.initNavigation(root);
//}
}

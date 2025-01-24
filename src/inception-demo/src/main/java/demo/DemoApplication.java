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

package demo;

import digital.inception.application.Application;
import digital.inception.core.util.ResourceUtil;
import digital.inception.executor.model.TaskEventType;
import digital.inception.executor.model.TaskPriority;
import digital.inception.executor.model.TaskType;
import digital.inception.executor.service.ExecutorService;
import digital.inception.reporting.model.ReportDefinition;
import digital.inception.reporting.service.ReportingService;
import digital.inception.security.model.GenerateTokenRequest;
import digital.inception.security.model.Policy;
import digital.inception.security.model.PolicyType;
import digital.inception.security.model.Token;
import digital.inception.security.model.TokenClaim;
import digital.inception.security.model.TokenType;
import digital.inception.security.service.SecurityService;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

/**
 * The <b>DemoApplication</b> class provides the implementation of the Inception Framework
 * application class for the demo application.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
@EnableCaching
public class DemoApplication extends Application {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

  /** The Executor Service. */
  private final ExecutorService executorService;

  /** The Reporting Service. */
  private final ReportingService reportingService;

  /** The Security Service. */
  private final SecurityService securityService;

  /**
   * Constructs a new <b>DemoApplication</b>.
   *
   * @param applicationContext the Spring application context
   * @param executorService the Executor Service
   * @param reportingService the Reporting Service
   * @param securityService the Security Service
   */
  public DemoApplication(
      ApplicationContext applicationContext,
      ExecutorService executorService,
      ReportingService reportingService,
      SecurityService securityService) {
    super(applicationContext);

    this.executorService = executorService;
    this.reportingService = reportingService;
    this.securityService = securityService;
  }

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    System.setProperty("com.sun.jndi.ldap.object.disableEndpointIdentification", "true");

    SpringApplication.run(DemoApplication.class, args);
  }

  /** Initialize the demo application. */
  @PostConstruct
  public void init() {
    try {
      byte[] demoReportDefinitionData =
          ResourceUtil.getClasspathResource("digital/inception/demo/DemoReport.jasper");

      ReportDefinition demoReportDefinition =
          new ReportDefinition(
              "Inception.Demo.DemoReport", "Demo Report", demoReportDefinitionData);

      if (!reportingService.reportDefinitionExists(demoReportDefinition.getId())) {
        reportingService.createReportDefinition(demoReportDefinition);
        log.info("Saved the \"Demo Report\" report definition");
      }

      String demoPolicyData =
          ResourceUtil.getStringClasspathResource("pdp/policies/DemoPolicy.xml");

      Policy demoPolicy =
          new Policy("DemoPolicy", "1.0", "Demo Policy", PolicyType.XACML_POLICY, demoPolicyData);

      securityService.createPolicy(demoPolicy);

      createDemoTokens();

      createDemoTaskTypes();
    } catch (Throwable e) {
      throw new BeanInitializationException("Failed to initialize the Demo application", e);
    }
  }

  private void createDemoTaskTypes() {
    try {
      if (!executorService.taskTypeExists("demo_task")) {
        TaskType demoTaskType =
            new TaskType(
                "demo_task",
                "Demo Task",
                TaskPriority.NORMAL,
                "demo.task.DemoTaskExecutor",
                3);

        demoTaskType.setEventTypesWithTaskData(
            List.of(
                TaskEventType.STEP_COMPLETED,
                TaskEventType.TASK_COMPLETED,
                TaskEventType.TASK_FAILED));

        executorService.createTaskType(demoTaskType);
      }
    } catch (Throwable e) {
      throw new RuntimeException("Failed to create the demo task types", e);
    }
  }

  private void createDemoTokens() {
    try {
      List<TokenClaim> tokenClaims = new ArrayList<>();

      tokenClaims.add(new TokenClaim("roles", List.of("Administrator")));
      tokenClaims.add(new TokenClaim("claim_name_1", List.of("claim_name_1_value_1")));
      tokenClaims.add(
          new TokenClaim("claim_name_2", List.of("claim_name_2_value_1", "claim_name_2_value_2")));

      GenerateTokenRequest generateValidFromTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT,
              "demo_valid_from_token",
              "Demo Valid From Token",
              LocalDate.parse("2016-07-17"),
              tokenClaims);

      securityService.generateToken(generateValidFromTokenRequest);

      GenerateTokenRequest generateRevokedTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT, "demo_revoked_token", "Demo Revoked Token", tokenClaims);

      Token permanentToken = securityService.generateToken(generateRevokedTokenRequest);

      securityService.revokeToken(permanentToken.getId());

      GenerateTokenRequest generateExpiringTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT,
              "demo_expiring_token",
              "Demo Expiring Token",
              LocalDate.parse("2016-07-17"),
              LocalDate.parse("2040-01-01"),
              tokenClaims);

      securityService.generateToken(generateExpiringTokenRequest);

      GenerateTokenRequest generateExpiredTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT,
              "demo_expired_token",
              "Demo Expired Token",
              LocalDate.parse("2016-07-17"),
              LocalDate.parse("2024-01-13"),
              tokenClaims);

      securityService.generateToken(generateExpiredTokenRequest);

      GenerateTokenRequest generatePendingTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT,
              "demo_pending_token",
              "Demo Pending Token",
              LocalDate.parse("2044-01-13"),
              tokenClaims);

      securityService.generateToken(generatePendingTokenRequest);

      for (int i = 0; i < 10; i++) {
        GenerateTokenRequest generateServiceTokenRequest =
            new GenerateTokenRequest(
                TokenType.JWT,
                "svc_service_name_" + i,
                "Service Name " + i + " Service Token",
                tokenClaims);

        securityService.generateToken(generateServiceTokenRequest);
      }

      GenerateTokenRequest generateExternalSystemTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT, "ext_system_name", "System Name External Token", tokenClaims);

      securityService.generateToken(generateExternalSystemTokenRequest);

      GenerateTokenRequest generateExternalOrganizationTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT,
              "ext_organization_name",
              "Organization Name External Token",
              tokenClaims);

      securityService.generateToken(generateExternalOrganizationTokenRequest);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to create the demo tokens", e);
    }
  }
}

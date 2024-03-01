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

package digital.inception.demo;

import digital.inception.application.Application;
import digital.inception.core.util.ResourceUtil;
import digital.inception.reporting.IReportingService;
import digital.inception.reporting.ReportDefinition;
import digital.inception.security.GenerateTokenRequest;
import digital.inception.security.ISecurityService;
import digital.inception.security.Policy;
import digital.inception.security.PolicyType;
import digital.inception.security.Token;
import digital.inception.security.TokenClaim;
import digital.inception.security.TokenType;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

  /** The Reporting Service. */
  private final IReportingService reportingService;

  /** The Security Service. */
  private final ISecurityService securityService;

  /**
   * Constructs a new <b>DemoApplication</b>.
   *
   * @param applicationContext the Spring application context
   * @param reportingService the Reporting Service
   * @param securityService the Security Service
   */
  public DemoApplication(
      ApplicationContext applicationContext,
      IReportingService reportingService,
      ISecurityService securityService) {
    super(applicationContext);

    this.reportingService = reportingService;
    this.securityService = securityService;
  }

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
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
        logger.info("Saved the \"Demo Report\" report definition");
      }

      String demoPolicyData =
          ResourceUtil.getStringClasspathResource("pdp/policies/DemoPolicy.xml");

      Policy demoPolicy =
          new Policy("DemoPolicy", "1.0", "Demo Policy", PolicyType.XACML_POLICY, demoPolicyData);

      securityService.createPolicy(demoPolicy);

      createDemoTokens();
    } catch (Throwable e) {
      throw new RuntimeException("Failed to initialize the Demo application", e);
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
              "Demo Valid From Token Name",
              "Demo Valid From Token Description",
              LocalDate.parse("2016-07-17"),
              tokenClaims);

      Token validFromToken = securityService.generateToken(generateValidFromTokenRequest);

      GenerateTokenRequest generateRevokedTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT,
              "Demo Revoked Token Name",
              "Demo Revoked Token Description",
              tokenClaims);

      Token permanentToken = securityService.generateToken(generateRevokedTokenRequest);

      securityService.revokeToken(permanentToken.getId());

      GenerateTokenRequest generateExpiringTokenRequest =
          new GenerateTokenRequest(
              TokenType.JWT,
              "Demo Expiring Token Name",
              "Demo Expiring Token Description",
              LocalDate.parse("2016-07-17"),
              LocalDate.parse("2040-01-01"),
              tokenClaims);

      Token expiringToken = securityService.generateToken(generateExpiringTokenRequest);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to create the demo tokens", e);
    }
  }
}

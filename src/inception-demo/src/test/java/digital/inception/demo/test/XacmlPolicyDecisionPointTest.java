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

package digital.inception.demo.test;

import static org.junit.jupiter.api.Assertions.fail;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import digital.inception.core.util.ISO8601Util;
import digital.inception.demo.DemoConfiguration;
import digital.inception.demo.model.Data;
import digital.inception.server.resource.xacmlpdp.XacmlPolicyDecisionPoint;
import digital.inception.server.resource.xacmlpdp.XacmlUtil;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import digital.inception.web.RequestBodyObjectContext;
import java.math.BigDecimal;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.DecisionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ow2.authzforce.core.pdp.api.DecisionRequest;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;
import org.ow2.authzforce.core.pdp.api.DecisionResult;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeId;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>XacmlPolicyDecisionPointTest</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, DemoConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class XacmlPolicyDecisionPointTest {

  /** The policy decision point. */
  private static XacmlPolicyDecisionPoint xacmlPolicyDecisionPoint;

  @BeforeAll
  public static void init(ApplicationContext applicationContext) {
    xacmlPolicyDecisionPoint =
        new XacmlPolicyDecisionPoint(
            applicationContext, true, false, "http://localhost:8080/api/security/policies", 43200);
  }

  @Test
  public void xacmlDemoProcessRuleTest() throws Exception {
    Data data = new Data();
    data.setId(System.currentTimeMillis());
    data.setBooleanValue(true);
    data.setDateValue(ISO8601Util.toLocalDate("1976-03-07"));
    data.setDecimalValue(new BigDecimal("111.111"));
    data.setDoubleValue(222.222);
    data.setFloatValue(333.333f);
    data.setIntegerValue(444);
    data.setStringValue("This is a valid string value");
    data.setTimeValue(ISO8601Util.toLocalTime("14:30:00"));
    data.setTimeWithTimeZoneValue(ISO8601Util.toOffsetTime("18:30:00+02:00"));
    data.setTimestampValue(ISO8601Util.toLocalDateTime("2016-07-17T23:56:19.123"));
    data.setTimestampWithTimeZoneValue(
        ISO8601Util.toOffsetDateTime("2019-02-28T00:14:27.505+02:00"));
    data.setCountry("ZA");
    data.setLanguage("EN");

    RequestBodyObjectContext.setRequestBodyObject(data);

    // Get the logger context from the LoggerFactory
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    // Get the specific logger by name, or use the root logger
    Logger logger = loggerContext.getLogger("org.ow2.authzforce.core.pdp.impl.rule.RuleEvaluator");

    logger.setLevel(Level.DEBUG);

    /*
     * Create the decision request.
     */
    DecisionRequestBuilder<?> requestBuilder =
        xacmlPolicyDecisionPoint.getPdpEngine().newRequestBuilder(-1, -1);

    /*
     * Add the subject role attribute (access-subject category), no issuer, string value.
     *
     * NOTE: This will be the user's roles extracted from the JWT.
     */
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
        XacmlAttributeId.XACML_2_0_SUBJECT_ROLE.value(),
        "ANONYMOUS");

    // Add the resource ID attribute (resource category), no issuer, string value
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_3_0_RESOURCE.value(),
        XacmlAttributeId.XACML_1_0_RESOURCE_ID.value(),
        "/api/data/process");

    // Add the action ID attribute (action category), no issuer, string value.
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_3_0_ACTION.value(),
        XacmlAttributeId.XACML_1_0_ACTION_ID.value(),
        "POST");

    // No more attributes, let's finalize the decision request creation
    DecisionRequest decisionRequest = requestBuilder.build(true);

    // Evaluate the decision request
    DecisionResult decisionResult = xacmlPolicyDecisionPoint.evaluate(decisionRequest);

    if (decisionResult.getDecision() != DecisionType.PERMIT) {
      fail(
          "Failed to successfully evaluate the data-process-rule in the DemoPolicy.xml XACML policy");
    }
  }
}

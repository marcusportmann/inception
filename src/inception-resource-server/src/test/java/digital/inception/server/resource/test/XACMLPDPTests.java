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

package digital.inception.server.resource.test;

import static org.junit.jupiter.api.Assertions.fail;

import digital.inception.server.resource.xacmlpdp.XacmlPolicyDecisionPoint;
import digital.inception.server.resource.xacmlpdp.XacmlPolicyDecisionPointAttributeCategory;
import digital.inception.server.resource.xacmlpdp.XacmlUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.DecisionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ow2.authzforce.core.pdp.api.DecisionRequest;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;
import org.ow2.authzforce.core.pdp.api.DecisionResult;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeId;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>XACMLPDPTests</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class XACMLPDPTests {

  /** The policy decision point. */
  private static XacmlPolicyDecisionPoint xacmlPolicyDecisionPoint;

  @BeforeAll
  public static void init(ApplicationContext applicationContext) {
    xacmlPolicyDecisionPoint =
        new XacmlPolicyDecisionPoint(
            applicationContext, true, false, "http://localhost:8080/api/security/policies", 43200);
  }

  @Test
  public void xacmlExternalClientAccessUsingResourceIdTest() throws Exception {
    Map<String, String> pathVariables = new HashMap<>();
    pathVariables.put("clientId", "1234567890");

    /*
     * Create the decision request.
     */
    DecisionRequestBuilder<?> requestBuilder =
        xacmlPolicyDecisionPoint.getPdpEngine().newRequestBuilder(-1, -1);

    /*
     * Add the subject ID attribute (access-subject category), no issuer, string value
     *
     * NOTE: This will be the user's identity extracted as the subject from the JWT.
     */
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
        XacmlAttributeId.XACML_1_0_SUBJECT_ID.value(),
        "clientusername");

    /*
     * Add the subject role attribute (access-subject category), no issuer, string value.
     *
     * NOTE: This will be the user's roles extracted from the JWT.
     */
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
        XacmlAttributeId.XACML_2_0_SUBJECT_ROLE.value(),
        "Client");

    /*
     * Add the clients attribute (jwt-claims category), no issuer, string value.
     *
     * NOTE: This will be the user's roles extracted from the JWT.
     */
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlPolicyDecisionPointAttributeCategory.JWT_CLAIMS.value(),
        "clients",
        Arrays.asList("0987654321", "1234567890"));

    // Add the path variable attributes (custom path variables category), no issuer, string value
    for (Entry<String, String> pathVariable : pathVariables.entrySet()) {
      XacmlUtil.addAttributeToRequest(
          requestBuilder,
          XacmlPolicyDecisionPointAttributeCategory.PATH_VARIABLES.value(),
          pathVariable.getKey(),
          pathVariable.getValue());
    }

    // Add the resource ID attribute (resource category), no issuer, string value
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_3_0_RESOURCE.value(),
        XacmlAttributeId.XACML_1_0_RESOURCE_ID.value(),
        "/api/clients/{clientId}");

    // Add the action ID attribute (action category), no issuer, string value.
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_3_0_ACTION.value(),
        XacmlAttributeId.XACML_1_0_ACTION_ID.value(),
        "GET");

    // No more attributes, let's finalize the decision request creation
    DecisionRequest decisionRequest = requestBuilder.build(true);

    // Evaluate the decision request
    DecisionResult decisionResult = xacmlPolicyDecisionPoint.evaluate(decisionRequest);
    if (decisionResult.getDecision() != DecisionType.PERMIT) {
      fail("Failed to successfully evaluate the External Client Access XACML policy");
      // This is a Permit :-)
    }
  }

  @Test
  public void xacmlInternalClientAccessUsingResourceIdTest() throws Exception {
    /*
     * Create the decision request.
     */
    DecisionRequestBuilder<?> requestBuilder =
        xacmlPolicyDecisionPoint.getPdpEngine().newRequestBuilder(-1, -1);

    /*
     * Add the subject ID attribute (access-subject category), no issuer, string value
     *
     * NOTE: This will be the user's identity extracted as the subject from the JWT.
     */
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
        XacmlAttributeId.XACML_1_0_SUBJECT_ID.value(),
        "jonas");

    /*
     * Add the subject role attribute (access-subject category), no issuer, string value.
     *
     * NOTE: This will be the user's roles extracted from the JWT.
     */
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
        XacmlAttributeId.XACML_2_0_SUBJECT_ROLE.value(),
        "BackOffice");

    // Add the resource ID attribute (resource category), no issuer, string value
    XacmlUtil.addAttributeToRequest(
        requestBuilder,
        XacmlAttributeCategory.XACML_3_0_RESOURCE.value(),
        XacmlAttributeId.XACML_1_0_RESOURCE_ID.value(),
        "/api/clients/{clientId}");

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
      fail("Failed to successfully evaluate the Internal Client Access XACML policy");
      // This is a Permit :-)
    }
  }

  /** The <b>XACMLTestConfiguration</b> class. */
  @Configuration
  public static class XACMLTestConfiguration {

    /** Constructs a new <b>XACMLTestConfiguration</b>. */
    public XACMLTestConfiguration() {}

    /**
     * Returns the policy decision point.
     *
     * @param applicationContext the Spring application context
     * @return the policy decision point
     */
    @Bean
    XacmlPolicyDecisionPoint policyDecisionPoint(ApplicationContext applicationContext) {
      return new XacmlPolicyDecisionPoint(
          applicationContext, true, false, "http://localhost:8080/api/security/policies", 43200);
    }
  }
}

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

package demo.controller;

import digital.inception.server.resource.xacmlpdp.XacmlPolicyDecisionPointAttributeCategory;
import digital.inception.server.resource.xacmlpdp.XacmlPolicyDecisionPointContextProvider;
import digital.inception.server.resource.xacmlpdp.XacmlUtil;
import org.aopalliance.intercept.MethodInvocation;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * The <b>TestApiXacmlPolicyDecisionPointContextProvider</b> class.
 *
 * @author Marcus Portmann
 */
@Component
public class TestApiXacmlPolicyDecisionPointContextProvider
    implements XacmlPolicyDecisionPointContextProvider {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(TestApiXacmlPolicyDecisionPointContextProvider.class);

  /** Constructs a new <b>TestApiXmlPolicyDecisionPointContextProvider</b>. */
  public TestApiXacmlPolicyDecisionPointContextProvider() {}

  @Override
  public void populateDecisionRequest(
      Object authenticationObject,
      MethodInvocation methodInvocation,
      DecisionRequestBuilder<?> decisionRequestBuilder) {
    if (methodInvocation.getThis() instanceof TestApiControllerImpl) {
      if (authenticationObject instanceof Authentication authentication) {
        log.info(
            "Adding request attributes for the XACML policy decision point authorization for the user ("
                + authentication.getName()
                + ") on class ("
                + methodInvocation.getMethod().getDeclaringClass().getName()
                + ") and method ("
                + methodInvocation.getMethod().getName()
                + ")");

      } else {
        log.info(
            "Adding request attributes for the XACML policy decision point authorization on class ("
                + methodInvocation.getMethod().getDeclaringClass().getName()
                + ") and method ("
                + methodInvocation.getMethod().getName()
                + ")");
      }

      XacmlUtil.addAttributeToRequest(
          decisionRequestBuilder,
          XacmlPolicyDecisionPointAttributeCategory.REQUEST_ATTRIBUTES.value(),
          "TestAttributeName",
          XacmlUtil.getAttributeValues("TestAttributeValue"));
    }
  }
}

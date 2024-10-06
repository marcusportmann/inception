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

package digital.inception.demo.controller;

import digital.inception.core.api.IPolicyDecisionPointContextProvider;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

/**
 * The <b>TestApiPolicyDecisionPointContextProvider</b> class.
 *
 * @author Marcus Portmann
 */
public class TestApiPolicyDecisionPointContextProvider
    implements IPolicyDecisionPointContextProvider {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(TestApiPolicyDecisionPointContextProvider.class);

  /** Constructs a new <b>TestApiPolicyDecisionPointContextProvider</b>. */
  public TestApiPolicyDecisionPointContextProvider() {}

  @Override
  public Map<String, Object> getRequestAttributes(
      Object authenticationObject, MethodInvocation methodInvocation) {

    if (authenticationObject instanceof Authentication authentication) {
      log.info(
          "Adding request attributes for the policy decision point authorization for user ("
              + authentication.getName()
              + ") on class ("
              + methodInvocation.getMethod().getDeclaringClass().getName()
              + ") and method ("
              + methodInvocation.getMethod().getName()
              + ")");

    } else {
      log.info(
          "Adding request attributes for the policy decision point authorization on class ("
              + methodInvocation.getMethod().getDeclaringClass().getName()
              + ") and method ("
              + methodInvocation.getMethod().getName()
              + ")");
    }

    Map<String, Object> requestAttributes = new HashMap<>();
    requestAttributes.put("TestAttributeName", "TestAttributeValue");
    return requestAttributes;
  }
}

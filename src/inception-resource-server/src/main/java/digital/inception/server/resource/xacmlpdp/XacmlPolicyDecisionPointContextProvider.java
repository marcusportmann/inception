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

package digital.inception.server.resource.xacmlpdp;

import org.aopalliance.intercept.MethodInvocation;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;

/**
 * The <b>XacmlPolicyDecisionPointContextProvider</b> interface defines the interface that must be
 * implemented by a XACML policy decision point context provider, which is used to add additional
 * attributes to the XACML decision request that will be authorized by the XACML policy decision
 * point using AuthzForce PDP engine.
 *
 * @author Marcus Portmann
 */
public interface XacmlPolicyDecisionPointContextProvider {

  /**
   * Add additional context to the AuthzForce XACML authorization decision request used as input to
   * AuthzForce PDP engine.
   *
   * @param authenticationObject the authentication object
   * @param methodInvocation the method invocation
   * @param decisionRequestBuilder the AuthzForce XACML authorization decision request builder
   */
  void populateDecisionRequest(
      Object authenticationObject,
      MethodInvocation methodInvocation,
      DecisionRequestBuilder<?> decisionRequestBuilder);
}

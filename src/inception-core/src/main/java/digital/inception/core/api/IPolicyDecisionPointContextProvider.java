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

package digital.inception.core.api;

import java.util.Map;
import org.aopalliance.intercept.MethodInvocation;

/**
 * The <b>IPolicyDecisionPointContextProvider</b> interface defines the interface that must be
 * implemented by a policy decision point context provider, which is used to add additional request
 * attributes to the decision request that will be authorized by the policy decision point.
 *
 * <p>The class implementing this interface should be passed to the
 *
 * @author Marcus Portmann
 */
public interface IPolicyDecisionPointContextProvider {

  /**
   * Returns the request attributes to add to the decision request sent to the policy decision
   * point.
   *
   * @param authenticationObject the authentication object
   * @param methodInvocation the method invocation
   * @return the request attributes to add to the decision request sent to the policy decision point
   */
  Map<String, Object> getRequestAttributes(
      Object authenticationObject, MethodInvocation methodInvocation);
}

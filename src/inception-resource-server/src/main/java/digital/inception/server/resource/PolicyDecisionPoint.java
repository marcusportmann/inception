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

package digital.inception.server.resource;

import org.aopalliance.intercept.MethodInvocation;

/**
 * The {@code PolicyDecisionPoint} interface defines the interface that must be implemented by all
 * policy decision points.
 *
 * @author Marcus Portmann
 */
public interface PolicyDecisionPoint {

  /**
   * Authorize the method invocation using the policy decision point.
   *
   * @param authenticationObject the authentication object
   * @param methodInvocation the method invocation
   * @return {@code true} if the policy decision point authorization was successful or {@code false}
   */
  boolean authorize(Object authenticationObject, MethodInvocation methodInvocation);
}

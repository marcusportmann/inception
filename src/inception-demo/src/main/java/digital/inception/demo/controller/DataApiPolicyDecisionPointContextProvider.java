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
import digital.inception.demo.model.Data;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

/**
 * The <b>DataApiPolicyDecisionPointContextProvider</b> class.
 *
 * @author Marcus Portmann
 */
public class DataApiPolicyDecisionPointContextProvider
    implements IPolicyDecisionPointContextProvider {

  /** Constructs a new <b>DataApiPolicyDecisionPointContextProvider</b>. */
  public DataApiPolicyDecisionPointContextProvider() {}

  @Override
  public Map<String, Object> getRequestAttributes(
      Object authenticationObject, MethodInvocation methodInvocation) {

    Map<String, Object> requestAttributes = new HashMap<>();

    for (Object argument : methodInvocation.getArguments()) {
      if (argument instanceof Data data) {
        requestAttributes.put("DataId", data.getId());
      }
    }

    return requestAttributes;
  }
}

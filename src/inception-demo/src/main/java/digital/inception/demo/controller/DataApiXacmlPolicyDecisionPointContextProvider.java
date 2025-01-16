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

import org.aopalliance.intercept.MethodInvocation;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import digital.inception.demo.model.Data;
import digital.inception.server.resource.xacmlpdp.XacmlPolicyDecisionPointAttributeCategory;
import digital.inception.server.resource.xacmlpdp.XacmlPolicyDecisionPointContextProvider;
import digital.inception.server.resource.xacmlpdp.XacmlUtil;

/**
 * The <b>DataApiXacmlPolicyDecisionPointContextProvider</b> class.
 *
 * @author Marcus Portmann
 */
@Component
public class DataApiXacmlPolicyDecisionPointContextProvider
    implements XacmlPolicyDecisionPointContextProvider {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(DataApiXacmlPolicyDecisionPointContextProvider.class);

  /** Constructs a new <b>DataApiPolicyDecisionPointContextProvider</b>. */
  public DataApiXacmlPolicyDecisionPointContextProvider() {}

  @Override
  public void populateDecisionRequest(
      Object authenticationObject,
      MethodInvocation methodInvocation,
      DecisionRequestBuilder<?> decisionRequestBuilder) {
    for (Object argument : methodInvocation.getArguments()) {
      if (argument instanceof Data data) {
        XacmlUtil.addAttributeToRequest(
            decisionRequestBuilder,
            XacmlPolicyDecisionPointAttributeCategory.REQUEST_ATTRIBUTES.value(),
            "DataId",
            XacmlUtil.getAttributeValues(data.getId()));
      }
    }
  }
}

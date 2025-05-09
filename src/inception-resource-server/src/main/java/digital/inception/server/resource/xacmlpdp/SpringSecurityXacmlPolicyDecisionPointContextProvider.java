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

import java.util.List;
import org.aopalliance.intercept.MethodInvocation;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * The {@code SpringSecurityXacmlPolicyDecisionPointContextProvider} class implements a {@code
 * XacmlPolicyDecisionPointContextProvider} that adds additional context to the AuthzForce XACML
 * authorization decision request from the Spring Security context.
 *
 * @author Marcus Portmann
 */
@Component
public class SpringSecurityXacmlPolicyDecisionPointContextProvider
    implements XacmlPolicyDecisionPointContextProvider {

  /** Constructs a new {@code SpringSecurityXacmlPolicyDecisionPointContextProvider}. */
  public SpringSecurityXacmlPolicyDecisionPointContextProvider() {}

  @Override
  public void populateDecisionRequest(
      Object authenticationObject,
      MethodInvocation methodInvocation,
      DecisionRequestBuilder<?> decisionRequestBuilder) {
    if (authenticationObject instanceof Authentication authentication) {
      if (authentication.getPrincipal()
          instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {

        jwt.getClaims()
            .forEach(
                (claimName, claimValue) -> {
                  switch (claimName) {
                    case "sub":
                      XacmlUtil.addAttributeToRequest(
                          decisionRequestBuilder,
                          XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
                          XacmlAttributeId.XACML_1_0_SUBJECT_ID.value(),
                          (String) claimValue);
                      break;
                    case "roles":
                      XacmlUtil.addAttributeToRequest(
                          decisionRequestBuilder,
                          XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
                          XacmlAttributeId.XACML_2_0_SUBJECT_ROLE.value(),
                          (List<String>) claimValue);
                      break;
                    case "iss":
                      // Ignore "iss" claim
                      break;
                    case "name":
                      // Ignore "name" claim
                      break;
                    case "iat":
                      // Ignore "iat" claim
                      break;
                    case "exp":
                      // Ignore "exp" claim
                      break;
                    default:
                      if (claimValue instanceof List) {
                        XacmlUtil.addAttributeToRequest(
                            decisionRequestBuilder,
                            XacmlPolicyDecisionPointAttributeCategory.JWT_CLAIMS.value(),
                            claimName,
                            XacmlUtil.getAttributeValues((List<?>) claimValue, String.class));
                      } else {
                        XacmlUtil.addAttributeToRequest(
                            decisionRequestBuilder,
                            XacmlPolicyDecisionPointAttributeCategory.JWT_CLAIMS.value(),
                            claimName,
                            XacmlUtil.getAttributeValues(claimValue));
                      }

                      break;
                  }
                });
      } else {
        XacmlUtil.addAttributeToRequest(
            decisionRequestBuilder,
            XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
            XacmlAttributeId.XACML_1_0_SUBJECT_ID.value(),
            authentication.getName());

        /*
         * Add the subject role attribute (access-subject category), no issuer, string value.
         */
        List<String> roleNames =
            authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .filter(authority -> authority.length() > 5)
                .map(authority -> authority.substring(5))
                .toList();

        XacmlUtil.addAttributeToRequest(
            decisionRequestBuilder,
            XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
            XacmlAttributeId.XACML_2_0_SUBJECT_ROLE.value(),
            roleNames);
      }
    }
  }
}

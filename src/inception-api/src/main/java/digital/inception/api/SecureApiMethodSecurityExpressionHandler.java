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

package digital.inception.api;

import digital.inception.core.api.IPolicyDecisionPoint;
import java.util.Optional;
import java.util.function.Supplier;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

/**
 * The <b>SecureApiMethodSecurityExpressionHandler</b> class.
 *
 * @author Marcus Portmann
 */
public class SecureApiMethodSecurityExpressionHandler
    extends DefaultMethodSecurityExpressionHandler {

  /** The policy decision point. */
  private final IPolicyDecisionPoint policyDecisionPoint;

  /** Is debugging enabled for the Inception Framework? */
  private boolean inDebugMode;

  /** Is API security enabled for the Inception Framework? */
  private boolean isSecurityEnabled;

  /**
   * Constructs a new <b>SecureApiMethodSecurityExpressionHandler</b>.
   *
   * @param applicationContext the Spring application context
   * @param policyDecisionPoint the optional policy decision point
   */
  public SecureApiMethodSecurityExpressionHandler(
      ApplicationContext applicationContext, Optional<IPolicyDecisionPoint> policyDecisionPoint) {
    this.policyDecisionPoint = policyDecisionPoint.orElse(null);

    // Check if debugging is enabled for the Inception Framework
    try {
      if (StringUtils.hasText(
          applicationContext.getEnvironment().getProperty("inception.debug.enabled"))) {
        this.inDebugMode =
            Boolean.parseBoolean(
                applicationContext.getEnvironment().getProperty("inception.debug.enabled"));
      }
    } catch (Throwable e) {
      this.inDebugMode = false;
    }

    // Check if security is enabled for the Inception Framework
    try {
      if (!inDebugMode) {
        if (StringUtils.hasText(
            applicationContext.getEnvironment().getProperty("inception.api.security.enabled"))) {
          this.isSecurityEnabled =
              Boolean.parseBoolean(
                  applicationContext
                      .getEnvironment()
                      .getProperty("inception.api.security.enabled"));
        }
      }
    } catch (Throwable e) {
      this.isSecurityEnabled = (!this.inDebugMode);
    }
  }

  @Override
  public EvaluationContext createEvaluationContext(
      Supplier<Authentication> authentication, MethodInvocation methodInvocation) {
    if (methodInvocation.getThis() instanceof SecureApiController) {
      SecureApiSecurityExpressionRoot secureApiSecurityExpressionRoot =
          new SecureApiSecurityExpressionRoot(
              authentication,
              methodInvocation,
              policyDecisionPoint,
              isSecurityEnabled,
              inDebugMode);
      secureApiSecurityExpressionRoot.setPermissionEvaluator(getPermissionEvaluator());
      secureApiSecurityExpressionRoot.setTrustResolver(new AuthenticationTrustResolverImpl());
      secureApiSecurityExpressionRoot.setRoleHierarchy(getRoleHierarchy());
      secureApiSecurityExpressionRoot.setDefaultRolePrefix(getDefaultRolePrefix());
      StandardEvaluationContext context =
          (StandardEvaluationContext)
              super.createEvaluationContext(authentication, methodInvocation);
      context.setRootObject(secureApiSecurityExpressionRoot);
      return context;
    } else {
      return super.createEvaluationContext(authentication, methodInvocation);
    }
  }
}

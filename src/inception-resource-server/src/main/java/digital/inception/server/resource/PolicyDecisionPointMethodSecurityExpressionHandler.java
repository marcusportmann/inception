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

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

/**
 * The <b>PolicyDecisionPointMethodSecurityExpressionHandler</b> class.
 *
 * @author Marcus Portmann
 */
public class PolicyDecisionPointMethodSecurityExpressionHandler
    extends DefaultMethodSecurityExpressionHandler {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(PolicyDecisionPointMethodSecurityExpressionHandler.class);

  /** The policy decision points. */
  private final Map<String, PolicyDecisionPoint> policyDecisionPoints;

  /** Is debugging enabled for the Inception Framework? */
  private boolean inDebugMode;

  /** Is API security enabled for the Inception Framework? */
  private boolean isSecurityEnabled;

  /**
   * Constructs a new <b>PolicyDecisionPointMethodSecurityExpressionHandler</b>.
   *
   * @param applicationContext the Spring application context
   */
  public PolicyDecisionPointMethodSecurityExpressionHandler(ApplicationContext applicationContext) {

    // Check if debugging is enabled for the Inception Framework
    try {
      if (StringUtils.hasText(
          applicationContext.getEnvironment().getProperty("inception.debug.enabled"))) {
        this.inDebugMode =
            Boolean.parseBoolean(
                applicationContext.getEnvironment().getProperty("inception.debug.enabled"));
      }
    } catch (Throwable ignored) {
    }

    // Check if security is enabled for the Inception Framework
    try {
      if (StringUtils.hasText(
          applicationContext.getEnvironment().getProperty("inception.api.security.enabled"))) {
        this.isSecurityEnabled =
            Boolean.parseBoolean(
                applicationContext.getEnvironment().getProperty("inception.api.security.enabled"));
      }
    } catch (Throwable ignored) {
    }

    try {
      this.policyDecisionPoints = applicationContext.getBeansOfType(PolicyDecisionPoint.class);

      for (Entry<String, PolicyDecisionPoint> policyDecisionPointContextProvider :
          this.policyDecisionPoints.entrySet()) {
        log.info(
            "Loaded the policy decision point ({}) with class ({})",
            policyDecisionPointContextProvider.getKey(),
            policyDecisionPointContextProvider.getValue().getClass().getName());
      }

    } catch (Throwable e) {
      throw new BeanCreationException("Failed to initialize the policy decision points", e);
    }
  }

  @Override
  public EvaluationContext createEvaluationContext(
      Supplier<Authentication> authentication, MethodInvocation methodInvocation) {
    PolicyDecisionPointSecurityExpressionRoot policyDecisionPointSecurityExpressionRoot =
        new PolicyDecisionPointSecurityExpressionRoot(
            policyDecisionPoints, authentication, methodInvocation, isSecurityEnabled, inDebugMode);
    policyDecisionPointSecurityExpressionRoot.setPermissionEvaluator(getPermissionEvaluator());
    policyDecisionPointSecurityExpressionRoot.setTrustResolver(
        new AuthenticationTrustResolverImpl());
    policyDecisionPointSecurityExpressionRoot.setRoleHierarchy(getRoleHierarchy());
    policyDecisionPointSecurityExpressionRoot.setDefaultRolePrefix(getDefaultRolePrefix());
    StandardEvaluationContext context =
        (StandardEvaluationContext) super.createEvaluationContext(authentication, methodInvocation);
    context.setRootObject(policyDecisionPointSecurityExpressionRoot);
    return context;
  }
}

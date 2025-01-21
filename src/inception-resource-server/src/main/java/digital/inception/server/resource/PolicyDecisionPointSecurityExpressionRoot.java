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
import java.util.function.Supplier;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

/**
 * The <b>PolicyDecisionPointSecurityExpressionRoot</b> class implements the custom methods, which
 * can be used in conjunction with the <b>@PreAuthorize</b> annotation.
 *
 * @author Marcus Portmann
 */
public class PolicyDecisionPointSecurityExpressionRoot extends SecurityExpressionRoot
    implements MethodSecurityExpressionOperations {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(PolicyDecisionPointSecurityExpressionRoot.class);

  /** Is debugging enabled for the Inception Framework? */
  private final boolean inDebugMode;

  /** Is API security enabled for the Inception Framework? */
  private final boolean isSecurityEnabled;

  /** The method invocation. */
  private final MethodInvocation methodInvocation;

  /** The policy decision points. */
  private final Map<String, PolicyDecisionPoint> policyDecisionPoints;

  /** The filter object. */
  private Object filterObject;

  /** The return object. */
  private Object returnObject;

  /**
   * Constructs a new <b>PolicyDecisionPointSecurityExpressionRoot</b>.
   *
   * @param policyDecisionPoints the policy decision points
   * @param authentication the authentication supplier
   * @param methodInvocation the method invocation
   * @param isSecurityEnabled is API security enabled for the Inception Framework
   * @param inDebugMode is debugging enabled for the Inception Framework
   */
  public PolicyDecisionPointSecurityExpressionRoot(
      Map<String, PolicyDecisionPoint> policyDecisionPoints,
      Supplier<Authentication> authentication,
      MethodInvocation methodInvocation,
      boolean isSecurityEnabled,
      boolean inDebugMode) {
    super(authentication);
    this.policyDecisionPoints = policyDecisionPoints;
    this.methodInvocation = methodInvocation;
    this.isSecurityEnabled = isSecurityEnabled;
    this.inDebugMode = inDebugMode;
  }

  /**
   * Constructs a new <b>PolicyDecisionPointSecurityExpressionRoot</b>.
   *
   * @param policyDecisionPoints the policy decision points
   * @param authentication the authentication
   * @param methodInvocation the method invocation
   * @param isSecurityEnabled is API security enabled for the Inception Framework
   * @param inDebugMode is debugging enabled for the Inception Framework
   */
  public PolicyDecisionPointSecurityExpressionRoot(
      Map<String, PolicyDecisionPoint> policyDecisionPoints,
      Authentication authentication,
      MethodInvocation methodInvocation,
      boolean isSecurityEnabled,
      boolean inDebugMode) {
    this(
        policyDecisionPoints,
        () -> authentication,
        methodInvocation,
        isSecurityEnabled,
        inDebugMode);
  }

  /**
   * Authorize using the policy decision point(s).
   *
   * @return <b>true</b> if the policy decision point authorization was successful or <b>false</b>
   */
  public boolean authorize() {
    if (isSecurityEnabled) {
      for (var policyDecisionPoint : policyDecisionPoints.entrySet()) {
        if (!policyDecisionPoint.getValue().authorize(getAuthentication(), methodInvocation)) {
          String message =
              String.format(
                  "Authorization failed for policy decision point (%s)",
                  policyDecisionPoint.getKey());

          if (inDebugMode) {
            log.info(message);
          } else if (log.isDebugEnabled()) {
            log.debug(message);
          }

          // NOTE: We want to execute the authorization but not enforce it in debug mode
          return inDebugMode;
        }
      }
    }

    return true;
  }

  @Override
  public Object getFilterObject() {
    return filterObject;
  }

  @Override
  public Object getReturnObject() {
    return returnObject;
  }

  @Override
  public Object getThis() {
    return methodInvocation.getThis();
  }

  /**
   * Confirm that the user associated with the authenticated request has access to the specified
   * function.
   *
   * @param functionCode the code for the function
   * @return <b>true</b> if the user associated with the authenticated request has access to the
   *     function identified by the specified function code or <b>false</b> otherwise
   */
  public boolean hasAccessToFunction(String functionCode) {
    if (isSecurityEnabled) {
      // Could not retrieve the currently authenticated principal
      if (getAuthentication() == null) {
        return false;
      }

      if (!StringUtils.hasText(functionCode)) {
        return false;
      }

      if (!getAuthentication().isAuthenticated()) {
        return false;
      }

      String functionAuthority = "FUNCTION_" + functionCode;

      for (GrantedAuthority grantedAuthority : getAuthentication().getAuthorities()) {
        if (grantedAuthority.getAuthority().equalsIgnoreCase(functionAuthority)) {
          return true;
        }
      }

      return false;
    } else {
      return true;
    }
  }

  /**
   * Returns whether security is disabled.
   *
   * @return <b>true</b> if security is disabled or <b>false</b> otherwise
   */
  public boolean isSecurityDisabled() {
    return (!isSecurityEnabled);
  }

  /**
   * Returns whether security is enabled.
   *
   * @return <b>true</b> if security is enabled or <b>false</b> otherwise
   */
  public boolean isSecurityEnabled() {
    return isSecurityEnabled;
  }

  @Override
  public void setFilterObject(Object filterObject) {
    this.filterObject = filterObject;
  }

  @Override
  public void setReturnObject(Object returnObject) {
    this.returnObject = returnObject;
  }
}

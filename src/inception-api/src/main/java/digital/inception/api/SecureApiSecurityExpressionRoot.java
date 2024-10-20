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
import digital.inception.core.api.IPolicyDecisionPointContextProvider;
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
 * The <b>SecureApiSecurityExpressionRoot</b> class implements the custom methods, which can be used
 * in conjunction with the <b>@PreAuthorize</b> annotation.
 *
 * @author Marcus Portmann
 */
public class SecureApiSecurityExpressionRoot extends SecurityExpressionRoot
    implements MethodSecurityExpressionOperations {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(SecureApiSecurityExpressionRoot.class);

  /** Is debugging enabled for the Inception Framework? */
  private final boolean inDebugMode;

  /** Is API security enabled for the Inception Framework? */
  private final boolean isSecurityEnabled;

  /** The method invocation. */
  private final MethodInvocation methodInvocation;

  /** The policy decision point. */
  private final IPolicyDecisionPoint policyDecisionPoint;

  /** The filter object. */
  private Object filterObject;

  /** The return object. */
  private Object returnObject;

  /**
   * Constructs a new <b>SecureApiSecurityExpressionRoot</b>.
   *
   * @param authentication the authentication supplier
   * @param methodInvocation the method invocation
   * @param policyDecisionPoint the policy decision point
   * @param isSecurityEnabled is API security enabled for the Inception Framework
   * @param inDebugMode is debugging enabled for the Inception Framework
   */
  public SecureApiSecurityExpressionRoot(
      Supplier<Authentication> authentication,
      MethodInvocation methodInvocation,
      IPolicyDecisionPoint policyDecisionPoint,
      boolean isSecurityEnabled,
      boolean inDebugMode) {
    super(authentication);
    this.methodInvocation = methodInvocation;
    this.policyDecisionPoint = policyDecisionPoint;
    this.isSecurityEnabled = isSecurityEnabled;
    this.inDebugMode = inDebugMode;
  }

  /**
   * Constructs a new <b>SecureApiSecurityExpressionRoot</b>.
   *
   * @param authentication the authentication
   * @param methodInvocation the method invocation
   * @param policyDecisionPoint the policy decision point
   * @param isSecurityEnabled is API security enabled for the Inception Framework
   * @param inDebugMode is debugging enabled for the Inception Framework
   */
  public SecureApiSecurityExpressionRoot(
      Authentication authentication,
      MethodInvocation methodInvocation,
      IPolicyDecisionPoint policyDecisionPoint,
      boolean isSecurityEnabled,
      boolean inDebugMode) {
    this(
        () -> authentication,
        methodInvocation,
        policyDecisionPoint,
        isSecurityEnabled,
        inDebugMode);
  }

  /**
   * Authorize using the policy decision point.
   *
   * @return <b>true</b> if the policy decision point authorization was successful or <b>false</b>
   */
  public boolean authorize() {
    return authorize(null);
  }

  /**
   * Authorize using the policy decision point.
   *
   * @param policyDecisionPointContextProviderClass the policy decision point context provider class
   *     to use to populate the XACML decision request authorized by the policy decision point
   * @return <b>true</b> if the policy decision point authorization was successful or <b>false</b>
   */
  public boolean authorize(
      Class<? extends IPolicyDecisionPointContextProvider>
          policyDecisionPointContextProviderClass) {
    if (policyDecisionPoint != null) {
      return policyDecisionPoint.authorize(
          getAuthentication(), methodInvocation, policyDecisionPointContextProviderClass);
    } else {
      log.error("Authorization failed: No policy decision point implementation available");
      return false;
    }
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

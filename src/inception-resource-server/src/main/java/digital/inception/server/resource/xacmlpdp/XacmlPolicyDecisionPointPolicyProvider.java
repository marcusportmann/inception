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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import org.ow2.authzforce.xmlns.pdp.ext.AbstractPolicyProvider;

/**
 * The {@code XacmlPolicyDecisionPointPolicyProvider} class holds the configuration information for
 * the custom AuthzForce dynamic policy provider implementation, which provides the XACML-based
 * policy sets and policies that will be applied by the policy decision point.
 *
 * @author Marcus Portmann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XacmlPolicyDecisionPointPolicyProvider")
public class XacmlPolicyDecisionPointPolicyProvider extends AbstractPolicyProvider {

  /** Should policy sets and policies be loaded from the classpath under pdp/policies? */
  private Boolean classpathPoliciesEnabled;

  /** Should policy sets and policies be loaded by invoking an external API. */
  private Boolean externalPoliciesEnabled;

  /** The external API endpoint used to retrieve policy sets and policies. */
  private String externalPoliciesEndpoint;

  /** The reload period in seconds for external policies. */
  private int externalPoliciesReloadPeriod;

  /** Constructs a new {@code XacmlPolicyDecisionPointPolicyProvider}. */
  public XacmlPolicyDecisionPointPolicyProvider() {}

  /**
   * Constructs a new {@code XacmlPolicyDecisionPointPolicyProvider}.
   *
   * @param classpathPoliciesEnabled should policy sets and policies be loaded from the classpath
   *     under pdp/policies
   * @param externalPoliciesEnabled should policy sets and policies be loaded by invoking an
   *     external API
   * @param externalPoliciesEndpoint the external API endpoint used to retrieve policy sets and
   *     policies
   * @param externalPoliciesReloadPeriod the reload period in seconds for external policies
   */
  public XacmlPolicyDecisionPointPolicyProvider(
      boolean classpathPoliciesEnabled,
      boolean externalPoliciesEnabled,
      String externalPoliciesEndpoint,
      int externalPoliciesReloadPeriod) {
    this.classpathPoliciesEnabled = classpathPoliciesEnabled;
    this.externalPoliciesEnabled = externalPoliciesEnabled;
    this.externalPoliciesEndpoint = externalPoliciesEndpoint;
    this.externalPoliciesReloadPeriod = externalPoliciesReloadPeriod;
  }

  /**
   * Returns whether policy sets and policies be loaded from the classpath under pdp/policies.
   *
   * @return {@code true} if policy sets and policies should be loaded from the classpath under
   *     pdp/policies or {@code false} otherwise
   */
  public boolean getClasspathPoliciesEnabled() {
    if (classpathPoliciesEnabled == null) {
      return false;
    } else {
      return classpathPoliciesEnabled;
    }
  }

  /**
   * Returns whether policy sets and policies be loaded by invoking an external API.
   *
   * @return {@code true} if policy sets and policies be loaded by invoking an external API or
   *     {@code false} otherwise
   */
  public Boolean getExternalPoliciesEnabled() {
    return externalPoliciesEnabled;
  }

  /**
   * Returns the external API endpoint used to retrieve policy sets and policies.
   *
   * @return the external API endpoint used to retrieve policy sets and policies
   */
  public String getExternalPoliciesEndpoint() {
    return externalPoliciesEndpoint;
  }

  /**
   * Returns the reload period in seconds for external policies.
   *
   * @return the reload period in seconds for external policies
   */
  public int getExternalPoliciesReloadPeriod() {
    return externalPoliciesReloadPeriod;
  }

  /**
   * Set whether the policy sets and policies should be loaded from the classpath under
   * pdp/policies.
   *
   * @param classpathPoliciesEnabled {@code true} if the policy sets and policies be loaded from the
   *     classpath under pdp/policies or {@code false} otherwise
   */
  public void setClasspathPoliciesEnabled(Boolean classpathPoliciesEnabled) {
    this.classpathPoliciesEnabled = classpathPoliciesEnabled;
  }

  /**
   * Set whether policy sets and policies be loaded by invoking an external API.
   *
   * @param externalPoliciesEnabled {@code true} if policy sets and policies be loaded by invoking
   *     an external API or {@code false} otherwise
   */
  public void setExternalPoliciesEnabled(Boolean externalPoliciesEnabled) {
    this.externalPoliciesEnabled = externalPoliciesEnabled;
  }

  /**
   * Set the external API endpoint used to retrieve policy sets and policies.
   *
   * @param externalPoliciesEndpoint the external API endpoint used to retrieve policy sets and
   *     policies
   */
  public void setExternalPoliciesEndpoint(String externalPoliciesEndpoint) {
    this.externalPoliciesEndpoint = externalPoliciesEndpoint;
  }

  /**
   * Set the reload period in seconds for external policies.
   *
   * @param externalPoliciesReloadPeriod the reload period in seconds for external policies
   */
  public void setExternalPoliciesReloadPeriod(int externalPoliciesReloadPeriod) {
    this.externalPoliciesReloadPeriod = externalPoliciesReloadPeriod;
  }
}

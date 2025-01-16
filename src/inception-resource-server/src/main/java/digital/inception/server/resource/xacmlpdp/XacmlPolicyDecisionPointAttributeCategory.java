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

/**
 * The <b>XacmlPolicyDecisionPointAttributeCategory</b> enumeration defines the custom XACML
 * Attribute Category identifiers for the XACML policy decision point.
 *
 * @author Marcus Portmann
 */
public enum XacmlPolicyDecisionPointAttributeCategory {
  /** urn:inception:xacml:attribute-category:jwt-claims */
  JWT_CLAIMS("urn:inception:xacml:attribute-category:jwt-claims"),

  /** urn:inception:xacml:attribute-category:path-variables */
  PATH_VARIABLES("urn:inception:xacml:attribute-category:path-variables"),

  /** urn:inception:xacml:attribute-category:request-attributes */
  REQUEST_ATTRIBUTES("urn:inception:xacml:attribute-category:request-attributes"),

  /** urn:inception:xacml:attribute-category:request-parameters */
  REQUEST_PARAMETERS("urn:inception:xacml:attribute-category:request-parameters");

  /** The XACML identifier for this category. */
  private final String value;

  /**
   * Constructs a new <b>XacmlPolicyDecisionPointAttributeCategory</b>
   *
   * @param value the XACML identifier for this category
   */
  XacmlPolicyDecisionPointAttributeCategory(String value) {
    this.value = value;
  }

  /**
   * Returns the XACML identifier for this category.
   *
   * @return the XACML identifier for this category
   */
  public String value() {
    return value;
  }
}

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

package digital.inception.security.model;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>PolicyType</b> enumeration defines the possible policy types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The policy type")
@XmlEnum
@XmlType(name = "PolicyType", namespace = "https://inception.digital/security")
public enum PolicyType implements CodeEnum {
  /** XACML Policy Set. */
  @XmlEnumValue("XACMLPolicySet")
  XACML_POLICY_SET("xacml_policy_set", "XACML Policy Set"),

  /** XACML Policy. */
  @XmlEnumValue("XACMLPolicy")
  XACML_POLICY("xacml_policy", "XACML Policy");

  private final String code;

  private final String description;

  PolicyType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the policy type.
   *
   * @return the code for the policy type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the policy type.
   *
   * @return the description for the policy type
   */
  public String description() {
    return description;
  }
}

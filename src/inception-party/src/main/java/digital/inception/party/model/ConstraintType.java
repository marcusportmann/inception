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

package digital.inception.party.model;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>ConstraintType</b> enumeration defines the types of constraint that can be applied when
 * validating an attribute or preference for a party.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of constraint")
@XmlEnum
@XmlType(name = "ConstraintType", namespace = "https://inception.digital/party")
public enum ConstraintType implements CodeEnum {
  /** Maximum size. */
  @XmlEnumValue("MaxSize")
  MAX_SIZE("max_size", "Maximum Size"),

  /** Minimum size. */
  @XmlEnumValue("MinSize")
  MIN_SIZE("min_size", "Minimum Size"),

  /** Pattern. */
  @XmlEnumValue("Pattern")
  PATTERN("pattern", "Pattern"),

  /** Reference. */
  @XmlEnumValue("Reference")
  REFERENCE("reference", "Reference"),

  /** Required. */
  @XmlEnumValue("Required")
  REQUIRED("required", "Required"),

  /** Size. */
  @XmlEnumValue("Size")
  SIZE("size", "Size");

  private final String code;

  private final String description;

  ConstraintType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the constraint type.
   *
   * @return the code for the constraint type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the constraint type.
   *
   * @return the description for the constraint type
   */
  public String description() {
    return description;
  }
}

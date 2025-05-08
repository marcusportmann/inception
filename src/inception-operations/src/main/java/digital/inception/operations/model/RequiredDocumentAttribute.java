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

package digital.inception.operations.model;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code RequiredDocumentAttribute} enumeration defines the possible required document
 * attributes.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The required document attribute")
@XmlEnum
@XmlType(name = "RequiredDocumentAttribute", namespace = "https://inception.digital/operations")
public enum RequiredDocumentAttribute implements CodeEnum {

  /** Expiry Date. */
  @XmlEnumValue("ExpiryDate")
  EXPIRY_DATE("expiry_date", "Expiry Date"),

  /** External Reference. */
  @XmlEnumValue("ExternalReference")
  EXTERNAL_REFERENCE("external_reference", "External Reference"),

  /** Issue Date. */
  @XmlEnumValue("IssueDate")
  ISSUE_DATE("issue_date", "Issue Date");

  private final String code;

  private final String description;

  RequiredDocumentAttribute(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the required document attribute.
   *
   * @return the code for the required document attribute
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the required document attribute.
   *
   * @return the description for the required document attribute
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the enumeration value.
   *
   * @return the string representation of the enumeration value
   */
  public String toString() {
    return description;
  }
}

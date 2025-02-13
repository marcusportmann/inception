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
 * The <b>TokenSortBy</b> enumeration defines the possible methods used to sort a list of tokens.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The method used to sort the list of tokens")
@XmlEnum
@XmlType(name = "TokenSortBy", namespace = "https://inception.digital/security")
public enum TokenSortBy implements CodeEnum {
  /** Sort by expires. */
  @XmlEnumValue("Expires")
  EXPIRES("expires", "Sort By Expires"),

  /** Sort by issued. */
  @XmlEnumValue("Issued")
  ISSUED("issued", "Sort By Issued"),

  /** Sort by name. */
  @XmlEnumValue("Name")
  NAME("name", "Sort By Name"),

  /** Sort by revoked. */
  @XmlEnumValue("Revoked")
  REVOKED("revoked", "Sort By Revoked"),

  /** Sort by type. */
  @XmlEnumValue("Type")
  TYPE("type", "Sort By Type");

  private final String code;

  private final String description;

  TokenSortBy(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the method used to sort a list of tokens.
   *
   * @return the code for the method used to sort a list of tokens
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the method used to sort a list of tokens.
   *
   * @return the description for the method used to sort a list of tokens
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the method used to sort a list of tokens enumeration
   * value.
   *
   * @return the string representation of the method used to sort a list of tokens enumeration value
   */
  public String toString() {
    return description;
  }
}

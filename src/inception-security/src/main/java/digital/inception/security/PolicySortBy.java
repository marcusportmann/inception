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

package digital.inception.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>PolicySortBy</b> enumeration defines the possible methods used to sort a list of policies.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The method used to sort the list of policies")
@XmlEnum
@XmlType(name = "PolicySortBy", namespace = "http://inception.digital/security")
public enum PolicySortBy {
  /** Sort by name. */
  @XmlEnumValue("Name")
  NAME("name", "Sort By Name"),

  /** Sort by type. */
  @XmlEnumValue("Type")
  TYPE("type", "Sort By Type");

  private final String code;

  private final String description;

  PolicySortBy(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the method used to sort a list of policies given by the specified code value.
   *
   * @param code the code for the method used to sort a list of policies
   * @return the method used to sort a list of policies given by the specified code value
   */
  @JsonCreator
  public static PolicySortBy fromCode(String code) {
    switch (code) {
      case "name":
        return PolicySortBy.NAME;
      case "type":
        return PolicySortBy.TYPE;
      default:
        throw new RuntimeException(
            "Failed to determine the policy sort by with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the method used to sort a list of policies.
   *
   * @return the code for the method used to sort a list of policies
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the method used to sort a list of policies.
   *
   * @return the description for the method used to sort a list of policies
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the method used to sort a list of policies enumeration
   * value.
   *
   * @return the string representation of the method used to sort a list of policies enumeration
   *     value
   */
  public String toString() {
    return description;
  }
}

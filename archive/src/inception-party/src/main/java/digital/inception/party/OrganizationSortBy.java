/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>OrganizationSortBy</b> enumeration defines the possible methods used to sort a list of
 * organizations.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The method used to sort the list of organizations")
@XmlEnum
@XmlType(name = "OrganizationSortBy", namespace = "http://inception.digital/party")
public enum OrganizationSortBy {
  /** Sort by name. */
  @XmlEnumValue("Name")
  NAME("name", "Sort By Name");

  private final String code;

  private final String description;

  OrganizationSortBy(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the method used to sort a list of organizations given by the specified code value.
   *
   * @param code the code for the method used to sort a list of organizations
   * @return the method used to sort a list of organizations given by the specified code value
   */
  @JsonCreator
  public static OrganizationSortBy fromCode(String code) {
    switch (code) {
      case "name":
        return OrganizationSortBy.NAME;

      default:
        throw new RuntimeException(
            "Failed to determine the organization sort by with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the method used to sort a list of organizations.
   *
   * @return the code for the method used to sort a list of organizations
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the method used to sort a list of organizations.
   *
   * @return the description for the method used to sort a list of organizations
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the method used to sort a list of organizations enumeration
   * value.
   *
   * @return the string representation of the method used to sort a list of organizations
   *     enumeration value
   */
  public String toString() {
    return description;
  }
}

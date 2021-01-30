/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.banking.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>CustomerType</b> enumeration defines the possible customer types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The customer type")
@XmlEnum
@XmlType(name = "CustomerType", namespace = "http://customer.banking.inception.digital")
public enum CustomerType {
  @XmlEnumValue("Business")
  BUSINESS("business", "Business"),
  @XmlEnumValue("Individual")
  INDIVIDUAL("individual", "Individual"),
  @XmlEnumValue("Unknown")
  UNKNOWN("unknown", "Unknown");

  private final String code;

  private final String description;

  CustomerType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the customer type given by the specified code value.
   *
   * @param code the code for the customer type
   * @return the customer type given by the specified code value
   */
  @JsonCreator
  public static CustomerType fromCode(String code) {
    switch (code) {
      case "business":
        return BUSINESS;

      case "individual":
        return INDIVIDUAL;

      default:
        throw new RuntimeException(
            "Failed to determine the customer type with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the customer type.
   *
   * @return the code for the customer type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the customer type.
   *
   * @return the description for the customer type
   */
  public String description() {
    return description;
  }
}

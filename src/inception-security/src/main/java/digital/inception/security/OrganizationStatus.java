/*
 * Copyright 2020 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>OrganizationStatus</code> enumeration defines the possible statuses for an
 * organization.
 *
 * @author Marcus Portmann
 */
@Schema(description = "OrganizationStatus")
@XmlEnum
@XmlType(name = "OrganizationStatus", namespace = "http://security.inception.digital")
public enum OrganizationStatus {
  @XmlEnumValue("Inactive")
  INACTIVE(0, "Inactive"),
  @XmlEnumValue("Active")
  ACTIVE(1, "Active");

  private int code;

  private String description;

  OrganizationStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the organization status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the organization status
   *
   * @return the organization status given by the specified numeric code value
   */
  @JsonCreator
  public static OrganizationStatus fromCode(int code) {
    switch (code) {
      case 0:
        return OrganizationStatus.INACTIVE;

      case 1:
        return OrganizationStatus.ACTIVE;

      default:
        return OrganizationStatus.INACTIVE;
    }
  }

  /**
   * Returns the numeric code value for the organization status.
   *
   * @return the numeric code value for the organization status
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the description for the organization status.
   *
   * @return the description for the organization status
   */
  public String description() {
    return description;
  }
}

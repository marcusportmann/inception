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
 * The <code>TenantStatus</code> enumeration defines the possible statuses for a tenant.
 *
 * @author Marcus Portmann
 */
@Schema(description = "TenantStatus")
@XmlEnum
@XmlType(name = "TenantStatus", namespace = "http://security.inception.digital")
public enum TenantStatus {
  @XmlEnumValue("Inactive")
  INACTIVE(0, "Inactive"),
  @XmlEnumValue("Active")
  ACTIVE(1, "Active");

  private final int code;

  private final String description;

  TenantStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the tenant status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the tenant status
   * @return the tenant status given by the specified numeric code value
   */
  @JsonCreator
  public static TenantStatus fromCode(int code) {
    switch (code) {
      case 0:
        return TenantStatus.INACTIVE;

      case 1:
        return TenantStatus.ACTIVE;

      default:
        return TenantStatus.INACTIVE;
    }
  }

  /**
   * Returns the numeric code value for the tenant status.
   *
   * @return the numeric code value for the tenant status
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the description for the tenant status.
   *
   * @return the description for the tenant status
   */
  public String description() {
    return description;
  }
}

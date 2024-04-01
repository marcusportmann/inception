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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>TenantStatus</b> enumeration defines the possible statuses for a tenant.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The tenant status")
@XmlEnum
@XmlType(name = "TenantStatus", namespace = "http://inception.digital/security")
public enum TenantStatus {
  /** Inactive. */
  @XmlEnumValue("Inactive")
  INACTIVE("inactive", "Inactive"),

  /** Active. */
  @XmlEnumValue("Active")
  ACTIVE("active", "Active");

  private final String code;

  private final String description;

  TenantStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the tenant status given by the specified code value.
   *
   * @param code the code for the tenant status
   * @return the tenant status given by the specified code value
   */
  @JsonCreator
  public static TenantStatus fromCode(String code) {
    switch (code) {
      case "inactive":
        return TenantStatus.INACTIVE;

      case "active":
        return TenantStatus.ACTIVE;

      default:
        throw new RuntimeException(
            "Failed to determine the tenant status with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code value for the tenant status.
   *
   * @return the code value for the tenant status
   */
  @JsonValue
  public String code() {
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

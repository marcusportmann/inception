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
 * The <b>TokenStatus</b> enumeration defines the possible token statuses.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The token status")
@XmlEnum
@XmlType(name = "TokenStatus", namespace = "http://digital.inception.co.za/security")
public enum TokenStatus {

  /** All. */
  @XmlEnumValue("All")
  ALL("all", "All"),

  /** Active. */
  @XmlEnumValue("Active")
  ACTIVE("active", "Active"),

  /** Expired. */
  @XmlEnumValue("Expired")
  EXPIRED("expired", "Expired"),

  /** Revoked. */
  @XmlEnumValue("Revoked")
  REVOKED("revoked", "Revoked"),

  /** Pending. */
  @XmlEnumValue("Pending")
  PENDING("pending", "Pending");

  private final String code;

  private final String description;

  TokenStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the token status given by the specified code value.
   *
   * @param code the code for the token status
   * @return the token status given by the specified code value
   */
  @JsonCreator
  public static TokenStatus fromCode(String code) {
    switch (code) {
      case "all":
        return TokenStatus.ALL;
      case "active":
        return TokenStatus.ACTIVE;
      case "expired":
        return TokenStatus.EXPIRED;
      case "pending":
        return TokenStatus.PENDING;
      case "revoked":
        return TokenStatus.REVOKED;
      default:
        throw new RuntimeException(
            "Failed to determine the token status with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the token status.
   *
   * @return the code for the token status
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the token status.
   *
   * @return the description for the token status
   */
  public String description() {
    return description;
  }
}

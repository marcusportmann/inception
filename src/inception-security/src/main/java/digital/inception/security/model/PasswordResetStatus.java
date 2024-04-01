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
 * The <b>PasswordResetStatus</b> enumeration defines the possible statuses for a password reset.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The password reset status")
@XmlEnum
@XmlType(name = "PasswordResetStatus", namespace = "http://inception.digital/security")
public enum PasswordResetStatus {
  /** Requested. */
  @XmlEnumValue("Requested")
  REQUESTED("requested", "Requested"),

  /** Completed. */
  @XmlEnumValue("Completed")
  COMPLETED("completed", "Completed"),

  /** Expired. */
  @XmlEnumValue("Expired")
  EXPIRED("expired", "Expired");

  private final String code;

  private final String description;

  PasswordResetStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the password reset status given by the specified code value.
   *
   * @param code the code for the password reset status
   * @return the password reset status given by the specified code value
   */
  @JsonCreator
  public static PasswordResetStatus fromCode(String code) {
    switch (code) {
      case "requested":
        return PasswordResetStatus.REQUESTED;
      case "completed":
        return PasswordResetStatus.COMPLETED;
      case "expired":
        return PasswordResetStatus.EXPIRED;
      default:
        throw new RuntimeException(
            "Failed to determine the password reset status with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code value for the password reset status.
   *
   * @return the code value for the password reset status
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the password reset status.
   *
   * @return the description for the password reset status
   */
  public String description() {
    return description;
  }
}

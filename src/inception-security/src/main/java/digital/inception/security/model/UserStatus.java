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
 * The {@code UserStatus} enumeration defines the possible statuses for a user.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The user status")
@XmlEnum
@XmlType(name = "UserStatus", namespace = "https://inception.digital/security")
public enum UserStatus implements CodeEnum {
  /** Inactive. */
  @XmlEnumValue("Inactive")
  INACTIVE("inactive", "Inactive"),

  /** Active. */
  @XmlEnumValue("Active")
  ACTIVE("active", "Active"),

  /** Locked. */
  @XmlEnumValue("Locked")
  LOCKED("locked", "Locked"),

  /** Expired. */
  @XmlEnumValue("Expired")
  EXPIRED("expired", "Expired");

  private final String code;

  private final String description;

  UserStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code value for the user status.
   *
   * @return the code value for the user status
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the user status.
   *
   * @return the description for the user status
   */
  public String description() {
    return description;
  }
}

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
 * The <b>PasswordChangeReason</b> enumeration defines the possible reasons for why a user's
 * password was changed.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The password change reason")
@XmlEnum
@XmlType(name = "PasswordChangeReason", namespace = "https://inception.digital/security")
public enum PasswordChangeReason implements CodeEnum {
  /** User. */
  @XmlEnumValue("User")
  USER("user", "User"),

  /** Administrative. */
  @XmlEnumValue("Administrative")
  ADMINISTRATIVE("administrative", "Administrative"),

  /** Reset. */
  @XmlEnumValue("Reset")
  RESET("reset", "Reset");

  private final String code;

  private final String description;

  PasswordChangeReason(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the password change reason.
   *
   * @return the code for the password change reason
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the password change reason.
   *
   * @return the description for the password change reason
   */
  public String description() {
    return description;
  }
}

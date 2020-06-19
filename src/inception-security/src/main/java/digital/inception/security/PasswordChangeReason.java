/*
 * Copyright 2019 Marcus Portmann
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
 * The <code>PasswordChangeReason</code> enumeration defines the possible reasons for why a user's
 * password was changed.
 *
 * @author Marcus Portmann
 */
@Schema(description = "PasswordChangeReason")
@XmlEnum
@XmlType(name = "PasswordChangeReason", namespace = "http://security.inception.digital")
public enum PasswordChangeReason {
  @XmlEnumValue("User")
  USER(0, "User"),
  @XmlEnumValue("Administrative")
  ADMINISTRATIVE(1, "Administrative"),
  @XmlEnumValue("Reset")
  RESET(2, "Reset");

  private int code;
  private String description;

  PasswordChangeReason(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the password change reason given by the specified code value.
   *
   * @param code the code value identifying the password change reason
   * @return the password change reason given by the specified code value
   */
  @JsonCreator
  public static PasswordChangeReason fromCode(int code) {
    switch (code) {
      case 0:
        return PasswordChangeReason.USER;

      case 1:
        return PasswordChangeReason.ADMINISTRATIVE;

      default:
        return PasswordChangeReason.RESET;
    }
  }

  /**
   * Returns the numeric code value identifying for the password change reason.
   *
   * @return the numeric code value identifying for the password change reason
   */
  @JsonValue
  public int code() {
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

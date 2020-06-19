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

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>PasswordResetStatus</code> enumeration defines the possible statuses for a password
 * reset.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "PasswordResetStatus")
@XmlEnum
@XmlType(name = "PasswordResetStatus", namespace = "http://security.inception.digital")
public enum PasswordResetStatus {
  @XmlEnumValue("Unknown")
  UNKNOWN(0, "Unknown"),
  @XmlEnumValue("Requested")
  REQUESTED(1, "Requested"),
  @XmlEnumValue("Completed")
  COMPLETED(2, "Completed"),
  @XmlEnumValue("Expired")
  EXPIRED(3, "Expired");

  private int code;
  private String description;

  PasswordResetStatus(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the password reset status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the password reset status
   *
   * @return the password reset status given by the specified numeric code value
   */
  @JsonCreator
  public static PasswordResetStatus fromCode(int code) {
    switch (code) {
      case 0:
        return PasswordResetStatus.UNKNOWN;

      case 1:
        return PasswordResetStatus.REQUESTED;

      case 2:
        return PasswordResetStatus.COMPLETED;

      case 3:
        return PasswordResetStatus.EXPIRED;

      default:
        return PasswordResetStatus.UNKNOWN;
    }
  }

  /**
   * Returns the numeric code value for the password reset status.
   *
   * @return the numeric code value for the password reset status
   */
  @JsonValue
  public int code() {
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

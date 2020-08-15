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
 * The <code>GroupMemberType</code> enumeration defines the possible group member types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "GroupMemberType")
@XmlEnum
@XmlType(name = "GroupMemberType", namespace = "http://security.inception.digital")
public enum GroupMemberType {
  @XmlEnumValue("User")
  USER(0, "User"),
  @XmlEnumValue("Group")
  GROUP(1, "Group");

  private int code;

  private String description;

  GroupMemberType(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the group member type given by the specified code value.
   *
   * @param code the code value identifying the group member type
   * @return the group member type given by the specified code value
   */
  @JsonCreator
  public static GroupMemberType fromCode(int code) {
    switch (code) {
      case 0:
        return GroupMemberType.USER;

      case 1:
        return GroupMemberType.GROUP;

      default:
        return GroupMemberType.USER;
    }
  }

  /**
   * Returns the numeric code value identifying for the group member type.
   *
   * @return the numeric code value identifying for the group member type
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the description for the group member type.
   *
   * @return the description for the group member type
   */
  public String description() {
    return description;
  }
}

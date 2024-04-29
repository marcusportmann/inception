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
 * The <b>GroupMemberType</b> enumeration defines the possible group member types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The group member type")
@XmlEnum
@XmlType(name = "GroupMemberType", namespace = "https://inception.digital/security")
public enum GroupMemberType {
  /** User. */
  @XmlEnumValue("User")
  USER("user", "User"),

  /** Group. */
  @XmlEnumValue("Group")
  GROUP("group", "Group");

  private final String code;

  private final String description;

  GroupMemberType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the group member type given by the specified code value.
   *
   * @param code the code for the group member type
   * @return the group member type given by the specified code value
   */
  @JsonCreator
  public static GroupMemberType fromCode(String code) {
    switch (code) {
      case "user":
        return GroupMemberType.USER;
      case "group":
        return GroupMemberType.GROUP;
      default:
        throw new RuntimeException(
            "Failed to determine the group member type with the invalid code (" + code + ")");
    }
  }

  /**
   * Returns the code for the group member type.
   *
   * @return the code for the group member type
   */
  @JsonValue
  public String code() {
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

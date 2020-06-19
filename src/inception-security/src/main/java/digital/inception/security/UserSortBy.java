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
 * The <code>UserSortBy</code> enumeration defines the possible methods used to sort a list of
 * users.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "UserSortBy")
@XmlEnum
@XmlType(name = "UserSortBy", namespace = "http://security.inception.digital")
public enum UserSortBy {
  /**
   * Sort by first name.
   */
  @XmlEnumValue("FirstName")
  FIRST_NAME(0, "Sort By First Name"),

  /**
   * Sort by last name.
   */
  @XmlEnumValue("LastName")
  LAST_NAME(1, "Sort By Last Name"),

  /**
   * Sort by username.
   */
  @XmlEnumValue("Username")
  USERNAME(2, "Sort By Username");

  private int code;
  private String description;

  UserSortBy(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the method used to sort a list of users given by the specified code value.
   *
   * @param code the code value identifying the method used to sort a list of users
   *
   * @return the method used to sort a list of users given by the specified code value
   */
  @JsonCreator
  public static UserSortBy fromCode(int code) {
    switch (code) {
      case 0:
        return UserSortBy.FIRST_NAME;

      case 1:
        return UserSortBy.LAST_NAME;

      default:
        return UserSortBy.USERNAME;
    }
  }

  /**
   * Returns the method used to sort a list of users given by the specified code value.
   *
   * @param code the code value identifying the method used to sort a list of users
   *
   * @return the method used to sort a list of users given by the specified code value
   */
  public static UserSortBy fromCode(String code) {
    return fromCode(Integer.parseInt(code));
  }

  /**
   * Returns the code value identifying the method used to sort a list of users.
   *
   * @return the code value identifying the method used to sort a list of users
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the description for the method used to sort a list of users.
   *
   * @return the description for the method used to sort a list of users
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the method used to sort a list of users enumeration value.
   *
   * @return the string representation of the method used to sort a list of users enumeration value
   */
  public String toString() {
    return description;
  }
}

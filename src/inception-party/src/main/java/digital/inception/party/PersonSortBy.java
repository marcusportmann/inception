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

package digital.inception.party;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>PersonSortBy</code> enumeration defines the possible methods used to sort a list of
 * persons.
 *
 * @author Marcus Portmann
 */
@Schema(description = "PersonSortBy")
@XmlEnum
@XmlType(name = "PersonSortBy", namespace = "http://party.inception.digital")
public enum PersonSortBy {
  /**
   * Sort by name.
   */
  @XmlEnumValue("Name")
  NAME(0, "Sort By Name"),
  /**
   * Sort by preferred name.
   */
  @XmlEnumValue("PreferredName")
  PREFERRED_NAME(1, "Sort By Preferred Name");

  private final int code;

  private final String description;

  PersonSortBy(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the method used to sort a list of persons given by the specified code value.
   *
   * @param code the code value identifying the method used to sort a list of persons
   *
   * @return the method used to sort a list of persons given by the specified code value
   */
  @JsonCreator
  public static PersonSortBy fromCode(int code) {
    switch (code) {
      case 0:
        return PersonSortBy.NAME;

      case 1:
        return PersonSortBy.PREFERRED_NAME;

      default:
        return PersonSortBy.NAME;
    }
  }

  /**
   * Returns the method used to sort a list of persons given by the specified code value.
   *
   * @param code the code value identifying the method used to sort a list of persons
   *
   * @return the method used to sort a list of persons given by the specified code value
   */
  public static PersonSortBy fromCode(String code) {
    return fromCode(Integer.parseInt(code));
  }

  /**
   * Returns the code value identifying the method used to sort a list of persons.
   *
   * @return the code value identifying the method used to sort a list of persons
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the description for the method used to sort a list of persons.
   *
   * @return the description for the method used to sort a list of persons
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the method used to sort a list of persons enumeration
   * value.
   *
   * @return the string representation of the method used to sort a list of persons enumeration
   * value
   */
  public String toString() {
    return description;
  }
}

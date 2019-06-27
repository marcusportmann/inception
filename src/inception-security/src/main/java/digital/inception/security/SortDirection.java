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

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>SortDirection</code> enumeration defines the possible sort directions when retrieving
 * security related entities.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "SortDirection")
@XmlEnum
@XmlType(name = "SortDirection", namespace = "http://security.inception.digital")
public enum SortDirection
{
  /**
   * Sort ascending.
   */
  @XmlEnumValue("Ascending")
  ASCENDING("asc", "Ascending"),

  /**
   * Sort descending.
   */
  @XmlEnumValue("Descending")
  DESCENDING("desc", "Descending");

  private String code;
  private String name;

  SortDirection(String code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the sort direction given by the specified code value.
   *
   * @param code the code value identifying the sort direction
   *
   * @return the sort direction given by the specified code value
   */
  @JsonCreator
  public static SortDirection fromCode(String code)
  {
    switch (code)
    {
      case "desc":
        return SortDirection.DESCENDING;

      default:
        return SortDirection.ASCENDING;
    }
  }

  /**
   * Returns the code value identifying the sort direction.
   *
   * @return the code value identifying the sort direction
   */
  @JsonValue
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the name of the sort direction.
   *
   * @return the name of the sort direction
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the sort direction enumeration value.
   *
   * @return the string representation of the sort direction enumeration value
   */
  public String toString()
  {
    return name;
  }
}

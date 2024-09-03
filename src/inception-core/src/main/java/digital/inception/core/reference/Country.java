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

package digital.inception.core.reference;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * The <b>Country</b> class holds the information for a country.
 *
 * @author Marcus Portmann
 */
@Getter
@Setter
public class Country implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 alpha-2 code for the country. */
  private String code;

  /** The description for the country. */
  private String description;

  /** The ISO 3166-1 alpha-3 code for the country. */
  private String iso3Code;

  /** The name of the country. */
  private String name;

  /** The nationality for the country. */
  private String nationality;

  /** The short name for the country. */
  private String shortName;

  /** The sort index for the country. */
  private Integer sortIndex;

  /** The ISO 3166-1 alpha-2 code for the sovereign state the country is associated with. */
  private String sovereignState;

  /** Constructs a new <b>Country</b>. */
  public Country() {}

  /**
   * Constructs a new <b>Country</b>.
   *
   * @param code the ISO 3166-1 alpha-2 code for the country
   * @param iso3Code the ISO 3166-1 alpha-3 code for the country
   * @param sortIndex the sort index for the country
   * @param name the name of the country
   * @param shortName the short name for the country
   * @param description the description for the country
   * @param sovereignState the ISO 3166-1 alpha-2 code for the sovereign state the country is
   *     associated with
   * @param nationality the nationality for the country
   */
  public Country(
      String code,
      String iso3Code,
      Integer sortIndex,
      String name,
      String shortName,
      String description,
      String sovereignState,
      String nationality) {
    this.code = code;
    this.iso3Code = iso3Code;
    this.sortIndex = sortIndex;
    this.name = name;
    this.shortName = shortName;
    this.description = description;
    this.sovereignState = sovereignState;
    this.nationality = nationality;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    Country other = (Country) object;

    return Objects.equals(code, other.code);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((code == null) ? 0 : code.hashCode());
  }
}

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

/**
 * The {@code Country} class holds the information for a country.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
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

  /** Constructs a new {@code Country}. */
  public Country() {}

  /**
   * Constructs a new {@code Country}.
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
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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
   * Returns the ISO 3166-1 alpha-2 code for the country.
   *
   * @return the ISO 3166-1 alpha-2 code for the country
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the country.
   *
   * @return the description for the country
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the ISO 3166-1 alpha-3 code for the country.
   *
   * @return the ISO 3166-1 alpha-3 code for the country
   */
  public String getIso3Code() {
    return iso3Code;
  }

  /**
   * Returns the name of the country.
   *
   * @return the name of the country
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the nationality for the country.
   *
   * @return the nationality for the country
   */
  public String getNationality() {
    return nationality;
  }

  /**
   * Returns the short name for the country.
   *
   * @return the short name for the country
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Returns the sort index for the country.
   *
   * @return the sort index for the country
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the sovereign state the country is associated with.
   *
   * @return the ISO 3166-1 alpha-2 code for the sovereign state the country is associated with
   */
  public String getSovereignState() {
    return sovereignState;
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

  /**
   * Set the ISO 3166-1 alpha-2 code for the country.
   *
   * @param code the ISO 3166-1 alpha-2 code for the country
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the country.
   *
   * @param description the description for the country
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the ISO 3166-1 alpha-3 code for the country.
   *
   * @param iso3Code the ISO 3166-1 alpha-3 code for the country
   */
  public void setIso3Code(String iso3Code) {
    this.iso3Code = iso3Code;
  }

  /**
   * Set the name of the country.
   *
   * @param name the name of the country
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the nationality for the country.
   *
   * @param nationality the nationality for the country
   */
  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  /**
   * Set the short name for the country.
   *
   * @param shortName the short name for the country
   */
  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  /**
   * Set the sort index for the country.
   *
   * @param sortIndex the sort index for the country
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the sovereign state the country is associated with.
   *
   * @param sovereignState the ISO 3166-1 alpha-2 code for the sovereign state the country is
   *     associated with
   */
  public void setSovereignState(String sovereignState) {
    this.sovereignState = sovereignState;
  }
}

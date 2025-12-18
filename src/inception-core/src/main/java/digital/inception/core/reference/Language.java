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
 * The {@code Language} class holds the information for a language.
 *
 * <p>See: https://en.wikipedia.org/wiki/List_of_official_languages_by_country_and_territory
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class Language implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 639-1 alpha-2 code for the language. */
  private String code;

  /** The description for the language. */
  private String description;

  /** The ISO 639-2 alpha-3 code for the language. */
  private String iso3Code;

  /** The name of the language. */
  private String name;

  /** The short name for the language. */
  private String shortName;

  /** The sort order for the language. */
  private Integer sortOrder;

  /** Constructs a new {@code Language}. */
  public Language() {}

  /**
   * Constructs a new {@code Language}.
   *
   * @param code the ISO 639-1 alpha-2 code for the language
   * @param iso3Code the ISO 639-2 alpha-3 code for the language
   * @param sortOrder the sort order for the language
   * @param name the name of the language
   * @param shortName the short name for the language
   * @param description description for the language
   */
  public Language(
      String code,
      String iso3Code,
      Integer sortOrder,
      String name,
      String shortName,
      String description) {
    this.code = code;
    this.iso3Code = iso3Code;
    this.sortOrder = sortOrder;
    this.name = name;
    this.shortName = shortName;
    this.description = description;
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

    Language other = (Language) object;

    return Objects.equals(code, other.code);
  }

  /**
   * Returns the ISO 639-1 alpha-2 code for the language.
   *
   * @return the ISO 639-1 alpha-2 code for the language
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the language.
   *
   * @return the description for the language
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the ISO 639-2 alpha-3 code for the language.
   *
   * @return the ISO 639-2 alpha-3 code for the language
   */
  public String getIso3Code() {
    return iso3Code;
  }

  /**
   * Returns the name of the language.
   *
   * @return the name of the language
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the short name for the language.
   *
   * @return the short name for the language
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Returns the sort order for the language.
   *
   * @return the sort order for the language
   */
  public Integer getSortOrder() {
    return sortOrder;
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
   * Sets the ISO 639-1 alpha-2 code for the language.
   *
   * @param code the ISO 639-1 alpha-2 code for the language
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Sets the description for the language.
   *
   * @param description the description for the language
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the ISO 639-2 alpha-3 code for the language.
   *
   * @param iso3Code the ISO 639-2 alpha-3 code for the language
   */
  public void setIso3Code(String iso3Code) {
    this.iso3Code = iso3Code;
  }

  /**
   * Sets the name of the language.
   *
   * @param name the name of the language
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the short name for the language.
   *
   * @param shortName the short name for the language
   */
  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  /**
   * Sets the sort order for the language.
   *
   * @param sortOrder the sort order for the language
   */
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
}

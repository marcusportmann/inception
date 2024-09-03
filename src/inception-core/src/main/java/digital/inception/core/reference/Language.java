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
 * The <b>Language</b> class holds the information for a language.
 *
 * <p>See: https://en.wikipedia.org/wiki/List_of_official_languages_by_country_and_territory
 *
 * @author Marcus Portmann
 */
@Getter
@Setter
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

  /** The sort index for the language. */
  private Integer sortIndex;

  /** Constructs a new <b>Language</b>. */
  public Language() {}

  /**
   * Constructs a new <b>Language</b>.
   *
   * @param code the ISO 639-1 alpha-2 code for the language
   * @param iso3Code the ISO 639-2 alpha-3 code for the language
   * @param sortIndex the sort index for the language
   * @param name the name of the language
   * @param shortName the short name for the language
   * @param description description for the language
   */
  public Language(
      String code,
      String iso3Code,
      Integer sortIndex,
      String name,
      String shortName,
      String description) {
    this.code = code;
    this.iso3Code = iso3Code;
    this.sortIndex = sortIndex;
    this.name = name;
    this.shortName = shortName;
    this.description = description;
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

    Language other = (Language) object;

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

/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.reference;

// ~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.Objects;

/**
 * The <code>LanguageId</code> class implements the ID class for the <code>Language</code> class.
 *
 * <p>See: https://en.wikipedia.org/wiki/List_of_official_languages_by_country_and_territory
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class LanguageId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the language. */
  private String code;

  /** The Unicode locale identifier for the language. */
  private String localeId;

  /** Constructs a new <code>LanguageId</code>. */
  public LanguageId() {}

  /**
   * Constructs a new <code>LanguageId</code>.
   *
   * @param code the code for the language
   * @param localeId the Unicode locale identifier for the language
   */
  public LanguageId(String code, String localeId) {
    this.code = code;
    this.localeId = localeId;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
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

    LanguageId other = (LanguageId) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the language.
   *
   * @return the code for the language
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the Unicode locale identifier for the language.
   *
   * @return the Unicode locale identifier for the language
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((code == null) ? 0 : code.hashCode()) + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Set the code for the language.
   *
   * @param code the code for the language
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the Unicode locale identifier for the language.
   *
   * @param locale the Unicode locale identifier for the language
   */
  public void setLocaleId(String locale) {
    this.localeId = locale;
  }
}

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

package digital.inception.reference;

// ~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.Objects;

/**
 * The <code>RegionId</code> class implements the ID class for the <code>Region</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class RegionId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the region. */
  private String code;

  /** The code for the country the region is associated with. */
  private String country;

  /** The Unicode locale identifier for the region. */
  private String locale;

  /** Constructs a new <code>RegionId</code>. */
  public RegionId() {}

  /**
   * Constructs a new <code>RegionId</code>.
   *
   * @param country the code for the country the region is associated with
   * @param code the code for the region
   * @param locale the Unicode locale identifier for the region
   */
  public RegionId(String country, String code, String locale) {
    this.country = country;
    this.code = code;
    this.locale = locale;
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

    RegionId other = (RegionId) object;

    return Objects.equals(country, other.country)
        && Objects.equals(code, other.code)
        && Objects.equals(locale, other.locale);
  }

  /**
   * Returns the code for the region.
   *
   * @return the code for the region
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the code for the country the region is associated with.
   *
   * @return the code for the country the region is associated with
   */
  public String getCountry() {
    return country;
  }

  /**
   * Returns the Unicode locale identifier for the region.
   *
   * @return the Unicode locale identifier for the region
   */
  public String getLocale() {
    return locale;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((country == null) ? 0 : country.hashCode())
        + ((code == null) ? 0 : code.hashCode())
        + ((locale == null) ? 0 : locale.hashCode());
  }

  /**
   * Set the code for the region.
   *
   * @param code the code for the region
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the code for the country the region is associated with.
   *
   * @param country the code for the country the region is associated with
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Set the Unicode locale identifier for the region.
   *
   * @param locale the Unicode locale identifier for the region
   */
  public void setLocale(String locale) {
    this.locale = locale;
  }
}

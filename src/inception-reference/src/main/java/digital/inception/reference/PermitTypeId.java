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
 * The <code>PermitTypeId</code> class implements the ID class for the <code>
 * PermitType</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class PermitTypeId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the permit type. */
  private String code;

  /** The Unicode locale identifier for the permit type. */
  private String locale;

  /** Constructs a new <code>PermitTypeId</code>. */
  public PermitTypeId() {}

  /**
   * Constructs a new <code>PermitTypeId</code>.
   *
   * @param code the code for the permit type
   * @param locale the Unicode locale identifier for the permit type
   */
  public PermitTypeId(String code, String locale) {
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

    PermitTypeId other = (PermitTypeId) object;

    return Objects.equals(code, other.code) && Objects.equals(locale, other.locale);
  }

  /**
   * Returns the code for the permit type.
   *
   * @return the code for the permit type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the Unicode locale identifier for the permit type.
   *
   * @return the Unicode locale identifier for the permit type
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
    return ((code == null) ? 0 : code.hashCode()) + ((locale == null) ? 0 : locale.hashCode());
  }

  /**
   * Set the code for the permit type.
   *
   * @param code the code for the permit type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the Unicode locale identifier for the permit type.
   *
   * @param locale the Unicode locale identifier for the permit type
   */
  public void setLocale(String locale) {
    this.locale = locale;
  }
}

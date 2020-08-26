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
 * The <code>NextOfKinTypeId</code> class implements the ID class for the <code>NextOfKinType</code>
 * class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class NextOfKinTypeId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the next of kin type. */
  private String code;

  /** The Unicode locale identifier for the next of kin type. */
  private String localeId;

  /** Constructs a new <code>NextOfKinTypeId</code>. */
  public NextOfKinTypeId() {}

  /**
   * Constructs a new <code>NextOfKinTypeId</code>.
   *
   * @param code the code for the next of kin type
   * @param localeId the Unicode locale identifier for the next of kin type
   */
  public NextOfKinTypeId(String code, String localeId) {
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

    NextOfKinTypeId other = (NextOfKinTypeId) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the next of kin type.
   *
   * @return the code for the next of kin type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the Unicode locale identifier for the next of kin type.
   *
   * @return the Unicode locale identifier for the next of kin type
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
   * Set the code for the next of kin type.
   *
   * @param code the code for the next of kin type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the Unicode locale identifier for the next of kin type.
   *
   * @param locale the Unicode locale identifier for the next of kin type
   */
  public void setLocaleId(String locale) {
    this.localeId = locale;
  }
}

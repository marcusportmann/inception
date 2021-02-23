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

package digital.inception.party;

import java.io.Serializable;
import java.util.Objects;

/**
 * The <b>PhysicalAddressPurposeId</b> class implements the ID class for the <b>
 * PhysicalAddressPurpose</b> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class PhysicalAddressPurposeId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the physical address purpose. */
  private String code;

  /** The Unicode locale identifier for the physical address purpose. */
  private String localeId;

  /** Constructs a new <b>PhysicalAddressPurposeId</b>. */
  public PhysicalAddressPurposeId() {}

  /**
   * Constructs a new <b>PhysicalAddressPurposeId</b>.
   *
   * @param code the code for the physical address purpose
   * @param localeId the Unicode locale identifier for the physical address purpose
   */
  public PhysicalAddressPurposeId(String code, String localeId) {
    this.code = code;
    this.localeId = localeId;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
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

    PhysicalAddressPurposeId other = (PhysicalAddressPurposeId) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
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
}

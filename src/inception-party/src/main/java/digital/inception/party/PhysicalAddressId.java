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

package digital.inception.party;

import java.io.Serializable;
import java.util.Objects;

/**
 * The <code>PhysicalAddressId</code> class implements the ID class for the <code>PhysicalAddress
 * </code> class.
 *
 * @author Marcus Portmann
 */
public class PhysicalAddressId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The party the physical address is associated with. */
  private Party party;

  /** The physical address purpose. */
  private Integer purpose;

  /** The physical address type. */
  private Integer type;

  /** Constructs a new <code>PhysicalAddressId</code>. */
  public PhysicalAddressId() {}

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

    PhysicalAddressId other = (PhysicalAddressId) object;

    return Objects.equals(party, other.party)
        && Objects.equals(type, other.type)
        && Objects.equals(purpose, other.purpose);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((party == null) ? 0 : party.hashCode())
        + ((type == null) ? 0 : type.hashCode())
        + ((purpose == null) ? 0 : purpose.hashCode());
  }
}

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

package digital.inception.party.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code IndustryAllocationId} class implements the ID class for the {@code IndustryAllocation}
 * class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"UnusedDeclaration"})
public class IndustryAllocationId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the industry classification. */
  private String industry;

  /** The organization the industry allocation is associated with. */
  private Organization organization;

  /** The code for the industry classification system. */
  private String system;

  /** Constructs a new {@code IndustryAllocationId}. */
  public IndustryAllocationId() {}

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

    IndustryAllocationId other = (IndustryAllocationId) object;

    return Objects.equals(organization, other.organization)
        && Objects.equals(system, other.system)
        && Objects.equals(industry, other.industry);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((organization == null) ? 0 : organization.hashCode())
        + ((system == null) ? 0 : system.hashCode())
        + ((industry == null) ? 0 : industry.hashCode());
  }
}

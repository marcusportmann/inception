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
 * The {@code SegmentAllocationId} class implements the ID class for the {@code SegmentAllocation}
 * class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"UnusedDeclaration"})
public class SegmentAllocationId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The party the segment allocation is associated with. */
  private PartyBase party;

  /** The code for the segment. */
  private String segment;

  /** Constructs a new {@code SegmentAllocationId}. */
  public SegmentAllocationId() {}

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

    SegmentAllocationId other = (SegmentAllocationId) object;

    return Objects.equals(party, other.party) && Objects.equals(segment, other.segment);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((party == null) ? 0 : party.hashCode()) + ((segment == null) ? 0 : segment.hashCode());
  }
}

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
import java.util.UUID;

/**
 * The {@code MandataryId} class implements the ID class for The {@code Mandatary} class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"UnusedDeclaration"})
public class MandataryId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the mandate the mandatary is associated with. */
  private UUID mandate;

  /** The ID for the party who is the recipient of the mandate. */
  private UUID partyId;

  /** Creates a new {@code MandataryId} instance. */
  public MandataryId() {}

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

    MandataryId other = (MandataryId) object;

    return Objects.equals(mandate, other.mandate) && Objects.equals(partyId, other.partyId);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((mandate == null) ? 0 : mandate.hashCode())
        + ((partyId == null) ? 0 : partyId.hashCode());
  }
}

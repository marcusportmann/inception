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
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * The <b>ResidencePermitId</b> class implements the ID class for the <b>ResidencePermit</b> class.
 *
 * @author Marcus Portmann
 */
public class ResidencePermitId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 alpha-2 code for the country of issue for the residence permit. */
  private String countryOfIssue;

  /** The date of issue for the residence permit. */
  private LocalDate dateOfIssue;

  /**
   * The Universally Unique Identifier (UUID) for the person the residence permit is associated
   * with.
   */
  private UUID person;

  /** The code for the residence permit type. */
  private String type;

  /** Constructs a new <b>ResidencePermitId</b>. */
  public ResidencePermitId() {}

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

    ResidencePermitId other = (ResidencePermitId) object;

    return Objects.equals(person, other.person)
        && Objects.equals(type, other.type)
        && Objects.equals(countryOfIssue, other.countryOfIssue)
        && Objects.equals(dateOfIssue, other.dateOfIssue);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((person == null) ? 0 : person.hashCode())
        + ((type == null) ? 0 : type.hashCode())
        + ((countryOfIssue == null) ? 0 : countryOfIssue.hashCode())
        + ((dateOfIssue == null) ? 0 : dateOfIssue.hashCode());
  }
}

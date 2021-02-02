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
 * The <b>IdentityDocumentId</b> class implements the ID class for the <b>IdentityDocument</b>
 * class.
 *
 * @author Marcus Portmann
 */
public class IdentityDocumentId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 alpha-2 code for the country of issue for the identity document. */
  private String countryOfIssue;

  /** The date of issue for the identity document. */
  private LocalDate dateOfIssue;

  /**
   * The Universally Unique Identifier (UUID) for the party the identity document is associated
   * with.
   */
  private UUID party;

  /** The code for the identity document type. */
  private String type;

  /** Constructs a new <b>IdentityDocumentId</b>. */
  public IdentityDocumentId() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b> false</b>
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

    IdentityDocumentId other = (IdentityDocumentId) object;

    return Objects.equals(party, other.party)
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
    return ((party == null) ? 0 : party.hashCode())
        + ((type == null) ? 0 : type.hashCode())
        + ((countryOfIssue == null) ? 0 : countryOfIssue.hashCode())
        + ((dateOfIssue == null) ? 0 : dateOfIssue.hashCode());
  }
}

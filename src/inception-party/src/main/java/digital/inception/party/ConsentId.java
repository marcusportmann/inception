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
import java.util.UUID;

/**
 * The <b>ConsentId</b> class implements the ID class for the <b>Consent</b> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"UnusedDeclaration"})
public class ConsentId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ID for the person the consent is associated with. */
  private UUID person;

  /** The code for the consent type. */
  private String type;

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

    ConsentId other = (ConsentId) object;

    return Objects.equals(person, other.person) && Objects.equals(type, other.type);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((person == null) ? 0 : person.hashCode()) + ((type == null) ? 0 : type.hashCode());
  }
}

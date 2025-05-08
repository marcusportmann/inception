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
 * The {@code AssociationPropertyTypeId} class implements the ID class for the {@code
 * AssociationPropertyType} class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"UnusedDeclaration"})
public class AssociationPropertyTypeId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the association type the association property type is associated with. */
  private String associationType;

  /** The code for the association property type. */
  private String code;

  /** The Unicode locale identifier for the association property type. */
  private String localeId;

  /** Creates a new {@code AssociationPropertyTypeId} instance. */
  public AssociationPropertyTypeId() {}

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

    AssociationPropertyTypeId other = (AssociationPropertyTypeId) object;

    return Objects.equals(associationType, other.associationType)
        && Objects.equals(code, other.code)
        && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((associationType == null) ? 0 : associationType.hashCode())
        + ((code == null) ? 0 : code.hashCode())
        + ((localeId == null) ? 0 : localeId.hashCode());
  }
}

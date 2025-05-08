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
 * The {@code RoleTypeAttributeTypeConstraintId} class implements the ID class for the {@code
 * RoleTypeAttributeTypeConstraint} class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"UnusedDeclaration"})
public class RoleTypeAttributeTypeConstraintId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the attribute type. */
  private String attributeType;

  /** The qualifier for the attribute type. */
  private String attributeTypeQualifier;

  /** The code for the role type. */
  private String roleType;

  /** The constraint type. */
  private ConstraintType type;

  /** Creates a new {@code RoleTypeAttributeTypeConstraintId} instance. */
  public RoleTypeAttributeTypeConstraintId() {}

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

    RoleTypeAttributeTypeConstraintId other = (RoleTypeAttributeTypeConstraintId) object;

    return Objects.equals(roleType, other.roleType)
        && Objects.equals(attributeType, other.attributeType)
        && Objects.equals(attributeTypeQualifier, other.attributeTypeQualifier)
        && Objects.equals(type, other.type);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((roleType == null) ? 0 : roleType.hashCode())
        + ((attributeType == null) ? 0 : attributeType.hashCode())
        + ((attributeTypeQualifier == null) ? 0 : attributeTypeQualifier.hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }
}

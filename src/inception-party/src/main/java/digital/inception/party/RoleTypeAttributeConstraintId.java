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
 * The <b>RoleTypeAttributeConstraintId</b> class implements the ID class for the
 * <b>RoleTypeAttributeConstraint</b> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class RoleTypeAttributeConstraintId implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the standard or custom attribute type. */
  private String attributeType;

  /** The code for the role type. */
  private String roleType;

  /** The attribute constraint type. */
  private AttributeConstraintType type;

  /** Constructs a new <b>RoleTypeAttributeConstraintId</b>. */
  public RoleTypeAttributeConstraintId() {}

  /**
   * Constructs a new <b>RoleTypeAttributeConstraintId</b>.
   *
   * @param roleType the code for the role type
   * @param attributeType the code for the standard or custom attribute type
   * @param type the attribute constraint type
   */
  public RoleTypeAttributeConstraintId(
      String roleType, String attributeType, AttributeConstraintType type) {
    this.roleType = roleType;
    this.attributeType = attributeType;
    this.type = type;
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

    RoleTypeAttributeConstraintId other = (RoleTypeAttributeConstraintId) object;

    return Objects.equals(roleType, other.roleType)
        && Objects.equals(attributeType, other.attributeType)
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
        + ((type == null) ? 0 : type.hashCode());
  }
}

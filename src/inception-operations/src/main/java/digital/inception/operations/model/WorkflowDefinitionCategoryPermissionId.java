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

package digital.inception.operations.model;

import digital.inception.core.util.StringUtil;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code WorkflowDefinitionCategoryPermissionId} class implements the ID class for The {@code
 * WorkflowDefinitionCategoryPermission} class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"UnusedDeclaration"})
public class WorkflowDefinitionCategoryPermissionId implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * The ID for the workflow definition category the workflow definition category permission is
   * associated with.
   */
  private String categoryId;

  /** The code for the role the workflow definition category permission is assigned to. */
  private String roleCode;

  /** The workflow permission type. */
  private WorkflowPermissionType type;

  /** Constructs a new {@code WorkflowDefinitionCategoryPermissionId}. */
  public WorkflowDefinitionCategoryPermissionId() {}

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

    WorkflowDefinitionCategoryPermissionId other = (WorkflowDefinitionCategoryPermissionId) object;

    return StringUtil.equalsIgnoreCase(categoryId, other.categoryId)
        && StringUtil.equalsIgnoreCase(roleCode, other.roleCode)
        && type == other.type;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((categoryId == null) ? 0 : categoryId.hashCode())
        + ((roleCode == null) ? 0 : roleCode.hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }
}

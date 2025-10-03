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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code WorkflowDefinitionCategoryPermission} class holds the information for a workflow
 * definition category permission.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow definition category permission")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"roleCode"})
@XmlRootElement(
    name = "WorkflowDefinitionCategoryPermission",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDefinitionCategoryPermission",
    namespace = "https://inception.digital/operations",
    propOrder = {"roleCode"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definition_category_permissions")
@IdClass(WorkflowDefinitionCategoryPermissionId.class)
public class WorkflowDefinitionCategoryPermission implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * The ID for the workflow definition category the workflow definition category permission is
   * associated with.
   */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "category_id", nullable = false)
  private String categoryId;

  /** The code for the role the workflow definition category permission is assigned to. */
  @Schema(
      description =
          "The code for the role the workflow definition category permission is assigned to",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "RoleCode", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "role_code", length = 100, nullable = false)
  private String roleCode;

  /** Constructs a new {@code WorkflowDefinitionCategoryPermission}. */
  public WorkflowDefinitionCategoryPermission() {}

  /**
   * Constructs a new {@code WorkflowDefinitionCategoryPermission}.
   *
   * @param roleCode the code for the role the workflow definition category permission is assigned
   *     to
   */
  public WorkflowDefinitionCategoryPermission(String roleCode) {
    this.roleCode = roleCode;
  }

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

    WorkflowDefinitionCategoryPermission other = (WorkflowDefinitionCategoryPermission) object;

    return StringUtil.equalsIgnoreCase(categoryId, other.categoryId)
        && StringUtil.equalsIgnoreCase(roleCode, other.roleCode);
  }

  /**
   * Returns the code for the role the workflow definition category permission is assigned to.
   *
   * @return the code for the role the workflow definition category permission is assigned to
   */
  public String getRoleCode() {
    return roleCode;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((categoryId == null) ? 0 : categoryId.hashCode())
        + ((roleCode == null) ? 0 : roleCode.hashCode());
  }

  /**
   * Set the code for the role the workflow definition category permission is assigned to.
   *
   * @param roleCode the code for the role the workflow definition category permission is assigned
   *     to
   */
  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  /**
   * Set the workflow definition category the workflow definition category permission is associated
   * with.
   *
   * @param workflowDefinitionCategory the workflow definition category the workflow definition
   *     category permission is associated with
   */
  @JsonBackReference("workflowDefinitionCategoryPermissionReference")
  @Schema(hidden = true)
  public void setWorkflowDefinitionCategory(WorkflowDefinitionCategory workflowDefinitionCategory) {
    if (workflowDefinitionCategory != null) {
      this.categoryId = workflowDefinitionCategory.getId();
    } else {
      this.categoryId = null;
    }
  }

  /**
   * Called by the JAXB runtime when an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof WorkflowDefinitionCategory parent) {
      setWorkflowDefinitionCategory(parent);
    }
  }
}

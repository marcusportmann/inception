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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The {@code WorkflowDefinitionCategory} class holds the information for a workflow definition
 * category.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow definition category")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "tenantId", "name", "permissions"})
@XmlRootElement(
    name = "WorkflowDefinitionCategory",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDefinitionCategory",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "tenantId", "name", "permissions"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definition_categories")
public class WorkflowDefinitionCategory implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The permissions for the workflow definition category. */
  @Schema(description = "The permissions for the workflow definition category")
  @JsonProperty
  @JsonManagedReference("workflowDefinitionCategoryPermissionReference")
  @XmlElementWrapper(name = "Permissions")
  @XmlElement(name = "Permission")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("roleCode")
  @JoinColumns({
    @JoinColumn(
        name = "category_id",
        referencedColumnName = "id",
        insertable = false,
        updatable = false)
  })
  private final List<WorkflowDefinitionCategoryPermission> permissions = new ArrayList<>();

  /** The ID for the workflow definition category. */
  @Schema(
      description = "The ID for the workflow definition category",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "id", length = 50, nullable = false)
  private String id;

  /** The name of the workflow definition category. */
  @Schema(
      description = "The name of the workflow definition category",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The ID for the tenant the workflow definition category is specific to. */
  @Schema(description = "The ID for the tenant the workflow definition category is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new {@code WorkflowDefinitionCategory}. */
  public WorkflowDefinitionCategory() {}

  /**
   * Constructs a new {@code WorkflowDefinitionCategory}.
   *
   * @param id the ID for the workflow definition category
   * @param name the name of the workflow definition category
   */
  public WorkflowDefinitionCategory(String id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Constructs a new {@code WorkflowDefinitionCategory}.
   *
   * @param id the ID for the workflow definition category
   * @param tenantId the ID for the tenant the workflow definition category is specific to
   * @param name the name of the workflow definition category
   */
  public WorkflowDefinitionCategory(String id, UUID tenantId, String name) {
    this.id = id;
    this.tenantId = tenantId;
    this.name = name;
  }

  /**
   * Add the permission for the workflow definition category.
   *
   * @param permission the permission
   */
  public void addPermission(WorkflowDefinitionCategoryPermission permission) {
    permissions.removeIf(
        existingPermission ->
            (StringUtil.equalsIgnoreCase(
                existingPermission.getRoleCode(), permission.getRoleCode())));

    permission.setWorkflowDefinitionCategory(this);

    permissions.add(permission);
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

    WorkflowDefinitionCategory other = (WorkflowDefinitionCategory) object;

    return StringUtil.equalsIgnoreCase(id, other.id);
  }

  /**
   * Returns the ID for the workflow definition category.
   *
   * @return the ID for the workflow definition category
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the workflow definition category.
   *
   * @return the name of the workflow definition category
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the permissions for the workflow definition category.
   *
   * @return the permissions for the workflow definition category
   */
  public List<WorkflowDefinitionCategoryPermission> getPermissions() {
    return permissions;
  }

  /**
   * Returns the ID for the tenant the workflow definition category is specific to.
   *
   * @return the ID for the tenant the workflow definition category is specific to
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Remove the permission with the specified role code and workflow permission type for the
   * workflow definition category.
   *
   * @param roleCode the role code for the permission
   * @param type the workflow permission type
   */
  public void removePermission(String roleCode, WorkflowPermissionType type) {
    permissions.removeIf(
        existingPermission ->
            (StringUtil.equalsIgnoreCase(existingPermission.getRoleCode(), roleCode)));
  }

  /**
   * Set the ID for the workflow definition category.
   *
   * @param id the ID for the workflow definition category
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the workflow definition category.
   *
   * @param name the name of the workflow definition category
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the permissions for the workflow definition category.
   *
   * @param permissions the permissions for the workflow definition category
   */
  public void setPermissions(List<WorkflowDefinitionCategoryPermission> permissions) {
    permissions.forEach(permission -> permission.setWorkflowDefinitionCategory(this));
    this.permissions.clear();
    this.permissions.addAll(permissions);
  }

  /**
   * Set the ID for the tenant the workflow definition category is specific to.
   *
   * @param tenantId the ID for the tenant the workflow definition category is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}

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
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code InteractionSourcePermission} class holds the information for an interaction source
 * permission.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An interaction source permission")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"roleCode", "type"})
@XmlRootElement(
    name = "InteractionSourcePermission",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionSourcePermission",
    namespace = "https://inception.digital/operations",
    propOrder = {"roleCode", "type"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interaction_source_permissions")
@IdClass(InteractionSourcePermissionId.class)
public class InteractionSourcePermission implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the role the interaction source permission is assigned to. */
  @Schema(
      description = "The code for the role the interaction source permission is assigned to",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "RoleCode", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "role_code", length = 100, nullable = false)
  private String roleCode;

  /** The ID for the interaction source the interaction source permission is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "source_id", nullable = false)
  private UUID sourceId;

  /** The interaction permission type. */
  @Schema(
      description = "The interaction permission type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Id
  @Column(name = "type", length = 50, nullable = false)
  private InteractionPermissionType type;

  /** Constructs a new {@code InteractionSourcePermission}. */
  public InteractionSourcePermission() {}

  /**
   * Constructs a new {@code InteractionSourcePermission}.
   *
   * @param roleCode the code for the role the interaction source permission is assigned to
   * @param type the interaction permission type
   */
  public InteractionSourcePermission(String roleCode, InteractionPermissionType type) {
    this.roleCode = roleCode;
    this.type = type;
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

    InteractionSourcePermission other = (InteractionSourcePermission) object;

    return Objects.equals(sourceId, other.sourceId)
        && StringUtil.equalsIgnoreCase(roleCode, other.roleCode)
        && type == other.type;
  }

  /**
   * Returns the code for the role the interaction source permission is assigned to.
   *
   * @return the code for the role the interaction source permission is assigned to
   */
  public String getRoleCode() {
    return roleCode;
  }

  /**
   * Returns the interaction permission type.
   *
   * @return the interaction permission type
   */
  public InteractionPermissionType getType() {
    return type;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((sourceId == null) ? 0 : sourceId.hashCode())
        + ((roleCode == null) ? 0 : roleCode.hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the interaction source the interaction permission type is associated with.
   *
   * @param interactionSource the interaction source the interaction permission type is associated
   *     with
   */
  @JsonBackReference("interactionSourcePermissionReference")
  @Schema(hidden = true)
  public void setInteractionSource(InteractionSource interactionSource) {
    if (interactionSource != null) {
      this.sourceId = interactionSource.getId();
    } else {
      this.sourceId = null;
    }
  }

  /**
   * Set the code for the role the interaction source permission is assigned to.
   *
   * @param roleCode the code for the role the interaction source permission is assigned to
   */
  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  /**
   * Set the interaction permission type.
   *
   * @param type the interaction permission type
   */
  public void setType(InteractionPermissionType type) {
    this.type = type;
  }

  /**
   * Called by the JAXB runtime an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof InteractionSource parent) {
      setInteractionSource(parent);
    }
  }
}

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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code WorkflowAttributeDefinition} class holds the information for a workflow attribute
 * definition, which defines a valid attribute for a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow attribute definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "workflowDefinitionId", "tenantId", "required", "description"})
@XmlRootElement(
    name = "WorkflowAttributeDefinition",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowAttributeDefinition",
    namespace = "https://inception.digital/operations",
    propOrder = {"code", "workflowDefinitionId", "tenantId", "required", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_attribute_definitions")
@SuppressWarnings({"unused", "WeakerAccess"})
public class WorkflowAttributeDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the workflow attribute. */
  @Schema(
      description = "The code for the workflow attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the workflow attribute. */
  @Schema(
      description = "The description for the workflow attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** Is the workflow attribute required? */
  @Schema(
      description = "Is the workflow attribute required",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Required", required = true)
  @NotNull
  @Column(name = "required", nullable = false)
  private boolean required;

  /** The ID for the tenant the workflow attribute definition is specific to. */
  @Schema(description = "The ID for the tenant the workflow attribute definition is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** The ID for the workflow definition the workflow attribute definition is specific to. */
  @Schema(
      description =
          "The ID for the workflow definition the workflow attribute definition is specific to")
  @JsonProperty
  @XmlElement(name = "WorkflowDefinitionId")
  @Size(min = 1, max = 50)
  @Column(name = "workflow_definition_id", length = 50)
  private String workflowDefinitionId;

  /** Constructs a new {@code WorkflowAttributeDefinition}. */
  public WorkflowAttributeDefinition() {}

  /**
   * Constructs a new {@code WorkflowAttributeDefinition}.
   *
   * @param code the code for the workflow attribute
   * @param description the description for the workflow attribute
   * @param required is the workflow attribute required
   * @param workflowDefinitionId the ID for the workflow definition the workflow attribute
   *     definition is specific to
   * @param tenantId the ID for the tenant the workflow attribute definition is specific to
   */
  public WorkflowAttributeDefinition(
      String code,
      String description,
      boolean required,
      String workflowDefinitionId,
      UUID tenantId) {
    this.code = code;
    this.description = description;
    this.required = required;
    this.workflowDefinitionId = workflowDefinitionId;
    this.tenantId = tenantId;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument otherwise {@code false}
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

    WorkflowAttributeDefinition other = (WorkflowAttributeDefinition) object;

    return Objects.equals(code, other.code);
  }

  /**
   * Returns the code for the workflow attribute.
   *
   * @return the code for the workflow attribute
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the workflow attribute.
   *
   * @return the description for the workflow attribute
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the ID for the tenant the workflow attribute definition is specific to.
   *
   * @return the ID for the tenant the workflow attribute definition is specific to
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the ID for the workflow definition the workflow attribute definition is specific to.
   *
   * @return the ID for the workflow definition the workflow attribute definition is specific to
   */
  public String getWorkflowDefinitionId() {
    return workflowDefinitionId;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (code == null) ? 0 : code.hashCode();
  }

  /**
   * Returns whether the workflow attribute is required.
   *
   * @return {@code true} if the workflow attribute is required or {@code false} otherwise
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Set the code for the workflow attribute.
   *
   * @param code the code for the workflow attribute
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the workflow attribute.
   *
   * @param description the description for the workflow attribute
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set whether the workflow attribute is required.
   *
   * @param required {@code true} if the workflow attribute is required or {@code false} otherwise
   */
  public void setRequired(boolean required) {
    this.required = required;
  }

  /**
   * Set the ID for the tenant the workflow attribute definition is specific to.
   *
   * @param tenantId the ID for the tenant the workflow attribute definition is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the ID for the workflow definition the workflow attribute definition is specific to.
   *
   * @param workflowDefinitionId the ID for the workflow definition the workflow attribute
   *     definition is specific to
   */
  public void setWorkflowDefinitionId(String workflowDefinitionId) {
    this.workflowDefinitionId = workflowDefinitionId;
  }
}

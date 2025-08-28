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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code WorkflowSummary} class holds the information for a workflow summary.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "parentId",
  "definitionId",
  "definitionVersion",
  "status",
  "initiated",
  "initiatedBy",
  "updated",
  "updatedBy",
  "finalized",
  "finalizedBy"
})
@XmlRootElement(name = "WorkflowSummary", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "parentId",
      "definitionId",
      "definitionVersion",
      "status",
      "initiated",
      "initiatedBy",
      "updated",
      "updatedBy",
      "finalized",
      "finalizedBy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflows")
@SuppressWarnings({"unused", "WeakerAccess"})
public class WorkflowSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * The attributes for the workflow.
   *
   * <p>NOTE: This is only present so we can retrieve workflow summaries for workflows based on the
   * values of the workflow attributes associated with the workflows.
   */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @OneToMany
  @OrderBy("code")
  @JoinColumn(
      name = "workflow_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private final List<WorkflowAttribute> attributes = new ArrayList<>();

  /** The ID for the workflow definition the workflow is associated with. */
  @Schema(
      description = "The ID for the workflow definition the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "definition_id", length = 50, nullable = false)
  private String definitionId;

  /** The version of the workflow definition the workflow is associated with. */
  @Schema(
      description = "The version of the workflow definition the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionVersion", required = true)
  @NotNull
  @Column(name = "definition_version", nullable = false)
  private int definitionVersion;

  /** The date and time the workflow was finalized. */
  @Schema(description = "The date and time the workflow was finalized")
  @JsonProperty
  @XmlElement(name = "Finalized")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "finalized")
  private OffsetDateTime finalized;

  /** The person or system that finalized the workflow. */
  @Schema(description = "The person or system that finalized the workflow")
  @JsonProperty
  @XmlElement(name = "FinalizedBy")
  @Size(min = 1, max = 100)
  @Column(name = "finalized_by", length = 100)
  private String finalizedBy;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The date and time the workflow was initiated. */
  @Schema(
      description = "The date and time the workflow was initiated",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Initiated", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "initiated", nullable = false)
  private OffsetDateTime initiated;

  /** The person or system that initiated the workflow. */
  @Schema(
      description = "The person or system that initiated the workflow",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InitiatedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "initiated_by", length = 100, nullable = false)
  private String initiatedBy;

  /** The ID for the parent workflow. */
  @Schema(description = "The ID for the parent workflow")
  @JsonProperty
  @XmlElement(name = "ParentId")
  @Column(name = "parent_id")
  private UUID parentId;

  /** The status of the workflow. */
  @Schema(description = "The status of the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private WorkflowStatus status;

  /** The ID for the tenant the workflow is associated with. */
  @Schema(
      description = "The ID for the tenant the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The date and time the workflow was last updated. */
  @Schema(description = "The date and time the workflow was last updated")
  @JsonProperty
  @XmlElement(name = "Updated")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "updated")
  private OffsetDateTime updated;

  /** The person or system that last updated the workflow. */
  @Schema(description = "The person or system that last updated the workflow")
  @JsonProperty
  @XmlElement(name = "UpdatedBy")
  @Size(min = 1, max = 100)
  @Column(name = "updated_by", length = 100)
  private String updatedBy;

  /** Constructs a new {@code WorkflowSummary}. */
  public WorkflowSummary() {}

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

    WorkflowSummary other = (WorkflowSummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the workflow definition the workflow is associated with.
   *
   * @return the ID for the workflow definition the workflow is associated with
   */
  public String getDefinitionId() {
    return definitionId;
  }

  /**
   * Returns the version of the workflow definition the workflow is associated with.
   *
   * @return the version of the workflow definition the workflow is associated with
   */
  public int getDefinitionVersion() {
    return definitionVersion;
  }

  /**
   * Returns the date and time the workflow was finalized.
   *
   * @return the date and time the workflow was finalized
   */
  public OffsetDateTime getFinalized() {
    return finalized;
  }

  /**
   * Returns the person or system that finalized the workflow.
   *
   * @return the person or system that finalized the workflow
   */
  public String getFinalizedBy() {
    return finalizedBy;
  }

  /**
   * Returns the ID for the workflow.
   *
   * @return the ID for the workflow
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the date and time the workflow was initiated.
   *
   * @return the date and time the workflow was initiated
   */
  public OffsetDateTime getInitiated() {
    return initiated;
  }

  /**
   * Returns the person or system that initiated the workflow.
   *
   * @return the person or system that initiated the workflow
   */
  public String getInitiatedBy() {
    return initiatedBy;
  }

  /**
   * Returns the ID for the parent workflow.
   *
   * @return the ID for the parent workflow
   */
  public UUID getParentId() {
    return parentId;
  }

  /**
   * Returns the status of the workflow.
   *
   * @return the status of the workflow
   */
  public WorkflowStatus getStatus() {
    return status;
  }

  /**
   * Returns the ID for the tenant the workflow is associated with.
   *
   * @return the ID for the tenant the workflow is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the workflow was last updated.
   *
   * @return the date and time the workflow was last updated
   */
  public OffsetDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the person or system that last updated the workflow.
   *
   * @return the person or system that last updated the workflow
   */
  public String getUpdatedBy() {
    return updatedBy;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }
}

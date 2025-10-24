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
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
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
  "definitionName",
  "status",
  "initiated",
  "initiatedBy",
  "updated",
  "updatedBy",
  "finalized",
  "finalizedBy",
  "description"
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
      "definitionName",
      "status",
      "initiated",
      "initiatedBy",
      "updated",
      "updatedBy",
      "finalized",
      "finalizedBy",
      "description"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class WorkflowSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the workflow definition the workflow is associated with. */
  @Schema(
      description = "The ID for the workflow definition the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String definitionId;

  /** The name of the workflow definition the workflow is associated with. */
  @Schema(
      description = "The name of the workflow definition the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String definitionName;

  /** The version of the workflow definition the workflow is associated with. */
  @Schema(
      description = "The version of the workflow definition the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionVersion", required = true)
  @NotNull
  private int definitionVersion;

  /** The description for the workflow. */
  @Schema(description = "The description for the workflow")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 500)
  private String description;

  /** The date and time the workflow was finalized. */
  @Schema(description = "The date and time the workflow was finalized")
  @JsonProperty
  @XmlElement(name = "Finalized")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime finalized;

  /** The person or system that finalized the workflow. */
  @Schema(description = "The person or system that finalized the workflow")
  @JsonProperty
  @XmlElement(name = "FinalizedBy")
  @Size(min = 1, max = 100)
  private String finalizedBy;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
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
  private OffsetDateTime initiated;

  /** The person or system that initiated the workflow. */
  @Schema(
      description = "The person or system that initiated the workflow",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InitiatedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String initiatedBy;

  /** The ID for the parent workflow. */
  @Schema(description = "The ID for the parent workflow")
  @JsonProperty
  @XmlElement(name = "ParentId")
  private UUID parentId;

  /** The status of the workflow. */
  @Schema(description = "The status of the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  private WorkflowStatus status;

  /** The ID for the tenant the workflow is associated with. */
  @Schema(
      description = "The ID for the tenant the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  private UUID tenantId;

  /** The date and time the workflow was last updated. */
  @Schema(description = "The date and time the workflow was last updated")
  @JsonProperty
  @XmlElement(name = "Updated")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime updated;

  /** The person or system that last updated the workflow. */
  @Schema(description = "The person or system that last updated the workflow")
  @JsonProperty
  @XmlElement(name = "UpdatedBy")
  @Size(min = 1, max = 100)
  private String updatedBy;

  /** Constructs a new {@code WorkflowSummary}. */
  public WorkflowSummary() {}

  /**
   * Constructs a new {@code WorkflowSummary}.
   *
   * @param id the ID for the workflow
   * @param tenantId the ID for the tenant the workflow is associated with
   * @param parentId the ID for the parent workflow
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param definitionVersion the version of the workflow definition the workflow is associated
   * @param definitionName the name of the workflow definition the workflow is associated with
   * @param status the status of the workflow
   * @param initiated the date and time the workflow was initiated
   * @param initiatedBy the person or system that initiated the workflow
   * @param updated the date and time the workflow was last updated
   * @param updatedBy the person or system that last updated the workflow
   * @param finalized the date and time the workflow was finalized
   * @param finalizedBy the person or system that finalized the workflow
   * @param description the description for the workflow
   */
  public WorkflowSummary(
      UUID id,
      UUID tenantId,
      UUID parentId,
      String definitionId,
      int definitionVersion,
      String definitionName,
      WorkflowStatus status,
      OffsetDateTime initiated,
      String initiatedBy,
      OffsetDateTime updated,
      String updatedBy,
      OffsetDateTime finalized,
      String finalizedBy,
      String description) {
    this.id = id;
    this.tenantId = tenantId;
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.definitionVersion = definitionVersion;
    this.definitionName = definitionName;
    this.status = status;
    this.initiated = initiated;
    this.initiatedBy = initiatedBy;
    this.updated = updated;
    this.updatedBy = updatedBy;
    this.finalized = finalized;
    this.finalizedBy = finalizedBy;
    this.description = description;
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
   * Returns the name of the workflow definition the workflow is associated with.
   *
   * @return the name of the workflow definition the workflow is associated with
   */
  public String getDefinitionName() {
    return definitionName;
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
   * Returns the description for the workflow.
   *
   * @return the description for the workflow
   */
  public String getDescription() {
    return description;
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

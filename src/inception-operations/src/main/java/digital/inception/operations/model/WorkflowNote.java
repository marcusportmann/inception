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
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.OffsetDateTimeAdapter;
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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code WorkflowNote} class holds the information for a workflow note.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow note")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "workflowId",
  "content",
  "created",
  "createdBy",
  "updated",
  "updatedBy"
})
@XmlRootElement(name = "WorkflowNote", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowNote",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "workflowId",
      "content",
      "created",
      "createdBy",
      "updated",
      "updatedBy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_notes")
@SuppressWarnings({"unused", "WeakerAccess"})
public class WorkflowNote implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The content for the workflow note. */
  @Schema(
      description = "The content for the workflow note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Content", required = true)
  @NotNull
  @Size(min = 1, max = 2000)
  @Column(name = "content", length = 2000, nullable = false)
  private String content;

  /** The date and time the workflow note was created. */
  @Schema(
      description = "The date and time the workflow note was created",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Created", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private OffsetDateTime created;

  /** The person or system that created the workflow note. */
  @Schema(
      description = "The person or system that created the workflow note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CreatedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "created_by", length = 100, nullable = false)
  private String createdBy;

  /** The ID for the workflow note. */
  @Schema(description = "The ID for the workflow note", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The ID for the tenant the workflow note is associated with. */
  @Schema(
      description = "The ID for the tenant the workflow note is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The date and time the workflow note was last updated. */
  @Schema(description = "The date and time the workflow note was last updated")
  @JsonProperty
  @XmlElement(name = "Updated")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "updated")
  private OffsetDateTime updated;

  /** The person or system that last updated the workflow note. */
  @Schema(description = "The person or system that last updated the workflow note")
  @JsonProperty
  @XmlElement(name = "UpdatedBy")
  @Size(min = 1, max = 100)
  @Column(name = "updated_by", length = 100)
  private String updatedBy;

  /** The ID for the workflow the workflow note is associated with. */
  @Schema(
      description = "The ID for the workflow the workflow note is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  @Column(name = "workflow_id", length = 50, nullable = false)
  private UUID workflowId;

  /** Constructs a new {@code WorkflowNote}. */
  public WorkflowNote() {}

  /**
   * Constructs a new {@code WorkflowNote}.
   *
   * @param tenantId the ID for the tenant the workflow note is associated with
   * @param workflowId the ID for the workflow the workflow note is associated with
   * @param content the content for the workflow note
   * @param created the date and time the workflow note was created
   * @param createdBy the person or system that created the workflow note
   */
  public WorkflowNote(
      UUID tenantId, UUID workflowId, String content, OffsetDateTime created, String createdBy) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.tenantId = tenantId;
    this.workflowId = workflowId;
    this.content = content;
    this.created = created;
    this.createdBy = createdBy;
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

    WorkflowNote other = (WorkflowNote) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the content for the workflow note.
   *
   * @return the content for the workflow note
   */
  public String getContent() {
    return content;
  }

  /**
   * Returns the date and time the workflow note was created.
   *
   * @return the date and time the workflow note was created
   */
  public OffsetDateTime getCreated() {
    return created;
  }

  /**
   * Returns the person or system that created the workflow note.
   *
   * @return the person or system that created the workflow note
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Returns the ID for the workflow note.
   *
   * @return the ID for the workflow note
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the ID for the tenant the workflow note is associated with.
   *
   * @return the ID for the tenant the workflow note is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the workflow note was last updated.
   *
   * @return the date and time the workflow note was last updated
   */
  public OffsetDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the person or system that last updated the workflow note.
   *
   * @return the person or system that last updated the workflow note
   */
  public String getUpdatedBy() {
    return updatedBy;
  }

  /**
   * Returns the ID for the workflow the workflow note is associated with.
   *
   * @return the ID for the workflow the workflow note is associated with
   */
  public UUID getWorkflowId() {
    return workflowId;
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

  /**
   * Sets the content for the workflow note.
   *
   * @param content the content for the workflow note
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Sets the date and time the workflow note was created.
   *
   * @param created the date and time the workflow note was created
   */
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  /**
   * Sets the person or system that created the workflow note.
   *
   * @param createdBy the person or system that created the workflow note
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Sets the ID for the workflow note.
   *
   * @param id the ID for the workflow note
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Sets the ID for the tenant the workflow note is associated with.
   *
   * @param tenantId the ID for the tenant the workflow note is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Sets the date and time the workflow note was last updated.
   *
   * @param updated the date and time the workflow note was last updated
   */
  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  /**
   * Sets the person or system that last updated the workflow note.
   *
   * @param updatedBy the person or system that last updated the workflow note
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  /**
   * Sets the ID for the workflow the workflow note is associated with.
   *
   * @param workflowId the ID for the workflow the workflow note is associated with
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}

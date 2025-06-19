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
import digital.inception.operations.constraint.ValidWorkflow;
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
@JsonPropertyOrder({"id", "workflowId", "who", "content", "created"})
@XmlRootElement(name = "WorkflowNote", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowNote",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "workflowId", "who", "content", "created"})
@XmlAccessorType(XmlAccessType.FIELD)
@ValidWorkflow
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
  @XmlElement(name = "Created")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private OffsetDateTime created;

  /** The ID for the workflow note. */
  @Schema(description = "The ID for the workflow note", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The username for the user associated with the workflow note. */
  @Schema(
      description = "The username for the user associated with the workflow note",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Who", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "who", length = 100)
  private String who;

  /** The ID for the workflow the workflow note is associated with. */
  @Schema(
      description = "The ID for the workflow the workflow note is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "workflow_id", length = 50, nullable = false)
  private String workflowId;

  /** Constructs a new {@code WorkflowNote}. */
  public WorkflowNote() {}

  /**
   * Constructs a new {@code WorkflowNote}.
   *
   * @param workflowId the ID for the workflow the workflow note is associated with
   * @param who the username for the user associated with the workflow note
   * @param content the content for the workflow note
   * @param created the date and time the workflow note was created
   */
  public WorkflowNote(UUID workflowId, String who, String content, OffsetDateTime created) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.who = who;
    this.content = content;
    this.created = created;
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
   * Returns the ID for the workflow note.
   *
   * @return the ID for the workflow note
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the username for the user associated with the workflow note.
   *
   * @return the username for the user associated with the workflow note
   */
  public String getWho() {
    return who;
  }

  /**
   * Returns the ID for the workflow the workflow note is associated with.
   *
   * @return the ID for the workflow the workflow note is associated with
   */
  public String getWorkflowId() {
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
   * Set the content for the workflow note.
   *
   * @param content the content for the workflow note
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Set the date and time the workflow note was created.
   *
   * @param created the date and time the workflow note was created
   */
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  /**
   * Set the ID for the workflow note.
   *
   * @param id the ID for the workflow note
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the username for the user associated with the workflow note.
   *
   * @param who the username for the user associated with the workflow note
   */
  public void setWho(String who) {
    this.who = who;
  }

  /**
   * Set the ID for the workflow the workflow note is associated with.
   *
   * @param workflowId the ID for the workflow the workflow note is associated with
   */
  public void setWorkflowId(String workflowId) {
    this.workflowId = workflowId;
  }
}

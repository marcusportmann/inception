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
import digital.inception.core.xml.OffsetDateTimeAdapter;
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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code WorkflowInteractionLink} class holds the information for a workflow interaction link.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow interaction link")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"interactionId", "conversationId", "linked", "linkedBy"})
@XmlRootElement(
    name = "WorkflowInteractionLink",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowInteractionLink",
    namespace = "https://inception.digital/operations",
    propOrder = {"interactionId", "conversationId", "linked", "linkedBy"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_interaction_links")
@IdClass(WorkflowInteractionLinkId.class)
public class WorkflowInteractionLink implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the conversation the workflow interaction link is associated with. */
  @Schema(
      description = "The ID for the conversation the workflow interaction link is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ConversationId", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "conversation_id", length = 30, nullable = false)
  private String conversationId;

  /** The ID for the interaction the workflow interaction link is associated with. */
  @Schema(
      description = "The ID for the interaction the workflow interaction link is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionId", required = true)
  @NotNull
  @Id
  @Column(name = "interaction_id", length = 50, nullable = false)
  private UUID interactionId;

  /** The date and time the interaction was linked to the workflow. */
  @Schema(
      description = "The date and time the interaction was linked to the workflow",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Linked", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "linked", nullable = false)
  private OffsetDateTime linked;

  /** The person or system who linked the interaction to the workflow. */
  @Schema(
      description = "The person or system who linked the interaction to the workflow",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LinkedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "linked_by", length = 100, nullable = false)
  private String linkedBy;

  /** The ID for the workflow the workflow interaction link is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "workflow_id", nullable = false)
  private UUID workflowId;

  /** Constructs a new {@code WorkflowInteractionLink}. */
  public WorkflowInteractionLink() {}

  /**
   * Constructs a new {@code WorkflowInteractionLink}.
   *
   * @param workflowId the ID for the workflow the workflow interaction link is associated with
   * @param interactionId the ID for the interaction the workflow interaction link is associated
   *     with
   * @param conversationId the ID for the conversation the workflow interaction link is associated
   *     with
   * @param linked the date and time the interaction was linked to the workflow
   * @param linkedBy the person or system who linked the interaction to the workflow
   */
  public WorkflowInteractionLink(
      UUID workflowId,
      UUID interactionId,
      String conversationId,
      OffsetDateTime linked,
      String linkedBy) {
    this.workflowId = workflowId;
    this.interactionId = interactionId;
    this.conversationId = conversationId;
    this.linked = linked;
    this.linkedBy = linkedBy;
  }

  /**
   * Constructs a new {@code WorkflowInteractionLink}.
   *
   * @param interactionId the ID for the interaction the workflow interaction link is associated
   *     with
   * @param conversationId the ID for the conversation the workflow interaction link is associated
   *     with
   * @param linked the date and time the interaction was linked to the workflow
   * @param linkedBy the person or system who linked the interaction to the workflow
   */
  public WorkflowInteractionLink(
      UUID interactionId, String conversationId, OffsetDateTime linked, String linkedBy) {
    this.interactionId = interactionId;
    this.conversationId = conversationId;
    this.linked = linked;
    this.linkedBy = linkedBy;
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

    WorkflowInteractionLink other = (WorkflowInteractionLink) object;

    return Objects.equals(workflowId, other.workflowId)
        && Objects.equals(interactionId, other.interactionId);
  }

  /**
   * Returns the ID for the conversation the workflow interaction link is associated with.
   *
   * @return the ID for the conversation the workflow interaction link is associated with
   */
  public String getConversationId() {
    return conversationId;
  }

  /**
   * Returns the ID for the interaction the workflow interaction link is associated with.
   *
   * @return the ID for the interaction the workflow interaction link is associated with
   */
  public UUID getInteractionId() {
    return interactionId;
  }

  /**
   * Returns the date and time the interaction was linked to the workflow.
   *
   * @return the date and time the interaction was linked to the workflow
   */
  public OffsetDateTime getLinked() {
    return linked;
  }

  /**
   * Returns the person or system who linked the interaction to the workflow.
   *
   * @return the person or system who linked the interaction to the workflow
   */
  public String getLinkedBy() {
    return linkedBy;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((workflowId == null) ? 0 : workflowId.hashCode())
        + ((interactionId == null) ? 0 : interactionId.hashCode());
  }

  /**
   * Set the ID for the conversation the workflow interaction link is associated with.
   *
   * @param conversationId the ID for the conversation the workflow interaction link is associated
   *     with
   */
  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * Set the ID for the interaction the workflow interaction link is associated with.
   *
   * @param interactionId the ID for the interaction the workflow interaction link is associated
   *     with
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
  }

  /**
   * Set the date and time the interaction was linked to the workflow.
   *
   * @param linked the date and time the interaction was linked to the workflow
   */
  public void setLinked(OffsetDateTime linked) {
    this.linked = linked;
  }

  /**
   * Set the person or system who linked the interaction to the workflow.
   *
   * @param linkedBy the person or system who linked the interaction to the workflow
   */
  public void setLinkedBy(String linkedBy) {
    this.linkedBy = linkedBy;
  }

  /**
   * Set the workflow the workflow interaction link is associated with.
   *
   * @param workflow the workflow the workflow interaction link is associated with
   */
  @JsonBackReference("workflowInteractionLinkReference")
  @Schema(hidden = true)
  public void setWorkflow(Workflow workflow) {
    if (workflow != null) {
      this.workflowId = workflow.getId();
    } else {
      this.workflowId = null;
    }
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
    if (parentObject instanceof Workflow parent) {
      setWorkflow(parent);
    }
  }
}

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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
 * The {@code WorkflowInteractionLink} class holds the information for a workflow interaction link.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow interaction link")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"interactionId"})
@XmlRootElement(
    name = "WorkflowInteractionLink",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowInteractionLink",
    namespace = "https://inception.digital/operations",
    propOrder = {"interactionId"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_interaction_links")
@IdClass(WorkflowInteractionLinkId.class)
public class WorkflowInteractionLink implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

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
   * @param interactionId the ID for the interaction the workflow interaction link is associated
   *     with
   */
  public WorkflowInteractionLink(UUID interactionId) {
    this.interactionId = interactionId;
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
   * Returns the ID for the interaction the workflow interaction link is associated with.
   *
   * @return the ID for the interaction the workflow interaction link is associated with
   */
  public UUID getInteractionId() {
    return interactionId;
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
   * Set the ID for the interaction the workflow interaction link is associated with.
   *
   * @param interactionId the ID for the interaction the workflow interaction link is associated
   *     with
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
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

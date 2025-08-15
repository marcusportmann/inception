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
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.util.StringUtil;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import digital.inception.operations.constraint.ValidWorkflow;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code Workflow} class holds the information for a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "parentId",
  "definitionId",
  "definitionVersion",
  "status",
  "partyId",
  "externalReference",
  "attributes",
  "steps",
  "data",
  "initiated",
  "initiatedBy",
  "updated",
  "updatedBy",
  "finalized",
  "finalizedBy"
})
@XmlRootElement(name = "Workflow", namespace = "https://inception.digital/operations")
@XmlType(
    name = "Workflow",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "parentId",
      "definitionId",
      "definitionVersion",
      "status",
      "partyId",
      "externalReference",
      "attributes",
      "steps",
      "data",
      "initiated",
      "initiatedBy",
      "updated",
      "updatedBy",
      "finalized",
      "finalizedBy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidWorkflow
@Entity
@Table(name = "operations_workflows")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Workflow implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the workflow. */
  @Schema(description = "The attributes for the workflow")
  @JsonProperty
  @JsonManagedReference("workflowAttributeReference")
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("code")
  @JoinColumn(
      name = "workflow_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private final List<WorkflowAttribute> attributes = new ArrayList<>();

  /** The interaction links for the workflow. */
  @Schema(description = "The interaction links for the workflow")
  @JsonProperty
  @JsonManagedReference("workflowInteractionLinkReference")
  @XmlElementWrapper(name = "InteractionLinks")
  @XmlElement(name = "InteractionLink")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("linked")
  @JoinColumn(
      name = "workflow_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private final List<WorkflowInteractionLink> interactionLinks = new ArrayList<>();

  /** The workflow steps for the workflow. */
  @Schema(description = "The workflow steps for the workflow")
  @JsonProperty
  @JsonManagedReference("workflowStepReference")
  @XmlElementWrapper(name = "Steps")
  @XmlElement(name = "Step")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("initiated")
  @JoinColumn(name = "workflow_id", insertable = false, updatable = false)
  private final List<WorkflowStep> steps = new ArrayList<>();

  /** The data for the workflow. */
  @Schema(description = "The data for the workflow")
  @JsonProperty
  @XmlElement(name = "Data")
  @Size(max = 10485760)
  @Column(name = "data", length = 10485760)
  private String data;

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

  /** The external reference used to link this workflow to an external system. */
  @Schema(description = "The external reference used to link this workflow to an external system")
  @JsonProperty
  @XmlElement(name = "ExternalReference")
  @Size(max = 100)
  @Column(name = "external_reference", length = 100)
  private String externalReference;

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

  /** The ID for the party the workflow is associated with. */
  @Schema(description = "The ID for the party the workflow is associated with")
  @JsonProperty
  @XmlElement(name = "PartyId")
  @Column(name = "party_id")
  private UUID partyId;

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

  /** Constructs a new {@code Workflow}. */
  public Workflow() {}

  /**
   * Constructs a new {@code Workflow}.
   *
   * @param tenantId the ID for the tenant the workflow is associated with
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param definitionVersion the version of the workflow definition the workflow is associated with
   * @param status the status of the workflow
   * @param attributes the attributes for the workflow
   * @param data the data for the workflow
   * @param initiated the date and time the workflow was initiated
   * @param initiatedBy the person or system that initiated the workflow
   */
  public Workflow(
      UUID tenantId,
      String definitionId,
      int definitionVersion,
      WorkflowStatus status,
      List<WorkflowAttribute> attributes,
      String data,
      OffsetDateTime initiated,
      String initiatedBy) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.tenantId = tenantId;
    this.definitionId = definitionId;
    this.definitionVersion = definitionVersion;
    this.status = status;
    this.data = data;
    this.initiated = initiated;
    this.initiatedBy = initiatedBy;

    for (WorkflowAttribute attribute : attributes) {
      addAttribute(new WorkflowAttribute(attribute.getCode(), attribute.getValue()));
    }
  }

  /**
   * Constructs a new {@code Workflow}.
   *
   * @param tenantId the ID for the tenant the workflow is associated with
   * @param parentId the ID for the parent workflow
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param definitionVersion the version of the workflow definition the workflow is associated with
   * @param status the status of the workflow
   * @param attributes the attributes for the workflow
   * @param data the data for the workflow
   * @param initiated the date and time the workflow was initiated
   * @param initiatedBy the person or system that initiated the workflow
   */
  public Workflow(
      UUID tenantId,
      UUID parentId,
      String definitionId,
      int definitionVersion,
      WorkflowStatus status,
      List<WorkflowAttribute> attributes,
      String data,
      OffsetDateTime initiated,
      String initiatedBy) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.tenantId = tenantId;
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.definitionVersion = definitionVersion;
    this.status = status;
    this.data = data;
    this.initiated = initiated;
    this.initiatedBy = initiatedBy;

    for (WorkflowAttribute attribute : attributes) {
      addAttribute(new WorkflowAttribute(attribute.getCode(), attribute.getValue()));
    }
  }

  /**
   * Add the attribute for the workflow.
   *
   * @param attribute the attribute
   */
  public void addAttribute(WorkflowAttribute attribute) {
    attributes.removeIf(
        existingAttribute ->
            StringUtil.equalsIgnoreCase(existingAttribute.getCode(), attribute.getCode()));

    attribute.setWorkflow(this);

    attributes.add(attribute);
  }

  /**
   * Add the interaction link for the workflow.
   *
   * @param interactionLink the interaction link
   */
  public void addInteractionLink(WorkflowInteractionLink interactionLink) {
    interactionLinks.removeIf(
        existingInteractionLink ->
            Objects.equals(
                existingInteractionLink.getInteractionId(), interactionLink.getInteractionId()));

    interactionLink.setWorkflow(this);

    interactionLinks.add(interactionLink);
  }

  /**
   * Add the workflow step for the workflow.
   *
   * @param step the workflow step
   */
  public void addStep(WorkflowStep step) {
    steps.removeIf(
        existingStep -> StringUtil.equalsIgnoreCase(existingStep.getCode(), step.getCode()));

    step.setWorkflow(this);

    steps.add(step);
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

    Workflow other = (Workflow) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Retrieve the attribute with the specified code for the workflow.
   *
   * @param code the code for the attribute
   * @return an Optional containing the attribute with the specified code for the workflow or an
   *     empty Optional if the attribute could not be found
   */
  public Optional<WorkflowAttribute> getAttributeWithCode(String code) {
    return attributes.stream()
        .filter(attribute -> StringUtil.equalsIgnoreCase(attribute.getCode(), code))
        .findFirst();
  }

  /**
   * Returns the attributes for the workflow.
   *
   * @return the attributes for the workflow
   */
  public List<WorkflowAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Returns the data for the workflow.
   *
   * @return the data for the workflow
   */
  public String getData() {
    return data;
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
   * Returns the external reference used to link this workflow to an external system.
   *
   * @return the external reference used to link this workflow to an external system
   */
  public String getExternalReference() {
    return externalReference;
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
   * Returns the interaction links for the workflow.
   *
   * @return the interaction links for the workflow
   */
  public List<WorkflowInteractionLink> getInteractionLinks() {
    return interactionLinks;
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
   * Returns the ID for the party the workflow is associated with.
   *
   * @return the ID for the party the workflow is associated with
   */
  public UUID getPartyId() {
    return partyId;
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
   * Returns the workflow steps for the workflow.
   *
   * @return the workflow steps for the workflow
   */
  public List<WorkflowStep> getSteps() {
    return steps;
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

  /**
   * Remove the attribute with the specified code for the workflow.
   *
   * @param code the code for the attribute
   */
  public void removeAttribute(String code) {
    attributes.removeIf(
        existingAttribute -> StringUtil.equalsIgnoreCase(existingAttribute.getCode(), code));
  }

  /**
   * Remove the interaction link with the specified ID for the workflow.
   *
   * @param interactionId the ID for the interaction
   */
  public void removeInteractionLink(UUID interactionId) {
    interactionLinks.removeIf(
        existingInteractionLink ->
            Objects.equals(existingInteractionLink.getInteractionId(), interactionId));
  }

  /**
   * Set the attributes for the workflow.
   *
   * @param attributes the attributes for the workflow
   */
  public void setAttributes(List<WorkflowAttribute> attributes) {
    attributes.forEach(attribute -> attribute.setWorkflow(this));
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the data for the workflow.
   *
   * @param data the data for the workflow
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the workflow definition the workflow is associated with.
   *
   * @param definitionId the ID for the workflow definition the workflow is associated with
   */
  public void setDefinitionId(String definitionId) {
    this.definitionId = definitionId;
  }

  /**
   * Set the version of the workflow definition the workflow is associated with.
   *
   * @param definitionVersion the version of the workflow definition the workflow is associated with
   */
  public void setDefinitionVersion(int definitionVersion) {
    this.definitionVersion = definitionVersion;
  }

  /**
   * Set the external reference used to link this workflow to an external system.
   *
   * @param externalReference the external reference used to link this workflow to an external
   *     system
   */
  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  /**
   * Set the date and time the workflow was finalized.
   *
   * @param finalized the date and time the workflow was finalized
   */
  public void setFinalized(OffsetDateTime finalized) {
    this.finalized = finalized;
  }

  /**
   * Set the person or system that finalized the workflow.
   *
   * @param finalizedBy the person or system that finalized the workflow
   */
  public void setFinalizedBy(String finalizedBy) {
    this.finalizedBy = finalizedBy;
  }

  /**
   * Set the ID for the workflow.
   *
   * @param id the ID for the workflow
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the date and time the workflow was initiated.
   *
   * @param initiated the date and time the workflow was initiated
   */
  public void setInitiated(OffsetDateTime initiated) {
    this.initiated = initiated;
  }

  /**
   * Set the person or system that initiated the workflow.
   *
   * @param initiatedBy the person or system that initiated the workflow
   */
  public void setInitiatedBy(String initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  /**
   * Set the interaction links for the workflow.
   *
   * @param interactionLinks the interaction links for the workflow
   */
  public void setInteractionLinks(List<WorkflowInteractionLink> interactionLinks) {
    interactionLinks.forEach(interactionLink -> interactionLink.setWorkflow(this));
    this.interactionLinks.clear();
    this.interactionLinks.addAll(interactionLinks);
  }

  /**
   * Set the ID for the parent workflow.
   *
   * @param parentId the ID for the parent workflow
   */
  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  /**
   * Set the ID for the party the workflow is associated with.
   *
   * @param partyId the ID for the party the workflow is associated with
   */
  public void setPartyId(UUID partyId) {
    this.partyId = partyId;
  }

  /**
   * Set the status of the workflow.
   *
   * @param status the status of the workflow
   */
  public void setStatus(WorkflowStatus status) {
    this.status = status;
  }

  /**
   * Set the workflow steps for the workflow.
   *
   * @param steps the workflow steps for the workflow
   */
  public void setSteps(List<WorkflowStep> steps) {
    steps.forEach(step -> step.setWorkflow(this));
    this.steps.clear();
    this.steps.addAll(steps);
  }

  /**
   * Set the ID for the tenant the workflow is associated with.
   *
   * @param tenantId the ID for the tenant the workflow is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the date and time the workflow was last updated.
   *
   * @param updated the date and time the workflow was last updated
   */
  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  /**
   * Set the person or system that last updated the workflow.
   *
   * @param updatedBy the person or system that last updated the workflow
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}

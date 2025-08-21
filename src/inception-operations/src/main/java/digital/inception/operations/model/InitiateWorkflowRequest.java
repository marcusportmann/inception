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
 * The {@code InitiateWorkflowRequest} class represents a request to initiate a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to initiate a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "definitionId",
  "parentId",
  "partyId",
  "externalReference",
  "pendWorkflow",
  "attributes",
  "data"
})
@XmlRootElement(
    name = "InitiateWorkflowRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "InitiateWorkflowRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "definitionId",
      "parentId",
      "partyId",
      "externalReference",
      "pendWorkflow",
      "attributes",
      "data"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class InitiateWorkflowRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the workflow. */
  @Schema(description = "The attributes for the workflow")
  @JsonProperty
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  private List<WorkflowAttribute> attributes = new ArrayList<>();

  /** The XML or JSON data for the workflow. */
  @Schema(description = "The XML or JSON data for the workflow")
  @JsonProperty
  @XmlElement(name = "Data")
  @Size(min = 1, max = 10485760)
  private String data;

  /** The ID for the workflow definition the workflow is associated with. */
  @Schema(
      description = "The ID for the workflow definition the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String definitionId;

  /** The external reference used to link this workflow to an external system. */
  @Schema(description = "The external reference used to link this workflow to an external system")
  @JsonProperty
  @XmlElement(name = "ExternalReference")
  @Size(max = 100)
  private String externalReference;

  /** The interaction links for the workflow. */
  @Schema(description = "The interaction links for the workflow")
  @JsonProperty
  @XmlElementWrapper(name = "InteractionLinks")
  @XmlElement(name = "InteractionLink")
  @Valid
  private List<InitiateWorkflowInteractionLink> interactionLinks = new ArrayList<>();

  /** The ID for the parent workflow. */
  @Schema(description = "The ID for the parent workflow")
  @JsonProperty
  @XmlElement(name = "ParentId")
  private UUID parentId;

  /** The ID for the party the workflow is associated with. */
  @Schema(description = "The ID for the party the workflow is associated with")
  @JsonProperty
  @XmlElement(name = "PartyId")
  private UUID partyId;

  /**
   * Pend the workflow.
   *
   * <p>A pended workflow will be created with the PENDING state, but not started.
   */
  @Schema(description = "Pend the workflow")
  @JsonProperty
  @XmlElement(name = "PendWorkflow")
  private boolean pendWorkflow;

  /** Constructs a new {@code InitiateWorkflowRequest}. */
  public InitiateWorkflowRequest() {}

  /**
   * Constructs a new {@code InitiateWorkflowRequest}.
   *
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param attributes the attributes for the workflow
   * @param data the XML or JSON data for the workflow
   */
  public InitiateWorkflowRequest(
      String definitionId, List<WorkflowAttribute> attributes, String data) {
    this.definitionId = definitionId;
    this.attributes = attributes;
    this.data = data;
  }

  /**
   * Constructs a new {@code InitiateWorkflowRequest}.
   *
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param externalReference the external reference used to link this workflow to an external
   *     system
   * @param attributes the attributes for the workflow
   * @param data the XML or JSON data for the workflow
   */
  public InitiateWorkflowRequest(
      String definitionId,
      String externalReference,
      List<WorkflowAttribute> attributes,
      String data) {
    this.definitionId = definitionId;
    this.externalReference = externalReference;
    this.attributes = attributes;
    this.data = data;
  }

  /**
   * Constructs a new {@code InitiateWorkflowRequest}.
   *
   * @param parentId the ID for the parent workflow
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param attributes the attributes for the workflow
   * @param data the XML or JSON data for the workflow
   */
  public InitiateWorkflowRequest(
      UUID parentId, String definitionId, List<WorkflowAttribute> attributes, String data) {
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.attributes = attributes;
    this.data = data;
  }

  /**
   * Constructs a new {@code InitiateWorkflowRequest}.
   *
   * @param parentId the ID for the parent workflow
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param externalReference the external reference used to link this workflow to an external
   *     system
   * @param attributes the attributes for the workflow
   * @param data the XML or JSON data for the workflow
   */
  public InitiateWorkflowRequest(
      UUID parentId,
      String definitionId,
      String externalReference,
      List<WorkflowAttribute> attributes,
      String data) {
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.externalReference = externalReference;
    this.attributes = attributes;
    this.data = data;
  }

  /**
   * Constructs a new {@code InitiateWorkflowRequest}.
   *
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param attributes the attributes for the workflow
   * @param interactionLinks the interaction links for the workflow
   * @param data the XML or JSON data for the workflow
   */
  public InitiateWorkflowRequest(
      String definitionId,
      List<WorkflowAttribute> attributes,
      List<InitiateWorkflowInteractionLink> interactionLinks,
      String data) {
    this.definitionId = definitionId;
    this.attributes = attributes;
    this.interactionLinks = interactionLinks;
    this.data = data;
  }

  /**
   * Constructs a new {@code InitiateWorkflowRequest}.
   *
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param externalReference the external reference used to link this workflow to an external
   *     system
   * @param attributes the attributes for the workflow
   * @param interactionLinks the interaction links for the workflow
   * @param data the XML or JSON data for the workflow
   */
  public InitiateWorkflowRequest(
      String definitionId,
      String externalReference,
      List<WorkflowAttribute> attributes,
      List<InitiateWorkflowInteractionLink> interactionLinks,
      String data) {
    this.definitionId = definitionId;
    this.externalReference = externalReference;
    this.attributes = attributes;
    this.interactionLinks = interactionLinks;
    this.data = data;
  }

  /**
   * Constructs a new {@code InitiateWorkflowRequest}.
   *
   * @param parentId the ID for the parent workflow
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param attributes the attributes for the workflow
   * @param interactionLinks the interaction links for the workflow
   * @param data the XML or JSON data for the workflow
   */
  public InitiateWorkflowRequest(
      UUID parentId,
      String definitionId,
      List<WorkflowAttribute> attributes,
      List<InitiateWorkflowInteractionLink> interactionLinks,
      String data) {
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.attributes = attributes;
    this.interactionLinks = interactionLinks;
    this.data = data;
  }

  /**
   * Constructs a new {@code InitiateWorkflowRequest}.
   *
   * @param parentId the ID for the parent workflow
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param externalReference the external reference used to link this workflow to an external
   *     system
   * @param attributes the attributes for the workflow
   * @param interactionLinks the interaction links for the workflow
   * @param data the XML or JSON data for the workflow
   */
  public InitiateWorkflowRequest(
      UUID parentId,
      String definitionId,
      String externalReference,
      List<WorkflowAttribute> attributes,
      List<InitiateWorkflowInteractionLink> interactionLinks,
      String data) {
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.externalReference = externalReference;
    this.attributes = attributes;
    this.interactionLinks = interactionLinks;
    this.data = data;
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
   * Returns the XML or JSON data for the workflow.
   *
   * @return the XML or JSON data for the workflow
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
   * Returns the external reference used to link this workflow to an external system.
   *
   * @return the external reference used to link this workflow to an external system
   */
  public String getExternalReference() {
    return externalReference;
  }

  /**
   * Returns the interaction links for the workflow.
   *
   * @return the interaction links for the workflow
   */
  public List<InitiateWorkflowInteractionLink> getInteractionLinks() {
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
   * Returns whether the workflow should be created in a PENDING state.
   *
   * @return {@code true} if the workflow should be created in a PENDING state or {@code false}
   *     otherwise
   */
  public boolean getPendWorkflow() {
    return pendWorkflow;
  }

  /**
   * Set the attributes for the workflow.
   *
   * @param attributes the attributes for the workflow
   */
  public void setAttributes(List<WorkflowAttribute> attributes) {
    this.attributes = attributes;
  }

  /**
   * Set the XML or JSON data for the workflow.
   *
   * @param data the XML or JSON data for the workflow
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
   * Set the external reference used to link this workflow to an external system.
   *
   * @param externalReference the external reference used to link this workflow to an external
   *     system
   */
  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  /**
   * Set the interaction links for the workflow.
   *
   * @param interactionLinks the interaction links for the workflow
   */
  public void setInteractionLinks(List<InitiateWorkflowInteractionLink> interactionLinks) {
    this.interactionLinks = interactionLinks;
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
   * Set whether the workflow should be created in a PENDING state.
   *
   * @param pendWorkflow {@code true} if the workflow should be created in a PENDING state or {@code
   *     false} otherwise
   */
  public void setPendWorkflow(boolean pendWorkflow) {
    this.pendWorkflow = pendWorkflow;
  }
}

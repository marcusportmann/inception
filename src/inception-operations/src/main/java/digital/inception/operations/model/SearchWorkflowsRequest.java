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
import digital.inception.core.sorting.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * The {@code SearchWorkflowsRequest} class represents a request to search for workflows matching
 * specific criteria.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to search for workflows matching specific criteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "definitionId",
  "status",
  "interactionId",
  "attributes",
  "externalReferences",
  "variables",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "SearchWorkflowsRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "SearchWorkflowsRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "definitionId",
      "status",
      "interactionId",
      "attributes",
      "externalReferences",
      "variables",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class SearchWorkflowsRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attribute search criteria to apply when searching for workflows. */
  @Schema(description = "The attribute search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  private List<AttributeSearchCriteria> attributes;

  /**
   * The person or system who canceled the workflow search criteria to apply when searching for
   * workflows.
   */
  @Schema(
      description =
          "The person or system who canceled the workflow search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "CanceledBy")
  @Size(min = 1, max = 100)
  private String canceledBy;

  /** The workflow definition ID search criteria to apply to the workflows. */
  @Schema(description = "The workflow definition ID search criteria to apply to the workflows")
  @JsonProperty
  @XmlElement(name = "DefinitionId")
  private String definitionId;

  /** The external reference search criteria to apply when searching for workflows. */
  @Schema(
      description = "The external reference search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElementWrapper(name = "ExternalReferences")
  @XmlElement(name = "ExternalReference")
  @Valid
  private List<ExternalReferenceSearchCriteria> externalReferences;

  /**
   * The person or system that finalized the workflow search criteria to apply when searching for
   * workflows.
   */
  @Schema(
      description =
          "The person or system that finalized the workflow search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "FinalizedBy")
  @Size(min = 1, max = 100)
  private String finalizedBy;

  /** The ID search criteria to apply when searching for workflows. */
  @Schema(description = "The ID search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "Id")
  private UUID id;

  /**
   * The person or system that initiated the workflow search criteria to apply when searching for
   * workflows.
   */
  @Schema(
      description =
          "The person or system that initiated the workflow search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "InitiatedBy")
  @Size(min = 1, max = 100)
  private String initiatedBy;

  /** The interaction ID search criteria to apply when searching for workflows. */
  @Schema(description = "The interaction ID search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "InteractionId")
  private UUID interactionId;

  /** The page index. */
  @Schema(description = "The page index")
  @JsonProperty
  @XmlElement(name = "PageIndex")
  private Integer pageIndex;

  /** The page size. */
  @Schema(description = "The page size")
  @JsonProperty
  @XmlElement(name = "PageSize")
  private Integer pageSize;

  /** The parent ID search criteria to apply when searching for workflows. */
  @Schema(description = "The parent ID search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "ParentId")
  private UUID parentId;

  /** The party ID search criteria to apply when searching for workflows. */
  @Schema(description = "The party ID search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "PartyId")
  private UUID partyId;

  /** The method used to sort the workflows e.g. by definition ID. */
  @Schema(description = "The method used to sort the workflows e.g. by definition ID")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private WorkflowSortBy sortBy;

  /** The sort direction to apply to the workflows. */
  @Schema(description = "The sort direction to apply to the workflows")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The status search criteria to apply to the workflows. */
  @Schema(description = "The status search criteria to apply to the workflows")
  @JsonProperty
  @XmlElement(name = "Status")
  private WorkflowStatus status;

  /**
   * The person or system that suspended the workflow search criteria to apply when searching for
   * workflows.
   */
  @Schema(
      description =
          "The person or system that suspended the workflow search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "SuspendedBy")
  @Size(min = 1, max = 100)
  private String suspendedBy;

  /**
   * The person or system that last updated the workflow search criteria to apply when searching for
   * workflows.
   */
  @Schema(
      description =
          "The person or system that last updated the workflow search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElement(name = "UpdatedBy")
  @Size(min = 1, max = 100)
  private String updatedBy;

  /** The variable search criteria to apply when searching for workflows. */
  @Schema(description = "The variable search criteria to apply when searching for workflows")
  @JsonProperty
  @XmlElementWrapper(name = "Variables")
  @XmlElement(name = "Variable")
  @Valid
  private List<VariableSearchCriteria> variables;

  /** Constructs a new {@code SearchWorkflowsRequest}. */
  public SearchWorkflowsRequest() {}

  /**
   * Constructs a new {@code SearchWorkflowsRequest}.
   *
   * @param id the ID search criteria to apply to the workflows
   * @param definitionId the workflow definition ID search criteria to apply to the workflows
   * @param status the status search criteria to apply to the workflows
   * @param interactionId the interaction ID search criteria to apply when searching for workflows
   * @param parentId the parent ID search criteria to apply when searching for workflows
   * @param partyId the party ID search criteria to apply when searching for workflows
   * @param initiatedBy the person or system that initiated the workflow search criteria to apply
   *     when searching for workflows
   * @param updatedBy the person or system that last updated the workflow search criteria to apply
   *     when searching for workflows
   * @param finalizedBy the person or system that finalized the workflow search criteria to apply
   *     when searching for workflows
   * @param suspendedBy the person or system that suspended the workflow search criteria to apply
   *     when searching for workflows
   * @param canceledBy the person or system who canceled the workflow search criteria to apply when
   *     searching for workflows
   * @param attributes the attribute search criteria to apply when searching for workflows
   * @param externalReferences the external reference search criteria to apply when searching for
   *     workflows
   * @param variables the variable search criteria to apply when searching for workflows
   * @param sortBy the method used to sort the workflows e.g. by definition ID
   * @param sortDirection the sort direction to apply to the workflows
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public SearchWorkflowsRequest(
      UUID id,
      String definitionId,
      WorkflowStatus status,
      UUID interactionId,
      UUID parentId,
      UUID partyId,
      String initiatedBy,
      String updatedBy,
      String finalizedBy,
      String suspendedBy,
      String canceledBy,
      List<AttributeSearchCriteria> attributes,
      List<ExternalReferenceSearchCriteria> externalReferences,
      List<VariableSearchCriteria> variables,
      WorkflowSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.id = id;
    this.definitionId = definitionId;
    this.status = status;
    this.interactionId = interactionId;
    this.parentId = parentId;
    this.partyId = partyId;
    this.initiatedBy = initiatedBy;
    this.updatedBy = updatedBy;
    this.finalizedBy = finalizedBy;
    this.suspendedBy = suspendedBy;
    this.canceledBy = canceledBy;
    this.attributes = attributes;
    this.externalReferences = externalReferences;
    this.variables = variables;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the attribute search criteria to apply when searching for workflows.
   *
   * @return the attribute search criteria to apply when searching for workflows
   */
  public List<AttributeSearchCriteria> getAttributes() {
    return attributes;
  }

  /**
   * Returns the person or system who canceled the workflow search criteria to apply when searching
   * for workflows.
   *
   * @return the person or system who canceled the workflow search criteria to apply when searching
   *     for workflows
   */
  public String getCanceledBy() {
    return canceledBy;
  }

  /**
   * Returns the workflow definition ID search criteria to apply to the workflows.
   *
   * @return the workflow definition ID search criteria to apply to the workflows
   */
  public String getDefinitionId() {
    return definitionId;
  }

  /**
   * Returns the external reference search criteria to apply when searching for workflows.
   *
   * @return the external reference search criteria to apply when searching for workflows
   */
  public List<ExternalReferenceSearchCriteria> getExternalReferences() {
    return externalReferences;
  }

  /**
   * Returns the person or system that finalized the workflow search criteria to apply when
   * searching for workflows.
   *
   * @return the person or system that finalized the workflow search criteria to apply when
   *     searching for workflows
   */
  public String getFinalizedBy() {
    return finalizedBy;
  }

  /**
   * Returns the ID search criteria to apply when searching for workflows.
   *
   * @return the ID search criteria to apply when searching for workflows
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the person or system that initiated the workflow search criteria to apply when
   * searching for workflows.
   *
   * @return the person or system that initiated the workflow search criteria to apply when
   *     searching for workflows
   */
  public String getInitiatedBy() {
    return initiatedBy;
  }

  /**
   * Returns the interaction ID search criteria to apply when searching for workflows.
   *
   * @return the interaction ID search criteria to apply when searching for workflows
   */
  public UUID getInteractionId() {
    return interactionId;
  }

  /**
   * Returns the page index.
   *
   * @return the page index
   */
  public Integer getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the page size.
   *
   * @return the page size
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * Returns the parent ID search criteria to apply when searching for workflows.
   *
   * @return the parent ID search criteria to apply when searching for workflows
   */
  public UUID getParentId() {
    return parentId;
  }

  /**
   * Returns the party ID search criteria to apply when searching for workflows.
   *
   * @return the party ID search criteria to apply when searching for workflows
   */
  public UUID getPartyId() {
    return partyId;
  }

  /**
   * Returns the method used to sort the workflows e.g. by definition ID.
   *
   * @return the method used to sort the workflows e.g. by definition ID
   */
  public WorkflowSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction to apply to the workflows.
   *
   * @return the sort direction to apply to the workflows
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the status search criteria to apply to the workflows.
   *
   * @return the status search criteria to apply to the workflows
   */
  public WorkflowStatus getStatus() {
    return status;
  }

  /**
   * Returns the person or system that suspended the workflow search criteria to apply when
   * searching for workflows.
   *
   * @return the person or system that suspended the workflow search criteria to apply when
   *     searching for workflows
   */
  public String getSuspendedBy() {
    return suspendedBy;
  }

  /**
   * Returns the person or system that last updated the workflow search criteria to apply when
   * searching for workflows.
   *
   * @return the person or system that last updated the workflow search criteria to apply when
   *     searching for workflows
   */
  public String getUpdatedBy() {
    return updatedBy;
  }

  /**
   * Returns the variable search criteria to apply when searching for workflows.
   *
   * @return the variable search criteria to apply when searching for workflows
   */
  public List<VariableSearchCriteria> getVariables() {
    return variables;
  }

  /**
   * Sets the attribute search criteria to apply when searching for workflows.
   *
   * @param attributes the attribute search criteria to apply when searching for workflows
   */
  public void setAttributes(List<AttributeSearchCriteria> attributes) {
    this.attributes = attributes;
  }

  /**
   * Sets the person or system who canceled the workflow search criteria to apply when searching for
   * workflows.
   *
   * @param canceledBy the person or system who canceled the workflow search criteria to apply when
   *     searching for workflows
   */
  public void setCanceledBy(String canceledBy) {
    this.canceledBy = canceledBy;
  }

  /**
   * Sets the workflow definition ID search criteria to apply to the workflows.
   *
   * @param definitionId the workflow definition ID search criteria to apply to the workflows
   */
  public void setDefinitionId(String definitionId) {
    this.definitionId = definitionId;
  }

  /**
   * Sets the external reference search criteria to apply when searching for workflows.
   *
   * @param externalReferences the external reference search criteria to apply when searching for
   *     workflows
   */
  public void setExternalReferences(List<ExternalReferenceSearchCriteria> externalReferences) {
    this.externalReferences = externalReferences;
  }

  /**
   * Sets the person or system that finalized the workflow search criteria to apply when searching
   * for workflows.
   *
   * @param finalizedBy the person or system that finalized the workflow search criteria to apply
   *     when searching for workflows
   */
  public void setFinalizedBy(String finalizedBy) {
    this.finalizedBy = finalizedBy;
  }

  /**
   * Sets the ID search criteria to apply when searching for workflows.
   *
   * @param id the ID search criteria to apply when searching for workflows
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Sets the person or system that initiated the workflow search criteria to apply when searching
   * for workflows.
   *
   * @param initiatedBy the person or system that initiated the workflow search criteria to apply
   *     when searching for workflows
   */
  public void setInitiatedBy(String initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  /**
   * Sets the interaction ID search criteria to apply when searching for workflows.
   *
   * @param interactionId the interaction ID search criteria to apply when searching for workflows
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
  }

  /**
   * Sets the page index.
   *
   * @param pageIndex the page index
   */
  public void setPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
  }

  /**
   * Sets the page size.
   *
   * @param pageSize the page size
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Sets the parent ID search criteria to apply when searching for workflows.
   *
   * @param parentId the parent ID search criteria to apply when searching for workflows
   */
  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  /**
   * Sets the party ID search criteria to apply when searching for workflows.
   *
   * @param partyId the party ID search criteria to apply when searching for workflows
   */
  public void setPartyId(UUID partyId) {
    this.partyId = partyId;
  }

  /**
   * Sets the method used to sort the workflows e.g. by definition ID.
   *
   * @param sortBy the method used to sort the workflows e.g. by definition ID
   */
  public void setSortBy(WorkflowSortBy sortBy) {
    this.sortBy = sortBy;
  }

  /**
   * Sets the sort direction to apply to the workflows.
   *
   * @param sortDirection the sort direction to apply to the workflows
   */
  public void setSortDirection(SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  /**
   * Sets the status search criteria to apply to the workflows.
   *
   * @param status the status search criteria to apply to the workflows
   */
  public void setStatus(WorkflowStatus status) {
    this.status = status;
  }

  /**
   * Sets the person or system that suspended the workflow search criteria to apply when searching
   * for workflows.
   *
   * @param suspendedBy the person or system that suspended the workflow search criteria to apply
   *     when searching for workflows
   */
  public void setSuspendedBy(String suspendedBy) {
    this.suspendedBy = suspendedBy;
  }

  /**
   * Sets the person or system that last updated the workflow search criteria to apply when
   * searching for workflows.
   *
   * @param updatedBy the person or system that last updated the workflow search criteria to apply
   *     when searching for workflows
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  /**
   * Sets the variable search criteria to apply when searching for workflows.
   *
   * @param variables the variable search criteria to apply when searching for workflows
   */
  public void setVariables(List<VariableSearchCriteria> variables) {
    this.variables = variables;
  }
}

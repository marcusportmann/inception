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
 * The {@code WorkflowNotes} class holds the results of a request to retrieve a list of workflow
 * notes for a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(
    description = "The results of a request to retrieve a list of workflow notes for a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "workflowId",
  "workflowNotes",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter"
})
@XmlRootElement(name = "WorkflowNotes", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowNotes",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "tenantId",
      "workflowId",
      "workflowNotes",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class WorkflowNotes implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The filter that was applied to the workflow notes. */
  @Schema(description = "The filter that was applied to the workflow notes")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The page index. */
  @Schema(description = "The page index", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PageIndex", required = true)
  private int pageIndex;

  /** The page size. */
  @Schema(description = "The page size", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PageSize", required = true)
  private int pageSize;

  /** The method used to sort the workflow notes e.g. by created. */
  @Schema(description = "The method used to sort the workflow notes e.g. by created")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private WorkflowNoteSortBy sortBy;

  /** The sort direction that was applied to the workflow notes. */
  @Schema(description = "The sort direction that was applied to the workflow notes")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the workflow notes are associated with. */
  @Schema(
      description = "The ID for the tenant the workflow notes are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of workflow notes. */
  @Schema(
      description = "The total number of workflow notes",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** The ID for the workflow the workflow notes are associated with. */
  @Schema(
      description = "The ID for the workflow the workflow notes are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  private UUID workflowId;

  /** The workflow notes. */
  @Schema(description = "The workflow notes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "WorkflowNotes", required = true)
  @XmlElement(name = "WorkflowNote", required = true)
  private List<WorkflowNote> workflowNotes;

  /** Constructs a new {@code WorkflowNotes}. */
  public WorkflowNotes() {}

  /**
   * Constructs a new {@code WorkflowNotes}.
   *
   * @param tenantId the ID for the tenant the workflow notes are associated with
   * @param workflowId the ID for the workflow the workflow notes are associated with
   * @param workflowNotes the workflow notes
   * @param total the total number of workflow notes
   * @param filter the filter that was applied to the workflow notes
   * @param sortBy the method used to sort the workflow notes e.g. by created
   * @param sortDirection the sort direction that was applied to the workflow notes
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public WorkflowNotes(
      UUID tenantId,
      UUID workflowId,
      List<WorkflowNote> workflowNotes,
      long total,
      String filter,
      WorkflowNoteSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.tenantId = tenantId;
    this.workflowId = workflowId;
    this.workflowNotes = workflowNotes;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter that was applied to the workflow notes.
   *
   * @return the filter that was applied to the workflow notes
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Returns the page index.
   *
   * @return the page index
   */
  public int getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the page size.
   *
   * @return the page size
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns the method used to sort the workflow notes e.g. by created.
   *
   * @return the method used to sort the workflow notes
   */
  public WorkflowNoteSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the workflow notes.
   *
   * @return the sort direction that was applied to the workflow notes
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the workflow notes are associated with.
   *
   * @return the ID for the tenant the workflow notes are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of workflow notes.
   *
   * @return the total number of workflow notes
   */
  public long getTotal() {
    return total;
  }

  /**
   * Returns the ID for the workflow the workflow notes are associated with.
   *
   * @return the ID for the workflow the workflow notes are associated with
   */
  public UUID getWorkflowId() {
    return workflowId;
  }

  /**
   * Returns the workflow notes.
   *
   * @return the workflow notes
   */
  public List<WorkflowNote> getWorkflowNotes() {
    return workflowNotes;
  }

  /**
   * Set the ID for the workflow the workflow notes are associated with.
   *
   * @param workflowId the ID for the workflow the workflow notes are associated with
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}

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
 * The {@code WorkflowSummaries} class holds the results of a request to retrieve a list of workflow
 * summaries.
 */
@Schema(description = "The results of a request to retrieve a list of workflow summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "workflowSummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "status",
  "filter"
})
@XmlRootElement(name = "WorkflowSummaries", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowSummaries",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "tenantId",
      "workflowSummaries",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "status",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class WorkflowSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The filter that was applied to the workflow summaries. */
  @Schema(description = "The filter that was applied to the workflow summaries")
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

  /** The method used to sort the workflow summaries e.g. by definition ID. */
  @Schema(description = "The method used to sort the workflow summaries e.g. by definition ID")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private WorkflowSortBy sortBy;

  /** The sort direction that was applied to the workflow summaries. */
  @Schema(description = "The sort direction that was applied to the workflow summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The status filter that was applied to the workflow summaries. */
  @Schema(description = "The status filter that was applied to the workflow summaries")
  @JsonProperty
  @XmlElement(name = "Status")
  private WorkflowStatus status;

  /** The ID for the tenant the workflow summaries are associated with. */
  @Schema(
      description = "The ID for the tenant the workflow summaries are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of workflow summaries. */
  @Schema(
      description = "The total number of workflow summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** The workflow summaries. */
  @Schema(description = "The workflow summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "WorkflowSummaries", required = true)
  @XmlElement(name = "WorkflowSummary", required = true)
  private List<WorkflowSummary> workflowSummaries;

  /** Creates a new {@code WorkflowSummaries} instance. */
  public WorkflowSummaries() {}

  /**
   * Creates a new {@code WorkflowSummaries} instance.
   *
   * @param tenantId the ID for the tenant the workflow summaries are associated with
   * @param workflowSummaries the workflow summaries
   * @param total the total number of workflow summaries
   * @param status the status filter that was applied to the workflow summaries
   * @param filter the filter that was applied to the workflow summaries
   * @param sortBy the method used to sort the workflow summaries e.g. by definition ID
   * @param sortDirection the sort direction that was applied to the workflow summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public WorkflowSummaries(
      UUID tenantId,
      List<WorkflowSummary> workflowSummaries,
      long total,
      WorkflowStatus status,
      String filter,
      WorkflowSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.tenantId = tenantId;
    this.workflowSummaries = workflowSummaries;
    this.total = total;
    this.status = status;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter that was applied to the workflow summaries.
   *
   * @return the filter that was applied to the workflow summaries
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
   * Returns the method used to sort the workflow summaries e.g. by definition ID.
   *
   * @return the method used to sort the workflow summaries
   */
  public WorkflowSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the workflow summaries.
   *
   * @return the sort direction that was applied to the workflow summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the status filter that was applied to the workflow summaries.
   *
   * @return the status filter that was applied to the workflow summaries
   */
  public WorkflowStatus getStatus() {
    return status;
  }

  /**
   * Returns the ID for the tenant the workflow summaries are associated with.
   *
   * @return the ID for the tenant the workflow summaries are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of workflow summaries.
   *
   * @return the total number of workflow summaries
   */
  public long getTotal() {
    return total;
  }

  /**
   * Returns the workflow summaries.
   *
   * @return the workflow summaries
   */
  public List<WorkflowSummary> getWorkflowSummaries() {
    return workflowSummaries;
  }
}

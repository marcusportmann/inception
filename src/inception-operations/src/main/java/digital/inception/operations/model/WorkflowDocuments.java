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
 * The {@code WorkflowDocuments} class holds the results of a request to retrieve a list of workflow
 * documents for a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "The results of a request to retrieve a list of workflow documents for a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "workflowId",
  "workflowDocuments",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter"
})
@XmlRootElement(name = "WorkflowDocuments", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDocuments",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "tenantId",
      "workflowId",
      "workflowDocuments",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class WorkflowDocuments implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The filter that was applied to the workflow documents. */
  @Schema(description = "The filter that was applied to the workflow documents")
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

  /** The method used to sort the workflow documents e.g. by created. */
  @Schema(description = "The method used to sort the workflow documents e.g. by created")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private WorkflowDocumentSortBy sortBy;

  /** The sort direction that was applied to the workflow documents. */
  @Schema(description = "The sort direction that was applied to the workflow documents")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the workflow documents are associated with. */
  @Schema(
      description = "The ID for the tenant the workflow documents are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of workflow documents. */
  @Schema(
      description = "The total number of workflow documents",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** The workflow documents. */
  @Schema(description = "The workflow documents", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "WorkflowDocuments", required = true)
  @XmlElement(name = "WorkflowDocument", required = true)
  private List<WorkflowDocument> workflowDocuments;

  /** The ID for the workflow the workflow documents are associated with. */
  @Schema(
      description = "The ID for the workflow the workflow documents are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  private UUID workflowId;

  /** Constructs a new {@code WorkflowDocuments}. */
  public WorkflowDocuments() {}

  /**
   * Constructs a new {@code WorkflowDocuments}.
   *
   * @param tenantId the ID for the tenant the workflow documents are associated with
   * @param workflowId the ID for the workflow the workflow documents are associated with
   * @param workflowDocuments the workflow documents
   * @param total the total number of workflow documents
   * @param filter the filter that was applied to the workflow documents
   * @param sortBy the method used to sort the workflow documents e.g. by created
   * @param sortDirection the sort direction that was applied to the workflow documents
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public WorkflowDocuments(
      UUID tenantId,
      UUID workflowId,
      List<WorkflowDocument> workflowDocuments,
      long total,
      String filter,
      WorkflowDocumentSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.tenantId = tenantId;
    this.workflowId = workflowId;
    this.workflowDocuments = workflowDocuments;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter that was applied to the workflow documents.
   *
   * @return the filter that was applied to the workflow documents
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
   * Returns the method used to sort the workflow documents e.g. by created.
   *
   * @return the method used to sort the workflow documents
   */
  public WorkflowDocumentSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the workflow documents.
   *
   * @return the sort direction that was applied to the workflow documents
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the workflow documents are associated with.
   *
   * @return the ID for the tenant the workflow documents are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of workflow documents.
   *
   * @return the total number of workflow documents
   */
  public long getTotal() {
    return total;
  }

  /**
   * Returns the workflow documents.
   *
   * @return the workflow documents
   */
  public List<WorkflowDocument> getWorkflowDocuments() {
    return workflowDocuments;
  }

  /**
   * Returns the ID for the workflow the workflow documents are associated with.
   *
   * @return the ID for the workflow the workflow documents are associated with
   */
  public UUID getWorkflowId() {
    return workflowId;
  }

  /**
   * Set the ID for the workflow the workflow documents are associated with.
   *
   * @param workflowId the ID for the workflow the workflow documents are associated with
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}

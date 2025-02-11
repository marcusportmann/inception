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

/**
 * The <b>ProcessSummaries</b> class holds the results of a request to retrieve a list of process
 * summaries.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of process summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "processSummaries",
    "total",
    "sortBy",
    "sortDirection",
    "pageIndex",
    "pageSize",
    "status",
    "filter"
})
@XmlRootElement(name = "ProcessSummaries", namespace = "https://inception.digital/operations")
@XmlType(
    name = "ProcessSummaries",
    namespace = "https://inception.digital/operations",
    propOrder = {
        "processSummaries",
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
public class ProcessSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The process summaries. */
  @Schema(description = "The process summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "ProcessSummaries", required = true)
  @XmlElement(name = "ProcessSummary", required = true)
  private List<ProcessSummary> processSummaries;

  /** The filter that was applied to the process summaries. */
  @Schema(description = "The filter that was applied to the process summaries")
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

  /** The method used to sort the process summaries e.g. by definition ID. */
  @Schema(description = "The method used to sort the process summaries e.g. by definition ID")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private ProcessSortBy sortBy;

  /** The sort direction that was applied to the process summaries. */
  @Schema(description = "The sort direction that was applied to the process summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The status filter that was applied to the process summaries. */
  @Schema(description = "The status filter that was applied to the process summaries")
  @JsonProperty
  @XmlElement(name = "Status")
  private ProcessStatus status;

  /** The total number of process summaries. */
  @Schema(
      description = "The total number of process summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>ProcessSummaries</b>. */
  public ProcessSummaries() {}

  /**
   * Constructs a new <b>ProcessSummaries</b>.
   *
   * @param processSummaries the process summaries
   * @param total the total number of process summaries
   * @param status the status filter that was applied to the process summaries
   * @param filter the filter that was applied to the process summaries
   * @param sortBy the method used to sort the process summaries e.g. by definition ID
   * @param sortDirection the sort direction that was applied to the process summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public ProcessSummaries(
      List<ProcessSummary> processSummaries,
      long total,
      ProcessStatus status,
      String filter,
      ProcessSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.processSummaries = processSummaries;
    this.total = total;
    this.status = status;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the process summaries.
   *
   * @return the process summaries
   */
  public List<ProcessSummary> getProcessSummaries() {
    return processSummaries;
  }

  /**
   * Returns the filter that was applied to the process summaries.
   *
   * @return the filter that was applied to the process summaries
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
   * Returns the method used to sort the process summaries e.g. by definition ID.
   *
   * @return the method used to sort the process summaries
   */
  public ProcessSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the process summaries.
   *
   * @return the sort direction that was applied to the process summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the status filter that was applied to the process summaries.
   *
   * @return the status filter that was applied to the process summaries
   */
  public ProcessStatus getStatus() {
    return status;
  }

  /**
   * Returns the total number of process summaries.
   *
   * @return the total number of process summaries
   */
  public long getTotal() {
    return total;
  }
}

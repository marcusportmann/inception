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

package digital.inception.executor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.sorting.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
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

/**
 * The <b>TaskSummaries</b> class holds the results of a request to retrieve a list of task
 * summaries.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of task summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "taskSummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter",
  "type",
  "status"
})
@XmlRootElement(name = "TaskSummaries", namespace = "https://inception.digital/executor")
@XmlType(
    name = "TaskSummaries",
    namespace = "https://inception.digital/executor",
    propOrder = {
      "taskSummaries",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter",
      "type",
      "status"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class TaskSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the task summaries. */
  @Schema(description = "The optional filter that was applied to the task summaries")
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

  /** The optional method used to sort the task summaries e.g. by name. */
  @Schema(description = "The optional method used to sort the task summaries e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private TaskSortBy sortBy;

  /** The optional sort direction that was applied to the task summaries. */
  @Schema(description = "The optional sort direction that was applied to the task summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The optional status filter to apply to the task summaries. */
  @Schema(description = "The optional status filter to apply to the task summaries")
  @JsonProperty
  @XmlElement(name = "Status")
  private TaskStatus status;

  /** The task summaries. */
  @Schema(description = "The task summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "TaskSummaries", required = true)
  @XmlElement(name = "TaskSummary", required = true)
  private List<TaskSummary> taskSummaries;

  /** The total number of task summaries. */
  @Schema(
      description = "The total number of task summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** The optional task type code filter to apply to the task summaries. */
  @Schema(description = "The optional task type code filter to apply to the task summaries")
  @JsonProperty
  @XmlElement(name = "Type")
  @Size(min = 1, max = 50)
  private String type;

  /** Constructs a new <b>TaskSummaries</b>. */
  public TaskSummaries() {}

  /**
   * Constructs a new <b>TaskSummaries</b>.
   *
   * @param taskSummaries the task summaries
   * @param total the total number of task summaries
   * @param type the optional task type code filter to apply to the task summaries
   * @param status the optional status filter to apply to the task summaries
   * @param filter the optional filter that was applied to the task summaries
   * @param sortBy the method used to sort the task summaries e.g. by name
   * @param sortDirection the sort direction that was applied to the task summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public TaskSummaries(
      List<TaskSummary> taskSummaries,
      long total,
      String type,
      TaskStatus status,
      String filter,
      TaskSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.taskSummaries = taskSummaries;
    this.total = total;
    this.status = status;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the task summaries.
   *
   * @return the optional filter that was applied to the task summaries
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
   * Returns the optional method used to sort the task summaries e.g. by name.
   *
   * @return the optional method used to sort the task summaries
   */
  public TaskSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the task summaries.
   *
   * @return the sort direction that was applied to the task summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the optional status filter to apply to the task summaries.
   *
   * @return the optional status filter to apply to the task summaries
   */
  public TaskStatus getStatus() {
    return status;
  }

  /**
   * Returns the task summaries.
   *
   * @return the task summaries
   */
  public List<TaskSummary> getTaskSummaries() {
    return taskSummaries;
  }

  /**
   * Returns the total number of task summaries.
   *
   * @return the total number of task summaries
   */
  public long getTotal() {
    return total;
  }

  /**
   * Returns the optional task type code filter to apply to the task summaries.
   *
   * @return the optional task type code filter to apply to the task summaries
   */
  public String getType() {
    return type;
  }
}

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

package digital.inception.error.model;

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
 * The {@code ErrorReportSummaries} class holds the results of a request to retrieve the summaries
 * for a list of error reports.
 *
 * @author Marcus Portmann
 */
@Schema(
    description = "The results of a request to retrieve the summaries for a list of error reports")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "errorReportSummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "ErrorReportSummaries", namespace = "https://inception.digital/error")
@XmlType(
    name = "ErrorReportSummaries",
    namespace = "https://inception.digital/error",
    propOrder = {
      "errorReportSummaries",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class ErrorReportSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The error report summaries. */
  @Schema(description = "The error report summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "ErrorReportSummaries", required = true)
  @XmlElement(name = "ErrorReportSummary", required = true)
  private List<ErrorReportSummary> errorReportSummaries;

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

  /** The method used to sort the error report summaries e.g. by who submitted them. */
  @Schema(
      description = "The method used to sort the error report summaries e.g. by who submitted them",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortBy", required = true)
  private ErrorReportSortBy sortBy;

  /** The sort direction that was applied to the error report summaries. */
  @Schema(
      description = "The sort direction that was applied to the error report summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortDirection", required = true)
  private SortDirection sortDirection;

  /** The total number of error report summaries. */
  @Schema(
      description = "The total number of error report summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code ErrorReportSummaries}. */
  public ErrorReportSummaries() {}

  /**
   * Constructs a new {@code ErrorReportSummaries}.
   *
   * @param errorReportSummaries the error report summaries
   * @param total the total number of error report summaries
   * @param sortBy the method used to sort the error report summaries e.g. by who submitted them
   * @param sortDirection the sort direction that was applied to the error report summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public ErrorReportSummaries(
      List<ErrorReportSummary> errorReportSummaries,
      long total,
      ErrorReportSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.errorReportSummaries = errorReportSummaries;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the error report summaries.
   *
   * @return the error report summaries
   */
  public List<ErrorReportSummary> getErrorReportSummaries() {
    return errorReportSummaries;
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
   * Returns the method used to sort the error report summaries e.g. by who submitted them.
   *
   * @return the method used to sort the error report summaries
   */
  public ErrorReportSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the error report summaries.
   *
   * @return the sort direction that was applied to the error report summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of error report summaries.
   *
   * @return the total number of error report summaries
   */
  public long getTotal() {
    return total;
  }
}

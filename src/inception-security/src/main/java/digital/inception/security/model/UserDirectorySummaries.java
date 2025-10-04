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

package digital.inception.security.model;

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
 * The {@code UserDirectorySummaries} class holds the results of a request to retrieve a list of
 * user directory summaries.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of user directory summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"userDirectorySummaries", "total", "sortDirection", "pageIndex", "pageSize"})
@XmlRootElement(name = "UserDirectorySummaries", namespace = "https://inception.digital/security")
@XmlType(
    name = "UserDirectorySummaries",
    namespace = "https://inception.digital/security",
    propOrder = {"userDirectorySummaries", "total", "sortDirection", "pageIndex", "pageSize"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class UserDirectorySummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

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

  /** The sort direction that was applied to the user directory summaries. */
  @Schema(
      description = "The sort direction that was applied to the user directory summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortDirection", required = true)
  private SortDirection sortDirection;

  /** The total number of user directory summaries. */
  @Schema(
      description = "The total number of user directory summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** The user directory summaries. */
  @Schema(description = "The user directory summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "UserDirectorySummaries", required = true)
  @XmlElement(name = "UserDirectorySummary", required = true)
  private List<UserDirectorySummary> userDirectorySummaries;

  /** Constructs a new {@code UserDirectorySummaries}. */
  public UserDirectorySummaries() {}

  /**
   * Constructs a new {@code UserDirectorySummaries}.
   *
   * @param userDirectorySummaries the user directory summaries
   * @param total the total number of user directory summaries
   * @param sortDirection the sort direction that was applied to the user directory summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public UserDirectorySummaries(
      List<UserDirectorySummary> userDirectorySummaries,
      long total,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.userDirectorySummaries = userDirectorySummaries;
    this.total = total;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
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
   * Returns the sort direction that was applied to the user directory summaries.
   *
   * @return the sort direction that was applied to the user directory summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of user directory summaries.
   *
   * @return the total number of user directory summaries
   */
  public long getTotal() {
    return total;
  }

  /**
   * Returns the user directory summaries.
   *
   * @return the user directory summaries
   */
  public List<UserDirectorySummary> getUserDirectorySummaries() {
    return userDirectorySummaries;
  }
}

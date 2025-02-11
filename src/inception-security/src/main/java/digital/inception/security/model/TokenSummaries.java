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
 * The <b>TokenSummaries</b> class holds the results of a request to retrieve a list of token
 * summaries.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of token summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tokenSummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "status",
  "filter"
})
@XmlRootElement(name = "TokenSummaries", namespace = "https://inception.digital/security")
@XmlType(
    name = "TokenSummaries",
    namespace = "https://inception.digital/security",
    propOrder = {
      "tokenSummaries",
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
public class TokenSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The filter that was applied to the token summaries. */
  @Schema(description = "The filter that was applied to the token summaries")
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

  /** The method used to sort the token summaries e.g. by name. */
  @Schema(description = "The method used to sort the token summaries e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private TokenSortBy sortBy;

  /** The sort direction that was applied to the token summaries. */
  @Schema(description = "The sort direction that was applied to the token summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The status filter that was applied to the token summaries. */
  @Schema(description = "The status filter that was applied to the token summaries")
  @JsonProperty
  @XmlElement(name = "Status")
  private TokenStatus status;

  /** The token summaries. */
  @Schema(description = "The token summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "TokenSummaries", required = true)
  @XmlElement(name = "TokenSummary", required = true)
  private List<TokenSummary> tokenSummaries;

  /** The total number of token summaries. */
  @Schema(
      description = "The total number of token summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>TokenSummaries</b>. */
  public TokenSummaries() {}

  /**
   * Constructs a new <b>TokenSummaries</b>.
   *
   * @param tokenSummaries the token summaries
   * @param total the total number of token summaries
   * @param status the status filter that was applied to the token summaries
   * @param filter the filter that was applied to the token summaries
   * @param sortBy the method used to sort the token summaries e.g. by name
   * @param sortDirection the sort direction that was applied to the token summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public TokenSummaries(
      List<TokenSummary> tokenSummaries,
      long total,
      TokenStatus status,
      String filter,
      TokenSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.tokenSummaries = tokenSummaries;
    this.total = total;
    this.status = status;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter that was applied to the token summaries.
   *
   * @return the filter that was applied to the token summaries
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
   * Returns the method used to sort the token summaries e.g. by name.
   *
   * @return the method used to sort the token summaries
   */
  public TokenSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the token summaries.
   *
   * @return the sort direction that was applied to the token summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the status filter that was applied to the token summaries.
   *
   * @return the status filter that was applied to the token summaries
   */
  public TokenStatus getStatus() {
    return status;
  }

  /**
   * Returns the token summaries.
   *
   * @return the token summaries
   */
  public List<TokenSummary> getTokenSummaries() {
    return tokenSummaries;
  }

  /**
   * Returns the total number of token summaries.
   *
   * @return the total number of token summaries
   */
  public long getTotal() {
    return total;
  }
}

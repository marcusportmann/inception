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
 * The <b>PolicySummaries</b> class holds the results of a request to retrieve a list of policy
 * summaries.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of policy summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "policySummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter"
})
@XmlRootElement(name = "PolicySummaries", namespace = "http://inception.digital/security")
@XmlType(
    name = "PolicySummaries",
    namespace = "http://inception.digital/security",
    propOrder = {
      "policySummaries",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class PolicySummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the policy summaries. */
  @Schema(description = "The optional filter that was applied to the policy summaries")
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

  /** The policy summaries. */
  @Schema(description = "The policy summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "PolicySummaries", required = true)
  @XmlElement(name = "PolicySummary", required = true)
  private List<PolicySummary> policySummaries;

  /** The optional method used to sort the policy summaries e.g. by name. */
  @Schema(description = "The optional method used to sort the policy summaries e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private PolicySortBy sortBy;

  /** The optional sort direction that was applied to the policy summaries. */
  @Schema(description = "The optional sort direction that was applied to the policy summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of policy summaries. */
  @Schema(
      description = "The total number of policy summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>PolicySummaries</b>. */
  public PolicySummaries() {}

  /**
   * Constructs a new <b>PolicySummaries</b>.
   *
   * @param policySummaries the policy summaries
   * @param total the total number of policy summaries
   * @param filter the optional filter that was applied to the policy summaries
   * @param sortBy the method used to sort the policy summaries e.g. by name
   * @param sortDirection the sort direction that was applied to the policy summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public PolicySummaries(
      List<PolicySummary> policySummaries,
      long total,
      String filter,
      PolicySortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.policySummaries = policySummaries;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the policy summaries.
   *
   * @return the optional filter that was applied to the policy summaries
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
   * Returns the policy summaries.
   *
   * @return the policy summaries
   */
  public List<PolicySummary> getPolicySummaries() {
    return policySummaries;
  }

  /**
   * Returns the optional method used to sort the policy summaries e.g. by name.
   *
   * @return the optional method used to sort the policy summaries
   */
  public PolicySortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the policy summaries.
   *
   * @return the sort direction that was applied to the policy summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of policy summaries.
   *
   * @return the total number of policy summaries
   */
  public long getTotal() {
    return total;
  }
}

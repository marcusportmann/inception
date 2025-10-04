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
 * The {@code InteractionSummaries} class represents the results of a request to retrieve a list of
 * interaction summaries.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of interaction summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "interactionSummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "InteractionSummaries", namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionSummaries",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "tenantId",
      "interactionSummaries",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class InteractionSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The interaction summaries. */
  @Schema(description = "The interaction summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "InteractionSummaries", required = true)
  @XmlElement(name = "InteractionSummary", required = true)
  private List<InteractionSummary> interactionSummaries;

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

  /** The method used to sort the interaction summaries e.g. by timestamp. */
  @Schema(description = "The method used to sort the interaction summaries e.g. by timestamp")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private InteractionSortBy sortBy;

  /** The sort direction that was applied to the interaction summaries. */
  @Schema(description = "The sort direction that was applied to the interaction summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the interaction summaries are associated with. */
  @Schema(
      description = "The ID for the tenant the interaction summaries are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of interaction summaries. */
  @Schema(
      description = "The total number of interaction summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code InteractionSummaries}. */
  public InteractionSummaries() {}

  /**
   * Constructs a new {@code InteractionSummaries}.
   *
   * @param tenantId the ID for the tenant the interaction summaries are associated with
   * @param interactionSummaries the interaction summaries
   * @param total the total number of interaction summaries
   * @param sortBy the method used to sort the interaction summaries e.g. by timestamp
   * @param sortDirection the sort direction that was applied to the interaction summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public InteractionSummaries(
      UUID tenantId,
      List<InteractionSummary> interactionSummaries,
      long total,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.tenantId = tenantId;
    this.interactionSummaries = interactionSummaries;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the interaction summaries.
   *
   * @return the interaction summaries
   */
  public List<InteractionSummary> getInteractionSummaries() {
    return interactionSummaries;
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
   * Returns the method used to sort the interaction summaries e.g. by timestamp.
   *
   * @return the method used to sort the interaction summaries
   */
  public InteractionSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the interaction summaries.
   *
   * @return the sort direction that was applied to the interaction summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the interaction summaries are associated with.
   *
   * @return the ID for the tenant the interaction summaries are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of interaction summaries.
   *
   * @return the total number of interaction summaries
   */
  public long getTotal() {
    return total;
  }
}

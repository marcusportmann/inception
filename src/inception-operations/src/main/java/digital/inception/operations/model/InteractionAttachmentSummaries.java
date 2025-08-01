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
 * The {@code InteractionAttachmentSummaries} class represents the results of a request to retrieve
 * a list of interaction attachment summaries.
 *
 * @author Marcus Portmann
 */
@Schema(
    description = "The results of a request to retrieve a list of interaction attachment summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "interactionAttachmentSummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "sourceId",
  "filter"
})
@XmlRootElement(
    name = "InteractionAttachmentSummaries",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionAttachmentSummaries",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "tenantId",
      "interactionAttachmentSummaries",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "sourceId",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class InteractionAttachmentSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The filter that was applied to the interaction attachment summaries. */
  @Schema(description = "The filter that was applied to the interaction attachment summaries")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The interaction attachment summaries. */
  @Schema(
      description = "The interaction attachment summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "InteractionAttachmentSummaries", required = true)
  @XmlElement(name = "InteractionAttachmentSummary", required = true)
  private List<InteractionAttachmentSummary> interactionAttachmentSummaries;

  /** The interaction ID filter that was applied to the interaction attachment summaries. */
  @Schema(
      description =
          "The interaction ID filter that was applied to the interaction attachment summaries")
  @JsonProperty
  @XmlElement(name = "InteractionId")
  private UUID interactionId;

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

  /** The method used to sort the interaction attachment summaries e.g. by name. */
  @Schema(description = "The method used to sort the interaction summaries e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private InteractionAttachmentSortBy sortBy;

  /** The sort direction that was applied to the interaction attachment summaries. */
  @Schema(
      description = "The sort direction that was applied to the interaction attachment summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the interaction attachment summaries are associated with. */
  @Schema(
      description =
          "The ID for the tenant the interaction attachment summaries are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of interaction attachment summaries. */
  @Schema(
      description = "The total number of interaction attachment summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code InteractionAttachmentSummaries}. */
  public InteractionAttachmentSummaries() {}

  /**
   * Constructs a new {@code InteractionAttachmentSummaries}.
   *
   * @param tenantId the ID for the tenant the interaction attachment summaries are associated with
   * @param interactionId the interaction ID filter that was applied to the interaction attachment
   *     summaries
   * @param interactionAttachmentSummaries the interaction attachment summaries
   * @param total the total number of interaction attachment summaries
   * @param filter the filter that was applied to the interaction attachment summaries
   * @param sortBy the method used to sort the interaction attachment summaries e.g. by name
   * @param sortDirection the sort direction that was applied to the interaction attachment
   *     summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public InteractionAttachmentSummaries(
      UUID tenantId,
      UUID interactionId,
      List<InteractionAttachmentSummary> interactionAttachmentSummaries,
      long total,
      String filter,
      InteractionAttachmentSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.tenantId = tenantId;
    this.interactionId = interactionId;
    this.interactionAttachmentSummaries = interactionAttachmentSummaries;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter that was applied to the interaction attachment summaries.
   *
   * @return the filter that was applied to the interaction attachment summaries
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Returns the interaction attachment summaries.
   *
   * @return the interaction attachment summaries
   */
  public List<InteractionAttachmentSummary> getInteractionAttachmentSummaries() {
    return interactionAttachmentSummaries;
  }

  /**
   * Returns the interaction ID filter that was applied to the interaction attachment summaries.
   *
   * @return the interaction ID filter that was applied to the interaction attachment summaries
   */
  public UUID getInteractionId() {
    return interactionId;
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
   * Returns the method used to sort the interaction attachment summaries e.g. by name.
   *
   * @return the method used to sort the interaction attachment summaries
   */
  public InteractionAttachmentSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the interaction attachment summaries.
   *
   * @return the sort direction that was applied to the interaction attachment summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the interaction attachment summaries are associated with.
   *
   * @return the ID for the tenant the interaction attachment summaries are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of interaction attachment summaries.
   *
   * @return the total number of interaction attachment summaries
   */
  public long getTotal() {
    return total;
  }
}

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
 * The {@code InteractionNotes} class holds the results of a request to retrieve a list of
 * interaction notes for an interaction.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "The results of a request to retrieve a list of interaction notes for an interaction")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "interactionId",
  "interactionNotes",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter"
})
@XmlRootElement(name = "InteractionNotes", namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionNotes",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "tenantId",
      "interactionId",
      "interactionNotes",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class InteractionNotes implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The filter that was applied to the interaction notes. */
  @Schema(description = "The filter that was applied to the interaction notes")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The ID for the interaction the interaction notes are associated with. */
  @Schema(
      description = "The ID for the interaction the interaction notes are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InteractionId", required = true)
  private UUID interactionId;

  /** The interaction notes. */
  @Schema(description = "The interaction notes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "InteractionNotes", required = true)
  @XmlElement(name = "InteractionNote", required = true)
  private List<InteractionNote> interactionNotes;

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

  /** The method used to sort the interaction notes e.g. by created. */
  @Schema(description = "The method used to sort the interaction notes e.g. by created")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private InteractionNoteSortBy sortBy;

  /** The sort direction that was applied to the interaction notes. */
  @Schema(description = "The sort direction that was applied to the interaction notes")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the interaction notes are associated with. */
  @Schema(
      description = "The ID for the tenant the interaction notes are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of interaction notes. */
  @Schema(
      description = "The total number of interaction notes",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code InteractionNotes}. */
  public InteractionNotes() {}

  /**
   * Constructs a new {@code InteractionNotes}.
   *
   * @param tenantId the ID for the tenant the interaction notes are associated with
   * @param interactionId the ID for the interaction the interaction notes are associated with
   * @param interactionNotes the interaction notes
   * @param total the total number of interaction notes
   * @param filter the filter that was applied to the interaction notes
   * @param sortBy the method used to sort the interaction notes e.g. by created
   * @param sortDirection the sort direction that was applied to the interaction notes
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public InteractionNotes(
      UUID tenantId,
      UUID interactionId,
      List<InteractionNote> interactionNotes,
      long total,
      String filter,
      InteractionNoteSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.tenantId = tenantId;
    this.interactionId = interactionId;
    this.interactionNotes = interactionNotes;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter that was applied to the interaction notes.
   *
   * @return the filter that was applied to the interaction notes
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Returns the ID for the interaction the interaction notes are associated with.
   *
   * @return the ID for the interaction the interaction notes are associated with
   */
  public UUID getInteractionId() {
    return interactionId;
  }

  /**
   * Returns the interaction notes.
   *
   * @return the interaction notes
   */
  public List<InteractionNote> getInteractionNotes() {
    return interactionNotes;
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
   * Returns the method used to sort the interaction notes e.g. by created.
   *
   * @return the method used to sort the interaction notes
   */
  public InteractionNoteSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the interaction notes.
   *
   * @return the sort direction that was applied to the interaction notes
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the interaction notes are associated with.
   *
   * @return the ID for the tenant the interaction notes are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of interaction notes.
   *
   * @return the total number of interaction notes
   */
  public long getTotal() {
    return total;
  }

  /**
   * Set the ID for the interaction the interaction notes are associated with.
   *
   * @param interactionId the ID for the interaction the interaction notes are associated with
   */
  public void setInteractionId(UUID interactionId) {
    this.interactionId = interactionId;
  }
}

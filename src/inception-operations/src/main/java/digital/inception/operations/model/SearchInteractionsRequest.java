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
 * The {@code SearchInteractionsRequest} class represents a request to search for interactions
 * matching specific criteria.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to search for interactions matching specific criteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "sourceId",
  "interactionIds",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(
    name = "SearchInteractionsRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "SearchInteractionsRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"sourceId", "interactionIds", "sortBy", "sortDirection", "pageIndex", "pageSize"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class SearchInteractionsRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The interaction ID search criteria to apply to the interactions. */
  @Schema(description = "The interaction ID search criteria to apply to the interactions")
  @JsonProperty
  @XmlElementWrapper(name = "InteractionIds")
  @XmlElement(name = "InteractionId")
  private List<UUID> interactionIds;

  /** The page index. */
  @Schema(description = "The page index")
  @JsonProperty
  @XmlElement(name = "PageIndex")
  private Integer pageIndex;

  /** The page size. */
  @Schema(description = "The page size")
  @JsonProperty
  @XmlElement(name = "PageSize")
  private Integer pageSize;

  /** The method used to sort the interactions e.g. by occurred. */
  @Schema(description = "The method used to sort the interactions e.g. by occured")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private InteractionSortBy sortBy;

  /** The sort direction to apply to the interactions. */
  @Schema(description = "The sort direction to apply to the interactions")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The interaction source ID search criteria to apply to the interactions. */
  @Schema(description = "The interaction source ID search criteria to apply to the interactions")
  @JsonProperty
  @XmlElement(name = "SourceId")
  private UUID sourceId;

  /** Constructs a new {@code SearchInteractionsRequest}. */
  public SearchInteractionsRequest() {}

  /**
   * Constructs a new {@code SearchInteractionsRequest}.
   *
   * @param sourceId the interaction source ID search criteria to apply to the interactions
   * @param interactionIds the interaction ID search criteria to apply to the interactions
   * @param sortBy the method used to sort the interactions e.g. by occurred
   * @param sortDirection the sort direction to apply to the interactions
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public SearchInteractionsRequest(
      UUID sourceId,
      List<UUID> interactionIds,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.sourceId = sourceId;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the interaction ID search criteria to apply to the interactions.
   *
   * @return the interaction ID search criteria to apply to the interactions
   */
  public List<UUID> getInteractionIds() {
    return interactionIds;
  }

  /**
   * Returns the page index.
   *
   * @return the page index
   */
  public Integer getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the page size.
   *
   * @return the page size
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * Returns the method used to sort the interactions e.g. by occurred
   *
   * @return the method used to sort the interactions e.g. by occurred
   */
  public InteractionSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction to apply to the interactions.
   *
   * @return the sort direction to apply to the interactions
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the interaction source ID search criteria to apply to the interactions.
   *
   * @return the interaction source ID search criteria to apply to the interactions
   */
  public UUID getSourceId() {
    return sourceId;
  }

  /**
   * Set the interaction ID search criteria to apply to the interactions.
   *
   * @param interactionIds the interaction ID search criteria to apply to the interactions
   */
  public void setInteractionIds(List<UUID> interactionIds) {
    this.interactionIds = interactionIds;
  }

  /**
   * Set the page index.
   *
   * @param pageIndex the page index
   */
  public void setPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
  }

  /**
   * Set the page size.
   *
   * @param pageSize the page size
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Set the method used to sort the interactions e.g. by occurred
   *
   * @param sortBy the method used to sort the interactions e.g. by occurred
   */
  public void setSortBy(InteractionSortBy sortBy) {
    this.sortBy = sortBy;
  }

  /**
   * Set the sort direction to apply to the interactions.
   *
   * @param sortDirection the sort direction to apply to the interactions
   */
  public void setSortDirection(SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  /**
   * Set the interaction source ID search criteria to apply to the interactions.
   *
   * @param sourceId the interaction source ID search criteria to apply to the interactions
   */
  public void setSourceId(UUID sourceId) {
    this.sourceId = sourceId;
  }
}

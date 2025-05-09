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

package digital.inception.party.model;

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
 * The {@code Parties} class holds the results of a request to retrieve a list of parties.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of parties")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "parties",
  "total",
  "filter",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "Parties", namespace = "https://inception.digital/party")
@XmlType(
    name = "Parties",
    namespace = "https://inception.digital/party",
    propOrder = {
      "tenantId",
      "parties",
      "total",
      "filter",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Parties implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The filter that was applied to the parties. */
  @Schema(description = "The filter that was applied to the parties")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

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

  /** The parties. */
  @Schema(description = "The parties", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Parties", required = true)
  @XmlElement(name = "Party", required = true)
  private List<Party> parties;

  /** The method used to sort the parties e.g. by name. */
  @Schema(description = "The method used to sort the parties e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private PartySortBy sortBy;

  /** The sort direction that was applied to the parties. */
  @Schema(description = "The sort direction that was applied to the parties")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the parties are associated with. */
  @Schema(
      description = "The ID for the tenant the parties are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of parties. */
  @Schema(description = "The total number of parties", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code Parties}. */
  public Parties() {}

  /**
   * Constructs a new {@code Parties}.
   *
   * @param tenantId the ID for the tenant the parties are associated with
   * @param parties the parties
   * @param total the total number of parties
   * @param filter the filter that was applied to the parties
   * @param sortBy the method used to sort the parties e.g. by name
   * @param sortDirection the sort direction that was applied to the parties
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public Parties(
      UUID tenantId,
      List<Party> parties,
      long total,
      String filter,
      PartySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.tenantId = tenantId;
    this.parties = parties;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter that was applied to the parties.
   *
   * @return the filter that was applied to the parties
   */
  public String getFilter() {
    return filter;
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
   * Returns the parties.
   *
   * @return the parties
   */
  public List<Party> getParties() {
    return parties;
  }

  /**
   * Returns the method used to sort the parties e.g. by name.
   *
   * @return the method used to sort the parties
   */
  public PartySortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the parties.
   *
   * @return the sort direction that was applied to the parties
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the parties are associated with.
   *
   * @return the ID for the tenant the parties are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of parties.
   *
   * @return the total number of parties
   */
  public Long getTotal() {
    return total;
  }
}

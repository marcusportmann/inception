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
 * The {@code AssociationsForParty} class holds the results of a request to retrieve a list of
 * associations for a party.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of associations for a party")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "partyId",
  "associations",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "AssociationsForParty", namespace = "https://inception.digital/party")
@XmlType(
    name = "AssociationsForParty",
    namespace = "https://inception.digital/party",
    propOrder = {
      "tenantId",
      "partyId",
      "associations",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class AssociationsForParty implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The associations. */
  @Schema(description = "The associations", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Associations", required = true)
  @XmlElement(name = "Association", required = true)
  private List<Association> associations;

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

  /** The ID for the party the associations are associated with. */
  @Schema(
      description = "The ID for the party the associations are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyId", required = true)
  private UUID partyId;

  /** The method used to sort the associations e.g. by name. */
  @Schema(description = "The method used to sort the associations e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private AssociationSortBy sortBy;

  /** The sort direction that was applied to the associations. */
  @Schema(description = "The sort direction that was applied to the associations")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the associations are associated with. */
  @Schema(
      description = "The ID for the tenant the associations are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of associations. */
  @Schema(
      description = "The total number of associations",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code AssociationsForParty}. */
  public AssociationsForParty() {}

  /**
   * Constructs a new {@code AssociationsForParty}.
   *
   * @param tenantId the ID for the tenant the associations are associated with
   * @param partyId the ID for the party the associations are associated with
   * @param associations the associations
   * @param total the total number of associations
   * @param sortBy the method used to sort the associations e.g. by name
   * @param sortDirection the sort direction that was applied to the associations
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public AssociationsForParty(
      UUID tenantId,
      UUID partyId,
      List<Association> associations,
      long total,
      AssociationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.tenantId = tenantId;
    this.partyId = partyId;
    this.associations = associations;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the associations.
   *
   * @return the associations
   */
  public List<Association> getAssociations() {
    return associations;
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
   * Returns the ID for the party the associations are associated with.
   *
   * @return the ID for the party the associations are associated with
   */
  public UUID getPartyId() {
    return partyId;
  }

  /**
   * Returns the method used to sort the associations e.g. by name.
   *
   * @return the method used to sort the associations
   */
  public AssociationSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the associations.
   *
   * @return the sort direction that was applied to the associations
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the associations are associated with.
   *
   * @return the ID for the tenant the associations are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of associations.
   *
   * @return the total number of associations
   */
  public Long getTotal() {
    return total;
  }
}

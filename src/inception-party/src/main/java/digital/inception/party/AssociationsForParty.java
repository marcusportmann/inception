/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.sorting.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>AssociationsForParty</b> class holds the results of a request to retrieve a list of
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
@XmlRootElement(name = "AssociationsForParty", namespace = "http://inception.digital/party")
@XmlType(
    name = "AssociationsForParty",
    namespace = "http://inception.digital/party",
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

  private static final long serialVersionUID = 1000000;

  /** The associations. */
  @Schema(description = "The associations", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Associations", required = true)
  @XmlElement(name = "Association", required = true)
  private List<Association> associations;

  /** The optional page index. */
  @Schema(description = "The optional page index")
  @JsonProperty
  @XmlElement(name = "PageIndex")
  private Integer pageIndex;

  /** The optional page size. */
  @Schema(description = "The optional page size")
  @JsonProperty
  @XmlElement(name = "PageSize")
  private Integer pageSize;

  /** The ID for the party the associations are associated with. */
  @Schema(
      description = "The ID for the party the associations are associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyId", required = true)
  private UUID partyId;

  /** The optional method used to sort the associations e.g. by name. */
  @Schema(description = "The optional method used to sort the associations e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private AssociationSortBy sortBy;

  /** The optional sort direction that was applied to the associations. */
  @Schema(description = "The optional sort direction that was applied to the associations")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the associations are associated with. */
  @Schema(
      description = "The ID for the tenant the associations are associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of associations. */
  @Schema(description = "The total number of associations", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>AssociationsForParty</b>. */
  public AssociationsForParty() {}

  /**
   * Constructs a new <b>AssociationsForParty</b>.
   *
   * @param tenantId the ID for the tenant the associations are associated with
   * @param partyId the ID for the party the associations are associated with
   * @param associations the associations
   * @param total the total number of associations
   * @param sortBy the optional method used to sort the associations e.g. by name
   * @param sortDirection the optional sort direction that was applied to the associations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
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
   * Returns the optional page index.
   *
   * @return the optional page index
   */
  public Integer getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the optional page size.
   *
   * @return the optional page size
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
   * Returns the optional method used to sort the associations e.g. by name.
   *
   * @return the optional method used to sort the associations
   */
  public AssociationSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the optional sort direction that was applied to the associations.
   *
   * @return the optional sort direction that was applied to the associations
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

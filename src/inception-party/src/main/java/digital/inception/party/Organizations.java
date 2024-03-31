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

package digital.inception.party;

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
 * The <b>Organizations</b> class holds the results of a request to retrieve a list of
 * organizations.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of organizations")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "organizations",
  "total",
  "filter",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "Organizations", namespace = "http://inception.digital/party")
@XmlType(
    name = "Organizations",
    namespace = "http://inception.digital/party",
    propOrder = {
      "tenantId",
      "organizations",
      "total",
      "filter",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Organizations implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the organizations. */
  @Schema(description = "The optional filter that was applied to the organizations")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The organizations. */
  @Schema(description = "The organizations", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Organizations", required = true)
  @XmlElement(name = "Organization", required = true)
  private List<Organization> organizations;

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

  /** The optional method used to sort the organizations e.g. by name. */
  @Schema(description = "The optional method used to sort the organizations e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private OrganizationSortBy sortBy;

  /** The optional sort direction that was applied to the organizations. */
  @Schema(description = "The optional sort direction that was applied to the organizations")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the organizations are associated with. */
  @Schema(
      description = "The ID for the tenant the organizations are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of organizations. */
  @Schema(
      description = "The total number of organizations",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>Organizations</b>. */
  public Organizations() {}

  /**
   * Constructs a new <b>Organizations</b>.
   *
   * @param tenantId the ID for the tenant the organizations are associated with
   * @param organizations the organizations
   * @param total the total number of organizations
   * @param filter the optional filter that was applied to the organizations
   * @param sortBy the optional method used to sort the organizations e.g. by name
   * @param sortDirection the optional sort direction that was applied to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   */
  public Organizations(
      UUID tenantId,
      List<Organization> organizations,
      long total,
      String filter,
      OrganizationSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.tenantId = tenantId;
    this.organizations = organizations;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the organizations.
   *
   * @return the optional filter that was applied to the organizations
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Returns the organizations.
   *
   * @return the organizations
   */
  public List<Organization> getOrganizations() {
    return organizations;
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
   * Returns the optional method used to sort the organizations e.g. by name.
   *
   * @return the optional method used to sort the organizations
   */
  public OrganizationSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the optional sort direction that was applied to the organizations.
   *
   * @return the optional sort direction that was applied to the organizations
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the organizations are associated with.
   *
   * @return the ID for the tenant the organizations are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of organizations.
   *
   * @return the total number of organizations
   */
  public Long getTotal() {
    return total;
  }
}

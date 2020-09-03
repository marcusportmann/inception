/*
 * Copyright 2020 Marcus Portmann
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Organizations</code> class holds the results of a request to retrieve a list of
 * organizations.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of organizations")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"organizations", "total", "filter", "sortDirection", "pageIndex", "pageSize"})
@XmlRootElement(name = "Organizations", namespace = "http://party.inception.digital")
@XmlType(
    name = "Organizations",
    namespace = "http://party.inception.digital",
    propOrder = {"organizations", "total", "filter", "sortDirection", "pageIndex", "pageSize"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Organizations implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the organizations. */
  @Schema(description = "The optional filter that was applied to the organizations")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The organizations. */
  @Schema(description = "The organizations", required = true)
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

  /** The optional sort direction that was applied to the organizations. */
  @Schema(description = "The optional sort direction that was applied to the organizations")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of organizations. */
  @Schema(description = "The total number of organizations", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <code>Organizations</code>. */
  public Organizations() {}

  /**
   * Constructs a new <code>Organizations</code>.
   *
   * @param organizations the organizations
   * @param total the total number of organizations
   * @param filter the optional filter that was applied to the organizations
   * @param sortDirection the optional sort direction that was applied to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   */
  public Organizations(
      List<Organization> organizations,
      long total,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.organizations = organizations;
    this.total = total;
    this.filter = filter;
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
   * Returns the optional sort direction that was applied to the organizations.
   *
   * @return the optional sort direction that was applied to the organizations
   */
  public SortDirection getSortDirection() {
    return sortDirection;
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

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
 * The <code>Parties</code> class holds the results of a request to retrieve a list of parties.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Parties")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"parties", "total", "filter", "sortDirection", "pageIndex", "pageSize"})
@XmlRootElement(name = "Parties", namespace = "http://party.inception.digital")
@XmlType(
    name = "Parties",
    namespace = "http://party.inception.digital",
    propOrder = {"parties", "total", "filter", "sortDirection", "pageIndex", "pageSize"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Parties implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the parties. */
  @Schema(description = "The optional filter that was applied to the parties")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

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

  /** The parties. */
  @Schema(description = "The parties", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Parties", required = true)
  @XmlElement(name = "Party", required = true)
  private List<Party> parties;

  /** The optional sort direction that was applied to the parties. */
  @Schema(description = "The optional sort direction that was applied to the parties")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of parties. */
  @Schema(description = "The total number of parties", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <code>Parties</code>. */
  public Parties() {}

  /**
   * Constructs a new <code>Parties</code>.
   *
   * @param parties the parties
   * @param total the total number of parties
   * @param filter the optional filter that was applied to the parties
   * @param sortDirection the optional sort direction that was applied to the parties
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   */
  public Parties(
      List<Party> parties,
      long total,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.parties = parties;
    this.total = total;
    this.filter = filter;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the parties.
   *
   * @return the optional filter that was applied to the parties
   */
  public String getFilter() {
    return filter;
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
   * Returns the parties.
   *
   * @return the parties
   */
  public List<Party> getParties() {
    return parties;
  }

  /**
   * Returns the optional sort direction that was applied to the parties.
   *
   * @return the optional sort direction that was applied to the parties
   */
  public SortDirection getSortDirection() {
    return sortDirection;
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

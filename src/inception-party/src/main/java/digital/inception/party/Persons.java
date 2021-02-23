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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>Persons</b> class holds the results of a request to retrieve a list of persons.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of persons")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "persons",
  "total",
  "filter",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "Persons", namespace = "http://inception.digital/party")
@XmlType(
    name = "Persons",
    namespace = "http://inception.digital/party",
    propOrder = {"persons", "total", "filter", "sortBy", "sortDirection", "pageIndex", "pageSize"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Persons implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the persons. */
  @Schema(description = "The optional filter that was applied to the persons")
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

  /** The persons. */
  @Schema(description = "The persons", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Persons", required = true)
  @XmlElement(name = "Person", required = true)
  private List<Person> persons;

  /** The optional method used to sort the persons e.g. by name. */
  @Schema(description = "The optional method used to sort the persons e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private PersonSortBy sortBy;

  /** The optional sort direction that was applied to the persons. */
  @Schema(description = "The optional sort direction that was applied to the persons")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of persons. */
  @Schema(description = "The total number of persons", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>Persons</b>. */
  public Persons() {}

  /**
   * Constructs a new <b>Persons</b>.
   *
   * @param persons the persons
   * @param total the total number of persons
   * @param filter the optional filter that was applied to the persons
   * @param sortBy the optional method used to sort the persons e.g. by name
   * @param sortDirection the optional sort direction that was applied to the persons
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   */
  public Persons(
      List<Person> persons,
      long total,
      String filter,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.persons = persons;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the persons.
   *
   * @return the optional filter that was applied to the persons
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
   * Returns the persons.
   *
   * @return the persons
   */
  public List<Person> getPersons() {
    return persons;
  }

  /**
   * Returns the optional method used to sort the persons e.g. by name.
   *
   * @return the optional method used to sort the persons e.g. by name
   */
  public PersonSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the optional sort direction that was applied to the persons.
   *
   * @return the optional sort direction that was applied to the persons
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of persons.
   *
   * @return the total number of persons
   */
  public Long getTotal() {
    return total;
  }
}

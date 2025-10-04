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
 * The {@code Persons} class holds the results of a request to retrieve a list of persons.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of persons")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "persons",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "Persons", namespace = "https://inception.digital/party")
@XmlType(
    name = "Persons",
    namespace = "https://inception.digital/party",
    propOrder = {
      "tenantId",
      "persons",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Persons implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

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

  /** The persons. */
  @Schema(description = "The persons", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Persons", required = true)
  @XmlElement(name = "Person", required = true)
  private List<Person> persons;

  /** The method used to sort the persons e.g. by name. */
  @Schema(description = "The method used to sort the persons e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private PersonSortBy sortBy;

  /** The sort direction that was applied to the persons. */
  @Schema(description = "The sort direction that was applied to the persons")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the persons are associated with. */
  @Schema(
      description = "The ID for the tenant the persons are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of persons. */
  @Schema(description = "The total number of persons", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code Persons}. */
  public Persons() {}

  /**
   * Constructs a new {@code Persons}.
   *
   * @param tenantId the ID for the tenant the persons are associated with
   * @param persons the persons
   * @param total the total number of persons
   * @param sortBy the method used to sort the persons e.g. by name
   * @param sortDirection the sort direction that was applied to the persons
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public Persons(
      UUID tenantId,
      List<Person> persons,
      long total,
      PersonSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.tenantId = tenantId;
    this.persons = persons;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
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
   * Returns the persons.
   *
   * @return the persons
   */
  public List<Person> getPersons() {
    return persons;
  }

  /**
   * Returns the method used to sort the persons e.g. by name.
   *
   * @return the method used to sort the persons
   */
  public PersonSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the persons.
   *
   * @return the sort direction that was applied to the persons
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the persons are associated with.
   *
   * @return the ID for the tenant the persons are associated with
   */
  public UUID getTenantId() {
    return tenantId;
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

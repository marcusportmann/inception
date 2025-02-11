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

package demo.model;

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

/**
 * The <b>Vehicles</b> class holds the results of a request to retrieve a list of vehicles.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of vehicles")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"vehicles", "total", "sortDirection", "pageIndex", "pageSize", "filter"})
@XmlRootElement(name = "Vehicles", namespace = "https://demo")
@XmlType(
    name = "Vehicles",
    namespace = "https://demo",
    propOrder = {"vehicles", "total", "sortDirection", "pageIndex", "pageSize", "filter"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Vehicles implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The filter that was applied to the vehicles. */
  @Schema(description = "The filter that was applied to the vehicles")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

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

  /** The sort direction that was applied to the vehicles. */
  @Schema(
      description = "The sort direction that was applied to the vehicles",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortDirection", required = true)
  private SortDirection sortDirection;

  /** The total number of vehicles. */
  @Schema(description = "The total number of vehicles", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** The vehicles. */
  @Schema(description = "The vehicles", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Vehicles", required = true)
  @XmlElement(name = "Vehicle", required = true)
  private List<Vehicle> vehicles;

  /** Constructs a new <b>Vehicles</b>. */
  public Vehicles() {}

  /**
   * Constructs a new <b>Vehicles</b>.
   *
   * @param vehicles the vehicles
   * @param total the total number of vehicles
   * @param filter the filter that was applied to the vehicles
   * @param sortDirection the sort direction that was applied to the vehicles
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public Vehicles(
      List<Vehicle> vehicles,
      long total,
      String filter,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.vehicles = vehicles;
    this.total = total;
    this.filter = filter;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter that was applied to the vehicles.
   *
   * @return the filter that was applied to the vehicles
   */
  public String getFilter() {
    return filter;
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
   * Returns the sort direction that was applied to the vehicles.
   *
   * @return the sort direction that was applied to the vehicles
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of vehicles.
   *
   * @return the total number of vehicles
   */
  public long getTotal() {
    return total;
  }

  /**
   * Returns the vehicles.
   *
   * @return the vehicles
   */
  public List<Vehicle> getVehicles() {
    return vehicles;
  }
}

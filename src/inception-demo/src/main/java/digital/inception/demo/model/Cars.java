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

package digital.inception.demo.model;

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
 * The <b>Cars</b> class holds the results of a request to retrieve a list of cars.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of cars")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"cars", "total", "sortDirection", "pageIndex", "pageSize", "filter"})
@XmlRootElement(name = "Cars", namespace = "http://inception.digital/demo")
@XmlType(
    name = "Cars",
    namespace = "http://inception.digital/demo",
    propOrder = {"cars", "total", "sortDirection", "pageIndex", "pageSize", "filter"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Cars implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The cars. */
  @Schema(description = "The cars", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Cars", required = true)
  @XmlElement(name = "Car", required = true)
  private List<Car> cars;

  /** The optional filter that was applied to the cars. */
  @Schema(description = "The optional filter that was applied to the cars")
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

  /** The sort direction that was applied to the cars. */
  @Schema(
      description = "The sort direction that was applied to the cars",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortDirection", required = true)
  private SortDirection sortDirection;

  /** The total number of cars. */
  @Schema(description = "The total number of cars", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>Cars</b>. */
  public Cars() {}

  /**
   * Constructs a new <b>Cars</b>.
   *
   * @param cars the cars
   * @param total the total number of cars
   * @param filter the optional filter that was applied to the cars
   * @param sortDirection the sort direction that was applied to the cars
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public Cars(
      List<Car> cars,
      long total,
      String filter,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.cars = cars;
    this.total = total;
    this.filter = filter;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the cars.
   *
   * @return the cars
   */
  public List<Car> getCars() {
    return cars;
  }

  /**
   * Returns the optional filter that was applied to the cars.
   *
   * @return the optional filter that was applied to the cars
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
   * Returns the sort direction that was applied to the cars.
   *
   * @return the sort direction that was applied to the cars
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of cars.
   *
   * @return the total number of cars
   */
  public long getTotal() {
    return total;
  }
}

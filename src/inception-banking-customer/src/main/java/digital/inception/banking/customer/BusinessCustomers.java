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

package digital.inception.banking.customer;

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
 * The <b>BusinessCustomers</b> class holds the results of a request to retrieve a list of
 * business customers.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of business customers")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "businessCustomers",
    "total",
    "filter",
    "sortBy",
    "sortDirection",
    "pageIndex",
    "pageSize"
})
@XmlRootElement(
    name = "BusinessCustomers",
    namespace = "http://inception.digital/banking/customer")
@XmlType(
    name = "BusinessCustomers",
    namespace = "http://inception.digital/banking/customer",
    propOrder = {
        "businessCustomers",
        "total",
        "filter",
        "sortBy",
        "sortDirection",
        "pageIndex",
        "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class BusinessCustomers implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the business customers. */
  @Schema(description = "The optional filter that was applied to the business customers")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The business customers. */
  @Schema(description = "The business customers", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "BusinessCustomers", required = true)
  @XmlElement(name = "BusinessCustomer", required = true)
  private List<BusinessCustomer> businessCustomers;

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

  /** The optional method used to sort the business customers e.g. by name. */
  @Schema(description = "The optional method used to sort the business customers e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private BusinessCustomerSortBy sortBy;

  /** The optional sort direction that was applied to the business customers. */
  @Schema(description = "The optional sort direction that was applied to the business customers")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of business customers. */
  @Schema(description = "The total number of business customers", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>BusinessCustomers</b>. */
  public BusinessCustomers() {}

  /**
   * Constructs a new <b>BusinessCustomers</b>.
   *
   * @param businessCustomers the business customers
   * @param total the total number of business customers
   * @param filter the optional filter that was applied to the business customers
   * @param sortBy the optional method used to sort the business customers e.g. by name
   * @param sortDirection the optional sort direction that was applied to the business customers
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   */
  public BusinessCustomers(
      List<BusinessCustomer> businessCustomers,
      long total,
      String filter,
      BusinessCustomerSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.businessCustomers = businessCustomers;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the business customers.
   *
   * @return the optional filter that was applied to the business customers
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Returns the business customers.
   *
   * @return the business customers
   */
  public List<BusinessCustomer> getBusinessCustomers() {
    return businessCustomers;
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
   * Returns the optional method used to sort the business customers e.g. by name.
   *
   * @return the optional method used to sort the business customers e.g. by name
   */
  public BusinessCustomerSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the optional sort direction that was applied to the business customers.
   *
   * @return the optional sort direction that was applied to the business customers
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of business customers.
   *
   * @return the total number of business customers
   */
  public Long getTotal() {
    return total;
  }
}

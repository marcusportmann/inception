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

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <b>CustomerWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "CustomerService",
    name = "ICustomerService",
    targetNamespace = "http://inception.digital/banking/customer")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class CustomerWebService {

  /** The Customer Service. */
  private final ICustomerService customerService;

  /**
   * Constructs a new <b>CustomerWebService</b>.
   *
   * @param customerService the Customer Service
   */
  public CustomerWebService(ICustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * Create the new business customer.
   *
   * @param businessCustomer the business customer
   */
  @WebMethod(operationName = "CreateBusinessCustomer")
  public void createBusinessCustomer(
      @WebParam(name = "BusinessCustomer") @XmlElement(required = true)
          BusinessCustomer businessCustomer)
      throws InvalidArgumentException, DuplicateBusinessCustomerException,
          ServiceUnavailableException {
    customerService.createBusinessCustomer(businessCustomer);
  }

  /**
   * Create the new individual customer.
   *
   * @param individualCustomer the individual customer
   */
  @WebMethod(operationName = "CreateIndividualCustomer")
  public void createIndividualCustomer(
      @WebParam(name = "IndividualCustomer") @XmlElement(required = true)
          IndividualCustomer individualCustomer)
      throws InvalidArgumentException, DuplicateIndividualCustomerException,
          ServiceUnavailableException {
    customerService.createIndividualCustomer(individualCustomer);
  }

  /**
   * Retrieve the business customers.
   *
   * @param filter the optional filter to apply to the business customers
   * @param sortBy the optional method used to sort the business customers e.g. by name
   * @param sortDirection the optional sort direction to apply to the business customers
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the business customers
   */
  @WebMethod(operationName = "GetBusinessCustomers")
  public BusinessCustomers getBusinessCustomers(
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortBy") @XmlElement BusinessCustomerSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return customerService.getBusinessCustomers(filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the individual customers.
   *
   * @param filter the optional filter to apply to the individual customers
   * @param sortBy the optional method used to sort the individual customers e.g. by name
   * @param sortDirection the optional sort direction to apply to the individual customers
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the individual customers
   */
  @WebMethod(operationName = "GetIndividualCustomers")
  public IndividualCustomers getIndividualCustomers(
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortBy") @XmlElement IndividualCustomerSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return customerService.getIndividualCustomers(
        filter, sortBy, sortDirection, pageIndex, pageSize);
  }
}

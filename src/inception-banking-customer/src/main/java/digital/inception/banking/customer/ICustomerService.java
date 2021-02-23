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
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;

/**
 * The <b>ICustomerService</b> interface defines the functionality provided by a Customer Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface ICustomerService {

  /**
   * Create the new business customer.
   *
   * @param businessCustomer the business customer
   */
  void createBusinessCustomer(BusinessCustomer businessCustomer)
      throws InvalidArgumentException, DuplicateBusinessCustomerException,
          ServiceUnavailableException;

  /**
   * Create the new individual customer.
   *
   * @param individualCustomer the individual customer
   */
  void createIndividualCustomer(IndividualCustomer individualCustomer)
      throws InvalidArgumentException, DuplicateIndividualCustomerException,
          ServiceUnavailableException;

  /**
   * Delete the business customer.
   *
   * @param businessCustomerId the Universally Unique Identifier (UUID) for the business customer
   */
  void deleteBusinessCustomer(UUID businessCustomerId)
      throws InvalidArgumentException, BusinessCustomerNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the individual customer.
   *
   * @param individualCustomerId the Universally Unique Identifier (UUID) for the individual
   *     customer
   */
  void deleteIndividualCustomer(UUID individualCustomerId)
      throws InvalidArgumentException, IndividualCustomerNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the business customer.
   *
   * @param businessCustomerId the Universally Unique Identifier (UUID) for the business customer
   * @return the business customer
   */
  BusinessCustomer getBusinessCustomer(UUID businessCustomerId)
      throws InvalidArgumentException, BusinessCustomerNotFoundException,
          ServiceUnavailableException;

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
  BusinessCustomers getBusinessCustomers(
      String filter,
      BusinessCustomerSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the individual customer.
   *
   * @param individualCustomerId the Universally Unique Identifier (UUID) for the individual
   *     customer
   * @return the individual customer
   */
  IndividualCustomer getIndividualCustomer(UUID individualCustomerId)
      throws InvalidArgumentException, IndividualCustomerNotFoundException,
          ServiceUnavailableException;

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
  IndividualCustomers getIndividualCustomers(
      String filter,
      IndividualCustomerSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Update the business customer.
   *
   * @param businessCustomer the business customer
   */
  void updateBusinessCustomer(BusinessCustomer businessCustomer)
      throws InvalidArgumentException, BusinessCustomerNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the individual customer.
   *
   * @param individualCustomer the individual customer
   */
  void updateIndividualCustomer(IndividualCustomer individualCustomer)
      throws InvalidArgumentException, IndividualCustomerNotFoundException,
          ServiceUnavailableException;

  /**
   * Validate the business customer.
   *
   * @param businessCustomer the business customer
   * @return the constraint violations for the business customer
   */
  Set<ConstraintViolation<BusinessCustomer>> validateBusinessCustomer(
      BusinessCustomer businessCustomer);

  /**
   * Validate the individual customer.
   *
   * @param individualCustomer the individual customer
   * @return the constraint violations for the individual customer
   */
  Set<ConstraintViolation<IndividualCustomer>> validateIndividualCustomer(
      IndividualCustomer individualCustomer);
}

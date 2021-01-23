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

import digital.inception.core.validation.InvalidArgumentException;
import digital.inception.party.PartyServiceException;
import java.util.UUID;

/**
 * The <code>ICustomerService</code> interface defines the functionality provided by a Customer
 * Service implementation.
 *
 * @author Marcus Portmann
 */
public interface ICustomerService {

  /**
   * Create the new individual customer.
   *
   * @param individualCustomer the individual customer
   */
  void createIndividualCustomer(IndividualCustomer individualCustomer)
      throws InvalidArgumentException, DuplicateIndividualCustomerException,
          CustomerServiceException;

  /**
   * Retrieve the individual customer.
   *
   * @param individualCustomerId the Universally Unique Identifier (UUID) for the individual
   *     customer
   * @return the individual customer
   */
  IndividualCustomer getIndividualCustomer(UUID individualCustomerId)
      throws InvalidArgumentException, IndividualCustomerNotFoundException, PartyServiceException;
}
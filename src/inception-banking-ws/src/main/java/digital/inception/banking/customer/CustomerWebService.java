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

// ~--- JDK imports ------------------------------------------------------------

import digital.inception.core.validation.InvalidArgumentException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <code>CustomerWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "CustomerService",
    name = "ICustomerService",
    targetNamespace = "http://customer.banking.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class CustomerWebService {

  /** The Customer Service. */
  private final ICustomerService customerService;

  /**
   * Constructs a new <code>CustomerWebService</code>.
   *
   * @param customerService the Customer Service
   */
  public CustomerWebService(ICustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * Create the new individual customer.
   *
   * @param individualCustomer the individual customer
   */
  @WebMethod(operationName = "CreateIndividualCustomer")
  public void createCode(
      @WebParam(name = "IndividualCustomer") @XmlElement(required = true)
          IndividualCustomer individualCustomer)
      throws InvalidArgumentException, DuplicateIndividualCustomerException,
          CustomerServiceException {
    customerService.createIndividualCustomer(individualCustomer);
  }
}

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

import digital.inception.api.Problem;
import digital.inception.core.service.ServiceException;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;

/**
 * The <b>BusinessCustomerNotFoundException</b> exception is thrown to indicate an error condition
 * as a result of an business customer that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "http://inception.digital/problems/banking/customer/business-customer-not-found",
    title = "The business customer could not be found.",
    status = HttpStatus.NOT_FOUND)
@WebFault(
    name = "BusinessCustomerNotFoundException",
    targetNamespace = "http://inception.digital/banking/customer",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BusinessCustomerNotFoundException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>BusinessCustomerNotFoundException</b>.
   *
   * @param businessCustomerId the Universally Unique Identifier (UUID) for the business customer
   */
  public BusinessCustomerNotFoundException(UUID businessCustomerId) {
    super("The business customer (" + businessCustomerId + ") could not be found");
  }
}

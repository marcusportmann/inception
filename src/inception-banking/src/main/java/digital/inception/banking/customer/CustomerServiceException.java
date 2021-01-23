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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.service.ServiceException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CustomerServiceException</code> exception is thrown to indicate an error condition when
 * working with the Customer Service.
 *
 * <p>NOTE: This is a checked exception to prevent the automatic rollback of the current
 * transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(
    value = HttpStatus.INTERNAL_SERVER_ERROR,
    reason = "An error has occurred and the request could not be processed at this time")
@WebFault(
    name = "CustomerServiceException",
    targetNamespace = "http://customer.banking.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused", "WeakerAccess"})
public class CustomerServiceException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>CustomerServiceException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public CustomerServiceException(String message) {
    super("CustomerServiceError", message);
  }

  /**
   * Constructs a new <code>CustomerServiceException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method. (A
   *     <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public CustomerServiceException(String message, Throwable cause) {
    super("CustomerServiceError", message, cause);
  }
}
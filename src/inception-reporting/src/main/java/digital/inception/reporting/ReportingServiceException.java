/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.reporting;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.service.ServiceException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The <code>ReportingServiceException</code> exception is thrown to indicate an error condition
 * when working with the Reporting Service.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
    reason = "An error has occurred and the service is unable to process the request at this time")
@WebFault(name = "ReportingServiceException",
    targetNamespace = "http://reporting.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ReportingServiceException extends ServiceException
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ReportingServiceException</code> with the specified message.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  ReportingServiceException(String message)
  {
    super("ReportingServiceError", message);
  }

  /**
   * Constructs a new <code>ReportingServiceException</code> with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method.
   *                (A <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  ReportingServiceException(String message, Throwable cause)
  {
    super("ReportingServiceError", message, cause);
  }
}

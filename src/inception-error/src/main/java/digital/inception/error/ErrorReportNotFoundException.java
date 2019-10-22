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

package digital.inception.error;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.service.ServiceException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The <code>ErrorReportNotFoundException</code> exception is thrown to indicate that the required
 * error report could not be found when working with the Error Service.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The error report could not be found")
@WebFault(name = "ErrorReportNotFoundException", targetNamespace = "http://error.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({ "unused" })
public class ErrorReportNotFoundException extends ServiceException
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ErrorReportNotFoundException</code> with the specified message.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report
   */
  public ErrorReportNotFoundException(UUID errorReportId)
  {
    super("ErrorReportNotFoundError", "The error report (" + errorReportId + ") could not be found");
  }
}

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

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The <code>ReportDefinitionNotFoundException</code> exception is thrown to indicate an error
 * condition as a result of a report definition that could not be found.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The report definition could not be found")
@WebFault(name = "ReportDefinitionNotFoundException",
    targetNamespace = "http://reporting.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ReportDefinitionNotFoundException extends ServiceException
{
  private static final long serialVersionUID = 1000000;

  /**
   * w
   * Constructs a new <code>ReportDefinitionNotFoundException</code>.
   *
   * @param reportDefinitionId the ID used to uniquely identify the report definition
   */
  public ReportDefinitionNotFoundException(UUID reportDefinitionId)
  {
    super("ReportDefinitionNotFoundError", "The report definition with ID (" + reportDefinitionId + ") could not be found");
  }
}

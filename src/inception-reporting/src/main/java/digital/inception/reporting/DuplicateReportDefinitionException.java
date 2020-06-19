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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DuplicateReportDefinitionException</code> exception is thrown to indicate an error
 * condition as a result of an attempt to create a duplicate report definition i.e a report
 * definition with the specified ID already exists.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.CONFLICT,
    reason = "A report definition with the specified ID already exists")
@WebFault(name = "DuplicateReportDefinitionException",
    targetNamespace = "http://reporting.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateReportDefinitionException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>DuplicateReportDefinitionException</code>.
   *
   * @param reportDefinitionId the ID used to uniquely identify the report definition
   */
  public DuplicateReportDefinitionException(String reportDefinitionId) {
    super("DuplicateReportDefinitionError", "The report definition with ID (" + reportDefinitionId
        + ") already exists");
  }
}

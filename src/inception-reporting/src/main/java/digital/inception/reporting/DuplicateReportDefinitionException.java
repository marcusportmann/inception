/*
 * Copyright 2018 Marcus Portmann
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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

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
public class DuplicateReportDefinitionException extends Exception
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>DuplicateReportDefinitionException</code>.
   *
   * @param reportDefinitionId the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the report definition
   */
  public DuplicateReportDefinitionException(UUID reportDefinitionId)
  {
    super("The report definition wih ID (" + reportDefinitionId + ") already exists");
  }
}

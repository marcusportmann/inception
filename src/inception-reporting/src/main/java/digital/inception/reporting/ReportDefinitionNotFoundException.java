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
 * The <code>ReportDefinitionNotFoundException</code> exception is thrown to indicate an error
 * condition as a result of a report definition that could not be found.
 * <p/>
 * NOTE: This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The report definition could not be found")
public class ReportDefinitionNotFoundException extends Exception
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ReportDefinitionNotFoundException</code>.
   *
   */
  public ReportDefinitionNotFoundException(UUID reportId)
  {
    super("The report definition with ID (" + reportId + ") could not be found");
  }
}

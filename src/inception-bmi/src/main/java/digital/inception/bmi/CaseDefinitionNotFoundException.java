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

package digital.inception.bmi;

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The <b>CaseDefinitionNotFoundException</b> exception is thrown to indicate an error condition as
 * a result of a case definition that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "http://inception.digital/problems/bmi/case-definition-not-found",
    title = "The case definition could not be found.",
    status = 404)
@WebFault(
    name = "CaseDefinitionNotFoundException",
    targetNamespace = "http://inception.digital/bmi",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CaseDefinitionNotFoundException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * w Constructs a new <b>CaseDefinitionNotFoundException</b>.
   *
   * @param caseDefinitionId the ID for the case definition
   */
  public CaseDefinitionNotFoundException(String caseDefinitionId) {
    super("The case definition with ID (" + caseDefinitionId + ") could not be found");
  }
}

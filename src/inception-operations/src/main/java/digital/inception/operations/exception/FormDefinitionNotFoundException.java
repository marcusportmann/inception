/*
 * Copyright Marcus Portmann
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

package digital.inception.operations.exception;

import digital.inception.core.exception.Problem;
import digital.inception.core.exception.ServiceException;
import digital.inception.operations.model.WorkflowFormType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The {@code FormDefinitionNotFoundException} exception is thrown to indicate an error condition as
 * a result of a form definition that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/operations/form-definition-not-found",
    title = "The form definition could not be found.",
    status = 404)
@WebFault(
    name = "FormDefinitionNotFoundException",
    targetNamespace = "https://inception.digital/operations",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class FormDefinitionNotFoundException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code FormDefinitionNotFoundException}.
   *
   * @param formDefinitionId the ID for the form definition
   */
  public FormDefinitionNotFoundException(String formDefinitionId) {
    super("The form definition (" + formDefinitionId + ") could not be found");
  }

  /**
   * Constructs a new {@code FormDefinitionNotFoundException}.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @param workflowFormType the workflow form type
   */
  public FormDefinitionNotFoundException(
      String workflowDefinitionId,
      int workflowDefinitionVersion,
      WorkflowFormType workflowFormType) {
    super(
        "The form definition could not be found for the workflow form type ("
            + workflowFormType
            + ") for the workflow definition ("
            + workflowDefinitionId
            + ") version ("
            + workflowDefinitionVersion
            + ")");
  }
}

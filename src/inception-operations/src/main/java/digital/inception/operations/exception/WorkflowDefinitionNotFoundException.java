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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The {@code WorkflowDefinitionNotFoundException} exception is thrown to indicate an error
 * condition as a result of a workflow definition that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/operations/workflow-definition-not-found",
    title = "The workflow definition could not be found.",
    status = 404)
@WebFault(
    name = "WorkflowDefinitionNotFoundException",
    targetNamespace = "https://inception.digital/operations",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class WorkflowDefinitionNotFoundException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code WorkflowDefinitionNotFoundException}.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   */
  public WorkflowDefinitionNotFoundException(String workflowDefinitionId) {
    super("The workflow definition (" + workflowDefinitionId + ") could not be found");
  }
}

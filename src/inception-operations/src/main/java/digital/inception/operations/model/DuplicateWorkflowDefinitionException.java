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

package digital.inception.operations.model;

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The <b>DuplicateWorkflowDefinitionException</b> exception is thrown to indicate an error
 * condition as a result of an attempt to create a duplicate workflow definition, i.e. a workflow
 * definition with the specified ID already exists.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/operations/duplicate-workflow-definition",
    title = "A workflow definition with the specified ID already exists.",
    status = 409)
@WebFault(
    name = "DuplicateWorkflowDefinitionException",
    targetNamespace = "https://inception.digital/operations",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateWorkflowDefinitionException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>DuplicateWorkflowDefinitionException</b>.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   */
  public DuplicateWorkflowDefinitionException(String workflowDefinitionId) {
    super("The workflow definition (" + workflowDefinitionId + ") already exists");
  }
}

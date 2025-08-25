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
import java.util.UUID;

/**
 * The {@code WorkflowInteractionLinkNotFoundException} exception is thrown to indicate an error
 * condition as a result of a workflow interaction link that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/operations/workflow-interaction-link-not-found",
    title = "The workflow interaction link could not be found.",
    status = 404)
@WebFault(
    name = "WorkflowInteractionLinkNotFoundException",
    targetNamespace = "https://inception.digital/operations",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class WorkflowInteractionLinkNotFoundException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code WorkflowInteractionLinkNotFoundException}.
   *
   * @param tenantId the ID for the tenant the workflow interaction link is associated with
   * @param workflowId the ID for the workflow
   * @param interactionId the ID for the interaction
   */
  public WorkflowInteractionLinkNotFoundException(
      UUID tenantId, UUID workflowId, UUID interactionId) {
    super(
        "The link between the workflow ("
            + workflowId
            + ") and the interaction ("
            + interactionId
            + ") could not be found for the tenant ("
            + tenantId
            + ")");
  }
}

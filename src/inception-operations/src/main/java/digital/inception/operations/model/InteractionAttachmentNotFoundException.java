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
import java.util.UUID;

/**
 * The {@code InteractionAttachmentNotFoundException} exception is thrown to indicate an error
 * condition as a result of an interaction attachment that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/operations/interaction-attachment-not-found",
    title = "The interaction attachment could not be found.",
    status = 404)
@WebFault(
    name = "InteractionAttachmentNotFoundException",
    targetNamespace = "https://inception.digital/operations",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class InteractionAttachmentNotFoundException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * w Constructs a new {@code InteractionAttachmentNotFoundException}.
   *
   * @param interactionAttachmentId the ID for the interaction attachment
   */
  public InteractionAttachmentNotFoundException(UUID interactionAttachmentId) {
    super("The interaction attachment (" + interactionAttachmentId + ") could not be found");
  }
}

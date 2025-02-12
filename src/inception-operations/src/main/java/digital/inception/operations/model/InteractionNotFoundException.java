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
 * The <b>InteractionNotFoundException</b> exception is thrown to indicate an error condition as a
 * result of an interaction that could not be found.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/operations/interaction-not-found",
    title = "The interaction could not be found.",
    status = 404)
@WebFault(
    name = "InteractionNotFoundException",
    targetNamespace = "https://inception.digital/operations",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class InteractionNotFoundException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * w Constructs a new <b>InteractionNotFoundException</b>.
   *
   * @param interactionId the ID for the interaction
   */
  public InteractionNotFoundException(UUID interactionId) {
    super("The interaction (" + interactionId + ") could not be found");
  }
}

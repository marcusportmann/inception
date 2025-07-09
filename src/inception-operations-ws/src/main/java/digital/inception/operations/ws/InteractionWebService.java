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

package digital.inception.operations.ws;

import digital.inception.operations.service.InteractionService;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import org.springframework.context.ApplicationContext;

/**
 * The {@code InteractionWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "InteractionService",
    name = "IInteractionService",
    targetNamespace = "https://inception.digital/operations")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class InteractionWebService extends AbstractWebServiceBase {

  /** The Interaction Service. */
  private final InteractionService interactionService;

  /**
   * Constructs a new {@code InteractionWebService}.
   *
   * @param applicationContext the Spring application context
   * @param interactionService the Interaction Service
   */
  public InteractionWebService(
      ApplicationContext applicationContext, InteractionService interactionService) {
    super(applicationContext);

    this.interactionService = interactionService;
  }
}

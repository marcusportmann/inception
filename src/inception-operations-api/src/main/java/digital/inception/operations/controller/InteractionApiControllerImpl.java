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

package digital.inception.operations.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.operations.exception.DuplicateInteractionSourceException;
import digital.inception.operations.exception.InteractionSourceNotFoundException;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.service.InteractionService;
import java.util.Objects;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code InteractionApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class InteractionApiControllerImpl extends SecureApiController
    implements InteractionApiController {

  /** The Interaction Service. */
  private final InteractionService interactionService;

  /**
   * Constructs a new {@code InteractionApiControllerImpl}.
   *
   * @param applicationContext the Spring application context
   * @param interactionService the Interaction Service
   */
  public InteractionApiControllerImpl(
      ApplicationContext applicationContext, InteractionService interactionService) {
    super(applicationContext);

    this.interactionService = interactionService;
  }

  @Override
  public void createInteractionSource(UUID tenantId, InteractionSource interactionSource)
      throws InvalidArgumentException,
          DuplicateInteractionSourceException,
          ServiceUnavailableException {
    interactionService.createInteractionSource(tenantId, interactionSource);
  }

  @Override
  public void deleteInteractionSource(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    interactionService.deleteInteractionSource(tenantId, interactionSourceId);
  }

  @Override
  public void updateInteractionSource(
      UUID tenantId, UUID interactionSourceId, InteractionSource interactionSource)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (interactionSourceId == null) {
      throw new InvalidArgumentException("interactionSourceId");
    }

    if (!Objects.equals(interactionSourceId, interactionSource.getId())) {
      throw new InvalidArgumentException("interactionSource.id");
    }

    interactionService.updateInteractionSource(tenantId, interactionSource);
  }
}

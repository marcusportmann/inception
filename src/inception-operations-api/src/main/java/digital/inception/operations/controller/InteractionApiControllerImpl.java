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
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.TenantUtil;
import digital.inception.operations.exception.DuplicateInteractionException;
import digital.inception.operations.exception.DuplicateInteractionSourceException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InteractionNoteNotFoundException;
import digital.inception.operations.exception.InteractionSourceNotFoundException;
import digital.inception.operations.model.CreateInteractionNoteRequest;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourceSummary;
import digital.inception.operations.model.UpdateInteractionNoteRequest;
import digital.inception.operations.service.InteractionService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
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
  public void createInteraction(UUID tenantId, Interaction interaction)
      throws InvalidArgumentException, DuplicateInteractionException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    interactionService.createInteraction(tenantId, interaction);
  }

  @Override
  public UUID createInteractionNote(
      UUID tenantId, CreateInteractionNoteRequest createInteractionNoteRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    InteractionNote interactionNote =
        interactionService.createInteractionNote(
            tenantId, createInteractionNoteRequest, getAuthenticationName());

    return interactionNote.getId();
  }

  @Override
  public void createInteractionSource(UUID tenantId, InteractionSource interactionSource)
      throws InvalidArgumentException,
          DuplicateInteractionSourceException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    interactionService.createInteractionSource(tenantId, interactionSource);
  }

  @Override
  public void deleteInteractionSource(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    interactionService.deleteInteractionSource(tenantId, interactionSourceId);
  }

  @Override
  public InteractionNote getInteractionNote(
      UUID tenantId, UUID interactionId, UUID interactionNoteId)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!interactionService.interactionExists(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      if (!interactionService.interactionNoteExists(tenantId, interactionId, interactionNoteId)) {
        throw new InteractionNoteNotFoundException(tenantId, interactionNoteId);
      }
    } catch (InteractionNotFoundException | InteractionNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction note ("
              + interactionNoteId
              + ") for the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    return interactionService.getInteractionNote(tenantId, interactionNoteId);
  }

  @Override
  public InteractionNotes getInteractionNotes(
      UUID tenantId,
      UUID interactionId,
      String filter,
      InteractionNoteSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return interactionService.getInteractionNotes(
        tenantId, interactionId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public InteractionSource getInteractionSource(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return interactionService.getInteractionSource(tenantId, interactionSourceId);
  }

  @Override
  public List<InteractionSourceSummary> getInteractionSourceSummaries(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return interactionService.getInteractionSourceSummaries(tenantId);
  }

  @Override
  public List<InteractionSource> getInteractionSources(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return interactionService.getInteractionSources(tenantId);
  }

  @Override
  public void updateInteraction(UUID tenantId, UUID interactionId, Interaction interaction)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    if (!Objects.equals(interactionId, interaction.getId())) {
      throw new InvalidArgumentException("interaction.id");
    }

    interactionService.updateInteraction(tenantId, interaction);
  }

  @Override
  public void updateInteractionNote(
      UUID tenantId, UpdateInteractionNoteRequest updateInteractionNoteRequest)
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    InteractionNote interactionNote =
        interactionService.updateInteractionNote(
            tenantId, updateInteractionNoteRequest, getAuthenticationName());
  }

  @Override
  public void updateInteractionSource(
      UUID tenantId, UUID interactionSourceId, InteractionSource interactionSource)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (interactionSourceId == null) {
      throw new InvalidArgumentException("interactionSourceId");
    }

    if (!Objects.equals(interactionSourceId, interactionSource.getId())) {
      throw new InvalidArgumentException("interactionSource.id");
    }

    interactionService.updateInteractionSource(tenantId, interactionSource);
  }
}

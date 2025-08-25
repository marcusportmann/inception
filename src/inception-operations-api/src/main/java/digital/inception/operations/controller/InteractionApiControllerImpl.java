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
import digital.inception.operations.exception.PartyNotFoundException;
import digital.inception.operations.model.AssignInteractionRequest;
import digital.inception.operations.model.CreateInteractionNoteRequest;
import digital.inception.operations.model.DelinkPartyFromInteractionRequest;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionDirection;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
import digital.inception.operations.model.InteractionPermissionType;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourcePermission;
import digital.inception.operations.model.InteractionSourceSummary;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.LinkPartyToInteractionRequest;
import digital.inception.operations.model.TransferInteractionRequest;
import digital.inception.operations.model.UpdateInteractionNoteRequest;
import digital.inception.operations.service.InteractionService;
import java.util.ArrayList;
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
  public void assignInteraction(UUID tenantId, AssignInteractionRequest assignInteractionRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(
            tenantId, assignInteractionRequest.getInteractionId());

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.ASSIGN_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
    }

    interactionService.assignInteraction(tenantId, assignInteractionRequest);
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

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interaction.getSourceId(), InteractionPermissionType.ASSIGN_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interaction.getSourceId() + ")");
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

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(
            tenantId, createInteractionNoteRequest.getInteractionId());

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.CREATE_INTERACTION_NOTE))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
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
  public void deleteInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(tenantId, interactionId);

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.DELETE_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
    }

    interactionService.deleteInteraction(tenantId, interactionId);
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
  public void delinkPartyFromInteraction(
      UUID tenantId, DelinkPartyFromInteractionRequest delinkPartyFromInteractionRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(
            tenantId, delinkPartyFromInteractionRequest.getInteractionId());

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.LINK_PARTY_TO_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
    }

    interactionService.delinkPartyFromInteraction(tenantId, delinkPartyFromInteractionRequest);
  }

  @Override
  public Interaction getInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    Interaction interaction = interactionService.getInteraction(tenantId, interactionId);

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interaction.getSourceId(), InteractionPermissionType.RETRIEVE_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interaction.getSourceId() + ")");
    }

    return interaction;
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

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(tenantId, interactionId);

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.RETRIEVE_INTERACTION_NOTE))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
    }

    try {
      if (!interactionService.interactionNoteExists(tenantId, interactionId, interactionNoteId)) {
        throw new InteractionNoteNotFoundException(tenantId, interactionNoteId);
      }
    } catch (InteractionNoteNotFoundException e) {
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

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(tenantId, interactionId);

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.RETRIEVE_INTERACTION_NOTE))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
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

    List<InteractionSourceSummary> filteredInteractionSourceSummaries = new ArrayList<>();

    for (InteractionSourceSummary interactionSourceSummary :
        interactionService.getInteractionSourceSummaries(tenantId)) {
      if ((hasAccessToFunction("Operations.OperationsAdministration"))
          || (hasAccessToFunction("Operations.InteractionAdministration"))
          || (hasInteractionSourcePermission(
              tenantId,
              interactionSourceSummary.getId(),
              InteractionPermissionType.RETRIEVE_INTERACTION))) {
        filteredInteractionSourceSummaries.add(interactionSourceSummary);
      }
    }

    return filteredInteractionSourceSummaries;
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
  public InteractionSummaries getInteractionSummaries(
      UUID tenantId,
      UUID interactionSourceId,
      InteractionStatus status,
      InteractionDirection direction,
      String filter,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
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

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.RETRIEVE_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
    }

    return interactionService.getInteractionSummaries(
        tenantId,
        interactionSourceId,
        status,
        direction,
        filter,
        sortBy,
        sortDirection,
        pageIndex,
        pageSize);
  }

  @Override
  public void linkPartyToInteraction(
      UUID tenantId, LinkPartyToInteractionRequest linkPartyToInteractionRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(
            tenantId, linkPartyToInteractionRequest.getInteractionId());

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.LINK_PARTY_TO_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
    }

    interactionService.linkPartyToInteraction(tenantId, linkPartyToInteractionRequest);
  }

  @Override
  public void transferInteraction(
      UUID tenantId, TransferInteractionRequest transferInteractionRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(
            tenantId, transferInteractionRequest.getInteractionId());

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.TRANSFER_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
    }

    interactionService.transferInteraction(tenantId, transferInteractionRequest);
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

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteraction(tenantId, interactionId);

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.UPDATE_INTERACTION))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
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

    UUID interactionSourceId =
        interactionService.getInteractionSourceIdForInteractionNote(
            tenantId, updateInteractionNoteRequest.getInteractionNoteId());

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.InteractionAdministration"))
        && (!hasInteractionSourcePermission(
            tenantId, interactionSourceId, InteractionPermissionType.UPDATE_INTERACTION_NOTE))) {
      throw new AccessDeniedException(
          "Access denied to the interaction source (" + interactionSourceId + ")");
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

  /**
   * Confirm that the user associated with the authenticated request has the specified permission
   * for the interaction source with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @param permissionType the interaction permission type
   * @return {@code true} if the user associated with the authenticated request has the specified
   *     permission for the interaction source with the specified ID or {@code false} otherwise
   */
  private boolean hasInteractionSourcePermission(
      UUID tenantId, UUID interactionSourceId, InteractionPermissionType permissionType)
      throws ServiceUnavailableException {
    if (isSecurityDisabled()) {
      return true;
    }

    try {
      List<InteractionSourcePermission> interactionSourcePermissions =
          interactionService.getInteractionSourcePermissions(tenantId, interactionSourceId);

      // If no permissions have been defined for the interaction source, allow access by default
      // TODO: Verify if we should deny by default here instead -- MARCUS
      if (interactionSourcePermissions.isEmpty()) {
        return true;
      }

      for (InteractionSourcePermission interactionSourcePermission : interactionSourcePermissions) {
        if (interactionSourcePermission.getType().equals(permissionType)) {
          if (hasRole(interactionSourcePermission.getRoleCode())) {
            return true;
          }
        }
      }
    } catch (InteractionSourceNotFoundException e) {
      // Do nothing, we will return false below
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to verify whether the user associated with the authenticated request has the permission ("
              + permissionType.code()
              + ") for the interaction source ("
              + interactionSourceId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    return false;
  }
}

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

package digital.inception.operations.store;

import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.ServiceUtil;
import digital.inception.operations.exception.DuplicateInteractionAttachmentException;
import digital.inception.operations.exception.DuplicateInteractionException;
import digital.inception.operations.exception.DuplicateInteractionNoteException;
import digital.inception.operations.exception.InteractionAttachmentNotFoundException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InteractionNoteNotFoundException;
import digital.inception.operations.exception.PartyNotFoundException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentSortBy;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionAttachmentSummary;
import digital.inception.operations.model.InteractionDirection;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.InteractionSummary;
import digital.inception.operations.model.SearchInteractionsRequest;
import digital.inception.operations.persistence.jpa.InteractionAttachmentRepository;
import digital.inception.operations.persistence.jpa.InteractionAttachmentSummaryRepository;
import digital.inception.operations.persistence.jpa.InteractionNoteRepository;
import digital.inception.operations.persistence.jpa.InteractionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The {@code InternalInteractionStore} class provides the internal interaction store
 * implementation.
 *
 * @author Marcus Portmann
 */
@Component
@Conditional(InternalInteractionStoreEnabledCondition.class)
@SuppressWarnings("unused")
public class InternalInteractionStore implements InteractionStore {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(InternalInteractionStore.class);

  /* The name of the Interaction Service the Internal Interaction Store instance is associated with. */
  private final String instanceName = ServiceUtil.getServiceInstanceName("InteractionService");

  /** The Interaction Attachment Repository. */
  private final InteractionAttachmentRepository interactionAttachmentRepository;

  /** The Interaction Attachment Summary Repository. */
  private final InteractionAttachmentSummaryRepository interactionAttachmentSummaryRepository;

  /** The Interaction Note Repository. */
  private final InteractionNoteRepository interactionNoteRepository;

  /** The Interaction Repository. */
  private final InteractionRepository interactionRepository;

  /* Entity Manager */
  @PersistenceContext(unitName = "operations")
  private EntityManager entityManager;

  /**
   * Constructs a new {@code InternalInteractionStore}.
   *
   * @param interactionAttachmentRepository the Interaction Attachment Repository
   * @param interactionAttachmentSummaryRepository the Interaction Attachment Summary Repository
   * @param interactionNoteRepository the Interaction Note Repository
   * @param interactionRepository the Interaction Repository
   */
  public InternalInteractionStore(
      InteractionAttachmentRepository interactionAttachmentRepository,
      InteractionNoteRepository interactionNoteRepository,
      InteractionAttachmentSummaryRepository interactionAttachmentSummaryRepository,
      InteractionRepository interactionRepository) {
    this.interactionAttachmentRepository = interactionAttachmentRepository;
    this.interactionNoteRepository = interactionNoteRepository;
    this.interactionAttachmentSummaryRepository = interactionAttachmentSummaryRepository;
    this.interactionRepository = interactionRepository;
  }

  @Override
  public void assignInteraction(UUID tenantId, UUID interactionId, String assignedTo)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionRepository.existsByTenantIdAndId(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      interactionRepository.assignInteraction(interactionId, OffsetDateTime.now(), assignedTo);
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to assign the interaction ("
              + interactionId
              + ") to the user ("
              + assignedTo
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Interaction createInteraction(UUID tenantId, Interaction interaction)
      throws DuplicateInteractionException, ServiceUnavailableException {
    try {
      if (interactionRepository.existsById(interaction.getId())) {
        throw new DuplicateInteractionException(interaction.getId());
      }

      interactionRepository.saveAndFlush(interaction);

      return interaction;
    } catch (DuplicateInteractionException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the interaction ("
              + interaction.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionAttachment createInteractionAttachment(
      UUID tenantId, InteractionAttachment interactionAttachment)
      throws DuplicateInteractionAttachmentException, ServiceUnavailableException {
    try {
      if (interactionAttachmentRepository.existsById(interactionAttachment.getId())) {
        throw new DuplicateInteractionAttachmentException(interactionAttachment.getId());
      }

      interactionAttachmentRepository.saveAndFlush(interactionAttachment);

      return interactionAttachment;
    } catch (DuplicateInteractionAttachmentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the interaction attachment ("
              + interactionAttachment.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionNote createInteractionNote(UUID tenantId, InteractionNote interactionNote)
      throws DuplicateInteractionNoteException, ServiceUnavailableException {
    try {
      if (interactionNoteRepository.existsById(interactionNote.getId())) {
        throw new DuplicateInteractionNoteException(interactionNote.getId());
      }

      return interactionNoteRepository.saveAndFlush(interactionNote);
    } catch (DuplicateInteractionNoteException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the interaction note ("
              + interactionNote.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteInteraction(UUID tenantId, UUID interactionId)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionRepository.existsByTenantIdAndId(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      interactionRepository.deleteById(interactionId);
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionAttachmentRepository.existsByTenantIdAndId(
          tenantId, interactionAttachmentId)) {
        throw new InteractionAttachmentNotFoundException(interactionAttachmentId);
      }

      interactionAttachmentRepository.deleteById(interactionAttachmentId);
    } catch (InteractionAttachmentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the interaction attachment ("
              + interactionAttachmentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteInteractionNote(UUID tenantId, UUID interactionNoteId)
      throws InteractionNoteNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionNoteRepository.existsByTenantIdAndId(tenantId, interactionNoteId)) {
        throw new InteractionNoteNotFoundException(tenantId, interactionNoteId);
      }

      interactionNoteRepository.deleteById(interactionNoteId);
    } catch (InteractionNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the interaction note ("
              + interactionNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void delinkPartyFromInteraction(UUID tenantId, UUID interactionId)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (interactionRepository.delinkPartyFromInteraction(tenantId, interactionId) <= 0) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delink the party from the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Interaction getInteraction(UUID tenantId, UUID interactionId)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      /*
       * NOTE: The search by both tenant ID and interaction ID includes a security check to ensure
       * that the interaction not only exists, but is also associated with the specified tenant.
       */
      Optional<Interaction> interactionOptional =
          interactionRepository.findByTenantIdAndId(tenantId, interactionId);

      if (interactionOptional.isPresent()) {
        return interactionOptional.get();
      } else {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionAttachment getInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException {
    try {
      /*
       * NOTE: The search by both tenant ID and interaction attachment ID includes a security check
       *       to ensure that the interaction attachment not only exists, but is also associated
       *       with the specified tenant.
       */

      Optional<InteractionAttachment> interactionAttachmentOptional =
          interactionAttachmentRepository.findByTenantIdAndId(tenantId, interactionAttachmentId);

      if (interactionAttachmentOptional.isPresent()) {
        return interactionAttachmentOptional.get();
      } else {
        throw new InteractionAttachmentNotFoundException(interactionAttachmentId);
      }
    } catch (InteractionAttachmentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction attachment ("
              + interactionAttachmentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Optional<UUID> getInteractionAttachmentIdByInteractionIdAndHash(
      UUID tenantId, UUID interactionId, String hash) throws ServiceUnavailableException {
    try {
      return interactionAttachmentRepository.findIdByTenantIdAndInteractionIdAndHash(
          tenantId, interactionId, hash);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the ID for the interaction attachment with interaction ID ("
              + interactionId
              + ") and hash ("
              + hash
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionAttachmentSummaries getInteractionAttachmentSummaries(
      UUID tenantId,
      UUID interactionId,
      String filter,
      InteractionAttachmentSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (sortBy == InteractionAttachmentSortBy.NAME) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "name");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "name");
      }

      Page<InteractionAttachmentSummary> interactionAttachmentSummaryPage =
          interactionAttachmentSummaryRepository.findAll(
              (Specification<InteractionAttachmentSummary>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                    predicates.add(criteriaBuilder.equal(root.get("interactionId"), interactionId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("name")),
                                  "%" + filter.toLowerCase() + "%")));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new InteractionAttachmentSummaries(
          tenantId,
          interactionAttachmentSummaryPage.toList(),
          interactionAttachmentSummaryPage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered interaction attachment summaries for the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Optional<UUID> getInteractionIdBySourceIdAndSourceReference(
      UUID tenantId, UUID sourceId, String sourceReference) throws ServiceUnavailableException {
    try {
      return interactionRepository.findIdByTenantIdAndSourceIdAndSourceReference(
          tenantId, sourceId, sourceReference);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the ID for the interaction with source ID ("
              + sourceId
              + ") and source reference ("
              + sourceReference
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionNote getInteractionNote(UUID tenantId, UUID interactionNoteId)
      throws InteractionNoteNotFoundException, ServiceUnavailableException {
    try {
      /*
       * NOTE: The search by both tenant ID and interaction note ID includes a security check to ensure
       * that the interaction note not only exists, but is also associated with the specified tenant.
       */
      Optional<InteractionNote> interactionNoteOptional =
          interactionNoteRepository.findByTenantIdAndId(tenantId, interactionNoteId);

      if (interactionNoteOptional.isEmpty()) {
        throw new InteractionNoteNotFoundException(tenantId, interactionNoteId);
      }

      return interactionNoteOptional.get();
    } catch (InteractionNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction note ("
              + interactionNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionNotes getInteractionNotes(
      UUID tenantId,
      UUID interactionId,
      String filter,
      InteractionNoteSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionRepository.existsByTenantIdAndId(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      PageRequest pageRequest;

      if (sortBy == InteractionNoteSortBy.CREATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "created");
      } else if (sortBy == InteractionNoteSortBy.CREATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "createdBy");
      } else if (sortBy == InteractionNoteSortBy.UPDATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updated");
      } else if (sortBy == InteractionNoteSortBy.UPDATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updatedBy");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "created");
      }

      Page<InteractionNote> interactionNotePage =
          interactionNoteRepository.findAll(
              (Specification<InteractionNote>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
                    predicates.add(criteriaBuilder.equal(root.get("interactionId"), interactionId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("createdBy")),
                                  "%" + filter.toLowerCase() + "%"),
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("updatedBy")),
                                  "%" + filter.toLowerCase() + "%")));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new InteractionNotes(
          tenantId,
          interactionId,
          interactionNotePage.toList(),
          interactionNotePage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered interaction notes for the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public UUID getInteractionSourceIdForInteraction(UUID tenantId, UUID interactionId)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      Optional<UUID> interactionSourceIdOptional =
          interactionRepository.findInteractionSourceIdByTenantIdAndInteractionId(
              tenantId, interactionId);

      if (interactionSourceIdOptional.isEmpty()) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      return interactionSourceIdOptional.get();
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction source ID for the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public UUID getInteractionSourceIdForInteractionNote(UUID tenantId, UUID interactionNoteId)
      throws InteractionNoteNotFoundException, ServiceUnavailableException {
    try {
      Optional<UUID> interactionSourceIdOptional =
          interactionNoteRepository.getInteractionSourceIdByTenantIdAndInteractionNoteId(
              tenantId, interactionNoteId);

      if (interactionSourceIdOptional.isEmpty()) {
        throw new InteractionNoteNotFoundException(tenantId, interactionNoteId);
      }

      return interactionSourceIdOptional.get();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction source ID for the interaction note ("
              + interactionNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
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
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (sortBy == InteractionSortBy.OCCURRED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "occurred");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "occurred");
      }

      String filterLike = (filter == null) ? null : "%" + filter.toLowerCase() + "%";

      Page<InteractionSummary> interactionSummaryPage =
          interactionRepository.findInteractionSummaries(
              tenantId, interactionSourceId, status, direction, filterLike, pageRequest);

      return new InteractionSummaries(
          tenantId,
          interactionSummaryPage.toList(),
          interactionSummaryPage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered interaction summaries for the interaction source ("
              + interactionSourceId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Optional<Interaction> getNextInteractionQueuedForProcessing(UUID tenantId)
      throws ServiceUnavailableException {
    try {
      // Handle the situation where different time precisions are used in the database
      OffsetDateTime now = OffsetDateTime.now().plusSeconds(1);

      PageRequest pageRequest = PageRequest.of(0, 1);

      List<Interaction> interactions =
          interactionRepository.findInteractionsQueuedForProcessingForWrite(
              tenantId, now, pageRequest);

      if (!interactions.isEmpty()) {
        Interaction interaction = interactions.getFirst();

        OffsetDateTime locked = OffsetDateTime.now();

        interactionRepository.lockInteractionForProcessing(
            tenantId, interaction.getId(), instanceName, locked);

        entityManager.detach(interaction);

        interaction.setStatus(InteractionStatus.PROCESSING);
        interaction.setLocked(locked);
        interaction.setLockName(instanceName);
        interaction.incrementProcessingAttempts();

        return Optional.of(interaction);
      } else {
        return Optional.empty();
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the next interaction that has been queued for processing for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean interactionAttachmentExistsWithId(UUID tenantId, UUID interactionAttachmentId)
      throws ServiceUnavailableException {
    try {
      return interactionAttachmentRepository.existsByTenantIdAndId(
          tenantId, interactionAttachmentId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the interaction attachment ("
              + interactionAttachmentId
              + ") exists for the tenant ("
              + tenantId
              + ")");
    }
  }

  @Override
  public boolean interactionExists(UUID tenantId, UUID interactionId)
      throws ServiceUnavailableException {
    try {
      return interactionRepository.existsByTenantIdAndId(tenantId, interactionId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the interaction ("
              + interactionId
              + ") exists for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean interactionNoteExists(UUID tenantId, UUID interactionId, UUID interactionNoteId)
      throws ServiceUnavailableException {
    try {
      return interactionNoteRepository.existsByTenantIdAndInteractionIdAndId(
          tenantId, interactionId, interactionNoteId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the interaction note ("
              + interactionNoteId
              + ") exists for the interaction ("
              + interactionId
              + ") and tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void linkPartyToInteraction(UUID tenantId, UUID interactionId, UUID partyId)
      throws InteractionNotFoundException, PartyNotFoundException, ServiceUnavailableException {
    try {
      if (interactionRepository.linkPartyToInteraction(tenantId, interactionId, partyId) <= 0) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to link the party ("
              + partyId
              + ") to the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void resetInteractionLocks(
      UUID tenantId, InteractionStatus status, InteractionStatus newStatus)
      throws ServiceUnavailableException {
    try {
      interactionRepository.resetInteractionLocks(tenantId, status, newStatus, instanceName);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to reset the locks for the interactions with status ("
              + status
              + ") that have been locked using the lock name ("
              + instanceName
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionSummaries searchInteractions(
      UUID tenantId, SearchInteractionsRequest searchInteractionsRequest, int maxResults)
      throws ServiceUnavailableException {
    throw new ServiceUnavailableException("Not Implemented");
  }

  @Override
  public void transferInteraction(UUID tenantId, UUID interactionId, UUID interactionSourceId)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionRepository.existsByTenantIdAndId(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      interactionRepository.transferInteraction(interactionId, interactionSourceId);
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to transfer the interaction ("
              + interactionId
              + ") to the interaction source ("
              + interactionSourceId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void unlockInteraction(UUID tenantId, UUID interactionId, InteractionStatus status)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionRepository.existsByTenantIdAndId(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      interactionRepository.unlockInteraction(tenantId, interactionId, status);
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unlock and set the status for the interaction ("
              + interactionId
              + ") for the tenant ("
              + tenantId
              + ") to ("
              + status
              + ")",
          e);
    }
  }

  @Override
  public Interaction updateInteraction(UUID tenantId, Interaction interaction)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionRepository.existsByTenantIdAndId(tenantId, interaction.getId())) {
        throw new InteractionNotFoundException(tenantId, interaction.getId());
      }

      interactionRepository.saveAndFlush(interaction);

      return interaction;
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the interaction ("
              + interaction.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionAttachment updateInteractionAttachment(
      UUID tenantId, InteractionAttachment interactionAttachment)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionAttachmentRepository.existsByTenantIdAndId(
          tenantId, interactionAttachment.getId())) {
        throw new InteractionAttachmentNotFoundException(interactionAttachment.getId());
      }

      interactionAttachmentRepository.saveAndFlush(interactionAttachment);

      return interactionAttachment;
    } catch (InteractionAttachmentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the interaction attachment ("
              + interactionAttachment.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionNote updateInteractionNote(UUID tenantId, InteractionNote interactionNote)
      throws InteractionNoteNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionNoteRepository.existsByTenantIdAndId(tenantId, interactionNote.getId())) {
        throw new InteractionNoteNotFoundException(tenantId, interactionNote.getId());
      }

      return interactionNoteRepository.saveAndFlush(interactionNote);
    } catch (InteractionNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the interaction note ("
              + interactionNote.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }
}

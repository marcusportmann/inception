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

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.model.DuplicateInteractionAttachmentException;
import digital.inception.operations.model.DuplicateInteractionException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentNotFoundException;
import digital.inception.operations.model.InteractionNotFoundException;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.InteractionSummary;
import digital.inception.operations.persistence.InteractionAttachmentRepository;
import digital.inception.operations.persistence.InteractionRepository;
import digital.inception.operations.persistence.InteractionSummaryRepository;
import jakarta.persistence.criteria.Predicate;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The <b>InternalInteractionStore</b> class provides the internal interaction store implementation.
 *
 * @author Marcus Portmann
 */
@Service
@Conditional(InternalInteractionStoreEnabledCondition.class)
@SuppressWarnings("unused")
public class InternalInteractionStore implements InteractionStore {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(InternalInteractionStore.class);

  /** The Interaction Attachment Repository. */
  private final InteractionAttachmentRepository interactionAttachmentRepository;

  /** The Interaction Repository. */
  private final InteractionRepository interactionRepository;

  /** The Interaction Summary Repository. */
  private final InteractionSummaryRepository interactionSummaryRepository;

  /**
   * Constructs a new <b>InternalInteractionStore</b>.
   *
   * @param interactionAttachmentRepository the Interaction Attachment Repository
   * @param interactionRepository the Interaction Repository
   * @param interactionSummaryRepository the Interaction Summary Repository
   */
  public InternalInteractionStore(
      InteractionAttachmentRepository interactionAttachmentRepository,
      InteractionRepository interactionRepository,
      InteractionSummaryRepository interactionSummaryRepository) {
    this.interactionAttachmentRepository = interactionAttachmentRepository;
    this.interactionRepository = interactionRepository;
    this.interactionSummaryRepository = interactionSummaryRepository;
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
          "Failed to create the interaction (" + interaction.getId() + ")", e);
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
          "Failed to create the interaction attachment (" + interactionAttachment.getId() + ")", e);
    }
  }

  @Override
  public void deleteInteraction(UUID tenantId, UUID interactionId)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionRepository.existsById(interactionId)) {
        throw new InteractionNotFoundException(interactionId);
      }

      interactionRepository.deleteById(interactionId);
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the interaction (" + interactionId + ")", e);
    }
  }

  @Override
  public void deleteInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionAttachmentRepository.existsById(interactionAttachmentId)) {
        throw new InteractionAttachmentNotFoundException(interactionAttachmentId);
      }

      interactionAttachmentRepository.deleteById(interactionAttachmentId);
    } catch (InteractionAttachmentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the interaction attachment (" + interactionAttachmentId + ")", e);
    }
  }

  @Override
  public Interaction getInteraction(UUID tenantId, UUID interactionId)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      Optional<Interaction> interactionOptional = interactionRepository.findById(interactionId);

      if (interactionOptional.isPresent()) {
        return interactionOptional.get();
      } else {
        throw new InteractionNotFoundException(interactionId);
      }
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction (" + interactionId + ")", e);
    }
  }

  @Override
  public InteractionAttachment getInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException {
    try {
      Optional<InteractionAttachment> interactionAttachmentOptional =
          interactionAttachmentRepository.findById(interactionAttachmentId);

      if (interactionAttachmentOptional.isPresent()) {
        return interactionAttachmentOptional.get();
      } else {
        throw new InteractionAttachmentNotFoundException(interactionAttachmentId);
      }
    } catch (InteractionAttachmentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction attachment (" + interactionAttachmentId + ")", e);
    }
  }

  @Override
  public Optional<UUID> getInteractionAttachmentIdByInteractionIdAndHash(
      UUID tenantId, UUID interactionId, String hash) throws ServiceUnavailableException {
    try {
      return interactionAttachmentRepository.getIdByInteractionIdAndHash(interactionId, hash);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the ID for the interaction attachment with interaction ID ("
              + interactionId
              + ") and hash ("
              + hash
              + ")",
          e);
    }
  }

  @Override
  public Optional<UUID> getInteractionIdBySourceIdAndSourceReference(
      UUID tenantId, String sourceId, String sourceReference) throws ServiceUnavailableException {
    try {
      return interactionRepository.getIdBySourceIdAndSourceReference(sourceId, sourceReference);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the ID for the interaction with source ID ("
              + sourceId
              + ") and source reference ("
              + sourceReference
              + ")",
          e);
    }
  }

  @Override
  public InteractionSummaries getInteractionSummaries(
      UUID tenantId,
      String sourceId,
      InteractionStatus status,
      String filter,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (sortBy == InteractionSortBy.TIMESTAMP) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "timestamp");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "timestamp");
      }

      Page<InteractionSummary> interactionSummaryPage =
          interactionSummaryRepository.findAll(
              (Specification<InteractionSummary>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("sourceId"), sourceId));

                    if (status != null) {
                      predicates.add(criteriaBuilder.equal(root.get("status"), status));
                    }

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("from")),
                                  "%" + filter.toLowerCase() + "%"),
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("subject")),
                                  "%" + filter.toLowerCase() + "%")));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new InteractionSummaries(
          tenantId,
          interactionSummaryPage.toList(),
          interactionSummaryPage.getTotalElements(),
          sourceId,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered interaction summaries", e);
    }
  }

  @Override
  public Interaction updateInteraction(UUID tenantId, Interaction interaction)
      throws InteractionNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionRepository.existsById(interaction.getId())) {
        throw new InteractionNotFoundException(interaction.getId());
      }

      interactionRepository.saveAndFlush(interaction);

      return interaction;
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the interaction (" + interaction.getId() + ")", e);
    }
  }

  @Override
  public InteractionAttachment updateInteractionAttachment(UUID tenantId,
      InteractionAttachment interactionAttachment)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException {
    try {
      if (!interactionAttachmentRepository.existsById(interactionAttachment.getId())) {
        throw new InteractionAttachmentNotFoundException(interactionAttachment.getId());
      }

      interactionAttachmentRepository.saveAndFlush(interactionAttachment);

      return interactionAttachment;
    } catch (InteractionAttachmentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the interaction attachment (" + interactionAttachment.getId() + ")", e);
    }
  }
}

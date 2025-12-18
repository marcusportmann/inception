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

package digital.inception.operations.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IClientCredential;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.file.FileType;
import digital.inception.core.model.CodeEnum;
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.MimeData;
import digital.inception.operations.exception.DuplicateInteractionAttachmentException;
import digital.inception.operations.exception.DuplicateInteractionException;
import digital.inception.operations.exception.DuplicateInteractionNoteException;
import digital.inception.operations.exception.DuplicateInteractionSourceException;
import digital.inception.operations.exception.InteractionAttachmentNotFoundException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InteractionNoteNotFoundException;
import digital.inception.operations.exception.InteractionSourceNotFoundException;
import digital.inception.operations.exception.PartyNotFoundException;
import digital.inception.operations.model.AssignInteractionRequest;
import digital.inception.operations.model.CreateInteractionNoteRequest;
import digital.inception.operations.model.DelinkPartyFromInteractionRequest;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentSortBy;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionAttachmentSummary;
import digital.inception.operations.model.InteractionDirection;
import digital.inception.operations.model.InteractionMimeType;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
import digital.inception.operations.model.InteractionPriority;
import digital.inception.operations.model.InteractionProcessingResult;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourcePermission;
import digital.inception.operations.model.InteractionSourceSummary;
import digital.inception.operations.model.InteractionSourceType;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.InteractionSummary;
import digital.inception.operations.model.InteractionType;
import digital.inception.operations.model.LinkPartyToInteractionRequest;
import digital.inception.operations.model.MailboxInteractionSourceAttributeName;
import digital.inception.operations.model.MailboxProtocol;
import digital.inception.operations.model.SearchInteractionsRequest;
import digital.inception.operations.model.TransferInteractionRequest;
import digital.inception.operations.model.UpdateInteractionNoteRequest;
import digital.inception.operations.persistence.jpa.InteractionAttachmentRepository;
import digital.inception.operations.persistence.jpa.InteractionAttachmentSummaryRepository;
import digital.inception.operations.persistence.jpa.InteractionNoteRepository;
import digital.inception.operations.persistence.jpa.InteractionRepository;
import digital.inception.operations.persistence.jpa.InteractionSourceRepository;
import digital.inception.operations.persistence.jpa.InteractionSourceSummaryRepository;
import digital.inception.operations.util.AttributeUtil;
import digital.inception.operations.util.HtmlToSimplifiedHtml;
import digital.inception.operations.util.MessageUtil;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Flags.Flag;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The {@code InteractionServiceImpl} class provides the Interaction Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class InteractionServiceImpl extends AbstractServiceBase implements InteractionService {

  /** The characters for a base-62 encoded conversation ID. */
  private static final char[] CORRELATION_ID_CHARACTERS = {
    '2', '3', '4', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
    'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  };

  /** The regular expression pattern used to extract the conversation ID from an email subject. */
  private final Pattern conversationIdPattern = Pattern.compile("\\[CID:([A-Z0-9]+)\\]");

  /** The Interaction Attachment Repository. */
  private final InteractionAttachmentRepository interactionAttachmentRepository;

  /** The Interaction Attachment Summary Repository. */
  private final InteractionAttachmentSummaryRepository interactionAttachmentSummaryRepository;

  /** The Interaction Note Repository. */
  private final InteractionNoteRepository interactionNoteRepository;

  /** The Interaction Processor. */
  private final InteractionProcessor interactionProcessor;

  /** The Interaction Repository. */
  private final InteractionRepository interactionRepository;

  /** The Interaction Source Repository. */
  private final InteractionSourceRepository interactionSourceRepository;

  /** The Interaction Source Summary Repository. */
  private final InteractionSourceSummaryRepository interactionSourceSummaryRepository;

  private final SecureRandom secureRandom = new SecureRandom();

  /** The internal reference to the Interaction Service to enable caching. */
  private InteractionService InteractionService;

  /** The Background Interaction Processor. */
  private BackgroundInteractionProcessor backgroundInteractionProcessor;

  /* Entity Manager */
  @PersistenceContext(unitName = "operations")
  private EntityManager entityManager;

  /** The maximum number of filtered interactions that will be returned by the service. */
  @Value("${inception.operations.max-filtered-interaction-attachments:#{100}}")
  private int maxFilteredInteractionAttachments;

  /** The maximum number of filtered interaction notes that will be returned by the service. */
  @Value("${inception.operations.max-filtered-interaction-notes:#{100}}")
  private int maxFilteredInteractionNotes;

  /** The maximum number of filtered interactions that will be returned by the service. */
  @Value("${inception.operations.max-filtered-interactions:#{100}}")
  private int maxFilteredInteractions;

  /**
   * The minimum size for an image attachment on an email for it be processed as a valid attachment.
   */
  @Value("${inception.operations.min-image-attachment-size:20480}")
  private int minImageAttachmentSize;

  /**
   * Constructs a new {@code InteractionServiceImpl}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param interactionAttachmentRepository the Interaction Attachment Repository
   * @param interactionAttachmentSummaryRepository the Interaction Attachment Summary Repository
   * @param interactionNoteRepository the Interaction Note Repository
   * @param interactionRepository the Interaction Repository
   * @param interactionSourceRepository the Interaction Source Repository
   * @param interactionSourceSummaryRepository the Interaction Source Summary Repository
   * @param interactionProcessor the Interaction Processor
   */
  public InteractionServiceImpl(
      ApplicationContext applicationContext,
      InteractionAttachmentRepository interactionAttachmentRepository,
      InteractionAttachmentSummaryRepository interactionAttachmentSummaryRepository,
      InteractionNoteRepository interactionNoteRepository,
      InteractionRepository interactionRepository,
      InteractionSourceRepository interactionSourceRepository,
      InteractionSourceSummaryRepository interactionSourceSummaryRepository,
      InteractionProcessor interactionProcessor) {
    super(applicationContext);

    this.interactionAttachmentRepository = interactionAttachmentRepository;
    this.interactionAttachmentSummaryRepository = interactionAttachmentSummaryRepository;
    this.interactionNoteRepository = interactionNoteRepository;
    this.interactionRepository = interactionRepository;
    this.interactionSourceRepository = interactionSourceRepository;
    this.interactionSourceSummaryRepository = interactionSourceSummaryRepository;
    this.interactionProcessor = interactionProcessor;
  }

  @Override
  public void assignInteraction(UUID tenantId, AssignInteractionRequest assignInteractionRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("assignInteractionRequest", assignInteractionRequest);

    try {
      if (!interactionRepository.existsByTenantIdAndId(
          tenantId, assignInteractionRequest.getInteractionId())) {
        throw new InteractionNotFoundException(
            tenantId, assignInteractionRequest.getInteractionId());
      }

      interactionRepository.assignInteraction(
          assignInteractionRequest.getInteractionId(),
          OffsetDateTime.now(),
          assignInteractionRequest.getUsername());
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to assign the interaction ("
              + assignInteractionRequest.getInteractionId()
              + ") to the user ("
              + assignInteractionRequest.getUsername()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Transactional
  public Interaction createInteraction(UUID tenantId, Interaction interaction)
      throws InvalidArgumentException, DuplicateInteractionException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("interaction", interaction);

    if (!Objects.equals(tenantId, interaction.getTenantId())) {
      throw new InvalidArgumentException("interaction.tenantId");
    }

    try {
      // Retrieve the interaction source using the InteractionService interface to leverage caching
      getInteractionService().getInteractionSource(tenantId, interaction.getSourceId());

      if (interactionRepository.existsById(interaction.getId())) {
        throw new DuplicateInteractionException(interaction.getId());
      }

      interactionRepository.saveAndFlush(interaction);

      return interaction;
    } catch (DuplicateInteractionException e) {
      throw e;
    } catch (InteractionSourceNotFoundException e) {
      throw new InvalidArgumentException("interaction.sourceId");
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
  @Transactional
  public InteractionAttachment createInteractionAttachment(
      UUID tenantId, InteractionAttachment interactionAttachment)
      throws InvalidArgumentException,
          DuplicateInteractionAttachmentException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("interactionAttachment", interactionAttachment);

    if (!Objects.equals(tenantId, interactionAttachment.getTenantId())) {
      throw new InvalidArgumentException("interactionAttachment.tenantId");
    }

    try {
      // Retrieve the interaction source using the InteractionService interface to leverage caching
      getInteractionService().getInteractionSource(tenantId, interactionAttachment.getSourceId());

      if (interactionAttachmentRepository.existsById(interactionAttachment.getId())) {
        throw new DuplicateInteractionAttachmentException(interactionAttachment.getId());
      }

      interactionAttachmentRepository.saveAndFlush(interactionAttachment);

      return interactionAttachment;
    } catch (DuplicateInteractionAttachmentException e) {
      throw e;
    } catch (InteractionSourceNotFoundException e) {
      throw new InvalidArgumentException("interactionAttachment.sourceId");
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
  public InteractionNote createInteractionNote(
      UUID tenantId, CreateInteractionNoteRequest createInteractionNoteRequest, String createdBy)
      throws InvalidArgumentException,
          DuplicateInteractionNoteException,
          InteractionNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("createInteractionNoteRequest", createInteractionNoteRequest);

    if (!StringUtils.hasText(createdBy)) {
      throw new InvalidArgumentException("createdBy");
    }

    try {
      if (!interactionExists(tenantId, createInteractionNoteRequest.getInteractionId())) {
        throw new InteractionNotFoundException(
            tenantId, createInteractionNoteRequest.getInteractionId());
      }

      InteractionNote interactionNote =
          new InteractionNote(
              tenantId,
              createInteractionNoteRequest.getInteractionId(),
              createInteractionNoteRequest.getContent(),
              OffsetDateTime.now(),
              createdBy);

      if (interactionNoteRepository.existsById(interactionNote.getId())) {
        throw new DuplicateInteractionNoteException(interactionNote.getId());
      }

      return interactionNoteRepository.saveAndFlush(interactionNote);
    } catch (DuplicateInteractionNoteException | InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the interaction note for the interaction ("
              + createInteractionNoteRequest.getInteractionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "interactionSourcePermissions", key = "#interactionSource.id"),
        @CacheEvict(
            cacheNames = {"interactionSources", "interactionSourceSummaries"},
            key = "#tenantId")
      })
  public InteractionSource createInteractionSource(
      UUID tenantId, InteractionSource interactionSource)
      throws InvalidArgumentException,
          DuplicateInteractionSourceException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("interactionSource", interactionSource);

    if (!Objects.equals(tenantId, interactionSource.getTenantId())) {
      throw new InvalidArgumentException("interactionSource.tenantId");
    }

    try {
      if (interactionSourceRepository.existsById(interactionSource.getId())) {
        throw new DuplicateInteractionSourceException(interactionSource.getId());
      }

      interactionSourceRepository.saveAndFlush(interactionSource);

      return interactionSource;
    } catch (DuplicateInteractionSourceException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the interaction source ("
              + interactionSource.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Transactional
  public void deleteInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

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
  @Transactional
  public void deleteInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionAttachmentId == null) {
      throw new InvalidArgumentException("interactionAttachmentId");
    }

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
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionNoteId == null) {
      throw new InvalidArgumentException("interactionNoteId");
    }

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
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(
            cacheNames = {"interactionSource", "interactionSourcePermissions"},
            key = "#interactionSourceId"),
        @CacheEvict(
            cacheNames = {"interactionSources", "interactionSourceSummaries"},
            key = "#tenantId")
      })
  public void deleteInteractionSource(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionSourceId == null) {
      throw new InvalidArgumentException("interactionSourceId");
    }

    try {
      if (!interactionSourceRepository.existsByTenantIdAndId(tenantId, interactionSourceId)) {
        throw new InteractionSourceNotFoundException(tenantId, interactionSourceId);
      }

      interactionSourceRepository.deleteById(interactionSourceId);
    } catch (InteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the interaction source ("
              + interactionSourceId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void delinkPartyFromInteraction(
      UUID tenantId, DelinkPartyFromInteractionRequest delinkPartyFromInteractionRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("delinkPartyFromInteractionRequest", delinkPartyFromInteractionRequest);

    try {
      if (interactionRepository.delinkPartyFromInteraction(
              tenantId, delinkPartyFromInteractionRequest.getInteractionId())
          <= 0) {
        throw new InteractionNotFoundException(
            tenantId, delinkPartyFromInteractionRequest.getInteractionId());
      }
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delink the party from the interaction ("
              + delinkPartyFromInteractionRequest.getInteractionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Interaction getInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

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
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionAttachmentId == null) {
      throw new InvalidArgumentException("interactionAttachmentId");
    }

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
      Integer pageSize)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = InteractionAttachmentSortBy.NAME;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.DESCENDING;
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = maxFilteredInteractions;
    }

    try {
      if (!interactionExists(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      PageRequest pageRequest;

      if (sortBy == InteractionAttachmentSortBy.NAME) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxFilteredInteractionAttachments),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "name");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxFilteredInteractionAttachments),
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
          interactionAttachmentSummaryPage.toList(),
          interactionAttachmentSummaryPage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the interaction attachments for the interaction ("
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
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionNoteId == null) {
      throw new InvalidArgumentException("interactionNoteId");
    }

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
      Integer pageSize)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    try {
      if (!interactionRepository.existsByTenantIdAndId(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      PageRequest pageRequest;

      if (sortBy == InteractionNoteSortBy.CREATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxFilteredInteractionNotes),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "created");
      } else if (sortBy == InteractionNoteSortBy.CREATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxFilteredInteractionNotes),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "createdBy");
      } else if (sortBy == InteractionNoteSortBy.UPDATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxFilteredInteractionNotes),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updated");
      } else if (sortBy == InteractionNoteSortBy.UPDATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxFilteredInteractionNotes),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updatedBy");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxFilteredInteractionNotes),
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
          interactionNotePage.toList(),
          interactionNotePage.getTotalElements(),
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
  @Cacheable(cacheNames = "interactionSource", key = "#interactionSourceId")
  public InteractionSource getInteractionSource(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionSourceId == null) {
      throw new InvalidArgumentException("interactionSourceId");
    }

    try {
      /*
       * NOTE: The search by both tenant ID and interaction source ID includes a security check to
       *       ensure that the interaction source not only exists, but is also associated with the
       *       specified tenant.
       */

      Optional<InteractionSource> interactionSourceOptional =
          interactionSourceRepository.findByTenantIdAndId(tenantId, interactionSourceId);

      if (interactionSourceOptional.isPresent()) {
        return interactionSourceOptional.get();
      } else {
        throw new InteractionSourceNotFoundException(tenantId, interactionSourceId);
      }
    } catch (InteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction source ("
              + interactionSourceId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public UUID getInteractionSourceIdForInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

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
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionNoteId == null) {
      throw new InvalidArgumentException("interactionNoteId");
    }

    try {
      Optional<UUID> interactionSourceIdOptional =
          interactionNoteRepository.getInteractionSourceIdByTenantIdAndInteractionNoteId(
              tenantId, interactionNoteId);

      if (interactionSourceIdOptional.isEmpty()) {
        throw new InteractionNoteNotFoundException(tenantId, interactionNoteId);
      }

      return interactionSourceIdOptional.get();
    } catch (InteractionNoteNotFoundException e) {
      throw e;
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
  @Cacheable(cacheNames = "interactionSourcePermissions", key = "#interactionSourceId")
  public List<InteractionSourcePermission> getInteractionSourcePermissions(
      UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionSourceId == null) {
      throw new InvalidArgumentException("interactionSourceId");
    }

    try {
      if (!interactionSourceRepository.existsByTenantIdAndId(tenantId, interactionSourceId)) {
        throw new InteractionSourceNotFoundException(tenantId, interactionSourceId);
      }

      return interactionSourceRepository.findPermissionsBySourceId(interactionSourceId);
    } catch (InteractionSourceNotFoundException e) {
      throw e;

    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction source permissions for the interaction source ("
              + interactionSourceId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Cacheable(cacheNames = "interactionSourceSummaries", key = "#tenantId")
  public List<InteractionSourceSummary> getInteractionSourceSummaries(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      return interactionSourceSummaryRepository.findByTenantIdOrderByNameAsc(tenantId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the interaction sources for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Cacheable(cacheNames = "interactionSources", key = "#tenantId")
  public List<InteractionSource> getInteractionSources(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      return interactionSourceRepository.findByTenantIdOrderByNameAsc(tenantId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction sources for the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "interactionSources", key = "'ALL'")
  public List<InteractionSource> getInteractionSources() throws ServiceUnavailableException {
    try {
      return interactionSourceRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the interaction sources)", e);
    }
  }

  @Override
  public List<InteractionSource> getInteractionSources(
      UUID tenantId, InteractionSourceType interactionSourceType)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      return interactionSourceRepository.findByTenantIdAndTypeOrderByNameAsc(
          tenantId, interactionSourceType);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction sources with the type ("
              + interactionSourceType
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
      Integer pageSize)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionSourceId == null) {
      throw new InvalidArgumentException("sourceId");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = InteractionSortBy.OCCURRED;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.DESCENDING;
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = maxFilteredInteractions;
    }

    try {
      if (!interactionSourceRepository.existsByTenantIdAndId(tenantId, interactionSourceId)) {
        throw new InteractionSourceNotFoundException(tenantId, interactionSourceId);
      }

      Sort sort;

      if (sortBy == InteractionSortBy.OCCURRED) {
        sort =
            Sort.by(
                Order.asc("priority"),
                sortDirection.isAscending() ? Order.asc("occurred") : Order.desc("occurred"));
      } else {
        sort =
            Sort.by(
                Order.asc("priority"),
                sortDirection.isAscending() ? Order.asc("occurred") : Order.desc("occurred"));
      }

      PageRequest pageRequest =
          PageRequest.of(pageIndex, Math.min(pageSize, maxFilteredInteractions), sort);

      String filterLike = (filter == null) ? null : "%" + filter.toLowerCase() + "%";

      Page<InteractionSummary> interactionSummaryPage =
          interactionRepository.findInteractionSummaries(
              tenantId, interactionSourceId, status, direction, filterLike, pageRequest);

      return new InteractionSummaries(
          interactionSummaryPage.toList(),
          interactionSummaryPage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (InteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the interactions for the interaction source ("
              + interactionSourceId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean interactionAttachmentExistsWithId(UUID tenantId, UUID interactionAttachmentId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionAttachmentId == null) {
      throw new InvalidArgumentException("interactionAttachmentId");
    }

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
  public boolean interactionAttachmentExistsWithInteractionIdAndHash(
      UUID tenantId, UUID interactionId, String hash)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    if (!StringUtils.hasText(hash)) {
      throw new InvalidArgumentException("hash");
    }

    try {
      return getInteractionAttachmentIdByInteractionIdAndHash(tenantId, interactionId, hash)
          .isPresent();

    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the interaction attachment with interaction ID ("
              + interactionId
              + ") and hash ("
              + hash
              + ") exists for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  //  @Override
  //  public Optional<Interaction> getNextInteractionQueuedForProcessing(UUID tenantId)
  //      throws InvalidArgumentException, ServiceUnavailableException {
  //    if (tenantId == null) {
  //      throw new InvalidArgumentException("tenantId");
  //    }
  //
  //    try {
  //      return interactionStore.getNextInteractionQueuedForProcessing(tenantId);
  //    } catch (Throwable e) {
  //      throw new ServiceUnavailableException(
  //          "Failed to retrieve the next interaction queued for processing for the tenant ("
  //              + tenantId
  //              + ")",
  //          e);
  //    }
  //  }

  @Override
  public boolean interactionExists(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

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
  public boolean interactionExistsWithSourceIdAndSourceReference(
      UUID tenantId, UUID sourceId, String sourceReference)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (sourceId == null) {
      throw new InvalidArgumentException("sourceId");
    }

    if (!StringUtils.hasText(sourceReference)) {
      throw new InvalidArgumentException("sourceReference");
    }

    try {
      return getInteractionIdBySourceIdAndSourceReference(tenantId, sourceId, sourceReference)
          .isPresent();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the interaction with source ID ("
              + sourceId
              + ") and source reference ("
              + sourceReference
              + ") exists for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean interactionNoteExists(UUID tenantId, UUID interactionId, UUID interactionNoteId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    if (interactionNoteId == null) {
      throw new InvalidArgumentException("interactionNoteId");
    }

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
  public boolean interactionSourceExists(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionSourceId == null) {
      throw new InvalidArgumentException("interactionSourceId");
    }

    try {
      return interactionSourceRepository.existsByTenantIdAndId(tenantId, interactionSourceId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the interaction source ("
              + interactionSourceId
              + ") exists for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void linkPartyToInteraction(
      UUID tenantId, LinkPartyToInteractionRequest linkPartyToInteractionRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("linkPartyToInteractionRequest", linkPartyToInteractionRequest);

    try {
      // TODO: Check if party exists
      //noinspection ConstantValue
      if (false) {
        throw new PartyNotFoundException(tenantId, linkPartyToInteractionRequest.getPartyId());
      }

      if (interactionRepository.linkPartyToInteraction(
              tenantId,
              linkPartyToInteractionRequest.getInteractionId(),
              linkPartyToInteractionRequest.getPartyId())
          <= 0) {
        throw new InteractionNotFoundException(
            tenantId, linkPartyToInteractionRequest.getInteractionId());
      }
    } catch (InteractionNotFoundException | PartyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to link the party ("
              + linkPartyToInteractionRequest.getPartyId()
              + ") to the interaction ("
              + linkPartyToInteractionRequest.getInteractionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionProcessingResult processInteraction(Interaction interaction)
      throws InvalidArgumentException, ServiceUnavailableException {
    return interactionProcessor.processInteraction(interaction);
  }

  @Override
  public void processInteractions() throws ServiceUnavailableException {
    try {
      getBackgroundInteractionProcessor().triggerProcessing();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to trigger the interaction processing", e);
    }
  }

  @Override
  public InteractionSummaries searchInteractions(
      UUID tenantId, SearchInteractionsRequest searchInteractionsRequest)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("searchInteractionsRequest", searchInteractionsRequest);

    try {
      // Build Specification
      Specification<Interaction> specification =
          (root, query, criteriaBuilder) -> {
            // Avoid duplicates when joins or subqueries are involved
            if (query != null) {
              query.distinct(true);
            }

            // AND'ed top-level predicates
            List<Predicate> andPredicates = new ArrayList<>();

            // Tenant filter
            andPredicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

            // Top-level filters
            if (searchInteractionsRequest.getSourceId() != null) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      root.get("sourceId"), searchInteractionsRequest.getSourceId()));
            }

            // Match on any of the interaction IDs that have been specified
            if ((searchInteractionsRequest.getInteractionIds() != null)
                && (!searchInteractionsRequest.getInteractionIds().isEmpty())) {
              andPredicates.add(root.get("id").in(searchInteractionsRequest.getInteractionIds()));
            }

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
          };

      // Sorting
      String sortByPropertyName =
          InteractionSortBy.resolveSortByPropertyName(searchInteractionsRequest.getSortBy());
      Sort.Direction dir = resolveSortDirection(searchInteractionsRequest.getSortDirection());
      Sort sort = Sort.by(dir, sortByPropertyName);

      // Paging
      int pageIndex =
          searchInteractionsRequest.getPageIndex() == null
              ? 0
              : Math.max(0, searchInteractionsRequest.getPageIndex());
      int pageSize =
          searchInteractionsRequest.getPageSize() == null
              ? 50
              : Math.max(
                  1, Math.min(searchInteractionsRequest.getPageSize(), maxFilteredInteractions));
      Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

      final int firstResult = pageIndex * pageSize;

      // Retrieve the criteria builder
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

      // Create a query using the InteractionSummary projection
      CriteriaQuery<InteractionSummary> dataCriteriaQuery =
          criteriaBuilder.createQuery(InteractionSummary.class);

      // Create the root
      Root<Interaction> root = dataCriteriaQuery.from(Interaction.class);

      // Apply the spec as predicate
      Predicate dataPredicate = specification.toPredicate(root, dataCriteriaQuery, criteriaBuilder);

      // Select only the fields required by InteractionSummary (no counts variant)
      dataCriteriaQuery
          .select(
              criteriaBuilder.construct(
                  InteractionSummary.class,
                  root.get("id"),
                  root.get("tenantId"),
                  root.get("status"),
                  root.get("sourceId"),
                  root.get("conversationId"),
                  root.get("partyId"),
                  root.get("type"),
                  root.get("direction"),
                  root.get("sender"),
                  root.get("recipients"),
                  root.get("subject"),
                  root.get("mimeType"),
                  root.get("priority"),
                  root.get("occurred"),
                  root.get("assigned"),
                  root.get("assignedTo")))
          .where(dataPredicate);

      SortDirection sortDirection =
          (searchInteractionsRequest.getSortDirection() != null)
              ? searchInteractionsRequest.getSortDirection()
              : SortDirection.ASCENDING;

      if (sortDirection == SortDirection.ASCENDING) {
        dataCriteriaQuery.orderBy(
            criteriaBuilder.asc(
                root.get(
                    InteractionSortBy.resolveSortByPropertyName(
                        searchInteractionsRequest.getSortBy()))));
      } else {
        dataCriteriaQuery.orderBy(
            criteriaBuilder.desc(
                root.get(
                    InteractionSortBy.resolveSortByPropertyName(
                        searchInteractionsRequest.getSortBy()))));
      }

      TypedQuery<InteractionSummary> dataQuery = entityManager.createQuery(dataCriteriaQuery);
      dataQuery.setFirstResult(firstResult);
      dataQuery.setMaxResults(pageSize);

      List<InteractionSummary> interactionSummaries = dataQuery.getResultList();

      // Count query (for total elements)
      CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Interaction> countRoot = countCriteriaQuery.from(Interaction.class);

      Predicate countPredicate =
          specification.toPredicate(countRoot, countCriteriaQuery, criteriaBuilder);

      // Because we used query.distinct(true) above due to joins, prefer countDistinct on the PK
      countCriteriaQuery
          .select(criteriaBuilder.countDistinct(countRoot.get("id")))
          .where(countPredicate);

      Long total = entityManager.createQuery(countCriteriaQuery).getSingleResult();

      return new InteractionSummaries(
          interactionSummaries,
          total,
          searchInteractionsRequest.getSortBy(),
          searchInteractionsRequest.getSortDirection(),
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to search for interactions for the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  @Transactional
  public int synchronizeInteractionSource(InteractionSource interactionSource)
      throws InvalidArgumentException, ServiceUnavailableException {
    validateArgument("interactionSource", interactionSource);

    if (!interactionSource.isEnabled()) {
      return 0;
    }

    if (interactionSource.getType() == InteractionSourceType.MAILBOX) {
      return synchronizeMailboxInteractionSource(interactionSource);
    } else {
      throw new ServiceUnavailableException(
          "Failed to synchronize the interaction source ("
              + interactionSource.getId()
              + ") with the unsupported type ("
              + interactionSource.getType()
              + ")");
    }
  }

  @Override
  public void transferInteraction(
      UUID tenantId, TransferInteractionRequest transferInteractionRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("transferInteractionRequest", transferInteractionRequest);

    try {
      if (!interactionSourceRepository.existsByTenantIdAndId(
          tenantId, transferInteractionRequest.getInteractionSourceId())) {
        throw new InteractionSourceNotFoundException(
            tenantId, transferInteractionRequest.getInteractionSourceId());
      }

      if (!interactionRepository.existsByTenantIdAndId(
          tenantId, transferInteractionRequest.getInteractionId())) {
        throw new InteractionNotFoundException(
            tenantId, transferInteractionRequest.getInteractionId());
      }

      interactionRepository.transferInteraction(
          transferInteractionRequest.getInteractionId(),
          transferInteractionRequest.getInteractionSourceId());
    } catch (InteractionNotFoundException | InteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to transfer the interaction ("
              + transferInteractionRequest.getInteractionId()
              + ") to the interaction source ("
              + transferInteractionRequest.getInteractionSourceId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Transactional
  public Interaction updateInteraction(UUID tenantId, Interaction interaction)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("interaction", interaction);

    if (!Objects.equals(tenantId, interaction.getTenantId())) {
      throw new InvalidArgumentException("interaction.tenantId");
    }

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
  @Transactional
  public InteractionAttachment updateInteractionAttachment(
      UUID tenantId, InteractionAttachment interactionAttachment)
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("interactionAttachment", interactionAttachment);

    if (!Objects.equals(tenantId, interactionAttachment.getTenantId())) {
      throw new InvalidArgumentException("interactionAttachment.tenantId");
    }

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
  public InteractionNote updateInteractionNote(
      UUID tenantId, UpdateInteractionNoteRequest updateInteractionNoteRequest, String updatedBy)
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("updateInteractionNoteRequest", updateInteractionNoteRequest);

    try {
      if (!interactionNoteRepository.existsByTenantIdAndId(
          tenantId, updateInteractionNoteRequest.getInteractionNoteId())) {
        throw new InteractionNoteNotFoundException(
            tenantId, updateInteractionNoteRequest.getInteractionNoteId());
      }

      Optional<InteractionNote> interactionNoteOptional =
          interactionNoteRepository.findByTenantIdAndId(
              tenantId, updateInteractionNoteRequest.getInteractionNoteId());

      if (interactionNoteOptional.isEmpty()) {
        throw new InteractionNoteNotFoundException(
            tenantId, updateInteractionNoteRequest.getInteractionNoteId());
      }

      InteractionNote interactionNote = interactionNoteOptional.get();

      interactionNote.setContent(updateInteractionNoteRequest.getContent());
      interactionNote.setUpdated(OffsetDateTime.now());
      interactionNote.setUpdatedBy(updatedBy);

      return interactionNoteRepository.saveAndFlush(interactionNote);
    } catch (InteractionNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the interaction note ("
              + updateInteractionNoteRequest.getInteractionNoteId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "interactionSourcePermissions", key = "#interactionSource.id"),
        @CacheEvict(
            cacheNames = {"interactionSources", "interactionSourceSummaries"},
            key = "#tenantId")
      })
  public InteractionSource updateInteractionSource(
      UUID tenantId, InteractionSource interactionSource)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("interactionSource", interactionSource);

    if (!Objects.equals(tenantId, interactionSource.getTenantId())) {
      throw new InvalidArgumentException("interactionSource.tenantId");
    }

    try {
      if (!interactionSourceRepository.existsByTenantIdAndId(tenantId, interactionSource.getId())) {
        throw new InteractionSourceNotFoundException(tenantId, interactionSource.getId());
      }

      interactionSourceRepository.saveAndFlush(interactionSource);

      return interactionSource;
    } catch (InteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to updated the interaction source ("
              + interactionSource.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  private Interaction createEmailInteraction(InteractionSource interactionSource, Message message)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (message == null) {
      throw new InvalidArgumentException("message");
    }

    try {
      Interaction interaction = new Interaction();
      interaction.setId(UuidCreator.getTimeOrderedEpoch());
      interaction.setTenantId(interactionSource.getTenantId());
      interaction.setStatus(InteractionStatus.QUEUED);
      interaction.setSourceId(interactionSource.getId());
      interaction.setSourceReference(message.getHeader("Message-ID")[0]);

      /*
       * Attempt to extract the conversation ID from the message subject. If this is not available,
       * generate a new conversation ID.
       */
      String conversationId = extractConversationId(message.getSubject());

      if (StringUtils.hasText(conversationId)) {
        interaction.setConversationId(conversationId);
      } else {
        interaction.setConversationId(generateConversationId());
      }

      interaction.setType(InteractionType.EMAIL);
      interaction.setDirection(InteractionDirection.INBOUND);
      interaction.setSender(message.getFrom()[0].toString());
      interaction.setRecipients(
          Arrays.stream(message.getAllRecipients())
              .map(Address::toString) // or InternetAddress::getAddress
              .toList());
      interaction.setSubject(
          (message.getSubject().length() > 2000)
              ? message.getSubject().substring(0, 2000)
              : message.getSubject());

      Date receivedDate = message.getReceivedDate();

      if (receivedDate != null) {
        interaction.setOccurred(
            receivedDate.toInstant().atOffset(OffsetDateTime.now().getOffset()));
      } else {
        interaction.setOccurred(OffsetDateTime.now());
      }

      // TODO: Add support for prioritizing interactions based on email domain -- MARCUS
      interaction.setPriority(InteractionPriority.NORMAL);

      MimeData messageContent = MessageUtil.getMessageContent(message);

      if (messageContent.isMimeType("text/html")) {
        String simplifiedHtml =
            HtmlToSimplifiedHtml.convertToSimplifiedHtml(messageContent.getDataAsString());

        interaction.setMimeType(InteractionMimeType.TEXT_HTML);
        interaction.setContent(simplifiedHtml);
      } else {
        interaction.setMimeType(InteractionMimeType.TEXT_PLAIN);
        interaction.setContent(messageContent.getDataAsString());
      }

      return interaction;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the interaction from the email message", e);
    }
  }

  /**
   * Extracts the conversation ID from an email subject line.
   *
   * @param emailSubject the email subject line
   * @return the conversation ID if present, otherwise {@code null}
   */
  private String extractConversationId(String emailSubject) {
    // Create a matcher for the subject line
    Matcher matcher = conversationIdPattern.matcher(emailSubject);

    // Check if the pattern matches
    if (matcher.find()) {
      // Return the group that matches the conversation ID (inside the brackets)
      return matcher.group(1);
    }

    // Return null if no conversation ID is found
    return null;
  }

  private String generateConversationId() {
    long timestamp = System.currentTimeMillis();
    StringBuilder encoded = new StringBuilder();
    while (timestamp > 0) {
      int mod = (int) (timestamp % CORRELATION_ID_CHARACTERS.length);
      encoded.insert(0, CORRELATION_ID_CHARACTERS[mod]);
      timestamp /= CORRELATION_ID_CHARACTERS.length;
    }

    encoded.append(
        CORRELATION_ID_CHARACTERS[secureRandom.nextInt(CORRELATION_ID_CHARACTERS.length)]);

    return encoded.toString();
  }

  /**
   * Returns the lazily evaluated Background Interaction Processor to avoid circular references.
   *
   * @return the lazily evaluated Background Interaction Processor to avoid circular references.
   */
  private BackgroundInteractionProcessor getBackgroundInteractionProcessor() {
    if (backgroundInteractionProcessor == null) {
      backgroundInteractionProcessor =
          getApplicationContext().getBean(BackgroundInteractionProcessor.class);
    }

    return backgroundInteractionProcessor;
  }

  /**
   * Returns the internal reference to the Interaction Service to enable caching.
   *
   * @return the internal reference to the Interaction Service to enable caching.
   */
  private InteractionService getInteractionService() {
    if (InteractionService == null) {
      InteractionService = getApplicationContext().getBean(InteractionService.class);
    }

    return InteractionService;
  }

  private Store getMicrosoft365ImapStore(InteractionSource interactionSource) {
    boolean debug =
        AttributeUtil.getAttributeValueAsBoolean(
            interactionSource, MailboxInteractionSourceAttributeName.DEBUG.code());

    String host =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.HOST.code());
    if (!StringUtils.hasText(host)) {
      throw new IllegalArgumentException(
          "No \"host\" attribute specified for the Microsoft 365 IMAP store interaction source");
    }

    String portStr =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.PORT.code());
    if (!StringUtils.hasText(portStr)) {
      throw new IllegalArgumentException(
          "No \"port\" attribute specified for the Microsoft 365 IMAP store interaction source");
    }
    int port;
    try {
      port = Integer.parseInt(portStr);
      if (port <= 0 || port > 65535) throw new NumberFormatException();
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid \"port\" ("
              + portStr
              + ") specified for the Microsoft 365 IMAP store interaction source");
    }

    String emailAddress =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.EMAIL_ADDRESS.code());
    if (!StringUtils.hasText(emailAddress)) {
      throw new IllegalArgumentException(
          "No \"email_address\" attribute specified for the Microsoft 365 IMAP store interaction source");
    }

    String clientSecret =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.CREDENTIAL.code());
    if (!StringUtils.hasText(clientSecret)) {
      throw new IllegalArgumentException(
          "No \"credential\" attribute specified for the Microsoft 365 IMAP store interaction source");
    }

    String clientId =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.PRINCIPAL.code());
    if (!StringUtils.hasText(clientId)) {
      throw new IllegalArgumentException(
          "No \"principal\" attribute specified for the Microsoft 365 IMAP store interaction source");
    }

    String authority =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.AUTHORITY.code());
    if (!StringUtils.hasText(authority)) {
      throw new IllegalArgumentException(
          "No \"authority\" attribute specified for the Microsoft 365 IMAP store interaction source");
    }

    Properties props = new Properties();
    if (debug) {
      props.put("mail.debug", "true");
      props.put("mail.debug.auth", "true");
    }
    props.put("mail.store.protocol", "imaps");
    props.put("mail.imaps.host", host);
    props.put("mail.imaps.port", String.valueOf(port));
    props.put("mail.imaps.ssl.enable", "true");
    props.put("mail.imaps.starttls.enable", "true");
    props.put("mail.imaps.auth", "true");
    props.put("mail.imaps.auth.mechanisms", "XOAUTH2");
    props.put("mail.imaps.user", emailAddress);
    props.put("mail.imaps.auth.plain.disable", "true");
    props.put("mail.imaps.auth.xoauth2.disable", "false");

    Authenticator auth =
        new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            try {
              IClientCredential credential = ClientCredentialFactory.createFromSecret(clientSecret);

              ConfidentialClientApplication app =
                  ConfidentialClientApplication.builder(clientId, credential)
                      .authority(authority)
                      .build();

              ClientCredentialParameters params =
                  ClientCredentialParameters.builder(
                          Set.of("https://outlook.office365.com/.default"))
                      .build();

              String accessToken = app.acquireToken(params).get().accessToken();
              return new PasswordAuthentication(emailAddress, accessToken);
            } catch (Throwable t) {
              throw new RuntimeException(
                  "Failed to obtain the OAuth token for the interaction source ("
                      + interactionSource.getId()
                      + ")",
                  t);
            }
          }
        };

    try {
      Session session = Session.getInstance(props, auth);
      session.setDebug(debug);

      Store store = session.getStore("imaps");
      store.connect(); // triggers the Authenticator

      return store;
    } catch (Throwable t) {
      throw new RuntimeException(
          "Failed to retrieve the Microsoft 365 IMAP Store for the interaction source ("
              + interactionSource.getId()
              + ")",
          t);
    }
  }

  private Store getStandardImapStore(InteractionSource interactionSource, boolean isSecure) {
    String host =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.HOST.code());
    if (!StringUtils.hasText(host)) {
      throw new IllegalArgumentException(
          "No \"host\" attribute specified for the IMAP store interaction source");
    }

    String portStr =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.PORT.code());
    if (!StringUtils.hasText(portStr)) {
      throw new IllegalArgumentException(
          "No \"port\" attribute specified for the IMAP store interaction source");
    }
    int port;
    try {
      port = Integer.parseInt(portStr);
      if (port <= 0 || port > 65535) throw new NumberFormatException();
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid \"port\" (" + portStr + ") specified for the IMAP store interaction source");
    }

    String credential =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.CREDENTIAL.code());
    if (!StringUtils.hasText(credential)) {
      throw new IllegalArgumentException(
          "No \"credential\" attribute specified for the IMAP store interaction source");
    }

    String principal =
        AttributeUtil.getAttributeValue(
            interactionSource, MailboxInteractionSourceAttributeName.PRINCIPAL.code());
    if (!StringUtils.hasText(principal)) {
      throw new IllegalArgumentException(
          "No \"principal\" attribute specified for the IMAP store interaction source");
    }

    try {
      Properties properties = new Properties();

      if (isSecure) {
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", String.valueOf(port));
        properties.put("mail.imaps.ssl.enable", "true");
        properties.put("mail.imaps.starttls.enable", "true");
        // TODO: FIX THIS -- MARCUS
        properties.put("mail.imaps.ssl.checkserveridentity", "false");
        properties.put("mail.imaps.ssl.trust", "*");
      } else {
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", String.valueOf(port));
      }

      // Create session and store
      Session session = Session.getInstance(properties);
      Store store = session.getStore(isSecure ? "imaps" : "imap");

      store.connect(host, port, principal, credential);

      return store;
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to retrieve the standard IMAP store for the interaction source ("
              + interactionSource.getId()
              + ")",
          e);
    }
  }

  private Sort.Direction resolveSortDirection(SortDirection sortDirection) {
    if (sortDirection == null) {
      return Sort.Direction.DESC;
    } else if (sortDirection == SortDirection.ASCENDING) {
      return Sort.Direction.ASC;
    } else {
      return Sort.Direction.DESC;
    }
  }

  private int synchronizeMailboxInteractionSource(InteractionSource interactionSource)
      throws ServiceUnavailableException {
    int numberOfNewInteractions = 0;

    try {
      MailboxProtocol mailboxProtocol =
          CodeEnum.fromCode(
              MailboxProtocol.class,
              AttributeUtil.getAttributeValue(
                  interactionSource, MailboxInteractionSourceAttributeName.PROTOCOL.code()));

      /*
       * Retrieve the Java Mail session and store using the mailbox configuration for the mailbox
       * interaction source.
       */
      Store store;
      if (mailboxProtocol == MailboxProtocol.STANDARD_IMAP) {
        store = getStandardImapStore(interactionSource, false);
      } else if (mailboxProtocol == MailboxProtocol.STANDARD_IMAPS) {
        store = getStandardImapStore(interactionSource, true);
      } else if (mailboxProtocol == MailboxProtocol.MICROSOFT_365_IMAPS) {
        store = getMicrosoft365ImapStore(interactionSource);
      } else {
        throw new RuntimeException(
            "Unsupported mailbox protocol ("
                + mailboxProtocol.code()
                + ") for the mailbox interaction source ("
                + interactionSource.getId()
                + ") for the tenant ("
                + interactionSource.getTenantId()
                + ")");
      }

      // Access inbox
      try (Folder inboxFolder = store.getFolder("INBOX")) {
        inboxFolder.open(Folder.READ_WRITE);

        Message[] messages = inboxFolder.getMessages();

        // Process each message
        for (Message message : messages) {
          if (MessageUtil.isAutoResponseEmail(message)) {
            if (log.isDebugEnabled()) {
              log.debug(
                  "Skipping auto-response email ("
                      + message.getSubject()
                      + ") for the mailbox interaction source ("
                      + interactionSource.getId()
                      + ") for the tenant ("
                      + interactionSource.getTenantId()
                      + ")");
            }
            continue;
          }

          Interaction interaction = createEmailInteraction(interactionSource, message);

          Optional<UUID> interactionIdOptional =
              getInteractionIdBySourceIdAndSourceReference(
                  interaction.getTenantId(),
                  interaction.getSourceId(),
                  interaction.getSourceReference());

          UUID interactionId;

          // Create the interaction if required
          if (interactionIdOptional.isEmpty()) {
            interactionId = interaction.getId();

            getInteractionService().createInteraction(interactionSource.getTenantId(), interaction);

            numberOfNewInteractions++;
          } else {
            interactionId = interactionIdOptional.get();
          }

          for (MimeData interactionAttachmentMimeData :
              MessageUtil.getMessageAttachments(message, minImageAttachmentSize)) {

            // Create the interaction attachments if required
            if (!interactionAttachmentExistsWithInteractionIdAndHash(
                interactionSource.getTenantId(),
                interactionId,
                interactionAttachmentMimeData.getHash())) {
              InteractionAttachment interactionAttachment = new InteractionAttachment();

              interactionAttachment.setId(UuidCreator.getTimeOrderedEpoch());
              interactionAttachment.setSourceId(interactionSource.getId());
              interactionAttachment.setTenantId(interactionSource.getTenantId());
              interactionAttachment.setInteractionId(interactionId);

              try {
                interactionAttachment.setFileType(
                    FileType.fromMimeType(
                        interactionAttachmentMimeData.getMimeType().getBaseType().toLowerCase()));
              } catch (Throwable ignored) {
                interactionAttachment.setFileType(FileType.BINARY);
              }

              interactionAttachment.setName(
                  StringUtils.hasText(interactionAttachmentMimeData.getName())
                      ? interactionAttachmentMimeData.getName()
                      : "No Name");
              interactionAttachment.setHash(interactionAttachmentMimeData.getHash());
              interactionAttachment.setData(interactionAttachmentMimeData.getData());

              getInteractionService()
                  .createInteractionAttachment(
                      interactionSource.getTenantId(), interactionAttachment);

              // TODO: Publish event
            }
          }

          // Archive the message if required
          boolean archiveEmail =
              AttributeUtil.getAttributeValueAsBoolean(
                  interactionSource, MailboxInteractionSourceAttributeName.ARCHIVE_MAIL.code());

          boolean deleteEmail =
              AttributeUtil.getAttributeValueAsBoolean(
                  interactionSource, MailboxInteractionSourceAttributeName.DELETE_MAIL.code());

          if (archiveEmail) {
            try (Folder archiveFolder = store.getFolder("Archive")) {
              if (!archiveFolder.exists()) {
                archiveFolder.create(Folder.HOLDS_MESSAGES);
              }

              archiveFolder.open(Folder.READ_WRITE);

              inboxFolder.copyMessages(new Message[] {message}, archiveFolder);
              message.setFlag(Flag.DELETED, true);
            }
          }
          // Delete the message if required
          else if (deleteEmail) {
            message.setFlag(Flag.DELETED, true);
          }
        }

        // Expunge delete messages from INBOX
        Message[] expungedMessages = inboxFolder.expunge();

        if (expungedMessages.length > 0) {
          log.info(
              "Expunged "
                  + expungedMessages.length
                  + " messages from the INBOX for the mailbox interaction source ("
                  + interactionSource.getId()
                  + ") for the tenant ("
                  + interactionSource.getTenantId()
                  + ")");
        }
      } finally {
        store.close();
      }

      // Trigger the processing of any new interactions
      if (numberOfNewInteractions > 0) {
        getInteractionService().processInteractions();
      }

      return numberOfNewInteractions;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to synchronize the mailbox interaction source ("
              + interactionSource.getId()
              + ") for the tenant ("
              + interactionSource.getTenantId()
              + ")",
          e);
    }
  }
}

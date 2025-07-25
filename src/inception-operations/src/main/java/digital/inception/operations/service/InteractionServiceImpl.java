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
import digital.inception.operations.exception.DuplicateInteractionSourceException;
import digital.inception.operations.exception.InteractionAttachmentNotFoundException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InteractionSourceNotFoundException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentSortBy;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionMimeType;
import digital.inception.operations.model.InteractionProcessingResult;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourceType;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.InteractionType;
import digital.inception.operations.model.MailboxInteractionSourceAttributeName;
import digital.inception.operations.model.MailboxProtocol;
import digital.inception.operations.persistence.jpa.InteractionSourceRepository;
import digital.inception.operations.store.InteractionStore;
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
import java.security.SecureRandom;
import java.time.OffsetDateTime;
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
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

/**
 * The {@code InteractionServiceImpl} class provides the Interaction Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class InteractionServiceImpl extends AbstractServiceBase implements InteractionService {

  /** The characters for a base-62 encoded conversation ID. */
  private static final String BASE_62_ENCODING_CHARACTERS =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  private static final char[] CORRELATION_ID_CHARACTERS = {
    '2', '3', '4', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
    'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  };

  /** The maximum number of filtered interactions. */
  private static final int MAX_FILTERED_INTERACTIONS = 100;

  /** The maximum number of filtered interaction attachments. */
  private static final int MAX_FILTERED_INTERACTION_ATTACHMENTS = 100;

  /** The Spring application event publisher. */
  private final ApplicationEventPublisher applicationEventPublisher;

  /** The regular expression pattern used to extract the conversation ID from an email subject. */
  private final Pattern conversationIdPattern = Pattern.compile("\\[CID:([A-Z0-9]+)\\]");

  /** The Interaction Processor. */
  private final InteractionProcessor interactionProcessor;

  /** The Interaction Source Repository. */
  private final InteractionSourceRepository interactionSourceRepository;

  /** The Interaction Store. */
  private final InteractionStore interactionStore;

  private final SecureRandom secureRandom = new SecureRandom();

  /** The internal reference to the Interaction Service to enable caching. */
  private InteractionService InteractionService;

  /** The maximum number of processing attempts for an interaction. */
  @Value("${inception.operations.maximum-interaction-processing-attempts:#{100}}")
  private int maximumInteractionProcessingAttempts;

  /**
   * The minimum size for an image attachment on an email for it be processed as a valid attachment.
   */
  @Value("${inception.operations.minimum-image-attachment-size:20480}")
  private int minimumImageAttachmentSize;

  /**
   * Constructs a new {@code InteractionServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param applicationEventPublisher the Spring application event publisher
   * @param interactionStore the Interaction Store
   * @param interactionSourceRepository the Interaction Source Repository
   * @param interactionProcessor the Interaction Processor
   */
  public InteractionServiceImpl(
      ApplicationContext applicationContext,
      ApplicationEventPublisher applicationEventPublisher,
      InteractionStore interactionStore,
      InteractionSourceRepository interactionSourceRepository,
      InteractionProcessor interactionProcessor) {
    super(applicationContext);

    this.applicationEventPublisher = applicationEventPublisher;
    this.interactionStore = interactionStore;
    this.interactionSourceRepository = interactionSourceRepository;
    this.interactionProcessor = interactionProcessor;
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

      return interactionStore.createInteraction(tenantId, interaction);
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

      return interactionStore.createInteractionAttachment(tenantId, interactionAttachment);
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
  @Transactional
  @CachePut(cacheNames = "interactionSources", key = "#interactionSource.id")
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

    interactionStore.deleteInteraction(tenantId, interactionId);
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

    interactionStore.deleteInteractionAttachment(tenantId, interactionAttachmentId);
  }

  @Override
  @Transactional
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
        throw new InteractionSourceNotFoundException(interactionSourceId);
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
  public Interaction getInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    return interactionStore.getInteraction(tenantId, interactionId);
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

    return interactionStore.getInteractionAttachment(tenantId, interactionAttachmentId);
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
      pageSize = MAX_FILTERED_INTERACTIONS;
    }

    try {
      if (!interactionStore.interactionExistsWithId(tenantId, interactionId)) {
        throw new InteractionNotFoundException(interactionId);
      }

      return interactionStore.getInteractionAttachmentSummaries(
          tenantId,
          interactionId,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize,
          MAX_FILTERED_INTERACTION_ATTACHMENTS);
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
  @Cacheable(cacheNames = "interactionSources", key = "#interactionSourceId")
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
        throw new InteractionSourceNotFoundException(interactionSourceId);
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
      UUID sourceId,
      InteractionStatus status,
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

    if (sourceId == null) {
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
      pageSize = MAX_FILTERED_INTERACTIONS;
    }

    try {
      if (!interactionSourceRepository.existsByTenantIdAndId(tenantId, sourceId)) {
        throw new InteractionSourceNotFoundException(sourceId);
      }

      return interactionStore.getInteractionSummaries(
          tenantId,
          sourceId,
          status,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize,
          MAX_FILTERED_INTERACTIONS);
    } catch (InteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the interactions for the interaction source ("
              + sourceId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public int getMaximumInteractionProcessingAttempts() {
    return maximumInteractionProcessingAttempts;
  }

  @Override
  public Optional<Interaction> getNextInteractionQueuedForProcessing()
      throws ServiceUnavailableException {
    try {
      return interactionStore.getNextInteractionQueuedForProcessing();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the next interaction queued for processing", e);
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

    return interactionStore.interactionAttachmentExistsWithId(tenantId, interactionAttachmentId);
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
      return interactionStore
          .getInteractionAttachmentIdByInteractionIdAndHash(tenantId, interactionId, hash)
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

  @Override
  public boolean interactionExistsWithId(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    return interactionStore.interactionExistsWithId(tenantId, interactionId);
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
      return interactionStore
          .getInteractionIdBySourceIdAndSourceReference(tenantId, sourceId, sourceReference)
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
  public InteractionProcessingResult processInteraction(Interaction interaction)
      throws InvalidArgumentException, ServiceUnavailableException {
    return interactionProcessor.processInteraction(interaction);
  }

  @Override
  public void resetInteractionLocks(InteractionStatus status, InteractionStatus newStatus)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (status == null) {
      throw new InvalidArgumentException("status");
    }

    if (newStatus == null) {
      throw new InvalidArgumentException("newStatus");
    }

    try {
      interactionStore.resetInteractionLocks(status, newStatus);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to reset the locks for the interactions with status ("
              + status
              + ") and set their status to ("
              + newStatus
              + ")",
          e);
    }
  }

  @Override
  @Transactional
  public int synchronizeInteractionSource(InteractionSource interactionSource)
      throws InvalidArgumentException, ServiceUnavailableException {
    validateArgument("interactionSource", interactionSource);

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
  public void triggerInteractionProcessing() {
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            // Fire-and-forget trigger *after* the TX is really committed
            applicationEventPublisher.publishEvent(new TriggerInteractionProcessingEvent());
          }
        });
  }

  @Override
  public void triggerInteractionSourceSynchronization() {
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            // Fire-and-forget trigger *after* the TX is really committed
            applicationEventPublisher.publishEvent(
                new TriggerInteractionSourceSynchronizationEvent());
          }
        });
  }

  @Override
  public void unlockInteraction(UUID interactionId, InteractionStatus status)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    if (status == null) {
      throw new InvalidArgumentException("status");
    }

    try {
      interactionStore.unlockInteraction(interactionId, status);
    } catch (InteractionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unlock and set the status for the interaction ("
              + interactionId
              + ") to ("
              + status
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

    return interactionStore.updateInteraction(tenantId, interaction);
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

    return interactionStore.updateInteractionAttachment(tenantId, interactionAttachment);
  }

  @Override
  @CachePut(cacheNames = "interactionSources", key = "#interactionSource.id")
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
        throw new InteractionSourceNotFoundException(interactionSource.getId());
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
          Interaction interaction = createEmailInteraction(interactionSource, message);

          Optional<UUID> interactionIdOptional =
              interactionStore.getInteractionIdBySourceIdAndSourceReference(
                  interaction.getTenantId(),
                  interaction.getSourceId(),
                  interaction.getSourceReference());

          UUID interactionId;

          // Create the interaction if required
          if (interactionIdOptional.isEmpty()) {
            interactionId = interaction.getId();

            interactionStore.createInteraction(interactionSource.getTenantId(), interaction);

            numberOfNewInteractions++;
          } else {
            interactionId = interactionIdOptional.get();
          }

          for (MimeData interactionAttachmentMimeData :
              MessageUtil.getMessageAttachments(message, minimumImageAttachmentSize)) {

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

              interactionStore.createInteractionAttachment(
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
        triggerInteractionProcessing();
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

  /** The {@code TriggerInteractionProcessingEvent} record. */
  public record TriggerInteractionProcessingEvent() {}

  /** The {@code TriggerInteractionSourceSynchronizationEvent} record. */
  public record TriggerInteractionSourceSynchronizationEvent() {}
}

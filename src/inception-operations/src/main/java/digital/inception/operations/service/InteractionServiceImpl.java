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
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.IClientCredential;
import digital.inception.core.file.FileType;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.ValidationError;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.MimeData;
import digital.inception.operations.model.DuplicateInteractionAttachmentException;
import digital.inception.operations.model.DuplicateInteractionException;
import digital.inception.operations.model.DuplicateMailboxInteractionSourceException;
import digital.inception.operations.model.DuplicateWhatsAppInteractionSourceException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentNotFoundException;
import digital.inception.operations.model.InteractionMimeType;
import digital.inception.operations.model.InteractionNotFoundException;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.InteractionType;
import digital.inception.operations.model.MailboxInteractionSource;
import digital.inception.operations.model.MailboxInteractionSourceNotFoundException;
import digital.inception.operations.model.MailboxProtocol;
import digital.inception.operations.model.WhatsAppInteractionSource;
import digital.inception.operations.model.WhatsAppInteractionSourceNotFoundException;
import digital.inception.operations.persistence.jpa.MailboxInteractionSourceRepository;
import digital.inception.operations.persistence.jpa.WhatsAppInteractionSourceRepository;
import digital.inception.operations.store.InteractionStore;
import digital.inception.operations.util.HtmlToSimplifiedHtml;
import digital.inception.operations.util.MessageUtil;
import jakarta.mail.Authenticator;
import jakarta.mail.Flags.Flag;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The {@code InteractionServiceImpl} class provides the Interaction Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class InteractionServiceImpl implements InteractionService {

  /** The characters for a base-62 encoded conversation ID. */
  private static final String BASE_62_ENCODING_CHARACTERS =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  private static final char[] CORRELATION_ID_CHARACTERS = {
    '2', '3', '4', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
    'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  };

  /** The maximum number of filtered interactions. */
  private static final int MAX_FILTERED_INTERACTIONS = 100;

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The regular expression pattern used to extract the conversation ID from an email subject. */
  private final Pattern conversationIdPattern = Pattern.compile("\\[CID:([A-Z0-9]+)\\]");

  /** The Interaction Store. */
  private final InteractionStore interactionStore;

  /** The Mailbox Interaction Source Repository. */
  private final MailboxInteractionSourceRepository mailboxInteractionSourceRepository;

  private final SecureRandom secureRandom = new SecureRandom();

  /** The JSR-303 validator. */
  private final Validator validator;

  /** The WhatsApp Interaction Source Repository. */
  private final WhatsAppInteractionSourceRepository whatsAppInteractionSourceRepository;

  /**
   * The minimum size for an image attachment on an email for it be processed as a valid attachment.
   */
  @Value("${inception.operations.minimum-image-attachment-size:20480}")
  private int minimumImageAttachmentSize;

  /**
   * Creates a new {@code InteractionServiceImpl} instance.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-380 validator
   * @param interactionStore the Interaction Store
   * @param mailboxInteractionSourceRepository the Mailbox Interaction Source Repository
   * @param whatsAppInteractionSourceRepository the WhatsApp Interaction Source Repository
   */
  public InteractionServiceImpl(
      ApplicationContext applicationContext,
      Validator validator,
      InteractionStore interactionStore,
      MailboxInteractionSourceRepository mailboxInteractionSourceRepository,
      WhatsAppInteractionSourceRepository whatsAppInteractionSourceRepository) {
    this.applicationContext = applicationContext;
    this.validator = validator;
    this.interactionStore = interactionStore;
    this.mailboxInteractionSourceRepository = mailboxInteractionSourceRepository;
    this.whatsAppInteractionSourceRepository = whatsAppInteractionSourceRepository;
  }

  @Override
  @Transactional
  public Interaction createInteraction(Interaction interaction)
      throws InvalidArgumentException, DuplicateInteractionException, ServiceUnavailableException {
    validateInteraction(interaction);

    return interactionStore.createInteraction(interaction);
  }

  @Override
  @Transactional
  public InteractionAttachment createInteractionAttachment(
      InteractionAttachment interactionAttachment)
      throws InvalidArgumentException,
          DuplicateInteractionAttachmentException,
          ServiceUnavailableException {
    validateInteractionAttachment(interactionAttachment);

    return interactionStore.createInteractionAttachment(interactionAttachment);
  }

  @Override
  @Transactional
  public MailboxInteractionSource createMailboxInteractionSource(
      MailboxInteractionSource mailboxInteractionSource)
      throws InvalidArgumentException,
          DuplicateMailboxInteractionSourceException,
          ServiceUnavailableException {
    validateMailboxInteractionSource(mailboxInteractionSource);

    try {
      if (mailboxInteractionSourceRepository.existsById(mailboxInteractionSource.getId())) {
        throw new DuplicateMailboxInteractionSourceException(mailboxInteractionSource.getId());
      }

      mailboxInteractionSourceRepository.saveAndFlush(mailboxInteractionSource);

      return mailboxInteractionSource;
    } catch (DuplicateMailboxInteractionSourceException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the mailbox interaction source ("
              + mailboxInteractionSource.getId()
              + ")",
          e);
    }
  }

  @Override
  @Transactional
  public WhatsAppInteractionSource createWhatsAppInteractionSource(
      WhatsAppInteractionSource whatsAppInteractionSource)
      throws InvalidArgumentException,
          DuplicateWhatsAppInteractionSourceException,
          ServiceUnavailableException {
    validateWhatsAppInteractionSource(whatsAppInteractionSource);

    try {
      if (whatsAppInteractionSourceRepository.existsById(whatsAppInteractionSource.getId())) {
        throw new DuplicateWhatsAppInteractionSourceException(whatsAppInteractionSource.getId());
      }

      whatsAppInteractionSourceRepository.saveAndFlush(whatsAppInteractionSource);

      return whatsAppInteractionSource;
    } catch (DuplicateWhatsAppInteractionSourceException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the WhatsApp interaction source ("
              + whatsAppInteractionSource.getId()
              + ")",
          e);
    }
  }

  @Override
  @Transactional
  public void deleteInteraction(UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    interactionStore.deleteInteraction(interactionId);
  }

  @Override
  @Transactional
  public void deleteInteractionAttachment(UUID interactionAttachmentId)
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException {
    if (interactionAttachmentId == null) {
      throw new InvalidArgumentException("interactionAttachmentId");
    }

    interactionStore.deleteInteractionAttachment(interactionAttachmentId);
  }

  @Override
  @Transactional
  public void deleteMailboxInteractionSource(String mailboxInteractionSourceId)
      throws InvalidArgumentException,
          MailboxInteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(mailboxInteractionSourceId)) {
      throw new InvalidArgumentException("mailboxInteractionSourceId");
    }

    try {
      if (!mailboxInteractionSourceRepository.existsById(mailboxInteractionSourceId)) {
        throw new MailboxInteractionSourceNotFoundException(mailboxInteractionSourceId);
      }

      mailboxInteractionSourceRepository.deleteById(mailboxInteractionSourceId);
    } catch (MailboxInteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the mailbox interaction source (" + mailboxInteractionSourceId + ")",
          e);
    }
  }

  @Override
  @Transactional
  public void deleteWhatsAppInteractionSource(String whatsAppInteractionSourceId)
      throws InvalidArgumentException,
          WhatsAppInteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(whatsAppInteractionSourceId)) {
      throw new InvalidArgumentException("whatsAppInteractionSourceId");
    }

    try {
      if (!whatsAppInteractionSourceRepository.existsById(whatsAppInteractionSourceId)) {
        throw new WhatsAppInteractionSourceNotFoundException(whatsAppInteractionSourceId);
      }

      whatsAppInteractionSourceRepository.deleteById(whatsAppInteractionSourceId);
    } catch (WhatsAppInteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the WhatsApp interaction source (" + whatsAppInteractionSourceId + ")",
          e);
    }
  }

  @Override
  public Interaction getInteraction(UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    return interactionStore.getInteraction(interactionId);
  }

  @Override
  public InteractionAttachment getInteractionAttachment(UUID interactionAttachmentId)
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException {
    if (interactionAttachmentId == null) {
      throw new InvalidArgumentException("interactionAttachmentId");
    }

    return interactionStore.getInteractionAttachment(interactionAttachmentId);
  }

  @Override
  public InteractionSummaries getInteractionSummaries(
      String sourceId,
      InteractionStatus status,
      String filter,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(sourceId)) {
      throw new InvalidArgumentException("sourceId");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = InteractionSortBy.TIMESTAMP;
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

    return interactionStore.getInteractionSummaries(
        sourceId,
        status,
        filter,
        sortBy,
        sortDirection,
        pageIndex,
        pageSize,
        MAX_FILTERED_INTERACTIONS);
  }

  @Override
  public MailboxInteractionSource getMailboxInteractionSource(String mailboxInteractionSourceId)
      throws InvalidArgumentException,
          MailboxInteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(mailboxInteractionSourceId)) {
      throw new InvalidArgumentException("mailboxInteractionSourceId");
    }

    try {
      Optional<MailboxInteractionSource> mailboxInteractionSourceOptional =
          mailboxInteractionSourceRepository.findById(mailboxInteractionSourceId);

      if (mailboxInteractionSourceOptional.isPresent()) {
        return mailboxInteractionSourceOptional.get();
      } else {
        throw new MailboxInteractionSourceNotFoundException(mailboxInteractionSourceId);
      }
    } catch (MailboxInteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the mailbox interaction source (" + mailboxInteractionSourceId + ")",
          e);
    }
  }

  @Override
  public List<MailboxInteractionSource> getMailboxInteractionSources()
      throws ServiceUnavailableException {
    try {
      return mailboxInteractionSourceRepository.findAllByOrderByIdAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the mailbox interaction sources", e);
    }
  }

  @Override
  public WhatsAppInteractionSource getWhatsAppInteractionSource(String whatsAppInteractionSourceId)
      throws InvalidArgumentException,
          WhatsAppInteractionSourceNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(whatsAppInteractionSourceId)) {
      throw new InvalidArgumentException("whatsAppInteractionSourceId");
    }

    try {
      Optional<WhatsAppInteractionSource> whatsAppInteractionSourceOptional =
          whatsAppInteractionSourceRepository.findById(whatsAppInteractionSourceId);

      if (whatsAppInteractionSourceOptional.isPresent()) {
        return whatsAppInteractionSourceOptional.get();
      } else {
        throw new WhatsAppInteractionSourceNotFoundException(whatsAppInteractionSourceId);
      }
    } catch (WhatsAppInteractionSourceNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the WhatsApp interaction source ("
              + whatsAppInteractionSourceId
              + ")",
          e);
    }
  }

  @Override
  public List<WhatsAppInteractionSource> getWhatsAppInteractionSources()
      throws ServiceUnavailableException {
    try {
      return whatsAppInteractionSourceRepository.findAllByOrderByIdAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the WhatsApp interaction sources", e);
    }
  }

  @Override
  public boolean interactionAttachmentExistsWithInteractionIdAndHash(
      UUID interactionId, String hash)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (interactionId == null) {
      throw new InvalidArgumentException("interactionId");
    }

    if (!StringUtils.hasText(hash)) {
      throw new InvalidArgumentException("hash");
    }

    try {
      return interactionStore
          .getInteractionAttachmentIdByInteractionIdAndHash(interactionId, hash)
          .isPresent();

    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the interaction attachment with interaction ID ("
              + interactionId
              + ") and hash ("
              + hash
              + ") exists",
          e);
    }
  }

  @Override
  public boolean interactionExistsWithSourceIdAndSourceReference(
      String sourceId, String sourceReference)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (sourceId == null) {
      throw new InvalidArgumentException("sourceId");
    }

    if (!StringUtils.hasText(sourceReference)) {
      throw new InvalidArgumentException("sourceReference");
    }

    try {
      return interactionStore
          .getInteractionIdBySourceIdAndSourceReference(sourceId, sourceReference)
          .isPresent();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the interaction with source ID ("
              + sourceId
              + ") and source reference ("
              + sourceReference
              + ") exists",
          e);
    }
  }

  @Override
  @Transactional
  public int synchronizeMailboxInteractionSource(MailboxInteractionSource mailboxInteractionSource)
      throws ServiceUnavailableException {
    int numberOfNewInteractions = 0;

    try {
      /*
       * Retrieve the Java Mail session and store using the mailbox configuration for the mailbox
       * interaction source.
       */
      Store store;
      if (mailboxInteractionSource.getProtocol() == MailboxProtocol.STANDARD_IMAP) {
        store = getStandardImapStore(mailboxInteractionSource, false);
      } else if (mailboxInteractionSource.getProtocol() == MailboxProtocol.STANDARD_IMAPS) {
        store = getStandardImapStore(mailboxInteractionSource, true);
      } else if (mailboxInteractionSource.getProtocol() == MailboxProtocol.MICROSOFT_365_IMAPS) {
        store = getMicrosoft365ImapStore(mailboxInteractionSource);
      } else {
        throw new RuntimeException(
            "Unsupported mailbox protocol ("
                + mailboxInteractionSource.getProtocol().code()
                + ") for mailbox interaction source ("
                + mailboxInteractionSource.getId()
                + ")");
      }

      // Access inbox
      try (Folder inboxFolder = store.getFolder("INBOX")) {
        inboxFolder.open(Folder.READ_WRITE);

        Message[] messages = inboxFolder.getMessages();

        // Process each message
        for (Message message : messages) {
          Interaction interaction =
              createMessageInteractionForMailboxInteractionSource(
                  mailboxInteractionSource, message);

          Optional<UUID> interactionIdOptional =
              interactionStore.getInteractionIdBySourceIdAndSourceReference(
                  interaction.getSourceId(), interaction.getSourceReference());

          UUID interactionId;

          // Create the interaction if required
          if (interactionIdOptional.isEmpty()) {
            interactionId = interaction.getId();

            interactionStore.createInteraction(interaction);

            numberOfNewInteractions++;
          } else {
            interactionId = interactionIdOptional.get();
          }

          for (MimeData interactionAttachmentMimeData :
              MessageUtil.getMessageAttachments(message, minimumImageAttachmentSize)) {

            // Create the interaction attachments if required
            if (!interactionAttachmentExistsWithInteractionIdAndHash(
                interactionId, interactionAttachmentMimeData.getHash())) {
              InteractionAttachment interactionAttachment = new InteractionAttachment();

              interactionAttachment.setId(UuidCreator.getTimeOrderedEpoch());
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

              interactionStore.createInteractionAttachment(interactionAttachment);
            }
          }

          // Archive the message if required
          if (mailboxInteractionSource.getArchiveMail()) {
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
          else if (mailboxInteractionSource.getDeleteMail()) {
            message.setFlag(Flag.DELETED, true);
          }
        }

        // Expunge delete messages from INBOX
        Message[] expungedMessages = inboxFolder.expunge();
      } finally {
        store.close();
      }

      return numberOfNewInteractions;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to synchronize the mailbox interaction source ("
              + mailboxInteractionSource.getId()
              + ")",
          e);
    }
  }

  @Override
  @Transactional
  public Interaction updateInteraction(Interaction interaction)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException {
    validateInteraction(interaction);

    return interactionStore.updateInteraction(interaction);
  }

  @Override
  @Transactional
  public InteractionAttachment updateInteractionAttachment(
      InteractionAttachment interactionAttachment)
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException {
    validateInteractionAttachment(interactionAttachment);

    return interactionStore.updateInteractionAttachment(interactionAttachment);
  }

  private Interaction createMessageInteractionForMailboxInteractionSource(
      MailboxInteractionSource mailboxInteractionSource, Message message)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (message == null) {
      throw new InvalidArgumentException("message");
    }

    try {
      Interaction interaction = new Interaction();
      interaction.setId(UuidCreator.getTimeOrderedEpoch());
      interaction.setStatus(InteractionStatus.RECEIVED);
      interaction.setSourceId(mailboxInteractionSource.getId());
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
      interaction.setFromAddress(message.getFrom()[0]);
      interaction.setToAddresses(message.getAllRecipients());
      interaction.setSubject(
          (message.getSubject().length() > 2000)
              ? message.getSubject().substring(0, 2000)
              : message.getSubject());

      Date receivedDate = message.getReceivedDate();

      if (receivedDate != null) {
        interaction.setTimestamp(
            receivedDate.toInstant().atOffset(OffsetDateTime.now().getOffset()));
      } else {
        interaction.setTimestamp(OffsetDateTime.now());
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

  private Store getMicrosoft365ImapStore(MailboxInteractionSource mailboxInteractionSource) {
    try {
      Properties properties = new Properties();

      if (mailboxInteractionSource.getDebug()) {
        properties.put("mail.debug", "true");
        properties.put("mail.debug.auth", "true");
      }

      properties.put("mail.store.protocol", "imaps");
      properties.put("mail.imaps.host", mailboxInteractionSource.getHost());
      properties.put("mail.imaps.port", String.valueOf(mailboxInteractionSource.getPort()));
      properties.put("mail.imaps.ssl.enable", "true");
      properties.put("mail.imaps.starttls.enable", "true");
      properties.put("mail.imaps.auth", "true");
      properties.put("mail.imaps.auth.mechanisms", "XOAUTH2");
      properties.put("mail.imaps.user", mailboxInteractionSource.getEmailAddress());

      properties.put("mail.imaps.auth.plain.disable", "true");
      properties.put("mail.imaps.auth.xoauth2.disable", "false");

      Authenticator authenticator =
          new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
              try {
                IClientCredential clientCredential =
                    ClientCredentialFactory.createFromSecret(
                        mailboxInteractionSource.getCredential());

                ConfidentialClientApplication app =
                    ConfidentialClientApplication.builder(
                            mailboxInteractionSource.getPrincipal(), clientCredential)
                        .authority(mailboxInteractionSource.getAuthority())
                        .build();

                String scopes = "https://outlook.office365.com/.default";

                ClientCredentialParameters clientCredentialParam =
                    ClientCredentialParameters.builder(
                            Arrays.stream(scopes.split(","))
                                .map(String::trim)
                                .collect(Collectors.toSet()))
                        .build();

                CompletableFuture<IAuthenticationResult> future =
                    app.acquireToken(clientCredentialParam);

                String accessToken;
                try {
                  accessToken = future.get().accessToken();
                } catch (Throwable e) {
                  throw new RuntimeException(
                      "Failed to retrieve the OAuth token used to access the Microsoft 365 IMAP mailbox ("
                          + mailboxInteractionSource.getEmailAddress()
                          + ")",
                      e);
                }

                return new PasswordAuthentication(
                    mailboxInteractionSource.getEmailAddress(), accessToken);
              } catch (Throwable e) {
                throw new RuntimeException(
                    "Failed to create the PasswordAuthentication for Microsoft 365 IMAP mailbox ("
                        + mailboxInteractionSource.getEmailAddress()
                        + ")",
                    e);
              }
            }
          };

      Session session = Session.getInstance(properties, authenticator);

      if (mailboxInteractionSource.getDebug()) {
        session.setDebug(true);
      }

      Store store = session.getStore("imaps");

      store.connect();

      return store;
    } catch (Throwable e) {
      throw new RuntimeException("Failed to retrieved the Microsoft 365 IMAP store", e);
    }
  }

  private Store getStandardImapStore(
      MailboxInteractionSource mailboxInteractionSource, boolean isSecure) {
    try {
      Properties properties = new Properties();

      if (isSecure) {
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", mailboxInteractionSource.getHost());
        properties.put("mail.imaps.port", String.valueOf(mailboxInteractionSource.getPort()));
        properties.put("mail.imaps.ssl.enable", "true");
        properties.put("mail.imaps.starttls.enable", "true");
        // TODO: FIX THIS -- MARCUS
        properties.put("mail.imaps.ssl.checkserveridentity", "false");
        properties.put("mail.imaps.ssl.trust", "*");
      } else {
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", mailboxInteractionSource.getHost());
        properties.put("mail.imap.port", String.valueOf(mailboxInteractionSource.getPort()));
      }

      // Create session and store
      Session session = Session.getInstance(properties);
      Store store = session.getStore(isSecure ? "imaps" : "imap");

      store.connect(
          mailboxInteractionSource.getHost(),
          mailboxInteractionSource.getPort(),
          mailboxInteractionSource.getPrincipal(),
          mailboxInteractionSource.getCredential());

      return store;
    } catch (Throwable e) {
      throw new RuntimeException("Failed to retrieved the standard IMAP store", e);
    }
  }

  private void validateInteraction(Interaction interaction) throws InvalidArgumentException {
    if (interaction == null) {
      throw new InvalidArgumentException("interaction");
    }

    Set<ConstraintViolation<Interaction>> constraintViolations = validator.validate(interaction);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "interaction", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateInteractionAttachment(InteractionAttachment interactionAttachment)
      throws InvalidArgumentException {
    if (interactionAttachment == null) {
      throw new InvalidArgumentException("interactionAttachment");
    }

    Set<ConstraintViolation<InteractionAttachment>> constraintViolations =
        validator.validate(interactionAttachment);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "interactionAttachment", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateMailboxInteractionSource(MailboxInteractionSource mailboxInteractionSource)
      throws InvalidArgumentException {
    if (mailboxInteractionSource == null) {
      throw new InvalidArgumentException("mailboxInteractionSource");
    }

    Set<ConstraintViolation<MailboxInteractionSource>> constraintViolations =
        validator.validate(mailboxInteractionSource);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "mailboxInteractionSource", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateWhatsAppInteractionSource(
      WhatsAppInteractionSource whatsAppInteractionSource) throws InvalidArgumentException {
    if (whatsAppInteractionSource == null) {
      throw new InvalidArgumentException("whatsAppInteractionSource");
    }

    Set<ConstraintViolation<WhatsAppInteractionSource>> constraintViolations =
        validator.validate(whatsAppInteractionSource);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "whatsAppInteractionSource", ValidationError.toValidationErrors(constraintViolations));
    }
  }
}

/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.messaging;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.Base64Util;
import digital.inception.core.util.CryptoUtil;
import digital.inception.core.util.ServiceUtil;
import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import digital.inception.messaging.handler.IMessageHandler;
import digital.inception.messaging.handler.MessageHandlerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;

import java.lang.reflect.Constructor;

import java.net.URL;

import java.security.MessageDigest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.*;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The <code>MessagingService</code> class provides the implementation of the Messaging Service
 * for the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class MessagingService
  implements IMessagingService, InitializingBean
{
  /**
   * The path to the messaging configuration files (META-INF/MessagingConfig.xml) on the
   * classpath.
   */
  private static final String MESSAGING_CONFIGURATION_PATH = "META-INF/MessagingConfig.xml";

  /**
   * The AES encryption IV used when generating user-device AES encryption keys.
   */
  private static final byte[] AES_USER_DEVICE_ENCRYPTION_KEY_GENERATION_ENCRYPTION_IV =
      Base64Util.decode("QSaz5pMnMbar66FsNdI/ZQ==");

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

  /**
   * The maximum number of messages to download at one time.
   */
  private static final int NUMBER_OF_MESSAGES_TO_DOWNLOAD = 3;

  /**
   * The maximum number of message parts to download at one time.
   */
  private static final int NUMBER_OF_MESSAGE_PARTS_TO_DOWNLOAD = 3;

  /* The name of the Messaging Service instance. */
  private String instanceName = ServiceUtil.getServiceInstanceName("MessagingService");

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The Archived Message Repository.
   */
  private ArchivedMessageRepository archivedMessageRepository;

  /**
   * The base64 encoded AES encryption master key used to derive the device/user encryption keys.
   */
  @Value("${application.messaging.encryptionKey:#{null}}")
  private String encryptionKeyBase64;

  /**
   * The AES encryption master key used to derive the device/user encryption keys.
   */
  private byte[] encryptionMasterKey;

  /* Entity Manager */
  @PersistenceContext(unitName = "applicationPersistenceUnit")
  private EntityManager entityManager;

  /**
   * The maximum number of times processing will be attempted for a message.
   */
  @Value("${application.messaging.maximumProcessingAttempts:#{1000}}")
  private int maximumProcessingAttempts;

  /**
   * The internal reference to the Messaging Service for transaction management.
   */
  @Resource
  private IMessagingService self;

  /**
   *  The message handlers.
   */
  private Map<UUID, IMessageHandler> messageHandlers;

  /**
   * The configuration information for the message handlers read from the messaging configuration
   * files (META-INF/MessagingConfig.xml) on the classpath.
   */
  private List<MessageHandlerConfig> messageHandlersConfig;

  /**
   * The Message Part Repository.
   */
  private MessagePartRepository messagePartRepository;

  /**
   * The Message Repository.
   */
  private MessageRepository messageRepository;

  /**
   * The delay in milliseconds to wait before re-attempting to process a message.
   */
  @Value("${application.messaging.processingRetryDelay:#{60000}}")
  private int processingRetryDelay;

  /**
   * Constructs a new <code>MessagingService</code>.
   *
   * @param applicationContext        the Spring application context
   * @param messageRepository         the Message Repository
   * @param messagePartRepository     the Message Part Repository
   * @param archivedMessageRepository the Archived Message Repository
   */
  public MessagingService(ApplicationContext applicationContext,
      MessageRepository messageRepository, MessagePartRepository messagePartRepository,
      ArchivedMessageRepository archivedMessageRepository)
  {
    this.applicationContext = applicationContext;
    this.messageRepository = messageRepository;
    this.messagePartRepository = messagePartRepository;
    this.archivedMessageRepository = archivedMessageRepository;
  }

  /**
   * Initialize the Messaging Service.
   */
  @Override
  public void afterPropertiesSet()
  {
    logger.info(String.format("Initializing the Messaging Service (%s)", instanceName));

    messageHandlers = new HashMap<>();

    try
    {
      // Initialize the configuration for the Messaging Service
      initConfiguration();

      // Read the messaging configuration
      readMessagingConfig();

      // Initialize the message handlers
      initMessageHandlers();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialize the Messaging Service", e);
    }
  }

  /**
   * Have all the parts been queued for assembly for the message?
   *
   * @param messageId  the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   message
   * @param totalParts the total number of parts for the message
   *
   * @return <code>true</code> if all the parts for the message have been queued for assembly or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean allPartsQueuedForMessage(UUID messageId, int totalParts)
    throws MessagingServiceException
  {
    try
    {
      return messagePartRepository.getNumberOfPartsForMessage(messageId) == totalParts;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format("Failed to check whether all the message "
          + "parts for the message (%s) have been queued for assembly", messageId), e);
    }
  }

  /**
   * Archive the message.
   *
   * @param message the message to archive
   */
  @Override
  @Transactional
  public void archiveMessage(Message message)
    throws MessagingServiceException
  {
    if (isArchivableMessage(message))
    {
      try
      {
        ArchivedMessage archivedMessage = new ArchivedMessage(message);

        archivedMessageRepository.saveAndFlush(archivedMessage);
      }
      catch (Throwable e)
      {
        throw new MessagingServiceException(String.format("Failed to archive the message (%s)",
            message.getId()), e);
      }
    }
  }

  /**
   * Returns <code>true</code> if the Messaging Service is capable of processing the specified
   * message or <code>false</code> otherwise.
   *
   * @param message the message to process
   *
   * @return <code>true</code> if the Messaging Service is capable of processing the specified
   *         message or <code>false</code> otherwise
   */
  @Override
  public boolean canProcessMessage(Message message)
  {
    return messageHandlers.containsKey(message.getTypeId());
  }

  /**
   * Returns <code>true</code> if the Messaging Service is capable of queueing the specified
   * message part for assembly or <code>false</code> otherwise.
   *
   * @param messagePart the message part to queue for assembly
   *
   * @return <code>true</code> if the Messaging Service is capable of queueing the specified
   *         message part for assembly or <code>false</code> otherwise
   */
  @Override
  public boolean canQueueMessagePartForAssembly(MessagePart messagePart)
  {
    return messageHandlers.containsKey(messagePart.getMessageTypeId());
  }

  /**
   * Create the message.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   */
  @Override
  @Transactional
  public void createMessage(Message message)
    throws MessagingServiceException
  {
    try
    {
      message.setUpdated(LocalDateTime.now());

      messageRepository.saveAndFlush(message);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format("Failed to create the message (%s)",
          message.getId()), e);

    }
  }

  /**
   * Create the message part.
   *
   * @param messagePart the <code>MessagePart</code> instance containing the information for the
   *                    message part
   */
  @Override
  @Transactional
  public void createMessagePart(MessagePart messagePart)
    throws MessagingServiceException
  {
    try
    {
      messagePart.setUpdated(LocalDateTime.now());

      messagePartRepository.saveAndFlush(messagePart);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to add the message part (%s) to the database", messagePart.getId()), e);
    }
  }

  /**
   * Decrypt the message.
   *
   * @param message the message to decrypt
   *
   * @return <code>true</code> if the message data was decrypted successfully or <code>false</code>
   *         otherwise
   */
  @Override
  public boolean decryptMessage(Message message)
    throws MessagingServiceException
  {
    // If the message is already decrypted then stop here
    if (!message.isEncrypted())
    {
      return true;
    }

    // TODO: Cache this key...
    byte[] userEncryptionKey = deriveUserDeviceEncryptionKey(message.getUsername(),
        message.getDeviceId());

    /*
     * if (logger.isDebugEnabled())
     * {
     * logger.debug("Attempting to decrypt the data for the message (" + message.getCodeCategoryId()
     *     + ") using the user's encryption key (" + Base64.encodeBytes(userEncryptionKey) + ")");
     * }
     */

    // Decrypt the message
    try
    {
      // Decrypt the message data
      byte[] decryptedData = MessageTranslator.decryptMessageData(userEncryptionKey,
          StringUtils.isEmpty(message.getEncryptionIV())
          ? new byte[0]
          : Base64Util.decode(message.getEncryptionIV()), message.getData());

      // Verify the data hash for the unencrypted data
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

      messageDigest.update(decryptedData);

      String messageChecksum = Base64Util.encodeBytes(messageDigest.digest());

      if (!messageChecksum.equals(message.getDataHash()))
      {
        logger.warn(String.format(
            "Data hash verification failed for the message (%s) from the user (%s) and device "
            + "(%s). %d (%d) bytes of message data was encrypted using the encryption IV (%s). "
            + "Expected data hash (%s) but got (%s)", message.getId(), message.getUsername(),
            message.getDeviceId(), message.getData().length, decryptedData.length,
            message.getEncryptionIV(), message.getDataHash(), messageChecksum));

        return false;
      }
      else
      {
        message.setData(decryptedData);
        message.setDataHash(null);
        message.setEncryptionIV(null);

        return true;
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to decrypt the data for the message (%s) from the user (%s) and device (%s)",
          message.getId(), message.getUsername(), message.getDeviceId()), e);
    }
  }

  /**
   * Delete the message.
   *
   * @param message the message to delete
   */
  @Override
  @Transactional
  public void deleteMessage(Message message)
    throws MessageNotFoundException, MessagingServiceException
  {
    deleteMessage(message.getId());
  }

  /**
   * Delete the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  @Override
  @Transactional
  public void deleteMessage(UUID messageId)
    throws MessageNotFoundException, MessagingServiceException
  {
    try
    {
      if (!messageRepository.existsById(messageId))
      {
        throw new MessageNotFoundException(messageId);
      }

      messageRepository.deleteById(messageId);
    }
    catch (MessageNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format("Failed to delete the message (%s)",
          messageId), e);
    }
  }

  /**
   * Delete the message part.
   *
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part
   */
  @Override
  @Transactional
  public void deleteMessagePart(UUID messagePartId)
    throws MessagePartNotFoundException, MessagingServiceException
  {
    try
    {
      if (!messagePartRepository.existsById(messagePartId))
      {
        throw new MessagePartNotFoundException(messagePartId);
      }

      messagePartRepository.deleteById(messagePartId);
    }
    catch (MessagePartNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format("Failed to delete the message part (%s)",
          messagePartId), e);
    }
  }

  /**
   * Delete the message parts for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  @Override
  @Transactional
  public void deleteMessagePartsForMessage(UUID messageId)
    throws MessagingServiceException
  {
    try
    {
      messagePartRepository.deleteMessagePartsForMessage(messageId);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to delete the message parts for the message (%s)", messageId), e);
    }
  }

  /**
   * Derive the user-device encryption key.
   *
   * @param username the username uniquely identifying the user e.g. test1
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *
   * @return the user-device encryption key
   */
  @Override
  public byte[] deriveUserDeviceEncryptionKey(String username, UUID deviceId)
    throws MessagingServiceException
  {
    try
    {
      String password = deviceId.toString() + username.toLowerCase();

      byte[] key = CryptoUtil.passwordToAESKey(password);

      SecretKey secretKey = new SecretKeySpec(encryptionMasterKey, CryptoUtil.AES_KEY_SPEC);
      IvParameterSpec iv = new IvParameterSpec(
          AES_USER_DEVICE_ENCRYPTION_KEY_GENERATION_ENCRYPTION_IV);
      Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");

      cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

      return cipher.doFinal(key);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to derive the encryption key for the user (%s) and device (%s)", username,
          deviceId), e);
    }
  }

  /**
   * Encrypt the message.
   *
   * @param message the message to encrypt
   *
   * @return <code>true</code> if the message data was encrypted successfully or <code>false</code>
   *         otherwise
   */
  @Override
  public boolean encryptMessage(Message message)
    throws MessagingServiceException
  {
    // If the message is already encrypted then stop here
    if (message.isEncrypted())
    {
      return true;
    }

    // TODO: Cache this key...
    byte[] userEncryptionKey = deriveUserDeviceEncryptionKey(message.getUsername(),
        message.getDeviceId());

    /*
     * if (logger.isDebugEnabled())
     * {
     * logger.debug("Attempting to encrypt the data for the message (" + message.getCodeCategoryId()
     *     + ") using the user's encryption key (" + Base64.encodeBytes(userEncryptionKey) + ")");
     * }
     */

    // Encrypt the message
    try
    {
      byte[] encryptionIV = CryptoUtil.createRandomEncryptionIV(CryptoUtil.AES_BLOCK_SIZE);

      // Encrypt the message data
      byte[] encryptedData = MessageTranslator.encryptMessageData(userEncryptionKey, encryptionIV,
          message.getData());

      // Generate the hash for the unencrypted data
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

      messageDigest.update(message.getData());

      String messageChecksum = Base64Util.encodeBytes(messageDigest.digest());

      message.setDataHash(messageChecksum);
      message.setData(encryptedData);
      message.setEncryptionIV((encryptionIV.length == 0)
          ? ""
          : Base64Util.encodeBytes(encryptionIV));

      return true;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to encrypt the data for the message (%s) from the user (%s) and device (%s)",
          message.getId(), message.getUsername(), message.getDeviceId()), e);
    }
  }

  /**
   * Returns the maximum number of times processing will be attempted for a message.
   *
   * @return the maximum number of times processing will be attempted for a message
   */
  public int getMaximumProcessingAttempts()
  {
    return maximumProcessingAttempts;
  }

  /**
   * Retrieve the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   *
   * @return the message
   */
  @Override
  public Message getMessage(UUID messageId)
    throws MessageNotFoundException, MessagingServiceException
  {
    try
    {
      Optional<Message> message = messageRepository.findById(messageId);

      if (message.isPresent())
      {
        return message.get();
      }
      else
      {
        throw new MessageNotFoundException(messageId);
      }
    }
    catch (MessageNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format("Failed to retrieve the message (%s)",
          messageId), e);
    }
  }

  /**
   * Retrieve the message parts queued for assembly for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   * @param lockName  the name of the lock that should be applied to the message parts queued for
   *                  assembly when they are retrieved
   *
   * @return the message parts queued for assembly for the message
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<MessagePart> getMessagePartsQueuedForAssembly(UUID messageId, String lockName)
    throws MessagingServiceException
  {
    try
    {
      List<MessagePart> messageParts =
          messagePartRepository.findMessagePartsQueuedForAssemblyForMessageForWrite(messageId);

      for (MessagePart messagePart : messageParts)
      {
        LocalDateTime when = LocalDateTime.now();

        messagePartRepository.lockMessagePartForAssembly(messagePart.getId(), instanceName, when);

        entityManager.detach(messagePart);

        messagePart.setStatus(MessagePartStatus.ASSEMBLING);
        messagePart.setLockName(lockName);
        messagePart.setUpdated(when);
      }

      return messageParts;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to retrieve the message parts that have been queued for assembly for the message (%s)",
          messageId), e);
    }
  }

  /**
   * Get the message parts for a user that have been queued for download by a particular remote
   * device.
   *
   * @param username the username identifying the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *
   * @return the message parts that have been queued for download by a particular remote device
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @SuppressWarnings("resource")
  public List<MessagePart> getMessagePartsQueuedForDownload(String username, UUID deviceId)
    throws MessagingServiceException
  {
    try
    {
      PageRequest pageRequest = PageRequest.of(0, NUMBER_OF_MESSAGE_PARTS_TO_DOWNLOAD);

      /*
       * First check if we already have message parts locked for downloading for this device, if
       * so update the lock and return these message parts. This handles the situation where a
       * device attempted to download message parts previously and failed leaving these message
       * parts locked in a "Downloading" state.
       */
      List<MessagePart> messageParts =
          messagePartRepository.findMessagePartsWithStatusForUserAndDeviceForWrite(MessagePartStatus
          .DOWNLOADING, username, deviceId, pageRequest);

      if (messageParts.size() == 0)
      {
        messageParts = messagePartRepository.findMessagePartsWithStatusForUserAndDeviceForWrite(
            MessagePartStatus.QUEUED_FOR_DOWNLOAD, username, deviceId, pageRequest);
      }

//    messageParts = messagePartRepository.findAll();
//
//    for (MessagePart messagePart : messageParts)
//    {
//      System.out.println(messagePart.toString());
//    }

      for (MessagePart messagePart : messageParts)
      {
        LocalDateTime when = LocalDateTime.now();

        messagePartRepository.lockMessagePartForDownload(messagePart.getId(), instanceName, when);

        entityManager.detach(messagePart);

        messagePart.setStatus(MessagePartStatus.DOWNLOADING);
        messagePart.setLockName(instanceName);
        messagePart.setUpdated(when);
        messagePart.incrementDownloadAttempts();
      }

      return messageParts;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to retrieve the message parts for the user (%s) that have been queued for "
          + "download by the device (%s)", username, deviceId), e);
    }
  }

  /**
   * Get the messages for a user that have been queued for download by a particular remote device.
   *
   * @param username the username identifying the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *
   * @return the messages for a user that have been queued for download by a particular remote
   *         device
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @SuppressWarnings("resource")
  public List<Message> getMessagesQueuedForDownload(String username, UUID deviceId)
    throws MessagingServiceException
  {
    try
    {
      PageRequest pageRequest = PageRequest.of(0, NUMBER_OF_MESSAGES_TO_DOWNLOAD);

      /*
       * First check if we already have messages locked for downloading for the user-device
       * combination, if so update the lock and return these messages. This handles the situation
       * where a device attempted to download messages previously and failed leaving these
       * messages locked in a "Downloading" state.
       */
      List<Message> messages = messageRepository.findMessagesWithStatusForUserAndDeviceForWrite(
          MessageStatus.DOWNLOADING, username, deviceId, pageRequest);

      if (messages.size() == 0)
      {
        /*
         * If we did not find messages already locked for downloading then retrieve the messages
         * that are "QueuedForDownload" for the user-device combination.
         */
        messages = messageRepository.findMessagesWithStatusForUserAndDeviceForWrite(MessageStatus
            .QUEUED_FOR_DOWNLOAD, username, deviceId, pageRequest);
      }

      /*
       * Ensure each message is locked correctly with the status "Downloading" and increment the
       * download attempts.
       */
      for (Message message : messages)
      {
        if (!StringUtils.isEmpty(message.getLockName()))
        {
          if (!message.getLockName().equals(instanceName))
          {
            if (logger.isDebugEnabled())
            {
              logger.debug(String.format(
                  "The message (%s) that was originally locked for download using the lock "
                  + "name (%s) will now be locked for download using the lock name (%s)",
                  message.getId(), message.getLockName(), instanceName));
            }
          }
        }

        LocalDateTime when = LocalDateTime.now();

        messageRepository.lockMessageForDownload(message.getId(), instanceName, when);

        entityManager.detach(message);

        message.incrementDownloadAttempts();
        message.setLockName(instanceName);
        message.setStatus(MessageStatus.DOWNLOADING);
        message.setUpdated(when);
      }

      return messages;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to retrieve the messages for the user (%s) that have been queued for download by "
          + "the device (%s)", username, deviceId), e);
    }
  }

  /**
   * Retrieve the next message that has been queued for processing.
   * <p/>
   * The message will be locked to prevent duplicate processing.
   *
   * @return the next message that has been queued for processing or <code>null</code> if no
   *         messages are currently queued for processing
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Message getNextMessageQueuedForProcessing()
    throws MessagingServiceException
  {
    try
    {
      LocalDateTime processedBefore = LocalDateTime.now();

      processedBefore = processedBefore.minus(processingRetryDelay, ChronoUnit.MILLIS);

      PageRequest pageRequest = PageRequest.of(0, 1);

      List<Message> messages = messageRepository.findMessagesQueuedForProcessingForWrite(
          processedBefore, pageRequest);

      if (messages.size() > 0)
      {
        Message message = messages.get(0);

        LocalDateTime when = LocalDateTime.now();

        messageRepository.lockMessageForProcessing(message.getId(), instanceName, when);

        entityManager.detach(message);

        message.setLockName(instanceName);
        message.setStatus(MessageStatus.PROCESSING);
        message.incrementProcessAttempts();
        message.setLastProcessed(when);
        message.setUpdated(when);

        return message;
      }
      else
      {
        return null;
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(
          "Failed to retrieve the next message that has been queued for processing", e);
    }
  }

  /**
   * Should the specified message be archived?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message should be archived or <code>false</code> otherwise
   */
  @Override
  public boolean isArchivableMessage(Message message)
  {
    return isArchivableMessage(message.getTypeId());
  }

  /**
   * Can the specified message be processed asynchronously?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message can be processed asynchronously or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean isAsynchronousMessage(Message message)
  {
    return isAsynchronousMessage(message.getTypeId());
  }

  /**
   * Has the message already been archived?
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   *
   * @return <code>true</code> if the message has already been archived or <code>false</code>
   *         otherwise
   */
  @Override
  public boolean isMessageArchived(UUID messageId)
    throws MessagingServiceException
  {
    try
    {
      return archivedMessageRepository.existsById(messageId);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to check whether the message (%s) is archived", messageId), e);
    }
  }

  /**
   * Has the message part already been queued for assembly?
   *
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part
   *
   * @return <code>true</code> if the message part has already been queued for assembly or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean isMessagePartQueuedForAssembly(UUID messagePartId)
    throws MessagingServiceException
  {
    try
    {
      return messagePartRepository.existsByIdAndStatus(messagePartId, MessagePartStatus
          .QUEUED_FOR_ASSEMBLY);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to check whether the message part (%s) is queued for assembly", messagePartId),
          e);
    }
  }

  /**
   * Should the specified message be be processed securely?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message is secure or <code>false</code> otherwise
   */
  @Override
  public boolean isSecureMessage(Message message)
  {
    return isSecureMessage(message.getTypeId());
  }

  /**
   * Should the specified message be processed synchronously?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message should be processed synchronously or
   *         <code>false</code> otherwise
   */
  public boolean isSynchronousMessage(Message message)
  {
    return isSynchronousMessage(message.getTypeId());
  }

  /**
   * Process the message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   */
  @Override
  public Message processMessage(Message message)
    throws MessagingServiceException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug(String.format("Processing message (%s) with type (%s)", message.getId(),
          message.getTypeId()));
    }

    if (!messageHandlers.containsKey(message.getTypeId()))
    {
      throw new MessagingServiceException(String.format(
          "No message handler registered to process messages with type (%s)", message.getTypeId()));
    }

    IMessageHandler messageHandler = messageHandlers.get(message.getTypeId());

    try
    {
      return messageHandler.processMessage(message);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to process the message (%s) with type (%s)", message.getId(),
          message.getTypeId()), e);
    }
  }

  /**
   * Queue the specified message for download by a remote device.
   *
   * @param message the message to queue
   */
  @Override
  @Transactional
  public void queueMessageForDownload(Message message)
    throws MessagingServiceException
  {
    try
    {
      if (message.getData().length <= Message.MAX_ASYNC_MESSAGE_SIZE)
      {
        // Update the status of the message to indicate that it is queued for sending
        message.setStatus(MessageStatus.QUEUED_FOR_DOWNLOAD);

        createMessage(message);
      }
      else
      {
        // Calculate the hash for the message data to use as the message checksum
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        messageDigest.update(message.getData());

        String messageChecksum = Base64Util.encodeBytes(messageDigest.digest());

        // Split the message up into a number of message parts and persist each message part
        int numberOfParts = message.getData().length / MessagePart.MAX_MESSAGE_PART_SIZE;

        if ((message.getData().length % MessagePart.MAX_MESSAGE_PART_SIZE) > 0)
        {
          numberOfParts++;
        }

        for (int i = 0; i < numberOfParts; i++)
        {
          byte[] messagePartData;

          // If this is not the last message part
          if (i < (numberOfParts - 1))
          {
            messagePartData = new byte[MessagePart.MAX_MESSAGE_PART_SIZE];

            System.arraycopy(message.getData(), (i * MessagePart.MAX_MESSAGE_PART_SIZE),
                messagePartData, 0, MessagePart.MAX_MESSAGE_PART_SIZE);
          }

          // If this is the last message part
          else
          {
            int sizeOfPart = message.getData().length - (i * MessagePart.MAX_MESSAGE_PART_SIZE);

            messagePartData = new byte[sizeOfPart];

            System.arraycopy(message.getData(), (i * MessagePart.MAX_MESSAGE_PART_SIZE),
                messagePartData, 0, sizeOfPart);
          }

          MessagePart messagePart = new MessagePart(i + 1, numberOfParts, message.getId(),
              message.getUsername(), message.getDeviceId(), message.getTypeId(),
              message.getCorrelationId(), message.getPriority(), message.getCreated(),
              message.getDataHash(), message.getEncryptionIV(), messageChecksum, messagePartData);

          messagePart.setStatus(MessagePartStatus.QUEUED_FOR_DOWNLOAD);

          // Persist the message part in the database
          createMessagePart(messagePart);
        }

        logger.debug(String.format("Queued %d message parts for download for the message (%s)",
            numberOfParts, message.getId()));
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to queue the message (%s) for download", message.getId()), e);
    }

    // Archive the message
    archiveMessage(message);
  }




  /**
   * Queue the specified message for processing and process the message using the Background
   * Message Processor.
   *
   * @param message the message to queue
   */
  @Override
  @Transactional
  public void queueMessageForProcessingAndProcess(Message message)
    throws MessagingServiceException
  {
    /*
     * Queue the message for processing in a new transaction so it is available to the
     * Background Message Processor, which will be triggered asynchronously in a different thread.
     */
    self.queueMessageForProcessing(message);

    // Trigger the Background Message Processor to process the message that was queued
    try
    {
      applicationContext.getBean(BackgroundMessageProcessor.class).processMessages();
    }
    catch (Throwable e)
    {
      logger.error("Failed to trigger the Background Message Processor", e);
    }
  }


  /**
   * Queue the specified message for processing.
   *
   * @param message the message to queue
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void queueMessageForProcessing(Message message)
    throws MessagingServiceException
  {
    // Update the status of the message to indicate that it is queued for processing
    message.setStatus(MessageStatus.QUEUED_FOR_PROCESSING);

    try
    {
      // Create the message
      createMessage(message);

      // Archive the message
      archiveMessage(message);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to queue the message (%s) for processing", message.getId()), e);
    }

    if (logger.isDebugEnabled())
    {
      logger.debug(String.format("Queued message (%s) with type (%s) for processing",
          message.getId(), message.getTypeId()));

      logger.debug(message.toString());
    }
  }

  /**
   * Queue the specified message part for assembly.
   *
   * @param messagePart the message part to queue
   */
  @Override
  @Transactional
  public void queueMessagePartForAssembly(MessagePart messagePart)
    throws MessagingServiceException
  {
    try
    {
      // Verify that the message has not already been queued for processing
      if (isMessageArchived(messagePart.getMessageId()))
      {
        logger.debug(String.format(
            "The message (%s) has already been queued for processing so the message part (%s) will be ignored",
            messagePart.getMessageId(), messagePart.getId()));

        return;
      }

      // Check that we have not already received and queued this message part for assembly
      if (!isMessagePartQueuedForAssembly(messagePart.getId()))
      {
        // Update the status of the message part to indicate that it is queued for assembly
        messagePart.setStatus(MessagePartStatus.QUEUED_FOR_ASSEMBLY);

        createMessagePart(messagePart);
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to queue the message part (%s) for assembly", messagePart.getId()), e);
    }

    // TODO: MOVE TO BACKGROUND THREAD RATHER TO PREVENT MOBILE DEVICE WAITING FOR THIS - MARCUS

    /*
     * Check whether all the parts for the message have been queued for assembly and if so
     * assemble the message.
     */
    try
    {
      // Check whether all the parts for the message have been queued for assembly
      if (allPartsQueuedForMessage(messagePart.getMessageId(), messagePart.getTotalParts()))
      {
        // Retrieve the message parts queued for assembly
        List<MessagePart> messageParts = getMessagePartsQueuedForAssembly(
            messagePart.getMessageId(), instanceName);

        // Assemble the message from its constituent parts
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (MessagePart tmpMessagePart : messageParts)
        {
          baos.write(tmpMessagePart.getData());
        }

        byte[] reconstructedData = baos.toByteArray();

        // Check that the reconstructed message data is valid
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        messageDigest.update(reconstructedData);

        String messageChecksum = Base64Util.encodeBytes(messageDigest.digest());

        if (!messageChecksum.equals(messagePart.getMessageChecksum()))
        {
          // Delete the message parts
          deleteMessagePartsForMessage(messagePart.getMessageId());

          logger.error(String.format(
              "Failed to verify the checksum for the reconstructed message (%s) with type (%s) "
              + "from the user (%s) and device (%s). Found %d bytes of message data with the hash "
              + "(%s) that was reconstructed from %d message parts. The message will NOT be processed",
              messagePart.getMessageId(), messagePart.getMessageTypeId(),
              messagePart.getMessageUsername(), messagePart.getMessageDeviceId(), reconstructedData
              .length, messageChecksum, messageParts.size()));

          return;
        }

        Message message = new Message(messagePart.getMessageId(), messagePart.getMessageUsername(),
            messagePart.getMessageDeviceId(), messagePart.getMessageTypeId(),
            messagePart.getMessageCorrelationId(), messagePart.getMessagePriority(),
            messagePart.getMessageCreated(), reconstructedData, messagePart.getMessageDataHash(),
            messagePart.getMessageEncryptionIV());

        // Queue the message for processing
        queueMessageForProcessingAndProcess(message);

        // Delete the message parts
        deleteMessagePartsForMessage(messagePart.getMessageId());
      }
    }
    catch (Exception e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to assemble the parts for the message (%s)", messagePart.getMessageId()), e);
    }
  }

  /**
   * Reset the expired message locks.
   *
   * @param status      the current status of the messages that have been locked
   * @param newStatus   the new status for the messages that have been unlocked
   * @param lockTimeout the lock timeout in seconds
   */
  @Override
  @Transactional
  public void resetExpiredMessageLocks(MessageStatus status, MessageStatus newStatus,
      int lockTimeout)
    throws MessagingServiceException
  {
    try
    {
      LocalDateTime lockExpiry = LocalDateTime.now();
      lockExpiry = lockExpiry.minus(lockTimeout, ChronoUnit.SECONDS);

      messageRepository.resetStatusAndLocksForMessagesWithStatusAndExpiredLocks(status, newStatus,
          lockExpiry);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to reset the expired locks for the messages with the status (%s)", status), e);
    }
  }

  /**
   * Reset the expired message part locks.
   *
   * @param status      the current status of the message parts that have been locked
   * @param newStatus   the new status for the message parts that have been unlocked
   * @param lockTimeout the lock timeout in seconds
   */
  @Override
  @Transactional
  public void resetExpiredMessagePartLocks(MessagePartStatus status, MessagePartStatus newStatus,
      int lockTimeout)
    throws MessagingServiceException
  {
    try
    {
      LocalDateTime lockExpiry = LocalDateTime.now();
      lockExpiry = lockExpiry.minus(lockTimeout, ChronoUnit.SECONDS);

      messagePartRepository.resetStatusAndLocksForMessagePartsWithStatusAndExpiredLocks(status,
          newStatus, lockExpiry);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to reset the expired locks for the message parts with the status (%s)", status),
          e);
    }
  }

  /**
   * Reset the locks for the messages.
   *
   * @param status    the current status of the messages that have been locked
   * @param newStatus the new status for the messages that have been unlocked
   */
  @Override
  @Transactional
  public void resetMessageLocks(MessageStatus status, MessageStatus newStatus)
    throws MessagingServiceException
  {
    try
    {
      messageRepository.resetStatusAndLocksForMessagesWithStatusAndLock(status, newStatus,
          instanceName);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to reset the locks for the messages with the status (%s) that have been locked "
          + "using the lock name (%s)", status, instanceName), e);
    }
  }

  /**
   * Reset the locks for the message parts.
   *
   * @param status    the current status of the message parts that have been locked
   * @param newStatus the new status for the message parts that have been unlocked
   */
  @Override
  @Transactional
  public void resetMessagePartLocks(MessagePartStatus status, MessagePartStatus newStatus)
    throws MessagingServiceException
  {
    try
    {
      messagePartRepository.resetStatusAndLocksForMessagesWithStatusAndLock(status, newStatus,
          instanceName);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to reset the locks for the message parts with the status (%s) that have been "
          + "locked using the lock name (%s)", status, instanceName), e);
    }
  }

  /**
   * Set the status for a message part.
   *
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part
   * @param status        the new status
   */
  @Override
  @Transactional
  public void setMessagePartStatus(UUID messagePartId, MessagePartStatus status)
    throws MessagingServiceException
  {
    try
    {
      messagePartRepository.setMessagePartStatus(messagePartId, status);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to set the status for the message part (%s) to (%s)", messagePartId,
          status.toString()), e);
    }
  }

  /**
   * Set the status for a message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   * @param status    the new status
   */
  @Override
  @Transactional
  public void setMessageStatus(UUID messageId, MessageStatus status)
    throws MessagingServiceException
  {
    try
    {
      messageRepository.setMessageStatus(messageId, status);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to set the status for the message (%s) to (%s)", messageId, status.toString()),
          e);
    }
  }

  /**
   * Unlock the message.
   *
   * @param message the message to unlock
   * @param status  the new status for the unlocked message
   */
  @Override
  @Transactional
  public void unlockMessage(Message message, MessageStatus status)
    throws MessagingServiceException
  {
    try
    {
      LocalDateTime when = LocalDateTime.now();

      messageRepository.unlockMessage(message.getId(), status, when);

      message.setStatus(status);
      message.setLockName(null);
      message.setUpdated(when);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to unlock and set the status for the message (%s) to (%s)", message.getId(),
          status.toString()), e);
    }
  }

  /**
   * Unlock a locked message part.
   *
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part
   * @param status        the new status for the unlocked message part
   */
  @Override
  @Transactional
  public void unlockMessagePart(UUID messagePartId, MessagePartStatus status)
    throws MessagingServiceException
  {
    try
    {
      LocalDateTime when = LocalDateTime.now();

      messagePartRepository.unlockMessagePart(messagePartId, status, when);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to unlock and set the status for the message part (%s) to (%s)", messagePartId,
          status.toString()), e);
    }
  }

  /**
   * Initialize the configuration for the Messaging Service.
   */
  private void initConfiguration()
    throws MessagingServiceException
  {
    try
    {
      if (StringUtils.isEmpty(encryptionKeyBase64))
      {
        throw new MessagingServiceException(
            "No application.messaging.encryptionKey configuration value found");
      }
      else
      {
        encryptionMasterKey = Base64Util.decode(encryptionKeyBase64);
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(
          "Failed to initialize the configuration for the Messaging Service", e);
    }
  }

  /**
   * Initialize the message handlers.
   */
  private void initMessageHandlers()
  {
    // Initialize each message handler
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      try
      {
        logger.info(String.format("Initializing the message handler (%s) with class (%s)",
            messageHandlerConfig.getName(), messageHandlerConfig.getClassName()));

        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(
            messageHandlerConfig.getClassName());

        Constructor<?> constructor = clazz.getConstructor(MessageHandlerConfig.class);

        if (constructor != null)
        {
          // Create an instance of the message handler
          IMessageHandler messageHandler = (IMessageHandler) constructor.newInstance(
              messageHandlerConfig);

          // Perform dependency injection on the message handler
          applicationContext.getAutowireCapableBeanFactory().autowireBean(messageHandler);

          List<MessageHandlerConfig.MessageConfig> messagesConfig =
              messageHandlerConfig.getMessagesConfig();

          for (MessageHandlerConfig.MessageConfig messageConfig : messagesConfig)
          {
            if (messageHandlers.containsKey(messageConfig.getMessageTypeId()))
            {
              IMessageHandler existingMessageHandler = messageHandlers.get(
                  messageConfig.getMessageTypeId());

              logger.warn(String.format(
                  "Failed to register the message handler (%s) for the message type (%s) since "
                  + "another message handler (%s) has already been registered to process messages "
                  + "of this type", messageHandler.getClass().getName(),
                  messageConfig.getMessageTypeId(), existingMessageHandler.getClass().getName()));
            }
            else
            {
              messageHandlers.put(messageConfig.getMessageTypeId(), messageHandler);
            }
          }
        }
        else
        {
          logger.error(String.format(
              "Failed to register the message handler (%s) since the message handler class does "
              + "not provide a constructor with the required signature",
              messageHandlerConfig.getClassName()));
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to initialize the message handler (%s) with class (%s)",
            messageHandlerConfig.getName(), messageHandlerConfig.getClassName()), e);
      }
    }
  }

  /**
   * Should a message with the specified type be archived?
   *
   * @param typeId the Universally Unique Identifier (UUID) used to uniquely identify the message
   *               type
   *
   * @return <code>true</code> if a message with the specified type should be archived or
   *         <code>false</code> otherwise
   */
  private boolean isArchivableMessage(UUID typeId)
  {
    // TODO: Add caching of this check

    // Check if any of the configured handlers supports archiving of the message
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      if (messageHandlerConfig.isArchivable(typeId))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Can a message with the specified type be processed asynchronously?
   *
   * @param typeId the Universally Unique Identifier (UUID) used to uniquely identify the message
   *               type
   *
   * @return <code>true</code> if a message with the specified type can be processed asynchronously
   *         or <code>false</code> otherwise
   */
  private boolean isAsynchronousMessage(UUID typeId)
  {
    // TODO: Add caching of this check

    // Check if any of the configured handlers support the synchronous processing of this message
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      if (messageHandlerConfig.supportsAsynchronousProcessing(typeId))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Should a message with the specified type be processed securely?
   *
   * @param typeId the Universally Unique Identifier (UUID) used to uniquely identify the message
   *               type
   *
   * @return <code>true</code> if a message with the specified type should be processed securely or
   *         <code>false</code> otherwise
   */
  private boolean isSecureMessage(UUID typeId)
  {
    // TODO: Add caching of this check

    // Check if any of the configured handlers required secure processing of the message type
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      if (messageHandlerConfig.isSecure(typeId))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Can a message with the specified type be processed synchronously?
   *
   * @param typeId the Universally Unique Identifier (UUID) used to uniquely identify the message
   *               type
   *
   * @return <code>true</code> if a message with the specified type can be processed synchronously
   *         or <code>false</code> otherwise
   */
  private boolean isSynchronousMessage(UUID typeId)
  {
    // TODO: Add caching of this check

    // Check if any of the configured handlers support the synchronous processing of this message
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      if (messageHandlerConfig.supportsSynchronousProcessing(typeId))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Read the messaging configuration from all the <i>META-INF/MessagingConfig.xml</i>
   * configuration files that can be found on the classpath.
   */
  private void readMessagingConfig()
    throws MessagingServiceException
  {
    try
    {
      messageHandlersConfig = new ArrayList<>();

      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      // Load the messaging configuration files from the classpath
      Enumeration<URL> configurationFiles = classLoader.getResources(MESSAGING_CONFIGURATION_PATH);

      while (configurationFiles.hasMoreElements())
      {
        URL configurationFile = configurationFiles.nextElement();

        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Reading the messaging configuration file (%s)",
              configurationFile.toURI().toString()));
        }

        // Retrieve a document builder instance using the factory
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setValidating(true);

        // builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        builder.setEntityResolver(new DtdJarResolver("MessagingConfig.dtd",
            "META-INF/MessagingConfig.dtd"));
        builder.setErrorHandler(new XmlParserErrorHandler());

        // Parse the XML messaging configuration file using the document builder
        InputSource inputSource = new InputSource(configurationFile.openStream());
        Document document = builder.parse(inputSource);
        Element rootElement = document.getDocumentElement();

        List<Element> messageHandlerElements = XmlUtil.getChildElements(rootElement,
            "messageHandler");

        for (Element messageHandlerElement : messageHandlerElements)
        {
          // Read the handler configuration
          String name = XmlUtil.getChildElementText(messageHandlerElement, "name");
          String className = XmlUtil.getChildElementText(messageHandlerElement, "class");

          MessageHandlerConfig messageHandlerConfig = new MessageHandlerConfig(name, className);

          // Get the "Messages" element
          Element messagesElement = XmlUtil.getChildElement(messageHandlerElement, "messages");

          // Read the message configuration for the handler
          if (messagesElement != null)
          {
            List<Element> messageElements = XmlUtil.getChildElements(messagesElement, "message");

            for (Element messageElement : messageElements)
            {
              UUID messageType = UUID.fromString(messageElement.getAttribute("type"));
              boolean isSynchronous = messageElement.getAttribute("isSynchronous").equalsIgnoreCase(
                  "Y");
              boolean isAsynchronous = messageElement.getAttribute("isAsynchronous")
                  .equalsIgnoreCase("Y");
              boolean isSecure = messageElement.getAttribute("isSecure").equalsIgnoreCase("Y");
              boolean isArchivable = messageElement.getAttribute("isArchivable").equalsIgnoreCase(
                  "Y");

              messageHandlerConfig.addMessageConfig(messageType, isSynchronous, isAsynchronous,
                  isSecure, isArchivable);
            }
          }

          messageHandlersConfig.add(messageHandlerConfig);
        }
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException("Failed to read the messaging configuration", e);
    }
  }
}

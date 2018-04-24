/*
 * Copyright 2018 Marcus Portmann
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
import digital.inception.core.util.StringUtil;
import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import digital.inception.messaging.handler.IMessageHandler;
import digital.inception.messaging.handler.MessageHandlerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

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

  /* The name of the Messaging Service instance. */
  private String instanceName = ServiceUtil.getServiceInstanceName("MessagingService");

  /**
   * The base64 encoded AES encryption master key used to derive the device/user encryption keys.
   */
  @Value("${application.messaging.encryptionKey:#{null}}")
  private String encryptionKeyBase64;

  /**
   * The AES encryption master key used to derive the device/user encryption keys.
   */
  private byte[] encryptionMasterKey;

  /**
   * The delay in milliseconds to wait before re-attempting to process a message.
   */
  @Value("${application.messaging.processingRetryDelay:#{60000}}")
  private int processingRetryDelay;

  /**
   * The maximum number of times processing will be attempted for a message.
   */
  @Value("${application.messaging.maximumProcessingAttempts:#{1000}}")
  private int maximumProcessingAttempts;

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
   * The Spring application context.
   */
  @Autowired
  private ApplicationContext applicationContext;

  /**
   * The data source used to provide connections to the database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Initialize the Messaging Service.
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet()
    throws Exception
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
    String allPartsQueuedForMessageSQL = "SELECT COUNT(MP.ID) FROM MESSAGING.MESSAGE_PARTS MP "
        + "WHERE MP.MSG_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(allPartsQueuedForMessageSQL))
    {
      statement.setObject(1, messageId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next() && (totalParts == rs.getInt(1));
      }
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
  public void archiveMessage(Message message)
    throws MessagingServiceException
  {
    if (isArchivableMessage(message))
    {
      String archiveMessageSQL = "INSERT INTO messaging.archived_messages "
          + "(id, username, device_id, type_id, correlation_id, created, archived, data) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(archiveMessageSQL))
      {
        statement.setObject(1, message.getId());
        statement.setString(2, message.getUsername());
        statement.setObject(3, message.getDeviceId());
        statement.setObject(4, message.getTypeId());
        statement.setObject(5, message.getCorrelationId());
        statement.setTimestamp(6, Timestamp.valueOf(message.getCreated()));
        statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        statement.setBytes(8, message.getData());

        if (statement.executeUpdate() != 1)
        {
          throw new MessagingServiceException(String.format(
              "No rows were affected as a result of executing the SQL statement (%s)",
              archiveMessageSQL));
        }
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
   * Create the error report.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   */
  @Override
  public void createErrorReport(ErrorReport errorReport)
    throws MessagingServiceException
  {
    String createErrorReportSQL = "INSERT INTO messaging.error_reports (id, application_id, "
        + "application_version, description, detail, feedback, created, who, device_id, data) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createErrorReportSQL))
    {
      String description = errorReport.getDescription();

      if (description.length() > 2048)
      {
        description = description.substring(0, 2048);
      }

      String detail = errorReport.getDetail();

      if (detail.length() > 16384)
      {
        detail = detail.substring(0, 16384);
      }

      String feedback = errorReport.getFeedback();

      if (feedback.length() > 4000)
      {
        feedback = feedback.substring(0, 4000);
      }

      statement.setObject(1, errorReport.getId());
      statement.setObject(2, errorReport.getApplicationId());
      statement.setInt(3, errorReport.getApplicationVersion());
      statement.setString(4, description);
      statement.setString(5, detail);
      statement.setString(6, feedback);
      statement.setTimestamp(7, Timestamp.valueOf(errorReport.getCreated()));
      statement.setString(8, errorReport.getWho());
      statement.setObject(9, errorReport.getDeviceId());
      statement.setBytes(10, errorReport.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new MessagingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createErrorReportSQL));
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format("Failed to create the error report (%s)",
          errorReport.getId()), e);
    }
  }

  /**
   * Create the message.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   */
  @Override
  public void createMessage(Message message)
    throws MessagingServiceException
  {
    String createMessageSQL = "INSERT INTO MESSAGING.MESSAGES (id, username, device_id, type_id, "
        + "correlation_id, priority, status, created, persisted, updated, send_attempts, "
        + "process_attempts, download_attempts, data) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createMessageSQL))
    {
      LocalDateTime persisted = LocalDateTime.now();

      statement.setObject(1, message.getId());
      statement.setString(2, message.getUsername());
      statement.setObject(3, message.getDeviceId());
      statement.setObject(4, message.getTypeId());
      statement.setObject(5, message.getCorrelationId());
      statement.setInt(6, message.getPriority().getCode());
      statement.setInt(7, message.getStatus().getCode());
      statement.setTimestamp(8, Timestamp.valueOf(message.getCreated()));
      statement.setTimestamp(9, Timestamp.valueOf(persisted));
      statement.setTimestamp(10, Timestamp.valueOf(message.getCreated()));
      statement.setInt(11, message.getSendAttempts());
      statement.setInt(12, message.getProcessAttempts());
      statement.setInt(13, message.getDownloadAttempts());
      statement.setBytes(14, message.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new MessagingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createMessageSQL));
      }

      message.setPersisted(persisted);
      message.setUpdated(message.getCreated());
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
  public void createMessagePart(MessagePart messagePart)
    throws MessagingServiceException
  {
    String createMessagePartSQL = "INSERT INTO messaging.message_parts (id, part_no, total_parts, "
        + "send_attempts, download_attempts, status, persisted, msg_id, msg_username, "
        + "msg_device_id, msg_type_id, msg_correlation_id, msg_priority, msg_created, "
        + "msg_data_hash, msg_encryption_iv, msg_checksum, data) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createMessagePartSQL))
    {
      LocalDateTime persisted = LocalDateTime.now();

      statement.setObject(1, messagePart.getId());
      statement.setInt(2, messagePart.getPartNo());
      statement.setInt(3, messagePart.getTotalParts());
      statement.setInt(4, messagePart.getSendAttempts());
      statement.setInt(5, messagePart.getDownloadAttempts());
      statement.setInt(6, messagePart.getStatus().getCode());
      statement.setTimestamp(7, Timestamp.valueOf(persisted));
      statement.setObject(8, messagePart.getMessageId());
      statement.setString(9, messagePart.getMessageUsername());
      statement.setObject(10, messagePart.getMessageDeviceId());
      statement.setObject(11, messagePart.getMessageTypeId());
      statement.setObject(12, messagePart.getMessageCorrelationId());
      statement.setInt(13, messagePart.getMessagePriority().getCode());
      statement.setTimestamp(14, Timestamp.valueOf(messagePart.getMessageCreated()));
      statement.setString(15, messagePart.getMessageDataHash());
      statement.setString(16, messagePart.getMessageEncryptionIV());
      statement.setString(17, messagePart.getMessageChecksum());
      statement.setBytes(18, messagePart.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new MessagingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createMessagePartSQL));
      }

      messagePart.setPersisted(persisted);
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
          StringUtil.isNullOrEmpty(message.getEncryptionIV())
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
        message.setDataHash("");
        message.setEncryptionIV("");

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
  public void deleteMessage(Message message)
    throws MessagingServiceException
  {
    deleteMessage(message.getId());
  }

  /**
   * Delete the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  @Override
  public void deleteMessage(UUID messageId)
    throws MessagingServiceException
  {
    String deleteMessageSQL = "DELETE FROM messaging.messages WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteMessageSQL))
    {
      statement.setObject(1, messageId);

      statement.executeUpdate();
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
  public void deleteMessagePart(UUID messagePartId)
    throws MessagingServiceException
  {
    String deleteMessagePartSQL = "DELETE FROM messaging.message_parts WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteMessagePartSQL))
    {
      statement.setObject(1, messagePartId);

      statement.executeUpdate();
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
  public void deleteMessagePartsForMessage(UUID messageId)
    throws MessagingServiceException
  {
    String deleteMessagePartsForMessageSQL = "DELETE FROM messaging.message_parts WHERE msg_id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteMessagePartsForMessageSQL))
    {
      statement.setObject(1, messageId);

      statement.executeUpdate();
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
   * Retrieve the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report
   *
   * @return the error report or <code>null</code> if the error report could not be found
   */
  @Override
  public ErrorReport getErrorReport(UUID errorReportId)
    throws MessagingServiceException
  {
    String getErrorReportSQL = "SELECT id, application_id, application_version, "
        + "description, detail, feedback, created, who, device_id, data "
        + "FROM messaging.error_reports WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getErrorReportSQL))
    {
      statement.setObject(1, errorReportId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildErrorReportFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format("Failed to retrieve the error report (%s)",
          errorReportId), e);
    }
  }

  /**
   * Retrieve the summary for the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report
   *
   * @return the summary for the error report or <code>null</code> if the error report could not be
   *         found
   */
  public ErrorReportSummary getErrorReportSummary(UUID errorReportId)
    throws MessagingServiceException
  {
    String getErrorReportSummarySQL =
        "SELECT id, application_id, application_version, created, who, device_id "
        + "FROM messaging.error_reports WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getErrorReportSummarySQL))
    {
      statement.setObject(1, errorReportId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildErrorReportSummaryFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to retrieve the summary for the error report (%s)", errorReportId), e);
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
   * @return the message or <code>null</code> if the message could not be found
   */
  @Override
  public Message getMessage(UUID messageId)
    throws MessagingServiceException
  {
    String getMessagSQL = "SELECT id, username, device_id, type_id, correlation_id, priority, "
        + "status, created, persisted, updated, send_attempts, process_attempts, download_attempts, "
        + "lock_name, last_processed, data FROM messaging.messages WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getMessagSQL))
    {
      statement.setObject(1, messageId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildMessageFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
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
  @SuppressWarnings("resource")
  public List<MessagePart> getMessagePartsQueuedForAssembly(UUID messageId, String lockName)
    throws MessagingServiceException
  {
    String getMessagePartsQueuedForAssemblySQL = "SELECT id, part_no, total_parts, send_attempts, "
        + "download_attempts, status, persisted, updated, msg_id, msg_username, msg_device_id, "
        + "msg_type_id, msg_correlation_id, msg_priority, msg_created, msg_data_hash, "
        + "msg_encryption_iv, msg_checksum, lock_name, data FROM messaging.message_parts "
        + "WHERE status=? AND msg_id=? ORDER BY part_no FOR UPDATE";

    String lockMessagePartSQL =
        "UPDATE messaging.message_parts SET status=?, lock_name=?, updated=? WHERE id=?";

    try
    {
      List<MessagePart> messageParts = new ArrayList<>();

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            getMessagePartsQueuedForAssemblySQL))
      {
        statement.setInt(1, MessagePartStatus.QUEUED_FOR_ASSEMBLY.getCode());
        statement.setObject(2, messageId);

        try (ResultSet rs = statement.executeQuery())
        {
          while (rs.next())
          {
            messageParts.add(buildMessagePartFromResultSet(rs));
          }
        }

        for (MessagePart messagePart : messageParts)
        {
          LocalDateTime updated = LocalDateTime.now();

          messagePart.setStatus(MessagePartStatus.ASSEMBLING);
          messagePart.setLockName(lockName);
          messagePart.setUpdated(updated);

          try (PreparedStatement updateStatement = connection.prepareStatement(lockMessagePartSQL))
          {
            updateStatement.setInt(1, MessagePartStatus.ASSEMBLING.getCode());
            updateStatement.setString(2, lockName);
            updateStatement.setTimestamp(3, Timestamp.valueOf(updated));
            updateStatement.setObject(4, messagePart.getId());

            if (updateStatement.executeUpdate() != 1)
            {
              throw new MessagingServiceException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  lockMessagePartSQL));
            }
          }
        }
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
    String getMessagePartsQueuedForDownloadSQL = "SELECT id, part_no, total_parts, send_attempts, "
        + "download_attempts, status, persisted, updated, msg_id, msg_username, msg_device_id, "
        + "msg_type_id, msg_correlation_id, msg_priority, msg_created, msg_data_hash, "
        + "msg_encryption_iv, msg_checksum, lock_name, data FROM messaging.message_parts "
        + "WHERE status=? AND msg_username=? AND msg_device_id=? ORDER BY part_no "
        + "FETCH FIRST 3 ROWS ONLY FOR UPDATE";

    String lockMessagePartForDownloadSQL = "UPDATE messaging.message_parts "
        + "SET status=?, lock_name=?, updated=?, download_attempts=download_attempts+1 WHERE id=?";

    try
    {
      List<MessagePart> messageParts = new ArrayList<>();

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            getMessagePartsQueuedForDownloadSQL))
      {
        /*
         * First check if we already have message parts locked for downloading for this device, if
         * so update the lock and return these message parts. This handles the situation where a
         * device attempted to download message parts previously and failed leaving these message
         * parts locked in a "Downloading" state.
         */
        statement.setInt(1, MessagePartStatus.DOWNLOADING.getCode());
        statement.setString(2, username);
        statement.setObject(3, deviceId);

        try (ResultSet rs = statement.executeQuery())
        {
          while (rs.next())
          {
            messageParts.add(buildMessagePartFromResultSet(rs));
          }
        }

        /*
         * If we did not find message parts already locked for downloading then retrieve the message
         * parts that are "QueuedForDownload" for the device.
         */
        if (messageParts.size() == 0)
        {
          statement.setInt(1, MessagePartStatus.QUEUED_FOR_DOWNLOAD.getCode());
          statement.setString(2, username);
          statement.setObject(3, deviceId);

          try (ResultSet rs = statement.executeQuery())
          {
            while (rs.next())
            {
              messageParts.add(buildMessagePartFromResultSet(rs));
            }
          }
        }

        for (MessagePart messagePart : messageParts)
        {
          LocalDateTime updated = LocalDateTime.now();

          messagePart.setStatus(MessagePartStatus.DOWNLOADING);
          messagePart.setLockName(instanceName);
          messagePart.setUpdated(updated);
          messagePart.setDownloadAttempts(messagePart.getDownloadAttempts() + 1);

          try (PreparedStatement updateStatement = connection.prepareStatement(
              lockMessagePartForDownloadSQL))
          {
            updateStatement.setInt(1, MessagePartStatus.DOWNLOADING.getCode());
            updateStatement.setString(2, instanceName);
            updateStatement.setTimestamp(3, Timestamp.valueOf(updated));
            updateStatement.setObject(4, messagePart.getId());

            if (updateStatement.executeUpdate() != 1)
            {
              throw new MessagingServiceException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  lockMessagePartForDownloadSQL));
            }
          }
        }
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
    String getMessagesQueuedForDownloadSQL = "SELECT id, username, device_id, type_id, "
        + "correlation_id, priority, status, created, persisted, updated, send_attempts, "
        + "process_attempts, download_attempts, lock_name, last_processed, data "
        + "FROM messaging.messages WHERE status=? AND username=? AND device_id=? ORDER BY created "
        + "FETCH FIRST 3 ROWS ONLY FOR UPDATE";

    String lockMessageForDownloadSQL = "UPDATE messaging.messages "
        + "SET status=?, lock_name=?, updated=?, download_attempts=download_attempts+1 WHERE id=?";

    try
    {
      List<Message> messages = new ArrayList<>();

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getMessagesQueuedForDownloadSQL))
      {
        /*
         * First check if we already have messages locked for downloading for the user-device
         * combination, if so update the lock and return these messages. This handles the situation
         * where a device attempted to download messages previously and failed leaving these
         * messages locked in a "Downloading" state.
         */
        statement.setInt(1, MessageStatus.DOWNLOADING.getCode());
        statement.setString(2, username);
        statement.setObject(3, deviceId);

        try (ResultSet rs = statement.executeQuery())
        {
          while (rs.next())
          {
            Message message = buildMessageFromResultSet(rs);

            if (!StringUtil.isNullOrEmpty(message.getLockName()))
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

            messages.add(message);
          }
        }

        /*
         * If we did not find messages already locked for downloading then retrieve the messages
         * that are "QueuedForDownload" for the user-device combination.
         */
        if (messages.size() == 0)
        {
          statement.setInt(1, MessageStatus.QUEUED_FOR_DOWNLOAD.getCode());
          statement.setString(2, username);
          statement.setObject(3, deviceId);

          try (ResultSet rs = statement.executeQuery())
          {
            while (rs.next())
            {
              messages.add(buildMessageFromResultSet(rs));
            }
          }
        }

        for (Message message : messages)
        {
          LocalDateTime updated = LocalDateTime.now();

          message.setStatus(MessageStatus.DOWNLOADING);
          message.setLockName(instanceName);
          message.setUpdated(updated);
          message.setDownloadAttempts(message.getDownloadAttempts() + 1);

          try (PreparedStatement updateStatement = connection.prepareStatement(
              lockMessageForDownloadSQL))
          {
            updateStatement.setInt(1, MessageStatus.DOWNLOADING.getCode());
            updateStatement.setString(2, instanceName);
            updateStatement.setTimestamp(3, Timestamp.valueOf(updated));
            updateStatement.setObject(4, message.getId());

            if (updateStatement.executeUpdate() != 1)
            {
              throw new MessagingServiceException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  lockMessageForDownloadSQL));
            }
          }
        }
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
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error
   *                               reports to retrieve
   *
   * @return the summaries for the most recent error reports
   */
  @Override
  public List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
    throws MessagingServiceException
  {
    String getMostRecentErrorReportSummariesSQL =
        "SELECT id, application_id, application_version, created, who, device_id "
        + "FROM messaging.error_reports ";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(String.format(
          "%s ORDER BY created DESC FETCH FIRST %d ROWS ONLY",
          getMostRecentErrorReportSummariesSQL, maximumNumberOfEntries)))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<ErrorReportSummary> errorReportSummaries = new ArrayList<>();

        while (rs.next())
        {
          errorReportSummaries.add(buildErrorReportSummaryFromResultSet(rs));
        }

        return errorReportSummaries;
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(
          "Failed to retrieve the summaries for the most recent error reports", e);
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
    String getNextMessageForProcessingSQL = "SELECT id, username, device_id, type_id, "
        + "correlation_id, priority, status, created, persisted, updated, send_attempts, "
        + "process_attempts, download_attempts, lock_name, last_processed, data "
        + "FROM messaging.messages "
        + "WHERE status=? AND (last_processed<? OR last_processed IS NULL) "
        + "ORDER BY updated FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    String lockMessageSQL =
        "UPDATE messaging.messages SET status=?, lock_name=?, updated=? WHERE id=?";

    try
    {
      Message message = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getNextMessageForProcessingSQL))
      {
        Timestamp processedBefore = new Timestamp(System.currentTimeMillis()
            - processingRetryDelay);

        statement.setInt(1, MessageStatus.QUEUED_FOR_PROCESSING.getCode());
        statement.setTimestamp(2, processedBefore);

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            LocalDateTime updated = LocalDateTime.now();

            message = buildMessageFromResultSet(rs);

            message.setStatus(MessageStatus.PROCESSING);
            message.setLockName(instanceName);
            message.setUpdated(updated);

            try (PreparedStatement updateStatement = connection.prepareStatement(lockMessageSQL))
            {
              updateStatement.setInt(1, MessageStatus.PROCESSING.getCode());
              updateStatement.setString(2, instanceName);
              updateStatement.setTimestamp(3, Timestamp.valueOf(updated));
              updateStatement.setObject(4, message.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new MessagingServiceException(String.format(
                    "No rows were affected as a result of executing the SQL statement (%s)",
                    lockMessageSQL));
              }
            }
          }
        }
      }

      return message;
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(
          "Failed to retrieve the next message that has been queued for processing", e);
    }
  }

  /**
   * Returns the total number of error reports.
   *
   * @return the total number of error reports
   */
  @Override
  public int getNumberOfErrorReports()
    throws MessagingServiceException
  {
    String getNumberOfErrorReportsSQL = "SELECT COUNT(id) FROM messaging.error_reports";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfErrorReportsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException("Failed to retrieve the total number of error reports",
          e);
    }
  }

  /**
   * Increment the processing attempts for the message.
   *
   * @param message the message whose processing attempts should be incremented
   */
  @Override
  public void incrementMessageProcessingAttempts(Message message)
    throws MessagingServiceException
  {
    String incrementMessageProcessingAttemptsSQL = "UPDATE messaging.messages "
        + "SET process_attempts=process_attempts + 1, updated=?, last_processed=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          incrementMessageProcessingAttemptsSQL))
    {
      LocalDateTime currentTime = LocalDateTime.now();

      statement.setTimestamp(1, Timestamp.valueOf(currentTime));
      statement.setTimestamp(2, Timestamp.valueOf(currentTime));
      statement.setObject(3, message.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new MessagingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            incrementMessageProcessingAttemptsSQL));
      }

      message.setProcessAttempts(message.getProcessAttempts() + 1);
      message.setLastProcessed(currentTime);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to increment the processing attempts for the message (%s)", message.getId()), e);
    }
  }

  /**
   * Should the specified message be archived?
   *
   * @param message the message
   *
   * @return <code>true</code> if a message with the specified type information should be archived
   *         or <code>false</code> otherwise
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
    String isMessageArchivedSQL = "SELECT id FROM messaging.archived_messages WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(isMessageArchivedSQL))
    {
      statement.setObject(1, messageId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
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
   * @return <code>true</code> if the message part has already been queued for assemble or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean isMessagePartQueuedForAssembly(UUID messagePartId)
    throws MessagingServiceException
  {
    String isMessagePartQueuedForAssemblySQL = "SELECT id FROM messaging.message_parts WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(isMessagePartQueuedForAssemblySQL))
    {
      statement.setObject(1, messagePartId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to check whether the message part (%s) is queued for assembly", messagePartId),
          e);
    }
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
  public void queueMessageForDownload(Message message)
    throws MessagingServiceException
  {
    // Update the status of the message to indicate that it is queued for sending
    message.setStatus(MessageStatus.QUEUED_FOR_DOWNLOAD);

    try
    {
      if (message.getData().length <= Message.MAX_ASYNC_MESSAGE_SIZE)
      {
        createMessage(message);
      }
      else
      {
        /*
         * NOTE: The message parts are not encrypted. Since asynchronous messages should ALWAYS be
         *       encrypted the original message needs to be encrypted BEFORE it is queued for
         *       download and split up into a number of message parts.
         */
        if (!message.isEncrypted())
        {
          if (!encryptMessage(message))
          {
            throw new MessagingServiceException(String.format(
                "Failed to process the asynchronous message (%s) with type (%s) that exceeds the "
                + "maximum asynchronous message size (%d) and must be encrypted prior to being "
                + "queued for download", message.getId(), message.getTypeId(), Message
                .MAX_ASYNC_MESSAGE_SIZE));
          }
        }

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
   * Queue the specified message for processing.
   *
   * @param message the message to queue
   */
  public void queueMessageForProcessing(Message message)
    throws MessagingServiceException
  {
    // Update the status of the message to indicate that it is queued for processing
    message.setStatus(MessageStatus.QUEUED_FOR_PROCESSING);

    try
    {
      createMessage(message);
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to queue the message (%s) for processing", message.getId()), e);
    }

    // Archive the message
    archiveMessage(message);

    if (logger.isDebugEnabled())
    {
      logger.debug(String.format("Queued message (%s) with type (%s) for processing",
          message.getId(), message.getTypeId()));

      logger.debug(message.toString());
    }

    // Inform the Background Message Processor that a new message has been queued for processing
    try
    {
      applicationContext.getBean(BackgroundMessageProcessor.class).processMessages();
    }
    catch (Throwable e)
    {
      logger.error(String.format("Failed to invoke the Background Message Processor to process "
          + "the message (%s) that was queued for processing", message.getId()), e);
    }
  }

  /**
   * Queue the specified message part for assembly.
   *
   * @param messagePart the message part to queue
   */
  public void queueMessagePartForAssembly(MessagePart messagePart)
    throws MessagingServiceException
  {
    // Update the status of the message part to indicate that it is queued for assembly
    messagePart.setStatus(MessagePartStatus.QUEUED_FOR_ASSEMBLY);

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
            messagePart.getMessageCorrelationId(), messagePart.getMessagePriority(), MessageStatus
            .INITIALIZED, messagePart.getMessageCreated(), null, null, 0, 0, 0, null, null,
            reconstructedData, messagePart.getMessageDataHash(),
            messagePart.getMessageEncryptionIV());

        if (decryptMessage(message))
        {
          queueMessageForProcessing(message);
        }
        else
        {
          // Delete the message parts
          deleteMessagePartsForMessage(messagePart.getMessageId());

          logger.error(String.format(
              "Failed to decrypt the reconstructed message (%s) with type (%s) from the user (%s) "
              + "and device (%s). Found %d bytes of message data with the hash (%s) that was "
              + "reconstructed from %d message parts. The message will NOT be processed",
              messagePart.getMessageId(), messagePart.getMessageTypeId(),
              messagePart.getMessageUsername(), messagePart.getMessageDeviceId(), reconstructedData
              .length, messageChecksum, messageParts.size()));

          return;
        }

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
   * @param lockTimeout the lock timeout in seconds
   * @param status      the current status of the messages that have been locked
   * @param newStatus   the new status for the messages that have been unlocked
   *
   * @return the number of message locks reset
   */
  @Override
  public int resetExpiredMessageLocks(int lockTimeout, MessageStatus status,
      MessageStatus newStatus)
    throws MessagingServiceException
  {
    String resetExpiredMessageLocksSQL =
        "UPDATE messaging.messages SET status=?, lock_name=NULL, updated=? "
        + "WHERE lock_name IS NOT NULL AND status=? AND updated < ?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetExpiredMessageLocksSQL))
    {
      return resetLocks(lockTimeout, statement, newStatus.getCode(), status.getCode());
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
   * @param lockTimeout the lock timeout in seconds
   * @param status      the current status of the message parts that have been locked
   * @param newStatus   the new status for the message parts that have been unlocked
   *
   * @return the number of message part locks reset
   */
  @Override
  public int resetExpiredMessagePartLocks(int lockTimeout, MessagePartStatus status,
      MessagePartStatus newStatus)
    throws MessagingServiceException
  {
    String resetExpiredMessagePartLocksSQL =
        "UPDATE messaging.message_parts SET status=?, lock_name=NULL, updated=? "
        + "WHERE lock_name IS NOT NULL AND status=? AND updated < ?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetExpiredMessagePartLocksSQL))
    {
      return resetLocks(lockTimeout, statement, newStatus.getCode(), status.getCode());
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
   *
   * @return the number of messages whose locks were reset
   */
  @Override
  public int resetMessageLocks(MessageStatus status, MessageStatus newStatus)
    throws MessagingServiceException
  {
    String resetMessageLocksSQL = "UPDATE messaging.messages "
        + "SET status=?, lock_name=NULL, updated=? WHERE lock_name=? AND status=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetMessageLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, instanceName);
      statement.setInt(4, status.getCode());

      return statement.executeUpdate();
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
   *
   * @return the number of message parts whose locks were reset
   */
  @Override
  public int resetMessagePartLocks(MessagePartStatus status, MessagePartStatus newStatus)
    throws MessagingServiceException
  {
    String resetMessagePartLocksSQL = "UPDATE messaging.message_parts "
        + "SET status=?, lock_name=NULL WHERE lock_name=? AND status=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetMessagePartLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setString(2, instanceName);
      statement.setInt(3, status.getCode());

      return statement.executeUpdate();
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
  public void setMessagePartStatus(UUID messagePartId, MessagePartStatus status)
    throws MessagingServiceException
  {
    String setMessagePartStatusSQL =
        "UPDATE messaging.message_parts SET status=?, updated=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(setMessagePartStatusSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, messagePartId);

      if (statement.executeUpdate() != 1)
      {
        throw new MessagingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            setMessagePartStatusSQL));
      }
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
  public void setMessageStatus(UUID messageId, MessageStatus status)
    throws MessagingServiceException
  {
    String setMessageStatusSQL = "UPDATE messaging.messages SET status=?, updated=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(setMessageStatusSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, messageId);

      if (statement.executeUpdate() != 1)
      {
        throw new MessagingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            setMessageStatusSQL));
      }
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
  public void unlockMessage(Message message, MessageStatus status)
    throws MessagingServiceException
  {
    String unlockMessageSQL =
        "UPDATE messaging.messages SET status=?, updated=?, lock_name=NULL WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockMessageSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, message.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new MessagingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            unlockMessageSQL));
      }
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
  public void unlockMessagePart(UUID messagePartId, MessagePartStatus status)
    throws MessagingServiceException
  {
    String unlockMessagePartSQL =
        "UPDATE messaging.message_parts SET status=?, updated=?, lock_name=NULL WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockMessagePartSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, messagePartId);

      if (statement.executeUpdate() != 1)
      {
        throw new MessagingServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            unlockMessagePartSQL));
      }
    }
    catch (Throwable e)
    {
      throw new MessagingServiceException(String.format(
          "Failed to unlock and set the status for the message part (%s) to (%s)", messagePartId,
          status.toString()), e);
    }
  }

  private ErrorReport buildErrorReportFromResultSet(ResultSet rs)
    throws SQLException
  {
    byte[] data = rs.getBytes(10);

    return new ErrorReport(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(2)),
        rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getTimestamp(7)
        .toLocalDateTime(), rs.getString(8), UUID.fromString(rs.getString(9)),
        (data == null)
        ? new byte[0]
        : data);
  }

  private ErrorReportSummary buildErrorReportSummaryFromResultSet(ResultSet rs)
    throws SQLException
  {
    return new ErrorReportSummary(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(
        2)), rs.getInt(3), rs.getTimestamp(4).toLocalDateTime(), rs.getString(5), UUID.fromString(
        rs.getString(6)));
  }

  private Message buildMessageFromResultSet(ResultSet rs)
    throws SQLException
  {
    return new Message(UUID.fromString(rs.getString(1)), rs.getString(2), UUID.fromString(
        rs.getString(3)), UUID.fromString(rs.getString(4)), UUID.fromString(rs.getString(5)),
        MessagePriority.fromCode(rs.getInt(6)), MessageStatus.fromCode(rs.getInt(7)),
        (rs.getTimestamp(8) == null)
        ? null
        : rs.getTimestamp(8).toLocalDateTime(),
        (rs.getTimestamp(9) == null)
        ? null
        : rs.getTimestamp(9).toLocalDateTime(),
        (rs.getTimestamp(10) == null)
        ? null
        : rs.getTimestamp(10).toLocalDateTime(), rs.getInt(11), rs.getInt(12), rs.getInt(13),
            rs.getString(14),
        (rs.getTimestamp(15) == null)
        ? null
        : rs.getTimestamp(15).toLocalDateTime(), rs.getBytes(16), "", "");
  }

  private MessagePart buildMessagePartFromResultSet(ResultSet rs)
    throws SQLException
  {
    return new MessagePart(UUID.fromString(rs.getString(1)), rs.getInt(2), rs.getInt(3), rs.getInt(
        4), rs.getInt(5), MessagePartStatus.fromCode(rs.getInt(6)),
        (rs.getTimestamp(7) == null)
        ? null
        : rs.getTimestamp(7).toLocalDateTime(),
        (rs.getTimestamp(8) == null)
        ? null
        : rs.getTimestamp(8).toLocalDateTime(), UUID.fromString(rs.getString(9)), rs.getString(10),
            UUID.fromString(rs.getString(11)), UUID.fromString(rs.getString(12)), UUID.fromString(
            rs.getString(13)), MessagePriority.fromCode(rs.getInt(14)),
        (rs.getTimestamp(15) == null)
        ? null
        : rs.getTimestamp(15).toLocalDateTime(), rs.getString(16), rs.getString(17), rs.getString(
            18), rs.getString(19), rs.getBytes(20));
  }

  /**
   * Initialize the configuration for the Messaging Service.
   */
  private void initConfiguration()
    throws MessagingServiceException
  {
    try
    {
      if (StringUtil.isNullOrEmpty(encryptionKeyBase64))
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
              boolean isArchivable = messageElement.getAttribute("isArchivable").equalsIgnoreCase(
                  "Y");

              messageHandlerConfig.addMessageConfig(messageType, isSynchronous, isAsynchronous,
                  isArchivable);
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

  private int resetLocks(int lockTimeout, PreparedStatement statement, int newStatusCode,
      int statusCode)
    throws SQLException
  {
    statement.setInt(1, newStatusCode);
    statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
    statement.setInt(3, statusCode);
    statement.setTimestamp(4, new Timestamp(System.currentTimeMillis() - (lockTimeout * 1000L)));

    return statement.executeUpdate();
  }
}

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

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.UUID;

/**
 * The <code>IMessagingService</code> interface defines the interface for the Messaging Service
 * for the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public interface IMessagingService
{
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
  boolean allPartsQueuedForMessage(UUID messageId, int totalParts)
    throws MessagingServiceException;

  /**
   * Archive the message.
   *
   * @param message the message to archive
   */
  void archiveMessage(Message message)
    throws MessagingServiceException;

  /**
   * Returns <code>true</code> if the message processor is capable of processing the specified
   * message or <code>false</code> otherwise.
   *
   * @param message the message to process
   *
   * @return <code>true</code> if the message processor is capable of processing the specified
   *         message or <code>false</code> otherwise
   */
  boolean canProcessMessage(Message message);

  /**
   * Returns <code>true</code> if the message processor is capable of queueing the specified
   * message part for assembly or <code>false</code> otherwise.
   *
   * @param messagePart the message part to queue for assembly
   *
   * @return <code>true</code> if the message processor is capable of queueing the specified
   *         message part for assembly or <code>false</code> otherwise
   */
  boolean canQueueMessagePartForAssembly(MessagePart messagePart);

  /**
   * Create the entry for the error report in the database.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   */
  void createErrorReport(ErrorReport errorReport)
    throws MessagingServiceException;

  /**
   * Create the message.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   */
  void createMessage(Message message)
    throws MessagingServiceException;

  /**
   * Create the message part.
   *
   * @param messagePart the <code>MessagePart</code> instance containing the information for the
   *                    message part
   */
  void createMessagePart(MessagePart messagePart)
    throws MessagingServiceException;

  /**
   * Decrypt the message.
   *
   * @param message the message to decrypt
   *
   * @return <code>true</code> if the message data was decrypted successfully or <code>false</code>
   *         otherwise
   */
  boolean decryptMessage(Message message)
    throws MessagingServiceException;

  /**
   * Delete the message.
   *
   * @param message the message to delete
   */
  void deleteMessage(Message message)
    throws MessagingServiceException;

  /**
   * Delete the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  void deleteMessage(UUID messageId)
    throws MessagingServiceException;

  /**
   * Delete the message part.
   *
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part
   */
  void deleteMessagePart(UUID messagePartId)
    throws MessagingServiceException;

  /**
   * Delete the message parts for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  void deleteMessagePartsForMessage(UUID messageId)
    throws MessagingServiceException;

  /**
   * Derive the user-device encryption key.
   *
   * @param username the username uniquely identifying the user e.g. test1
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *
   * @return the user-device encryption key
   */
  byte[] deriveUserDeviceEncryptionKey(String username, UUID deviceId)
    throws MessagingServiceException;

  /**
   * Encrypt the message.
   *
   * @param message the message to encrypt
   *
   * @return <code>true</code> if the message data was encrypted successfully or <code>false</code>
   *         otherwise
   */
  boolean encryptMessage(Message message)
    throws MessagingServiceException;

  /**
   * Retrieve the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report
   *
   * @return the error report or <code>null</code> if the error report could not be found
   */
  ErrorReport getErrorReport(UUID errorReportId)
    throws MessagingServiceException;

  /**
   * Retrieve the summary for the error report.
   *
   * @param errorReportId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      error report
   *
   * @return the summary for the error report or <code>null</code> if the error report could not be
   *         found
   */
  ErrorReportSummary getErrorReportSummary(UUID errorReportId)
    throws MessagingServiceException;

  /**
   * Returns the maximum number of times processing will be attempted for a message.
   *
   * @return the maximum number of times processing will be attempted for a message
   */
  int getMaximumProcessingAttempts();

  /**
   * Retrieve the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   *
   * @return the message or <code>null</code> if the message could not
   *         be found
   */
  Message getMessage(UUID messageId)
    throws MessagingServiceException;

  /**
   * Retrieve the message parts queued for assembly for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   * @param lockName  the name of the lock that should be applied to the message parts queued for
   *                  assembly when they are retrieved
   *
   * @return the message parts queued for assembly for the message
   */
  List<MessagePart> getMessagePartsQueuedForAssembly(UUID messageId, String lockName)
    throws MessagingServiceException;

  /**
   * Get the message parts for a user that have been queued for download by a particular remote
   * device.
   *
   * @param username the username identifying the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *
   * @return the message parts that have been queued for download by a particular remote device
   */
  List<MessagePart> getMessagePartsQueuedForDownload(String username, UUID deviceId)
    throws MessagingServiceException;

  /**
   * Get the messages for a user that have been queued for download by a particular remote device.
   *
   * @param username the username identifying the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *
   * @return the messages for a user that have been queued for download by a particular remote
   *         device
   */
  List<Message> getMessagesQueuedForDownload(String username, UUID deviceId)
    throws MessagingServiceException;

  /**
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error
   *                               reports to retrieve
   *
   * @return the summaries for the most recent error reports
   */
  List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
    throws MessagingServiceException;

  /**
   * Retrieve the next message that has been queued for processing.
   * <p/>
   * The message will be locked to prevent duplicate processing.
   *
   * @return the next message that has been queued for processing or <code>null</code> if no
   *         messages are currently queued for processing
   */
  Message getNextMessageQueuedForProcessing()
    throws MessagingServiceException;

  /**
   * Returns the total number of error reports.
   *
   * @return the total number of error reports
   */
  int getNumberOfErrorReports()
    throws MessagingServiceException;

  /**
   * Increment the processing attempts for the message.
   *
   * @param message the message whose processing attempts should be incremented
   */
  void incrementMessageProcessingAttempts(Message message)
    throws MessagingServiceException;

  /**
   * Should the specified message be archived?
   *
   * @param message the message
   *
   * @return <code>true</code> if a message with the specified type information should be archived
   *         or <code>false</code> otherwise
   */
  boolean isArchivableMessage(Message message);

  /**
   * Can the specified message be processed asynchronously?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message can be processed asynchronously or
   *         <code>false</code> otherwise
   */
  boolean isAsynchronousMessage(Message message);

  /**
   * Has the message already been archived?
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   *
   * @return <code>true</code> if the message has already been archived or <code>false</code>
   *         otherwise
   */
  boolean isMessageArchived(UUID messageId)
    throws MessagingServiceException;

  /**
   * Has the message part already been queued for assembly?
   *
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part
   *
   * @return <code>true</code> if the message part has already been queued for assemble or
   *         <code>false</code> otherwise
   */
  boolean isMessagePartQueuedForAssembly(UUID messagePartId)
    throws MessagingServiceException;

  /**
   * Can the specified message be processed synchronously?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message can be processed synchronously or
   *         <code>false</code> otherwise
   */
  boolean isSynchronousMessage(Message message);

  /**
   * Process the message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   */
  Message processMessage(Message message)
    throws MessagingServiceException;

  /**
   * Queue the specified message for download by a remote device.
   *
   * @param message the message to queue
   */
  void queueMessageForDownload(Message message)
    throws MessagingServiceException;

  /**
   * Queue the specified message for processing.
   *
   * @param message the message to queue
   */
  void queueMessageForProcessing(Message message)
    throws MessagingServiceException;

  /**
   * Queue the specified message part for assembly.
   *
   * @param messagePart the message part to queue
   */
  void queueMessagePartForAssembly(MessagePart messagePart)
    throws MessagingServiceException;

  /**
   * Reset the expired message locks.
   *
   * @param lockTimeout the lock timeout in seconds
   * @param status      the current status of the messages that have been locked
   * @param newStatus   the new status for the messages that have been unlocked
   *
   * @return the number of message locks reset
   */
  int resetExpiredMessageLocks(int lockTimeout, MessageStatus status, MessageStatus newStatus)
    throws MessagingServiceException;

  /**
   * Reset the expired message part locks.
   *
   * @param lockTimeout the lock timeout in seconds
   * @param status      the current status of the message parts that have been locked
   * @param newStatus   the new status for the message parts that have been unlocked
   *
   * @return the number of message part locks reset
   */
  int resetExpiredMessagePartLocks(int lockTimeout, MessagePartStatus status,
      MessagePartStatus newStatus)
    throws MessagingServiceException;

  /**
   * Reset the locks for the messages.
   *
   * @param status    the current status of the messages that have been locked
   * @param newStatus the new status for the messages that have been unlocked
   *
   * @return the number of messages whose locks were reset
   */
  int resetMessageLocks(MessageStatus status, MessageStatus newStatus)
    throws MessagingServiceException;

  /**
   * Reset the locks for the message parts.
   *
   * @param status    the current status of the message parts that have been locked
   * @param newStatus the new status for the message parts that have been unlocked
   *
   * @return the number of message parts whose locks were reset
   */
  int resetMessagePartLocks(MessagePartStatus status, MessagePartStatus newStatus)
    throws MessagingServiceException;

  /**
   * Set the status for a message part.
   *
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part
   * @param status        the new status
   */
  void setMessagePartStatus(UUID messagePartId, MessagePartStatus status)
    throws MessagingServiceException;

  /**
   * Set the status for a message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   * @param status    the new status
   */
  void setMessageStatus(UUID messageId, MessageStatus status)
    throws MessagingServiceException;

  /**
   * Unlock the message.
   *
   * @param message the message to unlock
   * @param status  the new status for the unlocked message
   */
  void unlockMessage(Message message, MessageStatus status)
    throws MessagingServiceException;

  /**
   * Unlock a locked message part.
   *
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part
   * @param status        the new status for the unlocked message part
   */
  void unlockMessagePart(UUID messagePartId, MessagePartStatus status)
    throws MessagingServiceException;
}

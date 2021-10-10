/*
 * Copyright 2021 Marcus Portmann
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>MessageRepository</b> interface declares the repository for the <b>Message</b> domain
 * type.
 *
 * @author Marcus Portmann
 */
public interface MessageRepository extends JpaRepository<Message, UUID> {

  /**
   * Delete the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) for the message
   */
  @Modifying
  @Query("delete from Message m where m.id = :messageId")
  void deleteById(@Param("messageId") UUID messageId);

  /**
   * Retrieve and lock the messages queued for processing.
   *
   * @param processedBefore the date and time used to select messages for reprocessing
   * @param pageable the pagination information
   * @return the locked messages queued for processing
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select m from Message m where m.status = 3 and "
          + "(m.lastProcessed < :processedBefore or m.lastProcessed is null) order by m.lastProcessed")
  List<Message> findMessagesQueuedForProcessingForWrite(
      @Param("processedBefore") LocalDateTime processedBefore, Pageable pageable);

  /**
   * Retrieve and lock the messages with the specified status for the user and device.
   *
   * @param status the message status
   * @param username the username for the user
   * @param deviceId the Universally Unique Identifier (UUID) for the device
   * @param pageable the pagination information
   * @return the locked messages with the specified status for the user and device
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select m from Message m where m.status = :status and m.username = :username and "
          + "m.deviceId = :deviceId order by m.created")
  List<Message> findMessagesWithStatusForUserAndDeviceForWrite(
      @Param("status") MessageStatus status,
      @Param("username") String username,
      @Param("deviceId") UUID deviceId,
      Pageable pageable);

  /**
   * Lock the message for download.
   *
   * @param messageId the Universally Unique Identifier (UUID) for the message
   * @param lockName the lock name
   */
  @Modifying
  @Query(
      "update Message m set m.lockName = :lockName, m.status = 9, "
          + "m.downloadAttempts = m.downloadAttempts + 1 where m.id = :messageId")
  void lockMessageForDownload(
      @Param("messageId") UUID messageId, @Param("lockName") String lockName);

  /**
   * Lock the message for processing.
   *
   * @param messageId the Universally Unique Identifier (UUID) for the message
   * @param lockName the lock name
   * @param when the date and time the message is locked for processing
   */
  @Modifying
  @Query(
      "update Message m set m.lockName = :lockName, m.status = 6, "
          + "m.processAttempts = m.processAttempts + 1, m.lastProcessed = :when "
          + "where m.id = :messageId")
  void lockMessageForProcessing(
      @Param("messageId") UUID messageId,
      @Param("lockName") String lockName,
      @Param("when") LocalDateTime when);

  /**
   * Reset the status and locks for the messages with the specified status and lock name.
   *
   * @param status the message status
   * @param newStatus the new message status
   * @param lockName the lock name
   */
  @Modifying
  @Query(
      "update Message m set m.status = :newStatus, m.lockName = null "
          + "where m.status = :status and m.lockName = :lockName ")
  void resetStatusAndLocksForMessagesWithStatusAndLock(
      @Param("status") MessageStatus status,
      @Param("newStatus") MessageStatus newStatus,
      @Param("lockName") String lockName);

  /**
   * Set the status for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) for the message
   * @param status the message status
   */
  @Modifying
  @Query("update Message m set m.status = :status where m.id = :messageId")
  void setMessageStatus(@Param("messageId") UUID messageId, @Param("status") MessageStatus status);

  /**
   * Unlock the message and set its status.
   *
   * @param messageId the Universally Unique Identifier (UUID) for the message
   * @param status the new status for the message
   */
  @Modifying
  @Query("update Message m set m.status = :status, m.lockName = null where m.id = :messageId")
  void unlockMessage(@Param("messageId") UUID messageId, @Param("status") MessageStatus status);
}

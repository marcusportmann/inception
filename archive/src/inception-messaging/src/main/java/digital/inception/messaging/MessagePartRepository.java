/*
 * Copyright 2022 Marcus Portmann
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
 * The <b>MessagePartRepository</b> interface declares the repository for the <b> MessagePart</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface MessagePartRepository extends JpaRepository<MessagePart, UUID> {

  /**
   * Retrieve the number of message parts queued for assembly for the message.
   *
   * @param messageId the ID for the message
   * @return the number of message parts queued for assembly for the message
   */
  @Query(
      "select count(mp.id) from MessagePart mp where mp.status = 4 and "
          + "mp.messageId = :messageId")
  int countMessagePartsQueuedForAssemblyByMessageId(@Param("messageId") UUID messageId);

  /**
   * Delete the message part.
   *
   * @param messagePartId the ID for the message part
   */
  @Modifying
  @Query("delete from MessagePart mp where mp.id = :messagePartId")
  void deleteById(@Param("messagePartId") UUID messagePartId);

  /**
   * Delete the message parts for the message.
   *
   * @param messageId the ID for the message
   */
  @Modifying
  @Query("delete from MessagePart mp where mp.messageId = :messageId")
  void deleteMessagePartsByMessageId(@Param("messageId") UUID messageId);

  /**
   * Check whether the message part exists.
   *
   * @param messagePartId the ID for the message part
   * @param status the message part status
   * @return <b>true</b> if the message part with the specified ID and status exists or <b>false</b>
   *     otherwise
   */
  boolean existsByIdAndStatus(UUID messagePartId, MessagePartStatus status);

  /**
   * Retrieve and lock the message parts with the specified status for the message.
   *
   * @param messageId the ID for the message
   * @param status the message part status
   * @return the locked message parts with the specified status for the message
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select mp from MessagePart mp where mp.messageId = :messageId and mp.status = :status "
          + "order by mp.partNo")
  List<MessagePart> findMessagePartsByMessageIdAndStatusForWrite(
      @Param("messageId") UUID messageId, @Param("status") MessagePartStatus status);

  /**
   * Retrieve and lock the message parts with the specified status for the user and device.
   *
   * @param username the username for the user
   * @param deviceId the ID for the device
   * @param status the message part status
   * @param pageable the pagination information
   * @return the locked message parts with the specified status for the user and device
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select mp from MessagePart mp where mp.messageUsername = :username and "
          + "mp.messageDeviceId = :deviceId and mp.status = :status "
          + "order by mp.messageId, mp.partNo")
  List<MessagePart> findMessagePartsByUsernameAndDeviceIdAndStatusForWrite(
      @Param("username") String username,
      @Param("deviceId") UUID deviceId,
      @Param("status") MessagePartStatus status,
      Pageable pageable);

  /**
   * Lock the message part for assembly.
   *
   * @param messagePartId the ID for the message part
   * @param lockName the lock name
   */
  @Modifying
  @Query(
      "update MessagePart mp set mp.lockName = :lockName, mp.status = 5 "
          + "where mp.id = :messagePartId")
  void lockMessagePartForAssembly(
      @Param("messagePartId") UUID messagePartId, @Param("lockName") String lockName);

  /**
   * Lock the message part for download.
   *
   * @param messagePartId the ID for the message part
   * @param lockName the lock name
   */
  @Modifying
  @Query(
      "update MessagePart mp set mp.lockName = :lockName, mp.status = 7, "
          + "mp.downloadAttempts = mp.downloadAttempts + 1 where mp.id = :messagePartId")
  void lockMessagePartForDownload(
      @Param("messagePartId") UUID messagePartId, @Param("lockName") String lockName);

  /**
   * Reset the status and locks for the message parts with the specified status and lock name.
   *
   * @param status the message part status
   * @param newStatus the new message part status
   * @param lockName the lock name
   */
  @Modifying
  @Query(
      "update MessagePart mp set mp.status = :newStatus, mp.lockName = null "
          + "where mp.status = :status and mp.lockName = :lockName ")
  void resetStatusAndLocksForMessagePartsWithStatusAndLock(
      @Param("status") MessagePartStatus status,
      @Param("newStatus") MessagePartStatus newStatus,
      @Param("lockName") String lockName);

  /**
   * Set the status for the message part.
   *
   * @param messagePartId the ID for the message part
   * @param status the message part status
   */
  @Modifying
  @Query("update MessagePart mp set mp.status = :status where mp.id = :messagePartId")
  void setStatusById(
      @Param("messagePartId") UUID messagePartId, @Param("status") MessagePartStatus status);

  /**
   * Unlock the message part and set its status.
   *
   * @param messagePartId the ID for the message part
   * @param status the new status for the message part
   */
  @Modifying
  @Query(
      "update MessagePart mp set mp.status = :status, mp.lockName = null "
          + "where mp.id = :messagePartId")
  void unlockMessagePart(
      @Param("messagePartId") UUID messagePartId, @Param("status") MessagePartStatus status);
}

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

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

import javax.persistence.LockModeType;

/**
 * The <code>MessageRepository</code> interface declares the repository for the
 * <code>Message</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface MessageRepository extends JpaRepository<Message, UUID>
{
  @Modifying
  @Query("delete from Message m where m.id = :messageId")
  void deleteById(@Param("messageId") UUID messageId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select m from Message m where m.status = 2 and "
      + "(m.lastProcessed < :processedBefore or m.lastProcessed is null) order by m.lastProcessed")
  List<Message> findMessagesQueuedForProcessingForWrite(@Param(
      "processedBefore") LocalDateTime processedBefore, Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select m from Message m where m.status = :status and m.username = :username and "
      + "m.deviceId = :deviceId order by m.created")
  List<Message> findMessagesWithStatusForUserAndDeviceForWrite(@Param(
      "status") MessageStatus status, @Param("username") String username, @Param(
      "deviceId") UUID deviceId, Pageable pageable);

  @Modifying
  @Query("update Message m set m.lockName = :lockName, m.status = 8, "
      + "m.downloadAttempts = m.downloadAttempts + 1 where m.id = :messageId")
  void lockMessageForDownload(@Param("messageId") UUID messageId, @Param(
      "lockName") String lockName);

  @Modifying
  @Query("update Message m set m.lockName = :lockName, m.status = 5, "
      + "m.processAttempts = m.processAttempts + 1, m.lastProcessed = :when "
      + "where m.id = :messageId")
  void lockMessageForProcessing(@Param("messageId") UUID messageId, @Param(
      "lockName") String lockName, @Param("when") LocalDateTime when);

  @Modifying
  @Query("update Message m set m.status = :newStatus, m.lockName = null "
      + "where m.status = :status and m.lockName = :lockName ")
  void resetStatusAndLocksForMessagesWithStatusAndLock(@Param("status") MessageStatus status,
      @Param("newStatus") MessageStatus newStatus, @Param("lockName") String lockName);

  @Modifying
  @Query("update Message m set m.status = :status "
      + "where m.id = :messageId")
  void setMessageStatus(@Param("messageId") UUID messageId, @Param("status") MessageStatus status);

  @Modifying
  @Query("update Message m set m.status = :status, m.lockName = null "
      + "where m.id = :messageId")
  void unlockMessage(@Param("messageId") UUID messageId, @Param("status") MessageStatus status);
}

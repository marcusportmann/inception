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
 * The <code>MessagePartRepository</code> interface declares the repository for the <code>
 * MessagePart</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface MessagePartRepository extends JpaRepository<MessagePart, UUID> {

  @Query(
      "select count(mp.id) from MessagePart mp where mp.status = 4 and "
          + "mp.messageId = :messageId")
  int countMessagePartsQueuedForAssemblyByMessageId(@Param("messageId") UUID messageId);

  @Modifying
  @Query("delete from MessagePart mp where mp.id = :messagePartId")
  void deleteById(@Param("messagePartId") UUID messagePartId);

  @Modifying
  @Query("delete from MessagePart mp where mp.messageId = :messageId")
  void deleteMessagePartsByMessageId(@Param("messageId") UUID messageId);

  boolean existsByIdAndStatus(UUID messagePartId, MessagePartStatus status);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select mp from MessagePart mp where mp.messageId = :messageId and mp.status = :status "
          + "order by mp.partNo")
  List<MessagePart> findMessagePartsByMessageIdAndStatusForWrite(
      @Param("messageId") UUID messageId, @Param("status") MessagePartStatus status);

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

  @Modifying
  @Query(
      "update MessagePart mp set mp.lockName = :lockName, mp.status = 5 "
          + "where mp.id = :messagePartId")
  void lockMessagePartForAssembly(
      @Param("messagePartId") UUID messagePartId, @Param("lockName") String lockName);

  @Modifying
  @Query(
      "update MessagePart mp set mp.lockName = :lockName, mp.status = 7, "
          + "mp.downloadAttempts = mp.downloadAttempts + 1 where mp.id = :messagePartId")
  void lockMessagePartForDownload(
      @Param("messagePartId") UUID messagePartId, @Param("lockName") String lockName);

  @Modifying
  @Query(
      "update MessagePart mp set mp.status = :newStatus, mp.lockName = null "
          + "where mp.status = :status and mp.lockName = :lockName ")
  void resetStatusAndLocksForMessagePartsWithStatusAndLock(
      @Param("status") MessagePartStatus status,
      @Param("newStatus") MessagePartStatus newStatus,
      @Param("lockName") String lockName);

  @Modifying
  @Query("update MessagePart mp set mp.status = :status where mp.id = :messagePartId")
  void setStatusById(
      @Param("messagePartId") UUID messagePartId, @Param("status") MessagePartStatus status);

  @Modifying
  @Query(
      "update MessagePart mp set mp.status = :status, mp.lockName = null "
          + "where mp.id = :messagePartId")
  void unlockMessagePart(
      @Param("messagePartId") UUID messagePartId, @Param("status") MessagePartStatus status);
}

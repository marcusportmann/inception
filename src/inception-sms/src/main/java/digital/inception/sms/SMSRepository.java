/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.sms;

// ~--- non-JDK imports --------------------------------------------------------

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

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SMSRepository</code> interface declares the repository for the <code>SMS</code> domain
 * type.`
 *
 * @author Marcus Portmann
 */
public interface SMSRepository extends JpaRepository<SMS, UUID> {

  @Modifying
  @Query("delete from SMS s where s.id = :smsId")
  void deleteById(@Param("smsId") UUID smsId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select s from SMS s where s.status = 1 and (s.lastProcessed < :lastProcessedBefore "
          + "or s.lastProcessed is null)")
  List<SMS> findSMSsScheduledForExecutionForWrite(
      @Param("lastProcessedBefore") LocalDateTime lastProcessedBefore, Pageable pageable);

  @Modifying
  @Query(
      "update SMS s set s.lockName = :lockName, s.status = 2, "
          + "s.sendAttempts = s.sendAttempts + 1, s.lastProcessed = :when where s.id = :smsId")
  void lockSMSForSending(
      @Param("smsId") UUID smsId,
      @Param("lockName") String lockName,
      @Param("when") LocalDateTime when);

  @Modifying
  @Query(
      "update SMS s set s.status = :newStatus, s.lockName = null "
          + "where s.lockName = :lockName and s.status = :status")
  void resetSMSLocks(
      @Param("status") SMSStatus status,
      @Param("newStatus") SMSStatus newStatus,
      @Param("lockName") String lockName);

  @Modifying
  @Query("update SMS s set s.status = :status where s.id = :smsId")
  void setSMSStatus(@Param("smsId") UUID smsId, @Param("status") SMSStatus status);

  @Modifying
  @Query("update SMS s set s.status = :status, s.lockName = null where s.id = :smsId")
  void unlockSMS(@Param("smsId") UUID smsId, @Param("status") SMSStatus status);
}

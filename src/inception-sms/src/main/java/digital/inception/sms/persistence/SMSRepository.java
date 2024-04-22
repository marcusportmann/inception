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

package digital.inception.sms.persistence;

import digital.inception.sms.model.SMS;
import digital.inception.sms.model.SMSStatus;
import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <b>SMSRepository</b> interface declares the persistence for the <b>SMS</b> domain type.`
 *
 * @author Marcus Portmann
 */
public interface SMSRepository extends JpaRepository<SMS, UUID> {

  /**
   * Retrieve the SMSs queued for sending.
   *
   * @param lastProcessedBefore the date and time used to select failed SMSs for sending
   * @param pageable the pagination information
   * @return the SMSs queued for sending
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select s from SMS s where s.status = digital.inception.sms.model.SMSStatus.QUEUED "
          + "and (s.lastProcessed < :lastProcessedBefore or s.lastProcessed is null)")
  List<SMS> findSMSsQueuedForSendingForWrite(
      @Param("lastProcessedBefore") OffsetDateTime lastProcessedBefore, Pageable pageable);

  /**
   * Lock the SMS for sending.
   *
   * @param smsId the ID for the SMS
   * @param lockName the name of the lock
   * @param when the date and time the SMS is locked for sending
   */
  @Transactional
  @Modifying
  @Query(
      "update SMS s set s.lockName = :lockName, s.status = digital.inception.sms.model.SMSStatus.SENDING, "
          + "s.sendAttempts = s.sendAttempts + 1, s.lastProcessed = :when where s.id = :smsId")
  void lockSMSForSending(
      @Param("smsId") UUID smsId,
      @Param("lockName") String lockName,
      @Param("when") OffsetDateTime when);

  /**
   * Reset the SMS locks with the specified status.
   *
   * @param status the status
   * @param newStatus the new status for the SMSs
   * @param lockName the lock name
   */
  @Transactional
  @Modifying
  @Query(
      "update SMS s set s.status = :newStatus, s.lockName = null "
          + "where s.lockName = :lockName and s.status = :status")
  void resetSMSLocks(
      @Param("status") SMSStatus status,
      @Param("newStatus") SMSStatus newStatus,
      @Param("lockName") String lockName);

  /**
   * Set the SMS status.
   *
   * @param smsId the ID for the SMS
   * @param status the status for the SMS
   */
  @Transactional
  @Modifying
  @Query("update SMS s set s.status = :status where s.id = :smsId")
  void setSMSStatus(@Param("smsId") UUID smsId, @Param("status") SMSStatus status);

  /**
   * Unlock the SMS.
   *
   * @param smsId the ID for the SMS
   * @param status the status for the SMS
   */
  @Transactional
  @Modifying
  @Query("update SMS s set s.status = :status, s.lockName = null where s.id = :smsId")
  void unlockSMS(@Param("smsId") UUID smsId, @Param("status") SMSStatus status);
}

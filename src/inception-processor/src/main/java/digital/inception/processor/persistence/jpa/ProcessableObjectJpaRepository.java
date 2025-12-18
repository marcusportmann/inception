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

package digital.inception.processor.persistence.jpa;

import digital.inception.processor.AbstractProcessableObject;
import digital.inception.processor.ProcessableObjectStatus;
import jakarta.persistence.LockModeType;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * Common Spring Data JPA repository contract for {@link AbstractProcessableObject} types.
 *
 * <p>This repository:
 *
 * <ul>
 *   <li>extends {@link JpaRepository} and {@link JpaSpecificationExecutor} for standard CRUD and
 *       query support, and
 *   <li>adds a small set of convenience methods used by the background-processing infrastructure.
 * </ul>
 *
 * <p>The additional operations support:
 *
 * <ul>
 *   <li>finding the next processable objects due for processing based on their status and {@code
 *       nextProcessed} time, and
 *   <li>acquiring and resetting PROCESSING locks in a coordinated way.
 * </ul>
 *
 * @param <T> the concrete processable object type
 * @param <ID> the identifier type for the processable object
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @author Marcus Portmann
 */
@NoRepositoryBean
public interface ProcessableObjectJpaRepository<
        T extends AbstractProcessableObject<ID, S>,
        ID extends Serializable,
        S extends ProcessableObjectStatus>
    extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

  /**
   * Finds the next batch of objects that are eligible for processing.
   *
   * <p>Eligibility is defined as:
   *
   * <ul>
   *   <li>the object is in one of the supplied {@code pendingStatuses},
   *   <li>{@code nextProcessed} is less than or equal to the supplied {@code now}, and
   *   <li>{@code processingSuspended} is {@code false} (i.e., processing for the object is not
   *       suspended).
   * </ul>
   *
   * <p>The query is executed with a pessimistic write lock so that multiple processing nodes can
   * safely compete for work. The exact locking semantics are database- and provider-specific (for
   * example, some platforms may translate this to {@code SELECT ... FOR UPDATE} or {@code SELECT
   * ... FOR UPDATE SKIP LOCKED} when appropriately configured).
   *
   * <p>The {@code pageable} parameter controls the page size and offset. The sort component of the
   * {@code pageable} is ignored because the query explicitly orders by {@code nextProcessed}.
   *
   * @param pendingStatuses the statuses that represent PENDING states in the state machine
   * @param now the current time used to compare against {@code nextProcessed}
   * @param pageable the paging information (typically first page with size {@code 1..N})
   * @return a list of eligible objects, possibly empty
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Transactional(readOnly = true)
  @Query(
      """
      select o
        from #{#entityName} o
       where o.status in :pendingStatuses
         and o.nextProcessed <= :now
         and o.processingSuspended = false
       order by o.nextProcessed
      """)
  List<T> findNextObjectsQueuedForProcessingForWrite(
      Collection<S> pendingStatuses, OffsetDateTime now, Pageable pageable);

  /**
   * Locks the specified object for processing by updating its status and lock metadata.
   *
   * <p>Typical usage is immediately after selecting a candidate object with {@link
   * #findNextObjectsQueuedForProcessingForWrite} in the same transaction, so that other processing
   * nodes cannot claim the same object.
   *
   * @param id the identifier of the object to lock
   * @param processingStatus the PROCESSING status to apply
   * @param lockName the logical name of the processing instance (for example, node or instance ID)
   * @param locked the timestamp when the lock is acquired
   * @param processingAttempts the new processing attempt count to persist
   * @return the number of rows updated (0 if the object could not be locked)
   */
  @Modifying
  @Transactional
  @Query(
      """
      update #{#entityName} o
         set o.status              = :processingStatus,
             o.lockName            = :lockName,
             o.locked              = :locked,
             o.processingAttempts  = :processingAttempts
       where o.id                  = :id
      """)
  int lockForProcessing(
      ID id, S processingStatus, String lockName, OffsetDateTime locked, int processingAttempts);

  /**
   * Resets stale PROCESSING locks back to a corresponding PENDING status.
   *
   * <p>This method is intended to support stale-lock recovery, for example, when a processing node
   * crashes while holding locks. It updates only those rows that:
   *
   * <ul>
   *   <li>are currently in the supplied {@code processingStatus},
   *   <li>have a non-null {@code locked} timestamp, and
   *   <li>were locked at or before the specified {@code lockCutoff} time.
   * </ul>
   *
   * <p>For all matching rows it:
   *
   * <ul>
   *   <li>sets {@code status} to {@code pendingStatus},
   *   <li>clears {@code lockName} and {@code locked} to release the execution lock, and
   *   <li>leaves scheduling fields such as {@code nextProcessed} unchanged so that the normal
   *       selection logic can pick the objects up again.
   * </ul>
   *
   * @param processingStatus the PROCESSING status currently held by stale objects
   * @param pendingStatus the PENDING status to apply when releasing stale locks
   * @param lockCutoff the latest allowed lock timestamp; any object locked at or before this time
   *     is considered stale
   * @return the number of rows updated
   */
  @Modifying
  @Transactional
  @Query(
      """
      update #{#entityName} o
         set o.status   = :pendingStatus,
             o.lockName = null,
             o.locked   = null
       where o.status   = :processingStatus
         and o.locked is not null
         and o.locked <= :lockCutoff
      """)
  int resetStaleLocks(S processingStatus, S pendingStatus, OffsetDateTime lockCutoff);

  /**
   * Updates the object's status and processing metadata when it is unlocked after a processing run.
   *
   * <p>This method is called after processing has completed (either successfully or
   * unsuccessfully). It:
   *
   * <ul>
   *   <li>updates {@code status} to {@code newStatus},
   *   <li>clears {@code lockName} and {@code locked} to release the execution lock,
   *   <li>increments the cumulative {@code processingTime} by the supplied value (in milliseconds),
   *   <li>sets {@code lastProcessed} to the time of the last processing attempt,
   *   <li>sets {@code nextProcessed} to the time when the object should next be considered for
   *       processing,
   *   <li>sets {@code processed} to the completion timestamp, or leaves it {@code null} if the
   *       object has not reached a terminal state (for example, a COMPLETED phase), and
   *   <li>sets {@code processingAttempts} to the supplied value (for example, the incremented
   *       attempt count or {@code 0} when resetting between processing phases).
   * </ul>
   *
   * @param id the identifier of the object being unlocked
   * @param newStatus the new status to apply
   * @param processingDuration the time taken to process the object in this attempt, in
   *     milliseconds; this value is added to the existing cumulative {@code processingTime}
   * @param lastProcessed the timestamp of the last processing attempt
   * @param nextProcessed the timestamp when the object should next be considered for processing
   * @param processed the completion timestamp, or {@code null} if the object has not completed
   * @param processingAttempts the value to persist for {@code processingAttempts}
   * @return the number of rows updated
   */
  @Modifying
  @Transactional
  @Query(
      """
      update #{#entityName} o
         set o.status             = :newStatus,
             o.lockName           = null,
             o.locked             = null,
             o.processingTime     = o.processingTime + :processingDuration,
             o.lastProcessed      = :lastProcessed,
             o.nextProcessed      = :nextProcessed,
             o.processed          = :processed,
             o.processingAttempts = :processingAttempts
       where o.id                 = :id
      """)
  int updateOnUnlock(
      ID id,
      S newStatus,
      long processingDuration,
      OffsetDateTime lastProcessed,
      OffsetDateTime nextProcessed,
      OffsetDateTime processed,
      Integer processingAttempts);
}

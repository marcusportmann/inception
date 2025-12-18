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

package digital.inception.processor.persistence.mongo;

import digital.inception.processor.AbstractProcessableObject;
import digital.inception.processor.ProcessableObjectStatus;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * Contract for MongoDB-based persistence operations used by the asynchronous/background processing
 * infrastructure for {@link AbstractProcessableObject} instances.
 *
 * <p>Implementations are typically backed by Spring Data's {@code MongoOperations} / {@code
 * MongoTemplate} and provide methods to:
 *
 * <ul>
 *   <li>find the next objects that are eligible for processing,
 *   <li>atomically claim and lock an object for processing,
 *   <li>reset stale PROCESSING locks back to a PENDING status, and
 *   <li>update status and processing metadata when an object is unlocked.
 * </ul>
 *
 * @param <T> the concrete processable object type
 * @param <ID> the identifier type for the processable object
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @author Marcus Portmann
 */
public interface ProcessableObjectMongoOperations<
    T extends AbstractProcessableObject<ID, S>,
    ID extends Serializable,
    S extends ProcessableObjectStatus> {

  /**
   * Finds the next batch of objects that are eligible for processing.
   *
   * <p>An object is considered eligible if:
   *
   * <ul>
   *   <li>its {@code status} is contained in {@code pendingStatuses}, and
   *   <li>its {@code nextProcessed} timestamp is less than or equal to {@code now}.
   * </ul>
   *
   * <p>The result is ordered by {@code nextProcessed} in ascending order and constrained by the
   * supplied {@link Pageable} (for example, the first page with size {@code 1} to retrieve a single
   * candidate).
   *
   * @param pendingStatuses the statuses that represent PENDING states
   * @param now the current time used to compare against {@code nextProcessed}
   * @param pageable paging information (page number, size, sort)
   * @return a list of eligible objects, possibly empty
   */
  List<T> findNextObjectsQueuedForProcessing(
      Collection<S> pendingStatuses, OffsetDateTime now, Pageable pageable);

  /**
   * Atomically locks the given object for processing using a MongoDB {@code findAndModify}
   * operation.
   *
   * <p>A typical implementation:
   *
   * <ul>
   *   <li>matches a document by:
   *       <ul>
   *         <li>{@code _id == id},
   *         <li>{@code status == expectedCurrentStatus}, and
   *         <li>{@code lockName == null} and {@code locked == null} (i.e., currently unlocked);
   *       </ul>
   *   <li>updates:
   *       <ul>
   *         <li>{@code status} to {@code processingStatus},
   *         <li>{@code lockName} and {@code locked},
   *         <li>{@code processingAttempts} to the supplied value;
   *       </ul>
   *   <li>and returns the updated document if the lock was acquired.
   * </ul>
   *
   * @param id the identifier of the object to lock
   * @param expectedCurrentStatus the expected current (typically PENDING) status
   * @param processingStatus the PROCESSING status to set when the lock is acquired
   * @param lockName the logical name of the processing instance acquiring the lock
   * @param locked the timestamp when the lock is acquired
   * @param processingAttempts the new processing-attempt count to persist
   * @return an {@link Optional} containing the updated object if the lock was acquired, or {@link
   *     Optional#empty()} if no matching unlocked document was found
   */
  Optional<T> lockForProcessing(
      ID id,
      S expectedCurrentStatus,
      S processingStatus,
      String lockName,
      OffsetDateTime locked,
      int processingAttempts);

  /**
   * Resets stale PROCESSING locks back to a corresponding PENDING status.
   *
   * <p>A lock is considered stale if:
   *
   * <ul>
   *   <li>the object is currently in {@code processingStatus}, and
   *   <li>its {@code locked} timestamp is less than or equal to {@code lockCutoff}.
   * </ul>
   *
   * <p>A typical implementation:
   *
   * <ul>
   *   <li>sets {@code status} to {@code pendingStatus}, and
   *   <li>clears {@code lockName} and {@code locked}.
   * </ul>
   *
   * @param processingStatus the PROCESSING status whose stale locks should be reset
   * @param pendingStatus the PENDING status to which stale-locked objects should be reverted
   * @param lockCutoff the latest allowed {@code locked} timestamp; any lock older than this is
   *     treated as stale
   * @return the number of documents that were updated
   */
  int resetStaleLocks(S processingStatus, S pendingStatus, OffsetDateTime lockCutoff);

  /**
   * Updates the object's status and processing metadata when it is unlocked after a processing
   * attempt (successful or failed).
   *
   * <p>A typical implementation:
   *
   * <ul>
   *   <li>sets {@code status} to {@code newStatus},
   *   <li>clears {@code lockName} and {@code locked},
   *   <li>increments {@code processingTime} by {@code processingDuration},
   *   <li>updates {@code lastProcessed}, {@code nextProcessed}, and {@code processed}, and
   *   <li>sets {@code processingAttempts} to the supplied value.
   * </ul>
   *
   * @param id the identifier of the object being unlocked
   * @param newStatus the new status to apply
   * @param processingDuration the duration, in milliseconds, of the processing attempt that just
   *     completed
   * @param lastProcessed the timestamp of the last processing attempt
   * @param nextProcessed the timestamp when processing should next be attempted, or {@code null} if
   *     no further processing is scheduled
   * @param processed the completion timestamp when processing finished successfully, or {@code
   *     null} if not applicable
   * @param processingAttempts the updated processing-attempt count
   * @return the number of documents that were updated
   */
  int updateOnUnlock(
      ID id,
      S newStatus,
      long processingDuration,
      OffsetDateTime lastProcessed,
      OffsetDateTime nextProcessed,
      OffsetDateTime processed,
      Integer processingAttempts);
}

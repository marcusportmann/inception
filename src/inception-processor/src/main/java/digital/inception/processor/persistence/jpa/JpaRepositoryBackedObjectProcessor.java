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

import digital.inception.core.util.ServiceUtil;
import digital.inception.processor.AbstractProcessableObject;
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessor;
import digital.inception.processor.ProcessableObjectStatus;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;

/**
 * Abstract {@link ObjectProcessor} implementation that uses a {@link
 * ProcessableObjectJpaRepository} to handle the standard persistence concerns for asynchronously
 * processed objects:
 *
 * <ul>
 *   <li>finding the next object due for processing using {@code nextProcessed},
 *   <li>locking the object for processing, and
 *   <li>unlocking the object while updating its status, {@code lastProcessed}, {@code
 *       nextProcessed}, {@code processed} timestamps, {@code processingAttempts}, and the
 *       cumulative processing time.
 * </ul>
 *
 * <p>Domain-specific logic such as:
 *
 * <ul>
 *   <li>how to perform the actual processing for a given state,
 *   <li>which statuses are treated as PENDING,
 *   <li>how to map PENDING → PROCESSING on claim,
 *   <li>how to determine retry and failure statuses, and
 *   <li>how to define and interpret the status type {@code S} and its transitions within the
 *       entity's state machine
 * </ul>
 *
 * <p>is delegated to abstract methods that subclasses must implement.
 *
 * @param <T> the concrete type of {@link AbstractProcessableObject} being processed
 * @param <ID> the identifier type for the processable object
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @param <R> the Spring Data repository type, which must extend {@link
 *     ProcessableObjectJpaRepository} for the same {@code T}, {@code ID} and {@code S}
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class JpaRepositoryBackedObjectProcessor<
        T extends AbstractProcessableObject<ID, S>,
        ID extends Serializable,
        S extends ProcessableObjectStatus,
        R extends ProcessableObjectJpaRepository<T, ID, S>>
    implements ObjectProcessor<T, S> {

  /** The logical name of this processing instance (e.g., node or instance identifier). */
  protected final String instanceName;

  /** The Spring Data repository used to query and update processable objects. */
  protected final R repository;

  /**
   * The maximum number of processing attempts that should be made for a single object before it is
   * treated as permanently failed.
   */
  private final int maxProcessingAttempts;

  /**
   * Constructs a new {@code JpaRepositoryBackedObjectProcessor}.
   *
   * @param repository the repository used to access and update processable objects
   * @param maxProcessingAttempts the maximum number of processing attempts that should be made for
   *     a single object before it is treated as permanently failed
   */
  protected JpaRepositoryBackedObjectProcessor(R repository, int maxProcessingAttempts) {
    this.repository = repository;
    this.instanceName = ServiceUtil.getServiceInstanceName(getClass().getSimpleName());
    this.maxProcessingAttempts = maxProcessingAttempts;
  }

  /**
   * Claims the next processable object from the underlying store.
   *
   * <p>The method:
   *
   * <ol>
   *   <li>Queries the repository for an object whose status is in {@link #getPendingStatuses} and
   *       whose {@code nextProcessed} is less than or equal to the current time.
   *   <li>Determines the PROCESSING status to apply using {@link
   *       #determineProcessingStatusOnClaim}.
   *   <li>Increments the processing-attempt counter.
   *   <li>Attempts to lock the object for processing via {@link
   *       ProcessableObjectJpaRepository#lockForProcessing}.
   * </ol>
   *
   * <p>If no eligible object is found, or if the lock attempt fails (for example, because another
   * node claimed the same object concurrently), this method returns {@link Optional#empty}.
   *
   * @return an {@link Optional} containing the claimed processable object, or {@link
   *     Optional#empty} if there is no pending object to process or the lock could not be acquired
   */
  @Override
  public Optional<T> claimNextProcessableObject() {

    OffsetDateTime now = OffsetDateTime.now();
    PageRequest pageRequest = PageRequest.of(0, 1);

    List<T> processableObjects =
        repository.findNextObjectsQueuedForProcessingForWrite(
            getPendingStatuses(), now, pageRequest);

    if (processableObjects.isEmpty()) {
      return Optional.empty();
    }

    T object = processableObjects.getFirst();

    S currentStatus = object.getStatus();
    S processingStatus = determineProcessingStatusOnClaim(object, currentStatus);

    // Update the database row to reflect the lock and processing status
    int updated =
        repository.lockForProcessing(
            object.getId(),
            processingStatus,
            instanceName,
            now,
            object.getProcessingAttempts() + 1);

    if (updated == 0) {
      // Another node may have claimed this object; treat as no work.
      return Optional.empty();
    }

    // Update in-memory instance so callers see the latest state
    applyClaimSideEffects(
        object, processingStatus, object.getProcessingAttempts() + 1, now, instanceName);

    return Optional.of(object);
  }

  /**
   * Returns the maximum number of processing attempts that should be made for a single object
   * before it is treated as permanently failed.
   *
   * @return the maximum number of processing attempts that should be made for a single object
   *     before it is treated as permanently failed
   */
  @Override
  public int getMaxProcessingAttempts() {
    return maxProcessingAttempts;
  }

  /**
   * Returns the set of statuses that should be treated as PENDING for this type of processable
   * object.
   *
   * <p>These are the statuses in which an object is eligible to be selected and claimed by {@link
   * #claimNextProcessableObject()}.
   *
   * @return the collection of statuses that represent pending states
   */
  @Override
  public abstract Collection<S> getPendingStatuses();

  /**
   * Updates the object's status and processing metadata and releases any execution lock after a
   * processing attempt has completed (whether successfully or unsuccessfully).
   *
   * <p>This implementation:
   *
   * <ol>
   *   <li>Computes {@code lastProcessed} as the current time.
   *   <li>Determines {@code processed} based on whether {@code newStatus} is in the {@link
   *       ProcessableObjectStatus.ProcessingPhase#COMPLETED COMPLETED} phase (in which case it is
   *       set to the current time; otherwise the existing value is preserved).
   *   <li>Determines the {@code processingAttempts} value to persist:
   *       <ul>
   *         <li>If {@code resetProcessingAttempts} is {@code true}, the attempt count is reset to
   *             {@code 0}.
   *         <li>If {@code resetProcessingAttempts} is {@code false}, the current attempt count is
   *             preserved (with {@code null} normalised to {@code 0}).
   *       </ul>
   *   <li>Delegates to {@link ProcessableObjectJpaRepository#updateOnUnlock(Serializable,
   *       ProcessableObjectStatus, long, OffsetDateTime, OffsetDateTime, OffsetDateTime, Integer)}
   *       to:
   *       <ul>
   *         <li>set {@code status} to {@code newStatus},
   *         <li>clear {@code lockName} and {@code locked} (releasing the execution lock),
   *         <li>increment the cumulative processing time by the supplied {@code processingDuration}
   *             (in milliseconds),
   *         <li>update {@code lastProcessed} and {@code nextProcessed}, and
   *         <li>set {@code processed} and {@code processingAttempts} as computed.
   *       </ul>
   *   <li>Updates the in-memory object to reflect the same changes (status, timestamps, lock
   *       fields, and {@code processingAttempts}).
   * </ol>
   *
   * <p>This method is invoked by the {@link BackgroundObjectProcessor} infrastructure both on
   * successful processing (using the status, duration, and next processing time returned from
   * {@link #process(AbstractProcessableObject)}) and on failure (using the status, duration, and
   * scheduling information determined by {@link #determineRetryHandling(AbstractProcessableObject,
   * ProcessableObjectStatus, Throwable)} or {@link
   * #determineFailureStatus(AbstractProcessableObject, ProcessableObjectStatus, Throwable)}).
   *
   * @param object the processable object whose status, timing metadata, and lock information must
   *     be updated
   * @param newStatus the new status to persist for the object
   * @param processingDuration the time taken to process the object in this attempt, in
   *     milliseconds; this value is added to the existing cumulative processing time for the object
   * @param nextProcessed the date and time the object should next be considered for processing, or
   *     {@code null} if the object should not be processed again automatically
   * @param resetProcessingAttempts whether the {@code processingAttempts} counter should be reset
   *     (for example, to {@code 0}) as part of unlocking
   */
  @Override
  public void unlockProcessableObject(
      T object,
      S newStatus,
      long processingDuration,
      OffsetDateTime nextProcessed,
      boolean resetProcessingAttempts) {

    OffsetDateTime now = OffsetDateTime.now();

    // Only set 'processed' when the object transitions into a COMPLETED phase.
    OffsetDateTime processedTimestamp =
        (newStatus.getProcessingPhase() == ProcessableObjectStatus.ProcessingPhase.COMPLETED)
            ? now
            : object.getProcessed();

    // Current attempts, normalize null → 0
    int attempts = object.getProcessingAttempts();

    // Apply reset policy based on the flag
    if (resetProcessingAttempts) {
      attempts = 0;
    }

    // Persist changes in the database, including cumulative processing time
    repository.updateOnUnlock(
        object.getId(),
        newStatus,
        processingDuration,
        now,
        nextProcessed,
        processedTimestamp,
        attempts);

    // Update in-memory state to match
    object.setLastProcessed(now);
    object.setNextProcessed(nextProcessed);
    object.setProcessed(processedTimestamp);
    object.setLockName(null);
    object.setLocked(null);
    object.setStatus(newStatus);
    object.setProcessingAttempts(attempts);
  }

  /**
   * Applies the side effects of a successful claim operation to the in-memory object, keeping it in
   * sync with the database row updated by {@link
   * ProcessableObjectJpaRepository#lockForProcessing(Serializable, ProcessableObjectStatus, String,
   * OffsetDateTime, int)}.
   *
   * <p>The default implementation:
   *
   * <ul>
   *   <li>sets the object's {@code status} to {@code processingStatus},
   *   <li>updates {@code processingAttempts} to the supplied value,
   *   <li>sets {@code locked} to the supplied timestamp, and
   *   <li>sets {@code lockName} to the supplied processing-instance name.
   * </ul>
   *
   * <p>Subclasses may override this method if additional side effects are required.
   *
   * @param object the claimed processable object
   * @param processingStatus the PROCESSING status applied to the object
   * @param processingAttempts the new value of {@code processingAttempts}
   * @param locked the timestamp when the object was locked
   * @param lockName the logical name of the processing instance
   */
  protected void applyClaimSideEffects(
      T object,
      S processingStatus,
      int processingAttempts,
      OffsetDateTime locked,
      String lockName) {

    object.setStatus(processingStatus);
    object.setProcessingAttempts(processingAttempts);
    object.setLocked(locked);
    object.setLockName(lockName);
  }

  /**
   * Determines the PROCESSING status that should be applied to the object when it is claimed for
   * processing.
   *
   * <p>Typical implementations map a PENDING status to its corresponding PROCESSING status. For
   * example:
   *
   * <pre>
   * REQUESTED            (PENDING)   → GENERATING           (PROCESSING)
   * GENERATION_INITIATED (PENDING)   → VERIFYING_GENERATION (PROCESSING)
   * </pre>
   *
   * @param object the processable object being claimed
   * @param currentStatus the status the object is currently in (typically a PENDING status)
   * @return the PROCESSING status that should be applied
   */
  protected abstract S determineProcessingStatusOnClaim(T object, S currentStatus);
}

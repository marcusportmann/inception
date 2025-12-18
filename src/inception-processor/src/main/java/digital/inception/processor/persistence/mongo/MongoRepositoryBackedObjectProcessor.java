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

import digital.inception.core.util.ServiceUtil;
import digital.inception.processor.AbstractProcessableObject;
import digital.inception.processor.ObjectProcessor;
import digital.inception.processor.ProcessableObjectStatus;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;

/**
 * Abstract {@link ObjectProcessor} implementation that uses a MongoDB-backed persistence layer via
 * {@link ProcessableObjectMongoOperations}.
 *
 * <p>This class encapsulates the generic persistence-related mechanics for asynchronously processed
 * objects:
 *
 * <ul>
 *   <li>finding the next object due for processing based on {@code nextProcessed} and status,
 *   <li>atomically claiming and locking an object for processing using a MongoDB {@code
 *       findAndModify} operation, and
 *   <li>unlocking the object after processing while updating:
 *       <ul>
 *         <li>its {@code status},
 *         <li>{@code lastProcessed}, {@code nextProcessed}, and {@code processed} timestamps,
 *         <li>{@code processingAttempts}, and
 *         <li>the cumulative processing time.
 *       </ul>
 * </ul>
 *
 * <p>Domain-specific concerns (for example, which statuses are considered PENDING, how to map from
 * a PENDING status to a PROCESSING status on claim, and the actual processing logic) are delegated
 * to abstract methods that subclasses must implement.
 *
 * @param <T> the concrete processable object type
 * @param <ID> the identifier type for the processable object
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @param <O> the MongoDB operations abstraction used to interact with the underlying collection,
 *     which must implement {@link ProcessableObjectMongoOperations} for the same {@code T}, {@code
 *     ID} and {@code S}
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class MongoRepositoryBackedObjectProcessor<
        T extends AbstractProcessableObject<ID, S>,
        ID extends Serializable,
        S extends ProcessableObjectStatus,
        O extends ProcessableObjectMongoOperations<T, ID, S>>
    implements ObjectProcessor<T, S> {

  /** Logical name of this processing instance (for example, node or instance identifier). */
  protected final String instanceName;

  /** MongoDB operations used to perform persistence-related tasks for the processable objects. */
  protected final O processableObjectMongoOperations;

  /** Maximum number of processing attempts before treating the object as permanently failed. */
  private final int maxProcessingAttempts;

  /**
   * Constructs a new {@code MongoRepositoryBackedObjectProcessor}.
   *
   * @param processableObjectMongoOperations the MongoDB operations abstraction used to persist and
   *     update processable objects
   * @param maxProcessingAttempts the maximum number of processing attempts allowed before an object
   *     is considered permanently failed
   */
  protected MongoRepositoryBackedObjectProcessor(
      O processableObjectMongoOperations, int maxProcessingAttempts) {

    this.processableObjectMongoOperations = processableObjectMongoOperations;
    this.instanceName = ServiceUtil.getServiceInstanceName(getClass().getSimpleName());
    this.maxProcessingAttempts = maxProcessingAttempts;
  }

  /**
   * Claims the next processable object that is due for processing.
   *
   * <p>Behaviour:
   *
   * <ol>
   *   <li>Fetches at most one candidate whose status is in {@link #getPendingStatuses} and whose
   *       {@code nextProcessed} is less than or equal to the current time.
   *   <li>Determines the PROCESSING status to apply on claim via {@link
   *       #determineProcessingStatusOnClaim}.
   *   <li>Attempts to atomically lock the candidate using {@link
   *       ProcessableObjectMongoOperations#lockForProcessing}.
   *   <li>If the lock succeeds, synchronizes the in-memory state with the persisted state via
   *       {@link #applyClaimSideEffects} and returns the locked object.
   *   <li>If the lock fails (for example, another instance claimed it first), the method loops and
   *       tries again until no further eligible candidates are found.
   * </ol>
   *
   * @return an {@link Optional} containing the claimed and locked object, or {@link
   *     Optional#empty()} if no eligible objects are currently available
   */
  @Override
  public Optional<T> claimNextProcessableObject() {

    PageRequest pageRequest = PageRequest.of(0, 1);

    while (true) {
      OffsetDateTime now = OffsetDateTime.now();

      // 1. Find a candidate (no locking yet)
      List<T> processableObjects =
          processableObjectMongoOperations.findNextObjectsQueuedForProcessing(
              getPendingStatuses(), now, pageRequest);

      if (processableObjects.isEmpty()) {
        // No more eligible objects at this moment
        return Optional.empty();
      }

      T candidate = processableObjects.getFirst();

      S currentStatus = candidate.getStatus();
      S processingStatus = determineProcessingStatusOnClaim(candidate, currentStatus);

      OffsetDateTime lockedTime = OffsetDateTime.now();

      // 2. Atomically claim and lock via findAndModify
      Optional<T> lockedOptional =
          processableObjectMongoOperations.lockForProcessing(
              candidate.getId(),
              currentStatus,
              processingStatus,
              instanceName,
              lockedTime,
              candidate.getProcessingAttempts() + 1);

      if (lockedOptional.isPresent()) {
        T locked = lockedOptional.get();

        // 3. Apply any additional in-memory side effects (if needed)
        applyClaimSideEffects(
            locked,
            processingStatus,
            candidate.getProcessingAttempts() + 1,
            lockedTime,
            instanceName);

        return Optional.of(locked);
      }

      // If we reach here, another instance likely claimed this candidate first.
      // Loop and try again: either another eligible object will be found,
      // or the query will eventually return empty, and we'll exit.
    }
  }

  /**
   * Returns the maximum number of processing attempts allowed for an object before it is treated as
   * a permanent failure.
   *
   * @return the maximum processing-attempt count
   */
  @Override
  public int getMaxProcessingAttempts() {
    return maxProcessingAttempts;
  }

  /**
   * Returns the collection of statuses that should be treated as PENDING for this type of
   * processable object.
   *
   * <p>Objects in one of these statuses and with a due {@code nextProcessed} timestamp are
   * considered eligible candidates for {@link #claimNextProcessableObject()}.
   *
   * @return the collection of PENDING statuses
   */
  @Override
  public abstract Collection<S> getPendingStatuses();

  /**
   * Updates the object's status and processing metadata in MongoDB and releases any execution lock
   * after a processing attempt has completed (successfully or unsuccessfully).
   *
   * <p>Behaviour:
   *
   * <ul>
   *   <li>Computes the effective {@code processed} timestamp if the new status is in the COMPLETED
   *       phase.
   *   <li>Optionally resets the {@code processingAttempts} counter if {@code
   *       resetProcessingAttempts} is {@code true}.
   *   <li>Persists the updated status, timestamps, attempt count, and cumulative processing time
   *       via {@link ProcessableObjectMongoOperations#updateOnUnlock}.
   *   <li>Synchronizes the in-memory representation of the object with the updated values and
   *       clears the lock information.
   * </ul>
   *
   * @param object the processable object being unlocked
   * @param newStatus the status to apply after processing
   * @param processingDuration the duration, in milliseconds, of the processing attempt
   * @param nextProcessed the time when the object should next be considered for processing, or
   *     {@code null} if no further processing is scheduled
   * @param resetProcessingAttempts whether the {@code processingAttempts} counter should be reset
   *     to {@code 0}
   */
  @Override
  public void unlockProcessableObject(
      T object,
      S newStatus,
      long processingDuration,
      OffsetDateTime nextProcessed,
      boolean resetProcessingAttempts) {

    OffsetDateTime now = OffsetDateTime.now();

    OffsetDateTime processedTimestamp =
        (newStatus.getProcessingPhase() == ProcessableObjectStatus.ProcessingPhase.COMPLETED)
            ? now
            : object.getProcessed();

    int attempts = object.getProcessingAttempts();

    if (resetProcessingAttempts) {
      attempts = 0;
    }

    processableObjectMongoOperations.updateOnUnlock(
        object.getId(),
        newStatus,
        processingDuration,
        now,
        nextProcessed,
        processedTimestamp,
        attempts);

    object.setLastProcessed(now);
    object.setNextProcessed(nextProcessed);
    object.setProcessed(processedTimestamp);
    object.setLockName(null);
    object.setLocked(null);
    object.setStatus(newStatus);
    object.setProcessingAttempts(attempts);
  }

  /**
   * Synchronizes the in-memory state of a claimed object with the MongoDB state after a successful
   * lock acquisition.
   *
   * <p>This method is invoked after {@link #claimNextProcessableObject()} has successfully locked
   * an object via {@link ProcessableObjectMongoOperations#lockForProcessing}.
   *
   * @param object the claimed object
   * @param processingStatus the PROCESSING status applied when the object was locked
   * @param processingAttempts the updated processing-attempt count
   * @param locked the timestamp when the lock was acquired
   * @param lockName the logical name of the processing instance that acquired the lock
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
   * <p>Implementations are typically based on a state machine that maps PENDING statuses to
   * corresponding PROCESSING statuses (for example, {@code QUEUED_FOR_SENDING -> SENDING}).
   *
   * @param object the object being claimed
   * @param currentStatus the current (usually PENDING) status of the object
   * @return the PROCESSING status to be set when the object is locked
   */
  protected abstract S determineProcessingStatusOnClaim(T object, S currentStatus);
}

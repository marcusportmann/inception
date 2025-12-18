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

package digital.inception.processor;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * The {@code ObjectProcessor} interface defines the contract for processing a specific type of
 * {@link AbstractProcessableObject} within its processing state machine.
 *
 * <p>An {@code ObjectProcessor} is responsible for:
 *
 * <ul>
 *   <li>Claiming the next processable object from the underlying store by transitioning it from a
 *       {@link ProcessableObjectStatus.ProcessingPhase#PENDING PENDING} status to a corresponding
 *       {@link ProcessableObjectStatus.ProcessingPhase#PROCESSING PROCESSING} status and
 *       establishing an execution lock.
 *   <li>Executing the domain-specific processing logic for the object in its current PROCESSING
 *       status and determining:
 *       <ul>
 *         <li>the next {@link ProcessableObjectStatus} value, and
 *         <li>the date and time when the object should next be considered for processing.
 *       </ul>
 *   <li>Deciding how to transition the object's status when processing fails, including:
 *       <ul>
 *         <li>which status to use for a retry when there are remaining attempts, and
 *         <li>which terminal failure status to use when no further automatic retries are allowed.
 *       </ul>
 *   <li>Persisting the final status, updating the next processing time and cumulative processing
 *       time, and releasing the lock once processing for a given run (successful or not) has
 *       completed.
 * </ul>
 *
 * <p>The {@link BackgroundObjectProcessor} infrastructure drives the overall lifecycle by:
 *
 * <ul>
 *   <li>calling {@link #claimNextProcessableObject} to obtain the next object to process,
 *   <li>invoking {@link #process} to execute the processing logic,
 *   <li>calling {@link #unlockProcessableObject} with:
 *       <ul>
 *         <li>the status and next processing time returned from {@code process},
 *         <li>the measured duration of the processing attempt (as {@code processingDuration}, in
 *             milliseconds), and
 *         <li>an implementation-specific decision for the {@code resetProcessingAttempts} flag,
 *       </ul>
 *       to update status, scheduling, cumulative processing time and lock metadata on success, and
 *   <li>delegating to {@link #determineRetryHandling} or {@link #determineFailureStatus} when
 *       exceptions occur.
 * </ul>
 *
 * @param <T> the concrete type of {@link AbstractProcessableObject} being processed
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface ObjectProcessor<
    T extends AbstractProcessableObject<?, S>, S extends ProcessableObjectStatus> {

  /**
   * Claims the next processable object from the underlying store.
   *
   * <p>An implementation of this interface must:
   *
   * <ol>
   *   <li>Query for an object whose status is in a set of PENDING states (i.e., statuses whose
   *       {@link ProcessableObjectStatus#getProcessingPhase} returns {@link
   *       ProcessableObjectStatus.ProcessingPhase#PENDING}).
   *   <li>Filter those objects to only include rows whose {@link
   *       AbstractProcessableObject#getNextProcessed()} is less than or equal to the current time
   *       (i.e. objects that are due for processing).
   *   <li>Select one such object using appropriate database-level locking semantics (for example,
   *       pessimistic locking such as {@code SELECT ... FOR UPDATE}, with optional {@code SKIP
   *       LOCKED} support depending on the database and JPA provider).
   *   <li>Update the object's status to an appropriate PROCESSING state and set lock metadata (for
   *       example, lock name, lock timestamp and processing-attempt counter), according to the
   *       implementation's locking strategy.
   *   <li>Return a detached or otherwise safe-to-use instance of the claimed object for further
   *       processing.
   * </ol>
   *
   * <p>If no suitable object is available, this method returns {@link Optional#empty()}.
   *
   * @return an {@link Optional} containing the claimed processable object, or {@link
   *     Optional#empty()} if there is no eligible object to process
   */
  Optional<T> claimNextProcessableObject();

  /**
   * Determines the status that should be applied to the object when processing fails, and no
   * further automatic attempts should be made.
   *
   * <p>This method is invoked by the background processing infrastructure when:
   *
   * <ul>
   *   <li>{@link #process(AbstractProcessableObject)} throws an exception, and
   *   <li>the object's current {@code processingAttempts} count is greater than or equal to {@link
   *       #getMaxProcessingAttempts()}.
   * </ul>
   *
   * <p>A common strategy is to transition to a terminal FAILED status (i.e., a status whose {@link
   * ProcessableObjectStatus#getProcessingPhase()} is {@link
   * ProcessableObjectStatus.ProcessingPhase#FAILED}). Alternative designs may transition into a
   * dedicated "manual intervention" state or similar terminal state.
   *
   * @param object the processable object that failed processing
   * @param currentStatus the status the object was in when the failure occurred
   * @param cause the exception that was thrown during processing
   * @return the terminal status that the object should transition to when no further retries are
   *     allowed
   */
  S determineFailureStatus(T object, S currentStatus, Throwable cause);

  /**
   * Determines how a failed processing attempt should be retried.
   *
   * <p>This method is invoked by the background processing infrastructure when:
   *
   * <ul>
   *   <li>{@link #process(AbstractProcessableObject)} throws an exception, and
   *   <li>the object's current {@code processingAttempts} count is strictly less than {@link
   *       #getMaxProcessingAttempts()}.
   * </ul>
   *
   * <p>The returned {@link RetryHandling} describes:
   *
   * <ul>
   *   <li>the status that the object should transition to in order to allow a retry, and
   *   <li>the {@link OffsetDateTime} when the object should next be considered for processing.
   * </ul>
   *
   * @param object the processable object that failed processing
   * @param currentStatus the status the object was in when the failure occurred
   * @param cause the exception that was thrown during processing
   * @return a {@link RetryHandling} describing how the retry must be handled
   */
  RetryHandling<S> determineRetryHandling(T object, S currentStatus, Throwable cause);

  /**
   * Returns the maximum number of processing attempts that should be made for a single object
   * before it is treated as permanently failed.
   *
   * <p>The background processing infrastructure compares this value with the object's {@code
   * processingAttempts} count to decide whether to:
   *
   * <ul>
   *   <li>retry processing using {@link #determineRetryHandling(AbstractProcessableObject,
   *       ProcessableObjectStatus, Throwable)}, or
   *   <li>transition to a terminal failure status using {@link
   *       #determineFailureStatus(AbstractProcessableObject, ProcessableObjectStatus, Throwable)}.
   * </ul>
   *
   * @return the maximum number of allowed processing attempts for a single object
   */
  int getMaxProcessingAttempts();

  /**
   * Returns the set of statuses that should be treated as PENDING for this type of processable
   * object.
   *
   * <p>These statuses represent the states in which an object is eligible to be selected and
   * claimed by {@link #claimNextProcessableObject()}, subject to its {@link
   * AbstractProcessableObject#getNextProcessed()} being due.
   *
   * @return the collection of statuses that represent pending states
   */
  Collection<S> getPendingStatuses();

  /**
   * Executes the domain-specific processing logic for the given object for its current PROCESSING
   * status and determines both the next status and when the object should next be considered for
   * processing.
   *
   * <p>This method is invoked by the background processing infrastructure once the object has been
   * claimed and placed into an appropriate PROCESSING state. The implementation should:
   *
   * <ul>
   *   <li>perform the required processing for the current state in the object's state machine,
   *   <li>decide which status the object should transition to next, and
   *   <li>decide the {@link OffsetDateTime} when the object should next be eligible for processing
   *       (or {@code null} if it should be immediately eligible).
   * </ul>
   *
   * <p>The returned {@link ObjectProcessingResult} encapsulates these two pieces of information and
   * is used by the {@link BackgroundObjectProcessor} when unlocking and scheduling the object for
   * subsequent processing.
   *
   * <p>Any exception thrown from this method will be treated as a processing failure, and the
   * background processing infrastructure will delegate to {@link
   * #determineRetryHandling(AbstractProcessableObject, ProcessableObjectStatus, Throwable)} or
   * {@link #determineFailureStatus(AbstractProcessableObject, ProcessableObjectStatus, Throwable)}
   * to determine the appropriate next status for failure handling.
   *
   * @param object the processable object to process
   * @return the {@link ObjectProcessingResult} containing the next status and the next processing
   *     time
   * @throws Exception if an error occurs during processing
   */
  ObjectProcessingResult<S> process(T object) throws Exception;

  /**
   * Updates the object's status and processing metadata and releases any execution lock after a
   * processing attempt has completed (whether successfully or unsuccessfully).
   *
   * <p>An implementation of this method is typically responsible for:
   *
   * <ul>
   *   <li>setting the object's {@code status} to the supplied {@code newStatus},
   *   <li>updating {@link AbstractProcessableObject#setLastProcessed(OffsetDateTime)} to the time
   *       of this processing attempt,
   *   <li>setting {@link AbstractProcessableObject#setNextProcessed(OffsetDateTime)} to {@code
   *       nextProcessed}, which determines when the object should next be considered for processing
   *       (or {@code null} if no further automatic processing is required),
   *   <li>incrementing the cumulative processing time by the supplied {@code processingDuration},
   *       representing the wall-clock time spent in this attempt,
   *   <li>setting {@link AbstractProcessableObject#setProcessed(OffsetDateTime)} when the object
   *       enters a terminal COMPLETED state (and typically leaving it unchanged otherwise),
   *   <li>clearing any lock-related fields (such as {@code locked} and {@code lockName}) to release
   *       the execution lock, and
   *   <li>updating the {@code processingAttempts} counter according to the {@code
   *       resetProcessingAttempts} flag (for example, resetting it to {@code 0} when a processing
   *       step completes and transitions back to a PENDING phase, or preserving the current value
   *       across retries or terminal outcomes).
   * </ul>
   *
   * <p>This method is invoked by the {@link BackgroundObjectProcessor} infrastructure both on
   * successful processing (using the status and next processing time returned from {@link
   * #process(AbstractProcessableObject)}) and on failure (using the status and scheduling
   * information determined by {@link #determineRetryHandling(AbstractProcessableObject,
   * ProcessableObjectStatus, Throwable)} or {@link
   * #determineFailureStatus(AbstractProcessableObject, ProcessableObjectStatus, Throwable)}).
   *
   * @param object the processable object whose status, timing metadata, and lock information must
   *     be updated
   * @param newStatus the new status to persist for the object
   * @param processingDuration the time taken to process the object in this attempt, in
   *     milliseconds; this value should be added to the existing cumulative processing time for the
   *     object
   * @param nextProcessed the date and time the object should next be considered for processing, or
   *     {@code null} if the object should not be processed again automatically
   * @param resetProcessingAttempts whether the {@code processingAttempts} counter should be reset
   *     (for example, to {@code 0}) as part of unlocking
   */
  void unlockProcessableObject(
      T object,
      S newStatus,
      long processingDuration,
      OffsetDateTime nextProcessed,
      boolean resetProcessingAttempts);
}

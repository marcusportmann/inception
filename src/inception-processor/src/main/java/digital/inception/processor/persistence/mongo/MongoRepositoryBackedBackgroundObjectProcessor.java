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
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessor;
import digital.inception.processor.ProcessableObjectStatus;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;

/**
 * Abstract {@link BackgroundObjectProcessor} implementation backed by MongoDB via {@link
 * ProcessableObjectMongoOperations}.
 *
 * <p>This class wires the generic background-processing infrastructure to a MongoDB persistence
 * layer and adds logic to:
 *
 * <ul>
 *   <li>access the Mongo-backed operations abstraction used to manage processable objects, and
 *   <li>periodically reset stale PROCESSING locks back to their corresponding PENDING statuses,
 *       based on a configurable lock timeout.
 * </ul>
 *
 * <p>Subclasses are responsible for:
 *
 * <ul>
 *   <li>providing the mapping from PENDING statuses to their corresponding PROCESSING statuses, and
 *   <li>supplying the appropriate {@link ObjectProcessor} and configuration values to the
 *       constructor.
 * </ul>
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
public abstract class MongoRepositoryBackedBackgroundObjectProcessor<
        T extends AbstractProcessableObject<ID, S>,
        ID extends Serializable,
        S extends ProcessableObjectStatus,
        O extends ProcessableObjectMongoOperations<T, ID, S>>
    extends BackgroundObjectProcessor<T, S> implements SmartLifecycle {

  /** MongoDB operations used to perform persistence-related tasks for the processable objects. */
  protected final O processableObjectMongoOperations;

  /**
   * Constructs a new {@code MongoRepositoryBackedBackgroundObjectProcessor}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param processableObjectMongoOperations the MongoDB operations abstraction used to persist and
   *     update processable objects
   * @param objectProcessor the domain-specific {@link ObjectProcessor} responsible for processing
   *     and status transitions
   * @param processingThreadCount the number of worker threads used by the background processor
   * @param maximumQueueLength the maximum number of tasks that may be queued for processing
   * @param lockTimeoutSeconds the maximum age (in seconds) for a PROCESSING lock before it is
   *     considered stale and eligible for reset
   * @param processingTimeoutMillis the base timeout (in milliseconds) used to derive the upper
   *     bound for shutting down the executor during {@link #stop()}
   */
  protected MongoRepositoryBackedBackgroundObjectProcessor(
      ApplicationContext applicationContext,
      O processableObjectMongoOperations,
      ObjectProcessor<T, S> objectProcessor,
      int processingThreadCount,
      int maximumQueueLength,
      long lockTimeoutSeconds,
      long processingTimeoutMillis) {

    super(
        applicationContext,
        objectProcessor,
        processingThreadCount,
        maximumQueueLength,
        lockTimeoutSeconds,
        processingTimeoutMillis);

    this.processableObjectMongoOperations = processableObjectMongoOperations;
  }

  /**
   * Returns the mapping from PENDING statuses to the PROCESSING statuses that are applied when an
   * object is claimed for processing.
   *
   * <p>Each entry represents:
   *
   * <ul>
   *   <li>key: a PENDING status, and
   *   <li>value: the corresponding PROCESSING status to which objects transition when claimed.
   * </ul>
   *
   * <p>This mapping is primarily used by the {@link ObjectProcessor} implementation for claim-time
   * transitions, but is also leveraged here (inverted) when resetting stale locks so that
   * PROCESSING statuses can be mapped back to their prior PENDING statuses.
   *
   * @return a map from PENDING statuses to their corresponding PROCESSING statuses
   */
  protected abstract Map<S, S> getPendingToProcessingStatusMappings();

  /**
   * Returns the MongoDB operations abstraction used by this processor.
   *
   * <p>The returned {@link ProcessableObjectMongoOperations} instance encapsulates all persistence
   * concerns required by the background-processing infrastructure, including:
   *
   * <ul>
   *   <li>locating the next objects that are eligible for processing based on status and {@code
   *       nextProcessed},
   *   <li>atomically claiming and locking objects for processing via a MongoDB {@code
   *       findAndModify} operation,
   *   <li>resetting stale PROCESSING locks back to their corresponding PENDING statuses, and
   *   <li>updating status and processing metadata (timestamps, attempt counters, cumulative
   *       processing time) when objects are unlocked after a processing run.
   * </ul>
   *
   * <p>Subclasses typically use this abstraction indirectly via higher-level operations such as
   * {@link digital.inception.processor.ObjectProcessor#claimNextProcessableObject()} and {@link
   * digital.inception.processor.BackgroundObjectProcessor#processObjects()}, but can also invoke it
   * directly if they need custom Mongo-specific behavior.
   *
   * @return the {@link ProcessableObjectMongoOperations} instance used for persistence operations
   */
  protected O getProcessableObjectMongoOperations() {
    return processableObjectMongoOperations;
  }

  /**
   * Resets any stale locks left over from previous processing runs by restoring affected objects
   * from PROCESSING statuses back to their configured PENDING statuses.
   *
   * <p>Behaviour:
   *
   * <ul>
   *   <li>If no mappings are defined or the timeout is non-positive, the method returns
   *       immediately.
   *   <li>Otherwise, computes a cutoff time as {@code now - lockTimeoutSeconds}.
   *   <li>For each PENDING → PROCESSING mapping, this method derives the reverse PROCESSING →
   *       PENDING relationship and invokes {@link ProcessableObjectMongoOperations#resetStaleLocks}
   *       to reset objects whose {@code locked} timestamp is older than the cutoff.
   * </ul>
   *
   * @param lockTimeoutSeconds the configured lock timeout in seconds; used to determine which locks
   *     are considered stale
   */
  @Override
  protected void resetStaleLocks(long lockTimeoutSeconds) {
    Map<S, S> pendingToProcessing = getPendingToProcessingStatusMappings();

    if (pendingToProcessing == null || pendingToProcessing.isEmpty()) {
      return;
    }

    if (lockTimeoutSeconds <= 0) {
      return;
    }

    OffsetDateTime cutoff = OffsetDateTime.now().minusSeconds(lockTimeoutSeconds);

    // Invert PENDING → PROCESSING to PROCESSING → PENDING for the reset logic.
    pendingToProcessing.forEach(
        (pendingStatus, processingStatus) -> {
          if (processingStatus != null && pendingStatus != null) {
            processableObjectMongoOperations.resetStaleLocks(
                processingStatus, pendingStatus, cutoff);
          }
        });
  }
}

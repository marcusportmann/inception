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
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessor;
import digital.inception.processor.ProcessableObjectStatus;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;

/**
 * Abstract {@link BackgroundObjectProcessor} implementation backed by a Spring Data JPA repository.
 *
 * <p>This processor uses a JPA-based persistence layer (via {@link ProcessableObjectJpaRepository})
 * to support background processing of {@link AbstractProcessableObject} instances. It wires the
 * generic background-processing infrastructure to a JPA repository and adds support for:
 *
 * <ul>
 *   <li>using a shared repository to load and update processable objects, and
 *   <li>resetting stale PROCESSING locks back to their corresponding PENDING statuses based on a
 *       configurable lock timeout.
 * </ul>
 *
 * <p>Concrete subclasses must:
 *
 * <ul>
 *   <li>define a Spring Data repository interface that extends {@code
 *       ProcessableObjectJpaRepository<T, ID, S>},
 *   <li>provide a mapping from PENDING statuses to their corresponding PROCESSING statuses via
 *       {@link #getPendingToProcessingStatusMappings}, and
 *   <li>use the injected repository for any additional queries that are specific to the processable
 *       object type.
 * </ul>
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
public abstract class JpaRepositoryBackedBackgroundObjectProcessor<
        T extends AbstractProcessableObject<ID, S>,
        ID extends Serializable,
        S extends ProcessableObjectStatus,
        R extends ProcessableObjectJpaRepository<T, ID, S>>
    extends BackgroundObjectProcessor<T, S> implements SmartLifecycle {

  /** The Spring Data repository used to manage the processable objects. */
  protected final R repository;

  /**
   * Constructs a new {@code JpaRepositoryBackedBackgroundObjectProcessor}.
   *
   * <p>This constructor wires together:
   *
   * <ul>
   *   <li>the {@link ObjectProcessor} responsible for domain-specific processing logic,
   *   <li>the Spring Data repository used to query and update processable objects, and
   *   <li>the thread-pool and lock-recovery configuration for the background processing
   *       infrastructure.
   * </ul>
   *
   * @param applicationContext the Spring {@link ApplicationContext} used for metrics and available
   *     to subclasses
   * @param repository the Spring Data repository for the processable objects
   * @param objectProcessor the {@link ObjectProcessor} that performs the actual processing and
   *     status transitions for objects of type {@code T}
   * @param processingThreadCount the fixed number of worker threads in the processing pool
   * @param maximumQueueLength the maximum number of objects to queue for processing if no object
   *     processing threads are available
   * @param lockTimeoutSeconds the maximum age, in seconds, that an object may remain in a
   *     PROCESSING state before its lock is considered stale and eligible for reset; a value {@code
   *     <= 0} disables periodic lock reset
   * @param processingTimeoutMillis a base timeout, in milliseconds, used together with the number
   *     of active and queued tasks to derive an upper bound when waiting for the executor to shut
   *     down (see {@link BackgroundObjectProcessor#stop})
   */
  protected JpaRepositoryBackedBackgroundObjectProcessor(
      ApplicationContext applicationContext,
      R repository,
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

    this.repository = repository;
  }

  /**
   * Provides the mapping from PENDING statuses to the PROCESSING statuses that are applied when an
   * object is claimed for processing.
   *
   * <p>For example, for an object whose status enumeration {@code S} includes:
   *
   * <ul>
   *   <li>{@code REQUESTED} (PENDING) → {@code GENERATING} (PROCESSING),
   *   <li>{@code QUEUED_FOR_PUBLISHING} (PENDING) → {@code PUBLISHING} (PROCESSING),
   *   <li>etc.
   * </ul>
   *
   * <p>This mapping is typically used by the {@link ObjectProcessor} implementation to determine
   * the PROCESSING status that should be set when an object is claimed. Here, it is also used in
   * reverse (PROCESSING → PENDING) when resetting stale locks, by inverting the mapping.
   *
   * @return a map where each key is a PENDING status and the corresponding value is the PROCESSING
   *     status that should be applied when an object is claimed for processing
   */
  protected abstract Map<S, S> getPendingToProcessingStatusMappings();

  /**
   * Returns the repository used by this processor.
   *
   * <p>Subclasses can use this repository for additional queries that are specific to the
   * processable object type beyond the generic behavior provided by {@link
   * ProcessableObjectJpaRepository}.
   *
   * @return the repository used by this processor
   */
  protected R getRepository() {
    return repository;
  }

  /**
   * Resets any stale locks left over from previous processing runs by restoring affected objects
   * from PROCESSING statuses back to their configured PENDING statuses.
   *
   * <p>This implementation uses the configured {@link #repository} together with the mappings
   * returned by {@link #getPendingToProcessingStatusMappings} to update objects that have remained
   * in PROCESSING statuses longer than the specified timeout. The PENDING → PROCESSING mapping is
   * inverted on the fly to determine the PROCESSING → PENDING relationship needed for lock reset.
   *
   * <p>For each PENDING → PROCESSING entry, it computes a lock cutoff timestamp from the current
   * time and the supplied {@code lockTimeoutSeconds}, and then calls {@link
   * ProcessableObjectJpaRepository#resetStaleLocks} with the PROCESSING status as the source and
   * the PENDING status as the target.
   *
   * <p>Objects that are reset in this way will have:
   *
   * <ul>
   *   <li>their {@code status} set back to the mapped PENDING status, and
   *   <li>{@code lockName} and {@code locked} cleared, while other scheduling fields such as {@code
   *       nextProcessed} remain unchanged.
   * </ul>
   *
   * <p>If the mapping is {@code null} or empty, this method performs no work. Subclasses can
   * override this method if more elaborate recovery logic is required, but the default behavior
   * should be sufficient for many use cases.
   *
   * @param lockTimeoutSeconds the maximum lock age, in seconds, before a PROCESSING lock is
   *     considered stale and eligible for reset
   */
  @Override
  protected void resetStaleLocks(long lockTimeoutSeconds) {
    Map<S, S> pendingToProcessing = getPendingToProcessingStatusMappings();

    if (pendingToProcessing == null || pendingToProcessing.isEmpty()) {
      return;
    }

    if (lockTimeoutSeconds <= 0) {
      // Nothing to do if timeout is disabled / non-positive.
      return;
    }

    OffsetDateTime cutoff = OffsetDateTime.now().minusSeconds(lockTimeoutSeconds);

    // Invert PENDING → PROCESSING to PROCESSING → PENDING for the reset logic.
    pendingToProcessing.forEach(
        (pendingStatus, processingStatus) -> {
          if (processingStatus != null && pendingStatus != null) {
            repository.resetStaleLocks(processingStatus, pendingStatus, cutoff);
          }
        });
  }
}

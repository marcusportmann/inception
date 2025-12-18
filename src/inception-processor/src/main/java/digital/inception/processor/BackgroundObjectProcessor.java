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

import digital.inception.processor.ProcessableObjectStatus.ProcessingPhase;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * The {@code BackgroundObjectProcessor} class provides generic infrastructure for asynchronously
 * processing {@link AbstractProcessableObject} instances using an {@link ObjectProcessor}.
 *
 * <p>It coordinates:
 *
 * <ul>
 *   <li>a thread pool for concurrent processing,
 *   <li>a work queue for pending processing tasks, and
 *   <li>the lifecycle around claiming, processing, unlocking, and recording processing metrics for
 *       processable objects.
 * </ul>
 *
 * <p>Concrete subclasses must:
 *
 * <ul>
 *   <li>configure scheduling (for example, via Spring's {@code @Scheduled}) to invoke {@link
 *       #processObjects} at the desired interval,
 *   <li>choose thread pool size, queue capacity and timeouts via the constructor, and
 *   <li>implement {@link #resetStaleLocks} to recover objects left in a PROCESSING state longer
 *       than a configured timeout.
 * </ul>
 *
 * <p>In addition, this base class provides {@link #triggerProcessing}, which can be called
 * (typically from application code that persists or updates a processable object) to asynchronously
 * trigger a call to {@link #processObjects}:
 *
 * <ul>
 *   <li>If called inside an active transaction, processing is triggered <strong>after the
 *       transaction commits</strong> (via {@link TransactionSynchronizationManager}).
 *   <li>If called outside a transaction, processing is triggered immediately.
 *   <li>All triggers are dispatched through a shared static {@link Executor} managed by this base
 *       class.
 * </ul>
 *
 * <p>The main processing flow is:
 *
 * <ol>
 *   <li>{@link #processObjects()} is invoked by a scheduler or via {@link #triggerProcessing}.
 *   <li>It periodically invokes {@link #resetStaleLocks}, passing the configured timeout so that
 *       subclasses can reset locks older than this age, but at most once per timeout interval.
 *   <li>It repeatedly calls {@link ObjectProcessor#claimNextProcessableObject} to obtain the next
 *       object that is currently in a PENDING status and whose {@link
 *       AbstractProcessableObject#getNextProcessed} is due.
 *   <li>For each claimed object, a {@link ProcessObjectRunnable} is submitted to the internal
 *       {@link ThreadPoolExecutor}.
 *   <li>The {@code ProcessObjectRunnable} invokes {@link
 *       ObjectProcessor#process(AbstractProcessableObject)} to execute the domain-specific logic
 *       and obtain the next status and next processing time.
 *   <li>On success, it measures the duration of the processing attempt and calls {@link
 *       #handleSuccess}, which in turn delegates to {@link ObjectProcessor#unlockProcessableObject}
 *       with:
 *       <ul>
 *         <li>the status returned from {@code process},
 *         <li>the measured duration (in milliseconds) for this attempt,
 *         <li>the next processing time returned from {@code process}, and
 *         <li>a flag indicating whether the {@code processingAttempts} counter should be reset (see
 *             {@link #handleSuccess(AbstractProcessableObject, ProcessableObjectStatus,
 *             ProcessableObjectStatus, OffsetDateTime, long)} for the default rule).
 *       </ul>
 *       After unlocking, {@link #afterSuccess(AbstractProcessableObject, ProcessableObjectStatus,
 *       ProcessableObjectStatus, long)} is invoked.
 *   <li>On failure (an exception from {@code process}), it measures the duration of the failed
 *       attempt and delegates to {@link #handleFailure(AbstractProcessableObject,
 *       ProcessableObjectStatus, Throwable, long)}, which:
 *       <ul>
 *         <li>inspects the object's processing attempt count,
 *         <li>uses {@link ObjectProcessor#determineRetryHandling(AbstractProcessableObject,
 *             ProcessableObjectStatus, Throwable)} to obtain retry status and scheduling
 *             information when further attempts are allowed, or {@link
 *             ObjectProcessor#determineFailureStatus(AbstractProcessableObject,
 *             ProcessableObjectStatus, Throwable)} when no further attempts are allowed,
 *         <li>invokes {@link ObjectProcessor#unlockProcessableObject(AbstractProcessableObject,
 *             ProcessableObjectStatus, long, OffsetDateTime, boolean)} with the chosen status, the
 *             measured duration, an appropriate next processing time (for retries or a terminal
 *             failure), and a decision on whether to reset {@code processingAttempts}, and
 *         <li>calls {@link #afterRetry(AbstractProcessableObject, ProcessableObjectStatus,
 *             ProcessableObjectStatus, Throwable)} or {@link
 *             #afterPermanentFailure(AbstractProcessableObject, ProcessableObjectStatus,
 *             ProcessableObjectStatus, Throwable)} accordingly.
 *       </ul>
 * </ol>
 *
 * <p>In addition to coordinating processing, this class can optionally bind Micrometer metrics via
 * {@link #bindMetrics(ApplicationContext)}, exposing gauges for queue and thread utilization and
 * counters/timers for successes, failures, retries and processing durations.
 *
 * @param <T> the concrete type of {@link AbstractProcessableObject} being processed
 * @param <S> the status type for the processable object, which must implement {@link
 *     ProcessableObjectStatus}
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused"})
public abstract class BackgroundObjectProcessor<
        T extends AbstractProcessableObject<?, S>, S extends ProcessableObjectStatus>
    implements SmartLifecycle {

  /**
   * Shared executor used to trigger calls to {@link #processObjects()} asynchronously.
   *
   * <p>This executor is shared across all {@code BackgroundObjectProcessor} instances in the JVM.
   * It is intentionally small, as the work it performs is limited to submitting a single {@link
   * #processObjects()} call per trigger.
   */
  private static final Executor TRIGGER_EXECUTOR = createTriggerExecutor();

  /** Flag preventing concurrent executions of {@link #processObjects()}. */
  protected final AtomicBoolean executing = new AtomicBoolean(false);

  /**
   * Maximum lock age, in seconds, before a PROCESSING lock is considered stale and eligible for
   * reset by {@link #resetStaleLocks(long)}.
   */
  protected final long lockTimeoutSeconds;

  /** The logger for the derived class. */
  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  /**
   * The {@link ObjectProcessor} responsible for the domain-specific processing and status
   * transitions of the processable object.
   */
  protected final ObjectProcessor<T, S> objectProcessor;

  /**
   * Base timeout, in milliseconds, used when waiting for the thread pool to terminate during {@link
   * #stop()}.
   *
   * <p>This value is multiplied by the number of active and queued tasks and compared against a
   * minimum bound to calculate an upper bound for shutting down the executor.
   */
  protected final long processingTimeoutMillis;

  /** Flag indicating whether the background processor is currently running. */
  protected final AtomicBoolean running = new AtomicBoolean(false);

  /** The number of {@link ProcessObjectRunnable} instances currently executing. */
  private final AtomicInteger activeProcessingCount = new AtomicInteger(0);

  /** The underlying thread pool used to process objects concurrently. */
  private final ThreadPoolExecutor executor;

  /** Last time (epoch ms) {@link #resetStaleLocks(long)} was invoked successfully. */
  private final AtomicLong lastLockResetTime = new AtomicLong(0L);

  /** Lock timeout in milliseconds (derived from {@link #lockTimeoutSeconds}). */
  private final long lockTimeoutMillis;

  /**
   * The number of processing tasks that have been submitted to the executor but have not yet
   * completed (i.e. tasks that are either queued for execution or currently running).
   */
  private final AtomicInteger processingCount = new AtomicInteger(0);

  /**
   * Telemetry for objects that are currently "in flight" (either queued or actively processing).
   *
   * <p>Entries are added when an object is claimed and queued, updated when a worker thread starts
   * processing, and removed once processing completes.
   */
  private final Map<Object, ObjectProcessingTelemetry> processingTelemetry;

  /** The queue backing the thread pool executor. */
  private final LinkedBlockingQueue<Runnable> queue;

  /** Total number of objects that ended in a permanent failure. */
  private Counter failureCounter;

  /** Total number of successfully processed objects. */
  private Counter processedCounter;

  /** Timer for successful processing durations. */
  private Timer processingTimer;

  /** Total number of retry schedules (i.e. failed attempts that will be retried). */
  private Counter retryCounter;

  /**
   * Constructs a new {@code BackgroundObjectProcessor}.
   *
   * <p>This constructor creates a bounded work queue and a thread pool whose fixed size is {@code
   * processingThreadCount}. All tasks submitted through {@link #processObjects()} run on this
   * executor.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param objectProcessor the {@link ObjectProcessor} that will perform the actual processing and
   *     status transitions for objects of type {@code T}
   * @param processingThreadCount the fixed number of worker threads in the processing pool
   * @param maximumQueueLength the maximum number of objects to queue for processing if no object
   *     processing threads are available
   * @param lockTimeoutSeconds the maximum age, in seconds, that an object may remain in a
   *     PROCESSING state before its lock is considered stale and eligible for reset by {@link
   *     #resetStaleLocks(long)}; a value &lt;= 0 disables periodic lock reset
   * @param processingTimeoutMillis a base timeout, in milliseconds, used together with the number
   *     of active and queued tasks to derive an upper bound when waiting for the executor to shut
   *     down in {@link #stop()}
   */
  protected BackgroundObjectProcessor(
      ApplicationContext applicationContext,
      ObjectProcessor<T, S> objectProcessor,
      int processingThreadCount,
      int maximumQueueLength,
      long lockTimeoutSeconds,
      long processingTimeoutMillis) {

    this.objectProcessor = objectProcessor;
    this.processingTimeoutMillis = processingTimeoutMillis;
    this.lockTimeoutSeconds = lockTimeoutSeconds;
    this.lockTimeoutMillis = lockTimeoutSeconds > 0 ? lockTimeoutSeconds * 1_000L : 0L;

    this.queue = new LinkedBlockingQueue<>(maximumQueueLength);

    // NOTE: We set the core pool size to the maximum number of threads because the
    // ThreadPoolExecutor will not grow the number of threads if the queue is not full.
    this.executor =
        new ThreadPoolExecutor(
            processingThreadCount,
            processingThreadCount,
            0,
            TimeUnit.MILLISECONDS,
            this.queue,
            new ProcessingThreadFactory());

    this.executor.allowCoreThreadTimeOut(false);

    bindMetrics(applicationContext);

    this.processingTelemetry = initProcessingTelemetry(applicationContext);
  }

  /**
   * Returns the {@link ObjectProcessor} responsible for the domain-specific processing and status
   * transitions of the processable object.
   *
   * @return the {@link ObjectProcessor} responsible for the domain-specific processing and status
   *     transitions of the processable object
   */
  public ObjectProcessor<T, S> getObjectProcessor() {
    return objectProcessor;
  }

  /**
   * Returns a read-only view of the current object processing telemetry.
   *
   * <p>Intended for monitoring and testing purposes.
   *
   * @return a read-only view of the current object processing telemetry
   */
  public Map<Object, ObjectProcessingTelemetry> getProcessingTelemetry() {
    return Map.copyOf(processingTelemetry);
  }

  /**
   * Returns whether there are any objects currently queued for processing or actively being
   * processed by this {@code BackgroundObjectProcessor}.
   *
   * <p>An object is considered "in flight" if it has been submitted to the internal executor but
   * has not yet completed, i.e. it is either:
   *
   * <ul>
   *   <li>waiting in the executor's work queue, or
   *   <li>currently running in one of the worker threads.
   * </ul>
   *
   * <p>This method is a lightweight check backed by an internal counter ({@code processingCount})
   * that is incremented when a task is submitted and decremented when it finishes. It is intended
   * primarily for tests and monitoring code that needs to wait until the processor becomes
   * completely idle (no queued or active tasks).
   *
   * @return {@code true} if at least one object is queued or actively being processed; {@code
   *     false} if the processor is currently idle
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean hasQueuedOrActiveObjects() {
    return processingCount.get() > 0;
  }

  /**
   * Returns whether this {@code BackgroundObjectProcessor} is currently running.
   *
   * @return {@code true} if the processor is running; {@code false} if it is stopped or has not yet
   *     started
   */
  @Override
  public boolean isRunning() {
    return running.get();
  }

  /**
   * Claims eligible processable objects and submits them to the internal thread pool for
   * asynchronous processing.
   *
   * <p>On each invocation this method:
   *
   * <ol>
   *   <li>Returns immediately with {@code 0} if the processor is not currently running (see {@link
   *       #isRunning()}).
   *   <li>Ensures that only a single call to {@code processObjects()} executes at a time by
   *       acquiring an internal guard ({@link #executing}); concurrent invocations that find
   *       another call already in progress return {@code 0} immediately.
   *   <li>Invokes a periodic stale-lock check via {@link #resetStaleLocks(long)} (through {@link
   *       #maybeResetStaleLocks()}), at most once per {@link #lockTimeoutSeconds} interval when a
   *       positive lock timeout is configured.
   *   <li>In a loop, while the processor is running:
   *       <ul>
   *         <li>Stops if the internal work queue is full (no remaining capacity).
   *         <li>Calls {@link ObjectProcessor#claimNextProcessableObject()} to obtain the next
   *             eligible object (typically one whose status is PENDING and whose {@code
   *             nextProcessed} is due).
   *         <li>Breaks the loop when no more eligible objects are available (empty {@link
   *             Optional}).
   *         <li>For each claimed object:
   *             <ul>
   *               <li>Increments an internal counter of in-flight tasks,
   *               <li>Submits a new {@link ProcessObjectRunnable} wrapping the object to the
   *                   internal {@link ThreadPoolExecutor} for asynchronous processing, and
   *               <li>Increments a local counter of how many objects were successfully submitted in
   *                   this call.
   *             </ul>
   *       </ul>
   *   <li>Releases the execution guard so that subsequent calls to {@code processObjects()} may
   *       proceed.
   * </ol>
   *
   * <p>Note that this method only <em>submits</em> work to the executor; it does not wait for the
   * submitted tasks to complete. Completion, status transitions, and retry/failure handling are
   * managed inside {@link ProcessObjectRunnable} and the associated {@link ObjectProcessor}
   * callbacks.
   *
   * @return the number of objects that were successfully claimed and submitted for processing
   *     during this invocation (which may be {@code 0} if no work was available, the processor was
   *     not running, the queue was full, or another call was already in progress)
   */
  public int processObjects() {
    if (!running.get()) {
      return 0;
    }

    // Prevent multiple concurrent executions of processObjects()
    if (!executing.compareAndSet(false, true)) {
      return 0;
    }

    // Periodically reset stale locks, at most once per lockTimeoutSeconds interval.
    maybeResetStaleLocks();

    int processedCount = 0;

    try {
      while (running.get()) {
        if (queue.remainingCapacity() == 0) {
          break;
        }

        Optional<T> optional = objectProcessor.claimNextProcessableObject();

        if (optional.isEmpty()) {
          break;
        }

        T object = optional.get();

        // Initialize the object processing telemetry
        ObjectProcessingTelemetry objectProcessingTelemetry =
            new ObjectProcessingTelemetry(object.getIdAsKey(), getClass().getSimpleName());

        objectProcessingTelemetry.setClaimed(object.getLocked());
        objectProcessingTelemetry.setProcessingStatus(object.getStatus());

        int attemptsBefore = object.getProcessingAttempts();
        objectProcessingTelemetry.setProcessingAttempts(attemptsBefore);

        processingTelemetry.put(object.getId(), objectProcessingTelemetry);

        processingCount.incrementAndGet();
        executor.execute(new ProcessObjectRunnable(object, objectProcessingTelemetry));
        processedCount++;
      }
    } finally {
      executing.set(false);
    }

    return processedCount;
  }

  @Override
  public void start() {
    running.compareAndSet(false, true);
  }

  /**
   * Stops the background processor and initiates a graceful shutdown of the underlying thread pool.
   *
   * <p>If the processor is currently marked as running, this method:
   *
   * <ol>
   *   <li>Atomically flips the {@link #running} flag from {@code true} to {@code false},
   *   <li>Computes an upper-bound shutdown timeout based on:
   *       <ul>
   *         <li>the current number of active worker threads ({@link
   *             ThreadPoolExecutor#getActiveCount()}),
   *         <li>the current queue size ({@link ThreadPoolExecutor#getQueue()}), and
   *         <li>the configured {@link #processingTimeoutMillis} (used as a per-task base),
   *       </ul>
   *       and enforces a minimum timeout of five minutes,
   *   <li>Calls {@link ThreadPoolExecutor#shutdown()} to reject new tasks while allowing
   *       already-submitted tasks to complete, and
   *   <li>Blocks in {@link ThreadPoolExecutor#awaitTermination(long, TimeUnit)} for at most the
   *       calculated timeout.
   * </ol>
   *
   * <p>If the executor does not terminate within the calculated timeout, it is left in a shutdown
   * state, and any remaining tasks are left for JVM shutdown to clean up; no further action (such
   * as {@code shutdownNow()}) is attempted by this method.
   *
   * <p>If the calling thread is interrupted while waiting for termination, the interrupt flag is
   * restored and the method returns without guaranteeing that all tasks have completed.
   *
   * <p>This method is part of the {@link SmartLifecycle} contract and is invoked by the Spring
   * container during application shutdown.
   */
  @Override
  public void stop() {
    if (running.compareAndSet(true, false)) {
      int active = executor.getActiveCount();
      int queued = executor.getQueue().size();

      long estimatedTimeout = Math.max(5 * 60_000L, (active + queued) * processingTimeoutMillis);

      executor.shutdown();
      try {
        //noinspection ResultOfMethodCallIgnored
        executor.awaitTermination(estimatedTimeout, TimeUnit.MILLISECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * Convenience method to trigger processing of eligible objects asynchronously.
   *
   * <p>This method is typically called by application code that has just persisted or updated a
   * processable object and wishes to "poke" the background processor so that new work can be picked
   * up promptly, rather than waiting for the next scheduled invocation of {@link
   * #processObjects()}.
   *
   * <p>Behaviour:
   *
   * <ul>
   *   <li>If the processor is not currently running (i.e. {@link #isRunning()} is {@code false}),
   *       the trigger is ignored and no work is scheduled.
   *   <li>If the method is invoked within an active Spring transaction ({@link
   *       TransactionSynchronizationManager #isActualTransactionActive()} returns {@code true}), a
   *       {@link TransactionSynchronization} is registered and the trigger is executed
   *       <strong>after the transaction successfully commits</strong>. This ensures that {@link
   *       #processObjects()} only sees changes that have been durably persisted.
   *   <li>If no transaction is active, the trigger is submitted immediately.
   *   <li>In both cases the actual execution is delegated to the shared static {@link
   *       #TRIGGER_EXECUTOR}, which submits a single call to {@link #processObjects()} on a
   *       background thread.
   * </ul>
   *
   * <p>This method does not itself perform any processing work and returns immediately after
   * scheduling (or skipping) the trigger.
   */
  public void triggerProcessing() {
    if (!running.get()) {
      return;
    }

    Runnable task = this::processObjects;

    if (TransactionSynchronizationManager.isActualTransactionActive()) {
      TransactionSynchronizationManager.registerSynchronization(
          new TransactionSynchronization() {
            @Override
            public void afterCommit() {
              submitTrigger(task);
            }
          });
    } else {
      submitTrigger(task);
    }
  }

  /**
   * Hook method invoked after an object has failed processing and has been transitioned to a
   * terminal failure status.
   *
   * <p>The default implementation is a no-op. Subclasses may override this method to perform
   * additional actions such as:
   *
   * <ul>
   *   <li>logging the failure with domain-specific context,
   *   <li>emitting metrics or alerts for monitoring,
   *   <li>notifying external systems or operators, or
   *   <li>recording audit information.
   * </ul>
   *
   * <p>This hook is invoked <strong>after</strong> the object has been unlocked and its terminal
   * failure status, cumulative processing time, and scheduling information have been persisted by
   * {@link ObjectProcessor#unlockProcessableObject(AbstractProcessableObject,
   * ProcessableObjectStatus, long, OffsetDateTime, boolean)}.
   *
   * @param object the processable object that has permanently failed
   * @param fromStatus the status the object was in when the failure occurred
   * @param toStatus the terminal failure status chosen by the {@link ObjectProcessor}
   * @param cause the exception that caused the failure (if any)
   */
  protected void afterPermanentFailure(T object, S fromStatus, S toStatus, Throwable cause) {
    // no-op by default
  }

  /**
   * Hook method invoked after an object has failed processing but has been transitioned to a
   * retryable status.
   *
   * <p>The default implementation is a no-op. Subclasses may override this method to perform
   * additional actions such as:
   *
   * <ul>
   *   <li>logging the retry with domain-specific context,
   *   <li>emitting metrics (for example, retry counters),
   *   <li>raising warnings in monitoring systems, or
   *   <li>enriching audit or diagnostic traces.
   * </ul>
   *
   * <p>This hook is invoked <strong>after</strong> the object has been unlocked and its retry
   * status, cumulative processing time, and scheduling information have been persisted by {@link
   * ObjectProcessor#unlockProcessableObject(AbstractProcessableObject, ProcessableObjectStatus,
   * long, OffsetDateTime, boolean)}.
   *
   * @param object the processable object that will be retried
   * @param fromStatus the status the object was in when the failure occurred
   * @param toStatus the status the object transitioned to in order to allow a retry
   * @param cause the exception that triggered the retry (if any)
   */
  protected void afterRetry(T object, S fromStatus, S toStatus, Throwable cause) {
    // no-op by default
  }

  /**
   * Hook method invoked after an object has been processed successfully and unlocked.
   *
   * <p>The default implementation is a no-op. Subclasses may override this method to perform
   * additional actions such as:
   *
   * <ul>
   *   <li>recording metrics (for example, incrementing success counters or updating processing-time
   *       histograms),
   *   <li>domain-specific logging or audit trail updates,
   *   <li>notifying external systems or emitting events, or
   *   <li>updating monitoring dashboards.
   * </ul>
   *
   * <p>This hook is invoked <strong>after</strong> the object has been unlocked and its status,
   * cumulative processing time for the attempt, and scheduling information have been persisted via
   * {@link ObjectProcessor#unlockProcessableObject(AbstractProcessableObject,
   * ProcessableObjectStatus, long, OffsetDateTime, boolean)}.
   *
   * @param object the successfully processed object
   * @param fromStatus the status the object was in when processing started
   * @param toStatus the status the object transitioned to on success
   * @param processingDuration the time taken to process the object in this attempt, in milliseconds
   */
  protected void afterSuccess(T object, S fromStatus, S toStatus, long processingDuration) {
    // no-op by default
  }

  /**
   * Returns the underlying thread pool used to process objects concurrently.
   *
   * @return the underlying thread pool used to process objects concurrently
   */
  protected ThreadPoolExecutor getExecutor() {
    return executor;
  }

  /**
   * Returns the queue backing the thread pool executor.
   *
   * @return the queue backing the thread pool executor
   */
  protected LinkedBlockingQueue<Runnable> getQueue() {
    return queue;
  }

  /**
   * Handles a failed processing run for a single object.
   *
   * <p>This method decides whether the failure should result in a retry or a permanent failure
   * based on:
   *
   * <ul>
   *   <li>the object's current {@code processingAttempts} count, and
   *   <li>the value returned by {@link ObjectProcessor#getMaxProcessingAttempts()}.
   * </ul>
   *
   * <p>Behaviour:
   *
   * <ul>
   *   <li>If the current attempt count is strictly less than {@code
   *       objectProcessor.getMaxProcessingAttempts()}:
   *       <ol>
   *         <li>{@link ObjectProcessor#determineRetryHandling(AbstractProcessableObject,
   *             ProcessableObjectStatus, Throwable)} is invoked to obtain a {@link RetryHandling}
   *             that specifies both the retry status and the next processing time.
   *         <li>{@link ObjectProcessor#unlockProcessableObject(AbstractProcessableObject,
   *             ProcessableObjectStatus, long, OffsetDateTime, boolean)} is called with the retry
   *             status, the measured {@code processingDuration} for this failed attempt, the {@code
   *             nextProcessed} value from the {@code RetryHandling}, and {@code false} for the
   *             reset flag so that {@code processingAttempts} remains cumulative across retries.
   *         <li>{@link #afterRetry(AbstractProcessableObject, ProcessableObjectStatus,
   *             ProcessableObjectStatus, Throwable)} is invoked as an extension hook.
   *       </ol>
   *   <li>If the current attempt count is greater than or equal to {@code
   *       objectProcessor.getMaxProcessingAttempts()}:
   *       <ol>
   *         <li>{@link ObjectProcessor#determineFailureStatus(AbstractProcessableObject,
   *             ProcessableObjectStatus, Throwable)} is invoked to obtain a terminal failure
   *             status.
   *         <li>{@code unlockProcessableObject} is called with that status, the measured {@code
   *             processingDuration} for this final attempt, a {@code null} {@code nextProcessed}
   *             (indicating no further automatic processing), and {@code false} for the reset flag.
   *         <li>{@link #afterPermanentFailure(AbstractProcessableObject, ProcessableObjectStatus,
   *             ProcessableObjectStatus, Throwable)} is invoked as an extension hook.
   *       </ol>
   * </ul>
   *
   * <p>Subclasses may override this method to alter the failure-handling strategy, for example, to
   * apply custom backoff policies, additional logging, or alternative retry limits. When
   * overriding, callers may optionally delegate to {@code super.handleFailure(...)} to retain the
   * default behaviour and augment it with custom logic.
   *
   * @param object the processable object that failed processing
   * @param fromStatus the status the object was in when the failure occurred
   * @param cause the exception thrown during processing
   * @param processingDuration the time taken to process the object in this failed attempt, in
   *     milliseconds
   */
  protected void handleFailure(T object, S fromStatus, Throwable cause, long processingDuration) {
    if (object.getProcessingAttempts() < objectProcessor.getMaxProcessingAttempts()) {
      // Retry path
      RetryHandling<S> retryHandling =
          objectProcessor.determineRetryHandling(object, fromStatus, cause);

      S toStatus = retryHandling.retryStatus();
      OffsetDateTime nextProcessed = retryHandling.nextProcessed();

      objectProcessor.unlockProcessableObject(
          object, toStatus, processingDuration, nextProcessed, false);

      // Record retry metric
      if (retryCounter != null) {
        retryCounter.increment();
      }

      afterRetry(object, fromStatus, toStatus, cause);
    } else {
      // Permanent failure path.
      S toStatus = objectProcessor.determineFailureStatus(object, fromStatus, cause);
      objectProcessor.unlockProcessableObject(object, toStatus, processingDuration, null, false);

      // Record failure metric
      if (failureCounter != null) {
        failureCounter.increment();
      }

      afterPermanentFailure(object, fromStatus, toStatus, cause);
    }
  }

  /**
   * Handles a successful processing run for a single object.
   *
   * <p>The default implementation performs three key tasks:
   *
   * <ol>
   *   <li>Determines whether the object's {@code processingAttempts} counter should be reset based
   *       on the transition from {@code fromStatus} to {@code toStatus}. By default, the counter is
   *       reset when:
   *       <ul>
   *         <li>the status changes ({@code fromStatus != toStatus}),
   *         <li>{@code fromStatus} is in the {@link
   *             ProcessableObjectStatus.ProcessingPhase#PROCESSING PROCESSING} phase, and
   *         <li>{@code toStatus} is in the {@link ProcessableObjectStatus.ProcessingPhase#PENDING
   *             PENDING} phase.
   *       </ul>
   *       This ensures that attempts counted for one processing step do not "bleed into" a
   *       subsequent step in the state machine.
   *   <li>Invokes {@link ObjectProcessor#unlockProcessableObject(AbstractProcessableObject,
   *       ProcessableObjectStatus, long, OffsetDateTime, boolean)} with:
   *       <ul>
   *         <li>{@code toStatus} as the new status,
   *         <li>{@code processingDuration} as the measured time spent in this successful attempt,
   *         <li>{@code nextProcessed} as the time when the object should next be considered for
   *             processing (which may be {@code null} to signal no further scheduled processing),
   *             and
   *         <li>the computed {@code resetProcessingAttempts} flag.
   *       </ul>
   *   <li>Records success-related metrics (if Micrometer metrics have been bound) by:
   *       <ul>
   *         <li>incrementing the total-success counter, and
   *         <li>recording {@code processingDuration} in the processing {@link Timer}, when
   *             available.
   *       </ul>
   *   <li>Calls {@link #afterSuccess(AbstractProcessableObject, ProcessableObjectStatus,
   *       ProcessableObjectStatus, long)} as an extension hook for subclasses to perform additional
   *       actions (for example, logging, domain-specific metrics, or notifications).
   * </ol>
   *
   * <p>Subclasses can override this method to customise how successful outcomes are handled, for
   * example, by:
   *
   * <ul>
   *   <li>changing the conditions under which {@code processingAttempts} is reset,
   *   <li>applying additional status transitions, or
   *   <li>recording additional domain-specific information.
   * </ul>
   *
   * <p>When overriding, subclasses may optionally delegate to {@code super.handleSuccess(...)} to
   * retain the default behaviour while augmenting it with custom logic.
   *
   * @param object the successfully processed object
   * @param fromStatus the status the object was in when processing started
   * @param toStatus the status returned from {@link
   *     ObjectProcessor#process(AbstractProcessableObject)}
   * @param nextProcessed the date and time when the object should next be considered for
   *     processing, or {@code null} if no further processing is required
   * @param processingDuration the time taken to process the object in this successful attempt, in
   *     milliseconds
   */
  protected void handleSuccess(
      T object, S fromStatus, S toStatus, OffsetDateTime nextProcessed, long processingDuration) {

    boolean resetProcessingAttempts =
        fromStatus != toStatus
            && fromStatus.getProcessingPhase() == ProcessingPhase.PROCESSING
            && toStatus.getProcessingPhase() == ProcessingPhase.PENDING;

    objectProcessor.unlockProcessableObject(
        object, toStatus, processingDuration, nextProcessed, resetProcessingAttempts);

    // Record "success" metrics
    if (processedCounter != null) {
      processedCounter.increment();
    }
    if (processingTimer != null && processingDuration >= 0) {
      processingTimer.record(processingDuration, TimeUnit.MILLISECONDS);
    }

    afterSuccess(object, fromStatus, toStatus, processingDuration);
  }

  /**
   * Hook method invoked with a fully populated telemetry snapshot after a processing attempt has
   * completed and just before the telemetry is removed from the processing telemetry map.
   *
   * <p>The default implementation is a no-op. Subclasses may override this to:
   *
   * <ul>
   *   <li>log per-object telemetry,
   *   <li>publish events,
   *   <li>send traces to an APM system,
   *   <li>or enrich domain-specific diagnostics.
   * </ul>
   *
   * @param objectProcessingTelemetry the telemetry snapshot for the completed processing attempt
   */
  protected void reportObjectProcessingTelemetry(
      ObjectProcessingTelemetry objectProcessingTelemetry) {
    // no-op by default
  }

  /**
   * Resets any stale PROCESSING locks that have exceeded the configured timeout.
   *
   * <p>Implementations should use the supplied {@code lockTimeoutSeconds} to identify objects that
   * have been in a {@link ProcessableObjectStatus.ProcessingPhase#PROCESSING PROCESSING} state for
   * longer than the allowed age and transition them back to an appropriate PENDING (or otherwise
   * recoverable) status so they can be picked up again by {@link #processObjects()}.
   *
   * <p>This method is invoked periodically from {@link #processObjects()} via {@link
   * #maybeResetStaleLocks()}, at most once per {@code lockTimeoutSeconds} interval when the timeout
   * is greater than zero. Any exceptions thrown from this method are caught and logged so that
   * lock-recovery failures do not prevent further processing.
   *
   * <p>Typical responsibilities of an implementation include:
   *
   * <ul>
   *   <li>Locating objects that:
   *       <ul>
   *         <li>are currently in a PROCESSING status, and
   *         <li>have a {@code locked} timestamp older than {@code now - lockTimeoutSeconds}.
   *       </ul>
   *   <li>Updating those objects to:
   *       <ul>
   *         <li>a suitable PENDING status,
   *         <li>clear {@code lockName} and {@code locked} fields, and
   *         <li>leave scheduling fields such as {@code nextProcessed} in a consistent state.
   *       </ul>
   * </ul>
   *
   * <p>Subclasses are free to choose how they implement the recovery (for example, using a JPA
   * repository, direct SQL, or another persistence mechanism), but the operation should be
   * idempotent and safe to invoke repeatedly.
   *
   * @param lockTimeoutSeconds the maximum age, in seconds, that a PROCESSING lock may be held
   *     before it is considered stale and eligible for reset
   */
  protected abstract void resetStaleLocks(long lockTimeoutSeconds);

  private static Executor createTriggerExecutor() {
    ThreadPoolExecutor executor =
        new ThreadPoolExecutor(
            1, 5, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>(100), new TriggerThreadFactory());
    executor.allowCoreThreadTimeOut(true);
    return executor;
  }

  /**
   * Binds Micrometer metrics for this processor if Micrometer and a {@link MeterRegistry} are
   * available in the Spring {@link ApplicationContext}.
   *
   * <p>Metric names follow the pattern:
   *
   * <pre>
   * inception.processor.&lt;DerivedClassSimpleName&gt;.processingCount
   * inception.processor.&lt;DerivedClassSimpleName&gt;.threads.active
   * inception.processor.&lt;DerivedClassSimpleName&gt;.queue.size
   * inception.processor.&lt;DerivedClassSimpleName&gt;.queue.remainingCapacity
   * inception.processor.&lt;DerivedClassSimpleName&gt;.processed.total
   * inception.processor.&lt;DerivedClassSimpleName&gt;.failures.total
   * inception.processor.&lt;DerivedClassSimpleName&gt;.retries.total
   * inception.processor.&lt;DerivedClassSimpleName&gt;.processing.duration
   * </pre>
   *
   * <p>The last four metrics are registered as Micrometer {@link Counter}s and a {@link Timer}
   * respectively. Time-windowed views such as "failures in the last 5 minutes" or "average
   * processing time over the last 5 minutes" are derived by the metrics backend (for example, using
   * {@code rate} / {@code increase} functions in Prometheus).
   *
   * @param applicationContext the Spring {@link ApplicationContext} used to look up the
   *     MeterRegistry
   */
  private void bindMetrics(ApplicationContext applicationContext) {
    String processorName = getClass().getSimpleName();
    String prefix = "inception.processor." + processorName;

    try {
      Class.forName("io.micrometer.core.instrument.MeterRegistry");
    } catch (ClassNotFoundException e) {
      log.info(
          "Micrometer not found, metrics will be disabled for BackgroundObjectProcessor ({})",
          processorName);
      return;
    }

    try {
      MeterRegistry meterRegistry = applicationContext.getBean(MeterRegistry.class);

      // In-flight tasks (submitted but not yet completed)
      Gauge.builder(prefix + ".processingCount", processingCount, AtomicInteger::get)
          .description("Number of objects being processed that have not yet completed")
          .register(meterRegistry);

      // Active worker threads
      Gauge.builder(prefix + ".threads.active", executor, ThreadPoolExecutor::getActiveCount)
          .description("Number of worker threads currently processing objects")
          .register(meterRegistry);

      // Queue size
      Gauge.builder(prefix + ".queue.size", queue, LinkedBlockingQueue::size)
          .description("Number of tasks currently queued for processing")
          .register(meterRegistry);

      // Queue remaining capacity
      Gauge.builder(
              prefix + ".queue.remainingCapacity", queue, LinkedBlockingQueue::remainingCapacity)
          .description("Remaining capacity in the processing queue")
          .register(meterRegistry);

      this.processedCounter =
          Counter.builder(prefix + ".processed.total")
              .description("Total number of successfully processed objects")
              .register(meterRegistry);

      this.failureCounter =
          Counter.builder(prefix + ".failures.total")
              .description("Total number of objects that ended in permanent failure")
              .register(meterRegistry);

      this.retryCounter =
          Counter.builder(prefix + ".retries.total")
              .description("Total number of processing attempts that scheduled a retry")
              .register(meterRegistry);

      this.processingTimer =
          Timer.builder(prefix + ".processing.duration")
              .description("Processing duration for successfully processed objects")
              // Optional: enable histogram / percentiles for richer analysis
              .publishPercentileHistogram()
              .publishPercentiles(0.5, 0.95, 0.99)
              .register(meterRegistry);

    } catch (NoSuchBeanDefinitionException ignored) {
      // No MeterRegistry bean found, metrics will not be available
    } catch (Throwable e) {
      log.error("Failed to bind metrics for the BackgroundObjectProcessor ({})", processorName, e);
    }
  }

  private Map<Object, ObjectProcessingTelemetry> initProcessingTelemetry(
      ApplicationContext applicationContext) {
    try {
      CacheManager cacheManager = applicationContext.getBean(CacheManager.class);

      String cacheName = "processingTelemetry." + getClass().getSimpleName();
      Cache cache = cacheManager.getCache(cacheName);

      if (cache != null) {
        Object nativeCache = cache.getNativeCache();

        if (nativeCache instanceof Map<?, ?> nativeMap) {
          @SuppressWarnings("unchecked")
          Map<Object, ObjectProcessingTelemetry> map =
              (Map<Object, ObjectProcessingTelemetry>) nativeMap;

          log.info(
              "Bound processingTelemetry for {} to cache '{}' (native type: {})",
              getClass().getSimpleName(),
              cacheName,
              nativeCache.getClass().getName());

          return map;
        }
      }
    } catch (NoSuchBeanDefinitionException ex) {
      // No CacheManager in the context
    } catch (Throwable e) {
      log.error(
          "Failed to bind the processingTelemetry cache for {}", getClass().getSimpleName(), e);
    }

    log.info(
        "Using local ConcurrentHashMap for processingTelemetry for {}", getClass().getSimpleName());

    return new ConcurrentHashMap<>();
  }

  private void maybeResetStaleLocks() {
    if (lockTimeoutMillis <= 0) {
      return;
    }

    long now = System.currentTimeMillis();
    long last = lastLockResetTime.get();

    if (now - last < lockTimeoutMillis) {
      return;
    }

    if (!lastLockResetTime.compareAndSet(last, now)) {
      return;
    }

    try {
      resetStaleLocks(lockTimeoutSeconds);
    } catch (Throwable ex) {
      log.error(
          "Failed to reset stale locks for BackgroundObjectProcessor ({})",
          getClass().getSimpleName(),
          ex);
    }
  }

  private void submitTrigger(Runnable task) {
    try {
      TRIGGER_EXECUTOR.execute(task);
    } catch (RejectedExecutionException ex) {
      // Optional: log at debug/trace level if desired
    }
  }

  private static class ProcessingThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    @Override
    public Thread newThread(@Nonnull Runnable runnable) {
      Thread thread = new Thread(runnable, "object-processing-" + threadNumber.getAndIncrement());
      thread.setDaemon(true);
      return thread;
    }
  }

  private static class TriggerThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    @Override
    public Thread newThread(@Nonnull Runnable runnable) {
      Thread thread =
          new Thread(runnable, "object-processing-trigger-" + threadNumber.getAndIncrement());
      thread.setDaemon(true);
      return thread;
    }
  }

  private class ProcessObjectRunnable implements Runnable {

    private final T object;

    private final ObjectProcessingTelemetry objectProcessingTelemetry;

    ProcessObjectRunnable(T object, ObjectProcessingTelemetry objectProcessingTelemetry) {
      this.object = object;
      this.objectProcessingTelemetry = objectProcessingTelemetry;
    }

    @Override
    public void run() {
      activeProcessingCount.incrementAndGet();
      try {
        S fromStatus = object.getStatus();

        // Mark the start of processing
        OffsetDateTime processingStarted = OffsetDateTime.now();
        objectProcessingTelemetry.setProcessingAttemptStarted(processingStarted);

        long start = System.currentTimeMillis();

        Throwable failure = null;

        S toStatus;

        OffsetDateTime nextProcessed;

        try {
          ObjectProcessingResult<S> result = objectProcessor.process(object);
          long duration = System.currentTimeMillis() - start;

          toStatus = result.nextStatus();
          nextProcessed = result.nextProcessed();

          handleSuccess(object, fromStatus, result.nextStatus(), result.nextProcessed(), duration);

          objectProcessingTelemetry.setFailureCause(null);
        } catch (Throwable e) {
          long duration = System.currentTimeMillis() - start;

          handleFailure(object, fromStatus, e, duration);

          toStatus = object.getStatus();
          nextProcessed = object.getNextProcessed();

          objectProcessingTelemetry.setFailureCause(e);
        }

        // Processing completed, update telemetry from the final object state
        objectProcessingTelemetry.setProcessingAttemptCompleted(OffsetDateTime.now());

        long duration = System.currentTimeMillis() - start;
        objectProcessingTelemetry.setProcessingAttemptDuration(duration);

        objectProcessingTelemetry.setStatusAfterProcessing(toStatus);
        objectProcessingTelemetry.setNextProcessed(nextProcessed);
        objectProcessingTelemetry.setProcessed(object.getProcessed());

        // Report object processing telemetry
        reportObjectProcessingTelemetry(objectProcessingTelemetry);
      } finally {
        activeProcessingCount.decrementAndGet();
        processingCount.decrementAndGet();

        // Remove object processing telemetry
        if (object.getId() != null) {
          processingTelemetry.remove(object.getId());
        }
      }
    }
  }
}

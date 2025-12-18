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

package digital.inception.operations.service;

import digital.inception.operations.model.Event;
import digital.inception.operations.model.EventStatus;
import digital.inception.operations.persistence.jpa.EventRepository;
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessingResult;
import digital.inception.processor.RetryHandling;
import digital.inception.processor.persistence.jpa.JpaRepositoryBackedBackgroundObjectProcessor;
import digital.inception.processor.persistence.jpa.JpaRepositoryBackedObjectProcessor;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The {@code BackgroundEventProcessor} class.
 *
 * @author Marcus Portmann
 */
@Component
public class BackgroundEventProcessor
    extends JpaRepositoryBackedBackgroundObjectProcessor<
        Event, UUID, EventStatus, EventRepository> {

  /**
   * Constructs a new {@code BackgroundEventProcessor}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param eventService the Event Service
   * @param eventRepository the Event Repository
   * @param processingThreadCount the fixed number of worker threads in the event processing pool
   * @param maximumQueueLength the maximum number of events to queue for processing if no event
   *     processing threads are available
   * @param lockTimeoutSeconds the maximum age, in seconds, that an event may remain in a PROCESSING
   *     state before its lock is considered stale and eligible for reset; a value &lt;= 0 disables
   *     periodic lock reset
   * @param processingTimeoutMillis a base timeout, in milliseconds, used together with the number
   *     of active and queued tasks to derive an upper bound when waiting for the executor to shut
   *     down (see {@link BackgroundObjectProcessor#stop()})
   * @param maximumProcessingAttempts the maximum number of times to attempt processing an event
   * @param retryDelayMillis the base delay, in milliseconds, between event processing retries
   */
  public BackgroundEventProcessor(
      ApplicationContext applicationContext,
      EventService eventService,
      EventRepository eventRepository,
      @Value("${ceb-svc-operations.event-processing.processing-thread-count:#{5}}")
          int processingThreadCount,
      @Value("${ceb-svc-operations.event-processing.max-queue-length:#{10}}")
          int maximumQueueLength,
      @Value("${ceb-svc-operations.event-processing.lock-timeout:#{600}}") long lockTimeoutSeconds,
      @Value("${ceb-svc-operations.event-processing.processing-timeout:#{60000L}}")
          long processingTimeoutMillis,
      @Value("${ceb-svc-operations.event-processing.max-processing-attempts:#{100}}")
          int maximumProcessingAttempts,
      @Value("${ceb-svc-operations.event-processing.retry-delay:#{10000}}") long retryDelayMillis) {
    super(
        applicationContext,
        eventRepository,
        new EventProcessor(
            eventService, eventRepository, maximumProcessingAttempts, retryDelayMillis),
        processingThreadCount,
        maximumQueueLength,
        lockTimeoutSeconds,
        processingTimeoutMillis);
  }

  /**
   * Process the events.
   *
   * @return the number of processed events
   */
  @Scheduled(cron = "0 * * * * *")
  public int processEvents() {
    return processObjects();
  }

  @Override
  protected Map<EventStatus, EventStatus> getPendingToProcessingStatusMappings() {
    return Map.of(EventStatus.QUEUED, EventStatus.PROCESSING);
  }

  private static class EventProcessor
      extends JpaRepositoryBackedObjectProcessor<Event, UUID, EventStatus, EventRepository> {

    private final EventService eventService;

    /** The base delay between event processing retries, in milliseconds. */
    private final long retryDelayMillis;

    public EventProcessor(
        EventService eventService,
        EventRepository eventRepository,
        int maximumEventProcessingAttempts,
        long retryDelayMillis) {
      super(eventRepository, maximumEventProcessingAttempts);

      this.eventService = eventService;
      this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public EventStatus determineFailureStatus(
        Event event, EventStatus currentStatus, Throwable cause) {
      return EventStatus.FAILED;
    }

    @Override
    public RetryHandling<EventStatus> determineRetryHandling(
        Event event, EventStatus currentStatus, Throwable cause) {
      return RetryHandling.delayedWithBackoff(
          EventStatus.QUEUED, event.getProcessingAttempts(), retryDelayMillis, ChronoUnit.MILLIS);
    }

    @Override
    public Collection<EventStatus> getPendingStatuses() {
      return List.of(EventStatus.QUEUED);
    }

    @Override
    public ObjectProcessingResult<EventStatus> process(Event event) throws Exception {
      //noinspection SwitchStatementWithTooFewBranches
      return switch (event.getStatus()) {
        case PROCESSING -> {
          eventService.processEvent(event);

          yield new ObjectProcessingResult<>(EventStatus.PROCESSED);
        }
        default ->
            throw new RuntimeException(
                "Invalid processing status for event (" + event.getStatus() + ")");
      };
    }

    @Override
    protected EventStatus determineProcessingStatusOnClaim(Event event, EventStatus currentStatus) {
      return EventStatus.PROCESSING;
    }
  }
}

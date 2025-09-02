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
import digital.inception.security.service.SecurityService;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The {@code BackgroundEventProcessorImpl} class implements the Background Event Processor.
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public class BackgroundEventProcessorImpl implements BackgroundEventProcessor, SmartLifecycle {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(BackgroundEventProcessorImpl.class);

  /** The Event Service. */
  private final EventService eventService;

  /** Is the Background Event Processor executing? */
  private final AtomicBoolean executing = new AtomicBoolean(false);

  /** Is the Background Event Processor running. */
  private final AtomicBoolean running = new AtomicBoolean(false);

  /** The Security Service. */
  private final SecurityService securityService;

  private LinkedBlockingQueue<Runnable> eventProcessingQueue;

  /** The number of minutes an idle event processing thread should be kept alive. */
  @Value("${inception.operations.event-processing-thread-keep-alive:#{5}}")
  private int eventProcessingThreadKeepAlive;

  /** The number of milliseconds to wait for the processing of an event to complete. */
  @Value("${inception.operations.event-processing-timeout:#{60000L}}")
  private long eventProcessingTimeout;

  /** The executor responsible for processing events. */
  private ThreadPoolExecutor eventProcessor;

  /** The number of event processing threads to start initially. */
  @Value("${inception.operations.initial-event-processing-threads:#{1}}")
  private int initialEventProcessingThreads;

  /**
   * The maximum number of events to queue for processing if no event processing threads are
   * available.
   */
  @Value("${inception.operations.maximum-event-processing-queue-length:#{50}}")
  private int maximumEventProcessingQueueLength;

  /** The maximum number of event processing threads to create to execute tasks. */
  @Value("${inception.operations.maximum-event-processing-threads:#{10}}")
  private int maximumEventProcessingThreads;

  /**
   * Constructs a new {@code BackgroundEventProcessorImpl}.
   *
   * @param eventService the Event Service
   * @param securityService the Security Service
   */
  public BackgroundEventProcessorImpl(EventService eventService, SecurityService securityService) {
    this.eventService = eventService;
    this.securityService = securityService;
  }

  /** Initialize the Background Event Processor. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Background Event Processor");

    if (eventService != null) {
      // Initialize the event processor
      eventProcessingQueue = new LinkedBlockingQueue<>(maximumEventProcessingQueueLength);

      // NOTE: We set the initial number of threads to the maximum number of threads because
      //       the implementation of the thread pool executor will never increase the number of
      //       threads if the queue is not full.
      //
      // https://medium.com/@ankithahjpgowda/policies-of-threadpoolexecutor-in-java-75f22fd6f637
      this.eventProcessor =
          new ThreadPoolExecutor(
              maximumEventProcessingThreads,
              maximumEventProcessingThreads,
              eventProcessingThreadKeepAlive,
              TimeUnit.MINUTES,
              eventProcessingQueue);

      // Reset any locks for events that were previously being processed
      try {
        for (UUID tenantId : securityService.getTenantIds()) {
          log.info(
              "Resetting the locks for the events being processed for the tenant ("
                  + tenantId
                  + ")");

          eventService.resetEventLocks(tenantId, EventStatus.PROCESSING, EventStatus.QUEUED);
        }
      } catch (Throwable e) {
        log.error("Failed to reset the locks for the events being processed", e);
      }
    } else {
      log.error(
          "Failed to initialize the Background Event Processor: "
              + "The Event Service was NOT injected");
    }
  }

  @Override
  public boolean isRunning() {
    return running.get() || eventProcessor.isTerminating();
  }

  /**
   * Process the events.
   *
   * @return the number of processed events
   */
  @Scheduled(cron = "0 * * * * *")
  public int processEvents() {
    int numberOfProcessedEvents = 0;

    if (!executing.compareAndSet(false, true)) {
      return 0;
    }

    try {
      for (UUID tenantId : securityService.getTenantIds()) {
        numberOfProcessedEvents += processEventsForTenant(tenantId);
      }
    } catch (Throwable e) {
      log.error("Failed to process the events", e);
    } finally {
      executing.set(false);
    }

    return numberOfProcessedEvents;
  }

  @Override
  public void start() {
    if (running.compareAndSet(false, true)) {
      log.info("Background Event Processor started");
    }
  }

  @Override
  public void stop() {
    long terminationTimeout =
        Math.max(
            5 * 60000L,
            (eventProcessor.getActiveCount() + eventProcessor.getQueue().size())
                * eventProcessingTimeout);

    if (running.compareAndSet(true, false)) {
      log.info(
          "Shutting down the Background Event Processor with "
              + eventProcessor.getActiveCount()
              + " events currently being processed and "
              + eventProcessor.getQueue().size()
              + " events queued for processing (Timeout is "
              + terminationTimeout
              + " milliseconds)");

      try {
        eventProcessor.shutdown();

        if (eventProcessor.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS)) {
          log.info("Successfully shutdown the Background Event Processor");
        } else {
          log.warn("Failed to cleanly shutdown the Background Event Processor");
        }
      } catch (InterruptedException e) {
        log.warn("The shutdown of the Background Event Processor was interrupted");
      }
    }
  }

  private int processEventsForTenant(UUID tenantId) {

    int numberOfProcessedEventsForTenant = 0;

    while (running.get()) {
      // Retrieve the next event queued for processing
      try {
        if (eventProcessor.getQueue().remainingCapacity() == 0) {
          if (log.isDebugEnabled()) {
            log.debug(
                "The maximum number of events queued for processing has been reached ("
                    + maximumEventProcessingQueueLength
                    + ")");
          }
        }

        Optional<Event> eventOptional = eventService.getNextEventQueuedForProcessing(tenantId);

        if (eventOptional.isEmpty()) {
          return numberOfProcessedEventsForTenant;
        } else {
          eventProcessor.execute(new EventProcessor(eventService, eventOptional.get()));

          numberOfProcessedEventsForTenant++;
        }
      } catch (Throwable e) {
        log.error("Failed to retrieve the next event queued for processing", e);
      }
    }

    return numberOfProcessedEventsForTenant;
  }

  /**
   * The {@code EventProcessor} class.
   *
   * @author Marcus Portmann
   */
  public static class EventProcessor implements Runnable {

    /** The event. */
    private final Event event;

    /** The Event Service. */
    private final EventService eventService;

    /**
     * Constructs a new {@code EventProcessor}.
     *
     * @param eventService the Event Service
     * @param event the event
     */
    public EventProcessor(EventService eventService, Event event) {
      this.eventService = eventService;
      this.event = event;
    }

    @Override
    public void run() {
      try {
        if (log.isDebugEnabled()) {
          log.debug(
              "Processing the event (%s) for the tenant (%s)"
                  .formatted(event.getId(), event.getTenantId()));
        }

        long startTime = System.currentTimeMillis();

        eventService.processEvent(event);

        long finishTime = System.currentTimeMillis();

        eventService.unlockEvent(event.getTenantId(), event.getId(), EventStatus.PROCESSED);
      } catch (Throwable e) {
        log.error(
            "Failed to process the event (%s) for the tenant (%s)"
                .formatted(event.getId(), event.getTenantId()),
            e);

        try {
          /*
           * If the event has exceeded the maximum number of processing attempts then
           * unlock it and set its status to "Available" otherwise unlock it and set its status to
           * "Received" to attempt processing again.
           */
          if (event.getProcessingAttempts() >= eventService.getMaximumEventProcessingAttempts()) {
            log.warn(
                "The event (%s) for the tenant (%s) has exceeded the maximum number of processing attempts and will be marked as AVAILABLE so it can be manually processed"
                    .formatted(event.getId(), event.getTenantId()));

            eventService.unlockEvent(event.getTenantId(), event.getId(), EventStatus.FAILED);
          } else {
            eventService.unlockEvent(event.getTenantId(), event.getId(), EventStatus.QUEUED);
          }
        } catch (Throwable f) {
          log.error(
              "Failed to unlock and set the status for the event (%s) for the tenant (%s)"
                  .formatted(event.getId(), event.getTenantId()),
              f);
        }
      }
    }
  }
}

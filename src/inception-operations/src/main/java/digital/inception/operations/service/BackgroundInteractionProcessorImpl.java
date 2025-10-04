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

import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionProcessingResult;
import digital.inception.operations.model.InteractionStatus;
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
 * The {@code BackgroundInteractionProcessorImpl} class implements the Background Interaction
 * Processor.
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public class BackgroundInteractionProcessorImpl
    implements BackgroundInteractionProcessor, SmartLifecycle {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(BackgroundInteractionProcessorImpl.class);

  /** Is the Background Interaction Processor executing? */
  private final AtomicBoolean executing = new AtomicBoolean(false);

  /** The Interaction Service. */
  private final InteractionService interactionService;

  /** Is the Background Interaction Processor running. */
  private final AtomicBoolean running = new AtomicBoolean(false);

  /** The Security Service. */
  private final SecurityService securityService;

  /** The number of interaction processing threads to start initially. */
  @Value("${inception.operations.initial-interaction-processing-threads:#{1}}")
  private int initialInteractionProcessingThreads;

  private LinkedBlockingQueue<Runnable> interactionProcessingQueue;

  /** The number of minutes an idle interaction processing thread should be kept alive. */
  @Value("${inception.operations.interaction-processing-thread-keep-alive:#{5}}")
  private int interactionProcessingThreadKeepAlive;

  /** The number of milliseconds to wait for the processing of an interaction to complete. */
  @Value("${inception.operations.interaction-processing-timeout:#{60000L}}")
  private long interactionProcessingTimeout;

  /** The executor responsible for processing interactions. */
  private ThreadPoolExecutor interactionProcessor;

  /**
   * The maximum number of interactions to queue for processing if no interaction processing threads
   * are available.
   */
  @Value("${inception.operations.max-interaction-processing-queue-length:#{50}}")
  private int maximumInteractionProcessingQueueLength;

  /** The maximum number of interaction processing threads to create to execute tasks. */
  @Value("${inception.operations.max-interaction-processing-threads:#{10}}")
  private int maximumInteractionProcessingThreads;

  /**
   * Constructs a new {@code BackgroundInteractionProcessorImpl}.
   *
   * @param interactionService the Interaction Service
   * @param securityService the Security Service
   */
  public BackgroundInteractionProcessorImpl(
      InteractionService interactionService, SecurityService securityService) {
    this.interactionService = interactionService;
    this.securityService = securityService;
  }

  /** Initialize the Background Interaction Processor. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Background Interaction Processor");

    if (interactionService != null) {
      // Initialize the interaction processor
      interactionProcessingQueue =
          new LinkedBlockingQueue<>(maximumInteractionProcessingQueueLength);

      // NOTE: We set the initial number of threads to the maximum number of threads because
      //       the implementation of the thread pool executor will never increase the number of
      //       threads if the queue is not full.
      //
      // https://medium.com/@ankithahjpgowda/policies-of-threadpoolexecutor-in-java-75f22fd6f637
      this.interactionProcessor =
          new ThreadPoolExecutor(
              maximumInteractionProcessingThreads,
              maximumInteractionProcessingThreads,
              interactionProcessingThreadKeepAlive,
              TimeUnit.MINUTES,
              interactionProcessingQueue);

      // Reset any locks for interactions that were previously being processed
      try {
        for (UUID tenantId : securityService.getTenantIds()) {
          log.info(
              "Resetting the locks for the interactions being processed for the tenant ("
                  + tenantId
                  + ")");

          interactionService.resetInteractionLocks(
              tenantId, InteractionStatus.PROCESSING, InteractionStatus.QUEUED);
        }
      } catch (Throwable e) {
        log.error("Failed to reset the locks for the interactions being processed", e);
      }
    } else {
      log.error(
          "Failed to initialize the Background Interaction Processor: "
              + "The Interaction Service was NOT injected");
    }
  }

  @Override
  public boolean isRunning() {
    return running.get() || interactionProcessor.isTerminating();
  }

  /**
   * Process the interactions.
   *
   * @return the number of processed interactions
   */
  @Scheduled(cron = "0 * * * * *")
  public int processInteractions() {
    int numberOfProcessedInteractions = 0;

    if (!executing.compareAndSet(false, true)) {
      return 0;
    }

    try {
      for (UUID tenantId : securityService.getTenantIds()) {
        numberOfProcessedInteractions += processInteractionsForTenant(tenantId);
      }
    } catch (Throwable e) {
      log.error("Failed to process the interactions", e);
    } finally {
      executing.set(false);
    }

    return numberOfProcessedInteractions;
  }

  @Override
  public void start() {
    if (running.compareAndSet(false, true)) {
      log.info("Background Interaction Processor started");
    }
  }

  @Override
  public void stop() {
    long terminationTimeout =
        Math.max(
            5 * 60000L,
            (interactionProcessor.getActiveCount() + interactionProcessor.getQueue().size())
                * interactionProcessingTimeout);

    if (running.compareAndSet(true, false)) {
      log.info(
          "Shutting down the Background Interaction Processor with "
              + interactionProcessor.getActiveCount()
              + " interactions currently being processed and "
              + interactionProcessor.getQueue().size()
              + " interactions queued for processing (Timeout is "
              + terminationTimeout
              + " milliseconds)");

      try {
        interactionProcessor.shutdown();

        if (interactionProcessor.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS)) {
          log.info("Successfully shutdown the Background Interaction Processor");
        } else {
          log.warn("Failed to cleanly shutdown the Background Interaction Processor");
        }
      } catch (InterruptedException e) {
        log.warn("The shutdown of the Background Interaction Processor was interrupted");
      }
    }
  }

  private int processInteractionsForTenant(UUID tenantId) {

    int numberOfProcessedInteractionsForTenant = 0;

    while (running.get()) {
      // Retrieve the next interaction queued for processing
      try {
        if (interactionProcessor.getQueue().remainingCapacity() == 0) {
          if (log.isDebugEnabled()) {
            log.debug(
                "The maximum number of interactions queued for processing has been reached ("
                    + maximumInteractionProcessingQueueLength
                    + ")");
          }
        }

        Optional<Interaction> interactionOptional =
            interactionService.getNextInteractionQueuedForProcessing(tenantId);

        if (interactionOptional.isEmpty()) {
          return numberOfProcessedInteractionsForTenant;
        } else {
          interactionProcessor.execute(
              new InteractionProcessor(interactionService, interactionOptional.get()));

          numberOfProcessedInteractionsForTenant++;
        }
      } catch (Throwable e) {
        log.error("Failed to retrieve the next interaction queued for processing", e);
      }
    }

    return numberOfProcessedInteractionsForTenant;
  }

  /**
   * The {@code InteractionProcessor} class.
   *
   * @author Marcus Portmann
   */
  public static class InteractionProcessor implements Runnable {

    /** The interaction. */
    private final Interaction interaction;

    /** The Interaction Service. */
    private final InteractionService interactionService;

    /**
     * Constructs a new {@code InteractionProcessor}.
     *
     * @param interactionService the Interaction Service
     * @param interaction the interaction
     */
    public InteractionProcessor(InteractionService interactionService, Interaction interaction) {
      this.interactionService = interactionService;
      this.interaction = interaction;
    }

    @Override
    public void run() {
      try {
        if (log.isDebugEnabled()) {
          log.debug(
              "Processing the interaction (%s) for the tenant (%s)"
                  .formatted(interaction.getId(), interaction.getTenantId()));
        }

        long startTime = System.currentTimeMillis();

        InteractionProcessingResult interactionProcessingResult =
            interactionService.processInteraction(interaction);

        long finishTime = System.currentTimeMillis();

        // Unlock the interaction and make it available for indexing.
        interactionService.unlockInteraction(
            interaction.getTenantId(), interaction.getId(), InteractionStatus.AVAILABLE);
      } catch (Throwable e) {
        log.error(
            "Failed to process the interaction (%s) for the tenant (%s)"
                .formatted(interaction.getId(), interaction.getTenantId()),
            e);

        try {
          /*
           * If the interaction has exceeded the maximum number of processing attempts then
           * unlock it and set its status to "Available" otherwise unlock it and set its status to
           * "Received" to attempt processing again.
           */
          if (interaction.getProcessingAttempts()
              >= interactionService.getMaximumInteractionProcessingAttempts()) {
            log.warn(
                "The interaction (%s) for the tenant (%s) has exceeded the maximum number of processing attempts and will be marked as AVAILABLE so it can be manually processed"
                    .formatted(interaction.getId(), interaction.getTenantId()));

            interactionService.unlockInteraction(
                interaction.getTenantId(), interaction.getId(), InteractionStatus.AVAILABLE);
          } else {
            interactionService.unlockInteraction(
                interaction.getTenantId(), interaction.getId(), InteractionStatus.QUEUED);
          }
        } catch (Throwable f) {
          log.error(
              "Failed to unlock and set the status for the interaction (%s) for the tenant (%s)"
                  .formatted(interaction.getId(), interaction.getTenantId()),
              f);
        }
      }
    }
  }
}

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
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.persistence.jpa.InteractionRepository;
import digital.inception.processor.BackgroundObjectProcessor;
import digital.inception.processor.ObjectProcessingResult;
import digital.inception.processor.RetryHandling;
import digital.inception.processor.persistence.jpa.JpaRepositoryBackedBackgroundObjectProcessor;
import digital.inception.processor.persistence.jpa.JpaRepositoryBackedObjectProcessor;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The {@code BackgroundInteractionProcessor} class.
 *
 * @author Marcus Portmann
 */
@Component
public class BackgroundInteractionProcessor
    extends JpaRepositoryBackedBackgroundObjectProcessor<
        Interaction, UUID, InteractionStatus, InteractionRepository> {

  /**
   * Constructs a new {@code BackgroundInteractionProcessor}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param interactionService the Interaction Service
   * @param interactionRepository the Interaction Repository
   * @param processingThreadCount the fixed number of worker threads in the interaction processing
   *     pool
   * @param maximumQueueLength the maximum number of interactions to queue for processing if no
   *     interaction processing threads are available
   * @param lockTimeoutSeconds the maximum age, in seconds, that an interaction may remain in a
   *     PROCESSING state before its lock is considered stale and eligible for reset; a value &lt;=
   *     0 disables periodic lock reset
   * @param processingTimeoutMillis a base timeout, in milliseconds, used together with the number
   *     of active and queued tasks to derive an upper bound when waiting for the executor to shut
   *     down (see {@link BackgroundObjectProcessor#stop()})
   * @param maximumProcessingAttempts the maximum number of times to attempt processing an
   *     interaction
   * @param retryDelayMillis the base delay, in milliseconds, between interaction processing retries
   */
  public BackgroundInteractionProcessor(
      ApplicationContext applicationContext,
      InteractionService interactionService,
      InteractionRepository interactionRepository,
      @Value("${ceb-svc-operations.interaction-processing.processing-thread-count:#{5}}")
          int processingThreadCount,
      @Value("${ceb-svc-operations.interaction-processing.max-queue-length:#{10}}")
          int maximumQueueLength,
      @Value("${ceb-svc-operations.interaction-processing.lock-timeout:#{600}}")
          long lockTimeoutSeconds,
      @Value("${ceb-svc-operations.interaction-processing.processing-timeout:#{60000L}}")
          long processingTimeoutMillis,
      @Value("${ceb-svc-operations.interaction-processing.max-processing-attempts:#{100}}")
          int maximumProcessingAttempts,
      @Value("${ceb-svc-operations.interaction-processing.retry-delay:#{10000}}")
          long retryDelayMillis) {
    super(
        applicationContext,
        interactionRepository,
        new InteractionProcessor(
            interactionService, interactionRepository, maximumProcessingAttempts, retryDelayMillis),
        processingThreadCount,
        maximumQueueLength,
        lockTimeoutSeconds,
        processingTimeoutMillis);
  }

  /**
   * Process the interactions.
   *
   * @return the number of processed interactions
   */
  @Scheduled(cron = "0 * * * * *")
  public int processInteractions() {
    return processObjects();
  }

  @Override
  protected Map<InteractionStatus, InteractionStatus> getPendingToProcessingStatusMappings() {
    return Map.of(InteractionStatus.QUEUED, InteractionStatus.PROCESSING);
  }

  private static class InteractionProcessor
      extends JpaRepositoryBackedObjectProcessor<
          Interaction, UUID, InteractionStatus, InteractionRepository> {

    private final InteractionService interactionService;

    /** The base delay between interaction processing retries, in milliseconds. */
    private final long retryDelayMillis;

    public InteractionProcessor(
        InteractionService interactionService,
        InteractionRepository interactionRepository,
        int maximumInteractionProcessingAttempts,
        long retryDelayMillis) {
      super(interactionRepository, maximumInteractionProcessingAttempts);

      this.interactionService = interactionService;
      this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public InteractionStatus determineFailureStatus(
        Interaction interaction, InteractionStatus currentStatus, Throwable cause) {
      return InteractionStatus.FAILED;
    }

    @Override
    public RetryHandling<InteractionStatus> determineRetryHandling(
        Interaction interaction, InteractionStatus currentStatus, Throwable cause) {
      return RetryHandling.delayedWithBackoff(
          InteractionStatus.QUEUED,
          interaction.getProcessingAttempts(),
          retryDelayMillis,
          ChronoUnit.MILLIS);
    }

    @Override
    public Collection<InteractionStatus> getPendingStatuses() {
      return List.of(InteractionStatus.QUEUED);
    }

    @Override
    public ObjectProcessingResult<InteractionStatus> process(Interaction interaction)
        throws Exception {
      if (Objects.requireNonNull(interaction.getStatus()) == InteractionStatus.PROCESSING) {
        interactionService.processInteraction(interaction);

        return new ObjectProcessingResult<>(InteractionStatus.AVAILABLE);
      }
      throw new RuntimeException(
          "Invalid processing status for interaction (" + interaction.getStatus() + ")");
    }

    @Override
    protected InteractionStatus determineProcessingStatusOnClaim(
        Interaction interaction, InteractionStatus currentStatus) {
      return InteractionStatus.PROCESSING;
    }
  }
}

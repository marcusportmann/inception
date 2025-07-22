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

import digital.inception.operations.model.InteractionSource;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The {@code BackgroundInteractionSourceSynchronizerImpl} class implements the Background
 * Interaction Source Synchronizer.
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public class BackgroundInteractionSourceSynchronizerImpl
    implements BackgroundInteractionSourceSynchronizer, SmartLifecycle {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(BackgroundInteractionSourceSynchronizerImpl.class);

  /** Is the Background Interaction Source Synchronizer executing? */
  private final AtomicBoolean executing = new AtomicBoolean(false);

  /** The Interaction Service. */
  private final InteractionService interactionService;

  /** Is the Background Interaction Processor running. */
  private final AtomicBoolean running = new AtomicBoolean(false);

  /**
   * Constructs a new {@code BackgroundInteractionSourceSynchronizerImpl}.
   *
   * @param interactionService the Interaction Service
   */
  public BackgroundInteractionSourceSynchronizerImpl(InteractionService interactionService) {
    this.interactionService = interactionService;
  }

  /** Initialize the Background Interaction Source Synchronizer. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Background Interaction Source Synchronizer");
  }

  @Override
  public boolean isRunning() {
    return running.get();
  }

  @Override
  public void start() {
    if (running.compareAndSet(false, true)) {
      log.info("Background Interaction Source Synchronizer started");
    }
  }

  @Override
  public void stop() {
    if (running.compareAndSet(true, false)) {
      log.info("Shutting down the Background Interaction Source Synchronizer");
    }
  }

  /** Synchronize the interaction sources. */
  @Scheduled(cron = "0 * * * * *")
  public int synchronizeInteractionSources() {
    if (!executing.compareAndSet(false, true)) {
      return 0;
    }

    try {
      int numberOfNewInteractions = 0;

      if (running.get()) {
        try {
          List<InteractionSource> interactionSources = interactionService.getInteractionSources();

          if (running.get()) {
            for (InteractionSource interactionSource : interactionSources) {
              numberOfNewInteractions +=
                  interactionService.synchronizeInteractionSource(interactionSource);
            }
          }
        } catch (Throwable e) {
          log.error("Failed to synchronize the interaction sources", e);
        }
      }

      return numberOfNewInteractions;
    } finally {
      executing.set(false);
    }
  }
}

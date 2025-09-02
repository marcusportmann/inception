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

import digital.inception.operations.service.InteractionService.TriggerInteractionSourceSynchronizationEvent;
import java.util.concurrent.RejectedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * The {@code TriggerInteractionSourceSynchronizationEventListener} class.
 *
 * @author Marcus Portmann
 */
@Component
public class TriggerInteractionSourceSynchronizationEventListener {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(TriggerInteractionSourceSynchronizationEventListener.class);

  /** The Background Interaction Source Synchronizer. */
  private final BackgroundInteractionSourceSynchronizer backgroundInteractionSourceSynchronizer;

  /**
   * Constructs a new {@code TriggerInteractionSourceSynchronizationEventListener}
   *
   * @param backgroundInteractionSourceSynchronizer the Background Interaction Source Synchronizer
   */
  public TriggerInteractionSourceSynchronizationEventListener(
      BackgroundInteractionSourceSynchronizer backgroundInteractionSourceSynchronizer) {
    this.backgroundInteractionSourceSynchronizer = backgroundInteractionSourceSynchronizer;
  }

  /**
   * Handle the event to trigger interaction source synchronization.
   *
   * @param triggerInteractionSourceSynchronizationEvent the event to trigger interaction source
   *     synchronization
   */
  @Async("triggerInteractionSourceSynchronizationExecutor")
  @EventListener
  @SuppressWarnings("unused")
  public void onInteractionSourceSynchronizationTriggered(
      TriggerInteractionSourceSynchronizationEvent triggerInteractionSourceSynchronizationEvent) {
    try {
      backgroundInteractionSourceSynchronizer.synchronizeInteractionSources();
    } catch (RejectedExecutionException ignored) {
    }
  }
}

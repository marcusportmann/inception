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

import digital.inception.operations.service.EventService.TriggerEventProcessingEvent;
import java.util.concurrent.RejectedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * The {@code TriggerEventProcessingEventListener} class.
 *
 * @author Marcus Portmann
 */
@Component
public class TriggerEventProcessingEventListener {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(TriggerEventProcessingEventListener.class);

  /** The Background Event Processor. */
  private final BackgroundEventProcessor backgroundEventProcessor;

  /**
   * Constructs a new {@code TriggerEventProcessingEventListener}
   *
   * @param backgroundEventProcessor the Background Event Processor
   */
  public TriggerEventProcessingEventListener(BackgroundEventProcessor backgroundEventProcessor) {
    this.backgroundEventProcessor = backgroundEventProcessor;
  }

  /**
   * Handle the event to trigger event processing.
   *
   * @param triggerEventProcessingEvent the event to trigger event processing
   */
  @Async("triggerEventProcessingExecutor")
  @EventListener
  @SuppressWarnings("unused")
  public void onEventProcessingTriggered(TriggerEventProcessingEvent triggerEventProcessingEvent) {
    try {
      backgroundEventProcessor.processEvents();
    } catch (RejectedExecutionException ignored) {
    }
  }
}

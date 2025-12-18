/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.operations.service;
//
// import digital.inception.operations.service.InteractionService.TriggerInteractionProcessingEvent;
// import java.util.concurrent.RejectedExecutionException;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.context.event.EventListener;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Component;
//
/// **
// * The {@code TriggerInteractionProcessingEventListener} class.
// *
// * @author Marcus Portmann
// */
// @Component
// public class TriggerInteractionProcessingEventListener {
//
//  /* Logger */
//  private static final Logger log =
//      LoggerFactory.getLogger(TriggerInteractionProcessingEventListener.class);
//
//  /** The Background Interaction Processor. */
//  private final BackgroundInteractionProcessor backgroundInteractionProcessor;
//
//  /**
//   * Constructs a new {@code TriggerInteractionProcessingEventListener}
//   *
//   * @param backgroundInteractionProcessor the Background Interaction Processor
//   */
//  public TriggerInteractionProcessingEventListener(
//      BackgroundInteractionProcessor backgroundInteractionProcessor) {
//    this.backgroundInteractionProcessor = backgroundInteractionProcessor;
//  }
//
//  /**
//   * Handle the event to trigger interaction processing.
//   *
//   * @param triggerInteractionProcessingEvent the event to trigger interaction processing
//   */
//  @Async("triggerInteractionProcessingExecutor")
//  @EventListener
//  @SuppressWarnings("unused")
//  public void onInteractionProcessingTriggered(
//      TriggerInteractionProcessingEvent triggerInteractionProcessingEvent) {
//    try {
//      backgroundInteractionProcessor.processInteractions();
//    } catch (RejectedExecutionException ignored) {
//    }
//  }
// }

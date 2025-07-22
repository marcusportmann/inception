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

package digital.inception.executor.service;

import digital.inception.executor.service.ExecutorServiceImpl.TriggerTaskExecutionEvent;
import java.util.concurrent.RejectedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * The {@code TriggerTaskExecutionEventListener} class.
 *
 * @author Marcus Portmann
 */
@Component
public class TriggerTaskExecutionEventListener {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(TriggerTaskExecutionEventListener.class);

  /** The Background Task Executor. */
  private final BackgroundTaskExecutor backgroundTaskExecutor;

  /**
   * Constructs a new {@code TriggerTaskExecutionEventListener}
   *
   * @param backgroundTaskExecutor the Background Task Executor
   */
  public TriggerTaskExecutionEventListener(BackgroundTaskExecutor backgroundTaskExecutor) {
    this.backgroundTaskExecutor = backgroundTaskExecutor;
  }

  /**
   * Handle the event to trigger task execution.
   *
   * @param triggerTaskExecutionEvent the event to trigger task execution
   */
  @Async("triggerTaskExecutionExecutor")
  @EventListener
  @SuppressWarnings("unused")
  public void onTaskExecutionTriggered(TriggerTaskExecutionEvent triggerTaskExecutionEvent) {
    try {
      backgroundTaskExecutor.executeTasks();
    } catch (RejectedExecutionException ignored) {
    }
  }
}

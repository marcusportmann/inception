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

package digital.inception.scheduler.service;

import digital.inception.scheduler.service.SchedulerServiceImpl.TriggerJobExecutionEvent;
import java.util.concurrent.RejectedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * The {@code TriggerJobExecutionEventListener} class.
 *
 * @author Marcus Portmann
 */
@Component
public class TriggerJobExecutionEventListener {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(TriggerJobExecutionEventListener.class);

  /** The Background Job Executor. */
  private final BackgroundJobExecutor backgroundJobExecutor;

  /**
   * Constructs a new {@code TriggerJobExecutionEventListener}
   *
   * @param backgroundJobExecutor the Background Job Executor
   */
  public TriggerJobExecutionEventListener(BackgroundJobExecutor backgroundJobExecutor) {
    this.backgroundJobExecutor = backgroundJobExecutor;
  }

  /**
   * Handle the event to trigger job execution.
   *
   * @param triggerJobExecutionEvent the event to trigger job execution
   */
  @Async("triggerJobExecutionExecutor")
  @EventListener
  @SuppressWarnings("unused")
  public void onJobExecutionTriggered(TriggerJobExecutionEvent triggerJobExecutionEvent) {
    try {
      backgroundJobExecutor.executeJobs();
    } catch (RejectedExecutionException ignored) {
    }
  }
}

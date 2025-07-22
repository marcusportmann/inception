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

package digital.inception.sms.service;

import digital.inception.sms.service.SMSServiceImpl.TriggerSMSSendingEvent;
import java.util.concurrent.RejectedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * The {@code TriggerSMSSendingEventListener} class.
 *
 * @author Marcus Portmann
 */
@Component
public class TriggerSMSSendingEventListener {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(TriggerSMSSendingEventListener.class);

  /** The Background SMS Sender. */
  private final BackgroundSMSSender backgroundSMSSender;

  /**
   * Constructs a new {@code TriggerSMSSendingEventListener}
   *
   * @param backgroundSMSSender the Background SMS Sender
   */
  public TriggerSMSSendingEventListener(BackgroundSMSSender backgroundSMSSender) {
    this.backgroundSMSSender = backgroundSMSSender;
  }

  /**
   * Handle the event to trigger SMS sending
   *
   * @param triggerSMSSendingEvent the event to trigger SMS sending
   */
  @Async("triggerSMSSendingExecutor")
  @EventListener
  @SuppressWarnings("unused")
  public void onSMSSendingTriggered(TriggerSMSSendingEvent triggerSMSSendingEvent) {
    try {
      backgroundSMSSender.sendSMSs();
    } catch (RejectedExecutionException ignored) {
    }
  }
}

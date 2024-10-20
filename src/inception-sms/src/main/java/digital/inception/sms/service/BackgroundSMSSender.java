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

import digital.inception.sms.model.SMS;
import digital.inception.sms.model.SMSStatus;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The <b>BackgroundSMSSender</b> class implements the Background SMS Sender.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class BackgroundSMSSender {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(BackgroundSMSSender.class);

  /* SMS Service */
  private final ISMSService smsService;

  /**
   * Constructs a new <b>BackgroundSMSSender</b>.
   *
   * @param smsService the SMS Service
   */
  public BackgroundSMSSender(ISMSService smsService) {
    this.smsService = smsService;
  }

  /** Initialize the Background SMS Sender. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Background SMS Sender");

    if (smsService != null) {
      /*
       * Reset any locks for SMS that were previously being sent by the background
       * SMS sender.
       */
      try {
        log.info("Resetting the locks for the SMSs being sent");

        smsService.resetSMSLocks(SMSStatus.SENDING, SMSStatus.QUEUED);
      } catch (Throwable e) {
        log.error("Failed to reset the locks for the SMSs being sent", e);
      }
    } else {
      log.error("Failed to initialize the Background SMS Sender: The SMS Service was NOT injected");
    }
  }

  /** Send the SMSs. */
  @Scheduled(cron = "0 * * * * *")
  @Async
  public void sendSMSs() {
    Optional<SMS> smsOptional;

    while (true) {
      // Retrieve the next SMS queued for sending
      try {
        smsOptional = smsService.getNextSMSQueuedForSending();

        if (smsOptional.isEmpty()) {
          if (log.isDebugEnabled()) {
            log.debug("No SMSs queued for sending");
          }

          return;
        }
      } catch (Throwable e) {
        log.error("Failed to retrieve the next SMS queued for sending", e);
        return;
      }

      SMS sms = smsOptional.get();

      // Send the SMS
      try {
        if (log.isDebugEnabled()) {
          log.debug("Sending the queued SMS (%s)".formatted(sms.getId()));
        }

        if (smsService.sendSMSSynchronously(sms.getId(), sms.getMobileNumber(), sms.getMessage())) {
          // Delete the SMS
          smsService.deleteSMS(sms.getId());
        } else {
          // Unlock the SMS and mark it as failed
          smsService.unlockSMS(sms.getId(), SMSStatus.FAILED);
        }
      } catch (Throwable e) {
        log.error("Failed to send the queued SMS (%s)".formatted(sms.getId()), e);

        try {
          /*
           * If the SMS has exceeded the maximum number of processing attempts then unlock it
           * and set its status to "Failed" otherwise unlock it and set its status to "Queued".
           */
          if (sms.getSendAttempts() >= smsService.getMaximumSendAttempts()) {
            log.warn(
                "The queued SMS ("
                    + sms.getId()
                    + ") has exceeded the maximum number of send attempts and will be marked as FAILED");

            smsService.unlockSMS(sms.getId(), SMSStatus.FAILED);
          } else {
            smsService.unlockSMS(sms.getId(), SMSStatus.QUEUED);
          }
        } catch (Throwable f) {
          log.error(
              "Failed to unlock and set the status for the queued SMS (%s)".formatted(sms.getId()),
              f);
        }
      }
    }
  }
}

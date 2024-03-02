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

package digital.inception.sms.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.sms.ISMSService;
import digital.inception.sms.SMS;
import digital.inception.sms.SMSStatus;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.StringUtils;

/**
 * The <b>SMSServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>SMSService</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class SMSServiceTest {

  /** The client ID to use for the SMS Portal API. */
  @Value("${inception.sms.providers.sms-portal.client-id:#{null}}")
  private String smsPortalClientId;

  /** The client secret to use for the SMS Portal API. */
  @Value("${inception.sms.providers.sms-portal.client-secret:#{null}}")
  private String smsPortalClientSecret;

  /** The SMS Service. */
  @Autowired private ISMSService smsService;

  private static synchronized SMS getTestSMSDetails() {
    SMS sms = new SMS();
    sms.setId(UuidCreator.getShortPrefixComb());
    sms.setMobileNumber("0832763107");
    sms.setMessage("Testing 1.. 2.. 3..");
    sms.setStatus(SMSStatus.SENT);

    return sms;
  }

  /** Test the get number of SMS credits remaining functionality. */
  @Test
  public void getNumberOfSMSCreditsRemainingTest() throws Exception {
    if ((StringUtils.hasText(smsPortalClientSecret)) && (!smsPortalClientId.equals("CLIENT_ID"))) {
      int numberOfSMSCreditsRemaining = smsService.getNumberOfSMSCreditsRemaining();
    }
  }

  /** Test the send SMS synchronously functionality */
  public void sendSMSSynchronouslyTest() {
    // smsService.sendSMSSynchronously(1, "0832763107", "Testing at 23:02...");
  }

  /** Test the send SMS functionality. */
  @Test
  public void sendSMSTest() throws Exception {
    if ((StringUtils.hasText(smsPortalClientSecret)) && (!smsPortalClientId.equals("CLIENT_ID"))) {
      smsService.sendSMS("0832763107", "Testing 1.. 2.. 3..");

      Thread.sleep(100000L);
    }
  }

  /** Test the SMS functionality. */
  @Test
  public void smsTest() throws Exception {
    SMS sms = getTestSMSDetails();

    smsService.createSMS(sms);

    SMS retrievedSMS = smsService.getSMS(sms.getId());

    compareSMSs(sms, retrievedSMS);

    smsService.deleteSMS(sms.getId());

    sms = getTestSMSDetails();

    sms.setStatus(SMSStatus.QUEUED_FOR_SENDING);

    smsService.createSMS(sms);

    Optional<SMS> smsQueuedForSendingOptional = smsService.getNextSMSQueuedForSending();

    if (smsQueuedForSendingOptional.isEmpty()) {
      fail("Failed to retrieve the SMS queued for sending");
    }

    smsQueuedForSendingOptional.ifPresent(
        smsQueuedForSending -> {
          assertEquals(
              Integer.valueOf(1),
              smsQueuedForSending.getSendAttempts(),
              "The send attempts for the SMS is not correct");

          assertEquals(
              SMSStatus.SENDING,
              smsQueuedForSending.getStatus(),
              "The status for the SMS is not correct");
        });

    smsService.resetSMSLocks(SMSStatus.SENDING, SMSStatus.FAILED);

    retrievedSMS = smsService.getSMS(sms.getId());

    assertEquals(
        SMSStatus.FAILED, retrievedSMS.getStatus(), "The status for the SMS is not correct");
    assertNull(retrievedSMS.getLockName(), "The lock name for the SMS is not null");

    smsService.setSMSStatus(sms.getId(), SMSStatus.SENT);

    retrievedSMS = smsService.getSMS(sms.getId());

    assertEquals(SMSStatus.SENT, retrievedSMS.getStatus(), "The status for the SMS is not correct");

    smsService.unlockSMS(sms.getId(), SMSStatus.FAILED);

    retrievedSMS = smsService.getSMS(sms.getId());

    assertEquals(
        SMSStatus.FAILED, retrievedSMS.getStatus(), "The status for the SMS is not correct");
  }

  private void compareSMSs(SMS sms1, SMS sms2) {
    assertEquals(sms1.getId(), sms2.getId(), "The ID values for the SMSs do not match");
    assertEquals(
        sms1.getMobileNumber(),
        sms2.getMobileNumber(),
        "The mobile number values for the SMSs do not match");
    assertEquals(
        sms1.getMessage(), sms2.getMessage(), "The message values for the SMSs do not match");
    assertEquals(sms1.getStatus(), sms2.getStatus(), "The status values for the SMSs do not match");
    assertEquals(
        sms1.getSendAttempts(),
        sms2.getSendAttempts(),
        "The send attempts values for the SMSs do not match");
    assertEquals(
        sms1.getLockName(), sms2.getLockName(), "The lock name values for the SMSs do not match");
    assertEquals(
        sms1.getLastProcessed(),
        sms2.getLastProcessed(),
        "The last processed values for the SMSs do not match");
  }
}

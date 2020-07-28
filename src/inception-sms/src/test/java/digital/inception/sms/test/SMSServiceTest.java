/*
 * Copyright 2020 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import digital.inception.sms.ISMSService;
import digital.inception.sms.SMS;
import digital.inception.sms.SMSStatus;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SMSServiceTest</code> class contains the implementation of the JUnit tests for the
 * <code>SMSService</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class SMSServiceTest {

  /** The SMS Service. */
  @Autowired private ISMSService smsService;

  /** The client ID to use for the SMS Portal API. */
  @Value("${inception.sms.providers.sms-portal.client-id:#{null}}")
  private String smsPortalClientId;

  /** The client secret to use for the SMS Portal API. */
  @Value("${inception.sms.providers.sms-portal.client-secret:#{null}}")
  private String smsPortalClientSecret;

  private static synchronized SMS getTestSMSDetails() {
    SMS sms = new SMS();
    sms.setMobileNumber("0832763107");
    sms.setMessage("Testing 1.. 2.. 3..");
    sms.setStatus(SMSStatus.UNKNOWN);

    return sms;
  }

  private void compareSMSs(SMS sms1, SMS sms2) {
    assertEquals("The ID values for the two SMSs do not match", sms1.getId(), sms2.getId());
    assertEquals(
        "The mobile number values for the two SMSs do not match",
        sms1.getMobileNumber(),
        sms2.getMobileNumber());
    assertEquals(
        "The message values for the two SMSs do not match", sms1.getMessage(), sms2.getMessage());
    assertEquals(
        "The status values for the two SMSs do not match", sms1.getStatus(), sms2.getStatus());
    assertEquals(
        "The send attempts values for the two SMSs do not match",
        sms1.getSendAttempts(),
        sms2.getSendAttempts());
    assertEquals(
        "The lock name values for the two SMSs do not match",
        sms1.getLockName(),
        sms2.getLockName());
    assertEquals(
        "The last processed values for the two SMSs do not match",
        sms1.getLastProcessed(),
        sms2.getLastProcessed());
  }

  /** Test the get number of SMS credits remaining functionality. */
  @Test
  public void getNumberOfSMSCreditsRemainingTest() throws Exception {
    if ((!StringUtils.isEmpty(smsPortalClientSecret)) && (!smsPortalClientId.equals("CLIENT_ID"))) {
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
    if ((!StringUtils.isEmpty(smsPortalClientSecret)) && (!smsPortalClientId.equals("CLIENT_ID"))) {
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

    retrievedSMS = smsService.getNextSMSQueuedForSending();

    assertNotNull(retrievedSMS);

    assertEquals(
        "The send attempts for the SMS is not correct",
        Integer.valueOf(1),
        retrievedSMS.getSendAttempts());
    assertEquals(
        "The status for the SMS is not correct", SMSStatus.SENDING, retrievedSMS.getStatus());

    smsService.resetSMSLocks(SMSStatus.SENDING, SMSStatus.FAILED);

    retrievedSMS = smsService.getSMS(sms.getId());

    assertEquals(
        "The status for the SMS is not correct", SMSStatus.FAILED, retrievedSMS.getStatus());
    assertNull("The lock name for the SMS is not null", retrievedSMS.getLockName());

    smsService.setSMSStatus(sms.getId(), SMSStatus.SENT);

    retrievedSMS = smsService.getSMS(sms.getId());

    assertEquals("The status for the SMS is not correct", SMSStatus.SENT, retrievedSMS.getStatus());

    smsService.unlockSMS(sms.getId(), SMSStatus.UNKNOWN);

    retrievedSMS = smsService.getSMS(sms.getId());

    assertEquals(
        "The status for the SMS is not correct", SMSStatus.UNKNOWN, retrievedSMS.getStatus());
  }
}

/*
 * Copyright 2017 Marcus Portmann
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

package digital.inception.sms;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

/**
 * The <code>ISMSService</code> interface defines the functionality provided by an SMS Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface ISMSService {

  /**
   * Create the SMS.
   *
   * @param sms the <code>SMS</code> instance containing the information for the SMS
   */
  void createSMS(SMS sms)
      throws SMSServiceException;

  /**
   * Delete the existing SMS.
   *
   * @param smsId the ID uniquely identifying the SMS
   */
  void deleteSMS(UUID smsId)
      throws SMSNotFoundException, SMSServiceException;

  /**
   * Returns the maximum number of send attempts for a SMS.
   *
   * @return the maximum number of send attempts for a SMS
   */
  int getMaximumSendAttempts();

  /**
   * Retrieve the next SMS that has been queued for sending.
   * <p/>
   * The SMS will be locked to prevent duplicate sending.
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   * currently queued for sending
   */
  SMS getNextSMSQueuedForSending()
      throws SMSServiceException;

  /**
   * Returns the number of SMS credits remaining.
   *
   * @return the number of SMS credits remaining
   */
  int getNumberOfSMSCreditsRemaining()
      throws SMSServiceException;

  /**
   * Retrieve the SMS.
   *
   * @param smsId the ID uniquely identifying the SMS
   *
   * @return the SMS or <code>null</code> if the SMS could not be found
   */
  SMS getSMS(UUID smsId)
      throws SMSNotFoundException, SMSServiceException;

  /**
   * Reset the SMS locks.
   *
   * @param status    the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   */
  void resetSMSLocks(SMSStatus status, SMSStatus newStatus)
      throws SMSServiceException;

  /**
   * Send the SMS.
   * <p/>
   * NOTE: This will queue the SMS for sending. The SMS will actually be sent asynchronously.
   *
   * @param mobileNumber the mobile number
   * @param message      the message
   */
  void sendSMS(String mobileNumber, String message)
      throws SMSServiceException;

  /**
   * Send the SMS synchronously.
   * <p/>
   * NOTE: This will NOT queue the SMS for sending. The SMS will actually be sent synchronously.
   *
   * @param smsId        the ID of the SMS
   * @param mobileNumber the mobile number
   * @param message      the message
   *
   * @return <code>true</code> if the SMS was sent successfully or <code>false</code> otherwise
   */
  boolean sendSMSSynchronously(UUID smsId, String mobileNumber, String message)
      throws SMSServiceException;

  /**
   * Set the status for the SMS.
   *
   * @param smsId  the ID uniquely identifying the SMS
   * @param status the new status for the SMS
   */
  void setSMSStatus(UUID smsId, SMSStatus status)
      throws SMSNotFoundException, SMSServiceException;

  /**
   * Unlock the SMS.
   *
   * @param smsId  the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   */
  void unlockSMS(UUID smsId, SMSStatus status)
      throws SMSNotFoundException, SMSServiceException;
}

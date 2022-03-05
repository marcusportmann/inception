/*
 * Copyright 2022 Marcus Portmann
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

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.util.Optional;
import java.util.UUID;

/**
 * The <b>ISMSService</b> interface defines the functionality provided by an SMS Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface ISMSService {

  /**
   * Create the new SMS.
   *
   * @param sms the <b>SMS</b> instance containing the information for the SMS
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the SMS could not be created
   */
  void createSMS(SMS sms) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Delete the SMS.
   *
   * @param smsId the ID for the SMS
   * @throws InvalidArgumentException if an argument is invalid
   * @throws SMSNotFoundException if the SMS could not be found
   * @throws ServiceUnavailableException if the SMS could not be deleted
   */
  void deleteSMS(UUID smsId)
      throws InvalidArgumentException, SMSNotFoundException, ServiceUnavailableException;

  /**
   * Returns the maximum number of send attempts for a SMS.
   *
   * @return the maximum number of send attempts for a SMS
   */
  int getMaximumSendAttempts();

  /**
   * Retrieve the next SMS that has been queued for sending.
   *
   * <p>The SMS will be locked to prevent duplicate sending.
   *
   * @return an Optional containing the next SMS that has been queued for sending or an empty
   *     Optional if no SMSs are currently queued for sending
   * @throws ServiceUnavailableException if the next SMS queued for sending could not be retrieved
   */
  Optional<SMS> getNextSMSQueuedForSending() throws ServiceUnavailableException;

  /**
   * Retrieve the number of SMS credits remaining.
   *
   * @return the number of SMS credits remaining
   * @throws ServiceUnavailableException if the number of SMS credits remaining could not be
   *     retrieved
   */
  int getNumberOfSMSCreditsRemaining() throws ServiceUnavailableException;

  /**
   * Retrieve the SMS.
   *
   * @param smsId the ID for the SMS
   * @return the SMS
   * @throws InvalidArgumentException if an argument is invalid
   * @throws SMSNotFoundException if the SMS could not be found
   * @throws ServiceUnavailableException if the SMS could not be retrieved
   */
  SMS getSMS(UUID smsId)
      throws InvalidArgumentException, SMSNotFoundException, ServiceUnavailableException;

  /**
   * Reset the SMS locks.
   *
   * @param status the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the SMS locks could not be reset
   */
  void resetSMSLocks(SMSStatus status, SMSStatus newStatus)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Send the SMS.
   *
   * <p>This will queue the SMS for sending. The SMS will actually be sent asynchronously.
   *
   * @param mobileNumber the mobile number
   * @param message the message
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the SMS could not be sent
   */
  void sendSMS(String mobileNumber, String message)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Send the SMS synchronously.
   *
   * <p>This will NOT queue the SMS for sending. The SMS will actually be sent synchronously.
   *
   * @param smsId the ID of the SMS
   * @param mobileNumber the mobile number
   * @param message the message
   * @return <b>true</b> if the SMS was sent successfully or <b>false</b> otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the SMS could not be sent synchronously
   */
  boolean sendSMSSynchronously(UUID smsId, String mobileNumber, String message)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Set the status for the SMS.
   *
   * @param smsId the ID for the SMS
   * @param status the new status for the SMS
   * @throws InvalidArgumentException if an argument is invalid
   * @throws SMSNotFoundException if the SMS could not be found
   * @throws ServiceUnavailableException if the status could not be set for the SMS
   */
  void setSMSStatus(UUID smsId, SMSStatus status)
      throws InvalidArgumentException, SMSNotFoundException, ServiceUnavailableException;

  /**
   * Unlock the SMS.
   *
   * @param smsId the ID for the SMS
   * @param status the new status for the unlocked SMS
   * @throws InvalidArgumentException if an argument is invalid
   * @throws SMSNotFoundException if the SMS could not be found
   * @throws ServiceUnavailableException if the SMS could not be unlocked
   */
  void unlockSMS(UUID smsId, SMSStatus status)
      throws InvalidArgumentException, SMSNotFoundException, ServiceUnavailableException;
}

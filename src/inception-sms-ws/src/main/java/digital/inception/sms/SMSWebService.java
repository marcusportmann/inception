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

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * The <b>SMSWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "SMSService",
    name = "ISMSService",
    targetNamespace = "http://inception.digital/sms")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class SMSWebService {

  /** The SMS Service. */
  private final ISMSService smsService;

  /**
   * Constructs a new <b>SMSWebService</b>.
   *
   * @param smsService the SMS Service
   */
  public SMSWebService(ISMSService smsService) {
    this.smsService = smsService;
  }
}

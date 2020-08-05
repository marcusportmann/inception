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

package digital.inception.sms;

// ~--- JDK imports ------------------------------------------------------------

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.Validator;

/**
 * The <code>SMSWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "SMSService",
    name = "ISMSService",
    targetNamespace = "http://sms.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class SMSWebService {

  /**
   * The SMS Service.
   */
  private final ISMSService smsService;

  /**
   * The JSR-303 validator.
   */
  private final Validator validator;

  /**
   * Constructs a new <code>SMSWebService</code>.
   *
   * @param smsService the SMS Service
   * @param validator  the JSR-303 validator
   */
  public SMSWebService(ISMSService smsService, Validator validator) {
    this.smsService = smsService;
    this.validator = validator;
  }
}

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

package digital.inception.sms.ws;

import digital.inception.sms.service.SMSService;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import org.springframework.context.ApplicationContext;

/**
 * The {@code SMSWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "SMSService",
    name = "ISMSService",
    targetNamespace = "https://inception.digital/sms")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class SMSWebService extends AbstractWebServiceBase {

  /** The SMS Service. */
  private final SMSService smsService;

  /**
   * Constructs a new {@code SMSWebService}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param smsService the SMS Service
   */
  public SMSWebService(ApplicationContext applicationContext, SMSService smsService) {
    super(applicationContext);

    this.smsService = smsService;
  }
}

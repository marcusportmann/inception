/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.messaging;

//~--- JDK imports ------------------------------------------------------------

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.Validator;

/**
 * The <code>MessagingWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(serviceName = "MessagingService", name = "IMessagingService",
    targetNamespace = "http://messaging.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "WeakerAccess", "ValidExternallyBoundObject"})
public class MessagingWebService {

  /**
   * The Messaging Service.
   */
  private IMessagingService messagingService;

  /**
   * The JSR-303 validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>MessagingWebService</code>.
   *
   * @param messagingService the Messaging Service
   * @param validator        the JSR-303 validator
   */
  public MessagingWebService(IMessagingService messagingService, Validator validator) {
    this.messagingService = messagingService;
    this.validator = validator;
  }
}

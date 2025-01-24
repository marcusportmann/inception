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

package digital.inception.sms.controller;

import digital.inception.api.SecureApiController;
import digital.inception.sms.service.SMSService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>SMSApiControllerImpl</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
// @el (isSecurityDisabled: digital.inception.api.ApiSecurityExpressionRoot.isSecurityEnabled)
public class SMSApiControllerImpl extends SecureApiController implements SMSApiController {

  /** The SMS Service. */
  private final SMSService smsService;

  /**
   * Constructs a new <b>SMSApiControllerImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param smsService the SMS Service
   */
  public SMSApiControllerImpl(ApplicationContext applicationContext, SMSService smsService) {
    super(applicationContext);

    this.smsService = smsService;
  }
}

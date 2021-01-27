/*
 * Copyright 2021 Marcus Portmann
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



import digital.inception.rs.SecureRestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * The <code>SMSRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "SMS API")
@RestController
@RequestMapping(value = "/api/sms")
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class SMSRestController extends SecureRestController {

  /** The SMS Service. */
  private final ISMSService smsService;

  /**
   * Constructs a new <code>SMSRestController</code>.
   *
   * @param smsService the SMS Service
   */
  public SMSRestController(ISMSService smsService) {
    this.smsService = smsService;
  }
}

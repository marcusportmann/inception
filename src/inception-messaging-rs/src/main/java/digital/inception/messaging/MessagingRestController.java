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

package digital.inception.messaging;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.SecureRestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessagingRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Messaging API")
@RestController
@RequestMapping(value = "/api/messaging")
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class MessagingRestController extends SecureRestController {

  /** The Messaging Service. */
  private final IMessagingService messagingService;

  /**
   * Constructs a new <code>MessagingRestController</code>.
   *
   * @param messagingService the Messaging Service
   */
  public MessagingRestController(IMessagingService messagingService) {
    this.messagingService = messagingService;
  }
}

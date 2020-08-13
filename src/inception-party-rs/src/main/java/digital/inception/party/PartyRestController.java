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

package digital.inception.party;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.SecureRestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Validator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>PartyRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Party API")
@RestController
@RequestMapping(value = "/api/party")
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class PartyRestController extends SecureRestController {

  /**
   * The Party Service.
   */
  private final IPartyService partyService;

  /**
   * The JSR-303 validator.
   */
  private final Validator validator;

  /**
   * Constructs a new <code>PartyRestController</code>.
   *
   * @param partyService the Party Service
   * @param validator    the JSR-303 validator
   */
  public PartyRestController(IPartyService partyService, Validator validator) {
    this.partyService = partyService;
    this.validator = validator;
  }
}

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

// ~--- JDK imports ------------------------------------------------------------

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.Validator;

/**
 * The <code>PartyWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "PartyService",
    name = "IPartyService",
    targetNamespace = "http://party.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class PartyWebService {

  /**
   * The Party Service.
   */
  private final IPartyService partyService;

  /**
   * The JSR-303 validator.
   */
  private final Validator validator;

  /**
   * Constructs a new <code>PartyWebService</code>.
   *
   * @param partyService the Party Service
   * @param validator    the JSR-303 validator
   */
  public PartyWebService(IPartyService partyService, Validator validator) {
    this.partyService = partyService;
    this.validator = validator;
  }
}

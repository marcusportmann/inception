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

package digital.inception.reference;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.SecureRestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Validator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReferenceRestController</code> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Reference API")
@RestController
@RequestMapping(value = "/api/reference")
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReferenceRestController extends SecureRestController {

  /**
   * The Reference Service.
   */
  private final IReferenceService referenceService;

  /**
   * The JSR-303 validator.
   */
  private final Validator validator;

  /**
   * Constructs a new <code>ReferenceRestController</code>.
   *
   * @param referenceService the Reference Service
   * @param validator        the JSR-303 validator
   */
  public ReferenceRestController(IReferenceService referenceService, Validator validator) {
    this.referenceService = referenceService;
    this.validator = validator;
  }
}

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

package digital.inception.party.constraints;

import digital.inception.party.Association;
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IPartyService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The <b>ValidAssociationValidator</b> class implements the custom constraint validator for
 * validating an association.
 *
 * @author Marcus Portmann
 */
public class ValidAssociationValidator
    implements ConstraintValidator<ValidAssociation, Association> {

  /** The Party Reference Service. */
  private final IPartyReferenceService partyReferenceService;

  /** The Party Service. */
  private final IPartyService partyService;

  /**
   * Constructs a new <b>ValidAssociationValidator</b>.
   *
   * @param partyService the Party Service
   * @param partyReferenceService the Party Reference Service
   */
  @Autowired
  public ValidAssociationValidator(
      IPartyService partyService, IPartyReferenceService partyReferenceService) {
    this.partyService = partyService;
    this.partyReferenceService = partyReferenceService;
  }

  /** Constructs a new <b>ValidAssociationValidator</b>. */
  public ValidAssociationValidator() {
    this.partyService = null;
    this.partyReferenceService = null;
  }

  @Override
  public void initialize(ValidAssociation constraintAnnotation) {}

  @Override
  public boolean isValid(
      Association association, ConstraintValidatorContext constraintValidatorContext) {
    if ((partyService != null) && (partyReferenceService != null)) {
      boolean isValid = true;

      return isValid;
    } else {



      return true;
    }
  }
}

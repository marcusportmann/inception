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

import digital.inception.party.Organization;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The <b>ValidOrganizationValidator</b> class implements the custom constraint validator for
 * validating an organization.
 *
 * @author Marcus Portmann
 */
public class ValidOrganizationValidator
    implements ConstraintValidator<ValidOrganization, Organization> {

  @Autowired
  public ValidOrganizationValidator() {}

  @Override
  public void initialize(ValidOrganization constraintAnnotation) {}

  @Override
  public boolean isValid(Organization physicalAddress, ConstraintValidatorContext context) {

    boolean isValid = true;

    // Disable the default constraint violation
    context.disableDefaultConstraintViolation();

    // TODO: Implement custom Organization validation.

    return isValid;
  }
}

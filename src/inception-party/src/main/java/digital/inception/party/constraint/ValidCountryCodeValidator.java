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

package digital.inception.party.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * The {@code ValidCountryCodeValidator} class implements the custom constraint validator for
 * validating a country code.
 *
 * @author Marcus Portmann
 */
public class ValidCountryCodeValidator implements ConstraintValidator<ValidCountryCode, String> {

  /** Constructs a new {@code ValidCountryCodeValidator}. */
  public ValidCountryCodeValidator() {}

  @Override
  public void initialize(ValidCountryCode constraintAnnotation) {}

  @Override
  public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
    if (code == null) {
      return true;
    }

    return StringUtils.hasText(code);
  }
}

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

package digital.inception.core.validation.constraint;

import digital.inception.core.reference.Countries;
import digital.inception.core.reference.Country;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * The {@code ValidISOCountryCodeValidator} class implements the custom constraint validator that
 * validates whether a <b>String</b> contains a valid ISO 3166-1 alpha-2 or ISO 3166-1 alpha-3
 * country code.
 *
 * @author Marcus Portmann
 */
public class ValidISOCountryCodeValidator
    implements ConstraintValidator<ValidISOCountryCode, Object> {

  /** Creates a new {@code ValidISOCountryCodeValidator} instance. */
  public ValidISOCountryCodeValidator() {}

  @Override
  public void initialize(ValidISOCountryCode constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
    if (object == null) {
      return true;
    }

    if (object instanceof String code) {
      if (code.length() == 2) {
        Optional<Country> countryOptional = Countries.getByCode(code);

        if (countryOptional.isEmpty()) {
          addConstraintViolation(constraintValidatorContext, code);
          return false;
        } else {
          return true;
        }
      } else if (code.length() == 3) {
        Optional<Country> countryOptional = Countries.getByIso3Code(code);

        if (countryOptional.isEmpty()) {
          addConstraintViolation(constraintValidatorContext, code);
          return false;
        } else {
          return true;
        }
      } else {
        addConstraintViolation(constraintValidatorContext, object.toString());
        return false;
      }
    } else {
      addConstraintViolation(constraintValidatorContext, object.toString());
      return false;
    }
  }

  private void addConstraintViolation(ConstraintValidatorContext context, String invalidValue) {
    // Disable the default message
    context.disableDefaultConstraintViolation();

    // Build the custom error message with the encoded value
    String message = String.format("invalid ISO 3166-1 country code (%s)".formatted(invalidValue));

    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }
}

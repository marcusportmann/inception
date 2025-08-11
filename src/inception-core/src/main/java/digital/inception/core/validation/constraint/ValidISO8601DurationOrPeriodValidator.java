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

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;

/**
 * The {@code ValidISO8601DurationOrPeriodValidator} class implements the custom constraint
 * validator for applying the string validation, which detects whether a {@code String} contains a
 * valid ISO-8601 duration or period.
 *
 * @author Marcus Portmann
 */
public class ValidISO8601DurationOrPeriodValidator
    implements ConstraintValidator<ValidISO8601DurationOrPeriod, String> {

  private boolean allowDuration;

  private boolean allowNegative;

  private boolean allowPeriod;

  /** Constructs a new {@code ValidISO8601DurationOrPeriodValidator}. */
  public ValidISO8601DurationOrPeriodValidator() {}

  @Override
  public void initialize(ValidISO8601DurationOrPeriod constraintAnnotation) {
    this.allowDuration = constraintAnnotation.allowDuration();
    this.allowPeriod = constraintAnnotation.allowPeriod();
    this.allowNegative = constraintAnnotation.allowNegative();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      // Let @NotNull/@NotBlank handle presence if needed
      return true;
    }
    String v = value.trim();

    // Try Duration if permitted
    if (allowDuration) {
      try {
        Duration d = Duration.parse(v);
        return allowNegative || !d.isNegative();
      } catch (DateTimeParseException ignore) {
        // Not a Duration
      }
    }

    // Try Period if permitted
    if (allowPeriod) {
      try {
        Period p = Period.parse(v);
        return allowNegative || !p.isNegative();
      } catch (DateTimeParseException ignore) {
        // Not a Period
      }
    }

    // Neither parsed successfully under allowed types
    return false;
  }
}

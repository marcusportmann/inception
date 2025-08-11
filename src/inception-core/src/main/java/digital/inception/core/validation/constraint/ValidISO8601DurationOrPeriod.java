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

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The {@code ValidISO8601DurationOrPeriod} annotation implements the custom constraint annotation
 * used to apply the string validation, which detects whether a {@code String} contains a valid
 * ISO-8601 duration or period.
 *
 * @author Marcus Portmann
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidISO8601DurationOrPeriodValidator.class)
public @interface ValidISO8601DurationOrPeriod {

  /**
   * Accept ISO-8601 time-based durations (e.g., PT15M, P2DT3H).
   *
   * @return {@code true} if ISO-8601 time-based durations are accepted or {@code false} otherwise
   */
  boolean allowDuration() default true;

  /**
   * Allow negative amounts (e.g., -PT5M, -P1M). Default: false.
   *
   * @return {@code true} if negative amounts are allowed or {@code false} otherwise
   */
  boolean allowNegative() default false;

  /**
   * Accept ISO-8601 calendar-based periods (e.g., P3M, P1Y2M10D).
   *
   * @return {@code true} if ISO-8601 calendar-based periods are accepted or {@code false} otherwise
   */
  boolean allowPeriod() default true;

  /**
   * The target groups.
   *
   * @return the target groups
   */
  Class<?>[] groups() default {};

  /**
   * The error message key.
   *
   * @return the error message key
   */
  String message() default
      "must be a valid ISO-8601 duration or period (Duration like PT15M or Period like P3M)";

  /**
   * The payload type.
   *
   * @return the payload type
   */
  Class<? extends Payload>[] payload() default {};
}

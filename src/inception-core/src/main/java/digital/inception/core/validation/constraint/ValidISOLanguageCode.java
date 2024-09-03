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

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <b>ValidISOLanguageCode</b> annotation implements the custom constraint annotation used to
 * validate whether a <b>String</b> contains a valid ISO 639-1 alpha-2 or ISO 639-1 alpha-3 country
 * code.
 *
 * @author Marcus Portmann
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidISOLanguageCodeValidator.class)
public @interface ValidISOLanguageCode {

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
  String message() default "invalid ISO 639-1 language code";

  /**
   * The payload type.
   *
   * @return the payload type
   */
  Class<? extends Payload>[] payload() default {};
}

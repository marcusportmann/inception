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
 * The <b>ValidSouthAfricanMobileNumber</b> annotation implements the custom constraint annotation
 * used to validate whether a <b>String</b> contains a valid South African mobile number.
 *
 * @author Marcus Portmann
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidSouthAfricanMobileNumberValidator.class)
public @interface ValidSouthAfricanMobileNumber {

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
  String message() default "invalid South African mobile number";

  /**
   * The payload type.
   *
   * @return the payload type
   */
  Class<? extends Payload>[] payload() default {};
}

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

package digital.inception.banking.customer.constraints;

// ~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The <code>ValidIndividualCustomer</code> annotation implements the custom constraint annotation
 * used to apply the individual customer validation.
 *
 * @author Marcus Portmann
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIndividualCustomerValidator.class)
@Documented
public @interface ValidIndividualCustomer {
  Class<?>[] groups() default {};

  String message() default
      "{digital.inception.banking.customer.constraints.ValidIndividualCustomer.message}";

  Class<? extends Payload>[] payload() default {};
}

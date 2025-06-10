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

package digital.inception.test.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;

/**
 * The {@code ExceptionConventionsRules} class holds the ArchUnit rules that verify structural
 * conventions for exceptions.
 */
public final class ExceptionConventionsRules {

  /** The ArchUnit rule that verifies that service exceptions follow the correct conventions. */
  public static final ArchRule SERVICE_EXCEPTION_CONVENTIONS =
      classes()
          .that()
          .areAssignableTo("digital.inception.core.exception.ServiceException")
          .and()
          .doNotHaveFullyQualifiedName("digital.inception.core.exception.ServiceException")
          .should()
          .beAnnotatedWith("digital.inception.core.exception.Problem")
          .andShould()
          .resideInAPackage("..exception..")
          .allowEmptyShould(true);

  /** Constructs a new {@code ExceptionConventionsRules}. */
  public ExceptionConventionsRules() {}

  /**
   * Check that the ArchUnit rules are being adhered to.
   *
   * @param classes the Java classes to check
   */
  public static void check(JavaClasses classes) {
    try {
      Class.forName("digital.inception.core.exception.ServiceException");
    } catch (ClassNotFoundException e) {
      return;
    }

    SERVICE_EXCEPTION_CONVENTIONS.check(classes);
  }
}

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
 * The {@code SchedulerConventionsRules} class holds the ArchUnit rules that verify structural
 * conventions when using the Inception Scheduler module.
 *
 * @author Marcus Portmann
 */
public final class SchedulerConventionsRules {

  /**
   * The ArchUnit rule that verifies that job implementations are named correctly, and are in the
   * correct package.
   */
  public static final ArchRule JOB_IMPLEMENTATION_IMPLS_FOLLOW_NAMING_AND_LOCATION =
      classes()
          .that()
          .areAssignableTo("digital.inception.scheduler.model.JobImplementation")
          .and()
          .doNotHaveFullyQualifiedName("digital.inception.scheduler.model.JobImplementation")
          .should()
          .haveSimpleNameEndingWith("Job") // 2. naming
          .andShould()
          .resideInAPackage("..job..") // 4. package
          .allowEmptyShould(true);

  /** Constructs a new {@code SchedulerConventionsRules}. */
  public SchedulerConventionsRules() {}

  /**
   * Check that the ArchUnit rules are being adhered to.
   *
   * @param classes the Java classes to check
   */
  public static void check(JavaClasses classes) {
    try {
      Class.forName("digital.inception.scheduler.model.JobImplementation");
    } catch (ClassNotFoundException e) {
      return;
    }

    JOB_IMPLEMENTATION_IMPLS_FOLLOW_NAMING_AND_LOCATION.check(classes);
  }
}

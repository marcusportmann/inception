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

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
          .haveSimpleNameEndingWith("Job")
          .andShould()
          .resideInAPackage("..job..")
          .andShould()
          .beAnnotatedWith(Component.class)
          .andShould(haveScopePrototype())
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

  private static ArchCondition<JavaClass> haveScopePrototype() {
    return new ArchCondition<JavaClass>("be annotated with @Scope(\"prototype\")") {
      @Override
      public void check(JavaClass item, ConditionEvents events) {
        boolean hasScope = item.isAnnotatedWith(Scope.class);
        boolean ok = false;
        String actual = null;

        if (hasScope) {
          Scope scope = item.getAnnotationOfType(Scope.class);
          String value = scope.value();
          String alias = scope.scopeName(); // Spring alias for 'value'
          actual = (value != null && !value.isEmpty()) ? value : alias;
          ok = ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(actual);
        }

        String message =
            item.getName()
                + (ok ? " has" : " does not have")
                + " @Scope(\"prototype\")"
                + (hasScope ? " (was \"" + actual + "\")" : " (annotation missing)");

        events.add(new SimpleConditionEvent(item, ok, message));
      }
    };
  }
}

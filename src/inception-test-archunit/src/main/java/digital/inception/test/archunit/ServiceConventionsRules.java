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
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.springframework.stereotype.Service;

/**
 * The {@code ServiceConventionsRules} class holds the ArchUnit rules that verify structural
 * conventions for Spring services.
 *
 * @author Marcus Portmann
 */
public class ServiceConventionsRules {

  /**
   * The ArchUnit rule that verifies that Spring service implementations are named correctly, and
   * are in the correct package.
   */
  public static final ArchRule SERVICE_IMPLS_FOLLOW_NAMING_AND_LOCATION =
      classes()
          .that()
          .areAnnotatedWith(Service.class)
          .should()
          .haveSimpleNameEndingWith("ServiceImpl") // 2. naming
          .andShould()
          .resideInAPackage("..service..") // 4. package
          .andShould(
              new ArchCondition<>("implement interface *Service") { // 3.
                @Override
                public void check(JavaClass clazz, ConditionEvents events) {
                  boolean ok =
                      clazz.getInterfaces().stream()
                          .anyMatch(i -> i.toErasure().getSimpleName().endsWith("Service"));

                  if (!ok) {
                    events.add(
                        SimpleConditionEvent.violated(
                            clazz,
                            String.format(
                                "%s does not implement an interface ending in 'Service'",
                                clazz.getName())));
                  }
                }
              });

  /** Constructs a new {@code ServiceConventionsRules}. */
  public ServiceConventionsRules() {}
}

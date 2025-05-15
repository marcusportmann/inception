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

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code ApiControllerConventionsRules} class holds the ArchUnit rules that verify structural
 * conventions for API rest controller interfaces and implementations.
 *
 * @author Marcus Portmann
 */
public class ApiControllerConventionsRules {

  /**
   * The ArchUnit rule that verifies that API controller implementations are named correctly, have
   * the correct annotations, and are in the correct package.
   */
  public static final ArchRule API_CONTROLLERS_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION =
      classes()
          .that()
          .areAnnotatedWith(RestController.class)
          .should()
          .haveSimpleNameEndingWith("ApiControllerImpl")
          .andShould()
          .resideInAPackage("..controller..")
          .andShould()
          .beAnnotatedWith(CrossOrigin.class)
          .andShould(
              new ArchCondition<JavaClass>(
                  "implement an interface named like the class minus \"Impl\"") {
                @Override
                public void check(JavaClass controller, ConditionEvents events) {
                  String expectedInterface = controller.getSimpleName().replaceFirst("Impl$", "");

                  boolean match =
                      controller.getInterfaces().stream()
                          .anyMatch(i -> i.toErasure().getSimpleName().equals(expectedInterface));

                  if (!match) {
                    events.add(
                        SimpleConditionEvent.violated(
                            controller,
                            String.format(
                                "%s does not implement interface %s",
                                controller.getName(), expectedInterface)));
                  }
                }
              })
          .andShould()
          .resideInAPackage("..controller..")
          .allowEmptyShould(true);

  /**
   * The ArchUnit rule that verifies that API controller interfaces are named correctly, have the
   * correct annotations, and are in the correct package.
   */
  public static final ArchRule
      API_CONTROLLER_INTERFACES_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION =
          classes()
              .that()
              .areInterfaces()
              .and()
              .haveSimpleNameEndingWith("ApiController")
              .should()
              .resideInAPackage("..controller..")
              .andShould()
              .beAnnotatedWith(RequestMapping.class)
              .andShould()
              .beAnnotatedWith(Tag.class)
              .andShould()
              .resideInAPackage("..controller..")
              .allowEmptyShould(true);

  /**
   * The ArchUnit rule that verifies that API controller methods have the correct annotations, and
   * are fully documented.
   */
  public static final ArchRule API_CONTROLLER_METHODS_FOLLOW_ANNOTATIONS_AND_FULLY_DOCUMENTED =
      methods()
          .that()
          .areAnnotatedWith(RequestMapping.class)
          .and()
          .areDeclaredInClassesThat()
          .areInterfaces()
          .and()
          .areDeclaredInClassesThat()
          .haveSimpleNameEndingWith("ApiController")
          .and()
          .areAnnotatedWith(RequestMapping.class)
          .should()
          .beAnnotatedWith(Operation.class)
          .andShould()
          .beAnnotatedWith(ApiResponses.class)
          .andShould()
          .beAnnotatedWith(ResponseStatus.class)
          .andShould(
              new ArchCondition<JavaMethod>(
                  "declare only exceptions annotated @Problem and extending ServiceException") {

                @Override
                public void check(JavaMethod method, ConditionEvents events) {
                  for (JavaClass ex : method.getExceptionTypes()) {

                    boolean conforms =
                        ex.isAnnotatedWith(Problem.class)
                            && ex.isAssignableTo(ServiceException.class);

                    if (!conforms) {
                      String message =
                          String.format(
                              "Method %s declares %s which is not a @Problem ServiceException",
                              method.getFullName(), ex.getName());

                      events.add(SimpleConditionEvent.violated(method, message));
                    }
                  }
                }
              })
          .allowEmptyShould(true);

  /** Constructs a new {@code ApiControllerConventionsRules}. */
  public ApiControllerConventionsRules() {}
}

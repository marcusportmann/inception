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

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaEnumConstant;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.Optional;

/**
 * The {@code ApiControllerConventionsRules} class holds the ArchUnit rules that verify structural
 * conventions for API rest controller interfaces and implementations.
 *
 * @author Marcus Portmann
 */
public final class ApiControllerConventionsRules {

  /**
   * The ArchUnit rule that verifies that API controller implementations are named correctly, have
   * the correct annotations, and are in the correct package.
   */
  public static final ArchRule API_CONTROLLERS_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION =
      classes()
          .that()
          .areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
          .should()
          .haveSimpleNameEndingWith("ApiControllerImpl")
          .andShould()
          .resideInAPackage("..controller..")
          .andShould()
          .beAnnotatedWith("org.springframework.web.bind.annotation.CrossOrigin")
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
          .beAssignableTo("digital.inception.api.SecureApiController")
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
              .beAnnotatedWith("org.springframework.web.bind.annotation.RequestMapping")
              .andShould()
              .beAnnotatedWith("io.swagger.v3.oas.annotations.tags.Tag")
              .allowEmptyShould(true);

  /**
   * The ArchUnit rule that verifies that API controller methods have the correct annotations, and
   * are fully documented.
   */
  public static final ArchRule API_CONTROLLER_METHODS_FOLLOW_ANNOTATIONS_AND_FULLY_DOCUMENTED =
      methods()
          .that()
          .areAnnotatedWith("org.springframework.web.bind.annotation.RequestMapping")
          .and()
          .areDeclaredInClassesThat()
          .areInterfaces()
          .and()
          .areDeclaredInClassesThat()
          .haveSimpleNameEndingWith("ApiController")
          .should()
          .beAnnotatedWith("io.swagger.v3.oas.annotations.Operation")
          .andShould()
          .beAnnotatedWith("io.swagger.v3.oas.annotations.responses.ApiResponses")
          .andShould()
          .beAnnotatedWith("org.springframework.web.bind.annotation.ResponseStatus")
          .andShould(
              new ArchCondition<JavaMethod>(
                  "declare only exceptions annotated @Problem and extending ServiceException") {

                @Override
                public void check(JavaMethod method, ConditionEvents events) {
                  for (JavaClass ex : method.getExceptionTypes()) {

                    boolean conforms =
                        ex.isAnnotatedWith("digital.inception.core.exception.Problem")
                            && ex.isAssignableTo(
                                "digital.inception.core.exception.ServiceException");

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

  /**
   * The ArchUnit rule that verifies that the methods on API controller interfaces have
   * the @RequestMapping annotation.
   */
  public static final ArchRule API_CONTROLLER_METHODS_HAVE_REQUEST_MAPPING =
      methods()
          .that()
          .areDeclaredInClassesThat()
          .areInterfaces()
          .and()
          .areDeclaredInClassesThat()
          .haveSimpleNameEndingWith("ApiController")
          .and()
          .areDeclaredInClassesThat()
          .resideInAPackage("..controller..")
          .should()
          .beAnnotatedWith("org.springframework.web.bind.annotation.RequestMapping")
          .orShould()
          .beMetaAnnotatedWith("org.springframework.web.bind.annotation.RequestMapping")
          .allowEmptyShould(true);

  /**
   * The ArchUnit rule that verifies that API controller methods without a return type have the
   * correct HTTP status of HttpStatus.NO_CONTENT.
   */
  public static final ArchRule API_CONTROLLER_VOID_METHODS_REQUIRE_NO_CONTENT_HTTP_STATUS =
      methods()
          // 1️⃣  Scope: methods annotated with @RequestMapping …
          .that()
          .areAnnotatedWith("org.springframework.web.bind.annotation.RequestMapping")
          //     … declared on an interface ending with “ApiController”
          .and()
          .areDeclaredInClassesThat()
          .areInterfaces()
          .and()
          .areDeclaredInClassesThat()
          .haveSimpleNameEndingWith("ApiController")

          // 2️⃣  Condition: if return type is void ⇒ must have @ResponseStatus(NO_CONTENT)
          .should(
              new ArchCondition<JavaMethod>(
                  "have @ResponseStatus(HttpStatus.NO_CONTENT) when the return type is void") {

                @Override
                public void check(JavaMethod method, ConditionEvents events) {
                  boolean isVoid = method.getRawReturnType().isEquivalentTo(void.class);

                  // Only enforce the annotation when the return type is void
                  if (isVoid) {
                    boolean hasResponseStatusAnnotation =
                        method.isAnnotatedWith(
                            "org.springframework.web.bind.annotation.ResponseStatus");

                    if (hasResponseStatusAnnotation) {
                      JavaAnnotation<?> annotation =
                          method.getAnnotationOfType(
                              "org.springframework.web.bind.annotation.ResponseStatus");

                      Optional<Object> valueOptional = annotation.get("value");

                      if (valueOptional.isPresent()) {
                        Object value = valueOptional.get();

                        if (value instanceof JavaEnumConstant javaEnumConstant) {
                          if (!"NO_CONTENT".equals(javaEnumConstant.name())) {
                            String message =
                                String.format(
                                    "Method %s returns void but is not annotated with "
                                        + "@ResponseStatus(HttpStatus.NO_CONTENT)",
                                    method.getFullName());
                            events.add(SimpleConditionEvent.violated(method, message));
                          }
                        }
                      }
                    }
                  }
                }
              })
          .allowEmptyShould(true);

  /** Constructs a new {@code ApiControllerConventionsRules}. */
  public ApiControllerConventionsRules() {}

  /**
   * Check that the ArchUnit rules are being adhered to.
   *
   * @param classes the Java classes to check
   */
  public static void check(JavaClasses classes) {
    try {
      Class.forName("org.springframework.web.bind.annotation.RestController");
    } catch (ClassNotFoundException e) {
      return;
    }

    API_CONTROLLERS_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION.check(classes);

    API_CONTROLLER_INTERFACES_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION.check(classes);

    API_CONTROLLER_METHODS_HAVE_REQUEST_MAPPING.check(classes);

    API_CONTROLLER_METHODS_FOLLOW_ANNOTATIONS_AND_FULLY_DOCUMENTED.check(classes);

    API_CONTROLLER_VOID_METHODS_REQUIRE_NO_CONTENT_HTTP_STATUS.check(classes);
  }
}

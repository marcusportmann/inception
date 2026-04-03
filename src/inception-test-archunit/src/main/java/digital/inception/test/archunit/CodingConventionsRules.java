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
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaConstructor;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.beans.ConstructorProperties;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * The {@code CodingConventionsRules} class holds the ArchUnit rules that verify general coding
 * conventions for applications based on the Inception Framework.
 *
 * @author Marcus Portmann
 */
public final class CodingConventionsRules {

  private static final String APPLICATION_CLOCK = "digital.inception.core.time.ApplicationClock";

  /**
   * The ArchUnit rule that verifies that {@link LocalDateTime#now()} is not used directly and that
   * {@code ApplicationClock.now()} is used instead.
   */
  public static final ArchRule LOCAL_DATE_TIME_NOW_WITHOUT_ARGS_IS_NOT_USED =
      noClasses()
          .that()
          .doNotHaveFullyQualifiedName(APPLICATION_CLOCK)
          .should()
          .callMethod(LocalDateTime.class, "now")
          .because(
              "LocalDateTime.now() bypasses the application clock - use "
                  + "digital.inception.core.time.ApplicationClock.now() instead");

  /**
   * The ArchUnit rule that verifies that {@link LocalDateTime#now(ZoneId)} is not used directly and
   * that {@code ApplicationClock.now(ZoneId)} is used instead.
   */
  public static final ArchRule LOCAL_DATE_TIME_NOW_WITH_ZONE_ID_IS_NOT_USED =
      noClasses()
          .that()
          .doNotHaveFullyQualifiedName(APPLICATION_CLOCK)
          .should()
          .callMethod(LocalDateTime.class, "now", ZoneId.class)
          .because(
              "LocalDateTime.now(ZoneId) bypasses the application clock - use "
                  + "digital.inception.core.time.ApplicationClock.now(ZoneId) instead");

  /**
   * The ArchUnit rule that verifies that {@link LocalDateTime#now(Clock)} is not used directly and
   * that {@code ApplicationClock.now()} or {@code ApplicationClock.getClock()} is used instead,
   * depending on the use case.
   */
  public static final ArchRule LOCAL_DATE_TIME_NOW_WITH_CLOCK_IS_NOT_USED =
      noClasses()
          .that()
          .doNotHaveFullyQualifiedName(APPLICATION_CLOCK)
          .should()
          .callMethod(LocalDateTime.class, "now", Clock.class)
          .because(
              "LocalDateTime.now(Clock) should not be used directly - use "
                  + "digital.inception.core.time.ApplicationClock.now() or "
                  + "digital.inception.core.time.ApplicationClock.getClock() instead");

  /**
   * The ArchUnit rule that verifies that {@link OffsetDateTime#now()} is not used directly and that
   * {@code ApplicationClock.offsetNow()} is used instead.
   */
  public static final ArchRule OFFSET_DATE_TIME_NOW_WITHOUT_ARGS_IS_NOT_USED =
      noClasses()
          .that()
          .doNotHaveFullyQualifiedName(APPLICATION_CLOCK)
          .should()
          .callMethod(OffsetDateTime.class, "now")
          .because(
              "OffsetDateTime.now() bypasses the application clock - use "
                  + "digital.inception.core.time.ApplicationClock.offsetNow() instead");

  /**
   * The ArchUnit rule that verifies that {@link OffsetDateTime#now(ZoneId)} is not used directly
   * and that {@code ApplicationClock.offsetNow(ZoneId)} is used instead.
   */
  public static final ArchRule OFFSET_DATE_TIME_NOW_WITH_ZONE_ID_IS_NOT_USED =
      noClasses()
          .that()
          .doNotHaveFullyQualifiedName(APPLICATION_CLOCK)
          .should()
          .callMethod(OffsetDateTime.class, "now", ZoneId.class)
          .because(
              "OffsetDateTime.now(ZoneId) bypasses the application clock - use "
                  + "digital.inception.core.time.ApplicationClock.offsetNow(ZoneId) instead");

  /**
   * The ArchUnit rule that verifies that {@link OffsetDateTime#now(Clock)} is not used directly and
   * that {@code ApplicationClock.offsetNow()} or {@code ApplicationClock.getClock()} is used
   * instead, depending on the use case.
   */
  public static final ArchRule OFFSET_DATE_TIME_NOW_WITH_CLOCK_IS_NOT_USED =
      noClasses()
          .that()
          .doNotHaveFullyQualifiedName(APPLICATION_CLOCK)
          .should()
          .callMethod(OffsetDateTime.class, "now", Clock.class)
          .because(
              "OffsetDateTime.now(Clock) should not be used directly - use "
                  + "digital.inception.core.time.ApplicationClock.offsetNow() or "
                  + "digital.inception.core.time.ApplicationClock.getClock() instead");

  private static final String JACKSON_ANNOTATION_PACKAGE = "com.fasterxml.jackson.annotation";

  private static final String LOMBOK_GENERATED_ANNOTATION = "lombok.Generated";

  /**
   * The ArchUnit rule that verifies that any public convenience constructor on a Jackson-annotated
   * class, which is not already annotated with {@code @JsonCreator} and is not a Lombok-generated
   * all-args constructor, is annotated with {@code @JsonCreator(mode = JsonCreator.Mode.DISABLED)}.
   */
  public static final ArchRule PUBLIC_CONVENIENCE_CONSTRUCTORS_DISABLE_JACKSON_CREATOR =
      classes()
          .that(areIntendedForJacksonJsonBinding())
          .should(
              new ArchCondition<>("have disabled Jackson on public convenience constructors") {
                @Override
                public void check(JavaClass javaClass, ConditionEvents events) {
                  for (JavaConstructor constructor : javaClass.getConstructors()) {
                    if (!constructor.getModifiers().contains(JavaModifier.PUBLIC)) {
                      continue;
                    }

                    if (isNoArgsConstructor(constructor)) {
                      continue;
                    }

                    if (hasJsonCreatorAnnotation(constructor)) {
                      continue;
                    }

                    if (isLombokGeneratedAllArgsConstructor(constructor)) {
                      continue;
                    }

                    String message =
                        constructor.getFullName()
                            + " is a public convenience constructor on a Jackson-annotated class and "
                            + "must be annotated with "
                            + "@JsonCreator(mode = JsonCreator.Mode.DISABLED)";
                    events.add(SimpleConditionEvent.violated(constructor, message));
                  }
                }
              })
          .because(
              "Jackson can treat public multi-argument convenience constructors as creators; on "
                  + "types intended for JSON binding, such constructors must explicitly disable "
                  + "Jackson creator use unless they are already explicitly annotated for Jackson "
                  + "or are the class's Lombok-generated all-args constructor");

  /** Constructs a new {@code CodingConventionsRules}. */
  public CodingConventionsRules() {}

  /**
   * Check that the ArchUnit rules are being adhered to.
   *
   * @param classes the Java classes to check
   */
  public static void check(JavaClasses classes) {
    try {
      Class.forName(APPLICATION_CLOCK);
    } catch (ClassNotFoundException e) {
      return;
    }

    LOCAL_DATE_TIME_NOW_WITHOUT_ARGS_IS_NOT_USED.check(classes);
    LOCAL_DATE_TIME_NOW_WITH_ZONE_ID_IS_NOT_USED.check(classes);
    LOCAL_DATE_TIME_NOW_WITH_CLOCK_IS_NOT_USED.check(classes);

    OFFSET_DATE_TIME_NOW_WITHOUT_ARGS_IS_NOT_USED.check(classes);
    OFFSET_DATE_TIME_NOW_WITH_ZONE_ID_IS_NOT_USED.check(classes);
    OFFSET_DATE_TIME_NOW_WITH_CLOCK_IS_NOT_USED.check(classes);

    PUBLIC_CONVENIENCE_CONSTRUCTORS_DISABLE_JACKSON_CREATOR.check(classes);
  }

  private static DescribedPredicate<JavaClass> areIntendedForJacksonJsonBinding() {
    return new DescribedPredicate<>("are intended for Jackson JSON binding") {
      @Override
      public boolean test(JavaClass javaClass) {
        return hasJacksonAnnotation(javaClass);
      }
    };
  }

  private static boolean isNoArgsConstructor(JavaConstructor constructor) {
    return constructor.getRawParameterTypes().isEmpty();
  }

  private static boolean hasJsonCreatorAnnotation(JavaConstructor constructor) {
    return constructor.isAnnotatedWith(JsonCreator.class);
  }

  private static boolean isLombokGeneratedAllArgsConstructor(JavaConstructor constructor) {
    return isLombokGenerated(constructor) && isAllArgsForDeclaredInstanceFields(constructor);
  }

  private static boolean isLombokGenerated(JavaConstructor constructor) {
    if (constructor.isAnnotatedWith(ConstructorProperties.class)) {
      return true;
    }

    for (JavaAnnotation<JavaConstructor> annotation : constructor.getAnnotations()) {
      if (annotation.getRawType().getName().equals(LOMBOK_GENERATED_ANNOTATION)) {
        return true;
      }
    }

    return false;
  }

  private static boolean isAllArgsForDeclaredInstanceFields(JavaConstructor constructor) {
    int declaredInstanceFieldCount = 0;

    for (JavaField field : constructor.getOwner().getFields()) {
      if (field.getModifiers().contains(JavaModifier.STATIC)) {
        continue;
      }

      if (field.getName().startsWith("$")) {
        continue;
      }

      declaredInstanceFieldCount++;
    }

    return constructor.getRawParameterTypes().size() == declaredInstanceFieldCount;
  }

  private static boolean hasJacksonAnnotation(JavaClass javaClass) {
    if (hasJacksonAnnotation(javaClass.getAnnotations())) {
      return true;
    }

    for (JavaField field : javaClass.getFields()) {
      if (hasJacksonAnnotation(field.getAnnotations())) {
        return true;
      }
    }

    for (JavaMethod method : javaClass.getMethods()) {
      if (hasJacksonAnnotation(method.getAnnotations())) {
        return true;
      }
    }

    for (JavaConstructor constructor : javaClass.getConstructors()) {
      if (hasJacksonAnnotation(constructor.getAnnotations())) {
        return true;
      }
    }

    return false;
  }

  private static boolean hasJacksonAnnotation(Iterable<? extends JavaAnnotation<?>> annotations) {
    for (JavaAnnotation<?> annotation : annotations) {
      String annotationTypeName = annotation.getRawType().getName();

      if (annotationTypeName.startsWith(JACKSON_ANNOTATION_PACKAGE + ".")) {
        return true;
      }
    }

    return false;
  }
}

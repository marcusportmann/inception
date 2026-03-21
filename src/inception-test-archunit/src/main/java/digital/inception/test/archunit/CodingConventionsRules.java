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

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
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
  }
}

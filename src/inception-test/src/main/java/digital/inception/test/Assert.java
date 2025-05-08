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

package digital.inception.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The {@code Assert} class provides additional assertions for JUnit tests.
 *
 * @author Marcus Portmann
 */
public final class Assert {

  /** Private constructor to prevent instantiation. */
  private Assert() {}

  /**
   * Asserts that two LocalDateTime values are equal.
   *
   * @param expected the expected value
   * @param actual the value to check
   * @param message the for message for the AssertionError
   */
  public static void assertEqualsToMillisecond(
      LocalDateTime expected, LocalDateTime actual, String message) {
    if ((expected != null) && (actual != null)) {
      assertEquals(
          expected.truncatedTo(ChronoUnit.MILLIS), actual.truncatedTo(ChronoUnit.MILLIS), message);
    } else {
      assertEquals(expected, actual, message);
    }
  }

  /**
   * Asserts that two Instant values are equal.
   *
   * @param expected the expected value
   * @param actual the value to check
   * @param message the for message for the AssertionError
   */
  public static void assertEqualsToMillisecond(Instant expected, Instant actual, String message) {
    if ((expected != null) && (actual != null)) {
      assertEquals(
          expected.truncatedTo(ChronoUnit.MILLIS), actual.truncatedTo(ChronoUnit.MILLIS), message);
    } else {
      assertEquals(expected, actual, message);
    }
  }

  /**
   * Asserts that two OffsetDateTime values are equal.
   *
   * @param expected the expected value
   * @param actual the value to check
   * @param message the for message for the AssertionError
   */
  public static void assertEqualsToMillisecond(
      OffsetDateTime expected, OffsetDateTime actual, String message) {
    if ((expected != null) && (actual != null)) {
      assertEquals(
          expected.truncatedTo(ChronoUnit.MILLIS), actual.truncatedTo(ChronoUnit.MILLIS), message);
    } else {
      assertEquals(expected, actual, message);
    }
  }
}

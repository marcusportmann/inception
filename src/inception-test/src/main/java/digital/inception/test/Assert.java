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

package digital.inception.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The <b>Assert</b> class provides additional assertions for JUnit tests.
 *
 * @author Marcus Portmann
 */
public class Assert {

  /**
   * Asserts that two LocalDateTime values are equal.
   *
   * @param message the for message for the AssertionError
   * @param expected the expected value
   * @param actual the value to check
   */
  public static void assertEqualsToMillisecond(
      String message, LocalDateTime expected, LocalDateTime actual) {
    if ((expected != null) && (actual != null)) {
      assertEquals(
          message, expected.truncatedTo(ChronoUnit.MILLIS), actual.truncatedTo(ChronoUnit.MILLIS));
    } else {
      assertEquals(message, expected, actual);
    }
  }
}

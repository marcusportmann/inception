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

package digital.inception.scheduler;

// ~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>DayOfMonthValueMatcher</code> class implements a <code>ValueMatcher</code> whose rules
 * are in a plain array of integer values.
 *
 * <p>When asked to validate a value, this ValueMatcher checks if it is in the array and, if not,
 * checks whether the last-day-of-month setting applies.
 *
 * @author Paul Fernley
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DayOfMonthValueMatcher extends IntArrayValueMatcher {

  private static final int[] lastDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  /**
   * Constructs a new <code>DayOfMonthValueMatcher</code>.
   *
   * @param integers The Integer elements, one for every value accepted by the matcher. The match()
   *     method will return <code>true</code> only if its parameter will be one of this list or the
   *     last-day-of-month setting applies.
   */
  public DayOfMonthValueMatcher(List<Integer> integers) {
    super(integers);
  }

  /**
   * Returns <code>true</code> if the given value is included in the matcher list or the
   * last-day-of-month setting applies otherwise <code>false</code>.
   *
   * @param value the value
   * @param month the month
   * @param isLeapYear <code>true</code> if this is a leap year <code>false</code> otherwise
   * @return <code>true</code> if the given value matches the rules of the <code>ValueMatcher</code>
   *     , <code>false</code> otherwise
   */
  public boolean match(int value, int month, boolean isLeapYear) {
    return (super.match(value)
        || ((value > 27) && match(32) && isLastDayOfMonth(value, month, isLeapYear)));
  }

  private boolean isLastDayOfMonth(int value, int month, boolean isLeapYear) {
    if (isLeapYear && (month == 2)) {
      return value == 29;
    } else {
      return value == lastDays[month - 1];
    }
  }
}

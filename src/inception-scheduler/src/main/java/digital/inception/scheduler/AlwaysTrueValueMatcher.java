/*
 * Copyright 2020 Marcus Portmann
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

/**
 * The <code>AlwaysTrueValueMatcher</code> class implements a <code>ValueMatcher</code> that always
 * returns <code>true</code>.
 *
 * @author Carlo Pelliccia
 * @author Marcus Portmann
 */
public class AlwaysTrueValueMatcher implements ValueMatcher {

  /**
   * Validate the given integer value against a set of rules.
   *
   * @param value the value
   * @return <code>true</code> if the given value matches the rules of the <code>ValueMatcher</code>
   *     , <code>false</code> otherwise
   */
  public boolean match(int value) {
    return true;
  }
}

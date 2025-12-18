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

package digital.inception.core.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The {@code CodeEnum} interface must be implemented by all custom Enum types that use snake case
 * code values, e.g. task_status, as the String representation for the Enum value.
 *
 * @author Marcus Portmann
 */
public interface CodeEnum {

  /**
   * Returns the enumeration value given by the specified snake case code value.
   *
   * @param <E> the enumeration type
   * @param enumType the class for the enumeration type
   * @param code the snake case code value for the enumeration value
   * @return the enumeration value given by the specified snake case code value
   */
  static <E extends Enum<E> & CodeEnum> E fromCode(Class<E> enumType, String code) {
    for (E value : enumType.getEnumConstants()) {
      // Compare ignoring case or do it however you like
      if (value.code().equalsIgnoreCase(code)) {
        return value;
      }
    }
    throw new IllegalArgumentException(
        "Failed to determine the enumeration value for the enumeration type ("
            + enumType.getSimpleName()
            + ") with the code value ("
            + code
            + ")");
  }

  /**
   * Returns the snake case code for the enumeration value.
   *
   * @return the snake case code for the enumeration value
   */
  @JsonValue
  String code();

  /**
   * Returns the description for the enumeration value.
   *
   * @return the description for the enumeration value
   */
  String description();
}

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

import com.fasterxml.jackson.annotation.JsonValue;
import digital.inception.core.model.CodeEnum;

/**
 * The {@code TestEnum} enumeration.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public enum TestEnum implements CodeEnum {

  /** Option1. */
  OPTION1("option1", "Option1"),

  /** Option2. */
  OPTION2("option2", "Option2");

  private final String code;

  private final String description;

  TestEnum(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the test enum.
   *
   * @return the code for the test enum
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the test enum.
   *
   * @return the description for the test enum
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the enumeration value.
   *
   * @return the string representation of the enumeration value
   */
  public String toString() {
    return description;
  }
}

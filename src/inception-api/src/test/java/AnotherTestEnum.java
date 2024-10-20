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


/**
 * The <b>AnotherTestEnum</b> enumeration.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public enum AnotherTestEnum {

  /** Option1. */
  OPTION1("option1", "Option1"),

  /** Option2. */
  OPTION2("option2", "Option2");

  private final String code;

  private final String description;

  AnotherTestEnum(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the another test enum given by the specified code value.
   *
   * @param code the code for the another test enum
   * @return the another test enum given by the specified code value
   */
  public static AnotherTestEnum fromCode(String code) {
    for (AnotherTestEnum value : AnotherTestEnum.values()) {
      if (value.code.equalsIgnoreCase(code)) {
        return value;
      }
    }
    throw new RuntimeException(
        "Failed to determine the another test enum with the invalid code (" + code + ")");
  }

  /**
   * Returns the code for the another test enum.
   *
   * @return the code for the another test enum
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the another test enum.
   *
   * @return the description for the another test enum
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the another test enum enumeration value.
   *
   * @return the string representation of the another test enum enumeration value
   */
  public String toString() {
    return description;
  }
}

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

package digital.inception.executor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The enumeration giving the possible priorities for a task.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The task priority")
@XmlEnum
@XmlType(name = "TaskPriority", namespace = "https://inception.digital/executor")
public enum TaskPriority {

  /** Low. */
  @XmlEnumValue("Low")
  LOW(20, "low", "Low"),

  /** Normal. */
  @XmlEnumValue("Normal")
  NORMAL(10, "normal", "Normal"),

  /** High. */
  @XmlEnumValue("High")
  HIGH(5, "high", "High"),

  /** Critical. */
  @XmlEnumValue("Critical")
  CRITICAL(1, "critical", "Critical");

  private final String code;

  private final int dbCode;

  private final String description;

  TaskPriority(int dbCode, String code, String description) {
    this.dbCode = dbCode;
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the task priority given by the specified code value.
   *
   * @param code the code for the task priority
   * @return the task priority given by the specified code value
   */
  @JsonCreator
  public static TaskPriority fromCode(String code) {
    return switch (code) {
      case "low" -> TaskPriority.LOW;
      case "normal" -> TaskPriority.NORMAL;
      case "high" -> TaskPriority.HIGH;
      case "critical" -> TaskPriority.CRITICAL;
      default ->
          throw new RuntimeException(
              "Failed to determine the task priority with the invalid code (" + code + ")");
    };
  }

  /**
   * Returns the task priority given by the specified database code value.
   *
   * @param dbCode the database code for the task priority
   * @return the task priority given by the specified database code value
   */
  public static TaskPriority fromDbCode(int dbCode) {
    return switch (dbCode) {
      case 20 -> TaskPriority.LOW;
      case 10 -> TaskPriority.NORMAL;
      case 5 -> TaskPriority.HIGH;
      case 1 -> TaskPriority.CRITICAL;
      default ->
          throw new RuntimeException(
              "Failed to determine the task priority with the invalid database code ("
                  + dbCode
                  + ")");
    };
  }

  /**
   * Returns the code for the task priority.
   *
   * @return the code for the task priority
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the database code for the task priority.
   *
   * @return the database code for the task priority
   */
  public int dbCode() {
    return dbCode;
  }

  /**
   * Returns the description for the task priority.
   *
   * @return the description for the task priority
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the task priority enumeration value.
   *
   * @return the string representation of the task priority enumeration value
   */
  public String toString() {
    return description;
  }
}

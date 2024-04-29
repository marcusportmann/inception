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
 * The <b>TaskSortBy</b> enumeration defines the possible methods used to sort a list of tasks.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The method used to sort the list of tasks")
@XmlEnum
@XmlType(name = "TaskSortBy", namespace = "https://inception.digital/executor")
public enum TaskSortBy {

  /** Sort by queued. */
  @XmlEnumValue("Queued")
  QUEUED("queued", "Sort By Queued"),

  /** Sort by type. */
  @XmlEnumValue("Type")
  TYPE("type", "Sort By Type");

  private final String code;

  private final String description;

  TaskSortBy(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the method used to sort a list of tasks given by the specified code value.
   *
   * @param code the code for the method used to sort a list of tasks
   * @return the method used to sort a list of tasks given by the specified code value
   */
  @JsonCreator
  public static TaskSortBy fromCode(String code) {
    return switch (code) {
      case "queued" -> TaskSortBy.QUEUED;
      case "type" -> TaskSortBy.TYPE;
      default ->
          throw new RuntimeException(
              "Failed to determine the task sort by with the invalid code (" + code + ")");
    };
  }

  /**
   * Returns the code for the method used to sort a list of tasks.
   *
   * @return the code for the method used to sort a list of tasks
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the method used to sort a list of tasks.
   *
   * @return the description for the method used to sort a list of tasks
   */
  public String description() {
    return description;
  }

  /**
   * Return the string representation of the method used to sort a list of tasks enumeration value.
   *
   * @return the string representation of the method used to sort a list of tasks enumeration value
   */
  public String toString() {
    return description;
  }
}

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

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The enumeration giving the possible statuses for a task.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The task status")
@XmlEnum
@XmlType(name = "TaskStatus", namespace = "https://inception.digital/executor")
public enum TaskStatus implements CodeEnum {

  /** Queued. */
  @XmlEnumValue("Queued")
  QUEUED("queued", "Queued"),

  /** Executing. */
  @XmlEnumValue("Executing")
  EXECUTING("executing", "Executing"),

  /** Completed. */
  @XmlEnumValue("Completed")
  COMPLETED("completed", "Completed"),

  /** Failed. */
  @XmlEnumValue("Failed")
  FAILED("failed", "Failed"),

  /** Cancelled. */
  @XmlEnumValue("Cancelled")
  CANCELLED("cancelled", "Cancelled"),

  /** Suspended. */
  @XmlEnumValue("Suspended")
  SUSPENDED("suspended", "Suspended");

  private final String code;

  private final String description;

  TaskStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the task status.
   *
   * @return the code for the task status
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the task status.
   *
   * @return the description for the task status
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

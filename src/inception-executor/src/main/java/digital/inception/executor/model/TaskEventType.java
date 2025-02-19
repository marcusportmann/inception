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
 * The enumeration giving the possible task event types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The task event type")
@XmlEnum
@XmlType(name = "TaskEventType", namespace = "https://inception.digital/executor")
public enum TaskEventType implements CodeEnum {

  /** Step Completed. */
  @XmlEnumValue("StepCompleted")
  STEP_COMPLETED("step_completed", "Step Completed"),

  /** Task Completed. */
  @XmlEnumValue("TaskCompleted")
  TASK_COMPLETED("task_completed", "Task Completed"),

  /** Task Failed. */
  @XmlEnumValue("TaskFailed")
  TASK_FAILED("task_failed", "Task Failed");

  private final String code;

  private final String description;

  TaskEventType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the task event type.
   *
   * @return the code for the task event type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the task event type.
   *
   * @return the description for the task event type
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

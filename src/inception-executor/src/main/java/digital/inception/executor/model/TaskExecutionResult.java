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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;

/**
 * The <b>TaskExecutionResult</b> class holds the result of executing a task.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The result of executing a task")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"nextTaskStep", "updatedTaskData"})
@XmlRootElement(name = "TaskExecutionResult", namespace = "https://inception.digital/executor")
@XmlType(
    name = "TaskExecutionResult",
    namespace = "https://inception.digital/executor",
    propOrder = {"nextTaskStep", "updatedTaskData"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class TaskExecutionResult implements Serializable {

  /** The empty task execution result. */
  @JsonIgnore @XmlTransient
  public static final TaskExecutionResult EMPTY_TASK_EXECUTION_RESULT = new TaskExecutionResult();

  @Serial private static final long serialVersionUID = 1000000;

  /** The next task step for a multistep task. */
  @Schema(description = "The next task step for a multistep task")
  @JsonProperty
  @XmlElement(name = "NextTaskStep")
  @Size(min = 1, max = 50)
  private TaskStep nextTaskStep;

  /** The updated task data. */
  @Schema(description = "The updated task data")
  @JsonProperty
  @XmlElement(name = "UpdatedTaskData")
  @Size(min = 1, max = 10485760)
  private String updatedTaskData;

  /** Constructs a new <b>TaskExecutionResult</b>. */
  public TaskExecutionResult() {}

  /**
   * Constructs a new <b>TestExecutionResult</b>.
   *
   * @param nextTaskStep the next task step for a multistep task
   */
  public TaskExecutionResult(TaskStep nextTaskStep) {
    this.nextTaskStep = nextTaskStep;
  }

  /**
   * Constructs a new <b>TestExecutionResult</b>.
   *
   * @param nextTaskStep the next task step for a multistep task
   * @param updatedTaskData the updated task data
   */
  public TaskExecutionResult(TaskStep nextTaskStep, String updatedTaskData) {
    this.nextTaskStep = nextTaskStep;
    this.updatedTaskData = updatedTaskData;
  }

  /**
   * Returns the next task step for a multistep task.
   *
   * @return the next task step for a multistep task
   */
  public TaskStep getNextTaskStep() {
    return nextTaskStep;
  }

  /**
   * Returns the updated task data.
   *
   * @return the updated task data
   */
  public String getUpdatedTaskData() {
    return updatedTaskData;
  }
}

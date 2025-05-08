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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code ExecuteTaskRequest} class holds the information for a request to execute a task
 * synchronously.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to execute a task synchronously")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"taskType", "taskData"})
@XmlRootElement(name = "ExecuteTaskRequest", namespace = "https://inception.digital/executor")
@XmlType(
    name = "ExecuteTaskRequest",
    namespace = "https://inception.digital/executor",
    propOrder = {"taskType", "taskData"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class ExecuteTaskRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The task data. */
  @Schema(description = "The task data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TaskData", required = true)
  @NotNull
  @Size(min = 1, max = 10485760)
  private String taskData;

  /** The code for the task type. */
  @Schema(description = "The code for the task type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TaskType", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String taskType;

  /** Creates a new {@code ExecuteTaskRequest} instance. */
  public ExecuteTaskRequest() {}

  /**
   * Creates a new {@code ExecuteTaskRequest} instance.
   *
   * @param taskType the code for the task type
   * @param taskData the task data
   */
  public ExecuteTaskRequest(String taskType, String taskData) {
    this.taskType = taskType;
    this.taskData = taskData;
  }

  /**
   * Returns the task data.
   *
   * @return the task data
   */
  public String getTaskData() {
    return taskData;
  }

  /**
   * Returns the code for the task type.
   *
   * @return the code for the task type
   */
  public String getTaskType() {
    return taskType;
  }

  /**
   * Set the task data.
   *
   * @param taskData the task data
   */
  public void setTaskData(String taskData) {
    this.taskData = taskData;
  }

  /**
   * Set the code for the task type.
   *
   * @param taskType the code for the task type
   */
  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }
}

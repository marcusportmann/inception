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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code TaskStep} class holds the information for a task step.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A task step")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "name", "postExecutionDelay"})
@XmlRootElement(name = "TaskStep", namespace = "https://inception.digital/executor")
@XmlType(
    name = "TaskStep",
    namespace = "https://inception.digital/executor",
    propOrder = {"code", "name", "postExecutionDelay"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class TaskStep implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the task step. */
  @Schema(description = "The code for the task step", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @Size(min = 1, max = 50)
  private String code;

  /** The name of the task step. */
  @Schema(description = "The name of the task step", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @Size(min = 1, max = 100)
  private String name;

  /** The delay in milliseconds before the next task step for a multistep task is executed. */
  @Schema(
      description =
          "The delay in milliseconds before the next task step for a multistep task is executed",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PostExecutionDelay", required = true)
  private long postExecutionDelay;

  /**
   * Creates a new {@code TaskStep} instance.
   *
   * @param code the code for the task step
   * @param name the name of the task step
   */
  public TaskStep(String code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * Creates a new {@code TaskStep} instance.
   *
   * @param code the code for the task step
   * @param name the name of the task step
   * @param postExecutionDelay the delay in milliseconds before the next task step for a multistep
   *     task is executed
   */
  public TaskStep(String code, String name, long postExecutionDelay) {
    this.code = code;
    this.name = name;
    this.postExecutionDelay = postExecutionDelay;
  }

  /**
   * Returns the code for the task step.
   *
   * @return the code for the task step
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the name of the task step.
   *
   * @return the name of the task step
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the delay in milliseconds before the next task step for a multistep task is executed.
   *
   * @return the delay in milliseconds before the next task step for a multistep task is executed
   */
  public long getPostExecutionDelay() {
    return postExecutionDelay;
  }

  /**
   * Set the code for the task step.
   *
   * @param code the code for the task step
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the name of the task step.
   *
   * @param name the name of the task step
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the delay in milliseconds before the next task step for a multistep task is executed.
   *
   * @param postExecutionDelay the delay in milliseconds before the next task step for a multistep
   *     task is executed
   */
  public void setPostExecutionDelay(long postExecutionDelay) {
    this.postExecutionDelay = postExecutionDelay;
  }
}

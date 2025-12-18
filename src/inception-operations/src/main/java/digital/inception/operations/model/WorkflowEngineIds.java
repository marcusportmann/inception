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

package digital.inception.operations.model;

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
 * The {@code WorkflowEngineIds} class holds the workflow engine IDs for a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The workflow engine IDs for a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "engineId",
  "engineInstanceId",
})
@XmlRootElement(name = "WorkflowEngineIds", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowEngineIds",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "engineId",
      "engineInstanceId",
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class WorkflowEngineIds implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the workflow engine the workflow is associated with. */
  @Schema(
      description = "The ID for the workflow engine the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EngineId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String engineId;

  /**
   * The ID for the corresponding process or case instance in the workflow engine for the workflow.
   */
  @Schema(
      description =
          "The ID for the corresponding process or case instance in the workflow engine for the workflow",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EngineInstanceId", required = true)
  @Size(min = 1, max = 100)
  private String engineInstanceId;

  /** Constructs a new {@code WorkflowEngineIds}. */
  public WorkflowEngineIds() {}

  /**
   * Constructs a new {@code WorkflowEngineIds}.
   *
   * @param engineId the ID for the workflow engine the workflow is associated with
   * @param engineInstanceId the ID for the parent workflow
   */
  public WorkflowEngineIds(String engineId, String engineInstanceId) {
    this.engineId = engineId;
    this.engineInstanceId = engineInstanceId;
  }

  /**
   * Returns the ID for the workflow engine the workflow is associated with.
   *
   * @return the ID for the workflow engine the workflow is associated with
   */
  public String getEngineId() {
    return engineId;
  }

  /**
   * Returns the ID for the corresponding process or case instance in the workflow engine for the
   * workflow.
   *
   * @return the ID for the corresponding process or case instance in the workflow engine for the
   *     workflow
   */
  public String getEngineInstanceId() {
    return engineInstanceId;
  }

  /**
   * Sets the ID for the workflow engine the workflow is associated with.
   *
   * @param engineId the ID for the workflow engine the workflow is associated with
   */
  public void setEngineId(String engineId) {
    this.engineId = engineId;
  }

  /**
   * Sets the ID for the corresponding process or case instance in the workflow engine for the
   * workflow.
   *
   * @param engineInstanceId the ID for the corresponding process or case instance in the workflow
   *     engine for the workflow
   */
  public void setEngineInstanceId(String engineInstanceId) {
    this.engineInstanceId = engineInstanceId;
  }
}

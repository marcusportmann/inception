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
import java.util.UUID;

/**
 * The {@code DeleteWorkflowStepRequest} class represents a request to delete a workflow step.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to delete a workflow step")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowId", "step"})
@XmlRootElement(
    name = "DeleteWorkflowStepRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "DeleteWorkflowStepRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowId", "step"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class DeleteWorkflowStepRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the workflow step. */
  @Schema(
      description = "The code for the workflow step",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Step", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String step;

  /** The ID for the workflow the workflow step is associated with. */
  @Schema(
      description = "The ID for the workflow the workflow step is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  private UUID workflowId;

  /** Constructs a new {@code DeleteWorkflowStepRequest}. */
  public DeleteWorkflowStepRequest() {}

  /**
   * Constructs a new {@code DeleteWorkflowStepRequest}.
   *
   * @param workflowId the ID for the workflow the workflow step is associated with
   * @param step the code for the workflow step
   */
  public DeleteWorkflowStepRequest(UUID workflowId, String step) {
    this.workflowId = workflowId;
    this.step = step;
  }

  /**
   * Returns the code for the workflow step.
   *
   * @return the code for the workflow step
   */
  public String getStep() {
    return step;
  }

  /**
   * Returns the ID for the workflow the workflow step is associated with.
   *
   * @return the ID for the workflow the workflow step is associated with
   */
  public UUID getWorkflowId() {
    return workflowId;
  }

  /**
   * Sets the code for the workflow step.
   *
   * @param step the code for the workflow step
   */
  public void setStep(String step) {
    this.step = step;
  }

  /**
   * Sets the ID for the workflow the workflow step is associated with.
   *
   * @param workflowId the ID for the workflow the workflow step is associated with
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}

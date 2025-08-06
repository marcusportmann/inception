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
 * The {@code FinalizeWorkflowStepRequest} class represents a request to finalize a workflow step.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to finalize a workflow step")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowId", "step", "status"})
@XmlRootElement(
    name = "FinalizeWorkflowStepRequest",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "FinalizeWorkflowStepRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowId", "step", "status"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class FinalizeWorkflowStepRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The status for the workflow step. */
  @Schema(
      description = "The status for the workflow step",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  private WorkflowStepStatus status;

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

  /** Constructs a new {@code FinalizeWorkflowStepRequest}. */
  public FinalizeWorkflowStepRequest() {}

  /**
   * Constructs a new {@code FinalizeWorkflowStepRequest}.
   *
   * @param workflowId the ID for the workflow the workflow step is associated with
   * @param step the code for the workflow step
   * @param status the status for the workflow step
   */
  public FinalizeWorkflowStepRequest(UUID workflowId, String step, WorkflowStepStatus status) {
    this.workflowId = workflowId;
    this.step = step;
    this.status = status;
  }

  /**
   * Returns the status for the workflow step.
   *
   * @return the status for the workflow step
   */
  public WorkflowStepStatus getStatus() {
    return status;
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
   * Set the status for the workflow step.
   *
   * @param status the status for the workflow step
   */
  public void setStatus(WorkflowStepStatus status) {
    this.status = status;
  }

  /**
   * Set the code for the workflow step.
   *
   * @param step the code for the workflow step
   */
  public void setStep(String step) {
    this.step = step;
  }

  /**
   * Set the ID for the workflow the workflow step is associated with.
   *
   * @param workflowId the ID for the workflow the workflow step is associated with
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}

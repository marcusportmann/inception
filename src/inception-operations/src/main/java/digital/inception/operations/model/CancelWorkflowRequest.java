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
 * The {@code CancelWorkflowRequest} class represents a request to cancel a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to cancel a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowId", "cancellationReason"})
@XmlRootElement(name = "CancelWorkflowRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "CancelWorkflowRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowId", "cancellationReason"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class CancelWorkflowRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The reason the workflow was canceled. */
  @Schema(
      description = "The reason the workflow was canceled",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CancellationReason", required = true)
  @NotNull
  @Size(max = 200)
  private String cancellationReason;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  private UUID workflowId;

  /** Constructs a new {@code CancelWorkflowRequest}. */
  public CancelWorkflowRequest() {}

  /**
   * Constructs a new {@code CancelWorkflowRequest}.
   *
   * @param workflowId the ID for the workflow
   * @param cancellationReason the reason the workflow was canceled
   */
  public CancelWorkflowRequest(UUID workflowId, String cancellationReason) {
    this.workflowId = workflowId;
    this.cancellationReason = cancellationReason;
  }

  /**
   * Return the reason the workflow was canceled.
   *
   * @return the reason the workflow was canceled
   */
  public String getCancellationReason() {
    return cancellationReason;
  }

  /**
   * Returns the ID for the workflow.
   *
   * @return the ID for the workflow
   */
  public UUID getWorkflowId() {
    return workflowId;
  }

  /**
   * Sets the reason the workflow was canceled.
   *
   * @param cancellationReason the reason the workflow was canceled
   */
  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  /**
   * Sets the ID for the workflow.
   *
   * @param workflowId the ID for the workflow
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}

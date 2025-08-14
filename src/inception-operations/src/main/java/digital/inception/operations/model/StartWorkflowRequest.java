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
import digital.inception.operations.constraint.ValidInitiateWorkflowRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * The {@code StartWorkflowRequest} class represents a request to start a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to start a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowId"})
@XmlRootElement(name = "StartWorkflowRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "StartWorkflowRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowId"})
@XmlAccessorType(XmlAccessType.FIELD)
@ValidInitiateWorkflowRequest
@SuppressWarnings({"unused", "WeakerAccess"})
public class StartWorkflowRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  private UUID workflowId;

  /** Constructs a new {@code StartWorkflowRequest}. */
  public StartWorkflowRequest() {}

  /**
   * Constructs a new {@code StartWorkflowRequest}.
   *
   * @param workflowId the ID for the workflow
   */
  public StartWorkflowRequest(UUID workflowId) {
    this.workflowId = workflowId;
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
   * Set the ID for the workflow
   *
   * @param workflowId the ID for the workflow
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}

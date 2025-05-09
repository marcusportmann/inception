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
import digital.inception.operations.constraint.ValidUpdateWorkflowRequest;
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

/** The {@code UpdateWorkflowRequest} class represents a request to update a workflow. */
@Schema(description = "A request to update a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "status", "data"})
@XmlRootElement(name = "UpdateWorkflowRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "UpdateWorkflowRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "status", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@ValidUpdateWorkflowRequest
@SuppressWarnings({"unused", "WeakerAccess"})
public class UpdateWorkflowRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The updated data for the workflow. */
  @Schema(description = "The updated data for the workflow")
  @JsonProperty
  @XmlElement(name = "Data")
  @Size(min = 1, max = 10485760)
  private String data;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /** The updated status of the workflow. */
  @Schema(description = "The updated status of the workflow")
  @JsonProperty
  @XmlElement(name = "Status")
  private WorkflowStatus status;

  /** Constructs a new {@code UpdateWorkflowRequest}. */
  public UpdateWorkflowRequest() {}

  /**
   * Constructs a new {@code UpdateWorkflowRequest}.
   *
   * @param id the ID for the workflow
   * @param data the updated data for the workflow
   */
  public UpdateWorkflowRequest(UUID id, String data) {
    this.id = id;
    this.data = data;
  }

  /**
   * Constructs a new {@code UpdateWorkflowRequest}.
   *
   * @param id the ID for the workflow
   * @param status the updated status of the workflow
   */
  public UpdateWorkflowRequest(UUID id, WorkflowStatus status) {
    this.id = id;
    this.status = status;
  }

  /**
   * Constructs a new {@code UpdateWorkflowRequest}.
   *
   * @param id the ID for the workflow
   * @param status the updated status of the workflow
   * @param data the updated data for the workflow
   */
  public UpdateWorkflowRequest(UUID id, WorkflowStatus status, String data) {
    this.id = id;
    this.status = status;
    this.data = data;
  }

  /**
   * Returns the updated data for the workflow.
   *
   * @return the updated data for the workflow
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the ID for the workflow.
   *
   * @return the ID for the workflow
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the updated status of the workflow.
   *
   * @return the updated status of the workflow
   */
  public WorkflowStatus getStatus() {
    return status;
  }

  /**
   * Set the updated data for the workflow.
   *
   * @param data the updated data for the workflow
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the workflow.
   *
   * @param id the ID for the workflow
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the updated status of the workflow.
   *
   * @param status the updated status of the workflow
   */
  public void setStatus(WorkflowStatus status) {
    this.status = status;
  }
}

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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The {@code UpdateWorkflowRequest} class represents a request to update a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to update a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"workflowId", "status", "attributes", "variables", "data"})
@XmlRootElement(name = "UpdateWorkflowRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "UpdateWorkflowRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"workflowId", "status", "attributes", "variables", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class UpdateWorkflowRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the workflow. */
  @Schema(description = "The attributes for the workflow")
  @JsonProperty
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  private List<WorkflowAttribute> attributes;

  /** The updated XML or JSON data for the workflow. */
  @Schema(description = "The updated XML or JSON data for the workflow")
  @JsonProperty
  @XmlElement(name = "Data")
  @Size(min = 1, max = 10485760)
  private String data;

  /** The updated status of the workflow. */
  @Schema(description = "The updated status of the workflow")
  @JsonProperty
  @XmlElement(name = "Status")
  private WorkflowStatus status;

  /** The variables for the workflow. */
  @Schema(description = "The variables for the workflow")
  @JsonProperty
  @XmlElementWrapper(name = "Variables")
  @XmlElement(name = "Variable")
  @Valid
  private List<WorkflowVariable> variables;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  private UUID workflowId;

  /** Constructs a new {@code UpdateWorkflowRequest}. */
  public UpdateWorkflowRequest() {}

  /**
   * Constructs a new {@code UpdateWorkflowRequest}.
   *
   * @param workflowId the ID for the workflow
   * @param data the updated XML or JSON data for the workflow
   */
  public UpdateWorkflowRequest(UUID workflowId, String data) {
    this.workflowId = workflowId;
    this.data = data;
  }

  /**
   * Constructs a new {@code UpdateWorkflowRequest}.
   *
   * @param workflowId the ID for the workflow
   * @param attributes the attributes for the workflow
   * @param variables the variables for the workflow
   */
  public UpdateWorkflowRequest(
      UUID workflowId, List<WorkflowAttribute> attributes, List<WorkflowVariable> variables) {
    this.workflowId = workflowId;
    this.attributes = attributes;
    this.variables = variables;
  }

  /**
   * Constructs a new {@code UpdateWorkflowRequest}.
   *
   * @param workflowId the ID for the workflow
   * @param status the updated status of the workflow
   */
  public UpdateWorkflowRequest(UUID workflowId, WorkflowStatus status) {
    this.workflowId = workflowId;
    this.status = status;
  }

  /**
   * Constructs a new {@code UpdateWorkflowRequest}.
   *
   * @param workflowId the ID for the workflow
   * @param attributes the attributes for the workflow
   */
  public UpdateWorkflowRequest(UUID workflowId, List<WorkflowAttribute> attributes) {
    this.workflowId = workflowId;
    this.attributes = attributes;
  }

  /**
   * Constructs a new {@code UpdateWorkflowRequest}.
   *
   * @param workflowId the ID for the workflow
   * @param status the updated status of the workflow
   * @param attributes the attributes for the workflow
   * @param data the updated XML or JSON data for the workflow
   */
  public UpdateWorkflowRequest(
      UUID workflowId, WorkflowStatus status, List<WorkflowAttribute> attributes, String data) {
    this.workflowId = workflowId;
    this.status = status;
    this.attributes = attributes;
    this.data = data;
  }

  /**
   * Returns the attributes for the workflow.
   *
   * @return the attributes for the workflow
   */
  public List<WorkflowAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Returns the updated XML or JSON data for the workflow.
   *
   * @return the updated XML or JSON data for the workflow
   */
  public String getData() {
    return data;
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
   * Returns the variables for the workflow.
   *
   * @return the variables for the workflow
   */
  public List<WorkflowVariable> getVariables() {
    return variables;
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
   * Set the attributes for the workflow.
   *
   * @param attributes the attributes for the workflow
   */
  public void setAttributes(List<WorkflowAttribute> attributes) {
    this.attributes = attributes;
  }

  /**
   * Set the updated XML or JSON data for the workflow.
   *
   * @param data the updated XML or JSON data for the workflow
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the updated status of the workflow.
   *
   * @param status the updated status of the workflow
   */
  public void setStatus(WorkflowStatus status) {
    this.status = status;
  }

  /**
   * Set the variables for the workflow.
   *
   * @param variables the variables for the workflow
   */
  public void setVariables(List<WorkflowVariable> variables) {
    this.variables = variables;
  }

  /**
   * Set the ID for the workflow.
   *
   * @param workflowId the ID for the workflow
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}

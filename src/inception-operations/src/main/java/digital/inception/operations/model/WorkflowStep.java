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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code WorkflowStep} class holds the information for a step for a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A step for a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "initiated", "completed"})
@XmlRootElement(name = "WorkflowStep", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowStep",
    namespace = "https://inception.digital/operations",
    propOrder = {"code", "initiated", "completed"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_steps")
@IdClass(WorkflowStepId.class)
public class WorkflowStep implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the workflow step. */
  @Schema(
      description = "The code for the workflow step",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The workflow the workflow step is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("workflowStepReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({@JoinColumn(name = "workflow_id")})
  private Workflow workflow;

  /** Constructs a new {@code WorkflowStep}. */
  public WorkflowStep() {}

  /**
   * Constructs a new {@code WorkflowStep}.
   *
   * @param code the code for the workflow step
   */
  public WorkflowStep(String code) {
    this.code = code;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    WorkflowStep other = (WorkflowStep) object;

    return Objects.equals(workflow, other.workflow)
        && StringUtil.equalsIgnoreCase(code, other.code);
  }

  /**
   * Returns the code for the workflow step.
   *
   * @return the code for the workflow step
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the workflow the workflow step is associated with.
   *
   * @return the workflow the workflow step is associated with
   */
  @Schema(hidden = true)
  public Workflow getWorkflow() {
    return workflow;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((workflow == null) ? 0 : workflow.hashCode()) + ((code == null) ? 0 : code.hashCode());
  }

  /**
   * Set the code for the workflow step.
   *
   * @param code the code for the workflow step
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the workflow the workflow step is associated with.
   *
   * @param workflow the workflow the workflow step is associated with
   */
  @Schema(hidden = true)
  public void setWorkflow(Workflow workflow) {
    this.workflow = workflow;
  }
}

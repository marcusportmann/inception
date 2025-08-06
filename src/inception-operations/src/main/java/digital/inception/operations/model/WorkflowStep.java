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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.StringUtil;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code WorkflowStep} class holds the information for a workflow step.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow step")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "status", "initiated", "finalized"})
@XmlRootElement(name = "WorkflowStep", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowStep",
    namespace = "https://inception.digital/operations",
    propOrder = {"code", "status", "initiated", "finalized"})
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

  /** The date and time the workflow step was finalized. */
  @Schema(description = "The date and time the workflow step was finalized")
  @JsonProperty
  @XmlElement(name = "Finalized")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "finalized")
  private OffsetDateTime finalized;

  /** The date and time the workflow was initiated. */
  @Schema(
      description = "The date and time the workflow step was initiated",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Initiated", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "initiated", nullable = false)
  private OffsetDateTime initiated;

  /** The status of the workflow step. */
  @Schema(
      description = "The status of the workflow step",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private WorkflowStepStatus status;

  /** The ID for the workflow the workflow step is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "workflow_id", nullable = false)
  private UUID workflowId;

  /** Constructs a new {@code WorkflowStep}. */
  public WorkflowStep() {}

  /**
   * Constructs a new {@code WorkflowStep}.
   *
   * @param workflowId the ID for the workflow the workflow step is associated with
   * @param code the code for the workflow step
   * @param status the status of the workflow step
   * @param initiated the date and time the workflow was initiated
   */
  public WorkflowStep(
      UUID workflowId, String code, WorkflowStepStatus status, OffsetDateTime initiated) {
    this.workflowId = workflowId;
    this.code = code;
    this.status = status;
    this.initiated = initiated;
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

    return Objects.equals(workflowId, other.workflowId)
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
   * Returns the date and time the workflow step was finalized.
   *
   * @return the date and time the workflow step was finalized
   */
  public OffsetDateTime getFinalized() {
    return finalized;
  }

  /**
   * Returns the date and time the workflow was initiated.
   *
   * @return the date and time the workflow was initiated
   */
  public OffsetDateTime getInitiated() {
    return initiated;
  }

  /**
   * Returns the status of the workflow step.
   *
   * @return the status of the workflow step
   */
  public WorkflowStepStatus getStatus() {
    return status;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((workflowId == null) ? 0 : workflowId.hashCode())
        + ((code == null) ? 0 : code.hashCode());
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
   * Set the date and time the workflow step was finalized.
   *
   * @param finalized the date and time the workflow step was finalized
   */
  public void setFinalized(OffsetDateTime finalized) {
    this.finalized = finalized;
  }

  /**
   * Set the date and time the workflow was initiated.
   *
   * @param initiated the date and time the workflow was initiated
   */
  public void setInitiated(OffsetDateTime initiated) {
    this.initiated = initiated;
  }

  /**
   * Set the status of the workflow step.
   *
   * @param status the status of the workflow step
   */
  public void setStatus(WorkflowStepStatus status) {
    this.status = status;
  }

  /**
   * Set the workflow the workflow step is associated with.
   *
   * @param workflow the workflow the workflow step is associated with
   */
  @JsonBackReference("workflowStepReference")
  @Schema(hidden = true)
  public void setWorkflow(Workflow workflow) {
    if (workflow != null) {
      this.workflowId = workflow.getId();
    } else {
      this.workflowId = null;
    }
  }

  /**
   * Called by the JAXB runtime an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof Workflow parent) {
      setWorkflow(parent);
    }
  }
}

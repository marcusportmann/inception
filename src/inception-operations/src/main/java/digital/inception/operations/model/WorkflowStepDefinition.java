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
import digital.inception.core.validation.constraint.ValidISO8601DurationOrPeriod;
import digital.inception.jpa.StringListAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.util.StringUtils;

/**
 * The {@code WorkflowStepDefinition} class encapsulates the definition of a step within a workflow.
 *
 * <p>A workflow step represents a distinct unit of work—such as a milestone, task, or decision
 * point— that contributes to achieving a specific outcome or completing a particular phase of an
 * overall workflow.
 *
 * <p>Each step may involve human or system activities, and exists within the broader context of an
 * end-to-end workflow composed of multiple such steps. A step typically transitions from an initial
 * state (e.g., "Initiated") to a terminal state (e.g., "Completed"), forming part of the structured
 * execution path of the workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow step definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "sequence",
  "code",
  "name",
  "description",
  "external",
  "internal",
  "optional",
  "previousSteps",
  "nextSteps",
  "timeToComplete",
})
@XmlRootElement(name = "WorkflowStepDefinition", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowStepDefinition",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "sequence",
      "code",
      "name",
      "description",
      "external",
      "internal",
      "optional",
      "previousSteps",
      "nextSteps",
      "timeToComplete"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_step_definitions")
@IdClass(WorkflowStepDefinitionId.class)
public class WorkflowStepDefinition implements Serializable {

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

  /** The ID for the workflow definition the workflow step definition is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "definition_id", nullable = false)
  private String definitionId;

  /** The version of the workflow definition the workflow step definition is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "definition_version", nullable = false)
  private int definitionVersion;

  /** The description for the workflow step. */
  @Schema(
      description = "The description for the workflow step",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** Is this workflow step completed by one or more external users? */
  @Schema(
      description = "Is this workflow step completed by one or more external users",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "External", required = true)
  @NotNull
  @Column(name = "external", nullable = false)
  private Boolean external;

  /** Is this workflow step internal-only and excluded for external users? */
  @Schema(
      description = "Is this workflow step internal-only and excluded for external users",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Internal", required = true)
  @NotNull
  @Column(name = "internal", nullable = false)
  private Boolean internal;

  /** The name of the workflow step. */
  @Schema(
      description = "The name of the workflow step",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The code(s) for the next workflow step(s). */
  @Schema(description = "The code(s) for the next workflow step(s)")
  @JsonProperty
  @XmlElementWrapper(name = "NextSteps")
  @XmlElement(name = "NextStep")
  @Size(max = 10)
  @Convert(converter = StringListAttributeConverter.class)
  @Column(name = "next_steps", length = 510)
  private List<String> nextSteps;

  /** Is this workflow step optional? */
  @Schema(
      description = "Is this workflow step optional",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Optional", required = true)
  @NotNull
  @Column(name = "optional", nullable = false)
  private Boolean optional;

  /** The code(s) for the previous workflow step(s). */
  @Schema(description = "The code(s) for the previous workflow step(s)")
  @JsonProperty
  @XmlElementWrapper(name = "PreviousSteps")
  @XmlElement(name = "PreviousStep")
  @Size(max = 10)
  @Convert(converter = StringListAttributeConverter.class)
  @Column(name = "previous_steps", length = 510)
  private List<String> previousSteps;

  /** The sequence number for the workflow step. */
  @Schema(
      description = "The sequence number for the workflow step",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Sequence", required = true)
  @NotNull
  @Column(name = "sequence", nullable = false)
  private Integer sequence;

  /** The ISO-8601 duration format amount of time to complete the workflow step. */
  @Schema(description = "The ISO-8601 duration format amount of time to complete the workflow step")
  @JsonProperty
  @XmlElement(name = "TimeToComplete")
  @ValidISO8601DurationOrPeriod
  @Column(name = "time_to_complete", length = 50)
  private String timeToComplete;

  /** Constructs a new {@code WorkflowStepDefinition}. */
  public WorkflowStepDefinition() {}

  /**
   * Constructs a new {@code WorkflowStepDefinition}.
   *
   * @param sequence the sequence number for the workflow step
   * @param code the code for the workflow step
   * @param name the name of the workflow step
   * @param description the description for the workflow step
   * @param external is this workflow step completed by one or more external users
   * @param internal is this workflow step internal-only and excluded for external users
   * @param optional is this workflow step optional
   * @param previousSteps the codes for the previous workflow steps
   * @param nextSteps the codes for the next workflow steps
   * @param timeToComplete the ISO-8601 duration format amount of time to complete the workflow step
   */
  public WorkflowStepDefinition(
      int sequence,
      String code,
      String name,
      String description,
      boolean external,
      boolean internal,
      boolean optional,
      List<String> previousSteps,
      List<String> nextSteps,
      String timeToComplete) {
    this.sequence = sequence;
    this.code = code;
    this.name = name;
    this.description = description;
    this.external = external;
    this.internal = internal;
    this.optional = optional;
    this.previousSteps = previousSteps;
    this.nextSteps = nextSteps;
    this.timeToComplete = timeToComplete;
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

    WorkflowStepDefinition other = (WorkflowStepDefinition) object;

    return StringUtil.equalsIgnoreCase(definitionId, other.definitionId)
        && (definitionVersion == other.definitionVersion)
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
   * Returns the description for the workflow step.
   *
   * @return the description for the workflow step
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns whether this workflow step is completed by one or more external users.
   *
   * @return {@code true} if this workflow step is completed by one or more external users or {@code
   *     false otherwise}
   */
  public Boolean getExternal() {
    return external;
  }

  /**
   * Returns the name of the workflow step.
   *
   * @return the name of the workflow step
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the code(s) for the next workflow step(s).
   *
   * @return the code(s) for the next workflow step(s)
   */
  public List<String> getNextSteps() {
    return nextSteps;
  }

  /**
   * Returns the code(s) for the previous workflow step(s).
   *
   * @return the code(s) for the previous workflow step(s)
   */
  public List<String> getPreviousStep() {
    return previousSteps;
  }

  /**
   * Returns the sequence number for the workflow step.
   *
   * @return the sequence number for the workflow step
   */
  public Integer getSequence() {
    return sequence;
  }

  /**
   * Returns the ISO-8601 duration format amount of time to complete the workflow step.
   *
   * @return the ISO-8601 duration format amount of time to complete the workflow step
   */
  public String getTimeToComplete() {
    return timeToComplete;
  }

  /**
   * Returns the {@code Duration} for the ISO-8601 duration format amount of time to complete the
   * workflow step.
   *
   * @return the {@code Duration} for the ISO-8601 duration format amount of time to complete the
   *     workflow step or {@code null} if no validity period has been specified
   */
  @JsonIgnore
  @XmlTransient
  public Duration getTimeToCompleteAsDuration() {
    if (this.timeToComplete == null) {
      return null;
    } else {
      return DurationStyle.detectAndParse(timeToComplete);
    }
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((definitionId == null) ? 0 : definitionId.hashCode())
        + ((definitionVersion == 0) ? 0 : Integer.hashCode(definitionVersion))
        + ((code == null) ? 0 : code.hashCode());
  }

  /**
   * Returns whether this workflow step is internal-only and excluded for external users.
   *
   * @return {@code true} if this workflow step is internal-only and excluded for external users or
   *     {@code false} otherwise
   */
  public boolean isInternal() {
    return internal;
  }

  /**
   * Returns whether the workflow step is optional.
   *
   * @return {@code true} if the workflow step is optional or {@code false} otherwise
   */
  public Boolean isOptional() {
    return optional;
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
   * Set the description for the workflow step.
   *
   * @param description the description for the workflow step
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set whether this workflow step is completed by one or more external users.
   *
   * @param external {@code true} if this workflow step is completed by one or more external users
   *     or {@code false} otherwise
   */
  public void setExternal(Boolean external) {
    this.external = external;
  }

  /**
   * Set whether this workflow step is internal-only and excluded for external users.
   *
   * @param internal {@code true} if this workflow step is internal-only and excluded for external
   *     users or {@code false} otherwise
   */
  public void setInternal(boolean internal) {
    this.internal = internal;
  }

  /**
   * Set the name of the workflow step.
   *
   * @param name the name of the workflow step
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the code(s) for the next workflow step(s).
   *
   * @param nextSteps the code(s) for the next workflow step(s)
   */
  public void setNextSteps(List<String> nextSteps) {
    this.nextSteps = nextSteps;
  }

  /**
   * Set whether the workflow step is optional.
   *
   * @param optional {@code true} if the workflow step is optional or {@code false} otherwise
   */
  public void setOptional(Boolean optional) {
    this.optional = optional;
  }

  /**
   * Set the code(s) for the previous workflow step(s).
   *
   * @param previousSteps the code(s) for the previous workflow step(s)
   */
  public void setPreviousSteps(List<String> previousSteps) {
    this.previousSteps = previousSteps;
  }

  /**
   * Set the sequence number for the workflow step.
   *
   * @param sequence the sequence number for the workflow step
   */
  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  /**
   * Set the ISO-8601 duration format amount of time to complete the workflow step.
   *
   * @param timeToComplete the ISO-8601 duration format amount of time to complete the workflow step
   */
  public void setTimeToComplete(String timeToComplete) {
    this.timeToComplete = timeToComplete;
  }

  /**
   * Set the workflow definition the workflow step definition is associated with.
   *
   * @param workflowDefinition the workflow definition the workflow step definition is associated
   *     with
   */
  @JsonBackReference("workflowDefinitionStepDefinitionReference")
  @Schema(hidden = true)
  public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
    if (workflowDefinition != null) {
      this.definitionId = workflowDefinition.getId();
      this.definitionVersion = workflowDefinition.getVersion();
    } else {
      this.definitionId = null;
      this.definitionVersion = 0;
    }
  }

  /**
   * Called by the JAXB runtime when an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof WorkflowDefinition parent) {
      setWorkflowDefinition(parent);
    }
  }
}

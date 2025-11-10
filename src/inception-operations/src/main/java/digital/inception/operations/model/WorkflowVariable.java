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
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code WorkflowVariable} class holds the information for a workflow variable.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow variable")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "name", "value"})
@XmlRootElement(name = "WorkflowVariable", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowVariable",
    namespace = "https://inception.digital/operations",
    propOrder = {"type", "name", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_variables")
@IdClass(WorkflowVariableId.class)
public class WorkflowVariable implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The name for the workflow variable. */
  @Schema(
      description = "The name for the workflow variable",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The variable type for the workflow variable. */
  @Schema(
      description = "The variable type for the workflow variable",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", nullable = false)
  private VariableType type;

  /**
   * The value for the workflow variable.
   *
   * <p>NOTE: All values are stored as a {@code String} with a maximum length of 4000 characters.
   */
  @Schema(description = "The value for the workflow variable",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  @Column(name = "value", length = 4000)
  private String value;

  /** The ID for the workflow the workflow variable is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "workflow_id", nullable = false)
  private UUID workflowId;

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param type the variable type for the workflow variable
   * @param value the value for the workflow variable
   * @param workflowId the ID for the workflow the workflow variable is associated with
   */
  public WorkflowVariable(String name, VariableType type, String value, UUID workflowId) {
    this.name = name;
    this.type = type;
    this.value = value;
    this.workflowId = workflowId;
  }

  /** Constructs a new {@code WorkflowVariable}. */
  public WorkflowVariable() {}

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param value the string value for the workflow variable
   */
  public WorkflowVariable(String name, String value) {
    this.type = VariableType.STRING;
    this.name = name;
    this.value = value;
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param booleanValue the boolean value for the workflow variable
   */
  public WorkflowVariable(String name, boolean booleanValue) {
    this.type = VariableType.BOOLEAN;
    this.name = name;
    this.value = Boolean.toString(booleanValue);
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param doubleValue the double value for the workflow variable
   */
  public WorkflowVariable(String name, double doubleValue) {
    this.type = VariableType.DOUBLE;
    this.name = name;
    this.value = Double.toString(doubleValue);
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param dateValue the date value for the workflow variable
   */
  public WorkflowVariable(String name, LocalDate dateValue) {
    this.type = VariableType.DATE;
    this.name = name;
    this.value = dateValue.toString();
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param decimalValue the decimal value for the workflow variable
   */
  public WorkflowVariable(String name, BigDecimal decimalValue) {
    this.type = VariableType.DECIMAL;
    this.name = name;
    this.value = decimalValue.toString();
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param integerValue the integer value for the workflow variable
   */
  public WorkflowVariable(String name, Integer integerValue) {
    this.type = VariableType.INTEGER;
    this.name = name;
    this.value = integerValue.toString();
  }

  /**
   * Clone the workflow variable.
   *
   * @return the cloned workflow variable
   */
  public WorkflowVariable cloneWorkflowVariable() {
    return new WorkflowVariable(name, type, value, workflowId);
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

    WorkflowVariable other = (WorkflowVariable) object;

    return Objects.equals(workflowId, other.workflowId)
        && StringUtil.equalsIgnoreCase(name, other.name);
  }

  /**
   * Returns the name of the workflow variable.
   *
   * @return the name of the workflow variable
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the variable type for the workflow variable.
   *
   * @return the variable type for the workflow variable
   */
  public VariableType getType() {
    return type;
  }

  /**
   * Returns the value for the workflow variable.
   *
   * @return the value for the workflow variable
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns a hash name value for the object.
   *
   * @return a hash name value for the object
   */
  @Override
  public int hashCode() {
    return ((workflowId == null) ? 0 : workflowId.hashCode())
        + ((name == null) ? 0 : name.hashCode())
        + ((value == null) ? 0 : value.hashCode());
  }

  /**
   * Set the name of the workflow variable.
   *
   * @param name the name of the workflow variable
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the variable type for the workflow variable.
   *
   * @param type the variable type for the workflow variable
   */
  public void setType(VariableType type) {
    this.type = type;
  }

  /**
   * Set the value for the workflow variable.
   *
   * @param value the value for the workflow variable
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Set the workflow the workflow variable is associated with.
   *
   * @param workflow the workflow the workflow variable is associated with
   */
  @JsonBackReference("workflowVariableReference")
  @Schema(hidden = true)
  public void setWorkflow(Workflow workflow) {
    if (workflow != null) {
      this.workflowId = workflow.getId();
    } else {
      this.workflowId = null;
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
    if (parentObject instanceof Workflow parent) {
      setWorkflow(parent);
    }
  }
}

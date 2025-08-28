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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.StringUtil;
import digital.inception.core.xml.LocalDateAdapter;
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
@JsonPropertyOrder({
  "name",
  "booleanValue",
  "dateValue",
  "decimalValue",
  "doubleValue",
  "integerValue",
  "stringValue"
})
@XmlRootElement(name = "WorkflowVariable", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowVariable",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "name",
      "booleanValue",
      "dateValue",
      "decimalValue",
      "doubleValue",
      "integerValue",
      "stringValue"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_variables")
@IdClass(WorkflowVariableId.class)
public class WorkflowVariable implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The boolean value for the workflow variable. */
  @Schema(description = "The boolean value for the workflow variable")
  @JsonProperty
  @XmlElement(name = "BooleanValue")
  @Column(name = "boolean_value")
  private Boolean booleanValue;

  /** The date value for the workflow variable. */
  @Schema(description = "The ISO 8601 format date value for the workflow variable")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "date_value")
  private LocalDate dateValue;

  /** The decimal value for the workflow variable. */
  @Schema(description = "The decimal value for the workflow variable")
  @JsonProperty
  @XmlElement(name = "DecimalValue")
  @Column(name = "decimal_value")
  private BigDecimal decimalValue;

  /** The double value for the workflow variable. */
  @Schema(description = "The double value for the workflow variable")
  @JsonProperty
  @XmlElement(name = "DoubleValue")
  @Column(name = "double_value")
  private Double doubleValue;

  /** The integer value for the workflow variable. */
  @Schema(description = "The integer value for the workflow variable")
  @JsonProperty
  @XmlElement(name = "IntegerValue")
  @Column(name = "integer_value")
  private Integer integerValue;

  /**
   * The name for the workflow variable.
   *
   * <p>NOTE: The length of this attribute aligns with the length of variable names in Camunda and
   * Flowable.
   */
  @Schema(
      description = "The name for the workflow variable",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 255)
  @Id
  @Column(name = "name", length = 255, nullable = false)
  private String name;

  /** The string value for the workflow variable. */
  @Schema(description = "The string value for the workflow variable")
  @JsonProperty
  @XmlElement(name = "StringValue")
  @Size(min = 1, max = 4000)
  @Column(name = "string_value", length = 4000)
  private String stringValue;

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
   * @param booleanValue the boolean value for the workflow variable
   * @param dateValue the date value for the workflow variable
   * @param decimalValue the decimal value for the workflow variable
   * @param doubleValue the double value for the workflow variable
   * @param integerValue the integer value for the workflow variable
   * @param stringValue the string value for the workflow variable
   * @param workflowId the ID for the workflow the workflow variable is associated with
   */
  public WorkflowVariable(
      String name,
      Boolean booleanValue,
      LocalDate dateValue,
      BigDecimal decimalValue,
      Double doubleValue,
      Integer integerValue,
      String stringValue,
      UUID workflowId) {
    this.name = name;
    this.booleanValue = booleanValue;
    this.dateValue = dateValue;
    this.decimalValue = decimalValue;
    this.doubleValue = doubleValue;
    this.integerValue = integerValue;
    this.stringValue = stringValue;
    this.workflowId = workflowId;
  }

  /** Constructs a new {@code WorkflowVariable}. */
  public WorkflowVariable() {}

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param stringValue the string value for the workflow variable
   */
  public WorkflowVariable(String name, String stringValue) {
    this.name = name;
    this.stringValue = stringValue;
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param booleanValue the boolean value for the workflow variable
   */
  public WorkflowVariable(String name, boolean booleanValue) {
    this.name = name;
    this.booleanValue = booleanValue;
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param doubleValue the double value for the workflow variable
   */
  public WorkflowVariable(String name, double doubleValue) {
    this.name = name;
    this.doubleValue = doubleValue;
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param dateValue the date value for the workflow variable
   */
  public WorkflowVariable(String name, LocalDate dateValue) {
    this.name = name;
    this.dateValue = dateValue;
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param decimalValue the decimal value for the workflow variable
   */
  public WorkflowVariable(String name, BigDecimal decimalValue) {
    this.name = name;
    this.decimalValue = decimalValue;
  }

  /**
   * Constructs a new {@code WorkflowVariable}.
   *
   * @param name the name of the workflow variable
   * @param integerValue the integer value for the workflow variable
   */
  public WorkflowVariable(String name, Integer integerValue) {
    this.name = name;
    this.integerValue = integerValue;
  }

  /**
   * Clone the workflow variable.
   *
   * @return the cloned workflow variable
   */
  public WorkflowVariable cloneWorkflowVariable() {
    return new WorkflowVariable(
        name,
        booleanValue,
        dateValue,
        decimalValue,
        doubleValue,
        integerValue,
        stringValue,
        workflowId);
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
   * Returns the boolean value for the workflow variable.
   *
   * @return the boolean value for the workflow variable
   */
  public Boolean getBooleanValue() {
    return booleanValue;
  }

  /**
   * Returns the date value for the workflow variable.
   *
   * @return the date value for the workflow variable
   */
  public LocalDate getDateValue() {
    return dateValue;
  }

  /**
   * Returns the decimal value for the workflow variable.
   *
   * @return the decimal value for the workflow variable
   */
  public BigDecimal getDecimalValue() {
    return decimalValue;
  }

  /**
   * Returns the double value for the workflow variable.
   *
   * @return the double value for the workflow variable
   */
  public Double getDoubleValue() {
    return doubleValue;
  }

  /**
   * Returns the integer value for the workflow variable.
   *
   * @return the integer value for the workflow variable
   */
  public Integer getIntegerValue() {
    return integerValue;
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
   * Returns the string value for the workflow variable.
   *
   * @return the string value for the workflow variable
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * Retrieve the workflow variable value as a {@code String}.
   *
   * @return the workflow variable value as a {@code String}
   */
  public String getValueAsString() {
    if (booleanValue != null) {
      return booleanValue.toString();
    } else if (dateValue != null) {
      return dateValue.toString();
    } else if (decimalValue != null) {
      return decimalValue.toString();
    } else if (doubleValue != null) {
      return doubleValue.toString();
    } else if (integerValue != null) {
      return integerValue.toString();
    } else {
      return stringValue;
    }
  }

  /**
   * Returns a hash name value for the object.
   *
   * @return a hash name value for the object
   */
  @Override
  public int hashCode() {
    return ((workflowId == null) ? 0 : workflowId.hashCode())
        + ((name == null) ? 0 : name.hashCode());
  }

  /**
   * Set the boolean value for the workflow variable.
   *
   * @param booleanValue the boolean value for the workflow variable
   */
  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * Set the date value for the workflow variable.
   *
   * @param dateValue the date value for the workflow variable
   */
  public void setDateValue(LocalDate dateValue) {
    this.dateValue = dateValue;
  }

  /**
   * Set the decimal value for the workflow variable.
   *
   * @param decimalValue the decimal value for the workflow variable
   */
  public void setDecimalValue(BigDecimal decimalValue) {
    this.decimalValue = decimalValue;
  }

  /**
   * Set the double value for the workflow variable.
   *
   * @param doubleValue the double value for the workflow variable
   */
  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  /**
   * Set the integer value for the workflow variable.
   *
   * @param integerValue the integer value for the workflow variable
   */
  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
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
   * Set the string value for the workflow variable.
   *
   * @param stringValue the string value for the workflow variable
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
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

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

/**
 * The {@code WorkflowDefinitionAttribute} class holds the information for an attribute for a
 * workflow definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An attribute for a workflow definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "value"})
@XmlRootElement(
    name = "WorkflowDefinitionAttribute",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDefinitionAttribute",
    namespace = "https://inception.digital/operations",
    propOrder = {"code", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_definition_attributes")
@IdClass(WorkflowDefinitionAttributeId.class)
public class WorkflowDefinitionAttribute implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the workflow definition attribute. */
  @Schema(
      description = "The code for the workflow definition attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The ID for the workflow definition the workflow definition attribute is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "definition_id", nullable = false)
  private String definitionId;

  /**
   * The version of the workflow definition the workflow definition attribute is associated with.
   */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "definition_version", nullable = false)
  private int definitionVersion;

  /** The value for the workflow definition attribute. */
  @Schema(
      description = "The value for the workflow definition attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "value", length = 1000, nullable = false)
  private String value;

  /** Constructs a new {@code WorkflowDefinitionAttribute}. */
  public WorkflowDefinitionAttribute() {}

  /**
   * Constructs a new {@code WorkflowDefinitionAttribute}.
   *
   * @param code the code for the workflow definition attribute
   * @param value the value for the workflow definition attribute
   */
  public WorkflowDefinitionAttribute(String code, String value) {
    this.code = code;
    this.value = value;
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

    WorkflowDefinitionAttribute other = (WorkflowDefinitionAttribute) object;

    return StringUtil.equalsIgnoreCase(definitionId, other.definitionId)
        && definitionVersion == other.definitionVersion
        && StringUtil.equalsIgnoreCase(code, other.code);
  }

  /**
   * Returns the code for the workflow definition attribute.
   *
   * @return the code for the workflow definition attribute
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the value for the workflow definition attribute.
   *
   * @return the value for the workflow definition attribute
   */
  public String getValue() {
    return value;
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
   * Set the code for the workflow definition attribute.
   *
   * @param code the code for the workflow definition attribute
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the value for the workflow definition attribute.
   *
   * @param value the value for the workflow definition attribute
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Set the workflow definition the workflow definition attribute is associated with.
   *
   * @param workflowDefinition the workflow definition the workflow definition attribute is
   *     associated with
   */
  @JsonBackReference("workflowDefinitionAttributeReference")
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
   * Called by the JAXB runtime an instance of this class has been completely unmarshalled, but
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

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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
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
import java.util.Objects;
import java.util.Optional;

/**
 * The {@code WorkflowEngine} class holds the information for a workflow engine.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow engine")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "connectorClassName", "attributes"})
@XmlRootElement(name = "WorkflowEngine", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowEngine",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "name", "connectorClassName", "attributes"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_engines")
public class WorkflowEngine implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the workflow engine. */
  @Schema(description = "The attributes for the workflow engine")
  @JsonProperty
  @JsonManagedReference("workflowEngineAttributeReference")
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("name")
  @JoinColumn(name = "engine_id", insertable = false, updatable = false)
  private final List<WorkflowEngineAttribute> attributes = new ArrayList<>();

  /** The fully qualified name of the connector class for the workflow engine. */
  @Schema(
      description = "The fully qualified name of the connector class for the workflow engine",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ConnectorClassName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "connector_class_name", length = 500, nullable = false)
  private String connectorClassName;

  /** The ID for the workflow engine. */
  @Schema(
      description = "The ID for the workflow engine",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "id", length = 50, nullable = false)
  private String id;

  /** The name of the workflow engine. */
  @Schema(
      description = "The name of the workflow engine",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** Constructs a new {@code WorkflowEngine}. */
  public WorkflowEngine() {}

  /**
   * Constructs a new {@code WorkflowEngine}.
   *
   * @param id the ID for the workflow engine
   * @param name the name of the workflow engine
   * @param connectorClassName the fully qualified name of the connector class for the workflow
   *     engine
   * @param attributes the attributes for the workflow engine
   */
  public WorkflowEngine(
      String id, String name, String connectorClassName, List<WorkflowEngineAttribute> attributes) {
    this.id = id;
    this.name = name;
    this.connectorClassName = connectorClassName;
    setAttributes(attributes);
  }

  /**
   * Constructs a new {@code WorkflowEngine}.
   *
   * @param id the ID for the workflow engine
   * @param name the name of the workflow engine
   * @param connectorClassName the fully qualified name of the connector class for the workflow
   *     engine
   */
  public WorkflowEngine(String id, String name, String connectorClassName) {
    this.id = id;
    this.name = name;
    this.connectorClassName = connectorClassName;
  }

  /**
   * Add the attribute for the workflow engine.
   *
   * @param attribute the attribute
   */
  public void addAttribute(WorkflowEngineAttribute attribute) {
    attributes.removeIf(
        existingAttribute ->
            StringUtil.equalsIgnoreCase(existingAttribute.getName(), attribute.getName()));

    attribute.setWorkflowEngine(this);

    attributes.add(attribute);
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

    WorkflowEngine other = (WorkflowEngine) object;

    return StringUtil.equalsIgnoreCase(id, other.id);
  }

  /**
   * Retrieve the attribute with the specified name for the workflow engine.
   *
   * @param name the name of the attribute
   * @return an Optional containing the attribute with the specified name for the workflow engine or
   *     an empty Optional if the attribute could not be found
   */
  public Optional<WorkflowEngineAttribute> getAttribute(String name) {
    return attributes.stream()
        .filter(attribute -> StringUtil.equalsIgnoreCase(attribute.getName(), name))
        .findFirst();
  }

  /**
   * Returns the attributes for the workflow engine.
   *
   * @return the attributes for the workflow engine
   */
  public List<WorkflowEngineAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Returns the fully qualified name of the connector class for the workflow engine.
   *
   * @return the fully qualified name of the connector class for the workflow engine
   */
  public String getConnectorClassName() {
    return connectorClassName;
  }

  /**
   * Returns the ID for the workflow engine.
   *
   * @return the ID for the workflow engine
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the workflow engine.
   *
   * @return the name of the workflow engine
   */
  public String getName() {
    return name;
  }

  /**
   * Returns whether the workflow engine has an attribute with the specified name.
   *
   * @param name the name of the attribute
   * @return {@code true} if the workflow engine has an attribute with the specified name or {@code
   *     false} otherwise
   */
  public boolean hasAttribute(String name) {
    return attributes.stream()
        .anyMatch(attribute -> StringUtil.equalsIgnoreCase(attribute.getName(), name));
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Remove the attribute with the specified name for the workflow engine.
   *
   * @param name the name of the attribute
   */
  public void removeAttribute(String name) {
    attributes.removeIf(existingAttribute -> Objects.equals(existingAttribute.getName(), name));
  }

  /**
   * Set the attributes for the workflow engine.
   *
   * @param attributes the attributes for the workflow engine
   */
  public void setAttributes(List<WorkflowEngineAttribute> attributes) {
    attributes.forEach(attribute -> attribute.setWorkflowEngine(this));
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the fully qualified name of the connector class for the workflow engine.
   *
   * @param connectorClassName the fully qualified name of the connector class for the workflow
   *     engine
   */
  public void setConnectorClassName(String connectorClassName) {
    this.connectorClassName = connectorClassName;
  }

  /**
   * Set the ID for the workflow engine.
   *
   * @param id the ID for the workflow engine
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the workflow engine.
   *
   * @param name the name of the workflow engine
   */
  public void setName(String name) {
    this.name = name;
  }
}

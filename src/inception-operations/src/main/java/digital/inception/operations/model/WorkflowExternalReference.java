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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code WorkflowExternalReference} class holds the information for an external reference for a
 * workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An external reference for a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "value"})
@XmlRootElement(
    name = "WorkflowExternalReference",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowExternalReference",
    namespace = "https://inception.digital/operations",
    propOrder = {"type", "value"})
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@DiscriminatorValue("workflow")
@SuppressWarnings({"unused", "WeakerAccess"})
public class WorkflowExternalReference extends ExternalReference implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** Constructs a new {@code WorkflowExternalReference}. */
  public WorkflowExternalReference() {}

  /**
   * Constructs a new {@code WorkflowExternalReference}.
   *
   * @param type the code for the external reference type
   * @param value the value for the external reference
   */
  public WorkflowExternalReference(String type, String value) {
    super(type, value);
  }

  /**
   * Returns the code for the external reference type.
   *
   * @return the code for the external reference type
   */
  @Schema(
      description = "The code for the external reference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  public String getType() {
    return type;
  }

  /**
   * Returns the value for the external reference.
   *
   * @return the value for the external reference
   */
  @Schema(
      description = "The value for the external reference",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  public String getValue() {
    return value;
  }

  /**
   * Set the code for the external reference type.
   *
   * @param type the code for the external reference type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Set the value for the external reference.
   *
   * @param value the value for the external reference
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Set the workflow the workflow external reference is associated with.
   *
   * @param workflow the workflow the workflow external reference is associated with
   */
  @JsonBackReference("workflowExternalReferenceReference")
  @Schema(hidden = true)
  public void setWorkflow(Workflow workflow) {
    if (workflow != null) {
      this.objectId = workflow.getId();
    } else {
      this.objectId = null;
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

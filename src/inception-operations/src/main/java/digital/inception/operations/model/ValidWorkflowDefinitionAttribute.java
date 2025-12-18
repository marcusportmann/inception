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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code ValidWorkflowDefinitionAttribute} class holds the information for a valid workflow
 * definition attribute.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A valid workflow definition attribute")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "required", "description"})
@XmlRootElement(
    name = "ValidWorkflowDefinitionAttribute",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "ValidWorkflowDefinitionAttribute",
    namespace = "https://inception.digital/operations",
    propOrder = {"code", "required", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class ValidWorkflowDefinitionAttribute implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the workflow definition attribute. */
  @Schema(
      description = "The code for the workflow definition attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String code;

  /** The description for the workflow definition attribute. */
  @Schema(
      description = "The description for the workflow definition attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  private String description;

  /** Is the workflow definition attribute required? */
  @Schema(
      description = "Is the workflow definition attribute required",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Required", required = true)
  @NotNull
  private Boolean required;

  /** Constructs a new {@code ValidWorkflowDefinitionAttribute}. */
  public ValidWorkflowDefinitionAttribute() {}

  /**
   * Constructs a new {@code ValidWorkflowDefinitionAttribute}.
   *
   * @param code the code for the workflow definition attribute
   * @param description the description for the workflow definition attribute
   * @param required is the workflow definition attribute required
   */
  public ValidWorkflowDefinitionAttribute(String code, String description, boolean required) {
    this.code = code;
    this.description = description;
    this.required = required;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument otherwise {@code false}
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

    ValidWorkflowDefinitionAttribute other = (ValidWorkflowDefinitionAttribute) object;

    return Objects.equals(code, other.code);
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
   * Returns the description for the workflow definition attribute.
   *
   * @return the description for the workflow definition attribute
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (code == null) ? 0 : code.hashCode();
  }

  /**
   * Returns whether the workflow definition attribute is required.
   *
   * @return {@code true} if the workflow definition attribute is required or {@code false}
   *     otherwise
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Sets the code for the workflow definition attribute.
   *
   * @param code the code for the workflow definition attribute
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Sets the description for the workflow definition attribute.
   *
   * @param description the description for the workflow definition attribute
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set whether the workflow definition attribute is required.
   *
   * @param required {@code true} if the workflow definition attribute is required or {@code false}
   *     otherwise
   */
  public void setRequired(boolean required) {
    this.required = required;
  }
}

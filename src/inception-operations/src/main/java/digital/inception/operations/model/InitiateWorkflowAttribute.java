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

/**
 * The {@code InitiateWorkflowAttribute} class holds the information for a workflow attribute
 * specified when initiating a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow attribute specified when initiating a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "value"})
@XmlRootElement(
    name = "InitiateWorkflowAttribute",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "InitiateWorkflowAttribute",
    namespace = "https://inception.digital/operations",
    propOrder = {"code", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
public class InitiateWorkflowAttribute implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the workflow attribute. */
  @Schema(
      description = "The code for the workflow attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String code;

  /** The value for the workflow attribute. */
  @Schema(
      description = "The value for the workflow attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  private String value;

  /** Constructs a new {@code InitiateWorkflowAttribute}. */
  public InitiateWorkflowAttribute() {}

  /**
   * Constructs a new {@code InitiateWorkflowAttribute}.
   *
   * @param code the code for the workflow attribute
   * @param value the value for the workflow attribute
   */
  public InitiateWorkflowAttribute(String code, String value) {
    this.code = code;
    this.value = value;
  }

  /**
   * Returns the code for the workflow attribute.
   *
   * @return the code for the workflow attribute
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the value for the workflow attribute.
   *
   * @return the value for the workflow attribute
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the code for the workflow attribute.
   *
   * @param code the code for the workflow attribute
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the value for the workflow attribute.
   *
   * @param value the value for the workflow attribute
   */
  public void setValue(String value) {
    this.value = value;
  }
}

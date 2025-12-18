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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code VariableSearchCriteria} class holds the variable search criteria to apply when
 * searching for workflows.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The variable search criteria to apply when searching for workflows")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "value"})
@XmlRootElement(name = "VariableSearchCriteria", namespace = "https://inception.digital/operations")
@XmlType(
    name = "VariableSearchCriteria",
    namespace = "https://inception.digital/operations",
    propOrder = {"name", "value"})
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused", "WeakerAccess"})
public class VariableSearchCriteria implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * The name for the variable.
   *
   * <p>NOTE: The length of this attribute aligns with the length of variable names in Camunda and
   * Flowable.
   */
  @Schema(description = "The name for the variable")
  @JsonProperty
  @XmlElement(name = "Name")
  @Size(min = 1, max = 255)
  private String name;

  /** The value for the variable. */
  @Schema(description = "The value for the variable")
  @JsonProperty
  @XmlElement(name = "Value")
  @Size(min = 1, max = 4000)
  private String value;

  /** Constructs a new {@code VariableSearchCriteria}. */
  public VariableSearchCriteria() {}

  /**
   * Constructs a new {@code VariableSearchCriteria}.
   *
   * @param name the name for the variable
   * @param value the value for the variable
   */
  public VariableSearchCriteria(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the name for the variable.
   *
   * @return the name for the variable
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value for the variable.
   *
   * @return the value for the variable
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the name for the variable
   *
   * @param name the name for the variable
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the value for the variable.
   *
   * @param value the value for the variable
   */
  public void setValue(String value) {
    this.value = value;
  }
}

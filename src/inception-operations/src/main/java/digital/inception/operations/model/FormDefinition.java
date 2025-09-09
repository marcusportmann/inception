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
import digital.inception.core.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
 * The {@code FormDefinition} class holds the information for a form definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A form definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "type", "data"})
@XmlRootElement(name = "FormDefinition", namespace = "https://inception.digital/operations")
@XmlType(
    name = "FormDefinition",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "name", "type", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_form_definitions")
public class FormDefinition implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The JSON or XML data for the form definition. */
  @Schema(
      description = "The JSON or XML data for the form definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Size(min = 1, max = 41943040)
  @Column(name = "data", length = 41943040, nullable = false)
  private String data;

  /** The ID for the form definition. */
  @Schema(
      description = "The ID for the form definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "id", length = 50, nullable = false)
  private String id;

  /** The name of the form definition. */
  @Schema(
      description = "The name of the form definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The form type for the form definition. */
  @Schema(
      description = "The form type for the form definition",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", length = 50, nullable = false)
  private FormType type;

  /** Constructs a new {@code FormDefinition}. */
  public FormDefinition() {}

  /**
   * Constructs a new {@code FormDefinition}.
   *
   * @param id ID for the form definition
   * @param name name of the form definition
   * @param type the form type for the form definition
   * @param data the JSON or XML data for the form definition
   */
  public FormDefinition(String id, String name, FormType type, String data) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.data = data;
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

    FormDefinition other = (FormDefinition) object;

    return StringUtil.equalsIgnoreCase(id, other.id);
  }

  /**
   * Returns the JSON or XML data for the form definition.
   *
   * @return the JSON or XML data for the form definition
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the ID for the form definition.
   *
   * @return the ID for the form definition
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the form definition.
   *
   * @return the name of the form definition
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the form type for the form definition.
   *
   * @return the form type for the form definition
   */
  public FormType getType() {
    return type;
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
   * Set the JSON or XML data for the form definition.
   *
   * @param data the JSON or XML data for the form definition
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the form definition.
   *
   * @param id the ID for the form definition
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name of the form definition.
   *
   * @param name the name of the form definition
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the form type for the form definition.
   *
   * @param type the form type for the form definition
   */
  public void setType(FormType type) {
    this.type = type;
  }
}

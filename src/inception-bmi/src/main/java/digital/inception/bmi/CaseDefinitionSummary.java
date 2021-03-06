/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.bmi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>CaseDefinitionSummary</b> class holds the summary information for a case definition.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A case definition summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name"})
@XmlRootElement(name = "CaseDefinitionSummary", namespace = "http://inception.digital/bmi")
@XmlType(
    name = "CaseDefinitionSummary",
    namespace = "http://inception.digital/bmi",
    propOrder = {"id", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class CaseDefinitionSummary implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ID for the case definition. */
  @Schema(description = "The ID for the case definition", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String id;

  /** The name of the case definition. */
  @Schema(description = "The name of the case definition", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** Constructs a new <b>CaseDefinitionSummary</b>. */
  public CaseDefinitionSummary() {}

  /**
   * Constructs a new <b>CaseDefinitionSummary</b>.
   *
   * @param id the ID for the case definition
   * @param name the name of the case definition
   */
  public CaseDefinitionSummary(String id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
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

    CaseDefinitionSummary other = (CaseDefinitionSummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the case definition.
   *
   * @return the ID for the case definition
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the case definition.
   *
   * @return the name of the case definition
   */
  public String getName() {
    return name;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString() {
    return "CaseDefinitionSummary {id=\"" + getId() + "\", name=\"" + getName() + "\"}";
  }
}

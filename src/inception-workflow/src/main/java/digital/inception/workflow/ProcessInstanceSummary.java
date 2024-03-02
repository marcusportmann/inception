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

package digital.inception.workflow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>ProcessInstanceSummary</b> class holds the summary information for a process§ instance.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A process instance summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "version"})
@XmlRootElement(name = "ProcessInstanceSummary", namespace = "http://inception.digital/workflow")
@XmlType(
    name = "ProcessInstanceSummary",
    namespace = "http://inception.digital/workflow",
    propOrder = {"id", "name", "version"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class ProcessInstanceSummary implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ID for the process definition. */
  @Schema(description = "The ID for the process definition", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String id;

  /** The name of the process definition. */
  @Schema(description = "The name of the process definition", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The version of the process definition given by the custom Camunda versionTag. */
  @Schema(description = "The version of the process definition", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Version", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String version;

  /** Constructs a new <b>ProcessInstanceSummary</b>. */
  public ProcessInstanceSummary() {}

  /**
   * Constructs a new <b>ProcessInstanceSummary</b>.
   *
   * @param id the ID for the process definition
   * @param name the name of the process definition
   * @param version the version of the process definition
   */
  public ProcessInstanceSummary(String id, String name, String version) {
    this.id = id;
    this.name = name;
    this.version = version;
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

    ProcessInstanceSummary other = (ProcessInstanceSummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the process definition.
   *
   * @return the ID for the process definition
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the process definition.
   *
   * @return the name of the process definition
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the version of the process definition given by the custom Camunda versionTag.
   *
   * @return the version of the process definition given by the custom Camunda versionTag
   */
  public String getVersion() {
    return version;
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
    return "ProcessDefinitionSummary {id=\"" + getId() + "\", name=\"" + getName() + "\"}";
  }
}

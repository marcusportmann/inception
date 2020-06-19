/// *
// * Copyright 2020 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.process;
//
//// ~--- non-JDK imports --------------------------------------------------------
//
// import com.fasterxml.jackson.annotation.JsonInclude;
// import com.fasterxml.jackson.annotation.JsonProperty;
// import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//
// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.media.Schema;
//
//// ~--- JDK imports ------------------------------------------------------------
//
// import java.io.Serializable;
//
// import javax.validation.constraints.NotNull;
// import javax.validation.constraints.Size;
//
// import javax.xml.bind.annotation.*;
//
/// **
// * The <code>ProcessDefinition</code> class holds the information for a process definition.
// *
// * @author Marcus Portmann
// */
// @Schema(description = "ProcessDefinition")
// @JsonInclude(JsonInclude.Include.NON_NULL)
// @JsonPropertyOrder({ "id", "name", "version", "data" })
// @XmlRootElement(name = "ProcessDefinition", namespace = "http://bmi.inception.digital")
// @XmlType(name = "ProcessDefinition", namespace = "http://bmi.inception.digital",
//    propOrder = { "id", "name", "version", "data" })
// @XmlAccessorType(XmlAccessType.FIELD)
// @SuppressWarnings({ "unused", "WeakerAccess" })
// public class ProcessDefinition
//  implements Serializable
// {
//  private static final long serialVersionUID = 1000000;
//
//  /**
//   * The BPMN XML data for the process definition.
//   */
//  @Schema(description = "The BPMN XML data for the process definition", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Data", required = true)
//  @NotNull
//  @Size(min = 1)
//  private byte[] data;
//
//  /**
//   * The ID uniquely identifying the process definition.
//   */
//  @Schema(description = "The ID uniquely identifying the process definition",
//      required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Id", required = true)
//  @NotNull
//  @Size(min = 1, max = 100)
//  private String id;
//
//  /**
//   * The name of the process definition.
//   */
//  @Schema(description = "The name of the process definition", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Name", required = true)
//  @NotNull
//  @Size(min = 1, max = 100)
//  private String name;
//
//  /**
//   * The version of the process definition.
//   */
//  @Schema(description = "The version of the process definition", required = true)
//  @JsonProperty(required = true)
//  @XmlElement(name = "Version", required = true)
//  @NotNull
//  @Size(min = 1, max = 100)
//  private String version;
//
//  /**
//   * Constructs a new <code>ProcessDefinition</code>.
//   */
//  public ProcessDefinition() {}
//
//  /**
//   * Constructs a new <code>ProcessDefinition</code>.
//   *
//   * @param id      the ID uniquely identifying the process definition
//   * @param name    the name of the process definition
//   * @param version the version of the process definition
//   * @param data    the BPMN XML data for the process definition
//   */
//  public ProcessDefinition(String id, String name, String version, byte[] data)
//  {
//    this.id = id;
//    this.name = name;
//    this.version = version;
//    this.data = data;
//  }
//
//  /**
//   * Indicates whether some other object is "equal to" this one.
//   *
//   * @param object the reference object with which to compare
//   *
//   * @return <code>true</code> if this object is the same as the object argument otherwise
//   *         <code>false</code>
//   */
//  @Override
//  public boolean equals(Object object)
//  {
//    if (this == object)
//    {
//      return true;
//    }
//
//    if (object == null)
//    {
//      return false;
//    }
//
//    if (getClass() != object.getClass())
//    {
//      return false;
//    }
//
//    ProcessDefinition other = (ProcessDefinition) object;
//
//    return (id != null) && id.equals(other.id);
//  }
//
//  /**
//   * Returns the BPMN XML data for the process definition.
//   *
//   * @return the BPMN XML data for the process definition
//   */
//  public byte[] getData()
//  {
//    return data;
//  }
//
//  /**
//   * Returns the ID uniquely identifying the process
//   * definition.
//   *
//   * @return the ID uniquely identifying the process
//   *         definition
//   */
//  public String getId()
//  {
//    return id;
//  }
//
//  /**
//   * Returns the name of the process definition.
//   *
//   * @return the name of the process definition
//   */
//  public String getName()
//  {
//    return name;
//  }
//
//  /**
//   * Returns the version of the process definition.
//   *
//   * @return the version of the process definition
//   */
//  public String getVersion()
//  {
//    return version;
//  }
//
//  /**
//   * Returns a hash code value for the object.
//   *
//   * @return a hash code value for the object
//   */
//  @Override
//  public int hashCode()
//  {
//    return (id == null)
//        ? 0
//        : id.hashCode();
//  }
//
//  /**
//   * Set the BPMN XML data for the process definition.
//   *
//   * @param data the BPMN XML data for the process definition
//   */
//  public void setData(byte[] data)
//  {
//    this.data = data;
//  }
//
//  /**
//   * Set the ID uniquely identifying the process definition.
//   *
//   * @param id the ID uniquely identifying the process
//   *           definition
//   */
//  public void setId(String id)
//  {
//    this.id = id;
//  }
//
//  /**
//   * Set the name of the process definition.
//   *
//   * @param name the name of the process definition
//   */
//  public void setName(String name)
//  {
//    this.name = name;
//  }
//
//  /**
//   * Set the version of the process definition.
//   *
//   * @param version the version of the process definition
//   */
//  public void setVersion(String version)
//  {
//    this.version = version;
//  }
//
//  /**
//   * Returns a string representation of the object.
//   *
//   * @return a string representation of the object
//   */
//  @Override
//  public String toString()
//  {
//    return "ProcessDefinition {id=\"" + getId() + "\", name=\"" + getName() + "\", version=\""
//        + getVersion() + "\"}";
//  }
// }

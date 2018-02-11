/*
 * Copyright 2018 Marcus Portmann
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

package digital.inception.reporting;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportDefinition</code> class holds the information for a report definition.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "ReportDefinition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "template" })
@XmlRootElement(name = "ReportDefinition", namespace = "http://reporting.inception.digital")
@XmlType(name = "ReportDefinition", namespace = "http://reporting.inception.digital",
    propOrder = { "id", "name", "template" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ReportDefinition
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the report definition",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /**
   * The name of the report definition.
   */
  @ApiModelProperty(value = "The name of the report definition", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 256)
  private String name;

  /**
   * The JasperReports template for the report definition.
   */
  @ApiModelProperty(value = "The JasperReports template for the report definition", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Template", required = true)
  @NotNull
  @Size(min = 1)
  private byte[] template;

  /**
   * Constructs a new <code>ReportDefinition</code>.
   */
  public ReportDefinition() {}

  /**
   * Constructs a new <code>ReportDefinition</code>.
   *
   * @param name     the name of the report definition
   * @param template the JasperReports template for the report definition
   */
  public ReportDefinition(String name, byte[] template)
  {
    this.id = UUID.randomUUID();
    this.name = name;
    this.template = template;
  }

  /**
   * Constructs a new <code>ReportDefinition</code>.
   *
   * @param id       the Universally Unique Identifier (UUID) used to uniquely identify the report
   *                 definition
   * @param name     the name of the report definition
   * @param template the JasperReports template for the report definition
   */
  public ReportDefinition(UUID id, String name, byte[] template)
  {
    this.id = id;
    this.name = name;
    this.template = template;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the report
   * definition.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the report
   * definition
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the report definition.
   *
   * @return the name of the report definition
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the JasperReports template for the report definition.
   *
   * @return the JasperReports template for the report definition
   */
  public byte[] getTemplate()
  {
    return template;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the report definition.
   *
   * @param name the name of the report definition
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the JasperReports template for the report definition.
   *
   * @param template the JasperReports template for the report definition
   */
  public void setTemplate(byte[] template)
  {
    this.template = template;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ReportDefinition {" + "id=\"" + getId() + "\", name=\"" + getName() + "\"}";
  }
}

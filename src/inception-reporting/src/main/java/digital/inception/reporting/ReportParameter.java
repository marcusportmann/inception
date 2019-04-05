/*
 * Copyright 2019 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>ReportParameter</code> class holds the information for a report parameter.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "ReportParameter")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "value" })
@XmlRootElement(name = "ReportParameter", namespace = "http://reporting.inception.digital")
@XmlType(name = "ReportParameter", namespace = "http://reporting.inception.digital",
    propOrder = { "name", "value" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ReportParameter
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The name of the report parameter.
   */
  @ApiModelProperty(value = "The name of the report parameter", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /**
   * The value for the report parameter.
   */
  @ApiModelProperty(value = "The value for the report parameter", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  private String value;

  /**
   * Constructs a new <code>ReportParameter</code>.
   */
  public ReportParameter() {}

  /**
   * Constructs a new <code>ReportParameter</code>.
   *
   * @param name  the name of the report parameter
   * @param value the value for the report parameter
   */
  public ReportParameter(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the name of the report parameter.
   *
   * @return the name of the report parameter
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the value for the report parameter.
   *
   * @return the value for the report parameter
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the name of the report parameter.
   *
   * @param name the name of the report parameter
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the value for the report parameter.
   *
   * @param value the value for the report parameter
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}

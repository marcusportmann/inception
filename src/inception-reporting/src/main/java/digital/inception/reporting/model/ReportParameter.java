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

package digital.inception.reporting.model;

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
 * The <b>ReportParameter</b> class holds the information for a report parameter.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A parameter associated with a report")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "value"})
@XmlRootElement(name = "ReportParameter", namespace = "https://inception.digital/reporting")
@XmlType(
    name = "ReportParameter",
    namespace = "https://inception.digital/reporting",
    propOrder = {"name", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReportParameter implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The name of the report parameter. */
  @Schema(
      description = "The name of the report parameter",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The value for the report parameter. */
  @Schema(
      description = "The value for the report parameter",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  private String value;

  /** Constructs a new <b>ReportParameter</b>. */
  public ReportParameter() {}

  /**
   * Constructs a new <b>ReportParameter</b>.
   *
   * @param name the name of the report parameter
   * @param value the value for the report parameter
   */
  public ReportParameter(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the name of the report parameter.
   *
   * @return the name of the report parameter
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value for the report parameter.
   *
   * @return the value for the report parameter
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the name of the report parameter.
   *
   * @param name the name of the report parameter
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the value for the report parameter.
   *
   * @param value the value for the report parameter
   */
  public void setValue(String value) {
    this.value = value;
  }
}

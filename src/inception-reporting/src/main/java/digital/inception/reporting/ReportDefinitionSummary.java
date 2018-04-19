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
import java.io.Serializable;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportDefinitionSummary</code> class holds the summary information for a report
 * definition.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "ReportDefinitionSummary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name" })
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ReportDefinitionSummary
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
  @NotNull
  private UUID id;

  /**
   * The name of the report definition.
   */
  @ApiModelProperty(value = "The name of the report definition", required = true)
  @JsonProperty(required = true)
  @NotNull
  @Size(min = 1, max = 256)
  private String name;

  /**
   * Constructs a new <code>ReportDefinitionSummary</code>.
   */
  @SuppressWarnings("unused")
  private ReportDefinitionSummary() {}

  /**
   * Constructs a new <code>ReportDefinitionSummary</code>.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the report
   *             definition
   * @param name the name of the report definition
   */
  ReportDefinitionSummary(UUID id, String name)
  {
    this.id = id;
    this.name = name;
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
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ReportDefinitionSummary {" + "id=\"" + getId() + "\", name=\"" + getName() + "\"}";
  }
}

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

package digital.inception.security;

//~--- JDK imports ------------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

/**
 * The <code>Function</code> class stores the information for an authorised function that can be
 * assigned to <code>Role</code>s.
 *
 * @author Marcus Portmann
 */
public class Function
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The unique code used to identify the function.
   */
  @ApiModelProperty(value = "The unique code used to identify the function", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String code;

  /**
   * The description for the function.
   */
  private String description;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the function.
   */
  @ApiModelProperty(
    value = "The Universally Unique Identifier (UUID) used to uniquely identify the function",
    required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /**
   * The name of the function.
   */
  @ApiModelProperty(value = "The unique code used to identify the function", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String name;

  /**
   * Constructs a new <code>Function</code>.
   */
  public Function() {}

  /**
   * Constructs a new <code>Function</code>.
   *
   * @param id          the Universally Unique Identifier (UUID) used to uniquely identify the
   *                    function
   * @param code        the unique code used to identify the function
   * @param name        the name of the function
   * @param description the description for the function
   */
  public Function(UUID id, String code, String name, String description)
  {
    this.id = id;
    this.code = code;
    this.name = name;
    this.description = description;
  }

  /**
   * Returns the unique code used to identify the function.
   *
   * @return the unique code used to identify the function
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the description for the function.
   *
   * @return the description for the function
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the function.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the function
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the function.
   *
   * @return the name of the function
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the unique code used to identify the function.
   *
   * @param code the unique code used to identify the function
   */
  public void setCode(String code)
  {
    this.code = code;
  }

  /**
   * Set the description for the function.
   *
   * @param description the description for the function
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the function.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the function
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the function.
   *
   * @param name the name of the function
   */
  public void setName(String name)
  {
    this.name = name;
  }
}

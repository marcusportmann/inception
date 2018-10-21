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

package digital.inception.codes;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>Code</code> class holds the information for a code.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "Code")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "codeCategoryId", "name", "value" })
@XmlRootElement(name = "Code", namespace = "http://codes.inception.digital")
@XmlType(name = "Code", namespace = "http://codes.inception.digital",
    propOrder = { "id", "codeCategoryId", "name", "value" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Code
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category the code
   * is associated with.
   */
  @ApiModelProperty(value = "The Universally Unique Identifier (UUID) used to uniquely identify the code category the "
      + "code is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "CodeCategoryId", required = true)
  @NotNull
  private UUID codeCategoryId;

  /**
   * The ID used to uniquely identify the code.
   */
  @ApiModelProperty(value = "The ID used to uniquely identify the code", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String id;

  /**
   * The name of the code.
   */
  @ApiModelProperty(value = "The name of the code", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String name;

  /**
   * The value for the code.
   */
  @ApiModelProperty(value = "The value for the code", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(max = 4000)
  private String value;

  /**
   * Constructs a new <code>Code</code>.
   */
  public Code() {}

  /**
   * Constructs a new <code>Code</code>.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category the code is associated with
   */
  public Code(UUID codeCategoryId)
  {
    this.codeCategoryId = codeCategoryId;
  }

  /**
   * Constructs a new <code>Code</code>.
   *
   * @param id             the ID used to uniquely identify the code
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category the code is associated with
   * @param name           the name of the code
   * @param value          the value for the code
   */
  public Code(String id, UUID codeCategoryId, String name, String value)
  {
    this.id = id;
    this.codeCategoryId = codeCategoryId;
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the code category
   * the code is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the code category
   * the code is associated with
   */
  public UUID getCodeCategoryId()
  {
    return codeCategoryId;
  }

  /**
   * Returns the ID used to uniquely identify the code.
   *
   * @return the ID used to uniquely identify the code
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the code.
   *
   * @return the name of the code
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the value for the code.
   *
   * @return the value for the code
   */
  public String getValue()
  {
    return this.value;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the code category the
   * code is associated with.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category the code is associated with
   */
  public void setCodeCategoryId(UUID codeCategoryId)
  {
    this.codeCategoryId = codeCategoryId;
  }

  /**
   * Set the ID used to uniquely identify the code.
   *
   * @param id the ID used to uniquely identify the code
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the name of the code.
   *
   * @param name the name of the code
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the value for the code.
   *
   * @param value the value for the code
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}

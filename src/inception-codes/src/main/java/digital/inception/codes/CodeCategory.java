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

import digital.inception.core.xml.LocalDateTimeAdapter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.time.LocalDateTime;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <code>CodeCategory</code> class holds the information for a code category.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "CodeCategory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "updated" })
@XmlRootElement(name = "CodeCategory", namespace = "http://codes.inception.digital")
@XmlType(name = "CodeCategory", namespace = "http://codes.inception.digital",
    propOrder = { "id", "name", "updated" })
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeCategory
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category.
   */
  @ApiModelProperty(value = "The Universally Unique Identifier (UUID) used to uniquely identify "
    + "the code category",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /**
   * The name of the code category.
   */
  @ApiModelProperty(value = "The name of the code category", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(max = 4000)
  private String name;

  /**
   * The date and time the code category was updated.
   */
  @ApiModelProperty(value = "The date and time the code category was last updated")
  @JsonProperty
  @XmlElement(name = "Updated")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private LocalDateTime updated;

  /**
   * Constructs a new <code>CodeCategory</code>.
   */
  public CodeCategory() {}

  /**
   * Constructs a new <code>CodeCategory</code>.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the code
   *             category
   * @param name the name of the code category
   */
  public CodeCategory(UUID id, String name)
  {
    this.id = id;
    this.name = name;
  }

  /**
   * Constructs a new <code>CodeCategory</code>.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the code
   *                category
   * @param name    the name of the code category
   * @param updated the date and time the code category was updated
   */
  public CodeCategory(UUID id, String name, LocalDateTime updated)
  {
    this.id = id;
    this.name = name;
    this.updated = updated;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the code category.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the code category
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the code category.
   *
   * @return the name of the code category
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the date and time the code category was updated.
   *
   * @return the date and time the code category was updated
   */
  public LocalDateTime getUpdated()
  {
    return updated;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the code category.
   *
   * @param name the name of the code category
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the date and time the code category was updated.
   *
   * @param updated the date and time the code category was updated
   */
  public void setUpdated(LocalDateTime updated)
  {
    this.updated = updated;
  }
}

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

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>Organization</code> class holds the information for an organization.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "Organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "status" })
@XmlRootElement(name = "Organization", namespace = "http://security.inception.digital")
@XmlType(name = "Organization", namespace = "http://security.inception.digital",
    propOrder = { "id", "name", "status" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class Organization
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the organization.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the organization",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /**
   * The name of the organization.
   */
  @ApiModelProperty(value = "The name of the organization", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String name;

  /**
   * The status for the organization.
   */
  @ApiModelProperty(value = "The status for the organization",
      allowableValues = "0 = Inactive, 1 = Active", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  private OrganizationStatus status;

  /**
   * Constructs a new <code>Organization</code>.
   */
  public Organization() {}

  /**
   * Constructs a new <code>Organization</code>.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the
   *               organization
   * @param name   the name of the organization
   * @param status the status for the organization
   */
  public Organization(UUID id, String name, OrganizationStatus status)
  {
    this.id = id;
    this.name = name;
    this.status = status;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the obj argument otherwise
   *         <code>false</code>
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    Organization other = (Organization) obj;

    return (id != null) && id.equals(other.id);
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the organization.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the organization
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the organization.
   *
   * @return the name of the organization
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the status for the organization.
   *
   * @return the status for the organization
   */
  public OrganizationStatus getStatus()
  {
    return status;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode()
  {
    return (id == null)
        ? 0
        : id.hashCode();
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the organization.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organization
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the organization.
   *
   * @param name the name of the organization
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the status for the organization.
   *
   * @param status the status for the organization
   */
  public void setStatus(OrganizationStatus status)
  {
    this.status = status;
  }
}

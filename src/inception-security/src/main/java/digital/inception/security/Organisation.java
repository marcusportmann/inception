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

package digital.inception.security;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

/**
 * The <code>Organisation</code> class stores the information for an organisation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public class Organisation
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   */
  private UUID id;

  /**
   * The name of the organisation.
   */
  private String name;

  /**
   * The status for the organisation.
   */
  private OrganisationStatus status;

  /**
   * Constructs a new <code>Organisation</code>.
   */
  protected Organisation() {}

  /**
   * Constructs a new <code>Organisation</code>.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the
   *               organisation
   * @param name   the name of the organisation
   * @param status the status for the organisation
   */
  public Organisation(UUID id, String name, OrganisationStatus status)
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

    Organisation other = (Organisation) obj;

    return (id != null) && id.equals(other.id);
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the organisation.
   *
   * @return the name of the organisation
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the status for the organisation.
   *
   * @return the status for the organisation
   */
  public OrganisationStatus getStatus()
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
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the organisation.
   *
   * @param name the name of the organisation
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the status for the organisation.
   *
   * @param status the status for the organisation
   */
  public void setStatus(OrganisationStatus status)
  {
    this.status = status;
  }
}

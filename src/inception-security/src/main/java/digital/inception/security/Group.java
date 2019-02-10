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

import java.util.UUID;

/**
 * The <code>Group</code> class stores the information for a security group.
 *
 * @author Marcus Portmann
 */
public class Group
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The description for the security group.
   */
  private String description;

  /**
   * The name of the security group uniquely identifying the security group.
   */
  private String groupName;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the security group.
   */
  private UUID id;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory the
   * security group is associated with
   */
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>Group</code>.
   */
  public Group() {}

  /**
   * Constructs a new <code>Group</code>.
   *
   * @param groupName the name of the security group uniquely identifying the security group
   */
  public Group(String groupName)
  {
    this.groupName = groupName;
  }

  /**
   * Constructs a new <code>Group</code>.
   *
   * @param id              the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        security group
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory the security group is associated with
   * @param groupName       the name of the security group uniquely identifying the security group
   * @param description     the description for the security group
   */
  public Group(UUID id, UUID userDirectoryId, String groupName, String description)
  {
    this.id = id;
    this.userDirectoryId = userDirectoryId;
    this.groupName = groupName;
    this.description = description;
  }

  /**
   * Returns the description for the security group.
   *
   * @return the description for the security group
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the name of the security group uniquely identifying the security group.
   *
   * @return the name of the security group uniquely identifying the security group
   */
  public String getGroupName()
  {
    return groupName;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the security group.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the security group
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * the security group is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *         the security group is associated with
   */
  public UUID getUserDirectoryId()
  {
    return userDirectoryId;
  }

  /**
   * Set the description for the security group.
   *
   * @param description the description for the security group
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the security group.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the security group
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the user directory the
   * security group is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory the security group is associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }
}

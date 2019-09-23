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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>Group</code> class holds the information for a security group.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "Group")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "userDirectoryId", "groupName", "description" })
@XmlRootElement(name = "Group", namespace = "http://security.inception.digital")
@XmlType(name = "Group", namespace = "http://security.inception.digital",
    propOrder = { "id", "userDirectoryId", "groupName", "description" })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "security", name = "groups")
public class Group
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The roles associated with the group.
   */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(schema = "security", name = "role_to_group_map",
      joinColumns = @JoinColumn(name = "role_id") ,
      inverseJoinColumns = @JoinColumn(name = "group_id"))
  private Set<Role> roles = new HashSet<>();

  /**
   * The users associated with the group.
   */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(schema = "security", name = "user_to_group_map",
      joinColumns = @JoinColumn(name = "user_id") ,
      inverseJoinColumns = @JoinColumn(name = "group_id"))
  private Set<User> users = new HashSet<>();

  /**
   * The description for the security group.
   */
  @ApiModelProperty(value = "The description for the security group")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 100)
  @Column(name = "description", length = 100)
  private String description;

  /**
   * The name of the security group uniquely identifying the security group.
   */
  @ApiModelProperty(
      value = "The name of the security group uniquely identifying the security group",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "GroupName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "groupname", nullable = false, length = 100)
  private String groupName;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the security group.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the security group",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory the
   * security group is associated with
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory the security group is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  @Column(name = "user_directory_id", nullable = false)
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
   * Add the role to the group.
   *
   * @param role the role
   */
  public void addRole(Role role)
  {
    roles.add(role);
    role.getGroups().add(this);
  }

  /**
   * Add the user to the group.
   *
   * @param user the user
   */
  public void addUser(User user)
  {
    users.add(user);
    user.getGroups().add(this);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the object argument otherwise
   *         <code>false</code>
   */
  @Override
  public boolean equals(Object object)
  {
    if (this == object)
    {
      return true;
    }

    if (object == null)
    {
      return false;
    }

    if (getClass() != object.getClass())
    {
      return false;
    }

    Group other = (Group) object;

    return (id != null) && id.equals(other.id);
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
   * Returns the roles associated with the group.
   *
   * @return the roles associated with the group
   */
  public Set<Role> getRoles()
  {
    return roles;
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
   * Returns the users associated with the group.
   *
   * @return the users associated with the group
   */
  public Set<User> getUsers()
  {
    return users;
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
   * Remove the role from the group.
   *
   * @param role the role
   */
  public void removeRole(Role role)
  {
    roles.remove(role);
    role.getGroups().remove(this);
  }

  /**
   * Remove the user from the group.
   *
   * @param user the user
   */
  public void removeUser(User user)
  {
    users.remove(user);
    user.getGroups().remove(this);
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
   * Set the roles associated with the group.
   *
   * @param roles the roles associated with the group
   */
  public void setRoles(Set<Role> roles)
  {
    this.roles = roles;
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

  /**
   * Set the users associated with the group.
   *
   * @param users the users associated with the group
   */
  public void setUsers(Set<User> users)
  {
    this.users = users;
  }
}

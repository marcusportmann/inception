/*
 * Copyright 2020 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Group</code> class holds the information for a group.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Group")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"userDirectoryId", "name", "description"})
@XmlRootElement(name = "Group", namespace = "http://security.inception.digital")
@XmlType(
    name = "Group",
    namespace = "http://security.inception.digital",
    propOrder = {"userDirectoryId", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "security", name = "groups")
@SuppressWarnings({"unused"})
public class Group implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The description for the group. */
  @Schema(description = "The description for the group")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 100)
  @Column(name = "description", length = 100)
  private String description;

  /** The Universally Unique Identifier (UUID) uniquely identifying the group. */
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name identifying the group. */
  @Schema(description = "The name identifying the group", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** The roles associated with the group. */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      schema = "security",
      name = "role_to_group_map",
      joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_code", referencedColumnName = "code"))
  private Set<Role> roles = new HashSet<>();

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the user directory the group is
   * associated with.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the user directory the "
              + "group is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  @Column(name = "user_directory_id", nullable = false)
  private UUID userDirectoryId;

  /** The users associated with the group. */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      schema = "security",
      name = "user_to_group_map",
      joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private Set<User> users = new HashSet<>();

  /** Constructs a new <code>Group</code>. */
  public Group() {}

  /**
   * Constructs a new <code>Group</code>.
   *
   * @param name the name identifying the group
   */
  public Group(String name) {
    this.name = name;
  }

  /**
   * Constructs a new <code>Group</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory the group is associated with
   * @param name the name identifying the group
   * @param description the description for the group
   */
  public Group(UUID userDirectoryId, String name, String description) {
    this.userDirectoryId = userDirectoryId;
    this.name = name;
    this.description = description;
  }

  /**
   * Add the role to the group.
   *
   * @param role the role
   */
  public void addRole(Role role) {
    roles.add(role);
    role.getGroups().add(this);
  }

  /**
   * Add the user to the group.
   *
   * @param user the user
   */
  public void addUser(User user) {
    users.add(user);
    user.getGroups().add(this);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    Group other = (Group) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the description for the group.
   *
   * @return the description for the group
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the group.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the group
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name identifying the group.
   *
   * @return the name identifying the group
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the roles associated with the group.
   *
   * @return the roles associated with the group
   */
  public Set<Role> getRoles() {
    return roles;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the user directory the
   * group is associated with.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user directory the
   *     group is associated with
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }

  /**
   * Returns the users associated with the group.
   *
   * @return the users associated with the group
   */
  public Set<User> getUsers() {
    return users;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Remove the role from the group.
   *
   * @param role the role
   */
  public void removeRole(Role role) {
    roles.remove(role);
    role.getGroups().remove(this);
  }

  /**
   * Remove the user from the group.
   *
   * @param user the user
   */
  public void removeUser(User user) {
    users.remove(user);
    user.getGroups().remove(this);
  }

  /**
   * Set the description for the group.
   *
   * @param description the description for the group
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the group.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the group
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name identifying the group.
   *
   * @param name the name identifying the group
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the roles associated with the group.
   *
   * @param roles the roles associated with the group
   */
  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the user directory the group
   * is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory the group is associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId) {
    this.userDirectoryId = userDirectoryId;
  }

  /**
   * Set the users associated with the group.
   *
   * @param users the users associated with the group
   */
  public void setUsers(Set<User> users) {
    this.users = users;
  }
}

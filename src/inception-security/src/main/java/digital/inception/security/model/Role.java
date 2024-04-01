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

package digital.inception.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The <b>Role</b> class holds the information for a role.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A representation of a particular job function that is used to assign the permissions needed to perform particular system functions to a user")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "name", "description"})
@XmlRootElement(name = "Role", namespace = "http://inception.digital/security")
@XmlType(
    name = "Role",
    namespace = "http://inception.digital/security",
    propOrder = {"code", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "security_roles")
@SuppressWarnings({"unused"})
public class Role implements java.io.Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the role. */
  @Schema(description = "The code for the role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "code", length = 100, nullable = false)
  private String code;

  /** The description for the role. */
  @Schema(description = "The description for the role")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 100)
  @Column(name = "description", length = 100)
  private String description;

  /** The functions associated with the role. */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "security_function_to_role_map",
      joinColumns = @JoinColumn(name = "role_code", referencedColumnName = "code"),
      inverseJoinColumns = @JoinColumn(name = "function_code", referencedColumnName = "code"))
  private Set<Function> functions = new HashSet<>();

  /** The groups the role is associated with. */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(mappedBy = "roles")
  private Set<Group> groups = new HashSet<>();

  /** The name of the role. */
  @Schema(description = "The name of the role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** Constructs a new <b>Role</b>. */
  public Role() {}

  /**
   * Add the function to the role.
   *
   * @param function the function
   */
  public void addFunction(Function function) {
    functions.removeIf(
        existingFunction -> Objects.equals(existingFunction.getCode(), function.getCode()));

    functions.add(function);
    function.getRoles().add(this);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
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

    Role other = (Role) object;

    return Objects.equals(code, other.code);
  }

  /**
   * Returns the code for the role.
   *
   * @return the code for the role
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the role.
   *
   * @return the description for the role
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the functions associated with the role.
   *
   * @return the functions associated with the role
   */
  public Set<Function> getFunctions() {
    return functions;
  }

  /**
   * Returns the groups the role is associated with.
   *
   * @return the groups the role is associated with
   */
  public Set<Group> getGroups() {
    return groups;
  }

  /**
   * Returns the name of the role.
   *
   * @return the name of the role
   */
  public String getName() {
    return name;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (code == null) ? 0 : code.hashCode();
  }

  /**
   * Remove the function to the role.
   *
   * @param function the function
   */
  public void removeFunction(Function function) {
    functions.remove(function);
    function.getRoles().remove(this);
  }

  /**
   * Set the code for the role.
   *
   * @param code the code for the role
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the role.
   *
   * @param description the description for the role
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the functions associated with the role.
   *
   * @param functions the functions associated with the role
   */
  public void setFunctions(Set<Function> functions) {
    this.functions = functions;
  }

  /**
   * Set the groups the role is associated with.
   *
   * @param groups the groups the role is associated with
   */
  public void setGroups(Set<Group> groups) {
    this.groups = groups;
  }

  /**
   * Set the name of the role.
   *
   * @param name the name of the role
   */
  public void setName(String name) {
    this.name = name;
  }
}

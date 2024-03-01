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

package digital.inception.security;

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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * The <b>Tenant</b> class holds the information for a tenant.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A group of users who share common access with specific privileges to a particular subset of the information managed by a multi-tenant application")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "status"})
@XmlRootElement(name = "Tenant", namespace = "http://inception.digital/security")
@XmlType(
    name = "Tenant",
    namespace = "http://inception.digital/security",
    propOrder = {"id", "name", "status"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "tenants")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Tenant implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the tenant. */
  @Schema(description = "The ID for the tenant", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name of the tenant. */
  @Schema(description = "The name of the tenant", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The status for the tenant. */
  @Schema(description = "The status for the tenant", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private TenantStatus status;

  /** The user directories associated with the tenant. */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "user_directory_to_tenant_map",
      joinColumns = @JoinColumn(name = "tenant_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_directory_id", referencedColumnName = "id"))
  private Set<UserDirectory> userDirectories = new HashSet<>();

  /** Constructs a new <b>Tenant</b>. */
  public Tenant() {}

  /**
   * Constructs a new <b>Tenant</b>.
   *
   * @param name the name of the tenant
   * @param status the status for the tenant
   */
  public Tenant(String name, TenantStatus status) {
    this.name = name;
    this.status = status;
  }

  /**
   * Constructs a new <b>Tenant</b>.
   *
   * @param id the ID for the tenant
   * @param name the name of the tenant
   * @param status the status for the tenant
   */
  public Tenant(UUID id, String name, TenantStatus status) {
    this.id = id;
    this.name = name;
    this.status = status;
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

    Tenant other = (Tenant) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the tenant.
   *
   * @return the ID for the tenant
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the tenant.
   *
   * @return the name of the tenant
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the status for the tenant.
   *
   * @return the status for the tenant
   */
  public TenantStatus getStatus() {
    return status;
  }

  /**
   * Returns the user directories associated with the tenant.
   *
   * @return the user directories associated with the tenant
   */
  public Set<UserDirectory> getUserDirectories() {
    return userDirectories;
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
   * Link the user directory to the tenant.
   *
   * @param userDirectory the user directory
   */
  public void linkUserDirectory(UserDirectory userDirectory) {
    userDirectories.add(userDirectory);
    userDirectory.getTenants().add(this);
  }

  /**
   * Set the ID for the tenant.
   *
   * @param id the ID for the tenant
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the tenant.
   *
   * @param name the name of the tenant
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the status for the tenant.
   *
   * @param status the status for the tenant
   */
  public void setStatus(TenantStatus status) {
    this.status = status;
  }

  /**
   * Set the user directories associated with the tenant.
   *
   * @param userDirectories the user directories associated with the tenant
   */
  public void setUserDirectories(Set<UserDirectory> userDirectories) {
    this.userDirectories = userDirectories;
  }

  /**
   * Unlink the user directory from the tenant.
   *
   * @param userDirectory the user directory
   */
  public void unlinkUserDirectory(UserDirectory userDirectory) {
    userDirectories.remove(userDirectory);
    userDirectory.getTenants().remove(this);
  }
}

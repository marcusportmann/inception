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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * The <b>UserDirectorySummary</b> class holds the summary information for a user directory.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A user directory summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "name"})
@XmlRootElement(name = "UserDirectorySummary", namespace = "http://inception.digital/security")
@XmlType(
    name = "UserDirectorySummary",
    namespace = "http://inception.digital/security",
    propOrder = {"id", "type", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "user_directories")
@SuppressWarnings({"unused"})
public class UserDirectorySummary implements java.io.Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The user directories associated with the tenant. */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(cascade = {CascadeType.REFRESH})
  @JoinTable(
      name = "user_directory_to_tenant_map",
      joinColumns = @JoinColumn(name = "user_directory_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "tenant_id", referencedColumnName = "id"))
  private final Set<Tenant> tenants = new HashSet<>();

  /** The ID for the user directory. */
  @Schema(
      description = "The ID for the user directory",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name of the user directory. */
  @Schema(
      description = "The name of the user directory",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The code for the user directory type. */
  @Schema(
      description = "The code for the user directory type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "type", length = 100, nullable = false)
  private String type;

  /** Constructs a new <b>UserDirectorySummary</b>. */
  public UserDirectorySummary() {}

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

    UserDirectorySummary other = (UserDirectorySummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the user directory.
   *
   * @return the ID for the user directory
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the user directory.
   *
   * @return the name of the user directory
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the code for the user directory type.
   *
   * @return the code for the user directory type
   */
  public String getType() {
    return type;
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
}

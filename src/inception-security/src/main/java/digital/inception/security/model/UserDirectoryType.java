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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.util.Objects;

/**
 * The <b>UserDirectoryType</b> class holds the information for a user directory type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A user directory type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "name", "userDirectoryClassName"})
@XmlRootElement(name = "UserDirectoryType", namespace = "http://inception.digital/security")
@XmlType(
    name = "UserDirectoryType",
    namespace = "http://inception.digital/security",
    propOrder = {"code", "name", "userDirectoryClassName"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "security_user_directory_types")
@SuppressWarnings({"unused"})
public class UserDirectoryType implements java.io.Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the user directory type. */
  @Schema(
      description = "The code for the user directory type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "code", length = 100, nullable = false)
  private String code;

  /** The name of the user directory type. */
  @Schema(
      description = "The name of the user directory type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The Java class that implements the user directory type. */
  @JsonIgnore @Transient private transient Class userDirectoryClass;

  /** The fully qualified name of the Java class that implements the user directory type. */
  @Schema(
      description =
          "The fully qualified name of the Java class that implements the user directory type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryClassName", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "user_directory_class", length = 1000, nullable = false)
  private String userDirectoryClassName;

  /** Constructs a new <b>UserDirectoryType</b>. */
  public UserDirectoryType() {}

  /**
   * Constructs a new <b>UserDirectoryType</b>.
   *
   * @param code the code for the user directory type
   * @param name the name of the user directory type
   * @param userDirectoryClassName the fully qualified name of the Java class that implements the
   *     user directory type
   */
  UserDirectoryType(String code, String name, String userDirectoryClassName) {
    this.code = code;
    this.name = name;
    this.userDirectoryClassName = userDirectoryClassName;
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

    UserDirectoryType other = (UserDirectoryType) object;

    return Objects.equals(code, other.code);
  }

  /**
   * Returns the code for the user directory type.
   *
   * @return the code for the user directory type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the name of the user directory type.
   *
   * @return the name of the user directory type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the fully qualified name of the Java class that implements the user directory type.
   *
   * @return the fully qualified name of the Java class that implements the user directory type
   */
  public String getUserDirectoryClassName() {
    return userDirectoryClassName;
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
}

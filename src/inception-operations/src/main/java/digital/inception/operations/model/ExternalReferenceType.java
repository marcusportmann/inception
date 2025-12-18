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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code ExternalReferenceType} class holds the information for an external reference type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An external reference type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "objectType", "tenantId", "name", "description"})
@XmlRootElement(name = "ExternalReferenceType", namespace = "https://inception.digital/operations")
@XmlType(
    name = "ExternalReferenceType",
    namespace = "https://inception.digital/operations",
    propOrder = {"code", "objectType", "tenantId", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_external_reference_types")
@SuppressWarnings({"unused", "WeakerAccess"})
public class ExternalReferenceType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the external reference type. */
  @Schema(
      description = "The code for the external reference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the external reference type. */
  @Schema(
      description = "The description for the external reference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The name of the external reference type. */
  @Schema(
      description = "The name of the external reference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The type of object the external reference type is specific to. */
  @Schema(description = "The type of object the external reference type is specific to")
  @JsonProperty
  @XmlElement(name = "ObjectType")
  @Column(name = "object_type")
  private ObjectType objectType;

  /** The ID for the tenant the external reference type is specific to. */
  @Schema(description = "The ID for the tenant the external reference type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new {@code ExternalReferenceType}. */
  public ExternalReferenceType() {}

  /**
   * Constructs a new {@code ExternalReferenceType}.
   *
   * @param code the code for the external reference type
   * @param name the name of the external reference type
   * @param description the description for the external reference type
   */
  public ExternalReferenceType(String code, String name, String description) {
    this.code = code;
    this.name = name;
    this.description = description;
  }

  /**
   * Constructs a new {@code ExternalReferenceType}.
   *
   * @param code the code for the external reference type
   * @param name the name of the external reference type
   * @param description the description for the external reference type
   * @param objectType the type of object the external reference type is specific to
   * @param tenantId the ID for the tenant the external reference type is specific to
   */
  public ExternalReferenceType(
      String code, String name, String description, ObjectType objectType, UUID tenantId) {
    this.code = code;
    this.name = name;
    this.description = description;
    this.objectType = objectType;
    this.tenantId = tenantId;
  }

  /**
   * Constructs a new {@code ExternalReferenceType}.
   *
   * @param code the code for the external reference type
   * @param name the name of the external reference type
   * @param description the description for the external reference type
   * @param objectType the type of object the external reference type is specific to
   */
  public ExternalReferenceType(
      String code, String name, String description, ObjectType objectType) {
    this.code = code;
    this.name = name;
    this.description = description;
    this.objectType = objectType;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument otherwise {@code false}
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

    ExternalReferenceType other = (ExternalReferenceType) object;

    return Objects.equals(code, other.code);
  }

  /**
   * Returns the code for the external reference type.
   *
   * @return the code for the external reference type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the external reference type.
   *
   * @return the description for the external reference type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the name of the external reference type.
   *
   * @return the name of the external reference type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the type of object the external reference type is specific to.
   *
   * @return the type of object the external reference type is specific to
   */
  public ObjectType getObjectType() {
    return objectType;
  }

  /**
   * Returns the ID for the tenant the external reference type is specific to.
   *
   * @return the ID for the tenant the external reference type is specific to
   */
  public UUID getTenantId() {
    return tenantId;
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
   * Sets the code for the external reference type.
   *
   * @param code the code for the external reference type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Sets the description for the external reference type.
   *
   * @param description the description for the external reference type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the name of the external reference type.
   *
   * @param name the name of the external reference type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the type of object the external reference type is specific to.
   *
   * @param objectType the type of object the external reference type is specific to
   */
  public void setObjectType(ObjectType objectType) {
    this.objectType = objectType;
  }

  /**
   * Sets the ID for the tenant the external reference type is specific to.
   *
   * @param tenantId the ID for the tenant the external reference type is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}

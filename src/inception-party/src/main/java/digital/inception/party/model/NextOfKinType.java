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

package digital.inception.party.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
 * The {@code NextOfKinType} class holds the information for a next of kin type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of living relative for a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "localeId", "tenantId", "sortOrder", "name", "description"})
@XmlRootElement(name = "NextOfKinType", namespace = "https://inception.digital/party")
@XmlType(
    name = "NextOfKinType",
    namespace = "https://inception.digital/party",
    propOrder = {"code", "localeId", "tenantId", "sortOrder", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_next_of_kin_types")
@IdClass(NextOfKinTypeId.class)
public class NextOfKinType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the next of kin type. */
  @Schema(
      description = "The code for the next of kin type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the next of kin type. */
  @Schema(
      description = "The description for the next of kin type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the next of kin type. */
  @Schema(
      description = "The Unicode locale identifier for the next of kin type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the next of kin type. */
  @Schema(
      description = "The name of the next of kin type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The sort order for the next of kin type. */
  @Schema(
      description = "The sort order for the next of kin type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortOrder", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortOrder;

  /** The ID for the tenant the next of kin type is specific to. */
  @Schema(description = "The ID for the tenant the next of kin type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new {@code NextOfKinType}. */
  public NextOfKinType() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    NextOfKinType other = (NextOfKinType) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the next of kin type.
   *
   * @return the code for the next of kin type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the next of kin type.
   *
   * @return the description for the next of kin type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the next of kin type.
   *
   * @return the Unicode locale identifier for the next of kin type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the next of kin type.
   *
   * @return the name of the next of kin type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort order for the next of kin type.
   *
   * @return the sort order for the next of kin type
   */
  public Integer getSortOrder() {
    return sortOrder;
  }

  /**
   * Returns the ID for the tenant the next of kin type is specific to.
   *
   * @return the ID for the tenant the next of kin type is specific to
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
    return ((code == null) ? 0 : code.hashCode()) + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Sets the code for the next of kin type.
   *
   * @param code the code for the next of kin type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Sets the description for the next of kin type.
   *
   * @param description the description for the next of kin type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the Unicode locale identifier for the next of kin type.
   *
   * @param localeId the Unicode locale identifier for the next of kin type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Sets the name of the next of kin type.
   *
   * @param name the name of the next of kin type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the sort order for the next of kin type.
   *
   * @param sortOrder the sort order for the next of kin type
   */
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  /**
   * Sets the ID for the tenant the next of kin type is specific to.
   *
   * @param tenantId the ID for the tenant the next of kin type is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}

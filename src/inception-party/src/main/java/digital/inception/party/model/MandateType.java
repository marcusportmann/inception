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
 * The {@code MandateType} class holds the information for a mandate type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of mandate")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "localeId", "tenantId", "sortIndex", "name", "description"})
@XmlRootElement(name = "MandateType", namespace = "https://inception.digital/party")
@XmlType(
    name = "MandateType",
    namespace = "https://inception.digital/party",
    propOrder = {"code", "localeId", "tenantId", "sortIndex", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_mandate_types")
@IdClass(MandateTypeId.class)
public class MandateType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the mandate type. */
  @Schema(
      description = "The code for the mandate type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the mandate type. */
  @Schema(
      description = "The description for the mandate type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the mandate type. */
  @Schema(
      description = "The Unicode locale identifier for the mandate type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the mandate type. */
  @Schema(description = "The name of the mandate type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The sort index for the mandate type. */
  @Schema(
      description = "The sort index for the mandate type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The ID for the tenant the mandate type is specific to. */
  @Schema(description = "The ID for the tenant the mandate type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new {@code MandateType}. */
  public MandateType() {}

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

    MandateType other = (MandateType) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the mandate type.
   *
   * @return the code for the mandate type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the mandate type.
   *
   * @return the description for the mandate type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the mandate type.
   *
   * @return the Unicode locale identifier for the mandate type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the mandate type.
   *
   * @return the name of the mandate type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort index for the mandate type.
   *
   * @return the sort index for the mandate type
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the ID for the tenant the mandate type is specific to.
   *
   * @return the ID for the tenant the mandate type is specific to
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
   * Set the code for the mandate type.
   *
   * @param code the code for the mandate type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the mandate type.
   *
   * @param description the description for the mandate type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the mandate type.
   *
   * @param localeId the Unicode locale identifier for the mandate type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the mandate type.
   *
   * @param name the name of the mandate type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the sort index for the mandate type.
   *
   * @param sortIndex the sort index for the mandate type
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the ID for the tenant the mandate type is specific to.
   *
   * @param tenantId the ID for the tenant the mandate type is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}

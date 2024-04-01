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

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.regex.Pattern;

/**
 * The <b>AssociationPropertyType</b> class holds the information for an association property type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of association property")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "associationType",
  "code",
  "localeId",
  "tenantId",
  "sortIndex",
  "name",
  "description",
  "valueType",
  "pattern"
})
@XmlRootElement(name = "AssociationPropertyType", namespace = "http://inception.digital/party")
@XmlType(
    name = "AssociationPropertyType",
    namespace = "http://inception.digital/party",
    propOrder = {
      "associationType",
      "code",
      "localeId",
      "tenantId",
      "sortIndex",
      "name",
      "description",
      "valueType",
      "pattern"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_association_property_types")
@IdClass(AssociationPropertyTypeId.class)
public class AssociationPropertyType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the association type the association property type is associated with. */
  @Schema(
      description =
          "The code for the association type the association property type is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "AssociationType", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "association_type", length = 50, nullable = false)
  private String associationType;

  /** The code for the association property type. */
  @Schema(
      description = "The code for the association property type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The compiled pattern. */
  @JsonIgnore private transient Pattern compiledPattern;

  /** The description for the association property type. */
  @Schema(
      description = "The description for the association property type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the association property type. */
  @Schema(
      description = "The Unicode locale identifier for the association property type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the association property type. */
  @Schema(
      description = "The name of the association property type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /**
   * The regular expression pattern used to validate a string value for the association property
   * type.
   */
  @Schema(
      description =
          "The regular expression pattern used to validate a string value for the association property type")
  @JsonProperty
  @XmlElement(name = "Pattern")
  @Size(min = 1, max = 1000)
  @Column(name = "pattern", length = 1000)
  private String pattern;

  /** The sort index for the association property type. */
  @Schema(
      description = "The sort index for the association property type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The ID for the tenant the association property type is specific to. */
  @Schema(description = "The ID for the tenant the association property type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** The value type for the association property type. */
  @Schema(
      description = "The value type for the association property type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ValueType", required = true)
  @Column(name = "value_type", length = 10, nullable = false)
  private ValueType valueType;

  /** Constructs a new <b>AssociationPropertyType</b>. */
  public AssociationPropertyType() {}

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

    AssociationPropertyType other = (AssociationPropertyType) object;

    return Objects.equals(associationType, other.associationType)
        && Objects.equals(code, other.code)
        && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the association type the association property type is associated with.
   *
   * @return the code for the association type the association property type is associated with
   */
  public String getAssociationType() {
    return associationType;
  }

  /**
   * Returns the code for the association property type.
   *
   * @return the code for the association property type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the compiled pattern.
   *
   * @return the compiled pattern
   */
  @JsonIgnore
  public Pattern getCompiledPattern() {
    if (compiledPattern == null) {
      compiledPattern = Pattern.compile(pattern);
    }

    return compiledPattern;
  }

  /**
   * Returns the description for the association property type.
   *
   * @return the description for the association property type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the association property type.
   *
   * @return the Unicode locale identifier for the association property type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the association property type.
   *
   * @return the name of the association property type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the regular expression pattern used to validate a string value for the association
   * property type.
   *
   * @return the regular expression pattern used to validate a string value for the association
   *     property type
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * Returns the sort index for the association property type.
   *
   * @return the sort index for the association property type
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the ID for the tenant the association property type is specific to.
   *
   * @return the ID for the tenant the association property type is specific to
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the value type for the association property type.
   *
   * @return the value type for the association property type
   */
  public ValueType getValueType() {
    return valueType;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((associationType == null) ? 0 : associationType.hashCode())
        + ((code == null) ? 0 : code.hashCode())
        + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Set the code for the association type the association property type is associated with.
   *
   * @param associationType the code for the association type the association property type is
   *     associated with
   */
  @SuppressWarnings("unused")
  public void setAssociationType(String associationType) {
    this.associationType = associationType;
  }

  /**
   * Set the code for the association property type.
   *
   * @param code the code for the association property type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the association property type.
   *
   * @param description the description for the association property type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the association property type.
   *
   * @param localeId the Unicode locale identifier for the association property type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the association property type.
   *
   * @param name the name of the association property type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the regular expression pattern used to validate a string value for the association property
   * type.
   *
   * @param pattern the regular expression pattern used to validate a string value for the
   *     association property type
   */
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  /**
   * Set the sort index for the association property type.
   *
   * @param sortIndex the sort index for the association property type
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the ID for the tenant the association property type is specific to.
   *
   * @param tenantId the ID for the tenant the association property type is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the value type for the association property type.
   *
   * @param valueType the value type for the association property type
   */
  @SuppressWarnings("unused")
  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }
}

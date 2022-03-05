/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>MandatePropertyType</b> class holds the information for a mandate property type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of mandate property")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "mandateType",
  "code",
  "localeId",
  "tenantId",
  "sortIndex",
  "name",
  "description",
  "valueType",
  "pattern"
})
@XmlRootElement(name = "MandatePropertyType", namespace = "http://inception.digital/party")
@XmlType(
    name = "MandatePropertyType",
    namespace = "http://inception.digital/party",
    propOrder = {
      "mandateType",
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
@Table(schema = "party", name = "mandate_property_types")
@IdClass(MandatePropertyTypeId.class)
public class MandatePropertyType implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the mandate property type. */
  @Schema(description = "The code for the mandate property type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "code", length = 30, nullable = false)
  private String code;

  /** The compiled pattern. */
  @JsonIgnore private transient Pattern compiledPattern;

  /** The description for the mandate property type. */
  @Schema(description = "The description for the mandate property type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the mandate property type. */
  @Schema(
      description = "The Unicode locale identifier for the mandate property type",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The code for the mandate type the mandate property type is associated with. */
  @Schema(
      description = "The code for the mandate type the mandate property type is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MandateType", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "mandate_type", length = 30, nullable = false)
  private String mandateType;

  /** The name of the mandate property type. */
  @Schema(description = "The name of the mandate property type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /**
   * The regular expression pattern used to validate a string value for the mandate property type.
   */
  @Schema(
      description =
          "The regular expression pattern used to validate a string value for the mandate property type")
  @JsonProperty
  @XmlElement(name = "Pattern")
  @Size(min = 1, max = 1000)
  @Column(name = "pattern", length = 1000)
  private String pattern;

  /** The sort index for the mandate property type. */
  @Schema(description = "The sort index for the mandate property type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The ID for the tenant the mandate property type is specific to. */
  @Schema(description = "The ID for the tenant the mandate property type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** The value type for the mandate property type. */
  @Schema(description = "The value type for the mandate property type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ValueType", required = true)
  @Column(name = "value_type", length = 10, nullable = false)
  private ValueType valueType;

  /** Constructs a new <b>MandatePropertyType</b>. */
  public MandatePropertyType() {}

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

    MandatePropertyType other = (MandatePropertyType) object;

    return Objects.equals(mandateType, other.mandateType)
        && Objects.equals(code, other.code)
        && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the mandate property type.
   *
   * @return the code for the mandate property type
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
   * Returns the description for the mandate property type.
   *
   * @return the description for the mandate property type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the mandate property type.
   *
   * @return the Unicode locale identifier for the mandate property type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the code for the mandate type the mandate property type is associated with.
   *
   * @return the code for the mandate type the mandate property type is associated with
   */
  public String getMandateType() {
    return mandateType;
  }

  /**
   * Returns the name of the mandate property type.
   *
   * @return the name of the mandate property type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the regular expression pattern used to validate a string value for the mandate property
   * type.
   *
   * @return the regular expression pattern used to validate a string value for the mandate property
   *     type
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * Returns the sort index for the mandate property type.
   *
   * @return the sort index for the mandate property type
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the ID for the tenant the mandate property type is specific to.
   *
   * @return the ID for the tenant the mandate property type is specific to
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the value type for the mandate property type.
   *
   * @return the value type for the mandate property type
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
    return ((mandateType == null) ? 0 : mandateType.hashCode())
        + ((code == null) ? 0 : code.hashCode())
        + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Set the code for the mandate property type.
   *
   * @param code the code for the mandate property type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the mandate property type.
   *
   * @param description the description for the mandate property type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the mandate property type.
   *
   * @param localeId the Unicode locale identifier for the mandate property type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the code for the mandate type the mandate property type is associated with.
   *
   * @param mandateType the code for the mandate type the mandate property type is associated with
   */
  public void setMandateType(String mandateType) {
    this.mandateType = mandateType;
  }

  /**
   * Set the name of the mandate property type.
   *
   * @param name the name of the mandate property type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the regular expression pattern used to validate a string value for the mandate property
   * type.
   *
   * @param pattern the regular expression pattern used to validate a string value for the mandate
   *     property type
   */
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  /**
   * Set the sort index for the mandate property type.
   *
   * @param sortIndex the sort index for the mandate property type
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the ID for the tenant the mandate property type is specific to.
   *
   * @param tenantId the ID for the tenant the mandate property type is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the value type for the mandate property type.
   *
   * @param valueType the value type for the mandate property type
   */
  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }
}

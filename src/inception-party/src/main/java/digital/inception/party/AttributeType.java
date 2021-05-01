/*
 * Copyright 2021 Marcus Portmann
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.springframework.util.StringUtils;

/**
 * The <b>AttributeType</b> class holds the information for an attribute type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of attribute")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "category",
  "localeId",
  "tenantId",
  "sortIndex",
  "name",
  "description",
  "partyTypes",
  "unitType"
})
@XmlRootElement(name = "AttributeType", namespace = "http://inception.digital/party")
@XmlType(
    name = "AttributeType",
    namespace = "http://inception.digital/party",
    propOrder = {
      "code",
      "category",
      "localeId",
      "tenantId",
      "sortIndex",
      "name",
      "description",
      "partyTypes",
      "unitType"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "attribute_types")
@IdClass(AttributeTypeId.class)
public class AttributeType implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the attribute type category the attribute type is associated with. */
  @Schema(
      description =
          "The code for the attribute type category the attribute type is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Category", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "category", length = 30, nullable = false)
  private String category;

  /** The code for the attribute type. */
  @Schema(description = "The code for the attribute type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "code", length = 30, nullable = false)
  private String code;

  /** The description for the attribute type. */
  @Schema(description = "The description for the attribute type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the attribute type. */
  @Schema(description = "The Unicode locale identifier for the attribute type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the attribute type. */
  @Schema(description = "The name of the attribute type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The comma-delimited codes for the party types the attribute type is associated with. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Size(min = 1, max = 310)
  @Column(name = "party_types", length = 310, nullable = false)
  private String partyTypes;

  /** The sort index for the attribute type. */
  @Schema(description = "The sort index for the attribute type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The Universally Unique Identifier (UUID) for the tenant the attribute type is specific to. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the tenant the attribute type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** The measurement unit type for the attribute type. */
  @Schema(description = "The measurement unit type for the attribute type")
  @JsonProperty
  @XmlElement(name = "UnitType")
  @Column(name = "unit_type", length = 30)
  private MeasurementUnitType unitType;

  /** Constructs a new <b>AttributeType</b>. */
  public AttributeType() {}

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

    AttributeType other = (AttributeType) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the attribute type category the attribute type is associated with.
   *
   * @return the code for the attribute type category the attribute type is associated with
   */
  public String getCategory() {
    return category;
  }

  /**
   * Returns the code for the attribute type.
   *
   * @return the code for the attribute type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the attribute type.
   *
   * @return the description for the attribute type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the attribute type.
   *
   * @return the Unicode locale identifier for the attribute type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the attribute type.
   *
   * @return the name of the attribute type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the codes for the party types the attribute type is associated with.
   *
   * @return the codes for the party types the attribute type is associated with
   */
  @Schema(
      description = "The codes for the party types the attribute type is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyTypes", required = true)
  public String[] getPartyTypes() {
    return StringUtils.commaDelimitedListToStringArray(partyTypes);
  }

  /**
   * Returns the sort index for the attribute type.
   *
   * @return the sort index for the attribute type
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the attribute type is specific
   * to.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the attribute type is specific
   *     to
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the measurement unit type for the attribute type.
   *
   * @return the measurement unit type for the attribute type
   */
  public MeasurementUnitType getUnitType() {
    return unitType;
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
   * Returns whether the attribute type is valid for the party type.
   *
   * @param partyTypeCode the code for the party type
   * @return <b>true</b> if the attribute type is valid for the party type or <b>false</b> otherwise
   */
  public boolean isValidForPartyType(String partyTypeCode) {
    return Arrays.stream(getPartyTypes())
        .anyMatch(validPartyType -> validPartyType.equals(partyTypeCode));
  }

  /**
   * Set the code for the attribute type category the attribute type is associated with.
   *
   * @param category the code for the attribute type category the attribute type is associated with
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Set the code for the attribute type.
   *
   * @param code the code for the attribute type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the attribute type.
   *
   * @param description the description for the attribute type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the attribute type.
   *
   * @param localeId the Unicode locale identifier for the attribute type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the attribute type.
   *
   * @param name the name of the attribute type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the codes for the party types the attribute type is associated with.
   *
   * @param partyTypes the codes for the party types the attribute type is associated with
   */
  public void setPartyTypes(String[] partyTypes) {
    this.partyTypes = StringUtils.arrayToCommaDelimitedString(partyTypes);
  }

  /**
   * Set the codes for the party types the attribute type is associated with.
   *
   * @param partyTypes the codes for the party types the attribute type is associated with
   */
  @JsonIgnore
  public void setPartyTypes(Collection<String> partyTypes) {
    this.partyTypes = StringUtils.collectionToDelimitedString(partyTypes, ",");
  }

  /**
   * Set the sort index for the attribute type.
   *
   * @param sortIndex the sort index for the attribute type
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the attribute type is specific to.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the attribute type is
   *     specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the measurement unit type for the attribute type.
   *
   * @param unitType the measurement unit type for the attribute type
   */
  public void setUnitType(MeasurementUnitType unitType) {
    this.unitType = unitType;
  }
}

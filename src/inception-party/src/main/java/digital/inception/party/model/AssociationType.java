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
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 * The <b>AssociationType</b> class holds the information for an association type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of association")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "localeId",
  "tenantId",
  "sortIndex",
  "name",
  "description",
  "firstPartyTypes",
  "firstPartyRole",
  "secondPartyTypes",
  "secondPartyRole"
})
@XmlRootElement(name = "AssociationType", namespace = "http://inception.digital/party")
@XmlType(
    name = "AssociationType",
    namespace = "http://inception.digital/party",
    propOrder = {
      "code",
      "localeId",
      "tenantId",
      "sortIndex",
      "name",
      "description",
      "firstPartyTypes",
      "firstPartyRole",
      "secondPartyTypes",
      "secondPartyRole"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_association_types")
@IdClass(AssociationTypeId.class)
@SuppressWarnings("unused")
public class AssociationType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the association type. */
  @Schema(
      description = "The code for the association type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the association type. */
  @Schema(
      description = "The description for the association type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The code for the role type for the first party in the association. */
  @Schema(
      description = "The code for the role type for the first party in the association",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "FirstPartyRole", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "first_party_role", length = 50, nullable = false)
  private String firstPartyRole;

  /**
   * The comma-delimited list of codes for the party types for the first party in the association.
   */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Size(min = 1, max = 310)
  @Column(name = "first_party_types", length = 310, nullable = false)
  private String firstPartyTypes;

  /** The Unicode locale identifier for the association type. */
  @Schema(
      description = "The Unicode locale identifier for the association type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the association type. */
  @Schema(
      description = "The name of the association type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The code for the role type for the second party in the association. */
  @Schema(
      description = "The code for the role type for the second party in the association",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SecondPartyRole", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "second_party_role", length = 50, nullable = false)
  private String secondPartyRole;

  /**
   * The comma-delimited list of codes for the party types for the second party in the association.
   */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Size(min = 1, max = 310)
  @Column(name = "second_party_types", length = 310, nullable = false)
  private String secondPartyTypes;

  /** The sort index for the association type. */
  @Schema(
      description = "The sort index for the association type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The ID for the tenant the association type is specific to. */
  @Schema(description = "The ID for the tenant the association type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new <b>AssociationType</b>. */
  public AssociationType() {}

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

    AssociationType other = (AssociationType) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the association type.
   *
   * @return the code for the association type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the association type.
   *
   * @return the description for the association type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the code for the role type for the first party in the association.
   *
   * @return the code for the role type for the first party in the association
   */
  public String getFirstPartyRole() {
    return firstPartyRole;
  }

  /**
   * Returns the codes for the party types for the first party in the association.
   *
   * @return the codes for the party types for the first party in the association
   */
  @Schema(
      description = "The codes for the party types for the first party in the association",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "FirstPartyTypes", required = true)
  public String[] getFirstPartyTypes() {
    return StringUtils.commaDelimitedListToStringArray(firstPartyTypes);
  }

  /**
   * Returns the Unicode locale identifier for the association type.
   *
   * @return the Unicode locale identifier for the association type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the association type.
   *
   * @return the name of the association type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the code for the role type for the second party in the association.
   *
   * @return the code for the role type for the second party in the association
   */
  public String getSecondPartyRole() {
    return secondPartyRole;
  }

  /**
   * Returns the codes for the party types for the second party in the association.
   *
   * @return the codes for the party types for the second party in the association
   */
  @Schema(
      description = "The codes for the party types for the second party in the association",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SecondPartyTypes", required = true)
  public String[] getSecondPartyTypes() {
    return StringUtils.commaDelimitedListToStringArray(secondPartyTypes);
  }

  /**
   * Returns the sort index for the association type.
   *
   * @return the sort index for the association type
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the ID for the tenant the association type is specific to.
   *
   * @return the ID for the tenant the association type is specific to
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
   * Returns whether the party type is valid for the first party.
   *
   * @param partyTypeCode the code for the party type
   * @return <b>true</b> if the party type is valid for the first party or <b>false</b> otherwise
   */
  public boolean isValidFirstPartyType(String partyTypeCode) {
    if (!StringUtils.hasText(partyTypeCode)) {
      return false;
    }

    return Arrays.asList(getFirstPartyTypes()).contains(partyTypeCode);
  }

  /**
   * Returns whether the party type is valid for the second party.
   *
   * @param partyTypeCode the code for the party type
   * @return <b>true</b> if the party type is valid for the second party or <b>false</b> otherwise
   */
  public boolean isValidSecondPartyType(String partyTypeCode) {
    if (!StringUtils.hasText(partyTypeCode)) {
      return false;
    }

    return Arrays.asList(getSecondPartyTypes()).contains(partyTypeCode);
  }

  /**
   * Set the code for the association type.
   *
   * @param code the code for the association type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the association type.
   *
   * @param description the description for the association type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the code for the role type for the first party in the association.
   *
   * @param firstPartyRole the code for the role type for the first party in the association
   */
  public void setFirstPartyRole(String firstPartyRole) {
    this.firstPartyRole = firstPartyRole;
  }

  /**
   * Set the codes for the party types for the first party in the association.
   *
   * @param firstPartyTypes the codes for the party types for the first party in the association
   */
  public void setFirstPartyTypes(String[] firstPartyTypes) {
    this.firstPartyTypes = StringUtils.arrayToCommaDelimitedString(firstPartyTypes);
  }

  /**
   * Set the Unicode locale identifier for the association type.
   *
   * @param localeId the Unicode locale identifier for the association type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the association type.
   *
   * @param name the name of the association type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the code for the role type for the second party in the association.
   *
   * @param secondPartyRole the code for the role type for the second party in the association
   */
  public void setSecondPartyRole(String secondPartyRole) {
    this.secondPartyRole = secondPartyRole;
  }

  /**
   * Set the codes for the party types for the second party in the association.
   *
   * @param secondPartyTypes the codes for the party types for the second party in the association
   */
  public void setSecondPartyTypes(String[] secondPartyTypes) {
    this.secondPartyTypes = StringUtils.arrayToCommaDelimitedString(secondPartyTypes);
  }

  /**
   * Set the sort index for the association type.
   *
   * @param sortIndex the sort index for the association type
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the ID for the tenant the association type is specific to.
   *
   * @param tenantId the ID for the tenant the association type is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}

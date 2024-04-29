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
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 * The <b>PhysicalAddressRole</b> class holds the information for a physical address role.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A physical address role")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "localeId",
  "tenantId",
  "sortIndex",
  "name",
  "description",
  "partyTypes"
})
@XmlRootElement(name = "PhysicalAddressRole", namespace = "https://inception.digital/party")
@XmlType(
    name = "PhysicalAddressRole",
    namespace = "https://inception.digital/party",
    propOrder = {"code", "localeId", "tenantId", "sortIndex", "name", "description", "partyTypes"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_physical_address_roles")
@IdClass(PhysicalAddressRoleId.class)
@SuppressWarnings("unused")
public class PhysicalAddressRole implements Serializable {

  /** The physical address role code for a business address. */
  public static final String BUSINESS = "business";

  /** The physical address role code for a home address. */
  public static final String HOME = "home";

  /** The physical address role code for a main address. */
  public static final String MAIN = "main";

  /** The physical address role code for a permanent address. */
  public static final String PERMANENT = "permanent";

  /** The physical address role code for a postal address. */
  public static final String POSTAL = "postal";

  /** The physical address role code for a registered office address. */
  public static final String REGISTERED_OFFICE = "registered_office";

  /** The physical address role code for a residential address. */
  public static final String RESIDENTIAL = "residential";

  /** The physical address role code for a service address. */
  public static final String SERVICE = "service";

  /** The physical address role code for a sole trader address. */
  public static final String SOLE_TRADER = "sole_trader";

  /** The physical address role code for a temporary address. */
  public static final String TEMPORARY = "temporary";

  /** The physical address role code for a work address. */
  public static final String WORK = "work";

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the physical address role. */
  @Schema(
      description = "The code for the physical address role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the physical address role. */
  @Schema(
      description = "The description for the physical address role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the physical address role. */
  @Schema(
      description = "The Unicode locale identifier for the physical address role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the physical address role. */
  @Schema(
      description = "The name of the physical address role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The comma-delimited codes for the party types the physical address role is associated with. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Size(min = 1, max = 310)
  @Column(name = "party_types", length = 310, nullable = false)
  private String partyTypes;

  /** The sort index for the physical address role. */
  @Schema(
      description = "The sort index for the physical address role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The ID for the tenant the physical address role is specific to. */
  @Schema(description = "The ID for the tenant the physical address role is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new <b>PhysicalAddressRole</b>. */
  public PhysicalAddressRole() {}

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

    PhysicalAddressRole other = (PhysicalAddressRole) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the physical address role.
   *
   * @return the code for the physical address role
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the physical address role.
   *
   * @return the description for the physical address role
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the physical address role.
   *
   * @return the Unicode locale identifier for the physical address role
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the physical address role.
   *
   * @return the name of the physical address role
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the codes for the party types the physical address role is associated with.
   *
   * @return the codes for the party types the physical address role is associated with
   */
  @Schema(
      description = "The codes for the party types the physical address role is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyTypes", required = true)
  public String[] getPartyTypes() {
    return StringUtils.removeDuplicateStrings(
        StringUtils.commaDelimitedListToStringArray(partyTypes));
  }

  /**
   * Returns the sort index for the physical address role.
   *
   * @return the sort index for the physical address role
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the ID for the tenant the physical address role is specific to.
   *
   * @return the ID for the tenant the physical address role is specific to
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
   * Returns whether the physical address role is valid for the party type.
   *
   * @param partyTypeCode the code for the party type
   * @return <b>true</b> if the physical address role is valid for the party type or <b>false</b>
   *     otherwise
   */
  public boolean isValidForPartyType(String partyTypeCode) {
    if (!StringUtils.hasText(partyTypeCode)) {
      return false;
    }

    return Arrays.asList(getPartyTypes()).contains(partyTypeCode);
  }

  /**
   * Set the code for the physical address role.
   *
   * @param code the code for the physical address role
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the physical address role.
   *
   * @param description the description for the physical address role
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the physical address role.
   *
   * @param localeId the Unicode locale identifier for the physical address role
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the physical address role.
   *
   * @param name the name of the physical address role
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the codes for the party types the physical address role is associated with.
   *
   * @param partyTypes the codes for the party types the physical address role is associated with
   */
  public void setPartyTypes(String[] partyTypes) {
    this.partyTypes = StringUtils.arrayToCommaDelimitedString(partyTypes);
  }

  /**
   * Set the codes for the party types the physical address role is associated with.
   *
   * @param partyTypes the codes for the party types the physical address role is associated with
   */
  @JsonIgnore
  public void setPartyTypes(Collection<String> partyTypes) {
    this.partyTypes = StringUtils.collectionToDelimitedString(partyTypes, ",");
  }

  /**
   * Set the sort index for the physical address role.
   *
   * @param sortIndex the sort index for the physical address role
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the ID for the tenant the physical address role is specific to.
   *
   * @param tenantId the ID for the tenant the physical address role is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}

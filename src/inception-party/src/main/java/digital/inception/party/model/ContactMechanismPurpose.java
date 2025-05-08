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
import digital.inception.jpa.StringListAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 * The {@code ContactMechanismPurpose} class holds the information for a contact mechanism purpose.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A contact mechanism purpose")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "localeId",
  "tenantId",
  "sortIndex",
  "name",
  "description",
  "contactMechanismTypes",
  "partyTypes"
})
@XmlRootElement(name = "ContactMechanismPurpose", namespace = "https://inception.digital/party")
@XmlType(
    name = "ContactMechanismPurpose",
    namespace = "https://inception.digital/party",
    propOrder = {
      "code",
      "localeId",
      "tenantId",
      "sortIndex",
      "name",
      "description",
      "contactMechanismTypes",
      "partyTypes"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_contact_mechanism_purposes")
@IdClass(ContactMechanismPurposeId.class)
@SuppressWarnings("unused")
public class ContactMechanismPurpose implements Serializable {

  /** The contact mechanism purpose code for general correspondence. */
  public static final String GENERAL = "general";

  /** The contact mechanism purpose code for marketing correspondence. */
  public static final String MARKETING = "marketing";

  /** The contact mechanism purpose code for security alerts. */
  public static final String SECURITY = "security";

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the contact mechanism purpose. */
  @Schema(
      description = "The code for the contact mechanism purpose",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The codes for the contact mechanism types the contact mechanism purpose is associated with. */
  @Schema(
      description =
          "The codes for the contact mechanism types the contact mechanism purpose is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "ContactMechanismTypes", required = true)
  @XmlElement(name = "ContactMechanismType", required = true)
  @NotNull
  @Size(min = 1, max = 10)
  @Convert(converter = StringListAttributeConverter.class)
  @Column(name = "contact_mechanism_types", length = 510, nullable = false)
  private List<String> contactMechanismTypes;

  /** The description for the contact mechanism purpose. */
  @Schema(
      description = "The description for the contact mechanism purpose",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the contact mechanism purpose. */
  @Schema(
      description = "The Unicode locale identifier for the contact mechanism purpose",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the contact mechanism purpose. */
  @Schema(
      description = "The name of the contact mechanism purpose",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The codes for the party types the contact mechanism purpose is associated with. */
  @Schema(
      description =
          "The codes for the party types the contact mechanism purpose is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "PartyTypes", required = true)
  @XmlElement(name = "PartyType", required = true)
  @NotNull
  @Size(min = 1, max = 10)
  @Convert(converter = StringListAttributeConverter.class)
  @Column(name = "party_types", length = 510, nullable = false)
  private List<String> partyTypes;

  /** The sort index for the contact mechanism purpose. */
  @Schema(
      description = "The sort index for the contact mechanism purpose",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The ID for the tenant the contact mechanism purpose is specific to. */
  @Schema(description = "The ID for the tenant the contact mechanism purpose is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Creates a new {@code ContactMechanismPurpose} instance. */
  public ContactMechanismPurpose() {}

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

    ContactMechanismPurpose other = (ContactMechanismPurpose) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the contact mechanism purpose.
   *
   * @return the code for the contact mechanism purpose
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the codes for the contact mechanism types the contact mechanism purpose is associated
   * with.
   *
   * @return the codes for the contact mechanism types the contact mechanism purpose is associated
   *     with
   */
  public List<String> getContactMechanismTypes() {
    return contactMechanismTypes;
  }

  /**
   * Returns the description for the contact mechanism purpose.
   *
   * @return the description for the contact mechanism purpose
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the contact mechanism purpose.
   *
   * @return the Unicode locale identifier for the contact mechanism purpose
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the contact mechanism purpose.
   *
   * @return the name of the contact mechanism purpose
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the codes for the party types the contact mechanism purpose is associated with.
   *
   * @return the codes for the party types the contact mechanism purpose is associated with
   */
  public List<String> getPartyTypes() {
    return partyTypes;
  }

  /**
   * Returns the sort index for the contact mechanism purpose.
   *
   * @return the sort index for the contact mechanism purpose
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the ID for the tenant the contact mechanism purpose is specific to.
   *
   * @return the ID for the tenant the contact mechanism purpose is specific to
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
   * Returns whether the contact mechanism purpose is valid for the contact mechanism type.
   *
   * @param contactMechanismTypeCode the code for the contact mechanism type
   * @return {@code true} if the contact mechanism purpose is valid for the contact mechanism, type
   *     or {@code false} otherwise
   */
  public boolean isValidForContactMechanismType(String contactMechanismTypeCode) {
    if (!StringUtils.hasText(contactMechanismTypeCode)) {
      return false;
    }

    return contactMechanismTypes.contains(contactMechanismTypeCode);
  }

  /**
   * Returns whether the contact mechanism purpose is valid for the party type.
   *
   * @param partyTypeCode the code for the party type
   * @return {@code true} if the contact mechanism purpose is valid for the party type or {@code
   *     false} otherwise
   */
  public boolean isValidForPartyType(String partyTypeCode) {
    if (!StringUtils.hasText(partyTypeCode)) {
      return false;
    }

    return partyTypes.contains(partyTypeCode);
  }

  /**
   * Set the code for the contact mechanism purpose.
   *
   * @param code the code for the contact mechanism purpose
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the codes for the contact mechanism types the contact mechanism purpose is associated with.
   *
   * @param contactMechanismTypes the codes for the contact mechanism types the contact mechanism
   *     purpose is associated with
   */
  public void setContactMechanismTypes(List<String> contactMechanismTypes) {
    this.contactMechanismTypes = contactMechanismTypes;
  }

  /**
   * Set the description for the contact mechanism purpose.
   *
   * @param description the description for the contact mechanism purpose
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the contact mechanism purpose.
   *
   * @param localeId the Unicode locale identifier for the contact mechanism purpose
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the contact mechanism purpose.
   *
   * @param name the name of the contact mechanism purpose
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the codes for the party types the contact mechanism purpose is associated with.
   *
   * @param partyTypes the codes for the party types the contact mechanism purpose is associated
   *     with
   */
  public void setPartyTypes(List<String> partyTypes) {
    this.partyTypes = partyTypes;
  }

  /**
   * Set the sort index for the contact mechanism purpose.
   *
   * @param sortIndex the sort index for the contact mechanism purpose
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the ID for the tenant the contact mechanism purpose is specific to.
   *
   * @param tenantId the ID for the tenant the contact mechanism purpose is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}

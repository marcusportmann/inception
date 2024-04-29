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
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/**
 * The <b>ContactMechanismRole</b> class holds the information for a contact mechanism role.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A contact mechanism role")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "contactMechanismType",
  "code",
  "localeId",
  "tenantId",
  "sortIndex",
  "name",
  "description",
  "partyTypes",
  "pattern"
})
@XmlRootElement(name = "ContactMechanismRole", namespace = "https://inception.digital/party")
@XmlType(
    name = "ContactMechanismRole",
    namespace = "https://inception.digital/party",
    propOrder = {
      "contactMechanismType",
      "code",
      "localeId",
      "tenantId",
      "sortIndex",
      "name",
      "description",
      "partyTypes",
      "pattern"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_contact_mechanism_roles")
@IdClass(ContactMechanismRoleId.class)
@SuppressWarnings("unused")
public class ContactMechanismRole implements Serializable {

  /** The contact mechanism role code for a home fax number. */
  public static final String HOME_FAX_NUMBER = "home_fax_number";

  /** The contact mechanism role code for a home phone number. */
  public static final String HOME_PHONE_NUMBER = "home_phone_number";

  /** The contact mechanism role code for a main e-mail address. */
  public static final String MAIN_EMAIL_ADDRESS = "main_email_address";

  /** The contact mechanism role code for a main fax number. */
  public static final String MAIN_FAX_NUMBER = "main_fax_number";

  /** The contact mechanism role code for a main mobile number. */
  public static final String MAIN_MOBILE_NUMBER = "main_mobile_number";

  /** The contact mechanism role code for a main phone number. */
  public static final String MAIN_PHONE_NUMBER = "main_phone_number";

  /** The contact mechanism role code for an other e-mail address. */
  public static final String OTHER_EMAIL_ADDRESS = "other_email_address";

  /** The contact mechanism role code for an other fax number. */
  public static final String OTHER_FAX_NUMBER = "other_fax_number";

  /** The contact mechanism role code for an other mobile number. */
  public static final String OTHER_MOBILE_NUMBER = "other_mobile_number";

  /** The contact mechanism role code for an other phone number. */
  public static final String OTHER_PHONE_NUMBER = "other_phone_number";

  /** The contact mechanism role code for a pager phone number. */
  public static final String PAGER_PHONE_NUMBER = "pager_phone_number";

  /** The contact mechanism role code for a personal e-mail address. */
  public static final String PERSONAL_EMAIL_ADDRESS = "personal_email_address";

  /** The contact mechanism role code for personal mobile number. */
  public static final String PERSONAL_MOBILE_NUMBER = "personal_mobile_number";

  /** The contact mechanism role code for a school e-mail address. */
  public static final String SCHOOL_EMAIL_ADDRESS = "school_email_address";

  /** The contact mechanism role code for a school phone number. */
  public static final String SCHOOL_PHONE_NUMBER = "school_phone_number";

  /** The contact mechanism role code for a Twitter ID. */
  public static final String TWITTER_ID = "twitter_id";

  /** The contact mechanism role code for a WhatsApp User ID. */
  public static final String WHATSAPP_USER_ID = "whatsapp_user_id";

  /** The contact mechanism role code for a work e-mail address. */
  public static final String WORK_EMAIL_ADDRESS = "work_email_address";

  /** The contact mechanism role code for a work fax number. */
  public static final String WORK_FAX_NUMBER = "work_fax_number";

  /** The contact mechanism role code for work mobile number. */
  public static final String WORK_MOBILE_NUMBER = "work_mobile_number";

  /** The contact mechanism role code for a work phone number. */
  public static final String WORK_PHONE_NUMBER = "work_phone_number";

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the contact mechanism role. */
  @Schema(
      description = "The code for the contact mechanism role",
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

  /** The code for the contact mechanism type the contact mechanism role is associated with. */
  @Schema(
      description =
          "The code for the contact mechanism type the contact mechanism role is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ContactMechanismType", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "contact_mechanism_type", length = 50, nullable = false)
  private String contactMechanismType;

  /** The description for the contact mechanism role. */
  @Schema(
      description = "The description for the contact mechanism role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the contact mechanism role. */
  @Schema(
      description = "The Unicode locale identifier for the contact mechanism role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the contact mechanism role. */
  @Schema(
      description = "The name of the contact mechanism role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /**
   * The comma-delimited codes for the party types the contact mechanism role is associated with.
   */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Size(min = 1, max = 310)
  @Column(name = "party_types", length = 310, nullable = false)
  private String partyTypes;

  /**
   * The regular expression pattern used to validate a contact mechanism value for the contact
   * mechanism role.
   */
  @Schema(
      description =
          "The regular expression pattern used to validate a contact mechanism value for the contact mechanism role")
  @JsonProperty
  @XmlElement(name = "Pattern")
  @Size(min = 1, max = 1000)
  @Column(name = "pattern", length = 1000)
  private String pattern;

  /** The sort index for the contact mechanism role. */
  @Schema(
      description = "The sort index for the contact mechanism role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The ID for the tenant the contact mechanism role is specific to. */
  @Schema(description = "The ID for the tenant the contact mechanism role is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new <b>ContactMechanismRole</b>. */
  public ContactMechanismRole() {}

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

    ContactMechanismRole other = (ContactMechanismRole) object;

    return Objects.equals(contactMechanismType, other.contactMechanismType)
        && Objects.equals(code, other.code)
        && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the contact mechanism role.
   *
   * @return the code for the contact mechanism role
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
   * Returns the code for the contact mechanism type the contact mechanism role is associated with.
   *
   * @return the code for the contact mechanism type the contact mechanism role is associated with
   */
  public String getContactMechanismType() {
    return contactMechanismType;
  }

  /**
   * Returns the description for the contact mechanism role.
   *
   * @return the description for the contact mechanism role
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the contact mechanism role.
   *
   * @return the Unicode locale identifier for the contact mechanism role
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the contact mechanism role.
   *
   * @return the name of the contact mechanism role
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the codes for the party types the contact mechanism role is associated with.
   *
   * @return the codes for the party types the contact mechanism role is associated with
   */
  @Schema(
      description = "The codes for the party types the contact mechanism role is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyTypes", required = true)
  public String[] getPartyTypes() {
    return StringUtils.removeDuplicateStrings(
        StringUtils.commaDelimitedListToStringArray(partyTypes));
  }

  /**
   * Returns the regular expression pattern used to validate a contact mechanism value for the
   * contact mechanism role.
   *
   * @return the regular expression pattern used to validate a contact mechanism value for the
   *     contact mechanism role
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * Returns the sort index for the contact mechanism role.
   *
   * @return the sort index for the contact mechanism role
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the ID for the tenant the contact mechanism role is specific to.
   *
   * @return the ID for the tenant the contact mechanism role is specific to
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
    return ((contactMechanismType == null) ? 0 : contactMechanismType.hashCode())
        + ((code == null) ? 0 : code.hashCode())
        + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Returns whether the contact mechanism role is valid for the party type.
   *
   * @param partyTypeCode the code for the party type
   * @return <b>true</b> if the contact mechanism role is valid for the party type or <b>false</b>
   *     otherwise
   */
  public boolean isValidForPartyType(String partyTypeCode) {
    if (!StringUtils.hasText(partyTypeCode)) {
      return false;
    }

    return Arrays.asList(getPartyTypes()).contains(partyTypeCode);
  }

  /**
   * Set the code for the contact mechanism role.
   *
   * @param code the code for the contact mechanism role
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the code for the contact mechanism type the contact mechanism role is associated with.
   *
   * @param type the code for the contact mechanism type the contact mechanism role is associated
   *     with
   */
  public void setContactMechanismType(String type) {
    this.contactMechanismType = type;
  }

  /**
   * Set the description for the contact mechanism role.
   *
   * @param description the description for the contact mechanism role
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the contact mechanism role.
   *
   * @param localeId the Unicode locale identifier for the contact mechanism role
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the contact mechanism role.
   *
   * @param name the name of the contact mechanism role
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the codes for the party types the contact mechanism role is associated with.
   *
   * @param partyTypes the codes for the party types the contact mechanism role is associated with
   */
  public void setPartyTypes(String[] partyTypes) {
    this.partyTypes = StringUtils.arrayToCommaDelimitedString(partyTypes);
  }

  /**
   * Set the codes for the party types the contact mechanism role is associated with.
   *
   * @param partyTypes the codes for the party types the contact mechanism role is associated with
   */
  @JsonIgnore
  public void setPartyTypes(Collection<String> partyTypes) {
    this.partyTypes = StringUtils.collectionToDelimitedString(partyTypes, ",");
  }

  /**
   * Set the regular expression pattern used to validate a contact mechanism value for the contact
   * mechanism role.
   *
   * @param pattern the regular expression pattern used to validate a contact mechanism value for
   *     the contact mechanism role
   */
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  /**
   * Set the sort index for the contact mechanism role.
   *
   * @param sortIndex the sort index for the contact mechanism role
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the ID for the tenant the contact mechanism role is specific to.
   *
   * @param tenantId the ID for the tenant the contact mechanism role is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}

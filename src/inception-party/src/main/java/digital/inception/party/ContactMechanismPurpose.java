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
 * The <b>ContactMechanismPurpose</b> class holds the information for a possible contact mechanism
 * purpose.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A contact mechanism purpose")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "code", "localeId", "sortIndex", "name", "description", "partyTypes"})
@XmlRootElement(name = "ContactMechanismPurpose", namespace = "http://inception.digital/party")
@XmlType(
    name = "ContactMechanismPurpose",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "code", "localeId", "sortIndex", "name", "description", "partyTypes"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "contact_mechanism_purposes")
@IdClass(ContactMechanismPurposeId.class)
public class ContactMechanismPurpose implements Serializable {

  /** The contact mechanism purpose code for a home fax number. */
  public static final String HOME_FAX_NUMBER = "home_fax_number";

  /** The contact mechanism purpose code for a home phone number. */
  public static final String HOME_PHONE_NUMBER = "home_phone_number";

  /** The contact mechanism purpose code for a main e-mail address. */
  public static final String MAIN_EMAIL_ADDRESS = "main_email_address";

  /** The contact mechanism purpose code for a main fax number. */
  public static final String MAIN_FAX_NUMBER = "main_fax_number";

  /** The contact mechanism purpose code for a main mobile number. */
  public static final String MAIN_MOBILE_NUMBER = "main_mobile_number";

  /** The contact mechanism purpose code for a main phone number. */
  public static final String MAIN_PHONE_NUMBER = "main_phone_number";

  /** The contact mechanism purpose code for an other e-mail address. */
  public static final String OTHER_EMAIL_ADDRESS = "other_email_address";

  /** The contact mechanism purpose code for a other fax number. */
  public static final String OTHER_FAX_NUMBER = "other_fax_number";

  /** The contact mechanism purpose code for an other mobile number. */
  public static final String OTHER_MOBILE_NUMBER = "other_mobile_number";

  /** The contact mechanism purpose code for an other phone number. */
  public static final String OTHER_PHONE_NUMBER = "other_phone_number";

  /** The contact mechanism purpose code for a pager phone number. */
  public static final String PAGER_PHONE_NUMBER = "pager_phone_number";

  /** The contact mechanism purpose code for a personal e-mail address. */
  public static final String PERSONAL_EMAIL_ADDRESS = "personal_email_address";

  /** The contact mechanism purpose code for personal mobile number. */
  public static final String PERSONAL_MOBILE_NUMBER = "personal_mobile_number";

  /** The contact mechanism purpose code for a school e-mail address. */
  public static final String SCHOOL_EMAIL_ADDRESS = "school_email_address";

  /** The contact mechanism purpose code for a school phone number. */
  public static final String SCHOOL_PHONE_NUMBER = "school_phone_number";

  /** The contact mechanism purpose code for a Twitter ID. */
  public static final String TWITTER_ID = "twitter_id";

  /** The contact mechanism purpose code for a WhatsApp User ID. */
  public static final String WHATSAPP_USER_ID = "whatsapp_user_id";

  /** The contact mechanism purpose code for a work e-mail address. */
  public static final String WORK_EMAIL_ADDRESS = "work_email_address";

  /** The contact mechanism purpose code for a work fax number. */
  public static final String WORK_FAX_NUMBER = "work_fax_number";

  /** The contact mechanism purpose code for work mobile number. */
  public static final String WORK_MOBILE_NUMBER = "work_mobile_number";

  /** The contact mechanism purpose code for a work phone number. */
  public static final String WORK_PHONE_NUMBER = "work_phone_number";

  private static final long serialVersionUID = 1000000;

  /** The code for the contact mechanism purpose. */
  @Schema(description = "The code for the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "code", length = 30, nullable = false)
  private String code;

  /** The description for the contact mechanism purpose. */
  @Schema(description = "The description for the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the contact mechanism purpose. */
  @Schema(
      description = "The Unicode locale identifier for the contact mechanism purpose",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the contact mechanism purpose. */
  @Schema(description = "The name of the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /**
   * The comma-delimited codes for the party types the contact mechanism purpose is associated with.
   */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Size(min = 1, max = 300)
  @Column(name = "party_types", length = 300, nullable = false)
  private String partyTypes;

  /** The sort index for the contact mechanism purpose. */
  @Schema(description = "The sort index for the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The code for the contact mechanism type the contact mechanism purpose is associated with. */
  @Schema(
      description =
          "The code for the contact mechanism type the contact mechanism purpose is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** Constructs a new <b>ContactMechanismPurpose</b>. */
  public ContactMechanismPurpose() {}

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

    ContactMechanismPurpose other = (ContactMechanismPurpose) object;

    return Objects.equals(type, other.type)
        && Objects.equals(code, other.code)
        && Objects.equals(localeId, other.localeId);
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
  @Schema(
      description =
          "The codes for the party types the contact mechanism purpose is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyTypes", required = true)
  public String[] getPartyTypes() {
    return StringUtils.commaDelimitedListToStringArray(partyTypes);
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
   * Returns the code for the contact mechanism type the contact mechanism purpose is associated
   * with.
   *
   * @return the code for the contact mechanism type the contact mechanism purpose is associated
   *     with
   */
  public String getType() {
    return type;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((type == null) ? 0 : type.hashCode())
        + ((code == null) ? 0 : code.hashCode())
        + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Returns whether the contact mechanism purpose is valid for the party type.
   *
   * @param partyTypeCode the party type code
   * @return <b>true</b> if the contact mechanism purpose is valid for the party type or
   *     <b>false</b> otherwise
   */
  public boolean isValidForPartyType(String partyTypeCode) {
    return Arrays.stream(getPartyTypes())
        .anyMatch(validPartyType -> validPartyType.equals(partyTypeCode));
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
  public void setPartyTypes(String[] partyTypes) {
    this.partyTypes = StringUtils.arrayToCommaDelimitedString(partyTypes);
  }

  /**
   * Set the codes for the party types the contact mechanism purpose is associated with.
   *
   * @param partyTypes the codes for the party types the contact mechanism purpose is associated
   *     with
   */
  @JsonIgnore
  public void setPartyTypes(Collection<String> partyTypes) {
    this.partyTypes = StringUtils.collectionToDelimitedString(partyTypes, ",");
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
   * Set the code for the contact mechanism type the contact mechanism purpose is associated with.
   *
   * @param type the code for the contact mechanism type the contact mechanism purpose is associated
   *     with
   */
  public void setType(String type) {
    this.type = type;
  }
}

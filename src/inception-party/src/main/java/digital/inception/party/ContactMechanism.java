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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

/**
 * The <b>ContactMechanism</b> class holds the information for a contact mechanism for a party.
 *
 * <p>The JPA 2.2 spec (10.6) does not support attribute converters for attributes annotated
 * with @Id. If Enum types are used for these attributes then the ordinal value is always used. As a
 * result, the type and purpose attributes for this class are Strings and the Getter and Setters
 * (a.k.a. Accessors and Mutators) convert to and from the Enum types. A consequence of this is that
 * the attributes are marked as @JsonIgnore and @XmlTransient and the Getters are annotated with
 *
 * @author Marcus Portmann @JsonProperty and @XmlElement.
 */
@Schema(description = "A contact mechanism")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "role", "value", "purposes"})
@XmlRootElement(name = "ContactMechanism", namespace = "http://inception.digital/party")
@XmlType(
    name = "ContactMechanism",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "role", "value", "purposes"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "contact_mechanisms")
@IdClass(ContactMechanismId.class)
public class ContactMechanism implements Serializable {

  /** The valid contact mechanism types. */
  @JsonIgnore
  @XmlTransient
  public static final Set<String> VALID_CONTACT_MECHANISM_TYPES =
      Set.of("mobile_number", "phone_number", "fax_number", "email_address", "social_media");

  private static final long serialVersionUID = 1000000;

  /** The date and time the contact mechanism was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The party the contact mechanism is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("contactMechanismReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The optional comma-delimited codes for the contact mechanism purposes. */
  @JsonIgnore
  @XmlTransient
  @Column(name = "purposes", length = 310)
  private String purposes;

  /** The code for the contact mechanism role. */
  @Schema(
      description = "The code for the contact mechanism role",
      required = true,
      allowableValues = {
        "personal_mobile_number",
        "work_mobile_number",
        "other_mobile_number",
        "main_mobile_number",
        "home_phone_number",
        "work_phone_number",
        "school_phone_number",
        "pager_phone_number",
        "other_phone_number",
        "main_phone_number",
        "home_fax_number",
        "work_fax_number",
        "other_fax_number",
        "main_fax_number",
        "personal_email_address",
        "work_email_address",
        "school_email_address",
        "other_email_address",
        "main_email_address",
        "whatsapp_user_id",
        "twitter_id"
      })
  @JsonProperty(required = true)
  @XmlElement(name = "Role", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "role", length = 30, nullable = false)
  private String role;

  /** The code for the contact mechanism type. */
  @Schema(
      description = "The code for the contact mechanism type",
      required = true,
      allowableValues = {
        "mobile_number",
        "phone_number",
        "fax_number",
        "email_address",
        "social_media"
      })
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Pattern(
      message = "{digital.inception.party.ContactMechanism.Type.Pattern.message}",
      regexp = "(mobile_number|phone_number|fax_number|email_address|social_media)")
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the contact mechanism was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** The value for the contact mechanism. */
  @Schema(description = "The value for the contact mechanism", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "value", length = 200, nullable = false)
  private String value;

  /** Constructs a new <b>ContactMechanism</b>. */
  public ContactMechanism() {}

  /**
   * Constructs a new <b>ContactMechanism</b>.
   *
   * @param type the code for the contact mechanism type
   * @param role the code for the contact mechanism role
   * @param value the value for the contact mechanism
   */
  public ContactMechanism(String type, String role, String value) {
    this.type = type;
    this.role = role;
    this.value = value;
  }

  /**
   * Constructs a new <b>ContactMechanism</b>.
   *
   * @param type the code for the contact mechanism type
   * @param role the code for the contact mechanism role
   * @param value the value for the contact mechanism
   * @param purpose the code for the contact mechanism purpose
   */
  public ContactMechanism(String type, String role, String value, String purpose) {
    this.type = type;
    this.role = role;
    this.value = value;
    this.purposes = purpose;
  }

  /**
   * Constructs a new <b>ContactMechanism</b>.
   *
   * @param type the code for the contact mechanism type
   * @param role the code for the contact mechanism role
   * @param value the value for the contact mechanism
   * @param purposes the codes for the contact mechanism purposes
   */
  public ContactMechanism(String type, String role, String value, Set<String> purposes) {
    this.type = type;
    this.role = role;
    this.value = value;

    if ((purposes != null) && (purposes.size() > 0)) {
      this.purposes = StringUtils.collectionToCommaDelimitedString(purposes);
    }
  }

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

    ContactMechanism other = (ContactMechanism) object;

    return Objects.equals(party, other.party)
        && Objects.equals(type, other.type)
        && Objects.equals(role, other.role);
  }

  /**
   * Returns the date and time the contact mechanism was created.
   *
   * @return the date and time the contact mechanism was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the party the contact mechanism is associated with.
   *
   * @return the party the contact mechanism is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the optional codes for the contact mechanism purposes.
   *
   * @return the optional codes for the contact mechanism purposes
   */
  @Schema(description = "The codes for the contact mechanism purposes", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Purposes", required = true)
  @XmlElement(name = "Purpose", required = true)
  public Set<String> getPurposes() {
    if (this.purposes != null) {
      return StringUtils.commaDelimitedListToSet(this.purposes);
    } else {
      return new LinkedHashSet<>();
    }
  }

  /**
   * Returns the code for the contact mechanism role.
   *
   * @return the code for the contact mechanism role
   */
  public String getRole() {
    return role;
  }

  /**
   * Returns the code for the contact mechanism type.
   *
   * @return the code for the contact mechanism type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the contact mechanism was last updated.
   *
   * @return the date and time the contact mechanism was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the value for the contact mechanism.
   *
   * @return the value for the contact mechanism
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((party == null) || (party.getId() == null)) ? 0 : party.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode())
        + ((role == null) ? 0 : role.hashCode());
  }

  /**
   * Set the party the contact mechanism is associated with.
   *
   * @param party the party the contact mechanism is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the optional codes for the contact mechanism purposes.
   *
   * @param purposes the optional codes for the contact mechanism purposes
   */
  public void setPurposes(Set<String> purposes) {
    if ((purposes != null) && (purposes.size() > 0)) {
      this.purposes = StringUtils.collectionToCommaDelimitedString(purposes);
    } else {
      this.purposes = null;
    }
  }

  /**
   * Set the code for the contact mechanism role.
   *
   * @param purpose the code for the contact mechanism role
   */
  public void setRole(String purpose) {
    this.role = purpose;
  }

  /**
   * Set the code for the contact mechanism type.
   *
   * @param type the code for the contact mechanism type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Set the value for the contact mechanism.
   *
   * @param value the value for the contact mechanism
   */
  public void setValue(String value) {
    this.value = value;
  }
}

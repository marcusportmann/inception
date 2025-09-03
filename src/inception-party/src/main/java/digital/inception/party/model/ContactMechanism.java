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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code ContactMechanism} class holds the information for a contact mechanism for an
 * organization or person.
 *
 * <p>The JPA 2.2 spec (10.6) does not support attribute converter for attributes annotated
 * with @Id. If Enum types are used for these attributes then the ordinal value is always used. As a
 * result, the type and purpose attributes for this class are Strings and the Getter and Setters
 * (a.k.a. Accessors and Mutators) convert to and from the Enum types. A consequence of this is that
 * the attributes are marked as @JsonIgnore and @XmlTransient and the Getters are annotated with
 *
 * @author Marcus Portmann @JsonProperty and @XmlElement.
 */
@Schema(description = "A contact mechanism for an organization or person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "role", "value", "purposes"})
@XmlRootElement(name = "ContactMechanism", namespace = "https://inception.digital/party")
@XmlType(
    name = "ContactMechanism",
    namespace = "https://inception.digital/party",
    propOrder = {"type", "role", "value", "purposes"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_contact_mechanisms")
@IdClass(ContactMechanismId.class)
public class ContactMechanism implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the party the contact mechanism is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /** The codes for the contact mechanism purposes. */
  @Schema(description = "The codes for the contact mechanism purposes")
  @JsonProperty
  @XmlElementWrapper(name = "Purposes")
  @XmlElement(name = "Purpose")
  @Size(min = 1, max = 10)
  @Convert(converter = StringListAttributeConverter.class)
  @Column(name = "purposes", length = 510)
  private List<String> purposes;

  /** The code for the contact mechanism role. */
  @Schema(
      description = "The code for the contact mechanism role",
      requiredMode = Schema.RequiredMode.REQUIRED,
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
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "role", length = 50, nullable = false)
  private String role;

  /** The code for the contact mechanism type. */
  @Schema(
      description = "The code for the contact mechanism type",
      requiredMode = Schema.RequiredMode.REQUIRED,
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
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.ContactMechanism.Type.Pattern.message}",
      regexp = "(mobile_number|phone_number|fax_number|email_address|social_media)")
  @Id
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** The value for the contact mechanism. */
  @Schema(
      description = "The value for the contact mechanism",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "value", length = 200, nullable = false)
  private String value;

  /** Constructs a new {@code ContactMechanism}. */
  public ContactMechanism() {}

  /**
   * Constructs a new {@code ContactMechanism}.
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
   * Constructs a new {@code ContactMechanism}.
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
    this.purposes = new ArrayList<>(List.of(purpose));
  }

  /**
   * Constructs a new {@code ContactMechanism}.
   *
   * @param type the code for the contact mechanism type
   * @param role the code for the contact mechanism role
   * @param value the value for the contact mechanism
   * @param purposes the codes for the contact mechanism purposes
   */
  public ContactMechanism(String type, String role, String value, List<String> purposes) {
    this.type = type;
    this.role = role;
    this.value = value;
    this.purposes = purposes;
  }

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

    ContactMechanism other = (ContactMechanism) object;

    return Objects.equals(partyId, other.partyId)
        && Objects.equals(type, other.type)
        && Objects.equals(role, other.role);
  }

  /**
   * Returns the codes for the contact mechanism purposes.
   *
   * @return the codes for the contact mechanism purposes
   */
  public List<String> getPurposes() {
    return purposes;
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
   * Returns the value for the contact mechanism.
   *
   * @return the value for the contact mechanism
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns whether the contact mechanism has the specified purpose.
   *
   * @param purpose the code for the contact mechanism purpose
   * @return {@code true} if the contact mechanism has the specified contact mechanism purpose or
   *     {@code false} otherwise
   */
  @JsonIgnore
  public boolean hasPurpose(String purpose) {
    if (purposes != null) {
      return purposes.contains(purpose);
    } else {
      return false;
    }
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((partyId == null) ? 0 : partyId.hashCode())
        + ((type == null) ? 0 : type.hashCode())
        + ((role == null) ? 0 : role.hashCode());
  }

  /**
   * Set the party the contact mechanism is associated with.
   *
   * @param party the party the contact mechanism is associated with
   */
  @JsonBackReference("contactMechanismReference")
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    if (party != null) {
      partyId = party.getId();
    } else {
      partyId = null;
    }
  }

  /**
   * Set the codes for the contact mechanism purposes.
   *
   * @param purposes the codes for the contact mechanism purposes
   */
  public void setPurposes(List<String> purposes) {
    this.purposes = purposes;
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

  /**
   * Called by the JAXB runtime an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof PartyBase parent) {
      setParty(parent);
    }
  }
}

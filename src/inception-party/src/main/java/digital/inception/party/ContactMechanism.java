/*
 * Copyright 2020 Marcus Portmann
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
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <code>ContactMechanism</code> class holds the information for a contact mechanism.
 *
 * <p><b>NOTE:</b> The JPA 2.2 spec (10.6) does not support attribute converters for attributes
 * annotated with @Id. If Enum types are used for these attributes then the ordinal value is always
 * used. As a result, the type and purpose attributes for this class are Integers and the Getter and
 * Setters (a.k.a. Accessors and Mutators) convert to and from the Enum types. A consequence of this
 * is that the attributes are marked as @JsonIgnore and @XmlTransient and the Getters are annotated
 * with @JsonProperty and @XmlElement.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A mechanism that can be used to contact a party")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "purpose", "value"})
@XmlRootElement(name = "ContactMechanism", namespace = "http://party.inception.digital")
@XmlType(
    name = "ContactMechanism",
    namespace = "http://party.inception.digital",
    propOrder = {"type", "purpose", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "contact_mechanisms")
@IdClass(ContactMechanismId.class)
public class ContactMechanism implements Serializable {

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
  private Party party;

  /** The contact mechanism purpose. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Id
  @Column(name = "purpose", nullable = false)
  private Integer purpose;

  /** The contact mechanism type. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Id
  @Column(name = "type", nullable = false)
  private Integer type;

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
  @Column(name = "value", nullable = false, length = 200)
  private String value;

  /** Constructs a new <code>ContactMechanism</code>. */
  public ContactMechanism() {}

  /**
   * Constructs a new <code>ContactMechanism</code>.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   * @param value the value for the contact mechanism
   */
  public ContactMechanism(
      ContactMechanismType type, ContactMechanismPurpose purpose, String value) {
    this.type = ContactMechanismType.toNumericCode(type);
    this.purpose = ContactMechanismPurpose.toNumericCode(purpose);
    this.value = value;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
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
        && Objects.equals(purpose, other.purpose);
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
  public Party getParty() {
    return party;
  }

  /**
   * Returns the contact mechanism purpose.
   *
   * @return the contact mechanism purpose
   */
  @Schema(description = "The contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Purpose", required = true)
  public ContactMechanismPurpose getPurpose() {
    return ContactMechanismPurpose.fromNumericCode(purpose);
  }

  /**
   * Returns the contact mechanism type.
   *
   * @return the contact mechanism type
   */
  @Schema(description = "The contact mechanism type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  public ContactMechanismType getType() {
    return ContactMechanismType.fromNumericCode(type);
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
        + ((purpose == null) ? 0 : purpose.hashCode());
  }

  /**
   * Set the party the contact mechanism is associated with.
   *
   * @param party the party the contact mechanism is associated with
   */
  @Schema(hidden = true)
  public void setParty(Party party) {
    this.party = party;
  }

  /**
   * Set the contact mechanism purpose.
   *
   * @param purpose the contact mechanism purpose
   */
  public void setPurpose(ContactMechanismPurpose purpose) {
    this.purpose = ContactMechanismPurpose.toNumericCode(purpose);
  }

  /**
   * Set the contact mechanism type.
   *
   * @param type the contact mechanism type
   */
  public void setType(ContactMechanismType type) {
    this.type = ContactMechanismType.toNumericCode(type);
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

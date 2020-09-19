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
  @JsonBackReference
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private Party party;

  /** The contact mechanism purpose. */
  @Schema(description = "The contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Purpose", required = true)
  @NotNull
  @Id
  @Column(name = "purpose", nullable = false)
  private ContactMechanismPurpose purpose;

  /** The contact mechanism type. */
  @Schema(description = "The contact mechanism type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Id
  @Column(name = "type", nullable = false)
  private ContactMechanismType type;

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
    this.type = type;
    this.purpose = purpose;
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
  public ContactMechanismPurpose getPurpose() {
    return purpose;
  }

  /**
   * Returns the contact mechanism type.
   *
   * @return the contact mechanism type
   */
  public ContactMechanismType getType() {
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
    this.purpose = purpose;
  }

  /**
   * Set the contact mechanism type.
   *
   * @param type the contact mechanism type
   */
  public void setType(ContactMechanismType type) {
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

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <code>Party</code> class holds the information for a party.
 *
 * <p>See: https://spec.edmcouncil.org/fibo/ontology/FND/Parties/Parties/IndependentParty
 *
 * @author Marcus Portmann
 */
@Schema(description = "A person or organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "name", "contactMechanisms", "physicalAddresses"})
@XmlRootElement(name = "Party", namespace = "http://party.inception.digital")
@XmlType(
    name = "Party",
    namespace = "http://party.inception.digital",
    propOrder = {"id", "type", "name", "contactMechanisms", "physicalAddresses"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("0")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@Table(schema = "party", name = "parties")
public class Party implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The contact mechanisms for the party. */
  @Schema(description = "The contact mechanisms for the party")
  @JsonProperty
  @JsonManagedReference
  @XmlElementWrapper(name = "ContactMechanisms")
  @XmlElement(name = "ContactMechanism")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(
      name = "party_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false,
      nullable = false)
  private final Set<ContactMechanism> contactMechanisms = new HashSet<>();

  /** The physical addresses for the party. */
  @Schema(description = "The physical addresses for the party")
  @JsonProperty
  @JsonManagedReference
  @XmlElementWrapper(name = "PhysicalAddresses")
  @XmlElement(name = "PhysicalAddress")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(
      name = "party_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false,
      nullable = false)
  private final Set<PhysicalAddress> physicalAddresses = new HashSet<>();

  /** The date and time the party was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  protected LocalDateTime created;

  /** The date and time the party was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  protected LocalDateTime updated;

  /** The Universally Unique Identifier (UUID) uniquely identifying the party. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) uniquely identifying the party",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name of the party. */
  @Schema(description = "The name of the party", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** The type of party. */
  @Schema(description = "The type of party", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", nullable = false, insertable = false, updatable = false)
  private PartyType type;

  /** Constructs a new <code>Party</code>. */
  public Party() {}

  /**
   * Constructs a new <code>Party</code>.
   *
   * @param type the type of party
   */
  public Party(PartyType type) {
    this.type = type;
  }

  /**
   * Add the contact mechanism for the party.
   *
   * @param contactMechanism the contact mechanism
   */
  public void addContactMechanism(ContactMechanism contactMechanism) {
    contactMechanism.setParty(this);

    contactMechanisms.add(contactMechanism);
  }

  /**
   * Add the physical for the party.
   *
   * @param physicalAddress the physical address
   */
  public void addPhysicalAddress(PhysicalAddress physicalAddress) {
    physicalAddress.setParty(this);

    physicalAddresses.add(physicalAddress);
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

    Party other = (Party) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the party.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   * @return the contact mechanism with the specified type and purpose for the party or <code>null
   *     </code> if the contact mechanism could not be found
   */
  public ContactMechanism getContactMechanism(
      ContactMechanismType type, ContactMechanismPurpose purpose) {
    return contactMechanisms.stream()
        .filter(
            contactMechanism ->
                Objects.equals(contactMechanism.getType(), type)
                    && Objects.equals(contactMechanism.getPurpose(), purpose))
        .findFirst()
        .get();
  }

  /**
   * Returns the contact mechanisms for the party.
   *
   * @return the contact mechanisms for the party
   */
  public Set<ContactMechanism> getContactMechanisms() {
    return contactMechanisms;
  }

  /**
   * Returns the date and time the party was created.
   *
   * @return the date and time the party was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the party.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the party
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the party.
   *
   * @return the name of the party
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieve the physical address with the specified type and purpose for the party.
   *
   * @param type the physical address type
   * @param purpose the physical address purpose
   * @return the physical address with the specified type and purpose for the party or <code>null
   *     </code> if the physical address could not be found
   */
  public PhysicalAddress getPhysicalAddress(
      PhysicalAddressType type, PhysicalAddressPurpose purpose) {
    return physicalAddresses.stream()
        .filter(
            physicalAddress ->
                Objects.equals(physicalAddress.getType(), type)
                    && Objects.equals(physicalAddress.getPurpose(), purpose))
        .findFirst()
        .get();
  }

  /**
   * Returns the physical addresses for the party.
   *
   * @return the physical addresses for the party
   */
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return physicalAddresses;
  }

  /**
   * Returns the type of party.
   *
   * @return the type of party
   */
  public PartyType getType() {
    return type;
  }

  /**
   * Returns the date and time the party was last updated.
   *
   * @return the date and time the party was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Remove the contact mechanism with the specified type and purpose for the party.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   */
  public void removeContactMechanism(ContactMechanismType type, ContactMechanismPurpose purpose) {
    contactMechanisms.removeIf(
        contactMechanism ->
            Objects.equals(contactMechanism.getType(), type)
                && Objects.equals(contactMechanism.getPurpose(), purpose));
  }

  /**
   * Remove the physical address with the specified type and purpose for the party.
   *
   * @param type the physical address type
   * @param purpose the physical address purpose
   */
  public void removePhysicalAddress(PhysicalAddressType type, PhysicalAddressPurpose purpose) {
    physicalAddresses.removeIf(
        physicalAddress ->
            Objects.equals(physicalAddress.getType(), type)
                && Objects.equals(physicalAddress.getPurpose(), purpose));
  }

  /**
   * Set the contact mechanisms for the party.
   *
   * @param contactMechanisms the contact mechanisms for the party
   */
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    this.contactMechanisms.clear();
    this.contactMechanisms.addAll(contactMechanisms);
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the party.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the party
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the party.
   *
   * @param name the name of the party
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the physical addresses for the party.
   *
   * @param physicalAddresses the physical addresses for the party
   */
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    this.physicalAddresses.clear();
    this.physicalAddresses.addAll(physicalAddresses);
  }

  /**
   * Set the type of party.
   *
   * @param type the type of party
   */
  public void setType(PartyType type) {
    this.type = type;
  }
}

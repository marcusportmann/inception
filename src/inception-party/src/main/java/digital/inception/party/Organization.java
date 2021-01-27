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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.party.constraints.ValidOrganization;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.springframework.util.StringUtils;

/**
 * The <code>Organization</code> class holds the information for an organization, which is an
 * organised group of people with a particular purpose, such as a business or government department.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "An organised group of people with a particular purpose, such as a business or government department")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"type"})
@JsonPropertyOrder({
  "id",
  "tenantId",
  "name",
  "contactMechanisms",
  "physicalAddresses",
  "countriesOfTaxResidence",
  "taxNumbers",
  "roles"
})
@XmlRootElement(name = "Organization", namespace = "http://party.inception.digital")
@XmlType(
    name = "Organization",
    namespace = "http://party.inception.digital",
    propOrder = {
      "contactMechanisms",
      "physicalAddresses",
      "countriesOfTaxResidence",
      "taxNumbers",
      "roles"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidOrganization
@Entity
// @DiscriminatorValue("organization")
@Table(schema = "party", name = "organizations")
public class Organization extends Party implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The contact mechanisms for the organization. */
  @Schema(description = "The contact mechanisms for the organization")
  @JsonProperty
  @JsonManagedReference("contactMechanismReference")
  @XmlElementWrapper(name = "ContactMechanisms")
  @XmlElement(name = "ContactMechanism")
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<ContactMechanism> contactMechanisms = new HashSet<>();

  /** The physical addresses for the organization. */
  @Schema(description = "The physical addresses for the organization")
  @JsonProperty
  @JsonManagedReference("physicalAddressReference")
  @XmlElementWrapper(name = "PhysicalAddresses")
  @XmlElement(name = "PhysicalAddress")
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PhysicalAddress> physicalAddresses = new HashSet<>();

  /** The roles for the organization independent of a party association. */
  @Schema(description = "The roles for the organization independent of a party association")
  @JsonProperty
  @JsonManagedReference("partyRoleReference")
  @XmlElementWrapper(name = "Roles")
  @XmlElement(name = "Role")
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PartyRole> roles = new HashSet<>();

  /** The tax numbers for the organization. */
  @Schema(description = "The tax numbers for the organization")
  @JsonProperty
  @JsonManagedReference("taxNumberReference")
  @XmlElementWrapper(name = "TaxNumbers")
  @XmlElement(name = "TaxNumber")
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<TaxNumber> taxNumbers = new HashSet<>();

  /** The optional comma-delimited codes for the countries of tax residence for the organization. */
  @JsonIgnore
  @XmlTransient
  @Size(min = 1, max = 100)
  @Column(table = "organizations", name = "countries_of_tax_residence", length = 100)
  private String countriesOfTaxResidence;

  /** Constructs a new <code>Organization</code>. */
  public Organization() {
    super(PartyType.ORGANIZATION);
  }

  /**
   * Constructs a new <code>Organization</code>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the organization is
   *     associated with
   * @param name the name of the organization
   */
  public Organization(UUID tenantId, String name) {
    super(tenantId, PartyType.ORGANIZATION, name);
  }

  // TODO: Add identity documents -- MARCUS

  /**
   * Add the contact mechanism for the organization.
   *
   * @param contactMechanism the contact mechanism
   */
  public void addContactMechanism(ContactMechanism contactMechanism) {
    contactMechanisms.removeIf(
        existingContactMechanism ->
            Objects.equals(existingContactMechanism.getType(), contactMechanism.getType())
                && Objects.equals(
                    existingContactMechanism.getPurpose(), contactMechanism.getPurpose()));

    contactMechanism.setParty(this);

    contactMechanisms.add(contactMechanism);
  }

  /**
   * Add the physical address for the organization.
   *
   * @param physicalAddress the physical address
   */
  public void addPhysicalAddress(PhysicalAddress physicalAddress) {
    physicalAddresses.removeIf(
        existingPhysicalAddress ->
            Objects.equals(existingPhysicalAddress.getId(), physicalAddress.getId()));

    physicalAddress.setParty(this);

    physicalAddresses.add(physicalAddress);
  }

  /**
   * Add the role to the organization independent of a party association.
   *
   * @param role the party role
   */
  public void addRole(PartyRole role) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), role.getType()));

    // role.setParty(this);

    roles.add(role);
  }

  /**
   * Add the tax number for the organization.
   *
   * @param taxNumber the tax number
   */
  public void addTaxNumber(TaxNumber taxNumber) {
    taxNumbers.removeIf(
        existingTaxNumber -> Objects.equals(existingTaxNumber.getType(), taxNumber.getType()));

    taxNumber.setParty(this);

    taxNumbers.add(taxNumber);
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

    Organization other = (Organization) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the organization.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   * @return the contact mechanism with the specified type and purpose for the organization or
   *     <code>null
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
   * Returns the contact mechanisms for the organization.
   *
   * @return the contact mechanisms for the organization
   */
  public Set<ContactMechanism> getContactMechanisms() {
    return contactMechanisms;
  }

  /**
   * Returns the optional codes for the countries of tax residence for the organization.
   *
   * @return the optional codes for the countries of tax residence for the organization
   */
  @Schema(
      description = "The optional codes for the countries of tax residence for the organization")
  @JsonProperty
  @XmlElement(name = "CountriesOfTaxResidence")
  public Set<String> getCountriesOfTaxResidence() {
    return Set.of(StringUtils.commaDelimitedListToStringArray(countriesOfTaxResidence));
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the organization.
   *
   * @return the Universally Unique Identifier (UUID) for the organization
   */
  @Schema(description = "The Universally Unique Identifier (UUID) for the organization")
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the name of the organization.
   *
   * @return the name of the organization
   */
  @Schema(description = "The name of the organization")
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Retrieve the first physical address with the specified purpose for the organization.
   *
   * @param purpose the physical address purpose
   * @return the first physical address with the specified purpose for the organization or <code>
   *     null</code> if the physical address could not be found
   */
  public PhysicalAddress getPhysicalAddress(PhysicalAddressPurpose purpose) {
    return physicalAddresses.stream()
        .filter(physicalAddress -> physicalAddress.getPurposes().contains(purpose))
        .findFirst()
        .get();
  }

  /**
   * Returns the physical addresses for the organization.
   *
   * @return the physical addresses for the organization
   */
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return physicalAddresses;
  }

  /**
   * Retrieve the role with the specified type for the organization independent of a party
   * association.
   *
   * @param type the code for the party role type
   * @return the role with the specified type for the organization independent of a party
   *     association or <code>null</code> if the role could not be found
   */
  public PartyRole getRole(String type) {
    return roles.stream().filter(role -> Objects.equals(role.getType(), type)).findFirst().get();
  }

  /**
   * Returns the roles for the party independent of a party association.
   *
   * @return the roles for the party independent of a party association
   */
  public Set<PartyRole> getRoles() {
    return roles;
  }

  /**
   * Retrieve the tax number with the specified type for the organization.
   *
   * @param type the tax number type
   * @return the tax number with the specified type for the organization or <code>null</code> if the
   *     tax number could not be found
   */
  public TaxNumber getTaxNumber(String type) {
    return taxNumbers.stream()
        .filter(taxNumber -> Objects.equals(taxNumber.getType(), type))
        .findFirst()
        .get();
  }

  /**
   * Returns the tax numbers for the organization.
   *
   * @return the tax numbers for the organization
   */
  public Set<TaxNumber> getTaxNumbers() {
    return taxNumbers;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((getId() == null) ? 0 : getId().hashCode());
  }

  /**
   * Remove the contact mechanism with the specified type and purpose for the organization.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   */
  public void removeContactMechanism(ContactMechanismType type, ContactMechanismPurpose purpose) {
    contactMechanisms.removeIf(
        existingContactMechanism ->
            Objects.equals(existingContactMechanism.getType(), type)
                && Objects.equals(existingContactMechanism.getPurpose(), purpose));
  }

  /**
   * Remove any physical addresses with the specified purpose for the person.
   *
   * @param purpose the physical address purpose
   */
  public void removePhysicalAddress(PhysicalAddressPurpose purpose) {
    physicalAddresses.removeIf(
        existingPhysicalAddress -> existingPhysicalAddress.getPurposes().contains(purpose));
  }

  /**
   * Remove the tax number with the specified type for the organization.
   *
   * @param type the tax number type
   */
  public void removeTaxNumber(String type) {
    taxNumbers.removeIf(existingTaxNumber -> Objects.equals(existingTaxNumber.getType(), type));
  }

  /**
   * Set the contact mechanisms for the organization.
   *
   * @param contactMechanisms the contact mechanisms for the organization
   */
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    this.contactMechanisms.clear();
    this.contactMechanisms.addAll(contactMechanisms);
  }

  /**
   * Set the optional codes for the countries of tax residence for the organization.
   *
   * @param countriesOfTaxResidence the optional codes for the countries of tax residence for the
   *     organization
   */
  public void setCountriesOfTaxResidence(Set<String> countriesOfTaxResidence) {
    this.countriesOfTaxResidence =
        StringUtils.collectionToDelimitedString(countriesOfTaxResidence, ",");
  }

  /**
   * Set the optional code for the single country of tax residence for the organization.
   *
   * @param countryOfTaxResidence the optional code for the single country of tax residence for the
   *     organization
   */
  @JsonIgnore
  public void setCountryOfTaxResidence(String countryOfTaxResidence) {
    this.countriesOfTaxResidence = countryOfTaxResidence;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the organization.
   *
   * @param id the Universally Unique Identifier (UUID) for the organization
   */
  @Override
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the name of the organization.
   *
   * @param name the name of the organization
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the physical addresses for the organization.
   *
   * @param physicalAddresses the physical addresses for the organization
   */
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    this.physicalAddresses.clear();
    this.physicalAddresses.addAll(physicalAddresses);
  }

  /**
   * Set the roles for the party independent of a party association.
   *
   * @param roles the party roles
   */
  public void setRoles(Set<PartyRole> roles) {
    this.roles.clear();
    this.roles.addAll(roles);
  }

  /**
   * Set the tax numbers for the organization.
   *
   * @param taxNumbers the tax numbers for the organization
   */
  public void setTaxNumbers(Set<TaxNumber> taxNumbers) {
    this.taxNumbers.clear();
    this.taxNumbers.addAll(taxNumbers);
  }
}

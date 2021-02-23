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

package digital.inception.banking.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.banking.customer.constraints.ValidBusinessCustomer;
import digital.inception.party.ContactMechanism;
import digital.inception.party.IdentityDocument;
import digital.inception.party.Organization;
import digital.inception.party.PartyAttribute;
import digital.inception.party.PartyRole;
import digital.inception.party.PartyType;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.Preference;
import digital.inception.party.TaxNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
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
 * The <b>BusinessCustomer</b> class holds the information for a business customer.
 *
 * <p>This class exposes the JSON and XML properties using a property-based approach rather than a
 * field-based approach to support the JPA inheritance model.
 *
 * <p>The following steps must be completed when adding a new attribute to the individual customer
 * entity:
 *
 * <ol>
 *   <li>Add a new column for the new attribute to the definition of the <b>party.organizations</b>
 *       table in the <b>inception-party-h2.sql</b> file, if the new attribute is common to all
 *       organizations, or the definition of the <b>customer.business_customers</b> table in the
 *       <b>inception-banking-customer-h2.sql</b> file.
 *   <li>Add a description for the new column for the new attribute to the
 *       <b>inception-party-h2.sql</b> file, if the new attribute is common to all organizations, or
 *       to the <b>inception-banking-customer-h2.sql</b> file.
 *   <li>Add a new property for the new attribute to the <b>Organization</b> class if required, if
 *       the new attribute is common to all organizations.
 *   <li>Add a new property for the new attribute to the <b>BusinessCustomer</b> class.
 *   <li>Add the appropriate validation for the new attribute to the
 *       <b>ValidOrganizationValidator</b> class if required, if the new attribute is common to all
 *       organizations.
 *   <li>Add the appropriate validation for the new attribute to the
 *       <b>ValidBusinessCustomerValidator</b> class.
 *   <li>Add a new column for the new attribute to the <b>inception-party.changelog.xml</b> file, if
 *       the new attribute is common to all organizations, or the
 *       <b>inception-banking-customer.changelog.xml</b>.
 * </ol>
 *
 * @author Marcus Portmann
 */
@Schema(description = "A business customer")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "name",
  "attributes",
  "contactMechanisms",
  "physicalAddresses",
  "identityDocuments",
  "preferences",
  "countriesOfTaxResidence",
  "taxNumbers",
  "roles"
})
@XmlRootElement(name = "BusinessCustomer", namespace = "http://inception.digital/banking/customer")
@XmlType(
    name = "BusinessCustomer",
    namespace = "http://inception.digital/banking/customer",
    propOrder = {
      "id",
      "tenantId",
      "name",
      "attributes",
      "contactMechanisms",
      "physicalAddresses",
      "identityDocuments",
      "preferences",
      "countriesOfTaxResidence",
      "taxNumbers",
      "roles"
    })
@XmlAccessorType(XmlAccessType.PROPERTY)
@ValidBusinessCustomer
@Entity
@Table(schema = "party", name = "organizations")
@SecondaryTable(
    schema = "customer",
    name = "business_customers",
    pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"))
public class BusinessCustomer extends CustomerBase implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The attributes for the business customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PartyAttribute> attributes = new HashSet<>();

  /** The contact mechanisms for the business customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<ContactMechanism> contactMechanisms = new HashSet<>();

  /** The identity documents for the business customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<IdentityDocument> identityDocuments = new HashSet<>();

  /** The physical addresses for the business customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PhysicalAddress> physicalAddresses = new HashSet<>();

  /** The preferences for the business customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Preference> preferences = new HashSet<>();

  /** The party roles for the business customer independent of a party association. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PartyRole> roles = new HashSet<>();

  /** The tax numbers for the business customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<TaxNumber> taxNumbers = new HashSet<>();

  /**
   * The optional comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for
   * the business customer.
   */
  @JsonIgnore
  @XmlTransient
  @Size(min = 1, max = 100)
  @Column(table = "organizations", name = "countries_of_tax_residence", length = 100)
  private String countriesOfTaxResidence;

  /** Constructs a new <b>BusinessCustomer</b>. */
  public BusinessCustomer() {}

  /**
   * Constructs a new <b>BusinessCustomer</b>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the business customer
   *     is associated with
   */
  public BusinessCustomer(UUID tenantId) {
    super(
        UuidCreator.getShortPrefixComb(), tenantId, PartyType.ORGANIZATION, CustomerType.BUSINESS);
  }

  /**
   * Add the attribute for the business customer.
   *
   * @param attribute the attribute
   */
  public void addAttribute(PartyAttribute attribute) {
    attributes.removeIf(
        existingAttribute -> Objects.equals(existingAttribute.getType(), attribute.getType()));

    attribute.setParty(this);

    attributes.add(attribute);
  }

  /**
   * Add the contact mechanism for the business customer.
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
   * Add the identity document for the business customer.
   *
   * @param identityDocument the identity document
   */
  public void addIdentityDocument(IdentityDocument identityDocument) {
    identityDocuments.removeIf(
        existingIdentityDocument ->
            Objects.equals(existingIdentityDocument.getType(), identityDocument.getType()));

    identityDocument.setParty(this);

    identityDocuments.add(identityDocument);
  }

  /**
   * Add the physical address for the business customer.
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
   * Add the preference for the business customer.
   *
   * @param preference the preference
   */
  public void addPreference(Preference preference) {
    preferences.removeIf(
        existingPreference -> Objects.equals(existingPreference.getType(), preference.getType()));

    preference.setParty(this);

    preferences.add(preference);
  }

  /**
   * Add the party role to the business customer independent of a party association.
   *
   * @param role the party role
   */
  public void addRole(PartyRole role) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), role.getType()));

    role.setParty(this);

    roles.add(role);
  }

  /**
   * Add the tax number for the business customer.
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

    BusinessCustomer other = (BusinessCustomer) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Retrieve the attribute with the specified type for the business customer.
   *
   * @param type the code for the attribute type
   * @return the attribute with the specified type for the business customer or <b>null</b> if the
   *     attribute could not be found
   */
  public PartyAttribute getAttribute(String type) {
    return attributes.stream()
        .filter(attribute -> Objects.equals(attribute.getType(), type))
        .findFirst()
        .get();
  }

  /**
   * Returns the attributes for the business customer.
   *
   * @return the attributes for the business customer
   */
  @Schema(description = "The attributes for the business customer")
  @JsonProperty
  @JsonManagedReference("attributeReference")
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  public Set<PartyAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the business customer.
   *
   * @param type the code for the contact mechanism type
   * @param purpose the code for the contact mechanism purpose
   * @return the contact mechanism with the specified type and purpose for the business customer or
   *     <b>null</b> if the contact mechanism could not be found
   */
  public ContactMechanism getContactMechanism(String type, String purpose) {
    return contactMechanisms.stream()
        .filter(
            contactMechanism ->
                Objects.equals(contactMechanism.getType(), type)
                    && Objects.equals(contactMechanism.getPurpose(), purpose))
        .findFirst()
        .get();
  }

  /**
   * Returns the contact mechanisms for the business customer.
   *
   * @return the contact mechanisms for the business customer
   */
  @Schema(description = "The contact mechanisms for the business customer")
  @JsonProperty
  @JsonManagedReference("contactMechanismReference")
  @XmlElementWrapper(name = "ContactMechanisms")
  @XmlElement(name = "ContactMechanism")
  public Set<ContactMechanism> getContactMechanisms() {
    return contactMechanisms;
  }

  /**
   * Returns the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the
   * business customer.
   *
   * @return the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the
   *     business customer
   */
  @Schema(
      description =
          "The optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the business customer")
  @JsonProperty
  @XmlElementWrapper(name = "CountriesOfTaxResidence")
  @XmlElement(name = "CountryOfTaxResidence")
  public Set<String> getCountriesOfTaxResidence() {
    return Set.of(StringUtils.commaDelimitedListToStringArray(countriesOfTaxResidence));
  }

  /**
   * Returns the date and time the business customer was created.
   *
   * @return the date and time the business customer was created
   */
  @JsonIgnore
  @XmlTransient
  public LocalDateTime getCreated() {
    return super.getCreated();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the business customer.
   *
   * @return the Universally Unique Identifier (UUID) for the business customer
   */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the business customer",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the identity documents for the business customer.
   *
   * @return the identity documents for the business customer
   */
  @Schema(description = "The identity documents for the business customer")
  @JsonProperty
  @JsonManagedReference("identityDocumentReference")
  @XmlElementWrapper(name = "IdentityDocuments")
  @XmlElement(name = "IdentityDocument")
  public Set<IdentityDocument> getIdentityDocuments() {
    return identityDocuments;
  }

  // TODO: Add identity documents -- MARCUS

  /**
   * Returns the name of the business customer.
   *
   * @return the name of the business customer
   */
  @Schema(description = "The name of the business customer")
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the party type for the business customer.
   *
   * @return the party type for the business customer
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public PartyType getPartyType() {
    return super.getPartyType();
  }

  /**
   * Retrieve the first physical address with the specified purpose for the business customer.
   *
   * @param purpose the physical address purpose
   * @return the first physical address with the specified purpose for the business customer or <b>
   *     null</b> if the physical address could not be found
   */
  public PhysicalAddress getPhysicalAddress(PhysicalAddressPurpose purpose) {
    return physicalAddresses.stream()
        .filter(physicalAddress -> physicalAddress.getPurposes().contains(purpose))
        .findFirst()
        .get();
  }

  /**
   * Returns the physical addresses for the business customer.
   *
   * @return the physical addresses for the business customer
   */
  @Schema(description = "The physical addresses for the business customer")
  @JsonProperty
  @JsonManagedReference("physicalAddressReference")
  @XmlElementWrapper(name = "PhysicalAddresses")
  @XmlElement(name = "PhysicalAddress")
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return physicalAddresses;
  }

  /**
   * Retrieve the preference with the specified type for the business customer.
   *
   * @param type the code for the preference type
   * @return the preference with the specified type for the business customer or <b>null</b> if the
   *     preference could not be found
   */
  public Preference getPreference(String type) {
    return preferences.stream()
        .filter(preference -> Objects.equals(preference.getType(), type))
        .findFirst()
        .get();
  }

  /**
   * Returns the preferences for the business customer.
   *
   * @return the preferences for the business customer
   */
  @Schema(description = "The preferences for the business customer")
  @JsonProperty
  @JsonManagedReference("preferenceReference")
  @XmlElementWrapper(name = "Preferences")
  @XmlElement(name = "Preference")
  public Set<Preference> getPreferences() {
    return preferences;
  }

  /**
   * Retrieve the role with the specified type for the business customer independent of a party
   * association.
   *
   * @param type the code for the party role type
   * @return the role with the specified type for the business customer independent of a party
   *     association or <b>null</b> if the role could not be found
   */
  public PartyRole getRole(String type) {
    return roles.stream().filter(role -> Objects.equals(role.getType(), type)).findFirst().get();
  }

  /**
   * Returns the party roles for the business customer independent of a party association.
   *
   * @return the party roles for the business customer independent of a party association
   */
  @Schema(
      description = "The party roles for the business customer independent of a party association")
  @JsonProperty
  @JsonManagedReference("partyRoleReference")
  @XmlElementWrapper(name = "Roles")
  @XmlElement(name = "Role")
  public Set<PartyRole> getRoles() {
    return roles;
  }

  /**
   * Retrieve the tax number with the specified type for the business customer.
   *
   * @param type the tax number type
   * @return the tax number with the specified type for the business customer or <b>null</b> if the
   *     tax number could not be found
   */
  public TaxNumber getTaxNumber(String type) {
    return taxNumbers.stream()
        .filter(taxNumber -> Objects.equals(taxNumber.getType(), type))
        .findFirst()
        .get();
  }

  /**
   * Returns the tax numbers for the business customer.
   *
   * @return the tax numbers for the business customer
   */
  @Schema(description = "The tax numbers for the business customer")
  @JsonProperty
  @JsonManagedReference("taxNumberReference")
  @XmlElementWrapper(name = "TaxNumbers")
  @XmlElement(name = "TaxNumber")
  public Set<TaxNumber> getTaxNumbers() {
    return taxNumbers;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the business customer is
   * associated with.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the business customer is
   *     associated with
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the tenant the business customer is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  public UUID getTenantId() {
    return super.getTenantId();
  }

  /**
   * Returns the date and time the business customer was last updated.
   *
   * @return the date and time the business customer was last updated
   */
  @JsonIgnore
  @XmlTransient
  public LocalDateTime getUpdated() {
    return super.getUpdated();
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
   * Remove the attribute with the specified type for the business customer.
   *
   * @param type the code for the attribute type
   */
  public void removeAttribute(String type) {
    attributes.removeIf(existingAttribute -> Objects.equals(existingAttribute.getType(), type));
  }

  /**
   * Remove the contact mechanism with the specified type and purpose for the business customer.
   *
   * @param type the code for the contact mechanism type
   * @param purpose the code for the contact mechanism purpose
   */
  public void removeContactMechanism(String type, String purpose) {
    contactMechanisms.removeIf(
        existingContactMechanism ->
            Objects.equals(existingContactMechanism.getType(), type)
                && Objects.equals(existingContactMechanism.getPurpose(), purpose));
  }

  /**
   * Remove the identity document with the specified type for the business customer.
   *
   * @param type the code for the identity document type
   */
  public void removeIdentityDocumentByType(String type) {
    identityDocuments.removeIf(
        existingIdentityDocument -> Objects.equals(existingIdentityDocument.getType(), type));
  }

  /**
   * Remove any physical addresses with the specified purpose for the business customer.
   *
   * @param purpose the physical address purpose
   */
  public void removePhysicalAddress(PhysicalAddressPurpose purpose) {
    physicalAddresses.removeIf(
        existingPhysicalAddress -> existingPhysicalAddress.getPurposes().contains(purpose));
  }

  /**
   * Remove the preference with the specified type for the business customer.
   *
   * @param type the code for the preference type
   */
  public void removePreference(String type) {
    preferences.removeIf(existingPreference -> Objects.equals(existingPreference.getType(), type));
  }

  /**
   * Remove the role with the specified type for the business customer.
   *
   * @param type the code for party role type
   */
  public void removeRole(String type) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), type));
  }

  /**
   * Remove the tax number with the specified type for the business customer.
   *
   * @param type the tax number type
   */
  public void removeTaxNumber(String type) {
    taxNumbers.removeIf(existingTaxNumber -> Objects.equals(existingTaxNumber.getType(), type));
  }

  /**
   * Set the attributes for the business customer.
   *
   * @param attributes the attributes for the business customer
   */
  public void setAttributes(Set<PartyAttribute> attributes) {
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the contact mechanisms for the business customer.
   *
   * @param contactMechanisms the contact mechanisms for the business customer
   */
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    this.contactMechanisms.clear();
    this.contactMechanisms.addAll(contactMechanisms);
  }

  /**
   * Set the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the business
   * customer.
   *
   * @param countriesOfTaxResidence the optional ISO 3166-1 alpha-2 codes for the countries of tax
   *     residence for the business customer
   */
  public void setCountriesOfTaxResidence(Set<String> countriesOfTaxResidence) {
    this.countriesOfTaxResidence =
        StringUtils.collectionToDelimitedString(countriesOfTaxResidence, ",");
  }

  /**
   * Set the optional code for the single country of tax residence for the business customer.
   *
   * @param countryOfTaxResidence the optional code for the single country of tax residence for the
   *     business customer
   */
  @JsonIgnore
  public void setCountryOfTaxResidence(String countryOfTaxResidence) {
    this.countriesOfTaxResidence = countryOfTaxResidence;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the business customer.
   *
   * @param id the Universally Unique Identifier (UUID) for the business customer
   */
  @Override
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the identity documents for the business customer.
   *
   * @param identityDocuments the identity documents for the business customer
   */
  public void setIdentityDocuments(Set<IdentityDocument> identityDocuments) {
    this.identityDocuments.clear();
    this.identityDocuments.addAll(identityDocuments);
  }

  /**
   * Set the name of the business customer.
   *
   * @param name the name of the business customer
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the physical addresses for the business customer.
   *
   * @param physicalAddresses the physical addresses for the business customer
   */
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    this.physicalAddresses.clear();
    this.physicalAddresses.addAll(physicalAddresses);
  }

  /**
   * Set the preferences for the business customer.
   *
   * @param preferences the preferences for the business customer
   */
  public void setPreferences(Set<Preference> preferences) {
    this.preferences.clear();
    this.preferences.addAll(preferences);
  }

  /**
   * Set the party roles for the business customer independent of a party association.
   *
   * @param roles the party roles
   */
  public void setRoles(Set<PartyRole> roles) {
    this.roles.clear();
    this.roles.addAll(roles);
  }

  /**
   * Set the tax numbers for the business customer.
   *
   * @param taxNumbers the tax numbers for the business customer
   */
  public void setTaxNumbers(Set<TaxNumber> taxNumbers) {
    this.taxNumbers.clear();
    this.taxNumbers.addAll(taxNumbers);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the business customer is associated
   * with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the business customer
   *     is associated with
   */
  public void setTenantId(UUID tenantId) {
    super.setTenantId(tenantId);
  }
}

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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.banking.customer.constraints.ValidIndividualCustomer;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismPurpose;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.IdentityDocument;
import digital.inception.party.PartyRole;
import digital.inception.party.PartyType;
import digital.inception.party.Person;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.Preference;
import digital.inception.party.TaxNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>IndividualCustomer</b> class holds the information for an individual customer.
 *
 * <p>This class exposes the JSON and XML properties using a property-based approach rather than a
 * field-based approach to support the JPA inheritance model.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An individual customer")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "name",
  "countryOfBirth",
  "countryOfResidence",
  "dateOfBirth",
  "dateOfDeath",
  "emancipatedMinor",
  "employmentStatus",
  "employmentType",
  "gender",
  "givenName",
  "homeLanguage",
  "initials",
  "maidenName",
  "maritalStatus",
  "marriageType",
  "middleNames",
  "occupation",
  "preferredName",
  "race",
  "residencyStatus",
  "residentialType",
  "surname",
  "title",
  "contactMechanisms",
  "identityDocuments",
  "physicalAddresses",
  "preferences",
  "countriesOfTaxResidence",
  "taxNumbers",
  "roles"
})
@XmlRootElement(
    name = "IndividualCustomer",
    namespace = "http://customer.banking.inception.digital")
@XmlType(
    name = "IndividualCustomer",
    namespace = "http://customer.banking.inception.digital",
    propOrder = {"emancipatedMinor"})
@XmlAccessorType(XmlAccessType.PROPERTY)
@ValidIndividualCustomer
@Entity
@Table(schema = "customer", name = "individual_customers")
public class IndividualCustomer extends Person implements Serializable {

  private static final long serialVersionUID = 1000000;

  /**
   * The optional boolean flag indicating whether the individual customer is an emancipated minor.
   */
  @Column(table = "individual_customers", name = "emancipated_minor")
  private Boolean emancipatedMinor = false;

  /** Constructs a new <b>IndividualCustomer</b>. */
  public IndividualCustomer() {}

  /**
   * Add the contact mechanism for the individual customer.
   *
   * @param contactMechanism the contact mechanism
   */
  @Override
  public void addContactMechanism(ContactMechanism contactMechanism) {
    super.addContactMechanism(contactMechanism);
  }

  /**
   * Add the identity document for the individual customer.
   *
   * @param identityDocument the identity document
   */
  @Override
  public void addIdentityDocument(IdentityDocument identityDocument) {
    super.addIdentityDocument(identityDocument);
  }

  /**
   * Add the physical address for the individual customer.
   *
   * @param physicalAddress the physical address
   */
  @Override
  public void addPhysicalAddress(PhysicalAddress physicalAddress) {
    super.addPhysicalAddress(physicalAddress);
  }

  /**
   * Add the preference for the individual customer.
   *
   * @param preference the preference
   */
  @Override
  public void addPreference(Preference preference) {
    super.addPreference(preference);
  }

  /**
   * Add the party role to the individual customer independent of a party association.
   *
   * @param role the party role
   */
  @Override
  public void addRole(PartyRole role) {
    super.addRole(role);
  }

  /**
   * Add the tax number for the individual customer.
   *
   * @param taxNumber the tax number
   */
  @Override
  public void addTaxNumber(TaxNumber taxNumber) {
    super.addTaxNumber(taxNumber);
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the individual customer.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   * @return the contact mechanism with the specified type and purpose for the individual customer
   *     or <b>null </b> if the contact mechanism could not be found
   */
  @Override
  public ContactMechanism getContactMechanism(
      ContactMechanismType type, ContactMechanismPurpose purpose) {
    return super.getContactMechanism(type, purpose);
  }

  /**
   * Returns the contact mechanisms for the individual customer.
   *
   * @return the contact mechanisms for the individual customer
   */
  @Schema(description = "The contact mechanisms for the individual customer")
  @Override
  public Set<ContactMechanism> getContactMechanisms() {
    return super.getContactMechanisms();
  }

  /**
   * Returns the codes for the countries of tax residence for the individual customer.
   *
   * @return the codes for the countries of tax residence for the individual customer
   */
  @Schema(
      description = "The codes for the countries of tax residence for the individual customer",
      required = true)
  @JsonProperty(required = true)
  public Set<String> getCountriesOfTaxResidence() {
    return super.getCountriesOfTaxResidence();
  }

  /**
   * Returns the code for the country of birth for the individual customer.
   *
   * @return the code for the country of birth for the individual customer
   */
  @Schema(
      description = "The code for the country of birth for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getCountryOfBirth() {
    return super.getCountryOfBirth();
  }

  /**
   * Returns the code for the country of residence for the individual customer.
   *
   * @return the code for the country of residence for the individual customer
   */
  @Schema(
      description = "The code for the country of residence for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getCountryOfResidence() {
    return super.getCountryOfResidence();
  }

  /**
   * Returns the date and time the individual customer was created.
   *
   * @return the date and time the individual customer was created
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getCreated() {
    return super.getCreated();
  }

  /**
   * Returns the date of birth for the individual customer.
   *
   * @return the date of birth for the individual customer
   */
  @Schema(description = "The date of birth for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public LocalDate getDateOfBirth() {
    return super.getDateOfBirth();
  }

  /**
   * Returns the optional date of death for the individual customer.
   *
   * @return the optional date of death for the individual customer
   */
  @Schema(description = "The optional date of death for the individual customer")
  @JsonProperty
  @Override
  public LocalDate getDateOfDeath() {
    return super.getDateOfDeath();
  }

  /**
   * Returns the optional boolean flag indicating whether the individual customer is an emancipated
   * minor.
   *
   * @return the optional boolean flag indicating whether the individual customer is an emancipated
   *     minor
   */
  @Schema(
      description =
          "The optional boolean flag indicating whether the individual customer is an emancipated minor")
  @JsonProperty
  @XmlElement(name = "EmancipatedMinor")
  public Boolean getEmancipatedMinor() {
    return emancipatedMinor;
  }

  /**
   * Returns the code for the employment status for the individual customer.
   *
   * @return the code for the employment status for the individual customer
   */
  @Schema(
      description = "The code for the employment status for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getEmploymentStatus() {
    return super.getEmploymentStatus();
  }

  /**
   * Returns the code for the employment type for the individual customer.
   *
   * @return the code for the employment type for the individual customer
   */
  @Schema(
      description = "The code for the employment type for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getEmploymentType() {
    return super.getEmploymentType();
  }

  /**
   * Returns the code for the gender for the individual customer.
   *
   * @return the code for the gender for the individual customer
   */
  @Schema(description = "The code for the gender for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getGender() {
    return super.getGender();
  }

  /**
   * Returns the given name, firstname, forename, or Christian name for the individual customer.
   *
   * @return the given name, firstname, forename, or Christian name for the individual customer
   */
  @Schema(
      description =
          "The given name, firstname, forename, or Christian name for the individual customer.",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getGivenName() {
    return super.getGivenName();
  }

  /**
   * Returns the code for the home language for the individual customer.
   *
   * @return the code for the home language for the individual customer
   */
  @Schema(
      description = "The code for the home language for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getHomeLanguage() {
    return super.getHomeLanguage();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the individual customer.
   *
   * @return the Universally Unique Identifier (UUID) for the individual customer
   */
  @Schema(description = "The Universally Unique Identifier (UUID) for the individual customer")
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the identity documents for the individual customer.
   *
   * @return the identity documents for the individual customer
   */
  @Schema(description = "The identity documents for the individual customer")
  @Override
  public Set<IdentityDocument> getIdentityDocuments() {
    return super.getIdentityDocuments();
  }

  /**
   * Returns the initials for the individual customer.
   *
   * @return the initials for the individual customer
   */
  @Schema(description = "The initials for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getInitials() {
    return super.getInitials();
  }

  /**
   * Returns the optional maiden name for the individual customer.
   *
   * @return the optional maiden name for the individual customer
   */
  @Schema(description = "The optional maiden name for the individual customer")
  @Override
  public String getMaidenName() {
    return super.getMaidenName();
  }

  /**
   * Returns the code for the marital status for the individual customer.
   *
   * @return the code for the marital status for the individual customer
   */
  @Schema(
      description = "The code for the marital status for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getMaritalStatus() {
    return super.getMaritalStatus();
  }

  /**
   * Returns the code for the marriage type for the individual customer if the individual customer
   * is married.
   *
   * @return the code for the marriage type for the individual customer if the individual customer
   *     is married
   */
  @Schema(
      description =
          "The code for the marriage type for the individual customer if the person is married")
  @Override
  public String getMarriageType() {
    return super.getMarriageType();
  }

  /**
   * Returns the optional middle names for the individual customer.
   *
   * @return the optional middle names for the individual customer
   */
  @Schema(description = "The optional middle names for the individual customer")
  @Override
  public String getMiddleNames() {
    return super.getMiddleNames();
  }

  /**
   * Returns the personal name or full name of the individual customer.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the individual customer. This name should match the full name on the identity
   * document(s) associated with the individual customer.
   *
   * <p>See https://en.wikipedia.org/wiki/Personal_name
   *
   * @return the personal name or full name of the individual customer
   */
  @Schema(description = "The personal name or full name of the individual customer")
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the code for the occupation for the individual customer.
   *
   * @return the code for the occupation for the individual customer
   */
  @Schema(description = "The code for the occupation for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getOccupation() {
    return super.getOccupation();
  }

  /**
   * Retrieve the first physical address with the specified purpose for the individual customer.
   *
   * @param purpose the physical address purpose
   * @return the first physical address with the specified purpose for the individual customer or
   *     <b>null </b> if the physical address could not be found
   */
  @Override
  public PhysicalAddress getPhysicalAddress(PhysicalAddressPurpose purpose) {
    return super.getPhysicalAddress(purpose);
  }

  /**
   * Returns the physical addresses for the individual customer.
   *
   * @return the physical addresses for the individual customer
   */
  @Schema(description = "The physical addresses for the individual customer")
  @Override
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return super.getPhysicalAddresses();
  }

  /**
   * Returns the preferences for the individual customer.
   *
   * @return the preferences for the individual customer
   */
  @Schema(description = "The preferences for the individual customer")
  @Override
  public Set<Preference> getPreferences() {
    return super.getPreferences();
  }

  /**
   * Returns the optional preferred name for the individual customer.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @return the optional preferred name for the individual customer
   */
  @Schema(description = "The optional preferred name for the individual customer")
  @Override
  public String getPreferredName() {
    return super.getPreferredName();
  }

  /**
   * The code for the race for the individual customer.
   *
   * @return the code for the race for the individual customer
   */
  @Schema(description = "The code for the race for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getRace() {
    return super.getRace();
  }

  /**
   * Returns the code for the residency status for the individual customer.
   *
   * @return the code for the residency status for the individual customer
   */
  @Schema(
      description = "The code for the residency status for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getResidencyStatus() {
    return super.getResidencyStatus();
  }

  /**
   * Returns the code for the residential type for the individual customer.
   *
   * @return the code for the residential type for the individual customer
   */
  @Schema(
      description = "The code for the residential type for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getResidentialType() {
    return super.getResidentialType();
  }

  /**
   * Retrieve the role with the specified type for the individual customer independent of a party
   * association.
   *
   * @param type the code for the party role type
   * @return the role with the specified type for the individual customer independent of a party
   *     association or <b>null</b> if the role could not be found
   */
  @Override
  public PartyRole getRole(String type) {
    return super.getRole(type);
  }

  /**
   * Returns the party roles for the individual customer independent of a party association.
   *
   * @return the party roles for the individual customer independent of a party association
   */
  @Override
  public Set<PartyRole> getRoles() {
    return super.getRoles();
  }

  /**
   * Returns the surname, last name, or family name for the individual customer.
   *
   * @return the surname, last name, or family name for the individual customer
   */
  @Schema(
      description = "The surname, last name, or family name for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getSurname() {
    return super.getSurname();
  }

  /**
   * Retrieve the tax number with the specified type for the individual customer.
   *
   * @param type the code for the tax number type
   * @return the tax number with the specified type for the individual customer or <b>null</b> if
   *     the tax number could not be found
   */
  @Override
  public TaxNumber getTaxNumber(String type) {
    return super.getTaxNumber(type);
  }

  /**
   * Returns the tax numbers for the individual customer.
   *
   * @return the tax numbers for the individual customer
   */
  @Schema(description = "The tax numbers for the individual customer")
  @Override
  public Set<TaxNumber> getTaxNumbers() {
    return super.getTaxNumbers();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the individual customer is
   * associated with.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the individual customer is
   *     associated with
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the tenant the individual customer is associated with",
      required = true)
  @Override
  public UUID getTenantId() {
    return super.getTenantId();
  }

  /**
   * Returns the code for the title for the individual customer.
   *
   * @return the code for the title for the individual customer
   */
  @Schema(description = "The code for the title for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getTitle() {
    return super.getTitle();
  }

  /**
   * Returns the party type for the individual customer.
   *
   * @return the party type for the individual customer
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public PartyType getType() {
    return super.getType();
  }

  /**
   * Returns the date and time the individual customer was last updated.
   *
   * @return the date and time the individual customer was last updated
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getUpdated() {
    return super.getUpdated();
  }

  /**
   * Remove the contact mechanism with the specified type and purpose for the individual customer.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   */
  @Override
  public void removeContactMechanism(ContactMechanismType type, ContactMechanismPurpose purpose) {
    super.removeContactMechanism(type, purpose);
  }

  /**
   * Remove the identity document with the specified type for the individual customer.
   *
   * @param type the code for the identity document type
   */
  @Override
  public void removeIdentityDocumentByType(String type) {
    super.removeIdentityDocumentByType(type);
  }

  /**
   * Remove any physical addresses with the specified purpose for the individual customer.
   *
   * @param purpose the physical address purpose
   */
  @Override
  public void removePhysicalAddress(PhysicalAddressPurpose purpose) {
    super.removePhysicalAddress(purpose);
  }

  /**
   * Remove the preference with the specified type for the individual customer.
   *
   * @param type the code for the preference type
   */
  @Override
  public void removePreference(String type) {
    super.removePreference(type);
  }

  /**
   * Remove the role with the specified type for the individual customer.
   *
   * @param type the code for party role type
   */
  @Override
  public void removeRole(String type) {
    super.removeRole(type);
  }

  /**
   * Remove the tax number with the specified type for the individual customer.
   *
   * @param type the code for the tax number type
   */
  @Override
  public void removeTaxNumber(String type) {
    super.removeTaxNumber(type);
  }

  /**
   * Set the contact mechanisms for the individual customer.
   *
   * @param contactMechanisms the contact mechanisms for the individual customer
   */
  @Override
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    super.setContactMechanisms(contactMechanisms);
  }

  /**
   * Set the codes for the countries of tax residence for the individual customer.
   *
   * @param countriesOfTaxResidence the codes for the countries of tax residence for the individual
   *     customer
   */
  public void setCountriesOfTaxResidence(Set<String> countriesOfTaxResidence) {
    super.setCountriesOfTaxResidence(countriesOfTaxResidence);
  }

  /**
   * Set the code for the country of birth for the individual customer.
   *
   * @param countryOfBirth the code for the country of birth for the individual customer
   */
  @Override
  public void setCountryOfBirth(String countryOfBirth) {
    super.setCountryOfBirth(countryOfBirth);
  }

  /**
   * Set the code for the country of residence for the individual customer.
   *
   * @param countryOfResidence the code for the country of residence for the individual customer
   */
  @Override
  public void setCountryOfResidence(String countryOfResidence) {
    super.setCountryOfResidence(countryOfResidence);
  }

  /**
   * Set the code for the single country of tax residence for the individual customer.
   *
   * @param countryOfTaxResidence the code for the single country of tax residence for the
   *     individual customer
   */
  @JsonIgnore
  @Override
  public void setCountryOfTaxResidence(String countryOfTaxResidence) {
    super.setCountryOfTaxResidence(countryOfTaxResidence);
  }

  /**
   * Set the date of birth for the individual customer.
   *
   * @param dateOfBirth the date of birth for the individual customer
   */
  @Override
  public void setDateOfBirth(LocalDate dateOfBirth) {
    super.setDateOfBirth(dateOfBirth);
  }

  /**
   * Set the optional date of death for the individual customer.
   *
   * @param dateOfDeath the optional date of death for the individual customer
   */
  @Override
  public void setDateOfDeath(LocalDate dateOfDeath) {
    super.setDateOfDeath(dateOfDeath);
  }

  /**
   * Set the optional boolean flag indicating whether the individual customer is an emancipated
   * minor.
   *
   * @param emancipatedMinor the optional boolean flag indicating whether the individual customer is
   *     an emancipated minor
   */
  public void setEmancipatedMinor(Boolean emancipatedMinor) {
    this.emancipatedMinor = emancipatedMinor;
  }

  /**
   * Set the code for the employment status for the individual customer.
   *
   * @param employmentStatus the code for the employment status for the individual customer
   */
  @Override
  public void setEmploymentStatus(String employmentStatus) {
    super.setEmploymentStatus(employmentStatus);
  }

  /**
   * Set the code for the employment type for the individual customer.
   *
   * @param employmentType the code for the employment type for the individual customer
   */
  @Override
  public void setEmploymentType(String employmentType) {
    super.setEmploymentType(employmentType);
  }

  /**
   * Set the code for the gender for the individual customer.
   *
   * @param gender the code for the gender for the individual customer
   */
  @Override
  public void setGender(String gender) {
    super.setGender(gender);
  }

  /**
   * Set the given name, firstname, forename, or Christian name for the individual customer.
   *
   * @param givenName the given name, firstname, forename, or Christian name for the individual customer
   */
  @Override
  public void setGivenName(String givenName) {
    super.setGivenName(givenName);
  }

  /**
   * Set the code for the home language for the individual customer.
   *
   * @param homeLanguage the code for the home language for the individual customer
   */
  @Override
  public void setHomeLanguage(String homeLanguage) {
    super.setHomeLanguage(homeLanguage);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the individual customer.
   *
   * @param id the Universally Unique Identifier (UUID) for the individual customer
   */
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the identity documents for the individual customer.
   *
   * @param identityDocuments the identity documents for the individual customer
   */
  @Override
  public void setIdentityDocuments(Set<IdentityDocument> identityDocuments) {
    super.setIdentityDocuments(identityDocuments);
  }

  /**
   * Set the initials for the individual customer.
   *
   * @param initials the initials for the individual customer
   */
  @Override
  public void setInitials(String initials) {
    super.setInitials(initials);
  }

  /**
   * Set the optional maiden name for the individual customer.
   *
   * @param maidenName the optional maiden name for the individual customer
   */
  @Override
  public void setMaidenName(String maidenName) {
    super.setMaidenName(maidenName);
  }

  /**
   * Set the code for the marital status for the individual customer.
   *
   * @param maritalStatus the code for the marital status for the individual customer
   */
  @Override
  public void setMaritalStatus(String maritalStatus) {
    super.setMaritalStatus(maritalStatus);
  }

  /**
   * Set the code for the marriage type for the individual customer if the individual customer is married.
   *
   * @param marriageType the code for the marriage type for the individual customer if the
   *     individual customer is married
   */
  @Override
  public void setMarriageType(String marriageType) {
    super.setMarriageType(marriageType);
  }

  /**
   * Set the optional middle names for the individual customer.
   *
   * @param middleNames the optional middle names for the individual customer
   */
  @Override
  public void setMiddleNames(String middleNames) {
    super.setMiddleNames(middleNames);
  }

  /**
   * Set the personal name or full name of the individual customer.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the individual customer. This name should match the full name on the identity
   * document(s) associated with the individual customer.
   *
   * <p>See https://en.wikipedia.org/wiki/Personal_name
   *
   * @param name the personal name or full name of the individual customer
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the code for the occupation for the individual customer.
   *
   * @param occupation the code for the occupation for the individual customer
   */
  @Override
  public void setOccupation(String occupation) {
    super.setOccupation(occupation);
  }

  /**
   * Set the physical addresses for the individual customer.
   *
   * @param physicalAddresses the physical addresses for the individual customer
   */
  @Override
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    super.setPhysicalAddresses(physicalAddresses);
  }

  /**
   * Set the preferences for the individual customer.
   *
   * @param preferences the preferences for the individual customer
   */
  @Override
  public void setPreferences(Set<Preference> preferences) {
    super.setPreferences(preferences);
  }

  /**
   * Set the optional preferred name for the individual customer.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @param preferredName the optional preferred name for the individual customer
   */
  @Override
  public void setPreferredName(String preferredName) {
    super.setPreferredName(preferredName);
  }

  /**
   * Set the code for the race for the individual customer.
   *
   * @param race the code for the race for the individual customer
   */
  @Override
  public void setRace(String race) {
    super.setRace(race);
  }

  /**
   * Set the code for the residency status for the individual customer.
   *
   * @param residencyStatus the code for the residency status for the individual customer
   */
  @Override
  public void setResidencyStatus(String residencyStatus) {
    super.setResidencyStatus(residencyStatus);
  }

  /**
   * Set the code for the residential type for the individual customer.
   *
   * @param residentialType the code for the residential type for the individual customer
   */
  @Override
  public void setResidentialType(String residentialType) {
    super.setResidentialType(residentialType);
  }

  /**
   * Set the party roles for the individual customer independent of a party association.
   *
   * @param roles the party roles for the individual customer
   */
  @Override
  public void setRoles(Set<PartyRole> roles) {
    super.setRoles(roles);
  }

  /**
   * Set the surname, last name, or family name for the individual customer.
   *
   * @param surname the surname, last name, or family name for the individual customer
   */
  @Override
  public void setSurname(String surname) {
    super.setSurname(surname);
  }

  /**
   * Set the tax numbers for the individual customer.
   *
   * @param taxNumbers the tax numbers for the individual customer
   */
  @Override
  public void setTaxNumbers(Set<TaxNumber> taxNumbers) {
    super.setTaxNumbers(taxNumbers);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the individual customer is
   * associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the individual customer
   *     is associated with
   */
  @Override
  public void setTenantId(UUID tenantId) {
    super.setTenantId(tenantId);
  }

  /**
   * Set the code for the title for the individual customer.
   *
   * @param title the code for the title for the individual customer
   */
  @Override
  public void setTitle(String title) {
    super.setTitle(title);
  }
}

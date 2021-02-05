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
import digital.inception.banking.customer.constraints.ValidIndividualCustomer;
import digital.inception.party.ContactMechanism;
import digital.inception.party.IdentityDocument;
import digital.inception.party.PartyRole;
import digital.inception.party.PartyType;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.Preference;
import digital.inception.party.TaxNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
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
import javax.validation.constraints.NotNull;
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
 * The <b>IndividualCustomer</b> class holds the information for an individual customer.
 *
 * <p>This class exposes the JSON and XML properties using a property-based approach rather than a
 * field-based approach to support the JPA inheritance model.
 *
 * <p>The following steps must be completed when adding a new attribute to the individual customer
 * entity:
 *
 * <ol>
 *   <li>Add a new column for the new attribute to the definition of the <b>party.persons</b> table
 *       in the <b>inception-party-h2.sql</b> file, if the new attribute is common to all persons,
 *       or the definition of the <b>customer.individual_customers</b> table in the
 *       <b>inception-banking-h2.sql</b> file.
 *   <li>Add a description for the new column for the new attribute to the
 *       <b>inception-party-h2.sql</b> file, if the new attribute is common to all persons, or
 *       <b>inception-banking-h2.sql</b> file.
 *   <li>Add a new property for the new attribute to the <b>Person</b> class if required, if the new
 *       attribute is common to all persons.
 *   <li>Add a new property for the new attribute to the <b>IndividualCustomer</b> class.
 *   <li>Add the appropriate validation for the new attribute to the <b>ValidPersonValidator</b>
 *       class if required, if the new attribute is common to all persons.
 *   <li>Add the appropriate validation for the new attribute to the
 *       <b>ValidIndividualCustomerValidator</b> class.
 *   <li>Add a new column for the new attribute to the <b>inception-party.changelog.xml</b> file, if
 *       the new attribute is common to all persons, or the <b>inception-banking.changelog.xml</b>.
 * </ol>
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
    propOrder = {
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
@XmlAccessorType(XmlAccessType.PROPERTY)
@ValidIndividualCustomer
@Entity
@Table(schema = "party", name = "persons")
@SecondaryTable(
    schema = "customer",
    name = "individual_customers",
    pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"))
public class IndividualCustomer extends CustomerBase implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The contact mechanisms for the individual customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<ContactMechanism> contactMechanisms = new HashSet<>();

  /** The identity documents for the individual customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<IdentityDocument> identityDocuments = new HashSet<>();

  /** The physical addresses for the individual customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PhysicalAddress> physicalAddresses = new HashSet<>();

  /** The preferences for the individual customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Preference> preferences = new HashSet<>();

  /** The party roles for the individual customer independent of a party association. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PartyRole> roles = new HashSet<>();

  /** The tax numbers for the individual customer. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<TaxNumber> taxNumbers = new HashSet<>();

  /**
   * The optional comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for
   * the individual customer.
   */
  @JsonIgnore
  @XmlTransient
  @Size(max = 100)
  @Column(table = "persons", name = "countries_of_tax_residence", length = 100)
  private String countriesOfTaxResidence;

  /** The ISO 3166-1 alpha-2 code for the country of birth for the individual customer. */
  @NotNull
  @Size(min = 2, max = 2)
  @Column(table = "persons", name = "country_of_birth", length = 2)
  private String countryOfBirth;

  /** The ISO 3166-1 alpha-2 code for the country of residence for the individual customer. */
  @NotNull
  @Size(min = 2, max = 2)
  @Column(table = "persons", name = "country_of_residence", length = 2)
  private String countryOfResidence;

  /** The date of birth for the individual customer. */
  @NotNull
  @Column(table = "persons", name = "date_of_birth")
  private LocalDate dateOfBirth;

  /** The optional date of death for the individual customer. */
  @Column(table = "persons", name = "date_of_death")
  private LocalDate dateOfDeath;

  /**
   * The optional boolean flag indicating whether the individual customer is an emancipated minor.
   */
  @Column(table = "individual_customers", name = "emancipated_minor")
  private Boolean emancipatedMinor = false;

  /** The code for the employment status for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "employment_status", length = 30)
  private String employmentStatus;

  /** The code for the employment type for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "employment_type", length = 30)
  private String employmentType;

  /** The code for the gender for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "gender", length = 30)
  private String gender;

  /** The given name, firstname, forename, or Christian name for the individual customer. */
  @NotNull
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "given_name", length = 100)
  private String givenName;

  /** The code for the home language for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "home_language", length = 30)
  private String homeLanguage;

  /** The initials for the individual customer. */
  @NotNull
  @Size(min = 1, max = 20)
  @Column(table = "persons", name = "initials", length = 20)
  private String initials;

  /** The optional maiden name for the individual customer. */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "maiden_name", length = 100)
  private String maidenName;

  /** The code for the marital status for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "marital_status", length = 30)
  private String maritalStatus;

  /**
   * The optional code for the marriage type for the individual customer if the individual customer
   * is married.
   */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "marriage_type", length = 30)
  private String marriageType;

  /** The optional middle names for the individual customer. */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "middle_names", length = 100)
  private String middleNames;

  /** The code for the occupation for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "occupation", length = 30)
  private String occupation;

  /**
   * The optional preferred name for the individual customer.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "preferred_name", length = 100)
  private String preferredName;

  /** The code for the race for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "race", length = 30)
  private String race;

  /** The code for the residency status for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "residency_status", length = 30)
  private String residencyStatus;

  /** The code for the residential type for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "residential_type", length = 30)
  private String residentialType;

  /** The surname, last name, or family name for the individual customer. */
  @NotNull
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "surname", length = 100)
  private String surname;

  /** The code for the title for the individual customer. */
  @NotNull
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "title", length = 30)
  private String title;

  /** Constructs a new <b>IndividualCustomer</b>. */
  public IndividualCustomer() {
    super(PartyType.PERSON, CustomerType.INDIVIDUAL);
  }

  /**
   * Add the contact mechanism for the individual customer.
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
   * Add the identity document for the individual customer.
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
   * Add the physical address for the individual customer.
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
   * Add the preference for the individual customer.
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
   * Add the party role to the individual customer independent of a party association.
   *
   * @param role the party role
   */
  public void addRole(PartyRole role) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), role.getType()));

    role.setParty(this);

    roles.add(role);
  }

  /**
   * Add the tax number for the individual customer.
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

    IndividualCustomer other = (IndividualCustomer) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the individual customer.
   *
   * @param type the code for the contact mechanism type
   * @param purpose the code for the contact mechanism purpose
   * @return the contact mechanism with the specified type and purpose for the individual customer
   *     or <b>null</b> if the contact mechanism could not be found
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
   * Returns the contact mechanisms for the individual customer.
   *
   * @return the contact mechanisms for the individual customer
   */
  @Schema(description = "The contact mechanisms for the individual customer")
  @JsonProperty
  @JsonManagedReference("contactMechanismReference")
  @XmlElementWrapper(name = "ContactMechanisms")
  @XmlElement(name = "ContactMechanism")
  public Set<ContactMechanism> getContactMechanisms() {
    return contactMechanisms;
  }

  /**
   * Returns the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the
   * individual customer.
   *
   * @return the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the
   *     individual customer
   */
  @Schema(
      description =
          "The optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the individual customer")
  @JsonProperty
  @XmlElementWrapper(name = "CountriesOfTaxResidence")
  @XmlElement(name = "CountryOfTaxResidence")
  public Set<String> getCountriesOfTaxResidence() {
    return Set.of(StringUtils.commaDelimitedListToStringArray(countriesOfTaxResidence));
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of birth for the individual customer.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of birth for the individual customer
   */
  @Schema(
      description =
          "The ISO 3166-1 alpha-2 code for the country of birth for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "CountryOfBirth", required = true)
  public String getCountryOfBirth() {
    return countryOfBirth;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of residence for the individual customer.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of residence for the individual customer
   */
  @Schema(
      description =
          "The ISO 3166-1 alpha-2 code for the country of residence for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "CountryOfResidence", required = true)
  public String getCountryOfResidence() {
    return countryOfResidence;
  }

  /**
   * Returns the date and time the individual customer was created.
   *
   * @return the date and time the individual customer was created
   */
  @JsonIgnore
  @XmlTransient
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
  @XmlElement(name = "DateOfBirth", required = true)
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Returns the optional date of death for the individual customer.
   *
   * @return the optional date of death for the individual customer
   */
  @Schema(description = "The optional date of death for the individual customer")
  @JsonProperty
  @XmlElement(name = "DateOfDeath")
  public LocalDate getDateOfDeath() {
    return dateOfDeath;
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
  @XmlElement(name = "EmploymentStatus", required = true)
  public String getEmploymentStatus() {
    return employmentStatus;
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
  @XmlElement(name = "EmploymentType", required = true)
  public String getEmploymentType() {
    return employmentType;
  }

  /**
   * Returns the code for the gender for the individual customer.
   *
   * @return the code for the gender for the individual customer
   */
  @Schema(description = "The code for the gender for the individual customer", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Gender", required = true)
  public String getGender() {
    return gender;
  }

  /**
   * Returns the given name, firstname, forename, or Christian name for the individual customer.
   *
   * @return the given name, firstname, forename, or Christian name for the individual customer
   */
  @Schema(
      description =
          "The given name, firstname, forename, or Christian name for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "GivenName", required = true)
  public String getGivenName() {
    return givenName;
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
  @XmlElement(name = "HomeLanguage", required = true)
  public String getHomeLanguage() {
    return homeLanguage;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the individual customer.
   *
   * @return the Universally Unique Identifier (UUID) for the individual customer
   */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
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
  @JsonProperty
  @JsonManagedReference("identityDocumentReference")
  @XmlElementWrapper(name = "IdentityDocuments")
  @XmlElement(name = "IdentityDocument")
  public Set<IdentityDocument> getIdentityDocuments() {
    return identityDocuments;
  }

  /**
   * Returns the initials for the individual customer.
   *
   * @return the initials for the individual customer
   */
  @Schema(description = "The initials for the individual customer", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Initials", required = true)
  public String getInitials() {
    return initials;
  }

  /**
   * Returns the optional maiden name for the individual customer.
   *
   * @return the optional maiden name for the individual customer
   */
  @Schema(description = "The optional maiden name for the individual customer")
  @JsonProperty
  @XmlElement(name = "MaidenName")
  public String getMaidenName() {
    return maidenName;
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
  @XmlElement(name = "MaritalStatus", required = true)
  public String getMaritalStatus() {
    return maritalStatus;
  }

  /**
   * Returns the optional code for the marriage type for the individual customer if the individual
   * customer is married.
   *
   * @return the optional code for the marriage type for the individual customer if the individual
   *     customer is married
   */
  @Schema(
      description =
          "The optional code for the marriage type for the individual customer if the individual customer is married")
  @JsonProperty
  @XmlElement(name = "MarriageType")
  public String getMarriageType() {
    return marriageType;
  }

  /**
   * Returns the optional middle names for the individual customer.
   *
   * @return the optional middle names for the individual customer
   */
  @Schema(description = "The optional middle names for the individual customer")
  @JsonProperty
  @XmlElement(name = "MiddleNames")
  public String getMiddleNames() {
    return middleNames;
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
  @Schema(
      description = "The personal name or full name of the individual customer",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
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
  @XmlElement(name = "Occupation", required = true)
  public String getOccupation() {
    return occupation;
  }

  /**
   * Returns the party type for the individual customer.
   *
   * @return the party type for the individual customer
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public PartyType getPartyType() {
    return super.getPartyType();
  }

  /**
   * Retrieve the first physical address with the specified purpose for the individual customer.
   *
   * @param purpose the physical address purpose
   * @return the first physical address with the specified purpose for the individual customer or
   *     <b>null</b> if the physical address could not be found
   */
  public PhysicalAddress getPhysicalAddress(PhysicalAddressPurpose purpose) {
    return physicalAddresses.stream()
        .filter(physicalAddress -> physicalAddress.getPurposes().contains(purpose))
        .findFirst()
        .get();
  }

  /**
   * Returns the physical addresses for the individual customer.
   *
   * @return the physical addresses for the individual customer
   */
  @Schema(description = "The physical addresses for the individual customer")
  @JsonProperty
  @JsonManagedReference("physicalAddressReference")
  @XmlElementWrapper(name = "PhysicalAddresses")
  @XmlElement(name = "PhysicalAddress")
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return physicalAddresses;
  }

  /**
   * Returns the preferences for the individual customer.
   *
   * @return the preferences for the individual customer
   */
  @Schema(description = "The preferences for the individual customer")
  @JsonProperty
  @JsonManagedReference("preferenceReference")
  @XmlElementWrapper(name = "Preferences")
  @XmlElement(name = "Preference")
  public Set<Preference> getPreferences() {
    return preferences;
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
  @JsonProperty
  @XmlElement(name = "PreferredName")
  public String getPreferredName() {
    return preferredName;
  }

  /**
   * The code for the race for the individual customer.
   *
   * @return the code for the race for the individual customer
   */
  @Schema(description = "The code for the race for the individual customer", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Race", required = true)
  public String getRace() {
    return race;
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
  @XmlElement(name = "ResidencyStatus", required = true)
  public String getResidencyStatus() {
    return residencyStatus;
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
  @XmlElement(name = "ResidentialType", required = true)
  public String getResidentialType() {
    return residentialType;
  }

  /**
   * Retrieve the role with the specified type for the individual customer independent of a party
   * association.
   *
   * @param type the code for the party role type
   * @return the role with the specified type for the individual customer independent of a party
   *     association or <b>null</b> if the role could not be found
   */
  public PartyRole getRole(String type) {
    return roles.stream().filter(role -> Objects.equals(role.getType(), type)).findFirst().get();
  }

  /**
   * Returns the party roles for the individual customer independent of a party association.
   *
   * @return the party roles for the individual customer independent of a party association
   */
  @Schema(
      description =
          "The party roles for the individual customer independent of a party association")
  @JsonProperty
  @JsonManagedReference("partyRoleReference")
  @XmlElementWrapper(name = "Roles")
  @XmlElement(name = "Role")
  public Set<PartyRole> getRoles() {
    return roles;
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
  @XmlElement(name = "Surname", required = true)
  public String getSurname() {
    return surname;
  }

  /**
   * Retrieve the tax number with the specified type for the individual customer.
   *
   * @param type the code for the tax number type
   * @return the tax number with the specified type for the individual customer or <b>null</b> if
   *     the tax number could not be found
   */
  public TaxNumber getTaxNumber(String type) {
    return taxNumbers.stream()
        .filter(taxNumber -> Objects.equals(taxNumber.getType(), type))
        .findFirst()
        .get();
  }

  /**
   * Returns the tax numbers for the individual customer.
   *
   * @return the tax numbers for the individual customer
   */
  @Schema(description = "The tax numbers for the individual customer")
  @JsonProperty
  @JsonManagedReference("taxNumberReference")
  @XmlElementWrapper(name = "TaxNumbers")
  @XmlElement(name = "TaxNumber")
  public Set<TaxNumber> getTaxNumbers() {
    return taxNumbers;
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
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
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
  @XmlElement(name = "Title", required = true)
  public String getTitle() {
    return title;
  }

  /**
   * Returns the date and time the individual customer was last updated.
   *
   * @return the date and time the individual customer was last updated
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
   * Remove the contact mechanism with the specified type and purpose for the individual customer.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   */
  public void removeContactMechanism(String type, String purpose) {
    contactMechanisms.removeIf(
        existingContactMechanism ->
            Objects.equals(existingContactMechanism.getType(), type)
                && Objects.equals(existingContactMechanism.getPurpose(), purpose));
  }

  /**
   * Remove the identity document with the specified type for the individual customer.
   *
   * @param type the code for the identity document type
   */
  public void removeIdentityDocumentByType(String type) {
    identityDocuments.removeIf(
        existingIdentityDocument -> Objects.equals(existingIdentityDocument.getType(), type));
  }
  //

  /**
   * Remove any physical addresses with the specified purpose for the individual customer.
   *
   * @param purpose the physical address purpose
   */
  public void removePhysicalAddress(PhysicalAddressPurpose purpose) {
    physicalAddresses.removeIf(
        existingPhysicalAddress -> existingPhysicalAddress.getPurposes().contains(purpose));
  }

  /**
   * Remove the preference with the specified type for the individual customer.
   *
   * @param type the code for the preference type
   */
  public void removePreference(String type) {
    preferences.removeIf(existingPreference -> Objects.equals(existingPreference.getType(), type));
  }

  /**
   * Remove the role with the specified type for the individual customer.
   *
   * @param type the code for party role type
   */
  public void removeRole(String type) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), type));
  }

  /**
   * Remove the tax number with the specified type for the individual customer.
   *
   * @param type the tax number type
   */
  public void removeTaxNumber(String type) {
    taxNumbers.removeIf(existingTaxNumber -> Objects.equals(existingTaxNumber.getType(), type));
  }

  /**
   * Set the contact mechanisms for the individual customer.
   *
   * @param contactMechanisms the contact mechanisms for the individual customer
   */
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    this.contactMechanisms.clear();
    this.contactMechanisms.addAll(contactMechanisms);
  }

  /**
   * Set the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the individual
   * customer.
   *
   * @param countriesOfTaxResidence the optional ISO 3166-1 alpha-2 codes for the countries of tax
   *     residence for the individual customer
   */
  public void setCountriesOfTaxResidence(Set<String> countriesOfTaxResidence) {
    this.countriesOfTaxResidence =
        StringUtils.collectionToDelimitedString(countriesOfTaxResidence, ",");
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country of birth for the individual customer.
   *
   * @param countryOfBirth the ISO 3166-1 alpha-2 code for the country of birth for the individual
   *     customer
   */
  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country of residence for the individual customer.
   *
   * @param countryOfResidence the ISO 3166-1 alpha-2 code for the country of residence for the
   *     individual customer
   */
  public void setCountryOfResidence(String countryOfResidence) {
    this.countryOfResidence = countryOfResidence;
  }

  /**
   * Set the optional code for the single country of tax residence for the individual customer.
   *
   * @param countryOfTaxResidence the optional code for the single country of tax residence for the
   *     individual customer
   */
  @JsonIgnore
  public void setCountryOfTaxResidence(String countryOfTaxResidence) {
    this.countriesOfTaxResidence = countryOfTaxResidence;
  }

  /**
   * Set the optional date of birth for the individual customer.
   *
   * @param dateOfBirth the optional date of birth for the individual customer
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Set the optional date of death for the individual customer.
   *
   * @param dateOfDeath the optional date of death for the individual customer
   */
  public void setDateOfDeath(LocalDate dateOfDeath) {
    this.dateOfDeath = dateOfDeath;
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
   * Set the optional code for the employment status for the individual customer.
   *
   * @param employmentStatus the optional code for the employment status for the individual customer
   */
  public void setEmploymentStatus(String employmentStatus) {
    this.employmentStatus = employmentStatus;
  }

  /**
   * Set the optional code for the employment type for the individual customer.
   *
   * @param employmentType the optional code for the employment type for the individual customer
   */
  public void setEmploymentType(String employmentType) {
    this.employmentType = employmentType;
  }

  /**
   * Set the optional code for the gender for the individual customer.
   *
   * @param gender the optional code for the gender for the individual customer
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Set the optional given name, firstname, forename, or Christian name for the individual
   * customer.
   *
   * @param givenName the optional given name, firstname, forename, or Christian name for the
   *     individual customer
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   * Set the optional code for the home language for the individual customer.
   *
   * @param homeLanguage the optional code for the home language for the individual customer
   */
  public void setHomeLanguage(String homeLanguage) {
    this.homeLanguage = homeLanguage;
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
  public void setIdentityDocuments(Set<IdentityDocument> identityDocuments) {
    this.identityDocuments.clear();
    this.identityDocuments.addAll(identityDocuments);
  }

  /**
   * Set the optional initials for the individual customer.
   *
   * @param initials the optional initials for the individual customer
   */
  public void setInitials(String initials) {
    this.initials = initials;
  }

  /**
   * Set the optional maiden name for the individual customer.
   *
   * @param maidenName the optional maiden name for the individual customer
   */
  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }

  /**
   * Set the optional code for the marital status for the individual customer.
   *
   * @param maritalStatus the optional code for the marital status for the individual customer
   */
  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  /**
   * Set the optional code for the marriage type for the individual customer if the individual
   * customer is married.
   *
   * @param marriageType the optional code for the marriage type for the individual customer if the
   *     individual customer is married
   */
  public void setMarriageType(String marriageType) {
    this.marriageType = marriageType;
  }

  /**
   * Set the optional middle names for the individual customer.
   *
   * @param middleNames the optional middle names for the individual customer
   */
  public void setMiddleNames(String middleNames) {
    this.middleNames = middleNames;
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
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the optional code for the occupation for the individual customer.
   *
   * @param occupation the optional code for the occupation for the individual customer
   */
  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  /**
   * Set the physical addresses for the individual customer.
   *
   * @param physicalAddresses the physical addresses for the individual customer
   */
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    this.physicalAddresses.clear();
    this.physicalAddresses.addAll(physicalAddresses);
  }

  /**
   * Set the preferences for the individual customer.
   *
   * @param preferences the preferences for the individual customer
   */
  public void setPreferences(Set<Preference> preferences) {
    this.preferences.clear();
    this.preferences.addAll(preferences);
  }

  /**
   * Set the optional preferred name for the individual customer.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @param preferredName the optional preferred name for the individual customer
   */
  public void setPreferredName(String preferredName) {
    this.preferredName = preferredName;
  }

  /**
   * Set the optional code for the race for the individual customer.
   *
   * @param race the optional code for the race for the individual customer
   */
  public void setRace(String race) {
    this.race = race;
  }

  /**
   * Set the optional code for the residency status for the individual customer.
   *
   * @param residencyStatus the optional code for the residency status for the individual customer
   */
  public void setResidencyStatus(String residencyStatus) {
    this.residencyStatus = residencyStatus;
  }

  /**
   * Set the optional code for the residential type for the individual customer.
   *
   * @param residentialType the optional code for the residential type for the individual customer
   */
  public void setResidentialType(String residentialType) {
    this.residentialType = residentialType;
  }

  /**
   * Set the party roles for the individual customer independent of a party association.
   *
   * @param roles the party roles for the individual customer
   */
  public void setRoles(Set<PartyRole> roles) {
    this.roles.clear();
    this.roles.addAll(roles);
  }

  /**
   * Set the optional surname, last name, or family name for the individual customer.
   *
   * @param surname the optional surname, last name, or family name for the individual customer
   */
  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   * Set the tax numbers for the individual customer.
   *
   * @param taxNumbers the tax numbers for the individual customer
   */
  public void setTaxNumbers(Set<TaxNumber> taxNumbers) {
    this.taxNumbers.clear();
    this.taxNumbers.addAll(taxNumbers);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the individual customer is
   * associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the individual customer
   *     is associated with
   */
  public void setTenantId(UUID tenantId) {
    super.setTenantId(tenantId);
  }

  /**
   * Set the optional code for the title for the individual customer.
   *
   * @param title the optional code for the title for the individual customer
   */
  public void setTitle(String title) {
    this.title = title;
  }
}

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
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.LocalDateAdapter;
import digital.inception.party.constraints.ValidPerson;
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
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.util.StringUtils;

/**
 * The <b>Person</b> class holds the information for a person.
 *
 * <p>This entity may be used under different circumstances when more or less information is
 * available for a person. As a result, most of the attributes are optional and the particular
 * application making use of this entity should enforce which attributes are mandatory.
 *
 * <p>This class exposes the JSON and XML properties using a property-based approach rather than a
 * field-based approach to support the JPA inheritance model.
 *
 * <p>The following steps must be completed when adding a new attribute to the person entity:
 *
 * <ol>
 *   <li>Add a new column for the new attribute to the definition of the <b>party.persons</b> table
 *       in the <b>inception-party-h2.sql</b> file.
 *   <li>Add a description for the new column for the new attribute to the
 *       <b>inception-party-h2.sql</b> file.
 *   <li>Add a new property for the new attribute to the <b>Person</b> class.
 *   <li>Add the appropriate validation for the new attribute to the <b>ValidPersonValidator</b>
 *       class.
 *   <li>Add a new column for the new attribute to the <b>inception-party.changelog.xml</b> file.
 *   <li>Add the name of the attribute to the <b>Attribute.RESERVED_ATTRIBUTE_TYPE_CODES</b> array.
 *   <li>Add support for applying validations described by <b>RoleTypeAttributeConstraint</b>s to
 *       the <b>ValidPersonValidator</b>.
 * </ol>
 *
 * @author Marcus Portmann
 */
@Schema(description = "A person; any member of the species homo sapiens")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"created", "type", "updated"})
@JsonPropertyOrder({
  "id",
  "tenantId",
  "name",
  "countryOfBirth",
  "countryOfResidence",
  "dateOfBirth",
  "dateOfDeath",
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
  "attributes",
  "contactMechanisms",
  "identityDocuments",
  "physicalAddresses",
  "preferences",
  "residencePermits",
  "roles",
  "countriesOfTaxResidence",
  "taxNumbers"
})
@XmlRootElement(name = "Person", namespace = "http://inception.digital/party")
@XmlType(
    name = "Person",
    namespace = "http://inception.digital/party",
    propOrder = {
      "id",
      "tenantId",
      "name",
      "countryOfBirth",
      "countryOfResidence",
      "dateOfBirth",
      "dateOfDeath",
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
      "attributes",
      "contactMechanisms",
      "identityDocuments",
      "physicalAddresses",
      "preferences",
      "residencePermits",
      "roles",
      "countriesOfTaxResidence",
      "taxNumbers"
    })
@XmlAccessorType(XmlAccessType.PROPERTY)
@ValidPerson
@Entity
@Table(schema = "party", name = "persons")
public class Person extends PartyBase implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The attributes for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Attribute> attributes = new HashSet<>();

  /** The contact mechanisms for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<ContactMechanism> contactMechanisms = new HashSet<>();

  /** The identity documents for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<IdentityDocument> identityDocuments = new HashSet<>();

  /** The physical addresses for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PhysicalAddress> physicalAddresses = new HashSet<>();

  /** The preferences for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Preference> preferences = new HashSet<>();

  /** The residence permits for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<ResidencePermit> residencePermits = new HashSet<>();

  /** The roles assigned directly to the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Role> roles = new HashSet<>();

  /** The tax numbers for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<TaxNumber> taxNumbers = new HashSet<>();

  /**
   * The optional comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for
   * the person.
   */
  @JsonIgnore
  @XmlTransient
  @Size(max = 100)
  @Column(table = "persons", name = "countries_of_tax_residence", length = 100)
  private String countriesOfTaxResidence;

  /** The optional ISO 3166-1 alpha-2 code for the country of birth for the person. */
  @Size(min = 2, max = 2)
  @Column(table = "persons", name = "country_of_birth", length = 2)
  private String countryOfBirth;

  /** The optional ISO 3166-1 alpha-2 code for the country of residence for the person. */
  @Size(min = 2, max = 2)
  @Column(table = "persons", name = "country_of_residence", length = 2)
  private String countryOfResidence;

  /** The optional date of birth for the person. */
  @Column(table = "persons", name = "date_of_birth")
  private LocalDate dateOfBirth;

  /** The optional date of death for the person. */
  @Column(table = "persons", name = "date_of_death")
  private LocalDate dateOfDeath;

  /** The optional code for the employment status for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "employment_status", length = 30)
  private String employmentStatus;

  /** The optional code for the employment type for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "employment_type", length = 30)
  private String employmentType;

  /** The optional code for the gender for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "gender", length = 30)
  private String gender;

  /** The optional given name, firstname, forename, or Christian name for the person. */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "given_name", length = 100)
  private String givenName;

  /** The optional code for the home language for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "home_language", length = 30)
  private String homeLanguage;

  /** The optional initials for the person. */
  @Size(min = 1, max = 20)
  @Column(table = "persons", name = "initials", length = 20)
  private String initials;

  /** The optional maiden name for the person. */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "maiden_name", length = 100)
  private String maidenName;

  /** The optional code for the marital status for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "marital_status", length = 30)
  private String maritalStatus;

  /** The optional code for the marriage type for the person if the person is married. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "marriage_type", length = 30)
  private String marriageType;

  /** The optional middle names for the person. */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "middle_names", length = 100)
  private String middleNames;

  /** The optional code for the occupation for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "occupation", length = 30)
  private String occupation;

  /**
   * The optional preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "preferred_name", length = 100)
  private String preferredName;

  /** The optional code for the race for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "race", length = 30)
  private String race;

  /** The optional code for the residency status for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "residency_status", length = 30)
  private String residencyStatus;

  /** The optional code for the residential type for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "residential_type", length = 30)
  private String residentialType;

  /** The optional surname, last name, or family name for the person. */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "surname", length = 100)
  private String surname;

  /** The optional code for the title for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "title", length = 30)
  private String title;

  /** Constructs a new <b>Person</b>. */
  public Person() {
    super(PartyType.PERSON);
  }

  /**
   * Constructs a new <b>Person</b>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the person is
   *     associated with
   * @param name the name of the person
   */
  public Person(UUID tenantId, String name) {
    super(UuidCreator.getShortPrefixComb(), tenantId, PartyType.PERSON, name);
  }

  /**
   * Add the attribute for the person.
   *
   * @param attribute the attribute
   */
  public void addAttribute(Attribute attribute) {
    attributes.removeIf(
        existingAttribute -> Objects.equals(existingAttribute.getType(), attribute.getType()));

    attribute.setParty(this);

    attributes.add(attribute);
  }

  /**
   * Add the contact mechanism for the person.
   *
   * @param contactMechanism the contact mechanism
   */
  public void addContactMechanism(ContactMechanism contactMechanism) {
    contactMechanisms.removeIf(
        existingContactMechanism ->
            Objects.equals(existingContactMechanism.getType(), contactMechanism.getType())
                && Objects.equals(
                    existingContactMechanism.getRole(), contactMechanism.getRole()));

    contactMechanism.setParty(this);

    contactMechanisms.add(contactMechanism);
  }

  /**
   * Add the identity document for the person.
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
   * Add the physical address for the person.
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
   * Add the preference for the person.
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
   * Add the residence permit for the person.
   *
   * @param residencePermit the residence permit
   */
  public void addResidencePermit(ResidencePermit residencePermit) {
    residencePermit.setParty(this);

    residencePermits.add(residencePermit);
  }

  /**
   * Add the role to the person independent of a party association.
   *
   * @param role the role
   */
  public void addRole(Role role) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), role.getType()));

    role.setParty(this);

    roles.add(role);
  }

  /**
   * Add the tax number for the person.
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

    Person other = (Person) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Retrieve the attribute with the specified type for the person.
   *
   * @param type the code for the attribute type
   * @return the attribute with the specified type for the person or <b>null</b> if the attribute
   *     could not be found
   */
  public Attribute getAttribute(String type) {
    return attributes.stream()
        .filter(attribute -> Objects.equals(attribute.getType(), type))
        .findFirst()
        .get();
  }

  /**
   * Returns the attributes for the person.
   *
   * @return the attributes for the person
   */
  @Schema(description = "The attributes for the person")
  @JsonProperty
  @JsonManagedReference("attributeReference")
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  public Set<Attribute> getAttributes() {
    return attributes;
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the person.
   *
   * @param type the code for the contact mechanism type
   * @param purpose the code for the contact mechanism role
   * @return the contact mechanism with the specified type and purpose for the person or <b>null</b>
   *     if the contact mechanism could not be found
   */
  public ContactMechanism getContactMechanism(String type, String purpose) {
    return contactMechanisms.stream()
        .filter(
            contactMechanism ->
                Objects.equals(contactMechanism.getType(), type)
                    && Objects.equals(contactMechanism.getRole(), purpose))
        .findFirst()
        .get();
  }

  /**
   * Returns the contact mechanisms for the person.
   *
   * @return the contact mechanisms for the person
   */
  @Schema(description = "The contact mechanisms for the person")
  @JsonProperty
  @JsonManagedReference("contactMechanismReference")
  @XmlElementWrapper(name = "ContactMechanisms")
  @XmlElement(name = "ContactMechanism")
  public Set<ContactMechanism> getContactMechanisms() {
    return contactMechanisms;
  }

  /**
   * Returns the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the
   * person.
   *
   * @return the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the person
   */
  @Schema(
      description =
          "The optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the person")
  @JsonProperty
  @XmlElementWrapper(name = "CountriesOfTaxResidence")
  @XmlElement(name = "CountryOfTaxResidence")
  public Set<String> getCountriesOfTaxResidence() {
    return Set.of(StringUtils.commaDelimitedListToStringArray(countriesOfTaxResidence));
  }

  /**
   * Returns the optional ISO 3166-1 alpha-2 code for the country of birth for the person.
   *
   * @return the optional ISO 3166-1 alpha-2 code for the country of birth for the person
   */
  @Schema(
      description = "The optional ISO 3166-1 alpha-2 code for the country of birth for the person")
  @JsonProperty
  @XmlElement(name = "CountryOfBirth")
  public String getCountryOfBirth() {
    return countryOfBirth;
  }

  /**
   * Returns the optional ISO 3166-1 alpha-2 code for the country of residence for the person.
   *
   * @return the optional ISO 3166-1 alpha-2 code for the country of residence for the person
   */
  @Schema(
      description =
          "The optional ISO 3166-1 alpha-2 code for the country of residence for the person")
  @JsonProperty
  @XmlElement(name = "CountryOfResidence")
  public String getCountryOfResidence() {
    return countryOfResidence;
  }

  /**
   * Returns the date and time the person was created.
   *
   * @return the date and time the person was created
   */
  @JsonIgnore
  @XmlTransient
  public LocalDateTime getCreated() {
    return super.getCreated();
  }

  /**
   * Returns the optional date of birth for the person.
   *
   * @return the optional date of birth for the person
   */
  @Schema(description = "The optional date of birth for the person")
  @JsonProperty
  @XmlElement(name = "DateOfBirth")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Returns the optional date of death for the person.
   *
   * @return the optional date of death for the person
   */
  @Schema(description = "The optional date of death for the person")
  @JsonProperty
  @XmlElement(name = "DateOfDeath")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  public LocalDate getDateOfDeath() {
    return dateOfDeath;
  }

  /**
   * Returns the optional code for the employment status for the person.
   *
   * @return the optional code for the employment status for the person
   */
  @Schema(description = "The optional code for the employment status for the person")
  @JsonProperty
  @XmlElement(name = "EmploymentStatus")
  public String getEmploymentStatus() {
    return employmentStatus;
  }

  /**
   * Returns the optional code for the employment type for the person.
   *
   * @return the optional code for the employment type for the person
   */
  @Schema(description = "The optional code for the employment type for the person")
  @JsonProperty
  @XmlElement(name = "EmploymentType")
  public String getEmploymentType() {
    return employmentType;
  }

  /**
   * Returns the optional code for the gender for the person.
   *
   * @return the optional code for the gender for the person
   */
  @Schema(description = "The optional code for the gender for the person")
  @JsonProperty
  @XmlElement(name = "Gender")
  public String getGender() {
    return gender;
  }

  /**
   * Returns the optional given name, firstname, forename, or Christian name for the person.
   *
   * @return the optional given name, firstname, forename, or Christian name for the person
   */
  @Schema(
      description =
          "The optional given name, firstname, forename, or Christian name for the person")
  @JsonProperty
  @XmlElement(name = "GivenName")
  public String getGivenName() {
    return givenName;
  }

  /**
   * Returns the optional code for the home language for the person.
   *
   * @return the optional code for the home language for the person
   */
  @Schema(description = "The optional code for the home language for the person")
  @JsonProperty
  @XmlElement(name = "HomeLanguage")
  public String getHomeLanguage() {
    return homeLanguage;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the person.
   *
   * @return the Universally Unique Identifier (UUID) for the person
   */
  @Schema(description = "The Universally Unique Identifier (UUID) for the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the identity documents for the person.
   *
   * @return the identity documents for the person
   */
  @Schema(description = "The identity documents for the person")
  @JsonProperty
  @JsonManagedReference("identityDocumentReference")
  @XmlElementWrapper(name = "IdentityDocuments")
  @XmlElement(name = "IdentityDocument")
  public Set<IdentityDocument> getIdentityDocuments() {
    return identityDocuments;
  }

  /**
   * Returns the optional initials for the person.
   *
   * @return the optional initials for the person
   */
  @Schema(description = "The optional initials for the person")
  @JsonProperty
  @XmlElement(name = "Initials")
  public String getInitials() {
    return initials;
  }

  /**
   * Returns the optional maiden name for the person.
   *
   * @return the optional maiden name for the person
   */
  @Schema(description = "The optional maiden name for the person")
  @JsonProperty
  @XmlElement(name = "MaidenName")
  public String getMaidenName() {
    return maidenName;
  }

  /**
   * Returns the optional code for the marital status for the person.
   *
   * @return the optional code for the marital status for the person
   */
  @Schema(description = "The optional code for the marital status for the person")
  @JsonProperty
  @XmlElement(name = "MaritalStatus")
  public String getMaritalStatus() {
    return maritalStatus;
  }

  /**
   * Returns the optional code for the marriage type for the person if the person is married.
   *
   * @return the optional code for the marriage type for the person if the person is married
   */
  @Schema(
      description =
          "The optional code for the marriage type for the person if the person is married")
  @JsonProperty
  @XmlElement(name = "MarriageType")
  public String getMarriageType() {
    return marriageType;
  }

  /**
   * Returns the optional middle names for the person.
   *
   * @return the optional middle names for the person
   */
  @Schema(description = "The optional middle names for the person")
  @JsonProperty
  @XmlElement(name = "MiddleNames")
  public String getMiddleNames() {
    return middleNames;
  }

  /**
   * Returns the personal name or full name of the person.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the person. This name should match the full name on the identity document(s)
   * associated with the person.
   *
   * <p>See https://en.wikipedia.org/wiki/Personal_name
   *
   * @return the personal name or full name of the person
   */
  @Schema(description = "The personal name or full name of the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the optional code for the occupation for the person.
   *
   * @return the optional code for the occupation for the person
   */
  @Schema(description = "The optional code for the occupation for the person")
  @JsonProperty
  @XmlElement(name = "Occupation")
  public String getOccupation() {
    return occupation;
  }

  /**
   * Retrieve the first physical address with the specified purpose for the person.
   *
   * @param purpose the physical address purpose
   * @return the first physical address with the specified purpose for the person or <b>null</b> if
   *     the physical address could not be found
   */
  public PhysicalAddress getPhysicalAddress(PhysicalAddressPurpose purpose) {
    return physicalAddresses.stream()
        .filter(physicalAddress -> physicalAddress.getPurposes().contains(purpose))
        .findFirst()
        .get();
  }

  /**
   * Returns the physical addresses for the person.
   *
   * @return the physical addresses for the person
   */
  @Schema(description = "The physical addresses for the person")
  @JsonProperty
  @JsonManagedReference("physicalAddressReference")
  @XmlElementWrapper(name = "PhysicalAddresses")
  @XmlElement(name = "PhysicalAddress")
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return physicalAddresses;
  }

  /**
   * Retrieve the preference with the specified type for the person.
   *
   * @param type the code for the preference type
   * @return the preference with the specified type for the person or <b>null</b> if the preference
   *     could not be found
   */
  public Preference getPreference(String type) {
    return preferences.stream()
        .filter(preference -> Objects.equals(preference.getType(), type))
        .findFirst()
        .get();
  }

  /**
   * Returns the preferences for the person.
   *
   * @return the preferences for the person
   */
  @Schema(description = "The preferences for the person")
  @JsonProperty
  @JsonManagedReference("preferenceReference")
  @XmlElementWrapper(name = "Preferences")
  @XmlElement(name = "Preference")
  public Set<Preference> getPreferences() {
    return preferences;
  }

  /**
   * Returns the optional preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @return the optional preferred name for the person
   */
  @Schema(description = "The optional preferred name for the person")
  @JsonProperty
  @XmlElement(name = "PreferredName")
  public String getPreferredName() {
    return preferredName;
  }

  /**
   * The optional code for the race for the person.
   *
   * @return the optional code for the race for the person
   */
  @Schema(description = "The optional code for the race for the person")
  @JsonProperty
  @XmlElement(name = "Race")
  public String getRace() {
    return race;
  }

  /**
   * Returns the residence permits for the person.
   *
   * @return the residence permits for the person
   */
  @Schema(description = "The residence permits for the person")
  @JsonProperty
  @JsonManagedReference("residencePermitReference")
  @XmlElementWrapper(name = "ResidencePermits")
  @XmlElement(name = "ResidencePermit")
  public Set<ResidencePermit> getResidencePermits() {
    return residencePermits;
  }

  /**
   * Returns the optional code for the residency status for the person.
   *
   * @return the optional code for the residency status for the person
   */
  @Schema(description = "The optional code for the residency status for the person")
  @JsonProperty
  @XmlElement(name = "ResidencyStatus")
  public String getResidencyStatus() {
    return residencyStatus;
  }

  /**
   * Returns the optional code for the residential type for the person.
   *
   * @return the optional code for the residential type for the person
   */
  @Schema(description = "The optional code for the residential type for the person")
  @JsonProperty
  @XmlElement(name = "ResidentialType")
  public String getResidentialType() {
    return residentialType;
  }

  /**
   * Retrieve the role with the specified type for the person independent of a party association.
   *
   * @param type the code for the role type
   * @return the role with the specified type for the person independent of a party association or
   *     <b>null</b> if the role could not be found
   */
  public Role getRole(String type) {
    return roles.stream().filter(role -> Objects.equals(role.getType(), type)).findFirst().get();
  }

  /**
   * Returns the roles assigned directly to the person.
   *
   * @return the roles assigned directly to the person
   */
  @Schema(description = "The roles assigned directly to the person")
  @JsonProperty
  @JsonManagedReference("roleReference")
  @XmlElementWrapper(name = "Roles")
  @XmlElement(name = "Role")
  public Set<Role> getRoles() {
    return roles;
  }

  /**
   * Returns the optional surname, last name, or family name for the person.
   *
   * @return the optional surname, last name, or family name for the person
   */
  @Schema(description = "The optional surname, last name, or family name for the person")
  @JsonProperty
  @XmlElement(name = "Surname")
  public String getSurname() {
    return surname;
  }

  /**
   * Retrieve the tax number with the specified type for the person.
   *
   * @param type the code for the tax number type
   * @return the tax number with the specified type for the person or <b>null</b> if the tax number
   *     could not be found
   */
  public TaxNumber getTaxNumber(String type) {
    return taxNumbers.stream()
        .filter(taxNumber -> Objects.equals(taxNumber.getType(), type))
        .findFirst()
        .get();
  }

  /**
   * Returns the tax numbers for the person.
   *
   * @return the tax numbers for the person
   */
  @Schema(description = "The tax numbers for the person")
  @JsonProperty
  @JsonManagedReference("taxNumberReference")
  @XmlElementWrapper(name = "TaxNumbers")
  @XmlElement(name = "TaxNumber")
  public Set<TaxNumber> getTaxNumbers() {
    return taxNumbers;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the person is associated with.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the person is associated with
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the tenant the person is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  public UUID getTenantId() {
    return super.getTenantId();
  }

  /**
   * Returns the optional code for the title for the person.
   *
   * @return the optional code for the title for the person
   */
  @Schema(description = "The optional code for the title for the person")
  @JsonProperty
  @XmlElement(name = "Title")
  public String getTitle() {
    return title;
  }

  /**
   * Returns the party type for the person.
   *
   * @return the party type for the person
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public PartyType getType() {
    return super.getType();
  }

  /**
   * Returns the date and time the person was last updated.
   *
   * @return the date and time the person was last updated
   */
  @JsonIgnore
  @XmlTransient
  public LocalDateTime getUpdated() {
    return super.getUpdated();
  }

  /**
   * Returns whether the person has a contact mechanism with the specified type.
   *
   * @param type the code for the contact mechanism type
   * @return <b>true</b>> if the person has a contact mechanism with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasContactMechanismType(String type) {
    return contactMechanisms.stream()
        .anyMatch(contactMechanism -> Objects.equals(contactMechanism.getType(), type));
  }

  /**
   * Returns whether the person has a physical address with the specified role.
   *
   * @param role the code for the physical address role
   * @return <b>true</b>> if the person has a physical address with the specified role or
   *     <b>false</b> otherwise
   */
  public boolean hasPhysicalAddressRole(String role) {
    return physicalAddresses.stream()
        .anyMatch(physicalAddress -> Objects.equals(physicalAddress.getRole(), role));
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
   * Remove the attribute with the specified type for the person.
   *
   * @param type the code for the attribute type
   */
  public void removeAttribute(String type) {
    attributes.removeIf(existingAttribute -> Objects.equals(existingAttribute.getType(), type));
  }
  //

  /**
   * Remove the contact mechanism with the specified type and purpose for the person.
   *
   * @param type the code for the contact mechanism type
   * @param purpose the code for the contact mechanism role
   */
  public void removeContactMechanism(String type, String purpose) {
    contactMechanisms.removeIf(
        existingContactMechanism ->
            Objects.equals(existingContactMechanism.getType(), type)
                && Objects.equals(existingContactMechanism.getRole(), purpose));
  }

  /**
   * Remove the identity document with the specified type for the person.
   *
   * @param type the code for the identity document type
   */
  public void removeIdentityDocumentByType(String type) {
    identityDocuments.removeIf(
        existingIdentityDocument -> Objects.equals(existingIdentityDocument.getType(), type));
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
   * Remove the preference with the specified type for the person.
   *
   * @param type the code for the preference type
   */
  public void removePreference(String type) {
    preferences.removeIf(existingPreference -> Objects.equals(existingPreference.getType(), type));
  }

  /**
   * Remove the residence permit with the specified type for the person.
   *
   * @param type the code for the residence permit type
   */
  public void removeResidencePermitByType(String type) {
    residencePermits.removeIf(
        existingResidencePermit -> Objects.equals(existingResidencePermit.getType(), type));
  }

  /**
   * Remove the role with the specified type for the person.
   *
   * @param type the code for role type
   */
  public void removeRole(String type) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), type));
  }

  /**
   * Remove the tax number with the specified type for the person.
   *
   * @param type the tax number type
   */
  public void removeTaxNumber(String type) {
    taxNumbers.removeIf(existingTaxNumber -> Objects.equals(existingTaxNumber.getType(), type));
  }

  /**
   * Set the attributes for the person.
   *
   * @param attributes the attributes for the person
   */
  public void setAttributes(Set<Attribute> attributes) {
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the contact mechanisms for the person.
   *
   * @param contactMechanisms the contact mechanisms for the person
   */
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    this.contactMechanisms.clear();
    this.contactMechanisms.addAll(contactMechanisms);
  }

  /**
   * Set the optional ISO 3166-1 alpha-2 codes for the countries of tax residence for the person.
   *
   * @param countriesOfTaxResidence the optional ISO 3166-1 alpha-2 codes for the countries of tax
   *     residence for the person
   */
  public void setCountriesOfTaxResidence(Set<String> countriesOfTaxResidence) {
    this.countriesOfTaxResidence =
        StringUtils.collectionToDelimitedString(countriesOfTaxResidence, ",");
  }

  /**
   * Set the optional ISO 3166-1 alpha-2 code for the country of birth for the person.
   *
   * @param countryOfBirth the optional ISO 3166-1 alpha-2 code for the country of birth for the
   *     person
   */
  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  /**
   * Set the optional ISO 3166-1 alpha-2 code for the country of residence for the person.
   *
   * @param countryOfResidence the optional ISO 3166-1 alpha-2 code for the country of residence for
   *     the person
   */
  public void setCountryOfResidence(String countryOfResidence) {
    this.countryOfResidence = countryOfResidence;
  }

  /**
   * Set the code for the single country of tax residence for the person.
   *
   * @param countryOfTaxResidence the code for the single country of tax residence for the person
   */
  @JsonIgnore
  public void setCountryOfTaxResidence(String countryOfTaxResidence) {
    this.countriesOfTaxResidence = countryOfTaxResidence;
  }

  /**
   * Set the optional date of birth for the person.
   *
   * @param dateOfBirth the optional date of birth for the person
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Set the optional date of death for the person.
   *
   * @param dateOfDeath the optional date of death for the person
   */
  public void setDateOfDeath(LocalDate dateOfDeath) {
    this.dateOfDeath = dateOfDeath;
  }

  /**
   * Set the optional code for the employment status for the person.
   *
   * @param employmentStatus the optional code for the employment status for the person
   */
  public void setEmploymentStatus(String employmentStatus) {
    this.employmentStatus = employmentStatus;
  }

  /**
   * Set the optional code for the employment type for the person.
   *
   * @param employmentType the optional code for the employment type for the person
   */
  public void setEmploymentType(String employmentType) {
    this.employmentType = employmentType;
  }

  /**
   * Set the optional code for the gender for the person.
   *
   * @param gender the optional code for the gender for the person
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Set the optional given name, firstname, forename, or Christian name for the person.
   *
   * @param givenName the optional given name, firstname, forename, or Christian name for the person
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;

    deriveName();
  }

  /**
   * Set the optional code for the home language for the person.
   *
   * @param homeLanguage the optional code for the home language for the person
   */
  public void setHomeLanguage(String homeLanguage) {
    this.homeLanguage = homeLanguage;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the person.
   *
   * @param id the Universally Unique Identifier (UUID) for the person
   */
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the identity documents for the person.
   *
   * @param identityDocuments the identity documents for the person
   */
  public void setIdentityDocuments(Set<IdentityDocument> identityDocuments) {
    this.identityDocuments.clear();
    this.identityDocuments.addAll(identityDocuments);
  }

  /**
   * Set the optional initials for the person.
   *
   * @param initials the optional initials for the person
   */
  public void setInitials(String initials) {
    this.initials = initials;
  }

  /**
   * Set the optional maiden name for the person.
   *
   * @param maidenName the optional maiden name for the person
   */
  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }

  /**
   * Set the optional code for the marital status for the person.
   *
   * @param maritalStatus the optional code for the marital status for the person
   */
  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  /**
   * Set the optional code for the marriage type for the person if the person is married.
   *
   * @param marriageType the optional code for the marriage type for the person if the person is
   *     married
   */
  public void setMarriageType(String marriageType) {
    this.marriageType = marriageType;
  }

  /**
   * Set the optional middle names for the person.
   *
   * @param middleNames the optional middle names for the person
   */
  public void setMiddleNames(String middleNames) {
    this.middleNames = middleNames;

    deriveName();
  }

  /**
   * Set the personal name or full name of the person.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the person. This name should match the full name on the identity document(s)
   * associated with the person.
   *
   * <p>See https://en.wikipedia.org/wiki/Personal_name
   *
   * @param name the personal name or full name of the person
   */
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the optional code for the occupation for the person.
   *
   * @param occupation the optional code for the occupation for the person
   */
  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  /**
   * Set the physical addresses for the person.
   *
   * @param physicalAddresses the physical addresses for the person
   */
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    this.physicalAddresses.clear();
    this.physicalAddresses.addAll(physicalAddresses);
  }

  /**
   * Set the preferences for the person.
   *
   * @param preferences the preferences for the person
   */
  public void setPreferences(Set<Preference> preferences) {
    this.preferences.clear();
    this.preferences.addAll(preferences);
  }

  /**
   * Set the optional preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @param preferredName the optional preferred name for the person
   */
  public void setPreferredName(String preferredName) {
    this.preferredName = preferredName;
  }

  /**
   * Set the optional code for the race for the person.
   *
   * @param race the optional code for the race for the person
   */
  public void setRace(String race) {
    this.race = race;
  }

  /**
   * Set the residence permits for the person.
   *
   * @param residencePermits the residence permits for the person
   */
  public void setResidencePermits(Set<ResidencePermit> residencePermits) {
    this.residencePermits.clear();
    this.residencePermits.addAll(residencePermits);
  }

  /**
   * Set the optional code for the residency status for the person.
   *
   * @param residencyStatus the optional code for the residency status for the person
   */
  public void setResidencyStatus(String residencyStatus) {
    this.residencyStatus = residencyStatus;
  }

  /**
   * Set the optional code for the residential type for the person.
   *
   * @param residentialType the optional code for the residential type for the person
   */
  public void setResidentialType(String residentialType) {
    this.residentialType = residentialType;
  }

  /**
   * Set the roles assigned directly to the person.
   *
   * @param roles the roles assigned directly to the person
   */
  public void setRoles(Set<Role> roles) {
    this.roles.clear();
    this.roles.addAll(roles);
  }

  /**
   * Set the optional surname, last name, or family name for the person.
   *
   * @param surname the optional surname, last name, or family name for the person
   */
  public void setSurname(String surname) {
    this.surname = surname;

    deriveName();
  }

  /**
   * Set the tax numbers for the person.
   *
   * @param taxNumbers the tax numbers for the person
   */
  public void setTaxNumbers(Set<TaxNumber> taxNumbers) {
    this.taxNumbers.clear();
    this.taxNumbers.addAll(taxNumbers);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the person is associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the person is
   *     associated with
   */
  public void setTenantId(UUID tenantId) {
    super.setTenantId(tenantId);
  }

  /**
   * Set the optional code for the title for the person.
   *
   * @param title the optional code for the title for the person
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /** Derive the name of the person from the given name, middle name(s) and surname. */
  private void deriveName() {
    StringBuffer derivedName = new StringBuffer();

    if (StringUtils.hasText(givenName)) {
      derivedName.append(givenName.trim());
    }

    if (StringUtils.hasText(middleNames)) {
      if (derivedName.length() > 0) {
        derivedName.append(" " + middleNames.trim());
      } else {
        derivedName.append(middleNames.trim());
      }
    }

    if (StringUtils.hasText(surname)) {
      if (derivedName.length() > 0) {
        derivedName.append(" " + surname.trim());
      } else {
        derivedName.append(surname.trim());
      }
    }

    if (derivedName.length() > 0) {
      setName(derivedName.toString());
    }
  }
}

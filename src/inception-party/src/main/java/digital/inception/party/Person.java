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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Person</code> class holds the information for a person.
 *
 * <p>This entity may be used under different circumstances when more or less information is
 * available for a person. As a result, most of the attributes are optional and the particular
 * application making use of this entity should enforce which attributes are mandatory.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A person; any member of the species homo sapiens")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"type"})
@JsonPropertyOrder({
  "correspondenceLanguage",
  "countryOfBirth",
  "countryOfResidence",
  "dateOfBirth",
  "dateOfDeath",
  "employmentStatus",
  "employmentType",
  "gender",
  "givenName",
  "homeLanguage",
  "id",
  "initials",
  "maidenName",
  "maritalStatus",
  "marriageType",
  "middleNames",
  "name",
  "preferredName",
  "race",
  "residencyStatus",
  "surname",
  "title",
  "contactMechanisms",
  "identityDocuments",
  "physicalAddresses"
})
@XmlRootElement(name = "Person", namespace = "http://party.inception.digital")
@XmlType(
    name = "Person",
    namespace = "http://party.inception.digital",
    propOrder = {
      "correspondenceLanguage",
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
      "preferredName",
      "race",
      "residencyStatus",
      "surname",
      "title",
      "identityDocuments"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@DiscriminatorValue("2")
@Table(schema = "party", name = "persons")
public class Person extends Party implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The identity documents for the person. */
  @Schema(description = "The identity documents for the person")
  @JsonProperty
  @JsonManagedReference
  @XmlElementWrapper(name = "IdentityDocuments")
  @XmlElement(name = "IdentityDocument")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(
      name = "person_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false,
      nullable = false)
  private final Set<IdentityDocument> identityDocuments = new HashSet<>();

  /** The optional code identifying the correspondence language for the person. */
  @Schema(description = "The optional code identifying the correspondence language for the person")
  @JsonProperty
  @XmlElement(name = "CorrespondenceLanguage")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "correspondence_language")
  private String correspondenceLanguage;

  /** The optional code identifying the country of birth for the person. */
  @Schema(description = "The optional code identifying the country of birth for the person")
  @JsonProperty
  @XmlElement(name = "CountryOfBirth")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "country_of_birth", length = 10)
  private String countryOfBirth;

  /** The optional code identifying the country of residence for the person. */
  @Schema(description = "The optional code identifying the country of residence for the person")
  @JsonProperty
  @XmlElement(name = "CountryOfResidence")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "country_of_residence", length = 10)
  private String countryOfResidence;

  /** The optional date of birth for the person. */
  @Schema(description = "The optional date of birth for the person")
  @JsonProperty
  @XmlElement(name = "DateOfBirth")
  @Column(table = "persons", name = "date_of_birth")
  private LocalDate dateOfBirth;

  /** The optional date of death for the person. */
  @Schema(description = "The optional date of death for the person")
  @JsonProperty
  @XmlElement(name = "DateOfDeath")
  @Column(table = "persons", name = "date_of_death")
  private LocalDate dateOfDeath;

  /** The optional code identifying the employment status for the person. */
  @Schema(description = "The optional code identifying the employment status for the person")
  @JsonProperty
  @XmlElement(name = "EmploymentStatus")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "employment_status")
  private String employmentStatus;

  /** The optional code identifying the employment type for the person. */
  @Schema(description = "The optional code identifying the employment type for the person")
  @JsonProperty
  @XmlElement(name = "EmploymentType")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "employment_type")
  private String employmentType;

  /** The optional code identifying the gender for the person. */
  @Schema(description = "The optional code identifying the gender for the person")
  @JsonProperty
  @XmlElement(name = "Gender")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "gender")
  private String gender;

  /** The optional given name, firstname, forename, or Christian name for the person. */
  @Schema(
      description =
          "The optional given name, firstname, forename, or Christian name for the person")
  @JsonProperty
  @XmlElement(name = "GivenName")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "given_name", length = 100)
  private String givenName;

  /** The optional code identifying the home language for the person. */
  @Schema(description = "The optional code identifying the home language for the person")
  @JsonProperty
  @XmlElement(name = "HomeLanguage")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "home_language")
  private String homeLanguage;

  /** The optional initials for the person. */
  @Schema(description = "The optional initials for the person")
  @JsonProperty
  @XmlElement(name = "Initials")
  @Size(min = 1, max = 20)
  @Column(table = "persons", name = "initials", length = 20)
  private String initials;

  /** The optional maiden name for the person. */
  @Schema(description = "The optional maiden name for the person")
  @JsonProperty
  @XmlElement(name = "MaidenName")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "maiden_name", length = 100)
  private String maidenName;

  /** The optional code identifying the marital status for the person. */
  @Schema(description = "The optional code identifying the marital status for the person")
  @JsonProperty
  @XmlElement(name = "MaritalStatus")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "marital_status")
  private String maritalStatus;

  /** The optional code identifying the marriage type for the person if the person is married. */
  @Schema(
      description =
          "The optional code identifying the marriage type for the person if the person is married")
  @JsonProperty
  @XmlElement(name = "MarriageType")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "marriage_type")
  private String marriageType;

  /** The optional middle names for the person. */
  @Schema(description = "The optional middle names for the person")
  @JsonProperty
  @XmlElement(name = "MiddleNames")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "middle_names", length = 100)
  private String middleNames;

  /**
   * The optional preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   */
  @Schema(description = "The optional preferred name for the person")
  @JsonProperty
  @XmlElement(name = "PreferredName")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "preferred_name", length = 100)
  private String preferredName;

  /** The optional code identifying the race for the person. */
  @Schema(description = "The optional code identifying the race for the person")
  @JsonProperty
  @XmlElement(name = "Race")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "race")
  private String race;

  /** The optional code identifying the residency status for the person. */
  @Schema(description = "The optional code identifying the residency status for the person")
  @JsonProperty
  @XmlElement(name = "ResidencyStatus")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "residency_status")
  private String residencyStatus;

  /** The optional surname, last name, or family name for the person. */
  @Schema(description = "The optional surname, last name, or family name for the person")
  @JsonProperty
  @XmlElement(name = "Surname")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "surname", length = 100)
  private String surname;

  /** The optional code identifying the title for the person. */
  @Schema(description = "The optional code identifying the title for the person")
  @JsonProperty
  @XmlElement(name = "Title")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "title")
  private String title;

  /** Constructs a new <code>Person</code>. */
  public Person() {
    super(PartyType.PERSON);
  }

  /**
   * Add the contact mechanism for the person.
   *
   * @param contactMechanism the contact mechanism
   */
  @Override
  public void addContactMechanism(ContactMechanism contactMechanism) {
    super.addContactMechanism(contactMechanism);
  }

  /**
   * Add the identity document for the person.
   *
   * @param identityDocument the identity document
   */
  public void addIdentityDocument(IdentityDocument identityDocument) {
    identityDocument.setPerson(this);

    identityDocuments.add(identityDocument);
  }

  /**
   * Add the physical address for the person.
   *
   * @param physicalAddress the physical address
   */
  @Override
  public void addPhysicalAddress(PhysicalAddress physicalAddress) {
    super.addPhysicalAddress(physicalAddress);
  }

  /**
   * Returns the contact mechanisms for the person.
   *
   * @return the contact mechanisms for the person
   */
  @Schema(description = "The contact mechanisms for the person")
  @Override
  public Set<ContactMechanism> getContactMechanisms() {
    return super.getContactMechanisms();
  }

  /**
   * Returns the optional code identifying the correspondence language for the person.
   *
   * @return the optional code identifying the correspondence language for the person
   */
  public String getCorrespondenceLanguage() {
    return correspondenceLanguage;
  }

  /**
   * Returns the optional code identifying the country of birth for the person.
   *
   * @return the optional code identifying the country of birth for the person
   */
  public String getCountryOfBirth() {
    return countryOfBirth;
  }

  /**
   * Returns the optional code identifying the country of residence for the person.
   *
   * @return the optional code identifying the country of residence for the person
   */
  public String getCountryOfResidence() {
    return countryOfResidence;
  }

  /**
   * Returns the date and time the person was created.
   *
   * @return the date and time the person was created
   */
  @Override
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the optional date of birth for the person.
   *
   * @return the optional date of birth for the person
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Returns the optional date of death for the person.
   *
   * @return the optional date of death for the person
   */
  public LocalDate getDateOfDeath() {
    return dateOfDeath;
  }

  /**
   * Returns the optional code identifying the employment status for the person.
   *
   * @return the optional code identifying the employment status for the person
   */
  public String getEmploymentStatus() {
    return employmentStatus;
  }

  /**
   * Returns the optional code identifying the employment type for the person.
   *
   * @return the optional code identifying the employment type for the person
   */
  public String getEmploymentType() {
    return employmentType;
  }

  /**
   * Returns the optional code identifying the gender for the person.
   *
   * @return the optional code identifying the gender for the person
   */
  public String getGender() {
    return gender;
  }

  /**
   * Returns the optional given name, firstname, forename, or Christian name for the person.
   *
   * @return the optional given name, firstname, forename, or Christian name for the person
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * Returns the optional code identifying the home language for the person.
   *
   * @return the optional code identifying the home language for the person
   */
  public String getHomeLanguage() {
    return homeLanguage;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the person.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the person
   */
  @Schema(description = "The Universally Unique Identifier (UUID) uniquely identifying the person")
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the identity documents for the person.
   *
   * @return the identity documents for the person
   */
  public Set<IdentityDocument> getIdentityDocuments() {
    return identityDocuments;
  }

  /**
   * Returns the optional initials for the person.
   *
   * @return the optional initials for the person
   */
  public String getInitials() {
    return initials;
  }

  /**
   * Returns the optional maiden name for the person.
   *
   * @return the optional maiden name for the person
   */
  public String getMaidenName() {
    return maidenName;
  }

  /**
   * Returns the optional code identifying the marital status for the person.
   *
   * @return the optional code identifying the marital status for the person
   */
  public String getMaritalStatus() {
    return maritalStatus;
  }

  /**
   * Returns the optional code identifying the marriage type for the person if the person is
   * married.
   *
   * @return the optional code identifying the marriage type for the person if the person is married
   */
  public String getMarriageType() {
    return marriageType;
  }

  /**
   * Returns the optional middle names for the person.
   *
   * @return the optional middle names for the person
   */
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
  @Schema(description = "The personal name or full name of the person")
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the physical addresses for the person.
   *
   * @return the physical addresses for the person
   */
  @Schema(description = "The physical addresses for the person")
  @Override
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return super.getPhysicalAddresses();
  }

  /**
   * Returns the optional preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @return the optional preferred name for the person
   */
  public String getPreferredName() {
    return preferredName;
  }

  /**
   * The optional code identifying the race for the person.
   *
   * @return the optional code identifying the race for the person
   */
  public String getRace() {
    return race;
  }

  /**
   * Returns the optional code identifying the residency status for the person.
   *
   * @return the optional code identifying the residency status for the person
   */
  public String getResidencyStatus() {
    return residencyStatus;
  }

  /**
   * Returns the optional surname, last name, or family name for the person.
   *
   * @return the optional surname, last name, or family name for the person
   */
  public String getSurname() {
    return surname;
  }

  /**
   * Returns the optional code identifying the title for the person.
   *
   * @return the optional code identifying the title for the person
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the date and time the person was last updated.
   *
   * @return the date and time the person was last updated
   */
  @Override
  public LocalDateTime getUpdated() {
    return super.getUpdated();
  }

  /**
   * Remove the contact mechanism with the specified type and purpose for the person.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   */
  @Override
  public void removeContactMechanism(ContactMechanismType type, ContactMechanismPurpose purpose) {
    super.removeContactMechanism(type, purpose);
  }

  /**
   * Remove the identity document with the specified type for the person.
   *
   * @param type the code identifying the type of identity document
   */
  public void removeIdentityDocumentByType(String type) {
    identityDocuments.removeIf(
        identityDocument -> Objects.equals(identityDocument.getType(), type));
  }

  /**
   * Remove the physical address with the specified type and purpose for the person.
   *
   * @param type the physical address type
   * @param purpose the physical address purpose
   */
  @Override
  public void removePhysicalAddress(PhysicalAddressType type, PhysicalAddressPurpose purpose) {
    super.removePhysicalAddress(type, purpose);
  }

  /**
   * Set the contact mechanisms for the person.
   *
   * @param contactMechanisms the contact mechanisms for the person
   */
  @Override
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    super.setContactMechanisms(contactMechanisms);
  }

  /**
   * Set the optional code identifying the correspondence language for the person.
   *
   * @param correspondenceLanguage the optional code identifying the correspondence language for the
   *     person
   */
  public void setCorrespondenceLanguage(String correspondenceLanguage) {
    this.correspondenceLanguage = correspondenceLanguage;
  }

  /**
   * Set the optional code identifying the country of birth for the person.
   *
   * @param countryOfBirth the optional code identifying the country of birth for the person
   */
  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  /**
   * Set the optional code identifying the country of residence for the person.
   *
   * @param countryOfResidence the optional code identifying the country of residence for the person
   */
  public void setCountryOfResidence(String countryOfResidence) {
    this.countryOfResidence = countryOfResidence;
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
   * Set the optional code identifying the employment status for the person.
   *
   * @param employmentStatus the optional code identifying the employment status for the person
   */
  public void setEmploymentStatus(String employmentStatus) {
    this.employmentStatus = employmentStatus;
  }

  /**
   * Set the optional code identifying the employment type for the person.
   *
   * @param employmentType the optional code identifying the employment type for the person
   */
  public void setEmploymentType(String employmentType) {
    this.employmentType = employmentType;
  }

  /**
   * Set the optional code identifying the gender for the person.
   *
   * @param gender the optional code identifying the gender for the person
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
  }

  /**
   * Set the optional code identifying the home language for the person.
   *
   * @param homeLanguage the optional code identifying the home language for the person
   */
  public void setHomeLanguage(String homeLanguage) {
    this.homeLanguage = homeLanguage;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the person.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the person
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
   * Set the optional code identifying the marital status for the person.
   *
   * @param maritalStatus the optional code identifying the marital status for the person
   */
  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  /**
   * Set the optional code identifying the marriage type for the person if the person is married.
   *
   * @param marriageType the optional code identifying the marriage type for the person if the
   *     person is married
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
   * @param name
   */
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the physical addresses for the person.
   *
   * @param physicalAddresses the physical addresses for the person
   */
  @Override
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    super.setPhysicalAddresses(physicalAddresses);
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
   * Set the optional code identifying the race for the person.
   *
   * @param race the optional code identifying the race for the person
   */
  public void setRace(String race) {
    this.race = race;
  }

  /**
   * Set the optional code identifying the residency status for the person.
   *
   * @param residencyStatus the optional code identifying the residency status for the person
   */
  public void setResidencyStatus(String residencyStatus) {
    this.residencyStatus = residencyStatus;
  }

  /**
   * Set the optional surname, last name, or family name for the person.
   *
   * @param surname the optional surname, last name, or family name for the person
   */
  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   * Set the optional code identifying the title for the person.
   *
   * @param title the optional code identifying the title for the person
   */
  public void setTitle(String title) {
    this.title = title;
  }
}

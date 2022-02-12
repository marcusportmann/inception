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

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
 * available for a person. As a result, most of the attributes are and the particular application
 * making use of this entity should enforce which attributes are mandatory.
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
 *   <li>Add support for applying validations described by <b>RoleTypeAttributeTypeConstraint</b>s
 *       to the <b>ValidPersonValidator</b>.
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
  "countriesOfCitizenship",
  "countryOfBirth",
  "countryOfResidence",
  "dateOfBirth",
  "dateOfDeath",
  "employmentStatus",
  "employmentType",
  "gender",
  "givenName",
  "highestQualificationType",
  "initials",
  "language",
  "maidenName",
  "maritalStatus",
  "maritalStatusDate",
  "marriageType",
  "measurementSystem",
  "middleNames",
  "occupation",
  "preferredName",
  "race",
  "residencyStatus",
  "residentialType",
  "surname",
  "timeZone",
  "title",
  "attributes",
  "consents",
  "contactMechanisms",
  "educations",
  "employments",
  "externalReferences",
  "identityDocuments",
  "languageProficiencies",
  "locks",
  "nextOfKin",
  "physicalAddresses",
  "preferences",
  "residencePermits",
  "roles",
  "segmentAllocations",
  "skills",
  "sourcesOfFunds",
  "sourcesOfWealth",
  "statuses",
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
      "countriesOfCitizenship",
      "countryOfBirth",
      "countryOfResidence",
      "dateOfBirth",
      "dateOfDeath",
      "employmentStatus",
      "employmentType",
      "gender",
      "givenName",
      "highestQualificationType",
      "initials",
      "language",
      "maidenName",
      "maritalStatus",
      "maritalStatusDate",
      "marriageType",
      "measurementSystem",
      "middleNames",
      "occupation",
      "preferredName",
      "race",
      "residencyStatus",
      "residentialType",
      "surname",
      "timeZone",
      "title",
      "attributes",
      "consents",
      "contactMechanisms",
      "educations",
      "employments",
      "externalReferences",
      "identityDocuments",
      "languageProficiencies",
      "locks",
      "nextOfKin",
      "physicalAddresses",
      "preferences",
      "residencePermits",
      "roles",
      "segmentAllocations",
      "skills",
      "sourcesOfFunds",
      "sourcesOfWealth",
      "statuses",
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
  @OrderBy("type")
  private final Set<Attribute> attributes = new HashSet<>();

  /** The consents provided by the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Consent> consents = new HashSet<>();

  /** The contact mechanisms for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<ContactMechanism> contactMechanisms = new HashSet<>();

  /** The educations for the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("qualificationYear")
  private final Set<Education> educations = new HashSet<>();

  /** The employments for the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("startDate")
  private final Set<Employment> employments = new HashSet<>();

  /** The external references for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<ExternalReference> externalReferences = new HashSet<>();

  /** The identity documents for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<IdentityDocument> identityDocuments = new HashSet<>();

  /** The language proficiencies for the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("language")
  private final Set<LanguageProficiency> languageProficiencies = new HashSet<>();

  /** The locks applied to the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Lock> locks = new HashSet<>();

  /** The next of kin for the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<NextOfKin> nextOfKin = new HashSet<>();

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
  @OrderBy("type")
  private final Set<Preference> preferences = new HashSet<>();

  /** The residence permits for the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<ResidencePermit> residencePermits = new HashSet<>();

  /** The roles assigned directly to the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Role> roles = new HashSet<>();

  /** The segment allocations for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("segment")
  private final Set<SegmentAllocation> segmentAllocations = new HashSet<>();

  /** The skills for the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Skill> skills = new HashSet<>();

  /** The sources of funds for the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<SourceOfFunds> sourcesOfFunds = new HashSet<>();

  /** The sources of wealth for the person. */
  @Valid
  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<SourceOfWealth> sourcesOfWealth = new HashSet<>();

  /** The statuses assigned to the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Status> statuses = new HashSet<>();

  /** The tax numbers for the person. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<TaxNumber> taxNumbers = new HashSet<>();

  /**
   * The comma-delimited ISO 3166-1 alpha-2 codes for the countries of citizenship for the person.
   */
  @JsonIgnore
  @XmlTransient
  @Size(max = 50)
  @Column(table = "persons", name = "countries_of_citizenship", length = 50)
  private String countriesOfCitizenship;

  /**
   * The comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for the person.
   */
  @JsonIgnore
  @XmlTransient
  @Size(max = 50)
  @Column(table = "persons", name = "countries_of_tax_residence", length = 50)
  private String countriesOfTaxResidence;

  /** The ISO 3166-1 alpha-2 code for the country of birth for the person. */
  @Size(min = 2, max = 2)
  @Column(table = "persons", name = "country_of_birth", length = 2)
  private String countryOfBirth;

  /** The ISO 3166-1 alpha-2 code for the country of residence for the person. */
  @Size(min = 2, max = 2)
  @Column(table = "persons", name = "country_of_residence", length = 2)
  private String countryOfResidence;

  /** The date of birth for the person. */
  @Column(table = "persons", name = "date_of_birth")
  private LocalDate dateOfBirth;

  /** The date of death for the person. */
  @Column(table = "persons", name = "date_of_death")
  private LocalDate dateOfDeath;

  /** The code for the employment status for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "employment_status", length = 30)
  private String employmentStatus;

  /** The code for the employment type for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "employment_type", length = 30)
  private String employmentType;

  /** The code for the gender for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "gender", length = 30)
  private String gender;

  /** The given name, firstname, forename, or Christian name for the person. */
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Person.GivenName.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}- ]*(?!\\s+)$")
  @Column(table = "persons", name = "given_name", length = 100)
  private String givenName;

  /** The code for the highest qualification type for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "highest_qualification_type", length = 30)
  private String highestQualificationType;

  /** The initials for the person. */
  @Size(min = 1, max = 20)
  @Column(table = "persons", name = "initials", length = 20)
  private String initials;

  /** The ISO 639-1 alpha-2 code for the language for the person. */
  @Size(min = 2, max = 2)
  @Column(table = "persons", name = "language", length = 2)
  private String language;

  /** The maiden name for the person. */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "maiden_name", length = 100)
  private String maidenName;

  /** The code for the marital status for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "marital_status", length = 30)
  private String maritalStatus;

  /** The date for the marital status for the person. */
  @Column(table = "persons", name = "marital_status_date")
  private LocalDate maritalStatusDate;

  /** The code for the marriage type for the person if the person is married. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "marriage_type", length = 30)
  private String marriageType;

  /** The measurement system for the person. */
  @Column(table = "persons", name = "measurement_system", length = 30)
  private MeasurementSystem measurementSystem;

  /** The middle names for the person. */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "middle_names", length = 100)
  private String middleNames;

  /** The code for the occupation for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "occupation", length = 30)
  private String occupation;

  /**
   * The preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   */
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "preferred_name", length = 100)
  private String preferredName;

  /** The code for the race for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "race", length = 30)
  private String race;

  /** The code for the residency status for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "residency_status", length = 30)
  private String residencyStatus;

  /** The code for the residential type for the person. */
  @Size(min = 1, max = 30)
  @Column(table = "persons", name = "residential_type", length = 30)
  private String residentialType;

  /** The surname, last name, or family name for the person. */
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Person.Surname.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}- ]*(?!\\s+)$")
  @Column(table = "persons", name = "surname", length = 100)
  private String surname;

  /** The time zone ID for the person. */
  @Size(min = 1, max = 50)
  @Column(table = "persons", name = "time_zone", length = 50)
  private String timeZone;

  /** The code for the title for the person. */
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
   * @param tenantId the ID for the tenant the person is associated with
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
   * Add the consent provided by the person.
   *
   * @param consent the consent
   */
  public void addConsent(Consent consent) {
    consents.removeIf(
        existingConsent -> Objects.equals(existingConsent.getType(), consent.getType()));

    consent.setPerson(this);

    consents.add(consent);
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
                && Objects.equals(existingContactMechanism.getRole(), contactMechanism.getRole()));

    contactMechanism.setParty(this);

    contactMechanisms.add(contactMechanism);
  }

  /**
   * Add the education for the person.
   *
   * @param education the education
   */
  public void addEducation(Education education) {
    educations.removeIf(
        existingEducation -> Objects.equals(existingEducation.getId(), education.getId()));

    education.setPerson(this);

    educations.add(education);
  }

  /**
   * Add the employment for the person.
   *
   * @param employment the employment
   */
  public void addEmployment(Employment employment) {
    employments.removeIf(
        existingEmployment -> Objects.equals(existingEmployment.getId(), employment.getId()));

    employment.setPerson(this);

    employments.add(employment);
  }

  /**
   * Add the external reference for the person.
   *
   * @param externalReference the external reference
   */
  public void addExternalReference(ExternalReference externalReference) {
    externalReferences.removeIf(
        existingExternalReference ->
            Objects.equals(existingExternalReference.getType(), externalReference.getType()));

    externalReference.setParty(this);

    externalReferences.add(externalReference);
  }

  /**
   * Add the identity document for the person.
   *
   * @param identityDocument the identity document
   */
  public void addIdentityDocument(IdentityDocument identityDocument) {
    identityDocuments.removeIf(
        existingIdentityDocument ->
            Objects.equals(existingIdentityDocument.getId(), identityDocument.getId()));

    identityDocuments.removeIf(
        existingIdentityDocument ->
            Objects.equals(existingIdentityDocument.getType(), identityDocument.getType())
                && Objects.equals(
                    existingIdentityDocument.getCountryOfIssue(),
                    identityDocument.getCountryOfIssue())
                && Objects.equals(
                    existingIdentityDocument.getDateOfIssue(), identityDocument.getDateOfIssue()));

    identityDocument.setParty(this);

    identityDocuments.add(identityDocument);
  }

  /**
   * Add the language proficiency for the person.
   *
   * @param languageProficiency the language proficiency
   */
  public void addLanguageProficiency(LanguageProficiency languageProficiency) {
    languageProficiencies.removeIf(
        existingLanguageProficiency ->
            Objects.equals(
                existingLanguageProficiency.getLanguage(), languageProficiency.getLanguage()));

    languageProficiency.setPerson(this);

    languageProficiencies.add(languageProficiency);
  }

  /**
   * Apply the lock to the person.
   *
   * @param lock the lock
   */
  public void addLock(Lock lock) {
    locks.removeIf(existingLock -> Objects.equals(existingLock.getType(), lock.getType()));

    lock.setParty(this);

    locks.add(lock);
  }

  /**
   * Add the next of kin for the person.
   *
   * @param nextOfKin the next of kin
   */
  public void addNextOfKin(NextOfKin nextOfKin) {
    this.nextOfKin.removeIf(
        existingNextOfKin -> Objects.equals(existingNextOfKin.getId(), nextOfKin.getId()));

    nextOfKin.setPerson(this);

    this.nextOfKin.add(nextOfKin);
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
    residencePermits.removeIf(
        existingResidencePermit ->
            Objects.equals(existingResidencePermit.getId(), residencePermit.getId()));

    residencePermits.removeIf(
        existingResidencePermit ->
            Objects.equals(existingResidencePermit.getType(), residencePermit.getType())
                && Objects.equals(
                    existingResidencePermit.getCountryOfIssue(),
                    residencePermit.getCountryOfIssue())
                && Objects.equals(
                    existingResidencePermit.getDateOfIssue(), residencePermit.getDateOfIssue()));

    residencePermit.setPerson(this);

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
   * Add the segment allocation to the person.
   *
   * @param segmentAllocation the segment allocation
   */
  public void addSegmentAllocation(SegmentAllocation segmentAllocation) {
    segmentAllocations.removeIf(
        existingSegmentAllocation ->
            Objects.equals(existingSegmentAllocation.getSegment(), segmentAllocation.getSegment()));

    segmentAllocation.setParty(this);

    segmentAllocations.add(segmentAllocation);
  }

  /**
   * Add the skill for the person.
   *
   * @param skill the skill
   */
  public void addSkill(Skill skill) {
    skills.removeIf(existingSkill -> Objects.equals(existingSkill.getType(), skill.getType()));

    skill.setPerson(this);

    skills.add(skill);
  }

  /**
   * Add the source of funds for the person.
   *
   * @param sourceOfFunds the source of funds
   */
  public void addSourceOfFunds(SourceOfFunds sourceOfFunds) {
    sourcesOfFunds.removeIf(
        existingSourceOfFunds ->
            Objects.equals(existingSourceOfFunds.getType(), sourceOfFunds.getType()));

    sourceOfFunds.setPerson(this);

    sourcesOfFunds.add(sourceOfFunds);
  }

  /**
   * Add the source of wealth for the person.
   *
   * @param sourceOfWealth the source of wealth
   */
  public void addSourceOfWealth(SourceOfWealth sourceOfWealth) {
    sourcesOfWealth.removeIf(
        existingSourceOfWealth ->
            Objects.equals(existingSourceOfWealth.getType(), sourceOfWealth.getType()));

    sourceOfWealth.setPerson(this);

    sourcesOfWealth.add(sourceOfWealth);
  }

  /**
   * Assign the status to the person.
   *
   * @param status the status
   */
  public void addStatus(Status status) {
    statuses.removeIf(existingStatus -> Objects.equals(existingStatus.getType(), status.getType()));

    status.setParty(this);

    statuses.add(status);
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
   * @return an Optional containing the attribute with the specified type for the person or an empty
   *     Optional if the attribute could not be found
   */
  public Optional<Attribute> getAttributeWithType(String type) {
    return attributes.stream()
        .filter(attribute -> Objects.equals(attribute.getType(), type))
        .findFirst();
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
   * Retrieve the consent with the specified type for the person.
   *
   * @param type the code for the consent type
   * @return an Optional containing the consent with the specified type for the person or an empty
   *     Optional if the consent could not be found
   */
  public Optional<Consent> getConsentWithType(String type) {
    return consents.stream().filter(consent -> Objects.equals(consent.getType(), type)).findFirst();
  }

  /**
   * Returns the consents provided by the person.
   *
   * @return the consents provided by the person
   */
  @Schema(description = "The consents provided by the person")
  @JsonProperty
  @JsonManagedReference("consentReference")
  @XmlElementWrapper(name = "Consents")
  @XmlElement(name = "Consent")
  public Set<Consent> getConsents() {
    return consents;
  }

  /**
   * Retrieve the contact mechanism with the specified role for the person.
   *
   * @param role the code for the contact mechanism role
   * @return an Optional containing the contact mechanism with the specified role for the person or
   *     an empty Optional if the contact mechanism could not be found
   */
  public Optional<ContactMechanism> getContactMechanismWithRole(String role) {
    return contactMechanisms.stream()
        .filter(contactMechanism -> Objects.equals(contactMechanism.getRole(), role))
        .findFirst();
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the person.
   *
   * @param type the code for the contact mechanism type
   * @param purpose the code for the contact mechanism role
   * @return an Optional containing the contact mechanism with the specified type and purpose for
   *     the person or an empty Optional if the contact mechanism could not be found
   */
  public Optional<ContactMechanism> getContactMechanismWithTypeAndPurpose(
      String type, String purpose) {
    return contactMechanisms.stream()
        .filter(
            contactMechanism ->
                Objects.equals(contactMechanism.getType(), type)
                    && contactMechanism.hasPurpose(purpose))
        .findFirst();
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
   * Returns the ISO 3166-1 alpha-2 codes for the countries of citizenship for the person.
   *
   * @return the ISO 3166-1 alpha-2 codes for the countries of citizenship for the person
   */
  @Schema(
      description = "The ISO 3166-1 alpha-2 codes for the countries of citizenship for the person")
  @JsonProperty
  @XmlElementWrapper(name = "CountriesOfCitizenship")
  @XmlElement(name = "CountryOfCitizenship")
  @Size(max = 10)
  public Set<String> getCountriesOfCitizenship() {
    return Set.of(StringUtils.commaDelimitedListToStringArray(countriesOfCitizenship));
  }

  /**
   * Returns the ISO 3166-1 alpha-2 codes for the countries of tax residence for the person.
   *
   * @return the ISO 3166-1 alpha-2 codes for the countries of tax residence for the person
   */
  @Schema(
      description =
          "The ISO 3166-1 alpha-2 codes for the countries of tax residence for the person")
  @JsonProperty
  @XmlElementWrapper(name = "CountriesOfTaxResidence")
  @XmlElement(name = "CountryOfTaxResidence")
  @Size(max = 10)
  public Set<String> getCountriesOfTaxResidence() {
    return Set.of(StringUtils.commaDelimitedListToStringArray(countriesOfTaxResidence));
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of birth for the person.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of birth for the person
   */
  @Schema(description = "The ISO 3166-1 alpha-2 code for the country of birth for the person")
  @JsonProperty
  @XmlElement(name = "CountryOfBirth")
  public String getCountryOfBirth() {
    return countryOfBirth;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of residence for the person.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of residence for the person
   */
  @Schema(description = "The ISO 3166-1 alpha-2 code for the country of residence for the person")
  @JsonProperty
  @XmlElement(name = "CountryOfResidence")
  public String getCountryOfResidence() {
    return countryOfResidence;
  }

  /**
   * Returns the date of birth for the person.
   *
   * @return the date of birth for the person
   */
  @Schema(description = "The date of birth for the person")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateOfBirth")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Returns the date of death for the person.
   *
   * @return the date of death for the person
   */
  @Schema(description = "The date of death for the person")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateOfDeath")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  public LocalDate getDateOfDeath() {
    return dateOfDeath;
  }

  /**
   * Retrieve the education with the specified ID for the person.
   *
   * @param id the ID for the education
   * @return an Optional containing the education with the specified ID for the person or an empty
   *     if the education could not be found
   */
  public Optional<Education> getEducationWithId(UUID id) {
    return educations.stream()
        .filter(education -> Objects.equals(education.getId(), id))
        .findFirst();
  }

  /**
   * Returns the educations for the person.
   *
   * @return the educations for the person
   */
  @Schema(description = "The educations obtained by the person")
  @JsonProperty
  @JsonManagedReference("educationReference")
  @XmlElementWrapper(name = "Educations")
  @XmlElement(name = "Education")
  public Set<Education> getEducations() {
    return educations;
  }

  /**
   * Returns the code for the employment status for the person.
   *
   * @return the code for the employment status for the person
   */
  @Schema(description = "The code for the employment status for the person")
  @JsonProperty
  @XmlElement(name = "EmploymentStatus")
  public String getEmploymentStatus() {
    return employmentStatus;
  }

  /**
   * Returns the code for the employment type for the person.
   *
   * @return the code for the employment type for the person
   */
  @Schema(description = "The code for the employment type for the person")
  @JsonProperty
  @XmlElement(name = "EmploymentType")
  public String getEmploymentType() {
    return employmentType;
  }

  /**
   * Retrieve the employment with the specified ID for the person.
   *
   * @param id the ID for the employment
   * @return an Optional containing the employment with the specified ID for the person or an empty
   *     if the employment could not be found
   */
  public Optional<Employment> getEmploymentWithId(UUID id) {
    return employments.stream()
        .filter(employment -> Objects.equals(employment.getId(), id))
        .findFirst();
  }

  /**
   * Returns the employments for the person.
   *
   * @return the employments for the person
   */
  @Schema(description = "The employments for the person")
  @JsonProperty
  @JsonManagedReference("employmentReference")
  @XmlElementWrapper(name = "Employments")
  @XmlElement(name = "Employment")
  public Set<Employment> getEmployments() {
    return employments;
  }

  /**
   * Retrieve the first external reference with the specified type for the person.
   *
   * @param type the code for the external reference type
   * @return an Optional containing the first external reference with the specified type for the
   *     person or an empty if an external reference could not be found
   */
  public Optional<ExternalReference> getExternalReferenceWithType(String type) {
    return externalReferences.stream()
        .filter(externalReference -> Objects.equals(externalReference.getType(), type))
        .findFirst();
  }

  /**
   * Returns the external references for the person.
   *
   * @return the external references for the person
   */
  @Schema(description = "The external references for the person")
  @JsonProperty
  @JsonManagedReference("externalReferenceReference")
  @XmlElementWrapper(name = "ExternalReferences")
  @XmlElement(name = "ExternalReference")
  public Set<ExternalReference> getExternalReferences() {
    return externalReferences;
  }

  /**
   * Retrieve the external references with the specified type for the person.
   *
   * @param type the code for the external reference type
   * @return the external references with the specified type for the person
   */
  public Set<ExternalReference> getExternalReferencesWithType(String type) {
    return externalReferences.stream()
        .filter(externalReference -> Objects.equals(externalReference.getType(), type))
        .collect(Collectors.toSet());
  }

  /**
   * Returns the code for the gender for the person.
   *
   * @return the code for the gender for the person
   */
  @Schema(description = "The code for the gender for the person")
  @JsonProperty
  @XmlElement(name = "Gender")
  public String getGender() {
    return gender;
  }

  /**
   * Returns the given name, firstname, forename, or Christian name for the person.
   *
   * @return the given name, firstname, forename, or Christian name for the person
   */
  @Schema(description = "The given name, firstname, forename, or Christian name for the person")
  @JsonProperty
  @XmlElement(name = "GivenName")
  public String getGivenName() {
    return givenName;
  }

  /**
   * Returns the code for the highest qualification type for the person.
   *
   * @return the code for the highest qualification type for the person
   */
  @Schema(description = "The code for the highest qualification type for the person")
  @JsonProperty
  @XmlElement(name = "HighestQualificationType")
  public String getHighestQualificationType() {
    return highestQualificationType;
  }

  /**
   * Returns the ID for the person.
   *
   * @return the ID for the person
   */
  @Schema(description = "The ID for the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Retrieve the identity document with the specified type for the person.
   *
   * @param type the code for the identity document type
   * @return an Optional containing the identity document with the specified type for the person or
   *     an empty if the identity document could not be found
   */
  public Optional<IdentityDocument> getIdentityDocumentWithType(String type) {
    return identityDocuments.stream()
        .filter(identityDocument -> Objects.equals(identityDocument.getType(), type))
        .findFirst();
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
   * Returns the initials for the person.
   *
   * @return the initials for the person
   */
  @Schema(description = "The initials for the person")
  @JsonProperty
  @XmlElement(name = "Initials")
  public String getInitials() {
    return initials;
  }

  /**
   * Returns the ISO 639-1 alpha-2 code for the language for the person.
   *
   * @return the ISO 639-1 alpha-2 code for the language for the person
   */
  @Schema(description = "The ISO 639-1 alpha-2 code for the language for the person")
  @JsonProperty
  @XmlElement(name = "Language")
  public String getLanguage() {
    return language;
  }

  /**
   * Returns the language proficiencies for the person.
   *
   * @return the language proficiencies for the person
   */
  @Schema(description = "The language proficiencies for the person")
  @JsonProperty
  @JsonManagedReference("languageProficiencyReference")
  @XmlElementWrapper(name = "LanguageProficiencies")
  @XmlElement(name = "LanguageProficiency")
  public Set<LanguageProficiency> getLanguageProficiencies() {
    return languageProficiencies;
  }

  /**
   * Retrieve the language proficiency with the specified language for the person.
   *
   * @param language the ISO 639-1 alpha-2 code for the language
   * @return an Optional containing the language proficiency with the specified language for the
   *     person or an empty Optional if the language proficiency could not be found
   */
  public Optional<LanguageProficiency> getLanguageProficiencyWithLanguage(String language) {
    return languageProficiencies.stream()
        .filter(languageProficiency -> Objects.equals(languageProficiency.getLanguage(), language))
        .findFirst();
  }

  /**
   * Retrieve the lock with the specified type for the person.
   *
   * @param type the code for the lock type
   * @return an Optional containing the lock with the specified type for the person or an empty
   *     Optional if the lock could not be found
   */
  public Optional<Lock> getLockWithType(String type) {
    return locks.stream().filter(lock -> Objects.equals(lock.getType(), type)).findFirst();
  }

  /**
   * Returns the locks applied to the person.
   *
   * @return the locks applied to the person
   */
  @Schema(description = "The locks applied to the person")
  @JsonProperty
  @JsonManagedReference("lockReference")
  @XmlElementWrapper(name = "Locks")
  @XmlElement(name = "Lock")
  public Set<Lock> getLocks() {
    return locks;
  }

  /**
   * Returns the maiden name for the person.
   *
   * @return the maiden name for the person
   */
  @Schema(description = "The maiden name for the person")
  @JsonProperty
  @XmlElement(name = "MaidenName")
  public String getMaidenName() {
    return maidenName;
  }

  /**
   * Returns the code for the marital status for the person.
   *
   * @return the code for the marital status for the person
   */
  @Schema(description = "The code for the marital status for the person")
  @JsonProperty
  @XmlElement(name = "MaritalStatus")
  public String getMaritalStatus() {
    return maritalStatus;
  }

  /**
   * Returns the date for the marital status for the person.
   *
   * @return the date for the marital status for the person
   */
  @Schema(description = "The date for the marital status for the person")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "MaritalStatusDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  public LocalDate getMaritalStatusDate() {
    return maritalStatusDate;
  }

  /**
   * Returns the code for the marriage type for the person if the person is married.
   *
   * @return the code for the marriage type for the person if the person is married
   */
  @Schema(description = "The code for the marriage type for the person if the person is married")
  @JsonProperty
  @XmlElement(name = "MarriageType")
  public String getMarriageType() {
    return marriageType;
  }

  /**
   * Returns the measurement system for the person.
   *
   * @return the measurement system for the person
   */
  @Schema(description = "The measurement system for the person")
  @JsonProperty
  @XmlElement(name = "MeasurementSystem")
  public MeasurementSystem getMeasurementSystem() {
    return measurementSystem;
  }

  /**
   * Returns the middle names for the person.
   *
   * @return the middle names for the person
   */
  @Schema(description = "The middle names for the person")
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
   * Returns the next of kin for the person.
   *
   * @return the next of kin for the person
   */
  @Schema(description = "The next of kin for the person")
  @JsonProperty
  @JsonManagedReference("nextOfKinReference")
  @XmlElementWrapper(name = "NextOfKin")
  @XmlElement(name = "NextOfKin")
  public Set<NextOfKin> getNextOfKin() {
    return nextOfKin;
  }

  /**
   * Retrieve the next of kin with the specified ID for the person.
   *
   * @param id the ID for the next of kin
   * @return an Optional containing the next of kin with the specified ID for the person or an empty
   *     Optional if the next of kin could not be found
   */
  public Optional<NextOfKin> getNextOfKinWithId(UUID id) {
    return nextOfKin.stream()
        .filter(existingNextOfKin -> Objects.equals(existingNextOfKin.getId(), id))
        .findFirst();
  }

  /**
   * Retrieve the next of kin with the specified type for the person.
   *
   * @param type the code for the next of kin type
   * @return an Optional containing the next of kin with the specified type for the person or an
   *     empty Optional if the next of kin could not be found
   */
  public Optional<NextOfKin> getNextOfKinWithType(String type) {
    return nextOfKin.stream()
        .filter(existingNextOfKin -> Objects.equals(existingNextOfKin.getType(), type))
        .findFirst();
  }

  /**
   * Returns the code for the occupation for the person.
   *
   * @return the code for the occupation for the person
   */
  @Schema(description = "The code for the occupation for the person")
  @JsonProperty
  @XmlElement(name = "Occupation")
  public String getOccupation() {
    return occupation;
  }

  /**
   * Retrieve the first physical address with the specified role for the person.
   *
   * @param role the code for the physical address role
   * @return an Optional containing the first physical address with the specified role for the
   *     person or an empty Optional if the physical address could not be found
   */
  public Optional<PhysicalAddress> getPhysicalAddressWithRole(String role) {
    return physicalAddresses.stream()
        .filter(physicalAddress -> Objects.equals(physicalAddress.getRole(), role))
        .findFirst();
  }

  /**
   * Retrieve the first physical address with the specified type and purpose for the person.
   *
   * @param type the code for the physical address type
   * @param purpose the code for the physical address purpose
   * @return an Optional containing the first physical address with the specified type and purpose
   *     for the person or an empty Optional if the physical address could not be found
   */
  public Optional<PhysicalAddress> getPhysicalAddressWithTypeAndPurpose(
      String type, String purpose) {
    return physicalAddresses.stream()
        .filter(
            physicalAddress ->
                Objects.equals(physicalAddress.getType(), type)
                    && physicalAddress.getPurposes().contains(purpose))
        .findFirst();
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
   * @return an Optional containing the preference with the specified type for the person or an
   *     empty if the preference could not be found
   */
  public Optional<Preference> getPreferenceWithType(String type) {
    return preferences.stream()
        .filter(preference -> Objects.equals(preference.getType(), type))
        .findFirst();
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
   * Returns the preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @return the preferred name for the person
   */
  @Schema(description = "The preferred name for the person")
  @JsonProperty
  @XmlElement(name = "PreferredName")
  public String getPreferredName() {
    return preferredName;
  }

  /**
   * The code for the race for the person.
   *
   * @return the code for the race for the person
   */
  @Schema(description = "The code for the race for the person")
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
   * Returns the code for the residency status for the person.
   *
   * @return the code for the residency status for the person
   */
  @Schema(description = "The code for the residency status for the person")
  @JsonProperty
  @XmlElement(name = "ResidencyStatus")
  public String getResidencyStatus() {
    return residencyStatus;
  }

  /**
   * Returns the code for the residential type for the person.
   *
   * @return the code for the residential type for the person
   */
  @Schema(description = "The code for the residential type for the person")
  @JsonProperty
  @XmlElement(name = "ResidentialType")
  public String getResidentialType() {
    return residentialType;
  }

  /**
   * Retrieve the role with the specified type for the person independent of a party association.
   *
   * @param type the code for the role type
   * @return an Optional containing the role with the specified type for the person independent of a
   *     party association or an empty Optional if the role could not be found
   */
  public Optional<Role> getRoleWithType(String type) {
    return roles.stream().filter(role -> Objects.equals(role.getType(), type)).findFirst();
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
   * Retrieve the segment allocation with the specified segment for the person.
   *
   * @param segment the code for the segment
   * @return an Optional containing the segment allocation with the specified segment for the person
   *     or an empty Optional if the segment allocation could not be found
   */
  public Optional<SegmentAllocation> getSegmentAllocationWithSegment(String segment) {
    return segmentAllocations.stream()
        .filter(segmentAllocation -> Objects.equals(segmentAllocation.getSegment(), segment))
        .findFirst();
  }

  /**
   * Returns the segment allocations for the person.
   *
   * @return the segment allocations for the person
   */
  @Schema(description = "The segment allocations for the person")
  @JsonProperty
  @JsonManagedReference("segmentAllocationReference")
  @XmlElementWrapper(name = "SegmentAllocations")
  @XmlElement(name = "SegmentAllocation")
  public Set<SegmentAllocation> getSegmentAllocations() {
    return segmentAllocations;
  }

  /**
   * Retrieve the skill with the specified type for the person.
   *
   * @param type the code for the skill type
   * @return an Optional containing the skill with the specified type for the person or an empty
   *     Optional if the skill could not be found
   */
  public Optional<Skill> getSkillWithType(String type) {
    return skills.stream().filter(skill -> Objects.equals(skill.getType(), type)).findFirst();
  }

  /**
   * Returns the skills for the person.
   *
   * @return the skills for the person
   */
  @Schema(description = "The skills for the person")
  @JsonProperty
  @JsonManagedReference("skillReference")
  @XmlElementWrapper(name = "Skills")
  @XmlElement(name = "Skill")
  public Set<Skill> getSkills() {
    return skills;
  }

  /**
   * Retrieve the source of funds with the specified type for the person.
   *
   * @param type the code for the source of funds type
   * @return an Optional containing the source of funds with the specified type for the person or an
   *     empty Optional if the source of funds could not be found
   */
  public Optional<SourceOfFunds> getSourceOfFundsWithType(String type) {
    return sourcesOfFunds.stream()
        .filter(sourceOfFunds -> Objects.equals(sourceOfFunds.getType(), type))
        .findFirst();
  }

  /**
   * Retrieve the source of wealth with the specified type for the person.
   *
   * @param type the code for the source of wealth type
   * @return an Optional containing the source of wealth with the specified type for the person or
   *     an empty Optional if the source of wealth could not be found
   */
  public Optional<SourceOfWealth> getSourceOfWealthWithType(String type) {
    return sourcesOfWealth.stream()
        .filter(sourceOfWealth -> Objects.equals(sourceOfWealth.getType(), type))
        .findFirst();
  }

  /**
   * Returns the sources of funds for the person.
   *
   * @return the sources of funds for the person
   */
  @Schema(description = "The sources of funds for the person")
  @JsonProperty
  @JsonManagedReference("sourceOfFundsReference")
  @XmlElementWrapper(name = "SourcesOfFunds")
  @XmlElement(name = "SourceOfFunds")
  public Set<SourceOfFunds> getSourcesOfFunds() {
    return sourcesOfFunds;
  }

  /**
   * Returns the sources of wealth for the person.
   *
   * @return the sources of wealth for the person
   */
  @Schema(description = "The sources of wealth for the person")
  @JsonProperty
  @JsonManagedReference("sourceOfWealthReference")
  @XmlElementWrapper(name = "SourcesOfWealth")
  @XmlElement(name = "SourceOfWealth")
  public Set<SourceOfWealth> getSourcesOfWealth() {
    return sourcesOfWealth;
  }

  /**
   * Retrieve the status with the specified type for the person.
   *
   * @param type the code for the status type
   * @return an Optional containing the status with the specified type for the person or an empty
   *     Optional if the status could not be found
   */
  public Optional<Status> getStatusWithType(String type) {
    return statuses.stream().filter(status -> Objects.equals(status.getType(), type)).findFirst();
  }

  /**
   * Returns the statuses assigned to the person.
   *
   * @return the statuses assigned to the person
   */
  @Schema(description = "The statuses assigned to the person")
  @JsonProperty
  @JsonManagedReference("statusReference")
  @XmlElementWrapper(name = "Statuses")
  @XmlElement(name = "Status")
  public Set<Status> getStatuses() {
    return statuses;
  }

  /**
   * Returns the surname, last name, or family name for the person.
   *
   * @return the surname, last name, or family name for the person
   */
  @Schema(description = "The surname, last name, or family name for the person")
  @JsonProperty
  @XmlElement(name = "Surname")
  public String getSurname() {
    return surname;
  }

  /**
   * Retrieve the tax number with the specified type for the person.
   *
   * @param type the code for the tax number type
   * @return an Optional containing the tax number with the specified type for the person or an
   *     empty Optional if the tax number could not be found
   */
  public Optional<TaxNumber> getTaxNumberWithType(String type) {
    return taxNumbers.stream()
        .filter(taxNumber -> Objects.equals(taxNumber.getType(), type))
        .findFirst();
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
   * Returns the ID for the tenant the person is associated with.
   *
   * @return the ID for the tenant the person is associated with
   */
  @Schema(description = "The ID for the tenant the person is associated with", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  public UUID getTenantId() {
    return super.getTenantId();
  }

  /**
   * Returns the time zone ID for the person.
   *
   * @return the time zone ID for the person
   */
  @Schema(description = "The time zone ID for the person")
  @JsonProperty
  @XmlElement(name = "TimeZone")
  public String getTimeZone() {
    return timeZone;
  }

  /**
   * Returns the code for the title for the person.
   *
   * @return the code for the title for the person
   */
  @Schema(description = "The code for the title for the person")
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
   * Returns whether the person has an attribute with the specified type.
   *
   * @param type the code for the attribute type
   * @return <b>true</b> if the person has an attribute with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasAttributeWithType(String type) {
    return attributes.stream().anyMatch(attribute -> Objects.equals(attribute.getType(), type));
  }

  /**
   * Returns whether the person has a consent with the specified type.
   *
   * @param type the code for the consent type
   * @return <b>true</b> if the person has a consent with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasConsentWithType(String type) {
    return consents.stream().anyMatch(consent -> Objects.equals(consent.getType(), type));
  }

  /**
   * Returns whether the person has a contact mechanism with the specified role.
   *
   * @param role the code for the contact mechanism role
   * @return <b>true</b> if the person has a contact mechanism with the specified role or
   *     <b>false</b> otherwise
   */
  public boolean hasContactMechanismWithRole(String role) {
    return contactMechanisms.stream()
        .anyMatch(contactMechanism -> Objects.equals(contactMechanism.getRole(), role));
  }

  /**
   * Returns whether the person has a contact mechanism with the specified type.
   *
   * @param type the code for the contact mechanism type
   * @return <b>true</b> if the person has a contact mechanism with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasContactMechanismWithType(String type) {
    return contactMechanisms.stream()
        .anyMatch(contactMechanism -> Objects.equals(contactMechanism.getType(), type));
  }

  /**
   * Returns whether the person has an external reference with the specified type.
   *
   * @param type the code for the external reference type
   * @return <b>true</b> if the person has an external reference with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasExternalReferenceWithType(String type) {
    return externalReferences.stream()
        .anyMatch(externalReference -> Objects.equals(externalReference.getType(), type));
  }

  /**
   * Returns whether the person has an identity document with the specified type.
   *
   * @param type the code for the identity document type
   * @return <b>true</b> if the person has an identity document with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasIdentityDocumentWithType(String type) {
    return identityDocuments.stream()
        .anyMatch(identityDocument -> Objects.equals(identityDocument.getType(), type));
  }

  /**
   * Returns whether the person has a language proficiency with the specified language.
   *
   * @param language the ISO 639-1 alpha-2 code for the language
   * @return <b>true</b> if the person has a language proficiency with the specified language or
   *     <b>false</b> otherwise
   */
  public boolean hasLanguageProficiencyWithLanguage(String language) {
    return languageProficiencies.stream()
        .anyMatch(
            languageProficiency -> Objects.equals(languageProficiency.getLanguage(), language));
  }

  /**
   * Returns whether the person has a lock with the specified type.
   *
   * @param type the code for the lock type
   * @return <b>true</b> if the person has a lock with the specified type or <b>false</b> otherwise
   */
  public boolean hasLockWithType(String type) {
    return locks.stream().anyMatch(lock -> Objects.equals(lock.getType(), type));
  }

  /**
   * Returns whether the person has a next of kin with the specified type.
   *
   * @param type the code for the next of kin type
   * @return <b>true</b> if the person has a next of kin with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasNextOfKinWithType(String type) {
    return nextOfKin.stream()
        .anyMatch(exitingNextOfKin -> Objects.equals(exitingNextOfKin.getType(), type));
  }

  /**
   * Returns whether the person has a physical address with the specified role.
   *
   * @param role the code for the physical address role
   * @return <b>true</b> if the person has a physical address with the specified role or
   *     <b>false</b> otherwise
   */
  public boolean hasPhysicalAddressWithRole(String role) {
    return physicalAddresses.stream()
        .anyMatch(physicalAddress -> Objects.equals(physicalAddress.getRole(), role));
  }

  /**
   * Returns whether the person has a physical address with the specified type.
   *
   * @param type the code for the physical address type
   * @return <b>true</b> if the person has a physical address with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasPhysicalAddressWithType(String type) {
    return physicalAddresses.stream()
        .anyMatch(physicalAddress -> Objects.equals(physicalAddress.getType(), type));
  }

  /**
   * Returns whether the person has a preference with the specified type.
   *
   * @param type the code for the preference type
   * @return <b>true</b> if the person has a preference with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasPreferenceWithType(String type) {
    return preferences.stream().anyMatch(preference -> Objects.equals(preference.getType(), type));
  }

  /**
   * Returns whether the person has a residence permit with the specified type.
   *
   * @param type the code for the residence permit type
   * @return <b>true</b> if the person has a residence permit with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasResidencePermitWithType(String type) {
    return residencePermits.stream()
        .anyMatch(residencePermit -> Objects.equals(residencePermit.getType(), type));
  }

  /**
   * Returns whether the person has a role with the specified type.
   *
   * @param type the code for the role type
   * @return <b>true</b> if the person has a role with the specified type or <b>false</b> otherwise
   */
  public boolean hasRoleWithType(String type) {
    return roles.stream().anyMatch(role -> Objects.equals(role.getType(), type));
  }

  /**
   * Returns whether the person has a segment allocation with the specified segment.
   *
   * @param segment the code for the segment
   * @return <b>true</b> if the person has a segment allocation with the specified segment or
   *     <b>false</b> otherwise
   */
  public boolean hasSegmentAllocationWithSegment(String segment) {
    return segmentAllocations.stream()
        .anyMatch(segmentAllocation -> Objects.equals(segmentAllocation.getSegment(), segment));
  }

  /**
   * Returns whether the person has a skill with the specified type.
   *
   * @param type the code for the skill type
   * @return <b>true</b> if the person has a skill with the specified type or <b>false</b> otherwise
   */
  public boolean hasSkill(String type) {
    return skills.stream().anyMatch(skill -> Objects.equals(skill.getType(), type));
  }

  /**
   * Returns whether the person has a source of funds with the specified type.
   *
   * @param type the code for the source of funds type
   * @return <b>true</b> if the person has a source of funds with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasSourceOfFundsWithType(String type) {
    return sourcesOfFunds.stream()
        .anyMatch(sourceOfFunds -> Objects.equals(sourceOfFunds.getType(), type));
  }

  /**
   * Returns whether the person has a source of wealth with the specified type.
   *
   * @param type the code for the source of wealth type
   * @return <b>true</b> if the person has a source of wealth with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasSourceOfWealthWithType(String type) {
    return sourcesOfWealth.stream()
        .anyMatch(sourceOfWealth -> Objects.equals(sourceOfWealth.getType(), type));
  }

  /**
   * Returns whether the person has a status with the specified type.
   *
   * @param type the code for the status type
   * @return <b>true</b> if the person has a status with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasStatusWithType(String type) {
    return statuses.stream().anyMatch(status -> Objects.equals(status.getType(), type));
  }

  /**
   * Returns whether the person has a tax number with the specified type.
   *
   * @param type the code for the tax number type
   * @return <b>true</b> if the person has a tax number with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasTaxNumberWithType(String type) {
    return taxNumbers.stream().anyMatch(taxNumber -> Objects.equals(taxNumber.getType(), type));
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
  public void removeAttributeWithType(String type) {
    attributes.removeIf(existingAttribute -> Objects.equals(existingAttribute.getType(), type));
  }

  /**
   * Remove the consent with the specified type for the person.
   *
   * @param type the code for the consent type
   */
  public void removeConsentWithType(String type) {
    consents.removeIf(existingConsent -> Objects.equals(existingConsent.getType(), type));
  }

  /**
   * Remove the contact mechanism with the specified role for the person.
   *
   * @param role the code for the contact mechanism role
   */
  public void removeContactMechanismWithRole(String role) {
    contactMechanisms.removeIf(
        existingContactMechanism -> Objects.equals(existingContactMechanism.getRole(), role));
  }

  /**
   * Remove the education with the specified ID for the person.
   *
   * @param id the ID for the education
   */
  public void removeEducationWithId(UUID id) {
    educations.removeIf(existingEducation -> Objects.equals(existingEducation.getId(), id));
  }

  /**
   * Remove the employment with the specified ID for the person.
   *
   * @param id the ID for the employment
   */
  public void removeEmploymentWithId(UUID id) {
    employments.removeIf(existingEmployment -> Objects.equals(existingEmployment.getId(), id));
  }

  /**
   * Remove the external reference with the specified type for the person.
   *
   * @param type the code for the external reference type
   */
  public void removeExternalReferenceWithType(String type) {
    externalReferences.removeIf(
        existingExternalReference -> Objects.equals(existingExternalReference.getType(), type));
  }

  /**
   * Remove the identity document with the specified ID for the person.
   *
   * @param id the ID for the identity document
   */
  public void removeIdentityDocumentWithId(UUID id) {
    identityDocuments.removeIf(
        existingIdentityDocument -> Objects.equals(existingIdentityDocument.getId(), id));
  }

  /**
   * Remove the identity document with the specified type for the person.
   *
   * @param type the code for the identity document type
   */
  public void removeIdentityDocumentWithType(String type) {
    identityDocuments.removeIf(
        existingIdentityDocument -> Objects.equals(existingIdentityDocument.getType(), type));
  }

  /**
   * Remove the language proficiency with the specified language for the person.
   *
   * @param language the ISO 639-1 alpha-2 code for the language
   */
  public void removeLanguageProficiencyWithLanguage(String language) {
    languageProficiencies.removeIf(
        existingLanguageProficiency ->
            Objects.equals(existingLanguageProficiency.getLanguage(), language));
  }

  /**
   * Remove the lock with the specified type for the person.
   *
   * @param type the code for the lock type
   */
  public void removeLockWithType(String type) {
    locks.removeIf(existingLock -> Objects.equals(existingLock.getType(), type));
  }

  /**
   * Remove the next of kin with the specified ID for the person.
   *
   * @param id the ID for the next of kin
   */
  public void removeNextOfKinWithId(UUID id) {
    nextOfKin.removeIf(existingNextOfKin -> Objects.equals(existingNextOfKin.getId(), id));
  }

  /**
   * Remove the next of kin with the specified type for the person.
   *
   * @param type the code for the next of kin type
   */
  public void removeNextOfKinWithType(String type) {
    nextOfKin.removeIf(existingNextOfKin -> Objects.equals(existingNextOfKin.getType(), type));
  }

  /**
   * Remove the physical address with the specified ID for the person.
   *
   * @param id the ID for the physical address
   */
  public void removePhysicalAddressWithId(UUID id) {
    physicalAddresses.removeIf(
        existingPhysicalAddress -> Objects.equals(existingPhysicalAddress.getId(), id));
  }

  /**
   * Remove any physical addresses with the specified role for the person.
   *
   * @param role the code for the physical address role
   */
  public void removePhysicalAddressWithRole(String role) {
    physicalAddresses.removeIf(
        existingPhysicalAddress -> Objects.equals(existingPhysicalAddress.getRole(), role));
  }

  /**
   * Remove the preference with the specified type for the person.
   *
   * @param type the code for the preference type
   */
  public void removePreferenceWithType(String type) {
    preferences.removeIf(existingPreference -> Objects.equals(existingPreference.getType(), type));
  }

  /**
   * Remove the residence permit with the specified ID for the person.
   *
   * @param id the ID for the residence permit
   */
  public void removeResidencePermitWithId(UUID id) {
    residencePermits.removeIf(residencePermit -> Objects.equals(residencePermit.getId(), id));
  }

  /**
   * Remove the residence permit with the specified type for the person.
   *
   * @param type the code for the residence permit type
   */
  public void removeResidencePermitWithType(String type) {
    residencePermits.removeIf(
        existingResidencePermit -> Objects.equals(existingResidencePermit.getType(), type));
  }

  /**
   * Remove the role with the specified type for the person.
   *
   * @param type the code for role type
   */
  public void removeRoleWithType(String type) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), type));
  }

  /**
   * Remove the segment allocation with the specified segment for the person.
   *
   * @param segment the code for the segment
   */
  public void removeSegmentAllocationWithSegment(String segment) {
    segmentAllocations.removeIf(
        segmentAllocation -> Objects.equals(segmentAllocation.getSegment(), segment));
  }

  /**
   * Remove the skill with the specified type for the person.
   *
   * @param type the code for the skill type
   */
  public void removeSkillWithType(String type) {
    skills.removeIf(existingSkill -> Objects.equals(existingSkill.getType(), type));
  }

  /**
   * Remove the source of funds with the specified type for the person.
   *
   * @param type the code for the source of funds type
   */
  public void removeSourceOfFundsWithType(String type) {
    sourcesOfFunds.removeIf(
        existingSourceOfFunds -> Objects.equals(existingSourceOfFunds.getType(), type));
  }

  /**
   * Remove the source of wealth with the specified type for the person.
   *
   * @param type the code for the source of wealth type
   */
  public void removeSourceOfWealthWithType(String type) {
    sourcesOfWealth.removeIf(
        existingSourceOfWealth -> Objects.equals(existingSourceOfWealth.getType(), type));
  }

  /**
   * Remove the status with the specified type for the person.
   *
   * @param type the code for the lock type
   */
  public void removeStatusWithType(String type) {
    statuses.removeIf(existingStatus -> Objects.equals(existingStatus.getType(), type));
  }

  /**
   * Remove the tax number with the specified type for the person.
   *
   * @param type the tax number type
   */
  public void removeTaxNumberWithType(String type) {
    taxNumbers.removeIf(existingTaxNumber -> Objects.equals(existingTaxNumber.getType(), type));
  }

  /**
   * Set the attributes for the person.
   *
   * @param attributes the attributes for the person
   */
  public void setAttributes(Set<Attribute> attributes) {
    attributes.forEach(attribute -> attribute.setParty(this));
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the consents provided by the person.
   *
   * @param consents the consents provided by the person
   */
  public void setConsents(Set<Consent> consents) {
    consents.forEach(consent -> consent.setPerson(this));
    this.consents.clear();
    this.consents.addAll(consents);
  }

  /**
   * Set the contact mechanisms for the person.
   *
   * @param contactMechanisms the contact mechanisms for the person
   */
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    contactMechanisms.forEach(contactMechanism -> contactMechanism.setParty(this));
    this.contactMechanisms.clear();
    this.contactMechanisms.addAll(contactMechanisms);
  }

  /**
   * Set the ISO 3166-1 alpha-2 codes for the countries of citizenship for the person.
   *
   * @param countriesOfCitizenship the ISO 3166-1 alpha-2 codes for the countries of citizenship for
   *     the person
   */
  public void setCountriesOfCitizenship(Set<String> countriesOfCitizenship) {
    this.countriesOfCitizenship =
        StringUtils.collectionToDelimitedString(countriesOfCitizenship, ",");
  }

  /**
   * Set the ISO 3166-1 alpha-2 codes for the countries of tax residence for the person.
   *
   * @param countriesOfTaxResidence the ISO 3166-1 alpha-2 codes for the countries of tax residence
   *     for the person
   */
  public void setCountriesOfTaxResidence(Set<String> countriesOfTaxResidence) {
    this.countriesOfTaxResidence =
        StringUtils.collectionToDelimitedString(countriesOfTaxResidence, ",");
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country of birth for the person.
   *
   * @param countryOfBirth the ISO 3166-1 alpha-2 code for the country of birth for the person
   */
  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  /**
   * Set the code for the single country of citizenship for the person.
   *
   * @param countryOfCitizenship the code for the single country of citizenship for the person
   */
  @JsonIgnore
  public void setCountryOfCitizenship(String countryOfCitizenship) {
    this.countriesOfCitizenship = countryOfCitizenship;
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country of residence for the person.
   *
   * @param countryOfResidence the ISO 3166-1 alpha-2 code for the country of residence for the
   *     person
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
   * Set the date of birth for the person.
   *
   * @param dateOfBirth the date of birth for the person
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Set the date of death for the person.
   *
   * @param dateOfDeath the date of death for the person
   */
  public void setDateOfDeath(LocalDate dateOfDeath) {
    this.dateOfDeath = dateOfDeath;
  }

  /**
   * Set the educations for the person.
   *
   * @param educations the educations for the person
   */
  public void setEducations(Set<Education> educations) {
    educations.forEach(education -> education.setPerson(this));
    this.educations.clear();
    this.educations.addAll(educations);
  }

  /**
   * Set the code for the employment status for the person.
   *
   * @param employmentStatus the code for the employment status for the person
   */
  public void setEmploymentStatus(String employmentStatus) {
    this.employmentStatus = employmentStatus;
  }

  /**
   * Set the code for the employment type for the person.
   *
   * @param employmentType the code for the employment type for the person
   */
  public void setEmploymentType(String employmentType) {
    this.employmentType = employmentType;
  }

  /**
   * Set the employments for the person.
   *
   * @param employments the employments for the person
   */
  public void setEmployments(Set<Employment> employments) {
    employments.forEach(employment -> employment.setPerson(this));
    this.employments.clear();
    this.employments.addAll(employments);
  }

  /**
   * Set the external references for the person.
   *
   * @param externalReferences the external references for the person
   */
  public void setExternalReferences(Set<ExternalReference> externalReferences) {
    externalReferences.forEach(externalReference -> externalReference.setParty(this));
    this.externalReferences.clear();
    this.externalReferences.addAll(externalReferences);
  }

  /**
   * Set the code for the gender for the person.
   *
   * @param gender the code for the gender for the person
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Set the given name, firstname, forename, or Christian name for the person.
   *
   * @param givenName the given name, firstname, forename, or Christian name for the person
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;

    deriveName();
  }

  /**
   * Set the code for the highest qualification type for the person.
   *
   * @param highestQualificationType the code for the highest qualification type for the person
   */
  public void setHighestQualificationType(String highestQualificationType) {
    this.highestQualificationType = highestQualificationType;
  }

  /**
   * Set the ID for the person.
   *
   * @param id the ID for the person
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
    identityDocuments.forEach(identityDocument -> identityDocument.setParty(this));
    this.identityDocuments.clear();
    this.identityDocuments.addAll(identityDocuments);
  }

  /**
   * Set the initials for the person.
   *
   * @param initials the initials for the person
   */
  public void setInitials(String initials) {
    this.initials = initials;
  }

  /**
   * Set the ISO 639-1 alpha-2 code for the language for the person.
   *
   * @param language the ISO 639-1 alpha-2 code for the language for the person
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Set the language proficiencies for the person.
   *
   * @param languageProficiencies the language proficiencies for the person
   */
  public void setLanguageProficiencies(Set<LanguageProficiency> languageProficiencies) {
    languageProficiencies.forEach(languageProficiency -> languageProficiency.setPerson(this));
    this.languageProficiencies.clear();
    this.languageProficiencies.addAll(languageProficiencies);
  }

  /**
   * Set the locks for the person.
   *
   * @param locks the locks for the person
   */
  public void setLocks(Set<Lock> locks) {
    locks.forEach(lock -> lock.setParty(this));
    this.locks.clear();
    this.locks.addAll(locks);
  }

  /**
   * Set the maiden name for the person.
   *
   * @param maidenName the maiden name for the person
   */
  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }

  /**
   * Set the code for the marital status for the person.
   *
   * @param maritalStatus the code for the marital status for the person
   */
  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  /**
   * Set the date for the marital status for the person.
   *
   * @param maritalStatusDate the date for the marital status for the person
   */
  public void setMaritalStatusDate(LocalDate maritalStatusDate) {
    this.maritalStatusDate = maritalStatusDate;
  }

  /**
   * Set the code for the marriage type for the person if the person is married.
   *
   * @param marriageType the code for the marriage type for the person if the person is married
   */
  public void setMarriageType(String marriageType) {
    this.marriageType = marriageType;
  }

  /**
   * Set the measurement system for the person.
   *
   * @param measurementSystem the measurement system for the person
   */
  public void setMeasurementSystem(MeasurementSystem measurementSystem) {
    this.measurementSystem = measurementSystem;
  }

  /**
   * Set the middle names for the person.
   *
   * @param middleNames the middle names for the person
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
   * Set the next of kin for the person.
   *
   * @param nextOfKin the next of kin for the person
   */
  public void setNextOfKin(Set<NextOfKin> nextOfKin) {
    nextOfKin.forEach(aNextOfKin -> aNextOfKin.setPerson(this));
    this.nextOfKin.clear();
    this.nextOfKin.addAll(nextOfKin);
  }

  /**
   * Set the code for the occupation for the person.
   *
   * @param occupation the code for the occupation for the person
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
    physicalAddresses.forEach(physicalAddress -> physicalAddress.setParty(this));
    this.physicalAddresses.clear();
    this.physicalAddresses.addAll(physicalAddresses);
  }

  /**
   * Set the preferences for the person.
   *
   * @param preferences the preferences for the person
   */
  public void setPreferences(Set<Preference> preferences) {
    preferences.forEach(preference -> preference.setParty(this));
    this.preferences.clear();
    this.preferences.addAll(preferences);
  }

  /**
   * Set the preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @param preferredName the preferred name for the person
   */
  public void setPreferredName(String preferredName) {
    this.preferredName = preferredName;
  }

  /**
   * Set the code for the race for the person.
   *
   * @param race the code for the race for the person
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
    preferences.forEach(residencePermit -> residencePermit.setParty(this));
    this.residencePermits.clear();
    this.residencePermits.addAll(residencePermits);
  }

  /**
   * Set the code for the residency status for the person.
   *
   * @param residencyStatus the code for the residency status for the person
   */
  public void setResidencyStatus(String residencyStatus) {
    this.residencyStatus = residencyStatus;
  }

  /**
   * Set the code for the residential type for the person.
   *
   * @param residentialType the code for the residential type for the person
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
    roles.forEach(role -> role.setParty(this));
    this.roles.clear();
    this.roles.addAll(roles);
  }

  /**
   * Set the segment allocations for the person.
   *
   * @param segmentAllocations the segment allocations for the person
   */
  public void setSegmentAllocations(Set<SegmentAllocation> segmentAllocations) {
    segmentAllocations.forEach(segmentAllocation -> segmentAllocation.setParty(this));
    this.segmentAllocations.clear();
    this.segmentAllocations.addAll(segmentAllocations);
  }

  /**
   * Set the skills for the person.
   *
   * @param skills the skills for the person
   */
  public void setSkills(Set<Skill> skills) {
    skills.forEach(skill -> skill.setPerson(this));
    this.skills.clear();
    this.skills.addAll(skills);
  }

  /**
   * Set the source of funds for the person.
   *
   * @param sourcesOfFunds the sources of funds for the person
   */
  public void setSourcesOfFunds(Set<SourceOfFunds> sourcesOfFunds) {
    sourcesOfFunds.forEach(sourceOfFunds -> sourceOfFunds.setPerson(this));
    this.sourcesOfFunds.clear();
    this.sourcesOfFunds.addAll(sourcesOfFunds);
  }

  /**
   * Set the source of wealth for the person.
   *
   * @param sourcesOfWealth the sources of wealth for the person
   */
  public void setSourcesOfWealth(Set<SourceOfWealth> sourcesOfWealth) {
    sourcesOfWealth.forEach(sourceOfWealth -> sourceOfWealth.setPerson(this));
    this.sourcesOfWealth.clear();
    this.sourcesOfWealth.addAll(sourcesOfWealth);
  }

  /**
   * Set the statuses for the person.
   *
   * @param statuses the statuses for the person
   */
  public void setStatuses(Set<Status> statuses) {
    statuses.forEach(status -> status.setParty(this));
    this.statuses.clear();
    this.statuses.addAll(statuses);
  }

  /**
   * Set the surname, last name, or family name for the person.
   *
   * @param surname the surname, last name, or family name for the person
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
    taxNumbers.forEach(taxNumber -> taxNumber.setParty(this));
    this.taxNumbers.clear();
    this.taxNumbers.addAll(taxNumbers);
  }

  /**
   * Set the ID for the tenant the person is associated with.
   *
   * @param tenantId the ID for the tenant the person is associated with
   */
  public void setTenantId(UUID tenantId) {
    super.setTenantId(tenantId);
  }

  /**
   * Set the time zone ID for the person.
   *
   * @param timeZone the time zone ID for the person
   */
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * Set the code for the title for the person.
   *
   * @param title the code for the title for the person
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /** Derive the name of the person from the given name, middle name(s) and surname. */
  private void deriveName() {
    StringBuilder derivedName = new StringBuilder();

    if (StringUtils.hasText(givenName)) {
      derivedName.append(givenName.trim());
    }

    if (StringUtils.hasText(middleNames)) {
      if (derivedName.length() > 0) {
        derivedName.append(" ").append(middleNames.trim());
      } else {
        derivedName.append(middleNames.trim());
      }
    }

    if (StringUtils.hasText(surname)) {
      if (derivedName.length() > 0) {
        derivedName.append(" ").append(surname.trim());
      } else {
        derivedName.append(surname.trim());
      }
    }

    if (derivedName.length() > 0) {
      setName(derivedName.toString());
    }
  }
}

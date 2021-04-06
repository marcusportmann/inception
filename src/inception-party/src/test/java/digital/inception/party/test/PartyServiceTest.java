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

package digital.inception.party.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.Attribute;
import digital.inception.party.Consent;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismRole;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.Education;
import digital.inception.party.Employment;
import digital.inception.party.IPartyService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.LanguageProficiency;
import digital.inception.party.LanguageProficiencyLevel;
import digital.inception.party.Lock;
import digital.inception.party.MeasurementSystem;
import digital.inception.party.MeasurementUnit;
import digital.inception.party.Organization;
import digital.inception.party.OrganizationSortBy;
import digital.inception.party.Organizations;
import digital.inception.party.Parties;
import digital.inception.party.Party;
import digital.inception.party.PartyType;
import digital.inception.party.Person;
import digital.inception.party.PersonSortBy;
import digital.inception.party.Persons;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressRole;
import digital.inception.party.PhysicalAddressType;
import digital.inception.party.Preference;
import digital.inception.party.ResidencePermit;
import digital.inception.party.Role;
import digital.inception.party.SourceOfFunds;
import digital.inception.party.SourceOfWealth;
import digital.inception.party.Status;
import digital.inception.party.TaxNumber;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>PartyServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>PartyService</b> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class PartyServiceTest {

  private static int organizationCount;

  private static int partyCount;

  private static int personCount;

  /** The Party Service. */
  @Autowired private IPartyService partyService;

  private static synchronized Organization getTestOrganizationDetails() {
    organizationCount++;

    Organization organization =
        new Organization(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "Organization Name " + organizationCount);

    organization.addIdentityDocument(
        new IdentityDocument(
            "za_company_registration", "ZA", LocalDate.of(2006, 4, 2), "2006/123456/23"));

    organization.addRole(new Role("employer"));

    return organization;
  }

  private static synchronized Organization getTestBasicOrganizationDetails() {
    organizationCount++;

    return new Organization(
        UUID.fromString("00000000-0000-0000-0000-000000000000"),
        "Organization Name " + organizationCount);
  }

  private static synchronized Party getTestPartyDetails() {
    partyCount++;

    return new Party(
        UUID.fromString("00000000-0000-0000-0000-000000000000"),
        PartyType.ORGANIZATION,
        "Party Name " + partyCount);
  }

  private static synchronized Person getTestCompletePersonDetails(boolean isMarried) {
    personCount++;

    Person person =
        new Person(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            String.format(
                "GivenName%d MiddleName%d Surname%d", personCount, personCount, personCount));

    person.setCountryOfBirth("US");
    person.setCountryOfResidence("ZA");
    person.setDateOfBirth(LocalDate.of(1976, 3, 7));
    person.setEmploymentStatus("employed");
    person.setEmploymentType("self_employed");
    person.setGender("female");
    person.setGivenName("GivenName" + personCount);
    person.setId(UuidCreator.getShortPrefixComb());
    person.setInitials("G M");
    person.setLanguage("EN");
    person.setMaidenName("MaidenName" + personCount);

    if (isMarried) {
      person.setMaritalStatus("married");
      person.setMarriageType("anc_with_accrual");
      person.setMaritalStatusDate(LocalDate.of(2015, 10, 10));
    } else {
      person.setMaritalStatus("single");
    }

    person.setMeasurementSystem(MeasurementSystem.METRIC);
    person.setMiddleNames("MiddleName" + personCount);
    person.setOccupation("professional_legal");
    person.setPreferredName("PreferredName" + personCount);
    person.setRace("white");
    person.setResidencyStatus("permanent_resident");
    person.setResidentialType("renter");
    person.setSurname("Surname" + personCount);
    person.setTimeZone("Africa/Johannesburg");
    person.setTitle("mrs");

    person.addAttribute(new Attribute("weight", 80, MeasurementUnit.METRIC_KILOGRAM));

    assertTrue(person.hasAttributeWithType("weight"));

    assertTrue(
        person.hasAttributeWithType("weight"),
        "Failed to confirm that the person has an attribute with type (weight)");

    person.setCountryOfTaxResidence("ZA");
    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27835551234"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.PERSONAL_EMAIL_ADDRESS,
            "giveName@test.com",
            "marketing"));

    Optional<ContactMechanism> contactMechanismOptional =
        person.getContactMechanismWithTypeAndPurpose(
            ContactMechanismType.EMAIL_ADDRESS, "marketing");

    if (contactMechanismOptional.isEmpty()) {
      fail(
          "Failed to retrieve the contact mechanism for the person with the type ("
              + ContactMechanismType.EMAIL_ADDRESS
              + ") and purpose (marketing)");
    }

    contactMechanismOptional =
        person.getContactMechanismWithRole(ContactMechanismRole.PERSONAL_EMAIL_ADDRESS);

    if (contactMechanismOptional.isEmpty()) {
      fail(
          "Failed to retrieve the contact mechanism for the person with the role ("
              + ContactMechanismRole.PERSONAL_EMAIL_ADDRESS
              + ")");
    }

    person.addEducation(
        new Education(
            "ZA",
            "University of the Witwatersrand",
            "bachelors_degree",
            "Bachelor of Science in Electrical Engineering",
            1998,
            "electrical_engineering"));

    person.addIdentityDocument(
        new IdentityDocument("za_id_card", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    PhysicalAddress residentialAddress =
        new PhysicalAddress(
            PhysicalAddressType.STREET,
            PhysicalAddressRole.RESIDENTIAL,
            Set.of(
                new String[] {
                  PhysicalAddressPurpose.BILLING,
                  PhysicalAddressPurpose.CORRESPONDENCE,
                  PhysicalAddressPurpose.DELIVERY
                }));
    residentialAddress.setStreetNumber("13");
    residentialAddress.setStreetName("Kraalbessie Avenue");
    residentialAddress.setSuburb("Weltevreden Park");
    residentialAddress.setCity("Johannesburg");
    residentialAddress.setRegion("GP");
    residentialAddress.setCountry("ZA");
    residentialAddress.setPostalCode("1709");

    person.addPhysicalAddress(residentialAddress);

    Optional<PhysicalAddress> physicalAddressOptional =
        person.getPhysicalAddressWithTypeAndPurpose(
            PhysicalAddressType.STREET, PhysicalAddressPurpose.CORRESPONDENCE);

    if (physicalAddressOptional.isEmpty()) {
      fail(
          "Failed to retrieve the physical address for the person with the type ("
              + PhysicalAddressType.STREET
              + ") and purpose ("
              + PhysicalAddressPurpose.CORRESPONDENCE
              + ")");
    }

    physicalAddressOptional = person.getPhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL);

    if (physicalAddressOptional.isEmpty()) {
      fail(
          "Failed to retrieve the physical address for the person with the role ("
              + PhysicalAddressRole.RESIDENTIAL
              + ")");
    }

    PhysicalAddress correspondenceAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressRole.WORK);

    correspondenceAddress.setLine1("1 Discovery Place");
    correspondenceAddress.setLine2("Sandhurst");
    correspondenceAddress.setCity("Sandton");
    correspondenceAddress.setRegion("GP");
    correspondenceAddress.setCountry("ZA");
    correspondenceAddress.setPostalCode("2194");

    person.addPhysicalAddress(correspondenceAddress);

    person.addPreference(new Preference("correspondence_language", "EN"));

    return person;
  }

  private static synchronized Person getTestForeignPersonDetails() {
    personCount++;

    Person person =
        new Person(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            String.format(
                "GivenName%d MiddleName%d Surname%d", personCount, personCount, personCount));

    person.setCountryOfBirth("ZW");
    person.setCountryOfResidence("ZA");
    person.setDateOfBirth(LocalDate.of(1976, 3, 7));
    person.setEmploymentStatus("employed");
    person.setEmploymentType("contractor");
    person.setGender("male");
    person.setGivenName("GivenName" + personCount);
    person.setId(UuidCreator.getShortPrefixComb());
    person.setInitials("G M");
    person.setLanguage("EN");
    person.setMaritalStatus("single");
    person.setOccupation("driver");
    person.setPreferredName("PreferredName" + personCount);
    person.setRace("black");
    person.setResidencyStatus("foreign_national");
    person.setResidentialType("renter");
    person.setSurname("Surname" + personCount);
    person.setTitle("mr");

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27825551234"));

    person.addIdentityDocument(
        new IdentityDocument("passport", "ZW", LocalDate.of(2010, 5, 20), "ZW1234567890"));

    person.addResidencePermit(
        new ResidencePermit(
            "za_general_work_visa", "ZA", LocalDate.of(2011, 4, 2), "AA1234567890"));

    PhysicalAddress residentialAddress =
        new PhysicalAddress(
            PhysicalAddressType.STREET,
            PhysicalAddressRole.RESIDENTIAL,
            Set.of(
                new String[] {
                  PhysicalAddressPurpose.BILLING,
                  PhysicalAddressPurpose.CORRESPONDENCE,
                  PhysicalAddressPurpose.DELIVERY
                }));
    residentialAddress.setStreetNumber("4");
    residentialAddress.setStreetName("Princess Avenue");
    residentialAddress.setSuburb("Windsor East");
    residentialAddress.setCity("Johannesburg");
    residentialAddress.setRegion("GP");
    residentialAddress.setCountry("ZA");
    residentialAddress.setPostalCode("2194");

    person.addPhysicalAddress(residentialAddress);

    return person;
  }

  private static synchronized Person getTestBasicPersonDetails() {
    personCount++;

    return new Person(
        UUID.fromString("00000000-0000-0000-0000-000000000000"), "Full Name " + personCount);
  }

  /** Test the attribute functionality. */
  @Test
  public void attributeTest() throws Exception {
    // Person attributes
    Person person = getTestBasicPersonDetails();

    person.addAttribute(new Attribute("test_attribute_name", "test_attribute_value"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareAttributes(
        person.getAttributeWithType("test_attribute_name").get(),
        retrievedPerson.getAttributeWithType("test_attribute_name").get());

    assertTrue(retrievedPerson.hasAttributeWithType("test_attribute_name"));

    person.removeAttributeWithType("test_attribute_name");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setAttributes(Set.of(new Attribute("test_attribute_name", "test_attribute_value")));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization attributes
    Organization organization = getTestBasicOrganizationDetails();

    organization.addAttribute(new Attribute("test_attribute_name", "test_attribute_value"));

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareAttributes(
        organization.getAttributeWithType("test_attribute_name").get(),
        retrievedOrganization.getAttributeWithType("test_attribute_name").get());

    assertTrue(retrievedOrganization.hasAttributeWithType("test_attribute_name"));

    organization.removeAttributeWithType("test_attribute_name");

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setAttributes(
        Set.of(new Attribute("test_attribute_name", "test_attribute_value")));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the person functionality. */
  @Test
  public void basicPersonTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27835551234"));

    person.addIdentityDocument(
        new IdentityDocument("za_id_card", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    person.addPreference(new Preference("correspondence_language", "EN"));

    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    partyService.createPerson(person);
  }

  /** Test the consent functionality. */
  @Test
  public void consentTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addConsent(new Consent("marketing"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareConsents(
        person.getConsentWithType("marketing").get(),
        retrievedPerson.getConsentWithType("marketing").get());

    assertTrue(retrievedPerson.hasConsentWithType("marketing"));

    person.removeConsentWithType("marketing");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setConsents(Set.of(new Consent("marketing", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());
  }

  /** Test the contact mechanism purpose validation functionality. */
  @Test
  public void contactMechanismPurposeValidationTest() {
    Person person = getTestBasicPersonDetails();

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER,
            "personal_mobile_number",
            "+27835551234",
            "invalid_purpose"));

    Set<ConstraintViolation<Person>> personConstraintViolations =
        partyService.validatePerson(person);

    assertEquals(
        1,
        personConstraintViolations.size(),
        "Failed to confirm that the person has an attribute with type (weight)");

    Organization organization = getTestBasicOrganizationDetails();

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.MAIN_EMAIL_ADDRESS,
            "test@test.com",
            "invalid_purpose"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(organization);

    assertEquals(
        1,
        organizationConstraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the contactMechanism functionality. */
  @Test
  public void contactMechanismTest() throws Exception {
    // Person contactMechanisms
    Person person = getTestBasicPersonDetails();

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.PERSONAL_EMAIL_ADDRESS,
            "giveName@test.com",
            "marketing"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareContactMechanisms(
        person.getContactMechanismWithRole(ContactMechanismRole.PERSONAL_EMAIL_ADDRESS).get(),
        retrievedPerson
            .getContactMechanismWithRole(ContactMechanismRole.PERSONAL_EMAIL_ADDRESS)
            .get());

    assertTrue(
        retrievedPerson.hasContactMechanismWithRole(ContactMechanismRole.PERSONAL_EMAIL_ADDRESS));

    assertTrue(retrievedPerson.hasContactMechanismWithType(ContactMechanismType.EMAIL_ADDRESS));

    person.removeContactMechanismWithRole(ContactMechanismRole.PERSONAL_EMAIL_ADDRESS);

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setContactMechanisms(
        Set.of(
            new ContactMechanism(
                ContactMechanismType.EMAIL_ADDRESS,
                ContactMechanismRole.PERSONAL_EMAIL_ADDRESS,
                "giveName@test.com",
                "marketing")));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization contactMechanisms
    Organization organization = getTestBasicOrganizationDetails();

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.MAIN_EMAIL_ADDRESS,
            "test@test.com",
            "marketing"));

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareContactMechanisms(
        organization.getContactMechanismWithRole(ContactMechanismRole.MAIN_EMAIL_ADDRESS).get(),
        retrievedOrganization
            .getContactMechanismWithRole(ContactMechanismRole.MAIN_EMAIL_ADDRESS)
            .get());

    assertTrue(
        retrievedOrganization.hasContactMechanismWithRole(ContactMechanismRole.MAIN_EMAIL_ADDRESS));

    assertTrue(
        retrievedOrganization.hasContactMechanismWithType(ContactMechanismType.EMAIL_ADDRESS));

    organization.removeContactMechanismWithRole(ContactMechanismRole.MAIN_EMAIL_ADDRESS);

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setContactMechanisms(
        Set.of(
            new ContactMechanism(
                ContactMechanismType.EMAIL_ADDRESS,
                ContactMechanismRole.MAIN_EMAIL_ADDRESS,
                "test@test.com",
                "marketing")));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the education functionality. */
  @Test
  public void educationTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    Education education = new Education();
    education.setId(UuidCreator.getShortPrefixComb());
    education.setInstitutionCountry("ZA");
    education.setInstitutionName("University of the Witwatersrand");
    education.setQualificationType("bachelors_degree");
    education.setQualificationName("Bachelor of Science in Electrical Engineering");
    education.setQualificationYear(1998);
    education.setFieldOfStudy("electrical_engineering");
    education.setFirstYearAttended(1994);
    education.setLastYearAttended(1998);

    person.addEducation(education);

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    assertTrue(retrievedPerson.getEducationWithId(education.getId()).isPresent());

    compareEducations(
        person.getEducationWithId(education.getId()).get(),
        retrievedPerson.getEducationWithId(education.getId()).get());

    person.removeEducationWithId(education.getId());

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setEducations(
        Set.of(new Education("Northcliff High School", "national_senior_certificate", 1993)));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());
  }

  /** Test the employment functionality. */
  @Test
  public void employmentTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    Employment employment = new Employment();
    employment.setId(UuidCreator.getShortPrefixComb());
    employment.setEmployerName("Discovery");
    employment.setEmployerPhoneNumber("+27 11 324 5000");
    employment.setEmployerEmailAddress("contactus@discovery.co.za");
    employment.setEmployerContactPerson("Joe Bloggs");
    employment.setEmployerAddressLine1("3rd Floor Grove");
    employment.setEmployerAddressLine2("1 Discovery Place");
    employment.setEmployerAddressSuburb("Sandhurst");
    employment.setEmployerAddressCity("Sandton");
    employment.setEmployerAddressRegion("GP");
    employment.setEmployerAddressCountry("ZA");
    employment.setEmployerAddressPostalCode("2196");
    employment.setStartDate(LocalDate.of(2017, 2, 1));
    employment.setEndDate(LocalDate.of(2020, 4, 30));
    employment.setType("full_time");
    employment.setOccupation("executive");

    person.addEmployment(employment);

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    assertTrue(retrievedPerson.getEmploymentWithId(employment.getId()).isPresent());

    compareEmployments(
        person.getEmploymentWithId(employment.getId()).get(),
        retrievedPerson.getEmploymentWithId(employment.getId()).get());

    person.removeEmploymentWithId(employment.getId());

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setEmployments(Set.of(new Employment("Absa", LocalDate.of(2020, 5, 1))));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());
  }

  /** Test the foreign person functionality. */
  @Test
  public void foreignPersonTest() throws Exception {
    Person foreignPerson = getTestForeignPersonDetails();

    partyService.createPerson(foreignPerson);

    Persons filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered persons was not retrieved");

    comparePersons(foreignPerson, filteredPersons.getPersons().get(0));

    assertTrue(foreignPerson.hasResidencePermitWithType("za_general_work_visa"));

    foreignPerson.removeResidencePermitWithType("za_general_work_visa");

    assertFalse(foreignPerson.hasResidencePermitWithType("za_general_work_visa"));

    foreignPerson.setResidencePermits(
        Set.of(
            new ResidencePermit(
                "za_critical_skills_visa", "ZA", LocalDate.of(2015, 3, 12), "CS1234567890")));

    assertTrue(foreignPerson.hasResidencePermitWithType("za_critical_skills_visa"));

    partyService.deletePerson(foreignPerson.getId());
  }

  /** Test the identityDocument functionality. */
  @Test
  public void identityDocumentTest() throws Exception {
    // Person identity documents
    Person person = getTestBasicPersonDetails();

    person.addIdentityDocument(
        new IdentityDocument("za_id_book", "ZA", LocalDate.of(2003, 4, 13), "8904085800089"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareIdentityDocuments(
        person.getIdentityDocumentWithType("za_id_book").get(),
        retrievedPerson.getIdentityDocumentWithType("za_id_book").get());

    assertTrue(retrievedPerson.hasIdentityDocumentWithType("za_id_book"));

    person.removeIdentityDocumentWithType("za_id_book");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setIdentityDocuments(
        Set.of(
            new IdentityDocument("za_id_card", "ZA", LocalDate.of(2018, 7, 16), "8904085800089")));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization identity documents
    Organization organization = getTestBasicOrganizationDetails();

    organization.addIdentityDocument(
        new IdentityDocument(
            "za_company_registration", "ZA", LocalDate.of(2006, 4, 2), "2006/123456/23"));

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareIdentityDocuments(
        organization.getIdentityDocumentWithType("za_company_registration").get(),
        retrievedOrganization.getIdentityDocumentWithType("za_company_registration").get());

    assertTrue(retrievedOrganization.hasIdentityDocumentWithType("za_company_registration"));

    organization.removeIdentityDocumentWithType("za_company_registration");

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setIdentityDocuments(
        Set.of(
            new IdentityDocument(
                "za_company_registration", "ZA", LocalDate.of(2016, 7, 21), "2016/654321/21")));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the invalid building address verification functionality. */
  @Test
  public void invalidBuildingAddressTest() {
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.BUILDING, PhysicalAddressRole.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        5,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid building address");

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.BUILDING, PhysicalAddressRole.RESIDENTIAL);
    invalidAddress.setBuildingFloor("Building Floor");
    invalidAddress.setBuildingName("Building Name");
    invalidAddress.setBuildingRoom("Building Room");
    invalidAddress.setCity("City");
    invalidAddress.setComplexName("Complex Name");
    invalidAddress.setComplexUnitNumber("Complex Unit Number");
    invalidAddress.setCountry("Country");
    invalidAddress.setFarmDescription("Farm Description");
    invalidAddress.setFarmName("Farm Name");
    invalidAddress.setFarmNumber("Farm Number");
    invalidAddress.setLine1("Line 1");
    invalidAddress.setLine2("Line 2");
    invalidAddress.setLine3("Line 3");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    constraintViolations = partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        13,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid building address");
  }

  /** Test the invalid complex address verification functionality. */
  @Test
  public void invalidComplexAddressTest() {
    // Validate an empty invalid address
    // Required: Complex Name, Complex Unit Number, Street Name, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.COMPLEX, PhysicalAddressRole.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        6,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid complex address");

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.COMPLEX, PhysicalAddressRole.RESIDENTIAL);
    invalidAddress.setBuildingFloor("Building Floor");
    invalidAddress.setBuildingName("Building Name");
    invalidAddress.setBuildingRoom("Building Room");
    invalidAddress.setCity("City");
    invalidAddress.setComplexName("Complex Name");
    invalidAddress.setComplexUnitNumber("Complex Unit Number");
    invalidAddress.setCountry("Country");
    invalidAddress.setFarmDescription("Farm Description");
    invalidAddress.setFarmName("Farm Name");
    invalidAddress.setFarmNumber("Farm Number");
    invalidAddress.setLine1("Line 1");
    invalidAddress.setLine2("Line 2");
    invalidAddress.setLine3("Line 3");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    constraintViolations = partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        14,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid complex address");
  }

  /** Test the invalid education verification functionality. */
  @Test
  public void invalidEducationTest() {
    Person person = getTestBasicPersonDetails();

    person.setHighestQualificationType("invalid_qualification_type");

    person.addEducation(
        new Education(
            "XX", null, "invalid_qualification_type", "", null, "invalid_field_of_study"));

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        7,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the person with an invalid education");
  }

  /** Test the invalid employment verification functionality. */
  @Test
  public void invalidEmploymentTest() {
    Person person = getTestBasicPersonDetails();

    person.addEmployment(
        new Employment(
            null,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "XX",
            "",
            null,
            null,
            "invalid_employment_type",
            "invalid_occupation"));

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        14,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the person with an invalid employment");
  }

  /** Test the invalid farm address verification functionality. */
  @Test
  public void invalidFarmAddressTest() {
    // Validate an empty invalid address
    // Required: Farm Number, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.FARM, PhysicalAddressRole.HOME);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        3,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid farm address");

    // Validate a fully populated invalid address
    invalidAddress = new PhysicalAddress(PhysicalAddressType.FARM, PhysicalAddressRole.HOME);
    invalidAddress.setBuildingFloor("Building Floor");
    invalidAddress.setBuildingName("Building Name");
    invalidAddress.setBuildingRoom("Building Room");
    invalidAddress.setCity("City");
    invalidAddress.setComplexName("Complex Name");
    invalidAddress.setComplexUnitNumber("Complex Unit Number");
    invalidAddress.setCountry("Country");
    invalidAddress.setFarmDescription("Farm Description");
    invalidAddress.setFarmName("Farm Name");
    invalidAddress.setFarmNumber("Farm Number");
    invalidAddress.setLine1("Line 1");
    invalidAddress.setLine2("Line 2");
    invalidAddress.setLine3("Line 3");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    constraintViolations = partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        13,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid farm address");
  }

  /** Test the invalid international address verification functionality. */
  @Test
  public void invalidInternationalAddressTest() {
    // Validate an empty invalid address
    // Required: Line 1, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.INTERNATIONAL, PhysicalAddressRole.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        3,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid international address");

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.INTERNATIONAL, PhysicalAddressRole.RESIDENTIAL);
    invalidAddress.setBuildingFloor("Building Floor");
    invalidAddress.setBuildingName("Building Name");
    invalidAddress.setBuildingRoom("Building Room");
    invalidAddress.setCity("City");
    invalidAddress.setComplexName("Complex Name");
    invalidAddress.setComplexUnitNumber("Complex Unit Number");
    invalidAddress.setCountry("Country");
    invalidAddress.setFarmDescription("Farm Description");
    invalidAddress.setFarmName("Farm Name");
    invalidAddress.setFarmNumber("Farm Number");
    invalidAddress.setLine1("Line 1");
    invalidAddress.setLine2("Line 2");
    invalidAddress.setLine3("Line 3");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    constraintViolations = partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        16,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid international address");
  }

  /** Test the invalid language proficiency verification functionality. */
  @Test
  public void invalidLanguageProficiencyTest() {
    Person person = getTestBasicPersonDetails();

    LanguageProficiency languageProficiency = new LanguageProficiency();
    languageProficiency.setLanguage("XX");

    person.addLanguageProficiency(languageProficiency);

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        5,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the person with an invalid language proficiency");
  }

  /** Test the invalid organization attribute test. */
  @Test
  public void invalidOrganizationAttributeTest() {
    Organization organization = getTestBasicOrganizationDetails();

    organization.addAttribute(new Attribute("given_name", "Given Name"));
    organization.addAttribute(new Attribute("invalid_attribute", "Invalid Attribute"));

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(organization);

    assertEquals(
        2,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the invalid person attribute functionality. */
  @Test
  public void invalidPersonAttributeTest() {
    Person person = getTestBasicPersonDetails();

    person.addAttribute(new Attribute("given_name", "Given Name"));
    person.addAttribute(new Attribute("invalid_attribute", "Invalid Attribute"));
    person.addAttribute(new Attribute("weight", 80, MeasurementUnit.CUSTOMARY_FOOT));

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        3,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person consent functionality. */
  @Test
  public void invalidPersonConsentTest() {
    Person person = getTestBasicPersonDetails();

    person.addConsent(new Consent("invalid_consent"));

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person source of funds functionality. */
  @Test
  public void invalidPersonSourceOfFundsTest() {
    Person person = getTestBasicPersonDetails();

    person.addSourceOfFunds(new SourceOfFunds("invalid_source_of_funds"));

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person source of wealth functionality. */
  @Test
  public void invalidPersonSourceOfWealthTest() {
    Person person = getTestBasicPersonDetails();

    person.addSourceOfWealth(new SourceOfWealth("invalid_source_of_wealth"));

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person time zone functionality. */
  @Test
  public void invalidPersonTimeZoneTest() {
    Person person = getTestBasicPersonDetails();

    person.setTimeZone("invalid_time_zone");

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid physical address purpose for party type verification functionality. */
  @Test
  public void invalidPhysicalAddressPurposeForPartyTypeTest() {
    Person person = getTestBasicPersonDetails();

    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.MAIN);

    invalidAddress.setStreetNumber("221B");
    invalidAddress.setStreetName("Baker Street");
    invalidAddress.setSuburb("Marylebone");
    invalidAddress.setCity("London");
    invalidAddress.setCountry("GB");
    invalidAddress.setPostalCode("NW1 6XE");

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid physical address role for the party");
  }

  /** Test the invalid physical address type verification functionality. */
  @Test
  public void invalidPhysicalAddressTypeRoleAndPurposeTest() {
    PhysicalAddress invalidAddress =
        new PhysicalAddress(
            "invalid physical address type",
            "invalid physical address role",
            "invalid physical address purpose");

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        3,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid physical address");
  }

  /** Test the invalid site address verification functionality. */
  @Test
  public void invalidSiteAddressTest() {
    // Validate an empty invalid address
    // Required: Site Block, Site Number, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.SITE, PhysicalAddressRole.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        5,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid site address");

    // Validate a fully populated invalid address
    invalidAddress = new PhysicalAddress(PhysicalAddressType.SITE, PhysicalAddressRole.RESIDENTIAL);
    invalidAddress.setBuildingFloor("Building Floor");
    invalidAddress.setBuildingName("Building Name");
    invalidAddress.setBuildingRoom("Building Room");
    invalidAddress.setCity("City");
    invalidAddress.setComplexName("Complex Name");
    invalidAddress.setComplexUnitNumber("Complex Unit Number");
    invalidAddress.setCountry("Country");
    invalidAddress.setFarmDescription("Farm Description");
    invalidAddress.setFarmName("Farm Name");
    invalidAddress.setFarmNumber("Farm Number");
    invalidAddress.setLine1("Line 1");
    invalidAddress.setLine2("Line 2");
    invalidAddress.setLine3("Line 3");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    constraintViolations = partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        14,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid site address");
  }

  /** Test the invalid street address verification functionality. */
  @Test
  public void invalidStreetAddressTest() {
    // Validate an empty invalid address
    // Required: Street Name, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        4,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid street address");

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.RESIDENTIAL);
    invalidAddress.setBuildingFloor("Building Floor");
    invalidAddress.setBuildingName("Building Name");
    invalidAddress.setBuildingRoom("Building Room");
    invalidAddress.setCity("City");
    invalidAddress.setComplexName("Complex Name");
    invalidAddress.setComplexUnitNumber("Complex Unit Number");
    invalidAddress.setCountry("Country");
    invalidAddress.setFarmDescription("Farm Description");
    invalidAddress.setFarmName("Farm Name");
    invalidAddress.setFarmNumber("Farm Number");
    invalidAddress.setLine1("Line 1");
    invalidAddress.setLine2("Line 2");
    invalidAddress.setLine3("Line 3");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    constraintViolations = partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        16,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid street address");
  }

  /** Test the invalid unstructured address verification functionality. */
  @Test
  public void invalidUnstructuredAddressTest() {
    // Validate an empty invalid address
    // Required: Line 1, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressRole.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        3,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid unstructured address");

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressRole.RESIDENTIAL);
    invalidAddress.setBuildingFloor("Building Floor");
    invalidAddress.setBuildingName("Building Name");
    invalidAddress.setBuildingRoom("Building Room");
    invalidAddress.setCity("City");
    invalidAddress.setComplexName("Complex Name");
    invalidAddress.setComplexUnitNumber("Complex Unit Number");
    invalidAddress.setCountry("Country");
    invalidAddress.setFarmDescription("Farm Description");
    invalidAddress.setFarmName("Farm Name");
    invalidAddress.setFarmNumber("Farm Number");
    invalidAddress.setLine1("Line 1");
    invalidAddress.setLine2("Line 2");
    invalidAddress.setLine3("Line 3");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    constraintViolations = partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        16,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid unstructured address");
  }

  /** Test the language proficiency functionality. */
  @Test
  public void languageProficiencyTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addLanguageProficiency(
        new LanguageProficiency(
            "EN",
            LanguageProficiencyLevel.PROFICIENT,
            LanguageProficiencyLevel.ADVANCED,
            LanguageProficiencyLevel.INTERMEDIATE,
            LanguageProficiencyLevel.ELEMENTARY));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    assertTrue(retrievedPerson.hasLanguageProficiencyWithLanguage("EN"));

    compareLanguageProficiencies(
        person.getLanguageProficiencyWithLanguage("EN").get(),
        retrievedPerson.getLanguageProficiencyWithLanguage("EN").get());

    person.removeLanguageProficiencyWithLanguage("EN");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setLanguageProficiencies(
        Set.of(
            new LanguageProficiency(
                "EN",
                LanguageProficiencyLevel.PROFICIENT,
                LanguageProficiencyLevel.ADVANCED,
                LanguageProficiencyLevel.INTERMEDIATE,
                LanguageProficiencyLevel.ELEMENTARY)));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());
  }

  /** Test the lock functionality. */
  @Test
  public void lockTest() throws Exception {
    // Person locks
    Person person = getTestBasicPersonDetails();

    person.addLock(new Lock("suspected_fraud"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareLocks(
        person.getLockWithType("suspected_fraud").get(),
        retrievedPerson.getLockWithType("suspected_fraud").get());

    assertTrue(retrievedPerson.hasLockWithType("suspected_fraud"));

    person.removeLockWithType("suspected_fraud");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setLocks(Set.of(new Lock("suspected_fraud", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization locks
    Organization organization = getTestBasicOrganizationDetails();

    organization.addLock(new Lock("suspected_fraud"));

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareLocks(
        organization.getLockWithType("suspected_fraud").get(),
        retrievedOrganization.getLockWithType("suspected_fraud").get());

    assertTrue(retrievedOrganization.hasLockWithType("suspected_fraud"));

    organization.removeLockWithType("suspected_fraud");

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setLocks(Set.of(new Lock("suspected_fraud", LocalDate.of(2016, 5, 1))));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the organization functionality. */
  @Test
  public void organizationTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    assertTrue(organization.hasIdentityDocumentWithType("za_company_registration"));

    organization.setCountryOfTaxResidence("ZA");
    organization.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    partyService.createOrganization(organization);

    Organizations filteredOrganizations =
        partyService.getOrganizations("", OrganizationSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredOrganizations.getOrganizations().size(),
        "The correct number of filtered organizations was not retrieved");

    compareOrganizations(organization, filteredOrganizations.getOrganizations().get(0));

    organization.setName(organization.getName() + " Updated");

    organization.setCountriesOfTaxResidence(Set.of("GB", "ZA"));

    organization.addContactMechanism(
        new ContactMechanism(ContactMechanismType.PHONE_NUMBER, "main_phone_number", "0115551234"));

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.MAIN_EMAIL_ADDRESS,
            "test@test.com",
            "marketing"));

    Optional<ContactMechanism> contactMechanismOptional =
        organization.getContactMechanismWithTypeAndPurpose(
            ContactMechanismType.EMAIL_ADDRESS, "marketing");

    if (contactMechanismOptional.isEmpty()) {
      fail(
          "Failed to retrieve the contact mechanism for the organization with the type ("
              + ContactMechanismType.EMAIL_ADDRESS
              + ") and purpose (marketing)");
    }

    contactMechanismOptional =
        organization.getContactMechanismWithRole(ContactMechanismRole.MAIN_EMAIL_ADDRESS);

    if (contactMechanismOptional.isEmpty()) {
      fail(
          "Failed to retrieve the contact mechanism for the organization with the role ("
              + ContactMechanismRole.MAIN_EMAIL_ADDRESS
              + ")");
    }

    PhysicalAddress mainAddress =
        new PhysicalAddress(
            PhysicalAddressType.STREET,
            PhysicalAddressRole.MAIN,
            Set.of(PhysicalAddressPurpose.CORRESPONDENCE));
    mainAddress.setStreetNumber("1");
    mainAddress.setStreetName("Discovery Place");
    mainAddress.setSuburb("Sandhurst");
    mainAddress.setCity("Sandton");
    mainAddress.setRegion("GP");
    mainAddress.setCountry("ZA");
    mainAddress.setPostalCode("2194");

    organization.addPhysicalAddress(mainAddress);

    Optional<PhysicalAddress> physicalAddressOptional =
        organization.getPhysicalAddressWithTypeAndPurpose(
            PhysicalAddressType.STREET, PhysicalAddressPurpose.CORRESPONDENCE);

    if (physicalAddressOptional.isEmpty()) {
      fail(
          "Failed to retrieve the physical address for the organization with the type ("
              + PhysicalAddressType.STREET
              + ") and purpose ("
              + PhysicalAddressPurpose.CORRESPONDENCE
              + ")");
    }

    physicalAddressOptional = organization.getPhysicalAddressWithRole(PhysicalAddressRole.MAIN);

    if (physicalAddressOptional.isEmpty()) {
      fail(
          "Failed to retrieve the physical address for the organization with the role ("
              + PhysicalAddressRole.MAIN
              + ")");
    }

    organization.addPreference(new Preference("correspondence_language", "EN"));

    organization.removeRoleWithType("employer");

    organization.addRole(new Role("vendor"));

    partyService.updateOrganization(organization);

    filteredOrganizations =
        partyService.getOrganizations("", OrganizationSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredOrganizations.getOrganizations().size(),
        "The correct number of filtered organizations was not retrieved");

    compareOrganizations(organization, filteredOrganizations.getOrganizations().get(0));

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredParties.getParties().size(),
        "The correct number of filtered parties was not retrieved");

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the party inheritance functionality. */
  @Test
  public void partyInheritanceTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    partyService.createOrganization(organization);

    Person person = getTestCompletePersonDetails(false);

    partyService.createPerson(person);

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        2,
        filteredParties.getParties().size(),
        "The correct number of filtered parties was not retrieved");

    partyService.deleteParty(person.getId());

    partyService.deleteParty(organization.getId());
  }

  /** Test the person functionality. */
  @Test
  public void personTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.setCountryOfCitizenship("ZA");

    partyService.createPerson(person);

    Persons filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered parties was not retrieved");

    comparePersons(person, filteredPersons.getPersons().get(0));

    partyService.deletePerson(person.getId());

    person = getTestCompletePersonDetails(true);

    partyService.createPerson(person);

    filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered persons was not retrieved");

    comparePersons(person, filteredPersons.getPersons().get(0));

    person.setCountriesOfCitizenship(Set.of("ZA", "GB"));
    person.setCountryOfBirth("GB");
    person.setCountryOfResidence("ZA");
    person.setCountryOfTaxResidence("GB");
    person.setDateOfBirth(LocalDate.of(1985, 5, 1));
    person.setDateOfDeath(LocalDate.of(2200, 1, 1));
    person.setEmploymentStatus("employed");
    person.setEmploymentType("full_time");
    person.setGender("female");
    person.setGivenName(person.getGivenName() + " Updated");
    person.setInitials(person.getInitials() + " Updated");
    person.setLanguage("AF");
    person.setMaidenName(person.getMaidenName() + " Updated");
    person.setMaritalStatus("divorced");
    person.setMarriageType(null);
    person.setMiddleNames(person.getMiddleNames() + " Updated");
    person.setOccupation("professional_business");
    person.setName(person.getName() + " Updated");
    person.setPreferredName(person.getPreferredName() + " Updated");
    person.setRace("black");
    person.setResidencyStatus("citizen");
    person.setResidentialType("owner");
    person.setSurname(person.getSurname() + " Updated");
    person.setTitle("ms");

    person.setCountriesOfTaxResidence(Set.of("GB", "ZA"));

    person.removeAttributeWithType("weight");

    person.addAttribute(new Attribute("height", 180, MeasurementUnit.METRIC_CENTIMETER));

    Attribute attribute = new Attribute("complex_attribute");
    attribute.setBooleanValue(true);
    attribute.setDateValue(LocalDate.now());
    attribute.setDoubleValue(123.456);
    attribute.setIntegerValue(777);
    attribute.setStringValue("String Value");

    person.addAttribute(new Attribute("height", 180, MeasurementUnit.METRIC_CENTIMETER));

    person.removeContactMechanismWithRole(ContactMechanismRole.MAIN_FAX_NUMBER);

    person
        .getContactMechanismWithRole(ContactMechanismRole.PERSONAL_EMAIL_ADDRESS)
        .get()
        .setValue("test.updated@test.com");

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.PHONE_NUMBER,
            ContactMechanismRole.HOME_PHONE_NUMBER,
            "0115551234"));

    person.addIdentityDocument(
        new IdentityDocument(
            "passport", "ZA", LocalDate.of(2016, 10, 7), LocalDate.of(2025, 9, 1), "A1234567890"));

    assertTrue(person.hasIdentityDocumentWithType("passport"));

    person.removeIdentityDocumentWithType("za_id_card");

    person.removePreferenceWithType("correspondence_language");

    person.addPreference(new Preference("time_to_contact", "anytime"));

    person.addTaxNumber(new TaxNumber("uk_tax_number", "GB", "987654321"));

    person.removeTaxNumberWithType("za_income_tax_number");

    partyService.updatePerson(person);

    filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered persons was not retrieved");

    comparePersons(person, filteredPersons.getPersons().get(0));

    filteredPersons =
        partyService.getPersons("Updated", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered persons was not retrieved");

    comparePersons(person, filteredPersons.getPersons().get(0));

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredParties.getParties().size(),
        "The correct number of filtered parties was not retrieved");

    partyService.deletePerson(person.getId());
  }

  /** Test the physical address functionality. */
  @Test
  public void physicalAddressTest() throws Exception {
    // Person physical addresses
    Person person = getTestBasicPersonDetails();

    PhysicalAddress residentialAddress =
        new PhysicalAddress(
            PhysicalAddressType.STREET,
            PhysicalAddressRole.RESIDENTIAL,
            Set.of(
                new String[] {
                  PhysicalAddressPurpose.BILLING,
                  PhysicalAddressPurpose.CORRESPONDENCE,
                  PhysicalAddressPurpose.DELIVERY
                }));
    residentialAddress.setStreetNumber("13");
    residentialAddress.setStreetName("Kraalbessie Avenue");
    residentialAddress.setSuburb("Weltevreden Park");
    residentialAddress.setCity("Johannesburg");
    residentialAddress.setRegion("GP");
    residentialAddress.setCountry("ZA");
    residentialAddress.setPostalCode("1709");

    person.addPhysicalAddress(residentialAddress);

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    comparePhysicalAddresses(
        person.getPhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL).get(),
        retrievedPerson.getPhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL).get());

    assertTrue(retrievedPerson.hasPhysicalAddressWithType(PhysicalAddressType.STREET));

    assertTrue(retrievedPerson.hasPhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL));

    person.removePhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL);

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setPhysicalAddresses(Set.of(residentialAddress));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization physical addresses
    Organization organization = getTestBasicOrganizationDetails();

    PhysicalAddress mainAddress =
        new PhysicalAddress(
            PhysicalAddressType.STREET,
            PhysicalAddressRole.MAIN,
            Set.of(PhysicalAddressPurpose.CORRESPONDENCE));
    mainAddress.setStreetNumber("1");
    mainAddress.setStreetName("Discovery Place");
    mainAddress.setSuburb("Sandhurst");
    mainAddress.setCity("Sandton");
    mainAddress.setRegion("GP");
    mainAddress.setCountry("ZA");
    mainAddress.setPostalCode("2194");

    organization.addPhysicalAddress(mainAddress);

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    comparePhysicalAddresses(
        organization.getPhysicalAddressWithRole(PhysicalAddressRole.MAIN).get(),
        retrievedOrganization.getPhysicalAddressWithRole(PhysicalAddressRole.MAIN).get());

    assertTrue(retrievedOrganization.hasPhysicalAddressWithType(PhysicalAddressType.STREET));

    assertTrue(retrievedOrganization.hasPhysicalAddressWithRole(PhysicalAddressRole.MAIN));

    organization.removePhysicalAddressWithRole(PhysicalAddressRole.MAIN);

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setPhysicalAddresses(Set.of(mainAddress));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the party physical address types functionality. */
  @Test
  public void physicalAddressTypesTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    PhysicalAddress buildingAddress =
        new PhysicalAddress(PhysicalAddressType.BUILDING, PhysicalAddressRole.RESIDENTIAL);
    buildingAddress.setBuildingName("Burj Khalifa");
    buildingAddress.setBuildingFloor("108");
    buildingAddress.setBuildingRoom("Room 1081");
    buildingAddress.setStreetNumber("1");
    buildingAddress.setStreetName("1 Mohammed Bin Rashid Boulevard");
    buildingAddress.setSuburb("Downtown Dubai");
    buildingAddress.setCity("Dubai");
    buildingAddress.setCountry("AE");
    buildingAddress.setPostalCode("800 BURJ");
    person.addPhysicalAddress(buildingAddress);

    PhysicalAddress complexAddress =
        new PhysicalAddress(PhysicalAddressType.COMPLEX, PhysicalAddressRole.RESIDENTIAL);
    complexAddress.setComplexName("Secret Hideaway");
    complexAddress.setComplexUnitNumber("10");
    complexAddress.setStreetNumber("56");
    complexAddress.setStreetName("First Road");
    complexAddress.setSuburb("Sandhurst");
    complexAddress.setCity("Sandton");
    complexAddress.setCountry("ZA");
    complexAddress.setPostalCode("2194");
    person.addPhysicalAddress(complexAddress);

    PhysicalAddress farmAddress =
        new PhysicalAddress(PhysicalAddressType.FARM, PhysicalAddressRole.RESIDENTIAL);
    farmAddress.setFarmNumber("S935");
    farmAddress.setFarmName("My Geluk");
    farmAddress.setFarmDescription("My Geluk");
    farmAddress.setCity("Koffiefontein");
    farmAddress.setRegion("FS");
    farmAddress.setCountry("ZA");
    farmAddress.setPostalCode("9986");
    person.addPhysicalAddress(farmAddress);

    PhysicalAddress internationalAddress =
        new PhysicalAddress(PhysicalAddressType.INTERNATIONAL, PhysicalAddressRole.RESIDENTIAL);
    internationalAddress.setLine1("Address Line 1");
    internationalAddress.setLine2("Address Line 2");
    internationalAddress.setLine3("Address Line 3");
    internationalAddress.setCity("Johannesburg");
    internationalAddress.setCountry("ZA");
    internationalAddress.setPostalCode("2194");
    person.addPhysicalAddress(internationalAddress);

    PhysicalAddress postalAddress =
        new PhysicalAddress(PhysicalAddressType.POSTAL, PhysicalAddressRole.POSTAL);
    postalAddress.setLine1("PO Box 12345");
    postalAddress.setSuburb("Fairland");
    postalAddress.setCity("Johannesburg");
    postalAddress.setCountry("ZA");
    postalAddress.setPostalCode("2130");
    person.addPhysicalAddress(postalAddress);

    PhysicalAddress siteAddress =
        new PhysicalAddress(PhysicalAddressType.SITE, PhysicalAddressRole.RESIDENTIAL);
    siteAddress.setSiteBlock("CC");
    siteAddress.setSiteNumber("25436");
    siteAddress.setCity("Soshanguve");
    siteAddress.setRegion("GP");
    siteAddress.setCountry("ZA");
    siteAddress.setPostalCode("0152");
    person.addPhysicalAddress(siteAddress);

    PhysicalAddress streetAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.WORK);
    streetAddress.setStreetNumber("1");
    streetAddress.setStreetName("Discovery Place");
    streetAddress.setSuburb("Sandhurst");
    streetAddress.setCity("Sandton");
    streetAddress.setRegion("GP");
    streetAddress.setCountry("ZA");
    streetAddress.setPostalCode("2194");
    person.addPhysicalAddress(streetAddress);

    PhysicalAddress unstructuredAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressRole.WORK);
    unstructuredAddress.setLine1("Address Line 1");
    unstructuredAddress.setLine2("Address Line 2");
    unstructuredAddress.setLine3("Address Line 3");
    // unstructuredAddress.setCity("Johannesburg");
    unstructuredAddress.setCountry("ZA");
    unstructuredAddress.setPostalCode("2194");
    person.addPhysicalAddress(unstructuredAddress);

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        0,
        constraintViolations.size(),
        "The correct number of constraint violations was not found");

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deleteParty(person.getId());
  }

  /** Test the preference functionality. */
  @Test
  public void preferenceTest() throws Exception {
    // Person preferences
    Person person = getTestBasicPersonDetails();

    person.addPreference(new Preference("test_preference", "test_preference_value"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    comparePreferences(
        person.getPreferenceWithType("test_preference").get(),
        retrievedPerson.getPreferenceWithType("test_preference").get());

    assertTrue(retrievedPerson.hasPreferenceWithType("test_preference"));

    person.removePreferenceWithType("test_preference");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setPreferences(Set.of(new Preference("test_preference", "test_preference_value")));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization preferences
    Organization organization = getTestBasicOrganizationDetails();

    organization.addPreference(new Preference("test_preference", "test_preference_value"));

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    comparePreferences(
        organization.getPreferenceWithType("test_preference").get(),
        retrievedOrganization.getPreferenceWithType("test_preference").get());

    assertTrue(retrievedOrganization.hasPreferenceWithType("test_preference"));

    organization.removePreferenceWithType("test_preference");

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setPreferences(Set.of(new Preference("test_preference", "test_preference_value")));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the role functionality. */
  @Test
  public void roleTest() throws Exception {
    // Person roles
    Person person = getTestBasicPersonDetails();

    person.addRole(new Role("employee"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareRoles(
        person.getRoleWithType("employee").get(),
        retrievedPerson.getRoleWithType("employee").get());

    assertTrue(retrievedPerson.hasRoleWithType("employee"));

    person.removeRoleWithType("employee");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setRoles(Set.of(new Role("employee", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization roles
    Organization organization = getTestBasicOrganizationDetails();

    organization.addRole(new Role("employer"));

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareRoles(
        organization.getRoleWithType("employer").get(),
        retrievedOrganization.getRoleWithType("employer").get());

    assertTrue(retrievedOrganization.hasRoleWithType("employer"));

    organization.removeRoleWithType("employer");

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setRoles(Set.of(new Role("employer", LocalDate.of(2016, 5, 1))));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the role type attribute type constraint functionality. */
  @Test
  public void roleTypeAttributeTypeConstraintTest() {
    Person person = getTestBasicPersonDetails();

    person.addRole(new Role("test_person_role"));

    Set<ConstraintViolation<Person>> personConstraintViolations =
        partyService.validatePerson(person);

    assertEquals(
        48,
        personConstraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");

    Organization organization = getTestBasicOrganizationDetails();

    organization.removeIdentityDocumentWithType("za_company_registration");

    organization.addRole(new Role("test_organization_role"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(organization);

    assertEquals(
        12,
        organizationConstraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the sourceOfFunds functionality. */
  @Test
  public void sourceOfFundsTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addSourceOfFunds(new SourceOfFunds("salary"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareSourcesOfFunds(
        person.getSourceOfFundsWithType("salary").get(),
        retrievedPerson.getSourceOfFundsWithType("salary").get());

    assertTrue(retrievedPerson.hasSourceOfFundsWithType("salary"));

    person.removeSourceOfFundsWithType("salary");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setSourcesOfFunds(Set.of(new SourceOfFunds("salary", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());
  }

  /** Test the sourceOfWealth functionality. */
  @Test
  public void sourceOfWealthTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addSourceOfWealth(new SourceOfWealth("employment"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareSourcesOfWealth(
        person.getSourceOfWealthWithType("employment").get(),
        retrievedPerson.getSourceOfWealthWithType("employment").get());

    assertTrue(retrievedPerson.hasSourceOfWealthWithType("employment"));

    person.removeSourceOfWealthWithType("employment");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setSourcesOfWealth(Set.of(new SourceOfWealth("employment", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());
  }

  /** Test the status functionality. */
  @Test
  public void statusTest() throws Exception {
    // Person statuses
    Person person = getTestBasicPersonDetails();

    person.addStatus(new Status("fraud_investigation"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareStatuses(
        person.getStatusWithType("fraud_investigation").get(),
        retrievedPerson.getStatusWithType("fraud_investigation").get());

    assertTrue(retrievedPerson.hasStatusWithType("fraud_investigation"));

    person.removeStatusWithType("fraud_investigation");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setStatuses(Set.of(new Status("fraud_investigation", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization statuses
    Organization organization = getTestBasicOrganizationDetails();

    organization.addStatus(new Status("fraud_investigation"));

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareStatuses(
        organization.getStatusWithType("fraud_investigation").get(),
        retrievedOrganization.getStatusWithType("fraud_investigation").get());

    assertTrue(retrievedOrganization.hasStatusWithType("fraud_investigation"));

    organization.removeStatusWithType("fraud_investigation");

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setStatuses(Set.of(new Status("fraud_investigation", LocalDate.of(2016, 5, 1))));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the tax number functionality. */
  @Test
  public void taxNumberTest() throws Exception {
    // Person tax numbers
    Person person = getTestBasicPersonDetails();

    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    compareTaxNumbers(
        person.getTaxNumberWithType("za_income_tax_number").get(),
        retrievedPerson.getTaxNumberWithType("za_income_tax_number").get());

    assertTrue(retrievedPerson.hasTaxNumberWithType("za_income_tax_number"));

    person.removeTaxNumberWithType("za_income_tax_number");

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    person.setTaxNumbers(Set.of(new TaxNumber("za_income_tax_number", "ZA", "987654321")));

    partyService.updatePerson(person);

    retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(person.getId());

    // Organization tax numbers
    Organization organization = getTestBasicOrganizationDetails();

    organization.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    partyService.createOrganization(organization);

    Organization retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareTaxNumbers(
        organization.getTaxNumberWithType("za_income_tax_number").get(),
        retrievedOrganization.getTaxNumberWithType("za_income_tax_number").get());

    assertTrue(retrievedOrganization.hasTaxNumberWithType("za_income_tax_number"));

    organization.removeTaxNumberWithType("za_income_tax_number");

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setTaxNumbers(Set.of(new TaxNumber("za_income_tax_number", "ZA", "987654321")));

    partyService.updateOrganization(organization);

    retrievedOrganization = partyService.getOrganization(organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(organization.getId());
  }

  /** Test the organization validation functionality. */
  @Test
  public void validateOrganizationTest() {
    Organization organization = getTestOrganizationDetails();

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(organization);

    if (constraintViolations.size() > 0) {
      fail("Failed to successfully validate the organization");
    }
  }

  /** Test the party validation functionality. */
  @Test
  public void validatePartyTest() {
    Party party = getTestPartyDetails();

    Set<ConstraintViolation<Party>> constraintViolations = partyService.validateParty(party);

    if (constraintViolations.size() > 0) {
      fail("Failed to successfully validate the party");
    }
  }

  /** Test the person validation functionality. */
  @Test
  public void validatePersonTest() {
    Person person = getTestCompletePersonDetails(true);

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    if (constraintViolations.size() > 0) {
      fail("Failed to successfully validate the person");
    }
  }

  private void compareAttributes(Attribute attribute1, Attribute attribute2) {
    assertEquals(
        attribute1.getType(),
        attribute2.getType(),
        "The type values for the attributes do not match");
    assertEquals(
        attribute1.getBooleanValue(),
        attribute2.getBooleanValue(),
        "The boolean value values for the attributes do not match");
    assertEquals(
        attribute1.getDateValue(),
        attribute2.getDateValue(),
        "The date value values for the attributes do not match");
    assertEquals(
        attribute1.getDoubleValue(),
        attribute2.getDoubleValue(),
        "The double value values for the attributes do not match");
    assertEquals(
        attribute1.getIntegerValue(),
        attribute2.getIntegerValue(),
        "The integer value values for the attributes do not match");
    assertEquals(
        attribute1.getStringValue(),
        attribute2.getStringValue(),
        "The string value values for the attributes do not match");
    assertEquals(
        attribute1.getUnit(),
        attribute2.getUnit(),
        "The unit values for the attributes do not match");
  }

  private void compareConsents(Consent consent1, Consent consent2) {
    assertEquals(
        consent1.getEffectiveFrom(),
        consent2.getEffectiveFrom(),
        "The effective from values for the consents do not match");
    assertEquals(
        consent1.getEffectiveTo(),
        consent2.getEffectiveTo(),
        "The effective to values for the consents do not match");
    assertEquals(
        consent1.getPerson(), consent2.getPerson(), "The persons for the consents do not match");
    assertEquals(consent1.getType(), consent2.getType(), "The types for the consents do not match");
  }

  private void compareContactMechanisms(
      ContactMechanism contactMechanism1, ContactMechanism contactMechanism2) {
    assertEquals(
        contactMechanism1.getParty(),
        contactMechanism2.getParty(),
        "The party values for the contact mechanisms do not match");
    assertEquals(
        contactMechanism1.getPurposes(),
        contactMechanism2.getPurposes(),
        "The purpose values for the contact mechanisms do not match");
    assertEquals(
        contactMechanism1.getRole(),
        contactMechanism2.getRole(),
        "The role values for the contact mechanisms do not match");
    assertEquals(
        contactMechanism1.getType(),
        contactMechanism2.getType(),
        "The type values for the contact mechanisms do not match");
    assertEquals(
        contactMechanism1.getValue(),
        contactMechanism2.getValue(),
        "The value values for the contact mechanisms do not match");
  }

  private void compareEducations(Education education1, Education education2) {
    assertEquals(
        education1.getFieldOfStudy(),
        education2.getFieldOfStudy(),
        "The field of study values for the educations do not match");
    assertEquals(
        education1.getFirstYearAttended(),
        education2.getFirstYearAttended(),
        "The first year attended values for the educations do not match");
    assertEquals(
        education1.getId(), education2.getId(), "The ID values for the educations do not match");
    assertEquals(
        education1.getInstitutionCountry(),
        education2.getInstitutionCountry(),
        "The institution country values for the educations do not match");
    assertEquals(
        education1.getInstitutionName(),
        education2.getInstitutionName(),
        "The institution name values for the educations do not match");
    assertEquals(
        education1.getLastYearAttended(),
        education2.getLastYearAttended(),
        "The last year attended values for the educations do not match");
    assertEquals(
        education1.getPerson(),
        education2.getPerson(),
        "The person values for the educations do not match");
    assertEquals(
        education1.getQualificationName(),
        education2.getQualificationName(),
        "The qualification name values for the educations do not match");
    assertEquals(
        education1.getQualificationType(),
        education2.getQualificationType(),
        "The qualification type values for the educations do not match");
    assertEquals(
        education1.getQualificationYear(),
        education2.getQualificationYear(),
        "The qualification year values for the educations do not match");
  }

  private void compareEmployments(Employment employment1, Employment employment2) {
    assertEquals(
        employment1.getEmployerAddressCity(),
        employment2.getEmployerAddressCity(),
        "The employer address city values for the employments do not match");
    assertEquals(
        employment1.getEmployerAddressCountry(),
        employment2.getEmployerAddressCountry(),
        "The employer address country values for the employments do not match");
    assertEquals(
        employment1.getEmployerAddressLine1(),
        employment2.getEmployerAddressLine1(),
        "The employer address line 1 values for the employments do not match");
    assertEquals(
        employment1.getEmployerAddressLine2(),
        employment2.getEmployerAddressLine2(),
        "The employer address line 2 values for the employments do not match");
    assertEquals(
        employment1.getEmployerAddressLine3(),
        employment2.getEmployerAddressLine3(),
        "The employer address line 3 values for the employments do not match");
    assertEquals(
        employment1.getEmployerAddressPostalCode(),
        employment2.getEmployerAddressPostalCode(),
        "The employer address postal code values for the employments do not match");
    assertEquals(
        employment1.getEmployerAddressRegion(),
        employment2.getEmployerAddressRegion(),
        "The employer address region values for the employments do not match");
    assertEquals(
        employment1.getEmployerAddressSuburb(),
        employment2.getEmployerAddressSuburb(),
        "The employer address suburb values for the employments do not match");
    assertEquals(
        employment1.getEmployerContactPerson(),
        employment2.getEmployerContactPerson(),
        "The employer contact person values for the employments do not match");
    assertEquals(
        employment1.getEmployerEmailAddress(),
        employment2.getEmployerEmailAddress(),
        "The employer e-mail address values for the employments do not match");
    assertEquals(
        employment1.getEmployerName(),
        employment2.getEmployerName(),
        "The employer name values for the employments do not match");
    assertEquals(
        employment1.getEmployerPhoneNumber(),
        employment2.getEmployerPhoneNumber(),
        "The employer phone number values for the employments do not match");
    assertEquals(
        employment1.getEndDate(),
        employment2.getEndDate(),
        "The end date values for the employments do not match");
    assertEquals(
        employment1.getId(), employment2.getId(), "The ID values for the employments do not match");
    assertEquals(
        employment1.getOccupation(),
        employment2.getOccupation(),
        "The occupation values for the employments do not match");
    assertEquals(
        employment1.getPerson(),
        employment2.getPerson(),
        "The person values for the employments do not match");
    assertEquals(
        employment1.getStartDate(),
        employment2.getStartDate(),
        "The start date values for the employments do not match");
    assertEquals(
        employment1.getType(),
        employment2.getType(),
        "The type values for the employments do not match");
  }

  private void compareIdentityDocuments(
      IdentityDocument identityDocument1, IdentityDocument identityDocument2) {
    assertEquals(
        identityDocument1.getCountryOfIssue(),
        identityDocument2.getCountryOfIssue(),
        "The country of issue values for the identity documents do not match");
    assertEquals(
        identityDocument1.getDateOfExpiry(),
        identityDocument2.getDateOfExpiry(),
        "The date of expiry values for the identity documents do not match");
    assertEquals(
        identityDocument1.getDateOfIssue(),
        identityDocument2.getDateOfIssue(),
        "The date of issue values for the identity documents do not match");
    assertEquals(
        identityDocument1.getDateProvided(),
        identityDocument2.getDateProvided(),
        "The date provided values for the identity documents do not match");
    assertEquals(
        identityDocument1.getId(),
        identityDocument2.getId(),
        "The ID values for the identity documents do not match");
    assertEquals(
        identityDocument1.getNumber(),
        identityDocument2.getNumber(),
        "The number values for the identity documents do not match");
    assertEquals(
        identityDocument1.getParty(),
        identityDocument2.getParty(),
        "The party values for the identity documents do not match");
    assertEquals(
        identityDocument1.getType(),
        identityDocument2.getType(),
        "The type values for the identity documents do not match");
  }

  private void compareLanguageProficiencies(
      LanguageProficiency languageProficiency1, LanguageProficiency languageProficiency2) {
    assertEquals(
        languageProficiency1.getLanguage(),
        languageProficiency2.getLanguage(),
        "The language values for the language proficiencies do not match");
    assertEquals(
        languageProficiency1.getListenLevel(),
        languageProficiency2.getListenLevel(),
        "The listen level values for the language proficiencies do not match");
    assertEquals(
        languageProficiency1.getPerson(),
        languageProficiency2.getPerson(),
        "The person values for the language proficiencies do not match");
    assertEquals(
        languageProficiency1.getReadLevel(),
        languageProficiency2.getReadLevel(),
        "The read level values for the language proficiencies do not match");
    assertEquals(
        languageProficiency1.getSpeakLevel(),
        languageProficiency2.getSpeakLevel(),
        "The speak level values for the language proficiencies do not match");
    assertEquals(
        languageProficiency1.getWriteLevel(),
        languageProficiency2.getWriteLevel(),
        "The write level values for the language proficiencies do not match");
  }

  private void compareLocks(Lock lock1, Lock lock2) {
    assertEquals(
        lock1.getEffectiveFrom(),
        lock2.getEffectiveFrom(),
        "The effective from values for the locks do not match");
    assertEquals(
        lock1.getEffectiveTo(),
        lock2.getEffectiveTo(),
        "The effective to values for the locks do not match");
    assertEquals(lock1.getParty(), lock2.getParty(), "The party values for the locks do not match");
    assertEquals(lock1.getType(), lock2.getType(), "The type values for the locks do not match");
  }

  private void compareOrganizations(Organization organization1, Organization organization2) {
    assertEquals(
        organization1.getCountriesOfTaxResidence(),
        organization2.getCountriesOfTaxResidence(),
        "The countries of tax residence values for the organizations do not match");
    assertEquals(
        organization1.getId(),
        organization2.getId(),
        "The ID values for the organizations do not match");
    assertEquals(
        organization1.getName(),
        organization2.getName(),
        "The name values for the organizations do not match");
    assertEquals(
        organization1.getTenantId(),
        organization2.getTenantId(),
        "The tenant ID values for the organizations do not match");

    assertEquals(
        organization1.getAttributes().size(),
        organization2.getAttributes().size(),
        "The number of attributes for the organizations do not match");

    for (Attribute organization1Attribute : organization1.getAttributes()) {
      boolean foundAttribute = false;

      for (Attribute organization2Attribute : organization2.getAttributes()) {

        if (Objects.equals(organization1Attribute.getParty(), organization2Attribute.getParty())
            && Objects.equals(organization1Attribute.getType(), organization2Attribute.getType())) {

          compareAttributes(organization1Attribute, organization2Attribute);

          foundAttribute = true;
        }
      }

      if (!foundAttribute) {
        fail("Failed to find the attribute (" + organization1Attribute.getType() + ")");
      }
    }

    assertEquals(
        organization1.getContactMechanisms().size(),
        organization2.getContactMechanisms().size(),
        "The number of contact mechanisms for the organizations do not match");

    for (ContactMechanism organization1ContactMechanism : organization1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism organization2ContactMechanism : organization2.getContactMechanisms()) {
        if (Objects.equals(
                organization1ContactMechanism.getParty(), organization2ContactMechanism.getParty())
            && Objects.equals(
                organization1ContactMechanism.getRole(), organization2ContactMechanism.getRole())) {

          compareContactMechanisms(organization1ContactMechanism, organization2ContactMechanism);

          foundContactMechanism = true;
        }
      }

      if (!foundContactMechanism) {
        fail(
            "Failed to find the contact mechanism ("
                + organization1ContactMechanism.getType()
                + ")("
                + organization1ContactMechanism.getRole()
                + ")");
      }
    }

    assertEquals(
        organization1.getIdentityDocuments().size(),
        organization2.getIdentityDocuments().size(),
        "The number of identity documents for the organizations do not match");

    for (IdentityDocument organization1IdentityDocument : organization1.getIdentityDocuments()) {
      boolean foundIdentityDocument = false;

      for (IdentityDocument organization2IdentityDocument : organization2.getIdentityDocuments()) {
        if (organization1IdentityDocument.getId().equals(organization2IdentityDocument.getId())) {

          compareIdentityDocuments(organization1IdentityDocument, organization2IdentityDocument);

          foundIdentityDocument = true;
        }
      }

      if (!foundIdentityDocument) {
        fail(
            "Failed to find the identity document ("
                + organization1IdentityDocument.getType()
                + ")("
                + organization1IdentityDocument.getCountryOfIssue()
                + ")("
                + organization1IdentityDocument.getDateOfIssue()
                + ")");
      }
    }

    assertEquals(
        organization1.getLocks().size(),
        organization2.getLocks().size(),
        "The number of locks for the organizations do not match");

    for (Lock organization1Lock : organization1.getLocks()) {
      boolean foundLock = false;

      for (Lock organization2Lock : organization2.getLocks()) {
        if (Objects.equals(organization1Lock.getParty(), organization2Lock.getParty())
            && Objects.equals(organization1Lock.getType(), organization2Lock.getType())) {

          compareLocks(organization1Lock, organization2Lock);

          foundLock = true;
        }
      }

      if (!foundLock) {
        fail("Failed to find the lock (" + organization1Lock.getType() + ")");
      }
    }

    assertEquals(
        organization1.getPhysicalAddresses().size(),
        organization2.getPhysicalAddresses().size(),
        "The number of physical addresses for the organizations do not match");

    for (PhysicalAddress organization1PhysicalAddress : organization1.getPhysicalAddresses()) {
      boolean foundPhysicalAddress = false;

      for (PhysicalAddress organization2PhysicalAddress : organization2.getPhysicalAddresses()) {
        if (Objects.equals(
                organization1PhysicalAddress.getParty(), organization2PhysicalAddress.getParty())
            && Objects.equals(
                organization1PhysicalAddress.getId(), organization2PhysicalAddress.getId())) {

          comparePhysicalAddresses(organization1PhysicalAddress, organization2PhysicalAddress);

          foundPhysicalAddress = true;
        }
      }

      if (!foundPhysicalAddress) {
        fail("Failed to find the physical address (" + organization1PhysicalAddress.getId() + ")");
      }
    }

    assertEquals(
        organization1.getPreferences().size(),
        organization2.getPreferences().size(),
        "The number of preferences for the organizations do not match");

    for (Preference organization1Preference : organization1.getPreferences()) {
      boolean foundPreference = false;

      for (Preference organization2Preference : organization2.getPreferences()) {
        if (Objects.equals(organization1Preference.getParty(), organization2Preference.getParty())
            && Objects.equals(
                organization1Preference.getType(), organization2Preference.getType())) {

          comparePreferences(organization1Preference, organization2Preference);

          foundPreference = true;
        }
      }

      if (!foundPreference) {
        fail("Failed to find the preference (" + organization1Preference.getType() + ")");
      }
    }

    assertEquals(
        organization1.getStatuses().size(),
        organization2.getStatuses().size(),
        "The number of statuses for the organizations do not match");

    for (Status organization1Status : organization1.getStatuses()) {
      boolean foundStatus = false;

      for (Status organization2Status : organization2.getStatuses()) {
        if (Objects.equals(organization1Status.getParty(), organization2Status.getParty())
            && Objects.equals(organization1Status.getType(), organization2Status.getType())) {

          compareStatuses(organization1Status, organization2Status);

          foundStatus = true;
        }
      }

      if (!foundStatus) {
        fail("Failed to find the status (" + organization1Status.getType() + ")");
      }
    }

    assertEquals(
        organization1.getTaxNumbers().size(),
        organization2.getTaxNumbers().size(),
        "The number of tax numbers for the organizations do not match");

    for (TaxNumber organization1TaxNumber : organization1.getTaxNumbers()) {
      boolean foundTaxNumber = false;

      for (TaxNumber organization2TaxNumber : organization2.getTaxNumbers()) {
        if (organization1TaxNumber.getId().equals(organization2TaxNumber.getId())) {

          assertEquals(
              organization1TaxNumber.getCountryOfIssue(),
              organization2TaxNumber.getCountryOfIssue(),
              "The country of issue values for the tax numbers do not match");
          assertEquals(
              organization1TaxNumber.getNumber(),
              organization2TaxNumber.getNumber(),
              "The number values for the tax numbers do not match");
          assertEquals(
              organization1TaxNumber.getType(),
              organization2TaxNumber.getType(),
              "The type values for the tax numbers do not match");

          foundTaxNumber = true;
        }
      }

      if (!foundTaxNumber) {
        fail("Failed to find the tax number (" + organization1TaxNumber.getType() + ")");
      }
    }
  }

  private void compareParties(Party party1, Party party2) {
    assertEquals(party1.getId(), party2.getId(), "The ID values for the parties do not match");
    assertEquals(
        party1.getTenantId(),
        party2.getTenantId(),
        "The tenant ID values for the parties do not match");
    assertEquals(
        party1.getType(), party2.getType(), "The type values for the parties do not match");
    assertEquals(
        party1.getName(), party2.getName(), "The name values for the parties do not match");
  }

  private void comparePersons(Person person1, Person person2) {
    assertEquals(
        person1.getCountriesOfCitizenship(),
        person2.getCountriesOfCitizenship(),
        "The countries of citizenship values for the persons do not match");
    assertEquals(
        person1.getCountriesOfTaxResidence(),
        person2.getCountriesOfTaxResidence(),
        "The countries of tax residence values for the persons do not match");
    assertEquals(
        person1.getCountryOfBirth(),
        person2.getCountryOfBirth(),
        "The country of birth values for the persons do not match");
    assertEquals(
        person1.getCountryOfResidence(),
        person2.getCountryOfResidence(),
        "The country of residence values for the persons do not match");
    assertEquals(
        person1.getDateOfBirth(),
        person2.getDateOfBirth(),
        "The date of birth values for the persons do not match");
    assertEquals(
        person1.getDateOfDeath(),
        person2.getDateOfDeath(),
        "The date of death values for the persons do not match");
    assertEquals(
        person1.getEmploymentStatus(),
        person2.getEmploymentStatus(),
        "The employment status values for the persons do not match");
    assertEquals(
        person1.getEmploymentType(),
        person2.getEmploymentType(),
        "The employment type values for the persons do not match");
    assertEquals(
        person1.getGender(), person2.getGender(), "The gender values for the persons do not match");
    assertEquals(
        person1.getGivenName(),
        person2.getGivenName(),
        "The given name values for the persons do not match");
    assertEquals(person1.getId(), person2.getId(), "The ID values for the persons do not match");
    assertEquals(
        person1.getInitials(),
        person2.getInitials(),
        "The initials values for the persons do not match");
    assertEquals(
        person1.getLanguage(),
        person2.getLanguage(),
        "The language values for the persons do not match");
    assertEquals(
        person1.getMaidenName(),
        person2.getMaidenName(),
        "The maiden name values for the persons do not match");
    assertEquals(
        person1.getMaritalStatus(),
        person2.getMaritalStatus(),
        "The marital status values for the persons do not match");
    assertEquals(
        person1.getMaritalStatusDate(),
        person2.getMaritalStatusDate(),
        "The marital status date values for the persons do not match");
    assertEquals(
        person1.getMarriageType(),
        person2.getMarriageType(),
        "The marriage type values for the persons do not match");
    assertEquals(
        person1.getMeasurementSystem(),
        person2.getMeasurementSystem(),
        "The measurement system values for the persons do not match");
    assertEquals(
        person1.getMiddleNames(),
        person2.getMiddleNames(),
        "The middle names values for the persons do not match");
    assertEquals(
        person1.getName(), person2.getName(), "The name values for the persons do not match");
    assertEquals(
        person1.getOccupation(),
        person2.getOccupation(),
        "The occupation values for the persons do not match");
    assertEquals(
        person1.getPreferredName(),
        person2.getPreferredName(),
        "The preferred name values for the persons do not match");
    assertEquals(
        person1.getRace(), person2.getRace(), "The race values for the persons do not match");
    assertEquals(
        person1.getResidencyStatus(),
        person2.getResidencyStatus(),
        "The residency status values for the persons do not match");
    assertEquals(
        person1.getResidentialType(),
        person2.getResidentialType(),
        "The residential type values for the persons do not match");
    assertEquals(
        person1.getSurname(),
        person2.getSurname(),
        "The surname values for the persons do not match");
    assertEquals(
        person1.getTenantId(),
        person2.getTenantId(),
        "The tenant ID values for the persons do not match");
    assertEquals(
        person1.getTimeZone(),
        person2.getTimeZone(),
        "The time zone values for the persons do not match");
    assertEquals(
        person1.getTitle(), person2.getTitle(), "The title values for the persons do not match");

    assertEquals(
        person1.getAttributes().size(),
        person2.getAttributes().size(),
        "The number of attributes for the persons do not match");

    for (Attribute person1Attribute : person1.getAttributes()) {
      boolean foundAttribute = false;

      for (Attribute person2Attribute : person2.getAttributes()) {
        if (Objects.equals(person1Attribute.getParty(), person2Attribute.getParty())
            && Objects.equals(person1Attribute.getType(), person2Attribute.getType())) {

          compareAttributes(person1Attribute, person2Attribute);

          foundAttribute = true;
        }
      }

      if (!foundAttribute) {
        fail("Failed to find the attribute (" + person1Attribute.getType() + ")");
      }
    }

    assertEquals(
        person1.getConsents().size(),
        person2.getConsents().size(),
        "The number of consents for the persons do not match");

    for (Consent person1Consent : person1.getConsents()) {
      boolean foundConsent = false;

      for (Consent person2Consent : person2.getConsents()) {
        if (Objects.equals(person1Consent.getPerson(), person2Consent.getPerson())
            && Objects.equals(person1Consent.getType(), person2Consent.getType())) {

          compareConsents(person1Consent, person2Consent);

          foundConsent = true;
        }
      }

      if (!foundConsent) {
        fail("Failed to find the consent (" + person1Consent.getType() + ")");
      }
    }

    assertEquals(
        person1.getContactMechanisms().size(),
        person2.getContactMechanisms().size(),
        "The number of contact mechanisms for the persons do not match");

    for (ContactMechanism person1ContactMechanism : person1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism person2ContactMechanism : person2.getContactMechanisms()) {
        if (Objects.equals(person1ContactMechanism.getParty(), person2ContactMechanism.getParty())
            && Objects.equals(
                person1ContactMechanism.getRole(), person2ContactMechanism.getRole())) {

          compareContactMechanisms(person1ContactMechanism, person2ContactMechanism);

          foundContactMechanism = true;
        }
      }

      if (!foundContactMechanism) {
        fail(
            "Failed to find the contact mechanism ("
                + person1ContactMechanism.getType()
                + ")("
                + person1ContactMechanism.getRole()
                + ")");
      }
    }

    assertEquals(
        person1.getEducations().size(),
        person2.getEducations().size(),
        "The number of educations for the persons do not match");

    for (Education person1Education : person1.getEducations()) {
      boolean foundEducation = false;

      for (Education person2Education : person2.getEducations()) {
        if (person1Education.getId().equals(person2Education.getId())) {

          compareEducations(person1Education, person2Education);

          foundEducation = true;
        }
      }

      if (!foundEducation) {
        fail("Failed to find the education (" + person1Education.getId() + ")");
      }
    }

    assertEquals(
        person1.getEmployments().size(),
        person2.getEmployments().size(),
        "The number of employments for the persons do not match");

    for (Employment person1Employment : person1.getEmployments()) {
      boolean foundEmployment = false;

      for (Employment person2Employment : person2.getEmployments()) {
        if (person1Employment.getId().equals(person2Employment.getId())) {

          compareEmployments(person1Employment, person2Employment);

          foundEmployment = true;
        }
      }

      if (!foundEmployment) {
        fail("Failed to find the employment (" + person1Employment.getId() + ")");
      }
    }

    assertEquals(
        person1.getIdentityDocuments().size(),
        person2.getIdentityDocuments().size(),
        "The number of identity documents for the persons do not match");

    for (IdentityDocument person1IdentityDocument : person1.getIdentityDocuments()) {
      boolean foundIdentityDocument = false;

      for (IdentityDocument person2IdentityDocument : person2.getIdentityDocuments()) {
        if (person1IdentityDocument.getId().equals(person2IdentityDocument.getId())) {

          compareIdentityDocuments(person1IdentityDocument, person2IdentityDocument);

          foundIdentityDocument = true;
        }
      }

      if (!foundIdentityDocument) {
        fail("Failed to find the identity document (" + person1IdentityDocument.getId() + ")");
      }
    }

    assertEquals(
        person1.getLocks().size(),
        person2.getLocks().size(),
        "The number of locks for the persons do not match");

    for (Lock person1Lock : person1.getLocks()) {
      boolean foundLock = false;

      for (Lock person2Lock : person2.getLocks()) {
        if (Objects.equals(person1Lock.getParty(), person2Lock.getParty())
            && Objects.equals(person1Lock.getType(), person2Lock.getType())) {

          compareLocks(person1Lock, person2Lock);

          foundLock = true;
        }
      }

      if (!foundLock) {
        fail("Failed to find the lock (" + person1Lock.getType() + ")");
      }
    }

    assertEquals(
        person1.getPhysicalAddresses().size(),
        person2.getPhysicalAddresses().size(),
        "The number of physical addresses for the persons do not match");

    for (PhysicalAddress person1PhysicalAddress : person1.getPhysicalAddresses()) {
      boolean foundPhysicalAddress = false;

      for (PhysicalAddress person2PhysicalAddress : person2.getPhysicalAddresses()) {
        if (Objects.equals(person1PhysicalAddress.getParty(), person2PhysicalAddress.getParty())
            && Objects.equals(person1PhysicalAddress.getId(), person2PhysicalAddress.getId())) {

          comparePhysicalAddresses(person1PhysicalAddress, person2PhysicalAddress);

          foundPhysicalAddress = true;
        }
      }

      if (!foundPhysicalAddress) {
        fail("Failed to find the physical address (" + person1PhysicalAddress.getId() + ")");
      }
    }

    assertEquals(
        person1.getPreferences().size(),
        person2.getPreferences().size(),
        "The number of preferences for the persons do not match");

    for (Preference person1Preference : person1.getPreferences()) {
      boolean foundPreference = false;

      for (Preference person2Preference : person2.getPreferences()) {
        if (Objects.equals(person1Preference.getParty(), person2Preference.getParty())
            && Objects.equals(person1Preference.getType(), person2Preference.getType())) {

          comparePreferences(person1Preference, person2Preference);

          foundPreference = true;
        }
      }

      if (!foundPreference) {
        fail("Failed to find the preference (" + person1Preference.getType() + ")");
      }
    }

    assertEquals(
        person1.getResidencePermits().size(),
        person2.getResidencePermits().size(),
        "The number of residence permits for the persons do not match");

    for (ResidencePermit person1ResidencePermit : person1.getResidencePermits()) {
      boolean foundResidencePermit = false;

      for (ResidencePermit person2ResidencePermit : person2.getResidencePermits()) {
        if (person1ResidencePermit.getId().equals(person2ResidencePermit.getId())) {

          compareResidencePermits(person1ResidencePermit, person2ResidencePermit);

          foundResidencePermit = true;
        }
      }

      if (!foundResidencePermit) {
        fail("Failed to find the residence permit (" + person1ResidencePermit.getId() + ")");
      }
    }

    assertEquals(
        person1.getRoles().size(),
        person2.getRoles().size(),
        "The number of roles for the persons do not match");

    for (Role person1Role : person1.getRoles()) {
      boolean foundRole = false;

      for (Role person2Role : person2.getRoles()) {
        if (person1Role.getType().equals(person2Role.getType())) {

          assertEquals(
              person1Role.getPurpose(),
              person2Role.getPurpose(),
              "The purpose values for the roles do not match");

          foundRole = true;
        }
      }

      if (!foundRole) {
        fail("Failed to find the role (" + person1Role.getType() + ")");
      }
    }

    assertEquals(
        person1.getStatuses().size(),
        person2.getStatuses().size(),
        "The number of statuses for the persons do not match");

    for (Status person1Status : person1.getStatuses()) {
      boolean foundStatus = false;

      for (Status person2Status : person2.getStatuses()) {
        if (Objects.equals(person1Status.getParty(), person2Status.getParty())
            && Objects.equals(person1Status.getType(), person2Status.getType())) {

          compareStatuses(person1Status, person2Status);

          foundStatus = true;
        }
      }

      if (!foundStatus) {
        fail("Failed to find the status (" + person1Status.getType() + ")");
      }
    }

    assertEquals(
        person1.getTaxNumbers().size(),
        person2.getTaxNumbers().size(),
        "The number of tax numbers for the persons do not match");

    for (TaxNumber person1TaxNumber : person1.getTaxNumbers()) {
      boolean foundTaxNumber = false;

      for (TaxNumber person2TaxNumber : person2.getTaxNumbers()) {
        if (person1TaxNumber.getId().equals(person2TaxNumber.getId())) {

          compareTaxNumbers(person1TaxNumber, person2TaxNumber);

          foundTaxNumber = true;
        }
      }

      if (!foundTaxNumber) {
        fail("Failed to find the tax number (" + person1TaxNumber.getType() + ")");
      }
    }
  }

  private void comparePhysicalAddresses(
      PhysicalAddress physicalAddress1, PhysicalAddress physicalAddress2) {
    assertEquals(
        physicalAddress1.getBuildingFloor(),
        physicalAddress2.getBuildingFloor(),
        "The building floor values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getBuildingName(),
        physicalAddress2.getBuildingName(),
        "The building name values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getBuildingRoom(),
        physicalAddress2.getBuildingRoom(),
        "The building room values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getCity(),
        physicalAddress2.getCity(),
        "The city values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getComplexName(),
        physicalAddress2.getComplexName(),
        "The complex name values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getComplexUnitNumber(),
        physicalAddress2.getComplexUnitNumber(),
        "The complex unit number values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getFarmDescription(),
        physicalAddress2.getFarmDescription(),
        "The farm description values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getFarmName(),
        physicalAddress2.getFarmName(),
        "The farm name values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getFarmNumber(),
        physicalAddress2.getFarmNumber(),
        "The farm number values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getLine1(),
        physicalAddress2.getLine1(),
        "The line 1 values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getLine2(),
        physicalAddress2.getLine2(),
        "The line 2 values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getLine3(),
        physicalAddress2.getLine3(),
        "The line 3 values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getParty(),
        physicalAddress2.getParty(),
        "The party values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getPurposes(),
        physicalAddress2.getPurposes(),
        "The purpose values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getRegion(),
        physicalAddress2.getRegion(),
        "The region values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getSiteBlock(),
        physicalAddress2.getSiteBlock(),
        "The site block values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getSiteNumber(),
        physicalAddress2.getSiteNumber(),
        "The site number values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getStreetName(),
        physicalAddress2.getStreetName(),
        "The street name values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getStreetNumber(),
        physicalAddress2.getStreetNumber(),
        "The street number values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getSuburb(),
        physicalAddress2.getSuburb(),
        "The suburb values for the physical addresses do not match");
    assertEquals(
        physicalAddress1.getType(),
        physicalAddress2.getType(),
        "The type values for the physical addresses do not match");
  }

  private void comparePreferences(Preference preference1, Preference preference2) {
    assertEquals(
        preference1.getParty(),
        preference2.getParty(),
        "The party values for the preferences do not match");
    assertEquals(
        preference1.getType(),
        preference2.getType(),
        "The type values for the preferences do not match");
    assertEquals(
        preference1.getValue(),
        preference2.getValue(),
        "The value values for the preferences do not match");
  }

  private void compareResidencePermits(
      ResidencePermit residencePermit1, ResidencePermit residencePermit2) {
    assertEquals(
        residencePermit1.getCountryOfIssue(),
        residencePermit2.getCountryOfIssue(),
        "The country of issue values for the residence permits do not match");
    assertEquals(
        residencePermit1.getDateOfExpiry(),
        residencePermit2.getDateOfExpiry(),
        "The date of expiry values for the residence permits do not match");
    assertEquals(
        residencePermit1.getDateOfIssue(),
        residencePermit2.getDateOfIssue(),
        "The date of issue values for the residence permits do not match");
    assertEquals(
        residencePermit1.getId(),
        residencePermit2.getId(),
        "The ID values for the residence permits do not match");
    assertEquals(
        residencePermit1.getNumber(),
        residencePermit2.getNumber(),
        "The number values for the residence permits do not match");
    assertEquals(
        residencePermit1.getPerson(),
        residencePermit1.getPerson(),
        "The person values for the residence permits do not match");
    assertEquals(
        residencePermit1.getType(),
        residencePermit2.getType(),
        "The type values for the residence permits do not match");
  }

  private void compareRoles(Role role1, Role role2) {
    assertEquals(
        role1.getEffectiveFrom(),
        role2.getEffectiveFrom(),
        "The effective from values for the roles do not match");
    assertEquals(
        role1.getEffectiveTo(),
        role2.getEffectiveTo(),
        "The effective to values for the roles do not match");
    assertEquals(role1.getParty(), role2.getParty(), "The party values for the roles do not match");
    assertEquals(role1.getType(), role2.getType(), "The type values for the roles do not match");
  }

  private void compareSourcesOfFunds(SourceOfFunds sourceOfFunds1, SourceOfFunds sourceOfFunds2) {
    assertEquals(
        sourceOfFunds1.getEffectiveFrom(),
        sourceOfFunds2.getEffectiveFrom(),
        "The effective from values for the sources of funds do not match");
    assertEquals(
        sourceOfFunds1.getEffectiveTo(),
        sourceOfFunds2.getEffectiveTo(),
        "The effective to values for the sources of funds do not match");
    assertEquals(
        sourceOfFunds1.getPerson(),
        sourceOfFunds2.getPerson(),
        "The person values for the sources of funds do not match");
    assertEquals(
        sourceOfFunds1.getPercentage(),
        sourceOfFunds2.getPercentage(),
        "The percentage values for the sources of funds do not match");
    assertEquals(
        sourceOfFunds1.getType(),
        sourceOfFunds2.getType(),
        "The type values for the sources of funds do not match");
  }

  private void compareSourcesOfWealth(
      SourceOfWealth sourceOfWealth1, SourceOfWealth sourceOfWealth2) {
    assertEquals(
        sourceOfWealth1.getEffectiveFrom(),
        sourceOfWealth2.getEffectiveFrom(),
        "The effective from values for the sources of wealth do not match");

    assertEquals(
        sourceOfWealth1.getEffectiveTo(),
        sourceOfWealth2.getEffectiveTo(),
        "The effective to values for the sources of wealth do not match");

    assertEquals(
        sourceOfWealth1.getPerson(),
        sourceOfWealth2.getPerson(),
        "The person values for the sources of wealth do not match");

    assertEquals(
        sourceOfWealth1.getType(),
        sourceOfWealth2.getType(),
        "The type values for the sources of wealth do not match");
  }

  private void compareStatuses(Status status1, Status status2) {
    assertEquals(
        status1.getEffectiveFrom(),
        status2.getEffectiveFrom(),
        "The effective from values for the statuses do not match");
    assertEquals(
        status1.getEffectiveTo(),
        status2.getEffectiveTo(),
        "The effective to values for the statuses do not match");
    assertEquals(
        status1.getParty(), status2.getParty(), "The party values for the statuses do not match");
    assertEquals(
        status1.getType(), status2.getType(), "The type valuess for the statuses do not match");
  }

  private void compareTaxNumbers(TaxNumber taxNumber1, TaxNumber taxNumber2) {
    assertEquals(
        taxNumber1.getCountryOfIssue(),
        taxNumber2.getCountryOfIssue(),
        "The country of issue values for the tax numbers do not match");
    assertEquals(
        taxNumber1.getNumber(),
        taxNumber2.getNumber(),
        "The number values for the tax numbers do not match");
    assertEquals(
        taxNumber1.getParty(),
        taxNumber2.getParty(),
        "The party values for the tax numbers do not match");
    assertEquals(
        taxNumber1.getType(),
        taxNumber2.getType(),
        "The type values for the tax numbers do not match");
  }
}

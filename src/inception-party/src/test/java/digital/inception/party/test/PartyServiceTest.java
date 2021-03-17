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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.Attribute;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.IPartyService;
import digital.inception.party.IdentityDocument;
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
import digital.inception.party.TaxNumber;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>PartyServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>PartyService</b> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
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
    } else {
      person.setMaritalStatus("single");
    }

    person.setMiddleNames("MiddleName" + personCount);
    person.setOccupation("professional_legal");
    person.setPreferredName("PreferredName" + personCount);
    person.setRace("white");
    person.setResidencyStatus("permanent_resident");
    person.setResidentialType("renter");
    person.setSurname("Surname" + personCount);
    person.setTitle("mrs");

    person.addAttribute(new Attribute("weight", "80kg"));

    assertTrue(
        "Failed to confirm that the person has an attribute with type (weight)",
        person.hasAttributeType("weight"));

    person.setCountryOfTaxResidence("ZA");
    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27835551234"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            "personal_email_address",
            "giveName@test.com",
            "marketing"));

    Optional<ContactMechanism> contactMechanismOptional =
        person.getContactMechanism(ContactMechanismType.EMAIL_ADDRESS, "marketing");

    if (contactMechanismOptional.isEmpty()) {
      fail(
          "Failed to retrieve the contact mechanism for the person with the type ("
              + ContactMechanismType.EMAIL_ADDRESS
              + ") and purpose (marketing)");
    }


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

    Person person =
        new Person(
            UUID.fromString("00000000-0000-0000-0000-000000000000"), "Full Name " + personCount);

    return person;
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

  /** Test the contact mechanism purpose validation functionality. */
  @Test
  public void contactMechanismPurposeValidationTest() throws Exception {
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
        "The correct number of constraint violations was not found for the invalid person",
        1,
        personConstraintViolations.size());

    Organization organization = getTestOrganizationDetails();

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            "main_email_address",
            "test@test.com",
            "invalid_purpose"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(organization);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid organization",
        1,
        organizationConstraintViolations.size());
  }

  /** Test the foreign person functionality. */
  @Test
  public void foreignPersonTest() throws Exception {
    Person foreignPerson = getTestForeignPersonDetails();

    partyService.createPerson(foreignPerson);

    Persons filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

    comparePersons(foreignPerson, filteredPersons.getPersons().get(0));

    partyService.deletePerson(foreignPerson.getId());
  }

  /** Test the invalid building address verification functionality. */
  @Test
  public void invalidBuildingAddressTest() {
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.BUILDING, PhysicalAddressRole.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid building address",
        5,
        constraintViolations.size());

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
        "The correct number of constraint violations was not found for the invalid building address",
        13,
        constraintViolations.size());
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
        "The correct number of constraint violations was not found for the invalid complex address",
        6,
        constraintViolations.size());

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
        "The correct number of constraint violations was not found for the invalid complex address",
        14,
        constraintViolations.size());
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
        "The correct number of constraint violations was not found for the invalid farm address",
        3,
        constraintViolations.size());

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
        "The correct number of constraint violations was not found for the invalid farm address",
        13,
        constraintViolations.size());
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
        "The correct number of constraint violations was not found for the invalid international address",
        3,
        constraintViolations.size());

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
        "The correct number of constraint violations was not found for the invalid international address",
        16,
        constraintViolations.size());
  }

  /** Test the invalid organization attribute test. */
  @Test
  public void invalidOrganizationAttributeTest() {
    Organization organization = getTestOrganizationDetails();

    organization.addAttribute(new Attribute("given_name", "Given Name"));

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(organization);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid organization",
        1,
        constraintViolations.size());
  }

  /** Test the invalid person attribute functionality. */
  @Test
  public void invalidPersonAttributeTest() {
    Person person = getTestBasicPersonDetails();

    person.addAttribute(new Attribute("given_name", "Given Name"));

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid person",
        1,
        constraintViolations.size());
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
        "The correct number of constraint violations was not found for the invalid physical address role for the party",
        1,
        constraintViolations.size());
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
        "The correct number of constraint violations was not found for the invalid physical address",
        3,
        constraintViolations.size());
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
        "The correct number of constraint violations was not found for the invalid site address",
        5,
        constraintViolations.size());

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
        "The correct number of constraint violations was not found for the invalid site address",
        14,
        constraintViolations.size());
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
        "The correct number of constraint violations was not found for the invalid street address",
        4,
        constraintViolations.size());

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
        "The correct number of constraint violations was not found for the invalid street address",
        16,
        constraintViolations.size());
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
        "The correct number of constraint violations was not found for the invalid unstructured address",
        3,
        constraintViolations.size());

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
        "The correct number of constraint violations was not found for the invalid unstructured address",
        16,
        constraintViolations.size());
  }

  /** Test the organization functionality. */
  @Test
  public void organizationTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    partyService.createOrganization(organization);

    Organizations filteredOrganizations =
        partyService.getOrganizations("", OrganizationSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered organizations was not retrieved",
        1,
        filteredOrganizations.getOrganizations().size());

    compareOrganizations(organization, filteredOrganizations.getOrganizations().get(0));

    organization.setName(organization.getName() + " Updated");

    organization.setCountriesOfTaxResidence(Set.of("GB", "ZA"));

    organization.addContactMechanism(
        new ContactMechanism(ContactMechanismType.PHONE_NUMBER, "main_phone_number", "0115551234"));

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            "main_email_address",
            "test@test.com",
            "marketing"));

    Optional<ContactMechanism> contactMechanismOptional =
        organization.getContactMechanism(ContactMechanismType.EMAIL_ADDRESS, "marketing");

    if (contactMechanismOptional.isEmpty()) {
      fail(
          "Failed to retrieve the contact mechanism for the organization with the type ("
              + ContactMechanismType.EMAIL_ADDRESS
              + ") and purpose (marketing)");
    }

    PhysicalAddress mainAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.MAIN);
    mainAddress.setStreetNumber("1");
    mainAddress.setStreetName("Discovery Place");
    mainAddress.setSuburb("Sandhurst");
    mainAddress.setCity("Sandton");
    mainAddress.setRegion("GP");
    mainAddress.setCountry("ZA");
    mainAddress.setPostalCode("2194");

    organization.addPhysicalAddress(mainAddress);

    organization.addPreference(new Preference("correspondence_language", "EN"));

    organization.removeRole("employer");

    organization.addRole(new Role("vendor"));

    partyService.updateOrganization(organization);

    filteredOrganizations =
        partyService.getOrganizations("", OrganizationSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered organizations was not retrieved",
        1,
        filteredOrganizations.getOrganizations().size());

    compareOrganizations(organization, filteredOrganizations.getOrganizations().get(0));

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered parties was not retrieved",
        1,
        filteredParties.getParties().size());

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
        "The correct number of filtered parties was not retrieved",
        2,
        filteredParties.getParties().size());

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
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

    comparePersons(person, filteredPersons.getPersons().get(0));

    partyService.deletePerson(person.getId());

    person = getTestCompletePersonDetails(true);

    partyService.createPerson(person);

    filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

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

    person.removeAttribute("weight");

    person.addAttribute(new Attribute("height", "180cm"));

    Attribute attribute = new Attribute("complex_attribute");
    attribute.setBooleanValue(true);
    attribute.setDateValue(LocalDate.now());
    attribute.setDoubleValue(123.456);
    attribute.setStringValue("String Value");

    person.addAttribute(new Attribute("height", "180cm"));

    person.removeContactMechanism("main_fax_number");

    person.getContactMechanism("personal_email_address").get().setValue("test.updated@test.com");

    person.addContactMechanism(
        new ContactMechanism(ContactMechanismType.PHONE_NUMBER, "home_phone_number", "0115551234"));

    person.addIdentityDocument(
        new IdentityDocument(
            "passport", "ZA", LocalDate.of(2016, 10, 7), LocalDate.of(2025, 9, 1), "A1234567890"));

    person.removeIdentityDocument("za_id_card");

    person.removePreference("correspondence_language");

    person.addPreference(new Preference("time_to_contact", "anytime"));

    person.addTaxNumber(new TaxNumber("uk_tax_number", "GB", "987654321"));

    person.removeTaxNumber("za_income_tax_number");

    partyService.updatePerson(person);

    filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

    comparePersons(person, filteredPersons.getPersons().get(0));

    filteredPersons =
        partyService.getPersons("Updated", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

    comparePersons(person, filteredPersons.getPersons().get(0));

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    Parties filteredParties = partyService.getParties("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered parties was not retrieved",
        1,
        filteredParties.getParties().size());

    partyService.deletePerson(person.getId());
  }

  /** Test the party physical address functionality. */
  @Test
  public void physicalAddressTest() throws Exception {
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
        "The correct number of constraint violations was not found",
        0,
        constraintViolations.size());

    partyService.createPerson(person);

    Person retrievedPerson = partyService.getPerson(person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deleteParty(person.getId());
  }

  /** Test the role type attribute type constraint functionality. */
  @Test
  public void roleTypeAttributeTypeConstraintTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addRole(new Role("test_person_role"));

    Set<ConstraintViolation<Person>> personConstraintViolations =
        partyService.validatePerson(person);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid person",
        39,
        personConstraintViolations.size());

    Organization organization = getTestOrganizationDetails();

    organization.removeIdentityDocument("za_company_registration");

    organization.addRole(new Role("test_organization_role"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(organization);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid organization",
        12,
        organizationConstraintViolations.size());
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
        "The type values for the two attributes do not match",
        attribute1.getType(),
        attribute2.getType());
    assertEquals(
        "The boolean value values for the two attributes do not match",
        attribute1.getBooleanValue(),
        attribute2.getBooleanValue());
    assertEquals(
        "The date value values for the two attributes do not match",
        attribute1.getDateValue(),
        attribute2.getDateValue());
    assertEquals(
        "The double value values for the two attributes do not match",
        attribute1.getDoubleValue(),
        attribute2.getDoubleValue());
    assertEquals(
        "The integer value values for the two attributes do not match",
        attribute1.getIntegerValue(),
        attribute2.getIntegerValue());
    assertEquals(
        "The string value values for the two attributes do not match",
        attribute1.getStringValue(),
        attribute2.getStringValue());
  }

  private void compareOrganizations(Organization organization1, Organization organization2) {
    assertEquals(
        "The countries of tax residence values for the two organizations do not match",
        organization1.getCountriesOfTaxResidence(),
        organization2.getCountriesOfTaxResidence());
    assertEquals(
        "The ID values for the two organizations do not match",
        organization1.getId(),
        organization2.getId());
    assertEquals(
        "The name values for the two organizations do not match",
        organization1.getName(),
        organization2.getName());
    assertEquals(
        "The tenant ID values for the two organizations do not match",
        organization1.getTenantId(),
        organization2.getTenantId());

    assertEquals(
        "The number of attributes for the two organizations do not match",
        organization1.getAttributes().size(),
        organization2.getAttributes().size());

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
        "The number of contact mechanisms for the two organizations do not match",
        organization1.getContactMechanisms().size(),
        organization2.getContactMechanisms().size());

    for (ContactMechanism person1ContactMechanism : organization1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism person2ContactMechanism : organization2.getContactMechanisms()) {

        if (Objects.equals(person1ContactMechanism.getParty(), person2ContactMechanism.getParty())
            && Objects.equals(person1ContactMechanism.getType(), person2ContactMechanism.getType())
            && Objects.equals(
                person1ContactMechanism.getRole(), person2ContactMechanism.getRole())) {
          assertEquals(
              "The values for the two contact mechanisms do not match",
              person1ContactMechanism.getValue(),
              person2ContactMechanism.getValue());

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
        "The number of physical addresses for the two organizations do not match",
        organization1.getPhysicalAddresses().size(),
        organization2.getPhysicalAddresses().size());

    for (PhysicalAddress person1PhysicalAddress : organization1.getPhysicalAddresses()) {
      boolean foundPhysicalAddress = false;

      for (PhysicalAddress person2PhysicalAddress : organization2.getPhysicalAddresses()) {

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
        "The number of preferences for the two organizations do not match",
        organization1.getPreferences().size(),
        organization2.getPreferences().size());

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
  }

  private void compareParties(Party party1, Party party2) {
    assertEquals("The ID values for the two parties do not match", party1.getId(), party2.getId());
    assertEquals(
        "The tenant ID values for the two parties do not match",
        party1.getTenantId(),
        party2.getTenantId());
    assertEquals(
        "The type values for the two parties do not match", party1.getType(), party2.getType());
    assertEquals(
        "The name values for the two parties do not match", party1.getName(), party2.getName());
  }

  private void comparePersons(Person person1, Person person2) {
    assertEquals(
        "The countries of citizenship values for the two persons do not match",
        person1.getCountriesOfCitizenship(),
        person2.getCountriesOfCitizenship());
    assertEquals(
        "The countries of tax residence values for the two persons do not match",
        person1.getCountriesOfTaxResidence(),
        person2.getCountriesOfTaxResidence());
    assertEquals(
        "The country of birth values for the two persons do not match",
        person1.getCountryOfBirth(),
        person2.getCountryOfBirth());
    assertEquals(
        "The country of residence values for the two persons do not match",
        person1.getCountryOfResidence(),
        person2.getCountryOfResidence());
    assertEquals(
        "The date of birth values for the two persons do not match",
        person1.getDateOfBirth(),
        person2.getDateOfBirth());
    assertEquals(
        "The date of death values for the two persons do not match",
        person1.getDateOfDeath(),
        person2.getDateOfDeath());
    assertEquals(
        "The employment status values for the two persons do not match",
        person1.getEmploymentStatus(),
        person2.getEmploymentStatus());
    assertEquals(
        "The employment type values for the two persons do not match",
        person1.getEmploymentType(),
        person2.getEmploymentType());
    assertEquals(
        "The gender values for the two persons do not match",
        person1.getGender(),
        person2.getGender());
    assertEquals(
        "The given name values for the two persons do not match",
        person1.getGivenName(),
        person2.getGivenName());
    assertEquals(
        "The ID values for the two persons do not match", person1.getId(), person2.getId());
    assertEquals(
        "The initials values for the two persons do not match",
        person1.getInitials(),
        person2.getInitials());
    assertEquals(
        "The language values for the two persons do not match",
        person1.getLanguage(),
        person2.getLanguage());
    assertEquals(
        "The maiden name values for the two persons do not match",
        person1.getMaidenName(),
        person2.getMaidenName());
    assertEquals(
        "The marital status values for the two persons do not match",
        person1.getMaritalStatus(),
        person2.getMaritalStatus());
    assertEquals(
        "The marriage type values for the two persons do not match",
        person1.getMarriageType(),
        person2.getMarriageType());
    assertEquals(
        "The middle names values for the two persons do not match",
        person1.getMiddleNames(),
        person2.getMiddleNames());
    assertEquals(
        "The name values for the two persons do not match", person1.getName(), person2.getName());
    assertEquals(
        "The occupation values for the two persons do not match",
        person1.getOccupation(),
        person2.getOccupation());
    assertEquals(
        "The preferred name values for the two persons do not match",
        person1.getPreferredName(),
        person2.getPreferredName());
    assertEquals(
        "The race values for the two persons do not match", person1.getRace(), person2.getRace());
    assertEquals(
        "The residency status values for the two persons do not match",
        person1.getResidencyStatus(),
        person2.getResidencyStatus());
    assertEquals(
        "The residential type values for the two persons do not match",
        person1.getResidentialType(),
        person2.getResidentialType());
    assertEquals(
        "The surname values for the two persons do not match",
        person1.getSurname(),
        person2.getSurname());
    assertEquals(
        "The tenant ID values for the two persons do not match",
        person1.getTenantId(),
        person2.getTenantId());
    assertEquals(
        "The title values for the two persons do not match",
        person1.getTitle(),
        person2.getTitle());

    assertEquals(
        "The number of attributes for the two persons do not match",
        person1.getAttributes().size(),
        person2.getAttributes().size());

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
        "The number of contact mechanisms for the two persons do not match",
        person1.getContactMechanisms().size(),
        person2.getContactMechanisms().size());

    for (ContactMechanism person1ContactMechanism : person1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism person2ContactMechanism : person2.getContactMechanisms()) {

        if (Objects.equals(person1ContactMechanism.getParty(), person2ContactMechanism.getParty())
            && Objects.equals(person1ContactMechanism.getType(), person2ContactMechanism.getType())
            && Objects.equals(
                person1ContactMechanism.getRole(), person2ContactMechanism.getRole())) {
          assertEquals(
              "The values for the two contact mechanisms do not match",
              person1ContactMechanism.getValue(),
              person2ContactMechanism.getValue());

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
        "The number of identity documents for the two persons do not match",
        person1.getIdentityDocuments().size(),
        person2.getIdentityDocuments().size());

    for (IdentityDocument person1IdentityDocument : person1.getIdentityDocuments()) {
      boolean foundIdentityDocument = false;

      for (IdentityDocument person2IdentityDocument : person2.getIdentityDocuments()) {
        if (person1IdentityDocument.getType().equals(person2IdentityDocument.getType())
            && person1IdentityDocument
                .getCountryOfIssue()
                .equals(person2IdentityDocument.getCountryOfIssue())
            && person1IdentityDocument
                .getDateOfIssue()
                .equals(person2IdentityDocument.getDateOfIssue())) {

          assertEquals(
              "The date of expiry for the two identity documents does not match",
              person1IdentityDocument.getDateOfExpiry(),
              person2IdentityDocument.getDateOfExpiry());
          assertEquals(
              "The date provided for the two identity documents does not match",
              person1IdentityDocument.getDateProvided(),
              person2IdentityDocument.getDateProvided());
          assertEquals(
              "The numbers for the two identity documents do not match",
              person1IdentityDocument.getNumber(),
              person2IdentityDocument.getNumber());

          foundIdentityDocument = true;
        }
      }

      if (!foundIdentityDocument) {
        fail(
            "Failed to find the identity document ("
                + person1IdentityDocument.getType()
                + ")("
                + person1IdentityDocument.getCountryOfIssue()
                + ")("
                + person1IdentityDocument.getDateOfIssue()
                + ")");
      }
    }

    assertEquals(
        "The number of physical addresses for the two persons do not match",
        person1.getPhysicalAddresses().size(),
        person2.getPhysicalAddresses().size());

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
        "The number of preferences for the two persons do not match",
        person1.getPreferences().size(),
        person2.getPreferences().size());

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
        "The number of residence permits for the two persons do not match",
        person1.getResidencePermits().size(),
        person2.getResidencePermits().size());

    for (ResidencePermit person1ResidencePermit : person1.getResidencePermits()) {
      boolean foundResidencePermit = false;

      for (ResidencePermit person2ResidencePermit : person2.getResidencePermits()) {
        if (person1ResidencePermit.getType().equals(person2ResidencePermit.getType())
            && person1ResidencePermit
                .getCountryOfIssue()
                .equals(person2ResidencePermit.getCountryOfIssue())
            && person1ResidencePermit
                .getDateOfIssue()
                .equals(person2ResidencePermit.getDateOfIssue())) {

          assertEquals(
              "The date of expiry for the two residence permits do not match",
              person1ResidencePermit.getDateOfExpiry(),
              person2ResidencePermit.getDateOfExpiry());
          assertEquals(
              "The numbers for the two residence permits do not match",
              person1ResidencePermit.getNumber(),
              person2ResidencePermit.getNumber());

          foundResidencePermit = true;
        }
      }

      if (!foundResidencePermit) {
        fail(
            "Failed to find the residence permit ("
                + person1ResidencePermit.getType()
                + ")("
                + person1ResidencePermit.getCountryOfIssue()
                + ")("
                + person1ResidencePermit.getDateOfIssue()
                + ")");
      }
    }

    assertEquals(
        "The number of roles for the two persons do not match",
        person1.getRoles().size(),
        person2.getRoles().size());

    for (Role person1Role : person1.getRoles()) {
      boolean foundRole = false;

      for (Role person2Role : person2.getRoles()) {
        if (person1Role.getType().equals(person2Role.getType())) {

          assertEquals(
              "The purpose for the two roles do not match",
              person1Role.getPurpose(),
              person2Role.getPurpose());

          foundRole = true;
        }
      }

      if (!foundRole) {
        fail("Failed to find the role (" + person1Role.getType() + ")");
      }
    }

    assertEquals(
        "The number of tax numbers for the two persons do not match",
        person1.getTaxNumbers().size(),
        person2.getTaxNumbers().size());

    for (TaxNumber person1TaxNumber : person1.getTaxNumbers()) {
      boolean foundTaxNumber = false;

      for (TaxNumber person2TaxNumber : person2.getTaxNumbers()) {
        if (person1TaxNumber.getType().equals(person2TaxNumber.getType())) {

          assertEquals(
              "The country of issue for the two tax numbers do not match",
              person1TaxNumber.getCountryOfIssue(),
              person2TaxNumber.getCountryOfIssue());
          assertEquals(
              "The numbers for the two tax numbers do not match",
              person1TaxNumber.getNumber(),
              person2TaxNumber.getNumber());

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
        "The building floor values for the two physical addresses do not match",
        physicalAddress1.getBuildingFloor(),
        physicalAddress2.getBuildingFloor());
    assertEquals(
        "The building name values for the two physical addresses do not match",
        physicalAddress1.getBuildingName(),
        physicalAddress2.getBuildingName());
    assertEquals(
        "The building room values for the two physical addresses do not match",
        physicalAddress1.getBuildingRoom(),
        physicalAddress2.getBuildingRoom());
    assertEquals(
        "The city values for the two physical addresses do not match",
        physicalAddress1.getCity(),
        physicalAddress2.getCity());
    assertEquals(
        "The complex name values for the two physical addresses do not match",
        physicalAddress1.getComplexName(),
        physicalAddress2.getComplexName());
    assertEquals(
        "The complex unit number values for the two physical addresses do not match",
        physicalAddress1.getComplexUnitNumber(),
        physicalAddress2.getComplexUnitNumber());
    assertEquals(
        "The farm description values for the two physical addresses do not match",
        physicalAddress1.getFarmDescription(),
        physicalAddress2.getFarmDescription());
    assertEquals(
        "The farm name values for the two physical addresses do not match",
        physicalAddress1.getFarmName(),
        physicalAddress2.getFarmName());
    assertEquals(
        "The farm number values for the two physical addresses do not match",
        physicalAddress1.getFarmNumber(),
        physicalAddress2.getFarmNumber());
    assertEquals(
        "The line 1 values for the two physical addresses do not match",
        physicalAddress1.getLine1(),
        physicalAddress2.getLine1());
    assertEquals(
        "The line 2 values for the two physical addresses do not match",
        physicalAddress1.getLine2(),
        physicalAddress2.getLine2());
    assertEquals(
        "The line 3 values for the two physical addresses do not match",
        physicalAddress1.getLine3(),
        physicalAddress2.getLine3());
    assertEquals(
        "The purposes values for the two physical addresses do not match",
        physicalAddress1.getPurposes(),
        physicalAddress2.getPurposes());
    assertEquals(
        "The region values for the two physical addresses do not match",
        physicalAddress1.getRegion(),
        physicalAddress2.getRegion());
    assertEquals(
        "The site block values for the two physical addresses do not match",
        physicalAddress1.getSiteBlock(),
        physicalAddress2.getSiteBlock());
    assertEquals(
        "The site number values for the two physical addresses do not match",
        physicalAddress1.getSiteNumber(),
        physicalAddress2.getSiteNumber());
    assertEquals(
        "The street name values for the two physical addresses do not match",
        physicalAddress1.getStreetName(),
        physicalAddress2.getStreetName());
    assertEquals(
        "The street number values for the two physical addresses do not match",
        physicalAddress1.getStreetNumber(),
        physicalAddress2.getStreetNumber());
    assertEquals(
        "The suburb values for the two physical addresses do not match",
        physicalAddress1.getSuburb(),
        physicalAddress2.getSuburb());
    assertEquals(
        "The type values for the two physical addresses do not match",
        physicalAddress1.getType(),
        physicalAddress2.getType());
  }

  private void comparePreferences(Preference preference1, Preference preference2) {
    assertEquals(
        "The type values for the two preferences do not match",
        preference1.getType(),
        preference2.getType());
    assertEquals(
        "The value values for the two preferences do not match",
        preference1.getValue(),
        preference2.getValue());
  }
}

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
import static org.junit.Assert.fail;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismPurpose;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.EmploymentStatus;
import digital.inception.party.EmploymentType;
import digital.inception.party.Gender;
import digital.inception.party.IPartyService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.IdentityDocumentType;
import digital.inception.party.MaritalStatus;
import digital.inception.party.MarriageType;
import digital.inception.party.NextOfKinType;
import digital.inception.party.Occupation;
import digital.inception.party.Organization;
import digital.inception.party.Organizations;
import digital.inception.party.Parties;
import digital.inception.party.Party;
import digital.inception.party.PartyAttributeType;
import digital.inception.party.PartyAttributeTypeCategory;
import digital.inception.party.PartyRole;
import digital.inception.party.PartyRolePurpose;
import digital.inception.party.PartyRoleType;
import digital.inception.party.PartyType;
import digital.inception.party.Person;
import digital.inception.party.PersonSortBy;
import digital.inception.party.Persons;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressType;
import digital.inception.party.Preference;
import digital.inception.party.PreferenceType;
import digital.inception.party.PreferenceTypeCategory;
import digital.inception.party.Race;
import digital.inception.party.ResidencePermitType;
import digital.inception.party.ResidencyStatus;
import digital.inception.party.ResidentialType;
import digital.inception.party.SourceOfFunds;
import digital.inception.party.TaxNumber;
import digital.inception.party.TaxNumberType;
import digital.inception.party.TimeToContact;
import digital.inception.party.Title;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    person.setHomeLanguage("EN");
    person.setId(UuidCreator.getShortPrefixComb());
    person.setInitials("G M");
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

    person.addIdentityDocument(
        new IdentityDocument("za_id_card", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    person.setCountryOfTaxResidence("ZA");
    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27835551234"));
    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS, "personal_email_address", "test@test.com"));

    PhysicalAddress residentialAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.RESIDENTIAL);
    residentialAddress.setStreetNumber("1");
    residentialAddress.setStreetName("Discovery Place");
    residentialAddress.setSuburb("Sandhurst");
    residentialAddress.setCity("Sandton");
    residentialAddress.setRegion("GP");
    residentialAddress.setCountry("ZA");
    residentialAddress.setPostalCode("2194");

    person.addPhysicalAddress(residentialAddress);

    PhysicalAddress correspondenceAddress =
        new PhysicalAddress(
            PhysicalAddressType.UNSTRUCTURED, PhysicalAddressPurpose.CORRESPONDENCE);

    correspondenceAddress.setLine1("1 Apple Park Way");
    correspondenceAddress.setCity("Cupertino");
    correspondenceAddress.setRegion("CA");
    correspondenceAddress.setCountry("US");
    correspondenceAddress.setPostalCode("CA 95014");

    person.addPhysicalAddress(correspondenceAddress);

    person.addPreference(new Preference("correspondence_language", "EN"));

    person.addRole(new PartyRole("employee"));

    return person;
  }

  private static synchronized Person getTestBasicPersonDetails() {
    personCount++;

    Person person = new Person();

    person.setId(UuidCreator.getShortPrefixComb());
    person.setName("Full Name " + personCount);
    person.setTenantId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

    return person;
  }

  /** Test the person functionality. */
  @Test
  public void basicPersonTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addRole(new PartyRole("employee"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27835551234"));

    person.addIdentityDocument(
        new IdentityDocument("za_id_card", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "123456789"));

    person.addPreference(new Preference("correspondence_language", "EN"));

    partyService.createPerson(person);
  }

  /** Test the contact mechanism purpose functionality. */
  @Test
  public void contactMechanismPurposesTest() throws Exception {
    List<ContactMechanismPurpose> retrievedContactMechanismPurposes =
        partyService.getContactMechanismPurposes();

    retrievedContactMechanismPurposes = partyService.getContactMechanismPurposes("en-US");
  }

  /** Test the contact mechanism type functionality. */
  @Test
  public void contactMechanismTypeTest() throws Exception {
    List<ContactMechanismType> retrievedContactMechanismTypes =
        partyService.getContactMechanismTypes();

    retrievedContactMechanismTypes = partyService.getContactMechanismTypes("en-US");
  }

  /** Test the employment status reference functionality. */
  @Test
  public void employmentStatusTest() throws Exception {
    List<EmploymentStatus> retrievedEmploymentStatuses = partyService.getEmploymentStatuses();

    assertEquals(
        "The correct number of employment statuses was not retrieved",
        6,
        retrievedEmploymentStatuses.size());

    retrievedEmploymentStatuses = partyService.getEmploymentStatuses("en-US");

    assertEquals(
        "The correct number of employment statuses was not retrieved",
        3,
        retrievedEmploymentStatuses.size());
  }

  /** Test the employment type reference functionality. */
  @Test
  public void employmentTypeTest() throws Exception {
    List<EmploymentType> retrievedEmploymentTypes = partyService.getEmploymentTypes();

    assertEquals(
        "The correct number of employment types was not retrieved",
        22,
        retrievedEmploymentTypes.size());

    retrievedEmploymentTypes = partyService.getEmploymentTypes("en-US");

    assertEquals(
        "The correct number of employment types was not retrieved",
        11,
        retrievedEmploymentTypes.size());
  }

  /** Test the gender reference functionality. */
  @Test
  public void genderTest() throws Exception {
    List<Gender> retrievedGenders = partyService.getGenders();

    assertEquals("The correct number of genders was not retrieved", 10, retrievedGenders.size());

    retrievedGenders = partyService.getGenders("en-US");

    assertEquals("The correct number of genders was not retrieved", 5, retrievedGenders.size());
  }

  /** Test the identity document type reference functionality. */
  @Test
  public void identityDocumentTypeTest() throws Exception {
    List<IdentityDocumentType> retrievedIdentityDocumentTypes =
        partyService.getIdentityDocumentTypes();

    assertEquals(
        "The correct number of identity document types was not retrieved",
        8,
        retrievedIdentityDocumentTypes.size());

    retrievedIdentityDocumentTypes = partyService.getIdentityDocumentTypes("en-US");

    assertEquals(
        "The correct number of identity document types was not retrieved",
        4,
        retrievedIdentityDocumentTypes.size());
  }

  /** Test the invalid building address verification functionality. */
  @Test
  public void invalidBuildingAddressTest() {
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.BUILDING, PhysicalAddressPurpose.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid building address",
        5,
        constraintViolations.size());

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.BUILDING, PhysicalAddressPurpose.RESIDENTIAL);
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
        12,
        constraintViolations.size());
  }

  /** Test the invalid complex address verification functionality. */
  @Test
  public void invalidComplexAddressTest() {
    // Validate an empty invalid address
    // Required: Complex Name, Complex Unit Number, Street Name, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.COMPLEX, PhysicalAddressPurpose.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid complex address",
        6,
        constraintViolations.size());

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.COMPLEX, PhysicalAddressPurpose.RESIDENTIAL);
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
        13,
        constraintViolations.size());
  }

  /** Test the invalid farm address verification functionality. */
  @Test
  public void invalidFarmAddressTest() {
    // Validate an empty invalid address
    // Required: Farm Number, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.FARM, PhysicalAddressPurpose.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid farm address",
        3,
        constraintViolations.size());

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.FARM, PhysicalAddressPurpose.RESIDENTIAL);
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
        12,
        constraintViolations.size());
  }

  /** Test the invalid international address verification functionality. */
  @Test
  public void invalidInternationalAddressTest() {
    // Validate an empty invalid address
    // Required: Line 1, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.INTERNATIONAL, PhysicalAddressPurpose.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid international address",
        3,
        constraintViolations.size());

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.INTERNATIONAL, PhysicalAddressPurpose.RESIDENTIAL);
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
        15,
        constraintViolations.size());
  }

  /** Test the invalid physical address purpose for party type verification functionality. */
  @Test
  public void invalidPhysicalAddressPurposeForPartyTypeTest() {
    Person person = getTestBasicPersonDetails();

    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.MAIN);

    invalidAddress.setStreetNumber("221B");
    invalidAddress.setStreetName("Baker Street");
    invalidAddress.setSuburb("Marylebone");
    invalidAddress.setCity("London");
    invalidAddress.setCountry("GB");
    invalidAddress.setPostalCode("NW1 6XE");

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations = partyService.validatePerson(person);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid physical address purpose for the party",
        1,
        constraintViolations.size());
  }

  /** Test the invalid physical address type verification functionality. */
  @Test
  public void invalidPhysicalAddressTypeAndPurposeTest() {
    PhysicalAddress invalidAddress =
        new PhysicalAddress("invalid physical address type", "invalid physical address purpose");

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid physical address",
        1,
        constraintViolations.size());
  }

  /** Test the invalid site address verification functionality. */
  @Test
  public void invalidSiteAddressTest() {
    // Validate an empty invalid address
    // Required: Site Block, Site Number, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.SITE, PhysicalAddressPurpose.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid site address",
        5,
        constraintViolations.size());

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.SITE, PhysicalAddressPurpose.RESIDENTIAL);
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
        13,
        constraintViolations.size());
  }

  /** Test the invalid street address verification functionality. */
  @Test
  public void invalidStreetAddressTest() {
    // Validate an empty invalid address
    // Required: Street Name, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid street address",
        4,
        constraintViolations.size());

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.RESIDENTIAL);
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
        15,
        constraintViolations.size());
  }

  /** Test the invalid unstructured address verification functionality. */
  @Test
  public void invalidUnstructuredAddressTest() {
    // Validate an empty invalid address
    // Required: Line 1, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressPurpose.RESIDENTIAL);

    Set<ConstraintViolation<PhysicalAddress>> constraintViolations =
        partyService.validatePhysicalAddress(invalidAddress);

    assertEquals(
        "The correct number of constraint violations was not found for the invalid unstructured address",
        3,
        constraintViolations.size());

    // Validate a fully populated invalid address
    invalidAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressPurpose.RESIDENTIAL);
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
        15,
        constraintViolations.size());
  }

  /** Test the marital status reference functionality. */
  @Test
  public void maritalStatusTest() throws Exception {
    List<MaritalStatus> retrievedMaritalStatuses = partyService.getMaritalStatuses();

    assertEquals(
        "The correct number of marital statuses was not retrieved",
        12,
        retrievedMaritalStatuses.size());

    retrievedMaritalStatuses = partyService.getMaritalStatuses("en-US");

    assertEquals(
        "The correct number of marital statuses was not retrieved",
        6,
        retrievedMaritalStatuses.size());
  }

  /** Test the marriage type reference functionality. */
  @Test
  public void marriageTypeTest() throws Exception {
    List<MarriageType> retrievedMarriageTypes = partyService.getMarriageTypes();

    assertEquals(
        "The correct number of marriage types was not retrieved", 8, retrievedMarriageTypes.size());

    retrievedMarriageTypes = partyService.getMarriageTypes("en-US");

    assertEquals(
        "The correct number of marriage types was not retrieved", 4, retrievedMarriageTypes.size());
  }

  /** Test the next of kin type reference functionality. */
  @Test
  public void nextOfKinTypeTest() throws Exception {
    List<NextOfKinType> retrievedNextOfKinTypes = partyService.getNextOfKinTypes();

    assertEquals(
        "The correct number of next of kin types was not retrieved",
        34,
        retrievedNextOfKinTypes.size());

    retrievedNextOfKinTypes = partyService.getNextOfKinTypes("en-US");

    assertEquals(
        "The correct number of next of kin types was not retrieved",
        17,
        retrievedNextOfKinTypes.size());
  }

  /** Test the occupation reference functionality. */
  @Test
  public void occupationTest() throws Exception {
    List<Occupation> retrievedOccupations = partyService.getOccupations();

    assertEquals(
        "The correct number of occupations was not retrieved", 58, retrievedOccupations.size());

    retrievedOccupations = partyService.getOccupations("en-US");

    assertEquals(
        "The correct number of occupations was not retrieved", 29, retrievedOccupations.size());
  }

  /** Test the organization functionality. */
  @Test
  public void organizationTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    partyService.createOrganization(organization);

    Organizations filteredOrganizations =
        partyService.getOrganizations("", SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered organizations was not retrieved",
        1,
        filteredOrganizations.getOrganizations().size());

    compareOrganizations(organization, filteredOrganizations.getOrganizations().get(0));

    organization.setName(organization.getName() + " Updated");

    organization.setCountriesOfTaxResidence(Set.of("UK", "ZA"));

    organization.addContactMechanism(
        new ContactMechanism("phone_number", "main_phone_number", "0115551234"));

    PhysicalAddress mainAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.MAIN);
    mainAddress.setStreetNumber("1");
    mainAddress.setStreetName("Discovery Place");
    mainAddress.setSuburb("Sandhurst");
    mainAddress.setCity("Sandton");
    mainAddress.setRegion("GP");
    mainAddress.setCountry("ZA");
    mainAddress.setPostalCode("2194");

    organization.addPhysicalAddress(mainAddress);

    organization.addPreference(new Preference("correspondence_language", "EN"));

    partyService.updateOrganization(organization);

    filteredOrganizations = partyService.getOrganizations("", SortDirection.ASCENDING, 0, 100);

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

  /** Test the party attribute type category reference functionality. */
  @Test
  public void partyAttributeTypeCategoryTest() throws Exception {
    List<PartyAttributeTypeCategory> retrievedPartyAttributeTypeCategories =
        partyService.getPartyAttributeTypeCategories();

    assertEquals(
        "The correct number of party attribute type categories was not retrieved",
        2,
        retrievedPartyAttributeTypeCategories.size());

    retrievedPartyAttributeTypeCategories = partyService.getPartyAttributeTypeCategories("en-US");

    assertEquals(
        "The correct number of party attribute type categories was not retrieved",
        1,
        retrievedPartyAttributeTypeCategories.size());
  }

  /** Test the party attribute type reference functionality. */
  @Test
  public void partyAttributeTypeTest() throws Exception {
    List<PartyAttributeType> retrievedPartyAttributeTypes = partyService.getPartyAttributeTypes();

    assertEquals(
        "The correct number of party attribute types was not retrieved",
        6,
        retrievedPartyAttributeTypes.size());

    retrievedPartyAttributeTypes = partyService.getPartyAttributeTypes("en-US");

    assertEquals(
        "The correct number of party attribute types was not retrieved",
        3,
        retrievedPartyAttributeTypes.size());
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

  /** Test the party physical address functionality. */
  @Test
  public void partyPhysicalAddressTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    PhysicalAddress buildingAddress = new PhysicalAddress(PhysicalAddressType.BUILDING);
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
        new PhysicalAddress(PhysicalAddressType.COMPLEX, PhysicalAddressPurpose.CORRESPONDENCE);
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
        new PhysicalAddress(PhysicalAddressType.FARM, PhysicalAddressPurpose.DELIVERY);
    farmAddress.setFarmNumber("S935");
    farmAddress.setFarmName("My Geluk");
    farmAddress.setFarmDescription("My Geluk");
    farmAddress.setCity("Koffiefontein");
    farmAddress.setRegion("FS");
    farmAddress.setCountry("ZA");
    farmAddress.setPostalCode("9986");
    person.addPhysicalAddress(farmAddress);

    PhysicalAddress internationalAddress =
        new PhysicalAddress(PhysicalAddressType.INTERNATIONAL, PhysicalAddressPurpose.HOME);
    internationalAddress.setLine1("Address Line 1");
    internationalAddress.setLine2("Address Line 2");
    internationalAddress.setLine3("Address Line 3");
    internationalAddress.setCity("Johannesburg");
    internationalAddress.setCountry("ZA");
    internationalAddress.setPostalCode("2194");
    person.addPhysicalAddress(internationalAddress);

    PhysicalAddress postalAddress =
        new PhysicalAddress(PhysicalAddressType.POSTAL, PhysicalAddressPurpose.CORRESPONDENCE);
    postalAddress.setLine1("PO Box 12345");
    postalAddress.setSuburb("Fairland");
    postalAddress.setCity("Johannesburg");
    postalAddress.setCountry("ZA");
    postalAddress.setPostalCode("2130");
    person.addPhysicalAddress(postalAddress);

    PhysicalAddress siteAddress =
        new PhysicalAddress(PhysicalAddressType.SITE, PhysicalAddressPurpose.RESIDENTIAL);
    siteAddress.setSiteBlock("CC");
    siteAddress.setSiteNumber("25436");
    siteAddress.setCity("Soshanguve");
    siteAddress.setRegion("GP");
    siteAddress.setCountry("ZA");
    siteAddress.setPostalCode("0152");
    person.addPhysicalAddress(siteAddress);

    PhysicalAddress streetAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressPurpose.TEMPORARY);
    streetAddress.setStreetNumber("1");
    streetAddress.setStreetName("Discovery Place");
    streetAddress.setSuburb("Sandhurst");
    streetAddress.setCity("Sandton");
    streetAddress.setRegion("GP");
    streetAddress.setCountry("ZA");
    streetAddress.setPostalCode("2194");
    person.addPhysicalAddress(streetAddress);

    PhysicalAddress unstructuredAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressPurpose.WORK);
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

  /** Test the party role purpose functionality. */
  @Test
  public void partyRolePurposeTest() throws Exception {
    List<PartyRolePurpose> retrievedPartyRolePurposes = partyService.getPartyRolePurposes();

    retrievedPartyRolePurposes = partyService.getPartyRolePurposes("en-US");
  }

  /** Test the party role type functionality. */
  @Test
  public void partyRoleTypeTest() throws Exception {
    List<PartyRoleType> retrievedPartyRoleTypes = partyService.getPartyRoleTypes();

    retrievedPartyRoleTypes = partyService.getPartyRoleTypes("en-US");
  }

  /** Test the person functionality. */
  @Test
  public void personTest() throws Exception {
    Person person = getTestBasicPersonDetails();

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

    //    try (Connection connection = dataSource.getConnection()) {
    //      try (PreparedStatement statement =
    //          connection.prepareStatement("SELECT type, purpose FROM party.contact_mechanisms")) {
    //        try (ResultSet rs = statement.executeQuery()) {
    //          while (rs.next()) {
    //            Integer contactMechanismType = rs.getInt(1);
    //            Integer contactMechanismPurpose = rs.getInt(2);
    //
    //            int xxx = 0;
    //            xxx++;
    //          }
    //        }
    //      }
    //    }
    //
    //    try (Connection connection = dataSource.getConnection()) {
    //      try (PreparedStatement statement =
    //          connection.prepareStatement("SELECT type, purpose FROM party.physical_addresses")) {
    //        try (ResultSet rs = statement.executeQuery()) {
    //          while (rs.next()) {
    //            Integer physicalAddressType = rs.getInt(1);
    //            Integer physicalAddressPurpose = rs.getInt(2);
    //
    //            int xxx = 0;
    //            xxx++;
    //          }
    //        }
    //      }
    //    }

    filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        "The correct number of filtered persons was not retrieved",
        1,
        filteredPersons.getPersons().size());

    comparePersons(person, filteredPersons.getPersons().get(0));

    person.setCountryOfBirth("GB");
    person.setCountryOfResidence("ZA");
    person.setCountryOfTaxResidence("ZA");
    person.setDateOfBirth(LocalDate.of(1985, 5, 1));
    person.setDateOfDeath(LocalDate.of(2200, 1, 1));
    person.setEmploymentStatus("employed");
    person.setEmploymentType("full_time");
    person.setGender("female");
    person.setGivenName(person.getGivenName() + " Updated");
    person.setHomeLanguage("AF");
    person.setInitials(person.getInitials() + " Updated");
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

    person.setCountryOfTaxResidence("GB");
    person.addTaxNumber(new TaxNumber("uk_tax_number", "GB", "987654321"));

    person.removeTaxNumber("za_income_tax_number");

    person.addIdentityDocument(
        new IdentityDocument(
            "passport", "ZA", LocalDate.of(2016, 10, 7), LocalDate.of(2025, 9, 1), "A1234567890"));

    person.removeIdentityDocumentByType("za_id_card");

    person.removeContactMechanism("fax_number", "main_fax_number");

    person
        .getContactMechanism(ContactMechanismType.EMAIL_ADDRESS, "personal_email_address")
        .setValue("test.updated@test.com");

    person.addContactMechanism(
        new ContactMechanism("phone_number", "home_phone_number", "0115551234"));

    partyService.updatePerson(person);

    filteredPersons =
        partyService.getPersons("", PersonSortBy.NAME, SortDirection.ASCENDING, 0, 100);

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

  /** Test the physical address purpose functionality. */
  @Test
  public void physicalAddressPurposeTest() throws Exception {
    List<PhysicalAddressPurpose> retrievedPhysicalAddressPurposes =
        partyService.getPhysicalAddressPurposes();

    retrievedPhysicalAddressPurposes = partyService.getPhysicalAddressPurposes("en-US");
  }

  /** Test the physical address type functionality. */
  @Test
  public void physicalAddressTypeTest() throws Exception {
    List<PhysicalAddressType> retrievedPhysicalAddressTypes =
        partyService.getPhysicalAddressTypes();

    retrievedPhysicalAddressTypes = partyService.getPhysicalAddressTypes("en-US");
  }

  /** Test the preference type category functionality. */
  @Test
  public void preferenceTypeCategoryTest() throws Exception {
    List<PreferenceTypeCategory> retrievedPreferenceTypeCategories =
        partyService.getPreferenceTypeCategories();

    retrievedPreferenceTypeCategories = partyService.getPreferenceTypeCategories("en-US");
  }

  /** Test the preference type functionality. */
  @Test
  public void preferenceTypeTest() throws Exception {
    List<PreferenceType> retrievedPreferenceTypes = partyService.getPreferenceTypes();

    retrievedPreferenceTypes = partyService.getPreferenceTypes("en-US");
  }

  /** Test the race reference functionality. */
  @Test
  public void raceTest() throws Exception {
    List<Race> retrievedRaces = partyService.getRaces();

    assertEquals("The correct number of races was not retrieved", 12, retrievedRaces.size());

    retrievedRaces = partyService.getRaces("en-US");

    assertEquals("The correct number of races was not retrieved", 6, retrievedRaces.size());
  }

  /** Test the residence permit type reference functionality. */
  @Test
  public void residencePermitTypeTest() throws Exception {
    List<ResidencePermitType> retrievedResidencePermitTypes =
        partyService.getResidencePermitTypes();

    assertEquals(
        "The correct number of residence permit types was not retrieved",
        18,
        retrievedResidencePermitTypes.size());

    retrievedResidencePermitTypes = partyService.getResidencePermitTypes("en-US");

    assertEquals(
        "The correct number of residence permit types was not retrieved",
        9,
        retrievedResidencePermitTypes.size());
  }

  /** Test the residency status reference functionality. */
  @Test
  public void residencyStatusTest() throws Exception {
    List<ResidencyStatus> retrievedResidencyStatuses = partyService.getResidencyStatuses();

    assertEquals(
        "The correct number of residency statuses was not retrieved",
        10,
        retrievedResidencyStatuses.size());

    retrievedResidencyStatuses = partyService.getResidencyStatuses("en-US");

    assertEquals(
        "The correct number of residency statuses was not retrieved",
        5,
        retrievedResidencyStatuses.size());
  }

  /** Test the residential type reference functionality. */
  @Test
  public void residentialTypeTest() throws Exception {
    List<ResidentialType> retrievedResidentialTypes = partyService.getResidentialTypes();

    assertEquals(
        "The correct number of residential types was not retrieved",
        14,
        retrievedResidentialTypes.size());

    retrievedResidentialTypes = partyService.getResidentialTypes("en-US");

    assertEquals(
        "The correct number of residential types was not retrieved",
        7,
        retrievedResidentialTypes.size());
  }

  /** Test the sources of funds reference functionality. */
  @Test
  public void sourceOfFundsTest() throws Exception {
    List<SourceOfFunds> retrievedSourceOfFunds = partyService.getSourcesOfFunds();

    assertEquals(
        "The correct number of sources of funds was not retrieved",
        38,
        retrievedSourceOfFunds.size());

    retrievedSourceOfFunds = partyService.getSourcesOfFunds("en-US");

    assertEquals(
        "The correct number of sources of funds was not retrieved",
        19,
        retrievedSourceOfFunds.size());
  }

  /** Test the tax number type reference functionality. */
  @Test
  public void taxNumberTypeTest() throws Exception {
    List<TaxNumberType> retrievedTaxNumberTypes = partyService.getTaxNumberTypes();

    assertEquals(
        "The correct number of tax number types was not retrieved",
        14,
        retrievedTaxNumberTypes.size());

    retrievedTaxNumberTypes = partyService.getTaxNumberTypes("en-US");

    assertEquals(
        "The correct number of tax number types was not retrieved",
        7,
        retrievedTaxNumberTypes.size());
  }

  /** Test the time to contact functionality. */
  @Test
  public void timeToContactTest() throws Exception {
    List<TimeToContact> retrievedTimesToContact = partyService.getTimesToContact();

    assertEquals(
        "The correct number of times to contact was not retrieved",
        10,
        retrievedTimesToContact.size());

    retrievedTimesToContact = partyService.getTimesToContact("en-US");

    assertEquals(
        "The correct number of times to contact was not retrieved",
        5,
        retrievedTimesToContact.size());
  }

  /** Test the title reference functionality. */
  @Test
  public void titleTest() throws Exception {
    List<Title> retrievedTitles = partyService.getTitles();

    assertEquals("The correct number of titles was not retrieved", 24, retrievedTitles.size());

    retrievedTitles = partyService.getTitles("en-US");

    assertEquals("The correct number of titles was not retrieved", 12, retrievedTitles.size());
  }

  /** Test the organization validation functionality. */
  @Test
  public void validateOrganizationTest() {
    Organization organization = getTestOrganizationDetails();

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(organization);
  }

  /** Test the party validation functionality. */
  @Test
  public void validatePartyTest() {
    Party party = getTestPartyDetails();

    Set<ConstraintViolation<Party>> constraintViolations = partyService.validateParty(party);
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

  /** Test the reference data validity check functionality. */
  @Test
  public void validityTest() throws Exception {
    partyService.isValidContactMechanismPurpose(
        "person", ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number");
    partyService.isValidContactMechanismType(ContactMechanismType.MOBILE_NUMBER);
    partyService.isValidEmploymentStatus("employed");
    partyService.isValidEmploymentType("employed", "full_time");
    partyService.isValidGender("female");
    partyService.isValidIdentityDocumentType("person", "passport");
    partyService.isValidMaritalStatus("married");
    partyService.isValidMarriageType("married", "anc_with_accrual");
    // referenceService.isValidMinorType("minor");
    partyService.isValidNextOfKinType("mother");
    partyService.isValidOccupation("executive");
    partyService.isValidPartyAttributeType("person", "height");
    partyService.isValidPartyAttributeTypeCategory("anthropometric_measurements");
    partyService.isValidPartyRolePurpose("test");
    partyService.isValidPartyRoleType("person", "employee");
    partyService.isValidPhysicalAddressPurpose("person", "billing");
    partyService.isValidPhysicalAddressType("complex");
    partyService.isValidPreferenceType("organization", "correspondence_language");
    partyService.isValidPreferenceTypeCategory("correspondence");
    partyService.isValidRace("white");
    partyService.isValidResidencePermitType("za_general_work_visa");
    partyService.isValidResidencyStatus("permanent_resident");
    partyService.isValidResidentialType("owner");
    partyService.isValidSourceOfFunds("salary");
    partyService.isValidTaxNumberType("za_income_tax_number");
    partyService.isValidTimeToContact("anytime");
    partyService.isValidTitle("mrs");
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
        "The number of contact mechanisms for the two persons do not match",
        organization1.getContactMechanisms().size(),
        organization2.getContactMechanisms().size());

    for (ContactMechanism person1ContactMechanism : organization1.getContactMechanisms()) {
      boolean foundContactMechanism = false;

      for (ContactMechanism person2ContactMechanism : organization2.getContactMechanisms()) {

        if (Objects.equals(person1ContactMechanism.getParty(), person2ContactMechanism.getParty())
            && Objects.equals(person1ContactMechanism.getType(), person2ContactMechanism.getType())
            && Objects.equals(
                person1ContactMechanism.getPurpose(), person2ContactMechanism.getPurpose())) {
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
                + person1ContactMechanism.getPurpose()
                + ")");
      }
    }

    assertEquals(
        "The number of physical addresses for the two persons do not match",
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
        "The home language values for the two persons do not match",
        person1.getHomeLanguage(),
        person2.getHomeLanguage());
    assertEquals(
        "The ID values for the two persons do not match", person1.getId(), person2.getId());
    assertEquals(
        "The initials values for the two persons do not match",
        person1.getInitials(),
        person2.getInitials());
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
              "The date of expiry for the two identity documents do not match",
              person1IdentityDocument.getDateOfExpiry(),
              person2IdentityDocument.getDateOfExpiry());
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
                person1ContactMechanism.getPurpose(), person2ContactMechanism.getPurpose())) {
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
                + person1ContactMechanism.getPurpose()
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

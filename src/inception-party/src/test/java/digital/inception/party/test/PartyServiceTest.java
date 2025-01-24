/*
 * Copyright Marcus Portmann
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

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.person.PersonProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.sorting.SortDirection;
import digital.inception.party.model.Association;
import digital.inception.party.model.AssociationProperty;
import digital.inception.party.model.AssociationSortBy;
import digital.inception.party.model.AssociationsForParty;
import digital.inception.party.model.Attribute;
import digital.inception.party.model.Consent;
import digital.inception.party.model.ContactMechanism;
import digital.inception.party.model.ContactMechanismRole;
import digital.inception.party.model.ContactMechanismType;
import digital.inception.party.model.Education;
import digital.inception.party.model.Employment;
import digital.inception.party.model.EntityType;
import digital.inception.party.model.ExternalReference;
import digital.inception.party.model.Identification;
import digital.inception.party.model.IndustryAllocation;
import digital.inception.party.model.LanguageProficiency;
import digital.inception.party.model.LanguageProficiencyLevel;
import digital.inception.party.model.Lock;
import digital.inception.party.model.Mandatary;
import digital.inception.party.model.Mandate;
import digital.inception.party.model.MandateLink;
import digital.inception.party.model.MandateProperty;
import digital.inception.party.model.MandateSortBy;
import digital.inception.party.model.MandatesForParty;
import digital.inception.party.model.MeasurementSystem;
import digital.inception.party.model.MeasurementUnit;
import digital.inception.party.model.NextOfKin;
import digital.inception.party.model.Organization;
import digital.inception.party.model.OrganizationSortBy;
import digital.inception.party.model.Organizations;
import digital.inception.party.model.Parties;
import digital.inception.party.model.Party;
import digital.inception.party.model.PartySortBy;
import digital.inception.party.model.PartyType;
import digital.inception.party.model.Person;
import digital.inception.party.model.PersonSortBy;
import digital.inception.party.model.Persons;
import digital.inception.party.model.PhysicalAddress;
import digital.inception.party.model.PhysicalAddressPurpose;
import digital.inception.party.model.PhysicalAddressRole;
import digital.inception.party.model.PhysicalAddressType;
import digital.inception.party.model.Preference;
import digital.inception.party.model.RequiredMandataries;
import digital.inception.party.model.ResidencePermit;
import digital.inception.party.model.Role;
import digital.inception.party.model.SegmentAllocation;
import digital.inception.party.model.Skill;
import digital.inception.party.model.SkillProficiencyLevel;
import digital.inception.party.model.Snapshots;
import digital.inception.party.model.SourceOfFunds;
import digital.inception.party.model.SourceOfWealth;
import digital.inception.party.model.Status;
import digital.inception.party.model.TaxNumber;
import digital.inception.party.model.ValueType;
import digital.inception.party.service.PartyService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import jakarta.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.util.StringUtils;

/**
 * The <b>PartyServiceTest</b> class contains the JUnit tests for the <b>PartyService</b> class.
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

  private static int partyCount;

  /** The Jackson2 object mapper. */
  @Autowired private ObjectMapper objectMapper;

  /** The Party Service. */
  @Autowired private PartyService partyService;

  private static synchronized Organization getTestBasicOrganizationDetails() {
    com.devskiller.jfairy.producer.person.DefaultPersonProvider xxx;

    Fairy fairy = Fairy.create();

    com.devskiller.jfairy.producer.person.Person generatedPerson = fairy.person();

    Company generatedCompany = generatedPerson.getCompany();

    return new Organization(PartyService.DEFAULT_TENANT_ID, generatedCompany.getName());
  }

  private static synchronized Person getTestBasicPersonDetails() {
    Fairy fairy = Fairy.create();

    com.devskiller.jfairy.producer.person.Person generatedPerson =
        fairy.person(PersonProperties.male(), PersonProperties.minAge(21));

    return new Person(
        PartyService.DEFAULT_TENANT_ID,
        generatedPerson.getFirstName() + " " + generatedPerson.getLastName());
  }

  private static synchronized Person getTestCompletePersonDetails(boolean isMarried) {
    Fairy fairy = Fairy.create();

    com.devskiller.jfairy.producer.person.Person generatedPerson =
        fairy.person(PersonProperties.female(), PersonProperties.minAge(21));

    boolean hasMiddleName = StringUtils.hasText(generatedPerson.getMiddleName());

    Person person;

    if (hasMiddleName) {
      person =
          new Person(
              PartyService.DEFAULT_TENANT_ID,
              generatedPerson.getFirstName()
                  + " "
                  + generatedPerson.getMiddleName()
                  + " "
                  + generatedPerson.getLastName());
    } else {
      person =
          new Person(
              PartyService.DEFAULT_TENANT_ID,
              generatedPerson.getFirstName() + " " + generatedPerson.getLastName());
    }

    person.setCountryOfBirth("US");
    person.setCountryOfResidence("ZA");
    person.setDateOfBirth(LocalDate.of(1976, 3, 7));
    person.setEmploymentStatus("employed");
    person.setEmploymentType("self_employed");
    person.setGender("female");
    person.setGivenName(generatedPerson.getFirstName());
    person.setId(UuidCreator.getTimeOrderedEpoch());

    if (hasMiddleName) {
      person.setInitials(
          generatedPerson.getFirstName().charAt(0)
              + " "
              + generatedPerson.getMiddleName().charAt(0));
    } else {
      person.setInitials("" + generatedPerson.getFirstName().charAt(0));
    }
    person.setLanguage("EN");

    if (isMarried) {
      person.setMaidenName(
          fairy.person(PersonProperties.female(), PersonProperties.minAge(21)).getLastName());

      person.setMaritalStatus("married");
      person.setMarriageType("anc_with_accrual");
      person.setMaritalStatusDate(LocalDate.of(2015, 10, 10));
    } else {
      person.setMaritalStatus("single");
    }

    person.setMeasurementSystem(MeasurementSystem.METRIC);
    if (hasMiddleName) {
      person.setMiddleNames(generatedPerson.getMiddleName());
    }
    person.setOccupation("professional_legal");
    person.setPreferredName(generatedPerson.getFirstName());
    person.setRace("white");
    person.setResidencyStatus("permanent_resident");
    person.setResidentialType("renter");
    person.setSurname(generatedPerson.getLastName());
    person.setTimeZone("Africa/Johannesburg");
    person.setTitle("ms");

    Attribute weightAttribute =
        new Attribute("weight", new BigDecimal("52.7"), MeasurementUnit.METRIC_KILOGRAM);
    weightAttribute.setDecimalValue(60);
    weightAttribute.setDecimalValue(52.7);
    weightAttribute.setDecimalValue(new BigDecimal("52.7"));

    person.addAttribute(weightAttribute);

    assertTrue(person.hasAttributeWithType("weight"));

    assertTrue(
        person.hasAttributeWithType("weight"),
        "Failed to confirm that the person has an attribute with type (weight)");

    person.setCountryOfTaxResidence("ZA");
    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "0123456789"));

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

    person.setIdentificationType("za_id");
    person.setIdentificationNumber("8904085800089");
    person.setIdentificationIssueDate(LocalDate.of(2012, 5, 1));
    person.setIdentificationExpiryDate(LocalDate.of(2040, 2, 28));
    person.setIdentificationCountryOfIssue("ZA");

    assertEquals("za_id", person.getIdentificationType());
    assertEquals("8904085800089", person.getIdentificationNumber());
    assertEquals(LocalDate.of(2012, 5, 1), person.getIdentificationIssueDate());
    assertEquals(LocalDate.of(2040, 2, 28), person.getIdentificationExpiryDate());
    assertEquals("ZA", person.getIdentificationCountryOfIssue());

    Identification identification =
        new Identification("za_id", "ZA", LocalDate.of(2012, 5, 1), "8904085800089");

    assertEquals("ZA", identification.getCountryOfIssue());
    assertEquals(LocalDate.of(2012, 5, 1), identification.getIssueDate());

    person.addIdentification(identification);

    LocalDate currentDate = LocalDate.now();

    identification.setCountryOfIssue("ZA");
    identification.setIssueDate(LocalDate.of(2014, 7, 6));
    identification.setExpiryDate(LocalDate.of(2050, 3, 4));
    identification.setProvidedDate(currentDate);

    assertEquals("ZA", identification.getCountryOfIssue());
    assertEquals(LocalDate.of(2014, 7, 6), identification.getIssueDate());
    assertEquals(currentDate, identification.getProvidedDate());
    assertEquals(LocalDate.of(2050, 3, 4), identification.getExpiryDate());

    PhysicalAddress residentialAddress =
        new PhysicalAddress(
            PhysicalAddressType.STREET,
            PhysicalAddressRole.RESIDENTIAL,
            List.of(
                new String[] {
                  PhysicalAddressPurpose.BILLING,
                  PhysicalAddressPurpose.CORRESPONDENCE,
                  PhysicalAddressPurpose.DELIVERY
                }));
    residentialAddress.setStreetNumber("13");
    residentialAddress.setStreetName("Kraalbessie Avenue");
    residentialAddress.setSuburb("Weltevreden Park");
    residentialAddress.setCity("Johannesburg");
    residentialAddress.setRegion("ZA-GP");
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

    correspondenceAddress.setLine1("1 Galaxy Place");
    correspondenceAddress.setLine2("Sandhurst");
    correspondenceAddress.setCity("Sandton");
    correspondenceAddress.setRegion("ZA-GP");
    correspondenceAddress.setCountry("ZA");
    correspondenceAddress.setPostalCode("2194");

    person.addPhysicalAddress(correspondenceAddress);

    person.addPreference(new Preference("correspondence_language", "EN"));

    person.addSkill(new Skill("administration", SkillProficiencyLevel.EXPERT));

    return person;
  }

  private static synchronized Person getTestForeignPersonDetails() {
    Fairy fairy = Fairy.create();

    com.devskiller.jfairy.producer.person.Person generatedPerson =
        fairy.person(PersonProperties.male(), PersonProperties.minAge(21));

    boolean hasMiddleName = StringUtils.hasText(generatedPerson.getMiddleName());

    Person person;

    if (hasMiddleName) {
      person =
          new Person(
              PartyService.DEFAULT_TENANT_ID,
              generatedPerson.getFirstName()
                  + " "
                  + generatedPerson.getMiddleName()
                  + " "
                  + generatedPerson.getLastName());
    } else {
      person =
          new Person(
              PartyService.DEFAULT_TENANT_ID,
              generatedPerson.getFirstName() + " " + generatedPerson.getLastName());
    }

    person.setCountryOfBirth("ZW");
    person.setCountryOfResidence("ZA");
    person.setDateOfBirth(LocalDate.of(1976, 3, 7));
    person.setEmploymentStatus("employed");
    person.setEmploymentType("contractor");
    person.setGender("male");
    person.setGivenName(generatedPerson.getFirstName());
    person.setId(UuidCreator.getTimeOrderedEpoch());
    if (hasMiddleName) {
      person.setInitials(
          generatedPerson.getFirstName().charAt(0)
              + " "
              + generatedPerson.getMiddleName().charAt(0));
    } else {
      person.setInitials("" + generatedPerson.getFirstName().charAt(0));
    }
    person.setLanguage("EN");
    person.setMaritalStatus("single");
    person.setOccupation("driver");
    person.setPreferredName(generatedPerson.getFirstName());
    person.setRace("black");
    person.setResidencyStatus("foreign_national");
    person.setResidentialType("renter");
    person.setSurname(generatedPerson.getLastName());
    person.setTitle("mr");

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27825551234"));

    person.addIdentification(
        new Identification("passport", "ZW", LocalDate.of(2010, 5, 20), "A01524783"));

    person.addResidencePermit(
        new ResidencePermit("za_general_work_visa", "ZA", LocalDate.of(2011, 4, 2), "AA0187236"));

    PhysicalAddress residentialAddress =
        new PhysicalAddress(
            PhysicalAddressType.STREET,
            PhysicalAddressRole.RESIDENTIAL,
            List.of(
                new String[] {
                  PhysicalAddressPurpose.BILLING,
                  PhysicalAddressPurpose.CORRESPONDENCE,
                  PhysicalAddressPurpose.DELIVERY
                }));
    residentialAddress.setStreetNumber("4");
    residentialAddress.setStreetName("Princess Avenue");
    residentialAddress.setSuburb("Windsor East");
    residentialAddress.setCity("Johannesburg");
    residentialAddress.setRegion("ZA-GP");
    residentialAddress.setCountry("ZA");
    residentialAddress.setPostalCode("2194");

    person.addPhysicalAddress(residentialAddress);

    return person;
  }

  private static synchronized Organization getTestOrganizationDetails() {
    Fairy fairy = Fairy.create();

    com.devskiller.jfairy.producer.person.Person generatedPerson = fairy.person();

    Company generatedCompany = generatedPerson.getCompany();

    Organization organization =
        new Organization(PartyService.DEFAULT_TENANT_ID, generatedCompany.getName());

    organization.setIdentificationType("za_company_registration_certificate");
    organization.setIdentificationNumber("2006/123456/23");
    organization.setIdentificationIssueDate(LocalDate.of(2006, 4, 2));
    organization.setIdentificationExpiryDate(LocalDate.of(2040, 2, 28));
    organization.setIdentificationCountryOfIssue("ZA");

    assertEquals("za_company_registration_certificate", organization.getIdentificationType());
    assertEquals("2006/123456/23", organization.getIdentificationNumber());
    assertEquals(LocalDate.of(2006, 4, 2), organization.getIdentificationIssueDate());
    assertEquals(LocalDate.of(2040, 2, 28), organization.getIdentificationExpiryDate());
    assertEquals("ZA", organization.getIdentificationCountryOfIssue());

    organization.addIdentification(
        new Identification(
            "za_company_registration_certificate",
            "ZA",
            LocalDate.of(2006, 4, 2),
            "2006/123456/23"));

    organization.addIndustryAllocation(
        new IndustryAllocation("sars_sic", "63", LocalDate.now(), LocalDate.of(2036, 4, 2)));

    organization.addRole(new Role("employer"));

    return organization;
  }

  private static synchronized Party getTestPartyDetails() {
    partyCount++;

    return new Party(
        PartyService.DEFAULT_TENANT_ID, PartyType.ORGANIZATION, "Party Name " + partyCount);
  }

  /** Test the association functionality. */
  @Test
  public void associationTest() throws Exception {
    Person firstPerson = getTestCompletePersonDetails(true);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, firstPerson);

    Person secondPerson = getTestCompletePersonDetails(true);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, secondPerson);

    Association association =
        new Association(
            PartyService.DEFAULT_TENANT_ID,
            "test_association_type",
            firstPerson.getId(),
            secondPerson.getId(),
            LocalDate.now());

    AssociationProperty booleanAssociationProperty =
        new AssociationProperty("test_boolean_property", true);
    booleanAssociationProperty.setBooleanValue(true);

    AssociationProperty dateAssociationProperty =
        new AssociationProperty("test_date_property", LocalDate.now());
    dateAssociationProperty.setDateValue(LocalDate.now());

    AssociationProperty decimalAssociationProperty =
        new AssociationProperty("test_decimal_property", new BigDecimal("82.6"));
    decimalAssociationProperty.setDecimalValue(777);
    decimalAssociationProperty.setDecimalValue(82.6);
    decimalAssociationProperty.setDecimalValue("82.6");
    decimalAssociationProperty.setDecimalValue(new BigDecimal("82.6"));

    AssociationProperty doubleAssociationProperty =
        new AssociationProperty("test_double_property", 12345.6789);
    doubleAssociationProperty.setDoubleValue(12345.6789);

    AssociationProperty integerAssociationProperty =
        new AssociationProperty("test_integer_property", Integer.valueOf(123456789));
    integerAssociationProperty.setIntegerValue(Integer.valueOf(123456789));

    AssociationProperty stringAssociationProperty =
        new AssociationProperty("test_string_property", "String Value");
    stringAssociationProperty.setStringValue("String Value");

    association.addProperty(booleanAssociationProperty);
    association.addProperty(dateAssociationProperty);
    association.addProperty(decimalAssociationProperty);
    association.addProperty(doubleAssociationProperty);
    association.addProperty(integerAssociationProperty);
    association.addProperty(stringAssociationProperty);

    assertEquals("String Value", stringAssociationProperty.getStringValue());
    assertTrue(association.hasPropertyWithType("test_string_property"));

    assertTrue(association.getPropertyWithType("test_string_property").isPresent());

    assertTrue(stringAssociationProperty.hasValue(ValueType.STRING));

    partyService.createAssociation(PartyService.DEFAULT_TENANT_ID, association);

    AssociationsForParty associationsForParty =
        partyService.getAssociationsForParty(
            PartyService.DEFAULT_TENANT_ID,
            firstPerson.getId(),
            AssociationSortBy.TYPE,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1,
        associationsForParty.getAssociations().size(),
        "The incorrect number of associations was retrieved");

    compareAssociations(association, associationsForParty.getAssociations().getFirst());

    Association retrievedAssociation =
        partyService.getAssociation(PartyService.DEFAULT_TENANT_ID, association.getId());

    assertEquals(
        6,
        retrievedAssociation.getProperties().size(),
        "The incorrect number of association properties was retrieved");

    compareAssociations(association, retrievedAssociation);

    association.removePropertyWithType("test_string_property");
    assertFalse(association.hasPropertyWithType("test_string_property"));

    association.removePropertyWithType("test_date_property");
    assertFalse(association.hasPropertyWithType("test_date_property"));

    partyService.updateAssociation(PartyService.DEFAULT_TENANT_ID, association);

    retrievedAssociation =
        partyService.getAssociation(PartyService.DEFAULT_TENANT_ID, association.getId());

    assertEquals(
        4,
        retrievedAssociation.getProperties().size(),
        "The incorrect number of association properties was retrieved");

    partyService.deleteAssociation(PartyService.DEFAULT_TENANT_ID, association.getId());

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, firstPerson.getId());

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, secondPerson.getId());
  }

  /** Test the attribute functionality. */
  @Test
  public void attributeTest() throws Exception {
    // Person attributes
    Person person = getTestBasicPersonDetails();

    assertEquals("booleanValue", Attribute.getAttributeNameForValueType(ValueType.BOOLEAN));
    assertEquals("dateValue", Attribute.getAttributeNameForValueType(ValueType.DATE));
    assertEquals("decimalValue", Attribute.getAttributeNameForValueType(ValueType.DECIMAL));
    assertEquals("doubleValue", Attribute.getAttributeNameForValueType(ValueType.DOUBLE));
    assertEquals("integerValue", Attribute.getAttributeNameForValueType(ValueType.INTEGER));
    assertEquals("stringValue", Attribute.getAttributeNameForValueType(ValueType.STRING));

    person.addAttribute(new Attribute("test_attribute_type", "test_attribute_value"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareAttributes(
        person.getAttributeWithType("test_attribute_type").get(),
        retrievedPerson.getAttributeWithType("test_attribute_type").get());

    assertTrue(retrievedPerson.hasAttributeWithType("test_attribute_type"));

    person.removeAttributeWithType("test_attribute_type");

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setAttributes(List.of(new Attribute("test_attribute_type", "test_attribute_value")));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization attributes
    Organization organization = getTestBasicOrganizationDetails();

    organization.addAttribute(new Attribute("test_attribute_type", "test_attribute_value"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareAttributes(
        organization.getAttributeWithType("test_attribute_type").get(),
        retrievedOrganization.getAttributeWithType("test_attribute_type").get());

    assertTrue(retrievedOrganization.hasAttributeWithType("test_attribute_type"));

    organization.removeAttributeWithType("test_attribute_type");

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setAttributes(
        List.of(new Attribute("test_attribute_type", "test_attribute_value")));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the person functionality. */
  @Test
  public void basicPersonTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number", "+27835551234"));

    person.addIdentification(
        new Identification("za_id", "ZA", LocalDate.of(2012, 5, 1), "8904085800089"));

    person.addPreference(new Preference("correspondence_language", "EN"));

    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "0123456789"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);
  }

  /** Test the consent functionality. */
  @Test
  public void consentTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addConsent(new Consent("marketing"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareConsents(
        person.getConsentWithType("marketing").get(),
        retrievedPerson.getConsentWithType("marketing").get());

    assertTrue(retrievedPerson.hasConsentWithType("marketing"));

    person.removeConsentWithType("marketing");

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setConsents(List.of(new Consent("marketing", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());
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
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

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
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

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

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

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

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setContactMechanisms(
        List.of(
            new ContactMechanism(
                ContactMechanismType.EMAIL_ADDRESS,
                ContactMechanismRole.PERSONAL_EMAIL_ADDRESS,
                "giveName@test.com",
                "marketing")));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization contactMechanisms
    Organization organization = getTestBasicOrganizationDetails();

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.MAIN_EMAIL_ADDRESS,
            "test@test.com",
            "marketing"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

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

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setContactMechanisms(
        List.of(
            new ContactMechanism(
                ContactMechanismType.EMAIL_ADDRESS,
                ContactMechanismRole.MAIN_EMAIL_ADDRESS,
                "test@test.com",
                "marketing")));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the education functionality. */
  @Test
  public void educationTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    Education education = new Education();
    education.setId(UuidCreator.getTimeOrderedEpoch());
    education.setInstitutionCountry("ZA");
    education.setInstitutionName("University of the Witwatersrand");
    education.setQualificationType("bachelors_degree");
    education.setQualificationName("Bachelor of Science in Electrical Engineering");
    education.setQualificationYear(1998);
    education.setFieldOfStudy("electrical_engineering");
    education.setFirstYearAttended(1994);
    education.setLastYearAttended(1998);

    person.addEducation(education);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    assertTrue(retrievedPerson.getEducationWithId(education.getId()).isPresent());

    compareEducations(
        person.getEducationWithId(education.getId()).get(),
        retrievedPerson.getEducationWithId(education.getId()).get());

    person.removeEducationWithId(education.getId());

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setEducations(
        List.of(new Education("Northcliff High School", "national_senior_certificate", 1993)));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());
  }

  /** Test the employment functionality. */
  @Test
  public void employmentTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    Employment employment = new Employment();
    employment.setId(UuidCreator.getTimeOrderedEpoch());
    employment.setEmployerName("Galaxy Software");
    employment.setEmployerPhoneNumber("+27 11 324 5000");
    employment.setEmployerEmailAddress("contactus@galaxy.co.za");
    employment.setEmployerContactPerson("Joe Bloggs");
    employment.setEmployerAddressLine1("3rd Floor Grove");
    employment.setEmployerAddressLine2("1 Galaxy Place");
    employment.setEmployerAddressLine3("Line 3");
    employment.setEmployerAddressLine4("Line 4");
    employment.setEmployerAddressSuburb("Sandhurst");
    employment.setEmployerAddressCity("Sandton");
    employment.setEmployerAddressRegion("ZA-GP");
    employment.setEmployerAddressCountry("ZA");
    employment.setEmployerAddressPostalCode("2196");
    employment.setStartDate(LocalDate.of(2017, 2, 1));
    employment.setEndDate(LocalDate.of(2020, 4, 30));
    employment.setType("full_time");
    employment.setOccupation("executive");

    person.addEmployment(employment);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    assertTrue(retrievedPerson.getEmploymentWithId(employment.getId()).isPresent());

    compareEmployments(
        person.getEmploymentWithId(employment.getId()).get(),
        retrievedPerson.getEmploymentWithId(employment.getId()).get());

    person.removeEmploymentWithId(employment.getId());

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setEmployments(List.of(new Employment("Absa", LocalDate.of(2020, 5, 1))));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());
  }

  /** Test the external reference functionality. */
  @Test
  public void externalReferenceTest() throws Exception {
    // Person identifications
    Person person = getTestBasicPersonDetails();

    person.addExternalReference(new ExternalReference("legacy_customer_code", "TEST001"));
    person.addExternalReference(
        new ExternalReference("test_external_reference_type", "Test External Reference"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareExternalReferences(
        person.getExternalReferenceWithType("test_external_reference_type").get(),
        retrievedPerson.getExternalReferenceWithType("test_external_reference_type").get());

    assertTrue(retrievedPerson.hasExternalReferenceWithType("legacy_customer_code"));
    assertTrue(retrievedPerson.hasExternalReferenceWithType("test_external_reference_type"));

    person.removeExternalReferenceWithType("legacy_customer_code");

    assertFalse(person.hasExternalReferenceWithType("legacy_customer_code"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setExternalReferences(
        List.of(
            new ExternalReference(
                "test_external_reference_type", "Another Test External Reference")));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization identifications
    Organization organization = getTestBasicOrganizationDetails();

    organization.addExternalReference(new ExternalReference("legacy_customer_code", "TEST002"));
    organization.addExternalReference(
        new ExternalReference("test_external_reference_type", "Test External Reference"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareExternalReferences(
        organization.getExternalReferenceWithType("test_external_reference_type").get(),
        retrievedOrganization.getExternalReferenceWithType("test_external_reference_type").get());

    assertTrue(retrievedOrganization.hasExternalReferenceWithType("legacy_customer_code"));
    assertTrue(retrievedOrganization.hasExternalReferenceWithType("test_external_reference_type"));

    organization.removeExternalReferenceWithType("legacy_customer_code");

    assertFalse(organization.hasExternalReferenceWithType("legacy_customer_code"));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setExternalReferences(
        List.of(
            new ExternalReference(
                "test_external_reference_type", "Another Test External Reference")));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the foreign person functionality. */
  @Test
  public void foreignPersonTest() throws Exception {
    Person foreignPerson = getTestForeignPersonDetails();

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, foreignPerson);

    Persons filteredPersons =
        partyService.getPersons(
            PartyService.DEFAULT_TENANT_ID,
            "",
            PersonSortBy.NAME,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered persons was not retrieved");

    comparePersons(foreignPerson, filteredPersons.getPersons().getFirst());

    assertTrue(foreignPerson.hasResidencePermitWithType("za_general_work_visa"));

    foreignPerson.removeResidencePermitWithType("za_general_work_visa");

    assertFalse(foreignPerson.hasResidencePermitWithType("za_general_work_visa"));

    foreignPerson.setResidencePermits(
        List.of(
            new ResidencePermit(
                "za_critical_skills_visa", "ZA", LocalDate.of(2015, 3, 12), "CS1234567890")));

    assertTrue(foreignPerson.hasResidencePermitWithType("za_critical_skills_visa"));

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, foreignPerson.getId());
  }

  /** Test the identification functionality. */
  @Test
  public void identificationTest() throws Exception {
    // Person identifications
    Person person = getTestBasicPersonDetails();

    person.addIdentification(
        new Identification("za_id", "ZA", LocalDate.of(2003, 4, 13), "8904085800089"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareIdentifications(
        person.getIdentificationWithType("za_id").get(),
        retrievedPerson.getIdentificationWithType("za_id").get());

    assertTrue(retrievedPerson.hasIdentificationWithType("za_id"));

    person.removeIdentificationWithType("za_id");

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setIdentifications(
        List.of(new Identification("za_id", "ZA", LocalDate.of(2018, 7, 16), "8904085800089")));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization identifications
    Organization organization = getTestBasicOrganizationDetails();

    organization.addIdentification(
        new Identification(
            "za_company_registration_certificate",
            "ZA",
            LocalDate.of(2006, 4, 2),
            "2006/123456/23"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareIdentifications(
        organization.getIdentificationWithType("za_company_registration_certificate").get(),
        retrievedOrganization
            .getIdentificationWithType("za_company_registration_certificate")
            .get());

    assertTrue(
        retrievedOrganization.hasIdentificationWithType("za_company_registration_certificate"));

    organization.removeIdentificationWithType("za_company_registration_certificate");

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setIdentifications(
        List.of(
            new Identification(
                "za_company_registration_certificate",
                "ZA",
                LocalDate.of(2016, 7, 21),
                "2016/654321/21")));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the invalid association property functionality. */
  @Test
  public void invalidAssociationPropertyTest() throws Exception {
    Person firstPerson = getTestCompletePersonDetails(true);

    Person secondPerson = getTestCompletePersonDetails(true);

    Association association =
        new Association(
            PartyService.DEFAULT_TENANT_ID,
            "test_association_type",
            firstPerson.getId(),
            secondPerson.getId());

    // Test setters
    association.setFirstPartyId(firstPerson.getId());
    association.setSecondPartyId(secondPerson.getId());

    // Test null properties
    association.addProperty(new AssociationProperty("test_boolean_property"));
    association.addProperty(new AssociationProperty("test_date_property"));
    association.addProperty(new AssociationProperty("test_decimal_property"));
    association.addProperty(new AssociationProperty("test_double_property"));
    association.addProperty(new AssociationProperty("test_integer_property"));
    association.addProperty(new AssociationProperty("test_string_property"));

    Set<ConstraintViolation<Association>> constraintViolations =
        partyService.validateAssociation(PartyService.DEFAULT_TENANT_ID, association);

    assertEquals(
        6,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the association");

    // Test invalid properties
    association.getProperties().clear();

    association.addProperty(
        new AssociationProperty("invalid_property_name", "Invalid Property Value"));

    association.addProperty(
        new AssociationProperty("test_string_property", "Invalid String Value!"));

    constraintViolations =
        partyService.validateAssociation(PartyService.DEFAULT_TENANT_ID, association);

    assertEquals(
        2,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the association");
  }

  /** Test the invalid attribute functionality. */
  @Test
  public void invalidAttributeTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    // Test null attributes
    person.addAttribute(new Attribute("test_boolean_attribute"));
    person.addAttribute(new Attribute("test_date_attribute"));
    person.addAttribute(new Attribute("test_decimal_attribute"));
    person.addAttribute(new Attribute("test_double_attribute"));
    person.addAttribute(new Attribute("test_integer_attribute"));
    person.addAttribute(new Attribute("test_string_attribute"));

    Set<ConstraintViolation<Person>> personConstraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        6,
        personConstraintViolations.size(),
        "The correct number of constraint violations was not found for the person");

    // Test invalid attributes
    person.getAttributes().clear();

    person.addAttribute(new Attribute("invalid_attribute_name", "Invalid Attribute Value"));

    person.addAttribute(new Attribute("test_string_attribute", "Invalid String Value!"));

    personConstraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        2,
        personConstraintViolations.size(),
        "The correct number of constraint violations was not found for the person");

    Organization organization = getTestBasicOrganizationDetails();

    // Test null attributes
    organization.addAttribute(new Attribute("test_boolean_attribute"));
    organization.addAttribute(new Attribute("test_date_attribute"));
    organization.addAttribute(new Attribute("test_decimal_attribute"));
    organization.addAttribute(new Attribute("test_double_attribute"));
    organization.addAttribute(new Attribute("test_integer_attribute"));
    organization.addAttribute(new Attribute("test_string_attribute"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        6,
        organizationConstraintViolations.size(),
        "The correct number of constraint violations was not found for the organization");

    // Test invalid attributes
    organization.getAttributes().clear();

    organization.addAttribute(new Attribute("invalid_attribute_name", "Invalid Attribute Value"));

    organization.addAttribute(new Attribute("test_string_attribute", "Invalid String Value!"));

    organizationConstraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        2,
        organizationConstraintViolations.size(),
        "The correct number of constraint violations was not found for the organization");
  }

  /** Test the invalid building address verification functionality. */
  @Test
  public void invalidBuildingAddressTest() throws Exception {
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.BUILDING, PhysicalAddressRole.RESIDENTIAL);

    Person person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

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
    invalidAddress.setLine4("Line 4");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    constraintViolations = partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        15,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid building address");
  }

  /** Test the invalid complex address verification functionality. */
  @Test
  public void invalidComplexAddressTest() throws Exception {

    // Validate an empty invalid address
    // Required: Complex Name, Complex Unit Number, Street Name, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.COMPLEX, PhysicalAddressRole.RESIDENTIAL);

    Person person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

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
    invalidAddress.setLine4("Line 4");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    constraintViolations = partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        16,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid complex address");
  }

  /** Test the invalid contact mechanism functionality. */
  @Test
  public void invalidContactMechanismTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.MAIN_EMAIL_ADDRESS,
            "invalid_contact_mechanism_role@test.com"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.PERSONAL_EMAIL_ADDRESS,
            "test@",
            "invalid_purpose"));

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.SOCIAL_MEDIA,
            ContactMechanismRole.TWITTER_ID,
            "!invalid_twitter_id"));

    Set<ConstraintViolation<Person>> personConstraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        4,
        personConstraintViolations.size(),
        "The correct number of constraint violations was not found for the person");

    Organization organization = getTestBasicOrganizationDetails();

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.PERSONAL_EMAIL_ADDRESS,
            "invalid_contact_mechanism_role@test.com"));

    organization.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.EMAIL_ADDRESS,
            ContactMechanismRole.MAIN_EMAIL_ADDRESS,
            "test@",
            "invalid_purpose"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        3,
        organizationConstraintViolations.size(),
        "The correct number of constraint violations was not found for the organization");
  }

  /** Test the invalid education verification functionality. */
  @Test
  public void invalidEducationTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.setHighestQualificationType("invalid_highest_qualification");

    person.addEducation(
        new Education(
            "XX",
            "Invalid Institution Name$",
            "invalid_qualification_type",
            "Invalid Qualification Name$",
            null,
            "invalid_field_of_study"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        7,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the person with an invalid education");
  }

  /** Test the invalid employment verification functionality. */
  @Test
  public void invalidEmploymentTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addEmployment(
        new Employment(
            null,
            "Invalid Employer Phone Number$",
            "Invalid Employer Email Address$",
            "Invalid Employer Contact Person$",
            "Invalid Employer Address Line 1$",
            "Invalid Employer Address Line 2$",
            "Invalid Employer Address Line 3$",
            "Invalid Employer Address Line 4$",
            "Invalid Employer Address Suburb$",
            "Invalid Employer Address City$",
            "Invalid Employer Address Region$",
            "XX",
            "Invalid Employer Address Postal Code$",
            null,
            null,
            "invalid_employment_type",
            "invalid_occupation"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        17,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the person with an invalid employment");
  }

  /** Test the invalid external reference functionality. */
  @Test
  public void invalidExternalReferenceTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addExternalReference(
        new ExternalReference("legacy_customer_code", "Invalid Legacy Customer Code"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the person with an invalid external reference");
  }

  /** Test the invalid farm address verification functionality. */
  @Test
  public void invalidFarmAddressTest() throws Exception {
    // Validate an empty invalid address
    // Required: Farm Number, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.FARM, PhysicalAddressRole.HOME);

    Person person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

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
    invalidAddress.setLine4("Line 4");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    constraintViolations = partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        15,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid farm address");
  }

  /** Test the invalid international address verification functionality. */
  @Test
  public void invalidInternationalAddressTest() throws Exception {
    // Validate an empty invalid address
    // Required: Line 1, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.INTERNATIONAL, PhysicalAddressRole.RESIDENTIAL);

    Person person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

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
    invalidAddress.setLine4("Line 4");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    constraintViolations = partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        17,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid international address");
  }

  /** Test the invalid language proficiency verification functionality. */
  @Test
  public void invalidLanguageProficiencyTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    LanguageProficiency languageProficiency = new LanguageProficiency();
    languageProficiency.setLanguage("XX");

    person.addLanguageProficiency(languageProficiency);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        5,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the person with an invalid language proficiency");
  }

  /** Test the invalid mandate property functionality. */
  @Test
  public void invalidMandatePropertyTest() throws Exception {
    Mandate mandate =
        new Mandate(PartyService.DEFAULT_TENANT_ID, "test_mandate_type", RequiredMandataries.ALL);

    // Test null properties
    mandate.addProperty(new MandateProperty("test_boolean_property"));
    mandate.addProperty(new MandateProperty("test_date_property"));
    mandate.addProperty(new MandateProperty("test_decimal_property"));
    mandate.addProperty(new MandateProperty("test_double_property"));
    mandate.addProperty(new MandateProperty("test_integer_property"));
    mandate.addProperty(new MandateProperty("test_string_property"));

    Set<ConstraintViolation<Mandate>> constraintViolations =
        partyService.validateMandate(PartyService.DEFAULT_TENANT_ID, mandate);

    assertEquals(
        6,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the mandate");

    // Test invalid properties
    mandate.getProperties().clear();

    mandate.addProperty(new MandateProperty("invalid_property_name", "Invalid Property Value"));

    mandate.addProperty(new MandateProperty("test_string_property", "Invalid String Value!"));

    constraintViolations = partyService.validateMandate(PartyService.DEFAULT_TENANT_ID, mandate);

    assertEquals(
        2,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the mandate");
  }

  /** Test the invalid next of kin verification functionality. */
  @Test
  public void invalidNextOfKinTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addNextOfKin(
        new NextOfKin(
            "invalid_next_of_kin_type",
            null,
            "Invalid Given Name$",
            "Invalid Surname$",
            "Invalid Home Number",
            "Invalid Work Number",
            "Invalid Mobile Number",
            "Invalid Email Address",
            "Invalid Address Line 1$",
            "Invalid Address Line 2$",
            "Invalid Address Line 3$",
            "Invalid Address Line 4$",
            "Invalid Address Suburb$",
            "Invalid Address City$",
            "Invalid Address Region$",
            "XX",
            "Invalid Address Postal Code$"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        18,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the person with an invalid next of kin");
  }

  /** Test the invalid organization attribute test. */
  @Test
  public void invalidOrganizationAttributeTest() throws Exception {
    Organization organization = getTestBasicOrganizationDetails();

    organization.addAttribute(new Attribute("given_name", "Given Name"));
    organization.addAttribute(new Attribute("invalid_attribute", "Invalid Attribute"));

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        2,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the invalid organization external reference test. */
  @Test
  public void invalidOrganizationExternalReferenceTest() throws Exception {
    Organization organization = getTestBasicOrganizationDetails();

    organization.addExternalReference(
        new ExternalReference(
            "invalid_external_reference_type", "invalid_external_reference_value"));

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the invalid organization identification. */
  @Test
  public void invalidOrganizationIdentificationTest() throws Exception {
    Organization organization = getTestBasicOrganizationDetails();

    organization.setIdentificationType("invalid_identification_type");
    organization.setIdentificationCountryOfIssue("XX");

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        2,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");

    organization = getTestBasicOrganizationDetails();

    organization.setIdentificationType("za_company_registration_certificate");
    organization.setIdentificationNumber("XXX");
    organization.setIdentificationCountryOfIssue("ZA");

    constraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the invalid organization industry allocation test. */
  @Test
  public void invalidOrganizationIndustryAllocationTest() throws Exception {
    Organization organization = getTestBasicOrganizationDetails();

    organization.addIndustryAllocation(
        new IndustryAllocation("invalid_system_code", "invalid_industry_code"));

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the invalid organization segment allocation test. */
  @Test
  public void invalidOrganizationSegmentAllocationTest() throws Exception {
    Organization organization = getTestBasicOrganizationDetails();

    organization.addSegmentAllocation(new SegmentAllocation("invalid_segment_allocation"));

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the invalid party types for association functionality. */
  @Test
  public void invalidPartyTypesForAssociationTest() throws Exception {
    Organization firstOrganization = getTestBasicOrganizationDetails();

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, firstOrganization);

    Organization secondOrganization = getTestBasicOrganizationDetails();

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, secondOrganization);

    Association association =
        new Association(
            PartyService.DEFAULT_TENANT_ID,
            "parent_child",
            firstOrganization.getId(),
            secondOrganization.getId(),
            LocalDate.now());

    Set<ConstraintViolation<Association>> constraintViolations =
        partyService.validateAssociation(PartyService.DEFAULT_TENANT_ID, association);

    assertEquals(
        2,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the association");

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, firstOrganization.getId());

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, secondOrganization.getId());
  }

  /** Test the invalid person attribute functionality. */
  @Test
  public void invalidPersonAttributeTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addAttribute(new Attribute("given_name", "Given Name"));
    person.addAttribute(new Attribute("invalid_attribute", "Invalid Attribute"));
    person.addAttribute(new Attribute("weight", 80, MeasurementUnit.CUSTOMARY_FOOT));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        4,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person consent functionality. */
  @Test
  public void invalidPersonConsentTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addConsent(new Consent("invalid_consent"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person external reference test. */
  @Test
  public void invalidPersonExternalReferenceTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addExternalReference(
        new ExternalReference(
            "invalid_external_reference_type", "invalid_external_reference_value"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person identification. */
  @Test
  public void invalidPersonIdentificationTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.setIdentificationType("invalid_identification_type");
    person.setIdentificationCountryOfIssue("XX");

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        2,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");

    person = getTestBasicPersonDetails();

    person.setIdentificationType("za_id");
    person.setIdentificationNumber("XXX");
    person.setIdentificationCountryOfIssue("ZA");

    constraintViolations = partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person segment allocation test. */
  @Test
  public void invalidPersonSegmentAllocationTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addSegmentAllocation(new SegmentAllocation("invalid_segment_allocation"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person skill functionality. */
  @Test
  public void invalidPersonSkillTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addSkill(new Skill("invalid_skill", SkillProficiencyLevel.EXPERT));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person source of funds functionality. */
  @Test
  public void invalidPersonSourceOfFundsTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addSourceOfFunds(new SourceOfFunds("invalid_source_of_funds"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person source of wealth functionality. */
  @Test
  public void invalidPersonSourceOfWealthTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addSourceOfWealth(new SourceOfWealth("invalid_source_of_wealth"));

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid person time zone functionality. */
  @Test
  public void invalidPersonTimeZoneTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.setTimeZone("invalid_time_zone");

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");
  }

  /** Test the invalid physical address purpose for party type verification functionality. */
  @Test
  public void invalidPhysicalAddressPurposeForPartyTypeTest() throws Exception {
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

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid physical address role for the party");
  }

  /** Test the invalid physical address type verification functionality. */
  @Test
  public void invalidPhysicalAddressTypeRoleAndPurposeTest() throws Exception {
    PhysicalAddress invalidAddress =
        new PhysicalAddress(
            "invalid physical address type",
            "invalid physical address role",
            "invalid physical address purpose");

    Person person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        3,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid physical address");
  }

  /** Test the invalid preference functionality. */
  @Test
  public void invalidPreferenceTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addPreference(new Preference("test_preference", "invalid_test_preference_value"));

    Set<ConstraintViolation<Person>> personConstraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        1,
        personConstraintViolations.size(),
        "The correct number of constraint violations was not found for the person");

    Organization organization = getTestBasicOrganizationDetails();

    organization.addPreference(new Preference("test_preference", "invalid_test_preference_value"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        1,
        organizationConstraintViolations.size(),
        "The correct number of constraint violations was not found for the organization");
  }

  /** Test the invalid site address verification functionality. */
  @Test
  public void invalidSiteAddressTest() throws Exception {
    // Validate an empty invalid address
    // Required: Site Block, Site Number, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.SITE, PhysicalAddressRole.RESIDENTIAL);

    Person person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

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
    invalidAddress.setLine4("Line 4");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    constraintViolations = partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        16,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid site address");
  }

  /** Test the invalid street address verification functionality. */
  @Test
  public void invalidStreetAddressTest() throws Exception {
    // Validate an empty invalid address
    // Required: Street Name, City, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.RESIDENTIAL);

    Person person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

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
    invalidAddress.setLine4("Line 4");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    constraintViolations = partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        18,
        constraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid street address");
  }

  /** Test the invalid unstructured address verification functionality. */
  @Test
  public void invalidUnstructuredAddressTest() throws Exception {
    // Validate an empty invalid address
    // Required: Line 1, Country Code, Postal Code
    PhysicalAddress invalidAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressRole.RESIDENTIAL);

    Person person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

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
    invalidAddress.setLine4("Line 4");
    invalidAddress.setPostalCode("Postal Code");
    invalidAddress.setRegion("Region");
    invalidAddress.setSiteBlock("Site Block");
    invalidAddress.setSiteNumber("Site Number");
    invalidAddress.setStreetName("Street Name");
    invalidAddress.setStreetNumber("Street Number");
    invalidAddress.setSuburb("Suburb");

    person = getTestBasicPersonDetails();

    person.addPhysicalAddress(invalidAddress);

    constraintViolations = partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        17,
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

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    assertTrue(retrievedPerson.hasLanguageProficiencyWithLanguage("EN"));

    compareLanguageProficiencies(
        person.getLanguageProficiencyWithLanguage("EN").get(),
        retrievedPerson.getLanguageProficiencyWithLanguage("EN").get());

    person.removeLanguageProficiencyWithLanguage("EN");

    assertFalse(person.hasLanguageProficiencyWithLanguage("EN"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setLanguageProficiencies(
        List.of(
            new LanguageProficiency(
                "EN",
                LanguageProficiencyLevel.PROFICIENT,
                LanguageProficiencyLevel.ADVANCED,
                LanguageProficiencyLevel.INTERMEDIATE,
                LanguageProficiencyLevel.ELEMENTARY)));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());
  }

  /** Test the lock functionality. */
  @Test
  public void lockTest() throws Exception {
    // Person locks
    Person person = getTestBasicPersonDetails();

    person.addLock(new Lock("suspected_fraud"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareLocks(
        person.getLockWithType("suspected_fraud").get(),
        retrievedPerson.getLockWithType("suspected_fraud").get());

    assertTrue(retrievedPerson.hasLockWithType("suspected_fraud"));

    person.removeLockWithType("suspected_fraud");

    assertFalse(person.hasLockWithType("suspected_fraud"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setLocks(List.of(new Lock("suspected_fraud", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization locks
    Organization organization = getTestBasicOrganizationDetails();

    organization.addLock(new Lock("suspected_fraud"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareLocks(
        organization.getLockWithType("suspected_fraud").get(),
        retrievedOrganization.getLockWithType("suspected_fraud").get());

    assertTrue(retrievedOrganization.hasLockWithType("suspected_fraud"));

    organization.removeLockWithType("suspected_fraud");

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setLocks(List.of(new Lock("suspected_fraud", LocalDate.of(2016, 5, 1))));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the mandate functionality. */
  @Test
  public void mandateTest() throws Exception {
    Person firstPerson = getTestCompletePersonDetails(true);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, firstPerson);

    Person secondPerson = getTestCompletePersonDetails(true);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, secondPerson);

    Person thirdPerson = getTestCompletePersonDetails(false);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, thirdPerson);

    Mandate mandate =
        new Mandate(
            PartyService.DEFAULT_TENANT_ID,
            "test_mandate_type",
            RequiredMandataries.ALL,
            LocalDate.of(2016, 7, 16),
            LocalDate.now());

    mandate.addLink(new MandateLink("party_id", thirdPerson.getId().toString()));

    mandate.addMandatary(new Mandatary(firstPerson.getId(), "test_mandatary_type"));
    mandate.addMandatary(new Mandatary(secondPerson.getId(), "test_mandatary_type"));

    mandate.addProperty(new MandateProperty("test_boolean_property", true));
    mandate.addProperty(new MandateProperty("test_date_property", LocalDate.now()));
    mandate.addProperty(new MandateProperty("test_decimal_property", new BigDecimal("82.6")));
    mandate.addProperty(new MandateProperty("test_double_property", 12345.6789));
    mandate.addProperty(new MandateProperty("test_integer_property", Integer.valueOf(123456789)));
    mandate.addProperty(new MandateProperty("test_string_property", "String Value"));

    assertEquals(
        "String Value", mandate.getPropertyWithType("test_string_property").get().getStringValue());
    assertTrue(mandate.hasPropertyWithType("test_string_property"));

    partyService.createMandate(PartyService.DEFAULT_TENANT_ID, mandate);

    MandatesForParty mandatesForParty =
        partyService.getMandatesForParty(
            PartyService.DEFAULT_TENANT_ID,
            firstPerson.getId(),
            MandateSortBy.TYPE,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1, mandatesForParty.getMandates().size(), "The incorrect number of mandates was retrieved");

    compareMandates(mandate, mandatesForParty.getMandates().getFirst());

    Mandate retrievedMandate =
        partyService.getMandate(PartyService.DEFAULT_TENANT_ID, mandate.getId());

    assertEquals(
        6,
        retrievedMandate.getProperties().size(),
        "The incorrect number of mandate properties was retrieved");

    compareMandates(mandate, retrievedMandate);

    mandate.removePropertyWithType("test_string_property");
    assertFalse(mandate.hasPropertyWithType("test_string_property"));

    mandate.removePropertyWithType("test_date_property");
    assertFalse(mandate.hasPropertyWithType("test_date_property"));

    partyService.updateMandate(PartyService.DEFAULT_TENANT_ID, mandate);

    retrievedMandate = partyService.getMandate(PartyService.DEFAULT_TENANT_ID, mandate.getId());

    assertEquals(
        4,
        retrievedMandate.getProperties().size(),
        "The incorrect number of mandate properties was retrieved");

    partyService.deleteMandate(PartyService.DEFAULT_TENANT_ID, mandate.getId());

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, firstPerson.getId());

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, secondPerson.getId());

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, thirdPerson.getId());
  }

  /** Test the next of kin functionality. */
  @Test
  public void nextOfKinTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    NextOfKin nextOfKin = new NextOfKin();
    nextOfKin.setId(UuidCreator.getTimeOrderedEpoch());
    nextOfKin.setType("father");
    nextOfKin.setName("Joe Bloggs");
    nextOfKin.setHomeNumber("+27 11 555 1234");
    nextOfKin.setWorkNumber("+27 11 555 5678");
    nextOfKin.setMobileNumber("+27832763107");
    nextOfKin.setEmailAddress("joe@zyx.com");
    nextOfKin.setAddressLine1("3 Happy Place");
    nextOfKin.setAddressLine2("17 Market Street");
    nextOfKin.setAddressLine3("Line 3");
    nextOfKin.setAddressLine4("Line 4");
    nextOfKin.setAddressSuburb("Fairland");
    nextOfKin.setAddressCity("Johannesburg");
    nextOfKin.setAddressRegion("ZA-GP");
    nextOfKin.setAddressCountry("ZA");
    nextOfKin.setAddressPostalCode("2170");

    person.addNextOfKin(nextOfKin);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    assertTrue(retrievedPerson.getNextOfKinWithId(nextOfKin.getId()).isPresent());

    assertTrue(retrievedPerson.getNextOfKinWithType("father").isPresent());

    compareNextOfKin(
        person.getNextOfKinWithId(nextOfKin.getId()).get(),
        retrievedPerson.getNextOfKinWithId(nextOfKin.getId()).get());

    person.removeNextOfKinWithId(nextOfKin.getId());

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setNextOfKin(List.of(new NextOfKin("brother", "Fred Bloggs")));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());
  }

  /** Test the organization functionality. */
  @Test
  public void organizationTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    assertTrue(organization.hasIdentificationWithType("za_company_registration_certificate"));

    organization.setCountryOfTaxResidence("ZA");
    organization.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "9123456789"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organizations filteredOrganizations =
        partyService.getOrganizations(
            PartyService.DEFAULT_TENANT_ID,
            "",
            OrganizationSortBy.NAME,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1,
        filteredOrganizations.getOrganizations().size(),
        "The correct number of filtered organizations was not retrieved");

    compareOrganizations(organization, filteredOrganizations.getOrganizations().getFirst());

    Snapshots snapshots =
        partyService.getSnapshots(
            PartyService.DEFAULT_TENANT_ID,
            EntityType.ORGANIZATION,
            organization.getId(),
            null,
            null,
            SortDirection.ASCENDING,
            0,
            100);

    Organization serializedOrganization =
        objectMapper.readValue(snapshots.getSnapshots().getFirst().getData(), Organization.class);

    compareOrganizations(organization, serializedOrganization);

    organization.setName(organization.getName() + " Updated");

    organization.setCountriesOfTaxResidence(List.of("GB", "ZA"));

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
            List.of(PhysicalAddressPurpose.CORRESPONDENCE));
    mainAddress.setStreetNumber("1");
    mainAddress.setStreetName("Galaxy Place");
    mainAddress.setSuburb("Sandhurst");
    mainAddress.setCity("Sandton");
    mainAddress.setRegion("ZA-GP");
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

    organization.addRole(new Role("supplier"));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    filteredOrganizations =
        partyService.getOrganizations(
            PartyService.DEFAULT_TENANT_ID,
            "",
            OrganizationSortBy.NAME,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1,
        filteredOrganizations.getOrganizations().size(),
        "The correct number of filtered organizations was not retrieved");

    compareOrganizations(organization, filteredOrganizations.getOrganizations().getFirst());

    Parties filteredParties =
        partyService.getParties(
            PartyService.DEFAULT_TENANT_ID, "", PartySortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredParties.getParties().size(),
        "The correct number of filtered parties was not retrieved");

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the party inheritance functionality. */
  @Test
  public void partyInheritanceTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Person person = getTestCompletePersonDetails(false);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Parties filteredParties =
        partyService.getParties(
            PartyService.DEFAULT_TENANT_ID, "", PartySortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        2,
        filteredParties.getParties().size(),
        "The correct number of filtered parties was not retrieved");

    partyService.deleteParty(PartyService.DEFAULT_TENANT_ID, person.getId());

    partyService.deleteParty(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the pattern validation functionality. */
  @Test
  public void patternValidationTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addIdentification(
        new Identification(
            "passport",
            "ZA",
            LocalDate.of(2016, 10, 7),
            LocalDate.of(2025, 9, 1),
            "A1234567890! "));

    person.addIdentification(
        new Identification("za_id", "ZA", LocalDate.of(2018, 7, 16), "X89 04085800089!"));

    person.addResidencePermit(
        new ResidencePermit(
            "za_general_work_visa", "ZA", LocalDate.of(2011, 4, 2), "BA018723633711"));

    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "0123456789!"));

    Set<ConstraintViolation<Person>> personConstraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        4,
        personConstraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");

    Organization organization = getTestBasicOrganizationDetails();

    organization.addIdentification(
        new Identification(
            "za_company_registration_certificate", "ZA", LocalDate.of(2006, 4, 2), "X123456!"));

    organization.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "9123456789!"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        2,
        organizationConstraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the person functionality. */
  @Test
  public void personTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.setCountryOfCitizenship("ZA");

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Persons filteredPersons =
        partyService.getPersons(
            PartyService.DEFAULT_TENANT_ID,
            "",
            PersonSortBy.NAME,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered parties was not retrieved");

    comparePersons(person, filteredPersons.getPersons().getFirst());

    assertEquals(
        person.getTenantId(),
        partyService.getTenantIdForParty(person.getId()).get(),
        "The tenant ID for the person is incorrect");

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    person = getTestCompletePersonDetails(true);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    filteredPersons =
        partyService.getPersons(
            PartyService.DEFAULT_TENANT_ID,
            "",
            PersonSortBy.NAME,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered persons was not retrieved");

    comparePersons(person, filteredPersons.getPersons().getFirst());

    Snapshots snapshots =
        partyService.getSnapshots(
            PartyService.DEFAULT_TENANT_ID,
            EntityType.PERSON,
            person.getId(),
            null,
            null,
            SortDirection.ASCENDING,
            0,
            100);

    Person serializedPerson =
        objectMapper.readValue(snapshots.getSnapshots().getFirst().getData(), Person.class);

    comparePersons(person, serializedPerson);

    person.setCountriesOfCitizenship(List.of("ZA", "GB"));
    person.setCountryOfBirth("GB");
    person.setCountryOfResidence("ZA");
    person.setCountryOfTaxResidence("GB");
    person.setDateOfBirth(LocalDate.of(1985, 5, 1));
    person.setDateOfDeath(LocalDate.of(2200, 1, 1));
    person.setEmploymentStatus("employed");
    person.setEmploymentType("full_time");
    person.setGender("female");
    person.setGivenName(person.getGivenName() + " Updated");
    person.setIdentificationNumber("A01524783");
    person.setIdentificationType("passport");
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

    person.setCountriesOfTaxResidence(List.of("GB", "ZA"));

    person.removeAttributeWithType("weight");

    assertFalse(person.hasAttributeWithType("weight"));

    person.addAttribute(new Attribute("height", 180, MeasurementUnit.METRIC_CENTIMETER));

    Attribute attribute = new Attribute("complex_attribute");
    attribute.setBooleanValue(true);
    attribute.setDateValue(LocalDate.now());
    attribute.setDecimalValue("123.456");
    attribute.setDoubleValue(123.456);
    attribute.setIntegerValue(777);
    attribute.setStringValue("String Value");

    person.addAttribute(
        new Attribute("height", new BigDecimal("180"), MeasurementUnit.METRIC_CENTIMETER));

    person.removeContactMechanismWithRole(ContactMechanismRole.MAIN_FAX_NUMBER);

    assertFalse(person.hasContactMechanismWithRole(ContactMechanismRole.MAIN_FAX_NUMBER));

    person
        .getContactMechanismWithRole(ContactMechanismRole.PERSONAL_EMAIL_ADDRESS)
        .get()
        .setValue("test.updated@test.com");

    person.addContactMechanism(
        new ContactMechanism(
            ContactMechanismType.PHONE_NUMBER,
            ContactMechanismRole.HOME_PHONE_NUMBER,
            "0115551234"));

    person.addIdentification(
        new Identification(
            "passport", "ZA", LocalDate.of(2016, 10, 7), LocalDate.of(2025, 9, 1), "A01524783"));

    assertTrue(person.hasIdentificationWithType("passport"));

    person.removeIdentificationWithType("za_id");

    assertFalse(person.hasIdentificationWithType("za_id"));

    person.removePreferenceWithType("correspondence_language");

    assertFalse(person.hasPreferenceWithType("correspondence_language"));

    person.addPreference(new Preference("time_to_contact", "anytime"));

    person.addSkill(new Skill("marketing", SkillProficiencyLevel.EXPERT));

    assertTrue(person.hasSkillWithType("marketing"));

    person.removeSkillWithType("marketing");

    assertFalse(person.hasSkillWithType("marketing"));

    person.addTaxNumber(new TaxNumber("uk_tax_number", "GB"));

    person.removeTaxNumberWithType("za_income_tax_number");

    assertFalse(person.hasTaxNumberWithType("za_income_tax_number"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    filteredPersons =
        partyService.getPersons(
            PartyService.DEFAULT_TENANT_ID,
            "",
            PersonSortBy.NAME,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered persons was not retrieved");

    comparePersons(person, filteredPersons.getPersons().getFirst());

    filteredPersons =
        partyService.getPersons(
            PartyService.DEFAULT_TENANT_ID,
            "Updated",
            PersonSortBy.NAME,
            SortDirection.ASCENDING,
            0,
            100);

    assertEquals(
        1,
        filteredPersons.getPersons().size(),
        "The correct number of filtered persons was not retrieved");

    comparePersons(person, filteredPersons.getPersons().getFirst());

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    Parties filteredParties =
        partyService.getParties(
            PartyService.DEFAULT_TENANT_ID, "", PartySortBy.NAME, SortDirection.ASCENDING, 0, 100);

    assertEquals(
        1,
        filteredParties.getParties().size(),
        "The correct number of filtered parties was not retrieved");

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());
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
            List.of(
                new String[] {
                  PhysicalAddressPurpose.BILLING,
                  PhysicalAddressPurpose.CORRESPONDENCE,
                  PhysicalAddressPurpose.DELIVERY
                }));
    residentialAddress.setStreetNumber("13");
    residentialAddress.setStreetName("Kraalbessie Avenue");
    residentialAddress.setSuburb("Weltevreden Park");
    residentialAddress.setCity("Johannesburg");
    residentialAddress.setRegion("ZA-GP");
    residentialAddress.setCountry("ZA");
    residentialAddress.setPostalCode("1709");

    person.addPhysicalAddress(residentialAddress);

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    comparePhysicalAddresses(
        person.getPhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL).get(),
        retrievedPerson.getPhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL).get());

    assertTrue(retrievedPerson.hasPhysicalAddressWithType(PhysicalAddressType.STREET));

    assertTrue(retrievedPerson.hasPhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL));

    person.removePhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL);

    assertFalse(person.hasPhysicalAddressWithRole(PhysicalAddressRole.RESIDENTIAL));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setPhysicalAddresses(List.of(residentialAddress));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization physical addresses
    Organization organization = getTestBasicOrganizationDetails();

    PhysicalAddress mainAddress =
        new PhysicalAddress(
            PhysicalAddressType.STREET,
            PhysicalAddressRole.MAIN,
            List.of(PhysicalAddressPurpose.CORRESPONDENCE));
    mainAddress.setStreetNumber("1");
    mainAddress.setStreetName("Galaxy Place");
    mainAddress.setSuburb("Sandhurst");
    mainAddress.setCity("Sandton");
    mainAddress.setRegion("ZA-GP");
    mainAddress.setCountry("ZA");
    mainAddress.setPostalCode("2194");

    organization.addPhysicalAddress(mainAddress);

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    comparePhysicalAddresses(
        organization.getPhysicalAddressWithRole(PhysicalAddressRole.MAIN).get(),
        retrievedOrganization.getPhysicalAddressWithRole(PhysicalAddressRole.MAIN).get());

    assertTrue(retrievedOrganization.hasPhysicalAddressWithType(PhysicalAddressType.STREET));

    assertTrue(retrievedOrganization.hasPhysicalAddressWithRole(PhysicalAddressRole.MAIN));

    organization.removePhysicalAddressWithRole(PhysicalAddressRole.MAIN);

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setPhysicalAddresses(List.of(mainAddress));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
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
    farmAddress.setRegion("ZA-FS");
    farmAddress.setCountry("ZA");
    farmAddress.setPostalCode("9986");
    person.addPhysicalAddress(farmAddress);

    PhysicalAddress internationalAddress =
        new PhysicalAddress(PhysicalAddressType.INTERNATIONAL, PhysicalAddressRole.RESIDENTIAL);
    internationalAddress.setLine1("Address Line 1");
    internationalAddress.setLine2("Address Line 2");
    internationalAddress.setLine3("Address Line 3");
    internationalAddress.setLine4("Address Line 4");
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
    siteAddress.setRegion("ZA-GP");
    siteAddress.setCountry("ZA");
    siteAddress.setPostalCode("0152");
    person.addPhysicalAddress(siteAddress);

    PhysicalAddress streetAddress =
        new PhysicalAddress(PhysicalAddressType.STREET, PhysicalAddressRole.WORK);
    streetAddress.setStreetNumber("1");
    streetAddress.setStreetName("Galaxy Place");
    streetAddress.setSuburb("Sandhurst");
    streetAddress.setCity("Sandton");
    streetAddress.setRegion("ZA-GP");
    streetAddress.setCountry("ZA");
    streetAddress.setPostalCode("2194");
    person.addPhysicalAddress(streetAddress);

    PhysicalAddress unstructuredAddress =
        new PhysicalAddress(PhysicalAddressType.UNSTRUCTURED, PhysicalAddressRole.WORK);
    unstructuredAddress.setLine1("Address Line 1");
    unstructuredAddress.setLine2("Address Line 2");
    unstructuredAddress.setLine3("Address Line 3");
    unstructuredAddress.setLine4("Address Line 4");
    // unstructuredAddress.setCity("Johannesburg");
    unstructuredAddress.setCountry("ZA");
    unstructuredAddress.setPostalCode("2194");
    person.addPhysicalAddress(unstructuredAddress);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        0,
        constraintViolations.size(),
        "The correct number of constraint violations was not found");

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deleteParty(PartyService.DEFAULT_TENANT_ID, person.getId());
  }

  /** Test the preference functionality. */
  @Test
  public void preferenceTest() throws Exception {
    // Person preferences
    Person person = getTestBasicPersonDetails();

    person.addPreference(new Preference("test_preference", "test_preference_value"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    comparePreferences(
        person.getPreferenceWithType("test_preference").get(),
        retrievedPerson.getPreferenceWithType("test_preference").get());

    assertTrue(retrievedPerson.hasPreferenceWithType("test_preference"));

    person.removePreferenceWithType("test_preference");

    assertFalse(person.hasPreferenceWithType("test_preference"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setPreferences(List.of(new Preference("test_preference", "test_preference_value")));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization preferences
    Organization organization = getTestBasicOrganizationDetails();

    organization.addPreference(new Preference("test_preference", "test_preference_value"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    comparePreferences(
        organization.getPreferenceWithType("test_preference").get(),
        retrievedOrganization.getPreferenceWithType("test_preference").get());

    assertTrue(retrievedOrganization.hasPreferenceWithType("test_preference"));

    organization.removePreferenceWithType("test_preference");

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setPreferences(
        List.of(new Preference("test_preference", "test_preference_value")));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the role functionality. */
  @Test
  public void roleTest() throws Exception {
    // Person roles
    Person person = getTestBasicPersonDetails();

    person.addRole(new Role("employee"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareRoles(
        person.getRoleWithType("employee").get(),
        retrievedPerson.getRoleWithType("employee").get());

    assertTrue(retrievedPerson.hasRoleWithType("employee"));

    person.removeRoleWithType("employee");

    assertFalse(person.hasRoleWithType("employee"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setRoles(List.of(new Role("employee", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization roles
    Organization organization = getTestBasicOrganizationDetails();

    organization.addRole(new Role("employer"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareRoles(
        organization.getRoleWithType("employer").get(),
        retrievedOrganization.getRoleWithType("employer").get());

    assertTrue(retrievedOrganization.hasRoleWithType("employer"));

    organization.removeRoleWithType("employer");

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setRoles(List.of(new Role("employer", LocalDate.of(2016, 5, 1))));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the role type attribute type constraint functionality. */
  @Test
  public void roleTypeAttributeTypeConstraintTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addRole(new Role("test_person_role"));

    Set<ConstraintViolation<Person>> personConstraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    assertEquals(
        55,
        personConstraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid person");

    Organization organization = getTestBasicOrganizationDetails();

    organization.removeIdentificationWithType("za_company_registration_certificate");

    organization.addRole(new Role("test_organization_role"));

    Set<ConstraintViolation<Organization>> organizationConstraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    assertEquals(
        14,
        organizationConstraintViolations.size(),
        "The correct number of constraint violations was not found for the invalid organization");
  }

  /** Test the segment allocation functionality. */
  @Test
  public void segmentAllocationTest() throws Exception {
    // Person segments
    Person person = getTestBasicPersonDetails();

    person.addSegmentAllocation(new SegmentAllocation("test_person_segment"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    assertTrue(retrievedPerson.getSegmentAllocationWithSegment("test_person_segment").isPresent());

    compareSegmentAllocations(
        person.getSegmentAllocationWithSegment("test_person_segment").get(),
        retrievedPerson.getSegmentAllocationWithSegment("test_person_segment").get());

    person.removeSegmentAllocationWithSegment("test_person_segment");

    assertFalse(person.hasSegmentAllocationWithSegment("test_person_segment"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setSegmentAllocations(
        List.of(
            new SegmentAllocation(
                "test_person_segment", LocalDate.of(2012, 5, 17), LocalDate.of(2019, 9, 23))));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization segments
    Organization organization = getTestBasicOrganizationDetails();

    organization.addSegmentAllocation(new SegmentAllocation("test_organization_segment"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    assertTrue(
        retrievedOrganization
            .getSegmentAllocationWithSegment("test_organization_segment")
            .isPresent());

    compareSegmentAllocations(
        organization.getSegmentAllocationWithSegment("test_organization_segment").get(),
        retrievedOrganization.getSegmentAllocationWithSegment("test_organization_segment").get());

    organization.removeSegmentAllocationWithSegment("test_organization_segment");

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setSegmentAllocations(
        List.of(
            new SegmentAllocation(
                "test_organization_segment",
                LocalDate.of(2012, 5, 17),
                LocalDate.of(2019, 9, 23))));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the sourceOfFunds functionality. */
  @Test
  public void sourceOfFundsTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addSourceOfFunds(new SourceOfFunds("salary_wages"));
    person.addSourceOfFunds(new SourceOfFunds("other", "Ill-gotten gains"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareSourcesOfFunds(
        person.getSourceOfFundsWithType("salary_wages").get(),
        retrievedPerson.getSourceOfFundsWithType("salary_wages").get());

    assertTrue(retrievedPerson.hasSourceOfFundsWithType("salary_wages"));

    person.removeSourceOfFundsWithType("salary_wages");

    assertFalse(person.hasSourceOfFundsWithType("salary_wages"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setSourcesOfFunds(List.of(new SourceOfFunds("salary_wages", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());
  }

  /** Test the sourceOfWealth functionality. */
  @Test
  public void sourceOfWealthTest() throws Exception {
    Person person = getTestBasicPersonDetails();

    person.addSourceOfWealth(new SourceOfWealth("savings"));
    person.addSourceOfWealth(new SourceOfWealth("other", "Ill-gotten gains"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareSourcesOfWealth(
        person.getSourceOfWealthWithType("savings").get(),
        retrievedPerson.getSourceOfWealthWithType("savings").get());

    assertTrue(retrievedPerson.hasSourceOfWealthWithType("savings"));

    person.removeSourceOfWealthWithType("savings");

    assertFalse(person.hasSourceOfWealthWithType("savings"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setSourcesOfWealth(List.of(new SourceOfWealth("savings", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());
  }

  /** Test the status functionality. */
  @Test
  public void statusTest() throws Exception {
    // Person statuses
    Person person = getTestBasicPersonDetails();

    person.addStatus(new Status("fraud_investigation"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareStatuses(
        person.getStatusWithType("fraud_investigation").get(),
        retrievedPerson.getStatusWithType("fraud_investigation").get());

    assertTrue(retrievedPerson.hasStatusWithType("fraud_investigation"));

    person.removeStatusWithType("fraud_investigation");

    assertFalse(person.hasStatusWithType("fraud_investigation"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setStatuses(List.of(new Status("fraud_investigation", LocalDate.of(2015, 10, 1))));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization statuses
    Organization organization = getTestBasicOrganizationDetails();

    organization.addStatus(new Status("fraud_investigation"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareStatuses(
        organization.getStatusWithType("fraud_investigation").get(),
        retrievedOrganization.getStatusWithType("fraud_investigation").get());

    assertTrue(retrievedOrganization.hasStatusWithType("fraud_investigation"));

    organization.removeStatusWithType("fraud_investigation");

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setStatuses(List.of(new Status("fraud_investigation", LocalDate.of(2016, 5, 1))));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the tax number functionality. */
  @Test
  public void taxNumberTest() throws Exception {
    // Person tax numbers
    Person person = getTestBasicPersonDetails();

    person.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "0123456789"));

    partyService.createPerson(PartyService.DEFAULT_TENANT_ID, person);

    Person retrievedPerson =
        partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    compareTaxNumbers(
        person.getTaxNumberWithType("za_income_tax_number").get(),
        retrievedPerson.getTaxNumberWithType("za_income_tax_number").get());

    assertTrue(retrievedPerson.hasTaxNumberWithType("za_income_tax_number"));

    person.removeTaxNumberWithType("za_income_tax_number");

    assertFalse(person.hasTaxNumberWithType("za_income_tax_number"));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    person.setTaxNumbers(List.of(new TaxNumber("za_income_tax_number", "ZA", "0987654321")));

    partyService.updatePerson(PartyService.DEFAULT_TENANT_ID, person);

    retrievedPerson = partyService.getPerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    comparePersons(person, retrievedPerson);

    partyService.deletePerson(PartyService.DEFAULT_TENANT_ID, person.getId());

    // Organization tax numbers
    Organization organization = getTestBasicOrganizationDetails();

    organization.addTaxNumber(new TaxNumber("za_income_tax_number", "ZA", "9123456789"));

    partyService.createOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    Organization retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    compareTaxNumbers(
        organization.getTaxNumberWithType("za_income_tax_number").get(),
        retrievedOrganization.getTaxNumberWithType("za_income_tax_number").get());

    assertTrue(retrievedOrganization.hasTaxNumberWithType("za_income_tax_number"));

    organization.removeTaxNumberWithType("za_income_tax_number");

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    organization.setTaxNumbers(List.of(new TaxNumber("za_income_tax_number", "ZA", "9987654321")));

    partyService.updateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    retrievedOrganization =
        partyService.getOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());

    compareOrganizations(organization, retrievedOrganization);

    partyService.deleteOrganization(PartyService.DEFAULT_TENANT_ID, organization.getId());
  }

  /** Test the organization validation functionality. */
  @Test
  public void validateOrganizationTest() throws Exception {
    Organization organization = getTestOrganizationDetails();

    Set<ConstraintViolation<Organization>> constraintViolations =
        partyService.validateOrganization(PartyService.DEFAULT_TENANT_ID, organization);

    if (!constraintViolations.isEmpty()) {
      fail("Failed to successfully validate the organization");
    }
  }

  /** Test the party validation functionality. */
  @Test
  public void validatePartyTest() throws Exception {
    Party party = getTestPartyDetails();

    Set<ConstraintViolation<Party>> constraintViolations =
        partyService.validateParty(PartyService.DEFAULT_TENANT_ID, party);

    if (!constraintViolations.isEmpty()) {
      fail("Failed to successfully validate the party");
    }
  }

  /** Test the person validation functionality. */
  @Test
  public void validatePersonTest() throws Exception {
    Person person = getTestCompletePersonDetails(true);

    Set<ConstraintViolation<Person>> constraintViolations =
        partyService.validatePerson(PartyService.DEFAULT_TENANT_ID, person);

    if (!constraintViolations.isEmpty()) {
      fail("Failed to successfully validate the person");
    }
  }

  private void compareAssociationProperties(
      AssociationProperty associationProperty1, AssociationProperty associationProperty2) {
    assertEquals(
        associationProperty1.getBooleanValue(),
        associationProperty2.getBooleanValue(),
        "The boolean value values for the association properties do not match");
    assertEquals(
        associationProperty1.getDateValue(),
        associationProperty2.getDateValue(),
        "The date value values for the association properties do not match");
    if (associationProperty1.getDecimalValue() != null) {
      assertEquals(
          0,
          associationProperty1.getDecimalValue().compareTo(associationProperty2.getDecimalValue()),
          "The decimal value values for the association properties do not match");
    } else {
      assertEquals(
          associationProperty1.getDecimalValue(),
          associationProperty2.getDecimalValue(),
          "The decimal value values for the association properties do not match");
    }
    assertEquals(
        associationProperty1.getDoubleValue(),
        associationProperty2.getDoubleValue(),
        "The double value values for the association properties do not match");
    assertEquals(
        associationProperty1.getIntegerValue(),
        associationProperty2.getIntegerValue(),
        "The integer value values for the association properties do not match");
    assertEquals(
        associationProperty1.getStringValue(),
        associationProperty2.getStringValue(),
        "The string value values for the association properties do not match");
    assertEquals(
        associationProperty1.getType(),
        associationProperty2.getType(),
        "The type values for the association properties do not match");
  }

  private void compareAssociations(Association association1, Association association2) {
    assertEquals(
        association1.getEffectiveFrom(),
        association2.getEffectiveFrom(),
        "The effective from values for the associations do not match");
    assertEquals(
        association1.getEffectiveTo(),
        association2.getEffectiveTo(),
        "The effective to values for the associations do not match");
    assertEquals(
        association1.getFirstPartyId(),
        association2.getFirstPartyId(),
        "The first party ID values for the associations do not match");
    assertEquals(
        association1.getId(),
        association2.getId(),
        "The ID values for the associations do not match");
    assertEquals(
        association1.getSecondPartyId(),
        association2.getSecondPartyId(),
        "The second party ID values for the associations do not match");
    assertEquals(
        association1.getTenantId(),
        association2.getTenantId(),
        "The tenant ID values for the associations do not match");
    assertEquals(
        association1.getType(),
        association2.getType(),
        "The type values for the associations do not match");

    assertEquals(
        association1.getProperties().size(),
        association2.getProperties().size(),
        "The number of properties for the associations do not match");

    for (AssociationProperty association1Property : association1.getProperties()) {
      boolean foundAssociationProperty = false;

      for (AssociationProperty association2Property : association2.getProperties()) {
        if (association1Property.getType().equals(association2Property.getType())) {

          compareAssociationProperties(association1Property, association2Property);

          foundAssociationProperty = true;
        }
      }

      if (!foundAssociationProperty) {
        fail("Failed to find the association property (" + association1Property.getType() + ")");
      }
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
    if (attribute1.getDecimalValue() != null) {
      assertEquals(
          0,
          attribute1.getDecimalValue().compareTo(attribute2.getDecimalValue()),
          "The decimal value values for the attributes do not match");
    } else {
      assertEquals(
          attribute1.getDecimalValue(),
          attribute2.getDecimalValue(),
          "The decimal value values for the attributes do not match");
    }
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
        employment1.getEmployerAddressLine4(),
        employment2.getEmployerAddressLine4(),
        "The employer address line 4 values for the employments do not match");
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
        "The employer email address values for the employments do not match");
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

  private void compareExternalReferences(
      ExternalReference externalReference1, ExternalReference externalReference2) {
    assertEquals(
        externalReference1.getParty(),
        externalReference2.getParty(),
        "The party values for the external references do not match");
    assertEquals(
        externalReference1.getType(),
        externalReference2.getType(),
        "The type values for the external references do not match");
    assertEquals(
        externalReference1.getValue(),
        externalReference2.getValue(),
        "The value values for the external references do not match");
  }

  private void compareIdentifications(
      Identification identification1, Identification identification2) {
    assertEquals(
        identification1.getCountryOfIssue(),
        identification2.getCountryOfIssue(),
        "The country of issue values for the identifications do not match");
    assertEquals(
        identification1.getExpiryDate(),
        identification2.getExpiryDate(),
        "The expiry date values for the identifications do not match");
    assertEquals(
        identification1.getIssueDate(),
        identification2.getIssueDate(),
        "The issue date values for the identifications do not match");
    assertEquals(
        identification1.getProvidedDate(),
        identification2.getProvidedDate(),
        "The date provided values for the identifications do not match");
    assertEquals(
        identification1.getId(),
        identification2.getId(),
        "The ID values for the identifications do not match");
    assertEquals(
        identification1.getNumber(),
        identification2.getNumber(),
        "The number values for the identifications do not match");
    assertEquals(
        identification1.getParty(),
        identification2.getParty(),
        "The party values for the identifications do not match");
    assertEquals(
        identification1.getType(),
        identification2.getType(),
        "The type values for the identifications do not match");
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

  private void compareMandateProperties(
      MandateProperty mandateProperty1, MandateProperty mandateProperty2) {
    assertEquals(
        mandateProperty1.getBooleanValue(),
        mandateProperty2.getBooleanValue(),
        "The boolean value values for the mandate properties do not match");
    assertEquals(
        mandateProperty1.getDateValue(),
        mandateProperty2.getDateValue(),
        "The date value values for the mandate properties do not match");
    if (mandateProperty1.getDecimalValue() != null) {
      assertEquals(
          0,
          mandateProperty1.getDecimalValue().compareTo(mandateProperty2.getDecimalValue()),
          "The decimal value values for the mandate properties do not match");
    } else {
      assertEquals(
          mandateProperty1.getDecimalValue(),
          mandateProperty2.getDecimalValue(),
          "The decimal value values for the mandate properties do not match");
    }
    assertEquals(
        mandateProperty1.getDoubleValue(),
        mandateProperty2.getDoubleValue(),
        "The double value values for the mandate properties do not match");
    assertEquals(
        mandateProperty1.getIntegerValue(),
        mandateProperty2.getIntegerValue(),
        "The integer value values for the mandate properties do not match");
    assertEquals(
        mandateProperty1.getStringValue(),
        mandateProperty2.getStringValue(),
        "The string value values for the mandate properties do not match");
    assertEquals(
        mandateProperty1.getType(),
        mandateProperty2.getType(),
        "The type values for the mandate properties do not match");
  }

  private void compareMandates(Mandate mandate1, Mandate mandate2) {
    assertEquals(
        mandate1.getEffectiveFrom(),
        mandate2.getEffectiveFrom(),
        "The effective from values for the mandates do not match");
    assertEquals(
        mandate1.getEffectiveTo(),
        mandate2.getEffectiveTo(),
        "The effective to values for the mandates do not match");
    assertEquals(mandate1.getId(), mandate2.getId(), "The ID values for the mandates do not match");
    assertEquals(
        mandate1.getRequiredMandataries(),
        mandate2.getRequiredMandataries(),
        "The required mandataries values for the mandates do not match");
    assertEquals(
        mandate1.getTenantId(),
        mandate2.getTenantId(),
        "The tenant ID values for the mandates do not match");
    assertEquals(
        mandate1.getType(), mandate2.getType(), "The type values for the mandates do not match");

    assertEquals(
        mandate1.getLinks().size(),
        mandate2.getLinks().size(),
        "The number of links for the mandates do not match");

    for (MandateLink link1 : mandate1.getLinks()) {
      boolean foundLink = false;

      for (MandateLink link2 : mandate2.getLinks()) {
        if ((link1.getType().equals(link2.getType()))
            && (link1.getTarget().equals(link2.getTarget()))) {

          foundLink = true;
          break;
        }
      }

      if (!foundLink) {
        fail(
            "Failed to find the mandate link with type ("
                + link1.getType()
                + ") and target ("
                + link1.getTarget()
                + ")");
      }
    }

    assertEquals(
        mandate1.getMandataries().size(),
        mandate2.getMandataries().size(),
        "The number of mandataries for the mandates do not match");

    for (Mandatary mandatary1 : mandate1.getMandataries()) {
      boolean foundMandatary = false;

      for (Mandatary mandatary2 : mandate2.getMandataries()) {
        if ((mandatary1.getPartyId().equals(mandatary2.getPartyId()))
            && (mandatary1.getRole().equals(mandatary2.getRole()))) {

          foundMandatary = true;
          break;
        }
      }

      if (!foundMandatary) {
        fail("Failed to find the mandatary (" + mandatary1.getPartyId() + ")");
      }
    }

    assertEquals(
        mandate1.getProperties().size(),
        mandate2.getProperties().size(),
        "The number of properties for the mandates do not match");

    for (MandateProperty mandate1Property : mandate1.getProperties()) {
      boolean foundMandateProperty = false;

      for (MandateProperty mandate2Property : mandate2.getProperties()) {
        if (mandate1Property.getType().equals(mandate2Property.getType())) {

          compareMandateProperties(mandate1Property, mandate2Property);

          foundMandateProperty = true;
        }
      }

      if (!foundMandateProperty) {
        fail("Failed to find the mandate property (" + mandate1Property.getType() + ")");
      }
    }
  }

  private void compareNextOfKin(NextOfKin nextOfKin1, NextOfKin nextOfKin2) {
    assertEquals(
        nextOfKin1.getId(), nextOfKin2.getId(), "The id values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getType(),
        nextOfKin2.getType(),
        "The type values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getName(),
        nextOfKin2.getName(),
        "The name values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getGivenName(),
        nextOfKin2.getGivenName(),
        "The given name values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getSurname(),
        nextOfKin2.getSurname(),
        "The surname values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getHomeNumber(),
        nextOfKin2.getHomeNumber(),
        "The home phone number values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getWorkNumber(),
        nextOfKin2.getWorkNumber(),
        "The work phone number values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getMobileNumber(),
        nextOfKin2.getMobileNumber(),
        "The mobile number values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getEmailAddress(),
        nextOfKin2.getEmailAddress(),
        "The email address values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressLine1(),
        nextOfKin2.getAddressLine1(),
        "The address line 1 values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressLine2(),
        nextOfKin2.getAddressLine2(),
        "The address line 2 values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressLine3(),
        nextOfKin2.getAddressLine3(),
        "The address line 3 values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressLine4(),
        nextOfKin2.getAddressLine4(),
        "The address line 4 values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressSuburb(),
        nextOfKin2.getAddressSuburb(),
        "The address suburb values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressCity(),
        nextOfKin2.getAddressCity(),
        "The address city values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressRegion(),
        nextOfKin2.getAddressRegion(),
        "The address region values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressCountry(),
        nextOfKin2.getAddressCountry(),
        "The address country values for the next of kin do not match");
    assertEquals(
        nextOfKin1.getAddressPostalCode(),
        nextOfKin2.getAddressPostalCode(),
        "The address postal code values for the next of kin do not match");
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
        organization1.getIdentificationNumber(),
        organization2.getIdentificationNumber(),
        "The identification number values for the organizations do not match");
    assertEquals(
        organization1.getIdentificationType(),
        organization2.getIdentificationType(),
        "The identification type values for the organizations do not match");
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
        organization1.getExternalReferences().size(),
        organization2.getExternalReferences().size(),
        "The number of external references for the organizations do not match");

    for (ExternalReference organization1ExternalReference : organization1.getExternalReferences()) {
      boolean foundExternalReference = false;

      for (ExternalReference organization2ExternalReference :
          organization2.getExternalReferences()) {
        if (organization1ExternalReference
            .getType()
            .equals(organization2ExternalReference.getType())) {

          compareExternalReferences(organization1ExternalReference, organization2ExternalReference);

          foundExternalReference = true;
        }
      }

      if (!foundExternalReference) {
        fail(
            "Failed to find the external reference with type ("
                + organization1ExternalReference.getType()
                + ")");
      }
    }

    assertEquals(
        organization1.getIdentifications().size(),
        organization2.getIdentifications().size(),
        "The number of identifications for the organizations do not match");

    for (Identification organization1Identification : organization1.getIdentifications()) {
      boolean foundIdentification = false;

      for (Identification organization2Identification : organization2.getIdentifications()) {
        if (organization1Identification.getId().equals(organization2Identification.getId())) {

          compareIdentifications(organization1Identification, organization2Identification);

          foundIdentification = true;
        }
      }

      if (!foundIdentification) {
        fail(
            "Failed to find the identification ("
                + organization1Identification.getType()
                + ")("
                + organization1Identification.getCountryOfIssue()
                + ")("
                + organization1Identification.getIssueDate()
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
        organization1.getSegmentAllocations().size(),
        organization2.getSegmentAllocations().size(),
        "The number of segment allocations for the organizations do not match");

    for (SegmentAllocation organization1SegmentAllocation : organization1.getSegmentAllocations()) {
      boolean foundSegmentAllocation = false;

      for (SegmentAllocation organization2SegmentAllocation :
          organization2.getSegmentAllocations()) {
        if (organization1SegmentAllocation
            .getSegment()
            .equals(organization2SegmentAllocation.getSegment())) {

          compareSegmentAllocations(organization1SegmentAllocation, organization2SegmentAllocation);

          foundSegmentAllocation = true;
        }
      }

      if (!foundSegmentAllocation) {
        fail(
            "Failed to find the segment allocation ("
                + organization1SegmentAllocation.getSegment()
                + ")");
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
        if (organization1TaxNumber.getType().equals(organization2TaxNumber.getType())) {
          compareTaxNumbers(organization1TaxNumber, organization2TaxNumber);

          foundTaxNumber = true;
        }
      }

      if (!foundTaxNumber) {
        fail("Failed to find the tax number (" + organization1TaxNumber.getType() + ")");
      }
    }
  }

  @SuppressWarnings("unused")
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
        person1.getIdentificationNumber(),
        person2.getIdentificationNumber(),
        "The identification number values for the persons do not match");
    assertEquals(
        person1.getIdentificationType(),
        person2.getIdentificationType(),
        "The identification type values for the persons do not match");
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
        "The number of employment records for the persons do not match");

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
        person1.getExternalReferences().size(),
        person2.getExternalReferences().size(),
        "The number of external references for the persons do not match");

    for (ExternalReference person1ExternalReference : person1.getExternalReferences()) {
      boolean foundExternalReference = false;

      for (ExternalReference person2ExternalReference : person2.getExternalReferences()) {
        if (person1ExternalReference.getType().equals(person2ExternalReference.getType())) {

          compareExternalReferences(person1ExternalReference, person2ExternalReference);

          foundExternalReference = true;
        }
      }

      if (!foundExternalReference) {
        fail(
            "Failed to find the external reference with type ("
                + person1ExternalReference.getType()
                + ")");
      }
    }

    assertEquals(
        person1.getIdentifications().size(),
        person2.getIdentifications().size(),
        "The number of identifications for the persons do not match");

    for (Identification person1Identification : person1.getIdentifications()) {
      boolean foundIdentification = false;

      for (Identification person2Identification : person2.getIdentifications()) {
        if (person1Identification.getId().equals(person2Identification.getId())) {

          compareIdentifications(person1Identification, person2Identification);

          foundIdentification = true;
        }
      }

      if (!foundIdentification) {
        fail("Failed to find the identification (" + person1Identification.getId() + ")");
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
        person1.getNextOfKin().size(),
        person2.getNextOfKin().size(),
        "The number of next of kin for the persons do not match");

    for (NextOfKin person1NextOfKin : person1.getNextOfKin()) {
      boolean foundEmployment = false;

      for (NextOfKin person2NextOfKin : person2.getNextOfKin()) {
        if (person1NextOfKin.getId().equals(person2NextOfKin.getId())) {

          compareNextOfKin(person1NextOfKin, person2NextOfKin);

          foundEmployment = true;
        }
      }

      if (!foundEmployment) {
        fail("Failed to find the next of kin (" + person1NextOfKin.getId() + ")");
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
        person1.getSegmentAllocations().size(),
        person2.getSegmentAllocations().size(),
        "The number of segment allocations for the persons do not match");

    for (SegmentAllocation person1SegmentAllocation : person1.getSegmentAllocations()) {
      boolean foundSegmentAllocation = false;

      for (SegmentAllocation person2SegmentAllocation : person2.getSegmentAllocations()) {
        if (person1SegmentAllocation.getSegment().equals(person2SegmentAllocation.getSegment())) {

          compareSegmentAllocations(person1SegmentAllocation, person2SegmentAllocation);

          foundSegmentAllocation = true;
        }
      }

      if (!foundSegmentAllocation) {
        fail(
            "Failed to find the segment allocation ("
                + person1SegmentAllocation.getSegment()
                + ")");
      }
    }

    assertEquals(
        person1.getSkills().size(),
        person2.getSkills().size(),
        "The number of skills for the persons do not match");

    for (Skill person1Skill : person1.getSkills()) {
      boolean foundSkill = false;

      for (Skill person2Skill : person2.getSkills()) {
        if (Objects.equals(person1Skill.getPerson(), person2Skill.getPerson())
            && Objects.equals(person1Skill.getType(), person2Skill.getType())) {

          compareSkills(person1Skill, person2Skill);

          foundSkill = true;
        }
      }

      if (!foundSkill) {
        fail("Failed to find the skill (" + person1Skill.getType() + ")");
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
        if (person1TaxNumber.getType().equals(person2TaxNumber.getType())) {
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
        physicalAddress1.getLine4(),
        physicalAddress2.getLine4(),
        "The line 4 values for the physical addresses do not match");
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
        residencePermit1.getExpiryDate(),
        residencePermit2.getExpiryDate(),
        "The expiry date values for the residence permits do not match");
    assertEquals(
        residencePermit1.getIssueDate(),
        residencePermit2.getIssueDate(),
        "The issue date values for the residence permits do not match");
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
        residencePermit2.getPerson(),
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

  private void compareSegmentAllocations(
      SegmentAllocation segmentAllocation1, SegmentAllocation segmentAllocation2) {
    assertEquals(
        segmentAllocation1.getEffectiveFrom(),
        segmentAllocation2.getEffectiveFrom(),
        "The effective from values for the segment allocations do not match");
    assertEquals(
        segmentAllocation1.getEffectiveTo(),
        segmentAllocation2.getEffectiveTo(),
        "The effective to values for the segment allocations do not match");
    assertEquals(
        segmentAllocation1.getParty(),
        segmentAllocation2.getParty(),
        "The party values for the segment allocations do not match");
    assertEquals(
        segmentAllocation1.getSegment(),
        segmentAllocation2.getSegment(),
        "The segment values for the segment allocations do not match");
  }

  private void compareSkills(Skill skill1, Skill skill2) {
    assertEquals(
        skill1.getLevel(), skill2.getLevel(), "The level values for the skills do not match");
    assertEquals(skill1.getPerson(), skill2.getPerson(), "The persons for the skills do not match");
    assertEquals(skill1.getType(), skill2.getType(), "The types skills the consents do not match");
  }

  private void compareSourcesOfFunds(SourceOfFunds sourceOfFunds1, SourceOfFunds sourceOfFunds2) {
    assertEquals(
        sourceOfFunds1.getDescription(),
        sourceOfFunds2.getDescription(),
        "The description values for the sources of funds do not match");
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
        sourceOfWealth1.getDescription(),
        sourceOfWealth2.getDescription(),
        "The description values for the sources of wealth do not match");
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

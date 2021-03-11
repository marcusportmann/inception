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

import digital.inception.party.Attribute;
import digital.inception.party.AttributeType;
import digital.inception.party.AttributeTypeCategory;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismRole;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.EmploymentStatus;
import digital.inception.party.EmploymentType;
import digital.inception.party.Gender;
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IdentityDocument;
import digital.inception.party.IdentityDocumentType;
import digital.inception.party.MaritalStatus;
import digital.inception.party.MarriageType;
import digital.inception.party.NextOfKinType;
import digital.inception.party.Occupation;
import digital.inception.party.Organization;
import digital.inception.party.Party;
import digital.inception.party.PartyType;
import digital.inception.party.Person;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressRole;
import digital.inception.party.PhysicalAddressType;
import digital.inception.party.Preference;
import digital.inception.party.PreferenceType;
import digital.inception.party.PreferenceTypeCategory;
import digital.inception.party.Race;
import digital.inception.party.ResidencePermitType;
import digital.inception.party.ResidencyStatus;
import digital.inception.party.ResidentialType;
import digital.inception.party.RolePurpose;
import digital.inception.party.RoleType;
import digital.inception.party.RoleTypeAttributeConstraint;
import digital.inception.party.SourceOfFunds;
import digital.inception.party.TaxNumber;
import digital.inception.party.TaxNumberType;
import digital.inception.party.TimeToContact;
import digital.inception.party.Title;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.util.List;
import java.util.Objects;
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
 * The <b>PartyReferenceServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>PartyReferenceService</b> class.
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
public class PartyReferenceServiceTest {

  /** The Party Reference Service. */
  @Autowired private IPartyReferenceService partyReferenceService;

  /** Test the attribute type category reference functionality. */
  @Test
  public void attributeTypeCategoryTest() throws Exception {
    List<AttributeTypeCategory> retrievedAttributeTypeCategories =
        partyReferenceService.getAttributeTypeCategories();

    assertEquals(
        "The correct number of attribute type categories was not retrieved",
        2,
        retrievedAttributeTypeCategories.size());

    retrievedAttributeTypeCategories = partyReferenceService.getAttributeTypeCategories("en-US");

    assertEquals(
        "The correct number of attribute type categories was not retrieved",
        1,
        retrievedAttributeTypeCategories.size());
  }

  /** Test the attribute type reference functionality. */
  @Test
  public void attributeTypeTest() throws Exception {
    List<AttributeType> retrievedAttributeTypes = partyReferenceService.getAttributeTypes();

    assertEquals(
        "The correct number of attribute types was not retrieved",
        6,
        retrievedAttributeTypes.size());

    retrievedAttributeTypes = partyReferenceService.getAttributeTypes("en-US");

    assertEquals(
        "The correct number of attribute types was not retrieved",
        3,
        retrievedAttributeTypes.size());
  }

  /** Test the contact mechanism role functionality. */
  @Test
  public void contactMechanismRolesTest() throws Exception {
    List<ContactMechanismRole> retrievedContactMechanismRoles =
        partyReferenceService.getContactMechanismRoles();

    retrievedContactMechanismRoles = partyReferenceService.getContactMechanismRoles("en-US");
  }

  /** Test the contact mechanism type functionality. */
  @Test
  public void contactMechanismTypeTest() throws Exception {
    List<ContactMechanismType> retrievedContactMechanismTypes =
        partyReferenceService.getContactMechanismTypes();

    retrievedContactMechanismTypes = partyReferenceService.getContactMechanismTypes("en-US");
  }

  /** Test the employment status reference functionality. */
  @Test
  public void employmentStatusTest() throws Exception {
    List<EmploymentStatus> retrievedEmploymentStatuses =
        partyReferenceService.getEmploymentStatuses();

    assertEquals(
        "The correct number of employment statuses was not retrieved",
        6,
        retrievedEmploymentStatuses.size());

    retrievedEmploymentStatuses = partyReferenceService.getEmploymentStatuses("en-US");

    assertEquals(
        "The correct number of employment statuses was not retrieved",
        3,
        retrievedEmploymentStatuses.size());
  }

  /** Test the employment type reference functionality. */
  @Test
  public void employmentTypeTest() throws Exception {
    List<EmploymentType> retrievedEmploymentTypes = partyReferenceService.getEmploymentTypes();

    assertEquals(
        "The correct number of employment types was not retrieved",
        22,
        retrievedEmploymentTypes.size());

    retrievedEmploymentTypes = partyReferenceService.getEmploymentTypes("en-US");

    assertEquals(
        "The correct number of employment types was not retrieved",
        11,
        retrievedEmploymentTypes.size());
  }

  /** Test the gender reference functionality. */
  @Test
  public void genderTest() throws Exception {
    List<Gender> retrievedGenders = partyReferenceService.getGenders();

    assertEquals("The correct number of genders was not retrieved", 10, retrievedGenders.size());

    retrievedGenders = partyReferenceService.getGenders("en-US");

    assertEquals("The correct number of genders was not retrieved", 5, retrievedGenders.size());
  }

  /** Test the identity document type reference functionality. */
  @Test
  public void identityDocumentTypeTest() throws Exception {
    List<IdentityDocumentType> retrievedIdentityDocumentTypes =
        partyReferenceService.getIdentityDocumentTypes();

    assertEquals(
        "The correct number of identity document types was not retrieved",
        10,
        retrievedIdentityDocumentTypes.size());

    retrievedIdentityDocumentTypes = partyReferenceService.getIdentityDocumentTypes("en-US");

    assertEquals(
        "The correct number of identity document types was not retrieved",
        5,
        retrievedIdentityDocumentTypes.size());
  }

  /** Test the marital status reference functionality. */
  @Test
  public void maritalStatusTest() throws Exception {
    List<MaritalStatus> retrievedMaritalStatuses = partyReferenceService.getMaritalStatuses();

    assertEquals(
        "The correct number of marital statuses was not retrieved",
        12,
        retrievedMaritalStatuses.size());

    retrievedMaritalStatuses = partyReferenceService.getMaritalStatuses("en-US");

    assertEquals(
        "The correct number of marital statuses was not retrieved",
        6,
        retrievedMaritalStatuses.size());
  }

  /** Test the marriage type reference functionality. */
  @Test
  public void marriageTypeTest() throws Exception {
    List<MarriageType> retrievedMarriageTypes = partyReferenceService.getMarriageTypes();

    assertEquals(
        "The correct number of marriage types was not retrieved", 8, retrievedMarriageTypes.size());

    retrievedMarriageTypes = partyReferenceService.getMarriageTypes("en-US");

    assertEquals(
        "The correct number of marriage types was not retrieved", 4, retrievedMarriageTypes.size());
  }

  /** Test the next of kin type reference functionality. */
  @Test
  public void nextOfKinTypeTest() throws Exception {
    List<NextOfKinType> retrievedNextOfKinTypes = partyReferenceService.getNextOfKinTypes();

    assertEquals(
        "The correct number of next of kin types was not retrieved",
        34,
        retrievedNextOfKinTypes.size());

    retrievedNextOfKinTypes = partyReferenceService.getNextOfKinTypes("en-US");

    assertEquals(
        "The correct number of next of kin types was not retrieved",
        17,
        retrievedNextOfKinTypes.size());
  }

  /** Test the occupation reference functionality. */
  @Test
  public void occupationTest() throws Exception {
    List<Occupation> retrievedOccupations = partyReferenceService.getOccupations();

    assertEquals(
        "The correct number of occupations was not retrieved", 58, retrievedOccupations.size());

    retrievedOccupations = partyReferenceService.getOccupations("en-US");

    assertEquals(
        "The correct number of occupations was not retrieved", 29, retrievedOccupations.size());
  }

  /** Test the physical address purpose functionality. */
  @Test
  public void physicalAddressPurposeTest() throws Exception {
    List<PhysicalAddressPurpose> retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes();

    retrievedPhysicalAddressPurposes = partyReferenceService.getPhysicalAddressPurposes("en-US");
  }

  /** Test the physical address role functionality. */
  @Test
  public void physicalAddressRoleTest() throws Exception {
    List<PhysicalAddressRole> retrievedPhysicalAddressRoles =
        partyReferenceService.getPhysicalAddressRoles();

    retrievedPhysicalAddressRoles = partyReferenceService.getPhysicalAddressRoles("en-US");
  }

  /** Test the physical address type functionality. */
  @Test
  public void physicalAddressTypeTest() throws Exception {
    List<PhysicalAddressType> retrievedPhysicalAddressTypes =
        partyReferenceService.getPhysicalAddressTypes();

    retrievedPhysicalAddressTypes = partyReferenceService.getPhysicalAddressTypes("en-US");
  }

  /** Test the preference type category functionality. */
  @Test
  public void preferenceTypeCategoryTest() throws Exception {
    List<PreferenceTypeCategory> retrievedPreferenceTypeCategories =
        partyReferenceService.getPreferenceTypeCategories();

    retrievedPreferenceTypeCategories = partyReferenceService.getPreferenceTypeCategories("en-US");
  }

  /** Test the preference type functionality. */
  @Test
  public void preferenceTypeTest() throws Exception {
    List<PreferenceType> retrievedPreferenceTypes = partyReferenceService.getPreferenceTypes();

    retrievedPreferenceTypes = partyReferenceService.getPreferenceTypes("en-US");
  }

  /** Test the race reference functionality. */
  @Test
  public void raceTest() throws Exception {
    List<Race> retrievedRaces = partyReferenceService.getRaces();

    assertEquals("The correct number of races was not retrieved", 12, retrievedRaces.size());

    retrievedRaces = partyReferenceService.getRaces("en-US");

    assertEquals("The correct number of races was not retrieved", 6, retrievedRaces.size());
  }

  /** Test the residence permit type reference functionality. */
  @Test
  public void residencePermitTypeTest() throws Exception {
    List<ResidencePermitType> retrievedResidencePermitTypes =
        partyReferenceService.getResidencePermitTypes();

    assertEquals(
        "The correct number of residence permit types was not retrieved",
        18,
        retrievedResidencePermitTypes.size());

    retrievedResidencePermitTypes = partyReferenceService.getResidencePermitTypes("en-US");

    assertEquals(
        "The correct number of residence permit types was not retrieved",
        9,
        retrievedResidencePermitTypes.size());
  }

  /** Test the residency status reference functionality. */
  @Test
  public void residencyStatusTest() throws Exception {
    List<ResidencyStatus> retrievedResidencyStatuses = partyReferenceService.getResidencyStatuses();

    assertEquals(
        "The correct number of residency statuses was not retrieved",
        10,
        retrievedResidencyStatuses.size());

    retrievedResidencyStatuses = partyReferenceService.getResidencyStatuses("en-US");

    assertEquals(
        "The correct number of residency statuses was not retrieved",
        5,
        retrievedResidencyStatuses.size());
  }

  /** Test the residential type reference functionality. */
  @Test
  public void residentialTypeTest() throws Exception {
    List<ResidentialType> retrievedResidentialTypes = partyReferenceService.getResidentialTypes();

    assertEquals(
        "The correct number of residential types was not retrieved",
        14,
        retrievedResidentialTypes.size());

    retrievedResidentialTypes = partyReferenceService.getResidentialTypes("en-US");

    assertEquals(
        "The correct number of residential types was not retrieved",
        7,
        retrievedResidentialTypes.size());
  }

  /** Test the role purpose functionality. */
  @Test
  public void rolePurposeTest() throws Exception {
    List<RolePurpose> retrievedRolePurposes = partyReferenceService.getRolePurposes();

    retrievedRolePurposes = partyReferenceService.getRolePurposes("en-US");
  }

  /** Test the role type attribute constraint functionality. */
  @Test
  public void roleTypeAttributeConstraintTest() throws Exception {
    List<RoleTypeAttributeConstraint> retrievedRoleTypeAttributeConstraints =
        partyReferenceService.getRoleTypeAttributeConstraints();

    assertEquals(
        "The correct number of role type attribute constraints was not retrieved",
        46,
        retrievedRoleTypeAttributeConstraints.size());

    retrievedRoleTypeAttributeConstraints =
        partyReferenceService.getRoleTypeAttributeConstraints("test_person_role");

    assertEquals(
        "The correct number of role type attribute constraints was not retrieved",
        33,
        retrievedRoleTypeAttributeConstraints.size());

    retrievedRoleTypeAttributeConstraints.stream()
        .filter(
            roleTypeAttributeConstraint ->
                !roleTypeAttributeConstraint.getRoleType().equals("test_person_role"))
        .findFirst()
        .ifPresent(
            roleTypeAttributeConstraint ->
                fail(
                    "Found invalid role type attribute constraint with role type ("
                        + roleTypeAttributeConstraint.getAttributeType()
                        + ")"));
  }

  /** Test the role type functionality. */
  @Test
  public void roleTypeTest() throws Exception {
    List<RoleType> retrievedRoleTypes = partyReferenceService.getRoleTypes();

    retrievedRoleTypes = partyReferenceService.getRoleTypes("en-US");
  }

  /** Test the sources of funds reference functionality. */
  @Test
  public void sourceOfFundsTest() throws Exception {
    List<SourceOfFunds> retrievedSourceOfFunds = partyReferenceService.getSourcesOfFunds();

    assertEquals(
        "The correct number of sources of funds was not retrieved",
        38,
        retrievedSourceOfFunds.size());

    retrievedSourceOfFunds = partyReferenceService.getSourcesOfFunds("en-US");

    assertEquals(
        "The correct number of sources of funds was not retrieved",
        19,
        retrievedSourceOfFunds.size());
  }

  /** Test the tax number type reference functionality. */
  @Test
  public void taxNumberTypeTest() throws Exception {
    List<TaxNumberType> retrievedTaxNumberTypes = partyReferenceService.getTaxNumberTypes();

    assertEquals(
        "The correct number of tax number types was not retrieved",
        14,
        retrievedTaxNumberTypes.size());

    retrievedTaxNumberTypes = partyReferenceService.getTaxNumberTypes("en-US");

    assertEquals(
        "The correct number of tax number types was not retrieved",
        7,
        retrievedTaxNumberTypes.size());
  }

  /** Test the time to contact functionality. */
  @Test
  public void timeToContactTest() throws Exception {
    List<TimeToContact> retrievedTimesToContact = partyReferenceService.getTimesToContact();

    assertEquals(
        "The correct number of times to contact was not retrieved",
        10,
        retrievedTimesToContact.size());

    retrievedTimesToContact = partyReferenceService.getTimesToContact("en-US");

    assertEquals(
        "The correct number of times to contact was not retrieved",
        5,
        retrievedTimesToContact.size());
  }

  /** Test the title reference functionality. */
  @Test
  public void titleTest() throws Exception {
    List<Title> retrievedTitles = partyReferenceService.getTitles();

    assertEquals("The correct number of titles was not retrieved", 24, retrievedTitles.size());

    retrievedTitles = partyReferenceService.getTitles("en-US");

    assertEquals("The correct number of titles was not retrieved", 12, retrievedTitles.size());
  }

  /** Test the reference data validity check functionality. */
  @Test
  public void validityTest() throws Exception {
    partyReferenceService.isValidContactMechanismRole(
        PartyType.PERSON.code(), ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number");
    partyReferenceService.isValidContactMechanismType(ContactMechanismType.MOBILE_NUMBER);
    partyReferenceService.isValidEmploymentStatus("employed");
    partyReferenceService.isValidEmploymentType("employed", "full_time");
    partyReferenceService.isValidGender("female");
    partyReferenceService.isValidIdentityDocumentType(PartyType.PERSON.code(), "passport");
    partyReferenceService.isValidMaritalStatus("married");
    partyReferenceService.isValidMarriageType("married", "anc_with_accrual");
    // referenceService.isValidMinorType("minor");
    partyReferenceService.isValidNextOfKinType("mother");
    partyReferenceService.isValidOccupation("executive");
    partyReferenceService.isValidAttributeType(PartyType.PERSON.code(), "height");
    partyReferenceService.isValidAttributeTypeCategory("anthropometric_measurements");
    partyReferenceService.isValidRolePurpose("test");
    partyReferenceService.isValidRoleType(PartyType.PERSON.code(), "test_person_role");
    partyReferenceService.isValidPhysicalAddressPurpose(PartyType.PERSON.code(), "billing");
    partyReferenceService.isValidPhysicalAddressRole(
        PartyType.PERSON.code(), PhysicalAddressRole.RESIDENTIAL);
    partyReferenceService.isValidPhysicalAddressType("complex");
    partyReferenceService.isValidPreferenceType(
        PartyType.ORGANIZATION.code(), "correspondence_language");
    partyReferenceService.isValidPreferenceTypeCategory("correspondence");
    partyReferenceService.isValidRace("white");
    partyReferenceService.isValidResidencePermitType("za_general_work_visa");
    partyReferenceService.isValidResidencyStatus("permanent_resident");
    partyReferenceService.isValidResidentialType("owner");
    partyReferenceService.isValidSourceOfFunds("salary");
    partyReferenceService.isValidTaxNumberType("person", "za_income_tax_number");
    partyReferenceService.isValidTimeToContact("anytime");
    partyReferenceService.isValidTitle("mrs");
  }

  private void compareAttributes(Attribute attribute1, Attribute attribute2) {
    assertEquals(
        "The type values for the two attributes do not match",
        attribute1.getType(),
        attribute2.getType());
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
        "The number of contact mechanisms for the two persons do not match",
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

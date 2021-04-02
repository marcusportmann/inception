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

import digital.inception.party.AttributeType;
import digital.inception.party.AttributeTypeCategory;
import digital.inception.party.ConsentType;
import digital.inception.party.ContactMechanismPurpose;
import digital.inception.party.ContactMechanismRole;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.EmploymentStatus;
import digital.inception.party.EmploymentType;
import digital.inception.party.FieldOfStudy;
import digital.inception.party.Gender;
import digital.inception.party.IPartyReferenceService;
import digital.inception.party.IdentityDocumentType;
import digital.inception.party.LockType;
import digital.inception.party.LockTypeCategory;
import digital.inception.party.MaritalStatus;
import digital.inception.party.MarriageType;
import digital.inception.party.NextOfKinType;
import digital.inception.party.Occupation;
import digital.inception.party.PartyType;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressRole;
import digital.inception.party.PhysicalAddressType;
import digital.inception.party.PreferenceType;
import digital.inception.party.PreferenceTypeCategory;
import digital.inception.party.QualificationType;
import digital.inception.party.Race;
import digital.inception.party.ResidencePermitType;
import digital.inception.party.ResidencyStatus;
import digital.inception.party.ResidentialType;
import digital.inception.party.RolePurpose;
import digital.inception.party.RoleType;
import digital.inception.party.RoleTypeAttributeTypeConstraint;
import digital.inception.party.RoleTypePreferenceTypeConstraint;
import digital.inception.party.SourceOfFundsType;
import digital.inception.party.StatusType;
import digital.inception.party.StatusTypeCategory;
import digital.inception.party.TaxNumberType;
import digital.inception.party.TimeToContact;
import digital.inception.party.Title;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.util.List;
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

  /** Test the consent type reference functionality. */
  @Test
  public void consentTypeTest() throws Exception {
    List<ConsentType> retrievedConsentTypes = partyReferenceService.getConsentTypes();

    assertEquals(
        "The correct number of consent types was not retrieved", 2, retrievedConsentTypes.size());

    retrievedConsentTypes = partyReferenceService.getConsentTypes("en-US");

    assertEquals(
        "The correct number of consent types was not retrieved", 1, retrievedConsentTypes.size());
  }

  /** Test the contact mechanism purpose functionality. */
  @Test
  public void contactMechanismPurposesTest() throws Exception {
    List<ContactMechanismPurpose> retrievedContactMechanismPurposes =
        partyReferenceService.getContactMechanismPurposes();

    assertEquals(
        "The correct number of contact mechanism purposes was not retrieved",
        6,
        retrievedContactMechanismPurposes.size());

    retrievedContactMechanismPurposes = partyReferenceService.getContactMechanismPurposes("en-US");

    assertEquals(
        "The correct number of contact mechanism purposes was not retrieved",
        3,
        retrievedContactMechanismPurposes.size());
  }

  /** Test the contact mechanism role functionality. */
  @Test
  public void contactMechanismRolesTest() throws Exception {
    List<ContactMechanismRole> retrievedContactMechanismRoles =
        partyReferenceService.getContactMechanismRoles();

    assertEquals(
        "The correct number of contact mechanism roles was not retrieved",
        50,
        retrievedContactMechanismRoles.size());

    retrievedContactMechanismRoles = partyReferenceService.getContactMechanismRoles("en-US");

    assertEquals(
        "The correct number of contact mechanism roles was not retrieved",
        25,
        retrievedContactMechanismRoles.size());
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

  /** Test the lock type category reference functionality. */
  @Test
  public void lockTypeCategoryTest() throws Exception {
    List<LockTypeCategory> retrievedLockTypeCategories =
        partyReferenceService.getLockTypeCategories();

    assertEquals(
        "The correct number of lock type categories was not retrieved",
        6,
        retrievedLockTypeCategories.size());

    retrievedLockTypeCategories = partyReferenceService.getLockTypeCategories("en-US");

    assertEquals(
        "The correct number of lock type categories was not retrieved",
        3,
        retrievedLockTypeCategories.size());
  }

  /** Test the lock type reference functionality. */
  @Test
  public void lockTypeTest() throws Exception {
    List<LockType> retrievedLockTypes = partyReferenceService.getLockTypes();

    assertEquals(
        "The correct number of lock types was not retrieved", 6, retrievedLockTypes.size());

    retrievedLockTypes = partyReferenceService.getLockTypes("en-US");

    assertEquals(
        "The correct number of lock types was not retrieved", 3, retrievedLockTypes.size());
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


  /** Test the qualification type reference functionality. */
  @Test
  public void qualificationTypeTest() throws Exception {
    List<QualificationType> retrievedQualificationTypes = partyReferenceService.getQualificationTypes();

    assertEquals(
        "The correct number of qualification types was not retrieved", 28, retrievedQualificationTypes.size());

    retrievedQualificationTypes = partyReferenceService.getQualificationTypes("en-US");

    assertEquals(
        "The correct number of qualification types was not retrieved", 14, retrievedQualificationTypes.size());
  }


  /** Test the field of study reference functionality. */
  @Test
  public void fieldOfStudyTest() throws Exception {
    List<FieldOfStudy> retrievedFieldsOfStudy = partyReferenceService.getFieldsOfStudy();

    assertEquals(
        "The correct number of fields of study was not retrieved", 382, retrievedFieldsOfStudy.size());

    retrievedFieldsOfStudy = partyReferenceService.getFieldsOfStudy("en-US");

    assertEquals(
        "The correct number of fields of study was not retrieved", 191, retrievedFieldsOfStudy.size());
  }



  /** Test the physical address purpose functionality. */
  @Test
  public void physicalAddressPurposeTest() throws Exception {
    List<PhysicalAddressPurpose> retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes();

    assertEquals(
        "The correct number of physical address purposes was not retrieved",
        6,
        retrievedPhysicalAddressPurposes.size());

    retrievedPhysicalAddressPurposes = partyReferenceService.getPhysicalAddressPurposes("en-US");

    assertEquals(
        "The correct number of physical address purposes was not retrieved",
        3,
        retrievedPhysicalAddressPurposes.size());
  }

  /** Test the physical address role functionality. */
  @Test
  public void physicalAddressRoleTest() throws Exception {
    List<PhysicalAddressRole> retrievedPhysicalAddressRoles =
        partyReferenceService.getPhysicalAddressRoles();

    assertEquals(
        "The correct number of physical address roles was not retrieved",
        22,
        retrievedPhysicalAddressRoles.size());

    retrievedPhysicalAddressRoles = partyReferenceService.getPhysicalAddressRoles("en-US");

    assertEquals(
        "The correct number of physical address roles was not retrieved",
        11,
        retrievedPhysicalAddressRoles.size());
  }

  /** Test the physical address type functionality. */
  @Test
  public void physicalAddressTypeTest() throws Exception {
    List<PhysicalAddressType> retrievedPhysicalAddressTypes =
        partyReferenceService.getPhysicalAddressTypes();

    assertEquals(
        "The correct number of physical address types was not retrieved",
        16,
        retrievedPhysicalAddressTypes.size());

    retrievedPhysicalAddressTypes = partyReferenceService.getPhysicalAddressTypes("en-US");

    assertEquals(
        "The correct number of physical address types was not retrieved",
        8,
        retrievedPhysicalAddressTypes.size());
  }

  /** Test the preference type category functionality. */
  @Test
  public void preferenceTypeCategoryTest() throws Exception {
    List<PreferenceTypeCategory> retrievedPreferenceTypeCategories =
        partyReferenceService.getPreferenceTypeCategories();

    assertEquals(
        "The correct number of preference type categories was not retrieved",
        4,
        retrievedPreferenceTypeCategories.size());

    retrievedPreferenceTypeCategories = partyReferenceService.getPreferenceTypeCategories("en-US");

    assertEquals(
        "The correct number of preference type categories was not retrieved",
        2,
        retrievedPreferenceTypeCategories.size());
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

  /** Test the role type attribute type constraint functionality. */
  @Test
  public void roleTypeAttributeTypeConstraintTest() throws Exception {
    List<RoleTypeAttributeTypeConstraint> retrievedRoleTypeAttributeTypeConstraints =
        partyReferenceService.getRoleTypeAttributeTypeConstraints();

    assertEquals(
        "The correct number of role type attribute type constraints was not retrieved",
        60,
        retrievedRoleTypeAttributeTypeConstraints.size());

    retrievedRoleTypeAttributeTypeConstraints =
        partyReferenceService.getRoleTypeAttributeTypeConstraints("test_person_role");

    assertEquals(
        "The correct number of role type attribute type constraints was not retrieved",
        47,
        retrievedRoleTypeAttributeTypeConstraints.size());

    retrievedRoleTypeAttributeTypeConstraints.stream()
        .filter(
            roleTypeAttributeTypeConstraint ->
                !roleTypeAttributeTypeConstraint.getRoleType().equals("test_person_role"))
        .findFirst()
        .ifPresent(
            roleTypeAttributeTypeConstraint ->
                fail(
                    "Found invalid role type attribute type constraint with role type ("
                        + roleTypeAttributeTypeConstraint.getRoleType()
                        + ")"));
  }

  /** Test the role type preference type constraint functionality. */
  @Test
  public void roleTypePreferenceTypeConstraintTest() throws Exception {
    List<RoleTypePreferenceTypeConstraint> retrievedRoleTypePreferenceTypeConstraints =
        partyReferenceService.getRoleTypePreferenceTypeConstraints();

    assertEquals(
        "The correct number of role type preference type constraints was not retrieved",
        16,
        retrievedRoleTypePreferenceTypeConstraints.size());

    retrievedRoleTypePreferenceTypeConstraints =
        partyReferenceService.getRoleTypePreferenceTypeConstraints("test_person_role");

    assertEquals(
        "The correct number of role type preference type constraints was not retrieved",
        12,
        retrievedRoleTypePreferenceTypeConstraints.size());

    retrievedRoleTypePreferenceTypeConstraints.stream()
        .filter(
            roleTypePreferenceTypeConstraint ->
                !roleTypePreferenceTypeConstraint.getRoleType().equals("test_person_role"))
        .findFirst()
        .ifPresent(
            roleTypeAttributeTypeConstraint ->
                fail(
                    "Found invalid role type preference type constraint with role type ("
                        + roleTypeAttributeTypeConstraint.getRoleType()
                        + ")"));
  }

  /** Test the role type functionality. */
  @Test
  public void roleTypeTest() throws Exception {
    List<RoleType> retrievedRoleTypes = partyReferenceService.getRoleTypes();

    assertEquals(
        "The correct number of role types was not retrieved", 10, retrievedRoleTypes.size());

    retrievedRoleTypes = partyReferenceService.getRoleTypes("en-US");

    assertEquals(
        "The correct number of role types was not retrieved", 5, retrievedRoleTypes.size());
  }

  /** Test the source of funds types reference functionality. */
  @Test
  public void sourceOfFundsTypeTest() throws Exception {
    List<SourceOfFundsType> retrievedSourceOfFundTypes =
        partyReferenceService.getSourceOfFundsTypes();

    assertEquals(
        "The correct number of source of funds types was not retrieved",
        38,
        retrievedSourceOfFundTypes.size());

    retrievedSourceOfFundTypes = partyReferenceService.getSourceOfFundsTypes("en-US");

    assertEquals(
        "The correct number of source of funds types was not retrieved",
        19,
        retrievedSourceOfFundTypes.size());
  }

  /** Test the status type category functionality. */
  @Test
  public void statusTypeCategoryTest() throws Exception {
    List<StatusTypeCategory> retrievedStatusTypeCategories =
        partyReferenceService.getStatusTypeCategories();

    assertEquals(
        "The correct number of status type categories was not retrieved",
        6,
        retrievedStatusTypeCategories.size());

    retrievedStatusTypeCategories = partyReferenceService.getStatusTypeCategories("en-US");

    assertEquals(
        "The correct number of status type categories was not retrieved",
        3,
        retrievedStatusTypeCategories.size());
  }

  /** Test the status type functionality. */
  @Test
  public void statusTypeTest() throws Exception {
    List<StatusType> retrievedStatusTypes = partyReferenceService.getStatusTypes();

    assertEquals(
        "The correct number of status types was not retrieved", 6, retrievedStatusTypes.size());

    retrievedStatusTypes = partyReferenceService.getStatusTypes("en-US");

    assertEquals(
        "The correct number of status types was not retrieved", 3, retrievedStatusTypes.size());
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
    assertTrue(partyReferenceService.isValidAttributeType(PartyType.PERSON.code(), "height"));
    assertTrue(partyReferenceService.isValidAttributeTypeCategory("anthropometric_measurements"));
    assertTrue(partyReferenceService.isValidConsentType("marketing"));
    assertTrue(
        partyReferenceService.isValidContactMechanismPurpose(
            "person", "email_address", "security"));
    assertTrue(
        partyReferenceService.isValidContactMechanismRole(
            PartyType.PERSON.code(), ContactMechanismType.MOBILE_NUMBER, "personal_mobile_number"));
    assertTrue(
        partyReferenceService.isValidContactMechanismType(ContactMechanismType.MOBILE_NUMBER));
    assertTrue(partyReferenceService.isValidEmploymentStatus("employed"));
    assertTrue(partyReferenceService.isValidEmploymentType("employed", "full_time"));
    assertTrue(partyReferenceService.isValidGender("female"));
    assertTrue(
        partyReferenceService.isValidIdentityDocumentType(PartyType.PERSON.code(), "passport"));
    assertTrue(partyReferenceService.isValidLockType("person", "suspected_fraud"));
    assertTrue(partyReferenceService.isValidLockTypeCategory("fraud"));
    assertTrue(partyReferenceService.isValidMaritalStatus("married"));
    assertTrue(partyReferenceService.isValidMarriageType("married", "anc_with_accrual"));
    // referenceService.isValidMinorType("minor");
    assertTrue(partyReferenceService.isValidNextOfKinType("mother"));
    assertTrue(partyReferenceService.isValidOccupation("executive"));
    assertTrue(
        partyReferenceService.isValidPhysicalAddressPurpose(PartyType.PERSON.code(), "billing"));
    assertTrue(
        partyReferenceService.isValidPhysicalAddressRole(
            PartyType.PERSON.code(), PhysicalAddressRole.RESIDENTIAL));
    assertTrue(partyReferenceService.isValidPhysicalAddressType("complex"));
    assertTrue(
        partyReferenceService.isValidPreferenceType(
            PartyType.ORGANIZATION.code(), "correspondence_language"));
    assertTrue(partyReferenceService.isValidPreferenceTypeCategory("correspondence"));
    assertTrue(partyReferenceService.isValidQualificationType("doctoral_degree"));
    assertTrue(partyReferenceService.isValidRace("white"));
    assertTrue(partyReferenceService.isValidResidencePermitType("za_general_work_visa"));
    assertTrue(partyReferenceService.isValidResidencyStatus("permanent_resident"));
    assertTrue(partyReferenceService.isValidResidentialType("owner"));
    assertTrue(partyReferenceService.isValidRolePurpose("test"));
    assertTrue(partyReferenceService.isValidRoleType(PartyType.PERSON.code(), "test_person_role"));
    assertTrue(partyReferenceService.isValidSourceOfFundsType("salary"));
    assertTrue(partyReferenceService.isValidStatusType("person", "fraud_investigation"));
    assertTrue(partyReferenceService.isValidStatusTypeCategory("fraud"));
    assertTrue(partyReferenceService.isValidTaxNumberType("person", "za_income_tax_number"));
    assertTrue(partyReferenceService.isValidTimeToContact("anytime"));
    assertTrue(partyReferenceService.isValidTitle("mrs"));
  }
}

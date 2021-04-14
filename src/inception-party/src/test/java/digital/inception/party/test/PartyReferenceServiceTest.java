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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import digital.inception.party.PartyReferenceService;
import digital.inception.party.PartyType;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressRole;
import digital.inception.party.PhysicalAddressType;
import digital.inception.party.PreferenceType;
import digital.inception.party.PreferenceTypeCategory;
import digital.inception.party.QualificationType;
import digital.inception.party.Race;
import digital.inception.party.RelationshipType;
import digital.inception.party.ResidencePermitType;
import digital.inception.party.ResidencyStatus;
import digital.inception.party.ResidentialType;
import digital.inception.party.RolePurpose;
import digital.inception.party.RoleType;
import digital.inception.party.RoleTypeAttributeTypeConstraint;
import digital.inception.party.RoleTypePreferenceTypeConstraint;
import digital.inception.party.Segment;
import digital.inception.party.SourceOfFundsType;
import digital.inception.party.StatusType;
import digital.inception.party.StatusTypeCategory;
import digital.inception.party.TaxNumberType;
import digital.inception.party.TimeToContact;
import digital.inception.party.Title;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.util.List;
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
 * The <b>PartyReferenceServiceTest</b> class contains the implementation of the JUnit tests for the
 * <b>PartyReferenceService</b> class.
 *
 * @author Marcus Portmann
 */
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
public class PartyReferenceServiceTest {

  /** The Party Reference Service. */
  @Autowired private IPartyReferenceService partyReferenceService;

  /** Test the attribute type category reference functionality. */
  @Test
  public void attributeTypeCategoryTest() throws Exception {
    List<AttributeTypeCategory> retrievedAttributeTypeCategories =
        partyReferenceService.getAttributeTypeCategories(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedAttributeTypeCategories.size(),
        "The correct number of attribute type categories was not retrieved");
  }

  /** Test the attribute type reference functionality. */
  @Test
  public void attributeTypeTest() throws Exception {
    List<AttributeType> retrievedAttributeTypes =
        partyReferenceService.getAttributeTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedAttributeTypes.size(),
        "The correct number of attribute types was not retrieved");
  }

  /** Test the consent type reference functionality. */
  @Test
  public void consentTypeTest() throws Exception {
    List<ConsentType> retrievedConsentTypes =
        partyReferenceService.getConsentTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1, retrievedConsentTypes.size(), "The correct number of consent types was not retrieved");
  }

  /** Test the contact mechanism purpose functionality. */
  @Test
  public void contactMechanismPurposesTest() throws Exception {
    List<ContactMechanismPurpose> retrievedContactMechanismPurposes =
        partyReferenceService.getContactMechanismPurposes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedContactMechanismPurposes.size(),
        "The correct number of contact mechanism purposes was not retrieved");
  }

  /** Test the contact mechanism role functionality. */
  @Test
  public void contactMechanismRolesTest() throws Exception {
    List<ContactMechanismRole> retrievedContactMechanismRoles =
        partyReferenceService.getContactMechanismRoles(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        25,
        retrievedContactMechanismRoles.size(),
        "The correct number of contact mechanism roles was not retrieved");
  }

  /** Test the contact mechanism type functionality. */
  @Test
  public void contactMechanismTypeTest() throws Exception {
    List<ContactMechanismType> retrievedContactMechanismTypes =
        partyReferenceService.getContactMechanismTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        5,
        retrievedContactMechanismTypes.size(),
        "The correct number of contact mechanism types was not retrieved");
  }

  /** Test the employment status reference functionality. */
  @Test
  public void employmentStatusTest() throws Exception {
    List<EmploymentStatus> retrievedEmploymentStatuses =
        partyReferenceService.getEmploymentStatuses(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedEmploymentStatuses.size(),
        "The correct number of employment statuses was not retrieved");
  }

  /** Test the employment type reference functionality. */
  @Test
  public void employmentTypeTest() throws Exception {
    List<EmploymentType> retrievedEmploymentTypes =
        partyReferenceService.getEmploymentTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedEmploymentTypes.size(),
        "The correct number of employment types was not retrieved");
  }

  /** Test the field of study reference functionality. */
  @Test
  public void fieldOfStudyTest() throws Exception {
    List<FieldOfStudy> retrievedFieldsOfStudy =
        partyReferenceService.getFieldsOfStudy(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        191,
        retrievedFieldsOfStudy.size(),
        "The correct number of fields of study was not retrieved");
  }

  /** Test the gender reference functionality. */
  @Test
  public void genderTest() throws Exception {
    List<Gender> retrievedGenders =
        partyReferenceService.getGenders(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(5, retrievedGenders.size(), "The correct number of genders was not retrieved");
  }

  /** Test the identity document type reference functionality. */
  @Test
  public void identityDocumentTypeTest() throws Exception {
    List<IdentityDocumentType> retrievedIdentityDocumentTypes =
        partyReferenceService.getIdentityDocumentTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        5,
        retrievedIdentityDocumentTypes.size(),
        "The correct number of identity document types was not retrieved");
  }

  /** Test the lock type category reference functionality. */
  @Test
  public void lockTypeCategoryTest() throws Exception {
    List<LockTypeCategory> retrievedLockTypeCategories =
        partyReferenceService.getLockTypeCategories(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedLockTypeCategories.size(),
        "The correct number of lock type categories was not retrieved");
  }

  /** Test the lock type reference functionality. */
  @Test
  public void lockTypeTest() throws Exception {
    List<LockType> retrievedLockTypes =
        partyReferenceService.getLockTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3, retrievedLockTypes.size(), "The correct number of lock types was not retrieved");
  }

  /** Test the marital status reference functionality. */
  @Test
  public void maritalStatusTest() throws Exception {
    List<MaritalStatus> retrievedMaritalStatuses =
        partyReferenceService.getMaritalStatuses(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedMaritalStatuses.size(),
        "The correct number of marital statuses was not retrieved");
  }

  /** Test the marriage type reference functionality. */
  @Test
  public void marriageTypeTest() throws Exception {
    List<MarriageType> retrievedMarriageTypes =
        partyReferenceService.getMarriageTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6, retrievedMarriageTypes.size(), "The correct number of marriage types was not retrieved");
  }

  /** Test the next of kin type reference functionality. */
  @Test
  public void nextOfKinTypeTest() throws Exception {
    List<NextOfKinType> retrievedNextOfKinTypes =
        partyReferenceService.getNextOfKinTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        17,
        retrievedNextOfKinTypes.size(),
        "The correct number of next of kin types was not retrieved");
  }

  /** Test the occupation reference functionality. */
  @Test
  public void occupationTest() throws Exception {
    List<Occupation> retrievedOccupations =
        partyReferenceService.getOccupations(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        29, retrievedOccupations.size(), "The correct number of occupations was not retrieved");
  }

  /** Test the physical address purpose functionality. */
  @Test
  public void physicalAddressPurposeTest() throws Exception {
    List<PhysicalAddressPurpose> retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedPhysicalAddressPurposes.size(),
        "The correct number of physical address purposes was not retrieved");
  }

  /** Test the physical address role functionality. */
  @Test
  public void physicalAddressRoleTest() throws Exception {
    List<PhysicalAddressRole> retrievedPhysicalAddressRoles =
        partyReferenceService.getPhysicalAddressRoles(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedPhysicalAddressRoles.size(),
        "The correct number of physical address roles was not retrieved");
  }

  /** Test the physical address type functionality. */
  @Test
  public void physicalAddressTypeTest() throws Exception {
    List<PhysicalAddressType> retrievedPhysicalAddressTypes =
        partyReferenceService.getPhysicalAddressTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedPhysicalAddressTypes.size(),
        "The correct number of physical address types was not retrieved");
  }

  /** Test the preference type category functionality. */
  @Test
  public void preferenceTypeCategoryTest() throws Exception {
    List<PreferenceTypeCategory> retrievedPreferenceTypeCategories =
        partyReferenceService.getPreferenceTypeCategories(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedPreferenceTypeCategories.size(),
        "The correct number of preference type categories was not retrieved");
  }

  /** Test the preference type functionality. */
  @Test
  public void preferenceTypeTest() throws Exception {
    List<PreferenceType> retrievedPreferenceTypes =
        partyReferenceService.getPreferenceTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedPreferenceTypes.size(),
        "The correct number of preference types was not retrieved");
  }

  /** Test the qualification type reference functionality. */
  @Test
  public void qualificationTypeTest() throws Exception {
    List<QualificationType> retrievedQualificationTypes =
        partyReferenceService.getQualificationTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        14,
        retrievedQualificationTypes.size(),
        "The correct number of qualification types was not retrieved");
  }

  /** Test the race reference functionality. */
  @Test
  public void raceTest() throws Exception {
    List<Race> retrievedRaces =
        partyReferenceService.getRaces(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(6, retrievedRaces.size(), "The correct number of races was not retrieved");
  }

  /** Test the relationship type reference functionality. */
  @Test
  public void relationshipTypeTest() throws Exception {
    List<RelationshipType> retrievedRelationshipTypes =
        partyReferenceService.getRelationshipTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        21,
        retrievedRelationshipTypes.size(),
        "The correct number of relationship typw was not retrieved");
  }

  /** Test the residence permit type reference functionality. */
  @Test
  public void residencePermitTypeTest() throws Exception {
    List<ResidencePermitType> retrievedResidencePermitTypes =
        partyReferenceService.getResidencePermitTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedResidencePermitTypes.size(),
        "The correct number of residence permit types was not retrieved");
  }

  /** Test the residency status reference functionality. */
  @Test
  public void residencyStatusTest() throws Exception {
    List<ResidencyStatus> retrievedResidencyStatuses =
        partyReferenceService.getResidencyStatuses(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        5,
        retrievedResidencyStatuses.size(),
        "The correct number of residency statuses was not retrieved");
  }

  /** Test the residential type reference functionality. */
  @Test
  public void residentialTypeTest() throws Exception {
    List<ResidentialType> retrievedResidentialTypes =
        partyReferenceService.getResidentialTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7,
        retrievedResidentialTypes.size(),
        "The correct number of residential types was not retrieved");
  }

  /** Test the role purpose functionality. */
  @Test
  public void rolePurposeTest() throws Exception {
    List<RolePurpose> retrievedRolePurposes =
        partyReferenceService.getRolePurposes(PartyReferenceService.DEFAULT_LOCALE_ID);

    // TODO: Check results
  }

  /** Test the role type attribute type constraint functionality. */
  @Test
  public void roleTypeAttributeTypeConstraintTest() throws Exception {
    List<RoleTypeAttributeTypeConstraint> retrievedRoleTypeAttributeTypeConstraints =
        partyReferenceService.getRoleTypeAttributeTypeConstraints();

    assertEquals(
        63,
        retrievedRoleTypeAttributeTypeConstraints.size(),
        "The correct number of role type attribute type constraints was not retrieved");

    retrievedRoleTypeAttributeTypeConstraints =
        partyReferenceService.getRoleTypeAttributeTypeConstraints("test_person_role");

    assertEquals(
        50,
        retrievedRoleTypeAttributeTypeConstraints.size(),
        "The correct number of role type attribute type constraints was not retrieved");

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
        16,
        retrievedRoleTypePreferenceTypeConstraints.size(),
        "The correct number of role type preference type constraints was not retrieved");

    retrievedRoleTypePreferenceTypeConstraints =
        partyReferenceService.getRoleTypePreferenceTypeConstraints("test_person_role");

    assertEquals(
        12,
        retrievedRoleTypePreferenceTypeConstraints.size(),
        "The correct number of role type preference type constraints was not retrieved");

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
    List<RoleType> retrievedRoleTypes =
        partyReferenceService.getRoleTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        37, retrievedRoleTypes.size(), "The correct number of role types was not retrieved");
  }

  /** Test the segment reference functionality. */
  @Test
  public void segmentTest() throws Exception {
    List<Segment> retrievedSegments =
        partyReferenceService.getSegments(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(2, retrievedSegments.size(), "The correct number of segments was not retrieved");
  }

  /** Test the source of funds types reference functionality. */
  @Test
  public void sourceOfFundsTypeTest() throws Exception {
    List<SourceOfFundsType> retrievedSourceOfFundTypes =
        partyReferenceService.getSourceOfFundsTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        19,
        retrievedSourceOfFundTypes.size(),
        "The correct number of source of funds types was not retrieved");
  }

  /** Test the status type category functionality. */
  @Test
  public void statusTypeCategoryTest() throws Exception {
    List<StatusTypeCategory> retrievedStatusTypeCategories =
        partyReferenceService.getStatusTypeCategories(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedStatusTypeCategories.size(),
        "The correct number of status type categories was not retrieved");
  }

  /** Test the status type functionality. */
  @Test
  public void statusTypeTest() throws Exception {
    List<StatusType> retrievedStatusTypes =
        partyReferenceService.getStatusTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3, retrievedStatusTypes.size(), "The correct number of status types was not retrieved");
  }

  /** Test the tax number type reference functionality. */
  @Test
  public void taxNumberTypeTest() throws Exception {
    List<TaxNumberType> retrievedTaxNumberTypes =
        partyReferenceService.getTaxNumberTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7,
        retrievedTaxNumberTypes.size(),
        "The correct number of tax number types was not retrieved");
  }

  /** Test the time to contact functionality. */
  @Test
  public void timeToContactTest() throws Exception {
    List<TimeToContact> retrievedTimesToContact =
        partyReferenceService.getTimesToContact(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        5,
        retrievedTimesToContact.size(),
        "The correct number of times to contact was not retrieved");
  }

  /** Test the title reference functionality. */
  @Test
  public void titleTest() throws Exception {
    List<Title> retrievedTitles =
        partyReferenceService.getTitles(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(12, retrievedTitles.size(), "The correct number of titles was not retrieved");
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
    assertTrue(partyReferenceService.isValidSegment("test_person_segment"));
    assertTrue(partyReferenceService.isValidSourceOfFundsType("salary"));
    assertTrue(partyReferenceService.isValidStatusType("person", "fraud_investigation"));
    assertTrue(partyReferenceService.isValidStatusTypeCategory("fraud"));
    assertTrue(partyReferenceService.isValidTaxNumberType("person", "za_income_tax_number"));
    assertTrue(partyReferenceService.isValidTimeToContact("anytime"));
    assertTrue(partyReferenceService.isValidTitle("mrs"));
  }
}

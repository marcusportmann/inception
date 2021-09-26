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

import digital.inception.party.AssociationPropertyType;
import digital.inception.party.AssociationType;
import digital.inception.party.AttributeType;
import digital.inception.party.AttributeTypeCategory;
import digital.inception.party.ConsentType;
import digital.inception.party.ContactMechanismPurpose;
import digital.inception.party.ContactMechanismRole;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.EmploymentStatus;
import digital.inception.party.EmploymentType;
import digital.inception.party.ExternalReferenceType;
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
import digital.inception.party.Segment;
import digital.inception.party.SourceOfFundsType;
import digital.inception.party.SourceOfWealthType;
import digital.inception.party.StatusType;
import digital.inception.party.StatusTypeCategory;
import digital.inception.party.TaxNumberType;
import digital.inception.party.TimeToContact;
import digital.inception.party.Title;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.util.List;
import java.util.UUID;
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

  /** Test the association property type reference functionality. */
  @Test
  public void associationPropertyTypeTest() throws Exception {
    List<AssociationPropertyType> retrievedAssociationPropertyTypes =
        partyReferenceService.getAssociationPropertyTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7,
        retrievedAssociationPropertyTypes.size(),
        "The correct number of association property types was not retrieved");

    retrievedAssociationPropertyTypes =
        partyReferenceService.getAssociationPropertyTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7,
        retrievedAssociationPropertyTypes.size(),
        "The correct number of association property types was not retrieved");

    retrievedAssociationPropertyTypes =
        partyReferenceService.getAssociationPropertyTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1,
        retrievedAssociationPropertyTypes.size(),
        "The correct number of association property types was not retrieved");
  }

  /** Test the association type reference functionality. */
  @Test
  public void associationTypeTest() throws Exception {
    List<AssociationType> retrievedAssociationTypes =
        partyReferenceService.getAssociationTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        22,
        retrievedAssociationTypes.size(),
        "The correct number of association types was not retrieved");

    retrievedAssociationTypes =
        partyReferenceService.getAssociationTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        22,
        retrievedAssociationTypes.size(),
        "The correct number of association types was not retrieved");

    retrievedAssociationTypes =
        partyReferenceService.getAssociationTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        21,
        retrievedAssociationTypes.size(),
        "The correct number of association types was not retrieved");
  }

  /** Test the attribute type category reference functionality. */
  @Test
  public void attributeTypeCategoryTest() throws Exception {
    List<AttributeTypeCategory> retrievedAttributeTypeCategories =
        partyReferenceService.getAttributeTypeCategories(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedAttributeTypeCategories.size(),
        "The correct number of attribute type categories was not retrieved");

    retrievedAttributeTypeCategories =
        partyReferenceService.getAttributeTypeCategories(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedAttributeTypeCategories.size(),
        "The correct number of attribute type categories was not retrieved");

    retrievedAttributeTypeCategories =
        partyReferenceService.getAttributeTypeCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1,
        retrievedAttributeTypeCategories.size(),
        "The correct number of attribute type categories was not retrieved");
  }

  /** Test the attribute type reference functionality. */
  @Test
  public void attributeTypeTest() throws Exception {
    List<AttributeType> retrievedAttributeTypes =
        partyReferenceService.getAttributeTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedAttributeTypes.size(),
        "The correct number of attribute types was not retrieved");

    retrievedAttributeTypes =
        partyReferenceService.getAttributeTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedAttributeTypes.size(),
        "The correct number of attribute types was not retrieved");

    retrievedAttributeTypes =
        partyReferenceService.getAttributeTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedAttributeTypes.size(),
        "The correct number of attribute types was not retrieved");
  }

  /** Test the consent type reference functionality. */
  @Test
  public void consentTypeTest() throws Exception {
    List<ConsentType> retrievedConsentTypes =
        partyReferenceService.getConsentTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2, retrievedConsentTypes.size(), "The correct number of consent types was not retrieved");

    retrievedConsentTypes =
        partyReferenceService.getConsentTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2, retrievedConsentTypes.size(), "The correct number of consent types was not retrieved");

    retrievedConsentTypes =
        partyReferenceService.getConsentTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1, retrievedConsentTypes.size(), "The correct number of consent types was not retrieved");
  }

  /** Test the contact mechanism purpose functionality. */
  @Test
  public void contactMechanismPurposesTest() throws Exception {
    List<ContactMechanismPurpose> retrievedContactMechanismPurposes =
        partyReferenceService.getContactMechanismPurposes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedContactMechanismPurposes.size(),
        "The correct number of contact mechanism purposes was not retrieved");

    retrievedContactMechanismPurposes =
        partyReferenceService.getContactMechanismPurposes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedContactMechanismPurposes.size(),
        "The correct number of contact mechanism purposes was not retrieved");

    retrievedContactMechanismPurposes =
        partyReferenceService.getContactMechanismPurposes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedContactMechanismPurposes.size(),
        "The correct number of contact mechanism purposes was not retrieved");
  }

  /** Test the contact mechanism role functionality. */
  @Test
  public void contactMechanismRolesTest() throws Exception {
    List<ContactMechanismRole> retrievedContactMechanismRoles =
        partyReferenceService.getContactMechanismRoles(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        26,
        retrievedContactMechanismRoles.size(),
        "The correct number of contact mechanism roles was not retrieved");

    retrievedContactMechanismRoles =
        partyReferenceService.getContactMechanismRoles(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        26,
        retrievedContactMechanismRoles.size(),
        "The correct number of contact mechanism roles was not retrieved");

    retrievedContactMechanismRoles =
        partyReferenceService.getContactMechanismRoles(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        25,
        retrievedContactMechanismRoles.size(),
        "The correct number of contact mechanism roles was not retrieved");
  }

  /** Test the contact mechanism type functionality. */
  @Test
  public void contactMechanismTypeTest() throws Exception {
    List<ContactMechanismType> retrievedContactMechanismTypes =
        partyReferenceService.getContactMechanismTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedContactMechanismTypes.size(),
        "The correct number of contact mechanism types was not retrieved");

    retrievedContactMechanismTypes =
        partyReferenceService.getContactMechanismTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedContactMechanismTypes.size(),
        "The correct number of contact mechanism types was not retrieved");

    retrievedContactMechanismTypes =
        partyReferenceService.getContactMechanismTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        5,
        retrievedContactMechanismTypes.size(),
        "The correct number of contact mechanism types was not retrieved");
  }

  /** Test the employment status reference functionality. */
  @Test
  public void employmentStatusTest() throws Exception {
    List<EmploymentStatus> retrievedEmploymentStatuses =
        partyReferenceService.getEmploymentStatuses(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedEmploymentStatuses.size(),
        "The correct number of employment statuses was not retrieved");

    retrievedEmploymentStatuses =
        partyReferenceService.getEmploymentStatuses(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedEmploymentStatuses.size(),
        "The correct number of employment statuses was not retrieved");

    retrievedEmploymentStatuses =
        partyReferenceService.getEmploymentStatuses(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedEmploymentStatuses.size(),
        "The correct number of employment statuses was not retrieved");
  }

  /** Test the employment type reference functionality. */
  @Test
  public void employmentTypeTest() throws Exception {
    List<EmploymentType> retrievedEmploymentTypes =
        partyReferenceService.getEmploymentTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedEmploymentTypes.size(),
        "The correct number of employment types was not retrieved");

    retrievedEmploymentTypes =
        partyReferenceService.getEmploymentTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedEmploymentTypes.size(),
        "The correct number of employment types was not retrieved");

    retrievedEmploymentTypes =
        partyReferenceService.getEmploymentTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedEmploymentTypes.size(),
        "The correct number of employment types was not retrieved");
  }

  /** Test the external reference type reference functionality. */
  @Test
  public void externalReferenceTypeTest() throws Exception {
    List<ExternalReferenceType> retrievedExternalReferenceTypes =
        partyReferenceService.getExternalReferenceTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedExternalReferenceTypes.size(),
        "The correct number of external reference types was not retrieved");

    retrievedExternalReferenceTypes =
        partyReferenceService.getExternalReferenceTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedExternalReferenceTypes.size(),
        "The correct number of external reference types was not retrieved");

    retrievedExternalReferenceTypes =
        partyReferenceService.getExternalReferenceTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1,
        retrievedExternalReferenceTypes.size(),
        "The correct number of external reference types was not retrieved");
  }

  /** Test the field of study reference functionality. */
  @Test
  public void fieldOfStudyTest() throws Exception {
    List<FieldOfStudy> retrievedFieldsOfStudy =
        partyReferenceService.getFieldsOfStudy(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        192,
        retrievedFieldsOfStudy.size(),
        "The correct number of fields of study was not retrieved");

    retrievedFieldsOfStudy =
        partyReferenceService.getFieldsOfStudy(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        192,
        retrievedFieldsOfStudy.size(),
        "The correct number of fields of study was not retrieved");

    retrievedFieldsOfStudy =
        partyReferenceService.getFieldsOfStudy(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        191,
        retrievedFieldsOfStudy.size(),
        "The correct number of fields of study was not retrieved");
  }

  /** Test the gender reference functionality. */
  @Test
  public void genderTest() throws Exception {
    List<Gender> retrievedGenders =
        partyReferenceService.getGenders(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(6, retrievedGenders.size(), "The correct number of genders was not retrieved");

    retrievedGenders =
        partyReferenceService.getGenders(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(6, retrievedGenders.size(), "The correct number of genders was not retrieved");

    retrievedGenders =
        partyReferenceService.getGenders(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(5, retrievedGenders.size(), "The correct number of genders was not retrieved");
  }

  /** Test the identity document type reference functionality. */
  @Test
  public void identityDocumentTypeTest() throws Exception {
    List<IdentityDocumentType> retrievedIdentityDocumentTypes =
        partyReferenceService.getIdentityDocumentTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedIdentityDocumentTypes.size(),
        "The correct number of identity document types was not retrieved");

    retrievedIdentityDocumentTypes =
        partyReferenceService.getIdentityDocumentTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedIdentityDocumentTypes.size(),
        "The correct number of identity document types was not retrieved");

    retrievedIdentityDocumentTypes =
        partyReferenceService.getIdentityDocumentTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        5,
        retrievedIdentityDocumentTypes.size(),
        "The correct number of identity document types was not retrieved");
  }

  /** Test the lock type category reference functionality. */
  @Test
  public void lockTypeCategoryTest() throws Exception {
    List<LockTypeCategory> retrievedLockTypeCategories =
        partyReferenceService.getLockTypeCategories(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedLockTypeCategories.size(),
        "The correct number of lock type categories was not retrieved");

    retrievedLockTypeCategories =
        partyReferenceService.getLockTypeCategories(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedLockTypeCategories.size(),
        "The correct number of lock type categories was not retrieved");

    retrievedLockTypeCategories =
        partyReferenceService.getLockTypeCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedLockTypeCategories.size(),
        "The correct number of lock type categories was not retrieved");
  }

  /** Test the lock type reference functionality. */
  @Test
  public void lockTypeTest() throws Exception {
    List<LockType> retrievedLockTypes =
        partyReferenceService.getLockTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3, retrievedLockTypes.size(), "The correct number of lock types was not retrieved");

    retrievedLockTypes =
        partyReferenceService.getLockTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3, retrievedLockTypes.size(), "The correct number of lock types was not retrieved");

    retrievedLockTypes =
        partyReferenceService.getLockTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2, retrievedLockTypes.size(), "The correct number of lock types was not retrieved");
  }

  /** Test the marital status reference functionality. */
  @Test
  public void maritalStatusTest() throws Exception {
    List<MaritalStatus> retrievedMaritalStatuses =
        partyReferenceService.getMaritalStatuses(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedMaritalStatuses.size(),
        "The correct number of marital statuses was not retrieved");

    retrievedMaritalStatuses =
        partyReferenceService.getMaritalStatuses(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedMaritalStatuses.size(),
        "The correct number of marital statuses was not retrieved");

    retrievedMaritalStatuses =
        partyReferenceService.getMaritalStatuses(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedMaritalStatuses.size(),
        "The correct number of marital statuses was not retrieved");
  }

  /** Test the marriage type reference functionality. */
  @Test
  public void marriageTypeTest() throws Exception {
    List<MarriageType> retrievedMarriageTypes =
        partyReferenceService.getMarriageTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7, retrievedMarriageTypes.size(), "The correct number of marriage types was not retrieved");

    retrievedMarriageTypes =
        partyReferenceService.getMarriageTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7, retrievedMarriageTypes.size(), "The correct number of marriage types was not retrieved");

    retrievedMarriageTypes =
        partyReferenceService.getMarriageTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6, retrievedMarriageTypes.size(), "The correct number of marriage types was not retrieved");
  }

  /** Test the next of kin type reference functionality. */
  @Test
  public void nextOfKinTypeTest() throws Exception {
    List<NextOfKinType> retrievedNextOfKinTypes =
        partyReferenceService.getNextOfKinTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        18,
        retrievedNextOfKinTypes.size(),
        "The correct number of next of kin types was not retrieved");

    retrievedNextOfKinTypes =
        partyReferenceService.getNextOfKinTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        18,
        retrievedNextOfKinTypes.size(),
        "The correct number of next of kin types was not retrieved");

    retrievedNextOfKinTypes =
        partyReferenceService.getNextOfKinTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        17,
        retrievedNextOfKinTypes.size(),
        "The correct number of next of kin types was not retrieved");
  }

  /** Test the occupation reference functionality. */
  @Test
  public void occupationTest() throws Exception {
    List<Occupation> retrievedOccupations =
        partyReferenceService.getOccupations(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        30, retrievedOccupations.size(), "The correct number of occupations was not retrieved");

    retrievedOccupations =
        partyReferenceService.getOccupations(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        30, retrievedOccupations.size(), "The correct number of occupations was not retrieved");

    retrievedOccupations =
        partyReferenceService.getOccupations(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        29, retrievedOccupations.size(), "The correct number of occupations was not retrieved");
  }

  /** Test the physical address purpose functionality. */
  @Test
  public void physicalAddressPurposeTest() throws Exception {
    List<PhysicalAddressPurpose> retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedPhysicalAddressPurposes.size(),
        "The correct number of physical address purposes was not retrieved");

    retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedPhysicalAddressPurposes.size(),
        "The correct number of physical address purposes was not retrieved");

    retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedPhysicalAddressPurposes.size(),
        "The correct number of physical address purposes was not retrieved");
  }

  /** Test the physical address role functionality. */
  @Test
  public void physicalAddressRoleTest() throws Exception {
    List<PhysicalAddressRole> retrievedPhysicalAddressRoles =
        partyReferenceService.getPhysicalAddressRoles(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedPhysicalAddressRoles.size(),
        "The correct number of physical address roles was not retrieved");

    retrievedPhysicalAddressRoles =
        partyReferenceService.getPhysicalAddressRoles(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedPhysicalAddressRoles.size(),
        "The correct number of physical address roles was not retrieved");

    retrievedPhysicalAddressRoles =
        partyReferenceService.getPhysicalAddressRoles(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedPhysicalAddressRoles.size(),
        "The correct number of physical address roles was not retrieved");
  }

  /** Test the physical address type functionality. */
  @Test
  public void physicalAddressTypeTest() throws Exception {
    List<PhysicalAddressType> retrievedPhysicalAddressTypes =
        partyReferenceService.getPhysicalAddressTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedPhysicalAddressTypes.size(),
        "The correct number of physical address types was not retrieved");

    retrievedPhysicalAddressTypes =
        partyReferenceService.getPhysicalAddressTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedPhysicalAddressTypes.size(),
        "The correct number of physical address types was not retrieved");

    retrievedPhysicalAddressTypes =
        partyReferenceService.getPhysicalAddressTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedPhysicalAddressTypes.size(),
        "The correct number of physical address types was not retrieved");
  }

  /** Test the preference type category functionality. */
  @Test
  public void preferenceTypeCategoryTest() throws Exception {
    List<PreferenceTypeCategory> retrievedPreferenceTypeCategories =
        partyReferenceService.getPreferenceTypeCategories(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedPreferenceTypeCategories.size(),
        "The correct number of preference type categories was not retrieved");

    retrievedPreferenceTypeCategories =
        partyReferenceService.getPreferenceTypeCategories(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedPreferenceTypeCategories.size(),
        "The correct number of preference type categories was not retrieved");

    retrievedPreferenceTypeCategories =
        partyReferenceService.getPreferenceTypeCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1,
        retrievedPreferenceTypeCategories.size(),
        "The correct number of preference type categories was not retrieved");
  }

  /** Test the preference type functionality. */
  @Test
  public void preferenceTypeTest() throws Exception {
    List<PreferenceType> retrievedPreferenceTypes =
        partyReferenceService.getPreferenceTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedPreferenceTypes.size(),
        "The correct number of preference types was not retrieved");

    retrievedPreferenceTypes =
        partyReferenceService.getPreferenceTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedPreferenceTypes.size(),
        "The correct number of preference types was not retrieved");

    retrievedPreferenceTypes =
        partyReferenceService.getPreferenceTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedPreferenceTypes.size(),
        "The correct number of preference types was not retrieved");
  }

  /** Test the qualification type reference functionality. */
  @Test
  public void qualificationTypeTest() throws Exception {
    List<QualificationType> retrievedQualificationTypes =
        partyReferenceService.getQualificationTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        15,
        retrievedQualificationTypes.size(),
        "The correct number of qualification types was not retrieved");

    retrievedQualificationTypes =
        partyReferenceService.getQualificationTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        15,
        retrievedQualificationTypes.size(),
        "The correct number of qualification types was not retrieved");

    retrievedQualificationTypes =
        partyReferenceService.getQualificationTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        14,
        retrievedQualificationTypes.size(),
        "The correct number of qualification types was not retrieved");
  }

  /** Test the race reference functionality. */
  @Test
  public void raceTest() throws Exception {
    List<Race> retrievedRaces =
        partyReferenceService.getRaces(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(7, retrievedRaces.size(), "The correct number of races was not retrieved");

    retrievedRaces =
        partyReferenceService.getRaces(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(7, retrievedRaces.size(), "The correct number of races was not retrieved");

    retrievedRaces =
        partyReferenceService.getRaces(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(6, retrievedRaces.size(), "The correct number of races was not retrieved");
  }

  /** Test the residence permit type reference functionality. */
  @Test
  public void residencePermitTypeTest() throws Exception {
    List<ResidencePermitType> retrievedResidencePermitTypes =
        partyReferenceService.getResidencePermitTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        10,
        retrievedResidencePermitTypes.size(),
        "The correct number of residence permit types was not retrieved");

    retrievedResidencePermitTypes =
        partyReferenceService.getResidencePermitTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        10,
        retrievedResidencePermitTypes.size(),
        "The correct number of residence permit types was not retrieved");

    retrievedResidencePermitTypes =
        partyReferenceService.getResidencePermitTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedResidencePermitTypes.size(),
        "The correct number of residence permit types was not retrieved");
  }

  /** Test the residency status reference functionality. */
  @Test
  public void residencyStatusTest() throws Exception {
    List<ResidencyStatus> retrievedResidencyStatuses =
        partyReferenceService.getResidencyStatuses(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedResidencyStatuses.size(),
        "The correct number of residency statuses was not retrieved");

    retrievedResidencyStatuses =
        partyReferenceService.getResidencyStatuses(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedResidencyStatuses.size(),
        "The correct number of residency statuses was not retrieved");

    retrievedResidencyStatuses =
        partyReferenceService.getResidencyStatuses(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        5,
        retrievedResidencyStatuses.size(),
        "The correct number of residency statuses was not retrieved");
  }

  /** Test the residential type reference functionality. */
  @Test
  public void residentialTypeTest() throws Exception {
    List<ResidentialType> retrievedResidentialTypes =
        partyReferenceService.getResidentialTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedResidentialTypes.size(),
        "The correct number of residential types was not retrieved");

    retrievedResidentialTypes =
        partyReferenceService.getResidentialTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedResidentialTypes.size(),
        "The correct number of residential types was not retrieved");

    retrievedResidentialTypes =
        partyReferenceService.getResidentialTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7,
        retrievedResidentialTypes.size(),
        "The correct number of residential types was not retrieved");
  }

  /** Test the role purpose functionality. */
  @Test
  public void rolePurposeTest() throws Exception {
    List<RolePurpose> retrievedRolePurposes =
        partyReferenceService.getRolePurposes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1, retrievedRolePurposes.size(), "The correct number of role purposes was not retrieved");

    retrievedRolePurposes =
        partyReferenceService.getRolePurposes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1, retrievedRolePurposes.size(), "The correct number of role purposes was not retrieved");

    retrievedRolePurposes =
        partyReferenceService.getRolePurposes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        0, retrievedRolePurposes.size(), "The correct number of role purposes was not retrieved");
  }

  /** Test the role type attribute type constraint functionality. */
  @Test
  public void roleTypeAttributeTypeConstraintTest() throws Exception {
    List<RoleTypeAttributeTypeConstraint> retrievedRoleTypeAttributeTypeConstraints =
        partyReferenceService.getRoleTypeAttributeTypeConstraints();

    assertEquals(
        72,
        retrievedRoleTypeAttributeTypeConstraints.size(),
        "The correct number of role type attribute type constraints was not retrieved");

    retrievedRoleTypeAttributeTypeConstraints =
        partyReferenceService.getRoleTypeAttributeTypeConstraints("test_person_role");

    assertEquals(
        57,
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
        partyReferenceService.getRoleTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        39, retrievedRoleTypes.size(), "The correct number of role types was not retrieved");

    retrievedRoleTypes =
        partyReferenceService.getRoleTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        39, retrievedRoleTypes.size(), "The correct number of role types was not retrieved");

    retrievedRoleTypes =
        partyReferenceService.getRoleTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        35, retrievedRoleTypes.size(), "The correct number of role types was not retrieved");
  }

  /** Test the segment reference functionality. */
  @Test
  public void segmentTest() throws Exception {
    List<Segment> retrievedSegments =
        partyReferenceService.getSegments(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(2, retrievedSegments.size(), "The correct number of segments was not retrieved");

    retrievedSegments =
        partyReferenceService.getSegments(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(2, retrievedSegments.size(), "The correct number of segments was not retrieved");

    retrievedSegments =
        partyReferenceService.getSegments(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(0, retrievedSegments.size(), "The correct number of segments was not retrieved");
  }

  /** Test the source of funds types reference functionality. */
  @Test
  public void sourceOfFundsTypeTest() throws Exception {
    List<SourceOfFundsType> retrievedSourceOfFundsTypes =
        partyReferenceService.getSourceOfFundsTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        42,
        retrievedSourceOfFundsTypes.size(),
        "The correct number of source of funds types was not retrieved");

    retrievedSourceOfFundsTypes =
        partyReferenceService.getSourceOfFundsTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        42,
        retrievedSourceOfFundsTypes.size(),
        "The correct number of source of funds types was not retrieved");

    retrievedSourceOfFundsTypes =
        partyReferenceService.getSourceOfFundsTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        41,
        retrievedSourceOfFundsTypes.size(),
        "The correct number of source of funds types was not retrieved");
  }

  /** Test the source of wealth types reference functionality. */
  @Test
  public void sourceOfWealthTypeTest() throws Exception {
    List<SourceOfWealthType> retrievedSourceOfWealthTypes =
        partyReferenceService.getSourceOfWealthTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedSourceOfWealthTypes.size(),
        "The correct number of source of wealth types was not retrieved");

    retrievedSourceOfWealthTypes =
        partyReferenceService.getSourceOfWealthTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedSourceOfWealthTypes.size(),
        "The correct number of source of wealth types was not retrieved");

    retrievedSourceOfWealthTypes =
        partyReferenceService.getSourceOfWealthTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedSourceOfWealthTypes.size(),
        "The correct number of source of wealth types was not retrieved");
  }

  /** Test the status type category functionality. */
  @Test
  public void statusTypeCategoryTest() throws Exception {
    List<StatusTypeCategory> retrievedStatusTypeCategories =
        partyReferenceService.getStatusTypeCategories(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedStatusTypeCategories.size(),
        "The correct number of status type categories was not retrieved");

    retrievedStatusTypeCategories =
        partyReferenceService.getStatusTypeCategories(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedStatusTypeCategories.size(),
        "The correct number of status type categories was not retrieved");

    retrievedStatusTypeCategories =
        partyReferenceService.getStatusTypeCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedStatusTypeCategories.size(),
        "The correct number of status type categories was not retrieved");
  }

  /** Test the status type functionality. */
  @Test
  public void statusTypeTest() throws Exception {
    List<StatusType> retrievedStatusTypes =
        partyReferenceService.getStatusTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3, retrievedStatusTypes.size(), "The correct number of status types was not retrieved");

    retrievedStatusTypes =
        partyReferenceService.getStatusTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3, retrievedStatusTypes.size(), "The correct number of status types was not retrieved");

    retrievedStatusTypes =
        partyReferenceService.getStatusTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2, retrievedStatusTypes.size(), "The correct number of status types was not retrieved");
  }

  /** Test the tax number type reference functionality. */
  @Test
  public void taxNumberTypeTest() throws Exception {
    List<TaxNumberType> retrievedTaxNumberTypes =
        partyReferenceService.getTaxNumberTypes(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedTaxNumberTypes.size(),
        "The correct number of tax number types was not retrieved");

    retrievedTaxNumberTypes =
        partyReferenceService.getTaxNumberTypes(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedTaxNumberTypes.size(),
        "The correct number of tax number types was not retrieved");

    retrievedTaxNumberTypes =
        partyReferenceService.getTaxNumberTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7,
        retrievedTaxNumberTypes.size(),
        "The correct number of tax number types was not retrieved");
  }

  /** Test the time to contact functionality. */
  @Test
  public void timeToContactTest() throws Exception {
    List<TimeToContact> retrievedTimesToContact =
        partyReferenceService.getTimesToContact(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedTimesToContact.size(),
        "The correct number of times to contact was not retrieved");

    retrievedTimesToContact =
        partyReferenceService.getTimesToContact(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedTimesToContact.size(),
        "The correct number of times to contact was not retrieved");

    retrievedTimesToContact =
        partyReferenceService.getTimesToContact(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        5,
        retrievedTimesToContact.size(),
        "The correct number of times to contact was not retrieved");
  }

  /** Test the title reference functionality. */
  @Test
  public void titleTest() throws Exception {
    List<Title> retrievedTitles =
        partyReferenceService.getTitles(IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(13, retrievedTitles.size(), "The correct number of titles was not retrieved");

    retrievedTitles =
        partyReferenceService.getTitles(
            IPartyReferenceService.DEFAULT_TENANT_ID, IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(13, retrievedTitles.size(), "The correct number of titles was not retrieved");

    retrievedTitles =
        partyReferenceService.getTitles(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            IPartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(12, retrievedTitles.size(), "The correct number of titles was not retrieved");
  }

  /** Test the reference data validity check functionality. */
  @Test
  public void validityTest() throws Exception {
    assertTrue(
        partyReferenceService.isValidAttributeType(
            IPartyReferenceService.DEFAULT_TENANT_ID, PartyType.PERSON.code(), "height"));
    assertTrue(
        partyReferenceService.isValidAttributeTypeCategory(
            IPartyReferenceService.DEFAULT_TENANT_ID, "anthropometric_measurements"));
    assertTrue(
        partyReferenceService.isValidConsentType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "marketing"));
    assertTrue(
        partyReferenceService.isValidContactMechanismPurpose(
            IPartyReferenceService.DEFAULT_TENANT_ID, "person", "email_address", "security"));
    assertTrue(
        partyReferenceService.isValidContactMechanismRole(
            IPartyReferenceService.DEFAULT_TENANT_ID,
            PartyType.PERSON.code(),
            ContactMechanismType.MOBILE_NUMBER,
            "personal_mobile_number"));
    assertTrue(
        partyReferenceService.isValidContactMechanismType(
            IPartyReferenceService.DEFAULT_TENANT_ID, ContactMechanismType.MOBILE_NUMBER));
    assertTrue(
        partyReferenceService.isValidEmploymentStatus(
            IPartyReferenceService.DEFAULT_TENANT_ID, "employed"));
    assertTrue(
        partyReferenceService.isValidEmploymentType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "employed", "full_time"));
    assertTrue(
        partyReferenceService.isValidExternalReferenceType(
            IPartyReferenceService.DEFAULT_TENANT_ID,
            PartyType.PERSON.code(),
            "test_external_reference_type"));
    assertTrue(
        partyReferenceService.isValidGender(IPartyReferenceService.DEFAULT_TENANT_ID, "female"));
    assertTrue(
        partyReferenceService.isValidIdentityDocumentType(
            IPartyReferenceService.DEFAULT_TENANT_ID, PartyType.PERSON.code(), "passport"));
    assertTrue(
        partyReferenceService.isValidLockType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "person", "suspected_fraud"));
    assertTrue(
        partyReferenceService.isValidLockTypeCategory(
            IPartyReferenceService.DEFAULT_TENANT_ID, "fraud"));
    assertTrue(
        partyReferenceService.isValidMaritalStatus(
            IPartyReferenceService.DEFAULT_TENANT_ID, "married"));
    assertTrue(
        partyReferenceService.isValidMarriageType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "married", "anc_with_accrual"));
    // referenceService.isValidMinorType("minor");
    assertTrue(
        partyReferenceService.isValidNextOfKinType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "mother"));
    assertTrue(
        partyReferenceService.isValidOccupation(
            IPartyReferenceService.DEFAULT_TENANT_ID, "executive"));
    assertTrue(
        partyReferenceService.isValidPhysicalAddressPurpose(
            IPartyReferenceService.DEFAULT_TENANT_ID, PartyType.PERSON.code(), "billing"));
    assertTrue(
        partyReferenceService.isValidPhysicalAddressRole(
            IPartyReferenceService.DEFAULT_TENANT_ID,
            PartyType.PERSON.code(),
            PhysicalAddressRole.RESIDENTIAL));
    assertTrue(
        partyReferenceService.isValidPhysicalAddressType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "complex"));
    assertTrue(
        partyReferenceService.isValidPreferenceType(
            IPartyReferenceService.DEFAULT_TENANT_ID,
            PartyType.ORGANIZATION.code(),
            "correspondence_language"));
    assertTrue(
        partyReferenceService.isValidPreferenceTypeCategory(
            IPartyReferenceService.DEFAULT_TENANT_ID, "correspondence"));
    assertTrue(
        partyReferenceService.isValidQualificationType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "doctoral_degree"));
    assertTrue(
        partyReferenceService.isValidRace(IPartyReferenceService.DEFAULT_TENANT_ID, "white"));
    assertTrue(
        partyReferenceService.isValidResidencePermitType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "za_general_work_visa"));
    assertTrue(
        partyReferenceService.isValidResidencyStatus(
            IPartyReferenceService.DEFAULT_TENANT_ID, "permanent_resident"));
    assertTrue(
        partyReferenceService.isValidResidentialType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "owner"));
    assertTrue(
        partyReferenceService.isValidRolePurpose(
            IPartyReferenceService.DEFAULT_TENANT_ID, "test_role_purpose"));
    assertTrue(
        partyReferenceService.isValidRoleType(
            IPartyReferenceService.DEFAULT_TENANT_ID, PartyType.PERSON.code(), "test_person_role"));
    assertTrue(
        partyReferenceService.isValidSegment(
            IPartyReferenceService.DEFAULT_TENANT_ID, "test_person_segment"));
    assertTrue(
        partyReferenceService.isValidSourceOfFundsType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "salary_wages"));
    assertTrue(
        partyReferenceService.isValidStatusType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "person", "fraud_investigation"));
    assertTrue(
        partyReferenceService.isValidStatusTypeCategory(
            IPartyReferenceService.DEFAULT_TENANT_ID, "fraud"));
    assertTrue(
        partyReferenceService.isValidTaxNumberType(
            IPartyReferenceService.DEFAULT_TENANT_ID, "person", "za_income_tax_number"));
    assertTrue(
        partyReferenceService.isValidTimeToContact(
            IPartyReferenceService.DEFAULT_TENANT_ID, "anytime"));
    assertTrue(partyReferenceService.isValidTitle(IPartyReferenceService.DEFAULT_TENANT_ID, "mrs"));
  }
}

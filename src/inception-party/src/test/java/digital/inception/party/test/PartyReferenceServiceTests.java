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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import digital.inception.party.model.AssociationPropertyType;
import digital.inception.party.model.AssociationType;
import digital.inception.party.model.AttributeType;
import digital.inception.party.model.AttributeTypeCategory;
import digital.inception.party.model.ConsentType;
import digital.inception.party.model.ContactMechanismPurpose;
import digital.inception.party.model.ContactMechanismRole;
import digital.inception.party.model.ContactMechanismType;
import digital.inception.party.model.EmploymentStatus;
import digital.inception.party.model.EmploymentType;
import digital.inception.party.model.ExternalReferenceType;
import digital.inception.party.model.FieldOfStudy;
import digital.inception.party.model.Gender;
import digital.inception.party.model.IdentificationType;
import digital.inception.party.model.IndustryClassification;
import digital.inception.party.model.IndustryClassificationCategory;
import digital.inception.party.model.IndustryClassificationSystem;
import digital.inception.party.model.LockType;
import digital.inception.party.model.LockTypeCategory;
import digital.inception.party.model.MandataryRole;
import digital.inception.party.model.MandatePropertyType;
import digital.inception.party.model.MandateType;
import digital.inception.party.model.MaritalStatus;
import digital.inception.party.model.MarriageType;
import digital.inception.party.model.NextOfKinType;
import digital.inception.party.model.Occupation;
import digital.inception.party.model.PartyType;
import digital.inception.party.model.PhysicalAddressPurpose;
import digital.inception.party.model.PhysicalAddressRole;
import digital.inception.party.model.PhysicalAddressType;
import digital.inception.party.model.PreferenceType;
import digital.inception.party.model.PreferenceTypeCategory;
import digital.inception.party.model.QualificationType;
import digital.inception.party.model.Race;
import digital.inception.party.model.ResidencePermitType;
import digital.inception.party.model.ResidencyStatus;
import digital.inception.party.model.ResidentialType;
import digital.inception.party.model.RolePurpose;
import digital.inception.party.model.RoleType;
import digital.inception.party.model.RoleTypeAttributeTypeConstraint;
import digital.inception.party.model.RoleTypePreferenceTypeConstraint;
import digital.inception.party.model.Segment;
import digital.inception.party.model.SegmentationType;
import digital.inception.party.model.SkillType;
import digital.inception.party.model.SourceOfFundsType;
import digital.inception.party.model.SourceOfWealthType;
import digital.inception.party.model.StatusType;
import digital.inception.party.model.StatusTypeCategory;
import digital.inception.party.model.TaxNumberType;
import digital.inception.party.model.TimeToContact;
import digital.inception.party.model.Title;
import digital.inception.party.service.PartyReferenceService;
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
 * The <b>PartyReferenceServiceTests</b> class contains the JUnit tests for the
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
public class PartyReferenceServiceTests {

  /** The Party Reference Service. */
  @Autowired private PartyReferenceService partyReferenceService;

  /** Test the association property type reference functionality. */
  @Test
  public void associationPropertyTypeTest() throws Exception {
    List<AssociationPropertyType> retrievedAssociationPropertyTypes =
        partyReferenceService.getAssociationPropertyTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7,
        retrievedAssociationPropertyTypes.size(),
        "The correct number of association property types was not retrieved");

    retrievedAssociationPropertyTypes =
        partyReferenceService.getAssociationPropertyTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7,
        retrievedAssociationPropertyTypes.size(),
        "The correct number of association property types was not retrieved");

    retrievedAssociationPropertyTypes =
        partyReferenceService.getAssociationPropertyTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1,
        retrievedAssociationPropertyTypes.size(),
        "The correct number of association property types was not retrieved");
  }

  /** Test the association type reference functionality. */
  @Test
  public void associationTypeTest() throws Exception {
    List<AssociationType> retrievedAssociationTypes =
        partyReferenceService.getAssociationTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        22,
        retrievedAssociationTypes.size(),
        "The correct number of association types was not retrieved");

    retrievedAssociationTypes =
        partyReferenceService.getAssociationTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        22,
        retrievedAssociationTypes.size(),
        "The correct number of association types was not retrieved");

    retrievedAssociationTypes =
        partyReferenceService.getAssociationTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        21,
        retrievedAssociationTypes.size(),
        "The correct number of association types was not retrieved");
  }

  /** Test the attribute type category reference functionality. */
  @Test
  public void attributeTypeCategoryTest() throws Exception {
    List<AttributeTypeCategory> retrievedAttributeTypeCategories =
        partyReferenceService.getAttributeTypeCategories(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedAttributeTypeCategories.size(),
        "The correct number of attribute type categories was not retrieved");

    retrievedAttributeTypeCategories =
        partyReferenceService.getAttributeTypeCategories(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedAttributeTypeCategories.size(),
        "The correct number of attribute type categories was not retrieved");

    retrievedAttributeTypeCategories =
        partyReferenceService.getAttributeTypeCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        11,
        retrievedAttributeTypes.size(),
        "The correct number of attribute types was not retrieved");

    retrievedAttributeTypes =
        partyReferenceService.getAttributeTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedAttributeTypes.size(),
        "The correct number of attribute types was not retrieved");

    retrievedAttributeTypes =
        partyReferenceService.getAttributeTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        2, retrievedConsentTypes.size(), "The correct number of consent types was not retrieved");

    retrievedConsentTypes =
        partyReferenceService.getConsentTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2, retrievedConsentTypes.size(), "The correct number of consent types was not retrieved");

    retrievedConsentTypes =
        partyReferenceService.getConsentTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1, retrievedConsentTypes.size(), "The correct number of consent types was not retrieved");
  }

  /** Test the contact mechanism purpose functionality. */
  @Test
  public void contactMechanismPurposesTest() throws Exception {
    List<ContactMechanismPurpose> retrievedContactMechanismPurposes =
        partyReferenceService.getContactMechanismPurposes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedContactMechanismPurposes.size(),
        "The correct number of contact mechanism purposes was not retrieved");

    retrievedContactMechanismPurposes =
        partyReferenceService.getContactMechanismPurposes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedContactMechanismPurposes.size(),
        "The correct number of contact mechanism purposes was not retrieved");

    retrievedContactMechanismPurposes =
        partyReferenceService.getContactMechanismPurposes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        26,
        retrievedContactMechanismRoles.size(),
        "The correct number of contact mechanism roles was not retrieved");

    retrievedContactMechanismRoles =
        partyReferenceService.getContactMechanismRoles(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        26,
        retrievedContactMechanismRoles.size(),
        "The correct number of contact mechanism roles was not retrieved");

    retrievedContactMechanismRoles =
        partyReferenceService.getContactMechanismRoles(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        6,
        retrievedContactMechanismTypes.size(),
        "The correct number of contact mechanism types was not retrieved");

    retrievedContactMechanismTypes =
        partyReferenceService.getContactMechanismTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedContactMechanismTypes.size(),
        "The correct number of contact mechanism types was not retrieved");

    retrievedContactMechanismTypes =
        partyReferenceService.getContactMechanismTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        4,
        retrievedEmploymentStatuses.size(),
        "The correct number of employment statuses was not retrieved");

    retrievedEmploymentStatuses =
        partyReferenceService.getEmploymentStatuses(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedEmploymentStatuses.size(),
        "The correct number of employment statuses was not retrieved");

    retrievedEmploymentStatuses =
        partyReferenceService.getEmploymentStatuses(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        12,
        retrievedEmploymentTypes.size(),
        "The correct number of employment types was not retrieved");

    retrievedEmploymentTypes =
        partyReferenceService.getEmploymentTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedEmploymentTypes.size(),
        "The correct number of employment types was not retrieved");

    retrievedEmploymentTypes =
        partyReferenceService.getEmploymentTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedEmploymentTypes.size(),
        "The correct number of employment types was not retrieved");
  }

  /** Test the external reference type reference functionality. */
  @Test
  public void externalReferenceTypeTest() throws Exception {
    List<ExternalReferenceType> retrievedExternalReferenceTypes =
        partyReferenceService.getExternalReferenceTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedExternalReferenceTypes.size(),
        "The correct number of external reference types was not retrieved");

    retrievedExternalReferenceTypes =
        partyReferenceService.getExternalReferenceTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedExternalReferenceTypes.size(),
        "The correct number of external reference types was not retrieved");

    retrievedExternalReferenceTypes =
        partyReferenceService.getExternalReferenceTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1,
        retrievedExternalReferenceTypes.size(),
        "The correct number of external reference types was not retrieved");
  }

  /** Test the field of study reference functionality. */
  @Test
  public void fieldOfStudyTest() throws Exception {
    List<FieldOfStudy> retrievedFieldsOfStudy =
        partyReferenceService.getFieldsOfStudy(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        192,
        retrievedFieldsOfStudy.size(),
        "The correct number of fields of study was not retrieved");

    retrievedFieldsOfStudy =
        partyReferenceService.getFieldsOfStudy(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        192,
        retrievedFieldsOfStudy.size(),
        "The correct number of fields of study was not retrieved");

    retrievedFieldsOfStudy =
        partyReferenceService.getFieldsOfStudy(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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

    assertEquals(6, retrievedGenders.size(), "The correct number of genders was not retrieved");

    retrievedGenders =
        partyReferenceService.getGenders(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(6, retrievedGenders.size(), "The correct number of genders was not retrieved");

    retrievedGenders =
        partyReferenceService.getGenders(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(5, retrievedGenders.size(), "The correct number of genders was not retrieved");
  }

  /** Test the identification type reference functionality. */
  @Test
  public void identificationTypeTest() throws Exception {
    List<IdentificationType> retrievedIdentificationTypes =
        partyReferenceService.getIdentificationTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedIdentificationTypes.size(),
        "The correct number of identification types was not retrieved");

    retrievedIdentificationTypes =
        partyReferenceService.getIdentificationTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedIdentificationTypes.size(),
        "The correct number of identification types was not retrieved");

    retrievedIdentificationTypes =
        partyReferenceService.getIdentificationTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedIdentificationTypes.size(),
        "The correct number of identification types was not retrieved");
  }

  /** Test the industry classification category functionality. */
  @Test
  public void industryClassificationCategoryTest() throws Exception {
    List<IndustryClassificationCategory> retrievedIndustryClassificationCategories =
        partyReferenceService.getIndustryClassificationCategories(
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        22,
        retrievedIndustryClassificationCategories.size(),
        "The correct number of industry classification categories was not retrieved");

    retrievedIndustryClassificationCategories =
        partyReferenceService.getIndustryClassificationCategories(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        22,
        retrievedIndustryClassificationCategories.size(),
        "The correct number of industry classification categories was not retrieved");

    retrievedIndustryClassificationCategories =
        partyReferenceService.getIndustryClassificationCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        21,
        retrievedIndustryClassificationCategories.size(),
        "The correct number of industry classification categories was not retrieved");
  }

  /** Test the industry classification system functionality. */
  @Test
  public void industryClassificationSystemTest() throws Exception {
    List<IndustryClassificationSystem> retrievedIndustryClassificationSystems =
        partyReferenceService.getIndustryClassificationSystems(
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedIndustryClassificationSystems.size(),
        "The correct number of industry classification systems was not retrieved");

    retrievedIndustryClassificationSystems =
        partyReferenceService.getIndustryClassificationSystems(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedIndustryClassificationSystems.size(),
        "The correct number of industry classification systems was not retrieved");

    retrievedIndustryClassificationSystems =
        partyReferenceService.getIndustryClassificationSystems(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1,
        retrievedIndustryClassificationSystems.size(),
        "The correct number of industry classification systems was not retrieved");
  }

  /** Test the industry classification functionality. */
  @Test
  public void industryClassificationTest() throws Exception {
    List<IndustryClassification> retrievedIndustryClassifications =
        partyReferenceService.getIndustryClassifications(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        89,
        retrievedIndustryClassifications.size(),
        "The correct number of industry classifications was not retrieved");

    retrievedIndustryClassifications =
        partyReferenceService.getIndustryClassifications(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        89,
        retrievedIndustryClassifications.size(),
        "The correct number of industry classifications was not retrieved");

    retrievedIndustryClassifications =
        partyReferenceService.getIndustryClassifications(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        88,
        retrievedIndustryClassifications.size(),
        "The correct number of industry classifications was not retrieved");
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

    retrievedLockTypeCategories =
        partyReferenceService.getLockTypeCategories(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedLockTypeCategories.size(),
        "The correct number of lock type categories was not retrieved");

    retrievedLockTypeCategories =
        partyReferenceService.getLockTypeCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
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

    retrievedLockTypes =
        partyReferenceService.getLockTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3, retrievedLockTypes.size(), "The correct number of lock types was not retrieved");

    retrievedLockTypes =
        partyReferenceService.getLockTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2, retrievedLockTypes.size(), "The correct number of lock types was not retrieved");
  }

  /** Test the mandatary role reference functionality. */
  @Test
  public void mandataryRoleTest() throws Exception {
    List<MandataryRole> retrievedMandataryRoles =
        partyReferenceService.getMandataryRoles(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        13,
        retrievedMandataryRoles.size(),
        "The correct number of mandatary roles was not retrieved");

    retrievedMandataryRoles =
        partyReferenceService.getMandataryRoles(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        13,
        retrievedMandataryRoles.size(),
        "The correct number of mandatary roles was not retrieved");

    retrievedMandataryRoles =
        partyReferenceService.getMandataryRoles(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedMandataryRoles.size(),
        "The correct number of mandatary roles was not retrieved");
  }

  /** Test the mandate property type reference functionality. */
  @Test
  public void mandatePropertyTypeTest() throws Exception {
    List<MandatePropertyType> retrievedMandatePropertyTypes =
        partyReferenceService.getMandatePropertyTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedMandatePropertyTypes.size(),
        "The correct number of mandate property types was not retrieved");

    retrievedMandatePropertyTypes =
        partyReferenceService.getMandatePropertyTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedMandatePropertyTypes.size(),
        "The correct number of mandate property types was not retrieved");

    retrievedMandatePropertyTypes =
        partyReferenceService.getMandatePropertyTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        0,
        retrievedMandatePropertyTypes.size(),
        "The correct number of mandate property types was not retrieved");
  }

  /** Test the mandate type reference functionality. */
  @Test
  public void mandateTypeTest() throws Exception {
    List<MandateType> retrievedMandateTypes =
        partyReferenceService.getMandateTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        14, retrievedMandateTypes.size(), "The correct number of mandate types was not retrieved");

    retrievedMandateTypes =
        partyReferenceService.getMandateTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        14, retrievedMandateTypes.size(), "The correct number of mandate types was not retrieved");

    retrievedMandateTypes =
        partyReferenceService.getMandateTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        13, retrievedMandateTypes.size(), "The correct number of mandate types was not retrieved");
  }

  /** Test the marital status reference functionality. */
  @Test
  public void maritalStatusTest() throws Exception {
    List<MaritalStatus> retrievedMaritalStatuses =
        partyReferenceService.getMaritalStatuses(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedMaritalStatuses.size(),
        "The correct number of marital statuses was not retrieved");

    retrievedMaritalStatuses =
        partyReferenceService.getMaritalStatuses(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedMaritalStatuses.size(),
        "The correct number of marital statuses was not retrieved");

    retrievedMaritalStatuses =
        partyReferenceService.getMaritalStatuses(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        7, retrievedMarriageTypes.size(), "The correct number of marriage types was not retrieved");

    retrievedMarriageTypes =
        partyReferenceService.getMarriageTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        7, retrievedMarriageTypes.size(), "The correct number of marriage types was not retrieved");

    retrievedMarriageTypes =
        partyReferenceService.getMarriageTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6, retrievedMarriageTypes.size(), "The correct number of marriage types was not retrieved");
  }

  /** Test the next of kin type reference functionality. */
  @Test
  public void nextOfKinTypeTest() throws Exception {
    List<NextOfKinType> retrievedNextOfKinTypes =
        partyReferenceService.getNextOfKinTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        18,
        retrievedNextOfKinTypes.size(),
        "The correct number of next of kin types was not retrieved");

    retrievedNextOfKinTypes =
        partyReferenceService.getNextOfKinTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        18,
        retrievedNextOfKinTypes.size(),
        "The correct number of next of kin types was not retrieved");

    retrievedNextOfKinTypes =
        partyReferenceService.getNextOfKinTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        30, retrievedOccupations.size(), "The correct number of occupations was not retrieved");

    retrievedOccupations =
        partyReferenceService.getOccupations(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        30, retrievedOccupations.size(), "The correct number of occupations was not retrieved");

    retrievedOccupations =
        partyReferenceService.getOccupations(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        29, retrievedOccupations.size(), "The correct number of occupations was not retrieved");
  }

  /** Test the physical address purpose functionality. */
  @Test
  public void physicalAddressPurposeTest() throws Exception {
    List<PhysicalAddressPurpose> retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedPhysicalAddressPurposes.size(),
        "The correct number of physical address purposes was not retrieved");

    retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedPhysicalAddressPurposes.size(),
        "The correct number of physical address purposes was not retrieved");

    retrievedPhysicalAddressPurposes =
        partyReferenceService.getPhysicalAddressPurposes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        12,
        retrievedPhysicalAddressRoles.size(),
        "The correct number of physical address roles was not retrieved");

    retrievedPhysicalAddressRoles =
        partyReferenceService.getPhysicalAddressRoles(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedPhysicalAddressRoles.size(),
        "The correct number of physical address roles was not retrieved");

    retrievedPhysicalAddressRoles =
        partyReferenceService.getPhysicalAddressRoles(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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

    retrievedPhysicalAddressTypes =
        partyReferenceService.getPhysicalAddressTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedPhysicalAddressTypes.size(),
        "The correct number of physical address types was not retrieved");

    retrievedPhysicalAddressTypes =
        partyReferenceService.getPhysicalAddressTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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

    retrievedPreferenceTypeCategories =
        partyReferenceService.getPreferenceTypeCategories(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedPreferenceTypeCategories.size(),
        "The correct number of preference type categories was not retrieved");

    retrievedPreferenceTypeCategories =
        partyReferenceService.getPreferenceTypeCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1,
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

    retrievedPreferenceTypes =
        partyReferenceService.getPreferenceTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        9,
        retrievedPreferenceTypes.size(),
        "The correct number of preference types was not retrieved");

    retrievedPreferenceTypes =
        partyReferenceService.getPreferenceTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        4,
        retrievedPreferenceTypes.size(),
        "The correct number of preference types was not retrieved");
  }

  /** Test the qualification type reference functionality. */
  @Test
  public void qualificationTypeTest() throws Exception {
    List<QualificationType> retrievedQualificationTypes =
        partyReferenceService.getQualificationTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        15,
        retrievedQualificationTypes.size(),
        "The correct number of qualification types was not retrieved");

    retrievedQualificationTypes =
        partyReferenceService.getQualificationTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        15,
        retrievedQualificationTypes.size(),
        "The correct number of qualification types was not retrieved");

    retrievedQualificationTypes =
        partyReferenceService.getQualificationTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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

    assertEquals(7, retrievedRaces.size(), "The correct number of races was not retrieved");

    retrievedRaces =
        partyReferenceService.getRaces(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(7, retrievedRaces.size(), "The correct number of races was not retrieved");

    retrievedRaces =
        partyReferenceService.getRaces(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(6, retrievedRaces.size(), "The correct number of races was not retrieved");
  }

  /** Test the residence permit type reference functionality. */
  @Test
  public void residencePermitTypeTest() throws Exception {
    List<ResidencePermitType> retrievedResidencePermitTypes =
        partyReferenceService.getResidencePermitTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        10,
        retrievedResidencePermitTypes.size(),
        "The correct number of residence permit types was not retrieved");

    retrievedResidencePermitTypes =
        partyReferenceService.getResidencePermitTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        10,
        retrievedResidencePermitTypes.size(),
        "The correct number of residence permit types was not retrieved");

    retrievedResidencePermitTypes =
        partyReferenceService.getResidencePermitTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        6,
        retrievedResidencyStatuses.size(),
        "The correct number of residency statuses was not retrieved");

    retrievedResidencyStatuses =
        partyReferenceService.getResidencyStatuses(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedResidencyStatuses.size(),
        "The correct number of residency statuses was not retrieved");

    retrievedResidencyStatuses =
        partyReferenceService.getResidencyStatuses(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        8,
        retrievedResidentialTypes.size(),
        "The correct number of residential types was not retrieved");

    retrievedResidentialTypes =
        partyReferenceService.getResidentialTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedResidentialTypes.size(),
        "The correct number of residential types was not retrieved");

    retrievedResidentialTypes =
        partyReferenceService.getResidentialTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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

    assertEquals(
        2, retrievedRolePurposes.size(), "The correct number of role purposes was not retrieved");

    retrievedRolePurposes =
        partyReferenceService.getRolePurposes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2, retrievedRolePurposes.size(), "The correct number of role purposes was not retrieved");

    retrievedRolePurposes =
        partyReferenceService.getRolePurposes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        1, retrievedRolePurposes.size(), "The correct number of role purposes was not retrieved");
  }

  /** Test the role type attribute type constraint functionality. */
  @Test
  public void roleTypeAttributeTypeConstraintTest() throws Exception {
    List<RoleTypeAttributeTypeConstraint> retrievedRoleTypeAttributeTypeConstraints =
        partyReferenceService.getRoleTypeAttributeTypeConstraints();

    assertEquals(
        72,
        retrievedRoleTypeAttributeTypeConstraints.size(),
        "The correct number of role type attribute type constraint was not retrieved");

    retrievedRoleTypeAttributeTypeConstraints =
        partyReferenceService.getRoleTypeAttributeTypeConstraints("test_person_role");

    assertEquals(
        57,
        retrievedRoleTypeAttributeTypeConstraints.size(),
        "The correct number of role type attribute type constraint was not retrieved");

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
        "The correct number of role type preference type constraint was not retrieved");

    retrievedRoleTypePreferenceTypeConstraints =
        partyReferenceService.getRoleTypePreferenceTypeConstraints("test_person_role");

    assertEquals(
        12,
        retrievedRoleTypePreferenceTypeConstraints.size(),
        "The correct number of role type preference type constraint was not retrieved");

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
        39, retrievedRoleTypes.size(), "The correct number of role types was not retrieved");

    retrievedRoleTypes =
        partyReferenceService.getRoleTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        39, retrievedRoleTypes.size(), "The correct number of role types was not retrieved");

    retrievedRoleTypes =
        partyReferenceService.getRoleTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        35, retrievedRoleTypes.size(), "The correct number of role types was not retrieved");
  }

  /** Test the segment reference functionality. */
  @Test
  public void segmentTest() throws Exception {
    List<Segment> retrievedSegments =
        partyReferenceService.getSegments(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(10, retrievedSegments.size(), "The correct number of segments was not retrieved");

    retrievedSegments =
        partyReferenceService.getSegments(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(10, retrievedSegments.size(), "The correct number of segments was not retrieved");

    retrievedSegments =
        partyReferenceService.getSegments(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(8, retrievedSegments.size(), "The correct number of segments was not retrieved");
  }

  /** Test the segmentation type reference functionality. */
  @Test
  public void segmentationTypeTest() throws Exception {
    List<SegmentationType> retrievedSegmentationTypes =
        partyReferenceService.getSegmentationTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedSegmentationTypes.size(),
        "The correct number of segmentation types was not retrieved");

    retrievedSegmentationTypes =
        partyReferenceService.getSegmentationTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedSegmentationTypes.size(),
        "The correct number of segmentation types was not retrieved");

    retrievedSegmentationTypes =
        partyReferenceService.getSegmentationTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
        retrievedSegmentationTypes.size(),
        "The correct number of segmentation types was not retrieved");
  }

  /** Test the skill type functionality. */
  @Test
  public void skillTypeTest() throws Exception {
    List<SkillType> retrievedSkillTypes =
        partyReferenceService.getSkillTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        42, retrievedSkillTypes.size(), "The correct number of skill types was not retrieved");

    retrievedSkillTypes =
        partyReferenceService.getSkillTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        42, retrievedSkillTypes.size(), "The correct number of skill types was not retrieved");

    retrievedSkillTypes =
        partyReferenceService.getSkillTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        41, retrievedSkillTypes.size(), "The correct number of skill types was not retrieved");
  }

  /** Test the source of funds types reference functionality. */
  @Test
  public void sourceOfFundsTypeTest() throws Exception {
    List<SourceOfFundsType> retrievedSourceOfFundsTypes =
        partyReferenceService.getSourceOfFundsTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        42,
        retrievedSourceOfFundsTypes.size(),
        "The correct number of source of funds types was not retrieved");

    retrievedSourceOfFundsTypes =
        partyReferenceService.getSourceOfFundsTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        42,
        retrievedSourceOfFundsTypes.size(),
        "The correct number of source of funds types was not retrieved");

    retrievedSourceOfFundsTypes =
        partyReferenceService.getSourceOfFundsTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        41,
        retrievedSourceOfFundsTypes.size(),
        "The correct number of source of funds types was not retrieved");
  }

  /** Test the source of wealth types reference functionality. */
  @Test
  public void sourceOfWealthTypeTest() throws Exception {
    List<SourceOfWealthType> retrievedSourceOfWealthTypes =
        partyReferenceService.getSourceOfWealthTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedSourceOfWealthTypes.size(),
        "The correct number of source of wealth types was not retrieved");

    retrievedSourceOfWealthTypes =
        partyReferenceService.getSourceOfWealthTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        12,
        retrievedSourceOfWealthTypes.size(),
        "The correct number of source of wealth types was not retrieved");

    retrievedSourceOfWealthTypes =
        partyReferenceService.getSourceOfWealthTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        11,
        retrievedSourceOfWealthTypes.size(),
        "The correct number of source of wealth types was not retrieved");
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

    retrievedStatusTypeCategories =
        partyReferenceService.getStatusTypeCategories(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3,
        retrievedStatusTypeCategories.size(),
        "The correct number of status type categories was not retrieved");

    retrievedStatusTypeCategories =
        partyReferenceService.getStatusTypeCategories(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2,
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

    retrievedStatusTypes =
        partyReferenceService.getStatusTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        3, retrievedStatusTypes.size(), "The correct number of status types was not retrieved");

    retrievedStatusTypes =
        partyReferenceService.getStatusTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        2, retrievedStatusTypes.size(), "The correct number of status types was not retrieved");
  }

  /** Test the tax number type reference functionality. */
  @Test
  public void taxNumberTypeTest() throws Exception {
    List<TaxNumberType> retrievedTaxNumberTypes =
        partyReferenceService.getTaxNumberTypes(PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedTaxNumberTypes.size(),
        "The correct number of tax number types was not retrieved");

    retrievedTaxNumberTypes =
        partyReferenceService.getTaxNumberTypes(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        8,
        retrievedTaxNumberTypes.size(),
        "The correct number of tax number types was not retrieved");

    retrievedTaxNumberTypes =
        partyReferenceService.getTaxNumberTypes(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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
        6,
        retrievedTimesToContact.size(),
        "The correct number of times to contact was not retrieved");

    retrievedTimesToContact =
        partyReferenceService.getTimesToContact(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(
        6,
        retrievedTimesToContact.size(),
        "The correct number of times to contact was not retrieved");

    retrievedTimesToContact =
        partyReferenceService.getTimesToContact(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

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

    assertEquals(13, retrievedTitles.size(), "The correct number of titles was not retrieved");

    retrievedTitles =
        partyReferenceService.getTitles(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(13, retrievedTitles.size(), "The correct number of titles was not retrieved");

    retrievedTitles =
        partyReferenceService.getTitles(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            PartyReferenceService.DEFAULT_LOCALE_ID);

    assertEquals(12, retrievedTitles.size(), "The correct number of titles was not retrieved");
  }

  /** Test the reference data validity check functionality. */
  @Test
  public void validityTest() throws Exception {
    assertTrue(
        partyReferenceService.isValidAttributeType(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyType.PERSON.code(), "height"));
    assertTrue(
        partyReferenceService.isValidAttributeTypeCategory(
            PartyReferenceService.DEFAULT_TENANT_ID, "anthropometric_measurements"));
    assertTrue(
        partyReferenceService.isValidConsentType(
            PartyReferenceService.DEFAULT_TENANT_ID, "marketing"));
    assertTrue(
        partyReferenceService.isValidContactMechanismPurpose(
            PartyReferenceService.DEFAULT_TENANT_ID, "person", "email_address", "security"));
    assertTrue(
        partyReferenceService.isValidContactMechanismRole(
            PartyReferenceService.DEFAULT_TENANT_ID,
            PartyType.PERSON.code(),
            ContactMechanismType.MOBILE_NUMBER,
            "personal_mobile_number"));
    assertTrue(
        partyReferenceService.isValidContactMechanismType(
            PartyReferenceService.DEFAULT_TENANT_ID, ContactMechanismType.MOBILE_NUMBER));
    assertTrue(
        partyReferenceService.isValidEmploymentStatus(
            PartyReferenceService.DEFAULT_TENANT_ID, "employed"));
    assertTrue(
        partyReferenceService.isValidEmploymentType(
            PartyReferenceService.DEFAULT_TENANT_ID, "employed", "full_time"));
    assertTrue(
        partyReferenceService.isValidExternalReferenceType(
            PartyReferenceService.DEFAULT_TENANT_ID,
            PartyType.PERSON.code(),
            "test_external_reference_type"));
    assertTrue(
        partyReferenceService.isValidGender(PartyReferenceService.DEFAULT_TENANT_ID, "female"));
    assertTrue(
        partyReferenceService.isValidIdentificationType(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyType.PERSON.code(), "passport"));
    assertTrue(
        partyReferenceService.isValidLockType(
            PartyReferenceService.DEFAULT_TENANT_ID, "person", "suspected_fraud"));
    assertTrue(
        partyReferenceService.isValidLockTypeCategory(
            PartyReferenceService.DEFAULT_TENANT_ID, "fraud"));
    assertTrue(
        partyReferenceService.isValidMaritalStatus(
            PartyReferenceService.DEFAULT_TENANT_ID, "married"));
    assertTrue(
        partyReferenceService.isValidMarriageType(
            PartyReferenceService.DEFAULT_TENANT_ID, "married", "anc_with_accrual"));
    // referenceService.isValidMinorType("minor");
    assertTrue(
        partyReferenceService.isValidNextOfKinType(
            PartyReferenceService.DEFAULT_TENANT_ID, "mother"));
    assertTrue(
        partyReferenceService.isValidOccupation(
            PartyReferenceService.DEFAULT_TENANT_ID, "executive"));
    assertTrue(
        partyReferenceService.isValidPhysicalAddressPurpose(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyType.PERSON.code(), "billing"));
    assertTrue(
        partyReferenceService.isValidPhysicalAddressRole(
            PartyReferenceService.DEFAULT_TENANT_ID,
            PartyType.PERSON.code(),
            PhysicalAddressRole.RESIDENTIAL));
    assertTrue(
        partyReferenceService.isValidPhysicalAddressType(
            PartyReferenceService.DEFAULT_TENANT_ID, "complex"));
    assertTrue(
        partyReferenceService.isValidPreferenceType(
            PartyReferenceService.DEFAULT_TENANT_ID,
            PartyType.ORGANIZATION.code(),
            "correspondence_language"));
    assertTrue(
        partyReferenceService.isValidPreferenceTypeCategory(
            PartyReferenceService.DEFAULT_TENANT_ID, "correspondence"));
    assertTrue(
        partyReferenceService.isValidQualificationType(
            PartyReferenceService.DEFAULT_TENANT_ID, "doctoral_degree"));
    assertTrue(partyReferenceService.isValidRace(PartyReferenceService.DEFAULT_TENANT_ID, "white"));
    assertTrue(
        partyReferenceService.isValidResidencePermitType(
            PartyReferenceService.DEFAULT_TENANT_ID, "za_general_work_visa"));
    assertTrue(
        partyReferenceService.isValidResidencyStatus(
            PartyReferenceService.DEFAULT_TENANT_ID, "permanent_resident"));
    assertTrue(
        partyReferenceService.isValidResidentialType(
            PartyReferenceService.DEFAULT_TENANT_ID, "owner"));
    assertTrue(
        partyReferenceService.isValidRolePurpose(
            PartyReferenceService.DEFAULT_TENANT_ID, "test_role_purpose"));
    assertTrue(
        partyReferenceService.isValidRoleType(
            PartyReferenceService.DEFAULT_TENANT_ID, PartyType.PERSON.code(), "test_person_role"));
    assertTrue(
        partyReferenceService.isValidSegment(
            PartyReferenceService.DEFAULT_TENANT_ID, "test_person_segment"));
    assertTrue(
        partyReferenceService.isValidSourceOfFundsType(
            PartyReferenceService.DEFAULT_TENANT_ID, "salary_wages"));
    assertTrue(
        partyReferenceService.isValidStatusType(
            PartyReferenceService.DEFAULT_TENANT_ID, "person", "fraud_investigation"));
    assertTrue(
        partyReferenceService.isValidStatusTypeCategory(
            PartyReferenceService.DEFAULT_TENANT_ID, "fraud"));
    assertTrue(
        partyReferenceService.isValidTaxNumberType(
            PartyReferenceService.DEFAULT_TENANT_ID, "person", "za_income_tax_number"));
    assertTrue(
        partyReferenceService.isValidTimeToContact(
            PartyReferenceService.DEFAULT_TENANT_ID, "anytime"));
    assertTrue(partyReferenceService.isValidTitle(PartyReferenceService.DEFAULT_TENANT_ID, "mrs"));
  }
}

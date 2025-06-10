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

package digital.inception.party.service;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.service.AbstractServiceBase;
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
import digital.inception.party.model.LinkType;
import digital.inception.party.model.LockType;
import digital.inception.party.model.LockTypeCategory;
import digital.inception.party.model.MandataryRole;
import digital.inception.party.model.MandatePropertyType;
import digital.inception.party.model.MandateType;
import digital.inception.party.model.MaritalStatus;
import digital.inception.party.model.MarriageType;
import digital.inception.party.model.MeasurementUnit;
import digital.inception.party.model.NextOfKinType;
import digital.inception.party.model.Occupation;
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
import digital.inception.party.model.ValueType;
import digital.inception.party.persistence.jpa.AssociationPropertyTypeRepository;
import digital.inception.party.persistence.jpa.AssociationTypeRepository;
import digital.inception.party.persistence.jpa.AttributeTypeCategoryRepository;
import digital.inception.party.persistence.jpa.AttributeTypeRepository;
import digital.inception.party.persistence.jpa.ConsentTypeRepository;
import digital.inception.party.persistence.jpa.ContactMechanismPurposeRepository;
import digital.inception.party.persistence.jpa.ContactMechanismRoleRepository;
import digital.inception.party.persistence.jpa.ContactMechanismTypeRepository;
import digital.inception.party.persistence.jpa.EmploymentStatusRepository;
import digital.inception.party.persistence.jpa.EmploymentTypeRepository;
import digital.inception.party.persistence.jpa.ExternalReferenceTypeRepository;
import digital.inception.party.persistence.jpa.FieldOfStudyRepository;
import digital.inception.party.persistence.jpa.GenderRepository;
import digital.inception.party.persistence.jpa.IdentificationTypeRepository;
import digital.inception.party.persistence.jpa.IndustryClassificationCategoryRepository;
import digital.inception.party.persistence.jpa.IndustryClassificationRepository;
import digital.inception.party.persistence.jpa.IndustryClassificationSystemRepository;
import digital.inception.party.persistence.jpa.LinkTypeRepository;
import digital.inception.party.persistence.jpa.LockTypeCategoryRepository;
import digital.inception.party.persistence.jpa.LockTypeRepository;
import digital.inception.party.persistence.jpa.MandataryRoleRepository;
import digital.inception.party.persistence.jpa.MandatePropertyTypeRepository;
import digital.inception.party.persistence.jpa.MandateTypeRepository;
import digital.inception.party.persistence.jpa.MaritalStatusRepository;
import digital.inception.party.persistence.jpa.MarriageTypeRepository;
import digital.inception.party.persistence.jpa.NextOfKinTypeRepository;
import digital.inception.party.persistence.jpa.OccupationRepository;
import digital.inception.party.persistence.jpa.PhysicalAddressPurposeRepository;
import digital.inception.party.persistence.jpa.PhysicalAddressRoleRepository;
import digital.inception.party.persistence.jpa.PhysicalAddressTypeRepository;
import digital.inception.party.persistence.jpa.PreferenceTypeCategoryRepository;
import digital.inception.party.persistence.jpa.PreferenceTypeRepository;
import digital.inception.party.persistence.jpa.QualificationTypeRepository;
import digital.inception.party.persistence.jpa.RaceRepository;
import digital.inception.party.persistence.jpa.ResidencePermitTypeRepository;
import digital.inception.party.persistence.jpa.ResidencyStatusRepository;
import digital.inception.party.persistence.jpa.ResidentialTypeRepository;
import digital.inception.party.persistence.jpa.RolePurposeRepository;
import digital.inception.party.persistence.jpa.RoleTypeAttributeTypeConstraintRepository;
import digital.inception.party.persistence.jpa.RoleTypePreferenceTypeConstraintRepository;
import digital.inception.party.persistence.jpa.RoleTypeRepository;
import digital.inception.party.persistence.jpa.SegmentRepository;
import digital.inception.party.persistence.jpa.SegmentationTypeRepository;
import digital.inception.party.persistence.jpa.SkillTypeRepository;
import digital.inception.party.persistence.jpa.SourceOfFundsTypeRepository;
import digital.inception.party.persistence.jpa.SourceOfWealthTypeRepository;
import digital.inception.party.persistence.jpa.StatusTypeCategoryRepository;
import digital.inception.party.persistence.jpa.StatusTypeRepository;
import digital.inception.party.persistence.jpa.TaxNumberTypeRepository;
import digital.inception.party.persistence.jpa.TimeToContactRepository;
import digital.inception.party.persistence.jpa.TitleRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code PartyReferenceServiceImpl} class provides the Party Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class PartyReferenceServiceImpl extends AbstractServiceBase
    implements PartyReferenceService {

  /** The Association Property Type Repository. */
  private final AssociationPropertyTypeRepository associationPropertyTypeRepository;

  /** The Association Type Repository. */
  private final AssociationTypeRepository associationTypeRepository;

  /** The Party Attribute Type Category Repository. */
  private final AttributeTypeCategoryRepository attributeTypeCategoryRepository;

  /** The Party Attribute Type Repository. */
  private final AttributeTypeRepository attributeTypeRepository;

  /** The Consent Type Repository */
  private final ConsentTypeRepository consentTypeRepository;

  /** The Contact Mechanism Purpose Repository. */
  private final ContactMechanismPurposeRepository contactMechanismPurposeRepository;

  /** The Contact Mechanism Role Repository. */
  private final ContactMechanismRoleRepository contactMechanismRoleRepository;

  /** The Contact Mechanism Type Repository. */
  private final ContactMechanismTypeRepository contactMechanismTypeRepository;

  /** The Employment Status Repository */
  private final EmploymentStatusRepository employmentStatusRepository;

  /** The Employment Type Repository. */
  private final EmploymentTypeRepository employmentTypeRepository;

  /** The External Reference Type Repository. */
  private final ExternalReferenceTypeRepository externalReferenceTypeRepository;

  /** The Field Of Study Repository */
  private final FieldOfStudyRepository fieldOfStudyRepository;

  /** The Gender Repository. */
  private final GenderRepository genderRepository;

  /** The Identification Document Type Repository. */
  private final IdentificationTypeRepository identificationTypeRepository;

  /** The Industry Classification Category Repository. */
  private final IndustryClassificationCategoryRepository industryClassificationCategoryRepository;

  /** The Industry Classification Repository. */
  private final IndustryClassificationRepository industryClassificationRepository;

  /** The Industry Classification System Repository. */
  private final IndustryClassificationSystemRepository industryClassificationSystemRepository;

  /** The Link Type Repository */
  private final LinkTypeRepository linkTypeRepository;

  /** The Lock Type Category Repository. */
  private final LockTypeCategoryRepository lockTypeCategoryRepository;

  /** The Lock Type Repository */
  private final LockTypeRepository lockTypeRepository;

  /** The Mandatary Role Repository. */
  private final MandataryRoleRepository mandataryRoleRepository;

  /** The Mandate Property Type Repository. */
  private final MandatePropertyTypeRepository mandatePropertyTypeRepository;

  /** The Mandate Type Repository. */
  private final MandateTypeRepository mandateTypeRepository;

  /** The Marital Status Repository. */
  private final MaritalStatusRepository maritalStatusRepository;

  /** The Marriage Type Repository. */
  private final MarriageTypeRepository marriageTypeRepository;

  /** The Next Of Kin Type Repository. */
  private final NextOfKinTypeRepository nextOfKinTypeRepository;

  /** The Occupation Repository. */
  private final OccupationRepository occupationRepository;

  /** The Physical Address Purpose Repository. */
  private final PhysicalAddressPurposeRepository physicalAddressPurposeRepository;

  /** The Physical Address Role Repository. */
  private final PhysicalAddressRoleRepository physicalAddressRoleRepository;

  /** The Physical Address Type Repository. */
  private final PhysicalAddressTypeRepository physicalAddressTypeRepository;

  /** The Preference Type Category Repository. */
  private final PreferenceTypeCategoryRepository preferenceTypeCategoryRepository;

  /** The Preference Type Repository */
  private final PreferenceTypeRepository preferenceTypeRepository;

  /** The Qualification Type Repository. */
  private final QualificationTypeRepository qualificationTypeRepository;

  /** The Race Repository. */
  private final RaceRepository raceRepository;

  /** The Residence Permit Type Repository. */
  private final ResidencePermitTypeRepository residencePermitTypeRepository;

  /** The Residency Status Repository. */
  private final ResidencyStatusRepository residencyStatusRepository;

  /** The Residential Type Repository. */
  private final ResidentialTypeRepository residentialTypeRepository;

  /** The Party Role Purpose Repository. */
  private final RolePurposeRepository rolePurposeRepository;

  /** The Role Type Attribute Type Constraint Repository. */
  private final RoleTypeAttributeTypeConstraintRepository roleTypeAttributeTypeConstraintRepository;

  /** The Role Type Preference Type Constraint Repository. */
  private final RoleTypePreferenceTypeConstraintRepository
      roleTypePreferenceTypeConstraintRepository;

  /** The Party Role Type Repository. */
  private final RoleTypeRepository roleTypeRepository;

  /** The Segment Repository. */
  private final SegmentRepository segmentRepository;

  /** The Segmentation Type Repository. */
  private final SegmentationTypeRepository segmentationTypeRepository;

  /** The Skill Type Repository */
  private final SkillTypeRepository skillTypeRepository;

  /** The Source of Funds Type Repository. */
  private final SourceOfFundsTypeRepository sourceOfFundsTypeRepository;

  /** The Source of Wealth Type Repository. */
  private final SourceOfWealthTypeRepository sourceOfWealthTypeRepository;

  /** The Status Type Category Repository. */
  private final StatusTypeCategoryRepository statusTypeCategoryRepository;

  /** The Status Type Repository */
  private final StatusTypeRepository statusTypeRepository;

  /** The Tax Number Type Repository. */
  private final TaxNumberTypeRepository taxNumberTypeRepository;

  /** The Time To Contact Repository. */
  private final TimeToContactRepository timeToContactRepository;

  /** The Title Repository. */
  private final TitleRepository titleRepository;

  /** The internal reference to the Party Reference Service to enable caching. */
  private PartyReferenceService partyReferenceService;

  /**
   * Constructs a new {@code PartyReferenceServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param associationPropertyTypeRepository the Association Property Type Repository
   * @param associationTypeRepository the Association Type Repository
   * @param attributeTypeCategoryRepository the Attribute Type Category Repository
   * @param attributeTypeRepository the Attribute Type Repository
   * @param consentTypeRepository the Consent Type Repository
   * @param contactMechanismPurposeRepository the Contact Mechanism Purpose Repository
   * @param contactMechanismRoleRepository the Contact Mechanism Role Repository
   * @param contactMechanismTypeRepository the Contact Mechanism Type Repository
   * @param employmentStatusRepository the Employment Status Repository
   * @param employmentTypeRepository the Employment Type Repository
   * @param externalReferenceTypeRepository the External Reference Type Repository
   * @param fieldOfStudyRepository the Field Of Study Repository
   * @param genderRepository the Gender Repository
   * @param identificationTypeRepository the Identification Document Type Repository
   * @param industryClassificationRepository the Industry Classification Repository
   * @param industryClassificationCategoryRepository the Industry Classification Category Repository
   * @param industryClassificationSystemRepository the Industry Classification System Repository
   * @param linkTypeRepository the Link Type Repository
   * @param lockTypeCategoryRepository the Lock Type Category Repository
   * @param lockTypeRepository the Lock Type Repository
   * @param mandataryRoleRepository the Mandatary Role Repository
   * @param mandatePropertyTypeRepository the Mandate Property Type Repository
   * @param mandateTypeRepository the Mandate Type Repository
   * @param maritalStatusRepository the Marital Status Repository
   * @param marriageTypeRepository the Marriage Type Repository
   * @param nextOfKinTypeRepository the Next Of Kin Repository
   * @param occupationRepository the Occupation Repository
   * @param physicalAddressPurposeRepository the Physical Address Purpose Repository
   * @param physicalAddressRoleRepository the Physical Address Role Repository
   * @param physicalAddressTypeRepository the Physical Address Type Repository
   * @param preferenceTypeCategoryRepository the Preference Type Category Repository
   * @param preferenceTypeRepository the Preference Type Repository
   * @param qualificationTypeRepository the Qualification Type Repository
   * @param raceRepository the Race Repository
   * @param residencePermitTypeRepository the Residence Permit Type Repository
   * @param residencyStatusRepository the Residency Status Repository
   * @param residentialTypeRepository the Residential Type Repository
   * @param rolePurposeRepository the Party Role Purpose Repository
   * @param roleTypeRepository the Party Role Type Repository
   * @param roleTypeAttributeTypeConstraintRepository the Role Type Attribute Type Constraint
   *     Repository
   * @param roleTypePreferenceTypeConstraintRepository the Role Type Preference Type Constraint
   *     Repository
   * @param segmentationTypeRepository the Segmentation Type Repository
   * @param segmentRepository the Segment Repository
   * @param skillTypeRepository the Skill Type Repository
   * @param sourceOfFundsTypeRepository the Source Of Funds Repository
   * @param sourceOfWealthTypeRepository the Source Of Wealth Repository
   * @param statusTypeCategoryRepository the Status Type Category Repository
   * @param statusTypeRepository the Status Type Repository
   * @param taxNumberTypeRepository the Tax Number Type Repository
   * @param timeToContactRepository the Time To Contact Repository
   * @param titleRepository the Title Repository
   */
  public PartyReferenceServiceImpl(
      ApplicationContext applicationContext,
      AssociationPropertyTypeRepository associationPropertyTypeRepository,
      AssociationTypeRepository associationTypeRepository,
      AttributeTypeCategoryRepository attributeTypeCategoryRepository,
      AttributeTypeRepository attributeTypeRepository,
      ConsentTypeRepository consentTypeRepository,
      ContactMechanismPurposeRepository contactMechanismPurposeRepository,
      ContactMechanismRoleRepository contactMechanismRoleRepository,
      ContactMechanismTypeRepository contactMechanismTypeRepository,
      EmploymentStatusRepository employmentStatusRepository,
      EmploymentTypeRepository employmentTypeRepository,
      ExternalReferenceTypeRepository externalReferenceTypeRepository,
      FieldOfStudyRepository fieldOfStudyRepository,
      GenderRepository genderRepository,
      IdentificationTypeRepository identificationTypeRepository,
      IndustryClassificationRepository industryClassificationRepository,
      IndustryClassificationCategoryRepository industryClassificationCategoryRepository,
      IndustryClassificationSystemRepository industryClassificationSystemRepository,
      LinkTypeRepository linkTypeRepository,
      LockTypeCategoryRepository lockTypeCategoryRepository,
      LockTypeRepository lockTypeRepository,
      MandataryRoleRepository mandataryRoleRepository,
      MandatePropertyTypeRepository mandatePropertyTypeRepository,
      MandateTypeRepository mandateTypeRepository,
      MaritalStatusRepository maritalStatusRepository,
      MarriageTypeRepository marriageTypeRepository,
      NextOfKinTypeRepository nextOfKinTypeRepository,
      OccupationRepository occupationRepository,
      PhysicalAddressPurposeRepository physicalAddressPurposeRepository,
      PhysicalAddressRoleRepository physicalAddressRoleRepository,
      PhysicalAddressTypeRepository physicalAddressTypeRepository,
      PreferenceTypeCategoryRepository preferenceTypeCategoryRepository,
      PreferenceTypeRepository preferenceTypeRepository,
      QualificationTypeRepository qualificationTypeRepository,
      RaceRepository raceRepository,
      ResidencePermitTypeRepository residencePermitTypeRepository,
      ResidencyStatusRepository residencyStatusRepository,
      ResidentialTypeRepository residentialTypeRepository,
      RolePurposeRepository rolePurposeRepository,
      RoleTypeRepository roleTypeRepository,
      RoleTypeAttributeTypeConstraintRepository roleTypeAttributeTypeConstraintRepository,
      RoleTypePreferenceTypeConstraintRepository roleTypePreferenceTypeConstraintRepository,
      SegmentationTypeRepository segmentationTypeRepository,
      SegmentRepository segmentRepository,
      SkillTypeRepository skillTypeRepository,
      SourceOfFundsTypeRepository sourceOfFundsTypeRepository,
      SourceOfWealthTypeRepository sourceOfWealthTypeRepository,
      StatusTypeCategoryRepository statusTypeCategoryRepository,
      StatusTypeRepository statusTypeRepository,
      TaxNumberTypeRepository taxNumberTypeRepository,
      TimeToContactRepository timeToContactRepository,
      TitleRepository titleRepository) {
    super(applicationContext);

    this.associationPropertyTypeRepository = associationPropertyTypeRepository;
    this.associationTypeRepository = associationTypeRepository;
    this.attributeTypeCategoryRepository = attributeTypeCategoryRepository;
    this.attributeTypeRepository = attributeTypeRepository;
    this.consentTypeRepository = consentTypeRepository;
    this.contactMechanismPurposeRepository = contactMechanismPurposeRepository;
    this.contactMechanismRoleRepository = contactMechanismRoleRepository;
    this.contactMechanismTypeRepository = contactMechanismTypeRepository;
    this.employmentStatusRepository = employmentStatusRepository;
    this.employmentTypeRepository = employmentTypeRepository;
    this.externalReferenceTypeRepository = externalReferenceTypeRepository;
    this.fieldOfStudyRepository = fieldOfStudyRepository;
    this.genderRepository = genderRepository;
    this.identificationTypeRepository = identificationTypeRepository;
    this.industryClassificationRepository = industryClassificationRepository;
    this.industryClassificationCategoryRepository = industryClassificationCategoryRepository;
    this.industryClassificationSystemRepository = industryClassificationSystemRepository;
    this.linkTypeRepository = linkTypeRepository;
    this.lockTypeCategoryRepository = lockTypeCategoryRepository;
    this.lockTypeRepository = lockTypeRepository;
    this.mandataryRoleRepository = mandataryRoleRepository;
    this.mandatePropertyTypeRepository = mandatePropertyTypeRepository;
    this.mandateTypeRepository = mandateTypeRepository;
    this.maritalStatusRepository = maritalStatusRepository;
    this.marriageTypeRepository = marriageTypeRepository;
    this.nextOfKinTypeRepository = nextOfKinTypeRepository;
    this.occupationRepository = occupationRepository;
    this.physicalAddressPurposeRepository = physicalAddressPurposeRepository;
    this.physicalAddressRoleRepository = physicalAddressRoleRepository;
    this.physicalAddressTypeRepository = physicalAddressTypeRepository;
    this.preferenceTypeCategoryRepository = preferenceTypeCategoryRepository;
    this.preferenceTypeRepository = preferenceTypeRepository;
    this.qualificationTypeRepository = qualificationTypeRepository;
    this.raceRepository = raceRepository;
    this.residencePermitTypeRepository = residencePermitTypeRepository;
    this.residencyStatusRepository = residencyStatusRepository;
    this.residentialTypeRepository = residentialTypeRepository;
    this.rolePurposeRepository = rolePurposeRepository;
    this.roleTypeRepository = roleTypeRepository;
    this.roleTypeAttributeTypeConstraintRepository = roleTypeAttributeTypeConstraintRepository;
    this.roleTypePreferenceTypeConstraintRepository = roleTypePreferenceTypeConstraintRepository;
    this.segmentationTypeRepository = segmentationTypeRepository;
    this.segmentRepository = segmentRepository;
    this.skillTypeRepository = skillTypeRepository;
    this.sourceOfFundsTypeRepository = sourceOfFundsTypeRepository;
    this.sourceOfWealthTypeRepository = sourceOfWealthTypeRepository;
    this.statusTypeCategoryRepository = statusTypeCategoryRepository;
    this.statusTypeRepository = statusTypeRepository;
    this.taxNumberTypeRepository = taxNumberTypeRepository;
    this.timeToContactRepository = timeToContactRepository;
    this.titleRepository = titleRepository;
  }

  @Override
  public Optional<AssociationPropertyType> getAssociationPropertyType(
      UUID tenantId, String associationTypeCode, String associationPropertyTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(associationPropertyTypeCode)) {
      return Optional.empty();
    }

    return getPartyReferenceService().getAssociationPropertyTypes().stream()
        .filter(
            associationPropertyType ->
                (associationPropertyType.getTenantId() == null
                        || associationPropertyType.getTenantId().equals(tenantId))
                    && Objects.equals(
                        associationPropertyType.getAssociationType(), associationTypeCode)
                    && Objects.equals(
                        associationPropertyType.getCode(), associationPropertyTypeCode))
        .findFirst();
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'associationPropertyTypes.' + #localeId")
  public List<AssociationPropertyType> getAssociationPropertyTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getAssociationPropertyTypes().stream()
        .filter(
            associationPropertyType ->
                (associationPropertyType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(associationPropertyType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'associationPropertyTypes.ALL'")
  public List<AssociationPropertyType> getAssociationPropertyTypes()
      throws ServiceUnavailableException {
    try {
      return associationPropertyTypeRepository.getAllAssociationPropertyTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the association property type reference data", e);
    }
  }

  @Override
  public List<AssociationPropertyType> getAssociationPropertyTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getAssociationPropertyTypes(localeId).stream()
        .filter(
            associationPropertyType ->
                (associationPropertyType.getTenantId() == null
                    || (Objects.equals(associationPropertyType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<AssociationType> getAssociationType(UUID tenantId, String associationTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(associationTypeCode)) {
      return Optional.empty();
    }

    return getPartyReferenceService().getAssociationTypes().stream()
        .filter(
            associationType ->
                (associationType.getTenantId() == null
                        || Objects.equals(associationType.getTenantId(), tenantId))
                    && Objects.equals(associationType.getCode(), associationTypeCode))
        .findFirst();
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'associationTypes.' + #localeId")
  public List<AssociationType> getAssociationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getAssociationTypes().stream()
        .filter(
            associationType ->
                (associationType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(associationType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'associationTypes.ALL'")
  public List<AssociationType> getAssociationTypes() throws ServiceUnavailableException {
    try {
      return associationTypeRepository.getAllAssociationTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the association type reference data", e);
    }
  }

  @Override
  public List<AssociationType> getAssociationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getAssociationTypes(localeId).stream()
        .filter(
            associationType ->
                (associationType.getTenantId() == null
                    || (Objects.equals(associationType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<AttributeType> getAttributeType(
      UUID tenantId, String partyTypeCode, String attributeTypeCode)
      throws ServiceUnavailableException {
    return getPartyReferenceService().getAttributeTypes().stream()
        .filter(
            attributeType ->
                (attributeType.getTenantId() == null
                        || attributeType.getTenantId().equals(tenantId))
                    && Objects.equals(attributeType.getCode(), attributeTypeCode)
                    && attributeType.isValidForPartyType(partyTypeCode))
        .findFirst();
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypeCategories.' + #localeId")
  public List<AttributeTypeCategory> getAttributeTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getAttributeTypeCategories().stream()
        .filter(
            attributeTypeCategory ->
                (attributeTypeCategory.getLocaleId() == null
                    || localeId.equalsIgnoreCase(attributeTypeCategory.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypeCategories.ALL'")
  public List<AttributeTypeCategory> getAttributeTypeCategories()
      throws ServiceUnavailableException {
    try {
      return attributeTypeCategoryRepository.getAllAttributeTypeCategories();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the attribute type category reference data", e);
    }
  }

  @Override
  public List<AttributeTypeCategory> getAttributeTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getAttributeTypeCategories(localeId).stream()
        .filter(
            attributeTypeCategory ->
                (attributeTypeCategory.getTenantId() == null
                    || (Objects.equals(attributeTypeCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "attributeTypesValueTypes", key = "#attributeTypeCode")
  public Optional<ValueType> getAttributeTypeValueType(String attributeTypeCode)
      throws ServiceUnavailableException {
    try {
      return getPartyReferenceService().getAttributeTypes().stream()
          .filter(attributeType -> Objects.equals(attributeType.getCode(), attributeTypeCode))
          .findFirst()
          .map(AttributeType::getValueType);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the value type for the attribute type (" + attributeTypeCode + ")",
          e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypes.' + #localeId")
  public List<AttributeType> getAttributeTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getAttributeTypes().stream()
        .filter(
            attributeType ->
                (attributeType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(attributeType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'attributeTypes.ALL'")
  public List<AttributeType> getAttributeTypes() throws ServiceUnavailableException {
    try {
      return attributeTypeRepository.getAllAttributeTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the attribute type reference data", e);
    }
  }

  @Override
  public List<AttributeType> getAttributeTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getAttributeTypes(localeId).stream()
        .filter(
            attributeType ->
                (attributeType.getTenantId() == null
                    || (Objects.equals(attributeType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'consentTypes.' + #localeId")
  public List<ConsentType> getConsentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getConsentTypes().stream()
        .filter(
            consentType ->
                (consentType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(consentType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'consentTypes.ALL'")
  public List<ConsentType> getConsentTypes() throws ServiceUnavailableException {
    try {
      return consentTypeRepository.getAllConsentTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the consent type reference data", e);
    }
  }

  @Override
  public List<ConsentType> getConsentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getConsentTypes(localeId).stream()
        .filter(
            consentType ->
                (consentType.getTenantId() == null
                    || (Objects.equals(consentType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismPurposes.' + #localeId")
  public List<ContactMechanismPurpose> getContactMechanismPurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getContactMechanismPurposes().stream()
        .filter(
            contactMechanismPurpose ->
                (contactMechanismPurpose.getLocaleId() == null
                    || localeId.equalsIgnoreCase(contactMechanismPurpose.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismPurposes.ALL'")
  public List<ContactMechanismPurpose> getContactMechanismPurposes()
      throws ServiceUnavailableException {
    try {
      return contactMechanismPurposeRepository.getAllContactMechanismPurposes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism purpose reference data", e);
    }
  }

  @Override
  public List<ContactMechanismPurpose> getContactMechanismPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getContactMechanismPurposes(localeId).stream()
        .filter(
            contactMechanismPurpose ->
                (contactMechanismPurpose.getTenantId() == null
                    || (Objects.equals(contactMechanismPurpose.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<ContactMechanismRole> getContactMechanismRole(
      UUID tenantId,
      String partyTypeCode,
      String contactMechanismTypeCode,
      String contactMechanismRoleCode)
      throws ServiceUnavailableException {
    return getPartyReferenceService().getContactMechanismRoles().stream()
        .filter(
            contactMechanismRole ->
                (contactMechanismRole.getTenantId() == null
                        || Objects.equals(contactMechanismRole.getTenantId(), tenantId))
                    && contactMechanismRole.isValidForPartyType(partyTypeCode)
                    && contactMechanismRole
                        .getContactMechanismType()
                        .equals(contactMechanismTypeCode)
                    && Objects.equals(contactMechanismRole.getCode(), contactMechanismRoleCode))
        .findFirst();
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismRoles.' + #localeId")
  public List<ContactMechanismRole> getContactMechanismRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getContactMechanismRoles().stream()
        .filter(
            contactMechanismRole ->
                (contactMechanismRole.getLocaleId() == null
                    || localeId.equalsIgnoreCase(contactMechanismRole.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismRoles.ALL'")
  public List<ContactMechanismRole> getContactMechanismRoles() throws ServiceUnavailableException {
    try {
      return contactMechanismRoleRepository.getAllContactMechanismRoles();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism role reference data", e);
    }
  }

  @Override
  public List<ContactMechanismRole> getContactMechanismRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getContactMechanismRoles(localeId).stream()
        .filter(
            contactMechanismRole ->
                (contactMechanismRole.getTenantId() == null
                    || (Objects.equals(contactMechanismRole.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<ContactMechanismType> getContactMechanismType(
      UUID tenantId, String contactMechanismTypeCode) throws ServiceUnavailableException {
    return getPartyReferenceService().getContactMechanismTypes().stream()
        .filter(
            contactMechanismType ->
                (contactMechanismType.getTenantId() == null
                        || Objects.equals(contactMechanismType.getTenantId(), tenantId))
                    && Objects.equals(contactMechanismType.getCode(), contactMechanismTypeCode))
        .findFirst();
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismTypes.' + #localeId")
  public List<ContactMechanismType> getContactMechanismTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getContactMechanismTypes().stream()
        .filter(
            contactMechanismType ->
                (contactMechanismType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(contactMechanismType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'contactMechanismTypes.ALL'")
  public List<ContactMechanismType> getContactMechanismTypes() throws ServiceUnavailableException {
    try {
      return contactMechanismTypeRepository.getAllContactMechanismTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the contact mechanism type reference data", e);
    }
  }

  @Override
  public List<ContactMechanismType> getContactMechanismTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getContactMechanismTypes(localeId).stream()
        .filter(
            contactMechanismType ->
                (contactMechanismType.getTenantId() == null
                    || (Objects.equals(contactMechanismType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentStatuses.' + #localeId")
  public List<EmploymentStatus> getEmploymentStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getEmploymentStatuses().stream()
        .filter(
            employmentStatus ->
                (employmentStatus.getLocaleId() == null
                    || localeId.equalsIgnoreCase(employmentStatus.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentStatuses.ALL'")
  public List<EmploymentStatus> getEmploymentStatuses() throws ServiceUnavailableException {
    try {
      return employmentStatusRepository.getAllEmploymentStatuses();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the employment status reference data", e);
    }
  }

  @Override
  public List<EmploymentStatus> getEmploymentStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getEmploymentStatuses(localeId).stream()
        .filter(
            employmentStatus ->
                (employmentStatus.getTenantId() == null
                    || (Objects.equals(employmentStatus.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentTypes.' + #localeId")
  public List<EmploymentType> getEmploymentTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getEmploymentTypes().stream()
        .filter(
            employmentType ->
                (employmentType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(employmentType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'employmentTypes.ALL'")
  public List<EmploymentType> getEmploymentTypes() throws ServiceUnavailableException {
    try {
      return employmentTypeRepository.getAllEmploymentTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the employment type reference data", e);
    }
  }

  @Override
  public List<EmploymentType> getEmploymentTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getEmploymentTypes(localeId).stream()
        .filter(
            employmentType ->
                (employmentType.getTenantId() == null
                    || (Objects.equals(employmentType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'externalReferenceTypes.' + #localeId")
  public List<ExternalReferenceType> getExternalReferenceTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getExternalReferenceTypes().stream()
        .filter(
            externalReferenceType ->
                (externalReferenceType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(externalReferenceType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'externalReferenceTypes.ALL'")
  public List<ExternalReferenceType> getExternalReferenceTypes()
      throws ServiceUnavailableException {
    try {
      return externalReferenceTypeRepository.getAllExternalReferenceTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the external reference type reference data", e);
    }
  }

  @Override
  public List<ExternalReferenceType> getExternalReferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getExternalReferenceTypes(localeId).stream()
        .filter(
            externalReferenceType ->
                (externalReferenceType.getTenantId() == null
                    || (Objects.equals(externalReferenceType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'fieldsOfStudy.' + #localeId")
  public List<FieldOfStudy> getFieldsOfStudy(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getFieldsOfStudy().stream()
        .filter(
            fieldOfStudy ->
                (fieldOfStudy.getLocaleId() == null
                    || localeId.equalsIgnoreCase(fieldOfStudy.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'fieldsOfStudy.ALL'")
  public List<FieldOfStudy> getFieldsOfStudy() throws ServiceUnavailableException {
    try {
      return fieldOfStudyRepository.getAllFieldsOfStudy();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the fields of study reference data", e);
    }
  }

  @Override
  public List<FieldOfStudy> getFieldsOfStudy(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getFieldsOfStudy(localeId).stream()
        .filter(
            fieldOfStudy ->
                (fieldOfStudy.getTenantId() == null
                    || (Objects.equals(fieldOfStudy.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'genders.' + #localeId")
  public List<Gender> getGenders(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getGenders().stream()
        .filter(
            gender ->
                (gender.getLocaleId() == null || localeId.equalsIgnoreCase(gender.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'genders.ALL'")
  public List<Gender> getGenders() throws ServiceUnavailableException {
    try {
      return genderRepository.getAllGenders();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the gender reference data", e);
    }
  }

  @Override
  public List<Gender> getGenders(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getGenders(localeId).stream()
        .filter(
            gender ->
                (gender.getTenantId() == null || (Objects.equals(gender.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'identificationTypes.' + #localeId")
  public List<IdentificationType> getIdentificationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getIdentificationTypes().stream()
        .filter(
            identificationType ->
                (identificationType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(identificationType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'identificationTypes.ALL'")
  public List<IdentificationType> getIdentificationTypes() throws ServiceUnavailableException {
    try {
      return identificationTypeRepository.getAllIdentificationTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the identification type reference data", e);
    }
  }

  @Override
  public List<IdentificationType> getIdentificationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getIdentificationTypes(localeId).stream()
        .filter(
            identificationType ->
                (identificationType.getTenantId() == null
                    || (Objects.equals(identificationType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public List<IndustryClassificationCategory> getIndustryClassificationCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getIndustryClassificationCategories().stream()
        .filter(
            industryClassificationCategory ->
                (industryClassificationCategory.getLocaleId() == null
                    || localeId.equalsIgnoreCase(industryClassificationCategory.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  public List<IndustryClassificationCategory> getIndustryClassificationCategories(
      UUID tenantId, String localeId) throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getIndustryClassificationCategories(localeId).stream()
        .filter(
            industryClassificationCategory ->
                (industryClassificationCategory.getTenantId() == null
                    || (Objects.equals(industryClassificationCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public List<IndustryClassificationCategory> getIndustryClassificationCategories()
      throws ServiceUnavailableException {
    try {
      return industryClassificationCategoryRepository.getAllIndustryClassificationCategories();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the industry classification category reference data", e);
    }
  }

  @Override
  public List<IndustryClassificationSystem> getIndustryClassificationSystems(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getIndustryClassificationSystems().stream()
        .filter(
            industryClassificationSystem ->
                (industryClassificationSystem.getLocaleId() == null
                    || localeId.equalsIgnoreCase(industryClassificationSystem.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  public List<IndustryClassificationSystem> getIndustryClassificationSystems(
      UUID tenantId, String localeId) throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getIndustryClassificationSystems(localeId).stream()
        .filter(
            industryClassificationSystem ->
                (industryClassificationSystem.getTenantId() == null
                    || (Objects.equals(industryClassificationSystem.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public List<IndustryClassificationSystem> getIndustryClassificationSystems()
      throws ServiceUnavailableException {
    try {
      return industryClassificationSystemRepository.getAllIndustryClassificationSystems();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the industry classification system reference data", e);
    }
  }

  @Override
  public List<IndustryClassification> getIndustryClassifications(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getIndustryClassifications().stream()
        .filter(
            industryClassification ->
                (industryClassification.getLocaleId() == null
                    || localeId.equalsIgnoreCase(industryClassification.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  public List<IndustryClassification> getIndustryClassifications(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getIndustryClassifications(localeId).stream()
        .filter(
            industryClassification ->
                (industryClassification.getTenantId() == null
                    || (Objects.equals(industryClassification.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public List<IndustryClassification> getIndustryClassifications()
      throws ServiceUnavailableException {
    try {
      return industryClassificationRepository.getAllIndustryClassifications();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the industry classification reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'linkTypes.' + #localeId")
  public List<LinkType> getLinkTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getLinkTypes().stream()
        .filter(
            linkType ->
                (linkType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(linkType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'linkTypes.ALL'")
  public List<LinkType> getLinkTypes() throws ServiceUnavailableException {
    try {
      return linkTypeRepository.getAllLinkTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the link type reference data", e);
    }
  }

  @Override
  public List<LinkType> getLinkTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getLinkTypes(localeId).stream()
        .filter(
            linkType ->
                (linkType.getTenantId() == null
                    || (Objects.equals(linkType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypeCategories.' + #localeId")
  public List<LockTypeCategory> getLockTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getLockTypeCategories().stream()
        .filter(
            lockTypeCategory ->
                (lockTypeCategory.getLocaleId() == null
                    || localeId.equalsIgnoreCase(lockTypeCategory.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypeCategories.ALL'")
  public List<LockTypeCategory> getLockTypeCategories() throws ServiceUnavailableException {
    try {
      return lockTypeCategoryRepository.getAllLockTypeCategories();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the lock type category reference data", e);
    }
  }

  @Override
  public List<LockTypeCategory> getLockTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getLockTypeCategories(localeId).stream()
        .filter(
            lockTypeCategory ->
                (lockTypeCategory.getTenantId() == null
                    || (Objects.equals(lockTypeCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypes.' + #localeId")
  public List<LockType> getLockTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getLockTypes().stream()
        .filter(
            lockType ->
                (lockType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(lockType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'lockTypes.ALL'")
  public List<LockType> getLockTypes() throws ServiceUnavailableException {
    try {
      return lockTypeRepository.getAllLockTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the lock type reference data", e);
    }
  }

  @Override
  public List<LockType> getLockTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getLockTypes(localeId).stream()
        .filter(
            lockType ->
                (lockType.getTenantId() == null
                    || (Objects.equals(lockType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'mandataryRoles.' + #localeId")
  public List<MandataryRole> getMandataryRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getMandataryRoles().stream()
        .filter(
            mandataryRole ->
                (mandataryRole.getLocaleId() == null
                    || localeId.equalsIgnoreCase(mandataryRole.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'mandataryRoles.ALL'")
  public List<MandataryRole> getMandataryRoles() throws ServiceUnavailableException {
    try {
      return mandataryRoleRepository.getAllMandataryRoles();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the mandatary role reference data", e);
    }
  }

  @Override
  public List<MandataryRole> getMandataryRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getMandataryRoles(localeId).stream()
        .filter(
            mandataryRole ->
                (mandataryRole.getTenantId() == null
                    || (Objects.equals(mandataryRole.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<MandatePropertyType> getMandatePropertyType(
      UUID tenantId, String mandateTypeCode, String mandatePropertyTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(mandatePropertyTypeCode)) {
      return Optional.empty();
    }

    return getPartyReferenceService().getMandatePropertyTypes().stream()
        .filter(
            mandatePropertyType ->
                (mandatePropertyType.getTenantId() == null
                        || mandatePropertyType.getTenantId().equals(tenantId))
                    && Objects.equals(mandatePropertyType.getMandateType(), mandateTypeCode)
                    && Objects.equals(mandatePropertyType.getCode(), mandatePropertyTypeCode))
        .findFirst();
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'mandatePropertyTypes.' + #localeId")
  public List<MandatePropertyType> getMandatePropertyTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getMandatePropertyTypes().stream()
        .filter(
            mandatePropertyType ->
                (mandatePropertyType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(mandatePropertyType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'mandatePropertyTypes.ALL'")
  public List<MandatePropertyType> getMandatePropertyTypes() throws ServiceUnavailableException {
    try {
      return mandatePropertyTypeRepository.getAllMandatePropertyTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the mandate property type reference data", e);
    }
  }

  @Override
  public List<MandatePropertyType> getMandatePropertyTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getMandatePropertyTypes(localeId).stream()
        .filter(
            mandatePropertyType ->
                (mandatePropertyType.getTenantId() == null
                    || (Objects.equals(mandatePropertyType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'mandateTypes.' + #localeId")
  public List<MandateType> getMandateTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getMandateTypes().stream()
        .filter(
            mandateType ->
                (mandateType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(mandateType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'mandateTypes.ALL'")
  public List<MandateType> getMandateTypes() throws ServiceUnavailableException {
    try {
      return mandateTypeRepository.getAllMandateTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the mandate type reference data", e);
    }
  }

  @Override
  public List<MandateType> getMandateTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getMandateTypes(localeId).stream()
        .filter(
            mandateType ->
                (mandateType.getTenantId() == null
                    || (Objects.equals(mandateType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'maritalStatuses.' + #localeId")
  public List<MaritalStatus> getMaritalStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getMaritalStatuses().stream()
        .filter(
            maritalStatus ->
                (maritalStatus.getLocaleId() == null
                    || localeId.equalsIgnoreCase(maritalStatus.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'maritalStatuses.ALL'")
  public List<MaritalStatus> getMaritalStatuses() throws ServiceUnavailableException {
    try {
      return maritalStatusRepository.getAllMaritalStatuses();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the marital status reference data", e);
    }
  }

  @Override
  public List<MaritalStatus> getMaritalStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getMaritalStatuses(localeId).stream()
        .filter(
            maritalStatus ->
                (maritalStatus.getTenantId() == null
                    || (Objects.equals(maritalStatus.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'marriageTypes.' + #localeId")
  public List<MarriageType> getMarriageTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getMarriageTypes().stream()
        .filter(
            marriageType ->
                (marriageType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(marriageType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'marriageTypes.ALL'")
  public List<MarriageType> getMarriageTypes() throws ServiceUnavailableException {
    try {
      return marriageTypeRepository.getAllMarriageTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the marriage type reference data", e);
    }
  }

  @Override
  public List<MarriageType> getMarriageTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getMarriageTypes(localeId).stream()
        .filter(
            marriageType ->
                (marriageType.getTenantId() == null
                    || (Objects.equals(marriageType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'nextOfKinTypes.' + #localeId")
  public List<NextOfKinType> getNextOfKinTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getNextOfKinTypes().stream()
        .filter(
            nextOfKinType ->
                (nextOfKinType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(nextOfKinType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'nextOfKinTypes.ALL'")
  public List<NextOfKinType> getNextOfKinTypes() throws ServiceUnavailableException {
    try {
      return nextOfKinTypeRepository.getAllNextOfKinTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the next of kin type reference data", e);
    }
  }

  @Override
  public List<NextOfKinType> getNextOfKinTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getNextOfKinTypes(localeId).stream()
        .filter(
            nextOfKinType ->
                (nextOfKinType.getTenantId() == null
                    || (Objects.equals(nextOfKinType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'occupations.' + #localeId")
  public List<Occupation> getOccupations(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getOccupations().stream()
        .filter(
            occupation ->
                (occupation.getLocaleId() == null
                    || localeId.equalsIgnoreCase(occupation.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'occupations.ALL'")
  public List<Occupation> getOccupations() throws ServiceUnavailableException {
    try {
      return occupationRepository.getAllOccupations();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the occupation reference data", e);
    }
  }

  @Override
  public List<Occupation> getOccupations(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getOccupations(localeId).stream()
        .filter(
            occupation ->
                (occupation.getTenantId() == null
                    || (Objects.equals(occupation.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressPurposes.' + #localeId")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getPhysicalAddressPurposes().stream()
        .filter(
            physicalAddressPurpose ->
                (physicalAddressPurpose.getLocaleId() == null
                    || localeId.equalsIgnoreCase(physicalAddressPurpose.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressPurposes.ALL'")
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes()
      throws ServiceUnavailableException {
    try {
      return physicalAddressPurposeRepository.getAllPhysicalAddressPurposes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address purpose reference data", e);
    }
  }

  @Override
  public List<PhysicalAddressPurpose> getPhysicalAddressPurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getPhysicalAddressPurposes(localeId).stream()
        .filter(
            physicalAddressPurpose ->
                (physicalAddressPurpose.getTenantId() == null
                    || (Objects.equals(physicalAddressPurpose.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressRoles.' + #localeId")
  public List<PhysicalAddressRole> getPhysicalAddressRoles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getPhysicalAddressRoles().stream()
        .filter(
            physicalAddressRole ->
                (physicalAddressRole.getLocaleId() == null
                    || localeId.equalsIgnoreCase(physicalAddressRole.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressRoles.ALL'")
  public List<PhysicalAddressRole> getPhysicalAddressRoles() throws ServiceUnavailableException {
    try {
      return physicalAddressRoleRepository.getAllPhysicalAddressRoles();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address role reference data", e);
    }
  }

  @Override
  public List<PhysicalAddressRole> getPhysicalAddressRoles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getPhysicalAddressRoles(localeId).stream()
        .filter(
            physicalAddressRole ->
                (physicalAddressRole.getTenantId() == null
                    || (Objects.equals(physicalAddressRole.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressTypes.' + #localeId")
  public List<PhysicalAddressType> getPhysicalAddressTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getPhysicalAddressTypes().stream()
        .filter(
            physicalAddressType ->
                (physicalAddressType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(physicalAddressType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'physicalAddressTypes.ALL'")
  public List<PhysicalAddressType> getPhysicalAddressTypes() throws ServiceUnavailableException {
    try {
      return physicalAddressTypeRepository.getAllPhysicalAddressTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the physical address type reference data", e);
    }
  }

  @Override
  public List<PhysicalAddressType> getPhysicalAddressTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getPhysicalAddressTypes(localeId).stream()
        .filter(
            physicalAddressType ->
                (physicalAddressType.getTenantId() == null
                    || (Objects.equals(physicalAddressType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<PreferenceType> getPreferenceType(
      UUID tenantId, String partyTypeCode, String preferenceTypeCode)
      throws ServiceUnavailableException {
    return getPartyReferenceService().getPreferenceTypes().stream()
        .filter(
            preferenceType ->
                (preferenceType.getTenantId() == null
                        || preferenceType.getTenantId().equals(tenantId))
                    && Objects.equals(preferenceType.getCode(), preferenceTypeCode)
                    && preferenceType.isValidForPartyType(partyTypeCode))
        .findFirst();
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypeCategories.' + #localeId")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getPreferenceTypeCategories().stream()
        .filter(
            preferenceTypeCategory ->
                (preferenceTypeCategory.getLocaleId() == null
                    || localeId.equalsIgnoreCase(preferenceTypeCategory.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypeCategories.ALL'")
  public List<PreferenceTypeCategory> getPreferenceTypeCategories()
      throws ServiceUnavailableException {
    try {
      return preferenceTypeCategoryRepository.getAllPreferenceTypeCategories();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the preference type category reference data", e);
    }
  }

  @Override
  public List<PreferenceTypeCategory> getPreferenceTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getPreferenceTypeCategories(localeId).stream()
        .filter(
            preferenceTypeCategory ->
                (preferenceTypeCategory.getTenantId() == null
                    || (Objects.equals(preferenceTypeCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypes.' + #localeId")
  public List<PreferenceType> getPreferenceTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getPreferenceTypes().stream()
        .filter(
            preferenceType ->
                (preferenceType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(preferenceType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'preferenceTypes.ALL'")
  public List<PreferenceType> getPreferenceTypes() throws ServiceUnavailableException {
    try {
      return preferenceTypeRepository.getAllPreferenceTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the preference type reference data", e);
    }
  }

  @Override
  public List<PreferenceType> getPreferenceTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getPreferenceTypes(localeId).stream()
        .filter(
            preferenceType ->
                (preferenceType.getTenantId() == null
                    || (Objects.equals(preferenceType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'qualificationTypes.' + #localeId")
  public List<QualificationType> getQualificationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getQualificationTypes().stream()
        .filter(
            qualificationType ->
                (qualificationType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(qualificationType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'qualificationTypes.ALL'")
  public List<QualificationType> getQualificationTypes() throws ServiceUnavailableException {
    try {
      return qualificationTypeRepository.getAllQualificationTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the qualification type reference data", e);
    }
  }

  @Override
  public List<QualificationType> getQualificationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getQualificationTypes(localeId).stream()
        .filter(
            qualificationType ->
                (qualificationType.getTenantId() == null
                    || (Objects.equals(qualificationType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'races.' + #localeId")
  public List<Race> getRaces(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getRaces().stream()
        .filter(
            race -> (race.getLocaleId() == null || localeId.equalsIgnoreCase(race.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'races.ALL'")
  public List<Race> getRaces() throws ServiceUnavailableException {
    try {
      return raceRepository.getAllRaces();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the race reference data", e);
    }
  }

  @Override
  public List<Race> getRaces(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getRaces(localeId).stream()
        .filter(
            race -> (race.getTenantId() == null || (Objects.equals(race.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'residencePermitTypes.' + #localeId")
  public List<ResidencePermitType> getResidencePermitTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getResidencePermitTypes().stream()
        .filter(
            residencePermitType ->
                (residencePermitType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(residencePermitType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'residencePermitTypes.ALL'")
  public List<ResidencePermitType> getResidencePermitTypes() throws ServiceUnavailableException {
    try {
      return residencePermitTypeRepository.getAllResidencePermitTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residence permit type reference data", e);
    }
  }

  @Override
  public List<ResidencePermitType> getResidencePermitTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getResidencePermitTypes(localeId).stream()
        .filter(
            residencePermitType ->
                (residencePermitType.getTenantId() == null
                    || (Objects.equals(residencePermitType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'residencyStatuses.' + #localeId")
  public List<ResidencyStatus> getResidencyStatuses(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getResidencyStatuses().stream()
        .filter(
            residencyStatus ->
                (residencyStatus.getLocaleId() == null
                    || localeId.equalsIgnoreCase(residencyStatus.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'residencyStatuses.ALL'")
  public List<ResidencyStatus> getResidencyStatuses() throws ServiceUnavailableException {
    try {
      return residencyStatusRepository.getAllResidencyStatuses();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residency status reference data", e);
    }
  }

  @Override
  public List<ResidencyStatus> getResidencyStatuses(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getResidencyStatuses(localeId).stream()
        .filter(
            residencyStatus ->
                (residencyStatus.getTenantId() == null
                    || (Objects.equals(residencyStatus.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'residentialTypes.' + #localeId")
  public List<ResidentialType> getResidentialTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getResidentialTypes().stream()
        .filter(
            residentialType ->
                (residentialType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(residentialType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'residentialTypes.ALL'")
  public List<ResidentialType> getResidentialTypes() throws ServiceUnavailableException {
    try {
      return residentialTypeRepository.getAllResidentialTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the residential type reference data", e);
    }
  }

  @Override
  public List<ResidentialType> getResidentialTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getResidentialTypes(localeId).stream()
        .filter(
            residentialType ->
                (residentialType.getTenantId() == null
                    || (Objects.equals(residentialType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'rolePurposes.' + #localeId")
  public List<RolePurpose> getRolePurposes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getRolePurposes().stream()
        .filter(
            rolePurpose ->
                (rolePurpose.getLocaleId() == null
                    || localeId.equalsIgnoreCase(rolePurpose.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'rolePurposes.ALL'")
  public List<RolePurpose> getRolePurposes() throws ServiceUnavailableException {
    try {
      return rolePurposeRepository.getAllRolePurposes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role purpose reference data", e);
    }
  }

  @Override
  public List<RolePurpose> getRolePurposes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getRolePurposes(localeId).stream()
        .filter(
            rolePurpose ->
                (rolePurpose.getTenantId() == null
                    || (Objects.equals(rolePurpose.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypeAttributeTypeConstraints.ALL'")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints()
      throws ServiceUnavailableException {
    try {
      return roleTypeAttributeTypeConstraintRepository.findAll(
          Sort.by(Direction.ASC, "roleType", "attributeType"));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type attribute type constraint", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypeAttributeTypeConstraints.' + #roleType")
  public List<RoleTypeAttributeTypeConstraint> getRoleTypeAttributeTypeConstraints(String roleType)
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      if (StringUtils.hasText(roleType)) {
        return roleTypeAttributeTypeConstraintRepository.findByRoleTypeIgnoreCase(
            roleType, Sort.by(Direction.ASC, "roleType", "attributeType"));
      } else {
        return roleTypeAttributeTypeConstraintRepository.findAll(
            Sort.by(Direction.ASC, "roleType", "attributeType"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type attribute type constraint", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypePreferenceTypeConstraints.ALL'")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints()
      throws ServiceUnavailableException {
    try {
      return roleTypePreferenceTypeConstraintRepository.findAll(
          Sort.by(Direction.ASC, "roleType", "preferenceType"));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type preference type constraint", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypePreferenceTypeConstraints.' + #roleType")
  public List<RoleTypePreferenceTypeConstraint> getRoleTypePreferenceTypeConstraints(
      String roleType) throws InvalidArgumentException, ServiceUnavailableException {
    try {
      if (StringUtils.hasText(roleType)) {
        return roleTypePreferenceTypeConstraintRepository.findByRoleTypeIgnoreCase(
            roleType, Sort.by(Direction.ASC, "roleType", "preferenceType"));
      } else {
        return roleTypePreferenceTypeConstraintRepository.findAll(
            Sort.by(Direction.ASC, "roleType", "preferenceType"));
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the role type preference type constraint", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypes.' + #localeId")
  public List<RoleType> getRoleTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getRoleTypes().stream()
        .filter(
            roleType ->
                (roleType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(roleType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'roleTypes.ALL'")
  public List<RoleType> getRoleTypes() throws ServiceUnavailableException {
    try {
      return roleTypeRepository.getAllRoleTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the role type reference data", e);
    }
  }

  @Override
  public List<RoleType> getRoleTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getRoleTypes(localeId).stream()
        .filter(
            roleType ->
                (roleType.getTenantId() == null
                    || (Objects.equals(roleType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public List<SegmentationType> getSegmentationTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getSegmentationTypes(localeId).stream()
        .filter(
            lockType ->
                (lockType.getTenantId() == null
                    || (Objects.equals(lockType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'segmentationTypes.' + #localeId")
  public List<SegmentationType> getSegmentationTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getSegmentationTypes().stream()
        .filter(
            segmentationType ->
                (segmentationType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(segmentationType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'segmentationTypes.ALL'")
  public List<SegmentationType> getSegmentationTypes()
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      return segmentationTypeRepository.getAllSegmentationTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the segmentation type reference data", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'segments.' + #localeId")
  public List<Segment> getSegments(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getSegments().stream()
        .filter(
            segment ->
                (segment.getLocaleId() == null || localeId.equalsIgnoreCase(segment.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'segments.ALL'")
  public List<Segment> getSegments() throws ServiceUnavailableException {
    try {
      return segmentRepository.getAllSegments();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the segment reference data", e);
    }
  }

  @Override
  public List<Segment> getSegments(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getSegments(localeId).stream()
        .filter(
            segment ->
                (segment.getTenantId() == null
                    || (Objects.equals(segment.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'skillTypes.' + #localeId")
  public List<SkillType> getSkillTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getSkillTypes().stream()
        .filter(
            skillType ->
                (skillType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(skillType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'skillTypes.ALL'")
  public List<SkillType> getSkillTypes() throws ServiceUnavailableException {
    try {
      return skillTypeRepository.getAllSkillTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the skill type reference data", e);
    }
  }

  @Override
  public List<SkillType> getSkillTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getSkillTypes(localeId).stream()
        .filter(
            skillType ->
                (skillType.getTenantId() == null
                    || (Objects.equals(skillType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfFundsTypes.' + #localeId")
  public List<SourceOfFundsType> getSourceOfFundsTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getSourceOfFundsTypes().stream()
        .filter(
            sourceOfFundsType ->
                (sourceOfFundsType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(sourceOfFundsType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfFundsTypes.ALL'")
  public List<SourceOfFundsType> getSourceOfFundsTypes() throws ServiceUnavailableException {
    try {
      return sourceOfFundsTypeRepository.getAllSourceOfFundsTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the source of funds type reference data", e);
    }
  }

  @Override
  public List<SourceOfFundsType> getSourceOfFundsTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getSourceOfFundsTypes(localeId).stream()
        .filter(
            sourceOfFundsType ->
                (sourceOfFundsType.getTenantId() == null
                    || (Objects.equals(sourceOfFundsType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfWealthTypes.' + #localeId")
  public List<SourceOfWealthType> getSourceOfWealthTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getSourceOfWealthTypes().stream()
        .filter(
            sourceOfWealthType ->
                (sourceOfWealthType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(sourceOfWealthType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'sourceOfWealthTypes.ALL'")
  public List<SourceOfWealthType> getSourceOfWealthTypes() throws ServiceUnavailableException {
    try {
      return sourceOfWealthTypeRepository.getAllSourceOfWealthTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the source of wealth type reference data", e);
    }
  }

  @Override
  public List<SourceOfWealthType> getSourceOfWealthTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getSourceOfWealthTypes(localeId).stream()
        .filter(
            sourceOfWealthType ->
                (sourceOfWealthType.getTenantId() == null
                    || (Objects.equals(sourceOfWealthType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypeCategories.' + #localeId")
  public List<StatusTypeCategory> getStatusTypeCategories(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getStatusTypeCategories().stream()
        .filter(
            statusTypeCategory ->
                (statusTypeCategory.getLocaleId() == null
                    || localeId.equalsIgnoreCase(statusTypeCategory.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypeCategories.ALL'")
  public List<StatusTypeCategory> getStatusTypeCategories() throws ServiceUnavailableException {
    try {
      return statusTypeCategoryRepository.getAllStatusTypeCategories();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the status type category reference data", e);
    }
  }

  @Override
  public List<StatusTypeCategory> getStatusTypeCategories(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getStatusTypeCategories(localeId).stream()
        .filter(
            statusTypeCategory ->
                (statusTypeCategory.getTenantId() == null
                    || (Objects.equals(statusTypeCategory.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypes.' + #localeId")
  public List<StatusType> getStatusTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getStatusTypes().stream()
        .filter(
            statusType ->
                (statusType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(statusType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'statusTypes.ALL'")
  public List<StatusType> getStatusTypes() throws ServiceUnavailableException {
    try {
      return statusTypeRepository.getAllStatusTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the status type reference data", e);
    }
  }

  @Override
  public List<StatusType> getStatusTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getStatusTypes(localeId).stream()
        .filter(
            statusType ->
                (statusType.getTenantId() == null
                    || (Objects.equals(statusType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'taxNumberTypes.' + #localeId")
  public List<TaxNumberType> getTaxNumberTypes(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getTaxNumberTypes().stream()
        .filter(
            taxNumberType ->
                (taxNumberType.getLocaleId() == null
                    || localeId.equalsIgnoreCase(taxNumberType.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'taxNumberTypes.ALL'")
  public List<TaxNumberType> getTaxNumberTypes() throws ServiceUnavailableException {
    try {
      return taxNumberTypeRepository.getAllTaxNumberTypes();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the tax number type reference data", e);
    }
  }

  @Override
  public List<TaxNumberType> getTaxNumberTypes(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getTaxNumberTypes(localeId).stream()
        .filter(
            taxNumberType ->
                (taxNumberType.getTenantId() == null
                    || (Objects.equals(taxNumberType.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'timesToContact.' + #localeId")
  public List<TimeToContact> getTimesToContact(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getTimesToContact().stream()
        .filter(
            timeToContact ->
                (timeToContact.getLocaleId() == null
                    || localeId.equalsIgnoreCase(timeToContact.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'timesToContact.ALL'")
  public List<TimeToContact> getTimesToContact() throws ServiceUnavailableException {
    try {
      return timeToContactRepository.getAllTimesToContact();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the times to contact reference data", e);
    }
  }

  @Override
  public List<TimeToContact> getTimesToContact(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getTimesToContact(localeId).stream()
        .filter(
            timeToContact ->
                (timeToContact.getTenantId() == null
                    || (Objects.equals(timeToContact.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'titles.' + #localeId")
  public List<Title> getTitles(String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(localeId)) {
      throw new InvalidArgumentException("localeId");
    }

    return getPartyReferenceService().getTitles().stream()
        .filter(
            title ->
                (title.getLocaleId() == null || localeId.equalsIgnoreCase(title.getLocaleId())))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(cacheNames = "reference", key = "'titles.ALL'")
  public List<Title> getTitles() throws ServiceUnavailableException {
    try {
      return titleRepository.getAllTitles();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the title reference data", e);
    }
  }

  @Override
  public List<Title> getTitles(UUID tenantId, String localeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    return getPartyReferenceService().getTitles(localeId).stream()
        .filter(
            title ->
                (title.getTenantId() == null || (Objects.equals(title.getTenantId(), tenantId))))
        .collect(Collectors.toList());
  }

  @Override
  public boolean isValidAssociationPropertyType(
      UUID tenantId, String associationTypeCode, String associationPropertyTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(associationPropertyTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getAssociationPropertyTypes().stream()
        .anyMatch(
            associationPropertyType ->
                (associationPropertyType.getTenantId() == null
                        || associationPropertyType.getTenantId().equals(tenantId))
                    && Objects.equals(
                        associationPropertyType.getAssociationType(), associationTypeCode)
                    && Objects.equals(
                        associationPropertyType.getCode(), associationPropertyTypeCode));
  }

  @Override
  public boolean isValidAssociationType(UUID tenantId, String associationTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(associationTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getAssociationTypes().stream()
        .anyMatch(
            associationType ->
                (associationType.getTenantId() == null
                        || Objects.equals(associationType.getTenantId(), tenantId))
                    && Objects.equals(associationType.getCode(), associationTypeCode));
  }

  @Override
  public boolean isValidAttributeType(UUID tenantId, String partyTypeCode, String attributeTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getAttributeTypes().stream()
        .anyMatch(
            attributeType ->
                (attributeType.getTenantId() == null
                        || attributeType.getTenantId().equals(tenantId))
                    && Objects.equals(attributeType.getCode(), attributeTypeCode)
                    && attributeType.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidAttributeTypeCategory(UUID tenantId, String attributeTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCategoryCode)) {
      return false;
    }

    return getPartyReferenceService().getAttributeTypeCategories().stream()
        .anyMatch(
            attributeTypeCategory ->
                (attributeTypeCategory.getTenantId() == null
                        || Objects.equals(attributeTypeCategory.getTenantId(), tenantId))
                    && Objects.equals(attributeTypeCategory.getCode(), attributeTypeCategoryCode));
  }

  @Override
  public boolean isValidConsentType(UUID tenantId, String consentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(consentTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getConsentTypes().stream()
        .anyMatch(
            consentType ->
                (consentType.getTenantId() == null
                        || Objects.equals(consentType.getTenantId(), tenantId))
                    && Objects.equals(consentType.getCode(), consentTypeCode));
  }

  @Override
  public boolean isValidContactMechanismPurpose(
      UUID tenantId,
      String partyTypeCode,
      String contactMechanismTypeCode,
      String contactMechanismPurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismPurposeCode)) {
      return false;
    }

    return getPartyReferenceService().getContactMechanismPurposes().stream()
        .anyMatch(
            contactMechanismPurpose ->
                (contactMechanismPurpose.getTenantId() == null
                        || Objects.equals(contactMechanismPurpose.getTenantId(), tenantId))
                    && contactMechanismPurpose.isValidForPartyType(partyTypeCode)
                    && contactMechanismPurpose.isValidForContactMechanismType(
                        contactMechanismTypeCode)
                    && Objects.equals(
                        contactMechanismPurpose.getCode(), contactMechanismPurposeCode));
  }

  @Override
  public boolean isValidContactMechanismRole(
      UUID tenantId,
      String partyTypeCode,
      String contactMechanismTypeCode,
      String contactMechanismRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismTypeCode)) {
      return false;
    }

    if (!StringUtils.hasText(contactMechanismRoleCode)) {
      return false;
    }

    return getPartyReferenceService().getContactMechanismRoles().stream()
        .anyMatch(
            contactMechanismRole ->
                (contactMechanismRole.getTenantId() == null
                        || Objects.equals(contactMechanismRole.getTenantId(), tenantId))
                    && contactMechanismRole.isValidForPartyType(partyTypeCode)
                    && contactMechanismRole
                        .getContactMechanismType()
                        .equals(contactMechanismTypeCode)
                    && Objects.equals(contactMechanismRole.getCode(), contactMechanismRoleCode));
  }

  @Override
  public boolean isValidContactMechanismType(UUID tenantId, String contactMechanismTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(contactMechanismTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getContactMechanismTypes().stream()
        .anyMatch(
            contactMechanismType ->
                (contactMechanismType.getTenantId() == null
                        || Objects.equals(contactMechanismType.getTenantId(), tenantId))
                    && Objects.equals(contactMechanismType.getCode(), contactMechanismTypeCode));
  }

  @Override
  public boolean isValidEmploymentStatus(UUID tenantId, String employmentStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentStatusCode)) {
      return false;
    }

    return getPartyReferenceService().getEmploymentStatuses().stream()
        .anyMatch(
            employmentStatus ->
                (employmentStatus.getTenantId() == null
                        || Objects.equals(employmentStatus.getTenantId(), tenantId))
                    && Objects.equals(employmentStatus.getCode(), employmentStatusCode));
  }

  @Override
  public boolean isValidEmploymentType(
      UUID tenantId, String employmentStatusCode, String employmentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getEmploymentTypes().stream()
        .anyMatch(
            employmentType ->
                (employmentType.getTenantId() == null
                        || Objects.equals(employmentType.getTenantId(), tenantId))
                    && Objects.equals(employmentType.getCode(), employmentTypeCode)
                    && Objects.equals(employmentType.getEmploymentStatus(), employmentStatusCode));
  }

  @Override
  public boolean isValidEmploymentType(UUID tenantId, String employmentTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(employmentTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getEmploymentTypes().stream()
        .anyMatch(
            employmentType ->
                (employmentType.getTenantId() == null
                        || Objects.equals(employmentType.getTenantId(), tenantId))
                    && Objects.equals(employmentType.getCode(), employmentTypeCode));
  }

  @Override
  public boolean isValidExternalReference(
      UUID tenantId, String partyTypeCode, String externalReferenceTypeCode, String value)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(externalReferenceTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getExternalReferenceTypes().stream()
        .anyMatch(
            externalReferenceType -> {
              if ((externalReferenceType.getTenantId() == null
                      || Objects.equals(externalReferenceType.getTenantId(), tenantId))
                  && Objects.equals(externalReferenceType.getCode(), externalReferenceTypeCode)
                  && externalReferenceType.isValidForPartyType(partyTypeCode)) {
                if (StringUtils.hasText(externalReferenceType.getPattern())) {
                  Pattern pattern = externalReferenceType.getCompiledPattern();

                  Matcher matcher = pattern.matcher(value);

                  return matcher.matches();
                } else {
                  return true;
                }
              } else {
                return false;
              }
            });
  }

  @Override
  public boolean isValidExternalReferenceType(
      UUID tenantId, String partyTypeCode, String externalReferenceTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(externalReferenceTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getExternalReferenceTypes().stream()
        .anyMatch(
            externalReferenceType ->
                (externalReferenceType.getTenantId() == null
                        || Objects.equals(externalReferenceType.getTenantId(), tenantId))
                    && Objects.equals(externalReferenceType.getCode(), externalReferenceTypeCode)
                    && externalReferenceType.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidFieldOfStudy(UUID tenantId, String fieldOfStudyCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(fieldOfStudyCode)) {
      return false;
    }

    return getPartyReferenceService().getFieldsOfStudy().stream()
        .anyMatch(
            fieldOfStudy ->
                (fieldOfStudy.getTenantId() == null
                        || Objects.equals(fieldOfStudy.getTenantId(), tenantId))
                    && Objects.equals(fieldOfStudy.getCode(), fieldOfStudyCode));
  }

  @Override
  public boolean isValidGender(UUID tenantId, String genderCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(genderCode)) {
      return false;
    }

    return getPartyReferenceService().getGenders().stream()
        .anyMatch(
            gender ->
                (gender.getTenantId() == null || Objects.equals(gender.getTenantId(), tenantId))
                    && Objects.equals(gender.getCode(), genderCode));
  }

  @Override
  public boolean isValidIdentification(
      UUID tenantId, String partyTypeCode, String identificationTypeCode, String number)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(identificationTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getIdentificationTypes().stream()
        .anyMatch(
            identificationType -> {
              if ((identificationType.getTenantId() == null
                      || Objects.equals(identificationType.getTenantId(), tenantId))
                  && Objects.equals(identificationType.getCode(), identificationTypeCode)
                  && identificationType.isValidForPartyType(partyTypeCode)) {
                if (StringUtils.hasText(identificationType.getPattern())) {
                  Pattern pattern = identificationType.getCompiledPattern();

                  Matcher matcher = pattern.matcher(number);

                  return matcher.matches();
                } else {
                  return true;
                }
              } else {
                return false;
              }
            });
  }

  @Override
  public boolean isValidIdentificationType(
      UUID tenantId, String partyTypeCode, String identificationTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(identificationTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getIdentificationTypes().stream()
        .anyMatch(
            identificationType ->
                (identificationType.getTenantId() == null
                        || Objects.equals(identificationType.getTenantId(), tenantId))
                    && Objects.equals(identificationType.getCode(), identificationTypeCode)
                    && identificationType.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidIndustryClassification(
      UUID tenantId, String industryClassificationSystemCode, String industryClassificationCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(industryClassificationSystemCode)) {
      return false;
    }

    if (!StringUtils.hasText(industryClassificationCode)) {
      return false;
    }

    return getPartyReferenceService().getIndustryClassifications().stream()
        .anyMatch(
            industryClassification ->
                (Objects.equals(
                        industryClassification.getSystem(), industryClassificationSystemCode)
                    && Objects.equals(
                        industryClassification.getCode(), industryClassificationCode)));
  }

  @Override
  public boolean isValidLockType(UUID tenantId, String partyTypeCode, String lockTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(lockTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getLockTypes().stream()
        .anyMatch(
            lockType ->
                (lockType.getTenantId() == null || Objects.equals(lockType.getTenantId(), tenantId))
                    && Objects.equals(lockType.getCode(), lockTypeCode)
                    && lockType.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidLockTypeCategory(UUID tenantId, String lockTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(lockTypeCategoryCode)) {
      return false;
    }

    return getPartyReferenceService().getLockTypeCategories().stream()
        .anyMatch(
            lockTypeCategory ->
                (lockTypeCategory.getTenantId() == null
                        || Objects.equals(lockTypeCategory.getTenantId(), tenantId))
                    && Objects.equals(lockTypeCategory.getCode(), lockTypeCategoryCode));
  }

  @Override
  public boolean isValidMandateType(UUID tenantId, String mandateTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(mandateTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getMandateTypes().stream()
        .anyMatch(
            mandateType ->
                (mandateType.getTenantId() == null
                        || Objects.equals(mandateType.getTenantId(), tenantId))
                    && Objects.equals(mandateType.getCode(), mandateTypeCode));
  }

  @Override
  public boolean isValidMaritalStatus(UUID tenantId, String maritalStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(maritalStatusCode)) {
      return false;
    }

    return getPartyReferenceService().getMaritalStatuses().stream()
        .anyMatch(
            maritalStatus ->
                (maritalStatus.getTenantId() == null
                        || Objects.equals(maritalStatus.getTenantId(), tenantId))
                    && Objects.equals(maritalStatus.getCode(), maritalStatusCode));
  }

  @Override
  public boolean isValidMarriageType(
      UUID tenantId, String maritalStatusCode, String marriageTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(maritalStatusCode)) {
      return true;
    }

    // Find marriage types for the specified marital status
    List<MarriageType> marriageTypes =
        getPartyReferenceService().getMarriageTypes().stream()
            .filter(
                marriageType ->
                    ((marriageType.getTenantId() == null)
                            && Objects.equals(marriageType.getMaritalStatus(), maritalStatusCode)
                        || (Objects.equals(marriageType.getTenantId(), tenantId))
                            && Objects.equals(marriageType.getMaritalStatus(), maritalStatusCode)))
            .toList();

    // If we have marriage types for the specified marital status then check if one matches
    if (!marriageTypes.isEmpty()) {
      return marriageTypes.stream()
          .anyMatch(
              marriageType ->
                  (marriageType.getTenantId() == null
                          || Objects.equals(marriageType.getTenantId(), tenantId))
                      && Objects.equals(marriageType.getCode(), marriageTypeCode)
                      && Objects.equals(marriageType.getMaritalStatus(), maritalStatusCode));
    } else {
      return true;
    }
  }

  public boolean isValidMeasurementUnitForAttributeType(
      UUID tenantId, String attributeTypeCode, MeasurementUnit measurementUnit)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(attributeTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getAttributeTypes().stream()
        .anyMatch(
            attributeType ->
                (attributeType.getTenantId() == null
                        || Objects.equals(attributeType.getTenantId(), tenantId))
                    && Objects.equals(attributeType.getCode(), attributeTypeCode)
                    && Objects.equals(
                        attributeType.getUnitType(),
                        measurementUnit == null ? null : measurementUnit.getType()));
  }

  @Override
  public boolean isValidNextOfKinType(UUID tenantId, String nextOfKinTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(nextOfKinTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getNextOfKinTypes().stream()
        .anyMatch(
            nextOfKinType ->
                (nextOfKinType.getTenantId() == null
                        || Objects.equals(nextOfKinType.getTenantId(), tenantId))
                    && Objects.equals(nextOfKinType.getCode(), nextOfKinTypeCode));
  }

  @Override
  public boolean isValidOccupation(UUID tenantId, String occupationCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(occupationCode)) {
      return false;
    }

    return getPartyReferenceService().getOccupations().stream()
        .anyMatch(
            occupation ->
                (occupation.getTenantId() == null
                        || Objects.equals(occupation.getTenantId(), tenantId))
                    && Objects.equals(occupation.getCode(), occupationCode));
  }

  @Override
  public boolean isValidPhysicalAddressPurpose(
      UUID tenantId, String partyTypeCode, String physicalAddressPurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressPurposeCode)) {
      return false;
    }

    return getPartyReferenceService().getPhysicalAddressPurposes().stream()
        .anyMatch(
            physicalAddressPurpose ->
                (physicalAddressPurpose.getTenantId() == null
                        || Objects.equals(physicalAddressPurpose.getTenantId(), tenantId))
                    && Objects.equals(physicalAddressPurpose.getCode(), physicalAddressPurposeCode)
                    && physicalAddressPurpose.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidPhysicalAddressPurpose(UUID tenantId, String physicalAddressPurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressPurposeCode)) {
      return false;
    }

    return getPartyReferenceService().getPhysicalAddressPurposes().stream()
        .anyMatch(
            physicalAddressPurpose ->
                (physicalAddressPurpose.getTenantId() == null
                        || Objects.equals(physicalAddressPurpose.getTenantId(), tenantId))
                    && Objects.equals(
                        physicalAddressPurpose.getCode(), physicalAddressPurposeCode));
  }

  @Override
  public boolean isValidPhysicalAddressRole(
      UUID tenantId, String partyTypeCode, String physicalAddressRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressRoleCode)) {
      return false;
    }

    return getPartyReferenceService().getPhysicalAddressRoles().stream()
        .anyMatch(
            physicalAddressRole ->
                (physicalAddressRole.getTenantId() == null
                        || Objects.equals(physicalAddressRole.getTenantId(), tenantId))
                    && Objects.equals(physicalAddressRole.getCode(), physicalAddressRoleCode)
                    && physicalAddressRole.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidPhysicalAddressRole(UUID tenantId, String physicalAddressRoleCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressRoleCode)) {
      return false;
    }

    return getPartyReferenceService().getPhysicalAddressRoles().stream()
        .anyMatch(
            physicalAddressRole ->
                (physicalAddressRole.getTenantId() == null
                        || Objects.equals(physicalAddressRole.getTenantId(), tenantId))
                    && Objects.equals(physicalAddressRole.getCode(), physicalAddressRoleCode));
  }

  @Override
  public boolean isValidPhysicalAddressType(UUID tenantId, String physicalAddressTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(physicalAddressTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getPhysicalAddressTypes().stream()
        .anyMatch(
            physicalAddressType ->
                (physicalAddressType.getTenantId() == null
                        || Objects.equals(physicalAddressType.getTenantId(), tenantId))
                    && Objects.equals(physicalAddressType.getCode(), physicalAddressTypeCode));
  }

  @Override
  public boolean isValidPreferenceType(
      UUID tenantId, String partyTypeCode, String preferenceTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(preferenceTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getPreferenceTypes().stream()
        .anyMatch(
            preferenceType ->
                (preferenceType.getTenantId() == null
                        || Objects.equals(preferenceType.getTenantId(), tenantId))
                    && Objects.equals(preferenceType.getCode(), preferenceTypeCode)
                    && preferenceType.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidPreferenceTypeCategory(UUID tenantId, String preferenceTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(preferenceTypeCategoryCode)) {
      return false;
    }

    return getPartyReferenceService().getPreferenceTypeCategories().stream()
        .anyMatch(
            preferenceTypeCategory ->
                (preferenceTypeCategory.getTenantId() == null
                        || Objects.equals(preferenceTypeCategory.getTenantId(), tenantId))
                    && Objects.equals(
                        preferenceTypeCategory.getCode(), preferenceTypeCategoryCode));
  }

  @Override
  public boolean isValidQualificationType(UUID tenantId, String qualificationTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(qualificationTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getQualificationTypes().stream()
        .anyMatch(
            qualificationType ->
                (qualificationType.getTenantId() == null
                        || Objects.equals(qualificationType.getTenantId(), tenantId))
                    && Objects.equals(qualificationType.getCode(), qualificationTypeCode));
  }

  @Override
  public boolean isValidRace(UUID tenantId, String raceCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(raceCode)) {
      return false;
    }

    return getPartyReferenceService().getRaces().stream()
        .anyMatch(
            race ->
                (race.getTenantId() == null || Objects.equals(race.getTenantId(), tenantId))
                    && Objects.equals(race.getCode(), raceCode));
  }

  @Override
  public boolean isValidResidencePermit(
      UUID tenantId, String partyTypeCode, String residencePermitTypeCode, String number)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residencePermitTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getResidencePermitTypes().stream()
        .anyMatch(
            residencePermitType -> {
              if ((residencePermitType.getTenantId() == null
                      || Objects.equals(residencePermitType.getTenantId(), tenantId))
                  && Objects.equals(residencePermitType.getCode(), residencePermitTypeCode)) {
                if (StringUtils.hasText(residencePermitType.getPattern())) {
                  Pattern pattern = residencePermitType.getCompiledPattern();

                  Matcher matcher = pattern.matcher(number);

                  return matcher.matches();
                } else {
                  return true;
                }
              } else {
                return false;
              }
            });
  }

  @Override
  public boolean isValidResidencePermitType(UUID tenantId, String residencePermitTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residencePermitTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getResidencePermitTypes().stream()
        .anyMatch(
            residencePermitType ->
                (residencePermitType.getTenantId() == null
                        || Objects.equals(residencePermitType.getTenantId(), tenantId))
                    && Objects.equals(residencePermitType.getCode(), residencePermitTypeCode));
  }

  @Override
  public boolean isValidResidencyStatus(UUID tenantId, String residencyStatusCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residencyStatusCode)) {
      return false;
    }

    return getPartyReferenceService().getResidencyStatuses().stream()
        .anyMatch(
            residencyStatus ->
                (residencyStatus.getTenantId() == null
                        || Objects.equals(residencyStatus.getTenantId(), tenantId))
                    && Objects.equals(residencyStatus.getCode(), residencyStatusCode));
  }

  @Override
  public boolean isValidResidentialType(UUID tenantId, String residentialTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(residentialTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getResidentialTypes().stream()
        .anyMatch(
            residentialType ->
                (residentialType.getTenantId() == null
                        || Objects.equals(residentialType.getTenantId(), tenantId))
                    && Objects.equals(residentialType.getCode(), residentialTypeCode));
  }

  @Override
  public boolean isValidRolePurpose(UUID tenantId, String rolePurposeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(rolePurposeCode)) {
      return false;
    }

    return getPartyReferenceService().getRolePurposes().stream()
        .anyMatch(
            rolePurpose ->
                (rolePurpose.getTenantId() == null
                        || (Objects.equals(rolePurpose.getTenantId(), tenantId)))
                    && Objects.equals(rolePurpose.getCode(), rolePurposeCode));
  }

  @Override
  public boolean isValidRoleType(UUID tenantId, String partyTypeCode, String roleTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(roleTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getRoleTypes().stream()
        .anyMatch(
            roleType ->
                (roleType.getTenantId() == null || Objects.equals(roleType.getTenantId(), tenantId))
                    && Objects.equals(roleType.getCode(), roleTypeCode)
                    && roleType.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidSegment(UUID tenantId, String segmentCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(segmentCode)) {
      return false;
    }

    return getPartyReferenceService().getSegments().stream()
        .anyMatch(
            segment ->
                (segment.getTenantId() == null || Objects.equals(segment.getTenantId(), tenantId))
                    && Objects.equals(segment.getCode(), segmentCode));
  }

  @Override
  public boolean isValidSkillType(UUID tenantId, String skillTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(skillTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getSkillTypes().stream()
        .anyMatch(
            skillType ->
                (skillType.getTenantId() == null
                        || Objects.equals(skillType.getTenantId(), tenantId))
                    && Objects.equals(skillType.getCode(), skillTypeCode));
  }

  @Override
  public boolean isValidSourceOfFundsType(UUID tenantId, String sourceOfFundsTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(sourceOfFundsTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getSourceOfFundsTypes().stream()
        .anyMatch(
            sourceOfFunds ->
                (sourceOfFunds.getTenantId() == null
                        || Objects.equals(sourceOfFunds.getTenantId(), tenantId))
                    && Objects.equals(sourceOfFunds.getCode(), sourceOfFundsTypeCode));
  }

  @Override
  public boolean isValidSourceOfWealthType(UUID tenantId, String sourceOfWealthTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(sourceOfWealthTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getSourceOfWealthTypes().stream()
        .anyMatch(
            sourceOfWealth ->
                (sourceOfWealth.getTenantId() == null
                        || Objects.equals(sourceOfWealth.getTenantId(), tenantId))
                    && Objects.equals(sourceOfWealth.getCode(), sourceOfWealthTypeCode));
  }

  @Override
  public boolean isValidStatusType(UUID tenantId, String partyTypeCode, String statusTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(statusTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getStatusTypes().stream()
        .anyMatch(
            statusType ->
                (statusType.getTenantId() == null
                        || Objects.equals(statusType.getTenantId(), tenantId))
                    && Objects.equals(statusType.getCode(), statusTypeCode)
                    && statusType.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidStatusTypeCategory(UUID tenantId, String statusTypeCategoryCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(statusTypeCategoryCode)) {
      return false;
    }

    return getPartyReferenceService().getStatusTypeCategories().stream()
        .anyMatch(
            statusTypeCategory ->
                (statusTypeCategory.getTenantId() == null
                        || Objects.equals(statusTypeCategory.getTenantId(), tenantId))
                    && Objects.equals(statusTypeCategory.getCode(), statusTypeCategoryCode));
  }

  @Override
  public boolean isValidTaxNumber(
      UUID tenantId, String partyTypeCode, String taxNumberTypeCode, String number)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(taxNumberTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getTaxNumberTypes().stream()
        .anyMatch(
            taxNumberType -> {
              if ((taxNumberType.getTenantId() == null
                      || Objects.equals(taxNumberType.getTenantId(), tenantId))
                  && Objects.equals(taxNumberType.getCode(), taxNumberTypeCode)
                  && taxNumberType.isValidForPartyType(partyTypeCode)) {
                if (StringUtils.hasText(taxNumberType.getPattern())) {
                  Pattern pattern = taxNumberType.getCompiledPattern();

                  Matcher matcher = pattern.matcher(number);

                  return matcher.matches();
                } else {
                  return true;
                }
              } else {
                return false;
              }
            });
  }

  @Override
  public boolean isValidTaxNumberType(UUID tenantId, String partyTypeCode, String taxNumberTypeCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(taxNumberTypeCode)) {
      return false;
    }

    return getPartyReferenceService().getTaxNumberTypes().stream()
        .anyMatch(
            taxNumberType ->
                (taxNumberType.getTenantId() == null
                        || Objects.equals(taxNumberType.getTenantId(), tenantId))
                    && Objects.equals(taxNumberType.getCode(), taxNumberTypeCode)
                    && taxNumberType.isValidForPartyType(partyTypeCode));
  }

  @Override
  public boolean isValidTimeToContact(UUID tenantId, String timeToContactCode)
      throws ServiceUnavailableException {
    if (!StringUtils.hasText(timeToContactCode)) {
      return false;
    }

    return getPartyReferenceService().getTimesToContact().stream()
        .anyMatch(
            timeToContact ->
                (timeToContact.getTenantId() == null
                        || Objects.equals(timeToContact.getTenantId(), tenantId))
                    && Objects.equals(timeToContact.getCode(), timeToContactCode));
  }

  @Override
  public boolean isValidTitle(UUID tenantId, String titleCode) throws ServiceUnavailableException {
    if (!StringUtils.hasText(titleCode)) {
      return false;
    }

    return getPartyReferenceService().getTitles().stream()
        .anyMatch(
            title ->
                (title.getTenantId() == null || Objects.equals(title.getTenantId(), tenantId))
                    && Objects.equals(title.getCode(), titleCode));
  }

  /**
   * Returns the internal reference to the Party Reference Service to enable caching.
   *
   * @return the internal reference to the Party Reference Service to enable caching.
   */
  private PartyReferenceService getPartyReferenceService() {
    if (partyReferenceService == null) {
      partyReferenceService = getApplicationContext().getBean(PartyReferenceService.class);
    }

    return partyReferenceService;
  }
}
